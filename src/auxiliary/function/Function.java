package auxiliary.function;

import arc.scene.style.Drawable;
import arc.scene.ui.layout.Table;
import mindustry.ui.Styles;

public class Function extends Table{
    public final String name;
    public final Drawable icon;
    public final Runnable listener;
    public final String labelName;


    public Function(String name, Drawable icon, Runnable listener, String labelName) {
        this.name = name;
        this.icon = icon;
        this.labelName = labelName;
        this.listener = listener;
    }

    public Table setTable() {
        return new Table(t -> {
            t.name = name;
            t.button(icon, listener).size(50f).tooltip(tt -> {
                tt.setBackground(Styles.black6);
                tt.label(() -> labelName).pad(2f);
            });
        });
    }
}