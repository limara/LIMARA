/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package limara;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

/**
 * FXML Controller class
 *
 * @author asus
 */
public class LogorregController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    TextField login;
    @FXML
    TextField ans;

    @FXML
    PasswordField password;

    @FXML
    PasswordField password1;
    @FXML
    Label l;
    int res = Integer.MIN_VALUE;
    @FXML
    Label error;
    @FXML
    RadioButton Admin;
    @FXML
    AnchorPane root;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        root.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image("file:regBack.jpg")), CornerRadii.EMPTY, Insets.EMPTY)));
        Random r = new Random();
        int n1 = r.nextInt(10);
        int n2 = r.nextInt(10);
        HashMap<Integer, String> map = new HashMap<>();
        map.put(0, "+");
        map.put(1, "-");
        map.put(2, "*");
        int nj = r.nextInt(3);
        String line = n1 + map.get(nj) + n2;
        if (map.get(nj).equals("+")) {
            res = n1 + n2;
        } else if (map.get(nj).equals("-")) {
            res = n1 - n2;
        } else if (map.get(nj).equals("*")) {
            res = n1 * n2;
        }
        l.setAlignment(Pos.CENTER);
        l.setText(line);
    }

    @FXML
    public void Register(ActionEvent act) throws SQLException {
        boolean bool = false;
        int i = 0;
        if (Admin.isSelected()) {
            i = 1;
        }
        if (res == Integer.parseInt(ans.getText())) {
            if (password.getText().equals(password1.getText())) {
                String url = "jdbc:mysql://localhost:3306/pharmacy";
                String username = "root";
                String passwordd = "";
                Connection connection = DriverManager.getConnection(url, username, passwordd);
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Users");//Setiing to resultset all users
                String log = login.getText();
                String pas = password.getText();
                while (rs.next()) {// going row by row from resultset
                    if (rs.getString("Login").equals(log)) {
                        bool = true;
                    }
                }
                if (bool) {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Validate Login");
                    alert.setHeaderText(null);
                    alert.setContentText("There is user with such login, Please choose Valid Login");
                    alert.showAndWait();
                } else {// if there is no such login in database we can insert him
                    String query = " insert into users (Login, Password,Admin)"
                            + " values (?, ?, ?)";
                    PreparedStatement preparedStmt = connection.prepareStatement(query);//1=1? 2=2?
                    preparedStmt.setString(1, log);
                    preparedStmt.setString(2, pas);
                    preparedStmt.setInt(3, i);
                    preparedStmt.execute();
                    connection.close();

                }
            } else {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Validate Password");
                alert.setHeaderText(null);
                alert.setContentText("Passwords are not same");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Captcha incorrect");
            alert.setHeaderText(null);
            alert.setContentText("Captcha is not correct");
            alert.showAndWait();
            Random r = new Random();
            int n1 = r.nextInt(10);
            int n2 = r.nextInt(10);
            HashMap<Integer, String> map = new HashMap<>();
            map.put(0, "+");
            map.put(1, "-");
            map.put(2, "*");
            int nj = r.nextInt(3);
            String line = n1 + map.get(nj) + n2;
            if (map.get(nj).equals("+")) {
                res = n1 + n2;
            } else if (map.get(nj).equals("-")) {
                res = n1 - n2;
            } else if (map.get(nj).equals("*")) {
                res = n1 * n2;
            }
            l.setAlignment(Pos.CENTER);
            l.setText(line);
        }
    }

}
