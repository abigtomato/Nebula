package org.abigtomato.nebula.designpatterns.adapter;

/**
 * 类基础型适配器
 *
 * @author abigtomato
 */
public class PrintBannerAdapter extends Banner implements Print {

    public PrintBannerAdapter(String strings) {
        super(strings);
    }

    @Override
    public void printWeak() {
        showWithParen();
    }

    @Override
    public void printStrong() {
        showWithAster();
    }
}
