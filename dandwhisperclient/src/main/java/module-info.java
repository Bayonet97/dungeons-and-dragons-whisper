module dandwhisperclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires dandwhispercommon;
    requires gson;
    requires java.sql;
    requires javax.websocket.client.api;
    opens gui.controllers;
    opens gui;
    opens websocketsclient;
    exports gui;
    exports gui.controllers;
    exports websocketsclient;
}