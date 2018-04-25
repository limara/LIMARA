/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package limara;

/**
 *
 * @author asus
 */
class Temperature {

    private int lowest;
    private int highest;

    public Temperature(int l, int h) {
        lowest = l;
        highest = h;
    }

    public Temperature(String temp) {
        String[] mas = temp.split(" ");
        lowest = Integer.parseInt(mas[0]);
        highest = Integer.parseInt(mas[1]);
    }

    @Override
    public String toString() {
        return "Store at a temperature at least \n" + lowest + ", and not exceeding " + highest;
    }
}
