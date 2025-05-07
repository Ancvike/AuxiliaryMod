package auxiliary.functions.mapEditor;

import arc.files.Fi;
import arc.util.Log;
import mindustry.editor.MapEditorDialog;
import mindustry.io.MapIO;

import static mindustry.Vars.editor;
import static mindustry.Vars.ui;

public class MyMapEditorDialog extends MapEditorDialog {

    public MyMapEditorDialog() {
        super();
    }

    @Override
    public void beginEditMap(Fi file) {
        ui.loadAnd(() -> {
            try {
                Log.info("Loading map...");
                editor.beginEdit(MapIO.createMap(file, true));
                show();
            } catch (Exception e) {
                Log.err(e);
                ui.showException("@editor.errorload", e);
            }
        });
    }
}