package org.abigtomato.nebula.designpatterns.adapter;

/**
 * 实际情况
 *
 * @author abigtomato
 */
public class Banner {

    private final String strings;

    public Banner(String strings) {
        this.strings = strings;
    }

    public void showWithParen() {
        System.out.println("(" + this.strings + ")");
    }

    public void showWithAster() {
        System.out.println("*" + this.strings + "*");
    }
}
