package FullResource.core;

import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.ui.dialogs.BaseDialog;

public class Core {
    public static BaseDialog dialog = new BaseDialog("测试");
    public static Table table = new Table();

    public Core() {
        dialog.addCloseButton();
        dialog.add(table).left().top();
        Vars.ui.hudGroup.fill(t -> {
            t.button("测试", () -> dialog.show());
            t.left();
            t.y = 200;
        });
    }
}