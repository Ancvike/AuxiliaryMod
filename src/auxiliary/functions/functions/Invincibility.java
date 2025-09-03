package auxiliary.functions.functions;

import arc.Events;
import arc.scene.ui.CheckBox;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Time;
import auxiliary.functions.Function;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Groups;
import mindustry.gen.Icon;
import mindustry.gen.Unit;
import mindustry.ui.dialogs.LoadDialog;
import mindustry.ui.dialogs.PausedDialog;
import mindustry.ui.dialogs.SaveDialog;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static auxiliary.functions.Menu.invincibility;
import static mindustry.Vars.*;

public class Invincibility extends Function {
    static boolean isInvincible = false;

    static Seq<Unit> invincibleUnits = new Seq<>();
    static Unit unitPlayer = null;

    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public Invincibility() {
        super(0, new Table(table -> table.add("当前单位和所控制单位无敌").tooltip("[red]建议关闭后保存, 否则会导致单位血量==999999")));

        ui.paused = new MyPausedDialog();

        Events.on(EventType.WorldLoadEvent.class, e -> Time.runTask(10f, () -> {
            for (Unit unit : Groups.unit) {
                if (unit.health > unit.maxHealth) {
                    unit.health = unit.maxHealth;
                }
            }
        }));

        Events.run(EventType.Trigger.update, () -> {
            if (isInvincible) {
                if (invincibleUnits.size > 0 && unitPlayer != null) {
                    for (Unit unit : invincibleUnits) {
                        unit.health = unit.maxHealth;
                    }
                    unitPlayer.health = unitPlayer.maxHealth;
                    invincibleUnits.clear();
                    unitPlayer = null;
                }

                for (Unit unit : Vars.control.input.selectedUnits) {
                    invincibleUnits.add(unit);
                    unit.health = 999999;
                }
                unitPlayer = player.unit();
                player.unit().health = 999999;
            }
        });
    }

    @Override
    public Table function() {
        return new Table(t -> {
            CheckBox box = new CheckBox("");
            box.update(() -> box.setChecked(isInvincible));
            box.changed(() -> {
                isInvincible = !isInvincible;
                if (!isInvincible) {
                    if (invincibleUnits.size > 0 && unitPlayer != null) {
                        for (Unit unit : invincibleUnits) {
                            unit.health = unit.maxHealth;
                        }
                        unitPlayer.health = unitPlayer.maxHealth;
                        invincibleUnits.clear();
                        unitPlayer = null;
                    }

                    for (Unit unit : Vars.control.input.selectedUnits) {
                        unit.health = unit.maxHealth;
                    }
                    player.unit().health = player.unit().maxHealth;
                }
            });
            t.add(box);
        });
    }

    void myQuit() {
        isInvincible = false;
        if (invincibleUnits.size > 0 && unitPlayer != null) {
            for (Unit unit : invincibleUnits) {
                unit.health = unit.maxHealth;
            }
            unitPlayer.health = unitPlayer.maxHealth;
            invincibleUnits.clear();
            unitPlayer = null;
        }

        for (Unit unit : Vars.control.input.selectedUnits) {
            unit.health = unit.maxHealth;
        }
        player.unit().health = player.unit().maxHealth;

        scheduler.shutdown();
    }
}

class MyPausedDialog extends PausedDialog {
    SaveDialog save = new SaveDialog();
    LoadDialog load = new LoadDialog();

    public MyPausedDialog() {
        super();
        shouldPause = true;

        shown(this::rebuild);

        addCloseListener();
    }

    void rebuild() {
        cont.clear();

        update(() -> {
            if (state.isMenu() && isShown()) {
                hide();
            }
        });

        if (!mobile) {
            float dw = 220f;
            cont.defaults().width(dw).height(55).pad(5f);

            cont.button("@objective", Icon.info, () -> ui.fullText.show("@objective", state.rules.sector.preset.description))
                    .visible(() -> state.rules.sector != null && state.rules.sector.preset != null && state.rules.sector.preset.description != null).padTop(-60f);

            cont.button("@abandon", Icon.cancel, () -> ui.planet.abandonSectorConfirm(state.rules.sector, this::hide)).padTop(-60f)
                    .disabled(b -> net.client()).visible(() -> state.rules.sector != null).row();

            cont.button("@back", Icon.left, this::hide).name("back");
            cont.button("@settings", Icon.settings, ui.settings::show).name("settings");

            if (!state.isCampaign() && !state.isEditor()) {
                cont.row();
                cont.button("@savegame", Icon.save, save::show);
                cont.button("@loadgame", Icon.upload, load::show).disabled(b -> net.active());
            }

            cont.row();

            cont.button("@hostserver", Icon.host, () -> {
                if (net.server() && steam) {
                    platform.inviteFriends();
                } else {
                    ui.host.show();
                }
            }).disabled(b -> !((steam && net.server()) || !net.active())).colspan(2).width(dw * 2 + 10f).update(e -> e.setText(net.server() && steam ? "@invitefriends" : "@hostserver"));

            cont.row();

            cont.button("@quit", Icon.exit, this::showQuitConfirm).colspan(2).width(dw + 10f).update(s -> s.setText(control.saves.getCurrent() != null && control.saves.getCurrent().isAutosave() ? "@save.quit" : "@quit"));

        } else {
            cont.defaults().size(130f).pad(5);
            cont.buttonRow("@back", Icon.play, this::hide);
            cont.buttonRow("@settings", Icon.settings, ui.settings::show);

            if (!state.isCampaign() && !state.isEditor()) {
                cont.buttonRow("@save", Icon.save, save::show);

                cont.row();

                cont.buttonRow("@load", Icon.download, load::show).disabled(b -> net.active());
            } else if (state.isCampaign()) {
                cont.buttonRow("@research", Icon.tree, ui.research::show);

                cont.row();

                cont.buttonRow("@planetmap", Icon.map, () -> {
                    hide();
                    ui.planet.show();
                });
            } else {
                cont.row();
            }

            cont.buttonRow("@hostserver.mobile", Icon.host, ui.host::show).disabled(b -> net.active());

            cont.buttonRow("@quit", Icon.exit, this::showQuitConfirm).update(s -> {
                s.setText(control.saves.getCurrent() != null && control.saves.getCurrent().isAutosave() ? "@save.quit" : "@quit");
                s.getLabelCell().growX().wrap();
            });
        }
    }

    void showQuitConfirm() {
        Runnable quit = () -> {
            //修改处
            invincibility.myQuit();
            runExitSave();
            hide();
        };

        if (confirmExit) {
            ui.showConfirm("@confirm", "@quit.confirm", quit);
        } else {
            quit.run();
        }
    }
}