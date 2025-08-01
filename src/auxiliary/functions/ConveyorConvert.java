package auxiliary.functions;

import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.content.Planets;
import mindustry.game.Gamemode;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.Block;

import static mindustry.Vars.ui;
import static mindustry.content.Blocks.*;

public class ConveyorConvert extends Function {
    BaseDialog dialog = new BaseDialog("传送带替换") {
        @Override
        public void addCloseButton() {
            this.buttons.defaults().size(210, 64.0F);
            this.buttons.button("@back", Icon.left, this::hide).size(210, 64.0F);
            this.addCloseListener();
            this.cont.clear();
            S_Conveyor.clear();
            E_Conveyor.clear();
            block_select = null;
            block_convert = null;
        }
    };
    Seq<MyConveyor> S_Conveyor = new Seq<>();
    Seq<MyConveyor> E_Conveyor = new Seq<>();

    ImageButton button_conveyor = new ImageButton(Tex.whiteui, Styles.clearNoneTogglei);
    ImageButton button_titaniumConveyor = new ImageButton(Tex.whiteui, Styles.clearNoneTogglei);
    ImageButton button_plastaniumConveyor = new ImageButton(Tex.whiteui, Styles.clearNoneTogglei);
    ImageButton button_armoredConveyor = new ImageButton(Tex.whiteui, Styles.clearNoneTogglei);

    ImageButton button_duct = new ImageButton(Tex.whiteui, Styles.clearNoneTogglei);
    ImageButton button_armoredDuct = new ImageButton(Tex.whiteui, Styles.clearNoneTogglei);

    ImageButton button_conveyor_ = new ImageButton(Tex.whiteui, Styles.clearNoneTogglei);
    ImageButton button_titaniumConveyor_ = new ImageButton(Tex.whiteui, Styles.clearNoneTogglei);
    ImageButton button_plastaniumConveyor_ = new ImageButton(Tex.whiteui, Styles.clearNoneTogglei);
    ImageButton button_armoredConveyor_ = new ImageButton(Tex.whiteui, Styles.clearNoneTogglei);

    ImageButton button_duct_ = new ImageButton(Tex.whiteui, Styles.clearNoneTogglei);
    ImageButton button_armoredDuct_ = new ImageButton(Tex.whiteui, Styles.clearNoneTogglei);

    Block block_select;
    Block block_convert;

    public ConveyorConvert() {
        super(1, new Table(table -> table.add("传送带替换")));

        button_conveyor.image(ui.getIcon(conveyor.name));
        button_conveyor.clicked(() -> block_select = conveyor);
        button_titaniumConveyor.image(ui.getIcon(titaniumConveyor.name));
        button_titaniumConveyor.clicked(() -> block_select = titaniumConveyor);
        button_plastaniumConveyor.image(ui.getIcon(plastaniumConveyor.name));
        button_plastaniumConveyor.clicked(() -> block_select = plastaniumConveyor);
        button_armoredConveyor.image(ui.getIcon(armoredConveyor.name));
        button_armoredConveyor.clicked(() -> block_select = armoredConveyor);

        button_duct.image(ui.getIcon(duct.name));
        button_duct.clicked(() -> block_select = duct);
        button_armoredDuct.image(ui.getIcon(armoredDuct.name));
        button_armoredDuct.clicked(() -> block_select = armoredDuct);

        button_conveyor_.image(ui.getIcon(conveyor.name));
        button_conveyor_.clicked(() -> block_convert = conveyor);
        button_titaniumConveyor_.image(ui.getIcon(titaniumConveyor.name));
        button_titaniumConveyor_.clicked(() -> block_convert = titaniumConveyor);
        button_plastaniumConveyor_.image(ui.getIcon(plastaniumConveyor.name));
        button_plastaniumConveyor_.clicked(() -> block_convert = plastaniumConveyor);
        button_armoredConveyor_.image(ui.getIcon(armoredConveyor.name));
        button_armoredConveyor_.clicked(() -> block_convert = armoredConveyor);

        button_duct_.image(ui.getIcon(duct.name));
        button_duct_.clicked(() -> block_convert = duct);
        button_armoredDuct_.image(ui.getIcon(armoredDuct.name));
        button_armoredDuct_.clicked(() -> block_convert = armoredDuct);

        button_conveyor.update(() -> button_conveyor.setChecked(block_select == conveyor));
        button_titaniumConveyor.update(() -> button_titaniumConveyor.setChecked(block_select == titaniumConveyor));
        button_plastaniumConveyor.update(() -> button_plastaniumConveyor.setChecked(block_select == plastaniumConveyor));
        button_armoredConveyor.update(() -> button_armoredConveyor.setChecked(block_select == armoredConveyor));

        button_duct.update(() -> button_duct.setChecked(block_select == duct));
        button_armoredDuct.update(() -> button_armoredDuct.setChecked(block_select == armoredDuct));

        button_conveyor_.update(() -> button_conveyor_.setChecked(block_convert == conveyor));
        button_titaniumConveyor_.update(() -> button_titaniumConveyor_.setChecked(block_convert == titaniumConveyor));
        button_plastaniumConveyor_.update(() -> button_plastaniumConveyor_.setChecked(block_convert == plastaniumConveyor));
        button_armoredConveyor_.update(() -> button_armoredConveyor_.setChecked(block_convert == armoredConveyor));

        button_duct_.update(() -> button_conveyor.setChecked(block_convert == duct));
        button_armoredDuct_.update(() -> button_conveyor.setChecked(block_convert == armoredDuct));
    }

    @Override
    public Table function() {
        return new Table(t -> t.button("使用", () -> {
            if ((Vars.state.rules.sector != null && Vars.state.rules.sector.isCaptured()) || Vars.state.rules.mode() == Gamemode.sandbox || Vars.state.rules.mode() == Gamemode.editor) {
                loadingData();
                buildDialog();
                dialog.show();
            } else {
                Menu.dialog.hide();
                ui.hudfrag.showToast(Icon.cancel, "[scarlet]区块未占领,无法使用该功能");
            }
        }).width(200f));
    }

    private void loadingData() {
        if (Vars.state.rules.sector.planet == Planets.serpulo) {
            for (Building building : Vars.player.team().data().buildings) {
                if (building.block == conveyor ||
                        building.block == titaniumConveyor ||
                        building.block == plastaniumConveyor ||
                        building.block == armoredConveyor) {
                    S_Conveyor.addAll(new MyConveyor(building.block, building.tileX(), building.tileY(), building.rotation));
                }
            }
        } else if (Vars.state.rules.sector.planet == Planets.erekir) {
            for (Building building : Vars.player.team().data().buildings) {
                if (building.block == duct ||
                        building.block == armoredDuct) {
                    E_Conveyor.addAll(new MyConveyor(building.block, building.tileX(), building.tileY(), building.rotation));
                }
            }
        }
    }

    private void buildDialog() {
        if (Vars.state.rules.sector.planet == Planets.serpulo) {
            dialog.cont.table(t -> {
                t.add(button_conveyor);
                t.row();
                t.add(button_titaniumConveyor);
                t.row();
                t.add(button_plastaniumConveyor);
                t.row();
                t.add(button_armoredConveyor);
            }).fill();

            dialog.cont.table(t -> t.image(Icon.right));

            dialog.cont.table(t -> {
                t.add(button_conveyor_);
                t.row();
                t.add(button_titaniumConveyor_);
                t.row();
                t.add(button_plastaniumConveyor_);
                t.row();
                t.add(button_armoredConveyor_);
            }).fill();
        } else if (Vars.state.rules.sector.planet == Planets.erekir) {
            dialog.cont.table(t -> {
                t.add(button_duct);
                t.row();
                t.add(button_armoredDuct);

            }).fill();

            dialog.cont.table(t -> t.image(Icon.right));

            dialog.cont.table(t -> {
                t.add(button_duct_);
                t.row();
                t.add(button_armoredDuct_);
            }).fill();
        }
    }
}

class MyConveyor {
    Block block;
    int x;
    int y;
    int rotation;

    public MyConveyor(Block block, int rotation, int x, int y) {
        this.block = block;
        this.rotation = rotation;
        this.x = x;
        this.y = y;
    }
}