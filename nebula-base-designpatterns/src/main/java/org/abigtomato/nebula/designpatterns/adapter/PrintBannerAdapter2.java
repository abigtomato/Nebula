package org.abigtomato.nebula.designpatterns.adapter;

/**
 * 对象委托型适配器
 *
 * @author abigtomato
 */
public class PrintBannerAdapter2 {

    private final Banner banner;

    public PrintBannerAdapter2(String string) {
        this.banner = new Banner(string);
    }

    public void printWeak() {
        this.banner.showWithParen();
    }

    public void printStrong() {
        this.banner.showWithAster();
    }
}
