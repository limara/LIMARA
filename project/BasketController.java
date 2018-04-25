/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package limara;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Эльнур
 */
public class BasketController implements Initializable {

    @FXML
    private TableView<Medicine> meds;
    @FXML
    private TableColumn<Medicine, Integer> med_ID;
    @FXML
    private TableColumn<Medicine, String> medName;
    @FXML
    private TableColumn<Medicine, Date> DateCreate;
    @FXML
    private TableColumn<Medicine, Date> DateExpire;
    @FXML
    private TableColumn<Medicine, Integer> Count;
    @FXML
    private TableColumn<Medicine, Integer> Price;
    @FXML
    private TableColumn<Medicine, String> Consist;
    @FXML
    private TableColumn<Medicine, String> forDiseases;
    @FXML
    private TableColumn<Medicine, String> Temperature;
    private ObservableList<Medicine> data;
    @FXML
    private Label cena;
    @FXML
    private TableColumn<?, ?> Meds;
    int pricee = 0;
    String url1 = "jdbc:mysql://localhost:3306/pharmacy";
    String username = "root";
    String passwordd = "";
    Connection connection;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pricee = 0;
        try {
            Scanner in = new Scanner(new FileInputStream("out.txt"));
            data = FXCollections.observableArrayList();
            ArrayList<Integer> ids = new ArrayList<>();
            while (in.hasNext()) {
                ids.add(in.nextInt());
            }
            connection = DriverManager.getConnection(url1, username, passwordd);
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM medicines");
            while (rs.next()) {
                if (ids.contains(rs.getInt("med_ID")) && rs.getInt("Count") > 0) {
                    Medicine m = new Medicine(rs.getInt("med_ID"), rs.getString("Name"), rs.getDate("DateCreated"), rs.getDate("ExpireDate"), rs.getInt("Count"), rs.getInt("Price"), rs.getString("temperature"), rs.getString("Consist"), rs.getString("forDiseaes"));
                    pricee += m.getCost();
                    data.add(m);
                }
            }
            med_ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
            medName.setCellValueFactory(new PropertyValueFactory<>("name"));
            DateCreate.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
            DateExpire.setCellValueFactory(new PropertyValueFactory<>("expiry_Date"));
            Count.setCellValueFactory(new PropertyValueFactory<>("Count"));
            Price.setCellValueFactory(new PropertyValueFactory<>("Cost"));
            Temperature.setCellValueFactory(new PropertyValueFactory<>("temp"));
            Consist.setCellValueFactory(new PropertyValueFactory<>("Consist"));
            forDiseases.setCellValueFactory(new PropertyValueFactory<>("forIllnesses"));
            meds.setItems(null);
            meds.setItems(data);
            cena.setText("Price: " + pricee);
            connection.close();
        } catch (Exception ex) {
            Logger.getLogger(BasketController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void Buy(ActionEvent event) throws FileNotFoundException, SQLException {
        try {
            connection = DriverManager.getConnection(url1, username, passwordd);
            int ID = meds.getSelectionModel().getSelectedItem().getID();
            int Count = meds.getSelectionModel().getSelectedItem().getCount();
            PreparedStatement st = connection.prepareStatement("UPDATE medicines SET Count = ? WHERE med_ID = ?");
            st.setInt(1, Count - 1);
            st.setInt(2, ID);
            st.executeUpdate();
            meds.getItems().removeAll(meds.getSelectionModel().getSelectedItem());
            int pr = pricee - meds.getSelectionModel().getSelectedItem().getCost();
            pricee = pr;
            cena.setText("Price: " + pr);

        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Drug is not selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select the drug");
            alert.showAndWait();
        }
    }

    @FXML
    private void Remove(ActionEvent event) {
        try {
            int ID = meds.getSelectionModel().getSelectedItem().getID();
            int pr = pricee - meds.getSelectionModel().getSelectedItem().getCost();
            pricee = pr;
            cena.setText("Price: " + pricee);
            meds.getItems().removeAll(meds.getSelectionModel().getSelectedItem());
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Drug is not selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select the drug");
            alert.showAndWait();
        }
    }

}
