package com.beekeeper.desktop.controller;

import com.beekeeper.desktop.dao.jdbc.JdbcFeedingDao;
import com.beekeeper.desktop.dialog.FeedingDialog;
import com.beekeeper.desktop.scheduler.DesktopSchedulerProvider;
import com.beekeeper.desktop.util.EnumHelper;
import com.beekeeper.shared.entity.Feeding;
import com.beekeeper.shared.repository.FeedingRepository;
import com.beekeeper.shared.util.DateUtils;
import com.beekeeper.shared.viewmodel.FeedingViewModel;
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
 * Controller for Feeding list view.
 * Displays feedings for a selected hive.
 */
public class FeedingListController {

    @FXML
    private TableView<Feeding> feedingTable;

    @FXML
    private TableColumn<Feeding, Long> dateColumn;

    @FXML
    private TableColumn<Feeding, String> feedTypeColumn;

    @FXML
    private TableColumn<Feeding, Double> amountColumn;

    @FXML
    private TableColumn<Feeding, Double> weightBeforeColumn;

    @FXML
    private TableColumn<Feeding, Double> weightAfterColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Label statusLabel;

    private FeedingViewModel viewModel;
    private CompositeDisposable disposables = new CompositeDisposable();
    private ObservableList<Feeding> feedingList = FXCollections.observableArrayList();
    private String currentHiveId;

    @FXML
    public void initialize() {
        // Initialize ViewModel
        JdbcFeedingDao feedingDao = new JdbcFeedingDao();
        FeedingRepository repository = new FeedingRepository(feedingDao);
        DesktopSchedulerProvider schedulerProvider = new DesktopSchedulerProvider();
        viewModel = new FeedingViewModel(repository, schedulerProvider);

        // Configure table columns
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("feedingDate"));
        dateColumn.setCellFactory(column -> new TableCell<Feeding, Long>() {
            @Override
            protected void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(DateUtils.formatDate(item));
                }
            }
        });

        feedTypeColumn.setCellValueFactory(new PropertyValueFactory<>("feedType"));
        feedTypeColumn.setCellFactory(column -> new TableCell<Feeding, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(EnumHelper.getFeedTypeLabel(item));
                }
            }
        });

        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amountKg"));
        weightBeforeColumn.setCellValueFactory(new PropertyValueFactory<>("weightBefore"));
        weightAfterColumn.setCellValueFactory(new PropertyValueFactory<>("weightAfter"));

        // Bind table to observable list
        feedingTable.setItems(feedingList);

        // Enable/disable buttons based on selection
        editButton.setDisable(true);
        deleteButton.setDisable(true);

        // Defer setup to avoid macOS NSTrackingRectTag bug
        Platform.runLater(() -> {
            feedingTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    boolean hasSelection = newSelection != null;
                    editButton.setDisable(!hasSelection);
                    deleteButton.setDisable(!hasSelection);
                }
            );

            // Subscribe to ViewModel state
            subscribeToViewModel();
        });
    }

    public void setHiveId(String hiveId) {
        this.currentHiveId = hiveId;
        if (viewModel != null) {
            viewModel.loadFeedingsByHiveId(hiveId);
        }
    }

    private void subscribeToViewModel() {
        disposables.add(
            viewModel.getFeedings()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(
                    feedings -> {
                        feedingList.clear();
                        feedingList.addAll(feedings);
                        statusLabel.setText(feedings.size() + " kŕmení");
                        statusLabel.setStyle("-fx-text-fill: black;");
                    },
                    error -> showError("Chyba: " + error.getMessage())
                )
        );

        disposables.add(
            viewModel.getLoading()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(loading -> {
                    addButton.setDisable(loading);
                    feedingTable.setDisable(loading);
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
    private void handleAddFeeding() {
        if (currentHiveId == null) {
            showError("Najprv vyberte úľ");
            return;
        }

        FeedingDialog dialog = new FeedingDialog(null, currentHiveId);
        Optional<Feeding> result = dialog.showAndWait();

        result.ifPresent(feeding -> viewModel.createFeeding(feeding));
    }

    @FXML
    private void handleEditFeeding() {
        Feeding selected = feedingTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        FeedingDialog dialog = new FeedingDialog(selected, currentHiveId);
        Optional<Feeding> result = dialog.showAndWait();

        result.ifPresent(feeding -> viewModel.updateFeeding(feeding));
    }

    @FXML
    private void handleDeleteFeeding() {
        Feeding selected = feedingTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Zmazať krmenie");
        alert.setHeaderText("Zmazať krmenie z " + DateUtils.formatDate(selected.getFeedingDate()));
        alert.setContentText("Naozaj chcete zmazať toto krmenie?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                viewModel.deleteFeeding(selected);
            }
        });
    }

    @FXML
    private void handleRefresh() {
        if (currentHiveId != null) {
            viewModel.loadFeedingsByHiveId(currentHiveId);
        }
    }

    private void showError(String message) {
        statusLabel.setText("Chyba: " + message);
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
