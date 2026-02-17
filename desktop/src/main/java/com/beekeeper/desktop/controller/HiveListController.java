package com.beekeeper.desktop.controller;

import com.beekeeper.desktop.component.HiveCard;
import com.beekeeper.desktop.dao.jdbc.JdbcHiveDao;
import com.beekeeper.desktop.dialog.HiveActivityHistoryDialog;
import com.beekeeper.desktop.dialog.HiveDialog;
import com.beekeeper.desktop.scheduler.DesktopSchedulerProvider;
import com.beekeeper.shared.entity.Hive;
import com.beekeeper.shared.i18n.TranslationManager;
import com.beekeeper.shared.repository.HiveRepository;
import com.beekeeper.shared.viewmodel.HiveViewModel;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;

import java.util.function.Consumer;

/**
 * Controller for Hive list view.
 * Displays hives for a selected apiary in a compact grid layout with cards.
 */
public class HiveListController {

    @FXML
    private FlowPane hiveGridPane;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button toggleActiveButton;

    @FXML
    private Button showInspectionsButton;

    @FXML
    private Label statusLabel;

    private HiveViewModel viewModel;
    private CompositeDisposable disposables = new CompositeDisposable();
    private String currentApiaryId;
    private Consumer<Hive> onHiveSelected;
    private TranslationManager tm;
    private Hive selectedHive = null;

    @FXML
    public void initialize() {
        // Get TranslationManager instance
        tm = TranslationManager.getInstance();

        // Initialize ViewModel
        JdbcHiveDao hiveDao = new JdbcHiveDao();
        HiveRepository repository = new HiveRepository(hiveDao);
        DesktopSchedulerProvider schedulerProvider = new DesktopSchedulerProvider();
        viewModel = new HiveViewModel(repository, schedulerProvider);

        // Disable toolbar buttons until a hive is selected
        editButton.setDisable(true);
        deleteButton.setDisable(true);
        toggleActiveButton.setDisable(true);
        showInspectionsButton.setDisable(true);

        // Defer setup to avoid macOS NSTrackingRectTag bug
        Platform.runLater(() -> {
            // Subscribe to ViewModel state
            subscribeToViewModel();
        });
    }

    public void setApiaryId(String apiaryId) {
        this.currentApiaryId = apiaryId;
        if (viewModel != null) {
            viewModel.loadHivesByApiaryId(apiaryId);
        }
    }

    private void subscribeToViewModel() {
        disposables.add(
            viewModel.getHives()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(
                    hives -> {
                        updateHiveGrid(hives);
                        statusLabel.setText(tm.get("status.hives_count", hives.size()));
                    },
                    error -> showError(tm.get("error.loading_hives", error.getMessage()))
                )
        );

        disposables.add(
            viewModel.getLoading()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(loading -> {
                    addButton.setDisable(loading);
                    hiveGridPane.setDisable(loading);
                })
        );

        disposables.add(
            viewModel.getError()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(this::showError)
        );

        disposables.add(
            viewModel.getSuccess()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(this::showSuccess)
        );
    }

    /**
     * Update the hive grid with HiveCard components.
     *
     * @param hives List of hives to display
     */
    private void updateHiveGrid(java.util.List<Hive> hives) {
        hiveGridPane.getChildren().clear();

        for (int i = 0; i < hives.size(); i++) {
            Hive hive = hives.get(i);
            HiveCard card = new HiveCard();
            card.setHive(hive);

            // Set action callbacks
            card.setOnEdit(this::handleEditHiveFromCard);
            card.setOnHistory(this::handleViewHistory);
            card.setOnInspect(this::handleShowInspectionsFromCard);

            // Click on card selects it
            card.setOnMouseClicked(e -> {
                if (e.getClickCount() == 1) {
                    selectHive(hive);
                }
            });

            // Enable drag-and-drop for reordering
            setupDragAndDrop(card, hive, hives);

            hiveGridPane.getChildren().add(card);
        }
    }

    /**
     * Setup drag-and-drop handlers for a hive card.
     *
     * @param card HiveCard component
     * @param hive Hive entity
     * @param allHives List of all hives (for reordering)
     */
    private void setupDragAndDrop(HiveCard card, Hive hive, java.util.List<Hive> allHives) {
        // Drag detected - start drag operation
        card.setOnDragDetected(event -> {
            javafx.scene.input.Dragboard db = card.startDragAndDrop(javafx.scene.input.TransferMode.MOVE);
            javafx.scene.input.ClipboardContent content = new javafx.scene.input.ClipboardContent();
            content.putString(hive.getId());
            db.setContent(content);
            card.setOpacity(0.5);
            event.consume();
        });

        // Drag over - accept if dragging another hive card
        card.setOnDragOver(event -> {
            if (event.getGestureSource() != card && event.getDragboard().hasString()) {
                event.acceptTransferModes(javafx.scene.input.TransferMode.MOVE);
            }
            event.consume();
        });

        // Drag entered - visual feedback
        card.setOnDragEntered(event -> {
            if (event.getGestureSource() != card && event.getDragboard().hasString()) {
                card.setStyle("-fx-border-color: #2196F3; -fx-border-width: 2px;");
            }
            event.consume();
        });

        // Drag exited - remove visual feedback
        card.setOnDragExited(event -> {
            card.setStyle("");
            event.consume();
        });

        // Drag dropped - reorder hives
        card.setOnDragDropped(event -> {
            javafx.scene.input.Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                String draggedHiveId = db.getString();
                Hive draggedHive = null;
                int draggedIndex = -1;
                int targetIndex = -1;

                // Find dragged hive and target hive indices
                for (int i = 0; i < allHives.size(); i++) {
                    if (allHives.get(i).getId().equals(draggedHiveId)) {
                        draggedHive = allHives.get(i);
                        draggedIndex = i;
                    }
                    if (allHives.get(i).getId().equals(hive.getId())) {
                        targetIndex = i;
                    }
                }

                if (draggedHive != null && draggedIndex != -1 && targetIndex != -1 && draggedIndex != targetIndex) {
                    // Reorder the list
                    allHives.remove(draggedIndex);
                    allHives.add(targetIndex, draggedHive);

                    // Update displayOrder for all hives
                    for (int i = 0; i < allHives.size(); i++) {
                        allHives.get(i).setDisplayOrder(i);
                    }

                    // Save new order to database
                    viewModel.updateHiveOrder(new java.util.ArrayList<>(allHives));

                    // Refresh UI
                    updateHiveGrid(allHives);
                    success = true;
                }
            }

            event.setDropCompleted(success);
            event.consume();
        });

        // Drag done - reset opacity
        card.setOnDragDone(event -> {
            card.setOpacity(1.0);
            card.setStyle("");
            event.consume();
        });
    }

    /**
     * Select a hive and enable toolbar buttons.
     */
    private void selectHive(Hive hive) {
        this.selectedHive = hive;
        editButton.setDisable(false);
        deleteButton.setDisable(false);
        toggleActiveButton.setDisable(false);
        showInspectionsButton.setDisable(false);
    }

    @FXML
    private void handleAddHive() {
        if (currentApiaryId == null) {
            showError(tm.get("error.select_apiary_first"));
            return;
        }

        HiveDialog dialog = new HiveDialog(null);
        dialog.showAndWait().ifPresent(hive -> {
            hive.setApiaryId(currentApiaryId);
            viewModel.createHive(hive);
        });
    }

    @FXML
    private void handleEditHive() {
        if (selectedHive == null) return;

        HiveDialog dialog = new HiveDialog(selectedHive);
        dialog.showAndWait().ifPresent(hive -> {
            viewModel.updateHive(hive);
        });
    }

    /**
     * Handle edit from card button.
     */
    private void handleEditHiveFromCard(Hive hive) {
        selectHive(hive);
        handleEditHive();
    }

    @FXML
    private void handleDeleteHive() {
        if (selectedHive == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(tm.get("dialog.delete_hive.title"));
        alert.setHeaderText(tm.get("dialog.delete_hive.header", selectedHive.getName()));
        alert.setContentText(tm.get("dialog.delete_hive.content"));

        // Use translated button labels
        ButtonType deleteButtonType = new ButtonType(tm.get("button.delete"), ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType(tm.get("button.cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(deleteButtonType, cancelButtonType);

        alert.showAndWait().ifPresent(response -> {
            if (response == deleteButtonType) {
                viewModel.deleteHive(selectedHive);
                selectedHive = null;
                editButton.setDisable(true);
                deleteButton.setDisable(true);
                toggleActiveButton.setDisable(true);
                showInspectionsButton.setDisable(true);
            }
        });
    }

    @FXML
    private void handleToggleActive() {
        if (selectedHive == null) return;

        viewModel.toggleHiveActive(selectedHive);
    }

    @FXML
    private void handleRefresh() {
        if (currentApiaryId != null) {
            viewModel.loadHivesByApiaryId(currentApiaryId);
        }
    }

    @FXML
    private void handleShowInspections() {
        if (selectedHive != null && onHiveSelected != null) {
            onHiveSelected.accept(selectedHive);
        }
    }

    /**
     * Handle show inspections from card button.
     */
    private void handleShowInspectionsFromCard(Hive hive) {
        selectHive(hive);
        handleShowInspections();
    }

    /**
     * Handle view history from card button.
     */
    private void handleViewHistory(Hive hive) {
        selectHive(hive);
        HiveActivityHistoryDialog dialog = new HiveActivityHistoryDialog(hive);
        dialog.showAndWait();
    }

    private void showError(String message) {
        statusLabel.setText(tm.get("error.prefix") + " " + message);
        statusLabel.setStyle("-fx-text-fill: red;");
    }

    private void showSuccess(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: green;");
    }

    public void cleanup() {
        disposables.clear();
        if (viewModel != null) {
            viewModel.dispose();
        }
    }

    /**
     * Set callback for when "Zobraziť prehliadky" button is clicked.
     * @param callback Consumer that receives the selected Hive
     */
    public void setOnHiveSelected(Consumer<Hive> callback) {
        this.onHiveSelected = callback;
    }
}
