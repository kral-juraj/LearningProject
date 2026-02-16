package com.beekeeper.desktop.dialog;

import com.beekeeper.desktop.calculator.TreatmentType;
import com.beekeeper.desktop.calculator.VarroaTreatment;
import com.beekeeper.shared.i18n.TranslationManager;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

/**
 * Dialog for adding or editing a Varroa treatment.
 */
public class VarroaTreatmentDialog extends Dialog<VarroaTreatment> {

    private final DatePicker treatmentDatePicker;
    private final ComboBox<TreatmentType> treatmentTypeCombo;
    private final TextField effectivenessField;
    private final TextArea descriptionArea;
    private final Label effectivenessLabel;
    private final TranslationManager tm;

    public VarroaTreatmentDialog(VarroaTreatment existingTreatment, LocalDate measurementDate, int projectionDays) {
        tm = TranslationManager.getInstance();
        setTitle(existingTreatment == null ? tm.get("dialog.add_treatment.title") : tm.get("dialog.edit_treatment.title"));
        setHeaderText(existingTreatment == null ? tm.get("dialog.treatment.header_add") : tm.get("dialog.treatment.header_edit"));

        // Layout
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Date picker
        Label dateLabel = new Label(tm.get("label.treatment_date"));
        treatmentDatePicker = new DatePicker();
        treatmentDatePicker.setPromptText(tm.get("prompt.select_date"));

        // Set min/max dates based on projection period
        LocalDate minDate = measurementDate;
        LocalDate maxDate = measurementDate.plusDays(projectionDays);
        treatmentDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(minDate) || date.isAfter(maxDate));
            }
        });

        // Treatment type
        Label typeLabel = new Label(tm.get("label.treatment_type"));
        treatmentTypeCombo = new ComboBox<>();
        treatmentTypeCombo.getItems().addAll(TreatmentType.values());
        treatmentTypeCombo.setPromptText(tm.get("prompt.select_type"));
        treatmentTypeCombo.setMaxWidth(Double.MAX_VALUE);

        // Help button for treatment type
        Button typeHelpButton = new Button("?");
        typeHelpButton.setStyle("-fx-font-weight: bold; -fx-background-color: #2196F3; -fx-text-fill: white; " +
                "-fx-background-radius: 15; -fx-min-width: 30; -fx-max-width: 30; -fx-min-height: 30; -fx-max-height: 30;");
        typeHelpButton.setOnAction(e -> {
            TreatmentType selected = treatmentTypeCombo.getValue();
            if (selected != null) {
                showTreatmentInfoDialog(selected);
            } else {
                showInfoAlert(tm.get("info.title"), tm.get("info.select_treatment_first"));
            }
        });

        // Effectiveness field
        Label effectivenessFieldLabel = new Label(tm.get("label.effectiveness"));
        effectivenessField = new TextField();
        effectivenessField.setPromptText("0-100");
        effectivenessLabel = new Label();
        effectivenessLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 10px;");

        // Description
        Label descLabel = new Label(tm.get("label.note"));
        descriptionArea = new TextArea();
        descriptionArea.setPromptText(tm.get("prompt.optional_note"));
        descriptionArea.setPrefRowCount(3);
        descriptionArea.setWrapText(true);

        // Add to grid
        grid.add(dateLabel, 0, 0);
        grid.add(treatmentDatePicker, 1, 0);
        grid.add(typeLabel, 0, 1);

        HBox typeBox = new HBox(10, treatmentTypeCombo, typeHelpButton);
        typeBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        grid.add(typeBox, 1, 1);

        grid.add(effectivenessFieldLabel, 0, 2);
        grid.add(effectivenessField, 1, 2);
        grid.add(effectivenessLabel, 1, 3);
        grid.add(descLabel, 0, 4);
        grid.add(descriptionArea, 1, 4);

        content.getChildren().add(grid);
        getDialogPane().setContent(content);

        // Buttons
        ButtonType saveButton = new ButtonType(tm.get("button.save"), ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        // Auto-fill effectiveness when treatment type selected
        treatmentTypeCombo.setOnAction(e -> {
            TreatmentType selected = treatmentTypeCombo.getValue();
            if (selected != null) {
                double defaultEffectiveness = selected.getDefaultEffectivenessPercent();
                effectivenessField.setText(String.format("%.0f", defaultEffectiveness));
                effectivenessLabel.setText(selected.getDescription());
            }
        });

        // Update description label when effectiveness changes
        effectivenessField.textProperty().addListener((obs, old, newVal) -> {
            TreatmentType selected = treatmentTypeCombo.getValue();
            if (selected != null && !newVal.trim().isEmpty()) {
                try {
                    double value = Double.parseDouble(newVal);
                    if (value >= 0 && value <= 100) {
                        effectivenessLabel.setText(selected.getDescription() +
                            String.format(" (" + tm.get("treatment.eliminates_mites") + ")", value));
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        });

        // Populate fields for edit mode
        if (existingTreatment != null) {
            LocalDate treatmentDate = LocalDate.ofInstant(
                java.time.Instant.ofEpochMilli(existingTreatment.getTreatmentDate()),
                ZoneId.systemDefault()
            );
            treatmentDatePicker.setValue(treatmentDate);

            TreatmentType type = TreatmentType.fromDisplayName(existingTreatment.getTreatmentType());
            treatmentTypeCombo.setValue(type);

            effectivenessField.setText(String.format("%.0f", existingTreatment.getEffectivenessPercent()));
            descriptionArea.setText(existingTreatment.getDescription());
        }

        // Enable save button only when required fields are filled
        Button saveBtn = (Button) getDialogPane().lookupButton(saveButton);
        saveBtn.setDisable(true);

        Runnable validateFields = () -> {
            boolean valid = treatmentDatePicker.getValue() != null &&
                           treatmentTypeCombo.getValue() != null &&
                           !effectivenessField.getText().trim().isEmpty() &&
                           isValidEffectiveness(effectivenessField.getText());
            saveBtn.setDisable(!valid);
        };

        treatmentDatePicker.valueProperty().addListener((obs, old, newVal) -> validateFields.run());
        treatmentTypeCombo.valueProperty().addListener((obs, old, newVal) -> validateFields.run());
        effectivenessField.textProperty().addListener((obs, old, newVal) -> validateFields.run());

        // Result converter
        setResultConverter(buttonType -> {
            if (buttonType == saveButton) {
                try {
                    VarroaTreatment treatment = existingTreatment != null ?
                        existingTreatment : new VarroaTreatment();

                    if (treatment.getId() == null || treatment.getId().isEmpty()) {
                        treatment.setId(UUID.randomUUID().toString());
                    }

                    LocalDate date = treatmentDatePicker.getValue();
                    long timestamp = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
                    treatment.setTreatmentDate(timestamp);

                    TreatmentType type = treatmentTypeCombo.getValue();
                    treatment.setTreatmentType(type.getDisplayName());

                    double effectiveness = Double.parseDouble(effectivenessField.getText()) / 100.0;
                    treatment.setEffectiveness(effectiveness);

                    treatment.setDescription(descriptionArea.getText());

                    return treatment;
                } catch (Exception e) {
                    showErrorAlert(tm.get("error.saving", e.getMessage()));
                    return null;
                }
            }
            return null;
        });
    }

    private boolean isValidEffectiveness(String text) {
        try {
            double value = Double.parseDouble(text);
            return value >= 0 && value <= 100;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(tm.get("error.title"));
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showTreatmentInfoDialog(TreatmentType type) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(tm.get("dialog.treatment_info.title", type.getDisplayName()));
        alert.setHeaderText(type.getDisplayName());

        // Create TextArea for scrollable content
        TextArea textArea = new TextArea(type.getDetailedInfo());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefRowCount(20);
        textArea.setPrefColumnCount(60);

        alert.getDialogPane().setContent(textArea);
        alert.getDialogPane().setPrefWidth(700);
        alert.showAndWait();
    }
}
