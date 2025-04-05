package auxiliary.functions;

import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Table;
import mindustry.game.Team;
import mindustry.gen.Tex;
import mindustry.ui.Styles;

import static mindustry.Vars.player;

public class TeamChange extends Function {
    public TeamChange() {
        super(3, "改变队伍");
    }

    @Override
    public Table function() {
        return new Table(t -> {
            int i = 0;
            for (Team team : Team.baseTeams) {
                ImageButton button = new ImageButton(Tex.whiteui, Styles.clearNoneTogglei);
                button.margin(4f);
                button.getImageCell().grow();
                button.getStyle().imageUpColor = team.color;
                button.clicked(() -> {
                    player.team(team);
                });
                button.update(() -> {
                    button.setChecked(player.team() == team);
                });
                t.add(button).size(40f);

                if (i++ % 3 == 2) t.row();
            }
        });
    }
}
