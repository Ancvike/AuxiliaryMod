package auxiliary.functions;

import arc.Events;
import arc.graphics.Color;
import arc.input.KeyCode;
import arc.scene.ui.*;
import arc.scene.ui.layout.Stack;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Align;
import auxiliary.functions.dragFunction.DragListener;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.graphics.Pal;

import static auxiliary.binding.HPChange_KeyBind.isOpen;
import static auxiliary.binding.HPChange_KeyBind.buildingsShown;
import static mindustry.Vars.*;

public class Menu {
    Table table = new Table();
    ImageButton button = new ImageButton(Icon.menu);
    static Seq<Function> functions = new Seq<>();
    private static final String[] tips = {"通用", "已占领或沙盒模式", "仅战役模式", "仅沙盒模式"};
    public static Dialog dialog = new Dialog("功能面板") {{
        functions.addAll(new checkFunction(), new TeamChange(), new Invincibility(), new SpeedChange(), new WarfareFog(), new FullResource(), new BuildingRestoration(), new UnitsRestoration(), new DerelictRemove(), new Launch(), new NoLimitZoom(), new NoLimitBlueprint(), new CheckEnemy());

        setFillParent(true);
        title.setAlignment(Align.center);
        titleTable.row();
        titleTable.add(new Image()).growX().height(3f).pad(4f).get().setColor(Pal.accent);

        buttons.button("返回", Icon.left, this::hide).size(210f, 64f);

        keyDown(key -> {
            if (key == KeyCode.escape || key == KeyCode.back) hide();
        });

        cont.clear();

        Stack stack = new Stack();
        ScrollPane pane = new ScrollPane(stack);
        pane.setFadeScrollBars(false);

        Table table = new Table();

        for (int i = 0; i < tips.length; i++) {
            table.add(tips[i]).color(Color.gray).colspan(4).pad(10).padBottom(4).row();
            table.image().color(Color.gray).fillX().height(3).pad(6).colspan(4).padTop(0).padBottom(10).row();

            for (Function function : functions) {
                if (function.getClassID() == i) {
                    table.add(function.getName()).left().padRight(40).padLeft(8);
                    table.add(function.function()).width(200f).pad(2f).padLeft(4f);
                    table.row();
                }
            }
        }

        stack.add(table);

        cont.row();
        cont.add(pane).growX().colspan(2);
    }};

    public static boolean isDragged = false;

    public Menu() {
        Events.run(EventType.Trigger.update, () -> table.visible = !(!Vars.ui.hudfrag.shown || Vars.ui.minimapfrag.shown()));

        table.add(button).row();
        if (mobile) {
            ImageButton androidButton = getImageButton();
            table.add(androidButton);
        }

        button.clicked(() -> {
            if (!isDragged) dialog.show();
        });

        Vars.ui.hudGroup.fill(t -> {
            t.name = "auxiliary-functions";
            t.add(table).row();
            t.right();
        });
        table.addListener(new DragListener(table));
    }

    private static ImageButton getImageButton() {
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle() {
            {
                this.checked = Tex.buttonDown;
                this.down = Tex.buttonDown;
                this.up = Tex.button;
                this.over = Tex.buttonOver;
                this.disabled = Tex.buttonDisabled;
            }
        };
        ImageButton androidButton = new ImageButton(Icon.android, style);
        androidButton.clicked(() -> {
            isOpen = !isOpen;
            buildingsShown = false;
        });
        return androidButton;
    }
}