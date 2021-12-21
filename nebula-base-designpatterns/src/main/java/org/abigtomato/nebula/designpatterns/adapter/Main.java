package org.abigtomato.nebula.designpatterns.adapter;

/**
 * @author abigtomato
 */
public class Main {

    public static void main(String[] args) {
        Print print = new PrintBannerAdapter("Say Hello");
        print.printWeak();
        print.printStrong();
    }
}
