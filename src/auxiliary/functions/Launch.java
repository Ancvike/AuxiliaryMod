package auxiliary.functions;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.input.KeyCode;
import arc.math.Mathf;
import arc.math.geom.Vec3;
import arc.scene.Element;
import arc.scene.event.ElementGestureListener;
import arc.scene.event.InputEvent;
import arc.scene.event.InputListener;
import arc.scene.event.Touchable;
import arc.scene.ui.Image;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.*;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.content.TechTree;
import mindustry.ctype.ContentType;
import mindustry.ctype.UnlockableContent;
import mindustry.game.EventType;
import mindustry.game.Objectives;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.maps.SectorDamage;
import mindustry.type.*;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.ui.dialogs.PlanetDialog;
import mindustry.world.blocks.storage.CoreBlock;

import static arc.Core.*;
import static auxiliary.functions.Menu.dialog;
import static mindustry.Vars.*;
import static mindustry.ui.dialogs.PlanetDialog.Mode.*;

public class Launch extends Function {
    MyPlanetDialog dialog = new MyPlanetDialog();

    public Launch() {
        super(2, "从此区块发射");
    }

    @Override
    public Table function() {
        return new Table(t -> t.button("使用", () -> {
            if (state.isCampaign()) {
                dialog.show();
                Menu.dialog.hide();
            } else {
                Vars.ui.hudfrag.showToast(Icon.cancel, "[scarlet]当前功能仅在战役中使用");
            }
        }).width(200f));
    }
}

class MyPlanetDialog extends PlanetDialog {

    public MyPlanetDialog() {
        //这里将 PlanetDialog 实例自身设置为渲染器，并设置 drawUi 标志为 true，表示需要绘制 UI。
        state.renderer = this;
        state.drawUi = true;

        //设置 shouldPause 标志为 true，表示在显示该对话框时游戏应该暂停
        shouldPause = true;

        //从游戏设置中加载上次访问的星球，如果未找到则默认设置为 serpulo 星球
        state.planet = content.getByName(ContentType.planet, Core.settings.getString("lastplanet", "serpulo"));

        //设置悬停标签的样式和对齐方式。
        hoverLabel.setStyle(Styles.outlineLabel);
        hoverLabel.setAlignment(Align.center);

        //调用 rebuildButtons () 方法来重建对话框的按钮布局。
        rebuildButtons();

        //当窗口大小调整时，重新调用 rebuildButtons () 方法来调整按钮布局。
        onResize(this::rebuildButtons);

        //添加了一个拖拽监听器，用于处理对话框的拖拽操作，并调整相机位置。
        dragged((cx, cy) -> {
            //no multitouch drag
            if (Core.input.getTouches() > 1) return;

            if (showing()) {
                newPresets.clear();
            }

            Vec3 pos = state.camPos;

            float upV = pos.angle(Vec3.Y);
            float xscale = 9f, yscale = 10f;
            float margin = 1;

            float speed = 1f - Math.abs(upV - 90) / 90f;

            pos.rotate(state.camUp, cx / xscale * speed);

            float amount = cy / yscale;
            amount = Mathf.clamp(upV + amount, margin, 180f - margin) - upV;

            pos.rotate(Tmp.v31.set(state.camUp).rotate(state.camDir, 90), amount);
        });

        //添加了一个滚动监听器，用于处理鼠标滚轮或触摸滚动事件，以调整缩放级别。
        addListener(new InputListener() {
            @Override
            public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY) {
                if (event.targetActor == MyPlanetDialog.this) {
                    zoom = Mathf.clamp(zoom + amountY / 10f, state.planet.minZoom, 2f);
                }
                return true;
            }
        });

        //添加了一个手势监听器，用于处理捏合缩放等手势操作。
        addCaptureListener(new ElementGestureListener() {
            float lastZoom = -1f;

            @Override
            public void zoom(InputEvent event, float initialDistance, float distance) {
                if (lastZoom < 0) {
                    lastZoom = zoom;
                }

                zoom = (Mathf.clamp(initialDistance / distance * lastZoom, state.planet.minZoom, 2f));
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, KeyCode button) {
                lastZoom = zoom;
            }
        });

        //当对话框显示时，调用 setup() 方法进行初始化设置。
        shown(this::setup);

        //根据游戏状态解锁默认的内容（如特定的建筑块或物品）。
        if (content.planets().contains(p -> p.sectors.contains(Sector::hasBase)) || Blocks.scatter.unlocked() || Blocks.router.unlocked()) {
            Seq.with(Blocks.junction, Blocks.mechanicalDrill, Blocks.conveyor, Blocks.duo, Items.copper, Items.lead).each(UnlockableContent::quietUnlock);
        }
    }

    boolean showing() {
        return newPresets.any();
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
                    if (c == null) continue;
                    t.image(c.uiIcon).padRight(3).scaling(Scaling.fit).size(iconSmall);
                }
            }).padLeft(10f).fillX().row();
        }

        stable.row();

        if ((sector.hasBase() && mode == look) || canSelect(sector) || (sector.preset != null && sector.preset.alwaysUnlocked) || debugSelect) {
            stable.button(mode == select ? "@sectors.select" : sector.isBeingPlayed() ? "@sectors.resume" : sector.hasBase() ? "@sectors.go" : locked ? "@locked" : "@sectors.launch", locked ? Icon.lock : Icon.play, this::playSelected).growX().height(54f).minWidth(170f).padTop(4).disabled(locked);
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
            table.add(Core.bundle.format("sectors.survives", Math.min(sector.info.wavesSurvived - sector.info.wavesPassed, toCapture <= 0 ? 200 : toCapture) + (plus ? "+" : "") + (toCapture < 0 ? "" : "/" + toCapture))).wrapLabel(wrap).row();
        }
    }

    boolean canSelect(Sector sector) {
        if (mode == select) return sector.hasBase() && launchSector != null && sector.planet == launchSector.planet;

        if (mode == planetLaunch) return sector.id == sector.planet.startSector;
        if (sector.hasBase() || sector.id == sector.planet.startSector) return true;

        if (sector.preset != null) {
            TechTree.TechNode node = sector.preset.techNode;
            return sector.preset.unlocked() || node == null || node.parent == null || (node.parent.content.unlocked() && (!(node.parent.content instanceof SectorPreset preset) || preset.sector.hasBase()));
        }

        return sector.planet.generator != null ?

                sector.planet.generator.allowLanding(sector) : sector.hasBase() || sector.near().contains(Sector::hasBase);
    }

    void playSelected() {
        if (selected == null) return;

        Sector sector = selected;

        if (sector.isBeingPlayed()) {
            hide();
            return;
        }

        if (sector.preset != null && sector.preset.locked() && sector.preset.techNode != null && !sector.hasBase()) {
            return;
        }

        for (Planet planet : content.planets()) {
            if (!planet.allowWaveSimulation && !debugSelect && planet.allowWaveSimulation == sector.planet.allowWaveSimulation) {
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

        if (control.saves.getCurrent() != null && Vars.state.isGame() && mode != select) {
            try {
                control.saves.getCurrent().save();
            } catch (Throwable e) {
                ui.showException("[accent]" + Core.bundle.get("savefail"), e);
            }
        }

        if (mode == look && !sector.hasBase()) {
            shouldHide = false;
            Sector from = Vars.state.rules.sector;

            if (from == null) {
                universe.clearLoadoutInfo();

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

                        control.playSector(from, sector);
                        Time.runTask(8f, this::hide);
                    } else {
                        hide();

                        Time.runTask(5f, () -> {
                            Runnable doLaunch = () -> {
                                renderer.showLaunch(schemCore);
                                Time.runTask(152f, () -> control.playSector(from, sector));
                            };

                            if (!from.isBeingPlayed()) {
                                Time.runTask(9f, doLaunch);
                                control.playSector(from);
                            } else {
                                doLaunch.run();
                            }
                        });
                    }
                });
            }
        } else if (mode == select) {
            listener.get(sector);
        } else if (mode == planetLaunch) {
            listener.get(sector);

            sector.planet.unlockedOnLand.each(UnlockableContent::unlock);
            control.playSector(sector);
        } else {

            control.playSector(sector);
        }

        if (shouldHide) hide();

        dialog.hide();
    }

    void setup() {
        searchText = "";
        zoom = state.zoom = 1f;
        state.uiAlpha = 1f;
        ui.minimapfrag.hide();

        clearChildren();

        margin(0f);

        stack(new Element() {
                  {
                      addListener(new ElementGestureListener() {
                          @Override
                          public void tap(InputEvent event, float x, float y, int count, KeyCode button) {
                              if (showing()) return;

                              if (hovered != null && selected == hovered && count == 2) {
                                  playSelected();
                              }

                              if (hovered != null && (canSelect(hovered) || debugSelect)) {
                                  selected = hovered;
                              }

                              if (selected != null) {
                                  updateSelected();
                              }
                          }
                      });
                  }

                  @Override
                  public void draw() {
                      planets.render(state);
                  }
              },
                new Table(t -> {
                    t.touchable = Touchable.disabled;
                    t.top();
                    t.label(() -> mode == select ? "@sectors.select" : "").style(Styles.outlineLabel).color(Pal.accent);
                }), buttons,

                new Table(t -> {
                    t.top().left();
                    ScrollPane pane = new ScrollPane(null, Styles.smallPane);
                    t.add(pane).colspan(2).row();

                    t.add().height(64f);
                    Table starsTable = new Table(Styles.black);
                    pane.setWidget(starsTable);
                    pane.setScrollingDisabled(true, false);
                }).visible(() -> mode != select),

                new Table(c -> expandTable = c)).grow();
        rebuildExpand();
    }

    void rebuildExpand() {
        Table c = expandTable;
        c.clear();
        c.visible(() -> !(graphics.isPortrait() && mobile));
        if (state.planet.sectors.contains(Sector::hasBase)) {
            int attacked = state.planet.sectors.count(Sector::isAttacked);

            c.top().right();
            c.defaults().width(290f);

            c.button(bundle.get("sectorlist") + (attacked == 0 ? "" : "\n[red]⚠[lightgray] " + bundle.format("sectorlist.attacked", "[red]" + attacked + "[]")), Icon.downOpen, Styles.squareTogglet, () -> sectorsShown = !sectorsShown).height(60f).checked(b -> {
                Image image = (Image) b.getCells().first().get();
                image.setDrawable(sectorsShown ? Icon.upOpen : Icon.downOpen);
                return sectorsShown;
            }).with(t -> t.left().margin(7f)).with(t -> t.getLabelCell().grow().left()).row();

            c.collapser(t -> {
                t.background(Styles.black8);

                notifs = t;
                rebuildList();
            }, false, () -> sectorsShown).padBottom(64f).row();
        }
    }

    void rebuildButtons() {
        buttons.clearChildren();

        buttons.bottom();

        if (Core.graphics.isPortrait()) {
            buttons.add(sectorTop).colspan(2).fillX().row();
            addBack();
        } else {
            addBack();
            buttons.add().growX();
            buttons.add(sectorTop).minWidth(230f);
            buttons.add().growX();
        }
    }

    void addBack() {
        buttons.button("@back", Icon.left, this::hide).size(200f, 54f).pad(2).bottom();
    }
}