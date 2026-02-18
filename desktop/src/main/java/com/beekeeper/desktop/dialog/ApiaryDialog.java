package com.beekeeper.desktop.dialog;

import com.beekeeper.shared.entity.Apiary;
import com.beekeeper.shared.i18n.TranslationManager;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * Dialog for creating or editing an Apiary with extended details.
 *
 * Use case: Add/edit apiary with comprehensive information including
 * name, location, address, registration number, and description.
 */
public class ApiaryDialog extends Dialog<Apiary> {

    private TextField nameField;
    private TextField locationField;
    private TextField addressField;
    private TextField registrationNumberField;
    private TextArea descriptionArea;

    private Apiary existingApiary;
    private TranslationManager tm;

    /**
     * Create an Apiary dialog.
     *
     * @param apiary Existing apiary to edit, or null to create new
     */
    public ApiaryDialog(Apiary apiary) {
        this.existingApiary = apiary;
        this.tm = TranslationManager.getInstance();

        setTitle(apiary == null ? tm.get("dialog.add_apiary.title") : tm.get("dialog.edit_apiary.title"));
        setHeaderText(apiary == null ? tm.get("dialog.apiary.header_add") : tm.get("dialog.apiary.header_edit"));

        // Create content panel
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 20));
        grid.setPrefWidth(450);

        int row = 0;

        // Name (required)
        Label nameLabel = new Label(tm.get("label.name") + " *");
        nameLabel.setMinWidth(150);
        nameField = new TextField();
        nameField.setPromptText(tm.get("prompt.required"));
        nameField.setPrefColumnCount(20);
        Tooltip nameTooltip = new Tooltip(tm.get("tooltip.apiary_name"));
        nameField.setTooltip(nameTooltip);
        grid.add(nameLabel, 0, row);
        grid.add(nameField, 1, row);
        row++;

        // Location (optional)
        Label locationLabel = new Label(tm.get("label.location"));
        locationLabel.setMinWidth(150);
        locationField = new TextField();
        locationField.setPromptText(tm.get("prompt.optional"));
        Tooltip locationTooltip = new Tooltip(tm.get("tooltip.apiary_location"));
        locationField.setTooltip(locationTooltip);
        grid.add(locationLabel, 0, row);
        grid.add(locationField, 1, row);
        row++;

        // Address (optional)
        Label addressLabel = new Label(tm.get("label.address"));
        addressLabel.setMinWidth(150);
        addressField = new TextField();
        addressField.setPromptText(tm.get("prompt.optional"));
        Tooltip addressTooltip = new Tooltip(tm.get("tooltip.apiary_address"));
        addressField.setTooltip(addressTooltip);
        grid.add(addressLabel, 0, row);
        grid.add(addressField, 1, row);
        row++;

        // Registration Number (optional)
        Label regNumberLabel = new Label(tm.get("label.registration_number"));
        regNumberLabel.setMinWidth(150);
        registrationNumberField = new TextField();
        registrationNumberField.setPromptText(tm.get("prompt.optional"));
        Tooltip regNumberTooltip = new Tooltip(tm.get("tooltip.registration_number"));
        registrationNumberField.setTooltip(regNumberTooltip);
        grid.add(regNumberLabel, 0, row);
        grid.add(registrationNumberField, 1, row);
        row++;

        // Description (optional)
        Label descLabel = new Label(tm.get("label.description"));
        descLabel.setMinWidth(150);
        descriptionArea = new TextArea();
        descriptionArea.setPromptText(tm.get("prompt.optional"));
        descriptionArea.setPrefRowCount(3);
        descriptionArea.setWrapText(true);
        Tooltip descTooltip = new Tooltip(tm.get("tooltip.apiary_description"));
        descriptionArea.setTooltip(descTooltip);
        grid.add(descLabel, 0, row);
        grid.add(descriptionArea, 1, row);
        GridPane.setVgrow(descriptionArea, Priority.ALWAYS);

        getDialogPane().setContent(grid);

        // Buttons
        ButtonType saveButton = new ButtonType(tm.get("button.save"), ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        // Enable save button only when name is filled
        javafx.scene.Node saveBtn = getDialogPane().lookupButton(saveButton);
        saveBtn.setDisable(true);

        nameField.textProperty().addListener((obs, old, newVal) -> {
            saveBtn.setDisable(newVal == null || newVal.trim().isEmpty());
        });

        // Populate fields for edit mode
        if (existingApiary != null) {
            populateFields();
            saveBtn.setDisable(nameField.getText() == null || nameField.getText().trim().isEmpty());
        }

        // Result converter
        setResultConverter(buttonType -> {
            if (buttonType == saveButton) {
                return createApiaryFromInput();
            }
            return null;
        });
    }

    /**
     * Populate fields with existing apiary data (edit mode).
     */
    private void populateFields() {
        nameField.setText(existingApiary.getName());
        locationField.setText(existingApiary.getLocation());
        addressField.setText(existingApiary.getAddress());
        registrationNumberField.setText(existingApiary.getRegistrationNumber());
        descriptionArea.setText(existingApiary.getDescription());
    }

    /**
     * Create Apiary object from user input.
     *
     * @return Apiary with all fields populated
     */
    private Apiary createApiaryFromInput() {
        Apiary apiary = existingApiary != null ? existingApiary : new Apiary();

        // Set values from input fields
        apiary.setName(nameField.getText().trim());
        apiary.setLocation(locationField.getText() != null ? locationField.getText().trim() : "");
        apiary.setAddress(addressField.getText() != null ? addressField.getText().trim() : null);
        apiary.setRegistrationNumber(registrationNumberField.getText() != null ? registrationNumberField.getText().trim() : null);
        apiary.setDescription(descriptionArea.getText() != null ? descriptionArea.getText().trim() : null);

        // Preserve existing values for fields not in dialog
        if (existingApiary != null) {
            apiary.setId(existingApiary.getId());
            apiary.setLatitude(existingApiary.getLatitude());
            apiary.setLongitude(existingApiary.getLongitude());
            apiary.setDisplayOrder(existingApiary.getDisplayOrder());
            apiary.setCreatedAt(existingApiary.getCreatedAt());
        }

        return apiary;
    }
}
