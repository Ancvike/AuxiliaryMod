package auxiliary.function;

import arc.scene.ui.Dialog;
import arc.scene.ui.Label;
import arc.scene.ui.Slider;
import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.ui.Styles;

import static auxiliary.dialogs.Dialogs.setDialog_yes;

public class UIMovement {
    private static final Dialog dialog_movement = new Dialog();
    private static final Table table = new Table();
    public static int[] position;
    private static final LabelShow label = new LabelShow("资源全满", 400, 400);
    private static final LabelShow label2 = new LabelShow("建筑修复", 450, 400);


    public static void init() {

    }

    public static void onClick() {
        position = new int[4];
        position[0] = label.getX();
        position[1] = label.getY();
        position[2] = label2.getX();
        position[3] = label2.getY();
        setTable();
        setDialog_yes(dialog_movement, table);
        dialog_movement.show();
    }

    private static void setTable() {
        table.add("UI移动界面").row();
        table.add(label.setTable()).row();
        table.add(label2.setTable()).row();
        table.button("确定", UIMovement::click_yes).size(120f, 50f);
        table.button("取消", UIMovement::click_no).size(120f, 50f);
        table.button("重置", UIMovement::click_reset).size(120f, 50f);
    }

    private static void click_no() {
        dialog_movement.hide();
        dialog_movement.removeChild(table);
    }

    private static void click_yes() {
        reset();
        click_no();
    }
    public static void click_reset() {
        Vars.ui.hudGroup.removeChild(Vars.ui.hudGroup.find("full-resource"));
        Vars.ui.hudGroup.removeChild(Vars.ui.hudGroup.find("building-restoration"));
        Vars.ui.hudGroup.fill(t -> {
            t.name = "full-resource";
            t.button(Icon.fill, FullResource::onClick).size(50f).tooltip(tt -> {
                tt.setBackground(Styles.black6);
                tt.label(() -> "资源全满").pad(2f);
            });
            t.x = 400;
            t.top();
        });
        Vars.ui.hudGroup.fill(t -> {
            t.name = "building-restoration";
            t.button(Icon.refresh1, BuildingRestoration::onClick).size(50f).tooltip(tt -> {
                tt.setBackground(Styles.black6);
                tt.label(() -> "建筑修复").pad(2f);
            });
            t.x = 450;
            t.top();
        });
    }
    private static void reset() {
        Vars.ui.hudGroup.removeChild(Vars.ui.hudGroup.find("full-resource"));
        Vars.ui.hudGroup.removeChild(Vars.ui.hudGroup.find("building-restoration"));
        Vars.ui.hudGroup.fill(t -> {
            t.name = "full-resource";
            t.button(Icon.fill, FullResource::onClick).size(50f).tooltip(tt -> {
                tt.setBackground(Styles.black6);
                tt.label(() -> "资源全满").pad(2f);
            });
            t.x = label.getX();
            t.y = label.getY();
        });
        Vars.ui.hudGroup.fill(t -> {
            t.name = "building-restoration";
            t.button(Icon.refresh1, BuildingRestoration::onClick).size(50f).tooltip(tt -> {
                tt.setBackground(Styles.black6);
                tt.label(() -> "建筑修复").pad(2f);
            });
            t.x = label2.getX();
            t.y = label2.getY();
        });
    }
}

class LabelShow {
    private final String text;
    private int x;
    private int y;

    public LabelShow(String text, int x, int y) {
        this.text = text;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Table setTable() {
        Slider sliderX = new Slider(0, 750, 1, false);
        Slider sliderY = new Slider(0, 400, 1, false);
        sliderX.setValue(x);
        sliderY.setValue(y);
        Label labelX = new Label("X:" + sliderX.getValue());
        Label labelY = new Label("Y:" + sliderY.getValue());
        return new Table(t -> {
            t.add(text).row();
            t.add(labelX);
            t.add(sliderX).row();
            sliderX.changed(() -> labelX.setText("X:" + sliderX.getValue()));
            sliderX.change();
            sliderX.moved(movedX -> x = (int) movedX);

            t.add(labelY);
            t.add(sliderY);
            sliderY.changed(() -> labelY.setText("Y:" + sliderY.getValue()));
            sliderY.change();
            sliderY.moved(movedY -> y = (int) movedY);
        });
    }
}