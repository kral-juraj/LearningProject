package com.beekeeper.desktop.dialog;

import com.beekeeper.desktop.i18n.I18nResourceBundle;
import com.beekeeper.desktop.util.DateTimeConverter;
import com.beekeeper.desktop.util.DatePickerFormatter;
import com.beekeeper.desktop.util.EnumHelper;
import com.beekeeper.desktop.util.ValidationHelper;
import com.beekeeper.shared.entity.Taxation;
import com.beekeeper.shared.entity.TaxationFrame;
import com.beekeeper.shared.i18n.TranslationManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Dialog for creating or editing a Taxation with its TaxationFrames.
 * Master-detail dialog with embedded frame table.
 */
public class TaxationDialog extends Dialog<TaxationWithFrames> {

    // Header fields
    private DatePicker datePicker;
    private TextField hourField;
    private TextField minuteField;
    private TextField temperatureField;
    private TextField totalFramesField;
    private TextField foodStoresField;
    private TextArea notesArea;

    // Frame management
    private Button addFrameButton;
    private Button editFrameButton;
    private Button deleteFrameButton;
    private TableView<TaxationFrame> frameTable;
    private TableColumn<TaxationFrame, Integer> positionColumn;
    private TableColumn<TaxationFrame, String> frameTypeColumn;
    private TableColumn<TaxationFrame, Integer> cappedBroodColumn;
    private TableColumn<TaxationFrame, Integer> uncappedBroodColumn;
    private TableColumn<TaxationFrame, Integer> pollenColumn;

    private Taxation existingTaxation;
    private String hiveId;
    private String temporaryTaxationId; // For new taxations
    private ObservableList<TaxationFrame> frameList = FXCollections.observableArrayList();
    private TranslationManager tm;

    public TaxationDialog(Taxation taxation, List<TaxationFrame> frames, String hiveId) {
        this.existingTaxation = taxation;
        this.hiveId = hiveId;
        this.tm = TranslationManager.getInstance();

        // Generate temporary ID for new taxation so frames can reference it
        if (taxation == null) {
            this.temporaryTaxationId = java.util.UUID.randomUUID().toString();
        } else {
            this.temporaryTaxationId = taxation.getId();
        }

        if (frames != null) {
            frameList.addAll(frames);
        }

        initDialog();
        setupFrameTable();
        populateFields();
    }

    private void initDialog() {
        setTitle(existingTaxation == null ? tm.get("dialog.add_taxation.title") : tm.get("dialog.edit_taxation.title"));
        initModality(Modality.APPLICATION_MODAL);
        setResizable(true);

        ButtonType saveButtonType = new ButtonType(tm.get("button.save"), ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType(tm.get("button.cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/taxation_dialog.fxml"));
            loader.setResources(new I18nResourceBundle(tm));
            VBox vbox = loader.load();
            getDialogPane().setContent(vbox);

            // Get references to header fields
            datePicker = (DatePicker) vbox.lookup("#datePicker");
            hourField = (TextField) vbox.lookup("#hourField");
            minuteField = (TextField) vbox.lookup("#minuteField");
            temperatureField = (TextField) vbox.lookup("#temperatureField");
            totalFramesField = (TextField) vbox.lookup("#totalFramesField");
            foodStoresField = (TextField) vbox.lookup("#foodStoresField");
            notesArea = (TextArea) vbox.lookup("#notesArea");

            // Format DatePicker with user's preferred date format
            DatePickerFormatter.format(datePicker);

            // Get references to frame management components
            // Note: Buttons are inside ToolBar, so we need to search within children
            for (javafx.scene.Node node : vbox.getChildren()) {
                if (node instanceof ToolBar) {
                    ToolBar toolbar = (ToolBar) node;
                    for (javafx.scene.Node item : toolbar.getItems()) {
                        if (item instanceof Button) {
                            Button btn = (Button) item;
                            String id = btn.getId();
                            if ("addFrameButton".equals(id)) {
                                addFrameButton = btn;
                            } else if ("editFrameButton".equals(id)) {
                                editFrameButton = btn;
                            } else if ("deleteFrameButton".equals(id)) {
                                deleteFrameButton = btn;
                            }
                        }
                    }
                }
            }

            @SuppressWarnings("unchecked")
            TableView<TaxationFrame> table = (TableView<TaxationFrame>) vbox.lookup("#frameTable");
            frameTable = table;

            @SuppressWarnings("unchecked")
            TableColumn<TaxationFrame, Integer> posCol = (TableColumn<TaxationFrame, Integer>) frameTable.getColumns().get(0);
            positionColumn = posCol;

            @SuppressWarnings("unchecked")
            TableColumn<TaxationFrame, String> typeCol = (TableColumn<TaxationFrame, String>) frameTable.getColumns().get(1);
            frameTypeColumn = typeCol;

            @SuppressWarnings("unchecked")
            TableColumn<TaxationFrame, Integer> cappedCol = (TableColumn<TaxationFrame, Integer>) frameTable.getColumns().get(2);
            cappedBroodColumn = cappedCol;

            @SuppressWarnings("unchecked")
            TableColumn<TaxationFrame, Integer> uncappedCol = (TableColumn<TaxationFrame, Integer>) frameTable.getColumns().get(3);
            uncappedBroodColumn = uncappedCol;

            @SuppressWarnings("unchecked")
            TableColumn<TaxationFrame, Integer> polCol = (TableColumn<TaxationFrame, Integer>) frameTable.getColumns().get(4);
            pollenColumn = polCol;

            // Connect button event handlers programmatically
            addFrameButton.setOnAction(event -> handleAddFrame());
            editFrameButton.setOnAction(event -> handleEditFrame());
            deleteFrameButton.setOnAction(event -> handleDeleteFrame());

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
                return createResultFromInput();
            }
            return null;
        });
    }

    private void setupFrameTable() {
        // Configure table columns
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));

        frameTypeColumn.setCellValueFactory(new PropertyValueFactory<>("frameType"));
        frameTypeColumn.setCellFactory(column -> new TableCell<TaxationFrame, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(EnumHelper.getFrameTypeLabel(item));
                }
            }
        });

        cappedBroodColumn.setCellValueFactory(new PropertyValueFactory<>("cappedBroodDm"));
        uncappedBroodColumn.setCellValueFactory(new PropertyValueFactory<>("uncappedBroodDm"));
        pollenColumn.setCellValueFactory(new PropertyValueFactory<>("pollenDm"));

        // Bind table to observable list
        frameTable.setItems(frameList);

        // Enable/disable buttons based on selection
        editFrameButton.setDisable(true);
        deleteFrameButton.setDisable(true);

        Platform.runLater(() -> {
            frameTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    boolean hasSelection = newSelection != null;
                    editFrameButton.setDisable(!hasSelection);
                    deleteFrameButton.setDisable(!hasSelection);
                }
            );
        });
    }

    private void populateFields() {
        if (existingTaxation != null) {
            // Date and time
            LocalDate date = DateTimeConverter.toLocalDate(existingTaxation.getTaxationDate());
            datePicker.setValue(date);
            hourField.setText(String.valueOf(DateTimeConverter.extractHour(existingTaxation.getTaxationDate())));
            minuteField.setText(String.valueOf(DateTimeConverter.extractMinute(existingTaxation.getTaxationDate())));

            // Header fields
            if (existingTaxation.getTemperature() > 0) {
                temperatureField.setText(String.valueOf(existingTaxation.getTemperature()));
            }
            if (existingTaxation.getTotalFrames() > 0) {
                totalFramesField.setText(String.valueOf(existingTaxation.getTotalFrames()));
            }
            if (existingTaxation.getFoodStoresKg() > 0) {
                foodStoresField.setText(String.valueOf(existingTaxation.getFoodStoresKg()));
            }

            notesArea.setText(existingTaxation.getNotes());
        } else {
            // Default values for new taxation
            datePicker.setValue(LocalDate.now());
            hourField.setText("12");
            minuteField.setText("0");
        }
    }

    public void handleAddFrame() {
        TaxationFrameDialog dialog = new TaxationFrameDialog(null, temporaryTaxationId);
        Optional<TaxationFrame> result = dialog.showAndWait();

        result.ifPresent(frame -> frameList.add(frame));
    }

    public void handleEditFrame() {
        TaxationFrame selected = frameTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        TaxationFrameDialog dialog = new TaxationFrameDialog(selected, temporaryTaxationId);
        Optional<TaxationFrame> result = dialog.showAndWait();

        result.ifPresent(frame -> {
            int index = frameList.indexOf(selected);
            if (index >= 0) {
                frameList.set(index, frame);
            }
        });
    }

    public void handleDeleteFrame() {
        TaxationFrame selected = frameTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(tm.get("dialog.delete_frame.title"));
        alert.setHeaderText(tm.get("dialog.delete_frame.header", selected.getPosition()));
        alert.setContentText(tm.get("dialog.delete_frame.content"));

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                frameList.remove(selected);
            }
        });
    }

    private boolean validateInput() {
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

        // Validate optional numeric fields
        if (!temperatureField.getText().trim().isEmpty() && !ValidationHelper.isValidDouble(temperatureField.getText())) {
            ValidationHelper.showValidationError(tm.get("validation.temperature_must_be_number"));
            return false;
        }

        if (!totalFramesField.getText().trim().isEmpty() && !ValidationHelper.isValidInteger(totalFramesField.getText())) {
            ValidationHelper.showValidationError(tm.get("validation.total_frames_must_be_number"));
            return false;
        }

        if (!foodStoresField.getText().trim().isEmpty() && !ValidationHelper.isValidDouble(foodStoresField.getText())) {
            ValidationHelper.showValidationError(tm.get("validation.food_stores_must_be_number"));
            return false;
        }

        return true;
    }

    private TaxationWithFrames createResultFromInput() {
        Taxation taxation = existingTaxation != null ? existingTaxation : new Taxation();

        // Set the temporary ID for new taxations (ViewModel will keep this ID)
        if (taxation.getId() == null || taxation.getId().isEmpty()) {
            taxation.setId(temporaryTaxationId);
        }

        taxation.setHiveId(hiveId);

        // Date and time
        LocalDate date = datePicker.getValue();
        int hour = ValidationHelper.parseInt(hourField.getText());
        int minute = ValidationHelper.parseInt(minuteField.getText());
        taxation.setTaxationDate(DateTimeConverter.toTimestamp(date, hour, minute));

        // Header fields
        taxation.setTemperature(ValidationHelper.parseDouble(temperatureField.getText()));
        taxation.setTotalFrames(ValidationHelper.parseInt(totalFramesField.getText()));
        taxation.setFoodStoresKg(ValidationHelper.parseDouble(foodStoresField.getText()));
        taxation.setNotes(notesArea.getText() != null ? notesArea.getText().trim() : "");

        return new TaxationWithFrames(taxation, new ArrayList<>(frameList));
    }
}
