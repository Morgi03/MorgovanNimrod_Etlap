package hu.petrik.etlapprojekt.etlap;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
        String[] kategoriak = {"előétel", "főétel", "desszert"};
        categoryComboInput = new ComboBox(FXCollections.observableArrayList(kategoriak));
    }

    @FXML
    public void submitClick(ActionEvent actionEvent) {
    }
}
