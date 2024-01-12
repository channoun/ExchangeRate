module channoun.desktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires retrofit2;
    requires java.sql;
    requires gson;
    requires retrofit2.converter.gson;


    opens channoun.desktop.api.model to gson;
    exports channoun.desktop;
    opens channoun.desktop to gson, javafx.fxml;
    exports channoun.desktop.api;
    opens channoun.desktop.api to gson, javafx.fxml;
}