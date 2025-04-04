package auxiliary.functions;

import arc.Core;
import arc.scene.ui.layout.Table;
import arc.util.Time;

public class SpeedChange extends Function {
    String speedLabel = "1x";

    public SpeedChange() {
        super(0, 0, "改变游戏速度");
    }

    @Override
    public Table function() {
        float[] speeds = {1f, 2f, 5f, 10f, 20f, 50f, 100f};
        return new Table(t -> {
            t.add(speedLabel).margin(0f).pad(0f).growX();
            t.slider(0, speeds.length - 1, 1, 0, value -> {
                Time.setDeltaProvider(() -> Math.min(Core.graphics.getDeltaTime() * 60.0f * value, 3.0f));
                speedLabel = value + "x";
            }).margin(0f).pad(0f).growX();
        });
    }
}