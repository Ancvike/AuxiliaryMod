package FullResource.core;

import mindustry.Vars;
import mindustry.ui.dialogs.BaseDialog;

public class Core {
    public static BaseDialog dialog = new BaseDialog("测试");

    public Core() {
        dialog.addCloseButton();
        Vars.ui.hudGroup.fill(t -> {
            t.button("测试", () -> dialog.show());
            t.left();
            t.y = 200;
        });
    }
}