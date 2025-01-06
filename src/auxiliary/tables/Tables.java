package auxiliary.tables;

import arc.scene.ui.Slider;
import arc.scene.ui.layout.Table;

public class Tables {
    public static final Table UITable = new Table();

    public static void init() {
        setUITable();
    }

    public static void setUITable() {
        UITable.add("调整当前图标的位置").row();
        UITable.add("X:");
        Slider sliderX = new Slider(0, 750, 1, false);
        UITable.add(sliderX.getValue() + "").row();
        UITable.add("Y:");
        Slider sliderY = new Slider(0, 400, 1, false);
        UITable.add(sliderY.getValue() + "").row();
    }

    public static void changeSlider(Slider slider) {
        
    }
}
