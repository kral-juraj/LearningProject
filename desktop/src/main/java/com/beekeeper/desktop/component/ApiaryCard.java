package com.beekeeper.desktop.component;

import com.beekeeper.shared.entity.Apiary;
import com.beekeeper.shared.i18n.TranslationManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Custom JavaFX component for displaying an apiary as a compact card.
 *
 * Use case: Show apiary details in a visually appealing card layout
 * with name, location, and hive count.
 */
public class ApiaryCard extends VBox {

    @FXML private Label apiaryIcon;
    @FXML private Label apiaryName;
    @FXML private Label apiaryLocation;
    @FXML private Label hiveCountLabel;
    @FXML private Label activeCountLabel;
    @FXML private Label regNumberLabel;

    private Apiary apiary;
    private TranslationManager tm;
    private int hiveCount = 0;
    private int activeCount = 0;

    /**
     * Constructor loads FXML and initializes the component.
     */
    public ApiaryCard() {
        tm = TranslationManager.getInstance();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/apiary_card.fxml"));
        loader.setController(this);

        try {
            VBox content = loader.load();
            getChildren().add(content);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load apiary_card.fxml", e);
        }

        // Apply card styling
        getStyleClass().add("apiary-card");
        setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #ddd; " +
            "-fx-border-width: 1; " +
            "-fx-border-radius: 10; " +
            "-fx-background-radius: 10; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 4, 0, 0, 2);"
        );

        // Hover effect
        setOnMouseEntered(e -> setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #2196F3; " +
            "-fx-border-width: 2; " +
            "-fx-border-radius: 10; " +
            "-fx-background-radius: 10; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(33,150,243,0.3), 8, 0, 0, 3); " +
            "-fx-cursor: hand;"
        ));

        setOnMouseExited(e -> setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #ddd; " +
            "-fx-border-width: 1; " +
            "-fx-border-radius: 10; " +
            "-fx-background-radius: 10; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 4, 0, 0, 2);"
        ));
    }

    /**
     * Set the apiary data and update the card display.
     *
     * @param apiary Apiary to display
     */
    public void setApiary(Apiary apiary) {
        this.apiary = apiary;
        updateDisplay();
    }

    /**
     * Set hive statistics for this apiary.
     *
     * @param totalHives Total number of hives
     * @param activeHives Number of active hives
     */
    public void setHiveStats(int totalHives, int activeHives) {
        this.hiveCount = totalHives;
        this.activeCount = activeHives;
        updateDisplay();
    }

    /**
     * Get the apiary associated with this card.
     *
     * @return Apiary entity
     */
    public Apiary getApiary() {
        return apiary;
    }

    /**
     * Update card display with apiary data.
     */
    private void updateDisplay() {
        if (apiary == null) return;

        // Apiary name
        apiaryName.setText(apiary.getName());

        // Location
        String location = apiary.getLocation();
        if (location != null && !location.trim().isEmpty()) {
            apiaryLocation.setText(location);
            apiaryLocation.setVisible(true);
        } else {
            apiaryLocation.setVisible(false);
        }

        // Hive count
        hiveCountLabel.setText(String.valueOf(hiveCount) + " " + tm.get("label.hives"));

        // Active count
        activeCountLabel.setText(activeCount + " " + tm.get("label.active"));

        // Registration number (if exists)
        String regNumber = apiary.getRegistrationNumber();
        if (regNumber != null && !regNumber.trim().isEmpty()) {
            regNumberLabel.setText(tm.get("label.reg_short") + ": " + regNumber);
            regNumberLabel.setVisible(true);
        } else {
            regNumberLabel.setVisible(false);
        }
    }

    /**
     * Highlight this card as selected.
     */
    public void setSelected(boolean selected) {
        if (selected) {
            setStyle(
                "-fx-background-color: #E3F2FD; " +
                "-fx-border-color: #2196F3; " +
                "-fx-border-width: 2; " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(33,150,243,0.4), 8, 0, 0, 3);"
            );
        } else {
            setStyle(
                "-fx-background-color: white; " +
                "-fx-border-color: #ddd; " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 10; " +
                "-fx-background-radius: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 4, 0, 0, 2);"
            );
        }
    }
}
