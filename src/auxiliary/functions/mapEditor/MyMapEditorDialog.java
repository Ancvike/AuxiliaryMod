package auxiliary.functions.mapEditor;

import arc.files.Fi;
import arc.util.*;
import mindustry.editor.*;
import mindustry.io.MapIO;

import static mindustry.Vars.*;

public class MyMapEditorDialog extends MapEditorDialog{

    public void beginEditMap(Fi file){
        ui.loadAnd(() -> {
            try{
                editor.beginEdit(MapIO.createMap(file, true));
                show();
            }catch(Exception e){
                Log.err(e);
                ui.showException("@editor.errorload", e);
            }
        });
    }
}
