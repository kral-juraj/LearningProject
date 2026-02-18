package com.beekeeper.desktop.controller;

import com.beekeeper.desktop.component.ApiaryCard;
import com.beekeeper.desktop.dao.jdbc.JdbcApiaryDao;
import com.beekeeper.desktop.dao.jdbc.JdbcHiveDao;
import com.beekeeper.desktop.dialog.ApiaryDialog;
import com.beekeeper.desktop.scheduler.DesktopSchedulerProvider;
import com.beekeeper.shared.entity.Apiary;
import com.beekeeper.shared.entity.Hive;
import com.beekeeper.shared.i18n.TranslationManager;
import com.beekeeper.shared.repository.ApiaryRepository;
import com.beekeeper.shared.repository.HiveRepository;
import com.beekeeper.shared.viewmodel.ApiaryViewModel;
import com.beekeeper.shared.viewmodel.HiveViewModel;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.chart.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Controller for Apiary list view with card-based layout.
 * Displays apiaries as cards with dashboard panel on selection.
 */
public class ApiaryListController {

    @FXML
    private FlowPane apiaryGridPane;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button showHivesButton;

    @FXML
    private Label statusLabel;

    // Dashboard components
    @FXML
    private VBox dashboardPanel;

    @FXML
    private Label dashboardApiaryName;

    @FXML
    private Label dashboardApiaryLocation;

    @FXML
    private Label dashboardApiaryAddress;

    @FXML
    private Label dashboardRegNumber;

    @FXML
    private TextArea dashboardDescription;

    // Charts
    @FXML
    private PieChart colonyTypesPieChart;

    // FlowPanes for cards
    @FXML
    private FlowPane mainStatsFlowPane;

    @FXML
    private FlowPane hiveTypesFlowPane;

    @FXML
    private FlowPane queenStatsFlowPane;

    @FXML
    private FlowPane frameStatsFlowPane;

    // Dashboard sections for drag & drop
    @FXML
    private VBox sectionMainStats;

    @FXML
    private VBox sectionHiveTypes;

    @FXML
    private VBox sectionQueens;

    @FXML
    private VBox sectionFrames;

    @FXML
    private VBox sectionColonyTypes;

    @FXML
    private VBox sectionDescription;

    private ApiaryViewModel viewModel;
    private HiveViewModel hiveViewModel;
    private CompositeDisposable disposables = new CompositeDisposable();
    private Consumer<Apiary> onApiarySelected;
    private TranslationManager tm;
    private Apiary selectedApiary = null;
    private Map<String, Integer> apiaryHiveCounts = new HashMap<>();
    private Map<String, Integer> apiaryActiveHiveCounts = new HashMap<>();

    @FXML
    public void initialize() {
        // Get TranslationManager instance
        tm = TranslationManager.getInstance();

        // Initialize ViewModels with dependencies
        JdbcApiaryDao apiaryDao = new JdbcApiaryDao();
        ApiaryRepository apiaryRepository = new ApiaryRepository(apiaryDao);
        DesktopSchedulerProvider schedulerProvider = new DesktopSchedulerProvider();
        viewModel = new ApiaryViewModel(apiaryRepository, schedulerProvider);

        // Initialize HiveViewModel for dashboard statistics
        JdbcHiveDao hiveDao = new JdbcHiveDao();
        HiveRepository hiveRepository = new HiveRepository(hiveDao);
        hiveViewModel = new HiveViewModel(hiveRepository, schedulerProvider);

        // Disable toolbar buttons until an apiary is selected
        editButton.setDisable(true);
        deleteButton.setDisable(true);
        showHivesButton.setDisable(true);

        // Hide dashboard initially
        dashboardPanel.setVisible(false);

        // Defer setup to avoid macOS NSTrackingRectTag bug
        Platform.runLater(() -> {
            // Subscribe to ViewModel state
            subscribeToViewModel();

            // Setup drag & drop for dashboard sections
            setupDashboardSectionDragAndDrop();

            // Load and apply saved dashboard order
            loadDashboardOrder();

            // Load initial data
            viewModel.loadApiaries();
        });
    }

    private void subscribeToViewModel() {
        disposables.add(
            viewModel.getApiaries()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(
                    apiaries -> {
                        updateApiaryGrid(apiaries);
                        statusLabel.setText(tm.get("status.apiaries_count", apiaries.size()));
                    },
                    error -> showError(tm.get("error.loading_apiaries", error.getMessage()))
                )
        );

        disposables.add(
            viewModel.getLoading()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(loading -> {
                    addButton.setDisable(loading);
                    apiaryGridPane.setDisable(loading);
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
     * Update the apiary grid with ApiaryCard components.
     *
     * @param apiaries List of apiaries to display
     */
    private void updateApiaryGrid(List<Apiary> apiaries) {
        apiaryGridPane.getChildren().clear();

        for (Apiary apiary : apiaries) {
            ApiaryCard card = new ApiaryCard();
            card.setApiary(apiary);

            // Set hive statistics if available
            int hiveCount = apiaryHiveCounts.getOrDefault(apiary.getId(), 0);
            int activeCount = apiaryActiveHiveCounts.getOrDefault(apiary.getId(), 0);
            card.setHiveStats(hiveCount, activeCount);

            // Click on card selects it
            card.setOnMouseClicked(e -> {
                if (e.getClickCount() == 1) {
                    selectApiary(apiary, card);
                }
            });

            // Enable drag-and-drop for reordering
            setupDragAndDrop(card, apiary, apiaries);

            apiaryGridPane.getChildren().add(card);
        }

        // Load hive counts for all apiaries
        loadHiveCountsForApiaries(apiaries);
    }

    /**
     * Load hive counts for all apiaries and update cards.
     * Uses direct DAO access to avoid BehaviorRelay issues with multiple rapid subscriptions.
     *
     * @param apiaries List of apiaries
     */
    private void loadHiveCountsForApiaries(List<Apiary> apiaries) {
        JdbcHiveDao hiveDao = new JdbcHiveDao();
        DesktopSchedulerProvider schedulerProvider = new DesktopSchedulerProvider();

        for (Apiary apiary : apiaries) {
            // Load hives directly from DAO for each apiary independently
            disposables.add(
                hiveDao.getByApiaryId(apiary.getId())
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(JavaFxScheduler.platform())
                    .subscribe(
                        hives -> {
                            int total = hives.size();
                            int active = (int) hives.stream().filter(Hive::isActive).count();

                            apiaryHiveCounts.put(apiary.getId(), total);
                            apiaryActiveHiveCounts.put(apiary.getId(), active);

                            // Update the card if it exists
                            for (javafx.scene.Node node : apiaryGridPane.getChildren()) {
                                if (node instanceof ApiaryCard) {
                                    ApiaryCard card = (ApiaryCard) node;
                                    if (card.getApiary().getId().equals(apiary.getId())) {
                                        card.setHiveStats(total, active);
                                        break;
                                    }
                                }
                            }
                        },
                        error -> {
                            // Set zero counts on error
                            apiaryHiveCounts.put(apiary.getId(), 0);
                            apiaryActiveHiveCounts.put(apiary.getId(), 0);
                        }
                    )
            );
        }
    }

    /**
     * Select an apiary and enable toolbar buttons.
     *
     * @param apiary Selected apiary
     * @param card Selected card component
     */
    private void selectApiary(Apiary apiary, ApiaryCard card) {
        this.selectedApiary = apiary;
        editButton.setDisable(false);
        deleteButton.setDisable(false);
        showHivesButton.setDisable(false);

        // Update visual selection
        for (javafx.scene.Node node : apiaryGridPane.getChildren()) {
            if (node instanceof ApiaryCard) {
                ((ApiaryCard) node).setSelected(false);
            }
        }
        card.setSelected(true);

        // Update dashboard
        updateDashboard(apiary);
    }

    /**
     * Setup drag-and-drop handlers for an apiary card.
     *
     * @param card ApiaryCard component
     * @param apiary Apiary entity
     * @param allApiaries List of all apiaries (for reordering)
     */
    private void setupDragAndDrop(ApiaryCard card, Apiary apiary, List<Apiary> allApiaries) {
        // Drag detected - start drag operation
        card.setOnDragDetected(event -> {
            javafx.scene.input.Dragboard db = card.startDragAndDrop(javafx.scene.input.TransferMode.MOVE);
            javafx.scene.input.ClipboardContent content = new javafx.scene.input.ClipboardContent();
            content.putString(apiary.getId());
            db.setContent(content);
            card.setOpacity(0.5);
            event.consume();
        });

        // Drag over - accept if dragging another apiary card
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

        // Drag dropped - reorder apiaries
        card.setOnDragDropped(event -> {
            javafx.scene.input.Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                String draggedApiaryId = db.getString();
                Apiary draggedApiary = null;
                int draggedIndex = -1;
                int targetIndex = -1;

                // Find dragged apiary and target apiary indices
                for (int i = 0; i < allApiaries.size(); i++) {
                    if (allApiaries.get(i).getId().equals(draggedApiaryId)) {
                        draggedApiary = allApiaries.get(i);
                        draggedIndex = i;
                    }
                    if (allApiaries.get(i).getId().equals(apiary.getId())) {
                        targetIndex = i;
                    }
                }

                if (draggedApiary != null && draggedIndex != -1 && targetIndex != -1 && draggedIndex != targetIndex) {
                    // Reorder the list
                    allApiaries.remove(draggedIndex);
                    allApiaries.add(targetIndex, draggedApiary);

                    // Update displayOrder for all apiaries
                    for (int i = 0; i < allApiaries.size(); i++) {
                        allApiaries.get(i).setDisplayOrder(i);
                    }

                    // Save new order to database
                    viewModel.updateApiaryOrder(new ArrayList<>(allApiaries));

                    // Refresh UI
                    updateApiaryGrid(allApiaries);
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

    @FXML
    private void handleAddApiary() {
        // Show dialog to add new apiary with extended fields
        ApiaryDialog dialog = new ApiaryDialog(null);

        dialog.showAndWait().ifPresent(apiary -> {
            // ViewModel will handle ID generation and timestamps
            viewModel.createApiary(
                apiary.getName(),
                apiary.getLocation(),
                apiary.getLatitude(),
                apiary.getLongitude(),
                apiary.getRegistrationNumber(),
                apiary.getAddress(),
                apiary.getDescription()
            );
        });
    }

    @FXML
    private void handleEditApiary() {
        if (selectedApiary == null) return;

        // Show dialog with existing apiary data
        ApiaryDialog dialog = new ApiaryDialog(selectedApiary);

        dialog.showAndWait().ifPresent(updatedApiary -> {
            // Update the apiary through ViewModel
            viewModel.updateApiary(updatedApiary);
        });
    }

    @FXML
    private void handleDeleteApiary() {
        if (selectedApiary == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(tm.get("dialog.delete_apiary.title"));
        alert.setHeaderText(tm.get("dialog.delete_apiary.header", selectedApiary.getName()));
        alert.setContentText(tm.get("dialog.delete_apiary.content"));

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                viewModel.deleteApiary(selectedApiary);
                selectedApiary = null;
                dashboardPanel.setVisible(false);
            }
        });
    }

    @FXML
    private void handleRefresh() {
        viewModel.loadApiaries();
    }

    @FXML
    private void handleShowHives() {
        if (selectedApiary != null && onApiarySelected != null) {
            onApiarySelected.accept(selectedApiary);
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

    /**
     * Update dashboard with statistics for the selected apiary.
     *
     * Use case: User selects an apiary card, dashboard shows comprehensive hive statistics.
     *
     * @param apiary Selected apiary to display statistics for
     */
    private void updateDashboard(Apiary apiary) {
        // Show dashboard panel
        dashboardPanel.setVisible(true);

        // Display apiary info
        dashboardApiaryName.setText(apiary.getName());
        dashboardApiaryLocation.setText(tm.get("label.location") + ": " +
            (apiary.getLocation() != null && !apiary.getLocation().isEmpty() ? apiary.getLocation() : tm.get("label.not_specified")));

        if (apiary.getAddress() != null && !apiary.getAddress().trim().isEmpty()) {
            dashboardApiaryAddress.setText(tm.get("label.address") + ": " + apiary.getAddress());
            dashboardApiaryAddress.setVisible(true);
        } else {
            dashboardApiaryAddress.setVisible(false);
        }

        if (apiary.getRegistrationNumber() != null && !apiary.getRegistrationNumber().trim().isEmpty()) {
            dashboardRegNumber.setText(tm.get("label.registration_number") + ": " + apiary.getRegistrationNumber());
            dashboardRegNumber.setVisible(true);
        } else {
            dashboardRegNumber.setVisible(false);
        }

        if (apiary.getDescription() != null && !apiary.getDescription().trim().isEmpty()) {
            dashboardDescription.setText(apiary.getDescription());
            dashboardDescription.setVisible(true);
        } else {
            dashboardDescription.setVisible(false);
        }

        // Load hives for this apiary and calculate statistics
        hiveViewModel.loadHivesByApiaryId(apiary.getId());

        // Subscribe to hive data for statistics
        disposables.add(
            hiveViewModel.getHives()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(
                    hives -> displayHiveStatistics(hives),
                    error -> {
                        // Error loading hives
                        mainStatsFlowPane.getChildren().clear();
                        colonyTypesPieChart.getData().clear();
                        hiveTypesFlowPane.getChildren().clear();
                        queenStatsFlowPane.getChildren().clear();
                        frameStatsFlowPane.getChildren().clear();
                    }
                )
        );
    }

    /**
     * Display comprehensive hive statistics in the dashboard.
     *
     * @param hives List of hives for the selected apiary
     */
    private void displayHiveStatistics(List<Hive> hives) {
        // Basic counts
        int total = hives.size();
        int active = (int) hives.stream().filter(Hive::isActive).count();
        int inactive = total - active;

        // Colony types (production vs nucs)
        int productionColonies = 0;
        int nucs = 0;
        int matingNucs = 0;

        for (Hive hive : hives) {
            if (!hive.isActive()) continue; // Count only active hives

            String type = hive.getType() != null ? hive.getType() : "VERTICAL";
            if ("NUKE".equals(type)) {
                nucs++;
            } else if ("MATING_NUC".equals(type)) {
                matingNucs++;
            } else {
                productionColonies++; // VERTICAL or HORIZONTAL are production colonies
            }
        }

        // Main Statistics Cards
        mainStatsFlowPane.getChildren().clear();
        VBox totalCard = createStatCard(tm.get("label.total_hives"), String.valueOf(total), "#3498db");
        VBox activeCard = createStatCard(tm.get("label.active_hives"), String.valueOf(active), "#27ae60");
        VBox inactiveCard = createStatCard(tm.get("label.inactive_hives"), String.valueOf(inactive), "#95a5a6");
        VBox productionCard = createStatCard(tm.get("label.production_colonies"), String.valueOf(productionColonies), "#f39c12");
        mainStatsFlowPane.getChildren().addAll(totalCard, activeCard, inactiveCard, productionCard);

        // Colony Types Pie Chart
        colonyTypesPieChart.getData().clear();
        if (productionColonies > 0) {
            PieChart.Data prodData = new PieChart.Data(tm.get("label.production_colonies") + " (" + productionColonies + ")", productionColonies);
            colonyTypesPieChart.getData().add(prodData);
        }
        if (nucs > 0) {
            PieChart.Data nucsData = new PieChart.Data(tm.get("label.nucs") + " (" + nucs + ")", nucs);
            colonyTypesPieChart.getData().add(nucsData);
        }
        if (matingNucs > 0) {
            PieChart.Data matingData = new PieChart.Data(tm.get("label.mating_nucs") + " (" + matingNucs + ")", matingNucs);
            colonyTypesPieChart.getData().add(matingData);
        }

        // Apply custom colors to pie chart
        Platform.runLater(() -> {
            if (colonyTypesPieChart.getData().size() > 0) {
                colonyTypesPieChart.getData().get(0).getNode().setStyle("-fx-pie-color: #f39c12;");
            }
            if (colonyTypesPieChart.getData().size() > 1) {
                colonyTypesPieChart.getData().get(1).getNode().setStyle("-fx-pie-color: #3498db;");
            }
            if (colonyTypesPieChart.getData().size() > 2) {
                colonyTypesPieChart.getData().get(2).getNode().setStyle("-fx-pie-color: #9b59b6;");
            }
        });

        // Hive Types Cards
        Map<String, Integer> typeCounts = new HashMap<>();
        for (Hive hive : hives) {
            String type = hive.getType() != null ? hive.getType() : "VERTICAL";
            typeCounts.put(type, typeCounts.getOrDefault(type, 0) + 1);
        }

        hiveTypesFlowPane.getChildren().clear();
        if (!typeCounts.isEmpty()) {
            String[] colors = {"#3498db", "#9b59b6", "#e74c3c", "#1abc9c", "#f39c12", "#34495e"};
            java.util.concurrent.atomic.AtomicInteger colorIndex = new java.util.concurrent.atomic.AtomicInteger(0);

            // Sort by count descending
            typeCounts.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .forEach(entry -> {
                    String typeName = tm.get("hive_type." + entry.getKey().toLowerCase());
                    String color = colors[colorIndex.getAndIncrement() % colors.length];
                    VBox card = createStatCard(typeName, String.valueOf(entry.getValue()), color);
                    hiveTypesFlowPane.getChildren().add(card);
                });
        }

        // Queen Statistics Cards
        Map<Integer, Integer> queensByYear = new HashMap<>();
        for (Hive hive : hives) {
            if (hive.getQueenYear() > 0) {
                queensByYear.put(hive.getQueenYear(), queensByYear.getOrDefault(hive.getQueenYear(), 0) + 1);
            }
        }

        queenStatsFlowPane.getChildren().clear();
        if (!queensByYear.isEmpty()) {
            // Sort by year descending (newest first)
            queensByYear.entrySet().stream()
                .sorted((e1, e2) -> e2.getKey().compareTo(e1.getKey()))
                .forEach(entry -> {
                    String year = String.valueOf(entry.getKey());
                    String count = String.valueOf(entry.getValue()); // Just the number
                    // Standard beekeeper queen marking colors based on year
                    String color = getQueenColorByYear(entry.getKey());
                    VBox card = createStatCard(year, count, color);
                    queenStatsFlowPane.getChildren().add(card);
                });
        } else {
            Label noQueensLabel = new Label(tm.get("label.no_queens"));
            noQueensLabel.setStyle("-fx-text-fill: #95a5a6; -fx-font-size: 13px; -fx-padding: 10;");
            queenStatsFlowPane.getChildren().add(noQueensLabel);
        }

        // Frame Statistics Cards
        int totalDarkFrames = hives.stream().mapToInt(Hive::getDarkFrames).sum();
        int totalLightFrames = hives.stream().mapToInt(Hive::getLightFrames).sum();
        int totalNewFrames = hives.stream().mapToInt(Hive::getNewFrames).sum();
        int totalFoundationFrames = hives.stream().mapToInt(Hive::getFoundationFrames).sum();
        int totalEmptyFrames = hives.stream().mapToInt(Hive::getEmptyFrames).sum();

        frameStatsFlowPane.getChildren().clear();

        // Create cards for each frame type with specific colors
        if (totalDarkFrames > 0) {
            VBox card = createStatCard(tm.get("label.dark_frames"), String.valueOf(totalDarkFrames), "#34495e");
            frameStatsFlowPane.getChildren().add(card);
        }
        if (totalLightFrames > 0) {
            VBox card = createStatCard(tm.get("label.light_frames"), String.valueOf(totalLightFrames), "#f1c40f");
            frameStatsFlowPane.getChildren().add(card);
        }
        if (totalNewFrames > 0) {
            VBox card = createStatCard(tm.get("label.new_frames"), String.valueOf(totalNewFrames), "#2ecc71");
            frameStatsFlowPane.getChildren().add(card);
        }
        if (totalFoundationFrames > 0) {
            VBox card = createStatCard(tm.get("label.foundation_frames"), String.valueOf(totalFoundationFrames), "#e67e22");
            frameStatsFlowPane.getChildren().add(card);
        }
        if (totalEmptyFrames > 0) {
            VBox card = createStatCard(tm.get("label.empty_frames"), String.valueOf(totalEmptyFrames), "#95a5a6");
            frameStatsFlowPane.getChildren().add(card);
        }

        if (totalDarkFrames == 0 && totalLightFrames == 0 && totalNewFrames == 0 &&
            totalFoundationFrames == 0 && totalEmptyFrames == 0) {
            Label noFramesLabel = new Label(tm.get("label.no_frames"));
            noFramesLabel.setStyle("-fx-text-fill: #95a5a6; -fx-font-size: 13px; -fx-padding: 10;");
            frameStatsFlowPane.getChildren().add(noFramesLabel);
        }
    }

    /**
     * Create a fancy statistics card with gradient background.
     *
     * @param label Card label (e.g., "LR", "2025", "Tmavé rámiky")
     * @param value Card value (e.g., "5", "3 matky")
     * @param baseColor Base color for gradient (hex format)
     * @return VBox card component
     */
    private VBox createStatCard(String label, String value, String baseColor) {
        VBox card = new VBox(3);
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(90);
        card.setMinWidth(90);
        card.setPrefHeight(65);

        // Darker shade for gradient
        String darkerColor = adjustBrightness(baseColor, 0.85);

        card.setStyle(
            "-fx-background-color: linear-gradient(to bottom, " + baseColor + ", " + darkerColor + "); " +
            "-fx-background-radius: 8; " +
            "-fx-padding: 8; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 6, 0, 0, 2);"
        );

        // Label
        Label titleLabel = new Label(label);
        titleLabel.setStyle(
            "-fx-font-size: 10px; " +
            "-fx-text-fill: rgba(255,255,255,0.95); " +
            "-fx-font-weight: 600;"
        );
        titleLabel.setWrapText(true);
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setMaxWidth(85);

        // Value
        Label valueLabel = new Label(value);
        valueLabel.setStyle(
            "-fx-font-size: 20px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: white;"
        );
        valueLabel.setWrapText(true);
        valueLabel.setAlignment(Pos.CENTER);
        valueLabel.setMaxWidth(85);

        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }

    /**
     * Get standard beekeeper queen marking color based on year.
     *
     * International standard:
     * - Years ending in 1 or 6: White
     * - Years ending in 2 or 7: Yellow
     * - Years ending in 3 or 8: Red
     * - Years ending in 4 or 9: Green
     * - Years ending in 5 or 0: Blue
     *
     * @param year Queen year
     * @return Hex color code for the year
     */
    private String getQueenColorByYear(int year) {
        int lastDigit = year % 10;
        switch (lastDigit) {
            case 1:
            case 6:
                return "#E8E8E8"; // White (slightly gray for visibility)
            case 2:
            case 7:
                return "#FFD700"; // Yellow (gold)
            case 3:
            case 8:
                return "#DC143C"; // Red (crimson)
            case 4:
            case 9:
                return "#228B22"; // Green (forest green)
            case 5:
            case 0:
                return "#4169E1"; // Blue (royal blue)
            default:
                return "#95a5a6"; // Gray fallback
        }
    }

    /**
     * Adjust color brightness for gradient effect.
     *
     * @param hexColor Hex color string (e.g., "#3498db")
     * @param factor Brightness factor (0.0-1.0, where 1.0 is original)
     * @return Adjusted hex color string
     */
    private String adjustBrightness(String hexColor, double factor) {
        try {
            // Remove # if present
            String hex = hexColor.startsWith("#") ? hexColor.substring(1) : hexColor;

            // Parse RGB components
            int r = Integer.parseInt(hex.substring(0, 2), 16);
            int g = Integer.parseInt(hex.substring(2, 4), 16);
            int b = Integer.parseInt(hex.substring(4, 6), 16);

            // Apply brightness factor
            r = (int) (r * factor);
            g = (int) (g * factor);
            b = (int) (b * factor);

            // Clamp to 0-255
            r = Math.max(0, Math.min(255, r));
            g = Math.max(0, Math.min(255, g));
            b = Math.max(0, Math.min(255, b));

            // Convert back to hex
            return String.format("#%02x%02x%02x", r, g, b);
        } catch (Exception e) {
            return hexColor; // Return original on error
        }
    }

    /**
     * Setup drag & drop for dashboard sections to allow reordering.
     */
    private void setupDashboardSectionDragAndDrop() {
        List<VBox> draggableSections = Arrays.asList(
            sectionMainStats, sectionHiveTypes, sectionQueens,
            sectionFrames, sectionColonyTypes
        );

        for (VBox section : draggableSections) {
            setupSectionDragAndDrop(section);
        }
    }

    /**
     * Setup drag & drop for a single dashboard section.
     *
     * @param section VBox section to enable drag & drop on
     */
    private void setupSectionDragAndDrop(VBox section) {
        // On drag detected
        section.setOnDragDetected(event -> {
            javafx.scene.input.Dragboard db = section.startDragAndDrop(javafx.scene.input.TransferMode.MOVE);
            javafx.scene.input.ClipboardContent content = new javafx.scene.input.ClipboardContent();
            content.putString(section.getId());
            db.setContent(content);

            // Visual feedback
            section.setOpacity(0.5);
            event.consume();
        });

        // On drag over
        section.setOnDragOver(event -> {
            if (event.getGestureSource() != section && event.getDragboard().hasString()) {
                event.acceptTransferModes(javafx.scene.input.TransferMode.MOVE);
            }
            event.consume();
        });

        // On drag entered
        section.setOnDragEntered(event -> {
            if (event.getGestureSource() != section && event.getDragboard().hasString()) {
                section.setStyle(section.getStyle() + "-fx-border-color: #3498db; -fx-border-width: 2;");
            }
            event.consume();
        });

        // On drag exited
        section.setOnDragExited(event -> {
            section.setStyle(section.getStyle().replace("-fx-border-color: #3498db; -fx-border-width: 2;", ""));
            event.consume();
        });

        // On drag dropped
        section.setOnDragDropped(event -> {
            javafx.scene.input.Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                String draggedSectionId = db.getString();
                VBox draggedSection = (VBox) section.getParent().lookup("#" + draggedSectionId);

                if (draggedSection != null) {
                    // Get current indices
                    int draggedIndex = dashboardPanel.getChildren().indexOf(draggedSection);
                    int targetIndex = dashboardPanel.getChildren().indexOf(section);

                    // Remove and reinsert at new position
                    dashboardPanel.getChildren().remove(draggedSection);
                    dashboardPanel.getChildren().add(targetIndex, draggedSection);

                    // Save new order
                    saveDashboardOrder();

                    success = true;
                }
            }

            event.setDropCompleted(success);
            event.consume();
        });

        // On drag done
        section.setOnDragDone(event -> {
            section.setOpacity(1.0);
            event.consume();
        });
    }

    /**
     * Load dashboard section order from settings.
     */
    private void loadDashboardOrder() {
        try {
            String order = getSettingValue("dashboard_section_order");
            if (order != null && !order.isEmpty()) {
                String[] sectionIds = order.split(",");

                // Collect all current children
                List<javafx.scene.Node> children = new ArrayList<>(dashboardPanel.getChildren());

                // Find title and info box (always first)
                javafx.scene.Node title = children.stream()
                    .filter(n -> n instanceof Label && ((Label)n).getText().equals(tm.get("label.apiary_dashboard")))
                    .findFirst().orElse(null);
                javafx.scene.Node infoBox = children.stream()
                    .filter(n -> n.getId() != null && n.getId().equals("apiaryInfoBox"))
                    .findFirst().orElse(null);

                // Collect movable sections
                Map<String, javafx.scene.Node> sections = new HashMap<>();
                for (javafx.scene.Node child : children) {
                    if (child.getId() != null && child.getId().startsWith("section") &&
                        !child.getId().equals("sectionDescription")) {
                        sections.put(child.getId(), child);
                    }
                }

                // Find description (always last)
                javafx.scene.Node description = children.stream()
                    .filter(n -> n.getId() != null && n.getId().equals("sectionDescription"))
                    .findFirst().orElse(null);

                // Rebuild dashboard in new order
                dashboardPanel.getChildren().clear();

                if (title != null) {
                    dashboardPanel.getChildren().add(title);
                }
                if (infoBox != null) {
                    dashboardPanel.getChildren().add(infoBox);
                }

                // Add sections in saved order
                for (String sectionId : sectionIds) {
                    javafx.scene.Node section = sections.get(sectionId.trim());
                    if (section != null) {
                        dashboardPanel.getChildren().add(section);
                        sections.remove(sectionId.trim());
                    }
                }

                // Add any remaining sections not in saved order
                for (javafx.scene.Node section : sections.values()) {
                    dashboardPanel.getChildren().add(section);
                }

                // Description always last
                if (description != null) {
                    dashboardPanel.getChildren().add(description);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading dashboard order: " + e.getMessage());
        }
    }

    /**
     * Save current dashboard section order to settings.
     */
    private void saveDashboardOrder() {
        try {
            StringBuilder order = new StringBuilder();
            boolean first = true;

            for (javafx.scene.Node node : dashboardPanel.getChildren()) {
                if (node instanceof VBox && node.getId() != null &&
                    (node.getId().startsWith("section") && !node.getId().equals("sectionDescription"))) {
                    if (!first) {
                        order.append(",");
                    }
                    order.append(node.getId());
                    first = false;
                }
            }

            saveSettingValue("dashboard_section_order", order.toString());
        } catch (Exception e) {
            System.err.println("Error saving dashboard order: " + e.getMessage());
        }
    }

    /**
     * Get setting value from database.
     *
     * @param key Setting key
     * @return Setting value or null if not found
     */
    private String getSettingValue(String key) {
        try (java.sql.Connection conn = com.beekeeper.desktop.db.DatabaseManager.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement("SELECT value FROM settings WHERE key = ?")) {
            stmt.setString(1, key);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("value");
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting setting: " + e.getMessage());
        }
        return null;
    }

    /**
     * Save setting value to database.
     *
     * @param key Setting key
     * @param value Setting value
     */
    private void saveSettingValue(String key, String value) {
        try (java.sql.Connection conn = com.beekeeper.desktop.db.DatabaseManager.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(
                 "INSERT OR REPLACE INTO settings (key, value, updatedAt) VALUES (?, ?, ?)")) {
            stmt.setString(1, key);
            stmt.setString(2, value);
            stmt.setLong(3, System.currentTimeMillis());
            stmt.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error saving setting: " + e.getMessage());
        }
    }

    public void cleanup() {
        disposables.clear();
        if (viewModel != null) {
            viewModel.dispose();
        }
    }

    /**
     * Set callback for when "Zobraziť úle" button is clicked.
     * @param callback Consumer that receives the selected Apiary
     */
    public void setOnApiarySelected(Consumer<Apiary> callback) {
        this.onApiarySelected = callback;
    }
}
