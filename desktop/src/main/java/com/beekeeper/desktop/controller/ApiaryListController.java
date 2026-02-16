package com.beekeeper.desktop.controller;

import com.beekeeper.desktop.dao.jdbc.JdbcApiaryDao;
import com.beekeeper.desktop.scheduler.DesktopSchedulerProvider;
import com.beekeeper.shared.entity.Apiary;
import com.beekeeper.shared.i18n.TranslationManager;
import com.beekeeper.shared.repository.ApiaryRepository;
import com.beekeeper.shared.viewmodel.ApiaryViewModel;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.function.Consumer;

/**
 * Controller for Apiary list view.
 * Displays all apiaries in a TableView with CRUD operations.
 */
public class ApiaryListController {

    @FXML
    private TableView<Apiary> apiaryTable;

    @FXML
    private TableColumn<Apiary, String> nameColumn;

    @FXML
    private TableColumn<Apiary, String> locationColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button showHivesButton;

    @FXML
    private Label statusLabel;

    private ApiaryViewModel viewModel;
    private CompositeDisposable disposables = new CompositeDisposable();
    private ObservableList<Apiary> apiaryList = FXCollections.observableArrayList();
    private Consumer<Apiary> onApiarySelected;
    private TranslationManager tm;

    @FXML
    public void initialize() {
        // Get TranslationManager instance
        tm = TranslationManager.getInstance();

        // Initialize ViewModel with dependencies
        JdbcApiaryDao apiaryDao = new JdbcApiaryDao();
        ApiaryRepository repository = new ApiaryRepository(apiaryDao);
        DesktopSchedulerProvider schedulerProvider = new DesktopSchedulerProvider();
        viewModel = new ApiaryViewModel(repository, schedulerProvider);

        // Configure table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        // Bind table to observable list
        apiaryTable.setItems(apiaryList);

        // Enable/disable buttons based on selection
        editButton.setDisable(true);
        deleteButton.setDisable(true);
        showHivesButton.setDisable(true);

        // Defer selection listener setup to avoid macOS NSTrackingRectTag bug
        Platform.runLater(() -> {
            apiaryTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    boolean hasSelection = newSelection != null;
                    editButton.setDisable(!hasSelection);
                    deleteButton.setDisable(!hasSelection);
                    showHivesButton.setDisable(!hasSelection);
                }
            );

            // Subscribe to ViewModel state after UI is fully initialized
            subscribeToViewModel();

            // Load initial data
            viewModel.loadApiaries();
        });
    }

    private void subscribeToViewModel() {
        // Subscribe to apiaries list
        disposables.add(
            viewModel.getApiaries()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(
                    apiaries -> {
                        apiaryList.clear();
                        apiaryList.addAll(apiaries);
                        statusLabel.setText(tm.get("status.apiaries_count", apiaries.size()));
                    },
                    error -> showError(tm.get("error.loading_apiaries", error.getMessage()))
                )
        );

        // Subscribe to loading state
        disposables.add(
            viewModel.getLoading()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(
                    loading -> {
                        addButton.setDisable(loading);
                        apiaryTable.setDisable(loading);
                        if (loading) {
                            statusLabel.setText(tm.get("status.loading"));
                        }
                    }
                )
        );

        // Subscribe to error messages
        disposables.add(
            viewModel.getError()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(this::showError)
        );

        // Subscribe to success messages
        disposables.add(
            viewModel.getSuccess()
                .observeOn(JavaFxScheduler.platform())
                .subscribe(this::showSuccess)
        );
    }

    @FXML
    private void handleAddApiary() {
        // Show dialog to add new apiary
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(tm.get("dialog.add_apiary.title"));
        dialog.setHeaderText(tm.get("dialog.add_apiary.header"));
        dialog.setContentText(tm.get("dialog.add_apiary.content"));

        dialog.showAndWait().ifPresent(name -> {
            if (!name.trim().isEmpty()) {
                viewModel.createApiary(name, "", 0.0, 0.0);
            }
        });
    }

    @FXML
    private void handleEditApiary() {
        Apiary selected = apiaryTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        TextInputDialog dialog = new TextInputDialog(selected.getName());
        dialog.setTitle(tm.get("dialog.edit_apiary.title"));
        dialog.setHeaderText(tm.get("dialog.edit_apiary.header"));
        dialog.setContentText(tm.get("dialog.edit_apiary.content"));

        dialog.showAndWait().ifPresent(name -> {
            if (!name.trim().isEmpty()) {
                selected.setName(name);
                viewModel.updateApiary(selected);
            }
        });
    }

    @FXML
    private void handleDeleteApiary() {
        Apiary selected = apiaryTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(tm.get("dialog.delete_apiary.title"));
        alert.setHeaderText(tm.get("dialog.delete_apiary.header", selected.getName()));
        alert.setContentText(tm.get("dialog.delete_apiary.content"));

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                viewModel.deleteApiary(selected);
            }
        });
    }

    @FXML
    private void handleRefresh() {
        viewModel.loadApiaries();
    }

    @FXML
    private void handleShowHives() {
        Apiary selected = apiaryTable.getSelectionModel().getSelectedItem();
        if (selected != null && onApiarySelected != null) {
            onApiarySelected.accept(selected);
        }
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

    /**
     * Set callback for when "Zobraziť úle" button is clicked.
     * @param callback Consumer that receives the selected Apiary
     */
    public void setOnApiarySelected(Consumer<Apiary> callback) {
        this.onApiarySelected = callback;
    }
}
