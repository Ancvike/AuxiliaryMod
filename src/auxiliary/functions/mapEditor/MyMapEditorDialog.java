package auxiliary.functions.mapEditor;

import arc.files.Fi;
import arc.util.*;
import mindustry.editor.*;
import mindustry.io.MapIO;

import static mindustry.Vars.*;

public class MyMapEditorDialog extends MapEditorDialog {
    @Override
    public void beginEditMap(Fi file) {
        ui.loadAnd(() -> {
            try {
                if (file == null) Log.info("11111");
                editor.beginEdit(MapIO.createMap(file, true));
                show();
            } catch (Exception e) {
                Log.err(e);
                ui.showException("@editor.errorload", e);
            }
        });
    }
}
