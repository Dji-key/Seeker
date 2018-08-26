package ru.seeker.controller;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class AboutController {
    public void Ok(ActionEvent actionEvent) {
        Node scene = (Node)actionEvent.getSource();
        Stage stage = (Stage)scene.getScene().getWindow();
        stage.close();
    }
}
