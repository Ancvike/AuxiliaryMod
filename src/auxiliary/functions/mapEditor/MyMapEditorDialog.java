package auxiliary.functions.mapEditor;

import arc.files.Fi;
import arc.util.Log;
import mindustry.io.MapIO;
import mindustry.ui.dialogs.BaseDialog;

import static mindustry.Vars.editor;
import static mindustry.Vars.ui;

public class MyMapEditorDialog extends mindustry.editor.MapEditorDialog {
    public void beginEditMap(Fi file){
        ui.loadAnd(() -> {
            try{
                BaseDialog baseDialog = new BaseDialog("Loading Successfully");
                baseDialog.addCloseButton();
                baseDialog.show();
                editor.beginEdit(MapIO.createMap(file, true));
                show();
            }catch(Exception e){
                Log.err(e);
                ui.showException("@editor.errorload", e);
            }
        });
    }
}