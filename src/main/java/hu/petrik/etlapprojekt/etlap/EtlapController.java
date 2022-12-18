package hu.petrik.etlapprojekt.etlap;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
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

    public void running() {

    }

    public void initialize() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        szazalekSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 50, 5, 5));
        forintSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(50, 3000, 50, 50));
        try {
            db = new EtlapDB();
            readFood();
        } catch (SQLException e) {
            Platform.runLater(() -> {
                sqlAlert(e);
                Platform.exit();
            });
        }

        etlapTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                if (etlapTable.getSelectionModel().getSelectedItem() != null) {
                    Etel food = getSelectedFood();
                    listview.getItems().clear();
                    listview.getItems().add(food != null ? food.getDesc() : "");
                }
            }
        });


    }

    private void readFood() throws SQLException {
        List<Etel> dogs = db.readFood();
        etlapTable.getItems().clear();
        etlapTable.getItems().addAll(dogs);
    }


    private Etel getSelectedFood() {
        int selectedIndex = etlapTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            alert(Alert.AlertType.WARNING, "A táblázatból előbb válasszon ki egy ételt!", "");
            return null;
        }
        return etlapTable.getSelectionModel().getSelectedItem();
    }

    private Etel getSelectedFoodNullNotMatter() {
        int selectedIndex = etlapTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            return null;
        }
        return etlapTable.getSelectionModel().getSelectedItem();
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
        Etel selected = getSelectedFood();
        if (selected == null) return;
        Optional<ButtonType> optionalButtonType = alert(Alert.AlertType.CONFIRMATION, "Biztos, hogy törölni szeretné a kiválasztott ételt?", "");
        if (optionalButtonType.isEmpty() || !(optionalButtonType.get().equals(ButtonType.OK)) && !(optionalButtonType.get().equals(ButtonType.YES))) {
            return;
        }
        try {
            if (db.deleteFood(selected.getId())) {
                alert(Alert.AlertType.WARNING, "Sikeres törlés", "");
                listview.getItems().clear();
            } else {
                alert(Alert.AlertType.WARNING, "Sikertelen törlés", "");
            }
            readFood();
        } catch (SQLException e) {
            sqlAlert(e);
        }
    }

    private void updateFood(int id, String name, String category, int price, String desc) {
        Etel food = new Etel(id, name, category, price, desc);
        try {
            if (db.updateFood(food)) {
                alert(Alert.AlertType.WARNING, "Sikeres módosítás", "");
            } else {
                alert(Alert.AlertType.WARNING, "Sikertelen módosítás", "");
            }
            resetSpinners();
            readFood();
        } catch (SQLException e) {
            sqlAlert(e);
        }
    }

    private void updateAllFood(int changeVal, boolean szazalek) {
        try {
            if (db.updateAllFoodPrice(changeVal, szazalek)) {
                alert(Alert.AlertType.WARNING, "Sikeres módosítás", "");
            } else {
                alert(Alert.AlertType.WARNING, "Sikertelen módosítás", "");
            }
            resetSpinners();
            readFood();
        } catch (SQLException e) {
            sqlAlert(e);
        }
    }

    private void resetSpinners() {
        szazalekSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 50, 5, 5));
        forintSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(50, 3000, 50, 50));
    }

    @FXML
    public void forintEmelesClick(ActionEvent actionEvent) {
        Etel selected = getSelectedFoodNullNotMatter();
        if (selected == null) {
            Optional<ButtonType> optionalButtonType = alert(Alert.AlertType.CONFIRMATION, "Biztos, hogy szerednéd növelni az összes étel árát " + forintSpinner.getValue() + " forinttal?", "");
            if (optionalButtonType.isEmpty() || !(optionalButtonType.get().equals(ButtonType.OK)) && !(optionalButtonType.get().equals(ButtonType.YES))) {
                return;
            }
            updateAllFood(forintSpinner.getValue(), false);
        } else {
            Optional<ButtonType> optionalButtonType = alert(Alert.AlertType.CONFIRMATION, "Biztos, hogy szerednéd növelni a(z) " + selected.getName() + " árát " + forintSpinner.getValue() + " forinttal?", "");
            if (optionalButtonType.isEmpty() || !(optionalButtonType.get().equals(ButtonType.OK)) && !(optionalButtonType.get().equals(ButtonType.YES))) {
                return;
            }
            updateFood(selected.getId(), selected.getName(), selected.getCategory(), selected.getPrice() + forintSpinner.getValue(), selected.getDesc());
        }
    }

    @FXML
    public void szazalekEmelesClick(ActionEvent actionEvent) {
        Etel selected = getSelectedFoodNullNotMatter();
        if (selected == null) {
            Optional<ButtonType> optionalButtonType = alert(Alert.AlertType.CONFIRMATION, "Biztos, hogy szerednéd növelni az összes étel árát " + szazalekSpinner.getValue() + " százalékkal?", "");
            if (optionalButtonType.isEmpty() || !(optionalButtonType.get().equals(ButtonType.OK)) && !(optionalButtonType.get().equals(ButtonType.YES))) {
                return;
            }
            updateAllFood(szazalekSpinner.getValue(), true);
        } else {
            Optional<ButtonType> optionalButtonType = alert(Alert.AlertType.CONFIRMATION, "Biztos, hogy szerednéd növelni a(z) " + selected.getName() + " árát " + szazalekSpinner.getValue() + " százalékkal?", "");
            if (optionalButtonType.isEmpty() || !(optionalButtonType.get().equals(ButtonType.OK)) && !(optionalButtonType.get().equals(ButtonType.YES))) {
                return;
            }
            int price = (int)(selected.getPrice() * (1 + ( (double)szazalekSpinner.getValue() / 100)));
            updateFood(selected.getId(), selected.getName(), selected.getCategory(), price , selected.getDesc());
        }
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