package com.beekeeper.desktop.component;

import com.beekeeper.shared.entity.FrameType;
import com.beekeeper.shared.entity.Hive;
import com.beekeeper.shared.entity.HiveType;
import com.beekeeper.shared.i18n.TranslationManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Custom JavaFX component for displaying a hive as a compact card.
 *
 * Use case: Show hive details in a visually appealing card layout
 * with icons, badges, and quick action buttons.
 */
public class HiveCard extends VBox {

    @FXML private Label hiveIcon;
    @FXML private Label hiveName;
    @FXML private Label hiveTypeBadge;
    @FXML private Label frameCountLabel;
    @FXML private Label queenYearLabel;
    @FXML private Label activeIcon;
    @FXML private Label activeLabel;
    @FXML private HBox equipmentBox;
    @FXML private Button editButton;
    @FXML private Button historyButton;
    @FXML private Button inspectButton;

    private Hive hive;
    private TranslationManager tm;

    // Callbacks for button actions
    private Consumer<Hive> onEdit;
    private Consumer<Hive> onHistory;
    private Consumer<Hive> onInspect;

    /**
     * Constructor loads FXML and initializes the component.
     */
    public HiveCard() {
        tm = TranslationManager.getInstance();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/hive_card.fxml"));
        loader.setController(this);

        try {
            VBox content = loader.load();
            getChildren().add(content);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(tm.get("exception.failed_to_load_hive_card_fxml"), e);
        }

        // Apply card styling
        getStyleClass().add("hive-card");
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
     * Set the hive data and update the card display.
     *
     * @param hive Hive to display
     */
    public void setHive(Hive hive) {
        this.hive = hive;
        updateDisplay();
    }

    /**
     * Update card display with hive data.
     */
    private void updateDisplay() {
        if (hive == null) return;

        // Hive name
        hiveName.setText(hive.getName());

        // Hive icon based on type (realistic ASCII art representation)
        HiveType hiveType = HiveType.fromCode(hive.getType());
        if (hiveType != null) {
            switch (hiveType) {
                case VERTICAL:
                    // Nádstavkový úľ - 3 nadstavky vertikálne
                    hiveIcon.setText(
                        "┌───┐\n" +
                        "│███│\n" +
                        "├───┤\n" +
                        "│███│\n" +
                        "├───┤\n" +
                        "│███│\n" +
                        "└───┘"
                    );
                    hiveIcon.setStyle("-fx-font-size: 9px; -fx-font-family: 'Courier New', monospace; -fx-text-fill: #8B4513; -fx-line-spacing: -3px;");
                    break;
                case HORIZONTAL:
                    // Ležiak - dlhý horizontálny úľ
                    hiveIcon.setText(
                        "┌───────────┐\n" +
                        "│███████████│\n" +
                        "│███████████│\n" +
                        "└───────────┘"
                    );
                    hiveIcon.setStyle("-fx-font-size: 9px; -fx-font-family: 'Courier New', monospace; -fx-text-fill: #8B4513; -fx-line-spacing: -3px;");
                    break;
                case NUKE:
                    // Oddielka - menší úľ
                    hiveIcon.setText(
                        "┌─────┐\n" +
                        "│█████│\n" +
                        "│█████│\n" +
                        "└─────┘"
                    );
                    hiveIcon.setStyle("-fx-font-size: 9px; -fx-font-family: 'Courier New', monospace; -fx-text-fill: #8B4513; -fx-line-spacing: -3px;");
                    break;
                case MATING_NUC:
                    // Oplodňovačik - úplne malý úlik
                    hiveIcon.setText(
                        "┌──┐\n" +
                        "│██│\n" +
                        "└──┘"
                    );
                    hiveIcon.setStyle("-fx-font-size: 10px; -fx-font-family: 'Courier New', monospace; -fx-text-fill: #8B4513; -fx-line-spacing: -3px;");
                    break;
            }
            hiveTypeBadge.setText(tm.get(hiveType.getTranslationKey()));
        }

        // Frame count
        if (hive.getFrameCount() > 0) {
            frameCountLabel.setText(hive.getFrameCount() + " " + tm.get("label.frames"));
        } else {
            frameCountLabel.setText("-");
        }

        // Queen year
        if (hive.getQueenYear() > 0) {
            queenYearLabel.setText(String.valueOf(hive.getQueenYear()));
        } else {
            queenYearLabel.setText("-");
        }

        // Active status
        if (hive.isActive()) {
            activeIcon.setText("✅");
            activeLabel.setText(tm.get("label.active"));
            activeLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: green;");
        } else {
            activeIcon.setText("⏸️");
            activeLabel.setText(tm.get("label.inactive"));
            activeLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");
        }

        // Equipment badges (zobrazené na úvodnej obrazovke)
        equipmentBox.getChildren().clear();

        // Materská mriežka - najdôležitejšia info
        if (hive.isHasQueenExcluder()) {
            Label badge = createBadge("⚡", tm.get("label.queen_excluder"));
            equipmentBox.getChildren().add(badge);
        }

        if (hive.isInsulated()) {
            Label badge = createBadge("❄️", tm.get("label.insulated"));
            equipmentBox.getChildren().add(badge);
        }

        if (hive.isHasPropolisTrap()) {
            Label badge = createBadge("🍯", tm.get("label.propolis_trap"));
            equipmentBox.getChildren().add(badge);
        }

        // Stavebné rámiky - zobraz ak sú
        if (hive.getFoundationFrames() > 0) {
            Label badge = createBadge("🔨", tm.get("label.foundation_frames") + ": " + hive.getFoundationFrames());
            equipmentBox.getChildren().add(badge);
        }

        // Tooltips for buttons
        editButton.setTooltip(new Tooltip(tm.get("button.edit")));
        historyButton.setTooltip(new Tooltip(tm.get("button.view_history")));
        inspectButton.setTooltip(new Tooltip(tm.get("button.show_inspections")));
    }

    /**
     * Create a small badge label with icon and tooltip.
     */
    private Label createBadge(String icon, String tooltipText) {
        Label badge = new Label(icon);
        badge.setStyle(
            "-fx-font-size: 16px; " +
            "-fx-padding: 2;"
        );
        badge.setTooltip(new Tooltip(tooltipText));
        return badge;
    }

    // FXML Action handlers

    @FXML
    private void handleEdit() {
        if (onEdit != null && hive != null) {
            onEdit.accept(hive);
        }
    }

    @FXML
    private void handleHistory() {
        if (onHistory != null && hive != null) {
            onHistory.accept(hive);
        }
    }

    @FXML
    private void handleInspect() {
        if (onInspect != null && hive != null) {
            onInspect.accept(hive);
        }
    }

    // Setters for action callbacks

    public void setOnEdit(Consumer<Hive> callback) {
        this.onEdit = callback;
    }

    public void setOnHistory(Consumer<Hive> callback) {
        this.onHistory = callback;
    }

    public void setOnInspect(Consumer<Hive> callback) {
        this.onInspect = callback;
    }

    public Hive getHive() {
        return hive;
    }
}
