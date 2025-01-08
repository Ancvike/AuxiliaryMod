package auxiliary.function;

import arc.struct.Seq;
import mindustry.Vars;
import mindustry.ui.Styles;

public class FunctionManager {
    public static final Seq<Function> functions = new Seq<>();

    public static void init() {
        functions.addAll(new FullResource(), new BuildingRestoration());

        Vars.ui.hudGroup.fill(t -> {
            for (Function function : functions) {
                t.add(function.setTable()).size(40f).tooltip(tt -> {
                    tt.setBackground(Styles.black6);
                    tt.label(() -> function.labelName).pad(2f);
                });
                t.row();
            }
            t.right();
        });
    }
}
