package com.beekeeper.desktop.controller;

import com.beekeeper.desktop.dao.jdbc.JdbcApiaryDao;
import com.beekeeper.desktop.scheduler.DesktopSchedulerProvider;
import com.beekeeper.shared.entity.Apiary;
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
    private Label statusLabel;

    private ApiaryViewModel viewModel;
    private CompositeDisposable disposables = new CompositeDisposable();
    private ObservableList<Apiary> apiaryList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
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
        apiaryTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                boolean hasSelection = newSelection != null;
                editButton.setDisable(!hasSelection);
                deleteButton.setDisable(!hasSelection);
            }
        );

        // Subscribe to ViewModel state
        subscribeToViewModel();

        // Load initial data
        viewModel.loadApiaries();
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
                        statusLabel.setText(apiaries.size() + " včelníc");
                    },
                    error -> showError("Chyba pri načítaní: " + error.getMessage())
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
                            statusLabel.setText("Načítavam...");
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
        dialog.setTitle("Nová včelnica");
        dialog.setHeaderText("Vytvorenie novej včelnice");
        dialog.setContentText("Názov včelnice:");

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
        dialog.setTitle("Upraviť včelnicu");
        dialog.setHeaderText("Úprava včelnice");
        dialog.setContentText("Názov včelnice:");

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
        alert.setTitle("Zmazať včelnicu");
        alert.setHeaderText("Zmazať včelnicu: " + selected.getName());
        alert.setContentText("Naozaj chcete zmazať túto včelnicu? Táto akcia je nevratná.");

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
