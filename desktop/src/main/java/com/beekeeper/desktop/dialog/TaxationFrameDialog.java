package com.beekeeper.desktop.dialog;

import com.beekeeper.desktop.i18n.I18nResourceBundle;
import com.beekeeper.desktop.util.EnumHelper;
import com.beekeeper.desktop.util.ValidationHelper;
import com.beekeeper.shared.entity.TaxationFrame;
import com.beekeeper.shared.i18n.TranslationManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;

import java.io.IOException;
import java.util.UUID;

/**
 * Dialog for creating or editing a TaxationFrame.
 * Contains all 13 fields of TaxationFrame entity.
 */
public class TaxationFrameDialog extends Dialog<TaxationFrame> {

    private TextField positionField;
    private ComboBox<String> frameTypeCombo;
    private TextField cappedBroodField;
    private TextField uncappedBroodField;
    private TextField pollenField;
    private TextField cappedStoresField;
    private TextField uncappedStoresField;
    private TextField frameYearField;
    private CheckBox isStarterCheckBox;
    private CheckBox hasQueenCheckBox;
    private CheckBox hasCageCheckBox;
    private CheckBox hasNucBoxCheckBox;
    private TextArea notesArea;

    private TaxationFrame existingFrame;
    private String taxationId;
    private TranslationManager tm;

    public TaxationFrameDialog(TaxationFrame frame, String taxationId) {
        this.existingFrame = frame;
        this.taxationId = taxationId;
        this.tm = TranslationManager.getInstance();

        initDialog();
        populateFields();
    }

    private void initDialog() {
        setTitle(existingFrame == null ? tm.get("dialog.add_frame.title") : tm.get("dialog.edit_frame.title"));
        initModality(Modality.APPLICATION_MODAL);
        setResizable(true);

        ButtonType saveButtonType = new ButtonType(tm.get("button.save"), ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType(tm.get("button.cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/taxation_frame_dialog.fxml"));
            loader.setResources(new I18nResourceBundle(tm));
            GridPane gridPane = loader.load();
            getDialogPane().setContent(gridPane);

            // Get references to UI components
            positionField = (TextField) gridPane.lookup("#positionField");
            frameTypeCombo = (ComboBox<String>) gridPane.lookup("#frameTypeCombo");
            cappedBroodField = (TextField) gridPane.lookup("#cappedBroodField");
            uncappedBroodField = (TextField) gridPane.lookup("#uncappedBroodField");
            pollenField = (TextField) gridPane.lookup("#pollenField");
            cappedStoresField = (TextField) gridPane.lookup("#cappedStoresField");
            uncappedStoresField = (TextField) gridPane.lookup("#uncappedStoresField");
            frameYearField = (TextField) gridPane.lookup("#frameYearField");
            isStarterCheckBox = (CheckBox) gridPane.lookup("#isStarterCheckBox");
            hasQueenCheckBox = (CheckBox) gridPane.lookup("#hasQueenCheckBox");
            hasCageCheckBox = (CheckBox) gridPane.lookup("#hasCageCheckBox");
            hasNucBoxCheckBox = (CheckBox) gridPane.lookup("#hasNucBoxCheckBox");
            notesArea = (TextArea) gridPane.lookup("#notesArea");

            // Populate frame types
            frameTypeCombo.setItems(EnumHelper.getFrameTypes());

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
                return createFrameFromInput();
            }
            return null;
        });
    }

    private void populateFields() {
        if (existingFrame != null) {
            positionField.setText(String.valueOf(existingFrame.getPosition()));
            frameTypeCombo.getSelectionModel().select(existingFrame.getFrameType());

            if (existingFrame.getCappedBroodDm() > 0) {
                cappedBroodField.setText(String.valueOf(existingFrame.getCappedBroodDm()));
            }
            if (existingFrame.getUncappedBroodDm() > 0) {
                uncappedBroodField.setText(String.valueOf(existingFrame.getUncappedBroodDm()));
            }
            if (existingFrame.getPollenDm() > 0) {
                pollenField.setText(String.valueOf(existingFrame.getPollenDm()));
            }
            if (existingFrame.getCappedStoresDm() > 0) {
                cappedStoresField.setText(String.valueOf(existingFrame.getCappedStoresDm()));
            }
            if (existingFrame.getUncappedStoresDm() > 0) {
                uncappedStoresField.setText(String.valueOf(existingFrame.getUncappedStoresDm()));
            }
            if (existingFrame.getFrameYear() > 0) {
                frameYearField.setText(String.valueOf(existingFrame.getFrameYear()));
            }

            isStarterCheckBox.setSelected(existingFrame.isStarter());
            hasQueenCheckBox.setSelected(existingFrame.isHasQueen());
            hasCageCheckBox.setSelected(existingFrame.isHasCage());
            hasNucBoxCheckBox.setSelected(existingFrame.isHasNucBox());

            notesArea.setText(existingFrame.getNotes());
        } else {
            // Default values
            frameTypeCombo.getSelectionModel().selectFirst();
        }
    }

    private boolean validateInput() {
        if (positionField.getText() == null || positionField.getText().trim().isEmpty()) {
            ValidationHelper.showValidationError(tm.get("validation.position_required"));
            return false;
        }

        if (!ValidationHelper.isValidInteger(positionField.getText())) {
            ValidationHelper.showValidationError(tm.get("validation.position_must_be_number"));
            return false;
        }

        if (frameTypeCombo.getSelectionModel().getSelectedItem() == null) {
            ValidationHelper.showValidationError(tm.get("validation.frame_type_required"));
            return false;
        }

        // Validate optional numeric fields
        if (!cappedBroodField.getText().trim().isEmpty() && !ValidationHelper.isValidInteger(cappedBroodField.getText())) {
            ValidationHelper.showValidationError(tm.get("validation.capped_brood_must_be_number"));
            return false;
        }

        if (!uncappedBroodField.getText().trim().isEmpty() && !ValidationHelper.isValidInteger(uncappedBroodField.getText())) {
            ValidationHelper.showValidationError(tm.get("validation.uncapped_brood_must_be_number"));
            return false;
        }

        if (!pollenField.getText().trim().isEmpty() && !ValidationHelper.isValidInteger(pollenField.getText())) {
            ValidationHelper.showValidationError(tm.get("validation.pollen_must_be_number"));
            return false;
        }

        if (!cappedStoresField.getText().trim().isEmpty() && !ValidationHelper.isValidInteger(cappedStoresField.getText())) {
            ValidationHelper.showValidationError(tm.get("validation.capped_stores_must_be_number"));
            return false;
        }

        if (!uncappedStoresField.getText().trim().isEmpty() && !ValidationHelper.isValidInteger(uncappedStoresField.getText())) {
            ValidationHelper.showValidationError(tm.get("validation.uncapped_stores_must_be_number"));
            return false;
        }

        if (!frameYearField.getText().trim().isEmpty() && !ValidationHelper.isValidInteger(frameYearField.getText())) {
            ValidationHelper.showValidationError(tm.get("validation.frame_year_must_be_number"));
            return false;
        }

        return true;
    }

    private TaxationFrame createFrameFromInput() {
        TaxationFrame frame = existingFrame != null ? existingFrame : new TaxationFrame();

        if (frame.getId() == null || frame.getId().isEmpty()) {
            frame.setId(UUID.randomUUID().toString());
        }

        frame.setTaxationId(taxationId);
        frame.setPosition(ValidationHelper.parseInt(positionField.getText()));
        frame.setFrameType(frameTypeCombo.getSelectionModel().getSelectedItem());

        frame.setCappedBroodDm(ValidationHelper.parseInt(cappedBroodField.getText()));
        frame.setUncappedBroodDm(ValidationHelper.parseInt(uncappedBroodField.getText()));
        frame.setPollenDm(ValidationHelper.parseInt(pollenField.getText()));
        frame.setCappedStoresDm(ValidationHelper.parseInt(cappedStoresField.getText()));
        frame.setUncappedStoresDm(ValidationHelper.parseInt(uncappedStoresField.getText()));
        frame.setFrameYear(ValidationHelper.parseInt(frameYearField.getText()));

        frame.setStarter(isStarterCheckBox.isSelected());
        frame.setHasQueen(hasQueenCheckBox.isSelected());
        frame.setHasCage(hasCageCheckBox.isSelected());
        frame.setHasNucBox(hasNucBoxCheckBox.isSelected());

        frame.setNotes(notesArea.getText() != null ? notesArea.getText().trim() : "");

        return frame;
    }
}
