package com.beekeeper.desktop.dialog;

import com.beekeeper.desktop.i18n.I18nResourceBundle;
import com.beekeeper.desktop.util.DateTimeConverter;
import com.beekeeper.desktop.util.EnumHelper;
import com.beekeeper.desktop.util.ValidationHelper;
import com.beekeeper.shared.entity.Feeding;
import com.beekeeper.shared.i18n.TranslationManager;
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
    private TranslationManager tm;

    public FeedingDialog(Feeding feeding, String hiveId) {
        this.existingFeeding = feeding;
        this.hiveId = hiveId;
        this.tm = TranslationManager.getInstance();

        initDialog();
        setupAutoCalculation();
        populateFields();
    }

    private void initDialog() {
        setTitle(existingFeeding == null ? tm.get("dialog.add_feeding.title") : tm.get("dialog.edit_feeding.title"));
        initModality(Modality.APPLICATION_MODAL);
        setResizable(true);

        ButtonType saveButtonType = new ButtonType(tm.get("button.save"), ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType(tm.get("button.cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/feeding_dialog.fxml"));
            loader.setResources(new I18nResourceBundle(tm));
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
            alert.setContentText(tm.get("error.loading_form", e.getMessage()));
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
            ValidationHelper.showValidationError(tm.get("validation.date_required"));
            return false;
        }

        if (!ValidationHelper.isValidInteger(hourField.getText())) {
            ValidationHelper.showValidationError(tm.get("validation.hour_must_be_number"));
            return false;
        }

        if (!ValidationHelper.isValidInteger(minuteField.getText())) {
            ValidationHelper.showValidationError(tm.get("validation.minute_must_be_number"));
            return false;
        }

        int hour = ValidationHelper.parseInt(hourField.getText());
        int minute = ValidationHelper.parseInt(minuteField.getText());

        if (!ValidationHelper.isInRange(hour, 0, 23)) {
            ValidationHelper.showValidationError(tm.get("validation.hour_range"));
            return false;
        }

        if (!ValidationHelper.isInRange(minute, 0, 59)) {
            ValidationHelper.showValidationError(tm.get("validation.minute_range"));
            return false;
        }

        if (feedTypeCombo.getSelectionModel().getSelectedItem() == null) {
            ValidationHelper.showValidationError(tm.get("validation.feed_type_required"));
            return false;
        }

        if (!ValidationHelper.isValidDouble(amountField.getText())) {
            ValidationHelper.showValidationError(tm.get("validation.amount_must_be_number"));
            return false;
        }

        double amount = ValidationHelper.parseDouble(amountField.getText());
        if (amount <= 0) {
            ValidationHelper.showValidationError(tm.get("validation.amount_greater_than_zero"));
            return false;
        }

        // Validate weight fields if provided
        if (!weightBeforeField.getText().trim().isEmpty() && !ValidationHelper.isValidDouble(weightBeforeField.getText())) {
            ValidationHelper.showValidationError(tm.get("validation.weight_before_must_be_number"));
            return false;
        }

        if (!weightAfterField.getText().trim().isEmpty() && !ValidationHelper.isValidDouble(weightAfterField.getText())) {
            ValidationHelper.showValidationError(tm.get("validation.weight_after_must_be_number"));
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
