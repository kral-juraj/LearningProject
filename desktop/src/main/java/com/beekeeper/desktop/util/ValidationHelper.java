package com.beekeeper.desktop.util;

import javafx.scene.control.Alert;

/**
 * Utility class for input validation in dialogs.
 * Provides validation methods and error display.
 */
public class ValidationHelper {

    /**
     * Checks if a string can be parsed as a valid double.
     */
    public static boolean isValidDouble(String text) {
        if (text == null || text.trim().isEmpty()) {
            return true; // Empty is valid (optional fields)
        }

        try {
            Double.parseDouble(text.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if a string can be parsed as a valid integer.
     */
    public static boolean isValidInteger(String text) {
        if (text == null || text.trim().isEmpty()) {
            return true; // Empty is valid (optional fields)
        }

        try {
            Integer.parseInt(text.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if an integer value is within the specified range (inclusive).
     */
    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    /**
     * Parses a double from text, returning 0.0 if empty or invalid.
     */
    public static double parseDouble(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0.0;
        }

        try {
            return Double.parseDouble(text.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    /**
     * Parses an integer from text, returning 0 if empty or invalid.
     */
    public static int parseInt(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }

        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Shows a validation error dialog.
     */
    public static void showValidationError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Chyba validácie");
        alert.setHeaderText("Neplatné údaje");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
