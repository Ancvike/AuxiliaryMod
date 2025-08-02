package auxiliary.functions;

import arc.scene.ui.ImageButton;
import arc.scene.ui.Label;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Planets;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.Block;
import mindustry.world.Tile;

import static mindustry.Vars.*;
import static mindustry.content.Blocks.conveyor;

public class ConveyorConvert extends Function {
    BaseDialog dialog = new BaseDialog("传送带替换") {
        @Override
        public void addCloseButton() {
            this.buttons.defaults().size(210, 64.0F);
            this.buttons.button("@back", Icon.left, () -> {
                this.hide();
                resetData();
            }).size(210, 64.0F);
            this.addCloseListener();
        }
    };

    private static final ImageButton[] S_imageButton = new ImageButton[4];
    private static final ImageButton[] S_imageButton_ = new ImageButton[4];
    private static final ImageButton[] E_imageButton = new ImageButton[2];
    private static final ImageButton[] E_imageButton_ = new ImageButton[2];

    private static final Block[] S_conveyor = {
            conveyor,
            Blocks.titaniumConveyor,
            Blocks.plastaniumConveyor,
            Blocks.armoredConveyor
    };

    private static final Block[] E_conveyor = {
            Blocks.duct,
            Blocks.armoredDuct
    };

    Block block_select;
    Block block_convert;

    public ConveyorConvert() {
        super(1, new Table(table -> table.add("传送带替换")));

        loadingData(S_imageButton, S_conveyor, true);
        loadingData(S_imageButton_, S_conveyor, false);
        loadingData(E_imageButton, E_conveyor, true);
        loadingData(E_imageButton_, E_conveyor, false);
        dialog.addCloseButton();
    }

    @Override
    public Table function() {
        return new Table(t -> t.button("使用", () -> {
//            if ((Vars.state.rules.sector != null && Vars.state.rules.sector.isCaptured()) || Vars.state.rules.mode() == Gamemode.sandbox || Vars.state.rules.mode() == Gamemode.editor) {
            buildDialog();
            dialog.show();
//            } else {
//                Menu.dialog.hide();
//                ui.hudfrag.showToast(Icon.cancel, "[scarlet]区块未占领,无法使用该功能");
//            }
        }).width(200f));
    }

    private void buildDialog() {
//        if (Vars.state.rules.sector.planet == Planets.serpulo) {
        dialog.cont.table(t -> {
            for (ImageButton imageButton : S_imageButton) {
                t.add(imageButton);
                t.row();
            }
        }).fill();

        dialog.cont.table(t -> t.image(Icon.right));

        dialog.cont.table(t -> {
            for (ImageButton imageButton : S_imageButton_) {
                t.add(imageButton);
                t.row();
            }
        }).fill();
//        } else if (Vars.state.rules.sector.planet == Planets.erekir) {
//            dialog.cont.table(t -> {
//                for (ImageButton imageButton : E_imageButton) {
//                    t.add(imageButton);
//                    t.row();
//                }
//            }).fill();
//
//            dialog.cont.table(t -> t.image(Icon.right));
//
//            dialog.cont.table(t -> {
//                for (ImageButton imageButton : E_imageButton_) {
//                    t.add(imageButton);
//                    t.row();
//                }
//            }).fill();
//        }
        dialog.cont.row();

        Label label = new Label("");
        label.update(() -> label.visible = block_select != null);

        dialog.cont.add(label);
        dialog.cont.button("转换", () -> {
            if (block_select != null && block_convert != null) {
                Seq<Building> buildings = new Seq<>();
                for (Building building : Vars.player.team().data().buildings) {
                    if (building.block == block_select) {
                        buildings.addAll(building);
                    }
                }
                for (Building building : buildings) {
                    Tile tile = world.tile(building.tileX(), building.tileY());
                    tile.setBlock(block_convert, Vars.player.team(), building.rotation);
                }
                dialog.hide();
                Menu.dialog.hide();
                resetData();
            } else ui.hudfrag.showToast(Icon.cancel, "未选择初始传送带或目标传送带");
        }).size(120f, 64f);
    }

    private void loadingData(ImageButton[] imageButton, Block[] block, boolean conv) {
        for (int i = 0; i < block.length; i++) {
            imageButton[i] = new ImageButton(Tex.whiteui, Styles.clearNoneTogglei);
            imageButton[i].image(ui.getIcon(block[i].name));
            int finalI = i;
            imageButton[i].clicked(() -> {
                if (conv) block_select = block[finalI];
                else block_convert = block[finalI];
            });
            imageButton[i].update(() -> {
                if (conv) imageButton[finalI].setChecked(block_select == block[finalI]);
                else imageButton[finalI].setChecked(block_convert == block[finalI]);
            });
        }
    }

    private void resetData() {
        dialog.cont.clear();
        block_select = null;
        block_convert = null;
    }
}