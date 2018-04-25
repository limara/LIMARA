/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package limara;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author asus
 */
public class ADDController implements Initializable {

    /**
     * Initializes the controller class.
     */
    Label l1;
    @FXML
    TextField name;
    @FXML
    TextField count;
    @FXML
    TextField price;
    @FXML
    TextField Temperature;
    @FXML
    TextField Consist;
    @FXML
    TextField ForDiseases;
    @FXML
    DatePicker dateCreated;
    @FXML
    DatePicker ExprieDate;
    @FXML
    TextField img;
    @FXML
    private AnchorPane root;

    @Override

    public void initialize(URL url, ResourceBundle rb) {
        root.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image("file:addBack.jpg")), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    /**
     *
     * @param e
     */
    int ID;
    Medicine med;

    Connection connection;
    String url = "jdbc:mysql://localhost:3306/pharmacy";
    String username = "root";
    String password = "";

    @FXML
    public void Add(ActionEvent e) throws Exception {
        try {//Creating new Medicine with values from textfields,datepicker
            Date DPCurrentDate1 = Date.valueOf(dateCreated.getValue());
            Date DPCurrentDate2 = Date.valueOf(ExprieDate.getValue());
            med = new Medicine();
            med.setName(name.getText());
            med.setConsist(Consist.getText());
            med.setTemp(new Temperature(Temperature.getText()));
            med.setCost(Integer.parseInt(price.getText()));
            med.setCount(Integer.parseInt(count.getText()));
            med.setDateCreated(DPCurrentDate1);
            med.setExpiry_Date(DPCurrentDate2);
            med.setImage(imageView);
            Scanner in = new Scanner(new FileInputStream("ID.txt"));//initializing which user adds the medicine
            ID = in.nextInt();
            in.close();
            med.setForIllnesses(ForDiseases.getText());
        } catch (NullPointerException er) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Values not setted");
            alert.setHeaderText(null);
            alert.setContentText("Please enter the values!");
            alert.showAndWait();
        }

        try {
            connection = DriverManager.getConnection(url, username, password);
            Statement stmt = connection.createStatement();
            String query = " insert into medicines (Name, Consist,forDiseaes,DateCreated,ExpireDate,Price,Count,temperature,userID,Image)"
                    + " values (?,?,?,?,?,?,?,?,?,?)";
            if (name.getText().length() > 0 && Consist.getText().length() > 0 && count.getText().length() > 0 && price.getText().length() > 0 && dateCreated.toString().length() > 0 && ExprieDate.toString().length() > 0 && ForDiseases.getText().length() > 0 && Temperature.getText().length() > 0) {
                PreparedStatement preparedStmt = connection.prepareStatement(query);
                preparedStmt.setString(1, med.getName());
                preparedStmt.setString(2, med.getConsist());
                preparedStmt.setString(3, med.getForIllnesses());
                preparedStmt.setDate(4, med.getDateCreated());
                preparedStmt.setDate(5, med.getExpiry_Date());
                preparedStmt.setInt(6, med.getCost());
                preparedStmt.setInt(7, med.getCount());
                preparedStmt.setString(8, med.getTemp().toString());
                preparedStmt.setInt(9, ID);
                FileInputStream fis = new FileInputStream(file);
                preparedStmt.setBinaryStream(10, (InputStream) fis, (int) file.length());//Blob -> stream
                preparedStmt.execute();
                connection.close();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Accepted");
                alert.setHeaderText(null);
                alert.setContentText("Drug inserted");
                alert.showAndWait();

            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Incorrect Values");
                alert.setHeaderText(null);
                alert.setContentText("Please enter the values correctly!");
                alert.showAndWait();
            }
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please try again!");
            alert.showAndWait();
            ex.printStackTrace();
        }
    }
    FileChooser filee;
    private ImageView imageView;
    private Image image;
    File file;

    @FXML
    private void Browse(ActionEvent event) {
        filee = new FileChooser();
        filee.getExtensionFilters().addAll(
                new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        Stage primaryStage = new Stage();
        file = filee.showOpenDialog(primaryStage);
        if (file != null) {
            image = new Image(file.toURI().toString(), 100, 150, true, true);
            imageView = new ImageView(image);
            img.setText(file.toURI().toString());
        }

    }
}
