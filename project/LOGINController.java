    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package limara;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author asus
 */
public class LOGINController implements Initializable {

    boolean entered = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image img = new Image("file:loginBack.jpg");//setting Background for main page
        root.setBackground(new Background(new BackgroundFill(new ImagePattern(img), CornerRadii.EMPTY, Insets.EMPTY)));
    }
    @FXML
    TextField login;
    @FXML
    PasswordField password;
    @FXML
    AnchorPane root;

    @FXML
    public void Enter(ActionEvent e) {//Entering to programm
        String log = login.getText();
        String pas = password.getText();
        String url = "jdbc:mysql://localhost:3306/pharmacy";
        String username = "root";
        String password = "";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);//Создание связи с ДБ
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Users");//Selecting all from table Users
            while (rs.next()) {//Selecting row by row from table
                if (rs.getString("Login").equals(log) && rs.getString("Password").equals(pas)) {
                    entered = true;
                    try {
                        PrintWriter out = new PrintWriter(new FileOutputStream("ID.txt"));
                        out.print(rs.getInt("ID"));//we will need it to know which user signed in
                        ((Node) (e.getSource())).getScene().getWindow().hide();//close opened window
                        if (rs.getInt("Admin") == 1) {
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
                            Parent root1 = (Parent) fxmlLoader.load();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(root1));
                            stage.show();
                        } else {
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("forUser.fxml"));
                            Parent root1 = (Parent) fxmlLoader.load();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(root1));
                            stage.show();
                        }
                        out.close();
                    } catch (Exception re) {
                        re.printStackTrace();
                    }
                }
            }
            if (!entered) {// if we do not have an user with such login and password
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Login error");
                alert.setHeaderText(null);
                alert.setContentText("Please Enter Valid Login and Password");
                alert.showAndWait();
            }
            stmt.close();

        } catch (SQLException er) {
            System.out.println(er.toString());
            er.printStackTrace();
        }
    }

    @FXML
    public void Register(ActionEvent e) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("logorreg.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception re) {
            re.printStackTrace();
        }
    }

}
