package org.abigtomato.nebula.designpatterns.template;

/**
 * @author abigtomato
 */
public class Main {

    public static void main(String[] args) {
        CharDisplay charDisplay = new CharDisplay('H');
        StringDisplay stringDisplay = new StringDisplay("Hello Java!");
        charDisplay.display();
        System.out.println();
        stringDisplay.display();
    }
}
