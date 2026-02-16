package com.beekeeper.desktop.controller;

import com.beekeeper.desktop.dao.jdbc.JdbcHiveDao;
import com.beekeeper.desktop.dao.jdbc.JdbcTaxationDao;
import com.beekeeper.desktop.dao.jdbc.JdbcTaxationFrameDao;
import com.beekeeper.desktop.dialog.TaxationDialog;
import com.beekeeper.desktop.dialog.TaxationWithFrames;
import com.beekeeper.desktop.scheduler.DesktopSchedulerProvider;
import com.beekeeper.shared.entity.Hive;
import com.beekeeper.shared.entity.Taxation;
import com.beekeeper.shared.entity.TaxationFrame;
import com.beekeeper.shared.i18n.TranslationManager;
import com.beekeeper.shared.repository.TaxationRepository;
import com.beekeeper.shared.util.DateUtils;
import com.beekeeper.shared.viewmodel.TaxationViewModel;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Optional;

/**
 * Controller for Taxation list view.
 * Displays taxations for a selected hive.
 */
public class TaxationListController {

    @FXML
    private TableView<Taxation> taxationTable;

    @FXML
    private TableColumn<Taxation, Long> dateColumn;

    @FXML
    private TableColumn<Taxation, String> hiveNameColumn;

    @FXML
    private TableColumn<Taxation, Double> temperatureColumn;

    @FXML
    private TableColumn<Taxation, Integer> totalFramesColumn;

    @FXML
    private TableColumn<Taxation, Double> foodStoresColumn;

    @FXML
    private TableColumn<Taxation, Integer> totalPollenColumn;

    @FXML
    private TableColumn<Taxation, Integer> totalCappedStoresColumn;

    @FXML
    private TableColumn<Taxation, Integer> totalUncappedStoresColumn;

    @FXML
    private TableColumn<Taxation, Integer> totalCappedBroodColumn;

    @FXML
    private TableColumn<Taxation, Integer> totalUncappedBroodColumn;

    @FXML
    private TableColumn<Taxation, Integer> totalStarterFramesColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button viewButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Label statusLabel;

    private TaxationViewModel viewModel;
    private CompositeDisposable disposables = new CompositeDisposable();
    private ObservableList<Taxation> taxationList = FXCollections.observableArrayList();
    private String currentApiaryId;
    private TranslationManager tm;
    private String currentHiveId; // Pre vytvorenie novej taxácie potrebujeme hiveId
    private JdbcTaxationFrameDao frameDao;

    @FXML
    public void initialize() {
        // Get TranslationManager instance
        tm = TranslationManager.getInstance();

        // Initialize ViewModel
        JdbcTaxationDao taxationDao = new JdbcTaxationDao();
        frameDao = new JdbcTaxationFrameDao();
        TaxationRepository repository = new TaxationRepository(taxationDao, frameDao);
        DesktopSchedulerProvider schedulerProvider = new DesktopSchedulerProvider();
        viewModel = new TaxationViewModel(repository, schedulerProvider);

        // Configure table columns
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("taxationDate"));
        dateColumn.setCellFactory(column -> new TableCell<Taxation, Long>() {
            @Override
            protected void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(DateUtils.formatDate(item));
                }
            }
        });

        hiveNameColumn.setCellValueFactory(new PropertyValueFactory<>("hiveName"));
        temperatureColumn.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        totalFramesColumn.setCellValueFactory(new PropertyValueFactory<>("totalFrames"));
        foodStoresColumn.setCellValueFactory(new PropertyValueFactory<>("foodStoresKg"));
        totalPollenColumn.setCellValueFactory(new PropertyValueFactory<>("totalPollenDm"));
        totalCappedStoresColumn.setCellValueFactory(new PropertyValueFactory<>("totalCappedStoresDm"));
        totalUncappedStoresColumn.setCellValueFactory(new PropertyValueFactory<>("totalUncappedStoresDm"));
        totalCappedBroodColumn.setCellValueFactory(new PropertyValueFactory<>("totalCappedBroodDm"));
        totalUncappedBroodColumn.setCellValueFactory(new PropertyValueFactory<>("totalUncappedBroodDm"));
        totalStarterFramesColumn.setCellValueFactory(new PropertyValueFactory<>("totalStarterFrames"));

        // Bind table to observable list
        taxationTable.setItems(taxationList);

        // Enable/disable buttons based on selection
        viewButton.setDisable(true);
        deleteButton.setDisable(true);

        // Defer setup to avoid macOS NSTrackingRectTag bug
        Platform.runLater(() -> {
            taxationTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    boolean hasSelection = newSelection != null;
                    viewButton.setDisable(!hasSelection);
                    deleteButton.setDisable(!hasSelection);
                }
            );

            // Subscribe to ViewModel state
            subscribeToViewModel();
        });
    }

    public void setApiaryId(String apiaryId) {
        this.currentApiaryId = apiaryId;
        if (viewModel != null) {
            viewModel.loadTaxationsByApiaryId(apiaryId);
        }
    }

    /**
     * Set the current hive for creating new taxations.
     * This is called when user selects a hive in dialog.
     */
    public void setCurrentHiveId(String hiveId) {
        this.currentHiveId = hiveId;
    }

    private void subscribeToViewModel() {
        disposables.add(
            viewModel.getTaxations()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(
                    taxations -> {
                        taxationList.clear();
                        taxationList.addAll(taxations);
                        statusLabel.setText(tm.get("status.taxations_count", taxations.size()));
                        statusLabel.setStyle("-fx-text-fill: black;");
                    },
                    error -> showError(tm.get("error.loading_taxations", error.getMessage()))
                )
        );

        disposables.add(
            viewModel.getLoading()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(loading -> {
                    addButton.setDisable(loading);
                    taxationTable.setDisable(loading);
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
    private void handleAddTaxation() {
        if (currentApiaryId == null) {
            showError(tm.get("error.select_apiary_first"));
            return;
        }

        // Load hives from current apiary
        JdbcHiveDao hiveDao = new JdbcHiveDao();
        try {
            List<Hive> hives = hiveDao.getByApiaryId(currentApiaryId).blockingFirst();

            if (hives.isEmpty()) {
                showError(tm.get("error.no_hives_in_apiary"));
                return;
            }

            // Create simple string choice dialog
            List<String> hiveNames = new java.util.ArrayList<>();
            for (Hive hive : hives) {
                hiveNames.add(hive.getName());
            }

            ChoiceDialog<String> hiveDialog = new ChoiceDialog<>(hiveNames.get(0), hiveNames);
            hiveDialog.setTitle(tm.get("dialog.select_hive.title"));
            hiveDialog.setHeaderText(tm.get("dialog.select_hive.header"));
            hiveDialog.setContentText(tm.get("dialog.select_hive.content"));

            Optional<String> selectedHiveName = hiveDialog.showAndWait();

            if (selectedHiveName.isPresent()) {
                // Find hive by name
                Hive selectedHive = null;
                for (Hive hive : hives) {
                    if (hive.getName().equals(selectedHiveName.get())) {
                        selectedHive = hive;
                        break;
                    }
                }

                if (selectedHive != null) {
                    String hiveId = selectedHive.getId();
                    TaxationDialog dialog = new TaxationDialog(null, null, hiveId);
                    Optional<TaxationWithFrames> result = dialog.showAndWait();

                    result.ifPresent(data -> viewModel.createTaxationWithFrames(data.taxation, data.frames));
                }
            }

        } catch (Exception e) {
            showError(tm.get("error.loading_hives", e.getMessage()));
        }
    }

    @FXML
    private void handleViewTaxation() {
        Taxation selected = taxationTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        // Load frames for this taxation
        try {
            List<TaxationFrame> frames = frameDao.getByTaxationId(selected.getId()).blockingFirst();

            // Use selected taxation's hiveId, not currentHiveId (which may be null in apiary view)
            TaxationDialog dialog = new TaxationDialog(selected, frames, selected.getHiveId());
            Optional<TaxationWithFrames> result = dialog.showAndWait();

            result.ifPresent(data -> {
                // Update existing taxation, not create new one
                viewModel.updateTaxationWithFrames(data.taxation, data.frames);
            });
        } catch (Exception e) {
            showError(tm.get("error.loading_frames", e.getMessage()));
        }
    }

    @FXML
    private void handleDeleteTaxation() {
        Taxation selected = taxationTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(tm.get("dialog.delete_taxation.title"));
        alert.setHeaderText(tm.get("dialog.delete_taxation.header", DateUtils.formatDate(selected.getTaxationDate())));
        alert.setContentText(tm.get("dialog.delete_taxation.content_with_frames"));

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                viewModel.deleteTaxation(selected);
            }
        });
    }

    @FXML
    private void handleRefresh() {
        if (currentApiaryId != null) {
            viewModel.loadTaxationsByApiaryId(currentApiaryId);
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
}
