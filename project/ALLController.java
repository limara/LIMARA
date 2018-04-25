/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package limara;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.ImagePattern;

/**
 * FXML Controller class
 *
 * @author Эльнур
 */
public class ALLController implements Initializable {

    @FXML//to bind with FXML
    TextField toFind;
    @FXML
    private TableView<Medicine> meds;
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
    @FXML
    private TableColumn<Medicine, ImageView> Image;

    private ObservableList<Medicine> data_Copy;
    private ObservableList<Medicine> data;
    Connection connection;

    @FXML
    RadioButton ifExpire;
    @FXML
    RadioButton ifEst;
    @FXML
    private TableColumn<Medicine, Date> forDate;
    @FXML
    private Button Search;

    @FXML
    public void FIND(ActionEvent e) throws IOException {//adds all drugs from db to data and copies it to datacopy, then delete all from data and adds подходящие обратно to empty data
        String name_toFind = toFind.getText().toLowerCase();
        Load();
        data_Copy = data;
        data = FXCollections.observableArrayList();
        meds.setItems(null);
        for (Medicine i : data_Copy) {
            Calendar calendar = Calendar.getInstance();
            java.util.Date currentDate = calendar.getTime();
            if (ifExpire.isSelected() && !ifEst.isSelected()) {
                if ((i.getName().toLowerCase().contains(name_toFind) || i.getForIllnesses().toLowerCase().contains(name_toFind)) && (i.getExpiry_Date().after(currentDate))) {
                    data.add(i);
                }
            } else if (!ifExpire.isSelected() && !ifEst.isSelected()) {
                if ((i.getName().toLowerCase().contains(name_toFind) || i.getForIllnesses().toLowerCase().contains(name_toFind))) {
                    data.add(i);
                }
            } else if (ifExpire.isSelected() && ifEst.isSelected()) {
                if ((i.getCount() >= 1) && (i.getName().toLowerCase().contains(name_toFind) || i.getForIllnesses().toLowerCase().contains(name_toFind)) && (i.getExpiry_Date().after(currentDate))) {
                    data.add(i);
                }
            } else {
                if ((i.getCount() >= 1) && (i.getName().toLowerCase().contains(name_toFind) || i.getForIllnesses().toLowerCase().contains(name_toFind))) {
                    data.add(i);
                }
            }
        }
        String cssLayout = "-fx-border-color: black;\n"
                + "-fx-border-width: 1;\n"
                + "-fx-background-color:  #ffe6f3;\n";
        medName.setCellValueFactory(new PropertyValueFactory<>("name"));
        medName.setStyle(cssLayout);
        DateCreate.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
        DateCreate.setStyle(cssLayout);
        forDate.setStyle(cssLayout);
        DateExpire.setCellValueFactory(new PropertyValueFactory<>("expiry_Date"));
        DateExpire.setStyle(cssLayout);
        Count.setCellValueFactory(new PropertyValueFactory<>("Count"));
        Count.setStyle(cssLayout);
        Price.setCellValueFactory(new PropertyValueFactory<>("Cost"));
        Price.setStyle(cssLayout);
        Temperature.setCellValueFactory(new PropertyValueFactory<>("temp"));
        Temperature.setStyle(cssLayout);

        Consist.setCellValueFactory(new PropertyValueFactory<>("Consist"));
        Consist.setStyle(cssLayout);

        forDiseases.setCellValueFactory(new PropertyValueFactory<>("forIllnesses"));
        forDiseases.setStyle(cssLayout);

        Image.setCellValueFactory(new PropertyValueFactory<>("Image"));
        Image.setStyle(cssLayout);
        meds.setStyle(cssLayout);
        meds.setItems(null);
        meds.setItems(data);

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Search.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image("file:SearchP.png")), CornerRadii.EMPTY, Insets.EMPTY)));
        try {
            Load();
        } catch (IOException ex) {
            Logger.getLogger(ALLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void Load() throws FileNotFoundException, IOException {
        String url = "jdbc:mysql://localhost:3306/pharmacy";
        String username = "root";
        String passwordd = "";
        try {
            connection = DriverManager.getConnection(url, username, passwordd);
            data = FXCollections.observableArrayList();
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM medicines");
            while (rs.next()) {
                Medicine med = new Medicine(rs.getInt("med_ID"), rs.getString("Name"), rs.getDate("DateCreated"), rs.getDate("ExpireDate"), rs.getInt("Count"), rs.getInt("Price"), rs.getString("temperature"), rs.getString("Consist"), rs.getString("forDiseaes"));
                InputStream is = rs.getBinaryStream("Image");
                OutputStream os = new FileOutputStream(new File("photo.jpg"));
                byte[] content = new byte[1024];
                int size = 0;
                while ((size = is.read(content)) != -1) {
                    os.write(content, 0, size);
                }
                os.close();
                is.close();
                ImageView img = new ImageView(new Image("file:photo.jpg", 250, 250, true, true));
                med.setImage(img);
                data.add(med);
            }
            String cssLayout = "-fx-border-color: black;\n"
                    + "-fx-border-width: 1;\n"
                    + "-fx-background-color:  #ffe6f3;\n";

            medName.setCellValueFactory(new PropertyValueFactory<>("name"));
            medName.setStyle(cssLayout);

            DateCreate.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
            DateCreate.setStyle(cssLayout);

            DateExpire.setCellValueFactory(new PropertyValueFactory<>("expiry_Date"));
            DateExpire.setStyle(cssLayout);
            Count.setCellValueFactory(new PropertyValueFactory<>("Count"));
            Count.setStyle(cssLayout);
            Price.setCellValueFactory(new PropertyValueFactory<>("Cost"));
            Price.setStyle(cssLayout);
            Temperature.setCellValueFactory(new PropertyValueFactory<>("temp"));
            Temperature.setStyle(cssLayout);
            forDate.setStyle(cssLayout);

            Consist.setCellValueFactory(new PropertyValueFactory<>("Consist"));
            Consist.setStyle(cssLayout);

            forDiseases.setCellValueFactory(new PropertyValueFactory<>("forIllnesses"));
            forDiseases.setStyle(cssLayout);

            Image.setCellValueFactory(new PropertyValueFactory<>("Image"));
            Image.setStyle(cssLayout);
            meds.setStyle(cssLayout);

            meds.setItems(null);
            meds.setItems(data);
            connection.close();

        } catch (SQLException ex) {
            Logger.getLogger(ALLController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    public void DeleteSelected(ActionEvent e) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/pharmacy";
        String username = "root";
        String passwordd = "";
        connection = DriverManager.getConnection(url, username, passwordd);
        int ID = meds.getSelectionModel().getSelectedItem().getID();
        meds.getItems().removeAll(meds.getSelectionModel().getSelectedItem());
        Statement stmt = connection.createStatement();
        stmt.execute("DELETE FROM medicines WHERE med_ID = '" + ID + "' ");
        connection.close();
    }

}
