package auxiliary.functions.mapEditor;

import arc.Core;
import arc.util.Log;
import arc.util.Structs;
import arc.util.Time;
import mindustry.editor.*;
import mindustry.game.Gamemode;
import mindustry.gen.Icon;
import mindustry.maps.Map;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

import static mindustry.Vars.*;
import static mindustry.Vars.platform;

public class MyMapEditorDialog extends MapEditorDialog {
    private MapView view;
    private MapInfoDialog infoDialog;
    private MapResizeDialog resizeDialog;
    private MapGenerateDialog generateDialog;
    private SectorGenerateDialog sectorGenDialog;
    private BaseDialog menu;

    public MyMapEditorDialog() {
        background(Styles.black);

        view = new MapView();
        infoDialog = new MapInfoDialog();
        generateDialog = new MapGenerateDialog(true);
        sectorGenDialog = new SectorGenerateDialog();

        menu = new BaseDialog("@menu");
        menu.addCloseButton();

        float swidth = 180f;

        menu.cont.table(t -> {
            t.defaults().size(swidth, 60f).padBottom(5).padRight(5).padLeft(5);

            t.button("@editor.savemap", Icon.save, this::save);

            t.button("@editor.mapinfo", Icon.pencil, () -> {
                infoDialog.show();
                menu.hide();
            });

            t.row();

            t.button("@editor.generate", Icon.terrain, () -> {
                generateDialog.show(generateDialog::applyToEditor);
                menu.hide();
            });

            t.button("@editor.resize", Icon.resize, () -> {
                resizeDialog.show();
                menu.hide();
            });

            t.row();

            t.button("@editor.import", Icon.download, () -> {
            });

            t.button("@editor.export", Icon.upload, () -> {
            });

            t.row();

            t.button("@editor.ingame", Icon.right, () -> Log.info("ingame"));

            t.button("@editor.playtest", Icon.play, () -> Log.info("playtest"));
        });

        menu.cont.row();

        if (steam) {
            menu.cont.button("@editor.publish.workshop", Icon.link, () -> {
                Map builtin = maps.all().find(m -> m.name().equals(editor.tags.get("name", "").trim()));

                if (editor.tags.containsKey("steamid") && builtin != null && !builtin.custom) {
                    platform.viewListingID(editor.tags.get("steamid"));
                    return;
                }

                Map map = save();

                if (editor.tags.containsKey("steamid") && map != null) {
                    platform.viewListing(map);
                    return;
                }

                if (map == null) return;

                if (map.tags.get("description", "").length() < 4) {
                    ui.showErrorMessage("@editor.nodescription");
                    return;
                }

                if (!Structs.contains(Gamemode.all, g -> g.valid(map))) {
                    ui.showErrorMessage("@map.nospawn");
                    return;
                }

                platform.publish(map);
            }).padTop(-3).size(swidth * 2f + 10, 60f).update(b -> b.setText(editor.tags.containsKey("steamid") ? editor.tags.get("author", "").equals(steamPlayerName) ? "@workshop.listing" : "@view.workshop" : "@editor.publish.workshop"));

            menu.cont.row();
        }

        menu.cont.button("@editor.sectorgenerate", Icon.terrain, () -> {
            menu.hide();
            sectorGenDialog.show();
        }).padTop(!steam ? -3 : 1).size(swidth * 2f + 10, 60f);
        menu.cont.row();

        menu.cont.row();

        menu.cont.button("@quit", Icon.exit, () -> {
            Log.info("tryExit");
            //tryExit();
            menu.hide();
        }).padTop(1).size(swidth * 2f + 10, 60f);

        resizeDialog = new MapResizeDialog((width, height, shiftX, shiftY) -> {
            if (!(editor.width() == width && editor.height() == height && shiftX == 0 && shiftY == 0)) {
                ui.loadAnd(() -> editor.resize(width, height, shiftX, shiftY));
            }
        });

        new MapLoadDialog(map -> ui.loadAnd(() -> {
            try {
                editor.beginEdit(map);
            } catch (Exception e) {
                ui.showException("@editor.errorload", e);
                Log.err(e);
            }
        }));

        setFillParent(true);

        clearChildren();
        margin(0);

        update(() -> {
            if (hasKeyboard()) {
//                Log.info("doInput");
                //doInput();
            }
        });

        shown(() -> {

            if (!Core.settings.getBool("landscape")) platform.beginForceLandscape();
            editor.clearOp();
            Core.scene.setScrollFocus(view);

            Time.runTask(10f, platform::updateRPC);
        });

        hidden(() -> {
            editor.clearOp();
            platform.updateRPC();
            if (!Core.settings.getBool("landscape")) platform.endForceLandscape();
        });

        shown(this::build);
    }
}