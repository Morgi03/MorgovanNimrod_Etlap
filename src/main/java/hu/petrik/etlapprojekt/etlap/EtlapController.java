package hu.petrik.etlapprojekt.etlap;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class EtlapController {

    @FXML
    private TableView<Etel> etlapTable;
    @FXML
    private Spinner<Integer> forintSpinner;
    @FXML
    private Button torlesButton;
    @FXML
    private TableColumn<Etel, String> nameCol;
    @FXML
    private ListView<String> listview;
    @FXML
    private Button ujEtelFelveteleButton;
    @FXML
    private Spinner<Integer> szazalekSpinner;
    @FXML
    private TableColumn<Etel, String> categoryCol;
    @FXML
    private Button szazalekEmelesButton;
    @FXML
    private Button forintEmelesButton;
    @FXML
    private TableColumn<Etel, Integer> priceCol;
    private EtlapDB db;

    public void initialize() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        szazalekSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000));
        forintSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 2500));
        try {
            db = new EtlapDB();
            readFood();
        } catch (SQLException e) {
            Platform.runLater(() -> {
                sqlAlert(e);
                Platform.exit();
            });
        }
    }

    private void readFood() throws SQLException {
        List<Etel> dogs = db.readFood();
        etlapTable.getItems().clear();
        etlapTable.getItems().addAll(dogs);
    }

    @FXML
    public void ujEtelFelveteleClick(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("etel-form.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 320, 350);
            Stage stage = new Stage();
            stage.setTitle("Make Food");
            stage.setScene(scene);
            stage.show();
            ujEtelFelveteleButton.setDisable(true);
            torlesButton.setDisable(true);
            forintEmelesButton.setDisable(true);
            szazalekEmelesButton.setDisable(true);
            stage.setOnCloseRequest(event -> {
                ujEtelFelveteleButton.setDisable(false);
                torlesButton.setDisable(false);
                forintEmelesButton.setDisable(false);
                szazalekEmelesButton.setDisable(false);
                try {
                    readFood();
                } catch (SQLException e) {
                    sqlAlert(e);
                }
            });
        } catch (Exception e) {
            alert(Alert.AlertType.ERROR, "HIBA", e.getMessage());
        }
    }

    @FXML
    public void torlesClick(ActionEvent actionEvent) {
    }

    @FXML
    public void forintEmelesClick(ActionEvent actionEvent) {
    }

    @FXML
    public void szazalekEmelesClick(ActionEvent actionEvent) {
    }


    private Optional<ButtonType> alert(Alert.AlertType alertType, String headerText, String conentText) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(headerText);
        alert.setContentText(conentText);
        return alert.showAndWait();
    }

    private void sqlAlert(SQLException e) {
        alert(Alert.AlertType.ERROR, "Hiba történt az adatbázis kapcsolat kialakításakor", e.getMessage());
    }
}