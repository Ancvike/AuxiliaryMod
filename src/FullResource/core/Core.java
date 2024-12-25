package FullResource.core;

import arc.scene.ui.Dialog;
import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.ui.Styles;

import static FullResource.ui.WindowManager.coreWindow;

public class Core {
    Table body;
    public static final Dialog dialog = new Dialog("失败");

    public Core() {
        setDialog();
        body = new Table(t -> {
            t.name = "Window Buttons";
            t.left();

            t.button(coreWindow.icon, Styles.emptyi, () -> {
                coreWindow.parent.setLayoutEnabled(false);
                coreWindow.toggle();

                coreWindow.setLayoutEnabled(true);

            }).size(40f).tooltip(tt -> {
                tt.setBackground(Styles.black6);
                tt.label(() -> "66666").pad(2f);
            });
            t.row();
        });

        Vars.ui.hudGroup.fill(t -> {
            t.add(body);
            t.top();
            t.x = 300;
        });
        dialog.show();
    }

    public static void setDialog() {
        dialog.add("区块未占领,无法使用该功能").row();
        dialog.addCloseButton();
    }
}