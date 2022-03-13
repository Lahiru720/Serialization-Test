package online.lahiru.controller;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import online.lahiru.UserData;

import java.io.IOException;
import java.io.ObjectOutputStream;
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
    }
    private boolean saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(dataBase, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING))) {
            oos.writeObject(new ArrayList<UserData>(tblView.getItems()));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void btnNew_OnAction(ActionEvent event) {
    }

    public void btnBrowse_OnAction(ActionEvent event) {
    }

    public void btnSave_OnAction(ActionEvent event) {
    }
}
