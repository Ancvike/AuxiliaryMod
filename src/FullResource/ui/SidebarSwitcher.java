package FullResource.ui;

import arc.*;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.*;
import arc.util.Log;
import mindustry.*;
import mindustry.gen.*;

public class SidebarSwitcher {
    private int showIndex = 0;
    private final Table sidebarTable = new Table();
    private final Table[] sidebars;
    public final ImageButton imageButton = new ImageButton(Core.atlas.find("full-resource-frog"));

    public SidebarSwitcher(Table... sidebars) {
        this.sidebars = sidebars;

        Vars.ui.hudGroup.fill(t -> {
            t.add(imageButton).size(70, 70);
            t.top();
            t.x = 300;
        });
        rebuildSidebarTable();
    }

    public void rebuildSidebarTable() {
        sidebarTable.visible = Core.settings.getBool("sidebar");
        if (!sidebarTable.visible) return;

        Stack sidebarTables = new Stack();
        for (Table elem : sidebars) {
            sidebarTables.add(new Table(table -> {
                table.left();
                table.add(elem).growY();
            }));
            elem.setBackground(Tex.buttonEdge3);
            elem.visible = false;
        }
        sidebars[showIndex].visible = true;

        sidebarTable.clear();
        sidebarTable.top().left();
        sidebarTable.add(sidebarTables).grow();
        sidebarTable.row();

        sidebars[showIndex].invalidate();
        Log.info(sidebars[showIndex].getWidth());
        Log.info(sidebars[showIndex].getMinWidth());
        Log.info(sidebars[showIndex].getPrefWidth());
    }
}
