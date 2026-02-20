package com.beekeeper.desktop.controller;

import com.beekeeper.desktop.dao.jdbc.JdbcHiveDao;
import com.beekeeper.desktop.dao.jdbc.JdbcTaxationDao;
import com.beekeeper.desktop.dao.jdbc.JdbcTaxationFrameDao;
import com.beekeeper.desktop.dialog.TaxationDialog;
import com.beekeeper.desktop.dialog.TaxationWithFrames;
import com.beekeeper.desktop.scheduler.DesktopSchedulerProvider;
import com.beekeeper.shared.dto.TaxationFilters;
import com.beekeeper.shared.entity.Hive;
import com.beekeeper.shared.entity.Taxation;
import com.beekeeper.shared.entity.TaxationFrame;
import com.beekeeper.shared.i18n.TranslationManager;
import com.beekeeper.shared.repository.TaxationRepository;
import com.beekeeper.shared.util.DateUtils;
import com.beekeeper.shared.util.TaxationFilterUtil;
import com.beekeeper.shared.viewmodel.TaxationViewModel;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
    private TableColumn<Taxation, Integer> freeSpaceColumn;  // Computed field

    @FXML
    private TableColumn<Taxation, Integer> strengthColumn;  // Computed field

    // Filter UI components
    @FXML
    private DatePicker filterDateFrom;

    @FXML
    private DatePicker filterDateTo;

    @FXML
    private TextField filterHiveName;

    @FXML
    private TextField filterMinFreeSpace;

    @FXML
    private TextField filterMaxFreeSpace;

    @FXML
    private CheckBox filterHasBrood;

    @FXML
    private CheckBox filterHasStarterFrames;

    @FXML
    private TextField filterMinFreeSpacePercent;

    @FXML
    private TextField filterMaxFreeSpacePercent;

    @FXML
    private TextField filterMinCappedStoresPercent;

    @FXML
    private TextField filterMaxCappedStoresPercent;

    @FXML
    private TextField filterMinTemp;

    @FXML
    private TextField filterMaxTemp;

    @FXML
    private TextField filterMinFrameCount;

    @FXML
    private TextField filterMaxFrameCount;

    @FXML
    private TextField filterMinStrength;

    @FXML
    private TextField filterMaxStrength;

    @FXML
    private TextField filterMinBrood;

    @FXML
    private TextField filterMaxBrood;

    @FXML
    private TextField filterMinStores;

    @FXML
    private TextField filterMaxStores;

    @FXML
    private TextField filterMinPollen;

    @FXML
    private TextField filterMaxPollen;

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

        // Setup computed columns (free space and strength)
        freeSpaceColumn.setCellFactory(column -> new TableCell<Taxation, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setStyle("");
                } else {
                    Taxation taxation = getTableView().getItems().get(getIndex());
                    String hiveType = taxation.getHiveType();
                    int freeSpace = TaxationFilterUtil.calculateFreeSpaceDm(taxation, hiveType);

                    if (freeSpace == -1) {
                        // Unknown frame type
                        setText("N/A");
                        setStyle("");
                    } else if (freeSpace < 0) {
                        // Negative = unrealistic data (occupied > capacity)
                        setText(String.valueOf(freeSpace));
                        setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    } else {
                        // Positive = normal
                        setText(String.valueOf(freeSpace));
                        setStyle("");
                    }
                }
            }
        });

        strengthColumn.setCellFactory(column -> new TableCell<Taxation, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    Taxation taxation = getTableView().getItems().get(getIndex());
                    int strength = TaxationFilterUtil.calculateColonyStrength(taxation);
                    setText(strength > 0 ? String.valueOf(strength) : "-");
                }
            }
        });

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

                        // Show filtered status if filters are active
                        TaxationFilters currentFilters = viewModel.getCurrentFilters().blockingFirst();
                        if (currentFilters != null && currentFilters.hasAnyFilter()) {
                            statusLabel.setText(tm.get("status.taxations_filtered", taxations.size()));
                        } else {
                            statusLabel.setText(tm.get("status.taxations_count", taxations.size()));
                        }
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

    /**
     * Applies user-selected filters to the taxation list.
     *
     * Use case: Beekeeper wants to filter taxations by date range, free space, brood presence, etc.
     * Builds TaxationFilters object from UI fields and applies via ViewModel.
     */
    @FXML
    private void handleApplyFilters() {
        TaxationFilters filters = new TaxationFilters();

        // Date range filter
        if (filterDateFrom.getValue() != null) {
            LocalDate dateFrom = filterDateFrom.getValue();
            long timestamp = dateFrom.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
            filters.setDateFrom(timestamp);
        }
        if (filterDateTo.getValue() != null) {
            LocalDate dateTo = filterDateTo.getValue();
            long timestamp = dateTo.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            filters.setDateTo(timestamp);
        }

        // Hive name filter
        if (filterHiveName.getText() != null && !filterHiveName.getText().trim().isEmpty()) {
            filters.setHiveNameFilter(filterHiveName.getText().trim());
        }

        // Free space filter
        if (filterMinFreeSpace.getText() != null && !filterMinFreeSpace.getText().trim().isEmpty()) {
            try {
                filters.setMinFreeSpaceDm(Integer.parseInt(filterMinFreeSpace.getText().trim()));
            } catch (NumberFormatException e) {
                showError(tm.get("error.invalid_number", filterMinFreeSpace.getText()));
                return;
            }
        }
        if (filterMaxFreeSpace.getText() != null && !filterMaxFreeSpace.getText().trim().isEmpty()) {
            try {
                filters.setMaxFreeSpaceDm(Integer.parseInt(filterMaxFreeSpace.getText().trim()));
            } catch (NumberFormatException e) {
                showError(tm.get("error.invalid_number", filterMaxFreeSpace.getText()));
                return;
            }
        }

        // Boolean filters (only set if checkbox is selected)
        if (filterHasBrood.isSelected()) {
            filters.setHasBrood(true);
        }
        if (filterHasStarterFrames.isSelected()) {
            filters.setHasStarterFrames(true);
        }

        // Free space percentage filter
        if (filterMinFreeSpacePercent.getText() != null && !filterMinFreeSpacePercent.getText().trim().isEmpty()) {
            try {
                filters.setMinFreeSpacePercent(Integer.parseInt(filterMinFreeSpacePercent.getText().trim()));
            } catch (NumberFormatException e) {
                showError(tm.get("error.invalid_number", filterMinFreeSpacePercent.getText()));
                return;
            }
        }
        if (filterMaxFreeSpacePercent.getText() != null && !filterMaxFreeSpacePercent.getText().trim().isEmpty()) {
            try {
                filters.setMaxFreeSpacePercent(Integer.parseInt(filterMaxFreeSpacePercent.getText().trim()));
            } catch (NumberFormatException e) {
                showError(tm.get("error.invalid_number", filterMaxFreeSpacePercent.getText()));
                return;
            }
        }

        // Capped stores percentage filter
        if (filterMinCappedStoresPercent.getText() != null && !filterMinCappedStoresPercent.getText().trim().isEmpty()) {
            try {
                filters.setMinCappedStoresPercent(Integer.parseInt(filterMinCappedStoresPercent.getText().trim()));
            } catch (NumberFormatException e) {
                showError(tm.get("error.invalid_number", filterMinCappedStoresPercent.getText()));
                return;
            }
        }
        if (filterMaxCappedStoresPercent.getText() != null && !filterMaxCappedStoresPercent.getText().trim().isEmpty()) {
            try {
                filters.setMaxCappedStoresPercent(Integer.parseInt(filterMaxCappedStoresPercent.getText().trim()));
            } catch (NumberFormatException e) {
                showError(tm.get("error.invalid_number", filterMaxCappedStoresPercent.getText()));
                return;
            }
        }

        // Temperature range filter
        if (filterMinTemp.getText() != null && !filterMinTemp.getText().trim().isEmpty()) {
            try {
                filters.setMinTemperature(Double.parseDouble(filterMinTemp.getText().trim()));
            } catch (NumberFormatException e) {
                showError(tm.get("error.invalid_number", filterMinTemp.getText()));
                return;
            }
        }
        if (filterMaxTemp.getText() != null && !filterMaxTemp.getText().trim().isEmpty()) {
            try {
                filters.setMaxTemperature(Double.parseDouble(filterMaxTemp.getText().trim()));
            } catch (NumberFormatException e) {
                showError(tm.get("error.invalid_number", filterMaxTemp.getText()));
                return;
            }
        }

        // Frame count range filter
        if (filterMinFrameCount.getText() != null && !filterMinFrameCount.getText().trim().isEmpty()) {
            try {
                filters.setMinFrameCount(Integer.parseInt(filterMinFrameCount.getText().trim()));
            } catch (NumberFormatException e) {
                showError(tm.get("error.invalid_number", filterMinFrameCount.getText()));
                return;
            }
        }
        if (filterMaxFrameCount.getText() != null && !filterMaxFrameCount.getText().trim().isEmpty()) {
            try {
                filters.setMaxFrameCount(Integer.parseInt(filterMaxFrameCount.getText().trim()));
            } catch (NumberFormatException e) {
                showError(tm.get("error.invalid_number", filterMaxFrameCount.getText()));
                return;
            }
        }

        // Strength range filter
        if (filterMinStrength.getText() != null && !filterMinStrength.getText().trim().isEmpty()) {
            try {
                filters.setMinStrength(Integer.parseInt(filterMinStrength.getText().trim()));
            } catch (NumberFormatException e) {
                showError(tm.get("error.invalid_number", filterMinStrength.getText()));
                return;
            }
        }
        if (filterMaxStrength.getText() != null && !filterMaxStrength.getText().trim().isEmpty()) {
            try {
                filters.setMaxStrength(Integer.parseInt(filterMaxStrength.getText().trim()));
            } catch (NumberFormatException e) {
                showError(tm.get("error.invalid_number", filterMaxStrength.getText()));
                return;
            }
        }

        // Brood range filter
        if (filterMinBrood.getText() != null && !filterMinBrood.getText().trim().isEmpty()) {
            try {
                filters.setMinBroodDm(Integer.parseInt(filterMinBrood.getText().trim()));
            } catch (NumberFormatException e) {
                showError(tm.get("error.invalid_number", filterMinBrood.getText()));
                return;
            }
        }
        if (filterMaxBrood.getText() != null && !filterMaxBrood.getText().trim().isEmpty()) {
            try {
                filters.setMaxBroodDm(Integer.parseInt(filterMaxBrood.getText().trim()));
            } catch (NumberFormatException e) {
                showError(tm.get("error.invalid_number", filterMaxBrood.getText()));
                return;
            }
        }

        // Stores range filter
        if (filterMinStores.getText() != null && !filterMinStores.getText().trim().isEmpty()) {
            try {
                filters.setMinStoresDm(Integer.parseInt(filterMinStores.getText().trim()));
            } catch (NumberFormatException e) {
                showError(tm.get("error.invalid_number", filterMinStores.getText()));
                return;
            }
        }
        if (filterMaxStores.getText() != null && !filterMaxStores.getText().trim().isEmpty()) {
            try {
                filters.setMaxStoresDm(Integer.parseInt(filterMaxStores.getText().trim()));
            } catch (NumberFormatException e) {
                showError(tm.get("error.invalid_number", filterMaxStores.getText()));
                return;
            }
        }

        // Pollen range filter
        if (filterMinPollen.getText() != null && !filterMinPollen.getText().trim().isEmpty()) {
            try {
                filters.setMinPollenDm(Integer.parseInt(filterMinPollen.getText().trim()));
            } catch (NumberFormatException e) {
                showError(tm.get("error.invalid_number", filterMinPollen.getText()));
                return;
            }
        }
        if (filterMaxPollen.getText() != null && !filterMaxPollen.getText().trim().isEmpty()) {
            try {
                filters.setMaxPollenDm(Integer.parseInt(filterMaxPollen.getText().trim()));
            } catch (NumberFormatException e) {
                showError(tm.get("error.invalid_number", filterMaxPollen.getText()));
                return;
            }
        }

        // Apply filters via ViewModel
        viewModel.applyFilters(filters);

        // Show success message briefly
        showSuccess(tm.get("status.filters_applied"));
        // Reset to normal black color after 2 seconds
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                Platform.runLater(() -> {
                    if (statusLabel.getStyle().contains("green")) {
                        statusLabel.setStyle("-fx-text-fill: black;");
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        }).start();
    }

    /**
     * Clears all filters and shows all taxations.
     *
     * Use case: Beekeeper wants to reset filters and see all taxations again.
     */
    @FXML
    private void handleClearFilters() {
        // Clear all UI fields
        filterDateFrom.setValue(null);
        filterDateTo.setValue(null);
        filterHiveName.clear();
        filterMinFreeSpace.clear();
        filterMaxFreeSpace.clear();
        filterHasBrood.setSelected(false);
        filterHasStarterFrames.setSelected(false);
        filterMinFreeSpacePercent.clear();
        filterMaxFreeSpacePercent.clear();
        filterMinCappedStoresPercent.clear();
        filterMaxCappedStoresPercent.clear();
        filterMinTemp.clear();
        filterMaxTemp.clear();
        filterMinFrameCount.clear();
        filterMaxFrameCount.clear();
        filterMinStrength.clear();
        filterMaxStrength.clear();
        filterMinBrood.clear();
        filterMaxBrood.clear();
        filterMinStores.clear();
        filterMaxStores.clear();
        filterMinPollen.clear();
        filterMaxPollen.clear();

        // Clear filters in ViewModel
        viewModel.clearFilters();

        // Show success message
        showSuccess(tm.get("status.filters_cleared"));
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                Platform.runLater(() -> {
                    if (statusLabel.getStyle().contains("green")) {
                        statusLabel.setStyle("-fx-text-fill: black;");
                    }
                });
            } catch (InterruptedException e) {
                // Ignore
            }
        }).start();
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
