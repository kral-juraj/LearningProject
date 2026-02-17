package com.beekeeper.desktop.dialog;

import com.beekeeper.desktop.util.DatePickerFormatter;
import com.beekeeper.shared.entity.HiveActivity;
import com.beekeeper.shared.entity.HiveActivityType;
import com.beekeeper.shared.i18n.TranslationManager;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Dialog for adding or editing a hive activity.
 *
 * Use case: Record equipment changes, major events, and modifications
 * to the hive (e.g., adding supers, honey harvest, treatment).
 */
public class HiveActivityDialog extends Dialog<HiveActivity> {

    private DatePicker activityDatePicker;
    private ComboBox<ActivityTypeItem> activityTypeCombo;
    private TextField oldValueField;
    private TextField newValueField;
    private TextArea descriptionArea;

    private HiveActivity existingActivity;
    private String hiveId;
    private TranslationManager tm;

    public HiveActivityDialog(HiveActivity activity, String hiveId) {
        this.existingActivity = activity;
        this.hiveId = hiveId;
        this.tm = TranslationManager.getInstance();

        setTitle(activity == null ? tm.get("dialog.add_activity.title") : tm.get("dialog.edit_activity.title"));
        setHeaderText(activity == null ? tm.get("dialog.activity.header_add") : tm.get("dialog.activity.header_edit"));

        // Create form
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Activity date
        Label dateLabel = new Label(tm.get("label.activity_date") + " *");
        activityDatePicker = new DatePicker();
        activityDatePicker.setValue(LocalDate.now());
        DatePickerFormatter.format(activityDatePicker);
        grid.add(dateLabel, 0, 0);
        grid.add(activityDatePicker, 1, 0);

        // Activity type
        Label typeLabel = new Label(tm.get("label.activity_type") + " *");
        activityTypeCombo = new ComboBox<>();
        populateActivityTypes();
        activityTypeCombo.setPromptText(tm.get("prompt.select_activity_type"));
        activityTypeCombo.setMaxWidth(Double.MAX_VALUE);
        grid.add(typeLabel, 0, 1);
        grid.add(activityTypeCombo, 1, 1);

        // Old value
        Label oldValueLabel = new Label(tm.get("label.old_value"));
        oldValueField = new TextField();
        oldValueField.setPromptText(tm.get("prompt.optional"));
        Tooltip oldValueTooltip = new Tooltip(tm.get("tooltip.old_value"));
        oldValueField.setTooltip(oldValueTooltip);
        grid.add(oldValueLabel, 0, 2);
        grid.add(oldValueField, 1, 2);

        // New value
        Label newValueLabel = new Label(tm.get("label.new_value"));
        newValueField = new TextField();
        newValueField.setPromptText(tm.get("prompt.optional"));
        Tooltip newValueTooltip = new Tooltip(tm.get("tooltip.new_value"));
        newValueField.setTooltip(newValueTooltip);
        grid.add(newValueLabel, 0, 3);
        grid.add(newValueField, 1, 3);

        // Description
        Label descLabel = new Label(tm.get("label.description"));
        descriptionArea = new TextArea();
        descriptionArea.setPromptText(tm.get("prompt.optional_description"));
        descriptionArea.setPrefRowCount(4);
        descriptionArea.setWrapText(true);
        grid.add(descLabel, 0, 4);
        grid.add(descriptionArea, 1, 4);

        content.getChildren().add(grid);
        getDialogPane().setContent(content);

        // Buttons
        ButtonType saveButton = new ButtonType(tm.get("button.save"), ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        // Populate fields for edit mode
        if (existingActivity != null) {
            populateFields();
        }

        // Enable save button only when required fields are filled
        javafx.scene.Node saveBtn = getDialogPane().lookupButton(saveButton);
        saveBtn.setDisable(true);

        Runnable validateFields = () -> {
            boolean valid = activityDatePicker.getValue() != null &&
                           activityTypeCombo.getValue() != null;
            saveBtn.setDisable(!valid);
        };

        activityDatePicker.valueProperty().addListener((obs, old, newVal) -> validateFields.run());
        activityTypeCombo.valueProperty().addListener((obs, old, newVal) -> validateFields.run());

        // Auto-suggest values based on activity type
        activityTypeCombo.setOnAction(e -> autoSuggestValues());

        // Result converter
        setResultConverter(buttonType -> {
            if (buttonType == saveButton) {
                return createActivityFromInput();
            }
            return null;
        });
    }

    /**
     * Populate activity type combo with all available types.
     */
    private void populateActivityTypes() {
        for (HiveActivityType type : HiveActivityType.values()) {
            activityTypeCombo.getItems().add(new ActivityTypeItem(type));
        }
    }

    /**
     * Auto-suggest old/new values based on selected activity type.
     */
    private void autoSuggestValues() {
        ActivityTypeItem selected = activityTypeCombo.getValue();
        if (selected == null) return;

        switch (selected.type) {
            case ADDED_SUPER:
                oldValueField.setPromptText(tm.get("prompt.supers_example_2"));
                newValueField.setPromptText(tm.get("prompt.supers_example_3"));
                break;
            case REMOVED_SUPER:
                oldValueField.setPromptText(tm.get("prompt.supers_example_3"));
                newValueField.setPromptText(tm.get("prompt.supers_example_2"));
                break;
            case ADDED_QUEEN_EXCLUDER:
            case REMOVED_QUEEN_EXCLUDER:
            case ADDED_PROPOLIS_TRAP:
            case REMOVED_PROPOLIS_TRAP:
            case ADDED_INSULATION:
            case REMOVED_INSULATION:
                oldValueField.setPromptText(tm.get("prompt.optional"));
                newValueField.setPromptText(tm.get("prompt.optional"));
                break;
            case CHANGED_BOTTOM_BOARD:
                oldValueField.setPromptText(tm.get("prompt.bottom_board_low"));
                newValueField.setPromptText(tm.get("prompt.bottom_board_high"));
                break;
            case HONEY_HARVEST:
                oldValueField.setPromptText(tm.get("prompt.optional"));
                newValueField.setPromptText(tm.get("prompt.honey_harvest_kg"));
                break;
            default:
                oldValueField.setPromptText(tm.get("prompt.optional"));
                newValueField.setPromptText(tm.get("prompt.optional"));
        }
    }

    /**
     * Populate fields with existing activity data.
     */
    private void populateFields() {
        LocalDate date = LocalDate.ofInstant(
            java.time.Instant.ofEpochMilli(existingActivity.getActivityDate()),
            ZoneId.systemDefault()
        );
        activityDatePicker.setValue(date);

        HiveActivityType type = HiveActivityType.fromCode(existingActivity.getActivityType());
        if (type != null) {
            for (ActivityTypeItem item : activityTypeCombo.getItems()) {
                if (item.type == type) {
                    activityTypeCombo.setValue(item);
                    break;
                }
            }
        }

        oldValueField.setText(existingActivity.getOldValue());
        newValueField.setText(existingActivity.getNewValue());
        descriptionArea.setText(existingActivity.getDescription());
    }

    /**
     * Create HiveActivity from form inputs.
     */
    private HiveActivity createActivityFromInput() {
        HiveActivity activity = existingActivity != null ? existingActivity : new HiveActivity();

        if (activity.getId() == null || activity.getId().isEmpty()) {
            activity.setId(java.util.UUID.randomUUID().toString());
        }

        activity.setHiveId(hiveId);

        LocalDate date = activityDatePicker.getValue();
        long timestamp = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        activity.setActivityDate(timestamp);

        ActivityTypeItem typeItem = activityTypeCombo.getValue();
        activity.setActivityType(typeItem.type.getCode());

        activity.setOldValue(oldValueField.getText() != null ? oldValueField.getText().trim() : "");
        activity.setNewValue(newValueField.getText() != null ? newValueField.getText().trim() : "");
        activity.setDescription(descriptionArea.getText() != null ? descriptionArea.getText().trim() : "");

        long now = System.currentTimeMillis();
        if (activity.getCreatedAt() == 0) {
            activity.setCreatedAt(now);
        }
        activity.setUpdatedAt(now);

        return activity;
    }

    /**
     * Helper class for activity type combo items.
     */
    private class ActivityTypeItem {
        HiveActivityType type;

        ActivityTypeItem(HiveActivityType type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return tm.get(type.getTranslationKey());
        }
    }
}
