package com.beekeeper.desktop.dialog;

import com.beekeeper.desktop.dao.jdbc.JdbcSettingsDao;
import com.beekeeper.desktop.i18n.I18nResourceBundle;
import com.beekeeper.shared.i18n.TranslationManager;
import com.beekeeper.shared.util.DateFormatManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Settings dialog for application preferences.
 * Currently supports date format configuration with live preview.
 *
 * Use case: User wants to change date format from dd.MM.yyyy to MM/dd/yyyy
 * or any other preferred format.
 */
public class SettingsDialog extends Dialog<Boolean> {

    @FXML
    private TextField dateFormatField;

    @FXML
    private TextField timeFormatField;

    @FXML
    private TextField dateTimeFormatField;

    @FXML
    private Label datePreviewLabel;

    @FXML
    private Label timePreviewLabel;

    @FXML
    private Label dateTimePreviewLabel;

    @FXML
    private Button resetButton;

    private final TranslationManager tm;
    private final DateFormatManager dfm;
    private final JdbcSettingsDao settingsDao;
    private final long sampleTimestamp;

    /**
     * Creates settings dialog with current format values.
     */
    public SettingsDialog() {
        this.tm = TranslationManager.getInstance();
        this.dfm = DateFormatManager.getInstance();
        this.settingsDao = new JdbcSettingsDao();
        this.sampleTimestamp = System.currentTimeMillis();

        setTitle(tm.get("settings.dialog.title"));
        setResizable(true);

        // Load FXML
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/settings_dialog.fxml"));
            loader.setResources(new I18nResourceBundle(tm));
            loader.setController(this);
            VBox content = loader.load();

            getDialogPane().setContent(content);
        } catch (IOException e) {
            e.printStackTrace();
            Label errorLabel = new Label("Error loading settings dialog: " + e.getMessage());
            getDialogPane().setContent(errorLabel);
        }

        // Add OK and Cancel buttons
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Set result converter
        setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                return saveSettings();
            }
            return false;
        });
    }

    @FXML
    private void initialize() {
        // Load current formats
        dateFormatField.setText(dfm.getDateFormat());
        timeFormatField.setText(dfm.getTimeFormat());
        dateTimeFormatField.setText(dfm.getDateTimeFormat());

        // Add listeners for live preview
        dateFormatField.textProperty().addListener((obs, oldVal, newVal) -> updatePreview());
        timeFormatField.textProperty().addListener((obs, oldVal, newVal) -> updatePreview());
        dateTimeFormatField.textProperty().addListener((obs, oldVal, newVal) -> updatePreview());

        // Reset button action
        resetButton.setOnAction(event -> resetToDefaults());

        // Initial preview
        updatePreview();
    }

    /**
     * Updates preview labels with current format patterns.
     * Shows example formatted date/time using sample timestamp.
     */
    private void updatePreview() {
        // Date preview
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormatField.getText());
            datePreviewLabel.setText(sdf.format(new Date(sampleTimestamp)));
            datePreviewLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
        } catch (Exception e) {
            datePreviewLabel.setText(tm.get("settings.preview.invalid"));
            datePreviewLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
        }

        // Time preview
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(timeFormatField.getText());
            timePreviewLabel.setText(sdf.format(new Date(sampleTimestamp)));
            timePreviewLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
        } catch (Exception e) {
            timePreviewLabel.setText(tm.get("settings.preview.invalid"));
            timePreviewLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
        }

        // DateTime preview
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormatField.getText());
            dateTimePreviewLabel.setText(sdf.format(new Date(sampleTimestamp)));
            dateTimePreviewLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
        } catch (Exception e) {
            dateTimePreviewLabel.setText(tm.get("settings.preview.invalid"));
            dateTimePreviewLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
        }
    }

    /**
     * Resets all formats to defaults (dd.MM.yyyy, HH:mm, dd.MM.yyyy HH:mm).
     */
    private void resetToDefaults() {
        dateFormatField.setText("dd.MM.yyyy");
        timeFormatField.setText("HH:mm");
        dateTimeFormatField.setText("dd.MM.yyyy HH:mm");
    }

    /**
     * Validates and saves settings to database and DateFormatManager.
     *
     * @return true if settings were saved successfully, false if validation failed
     */
    private boolean saveSettings() {
        String dateFormat = dateFormatField.getText().trim();
        String timeFormat = timeFormatField.getText().trim();
        String dateTimeFormat = dateTimeFormatField.getText().trim();

        // Validate formats
        if (!DateFormatManager.isValidFormat(dateFormat)) {
            showValidationError(tm.get("settings.error.invalid_date_format"));
            return false;
        }

        if (!DateFormatManager.isValidFormat(timeFormat)) {
            showValidationError(tm.get("settings.error.invalid_time_format"));
            return false;
        }

        if (!DateFormatManager.isValidFormat(dateTimeFormat)) {
            showValidationError(tm.get("settings.error.invalid_datetime_format"));
            return false;
        }

        // Save to database
        settingsDao.saveDateFormat(dateFormat);
        settingsDao.saveTimeFormat(timeFormat);
        settingsDao.saveDateTimeFormat(dateTimeFormat);

        // Update DateFormatManager
        dfm.setDateFormat(dateFormat);
        dfm.setTimeFormat(timeFormat);
        dfm.setDateTimeFormat(dateTimeFormat);

        // Show restart prompt
        showRestartPrompt();

        return true;
    }

    /**
     * Shows validation error alert.
     */
    private void showValidationError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(tm.get("error.title"));
        alert.setHeaderText(tm.get("settings.error.validation_failed"));
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows restart prompt after settings change.
     * Informs user that application needs restart to apply new date formats.
     */
    private void showRestartPrompt() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(tm.get("settings.restart.title"));
        alert.setHeaderText(tm.get("settings.restart.header"));
        alert.setContentText(tm.get("settings.restart.content"));
        alert.showAndWait();

        // Exit application (user will restart manually)
        Platform.exit();
    }
}
