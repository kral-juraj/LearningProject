package com.beekeeper.desktop.controller;

import com.beekeeper.desktop.dao.jdbc.JdbcInspectionDao;
import com.beekeeper.desktop.scheduler.DesktopSchedulerProvider;
import com.beekeeper.shared.entity.Inspection;
import com.beekeeper.shared.i18n.TranslationManager;
import com.beekeeper.shared.repository.InspectionRepository;
import com.beekeeper.shared.util.DateUtils;
import com.beekeeper.shared.viewmodel.InspectionViewModel;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controller for Inspection list view.
 * Displays inspections for a selected hive.
 */
public class InspectionListController {

    @FXML
    private TableView<Inspection> inspectionTable;

    @FXML
    private TableColumn<Inspection, Long> dateColumn;

    @FXML
    private TableColumn<Inspection, Double> temperatureColumn;

    @FXML
    private TableColumn<Inspection, Integer> strengthColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button viewButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Label statusLabel;

    private InspectionViewModel viewModel;
    private CompositeDisposable disposables = new CompositeDisposable();
    private ObservableList<Inspection> inspectionList = FXCollections.observableArrayList();
    private String currentHiveId;
    private TranslationManager tm;

    @FXML
    public void initialize() {
        // Get TranslationManager instance
        tm = TranslationManager.getInstance();

        // Initialize ViewModel
        JdbcInspectionDao inspectionDao = new JdbcInspectionDao();
        InspectionRepository repository = new InspectionRepository(inspectionDao);
        DesktopSchedulerProvider schedulerProvider = new DesktopSchedulerProvider();
        viewModel = new InspectionViewModel(repository, schedulerProvider);

        // Configure table columns
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("inspectionDate"));
        dateColumn.setCellFactory(column -> new TableCell<Inspection, Long>() {
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

        temperatureColumn.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        strengthColumn.setCellValueFactory(new PropertyValueFactory<>("strengthEstimate"));

        // Bind table to observable list
        inspectionTable.setItems(inspectionList);

        // Enable/disable buttons based on selection
        viewButton.setDisable(true);
        deleteButton.setDisable(true);

        // Defer setup to avoid macOS NSTrackingRectTag bug
        Platform.runLater(() -> {
            inspectionTable.getSelectionModel().selectedItemProperty().addListener(
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

    public void setHiveId(String hiveId) {
        this.currentHiveId = hiveId;
        if (viewModel != null) {
            viewModel.loadInspectionsByHiveId(hiveId);
        }
    }

    private void subscribeToViewModel() {
        disposables.add(
            viewModel.getInspections()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(
                    inspections -> {
                        inspectionList.clear();
                        inspectionList.addAll(inspections);
                        statusLabel.setText(tm.get("status.inspections_count", inspections.size()));
                    },
                    error -> showError(tm.get("error.loading_inspections", error.getMessage()))
                )
        );

        disposables.add(
            viewModel.getLoading()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(loading -> {
                    addButton.setDisable(loading);
                    inspectionTable.setDisable(loading);
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
    private void handleAddInspection() {
        if (currentHiveId == null) {
            showError(tm.get("error.select_hive_first"));
            return;
        }

        com.beekeeper.desktop.dialog.InspectionDialog dialog =
            new com.beekeeper.desktop.dialog.InspectionDialog(null, currentHiveId);
        java.util.Optional<Inspection> result = dialog.showAndWait();

        result.ifPresent(inspection -> viewModel.createInspection(inspection));
    }

    @FXML
    private void handleViewInspection() {
        Inspection selected = inspectionTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        com.beekeeper.desktop.dialog.InspectionDialog dialog =
            new com.beekeeper.desktop.dialog.InspectionDialog(selected, currentHiveId);
        java.util.Optional<Inspection> result = dialog.showAndWait();

        result.ifPresent(inspection -> viewModel.updateInspection(inspection));
    }

    @FXML
    private void handleDeleteInspection() {
        Inspection selected = inspectionTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(tm.get("dialog.delete_inspection.title"));
        alert.setHeaderText(tm.get("dialog.delete_inspection.header", DateUtils.formatDate(selected.getInspectionDate())));
        alert.setContentText(tm.get("dialog.delete_inspection.content"));

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                viewModel.deleteInspection(selected);
            }
        });
    }

    @FXML
    private void handleRefresh() {
        if (currentHiveId != null) {
            viewModel.loadInspectionsByHiveId(currentHiveId);
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
