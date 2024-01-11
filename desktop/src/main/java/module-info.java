module channoun.desktop {
    requires javafx.controls;
    requires javafx.fxml;


    opens channoun.desktop to javafx.fxml;
    exports channoun.desktop;
}