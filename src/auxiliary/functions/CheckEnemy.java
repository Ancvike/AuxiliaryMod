package auxiliary.functions;

import arc.Events;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.Tooltip;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectIntMap;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.content.StatusEffects;
import mindustry.game.EventType;
import mindustry.game.SpawnGroup;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.ui.dialogs.BaseDialog;

import static mindustry.Vars.state;

public class CheckEnemy extends Function {
    private final BaseDialog dialog = new BaseDialog("敌人波次");
    ScrollPane waveEnemy = new ScrollPane(build());
    int wave = 0;

    public CheckEnemy() {
        super(0, new Table(table -> {
            table.add("查看敌人波次");
            table.addListener(new Tooltip(t -> t.add("查看敌人波次信息,若非战役模式则只显示当前波次及后20波次信息")));
        }));

        dialog.cont.add(waveEnemy).grow();
        dialog.addCloseButton();

        Events.on(EventType.WorldLoadEvent.class, e -> {
            wave = 0;
            waveEnemy.clearChildren();
            if (state.rules.winWave == 0) wave = state.wave + 20;
            else wave = state.rules.winWave;
            waveEnemy.setWidget(build());
        });

        Events.on(EventType.WaveEvent.class, e -> {
            wave = 0;
            waveEnemy.clearChildren();
            if (state.rules.winWave == 0) wave = state.wave + 20;
            else wave = state.rules.winWave;
            waveEnemy.setWidget(build());
        });
    }

    @Override
    public Table function() {
        return new Table(t -> t.button("查看", dialog::show).width(200f));
    }

    public Table build() {
        return new Table(t -> {
            t.center().defaults().growX();

            for (int i = 1; i <= wave; i++) {
                final int index = i;

                t.table(waveRow -> {
                    waveRow.left();

                    waveRow.add(String.valueOf(index)).update(label -> label.setColor(Pal.accent));
                    waveRow.table(unitTable -> {
                        unitTable.center();

                        ObjectIntMap<SpawnGroup> groups = getWaveGroup(index - 1);

                        int row = 0;
                        int max = 16;
                        for (SpawnGroup group : groups.keys()) {
                            state.rules.waveTeam.cores();
                            int amount = groups.get(group);
                            unitTable.stack(new Table(ttt -> {
                                ttt.center();
                                ttt.image(group.type.shadowRegion).size(64);
                                ttt.pack();
                            }), new Table(ttt -> {
                                ttt.bottom().left();
                                ttt.add(amount + "").padTop(2f).fontScale(0.9f);
                                ttt.add("[stat]" + group.type.localizedName + "[]").padTop(10f).fontScale(1f);
                                ttt.pack();
                            }), new Table(ttt -> {
                                ttt.top().right();
                                ttt.image(Icon.warning.getRegion()).update(img -> img.setColor(Tmp.c2.set(Color.orange).lerp(Color.scarlet, Mathf.absin(Time.time, 2f, 1f)))).size(12f);
                                ttt.visible(() -> group.effect == StatusEffects.boss);
                                ttt.pack();
                            })).pad(2f).get();
                            if (row++ % max == max - 1) {
                                unitTable.row();
                            }
                        }
                    }).growX().margin(12f);
                });
                t.row();

                t.image().height(4f).color(Pal.gray);
                t.row();
            }
        });
    }

    private ObjectIntMap<SpawnGroup> getWaveGroup(int index) {
        ObjectIntMap<SpawnGroup> groups = new ObjectIntMap<>();
        for (SpawnGroup group : state.rules.spawns) {
            if (group.getSpawned(index) <= 0) continue;
            SpawnGroup sameTypeKey = groups.keys().toArray().find(g -> g.type == group.type && g.effect != StatusEffects.boss);
            if (sameTypeKey != null) groups.increment(sameTypeKey, sameTypeKey.getSpawned(index));
            else groups.put(group, group.getSpawned(index));
        }
        Seq<SpawnGroup> groupSorted = groups.keys().toArray().copy().sort((g1, g2) -> {
            int boss = Boolean.compare(g1.effect != StatusEffects.boss, g2.effect != StatusEffects.boss);
            if (boss != 0) return boss;
            int hitSize = Float.compare(-g1.type.hitSize, -g2.type.hitSize);
            if (hitSize != 0) return hitSize;
            return Integer.compare(-g1.type.id, -g2.type.id);
        });
        ObjectIntMap<SpawnGroup> groupsTmp = new ObjectIntMap<>();
        groupSorted.each(g -> groupsTmp.put(g, groups.get(g)));

        return groupsTmp;
    }
}
