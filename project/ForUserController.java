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
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Эльнур
 */
public class ForUserController implements Initializable {

    @FXML
    TextField toFind;

    private ObservableList<Medicine> data;
    private ObservableList<Medicine> dataCopy;

    Connection connection;

    @FXML
    RadioButton ifExpire;
    @FXML
    RadioButton ifEst;

    /**
     * Initializes the controller class.
     */
    PrintWriter out;
    @FXML
    private ScrollPane VBOX;
    private VBox vb;
    @FXML
    private AnchorPane root;
    @FXML
    private Button Basket;
    @FXML
    private Button Search;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Search.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image("file:Search.png")), CornerRadii.EMPTY, Insets.EMPTY)));
        Basket.setBackground(new Background(new BackgroundFill(new ImagePattern(new Image("file:baskter.jpg")), CornerRadii.EMPTY, Insets.EMPTY)));
        root.setBackground(new Background(new BackgroundFill(Color.HONEYDEW, CornerRadii.EMPTY, Insets.EMPTY)));
        VBOX.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        VBOX.setHbarPolicy(ScrollBarPolicy.NEVER);
        try {
            Load();
        } catch (IOException ex) {
            Logger.getLogger(ForUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    Button b;
    ArrayList<Button> btns = new ArrayList<>();

    @FXML
    private void Load() throws FileNotFoundException, IOException {
        String url = "jdbc:mysql://localhost:3306/pharmacy";
        out = new PrintWriter(new FileOutputStream("out.txt"));
        data = FXCollections.observableArrayList();
        String username = "root";
        String passwordd = "";
        data.clear();
        try {
            connection = DriverManager.getConnection(url, username, passwordd);
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM medicines");
            while (rs.next()) {
                Medicine med = new Medicine(rs.getInt("med_ID"), rs.getString("Name"), rs.getDate("DateCreated"), rs.getDate("ExpireDate"), rs.getInt("Count"), rs.getInt("Price"), rs.getString("temperature"), rs.getString("Consist"), rs.getString("forDiseaes"));
                InputStream is = rs.getBinaryStream("Image");
                OutputStream os = new FileOutputStream(new File("photo.jpg"));
                byte[] content = new byte[2048];
                int size = 0;
                while ((size = is.read(content)) != -1) {
                    os.write(content, 0, size);
                }
                ImageView img = new ImageView(new Image("file:photo.jpg", 250, 250, true, true));
                os.close();
                is.close();
                med.setImage(img);
                data.add(med);
            }
            Show(data);
            connection.close();

        } catch (SQLException ex) {
            Logger.getLogger(ALLController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    boolean created = false;

    @FXML
    private void toBasket(ActionEvent e) throws IOException {
        vb.getChildren().clear();
        out.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Basket.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.show();
    }

    @FXML
    private void Find(ActionEvent event) {
        String name_toFind = toFind.getText().toLowerCase();
        dataCopy = FXCollections.observableArrayList();
        dataCopy.clear();
        for (Medicine i : data) {
            Calendar calendar = Calendar.getInstance();
            java.util.Date currentDate = calendar.getTime();
            if (ifExpire.isSelected() && !ifEst.isSelected()) {
                if ((i.getName().toLowerCase().contains(name_toFind) || i.getForIllnesses().toLowerCase().contains(name_toFind)) && (i.getExpiry_Date().after(currentDate))) {
                    dataCopy.add(i);
                }
            } else if (!ifExpire.isSelected() && !ifEst.isSelected()) {
                if ((i.getName().toLowerCase().contains(name_toFind) || i.getForIllnesses().toLowerCase().contains(name_toFind))) {
                    dataCopy.add(i);
                }
            } else if (ifExpire.isSelected() && ifEst.isSelected()) {
                if ((i.getCount() >= 1) && (i.getName().toLowerCase().contains(name_toFind) || i.getForIllnesses().toLowerCase().contains(name_toFind)) && (i.getExpiry_Date().after(currentDate))) {
                    dataCopy.add(i);
                }
            } else {
                if ((i.getCount() >= 1) && (i.getName().toLowerCase().contains(name_toFind) || i.getForIllnesses().toLowerCase().contains(name_toFind))) {
                    dataCopy.add(i);
                }
            }
        }
        Show(dataCopy);
    }

    private void Show(ObservableList<Medicine> dataCopy) {
        vb = new VBox();
        vb.setMinWidth(866);
        vb.setMaxWidth(866);
        vb.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        btns = new ArrayList<>();
        for (Medicine med : dataCopy) {
            HBox hb = new HBox();
            VBox forInfo = new VBox();
            hb.getChildren().add(med.getImage());
            Label ID = new Label("ID: " + med.getID());
            Label Name = new Label("Name: " + med.getName());
            Label DateCreated = new Label("Date Created: " + med.getDateCreated().toString());
            Label ExpireDateL = new Label("Expire Date: " + med.getExpiry_Date().toString());
            String[] cons = med.getConsist().split(" ");
            StringBuilder neww = new StringBuilder();
            if (cons.length >= 10) {
                for (int i = 0; i < cons.length - 10; i += 10) {
                    neww.append(cons[i] + " " + cons[i + 1] + " " + cons[i + 2] + " " + cons[i + 3] + " " + cons[i + 4] + cons[i + 5] + " " + cons[i + 6] + " " + cons[i + 7] + " " + cons[i + 8] + " " + cons[i + 9] + "\n");

                }
            } else {
                neww.append(med.getConsist());
            }
            med.setConsist(neww.toString());

            Label Consist = new Label("Consist: " + med.getConsist());
            Label Cost = new Label("Price: " + med.getCost());
            Label Temperature = new Label("" + med.getTemp().toString() + "");
            Label forIll = new Label("For diseses: " + med.getForIllnesses());
            Label Count = new Label("Count: " + med.getCount());
            Button b1 = new Button("BUY");
            b1.setId(med.getID() + "");
            btns.add(b1);
            forInfo.getChildren().addAll(ID, Name, DateCreated, ExpireDateL, Consist, Cost, Temperature, forIll, Count, b1);
            forInfo.setPadding(new Insets(0, 0, 0, 20));
            hb.getChildren().add(forInfo);
            hb.setPadding(new Insets(0, 0, 20, 0));
            String cssLayout = "-fx-border-color: green;\n"
                    + "-fx-border-insets: 5;\n"
                    + "-fx-border-width: 3;\n"
                    + "-fx-border-style: dashed;\n";
            hb.setStyle(cssLayout);
            vb.getChildren().add(hb);
            for (Button b : btns) {
                b.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        out.print(b.getId() + "\n");
                    }
                });
            }
        }
        VBOX.setContent(vb);

    }
}
