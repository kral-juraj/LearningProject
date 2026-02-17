package com.beekeeper.desktop.dialog;

import com.beekeeper.desktop.dao.jdbc.JdbcHiveActivityDao;
import com.beekeeper.shared.entity.Hive;
import com.beekeeper.shared.entity.HiveActivity;
import com.beekeeper.shared.entity.HiveActivityType;
import com.beekeeper.shared.i18n.TranslationManager;
import com.beekeeper.shared.util.DateUtils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.Optional;

/**
 * Dialog for viewing and managing hive activity history.
 *
 * Use case: Display chronological list of all changes and events
 * for a specific hive, with ability to add/edit/delete activities.
 */
public class HiveActivityHistoryDialog extends Dialog<Void> {

    private TableView<HiveActivityRow> activityTable;
    private TableColumn<HiveActivityRow, String> dateColumn;
    private TableColumn<HiveActivityRow, String> typeColumn;
    private TableColumn<HiveActivityRow, String> oldValueColumn;
    private TableColumn<HiveActivityRow, String> newValueColumn;
    private TableColumn<HiveActivityRow, String> descriptionColumn;

    private Button addButton;
    private Button editButton;
    private Button deleteButton;
    private Label statusLabel;

    private Hive hive;
    private JdbcHiveActivityDao activityDao;
    private TranslationManager tm;
    private ObservableList<HiveActivityRow> activityList = FXCollections.observableArrayList();
    private CompositeDisposable disposables = new CompositeDisposable();

    public HiveActivityHistoryDialog(Hive hive) {
        this.hive = hive;
        this.activityDao = new JdbcHiveActivityDao();
        this.tm = TranslationManager.getInstance();

        setTitle(tm.get("dialog.hive_history.title", hive.getName()));
        setHeaderText(tm.get("dialog.hive_history.header"));

        // Create content
        BorderPane content = new BorderPane();
        content.setPrefSize(800, 500);

        // Top toolbar
        ToolBar toolbar = new ToolBar();
        addButton = new Button(tm.get("button.add_activity"));
        editButton = new Button(tm.get("button.edit"));
        deleteButton = new Button(tm.get("button.delete"));
        Button refreshButton = new Button(tm.get("button.refresh"));

        addButton.setOnAction(e -> handleAddActivity());
        editButton.setOnAction(e -> handleEditActivity());
        deleteButton.setOnAction(e -> handleDeleteActivity());
        refreshButton.setOnAction(e -> loadActivities());

        toolbar.getItems().addAll(addButton, editButton, deleteButton, new Separator(), refreshButton);
        content.setTop(toolbar);

        // Center table
        activityTable = new TableView<>();
        activityTable.setItems(activityList);

        dateColumn = new TableColumn<>(tm.get("label.date"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateFormatted"));
        dateColumn.setPrefWidth(120);

        typeColumn = new TableColumn<>(tm.get("label.activity_type"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("typeFormatted"));
        typeColumn.setPrefWidth(200);

        oldValueColumn = new TableColumn<>(tm.get("label.old_value"));
        oldValueColumn.setCellValueFactory(new PropertyValueFactory<>("oldValue"));
        oldValueColumn.setPrefWidth(120);

        newValueColumn = new TableColumn<>(tm.get("label.new_value"));
        newValueColumn.setCellValueFactory(new PropertyValueFactory<>("newValue"));
        newValueColumn.setPrefWidth(120);

        descriptionColumn = new TableColumn<>(tm.get("label.description"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionColumn.setPrefWidth(200);

        activityTable.getColumns().addAll(dateColumn, typeColumn, oldValueColumn, newValueColumn, descriptionColumn);
        content.setCenter(activityTable);

        // Bottom status bar
        HBox statusBar = new HBox();
        statusBar.setPadding(new Insets(8));
        statusBar.setStyle("-fx-background-color: #f9f9f9;");
        statusLabel = new Label();
        statusBar.getChildren().add(statusLabel);
        content.setBottom(statusBar);

        getDialogPane().setContent(content);

        // Close button
        ButtonType closeButton = new ButtonType(tm.get("button.close"), ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().add(closeButton);

        // Enable/disable buttons based on selection
        editButton.setDisable(true);
        deleteButton.setDisable(true);

        Platform.runLater(() -> {
            activityTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    boolean hasSelection = newSelection != null;
                    editButton.setDisable(!hasSelection);
                    deleteButton.setDisable(!hasSelection);
                }
            );

            loadActivities();
        });

        // Cleanup on close
        setOnCloseRequest(e -> disposables.clear());
    }

    /**
     * Load activities from database.
     */
    private void loadActivities() {
        statusLabel.setText(tm.get("status.loading"));

        disposables.add(
            activityDao.getByHiveId(hive.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(JavaFxScheduler.platform())
                .subscribe(
                    activities -> {
                        activityList.clear();
                        for (HiveActivity activity : activities) {
                            activityList.add(new HiveActivityRow(activity));
                        }
                        statusLabel.setText(tm.get("status.activities_count", activities.size()));
                    },
                    error -> {
                        statusLabel.setText(tm.get("error.loading_activities", error.getMessage()));
                        statusLabel.setStyle("-fx-text-fill: red;");
                    }
                )
        );
    }

    /**
     * Handle add activity button.
     */
    private void handleAddActivity() {
        HiveActivityDialog dialog = new HiveActivityDialog(null, hive.getId());
        Optional<HiveActivity> result = dialog.showAndWait();

        result.ifPresent(activity -> {
            disposables.add(
                activityDao.insert(activity)
                    .subscribeOn(Schedulers.io())
                    .observeOn(JavaFxScheduler.platform())
                    .subscribe(
                        () -> {
                            statusLabel.setText(tm.get("success.activity_created"));
                            statusLabel.setStyle("-fx-text-fill: green;");
                            loadActivities();
                        },
                        error -> {
                            statusLabel.setText(tm.get("error.saving_activity", error.getMessage()));
                            statusLabel.setStyle("-fx-text-fill: red;");
                        }
                    )
            );
        });
    }

    /**
     * Handle edit activity button.
     */
    private void handleEditActivity() {
        HiveActivityRow selected = activityTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        HiveActivityDialog dialog = new HiveActivityDialog(selected.activity, hive.getId());
        Optional<HiveActivity> result = dialog.showAndWait();

        result.ifPresent(activity -> {
            disposables.add(
                activityDao.update(activity)
                    .subscribeOn(Schedulers.io())
                    .observeOn(JavaFxScheduler.platform())
                    .subscribe(
                        () -> {
                            statusLabel.setText(tm.get("success.activity_updated"));
                            statusLabel.setStyle("-fx-text-fill: green;");
                            loadActivities();
                        },
                        error -> {
                            statusLabel.setText(tm.get("error.updating_activity", error.getMessage()));
                            statusLabel.setStyle("-fx-text-fill: red;");
                        }
                    )
            );
        });
    }

    /**
     * Handle delete activity button.
     */
    private void handleDeleteActivity() {
        HiveActivityRow selected = activityTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(tm.get("dialog.delete_activity.title"));
        alert.setHeaderText(tm.get("dialog.delete_activity.header"));
        alert.setContentText(tm.get("dialog.delete_activity.content"));

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                disposables.add(
                    activityDao.delete(selected.activity)
                        .subscribeOn(Schedulers.io())
                        .observeOn(JavaFxScheduler.platform())
                        .subscribe(
                            () -> {
                                statusLabel.setText(tm.get("success.activity_deleted"));
                                statusLabel.setStyle("-fx-text-fill: green;");
                                loadActivities();
                            },
                            error -> {
                                statusLabel.setText(tm.get("error.deleting_activity", error.getMessage()));
                                statusLabel.setStyle("-fx-text-fill: red;");
                            }
                        )
                );
            }
        });
    }

    /**
     * Wrapper class for displaying HiveActivity in TableView.
     * Provides formatted strings for date and type.
     */
    public class HiveActivityRow {
        private HiveActivity activity;
        private String dateFormatted;
        private String typeFormatted;

        public HiveActivityRow(HiveActivity activity) {
            this.activity = activity;
            this.dateFormatted = DateUtils.formatDate(activity.getActivityDate());

            HiveActivityType type = HiveActivityType.fromCode(activity.getActivityType());
            this.typeFormatted = type != null ? tm.get(type.getTranslationKey()) : activity.getActivityType();
        }

        public String getDateFormatted() {
            return dateFormatted;
        }

        public String getTypeFormatted() {
            return typeFormatted;
        }

        public String getOldValue() {
            return activity.getOldValue();
        }

        public String getNewValue() {
            return activity.getNewValue();
        }

        public String getDescription() {
            return activity.getDescription();
        }

        public HiveActivity getActivity() {
            return activity;
        }
    }
}
