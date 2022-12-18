package hu.petrik.etlapprojekt.etlap;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.Optional;

public class EtelFormController {
    @FXML
    private ComboBox categoryComboInput;
    @FXML
    private Button submit;
    @FXML
    private Spinner<Integer> priceSpinnerInput;
    @FXML
    private TextField nameInput;
    @FXML
    private TextArea descInput;
    private EtlapDB db;

    public void initialize() {
        priceSpinnerInput.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(50, 10000,50,50));
        categoryComboInput.getItems().addAll("előétel", "főétel", "desszert");
        categoryComboInput.setValue("előétel");
        try {
            db = new EtlapDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void submitClick(ActionEvent actionEvent) {
        String name = nameInput.getText().trim();
        String desc = descInput.getText().trim();
        int price = priceSpinnerInput.getValue();
        String category = categoryComboInput.getValue().toString();
        if (name.isEmpty()) {
            alert(Alert.AlertType.WARNING, "A név megadása kötelező", "");
            return;
        }
        Etel food = new Etel(name, category, price, desc);
        try {
            if (db.createFood(food)) {
                alert(Alert.AlertType.WARNING, "Sikeres felvétel", "");
                nameInput.setText("");
                descInput.setText("");
                priceSpinnerInput.getValueFactory().setValue(0);
                categoryComboInput.setValue("előétel");
            } else {
                alert(Alert.AlertType.WARNING, "Sikertelen felvétel", "");
            }
        } catch (SQLException e) {
            sqlAlert(e);
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
