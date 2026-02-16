package com.beekeeper.desktop.controller;

import com.beekeeper.desktop.dao.jdbc.JdbcHiveDao;
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
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.function.Consumer;

/**
 * Controller for Hive list view.
 * Displays hives for a selected apiary.
 */
public class HiveListController {

    @FXML
    private TableView<Hive> hiveTable;

    @FXML
    private TableColumn<Hive, String> nameColumn;

    @FXML
    private TableColumn<Hive, String> typeColumn;

    @FXML
    private TableColumn<Hive, Boolean> activeColumn;

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
    private ObservableList<Hive> hiveList = FXCollections.observableArrayList();
    private String currentApiaryId;
    private Consumer<Hive> onHiveSelected;
    private TranslationManager tm;

    @FXML
    public void initialize() {
        // Get TranslationManager instance
        tm = TranslationManager.getInstance();

        // Initialize ViewModel
        JdbcHiveDao hiveDao = new JdbcHiveDao();
        HiveRepository repository = new HiveRepository(hiveDao);
        DesktopSchedulerProvider schedulerProvider = new DesktopSchedulerProvider();
        viewModel = new HiveViewModel(repository, schedulerProvider);

        // Configure table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        activeColumn.setCellValueFactory(new PropertyValueFactory<>("active"));

        // Bind table to observable list
        hiveTable.setItems(hiveList);

        // Enable/disable buttons based on selection
        editButton.setDisable(true);
        deleteButton.setDisable(true);
        toggleActiveButton.setDisable(true);
        showInspectionsButton.setDisable(true);

        // Defer setup to avoid macOS NSTrackingRectTag bug
        Platform.runLater(() -> {
            hiveTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    boolean hasSelection = newSelection != null;
                    editButton.setDisable(!hasSelection);
                    deleteButton.setDisable(!hasSelection);
                    toggleActiveButton.setDisable(!hasSelection);
                    showInspectionsButton.setDisable(!hasSelection);
                }
            );

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
                        hiveList.clear();
                        hiveList.addAll(hives);
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
                    hiveTable.setDisable(loading);
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

    @FXML
    private void handleAddHive() {
        if (currentApiaryId == null) {
            showError(tm.get("error.select_apiary_first"));
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(tm.get("dialog.add_hive.title"));
        dialog.setHeaderText(tm.get("dialog.add_hive.header"));
        dialog.setContentText(tm.get("dialog.add_hive.content"));

        dialog.showAndWait().ifPresent(name -> {
            if (!name.trim().isEmpty()) {
                viewModel.createHive(currentApiaryId, name, "VERTICAL", "", 0);
            }
        });
    }

    @FXML
    private void handleEditHive() {
        Hive selected = hiveTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        TextInputDialog dialog = new TextInputDialog(selected.getName());
        dialog.setTitle(tm.get("dialog.edit_hive.title"));
        dialog.setHeaderText(tm.get("dialog.edit_hive.header"));
        dialog.setContentText(tm.get("dialog.edit_hive.content"));

        dialog.showAndWait().ifPresent(name -> {
            if (!name.trim().isEmpty()) {
                selected.setName(name);
                viewModel.updateHive(selected);
            }
        });
    }

    @FXML
    private void handleDeleteHive() {
        Hive selected = hiveTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(tm.get("dialog.delete_hive.title"));
        alert.setHeaderText(tm.get("dialog.delete_hive.header", selected.getName()));
        alert.setContentText(tm.get("dialog.delete_hive.content"));

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                viewModel.deleteHive(selected);
            }
        });
    }

    @FXML
    private void handleToggleActive() {
        Hive selected = hiveTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        viewModel.toggleHiveActive(selected);
    }

    @FXML
    private void handleRefresh() {
        if (currentApiaryId != null) {
            viewModel.loadHivesByApiaryId(currentApiaryId);
        }
    }

    @FXML
    private void handleShowInspections() {
        Hive selected = hiveTable.getSelectionModel().getSelectedItem();
        if (selected != null && onHiveSelected != null) {
            onHiveSelected.accept(selected);
        }
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
