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
    private TextField descInput;

    public void initialize() {
        priceSpinnerInput.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000));
        categoryComboInput.getItems().addAll("előétel", "főétel", "desszert");
        categoryComboInput.setValue("előétel");
    }

    @FXML
    public void submitClick(ActionEvent actionEvent) {
        String name = nameInput.getText().trim();
        String desc  = descInput.getText().trim();
        int price = priceSpinnerInput.getValue();
        String category = categoryComboInput.getValue().toString();
        if (name.isEmpty()) {
            alert(Alert.AlertType.WARNING, "A név megadása kötelező", "");
            return;
        }
        if (desc.isEmpty()) {
            alert(Alert.AlertType.WARNING, "A leírás megadása kötelező", "");
            return;
        }
        //TODO: adat felvétele az adatbázisba

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
