package FullResource.core;

import arc.scene.ui.layout.Table;

import static FullResource.ui.Dialogs.*;
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

    }
}