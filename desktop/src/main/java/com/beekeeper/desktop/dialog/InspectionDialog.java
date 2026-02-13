package com.beekeeper.desktop.dialog;

import com.beekeeper.desktop.util.DateTimeConverter;
import com.beekeeper.desktop.util.ValidationHelper;
import com.beekeeper.shared.entity.Inspection;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Dialog for creating or editing an Inspection.
 * Contains all 23 fields of Inspection entity organized in sections.
 */
public class InspectionDialog extends Dialog<Inspection> {

    // Section 1: Základné údaje
    private DatePicker datePicker;
    private TextField hourField;
    private TextField minuteField;
    private TextField temperatureField;

    // Section 2: Sila a zásoby
    private Slider strengthSlider;
    private Label strengthLabel;
    private TextField foodStoresField;

    // Section 3: Rámiky a plod
    private TextField totalFramesField;
    private TextField broodFramesField;
    private TextField pollenFramesField;
    private TextField cappedBroodField;
    private TextField uncappedBroodField;

    // Section 4: Matka
    private CheckBox queenSeenCheckBox;
    private TextField queenNoteField;

    // Section 5: Varroa
    private CheckBox varroaCheckBox;
    private TextField varroaCountField;

    // Section 6: Správanie
    private Slider aggressionSlider;
    private Label aggressionLabel;
    private TextField behaviorField;

    // Section 7: Poznámky
    private TextArea notesArea;

    private Inspection existingInspection;
    private String hiveId;

    public InspectionDialog(Inspection inspection, String hiveId) {
        this.existingInspection = inspection;
        this.hiveId = hiveId;

        initDialog();
        setupBindings();
        populateFields();
    }

    private void initDialog() {
        setTitle(existingInspection == null ? "Nová prehliadka" : "Upraviť prehliadku");
        initModality(Modality.APPLICATION_MODAL);
        setResizable(true);

        ButtonType saveButtonType = new ButtonType("Uložiť", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Zrušiť", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/inspection_dialog.fxml"));
            ScrollPane scrollPane = loader.load();
            getDialogPane().setContent(scrollPane);

            VBox vbox = (VBox) scrollPane.getContent();

            // Get references to all UI components by traversing the FXML structure
            datePicker = (DatePicker) vbox.lookup("#datePicker");
            hourField = (TextField) vbox.lookup("#hourField");
            minuteField = (TextField) vbox.lookup("#minuteField");
            temperatureField = (TextField) vbox.lookup("#temperatureField");

            strengthSlider = (Slider) vbox.lookup("#strengthSlider");
            strengthLabel = (Label) vbox.lookup("#strengthLabel");
            foodStoresField = (TextField) vbox.lookup("#foodStoresField");

            totalFramesField = (TextField) vbox.lookup("#totalFramesField");
            broodFramesField = (TextField) vbox.lookup("#broodFramesField");
            pollenFramesField = (TextField) vbox.lookup("#pollenFramesField");
            cappedBroodField = (TextField) vbox.lookup("#cappedBroodField");
            uncappedBroodField = (TextField) vbox.lookup("#uncappedBroodField");

            queenSeenCheckBox = (CheckBox) vbox.lookup("#queenSeenCheckBox");
            queenNoteField = (TextField) vbox.lookup("#queenNoteField");

            varroaCheckBox = (CheckBox) vbox.lookup("#varroaCheckBox");
            varroaCountField = (TextField) vbox.lookup("#varroaCountField");

            aggressionSlider = (Slider) vbox.lookup("#aggressionSlider");
            aggressionLabel = (Label) vbox.lookup("#aggressionLabel");
            behaviorField = (TextField) vbox.lookup("#behaviorField");

            notesArea = (TextArea) vbox.lookup("#notesArea");

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
                return createInspectionFromInput();
            }
            return null;
        });
    }

    private void setupBindings() {
        // Bind strength slider to label
        strengthSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            strengthLabel.setText(String.valueOf(newVal.intValue()));
        });

        // Bind aggression slider to label
        aggressionSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            aggressionLabel.setText(String.valueOf(newVal.intValue()));
        });

        // Enable varroaCount field only when varroa checkbox is checked
        varroaCountField.disableProperty().bind(varroaCheckBox.selectedProperty().not());
    }

    private void populateFields() {
        if (existingInspection != null) {
            // Section 1: Základné údaje
            LocalDate date = DateTimeConverter.toLocalDate(existingInspection.getInspectionDate());
            datePicker.setValue(date);
            hourField.setText(String.valueOf(DateTimeConverter.extractHour(existingInspection.getInspectionDate())));
            minuteField.setText(String.valueOf(DateTimeConverter.extractMinute(existingInspection.getInspectionDate())));
            if (existingInspection.getTemperature() > 0) {
                temperatureField.setText(String.valueOf(existingInspection.getTemperature()));
            }

            // Section 2: Sila a zásoby
            strengthSlider.setValue(existingInspection.getStrengthEstimate());
            if (existingInspection.getFoodStoresKg() > 0) {
                foodStoresField.setText(String.valueOf(existingInspection.getFoodStoresKg()));
            }

            // Section 3: Rámiky a plod
            if (existingInspection.getTotalFrames() > 0) {
                totalFramesField.setText(String.valueOf(existingInspection.getTotalFrames()));
            }
            if (existingInspection.getBroodFrames() > 0) {
                broodFramesField.setText(String.valueOf(existingInspection.getBroodFrames()));
            }
            if (existingInspection.getPollenFrames() > 0) {
                pollenFramesField.setText(String.valueOf(existingInspection.getPollenFrames()));
            }
            if (existingInspection.getCappedBroodDm() > 0) {
                cappedBroodField.setText(String.valueOf(existingInspection.getCappedBroodDm()));
            }
            if (existingInspection.getUncappedBroodDm() > 0) {
                uncappedBroodField.setText(String.valueOf(existingInspection.getUncappedBroodDm()));
            }

            // Section 4: Matka
            queenSeenCheckBox.setSelected(existingInspection.isQueenSeen());
            queenNoteField.setText(existingInspection.getQueenNote());

            // Section 5: Varroa
            varroaCheckBox.setSelected(existingInspection.isVarroa());
            if (existingInspection.getVarroaCount() > 0) {
                varroaCountField.setText(String.valueOf(existingInspection.getVarroaCount()));
            }

            // Section 6: Správanie
            aggressionSlider.setValue(existingInspection.getAggression());
            behaviorField.setText(existingInspection.getBehavior());

            // Section 7: Poznámky
            notesArea.setText(existingInspection.getNotes());
        } else {
            // Default values for new inspection
            datePicker.setValue(LocalDate.now());
            hourField.setText("12");
            minuteField.setText("0");
            strengthSlider.setValue(5);
            aggressionSlider.setValue(1);
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

        // Validate numeric fields
        if (!temperatureField.getText().trim().isEmpty() && !ValidationHelper.isValidDouble(temperatureField.getText())) {
            ValidationHelper.showValidationError("Teplota musí byť číslo");
            return false;
        }

        if (!foodStoresField.getText().trim().isEmpty() && !ValidationHelper.isValidDouble(foodStoresField.getText())) {
            ValidationHelper.showValidationError("Zásoby musia byť číslo");
            return false;
        }

        if (!totalFramesField.getText().trim().isEmpty() && !ValidationHelper.isValidInteger(totalFramesField.getText())) {
            ValidationHelper.showValidationError("Celkový počet rámikov musí byť číslo");
            return false;
        }

        if (!broodFramesField.getText().trim().isEmpty() && !ValidationHelper.isValidInteger(broodFramesField.getText())) {
            ValidationHelper.showValidationError("Plodové rámiky musia byť číslo");
            return false;
        }

        if (!pollenFramesField.getText().trim().isEmpty() && !ValidationHelper.isValidInteger(pollenFramesField.getText())) {
            ValidationHelper.showValidationError("Peľové rámiky musia byť číslo");
            return false;
        }

        if (!cappedBroodField.getText().trim().isEmpty() && !ValidationHelper.isValidInteger(cappedBroodField.getText())) {
            ValidationHelper.showValidationError("Zapečatený plod musí byť číslo");
            return false;
        }

        if (!uncappedBroodField.getText().trim().isEmpty() && !ValidationHelper.isValidInteger(uncappedBroodField.getText())) {
            ValidationHelper.showValidationError("Nezapečatený plod musí byť číslo");
            return false;
        }

        if (!varroaCountField.getText().trim().isEmpty() && !ValidationHelper.isValidInteger(varroaCountField.getText())) {
            ValidationHelper.showValidationError("Počet varry musí byť číslo");
            return false;
        }

        return true;
    }

    private Inspection createInspectionFromInput() {
        Inspection inspection = existingInspection != null ? existingInspection : new Inspection();

        inspection.setHiveId(hiveId);

        // Section 1: Základné údaje
        LocalDate date = datePicker.getValue();
        int hour = ValidationHelper.parseInt(hourField.getText());
        int minute = ValidationHelper.parseInt(minuteField.getText());
        inspection.setInspectionDate(DateTimeConverter.toTimestamp(date, hour, minute));
        inspection.setTemperature(ValidationHelper.parseDouble(temperatureField.getText()));

        // Section 2: Sila a zásoby
        inspection.setStrengthEstimate((int) strengthSlider.getValue());
        inspection.setFoodStoresKg(ValidationHelper.parseDouble(foodStoresField.getText()));

        // Section 3: Rámiky a plod
        inspection.setTotalFrames(ValidationHelper.parseInt(totalFramesField.getText()));
        inspection.setBroodFrames(ValidationHelper.parseInt(broodFramesField.getText()));
        inspection.setPollenFrames(ValidationHelper.parseInt(pollenFramesField.getText()));
        inspection.setCappedBroodDm(ValidationHelper.parseInt(cappedBroodField.getText()));
        inspection.setUncappedBroodDm(ValidationHelper.parseInt(uncappedBroodField.getText()));

        // Section 4: Matka
        inspection.setQueenSeen(queenSeenCheckBox.isSelected());
        inspection.setQueenNote(queenNoteField.getText() != null ? queenNoteField.getText().trim() : "");

        // Section 5: Varroa
        inspection.setVarroa(varroaCheckBox.isSelected());
        inspection.setVarroaCount(ValidationHelper.parseInt(varroaCountField.getText()));

        // Section 6: Správanie
        inspection.setAggression((int) aggressionSlider.getValue());
        inspection.setBehavior(behaviorField.getText() != null ? behaviorField.getText().trim() : "");

        // Section 7: Poznámky
        inspection.setNotes(notesArea.getText() != null ? notesArea.getText().trim() : "");

        // Hidden fields (not used in desktop)
        inspection.setRecordingId(null);
        inspection.setExtractedFromAudio(false);

        return inspection;
    }
}
