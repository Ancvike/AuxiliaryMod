package collection.core;

import arc.scene.ui.Label;
import arc.scene.ui.Slider;
import arc.scene.ui.layout.Table;
import mindustry.gen.Groups;

import static collection.ui.Dialogs.*;
import static mindustry.Vars.state;

public class BuildingRestoration {
    private static final Table table = new Table();

    public static void init() {
        table.add("你确定要修复所有建筑吗?").size(200f, 50f);
        table.button("确认", BuildingRestoration::click_yes).size(120f, 50f);
        table.button("取消", BuildingRestoration::click_no).size(120f, 50f);
    }

    public static void onClick() {
        if (!state.rules.waves && state.isCampaign()) {

            dialog_restoration.show();
        } else {
            dialog_no.show();
        }
    }

    public static void click_no() {
        dialog_restoration.hide();
    }

    public static void click_yes() {
        restoration();
        click_no();
    }

    public static void restoration() {
        Groups.build.each(b -> b.health(b.block.health));
    }

    Table rebuildRule() {
        return new Table(table -> {
            table.top().left();

            Label label = table.add("Block Health: ").get();
            Slider slider = new Slider(0, 100, 1, false);
            slider.changed(() -> label.setText("Block Health: " + (int) slider.getValue() + "%"));
            slider.change();
            slider.moved(hp -> Groups.build.each(b -> b.health(b.block.health * hp / 100)));
            table.add(slider);
        });
    }
}