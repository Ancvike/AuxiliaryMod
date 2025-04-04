package auxiliary.functions;

import arc.Events;
import arc.graphics.Color;
import arc.input.KeyCode;
import arc.math.geom.Vec2;
import arc.scene.event.InputEvent;
import arc.scene.event.InputListener;
import arc.scene.ui.*;
import arc.scene.ui.layout.Stack;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Align;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.graphics.Pal;

import static auxiliary.binding.KeyBind.isOpen;
import static auxiliary.functions.Menu.isDragged;
import static mindustry.Vars.mobile;

public class Menu {
    Table table = new Table();
    ImageButton button = new ImageButton(Icon.menu);
    static Dialog dialog = new Dialog("功能面板") {{
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

        table.add("aaaaaa").color(Color.gray).colspan(4).pad(10).padBottom(4).row();
        table.image().color(Color.gray).fillX().height(3).pad(6).colspan(4).padTop(0).padBottom(10).row();

        table.add(new SpeedChange().getName(), Color.white).left().padRight(40).padLeft(8);
//        table.label(() -> "1111").color(Pal.accent).left().minWidth(90).padRight(20);
//
//        table.button("绑定", Styles.defaultt, () -> {
//        }).width(130f);

//        table.button("重置", Styles.defaultt, () -> {
//
//        }).width(130f).pad(2f).padLeft(4f);
        table.add(new SpeedChange().function()).width(200f).pad(2f).padLeft(4f);
        table.row();

        table.add(new SunLight().function()).width(200f).pad(2f).padLeft(4f);
        table.row();

        table.add("bbbbbb").color(Color.gray).colspan(4).pad(10).padBottom(4).row();
        table.image().color(Color.gray).fillX().height(3).pad(6).colspan(4).padTop(0).padBottom(10).row();

        stack.add(table);

        cont.row();
        cont.add(pane).growX().colspan(2);
    }};

    public static boolean isDragged = false;
    Seq<Function> functions = new Seq<>();

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
        androidButton.clicked(() -> isOpen = !isOpen);
        return androidButton;
    }

    public void setDialog(Dialog dialog) {
        functions.addAll(new SpeedChange(), new SunLight(), new WarfareFog(), new FullResource(), new BuildingRestoration(), new UnitsRestoration(), new DerelictRemove(), new Launch());

    }
}

class DragListener extends InputListener {
    protected float lastX, lastY;
    final Table table;

    public DragListener(Table table) {
        this.table = table;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, KeyCode button) {
        Vec2 v = event.listenerActor.localToStageCoordinates(Tmp.v1.set(x, y));
        lastX = v.x;
        lastY = v.y;
        table.toFront();
        isDragged = false;
        return true;
    }

    @Override
    public void touchDragged(InputEvent event, float dx, float dy, int pointer) {
        Vec2 v = event.listenerActor.localToStageCoordinates(Tmp.v1.set(dx, dy));
        table.setPosition(table.x + (v.x - lastX), table.y + (v.y - lastY));
        lastX = v.x;
        lastY = v.y;
        isDragged = true;
    }
}