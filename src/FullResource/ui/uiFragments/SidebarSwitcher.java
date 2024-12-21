package FullResource.ui.uiFragments;

import arc.*;
import arc.scene.ui.layout.*;
import arc.util.Log;
import mindustry.*;
import mindustry.gen.*;

public class SidebarSwitcher {
    private int showIndex = 0;
    private final Table sidebarTable = new Table();
    private final Table[] sidebars;

    public SidebarSwitcher(Table... sidebars) {
        this.sidebars = sidebars;

        Vars.ui.hudGroup.fill(t -> {
            t.name = "informatis sidebar";
            t.left();
            t.add(sidebarTable);
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