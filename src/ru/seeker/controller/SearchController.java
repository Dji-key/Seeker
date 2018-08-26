package ru.seeker.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchController {

    @FXML
    private TextField searchField;

    @FXML
    private TextField extensionField;

    public void btnCancel(ActionEvent actionEvent) {
        Node scene = (Node)actionEvent.getSource();
        Stage stage = (Stage)scene.getScene().getWindow();
        stage.close();
    }

    public void btnSearch(ActionEvent actionEvent) {
        Pattern pattern = Pattern.compile("[a-z]+");
        Matcher matcher = pattern.matcher(extensionField.getText());
        if (!searchField.getText().trim().isEmpty()) {
            if (extensionField.getText().trim().isEmpty() || matcher.matches()) {
                MainController.setSearch(searchField.getText().trim(), extensionField.getText().trim());
                Node scene = (Node) actionEvent.getSource();
                Stage stage = (Stage) scene.getScene().getWindow();
                stage.close();
            }
        }
    }
}
