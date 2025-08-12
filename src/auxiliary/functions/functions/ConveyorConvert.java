package auxiliary.functions.functions;

import arc.Core;
import arc.input.KeyCode;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import auxiliary.functions.Function;
import auxiliary.functions.Menu;
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

import static mindustry.Vars.ui;
import static mindustry.Vars.world;
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

        @Override
        public void closeOnBack(Runnable callback) {
            this.keyDown((key) -> {
                if (key == KeyCode.escape || key == KeyCode.back) {
                    Core.app.post(this::hide);
                    callback.run();
                    resetData();
                }
            });
        }
    };

    private static final ImageButton[] S_imageButton = new ImageButton[4];
    private static final ImageButton[] S_imageButton_ = new ImageButton[4];
    private static final ImageButton[] E_imageButton = new ImageButton[2];
    private static final ImageButton[] E_imageButton_ = new ImageButton[2];
    private static final ImageButton[] all_imageButton = new ImageButton[6];
    private static final ImageButton[] all_imageButton_ = new ImageButton[6];

    private static final Block[] S_conveyor = {conveyor, Blocks.titaniumConveyor, Blocks.plastaniumConveyor, Blocks.armoredConveyor};

    private static final Block[] E_conveyor = {Blocks.duct, Blocks.armoredDuct};

    Block block_select;
    Block block_convert;

    public ConveyorConvert() {
        super(0, new Table(table -> table.add("传送带替换")));

        loadingData(S_imageButton, S_conveyor, true);
        loadingData(S_imageButton_, S_conveyor, false);
        loadingData(E_imageButton, E_conveyor, true);
        loadingData(E_imageButton_, E_conveyor, false);
        setAll_imageButton();
        dialog.addCloseButton();
    }

    @Override
    public Table function() {
        return new Table(t -> t.button("使用", () -> {
            buildDialog();
            dialog.show();
        }).width(200f));
    }

    private void buildDialog() {
        if (Vars.state.rules.sector == null) {
            dialog.cont.table(t -> {
                t.table(leftTable -> {
                    leftTable.defaults().size(64f, 64f).pad(4f);
                    for (ImageButton imageButton : all_imageButton) {
                        leftTable.add(imageButton);
                        leftTable.row();
                    }
                }).padRight(8f);

                t.table(arrowTable -> arrowTable.image(Icon.right).size(48f, 48f)).pad(4f);

                t.table(tt -> {
                    tt.defaults().size(64f, 64f).pad(4f);
                    for (ImageButton imageButton : all_imageButton_) {
                        tt.add(imageButton);
                        tt.row();
                    }
                }).padLeft(8f);
            }).fillX().pad(12f);
        } else if (Vars.state.rules.sector.planet == Planets.serpulo) {
            dialog.cont.table(t -> {
                t.table(leftTable -> {
                    leftTable.defaults().size(64f, 64f).pad(4f);
                    for (ImageButton imageButton : S_imageButton) {
                        leftTable.add(imageButton);
                        leftTable.row();
                    }
                }).padRight(8f);

                t.table(arrowTable -> arrowTable.image(Icon.right).size(48f, 48f)).pad(4f);

                t.table(tt -> {
                    tt.defaults().size(64f, 64f).pad(4f);
                    for (ImageButton imageButton : S_imageButton_) {
                        tt.add(imageButton);
                        tt.row();
                    }
                }).padLeft(8f);
            }).fillX().pad(12f);
        } else if (Vars.state.rules.sector.planet == Planets.erekir) {
            dialog.cont.table(t -> {
                t.table(leftTable -> {
                    leftTable.defaults().size(64f, 64f).pad(4f);
                    for (ImageButton imageButton : E_imageButton) {
                        leftTable.add(imageButton);
                        leftTable.row();
                    }
                }).padRight(8f);

                t.table(arrowTable -> arrowTable.image(Icon.right).size(48f, 48f)).pad(4f);

                t.table(tt -> {
                    tt.defaults().size(64f, 64f).pad(4f);
                    for (ImageButton imageButton : E_imageButton_) {
                        tt.add(imageButton);
                        tt.row();
                    }
                }).padLeft(8f);
            }).fillX().pad(12f);
        }

        dialog.cont.row();

        dialog.cont.label(() -> "").update(r -> {
            if (block_select != null) {
                int sum = 0;
                for (Building building : Vars.player.team().data().buildings) {
                    if (building.block == block_select) {
                        sum++;
                    }
                }
                r.setText("当前传送带: " + block_select.localizedName + ", 数量: " + sum);
            } else {
                r.setText("未选择传送带...");
            }
        }).pad(8f).get().setFontScale(1.1f);

        dialog.cont.row();

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
                Vars.ui.hudfrag.showToast("已完成替换");
                dialog.hide();
                Menu.dialog.hide();
                resetData();
            } else {
                ui.hudfrag.showToast(Icon.cancel, "未选择初始传送带或目标传送带");
            }
        }).size(140f, 60f).pad(8f);
    }

    private void loadingData(ImageButton[] imageButton, Block[] block, boolean conv) {
        for (int i = 0; i < block.length; i++) {
            imageButton[i] = new ImageButton(Tex.whiteui, Styles.clearNoneTogglei);
            imageButton[i].image(block[i].uiIcon);
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

    private void setAll_imageButton() {
        for (int i = 0; i < S_conveyor.length; i++) {
            all_imageButton[i] = new ImageButton(Tex.whiteui, Styles.clearNoneTogglei);
            all_imageButton[i].image(S_conveyor[i].uiIcon);
            int finalI = i;
            all_imageButton[i].clicked(() -> block_select = S_conveyor[finalI]);
            all_imageButton[i].update(() -> all_imageButton[finalI].setChecked(block_select == S_conveyor[finalI]));
        }
        for (int i = S_conveyor.length; i < S_conveyor.length + E_conveyor.length; i++) {
            all_imageButton[i] = new ImageButton(Tex.whiteui, Styles.clearNoneTogglei);
            all_imageButton[i].image(E_conveyor[i - S_conveyor.length].uiIcon);
            int finalI = i - S_conveyor.length;
            int finalI1 = i;
            all_imageButton[i].clicked(() -> block_select = E_conveyor[finalI]);
            all_imageButton[i].update(() -> all_imageButton[finalI1].setChecked(block_select == E_conveyor[finalI]));
        }

        for (int i = 0; i < S_conveyor.length; i++) {
            all_imageButton_[i] = new ImageButton(Tex.whiteui, Styles.clearNoneTogglei);
            all_imageButton_[i].image(S_conveyor[i].uiIcon);
            int finalI = i;
            all_imageButton_[i].clicked(() -> block_convert = S_conveyor[finalI]);
            all_imageButton_[i].update(() -> all_imageButton_[finalI].setChecked(block_convert == S_conveyor[finalI]));
        }
        for (int i = S_conveyor.length; i < S_conveyor.length + E_conveyor.length; i++) {
            all_imageButton_[i] = new ImageButton(Tex.whiteui, Styles.clearNoneTogglei);
            all_imageButton_[i].image(E_conveyor[i - S_conveyor.length].uiIcon);
            int finalI = i - S_conveyor.length;
            int finalI1 = i;
            all_imageButton_[i].clicked(() -> block_convert = E_conveyor[finalI]);
            all_imageButton_[i].update(() -> all_imageButton_[finalI1].setChecked(block_convert == E_conveyor[finalI]));
        }
    }

    private void resetData() {
        dialog.cont.clear();
        block_select = null;
        block_convert = null;
    }
}