package com.beekeeper.desktop.controller;

import com.beekeeper.desktop.calculator.*;
import com.beekeeper.desktop.dao.jdbc.JdbcCalendarEventDao;
import com.beekeeper.desktop.db.DatabaseManager;
import com.beekeeper.desktop.dialog.VarroaSettingsDialog;
import com.beekeeper.desktop.dialog.VarroaTreatmentDialog;
import com.beekeeper.desktop.scheduler.DesktopSchedulerProvider;
import com.beekeeper.desktop.util.DateTimeConverter;
import com.beekeeper.desktop.util.ValidationHelper;
import com.beekeeper.shared.entity.CalendarEvent;
import com.beekeeper.shared.repository.CalendarEventRepository;
import com.beekeeper.shared.scheduler.SchedulerProvider;
import com.beekeeper.shared.viewmodel.CalendarEventViewModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Controller for Calculators tab.
 * Handles 3 calculators: Varroa, Queen Rearing Timeline, and Feed Calculator.
 */
public class CalculatorsController {

    // ========== VARROA COMPONENTS ==========
    @FXML private TextField varroaCountField;
    @FXML private DatePicker varroaMeasurementDate;
    @FXML private CheckBox varroaBroodPresent;
    @FXML private ComboBox<String> varroaProjectionDays;
    @FXML private TextField varroaDroneBroodField;
    @FXML private TableView<VarroaTreatment> varroaTreatmentsTable;
    @FXML private TableColumn<VarroaTreatment, String> treatmentDateColumn;
    @FXML private TableColumn<VarroaTreatment, String> treatmentTypeColumn;
    @FXML private TableColumn<VarroaTreatment, String> treatmentEffectivenessColumn;
    @FXML private TableColumn<VarroaTreatment, String> treatmentDescColumn;
    @FXML private Button editTreatmentButton;
    @FXML private Button deleteTreatmentButton;
    @FXML private LineChart<Number, Number> varroaChart;
    @FXML private javafx.scene.chart.NumberAxis varroaChartXAxis;
    @FXML private VBox varroaResultsBox;
    @FXML private Label varroaStatusLabel;
    @FXML private TextArea varroaRecommendationArea;
    @FXML private Button varroaExportButton;

    // ========== QUEEN COMPONENTS ==========
    @FXML private DatePicker queenStartDate;
    @FXML private ComboBox<String> queenMethodCombo;
    @FXML private TableView<QueenMilestone> queenMilestonesTable;
    @FXML private TableColumn<QueenMilestone, String> queenDayColumn;
    @FXML private TableColumn<QueenMilestone, String> queenDateColumn;
    @FXML private TableColumn<QueenMilestone, String> queenNameColumn;
    @FXML private TableColumn<QueenMilestone, String> queenDescColumn;
    @FXML private Button queenExportButton;

    // ========== FEED COMPONENTS ==========
    @FXML private TextField feedCurrentStoresField;
    @FXML private TextField feedTargetStoresField;
    @FXML private ComboBox<String> feedTypeCombo;
    @FXML private VBox feedResultsBox;
    @FXML private Label feedNeededKgLabel;
    @FXML private Label feedNeededLitersLabel;
    @FXML private Label feedSugarLabel;
    @FXML private Label feedWaterLabel;
    @FXML private TextArea feedInstructionsArea;
    @FXML private GridPane feedIngredientsGrid;

    // ViewModel for calendar export
    private CalendarEventViewModel calendarViewModel;

    // Current calculation results
    private VarroaProjection currentVarroaProjection;
    private List<QueenMilestone> currentQueenMilestones;

    // Varroa parameters (configurable)
    private VarroaParameters varroaParameters = VarroaParameters.createDefault();

    // Varroa treatments list
    private ObservableList<VarroaTreatment> varroaTreatments = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupCalendarViewModel();
        setupVarroaCalculator();
        setupQueenCalculator();
        setupFeedCalculator();
    }

    /**
     * Initialize calendar ViewModel for export functionality.
     * Database is already initialized by Main.java, so we just create the ViewModel.
     */
    private void setupCalendarViewModel() {
        SchedulerProvider schedulerProvider = new DesktopSchedulerProvider();
        JdbcCalendarEventDao dao = new JdbcCalendarEventDao();
        CalendarEventRepository repository = new CalendarEventRepository(dao);
        calendarViewModel = new CalendarEventViewModel(repository, schedulerProvider);
    }

    // ==================== VARROA CALCULATOR ====================

    private void setupVarroaCalculator() {
        // Set default values
        varroaMeasurementDate.setValue(LocalDate.now());
        varroaProjectionDays.setItems(FXCollections.observableArrayList("30", "60", "90", "120", "150", "180"));
        varroaProjectionDays.setValue("90");
        varroaBroodPresent.setSelected(true);
        varroaDroneBroodField.setText("10");

        // Setup treatments table
        treatmentDateColumn.setCellValueFactory(cellData -> {
            long timestamp = cellData.getValue().getTreatmentDate();
            LocalDate date = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
            return new javafx.beans.property.SimpleStringProperty(date.toString());
        });

        treatmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("treatmentType"));

        treatmentEffectivenessColumn.setCellValueFactory(cellData -> {
            double effectiveness = cellData.getValue().getEffectivenessPercent();
            return new javafx.beans.property.SimpleStringProperty(String.format("%.0f%%", effectiveness));
        });

        treatmentDescColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        varroaTreatmentsTable.setItems(varroaTreatments);

        // Enable edit/delete buttons only when row is selected
        varroaTreatmentsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean hasSelection = newSelection != null;
            editTreatmentButton.setDisable(!hasSelection);
            deleteTreatmentButton.setDisable(!hasSelection);
        });
    }

    @FXML
    private void handleVarroaCalculate() {
        // Validate inputs
        String countText = varroaCountField.getText();
        if (!ValidationHelper.isValidInteger(countText)) {
            showError("Varroa kalkulačka", "Zadajte platný počet kliešťov (celé číslo).");
            return;
        }

        int count = Integer.parseInt(countText);
        if (count <= 0) {
            showError("Varroa kalkulačka", "Počet kliešťov musí byť väčší ako 0.");
            return;
        }

        LocalDate measurementDate = varroaMeasurementDate.getValue();
        if (measurementDate == null) {
            showError("Varroa kalkulačka", "Vyberte dátum merania.");
            return;
        }

        String daysText = varroaProjectionDays.getValue();
        if (daysText == null) {
            showError("Varroa kalkulačka", "Vyberte počet dní projekcie.");
            return;
        }

        int days = Integer.parseInt(daysText);
        boolean broodPresent = varroaBroodPresent.isSelected();

        // Validate drone brood percentage
        String droneBroodText = varroaDroneBroodField.getText();
        if (!ValidationHelper.isValidDouble(droneBroodText)) {
            showError("Varroa kalkulačka", "Zadajte platné percento trúdieho plodu (0-100).");
            return;
        }

        double droneBroodPercent = Double.parseDouble(droneBroodText);
        if (droneBroodPercent < 0 || droneBroodPercent > 100) {
            showError("Varroa kalkulačka", "Percento trúdieho plodu musí byť medzi 0 a 100.");
            return;
        }

        // Update parameters with drone brood percentage
        varroaParameters.setDroneBroodPercentage(droneBroodPercent / 100.0);

        // Convert date to timestamp
        long timestamp = DateTimeConverter.toTimestamp(measurementDate, 0, 0);

        // Calculate projection with parameters and treatments
        currentVarroaProjection = VarroaCalculator.project(count, timestamp, days, broodPresent,
                varroaParameters, new ArrayList<>(varroaTreatments));

        // Update chart
        updateVarroaChart(currentVarroaProjection);

        // Update results
        updateVarroaResults(currentVarroaProjection);

        // Enable export button
        varroaExportButton.setDisable(false);
    }

    private void updateVarroaChart(VarroaProjection projection) {
        varroaChart.getData().clear();

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Varroa projekcia");

        // Formatters for display
        java.time.format.DateTimeFormatter xAxisFormatter = java.time.format.DateTimeFormatter.ofPattern("d.M.");
        java.time.format.DateTimeFormatter tooltipFormatter = java.time.format.DateTimeFormatter.ofPattern("d. MMMM yyyy",
                new java.util.Locale("sk", "SK"));

        // Calculate tick interval based on total days for readable labels
        int totalDays = projection.getDataPoints().size() - 1;
        double tickInterval;
        if (totalDays <= 30) {
            tickInterval = 5;  // Show every 5 days
        } else if (totalDays <= 60) {
            tickInterval = 10; // Show every 10 days
        } else {
            tickInterval = 15; // Show every 15 days
        }

        // Store dates for tick formatter
        java.util.Map<Integer, String> dayToDateMap = new java.util.HashMap<>();

        for (VarroaProjection.DataPoint point : projection.getDataPoints()) {
            // Convert timestamp to formatted date
            LocalDate date = Instant.ofEpochMilli(point.getDate())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            String dateStr = date.format(xAxisFormatter);

            dayToDateMap.put(point.getDay(), dateStr);

            XYChart.Data<Number, Number> dataPoint = new XYChart.Data<>(point.getDay(), point.getCount());
            series.getData().add(dataPoint);

            // Add tooltip to each data point
            String tooltipDate = date.format(tooltipFormatter);
            String tooltipText = String.format("Dátum: %s\nPočet kliešťov: %d", tooltipDate, point.getCount());

            // Install tooltip after node is created
            dataPoint.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    Tooltip tooltip = new Tooltip(tooltipText);
                    tooltip.setShowDelay(javafx.util.Duration.millis(100));
                    tooltip.setShowDuration(javafx.util.Duration.INDEFINITE);
                    Tooltip.install(newNode, tooltip);
                }
            });
        }

        varroaChart.getData().add(series);

        // Add treatment markers as a separate series
        if (!varroaTreatments.isEmpty()) {
            XYChart.Series<Number, Number> treatmentSeries = new XYChart.Series<>();
            treatmentSeries.setName("Liečenia");

            long measurementTimestamp = projection.getDataPoints().get(0).getDate();

            for (VarroaTreatment treatment : varroaTreatments) {
                // Calculate day offset from measurement date
                long daysSinceMeasurement = (treatment.getTreatmentDate() - measurementTimestamp) / (1000 * 60 * 60 * 24);
                int treatmentDay = (int) daysSinceMeasurement;

                // Find corresponding mite count on that day
                Integer miteCount = null;
                for (VarroaProjection.DataPoint point : projection.getDataPoints()) {
                    if (point.getDay() == treatmentDay) {
                        miteCount = point.getCount();
                        break;
                    }
                }

                if (miteCount != null) {
                    XYChart.Data<Number, Number> marker = new XYChart.Data<>(treatmentDay, miteCount);
                    treatmentSeries.getData().add(marker);

                    // Add tooltip with treatment info
                    LocalDate treatmentDate = Instant.ofEpochMilli(treatment.getTreatmentDate())
                            .atZone(ZoneId.systemDefault()).toLocalDate();
                    String tooltipText = String.format("Liečenie: %s\nDátum: %s\nEfektivita: %.0f%%",
                            treatment.getTreatmentType(),
                            treatmentDate.format(java.time.format.DateTimeFormatter.ofPattern("d. MMMM yyyy",
                                    new java.util.Locale("sk", "SK"))),
                            treatment.getEffectivenessPercent());

                    // Install tooltip after node is created
                    marker.nodeProperty().addListener((obs, oldNode, newNode) -> {
                        if (newNode != null) {
                            Tooltip tooltip = new Tooltip(tooltipText);
                            tooltip.setShowDelay(javafx.util.Duration.millis(100));
                            tooltip.setShowDuration(javafx.util.Duration.INDEFINITE);
                            Tooltip.install(newNode, tooltip);

                            // Style the marker to be larger and orange
                            newNode.setStyle("-fx-background-color: #FF9800; -fx-background-radius: 8px; -fx-padding: 4px;");
                        }
                    });
                }
            }

            varroaChart.getData().add(treatmentSeries);
        }

        // Configure X axis with custom tick formatter and readable intervals
        varroaChartXAxis.setTickUnit(tickInterval);
        varroaChartXAxis.setTickLabelRotation(90); // Vertical text
        varroaChartXAxis.setMinorTickVisible(false);

        // Set custom tick label formatter to show dates instead of day numbers
        varroaChartXAxis.setTickLabelFormatter(new javafx.util.StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                int day = object.intValue();
                return dayToDateMap.getOrDefault(day, "");
            }

            @Override
            public Number fromString(String string) {
                return null;
            }
        });

        // Apply color based on status
        String color = getStatusColor(projection.getStatus());
        series.getNode().setStyle("-fx-stroke: " + color + "; -fx-stroke-width: 2px;");
    }

    private void updateVarroaResults(VarroaProjection projection) {
        varroaResultsBox.setVisible(true);

        // Set status with color
        String status = projection.getStatus();
        varroaStatusLabel.setText("Status: " + status);
        varroaStatusLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: " +
                getStatusColor(status) + ";");

        // Set recommendation
        varroaRecommendationArea.setText(projection.getRecommendation());
    }

    private String getStatusColor(String status) {
        switch (status) {
            case "OK":
                return "#4CAF50"; // green
            case "VAROVANIE":
                return "#FF9800"; // orange
            case "KRITICKÉ":
                return "#F44336"; // red
            default:
                return "#000000"; // black
        }
    }

    @FXML
    private void handleAddTreatment() {
        // Validate that we have measurement date and projection days
        LocalDate measurementDate = varroaMeasurementDate.getValue();
        if (measurementDate == null) {
            showError("Pridať liečenie", "Najprv zadajte dátum merania.");
            return;
        }

        String daysText = varroaProjectionDays.getValue();
        if (daysText == null) {
            showError("Pridať liečenie", "Najprv vyberte počet dní projekcie.");
            return;
        }

        int projectionDays = Integer.parseInt(daysText);

        VarroaTreatmentDialog dialog = new VarroaTreatmentDialog(null, measurementDate, projectionDays);
        dialog.showAndWait().ifPresent(treatment -> {
            varroaTreatments.add(treatment);

            // Automatically recalculate if we have valid inputs
            if (hasValidVarroaInputs()) {
                handleVarroaCalculate();
            }
        });
    }

    @FXML
    private void handleEditTreatment() {
        VarroaTreatment selected = varroaTreatmentsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        LocalDate measurementDate = varroaMeasurementDate.getValue();
        if (measurementDate == null) {
            showError("Upraviť liečenie", "Chyba: Dátum merania nie je nastavený.");
            return;
        }

        String daysText = varroaProjectionDays.getValue();
        if (daysText == null) {
            showError("Upraviť liečenie", "Chyba: Projekcia nie je nastavená.");
            return;
        }

        int projectionDays = Integer.parseInt(daysText);

        VarroaTreatmentDialog dialog = new VarroaTreatmentDialog(selected, measurementDate, projectionDays);
        dialog.showAndWait().ifPresent(updatedTreatment -> {
            // Replace in list
            int index = varroaTreatments.indexOf(selected);
            if (index >= 0) {
                varroaTreatments.set(index, updatedTreatment);

                // Automatically recalculate if we have valid inputs
                if (hasValidVarroaInputs()) {
                    handleVarroaCalculate();
                }
            }
        });
    }

    @FXML
    private void handleDeleteTreatment() {
        VarroaTreatment selected = varroaTreatmentsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        // Confirm deletion
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Zmazať liečenie");
        confirm.setHeaderText(null);
        confirm.setContentText("Naozaj chcete zmazať toto liečenie?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                varroaTreatments.remove(selected);

                // Automatically recalculate if we have valid inputs
                if (hasValidVarroaInputs()) {
                    handleVarroaCalculate();
                }
            }
        });
    }

    @FXML
    private void handleVarroaExport() {
        if (currentVarroaProjection == null) {
            showError("Export", "Najprv vypočítajte projekciu.");
            return;
        }

        if (varroaTreatments.isEmpty()) {
            showError("Export", "Pridajte aspoň jedno liečenie do plánu.");
            return;
        }

        // Create calendar event for each treatment
        int count = 0;
        for (VarroaTreatment treatment : varroaTreatments) {
            CalendarEvent event = new CalendarEvent();
            event.setId(UUID.randomUUID().toString());
            event.setTitle("Varroa liečenie: " + treatment.getTreatmentType());

            // Build description with effectiveness and recommendation
            StringBuilder description = new StringBuilder();
            description.append("Typ: ").append(treatment.getTreatmentType()).append("\n");
            description.append("Efektivita: ").append(String.format("%.0f%%", treatment.getEffectivenessPercent())).append("\n");

            if (treatment.getDescription() != null && !treatment.getDescription().trim().isEmpty()) {
                description.append("\nPoznámka: ").append(treatment.getDescription()).append("\n");
            }

            description.append("\n--- Projekcia Varroa ---\n");
            description.append("Status: ").append(currentVarroaProjection.getStatus()).append("\n");
            description.append(currentVarroaProjection.getRecommendation());

            event.setDescription(description.toString());
            event.setEventDate(treatment.getTreatmentDate());
            event.setEventType("TREATMENT");
            event.setCreatedAt(System.currentTimeMillis());

            calendarViewModel.createEvent(event);
            count++;
        }

        showInfo("Export úspešný", count + " udalostí bolo vytvorených v kalendári.");
    }

    @FXML
    private void handleVarroaSettings() {
        VarroaSettingsDialog dialog = new VarroaSettingsDialog(varroaParameters);
        dialog.showAndWait().ifPresent(params -> {
            varroaParameters = params;
            // Update drone brood field from parameters
            varroaDroneBroodField.setText(String.format("%.0f", params.getDroneBroodPercentage() * 100));
            showInfo("Nastavenia", "Parametre boli aktualizované.");

            // Automatically recalculate if we have input data
            if (hasValidVarroaInputs()) {
                handleVarroaCalculate();
            }
        });
    }

    private boolean hasValidVarroaInputs() {
        return !varroaCountField.getText().trim().isEmpty() &&
               varroaMeasurementDate.getValue() != null &&
               varroaProjectionDays.getValue() != null &&
               !varroaDroneBroodField.getText().trim().isEmpty();
    }

    @FXML
    private void handleVarroaClear() {
        varroaCountField.clear();
        varroaMeasurementDate.setValue(LocalDate.now());
        varroaBroodPresent.setSelected(true);
        varroaProjectionDays.setValue("30");
        varroaDroneBroodField.setText(String.format("%.0f", varroaParameters.getDroneBroodPercentage() * 100));
        varroaTreatments.clear();
        varroaChart.getData().clear();
        varroaResultsBox.setVisible(false);
        varroaExportButton.setDisable(true);
        currentVarroaProjection = null;
    }

    // ==================== QUEEN REARING CALCULATOR ====================

    private void setupQueenCalculator() {
        // Set default values
        queenStartDate.setValue(LocalDate.now());
        queenMethodCombo.setItems(FXCollections.observableArrayList("Štandardná", "Doolittle", "Jenter"));
        queenMethodCombo.setValue("Štandardná");

        // Setup table columns
        queenDayColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFormattedDayOffset()));

        queenDateColumn.setCellValueFactory(cellData -> {
            long timestamp = cellData.getValue().getDate();
            LocalDate date = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
            return new javafx.beans.property.SimpleStringProperty(date.toString());
        });

        queenNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        queenDescColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Apply row coloring
        queenMilestonesTable.setRowFactory(tv -> new TableRow<QueenMilestone>() {
            @Override
            protected void updateItem(QueenMilestone item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    setStyle("-fx-background-color: " + item.getColor() + ";");
                }
            }
        });
    }

    @FXML
    private void handleQueenCalculate() {
        LocalDate startDate = queenStartDate.getValue();
        if (startDate == null) {
            showError("Queen Timeline", "Vyberte dátum začiatku.");
            return;
        }

        String method = queenMethodCombo.getValue();
        if (method == null) {
            method = "Štandardná";
        }

        // Convert to timestamp
        long timestamp = DateTimeConverter.toTimestamp(startDate, 0, 0);

        // Calculate milestones
        currentQueenMilestones = QueenRearingCalculator.calculateMilestones(timestamp, method);

        // Update table
        queenMilestonesTable.setItems(FXCollections.observableArrayList(currentQueenMilestones));

        // Enable export button
        queenExportButton.setDisable(false);
    }

    @FXML
    private void handleQueenExport() {
        if (currentQueenMilestones == null || currentQueenMilestones.isEmpty()) {
            showError("Export", "Najprv vypočítajte míľniky.");
            return;
        }

        // Create calendar events for all milestones
        int count = 0;
        for (QueenMilestone milestone : currentQueenMilestones) {
            CalendarEvent event = new CalendarEvent();
            event.setId(UUID.randomUUID().toString());
            event.setTitle("Matka: " + milestone.getName());
            event.setDescription(milestone.getDescription() + " (" + milestone.getFormattedDayOffset() + ")");
            event.setEventDate(milestone.getDate());
            event.setEventType("REMINDER");
            event.setCreatedAt(System.currentTimeMillis());

            calendarViewModel.createEvent(event);
            count++;
        }

        showInfo("Export úspešný", count + " udalostí bolo vytvorených v kalendári.");
    }

    @FXML
    private void handleQueenClear() {
        queenStartDate.setValue(LocalDate.now());
        queenMethodCombo.setValue("Štandardná");
        queenMilestonesTable.getItems().clear();
        queenExportButton.setDisable(true);
        currentQueenMilestones = null;
    }

    // ==================== FEED CALCULATOR ====================

    private void setupFeedCalculator() {
        // Set default values
        feedTypeCombo.setItems(FXCollections.observableArrayList("Sirup 1:1", "Sirup 3:2", "Fondant"));
        feedTypeCombo.setValue("Sirup 1:1");

        // Add real-time calculation listeners
        feedCurrentStoresField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (shouldAutoCalculate()) {
                handleFeedCalculate();
            }
        });

        feedTargetStoresField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (shouldAutoCalculate()) {
                handleFeedCalculate();
            }
        });

        feedTypeCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (shouldAutoCalculate()) {
                handleFeedCalculate();
            }
        });
    }

    private boolean shouldAutoCalculate() {
        String currentText = feedCurrentStoresField.getText();
        String targetText = feedTargetStoresField.getText();
        String feedType = feedTypeCombo.getValue();

        return ValidationHelper.isValidDouble(currentText) &&
                ValidationHelper.isValidDouble(targetText) &&
                feedType != null;
    }

    @FXML
    private void handleFeedCalculate() {
        // Validate inputs
        String currentText = feedCurrentStoresField.getText();
        if (!ValidationHelper.isValidDouble(currentText)) {
            feedResultsBox.setVisible(false);
            return;
        }

        String targetText = feedTargetStoresField.getText();
        if (!ValidationHelper.isValidDouble(targetText)) {
            feedResultsBox.setVisible(false);
            return;
        }

        String feedType = feedTypeCombo.getValue();
        if (feedType == null) {
            showError("Feed kalkulačka", "Vyberte typ krmiva.");
            feedResultsBox.setVisible(false);
            return;
        }

        double currentStores = Double.parseDouble(currentText);
        double targetStores = Double.parseDouble(targetText);

        if (currentStores < 0 || targetStores < 0) {
            showError("Feed kalkulačka", "Hodnoty nesmú byť záporné.");
            feedResultsBox.setVisible(false);
            return;
        }

        // Calculate
        FeedResult result = FeedCalculator.calculate(currentStores, targetStores, feedType);

        // Update results
        updateFeedResults(result, feedType);
    }

    private void updateFeedResults(FeedResult result, String feedType) {
        feedResultsBox.setVisible(true);

        feedNeededKgLabel.setText(FeedCalculator.formatAmount(result.getNeededKg()) + " kg");
        feedNeededLitersLabel.setText(FeedCalculator.formatAmount(result.getNeededLiters()) + " L");

        // Show/hide ingredients based on feed type
        if (feedType.equals(FeedCalculator.FONDANT)) {
            feedIngredientsGrid.setVisible(false);
        } else {
            feedIngredientsGrid.setVisible(true);
            feedSugarLabel.setText(FeedCalculator.formatAmount(result.getSugarKg()) + " kg");
            feedWaterLabel.setText(FeedCalculator.formatAmount(result.getWaterLiters()) + " L");
        }

        feedInstructionsArea.setText(result.getInstructions());
    }

    @FXML
    private void handleFeedClear() {
        feedCurrentStoresField.clear();
        feedTargetStoresField.clear();
        feedTypeCombo.setValue("Sirup 1:1");
        feedResultsBox.setVisible(false);
    }

    // ==================== UTILITY METHODS ====================

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
