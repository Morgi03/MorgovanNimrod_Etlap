package hu.petrik.etlapprojekt.etlap;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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

    @FXML
    public void ujEtelFelveteleClick(ActionEvent actionEvent) {
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
}