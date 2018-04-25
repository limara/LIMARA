/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package limara;

import java.sql.Date;
import javafx.scene.image.ImageView;

/**
 *
 * @author asus
 */
public class Medicine {

    private ImageView image;
    private int ID;
    private String name;
    private Date dateCreated;
    private Date expiry_Date;
    private int Count;
    private int Cost;
    private String forIllnesses;
    private String Consist;
    private Temperature temp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getExpiry_Date() {
        return expiry_Date;
    }

    public void setExpiry_Date(Date expiry_Date) {
        this.expiry_Date = expiry_Date;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int Count) {
        this.Count = Count;
    }

    public int getCost() {
        return Cost;
    }

    public void setCost(int Cost) {
        this.Cost = Cost;
    }

    public String getForIllnesses() {
        return forIllnesses;
    }

    public void setForIllnesses(String forIllnesses) {
        this.forIllnesses = forIllnesses;
    }

    public String getConsist() {
        return Consist;
    }

    public void setConsist(String Consist) {
        this.Consist = Consist;
    }

    public Temperature getTemp() {
        return temp;
    }

    public void setTemp(Temperature temp) {
        this.temp = temp;
    }

    public Medicine(int ID, String name, Date dateCreated, Date expiry_Date, int Count, int Cost, String temp, String Consist, String ill) {
        this.ID = ID;
        this.name = name;
        this.dateCreated = dateCreated;
        this.expiry_Date = expiry_Date;
        this.Count = Count;
        this.Cost = Cost;
        this.Consist = Consist;
        String[] masive = temp.split(" ");
        try {
            this.temp = new Temperature(Integer.parseInt(masive[6].split(",")[0]), Integer.parseInt(masive[10]));
        } catch (NumberFormatException er) {
            this.temp = new Temperature(0, Integer.parseInt(masive[10]));
        }
        this.forIllnesses = ill;
    }

    Medicine() {
    }

    public int getID() {
        return ID;
    }

}
