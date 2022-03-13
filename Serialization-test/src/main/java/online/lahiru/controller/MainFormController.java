package online.lahiru.controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import online.lahiru.UserData;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class MainFormController {
    private final Path dataBase = Paths.get("DB/User.DB");
    public com.jfoenix.controls.JFXTextField txtId;
    public com.jfoenix.controls.JFXTextField txtName;
    public com.jfoenix.controls.JFXTextField txtAddress;
    public com.jfoenix.controls.JFXTextField txtPicture;
    public JFXButton btnBrowse;
    public TableView<UserData> tblView;
    public JFXButton btnNew;
    public JFXButton btnSave;

    public void initialize(){
        tblView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<UserData, Button> lastCol = (TableColumn<UserData, Button>) tblView.getColumns().get(4);
        lastCol.setCellValueFactory(param -> {
            Button btnDelete = new Button("Delete");
            btnDelete.setOnAction(event -> {
                tblView.getItems().remove(param.getValue());
                saveData();
            });
            return new ReadOnlyObjectWrapper<>(btnDelete);
        });

        TableColumn<UserData, ImageView> col = (TableColumn<UserData, ImageView>) tblView.getColumns().get(3);
        col.setCellValueFactory(param -> {
            ByteArrayInputStream is = new ByteArrayInputStream(param.getValue().getBytes());
            ImageView imageView = new ImageView(new Image(is));
            imageView.setFitWidth(70);
            imageView.setFitHeight(70);
            return new ReadOnlyObjectWrapper<>(imageView);
        });
        tblView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                txtId.setText(newValue.getId());
                txtId.setEditable(false);
                txtName.setText(newValue.getName());
                txtAddress.setText(newValue.getAddress());
                txtPicture.setText("[set ur picture here !]");
                btnSave.setText("Update");
            }
        });
    }
    private boolean saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(dataBase, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING))) {
            oos.writeObject(new ArrayList<UserData>(tblView.getItems()));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }private void initDatabase() {
        try {

            if (!Files.exists(dataBase)) {
                Files.createDirectories(dataBase.getParent());
                Files.createFile(dataBase);
            }

            loadUserdata();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to initialize the database").showAndWait();
            Platform.exit();
        }
    }
    private void loadUserdata() {
        try (InputStream is = Files.newInputStream(dataBase, StandardOpenOption.READ);
             ObjectInputStream ois = new ObjectInputStream(is)) {
            tblView.getItems().clear();
            tblView.setItems(FXCollections.observableArrayList((ArrayList<UserData>) ois.readObject()));
        } catch (IOException | ClassNotFoundException e) {
            if (!(e instanceof EOFException)) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to load customers").showAndWait();
            }
        }
    }
    public void btnNew_OnAction(ActionEvent event) {
    }

    public void btnBrowse_OnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images", "*.jpg", "*.jpeg", ".png"));
        File file = fileChooser.showOpenDialog(tblView.getScene().getWindow());
        if (file != null) {
            txtPicture.setText(file.getAbsolutePath());
        }
    }

    public void btnSave_OnAction(ActionEvent event) {
    }
}
