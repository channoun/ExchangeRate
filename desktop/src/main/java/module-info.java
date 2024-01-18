module channoun.desktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires retrofit2;
    requires java.sql;
    requires gson;
    requires java.prefs;
    requires retrofit2.converter.gson;


    opens channoun.desktop.api.model to gson, javafx.base;
    exports channoun.desktop;
    opens channoun.desktop to gson, javafx.fxml;
    exports channoun.desktop.api;
    opens channoun.desktop.api to gson, javafx.fxml;
    exports channoun.desktop.rates;
    opens channoun.desktop.rates to gson, javafx.fxml;
    exports channoun.desktop.login;
    opens channoun.desktop.login to javafx.fxml;
    exports channoun.desktop.register;
    opens channoun.desktop.register to javafx.fxml;
    exports channoun.desktop.transactions;
    opens channoun.desktop.transactions to javafx.fxml;
}