package auxiliary.functions.mapEditor;

import arc.files.Fi;
import arc.util.Log;
import mindustry.editor.*;
import mindustry.io.MapIO;
import mindustry.maps.Map;
import mindustry.world.Tile;
import mindustry.world.WorldContext;

import static mindustry.Vars.*;

public class MyMapEditorDialog extends MapEditorDialog {
    private final Context context = new Context();

    public MyMapEditorDialog() {
        super();
    }

    @Override
    public void beginEditMap(Fi file) {
        ui.loadAnd(() -> {
            try {
                Map map = MapIO.createMap(file, true);
                editor.tags.putAll(map.tags);
                if (map.file.parent().parent().name().equals("1127400") && steam) {
                    editor.tags.put("steamid", map.file.parent().name());
                }
                editor.load(() -> MapIO.loadMap(map, context));
                renderer.resize(editor.width(), editor.height());
                //editor.beginEdit(MapIO.createMap(file, true));
                show();
            } catch (Exception e) {
                Log.err(e);
                ui.showException("@editor.errorload", e);
            }
        });
    }
}

class Context implements WorldContext {
    @Override
    public Tile tile(int index) {
        return world.tiles.geti(index);
    }

    @Override
    public void resize(int width, int height) {
        world.resize(width, height);
    }

    @Override
    public Tile create(int x, int y, int floorID, int overlayID, int wallID) {
        Tile tile = new EditorTile(x, y, floorID, overlayID, wallID);
        editor.tiles().set(x, y, tile);
        return tile;
    }

    @Override
    public boolean isGenerating() {
        return world.isGenerating();
    }

    @Override
    public void begin() {
        world.beginMapLoad();
    }

    @Override
    public void end() {
        world.endMapLoad();
    }
}