module hu.petrik.etlapprojekt.etlap {
    requires javafx.controls;
    requires javafx.fxml;


    opens hu.petrik.etlapprojekt.etlap to javafx.fxml;
    exports hu.petrik.etlapprojekt.etlap;
}