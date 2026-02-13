package com.beekeeper.desktop.dialog;

import com.beekeeper.desktop.util.DateTimeConverter;
import com.beekeeper.desktop.util.EnumHelper;
import com.beekeeper.desktop.util.ValidationHelper;
import com.beekeeper.shared.entity.Feeding;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Dialog for creating or editing a Feeding.
 * Contains all 9 fields of Feeding entity.
 */
public class FeedingDialog extends Dialog<Feeding> {

    private DatePicker datePicker;
    private TextField hourField;
    private TextField minuteField;
    private ComboBox<String> feedTypeCombo;
    private TextField weightBeforeField;
    private TextField weightAfterField;
    private TextField amountField;
    private TextArea notesArea;

    private Feeding existingFeeding;
    private String hiveId;

    public FeedingDialog(Feeding feeding, String hiveId) {
        this.existingFeeding = feeding;
        this.hiveId = hiveId;

        initDialog();
        setupAutoCalculation();
        populateFields();
    }

    private void initDialog() {
        setTitle(existingFeeding == null ? "Nové krmenie" : "Upraviť krmenie");
        initModality(Modality.APPLICATION_MODAL);
        setResizable(true);

        ButtonType saveButtonType = new ButtonType("Uložiť", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Zrušiť", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/feeding_dialog.fxml"));
            GridPane gridPane = loader.load();
            getDialogPane().setContent(gridPane);

            // Get references to UI components
            datePicker = (DatePicker) gridPane.lookup("#datePicker");
            hourField = (TextField) gridPane.lookup("#hourField");
            minuteField = (TextField) gridPane.lookup("#minuteField");
            feedTypeCombo = (ComboBox<String>) gridPane.lookup("#feedTypeCombo");
            weightBeforeField = (TextField) gridPane.lookup("#weightBeforeField");
            weightAfterField = (TextField) gridPane.lookup("#weightAfterField");
            amountField = (TextField) gridPane.lookup("#amountField");
            notesArea = (TextArea) gridPane.lookup("#notesArea");

            // Populate feed types
            feedTypeCombo.setItems(EnumHelper.getFeedTypes());

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Chyba pri načítaní formulára: " + e.getMessage());
            alert.showAndWait();
        }

        // Handle result conversion
        Node saveButton = getDialogPane().lookupButton(saveButtonType);
        saveButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            if (!validateInput()) {
                event.consume();
            }
        });

        setResultConverter(buttonType -> {
            if (buttonType == saveButtonType) {
                return createFeedingFromInput();
            }
            return null;
        });
    }

    private void setupAutoCalculation() {
        // Auto-calculate amount when weight before/after change
        weightBeforeField.textProperty().addListener((obs, oldVal, newVal) -> calculateAmount());
        weightAfterField.textProperty().addListener((obs, oldVal, newVal) -> calculateAmount());
    }

    private void calculateAmount() {
        if (ValidationHelper.isValidDouble(weightBeforeField.getText()) &&
            ValidationHelper.isValidDouble(weightAfterField.getText())) {

            double weightBefore = ValidationHelper.parseDouble(weightBeforeField.getText());
            double weightAfter = ValidationHelper.parseDouble(weightAfterField.getText());

            if (weightBefore > 0 && weightAfter > 0) {
                double amount = weightAfter - weightBefore;
                if (amount > 0) {
                    amountField.setText(String.format("%.2f", amount));
                }
            }
        }
    }

    private void populateFields() {
        if (existingFeeding != null) {
            // Date and time
            LocalDate date = DateTimeConverter.toLocalDate(existingFeeding.getFeedingDate());
            datePicker.setValue(date);
            hourField.setText(String.valueOf(DateTimeConverter.extractHour(existingFeeding.getFeedingDate())));
            minuteField.setText(String.valueOf(DateTimeConverter.extractMinute(existingFeeding.getFeedingDate())));

            // Feed type
            feedTypeCombo.getSelectionModel().select(existingFeeding.getFeedType());

            // Weights and amount
            if (existingFeeding.getWeightBefore() > 0) {
                weightBeforeField.setText(String.valueOf(existingFeeding.getWeightBefore()));
            }
            if (existingFeeding.getWeightAfter() > 0) {
                weightAfterField.setText(String.valueOf(existingFeeding.getWeightAfter()));
            }
            if (existingFeeding.getAmountKg() > 0) {
                amountField.setText(String.valueOf(existingFeeding.getAmountKg()));
            }

            // Notes
            notesArea.setText(existingFeeding.getNotes());
        } else {
            // Default values for new feeding
            datePicker.setValue(LocalDate.now());
            hourField.setText("12");
            minuteField.setText("0");
            feedTypeCombo.getSelectionModel().selectFirst();
        }
    }

    private boolean validateInput() {
        if (datePicker.getValue() == null) {
            ValidationHelper.showValidationError("Dátum je povinný");
            return false;
        }

        if (!ValidationHelper.isValidInteger(hourField.getText())) {
            ValidationHelper.showValidationError("Hodina musí byť číslo");
            return false;
        }

        if (!ValidationHelper.isValidInteger(minuteField.getText())) {
            ValidationHelper.showValidationError("Minúta musí byť číslo");
            return false;
        }

        int hour = ValidationHelper.parseInt(hourField.getText());
        int minute = ValidationHelper.parseInt(minuteField.getText());

        if (!ValidationHelper.isInRange(hour, 0, 23)) {
            ValidationHelper.showValidationError("Hodina musí byť medzi 0 a 23");
            return false;
        }

        if (!ValidationHelper.isInRange(minute, 0, 59)) {
            ValidationHelper.showValidationError("Minúta musí byť medzi 0 a 59");
            return false;
        }

        if (feedTypeCombo.getSelectionModel().getSelectedItem() == null) {
            ValidationHelper.showValidationError("Typ krmiva je povinný");
            return false;
        }

        if (!ValidationHelper.isValidDouble(amountField.getText())) {
            ValidationHelper.showValidationError("Množstvo musí byť číslo");
            return false;
        }

        double amount = ValidationHelper.parseDouble(amountField.getText());
        if (amount <= 0) {
            ValidationHelper.showValidationError("Množstvo musí byť väčšie ako 0");
            return false;
        }

        // Validate weight fields if provided
        if (!weightBeforeField.getText().trim().isEmpty() && !ValidationHelper.isValidDouble(weightBeforeField.getText())) {
            ValidationHelper.showValidationError("Hmotnosť pred musí byť číslo");
            return false;
        }

        if (!weightAfterField.getText().trim().isEmpty() && !ValidationHelper.isValidDouble(weightAfterField.getText())) {
            ValidationHelper.showValidationError("Hmotnosť po musí byť číslo");
            return false;
        }

        return true;
    }

    private Feeding createFeedingFromInput() {
        Feeding feeding = existingFeeding != null ? existingFeeding : new Feeding();

        feeding.setHiveId(hiveId);

        // Date and time
        LocalDate date = datePicker.getValue();
        int hour = ValidationHelper.parseInt(hourField.getText());
        int minute = ValidationHelper.parseInt(minuteField.getText());
        feeding.setFeedingDate(DateTimeConverter.toTimestamp(date, hour, minute));

        // Feed type
        feeding.setFeedType(feedTypeCombo.getSelectionModel().getSelectedItem());

        // Weights
        feeding.setWeightBefore(ValidationHelper.parseDouble(weightBeforeField.getText()));
        feeding.setWeightAfter(ValidationHelper.parseDouble(weightAfterField.getText()));

        // Amount
        feeding.setAmountKg(ValidationHelper.parseDouble(amountField.getText()));

        // Notes
        feeding.setNotes(notesArea.getText() != null ? notesArea.getText().trim() : "");

        return feeding;
    }
}
