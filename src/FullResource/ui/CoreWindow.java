package FullResource.ui;

import arc.*;
import mindustry.game.*;
import arc.graphics.*;
import arc.math.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.core.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.blocks.storage.*;

import static FullResource.core.Core.dialog;
import static mindustry.Vars.*;

public class CoreWindow extends Window {
    Table window;
    float heat;
    final ObjectMap<Team, ItemData> itemData = new ObjectMap<>();

    public CoreWindow()  {
        super(Icon.list, "core");
        height = 300;
        width = 300;

        resetUsed();

        Events.run(EventType.Trigger.update, () -> {
            heat += Time.delta;
            if(heat >= 60f) {
                heat = 0f;
                ScrollPane pane = find("core-pane");
                pane.setWidget(rebuild());
                for(Team team : getTeams()) {
                    if(!itemData.containsKey(team)) itemData.put(team, new ItemData());
                    itemData.get(team).updateItems(team);
                }
            }
        });
        dialog.add("CoreWindow触发").row();
    }

    @Override
    public void buildBody(Table table) {
        window = table;

        table.background(Styles.black8).top();
        table.pane(Styles.noBarPane, rebuild()).grow().name("core-pane").get().setScrollingDisabled(true, false);
        Events.on(EventType.WorldLoadEvent.class, e -> resetUsed());
    }

    Table rebuild() {
        return new Table(table -> {
            table.top();
            for(Team team : getTeams()) {
                table.table(row-> {
                    row.center();
                    row.add(setTable(team)).margin(8f).row();
                    row.image().height(4f).color(team.color).growX();
                }).growX().row();
            }
        });
    }

    public Seq<Team> getTeams(){
        return Seq.with(Team.all).filter(Team::active);
    }

    public void resetUsed(){
        for(Team team : getTeams()) {
            itemData.put(team, new ItemData());
        }
    }

    public Table setTable(Team team){
        return new Table(table -> {
            table.add(team.name).color(team.color).row();
            int max = Math.max(1, Math.round(window.getWidth()/2/60));
            table.table(itemTable -> {
                int row = 0;

                CoreBlock.CoreBuild core = team.core();
                if(core == null || core.items == null) {
                    return;
                }
                for(int i = 0; i < Vars.content.items().size; i++){
                    Item item = Vars.content.item(i);
                    if(!team.items().has(item)) continue;
                    itemTable.stack(
                            new Table(ttt -> {
                                ttt.image(item.uiIcon).size(iconSmall).tooltip(tttt -> tttt.background(Styles.black6).add(item.localizedName).style(Styles.outlineLabel).margin(2f));
                                ttt.add(UI.formatAmount(core.items.get(item))).minWidth(5 * 8f).left();
                            }),
                            new Table(ttt -> {
                                ttt.bottom().right();
                                if(itemData == null || itemData.get(team) == null) return;
                                int amount = itemData.get(team).updateItems.isEmpty()?0:Mathf.floor(itemData.get(team).updateItems.get(item.id).amount);
                                Label label = new Label(amount + "/s");
                                label.setFontScale(0.65f);
                                label.setColor(amount > 0 ? Color.green : amount == 0 ? Color.orange : Color.red);
                                ttt.add(label).bottom().right().padTop(16f);
                                ttt.pack();
                            })).padRight(3).left();
                    if(row++ % max == max-1){
                        itemTable.row();
                    }
                }
            }).row();
        });
    }

    static class ItemData {
        final Seq<ItemStack> prevItems = new Seq<>();
        final Seq<ItemStack> updateItems = new Seq<>();

        ItemData() {
            resetItems();
        }

        public void resetItems(){
            Seq<ItemStack> stacks = Vars.content.items().map(item -> new ItemStack(item, 0));
            updateItems.clear().addAll(stacks);
            prevItems.clear().addAll(stacks);
        }

        public void updateItems(Team team){
            CoreBlock.CoreBuild core = team.core();
            if (core == null || core.items == null) return;

            Seq<ItemStack> stack = updateItems;
            if(stack.isEmpty()) {
                Vars.content.items().each(i -> stack.add(new ItemStack(i, 0)));
            }

            for (Item item : Vars.content.items()) {
                stack.get(item.id).set(item, core.items.get(item) - prevItems.get(item.id).amount);
                prevItems.get(item.id).set(item, core.items.get(item));
            }
            prevItems.clear().addAll(Vars.content.items().map(i -> new ItemStack(i, core.items.get(i))));
        }
    }
}