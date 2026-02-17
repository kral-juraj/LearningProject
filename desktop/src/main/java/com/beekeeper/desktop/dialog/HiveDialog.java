package com.beekeeper.desktop.dialog;

import com.beekeeper.shared.entity.FrameType;
import com.beekeeper.shared.entity.Hive;
import com.beekeeper.shared.entity.HiveType;
import com.beekeeper.shared.i18n.TranslationManager;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * Dialog for creating or editing a Hive with extended details.
 * Uses TabPane for organized, compact layout.
 *
 * Use case: Add/edit hive with comprehensive information.
 */
public class HiveDialog extends Dialog<Hive> {

    private TextField nameField;
    private ComboBox<HiveTypeItem> hiveTypeCombo;
    private CheckBox activeCheckBox;

    // Frame info
    private ComboBox<FrameTypeItem> frameTypeCombo;
    private Label frameCountLabel;  // Auto-calculated from details

    // Frame details
    private Spinner<Integer> darkFramesSpinner;
    private Spinner<Integer> lightFramesSpinner;
    private Spinner<Integer> newFramesSpinner;
    private Spinner<Integer> foundationFramesSpinner;
    private Spinner<Integer> emptyFramesSpinner;
    private Spinner<Integer> foundationSheetsSpinner;

    // Equipment
    private CheckBox insulatedCheckBox;
    private ComboBox<BottomBoardItem> bottomBoardCombo;
    private CheckBox queenExcluderCheckBox;
    private CheckBox propolisTrapCheckBox;
    private CheckBox entranceReducerCheckBox;
    private CheckBox pollenTrapCheckBox;
    private CheckBox topInsulationCheckBox;
    private CheckBox foilCheckBox;

    // Queen info
    private TextField queenIdField;
    private Spinner<Integer> queenYearSpinner;
    private TextField queenColorField;
    private ComboBox<AggressionItem> aggressionCombo;
    private CheckBox chalkBroodCheckBox;
    private CheckBox droneCellsCheckBox;
    private CheckBox droneLayingCheckBox;

    // Notes
    private TextArea notesArea;

    private Hive existingHive;
    private TranslationManager tm;

    public HiveDialog(Hive hive) {
        this.existingHive = hive;
        this.tm = TranslationManager.getInstance();

        setTitle(hive == null ? tm.get("dialog.add_hive.title") : tm.get("dialog.edit_hive.title"));
        setHeaderText(hive == null ? tm.get("dialog.hive.header_add") : tm.get("dialog.hive.header_edit"));

        // Create TabPane
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setPrefSize(550, 400);

        // Tab 1: Basic Info & Equipment
        Tab basicTab = new Tab(tm.get("label.basic_info_equipment"));
        basicTab.setContent(createBasicInfoEquipmentPanel());

        // Tab 2: Frames
        Tab framesTab = new Tab(tm.get("label.frames"));
        framesTab.setContent(createFramesPanel());

        // Tab 3: Queen
        Tab queenTab = new Tab(tm.get("label.queen"));
        queenTab.setContent(createQueenPanel());

        // Tab 4: Notes
        Tab notesTab = new Tab(tm.get("label.notes"));
        notesTab.setContent(createNotesPanel());

        tabPane.getTabs().addAll(basicTab, framesTab, queenTab, notesTab);
        getDialogPane().setContent(tabPane);

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
        if (existingHive != null) {
            populateFields();
            saveBtn.setDisable(nameField.getText() == null || nameField.getText().trim().isEmpty());
        } else {
            activeCheckBox.setSelected(true);
        }

        // Result converter
        setResultConverter(buttonType -> {
            if (buttonType == saveButton) {
                return createHiveFromInput();
            }
            return null;
        });
    }

    /**
     * Create Basic Info & Equipment panel (Tab 1).
     */
    private VBox createBasicInfoEquipmentPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(15));

        // Basic info section
        GridPane basicGrid = new GridPane();
        basicGrid.setHgap(10);
        basicGrid.setVgap(10);

        // Name
        Label nameLabel = new Label(tm.get("label.name") + " *");
        nameField = new TextField();
        nameField.setPromptText(tm.get("prompt.required"));
        basicGrid.add(nameLabel, 0, 0);
        basicGrid.add(nameField, 1, 0);

        // Hive type
        Label typeLabel = new Label(tm.get("label.hive_type"));
        hiveTypeCombo = new ComboBox<>();
        hiveTypeCombo.getItems().addAll(
            new HiveTypeItem(HiveType.VERTICAL),
            new HiveTypeItem(HiveType.HORIZONTAL),
            new HiveTypeItem(HiveType.NUKE),
            new HiveTypeItem(HiveType.MATING_NUC)
        );
        hiveTypeCombo.getSelectionModel().selectFirst();
        basicGrid.add(typeLabel, 0, 1);
        basicGrid.add(hiveTypeCombo, 1, 1);

        // Bottom board type
        Label bottomBoardLabel = new Label(tm.get("label.bottom_board"));
        bottomBoardCombo = new ComboBox<>();
        bottomBoardCombo.getItems().addAll(
            new BottomBoardItem(true),
            new BottomBoardItem(false)
        );
        bottomBoardCombo.getSelectionModel().selectFirst();
        basicGrid.add(bottomBoardLabel, 0, 2);
        basicGrid.add(bottomBoardCombo, 1, 2);

        // Active
        activeCheckBox = new CheckBox(tm.get("label.active"));
        basicGrid.add(activeCheckBox, 0, 3, 2, 1);

        panel.getChildren().add(basicGrid);

        // Equipment section
        Label equipLabel = new Label(tm.get("label.equipment"));
        equipLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        panel.getChildren().add(equipLabel);

        GridPane equipGrid = new GridPane();
        equipGrid.setHgap(15);
        equipGrid.setVgap(8);

        // Column 0
        insulatedCheckBox = new CheckBox(tm.get("label.insulated"));
        equipGrid.add(insulatedCheckBox, 0, 0);

        queenExcluderCheckBox = new CheckBox(tm.get("label.queen_excluder"));
        equipGrid.add(queenExcluderCheckBox, 0, 1);

        propolisTrapCheckBox = new CheckBox(tm.get("label.propolis_trap"));
        equipGrid.add(propolisTrapCheckBox, 0, 2);

        // Column 1
        entranceReducerCheckBox = new CheckBox(tm.get("label.entrance_reducer"));
        equipGrid.add(entranceReducerCheckBox, 1, 0);

        pollenTrapCheckBox = new CheckBox(tm.get("label.pollen_trap"));
        equipGrid.add(pollenTrapCheckBox, 1, 1);

        topInsulationCheckBox = new CheckBox(tm.get("label.top_insulation"));
        equipGrid.add(topInsulationCheckBox, 1, 2);

        foilCheckBox = new CheckBox(tm.get("label.foil"));
        equipGrid.add(foilCheckBox, 1, 3);

        panel.getChildren().add(equipGrid);
        return panel;
    }

    /**
     * Create Frames panel (Tab 2).
     */
    private VBox createFramesPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(15));

        // Frame type
        GridPane frameTypeGrid = new GridPane();
        frameTypeGrid.setHgap(10);
        frameTypeGrid.setVgap(10);

        Label frameTypeLabel = new Label(tm.get("label.frame_type"));
        frameTypeCombo = new ComboBox<>();
        frameTypeCombo.getItems().addAll(
            new FrameTypeItem(FrameType.B),
            new FrameTypeItem(FrameType.LANGSTROTH),
            new FrameTypeItem(FrameType.DADANT),
            new FrameTypeItem(FrameType.ZANDER)
        );
        frameTypeGrid.add(frameTypeLabel, 0, 0);
        frameTypeGrid.add(frameTypeCombo, 1, 0);

        // Total frame count (auto-calculated, readonly)
        Label totalLabel = new Label(tm.get("label.frame_count_total"));
        frameCountLabel = new Label("0");
        frameCountLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2196F3;");
        frameTypeGrid.add(totalLabel, 0, 1);
        frameTypeGrid.add(frameCountLabel, 1, 1);

        panel.getChildren().add(frameTypeGrid);

        // Frame details
        Label detailsLabel = new Label(tm.get("label.frame_details"));
        detailsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        panel.getChildren().add(detailsLabel);

        GridPane detailsGrid = new GridPane();
        detailsGrid.setHgap(10);
        detailsGrid.setVgap(8);

        // Create spinners with auto-update listener
        Runnable updateTotal = this::updateFrameCountTotal;

        darkFramesSpinner = createFrameSpinner(tm.get("label.dark_frames"), detailsGrid, 0, updateTotal);
        lightFramesSpinner = createFrameSpinner(tm.get("label.light_frames"), detailsGrid, 1, updateTotal);
        newFramesSpinner = createFrameSpinner(tm.get("label.new_frames"), detailsGrid, 2, updateTotal);
        foundationFramesSpinner = createFrameSpinner(tm.get("label.foundation_frames"), detailsGrid, 3, updateTotal);
        emptyFramesSpinner = createFrameSpinner(tm.get("label.empty_frames"), detailsGrid, 4, updateTotal);
        foundationSheetsSpinner = createFrameSpinner(tm.get("label.foundation_sheets"), detailsGrid, 5, updateTotal);

        panel.getChildren().add(detailsGrid);
        return panel;
    }

    /**
     * Helper to create frame detail spinner with auto-update.
     */
    private Spinner<Integer> createFrameSpinner(String label, GridPane grid, int row, Runnable onChange) {
        Label lbl = new Label(label);
        Spinner<Integer> spinner = new Spinner<>(0, 100, 0);
        spinner.setEditable(true);
        spinner.setPrefWidth(80);
        spinner.valueProperty().addListener((obs, old, newVal) -> onChange.run());
        grid.add(lbl, 0, row);
        grid.add(spinner, 1, row);
        return spinner;
    }

    /**
     * Update total frame count label.
     */
    private void updateFrameCountTotal() {
        int total = darkFramesSpinner.getValue() +
                    lightFramesSpinner.getValue() +
                    newFramesSpinner.getValue() +
                    foundationFramesSpinner.getValue() +
                    emptyFramesSpinner.getValue() +
                    foundationSheetsSpinner.getValue();
        frameCountLabel.setText(String.valueOf(total));
    }

    /**
     * Create Queen panel (Tab 3).
     */
    private VBox createQueenPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(15));

        // Queen info section
        Label queenLabel = new Label(tm.get("label.queen_info"));
        queenLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        panel.getChildren().add(queenLabel);

        GridPane queenGrid = new GridPane();
        queenGrid.setHgap(10);
        queenGrid.setVgap(10);

        // Queen ID
        Label queenIdLabel = new Label(tm.get("label.queen_id"));
        queenIdField = new TextField();
        queenGrid.add(queenIdLabel, 0, 0);
        queenGrid.add(queenIdField, 1, 0);

        // Queen year
        Label queenYearLabel = new Label(tm.get("label.queen_year"));
        queenYearSpinner = new Spinner<>(2000, 2030, 2026);
        queenYearSpinner.setEditable(true);
        queenGrid.add(queenYearLabel, 0, 1);
        queenGrid.add(queenYearSpinner, 1, 1);

        // Queen color
        Label queenColorLabel = new Label(tm.get("label.queen_color"));
        queenColorField = new TextField();
        queenGrid.add(queenColorLabel, 0, 2);
        queenGrid.add(queenColorField, 1, 2);

        // Aggression level
        Label aggressionLabel = new Label(tm.get("label.aggression"));
        aggressionCombo = new ComboBox<>();
        aggressionCombo.getItems().addAll(
            new AggressionItem("LOW"),
            new AggressionItem("MEDIUM"),
            new AggressionItem("HIGH")
        );
        aggressionCombo.getSelectionModel().selectFirst();
        queenGrid.add(aggressionLabel, 0, 3);
        queenGrid.add(aggressionCombo, 1, 3);

        panel.getChildren().add(queenGrid);

        // Colony health indicators
        Label healthLabel = new Label(tm.get("label.colony_health"));
        healthLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        panel.getChildren().add(healthLabel);

        GridPane healthGrid = new GridPane();
        healthGrid.setHgap(10);
        healthGrid.setVgap(8);

        chalkBroodCheckBox = new CheckBox(tm.get("label.chalk_brood"));
        healthGrid.add(chalkBroodCheckBox, 0, 0);

        droneCellsCheckBox = new CheckBox(tm.get("label.drone_cells"));
        healthGrid.add(droneCellsCheckBox, 0, 1);

        droneLayingCheckBox = new CheckBox(tm.get("label.drone_laying_queen"));
        healthGrid.add(droneLayingCheckBox, 0, 2);

        panel.getChildren().add(healthGrid);

        return panel;
    }

    /**
     * Create Notes panel (Tab 4).
     */
    private VBox createNotesPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(15));

        // Notes
        Label notesLabel = new Label(tm.get("label.notes"));
        notesLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        notesArea = new TextArea();
        notesArea.setPrefRowCount(12);
        notesArea.setWrapText(true);
        panel.getChildren().addAll(notesLabel, notesArea);

        return panel;
    }

    /**
     * Populate fields with existing hive data.
     */
    private void populateFields() {
        nameField.setText(existingHive.getName());

        HiveType hiveType = HiveType.fromCode(existingHive.getType());
        if (hiveType != null) {
            for (HiveTypeItem item : hiveTypeCombo.getItems()) {
                if (item.type == hiveType) {
                    hiveTypeCombo.getSelectionModel().select(item);
                    break;
                }
            }
        }

        FrameType frameType = FrameType.fromCode(existingHive.getFrameType());
        if (frameType != null) {
            for (FrameTypeItem item : frameTypeCombo.getItems()) {
                if (item.type == frameType) {
                    frameTypeCombo.getSelectionModel().select(item);
                    break;
                }
            }
        }

        insulatedCheckBox.setSelected(existingHive.isInsulated());

        for (BottomBoardItem item : bottomBoardCombo.getItems()) {
            if (item.isHigh == existingHive.isHighBottomBoard()) {
                bottomBoardCombo.getSelectionModel().select(item);
                break;
            }
        }

        queenExcluderCheckBox.setSelected(existingHive.isHasQueenExcluder());
        propolisTrapCheckBox.setSelected(existingHive.isHasPropolisTrap());
        entranceReducerCheckBox.setSelected(existingHive.isHasEntranceReducer());
        pollenTrapCheckBox.setSelected(existingHive.isHasPollenTrap());
        topInsulationCheckBox.setSelected(existingHive.isHasTopInsulation());
        foilCheckBox.setSelected(existingHive.isHasFoil());

        // Frame details
        darkFramesSpinner.getValueFactory().setValue(existingHive.getDarkFrames());
        lightFramesSpinner.getValueFactory().setValue(existingHive.getLightFrames());
        newFramesSpinner.getValueFactory().setValue(existingHive.getNewFrames());
        foundationFramesSpinner.getValueFactory().setValue(existingHive.getFoundationFrames());
        emptyFramesSpinner.getValueFactory().setValue(existingHive.getEmptyFrames());
        foundationSheetsSpinner.getValueFactory().setValue(existingHive.getFoundationSheets());

        queenIdField.setText(existingHive.getQueenId());
        queenYearSpinner.getValueFactory().setValue(existingHive.getQueenYear() > 0 ? existingHive.getQueenYear() : 2026);
        queenColorField.setText(existingHive.getQueenColor());

        // Aggression level
        if (existingHive.getAggression() != null) {
            for (AggressionItem item : aggressionCombo.getItems()) {
                if (item.level.equals(existingHive.getAggression())) {
                    aggressionCombo.getSelectionModel().select(item);
                    break;
                }
            }
        }

        // Colony health indicators
        chalkBroodCheckBox.setSelected(existingHive.isChalkBrood());
        droneCellsCheckBox.setSelected(existingHive.isDroneCells());
        droneLayingCheckBox.setSelected(existingHive.isDroneLaying());

        activeCheckBox.setSelected(existingHive.isActive());
        notesArea.setText(existingHive.getNotes());

        // Update total after loading
        updateFrameCountTotal();
    }

    /**
     * Create Hive object from form inputs.
     */
    private Hive createHiveFromInput() {
        Hive hive = existingHive != null ? existingHive : new Hive();

        hive.setName(nameField.getText().trim());

        HiveTypeItem hiveTypeItem = hiveTypeCombo.getSelectionModel().getSelectedItem();
        if (hiveTypeItem != null) {
            hive.setType(hiveTypeItem.type.getCode());
        }

        FrameTypeItem frameTypeItem = frameTypeCombo.getSelectionModel().getSelectedItem();
        if (frameTypeItem != null) {
            hive.setFrameType(frameTypeItem.type.getCode());
        }

        // Calculate total frame count from details
        int totalFrames = darkFramesSpinner.getValue() +
                          lightFramesSpinner.getValue() +
                          newFramesSpinner.getValue() +
                          foundationFramesSpinner.getValue() +
                          emptyFramesSpinner.getValue() +
                          foundationSheetsSpinner.getValue();
        hive.setFrameCount(totalFrames);

        hive.setInsulated(insulatedCheckBox.isSelected());

        BottomBoardItem bottomBoardItem = bottomBoardCombo.getSelectionModel().getSelectedItem();
        if (bottomBoardItem != null) {
            hive.setHighBottomBoard(bottomBoardItem.isHigh);
        }

        hive.setHasQueenExcluder(queenExcluderCheckBox.isSelected());
        hive.setHasPropolisTrap(propolisTrapCheckBox.isSelected());
        hive.setHasEntranceReducer(entranceReducerCheckBox.isSelected());
        hive.setHasPollenTrap(pollenTrapCheckBox.isSelected());
        hive.setHasTopInsulation(topInsulationCheckBox.isSelected());
        hive.setHasFoil(foilCheckBox.isSelected());

        // Frame details
        hive.setDarkFrames(darkFramesSpinner.getValue());
        hive.setLightFrames(lightFramesSpinner.getValue());
        hive.setNewFrames(newFramesSpinner.getValue());
        hive.setFoundationFrames(foundationFramesSpinner.getValue());
        hive.setEmptyFrames(emptyFramesSpinner.getValue());
        hive.setFoundationSheets(foundationSheetsSpinner.getValue());

        hive.setQueenId(queenIdField.getText());
        hive.setQueenYear(queenYearSpinner.getValue());
        hive.setQueenColor(queenColorField.getText());

        // Aggression level
        AggressionItem aggressionItem = aggressionCombo.getSelectionModel().getSelectedItem();
        if (aggressionItem != null) {
            hive.setAggression(aggressionItem.level);
        }

        // Colony health indicators
        hive.setChalkBrood(chalkBroodCheckBox.isSelected());
        hive.setDroneCells(droneCellsCheckBox.isSelected());
        hive.setDroneLaying(droneLayingCheckBox.isSelected());

        hive.setActive(activeCheckBox.isSelected());
        hive.setNotes(notesArea.getText() != null ? notesArea.getText().trim() : "");

        return hive;
    }

    /**
     * Helper class for HiveType ComboBox items.
     */
    private class HiveTypeItem {
        HiveType type;

        HiveTypeItem(HiveType type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return tm.get(type.getTranslationKey());
        }
    }

    /**
     * Helper class for FrameType ComboBox items.
     */
    private class FrameTypeItem {
        FrameType type;

        FrameTypeItem(FrameType type) {
            this.type = type;
        }

        @Override
        public String toString() {
            String name = tm.get(type.getTranslationKey());
            String dims = type.getDimensions();
            return dims.isEmpty() ? name : name + " (" + dims + ")";
        }
    }

    /**
     * Helper class for bottom board ComboBox items.
     */
    private class BottomBoardItem {
        boolean isHigh;

        BottomBoardItem(boolean isHigh) {
            this.isHigh = isHigh;
        }

        @Override
        public String toString() {
            return isHigh ? tm.get("label.high_bottom_board") : tm.get("label.low_bottom_board");
        }
    }

    /**
     * Helper class for aggression level ComboBox items.
     */
    private class AggressionItem {
        String level;

        AggressionItem(String level) {
            this.level = level;
        }

        @Override
        public String toString() {
            return tm.get("aggression." + level.toLowerCase());
        }
    }
}
