package com.beekeeper.desktop.controller;

import com.beekeeper.desktop.dao.jdbc.JdbcHiveDao;
import com.beekeeper.desktop.dao.jdbc.JdbcInspectionDao;
import com.beekeeper.desktop.scheduler.DesktopSchedulerProvider;
import com.beekeeper.shared.entity.Hive;
import com.beekeeper.shared.entity.Inspection;
import com.beekeeper.shared.i18n.TranslationManager;
import com.beekeeper.shared.repository.HiveRepository;
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

import java.util.List;

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
    private TableColumn<Inspection, Integer> broodFramesColumn;

    @FXML
    private TableColumn<Inspection, Boolean> queenSeenColumn;

    @FXML
    private TableColumn<Inspection, Integer> aggressionColumn;

    @FXML
    private ComboBox<HiveItem> hiveComboBox;

    @FXML
    private Button addButton;

    @FXML
    private Button viewButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Label statusLabel;

    private InspectionViewModel viewModel;
    private HiveRepository hiveRepository;
    private CompositeDisposable disposables = new CompositeDisposable();
    private ObservableList<Inspection> inspectionList = FXCollections.observableArrayList();
    private ObservableList<HiveItem> hiveItems = FXCollections.observableArrayList();
    private String currentHiveId;
    private String currentApiaryId;
    private TranslationManager tm;

    @FXML
    public void initialize() {
        // Get TranslationManager instance
        tm = TranslationManager.getInstance();

        // Initialize ViewModels
        JdbcInspectionDao inspectionDao = new JdbcInspectionDao();
        InspectionRepository repository = new InspectionRepository(inspectionDao);
        DesktopSchedulerProvider schedulerProvider = new DesktopSchedulerProvider();
        viewModel = new InspectionViewModel(repository, schedulerProvider);

        // Initialize Hive Repository for loading hives
        JdbcHiveDao hiveDao = new JdbcHiveDao();
        hiveRepository = new HiveRepository(hiveDao);

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

        // Brood frames column
        broodFramesColumn.setCellValueFactory(new PropertyValueFactory<>("broodFrames"));

        // Queen seen column with checkmark/cross (boolean in entity)
        queenSeenColumn.setCellValueFactory(new PropertyValueFactory<>("queenSeen"));
        queenSeenColumn.setCellFactory(column -> new TableCell<Inspection, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item ? "✓" : "✗");
                    setStyle(item ? "-fx-text-fill: green; -fx-font-size: 16px;" : "-fx-text-fill: gray; -fx-font-size: 16px;");
                }
            }
        });

        // Aggression column with level indicator (int 1-5 scale)
        // 1-2: green check, 3: orange warning, 4-5: red warning
        aggressionColumn.setCellValueFactory(new PropertyValueFactory<>("aggression"));
        aggressionColumn.setCellFactory(column -> new TableCell<Inspection, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    int level = item;
                    String icon;
                    String style;

                    if (level <= 2) {
                        // Low aggression (1-2): green check
                        icon = "✓";
                        style = "-fx-text-fill: green; -fx-font-size: 14px; -fx-font-weight: bold;";
                    } else if (level == 3) {
                        // Moderate aggression (3): orange warning
                        icon = "⚠";
                        style = "-fx-text-fill: orange; -fx-font-size: 14px; -fx-font-weight: bold;";
                    } else {
                        // High aggression (4-5): red warning
                        icon = "⚠";
                        style = "-fx-text-fill: red; -fx-font-size: 14px; -fx-font-weight: bold;";
                    }

                    setText(level + " " + icon);
                    setStyle(style);
                }
            }
        });

        // Bind table to observable list
        inspectionTable.setItems(inspectionList);

        // Setup hive ComboBox
        hiveComboBox.setItems(hiveItems);
        hiveComboBox.setOnAction(e -> {
            HiveItem selected = hiveComboBox.getValue();
            if (selected != null) {
                setHiveId(selected.getId());
            }
        });

        // Enable/disable buttons based on selection
        viewButton.setDisable(true);
        deleteButton.setDisable(true);
        addButton.setDisable(true); // Disable until hive is selected

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

    /**
     * Set apiary ID and load all hives from that apiary into ComboBox.
     */
    public void setApiaryId(String apiaryId) {
        this.currentApiaryId = apiaryId;
        loadHivesForApiary(apiaryId);
    }

    /**
     * Set hive ID and load inspections for that hive.
     */
    public void setHiveId(String hiveId) {
        this.currentHiveId = hiveId;
        addButton.setDisable(false); // Enable add button when hive is selected
        if (viewModel != null) {
            viewModel.loadInspectionsByHiveId(hiveId);
        }
    }

    /**
     * Load hives from apiary and populate ComboBox.
     */
    private void loadHivesForApiary(String apiaryId) {
        disposables.add(
            hiveRepository.getHivesByApiaryId(apiaryId)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(
                    hives -> {
                        hiveItems.clear();
                        for (Hive hive : hives) {
                            hiveItems.add(new HiveItem(hive.getId(), hive.getName()));
                        }
                        if (!hiveItems.isEmpty()) {
                            hiveComboBox.setValue(hiveItems.get(0)); // Select first hive
                            setHiveId(hiveItems.get(0).getId());
                        }
                        statusLabel.setText(tm.get("status.hives_loaded", hives.size()));
                    },
                    error -> statusLabel.setText(tm.get("error.loading_hives", error.getMessage()))
                )
        );
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

    /**
     * Helper class for displaying hives in ComboBox.
     */
    private static class HiveItem {
        private final String id;
        private final String name;

        public HiveItem(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        @Override
        public String toString() {
            return name; // Display hive name in ComboBox
        }
    }
}
