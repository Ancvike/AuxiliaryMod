package auxiliary.functions.window;

import arc.scene.event.Touchable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Label;
import arc.scene.ui.Slider;
import arc.scene.ui.layout.Table;
import arc.util.Scaling;
import auxiliary.functions.dragFunction.DragListener;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.ui.Styles;

import static auxiliary.binding.ChangeHP_KeyBind.buildings;

public class MapEditorWindow extends Table {
    public final TextureRegionDrawable icon;
    public boolean shown = false;
    public float minWindowWidth = 160;

    public MapEditorWindow(TextureRegionDrawable icon) {
        this.icon = icon;
        height = 300;
        width = 300;
    }

    public void buildBody(Table table) {
        table.top().left();

        table.table(rules -> {
            rules.top().left();

            Label label = rules.add("100%").get();
            Slider slider = new Slider(0, 10, 1, false);
            slider.setValue(10f);
            slider.changed(() -> label.setText((int) (slider.getValue() * 10) + "%"));
            slider.change();
            slider.moved(hp -> {
                for (Building building : buildings) {
                    building.health = building.maxHealth * (int) hp * 0.1f;
                }
            });
            rules.add(slider);
        }).grow();
    }

    public void build() {
        table(t -> {
            t.table(Tex.buttonEdge1, b -> {
                b.left();
                b.image(icon.getRegion()).scaling(Scaling.fill).size(20f);
                b.add("血量修改").padLeft(20);
            }).grow();

            t.table(Tex.buttonEdge3, b -> b.button(Icon.cancel, Styles.emptyi, () -> shown = false).grow()).maxWidth(8 * 15f).growY();

            t.touchable = Touchable.enabled;
            t.addListener(new DragListener(this));
        }).height(8 * 6f).growX().prefWidth();

        row();
        table(Styles.black5, this::buildBody).grow();

        visible(() -> shown);
    }

    public void toggle() {
        shown = !shown;
        if (shown) toFront();
    }
}