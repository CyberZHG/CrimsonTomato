package zhaohg.crimson.main;

public class DrawerItem {

    private int icon;
    private String text;

    public DrawerItem() {
    }

    public DrawerItem(int icon, String text) {
        this.setIcon(icon);
        this.setText(text);
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
