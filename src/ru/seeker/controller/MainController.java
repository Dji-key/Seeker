package ru.seeker.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import ru.seeker.service.MainService;

import java.io.*;

public class MainController {

    private static File directory;

    private File chooseDirectory;

    @FXML
    private VBox mainWindow;

    @FXML
    private MenuItem btnSearch;

    @FXML
    private Label selectedDirectory;

    @FXML
    private TabPane tabPane;

    @FXML
    private TreeView<File> treeView;

    private static String searchField;

    private static String extensionField;


    public static void setSearch(String name, String extension) {
        searchField = name;
        extensionField = extension;
    }

    private MainService service = new MainService();

    @FXML
    private void initialize() {
        treeView.setCellFactory(new Callback<TreeView<File>, TreeCell<File>>() {
            @Override
            public TreeCell<File> call(TreeView<File> tv) {
                return new TreeCell<File>() {

                    @Override
                    protected void updateItem(File item, boolean empty) {
                        super.updateItem(item, empty);

                        setText((empty || item == null) ? "" : item.getName());
                    }

                };
            }
        });
        treeView.setShowRoot(false);
    }

    public void openDirectory(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        chooseDirectory = directoryChooser.showDialog(mainWindow.getScene().getWindow());
        if (chooseDirectory != null) {
            directory = chooseDirectory;
        }
        if (directory != null) {
            selectedDirectory.setText("Chosen directory is " + directory.toString());
            btnSearch.setDisable(false);
            service.createTree(treeView, directory);
        }
    }

    public void search(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/searchWindow.fxml"));
            Parent root = loader.load();
            stage.setTitle("Search");
            stage.setMinHeight(170);
            stage.setMinWidth(400);
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(mainWindow.getScene().getWindow());
            stage.showAndWait();
            if (!searchField.isEmpty()) {
                if (extensionField.isEmpty()) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            service.createTree(treeView, directory, ".log", searchField);
                        }
                    });
                } else {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            service.createTree(treeView, directory, "." + extensionField, searchField);
                        }
                    });
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openTreeFile(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2 && treeView.getSelectionModel().getSelectedItem().isLeaf()) {
            Tab tab = new Tab();
            tab.setText(treeView.getSelectionModel().getSelectedItem().getValue().getName());
            tab.setClosable(true);
            StringBuffer buffer = new StringBuffer();

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(treeView.getSelectionModel().getSelectedItem().getValue().getAbsoluteFile())));
                while (reader.ready()) {
                    buffer.append(reader.readLine() + "\n");
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            TextFlow textFlow = service.colorTextFlow(buffer, searchField);
            textFlow.setPadding(new Insets(10));
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(textFlow);
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            tab.setContent(scrollPane);
        }
    }

    public void about(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/aboutWindow.fxml"));
        Parent root = loader.load();
        stage.setTitle("About program");
        stage.setHeight(430);
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
