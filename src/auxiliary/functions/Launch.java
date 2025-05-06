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
        super(2, new Table(table -> table.add("从此区块发射")));
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
}