package auxiliary.functions;

import arc.Core;
import arc.Events;
import arc.func.Cons;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.scene.event.Touchable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Dialog;
import arc.scene.ui.layout.Scl;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectMap;
import arc.util.Align;
import arc.util.Scaling;
import arc.util.Structs;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.TechTree;
import mindustry.core.UI;
import mindustry.ctype.ContentType;
import mindustry.ctype.UnlockableContent;
import mindustry.game.EventType;
import mindustry.game.Objectives;
import mindustry.game.SectorInfo;
import mindustry.gen.Icon;
import mindustry.gen.Iconc;
import mindustry.graphics.Pal;
import mindustry.maps.SectorDamage;
import mindustry.type.*;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.ui.dialogs.PlanetDialog;
import mindustry.world.blocks.storage.CoreBlock;

import static arc.Core.bundle;
import static arc.Core.settings;
import static mindustry.Vars.*;
import static mindustry.ui.dialogs.PlanetDialog.Mode.*;

public class Launch extends Function {
    MyPlanetDialog dialog = new MyPlanetDialog();

    public Launch() {
        super(0, "从此区块发射");

    }

    @Override
    public void onClick() {
        if (state.isCampaign()) {
            dialog.show();
        } else {
            Vars.ui.hudfrag.showToast("当前功能仅在战役中使用");
        }
    }


}

class MyPlanetDialog extends PlanetDialog {
    @Override
    public Dialog show() {
        updateSelected();
        return this.show(Core.scene);
    }

    void updateSelected() {
        Sector sector = selected;
        Table stable = sectorTop;

        if (sector == null) {
            stable.clear();
            stable.visible = false;
            return;
        }
        stable.visible = true;

        float x = stable.getX(Align.center), y = stable.getY(Align.center);
        stable.clear();
        stable.background(Styles.black6);

        stable.table(title -> {
            title.add("[accent]" + sector.name()).padLeft(3);
            if (sector.preset == null) {
                title.add().growX();

                title.button(Icon.pencilSmall, Styles.clearNonei, () -> ui.showTextInput("@sectors.rename", "@name", 20, sector.name(), v -> {
                    sector.setName(v);
                    updateSelected();
                    rebuildList();
                })).size(40f).padLeft(4);
            }

            var icon = sector.info.contentIcon != null ? new TextureRegionDrawable(sector.info.contentIcon.uiIcon) : Icon.icons.get(sector.info.icon + "Small");

            title.button(icon == null ? Icon.noneSmall : icon, Styles.clearNonei, iconSmall, () -> {
                new Dialog("") {{
                    closeOnBack();
                    setFillParent(true);

                    Runnable refresh = () -> {
                        sector.saveInfo();
                        hide();
                        updateSelected();
                        rebuildList();
                    };

                    cont.pane(t -> resized(true, () -> {
                        t.clearChildren();
                        t.marginRight(19f);
                        t.defaults().size(48f);

                        t.button(Icon.none, Styles.squareTogglei, () -> {
                            sector.info.icon = null;
                            sector.info.contentIcon = null;
                            refresh.run();
                        }).checked(sector.info.icon == null && sector.info.contentIcon == null);

                        int cols = (int) Math.min(20, Core.graphics.getWidth() / Scl.scl(52f));

                        int i;

                        for (ContentType ctype : defaultContentIcons) {
                            t.row();
                            t.image().colspan(cols).growX().width(Float.NEGATIVE_INFINITY).height(3f).color(Pal.accent);
                            t.row();

                            i = 0;
                            for (UnlockableContent u : content.getBy(ctype).<UnlockableContent>as()) {
                                if (!u.isHidden() && u.unlocked()) {
                                    t.button(new TextureRegionDrawable(u.uiIcon), Styles.squareTogglei, iconMed, () -> {
                                        sector.info.icon = null;
                                        sector.info.contentIcon = u;
                                        refresh.run();
                                    }).checked(sector.info.contentIcon == u);

                                    if (++i % cols == 0) t.row();
                                }
                            }
                        }
                    }));
                    buttons.button("@back", Icon.left, this::hide).size(210f, 64f);
                }}.show();
            }).size(40f).tooltip("@sector.changeicon");
        }).row();

        stable.image().color(Pal.accent).fillX().height(3f).pad(3f).row();

        boolean locked = sector.preset != null && sector.preset.locked() && !sector.hasBase() && sector.preset.techNode != null;

        if (locked) {
            stable.table(r -> {
                r.add("@complete").colspan(2).left();
                r.row();
                for (Objectives.Objective o : sector.preset.techNode.objectives) {
                    if (o.complete()) continue;

                    r.add("> " + o.display()).color(Color.lightGray).left();
                    r.image(o.complete() ? Icon.ok : Icon.cancel, o.complete() ? Color.lightGray : Color.scarlet).padLeft(3);
                    r.row();
                }
            }).row();
        } else if (!sector.hasBase()) {
            stable.add(Core.bundle.get("sectors.threat") + " [accent]" + sector.displayThreat()).row();
        }

        if (sector.isAttacked()) {
            addSurvivedInfo(sector, stable, false);
        } else if (!sector.hasBase() && sector.hasEnemyBase()) {
            stable.add("@sectors.enemybase");
            stable.row();
        }

        if (sector.save != null && sector.info.resources.any()) {
            stable.table(t -> {
                t.add("@sectors.resources").padRight(4);
                for (UnlockableContent c : sector.info.resources) {
                    if (c == null) continue; //apparently this is possible.
                    t.image(c.uiIcon).padRight(3).scaling(Scaling.fit).size(iconSmall);
                }
            }).padLeft(10f).fillX().row();
        }

        stable.row();

        if (sector.hasBase()) {
            stable.button("@stats", Icon.info, Styles.cleart, () -> showStats(sector)).height(40f).fillX().row();
        }

        if ((sector.hasBase() && mode == look) || canSelect(sector) || (sector.preset != null && sector.preset.alwaysUnlocked) || debugSelect) {
            stable.button(mode == select ? "@sectors.select" : sector.isBeingPlayed() ? "@sectors.resume" : sector.hasBase() ? "@sectors.go" : locked ? "@locked" : "发射-->", locked ? Icon.lock : Icon.play, this::playSelected).growX().height(54f).minWidth(170f).padTop(4).disabled(locked);
        }

        stable.pack();
        stable.setPosition(x, y, Align.center);

        stable.act(0f);
    }

    void rebuildList() {
        if (notifs == null) return;

        notifs.clear();

        var all = state.planet.sectors.select(Sector::hasBase);
        all.sort(Structs.comps(Structs.comparingBool(s -> !s.isAttacked()), Structs.comparingInt(s -> s.save == null ? 0 : -(int) s.save.meta.timePlayed)));

        notifs.pane(p -> {
            Runnable[] readd = {null};

            p.table(s -> {
                s.image(Icon.zoom).padRight(4);
                s.field(searchText, t -> {
                    searchText = t;
                    readd[0].run();
                }).growX().height(50f);
            }).growX().row();

            Table con = p.table().growX().get();
            con.touchable = Touchable.enabled;

            readd[0] = () -> {
                con.clearChildren();
                for (Sector sec : all) {
                    if (sec.hasBase() && (searchText.isEmpty() || sec.name().toLowerCase().contains(searchText.toLowerCase()))) {
                        con.button(t -> {
                            t.marginRight(10f);
                            t.left();
                            t.defaults().growX();

                            t.table(head -> {
                                head.left().defaults();

                                if (sec.isAttacked()) {
                                    head.image(Icon.warningSmall).update(i -> i.color.set(Pal.accent).lerp(Pal.remove, Mathf.absin(Time.globalTime, 9f, 1f))).padRight(4f);
                                }

                                String ic = sec.iconChar() == null ? "" : sec.iconChar() + " ";

                                head.add(ic + sec.name()).growX().wrap();
                            }).growX().row();

                            if (sec.isAttacked()) {
                                addSurvivedInfo(sec, t, true);
                            }
                        }, Styles.underlineb, () -> {
                        }).margin(8f).marginLeft(13f).marginBottom(6f).marginTop(6f).padBottom(3f).padTop(3f).growX().checked(b -> selected == sec).row();
                        //for resources: .tooltip(sec.info.resources.toString("", u -> u.emoji()))
                    }
                }

                if (con.getChildren().isEmpty()) {
                    con.add("@none.found").pad(10f);
                }
            };

            readd[0].run();
        }).grow().scrollX(false);
    }

    void addSurvivedInfo(Sector sector, Table table, boolean wrap) {
        if (!wrap) {
            table.add(sector.planet.allowWaveSimulation ? Core.bundle.format("sectors.underattack", (int) (sector.info.damage * 100)) : "@sectors.underattack.nodamage").wrapLabel(wrap).row();
        }

        if (sector.planet.allowWaveSimulation && sector.info.wavesSurvived >= 0 && sector.info.wavesSurvived - sector.info.wavesPassed >= 0 && !sector.isBeingPlayed()) {
            int toCapture = sector.info.attack || sector.info.winWave <= 1 ? -1 : sector.info.winWave - (sector.info.wave + sector.info.wavesPassed);
            boolean plus = (sector.info.wavesSurvived - sector.info.wavesPassed) >= SectorDamage.maxRetWave - 1;
            table.add(Core.bundle.format("sectors.survives", Math.min(sector.info.wavesSurvived - sector.info.wavesPassed, toCapture <= 0 ? 200 : toCapture) +
                    (plus ? "+" : "") + (toCapture < 0 ? "" : "/" + toCapture))).wrapLabel(wrap).row();
        }
    }

    void showStats(Sector sector) {
        BaseDialog dialog = new BaseDialog(sector.name());

        dialog.cont.pane(c -> {
            c.defaults().padBottom(5);

            if (sector.preset != null && sector.preset.description != null) {
                c.add(sector.preset.displayDescription()).width(420f).wrap().left().row();
            }

            c.add(Core.bundle.get("sectors.time") + " [accent]" + sector.save.getPlayTime()).left().row();

            if (sector.info.waves && sector.hasBase()) {
                c.add(Core.bundle.get("sectors.wave") + " [accent]" + (sector.info.wave + sector.info.wavesPassed)).left().row();
            }

            if (sector.isAttacked() || !sector.hasBase()) {
                c.add(Core.bundle.get("sectors.threat") + " [accent]" + sector.displayThreat()).left().row();
            }

            if (sector.save != null && sector.info.resources.any()) {
                c.add("@sectors.resources").left().row();
                c.table(t -> {
                    for (UnlockableContent uc : sector.info.resources) {
                        if (uc == null) continue;
                        t.image(uc.uiIcon).scaling(Scaling.fit).padRight(3).size(iconSmall);
                    }
                }).padLeft(10f).left().row();
            }

            //production
            displayItems(c, sector.getProductionScale(), sector.info.production);

            //export
            displayItems(c, sector.getProductionScale(), sector.info.export, "@sectors.export", t -> {
                if (sector.info.destination != null && sector.info.destination.hasBase()) {
                    String ic = sector.info.destination.iconChar();
                    t.add(Iconc.rightOpen + " " + (ic == null || ic.isEmpty() ? "" : ic + " ") + sector.info.destination.name()).padLeft(10f).row();
                }
            });

            //import
            if (sector.hasBase()) {
                displayItems(c, 1f, sector.info.importStats(sector.planet), "@sectors.import", t -> sector.info.eachImport(sector.planet, other -> {
                    String ic = other.iconChar();
                    t.add(Iconc.rightOpen + " " + (ic == null || ic.isEmpty() ? "" : ic + " ") + other.name()).padLeft(10f).row();
                }));
            }

            ItemSeq items = sector.items();

            //stored resources
            if (sector.hasBase() && items.total > 0) {

                c.add("@sectors.stored").left().row();
                c.table(t -> {
                    t.left();

                    t.table(res -> {

                        int i = 0;
                        for (ItemStack stack : items) {
                            res.image(stack.item.uiIcon).padRight(3);
                            res.add(UI.formatAmount(Math.max(stack.amount, 0))).color(Color.lightGray);
                            if (++i % 4 == 0) {
                                res.row();
                            }
                        }
                    }).padLeft(10f);
                }).left().row();
            }
        });

        dialog.addCloseButton();

        if (sector.hasBase()) {
            dialog.buttons.button("@sector.abandon", Icon.cancel, () -> abandonSectorConfirm(sector, dialog::hide));
        }

        dialog.show();
    }

    boolean canSelect(Sector sector) {
        if (mode == select) return sector.hasBase() && launchSector != null && sector.planet == launchSector.planet;
        if (mode == planetLaunch) return sector.id == sector.planet.startSector;
        if (sector.hasBase() || sector.id == sector.planet.startSector) return true;
        //preset sectors can only be selected once unlocked
        if (sector.preset != null) {
            TechTree.TechNode node = sector.preset.techNode;
            return sector.preset.unlocked() || node == null || node.parent == null || (node.parent.content.unlocked() && (!(node.parent.content instanceof SectorPreset preset) || preset.sector.hasBase()));
        }

        return sector.planet.generator != null ?
                //use planet impl when possible
                sector.planet.generator.allowLanding(sector) :
                sector.hasBase() || sector.near().contains(Sector::hasBase); //near an occupied sector
    }

    void playSelected() {
        if (selected == null) return;

        Sector sector = selected;

        if (sector.isBeingPlayed()) {
            //already at this sector
            hide();
            return;
        }

        if (sector.preset != null && sector.preset.locked() && sector.preset.techNode != null && !sector.hasBase()) {
            return;
        }

        //make sure there are no under-attack sectors (other than this one)
        for (Planet planet : content.planets()) {
            if (!planet.allowWaveSimulation && !debugSelect && planet.allowWaveSimulation == sector.planet.allowWaveSimulation) {
                //if there are two or more attacked sectors... something went wrong, don't show the dialog to prevent softlock
                Sector attacked = planet.sectors.find(s -> s.isAttacked() && s != sector);
                if (attacked != null && planet.sectors.count(Sector::isAttacked) < 2) {
                    BaseDialog dialog = new BaseDialog("@sector.noswitch.title");
                    dialog.cont.add(bundle.format("sector.noswitch", attacked.name(), attacked.planet.localizedName)).width(400f).labelAlign(Align.center).center().wrap();
                    dialog.addCloseButton();
                    dialog.show();

                    return;
                }
            }
        }

        boolean shouldHide = true;

        //save before launch.
        if (control.saves.getCurrent() != null && Vars.state.isGame() && mode != select) {
            try {
                control.saves.getCurrent().save();
            } catch (Throwable e) {
                e.printStackTrace();
                ui.showException("[accent]" + Core.bundle.get("savefail"), e);
            }
        }

        if (mode == look && !sector.hasBase()) {
            shouldHide = false;
            Sector from = Vars.state.rules.sector;

            if (from == null) {
                //clear loadout information, so only the basic loadout gets used
                universe.clearLoadoutInfo();
                //free launch.
                control.playSector(sector);
            } else {
                CoreBlock block = sector.allowLaunchSchematics() ? (from.info.bestCoreType instanceof CoreBlock b ? b : (CoreBlock) from.planet.defaultCore) : (CoreBlock) from.planet.defaultCore;

                loadouts.show(block, from, sector, () -> {
                    var loadout = universe.getLastLoadout();
                    var schemCore = loadout.findCore();
                    from.removeItems(loadout.requirements());
                    from.removeItems(universe.getLaunchResources());

                    Events.fire(new EventType.SectorLaunchLoadoutEvent(sector, from, loadout));

                    CoreBlock.CoreBuild core = player.team().core();
                    if (core == null || settings.getBool("skipcoreanimation")) {
                        //just... go there
                        control.playSector(from, sector);
                        //hide only after load screen is shown
                        Time.runTask(8f, this::hide);
                    } else {
                        //hide immediately so launch sector is visible
                        hide();

                        //allow planet dialog to finish hiding before actually launching
                        Time.runTask(5f, () -> {
                            Runnable doLaunch = () -> {
                                renderer.showLaunch(schemCore);
                                //run with less delay, as the loading animation is delayed by several frames
                                Time.runTask(152f, () -> control.playSector(from, sector));
                            };

                            //load launchFrom sector right before launching so animation is correct
                            if (!from.isBeingPlayed()) {
                                //run *after* the loading animation is done
                                Time.runTask(9f, doLaunch);
                                control.playSector(from);
                            } else {
                                doLaunch.run();
                            }
                        });
                    }
                });
            }
        } else {
            //sector should have base here
            control.playSector(sector);
        }

        if (shouldHide) hide();
    }

    void displayItems(Table c, float scl, ObjectMap<Item, SectorInfo.ExportStat> stats) {
        displayItems(c, scl, stats, "@sectors.production", t -> {
        });
    }

    void displayItems(Table c, float scl, ObjectMap<Item, SectorInfo.ExportStat> stats, String name, Cons<Table> builder) {
        Table t = new Table().left();

        int i = 0;
        for (var item : content.items()) {
            var stat = stats.get(item);
            if (stat == null) continue;
            int total = (int) (stat.mean * 60 * scl);
            if (total > 1) {
                t.image(item.uiIcon).padRight(3);
                t.add(UI.formatAmount(total) + " " + Core.bundle.get("unit.perminute")).color(Color.lightGray).padRight(3);
                if (++i % 3 == 0) {
                    t.row();
                }
            }
        }

        if (t.getChildren().any()) {
            c.defaults().left();
            c.add(name).row();
            builder.get(c);
            c.add(t).padLeft(10f).row();
        }
    }
}