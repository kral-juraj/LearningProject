package com.beekeeper.desktop.controller;

import com.beekeeper.desktop.dao.jdbc.JdbcCalendarEventDao;
import com.beekeeper.desktop.dialog.CalendarEventDialog;
import com.beekeeper.desktop.scheduler.DesktopSchedulerProvider;
import com.beekeeper.desktop.util.EnumHelper;
import com.beekeeper.shared.entity.CalendarEvent;
import com.beekeeper.shared.i18n.TranslationManager;
import com.beekeeper.shared.repository.CalendarEventRepository;
import com.beekeeper.shared.util.DateUtils;
import com.beekeeper.shared.viewmodel.CalendarEventViewModel;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Optional;

/**
 * Controller for CalendarEvent list view.
 * Displays all calendar events with CRUD operations.
 */
public class CalendarEventListController {

    @FXML
    private TableView<CalendarEvent> eventTable;

    @FXML
    private TableColumn<CalendarEvent, Long> dateColumn;

    @FXML
    private TableColumn<CalendarEvent, String> titleColumn;

    @FXML
    private TableColumn<CalendarEvent, String> typeColumn;

    @FXML
    private TableColumn<CalendarEvent, String> descriptionColumn;

    @FXML
    private TableColumn<CalendarEvent, Boolean> completedColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button toggleButton;

    @FXML
    private Label statusLabel;

    private CalendarEventViewModel viewModel;
    private CompositeDisposable disposables = new CompositeDisposable();
    private ObservableList<CalendarEvent> eventList = FXCollections.observableArrayList();
    private TranslationManager tm;

    @FXML
    public void initialize() {
        // Get TranslationManager instance
        tm = TranslationManager.getInstance();

        // Initialize ViewModel
        JdbcCalendarEventDao calendarEventDao = new JdbcCalendarEventDao();
        CalendarEventRepository repository = new CalendarEventRepository(calendarEventDao);
        DesktopSchedulerProvider schedulerProvider = new DesktopSchedulerProvider();
        viewModel = new CalendarEventViewModel(repository, schedulerProvider);

        // Configure table columns
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("eventDate"));
        dateColumn.setCellFactory(column -> new TableCell<CalendarEvent, Long>() {
            @Override
            protected void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(DateUtils.formatDateTime(item));
                }
            }
        });
        // Set custom comparator to fix sorting (timestamp comparison)
        dateColumn.setComparator((t1, t2) -> {
            if (t1 == null && t2 == null) return 0;
            if (t1 == null) return -1;
            if (t2 == null) return 1;
            return Long.compare(t1, t2);
        });

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        typeColumn.setCellValueFactory(new PropertyValueFactory<>("eventType"));
        typeColumn.setCellFactory(column -> new TableCell<CalendarEvent, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(EnumHelper.getEventTypeLabel(item));
                }
            }
        });

        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        completedColumn.setCellValueFactory(new PropertyValueFactory<>("completed"));
        completedColumn.setCellFactory(column -> new TableCell<CalendarEvent, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? tm.get("common.yes") : tm.get("common.no"));
                }
            }
        });

        // Bind table to observable list
        eventTable.setItems(eventList);

        // Set default sorting: dateColumn descending (newest first)
        dateColumn.setSortType(TableColumn.SortType.DESCENDING);
        eventTable.getSortOrder().add(dateColumn);

        // Enable/disable buttons based on selection
        editButton.setDisable(true);
        deleteButton.setDisable(true);
        toggleButton.setDisable(true);

        // Defer setup to avoid macOS NSTrackingRectTag bug
        Platform.runLater(() -> {
            eventTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    boolean hasSelection = newSelection != null;
                    editButton.setDisable(!hasSelection);
                    deleteButton.setDisable(!hasSelection);
                    toggleButton.setDisable(!hasSelection);
                }
            );

            // Subscribe to ViewModel state
            subscribeToViewModel();

            // Load all events (including past ones)
            viewModel.loadAllEvents();
        });
    }

    private void subscribeToViewModel() {
        disposables.add(
            viewModel.getEvents()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(
                    events -> {
                        eventList.clear();
                        eventList.addAll(events);
                        statusLabel.setText(tm.get("status.events_count", events.size()));
                        statusLabel.setStyle("-fx-text-fill: black;");
                        // Reapply sorting after data reload
                        eventTable.sort();
                    },
                    error -> showError(tm.get("error.loading_events", error.getMessage()))
                )
        );

        disposables.add(
            viewModel.getLoading()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(loading -> {
                    addButton.setDisable(loading);
                    eventTable.setDisable(loading);
                })
        );

        disposables.add(
            viewModel.getError()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(this::showError)
        );

        disposables.add(
            viewModel.getSuccess()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(this::showSuccess)
        );
    }

    @FXML
    private void handleAddEvent() {
        CalendarEventDialog dialog = new CalendarEventDialog(null);
        Optional<CalendarEvent> result = dialog.showAndWait();

        result.ifPresent(event -> viewModel.createEvent(event));
    }

    @FXML
    private void handleEditEvent() {
        CalendarEvent selected = eventTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        CalendarEventDialog dialog = new CalendarEventDialog(selected);
        Optional<CalendarEvent> result = dialog.showAndWait();

        result.ifPresent(event -> viewModel.updateEvent(event));
    }

    @FXML
    private void handleDeleteEvent() {
        CalendarEvent selected = eventTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(tm.get("dialog.delete_event.title"));
        alert.setHeaderText(tm.get("dialog.delete_event.header", selected.getTitle()));
        alert.setContentText(tm.get("dialog.delete_event.content"));

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                viewModel.deleteEvent(selected);
            }
        });
    }

    @FXML
    private void handleToggleCompleted() {
        CalendarEvent selected = eventTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        viewModel.toggleCompleted(selected);
    }

    @FXML
    private void handleRefresh() {
        viewModel.loadAllEvents();
    }

    private void showError(String message) {
        statusLabel.setText(tm.get("error.prefix") + " " + message);
        statusLabel.setStyle("-fx-text-fill: red;");
    }

    private void showSuccess(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: green;");
    }

    public void cleanup() {
        disposables.clear();
        if (viewModel != null) {
            viewModel.dispose();
        }
    }
}
