module com.example.rollingball {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.rollingball to javafx.fxml;
    exports com.example.rollingball;
}