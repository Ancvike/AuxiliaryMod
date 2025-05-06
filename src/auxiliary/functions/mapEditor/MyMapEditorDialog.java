package auxiliary.functions.mapEditor;

import arc.files.Fi;
import arc.util.Log;
import mindustry.editor.MapEditorDialog;
import mindustry.game.Rules;
import mindustry.io.MapIO;

import static mindustry.Vars.*;
import static mindustry.Vars.state;

public class MyMapEditorDialog extends MapEditorDialog {
    boolean shownWithMap = false;

    public MyMapEditorDialog() {
        super();
        if (!shownWithMap) {
            logic.reset();
            state.rules = new Rules();
            editor.beginEdit(200, 200);
        }
        shownWithMap = false;
    }

    @Override
    public void beginEditMap(Fi file) {
        ui.loadAnd(() -> {
            try {
                shownWithMap = true;
                editor.beginEdit(MapIO.createMap(file, true));
                show();
            } catch (Exception e) {
                Log.err(e);
                ui.showException("@editor.errorload", e);
            }
        });
    }
}