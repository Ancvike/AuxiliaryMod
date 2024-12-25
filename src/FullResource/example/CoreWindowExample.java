//package FullResource.example;
//
//import arc.*;
//import mindustry.game.*;
//import arc.scene.ui.*;
//import arc.scene.ui.layout.*;
//import arc.struct.*;
//import arc.util.*;
//import mindustry.*;
//import mindustry.core.*;
//import mindustry.gen.*;
//import mindustry.type.*;
//import mindustry.ui.*;
//import mindustry.world.blocks.storage.*;
//import FullResource.ui.Window;
//
//import static mindustry.Vars.*;
//
//public class CoreWindowExample extends Window {
//    Table window;
//    float heat;
//
//    public CoreWindowExample() {
//        super(Icon.list, "core");
//        height = 300;
//        width = 300;
//
//        Events.run(EventType.Trigger.update, () -> {
//            heat += Time.delta;
//            if (heat >= 60f) {
//                heat = 0f;
//                ScrollPane pane = find("core-pane");
//                pane.setWidget(rebuild());
//            }
//        });
//    }
//
//    @Override
//    public void buildBody(Table table) {
//        window = table;
//
//        table.background(Styles.black8).top();
//        table.pane(Styles.noBarPane, rebuild()).grow().name("core-pane").get().setScrollingDisabled(true, false);
//        Events.on(EventType.WorldLoadEvent.class, e -> {
//            // 如果需要重置其他数据，可以在这里添加代码
//        });
//    }
//
//    Table rebuild() {
//        return new Table(table -> {
//            table.top();
//            for (Team team : getTeams()) {
//                table.table(row -> {
//                    row.center();
//                    row.add(setTable(team)).margin(8f).row();
//                    row.image().height(4f).color(team.color).growX();
//                }).growX().row();
//            }
//        });
//    }
//
//    public Seq<Team> getTeams() {
//        return Seq.with(Team.all).select(Team::active);
//    }
//
//    public Table setTable(Team team) {
//        return new Table(table -> {
//            table.add(team.name).color(team.color).row();
//            table.table(itemTable -> {
//                CoreBlock.CoreBuild core = team.core();
//                if (core == null || core.items == null) {
//                    return;
//                }
//                for (int i = 0; i < Vars.content.items().size; i++) {
//                    Item item = Vars.content.item(i);
//                    if (!team.items().has(item)) continue;
//                    itemTable.stack(
//                            new Table(ttt -> {
//                                ttt.image(item.uiIcon).size(iconSmall).tooltip(tttt -> tttt.background(Styles.black6).add(item.localizedName).style(Styles.outlineLabel).margin(2f));
//                                ttt.add(UI.formatAmount(core.items.get(item))).minWidth(5 * 8f).left();
//                            })
//                    ).padRight(3).left();
//                }
//            }).row();
//        });
//    }
//}
