package auxiliary.functions.mapEditor;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.Pixmap;
import arc.input.KeyCode;
import arc.math.Mathf;
import arc.scene.style.Drawable;
import arc.scene.ui.TextButton;
import arc.struct.StringMap;
import arc.util.Log;
import arc.util.Structs;
import arc.util.Time;
import mindustry.content.UnitTypes;
import mindustry.editor.*;
import mindustry.game.Gamemode;
import mindustry.game.Rules;
import mindustry.game.Teams;
import mindustry.gen.Groups;
import mindustry.gen.Icon;
import mindustry.io.MapIO;
import mindustry.maps.Map;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.ui.dialogs.MapPlayDialog;
import mindustry.world.meta.Env;

import static mindustry.Vars.*;
import static mindustry.Vars.platform;

public class MyMapEditorDialog extends MapEditorDialog {
    private MapView view;
    private MapInfoDialog infoDialog;
    private MapLoadDialog loadDialog;
    private MapResizeDialog resizeDialog;
    private MapGenerateDialog generateDialog;
    private SectorGenerateDialog sectorGenDialog;
    private MapPlayDialog playtestDialog;
    private BaseDialog menu;
    private Rules lastSavedRules;
    //currently never read

    public MyMapEditorDialog() {
        background(Styles.black);

        view = new MapView();
        infoDialog = new MapInfoDialog();
        generateDialog = new MapGenerateDialog(true);
        sectorGenDialog = new SectorGenerateDialog();
        playtestDialog = new MapPlayDialog();

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

            t.button("@editor.import", Icon.download, () -> createDialog("@editor.import", "@editor.importmap", "@editor.importmap.description", Icon.download, (Runnable) loadDialog::show, "@editor.importfile", "@editor.importfile.description", Icon.file, (Runnable) () -> platform.showFileChooser(true, mapExtension, file -> ui.loadAnd(() -> maps.tryCatchMapError(() -> {
                        if (MapIO.isImage(file)) {
                            ui.showInfo("@editor.errorimage");
                        } else {
                            editor.beginEdit(MapIO.createMap(file, true));
                        }
                    }))),

                    "@editor.importimage", "@editor.importimage.description", Icon.fileImage, (Runnable) () -> platform.showFileChooser(true, "png", file -> ui.loadAnd(() -> {
                        try {
                            Pixmap pixmap = new Pixmap(file);
                            editor.beginEdit(pixmap);
                            pixmap.dispose();
                        } catch (Exception e) {
                            ui.showException("@editor.errorload", e);
                            Log.err(e);
                        }
                    }))));

            t.button("@editor.export", Icon.upload, () -> createDialog("@editor.export", "@editor.exportfile", "@editor.exportfile.description", Icon.file, (Runnable) () -> platform.export(editor.tags.get("name", "unknown"), mapExtension, file -> MapIO.writeMap(file, editor.createMap(file))), "@editor.exportimage", "@editor.exportimage.description", Icon.fileImage, (Runnable) () -> platform.export(editor.tags.get("name", "unknown"), "png", file -> {
                Pixmap out = MapIO.writeImage(editor.tiles());
                file.writePng(out);
                out.dispose();
            })));

            t.row();

            t.button("@editor.ingame", Icon.right, this::editInGame);

            t.button("@editor.playtest", Icon.play, this::playtest);
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
            tryExit();
            menu.hide();
        }).padTop(1).size(swidth * 2f + 10, 60f);

        resizeDialog = new MapResizeDialog((width, height, shiftX, shiftY) -> {
            if (!(editor.width() == width && editor.height() == height && shiftX == 0 && shiftY == 0)) {
                ui.loadAnd(() -> editor.resize(width, height, shiftX, shiftY));
            }
        });

        loadDialog = new MapLoadDialog(map -> ui.loadAnd(() -> {
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
                doInput();
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

    private void createDialog(String title, Object... arguments) {
        BaseDialog dialog = new BaseDialog(title);

        float h = 90f;

        dialog.cont.defaults().size(360f, h).padBottom(5).padRight(5).padLeft(5);

        for (int i = 0; i < arguments.length; i += 4) {
            String name = (String) arguments[i];
            String description = (String) arguments[i + 1];
            Drawable iconname = (Drawable) arguments[i + 2];
            Runnable listenable = (Runnable) arguments[i + 3];

            TextButton button = dialog.cont.button(name, () -> {
                listenable.run();
                dialog.hide();
                menu.hide();
            }).left().margin(0).get();

            button.clearChildren();
            button.image(iconname).padLeft(10);
            button.table(t -> {
                t.add(name).growX().wrap();
                t.row();
                t.add(description).color(Color.gray).growX().wrap();
            }).growX().pad(10f).padLeft(5);

            button.row();

            dialog.cont.row();
        }

        dialog.addCloseButton();
        dialog.show();
    }

    private void editInGame() {
        menu.hide();
        ui.loadAnd(() -> {
            lastSavedRules = state.rules;
            hide();
            //only reset the player; logic.reset() will clear entities, which we do not want
            state.teams = new Teams();
            player.reset();
            state.rules = Gamemode.editor.apply(lastSavedRules.copy());
            state.rules.limitMapArea = false;
            state.rules.sector = null;
            state.rules.fog = false;
            state.map = new Map(StringMap.of("name", "Editor Playtesting", "width", editor.width(), "height", editor.height()));
            world.endMapLoad();
            player.set(world.width() * tilesize / 2f, world.height() * tilesize / 2f);
            Core.camera.position.set(player);
            player.clearUnit();

            for (var unit : Groups.unit) {
                if (unit.spawnedByCore) {
                    unit.remove();
                }
            }

            Groups.build.clear();
            Groups.weather.clear();
            logic.play();

            if (player.team().core() == null) {
                player.set(world.width() * tilesize / 2f, world.height() * tilesize / 2f);
                var unit = (state.rules.hasEnv(Env.scorching) ? UnitTypes.evoke : UnitTypes.alpha).spawn(player.team(), player.x, player.y);
                unit.spawnedByCore = true;
                player.unit(unit);
            }

            player.checkSpawn();
        });
    }

    private void playtest() {
        menu.hide();
        Map map = save();

        if (map != null) {
            //skip dialog, play immediately when shift clicked
            if (Core.input.shift()) {
                hide();
                //auto pick best fit
                control.playMap(map, map.applyRules(Gamemode.survival.valid(map) ? Gamemode.survival : Gamemode.attack.valid(map) ? Gamemode.attack : Gamemode.sandbox), true);
            } else {
                playtestDialog.playListener = this::hide;
                playtestDialog.show(map, true);
            }
        }
    }

    private void tryExit() {
        ui.showConfirm("@confirm", "@editor.unsaved", this::hide);
    }

    private void doInput() {

        if (Core.input.ctrl()) {
            //alt mode select
            for (int i = 0; i < view.getTool().altModes.length; i++) {
                if (i + 1 < KeyCode.numbers.length && Core.input.keyTap(KeyCode.numbers[i + 1])) {
                    view.getTool().mode = i;
                }
            }
        } else {
            for (EditorTool tool : EditorTool.all) {
                if (Core.input.keyTap(tool.key)) {
                    view.setTool(tool);
                    break;
                }
            }
        }

        if (Core.input.keyTap(KeyCode.escape)) {
            if (!menu.isShown()) {
                menu.show();
            }
        }

        if (Core.input.keyTap(KeyCode.r)) {
            editor.rotation = Mathf.mod(editor.rotation + 1, 4);
        }

        if (Core.input.keyTap(KeyCode.e)) {
            editor.rotation = Mathf.mod(editor.rotation - 1, 4);
        }

        //ctrl keys (undo, redo, save)
        if (Core.input.ctrl()) {
            if (Core.input.keyTap(KeyCode.z)) {
                editor.undo();
            }

            if (Core.input.keyTap(KeyCode.y)) {
                editor.redo();
            }

            if (Core.input.keyTap(KeyCode.s)) {
                save();
            }

            if (Core.input.keyTap(KeyCode.g)) {
                view.setGrid(!view.isGrid());
            }
        }
    }
}