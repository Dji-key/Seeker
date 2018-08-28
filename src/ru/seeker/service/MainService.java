package ru.seeker.service;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainService {

    public void createTree(TreeView<File> treeView, File directory) {

        TreeItem<File> root = new TreeItem<File>(directory);
        treeView.setRoot(root);
        for (File file : directory.listFiles()) {
            setItems(root, file);
        }
    }

    public void setItems(TreeItem<File> treeItem, File file) {
        if (file.isDirectory()) {
            TreeItem<File> directory = new TreeItem<File>(file);
            treeItem.getChildren().add(directory);
            for (File inside : file.listFiles()) {
                setItems(directory, inside);
            }
        } else {
            TreeItem<File> inside = new TreeItem<File>(file);
            treeItem.getChildren().add(inside);
        }
    }

    public void createTree(TreeView<File> treeView, File directory, String extension, String statement) {

        TreeItem<File> root = new TreeItem<File>(directory);
        treeView.setRoot(root);
        for (File file : directory.listFiles()) {
            setItems(root, file, extension, statement);
        }
    }

    public boolean setItems(TreeItem<File> treeItem, File file, String extension, String statement) {
        boolean exist = false;
        if (file.isDirectory()) {
            TreeItem<File> directory = new TreeItem<File>(file);
            treeItem.getChildren().add(directory);
            for (File inside : file.listFiles()) {
                if (setItems(directory, inside, extension, statement)) {
                    exist = true;
                }
            }
            if (!exist) {
                treeItem.getChildren().remove(directory);
            }
            return exist;
        } else {
            if (file.getName().endsWith(extension) && isStatementExist(file.getAbsoluteFile(), statement)) {
                TreeItem<File> inside = new TreeItem<File>(file);
                treeItem.getChildren().add(inside);
                return true;
            }
            return false;
        }
    }

    public TextFlow colorTextFlow(StringBuffer buffer, String search) {
        if (search != null) {
            TextFlow result = new TextFlow();
            ArrayList<Text> allText = new ArrayList<Text>();
            Pattern pattern = Pattern.compile(search);
            Matcher matcher = pattern.matcher(buffer.toString());
            int start = 0;
            int end = 0;
            while (matcher.find()) {
                end = matcher.start();
                Text textBefore = new Text(buffer.substring(start, end));
                Text foundText = new Text(buffer.substring(end, matcher.end()));
                foundText.setStyle("-fx-underline: true; -fx-fill: #c3d315; -fx-font-weight: bold");
                start = matcher.end();
                allText.add(textBefore);
                allText.add(foundText);
            }
            allText.add(new Text(buffer.substring(start)));
            for (Text text : allText) {
                result.getChildren().add(text);
            }
            return result;
        } else {
            TextFlow result = new TextFlow();
            result.getChildren().add(new Text(buffer.toString()));
            return result;
        }
    }

    private boolean isStatementExist(File file, String statement) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            StringBuffer buffer = new StringBuffer();
            while (reader.ready()) {
                buffer.append(reader.readLine() + "\n");
            }
            reader.close();
            if (buffer.toString().contains(statement)) {
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
