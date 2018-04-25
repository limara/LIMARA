/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package limara;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;

/**
 *
 * @author asus
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    AnchorPane root;

    @FXML
    private void ADD(ActionEvent e) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ADD.fxml"));
            Parent rootere12 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(rootere12));
            stage.show();
        } catch (Exception re) {
            re.printStackTrace();
        }
    }

    @FXML
    private void ALL(ActionEvent e) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ALL.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception re) {
            re.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        root.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image("file:adminBack.jpg")), CornerRadii.EMPTY, Insets.EMPTY)));
    }

}
