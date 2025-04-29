package auxiliary.functions;

import arc.Events;
import arc.scene.ui.ScrollPane;
import mindustry.game.EventType;

public class CheckEnemy extends Function {
    ScrollPane wavePane;

    public CheckEnemy() {
        super(0, "查看波次敌人");

        Events.on(EventType.WorldLoadEvent.class, e -> {
            wavePane.clearChildren();
            //重建面板
        });

        Events.on(EventType.WaveEvent.class, e -> {
            wavePane.clearChildren();
            //重建面板
        });
    }
}
