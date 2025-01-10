package auxiliary.function;

import arc.Core;
import arc.graphics.Color;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.ui.dialogs.BaseDialog;

import static auxiliary.dialogs.Dialogs.setDialog_yes;
import static mindustry.Vars.mobile;

public class UIMovement extends Function {
    private static final BaseDialog dialog_movement = new BaseDialog("UI移动界面");
    private static Table table;
    private float xFloat;
    private float yFloat;
    private TextField xText;
    private TextField yText;
    private final Seq<String> exceptions = new Seq<>();

    public UIMovement() {
        super("ui-move", Icon.menu, "UI移动");
    }

    @Override
    public void onClick() {
        if (mobile && !Core.settings.getBool("landscape")){
            table = setDialogTable_mobile();
        }else {
            table = setDialogTable();
        }
        setDialog_yes(dialog_movement, table);
        dialog_movement.show();
    }
    public Table setDialogTable_mobile() {
        return new Table(t -> {
            t.add("UI移动界面").row();
            t.image(Icon.warningSmall).size(20f).row();
            t.add("(目前只支持按钮一起移动, 后续可能会支持更多UI元素移动)").row();
            t.add("当前屏幕像素范围").color(Color.yellow).row();
            t.add("X:[" + -(Core.graphics.getWidth() / 2) + "," + Core.graphics.getWidth() / 2 + "]").color(Color.red).row();
            t.add("Y:[" + -(Core.graphics.getHeight() / 2) + "," + Core.graphics.getHeight() / 2 + "]").color(Color.red).row();
            t.add("请在下方输入你要移动到的位置坐标").row();
            t.add("X:").color(Color.yellow).row();
            xText = t.field(null, text -> xText()).get();
            t.row();
            t.add("Y:").color(Color.yellow).row();
            yText = t.field(null, text -> yText()).get();
            t.row();
            t.button("确定", this::click_yes).size(80f, 30f);
            t.button("取消", this::click_no).size(80f, 30f);
            t.button("重置", this::click_reset).size(80f, 30f);
        });
    }
    public Table setDialogTable() {
        return new Table(t -> {
            t.add("UI移动界面");
            t.image(Icon.warningSmall);
            t.add("(目前只支持按钮一起移动, 后续可能会支持更多UI元素移动)").row();
            t.add("当前屏幕像素范围").color(Color.yellow).row();
            t.add("X:[" + -(Core.graphics.getWidth() / 2) + "," + Core.graphics.getWidth() / 2 + "]").color(Color.red).row();
            t.add("Y:[" + -(Core.graphics.getHeight() / 2) + "," + Core.graphics.getHeight() / 2 + "]").color(Color.red).row();
            t.add("请在下方输入你要移动到的位置坐标").row();
            t.add("X:").color(Color.yellow);
            xText = t.field(null, text -> xText()).get();
            t.row();
            t.add("Y:").color(Color.yellow);
            yText = t.field(null, text -> yText()).get();
            t.row();
            t.button("确定", this::click_yes).size(120f, 50f);
            t.button("取消", this::click_no).size(120f, 50f);
            t.button("重置", this::click_reset).size(120f, 50f);
        });
    }

    private void click_reset() {
        click_no();
        if (mobile && Core.settings.getBool("landscape")) {
            Vars.ui.hudGroup.removeChild(Vars.ui.hudGroup.find("auxiliary-functions-mobile-landscape"));
            Vars.ui.hudGroup.fill(t -> {
                t.name = "auxiliary-functions-mobile-landscape";
                t.add(FunctionManager.table);
                t.bottom();
            });
        } else {
            Vars.ui.hudGroup.removeChild(Vars.ui.hudGroup.find("auxiliary-functions"));
            Vars.ui.hudGroup.fill(t -> {
                t.name = "auxiliary-functions";
                t.add(FunctionManager.table);
                t.right();
            });
        }
    }

    private void click_no() {
        dialog_movement.hide();
        table.clearChildren();
    }

    private void click_yes() {
        BaseDialog dialog = new BaseDialog("错误");
        dialog.cont.add("移动失败,原因如下:").row();
        dialog.addCloseButton();
        try {
            xFloat = Float.parseFloat(xText.getText());
            if (xFloat < -((float) Core.graphics.getWidth() / 2) || xFloat > (float) Core.graphics.getWidth() / 2) {
                xText.setText("");
                dialog.cont.add("X值超出屏幕范围").row();
                exceptions.add("X值超出屏幕范围");
            }
        } catch (NumberFormatException e) {
            xText.setText("");
            dialog.cont.add("X不是一个数字").row();
            exceptions.add("X不是一个数字");
        }
        try {
            yFloat = Float.parseFloat(yText.getText());
            if (yFloat < -((float) Core.graphics.getHeight() / 2) || yFloat > (float) Core.graphics.getHeight() / 2) {
                yText.setText("");
                dialog.cont.add("Y值超出屏幕范围").row();
                exceptions.add("Y值超出屏幕范围");
            }
        } catch (NumberFormatException e) {
            yText.setText("");
            dialog.cont.add("Y不是一个数字").row();
            exceptions.add("Y不是一个数字");
        }
        if (exceptions.size > 0) {
            dialog.show();
        } else {
            if (mobile && Core.settings.getBool("landscape")) {
                Vars.ui.hudGroup.removeChild(Vars.ui.hudGroup.find("auxiliary-functions-mobile-landscape"));
                Vars.ui.hudGroup.fill(t -> {
                    t.name = "auxiliary-functions-mobile-landscape";
                    t.add(FunctionManager.table);
                    t.x = xFloat;
                    t.y = yFloat;
                });
            } else {
                Vars.ui.hudGroup.removeChild(Vars.ui.hudGroup.find("auxiliary-functions"));
                Vars.ui.hudGroup.fill(t -> {
                    t.name = "auxiliary-functions";
                    t.add(FunctionManager.table);
                    t.x = xFloat;
                    t.y = yFloat;
                });
            }
        }
        click_no();
    }

    private void xText() {
    }

    private void yText() {
    }
}