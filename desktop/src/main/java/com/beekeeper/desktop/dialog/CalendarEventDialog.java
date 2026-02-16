package com.beekeeper.desktop.dialog;

import com.beekeeper.desktop.dao.jdbc.JdbcApiaryDao;
import com.beekeeper.desktop.dao.jdbc.JdbcHiveDao;
import com.beekeeper.desktop.i18n.I18nResourceBundle;
import com.beekeeper.desktop.util.DateTimeConverter;
import com.beekeeper.desktop.util.EnumHelper;
import com.beekeeper.desktop.util.ValidationHelper;
import com.beekeeper.shared.entity.Apiary;
import com.beekeeper.shared.entity.CalendarEvent;
import com.beekeeper.shared.entity.Hive;
import com.beekeeper.shared.i18n.TranslationManager;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Dialog for creating or editing a CalendarEvent.
 * Contains all 11 fields of CalendarEvent entity.
 */
public class CalendarEventDialog extends Dialog<CalendarEvent> {

    private TextField titleField;
    private DatePicker datePicker;
    private TextField hourField;
    private TextField minuteField;
    private ComboBox<String> eventTypeCombo;
    private ComboBox<ApiaryItem> apiaryCombo;
    private ComboBox<HiveItem> hiveCombo;
    private TextField descriptionField;
    private CheckBox completedCheckBox;
    private TextArea notesArea;

    private CalendarEvent existingEvent;
    private JdbcApiaryDao apiaryDao;
    private JdbcHiveDao hiveDao;
    private TranslationManager tm;

    public CalendarEventDialog(CalendarEvent event) {
        this.existingEvent = event;
        this.apiaryDao = new JdbcApiaryDao();
        this.hiveDao = new JdbcHiveDao();
        this.tm = TranslationManager.getInstance();

        initDialog();
        loadUIComponents();
        populateFields();
    }

    private void initDialog() {
        setTitle(existingEvent == null ? tm.get("dialog.add_event.title") : tm.get("dialog.edit_event.title"));
        initModality(Modality.APPLICATION_MODAL);
        setResizable(true);

        ButtonType saveButtonType = new ButtonType(tm.get("button.save"), ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType(tm.get("button.cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/calendar_event_dialog.fxml"));
            loader.setResources(new I18nResourceBundle(tm));
            GridPane gridPane = loader.load();
            getDialogPane().setContent(gridPane);

            // Get references to UI components
            titleField = (TextField) gridPane.lookup("#titleField");
            datePicker = (DatePicker) gridPane.lookup("#datePicker");
            hourField = (TextField) gridPane.lookup("#hourField");
            minuteField = (TextField) gridPane.lookup("#minuteField");
            eventTypeCombo = (ComboBox<String>) gridPane.lookup("#eventTypeCombo");
            apiaryCombo = (ComboBox<ApiaryItem>) gridPane.lookup("#apiaryCombo");
            hiveCombo = (ComboBox<HiveItem>) gridPane.lookup("#hiveCombo");
            descriptionField = (TextField) gridPane.lookup("#descriptionField");
            completedCheckBox = (CheckBox) gridPane.lookup("#completedCheckBox");
            notesArea = (TextArea) gridPane.lookup("#notesArea");

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
                return createEventFromInput();
            }
            return null;
        });
    }

    private void loadUIComponents() {
        // Populate event types
        eventTypeCombo.setItems(EnumHelper.getEventTypes());

        // Populate apiaries
        try {
            List<Apiary> apiaries = apiaryDao.getAll().blockingFirst();
            apiaryCombo.getItems().add(new ApiaryItem(null, tm.get("option.no_apiary")));
            for (Apiary apiary : apiaries) {
                apiaryCombo.getItems().add(new ApiaryItem(apiary.getId(), apiary.getName()));
            }
            apiaryCombo.getSelectionModel().selectFirst();

            // When apiary changes, load hives for that apiary
            apiaryCombo.setOnAction(e -> loadHivesForApiary());

        } catch (Exception e) {
            System.err.println("Error loading apiaries: " + e.getMessage());
        }

        // Initialize hive combo
        hiveCombo.getItems().add(new HiveItem(null, tm.get("option.no_hive")));
        hiveCombo.getSelectionModel().selectFirst();
    }

    private void loadHivesForApiary() {
        ApiaryItem selectedApiary = apiaryCombo.getSelectionModel().getSelectedItem();
        hiveCombo.getItems().clear();
        hiveCombo.getItems().add(new HiveItem(null, tm.get("option.no_hive")));

        if (selectedApiary != null && selectedApiary.id != null) {
            try {
                List<Hive> hives = hiveDao.getByApiaryId(selectedApiary.id).blockingFirst();
                for (Hive hive : hives) {
                    hiveCombo.getItems().add(new HiveItem(hive.getId(), hive.getName()));
                }
            } catch (Exception e) {
                System.err.println("Error loading hives: " + e.getMessage());
            }
        }

        hiveCombo.getSelectionModel().selectFirst();
    }

    private void populateFields() {
        if (existingEvent != null) {
            titleField.setText(existingEvent.getTitle());
            descriptionField.setText(existingEvent.getDescription());
            notesArea.setText(existingEvent.getNotes());
            completedCheckBox.setSelected(existingEvent.isCompleted());

            // Date and time
            LocalDate date = DateTimeConverter.toLocalDate(existingEvent.getEventDate());
            datePicker.setValue(date);
            hourField.setText(String.valueOf(DateTimeConverter.extractHour(existingEvent.getEventDate())));
            minuteField.setText(String.valueOf(DateTimeConverter.extractMinute(existingEvent.getEventDate())));

            // Event type
            eventTypeCombo.getSelectionModel().select(existingEvent.getEventType());

            // Apiary and Hive
            Platform.runLater(() -> {
                if (existingEvent.getApiaryId() != null) {
                    for (ApiaryItem item : apiaryCombo.getItems()) {
                        if (item.id != null && item.id.equals(existingEvent.getApiaryId())) {
                            apiaryCombo.getSelectionModel().select(item);
                            loadHivesForApiary();

                            // Select hive after loading
                            if (existingEvent.getHiveId() != null) {
                                Platform.runLater(() -> {
                                    for (HiveItem hiveItem : hiveCombo.getItems()) {
                                        if (hiveItem.id != null && hiveItem.id.equals(existingEvent.getHiveId())) {
                                            hiveCombo.getSelectionModel().select(hiveItem);
                                            break;
                                        }
                                    }
                                });
                            }
                            break;
                        }
                    }
                }
            });
        } else {
            // Default values for new event
            datePicker.setValue(LocalDate.now());
            hourField.setText("12");
            minuteField.setText("0");
            eventTypeCombo.getSelectionModel().selectFirst();
        }
    }

    private boolean validateInput() {
        if (titleField.getText() == null || titleField.getText().trim().isEmpty()) {
            ValidationHelper.showValidationError(tm.get("validation.name_required"));
            return false;
        }

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

        if (eventTypeCombo.getSelectionModel().getSelectedItem() == null) {
            ValidationHelper.showValidationError(tm.get("validation.event_type_required"));
            return false;
        }

        return true;
    }

    private CalendarEvent createEventFromInput() {
        CalendarEvent event = existingEvent != null ? existingEvent : new CalendarEvent();

        event.setTitle(titleField.getText().trim());
        event.setDescription(descriptionField.getText() != null ? descriptionField.getText().trim() : "");
        event.setNotes(notesArea.getText() != null ? notesArea.getText().trim() : "");
        event.setCompleted(completedCheckBox.isSelected());

        // Date and time
        LocalDate date = datePicker.getValue();
        int hour = ValidationHelper.parseInt(hourField.getText());
        int minute = ValidationHelper.parseInt(minuteField.getText());
        event.setEventDate(DateTimeConverter.toTimestamp(date, hour, minute));

        // Event type
        event.setEventType(eventTypeCombo.getSelectionModel().getSelectedItem());

        // Apiary and Hive
        ApiaryItem selectedApiary = apiaryCombo.getSelectionModel().getSelectedItem();
        event.setApiaryId(selectedApiary != null ? selectedApiary.id : null);

        HiveItem selectedHive = hiveCombo.getSelectionModel().getSelectedItem();
        event.setHiveId(selectedHive != null ? selectedHive.id : null);

        return event;
    }

    // Helper classes for ComboBox items
    private static class ApiaryItem {
        String id;
        String name;

        ApiaryItem(String id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static class HiveItem {
        String id;
        String name;

        HiveItem(String id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
