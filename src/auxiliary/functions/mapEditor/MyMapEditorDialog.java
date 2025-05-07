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
                Log.info("成功替换地图编辑器实例");
                editor.beginEdit(MapIO.createMap(file, true));
                show();
            } catch (Exception e) {
                Log.err(e);
                ui.showException("@editor.errorload", e);
            }
        });
    }
}
