package com.beekeeper.desktop.dialog;

import com.beekeeper.desktop.calculator.VarroaParameters;
import com.beekeeper.desktop.util.ValidationHelper;
import com.beekeeper.shared.i18n.TranslationManager;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Dialog for configuring Varroa calculator parameters.
 * Allows users to adjust biological parameters and reset to defaults.
 */
public class VarroaSettingsDialog extends Dialog<VarroaParameters> {

    private VarroaParameters parameters;
    private VarroaParameters defaultParameters;
    private TranslationManager tm;

    // Input fields
    private TextField workerOffspringField;
    private TextField droneOffspringField;
    private TextField workerFailureField;
    private TextField droneFailureField;
    private TextField workerCycleLengthField;
    private TextField droneCycleLengthField;
    private TextField phoreticPhaseField;
    private TextField dronePreferenceField;
    private TextField droneBroodPercentageField;
    private TextField mortalityPerCycleField;
    private TextField phoreticMortalityField;
    private TextField cellInvasionField;

    public VarroaSettingsDialog(VarroaParameters currentParameters) {
        this.parameters = new VarroaParameters(currentParameters);
        this.defaultParameters = VarroaParameters.createDefault();
        this.tm = TranslationManager.getInstance();

        setTitle(tm.get("dialog.varroa_settings.title"));
        setHeaderText(tm.get("dialog.varroa_settings.header"));

        // Create form
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        // Section 1: Reproduction rates
        content.getChildren().add(createSectionLabel(tm.get("varroa.section.reproduction")));
        GridPane reproductionGrid = createGrid();
        int row = 0;

        addFieldToGrid(reproductionGrid, row++, tm.get("varroa.label.worker_offspring"),
                workerOffspringField = createTextField(parameters.getWorkerOffspring()),
                tm.get("varroa.tooltip.worker_offspring"));

        addFieldToGrid(reproductionGrid, row++, tm.get("varroa.label.drone_offspring"),
                droneOffspringField = createTextField(parameters.getDroneOffspring()),
                tm.get("varroa.tooltip.drone_offspring"));

        addFieldToGrid(reproductionGrid, row++, tm.get("varroa.label.worker_failure"),
                workerFailureField = createTextField(parameters.getWorkerFailureRate() * 100),
                tm.get("varroa.tooltip.worker_failure"));

        addFieldToGrid(reproductionGrid, row++, tm.get("varroa.label.drone_failure"),
                droneFailureField = createTextField(parameters.getDroneFailureRate() * 100),
                tm.get("varroa.tooltip.drone_failure"));

        content.getChildren().add(reproductionGrid);

        // Section 2: Life cycle
        content.getChildren().add(createSectionLabel(tm.get("varroa.section.lifecycle")));
        GridPane cycleGrid = createGrid();
        row = 0;

        addFieldToGrid(cycleGrid, row++, tm.get("varroa.label.worker_cycle"),
                workerCycleLengthField = createTextField(parameters.getWorkerCycleLength()),
                tm.get("varroa.tooltip.worker_cycle"));

        addFieldToGrid(cycleGrid, row++, tm.get("varroa.label.drone_cycle"),
                droneCycleLengthField = createTextField(parameters.getDroneCycleLength()),
                tm.get("varroa.tooltip.drone_cycle"));

        addFieldToGrid(cycleGrid, row++, tm.get("varroa.label.phoretic_phase"),
                phoreticPhaseField = createTextField(parameters.getPhoreticPhaseDays()),
                tm.get("varroa.tooltip.phoretic_phase"));

        content.getChildren().add(cycleGrid);

        // Section 3: Behavior
        content.getChildren().add(createSectionLabel(tm.get("varroa.section.behavior")));
        GridPane behaviorGrid = createGrid();
        row = 0;

        addFieldToGrid(behaviorGrid, row++, tm.get("varroa.label.drone_preference"),
                dronePreferenceField = createTextField(parameters.getDronePreference()),
                tm.get("varroa.tooltip.drone_preference"));

        addFieldToGrid(behaviorGrid, row++, tm.get("varroa.label.drone_brood_percent"),
                droneBroodPercentageField = createTextField(parameters.getDroneBroodPercentage() * 100),
                tm.get("varroa.tooltip.drone_brood_percent"));

        addFieldToGrid(behaviorGrid, row++, tm.get("varroa.label.cell_invasion"),
                cellInvasionField = createTextField(parameters.getCellInvasionRate() * 100),
                tm.get("varroa.tooltip.cell_invasion"));

        content.getChildren().add(behaviorGrid);

        // Section 4: Mortality
        content.getChildren().add(createSectionLabel(tm.get("varroa.section.mortality")));
        GridPane mortalityGrid = createGrid();
        row = 0;

        addFieldToGrid(mortalityGrid, row++, tm.get("varroa.label.mortality_per_cycle"),
                mortalityPerCycleField = createTextField(parameters.getMortalityPerCycle() * 100),
                tm.get("varroa.tooltip.mortality_per_cycle"));

        addFieldToGrid(mortalityGrid, row++, tm.get("varroa.label.phoretic_mortality"),
                phoreticMortalityField = createTextField(parameters.getPhoreticMortalityPerDay() * 100),
                tm.get("varroa.tooltip.phoretic_mortality"));

        content.getChildren().add(mortalityGrid);

        // Scroll pane for long content
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(500);

        getDialogPane().setContent(scrollPane);

        // Buttons
        ButtonType resetButtonType = new ButtonType(tm.get("button.reset_defaults"), ButtonBar.ButtonData.LEFT);
        ButtonType okButtonType = ButtonType.OK;
        ButtonType cancelButtonType = ButtonType.CANCEL;

        getDialogPane().getButtonTypes().addAll(resetButtonType, okButtonType, cancelButtonType);

        // Reset button handler
        Button resetButton = (Button) getDialogPane().lookupButton(resetButtonType);
        resetButton.setOnAction(e -> {
            resetToDefaults();
            e.consume();
        });

        // Result converter
        setResultConverter(buttonType -> {
            if (buttonType == okButtonType) {
                if (validateAndApply()) {
                    return parameters;
                }
            }
            return null;
        });
    }

    private GridPane createGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        return grid;
    }

    private Label createSectionLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        return label;
    }

    private TextField createTextField(double value) {
        TextField field = new TextField(String.format("%.2f", value));
        field.setPrefWidth(100);
        return field;
    }

    private void addFieldToGrid(GridPane grid, int row, String labelText,
                                 TextField field, String tooltipText) {
        Label label = new Label(labelText);

        // Create help icon with tooltip
        Label helpIcon = new Label("?");
        helpIcon.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; " +
                "-fx-background-radius: 10; -fx-padding: 2 6 2 6; " +
                "-fx-font-size: 11px; -fx-cursor: hand;");
        Tooltip tooltip = new Tooltip(tooltipText);
        tooltip.setWrapText(true);
        tooltip.setMaxWidth(300);
        tooltip.setShowDuration(javafx.util.Duration.INDEFINITE); // Stay visible until mouse leaves
        helpIcon.setTooltip(tooltip);

        // Add to grid: label, field, help icon
        grid.add(label, 0, row);
        grid.add(field, 1, row);
        grid.add(helpIcon, 2, row);
    }

    private void resetToDefaults() {
        workerOffspringField.setText(String.format("%.2f", defaultParameters.getWorkerOffspring()));
        droneOffspringField.setText(String.format("%.2f", defaultParameters.getDroneOffspring()));
        workerFailureField.setText(String.format("%.2f", defaultParameters.getWorkerFailureRate() * 100));
        droneFailureField.setText(String.format("%.2f", defaultParameters.getDroneFailureRate() * 100));
        workerCycleLengthField.setText(String.format("%.2f", defaultParameters.getWorkerCycleLength()));
        droneCycleLengthField.setText(String.format("%.2f", defaultParameters.getDroneCycleLength()));
        phoreticPhaseField.setText(String.format("%.2f", defaultParameters.getPhoreticPhaseDays()));
        dronePreferenceField.setText(String.format("%.2f", defaultParameters.getDronePreference()));
        droneBroodPercentageField.setText(String.format("%.2f", defaultParameters.getDroneBroodPercentage() * 100));
        mortalityPerCycleField.setText(String.format("%.2f", defaultParameters.getMortalityPerCycle() * 100));
        phoreticMortalityField.setText(String.format("%.2f", defaultParameters.getPhoreticMortalityPerDay() * 100));
        cellInvasionField.setText(String.format("%.2f", defaultParameters.getCellInvasionRate() * 100));
    }

    private boolean validateAndApply() {
        try {
            // Validate and parse all fields
            double workerOffspring = parseDouble(workerOffspringField, "Potomstvo - robotnice");
            double droneOffspring = parseDouble(droneOffspringField, "Potomstvo - trúdy");
            double workerFailure = parseDouble(workerFailureField, "Zlyhanie - robotnice") / 100.0;
            double droneFailure = parseDouble(droneFailureField, "Zlyhanie - trúdy") / 100.0;
            double workerCycleLength = parseDouble(workerCycleLengthField, "Cyklus - robotnice");
            double droneCycleLength = parseDouble(droneCycleLengthField, "Cyklus - trúdy");
            double phoreticPhase = parseDouble(phoreticPhaseField, "Foretická fáza");
            double dronePreference = parseDouble(dronePreferenceField, "Preferencia trúdov");
            double droneBroodPercentage = parseDouble(droneBroodPercentageField, "Trúdí plod") / 100.0;
            double mortalityPerCycle = parseDouble(mortalityPerCycleField, "Úmrtnosť na cyklus") / 100.0;
            double phoreticMortality = parseDouble(phoreticMortalityField, "Foretická úmrtnosť") / 100.0;
            double cellInvasion = parseDouble(cellInvasionField, "Miera invázie") / 100.0;

            // Range validation
            if (workerOffspring < 0 || workerOffspring > 5) {
                showError("Potomstvo - robotnice musí byť medzi 0 a 5");
                return false;
            }
            if (droneOffspring < 0 || droneOffspring > 5) {
                showError("Potomstvo - trúdy musí byť medzi 0 a 5");
                return false;
            }
            if (workerFailure < 0 || workerFailure > 1) {
                showError("Zlyhanie musí byť medzi 0% a 100%");
                return false;
            }
            if (droneFailure < 0 || droneFailure > 1) {
                showError("Zlyhanie musí byť medzi 0% a 100%");
                return false;
            }
            if (workerCycleLength < 1 || workerCycleLength > 30) {
                showError("Dĺžka cyklu musí byť medzi 1 a 30 dní");
                return false;
            }
            if (droneCycleLength < 1 || droneCycleLength > 30) {
                showError("Dĺžka cyklu musí byť medzi 1 a 30 dní");
                return false;
            }

            // Apply values
            parameters.setWorkerOffspring(workerOffspring);
            parameters.setDroneOffspring(droneOffspring);
            parameters.setWorkerFailureRate(workerFailure);
            parameters.setDroneFailureRate(droneFailure);
            parameters.setWorkerCycleLength(workerCycleLength);
            parameters.setDroneCycleLength(droneCycleLength);
            parameters.setPhoreticPhaseDays(phoreticPhase);
            parameters.setDronePreference(dronePreference);
            parameters.setDroneBroodPercentage(droneBroodPercentage);
            parameters.setMortalityPerCycle(mortalityPerCycle);
            parameters.setPhoreticMortalityPerDay(phoreticMortality);
            parameters.setCellInvasionRate(cellInvasion);

            return true;

        } catch (Exception e) {
            showError("Chyba pri validácii: " + e.getMessage());
            return false;
        }
    }

    private double parseDouble(TextField field, String fieldName) throws Exception {
        String text = field.getText().trim().replace(',', '.');
        if (!ValidationHelper.isValidDouble(text)) {
            throw new Exception(fieldName + " musí byť platné číslo");
        }
        return Double.parseDouble(text);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(tm.get("error.validation_title"));
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
