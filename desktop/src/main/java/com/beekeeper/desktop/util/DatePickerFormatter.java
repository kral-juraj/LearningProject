package com.beekeeper.desktop.util;

import com.beekeeper.shared.util.DateFormatManager;
import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for configuring DatePicker components with user-defined format.
 * Ensures all DatePickers in the application use consistent formatting.
 *
 * Use case: Apply user's preferred date format (dd.MM.yyyy, MM/dd/yyyy, etc.)
 * to all DatePicker components throughout the application.
 */
public class DatePickerFormatter {

    /**
     * Configures DatePicker to use user-defined date format from DateFormatManager.
     * 
     * This method MUST be called for every DatePicker in the application to ensure
     * consistent date formatting according to user preferences.
     *
     * Use case: Call this in dialog initialize() methods after DatePicker creation.
     *
     * Example:
     * <pre>
     * {@code
     * @FXML
     * private DatePicker datePicker;
     * 
     * @FXML
     * public void initialize() {
     *     DatePickerFormatter.format(datePicker);
     * }
     * }
     * </pre>
     *
     * @param datePicker DatePicker component to format
     */
    public static void format(DatePicker datePicker) {
        if (datePicker == null) {
            return;
        }

        // Get user's preferred date format
        DateFormatManager dfm = DateFormatManager.getInstance();
        String dateFormatPattern = dfm.getDateFormat();

        // Convert SimpleDateFormat pattern to DateTimeFormatter pattern
        // (they use same syntax: dd.MM.yyyy, MM/dd/yyyy, etc.)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormatPattern);

        // Set custom StringConverter for DatePicker
        datePicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return formatter.format(date);
                }
                return "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    try {
                        return LocalDate.parse(string, formatter);
                    } catch (Exception e) {
                        // Invalid format, return null
                        return null;
                    }
                }
                return null;
            }
        });

        // Set prompt text to show expected format
        datePicker.setPromptText(dateFormatPattern.toLowerCase());
    }

    /**
     * Formats multiple DatePickers at once.
     * Convenience method for dialogs with multiple date fields.
     *
     * Use case: Format all DatePickers in a dialog in one call.
     *
     * Example:
     * <pre>
     * {@code
     * DatePickerFormatter.formatAll(startDatePicker, endDatePicker, dueDatePicker);
     * }
     * </pre>
     *
     * @param datePickers Variable number of DatePicker components to format
     */
    public static void formatAll(DatePicker... datePickers) {
        for (DatePicker datePicker : datePickers) {
            format(datePicker);
        }
    }
}
