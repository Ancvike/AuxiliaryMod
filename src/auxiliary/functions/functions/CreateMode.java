package auxiliary.functions.functions;

import arc.Core;
import arc.Events;
import arc.func.Boolf;
import arc.func.Cons;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.input.KeyCode;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.math.geom.Point2;
import arc.math.geom.Vec2;
import arc.scene.Element;
import arc.scene.event.HandCursorListener;
import arc.scene.event.InputEvent;
import arc.scene.event.Touchable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.*;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.layout.Scl;
import arc.scene.ui.layout.Table;
import arc.struct.IntSeq;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Structs;
import arc.util.Time;
import auxiliary.functions.Function;
import auxiliary.functions.dragFunction.DragListener;
import auxiliary.functions.dragFunction.ScaleInputListener;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.editor.MapEditor;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.*;
import mindustry.world.meta.BuildVisibility;

import static arc.Core.scene;
import static mindustry.Vars.world;

public class CreateMode extends Function {
    Table createModeTable = new Table();

    boolean disableRootScroll = false;

    float heat = 0f;
    float lastWidth = 0f, lastHeight = 0f;
    float lastTool = -1;

    public static Block drawBlock = null;
    public static Team drawTeam = null;
    public static float drawTool = -1;

    float blockScroll;

    Seq<Block> groundBlocks = new Seq<>();
    Seq<Block> oreBlocks = new Seq<>();
    Seq<Block> wallBlocks = new Seq<>();
    Seq<Block> decorateBlocks = new Seq<>();
    Seq<Block> serpuloBlocks = new Seq<>();
    Seq<Block> erekirBlocks = new Seq<>();
    Seq<Block> otherBlocks = new Seq<>();

    int rotation = 0;
    float brushSize = 1f;
    int brushIndex;
    final Vec2[][] brushPolygons = new Vec2[MapEditor.brushSizes.length][0];

    boolean canDraw = false;

    int lastX, lastY;

    public CreateMode() {
        super(0, new Table(table -> table.add("自由改造").tooltip("你可以自由地在地图上修改一切")));

        for (int i = 0; i < MapEditor.brushSizes.length; i++) {
            float size = MapEditor.brushSizes[i];
            float mod = size % 1f;
            brushPolygons[i] = Geometry.pixelCircle(size, (index, x, y) -> Mathf.dst(x, y, index - mod, index - mod) <= size - 0.5f);
        }

        Events.run(EventType.Trigger.update, () -> {
            heat += Time.delta;
            if (heat >= 60f) {
                heat = 0f;

                if (lastWidth != createModeTable.getWidth() || lastHeight != createModeTable.getHeight())
                    buildTable();
                lastWidth = createModeTable.getWidth();
                lastHeight = createModeTable.getHeight();
            }
        });

        Events.run(EventType.Trigger.update, () -> {
            if (lastTool != drawTool) buildTable();
            lastTool = drawTool;
        });

        Events.run(EventType.Trigger.draw, () -> {
            float scaling = 8;

            Draw.z(Layer.max);

            Tile tile = world.tileWorld(Core.input.mouseWorldX(), Core.input.mouseWorldY());
            if (tile == null || !canDraw) return;

            int index = 0;
            for (int i = 0; i < MapEditor.brushSizes.length; i++) {
                if (brushSize == MapEditor.brushSizes[i]) {
                    index = i;
                    break;
                }
            }
            Lines.stroke(Scl.scl(1f), Pal.accent);

            if (drawTool >= 0 && drawTool < 1 && drawBlock != null) {
                Lines.poly(brushPolygons[index], tile.x * 8 - 4, tile.y * 8 - 4, scaling);
            } else if (drawTool >= 1 && drawTool < 2 && drawBlock != null) {
                Lines.poly(brushPolygons[0], tile.x * 8 - 4, tile.y * 8 - 4, scaling);
            } else if (drawTool >= 2) {
                Lines.poly(brushPolygons[index], tile.x * 8 - 4, tile.y * 8 - 4, scaling);
            }
        });

        Events.run(EventType.Trigger.update, () -> {
            Tile tile = world.tileWorld(Core.input.mouseWorldX(), Core.input.mouseWorldY());
            if (createModeTable.hasMouse()) return;
            if (tile == null || !canDraw) return;
            if (Core.input.isTouched()) {
                if (!Core.input.keyDown(KeyCode.mouseLeft)) return;
                lastX = tile.x;
                lastY = tile.y;
                if (drawTool >= 0 && drawTool < 1) drawBlocks(lastX, lastY, tile_ -> true);
                else if (drawTool >= 1 && drawTool < 2) {
                    if (!Structs.inBounds(lastX, lastY, world.width(), world.height())) return;
                    Tile tile_ = world.tile(lastX, lastY);
                    if (tile_ == null) return;
                    if (tile.block().isMultiblock()) return;

                    Boolf<Tile> tester;
                    Cons<Tile> setter;
                    Block drawBlock_ = drawBlock;

                    if (drawBlock_.isOverlay()) {
                        Block dest = tile.overlay();
                        if (dest == drawBlock_) return;
                        tester = t -> t.overlay() == dest && (t.floor().hasSurface() || !t.floor().needsSurface);
                        setter = t -> t.setOverlay(drawBlock_);
                    } else if (drawBlock_.isFloor()) {
                        Block dest = tile.floor();
                        if (dest == drawBlock_) return;
                        tester = t -> t.floor() == dest;
                        setter = t -> t.setFloorUnder(drawBlock_.asFloor());
                    } else {
                        Block dest = tile.block();
                        if (dest == drawBlock_) return;
                        tester = t -> t.block() == dest;
                        setter = t -> t.setBlock(drawBlock_, drawTeam);
                    }

                    fill(lastX, lastY, drawTool == 1.5f, tester, setter);
                } else if (drawTool == 2) tile.remove();
                else if (drawTool == 2.5f) tile.clearOverlay();
            } else {
                lastX = -1;
                lastY = -1;
            }
        });

        for (Block block : Vars.content.blocks()) {
            if (!Core.atlas.isFound(block.uiIcon) || !block.inEditor || block.buildVisibility == BuildVisibility.debugOnly)
                continue;

            if (block instanceof Floor && !(block instanceof OreBlock)) {
                groundBlocks.add(block);
            } else if (block instanceof OreBlock) {
                oreBlocks.add(block);
            } else if (block instanceof StaticWall || block instanceof TreeBlock || block instanceof TallBlock) {
                wallBlocks.add(block);
            } else if (block instanceof Prop) {
                decorateBlocks.add(block);
            } else if (Items.erekirOnlyItems.asSet().isEmpty() || !Structs.contains(block.requirements, i -> Items.erekirOnlyItems.asSet().contains(i.item))) {
                serpuloBlocks.add(block);
            } else if (!(Items.erekirOnlyItems.asSet().isEmpty() || !Structs.contains(block.requirements, i -> Items.erekirOnlyItems.asSet().contains(i.item)))) {
                erekirBlocks.add(block);
            } else {
                otherBlocks.add(block);
            }
        }

        buildTable();

        Vars.ui.hudGroup.fill(t -> t.add(createModeTable).visible(false));
    }


    @Override
    public Table function() {
        return new Table(t -> {
            CheckBox box = new CheckBox("");
            box.update(() -> box.setChecked(createModeTable.visible));
            box.changed(() -> {
                drawBlock = null;
                drawTeam = null;
                drawTool = -1;
                canDraw = false;

                createModeTable.parent.setLayoutEnabled(false);
                createModeTable.visible = !createModeTable.visible;
                createModeTable.setLayoutEnabled(true);
            });
            t.add(box);
        });
    }

    void buildTable() {
        createModeTable.clear();
        createModeTable.table(t -> {
            t.name = "top-table";
            t.table(Tex.buttonEdge1, b -> {
                b.left();
                b.add("自由改造");
            }).grow();

            t.table(Tex.buttonEdge3, b -> b.button(Icon.cancel, Styles.emptyi, () -> {
                createModeTable.visible = false;
                drawBlock = null;
                drawTeam = null;
                drawTool = -1;
                canDraw = false;
            }).grow()).maxWidth(8 * 15f).growY();

            t.touchable = Touchable.enabled;
            t.addListener(new DragListener(createModeTable));
        }).growX();

        createModeTable.row();
        createModeTable.table(Styles.black5, body -> {
            body.name = "middle-table";
            body.pane(Styles.noBarPane, new Table(t -> {
                t.left();
                t.top().background(Styles.black8);

                t.pane(Styles.noBarPane, buildPane()).get().setScrollingDisabled(true, false);

            })).scrollX(!disableRootScroll).scrollY(!disableRootScroll).grow();
        }).grow();

        createModeTable.row();
        createModeTable.table(Styles.black5, t -> {
            t.right();
            t.image(Icon.resizeSmall).size(20f).get().addListener(new ScaleInputListener(createModeTable));
        }).height(8 * 2f).growX();

        createModeTable.addListener(new HandCursorListener() {
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Element toActor) {
                super.exit(event, x, y, pointer, toActor);
                scene.setScrollFocus(null);
            }
        });
    }

    Table buildPane() {
        return new Table(t -> {
            t.top();

            t.add(new Table(tt -> {
                Table allBlocksTable = new Table(ttt -> {
                    ttt.add(buildBlockTable(groundBlocks, 0));
                    ttt.row();
                    ttt.add(buildBlockTable(oreBlocks, 1));
                    ttt.row();
                    ttt.add(buildBlockTable(wallBlocks, 2));
                    ttt.row();
                    ttt.add(buildBlockTable(decorateBlocks, 3));
                    ttt.row();
                    ttt.add(buildBlockTable(serpuloBlocks, 4));
                    ttt.row();
                    ttt.add(buildBlockTable(erekirBlocks, 5));

                    if (otherBlocks.size > 0) {
                        ttt.row();
                        ttt.add(buildBlockTable(otherBlocks, 6));
                    }
                });

                ScrollPane pane = new ScrollPane(allBlocksTable, Styles.smallPane);
                pane.setScrollingDisabled(true, false);
                pane.setScrollYForce(blockScroll);
                pane.update(() -> blockScroll = pane.getScrollY());
                pane.setOverscroll(false, false);
                tt.add(pane).maxHeight(Scl.scl(6 * 10 * 5));
            })).marginTop(16f).marginBottom(16f).row();
            t.image().height(4f).color(Pal.accent).growX().row();

            t.add(buildTeamTable()).marginTop(16f).marginBottom(16f).row();
            t.image().height(4f).color(Pal.accent).growX().row();

            t.add(buildToolTable()).marginTop(16f).marginBottom(16f).row();
        });
    }

    Table buildBlockTable(Seq<Block> blocks, int id) {
        return new Table(t -> {
            t.defaults().size(40);

            int i = 0;
            int row = 7;
            int max = Math.max(row, (int) createModeTable.getWidth() / 40);

            if (id == 0) {
                t.add("地形").row();
                t.image().height(4f).color(Pal.gray).growX().row();
            }
            if (id == 1) {
                t.add("矿脉").row();
                t.image().height(4f).color(Pal.gray).growX().row();
            }
            if (id == 2) {
                t.add("墙体").row();
                t.image().height(4f).color(Pal.gray).growX().row();
            }
            if (id == 3) {
                t.add("装饰建筑").row();
                t.image().height(4f).color(Pal.gray).growX().row();
            }

            if (id == 4) {
                t.add("塞普罗").row();
                t.image().height(4f).color(Pal.gray).growX().row();
            }
            if (id == 5) {
                t.add("埃里克尔").row();
                t.image().height(4f).color(Pal.gray).growX().row();
            }
            if (otherBlocks.size > 0) {
                if (id == 6) {
                    t.add("其他").row();
                }
            }

            for (Block block : blocks) {
                ImageButton button = t.button(Tex.whiteui, Styles.clearTogglei, 24, () -> canDraw = canDraw()).tooltip(tt -> tt.background(Styles.black8).add(block.localizedName)).get();
                button.changed(() -> drawBlock = button.isChecked() ? block : null);
                button.getStyle().imageUp = new TextureRegionDrawable(block.uiIcon);
                button.update(() -> button.setChecked(block == drawBlock));

                if (i++ % max == max - 1) {
                    t.row();
                }
            }

            if (i % max != 0) {
                int remaining = max - (i % max);
                for (int j = 0; j < remaining; j++) {
                    t.image(Styles.black6);
                }
            }
        });
    }

    Table buildTeamTable() {
        Table table = new Table(t -> {
            t.defaults().size(40);

            int i = 0;
            int row = 6;
            int max = Math.max(row, Math.round((createModeTable.getWidth() - 1) / 2 / 8 / row));

            for (Team team : Team.baseTeams) {
                ImageButton button = t.button(Tex.whiteui, Styles.clearTogglei, 24, () -> canDraw = canDraw()).tooltip(tt -> tt.background(Styles.black8).add(team.localized())).get();
                button.changed(() -> drawTeam = button.isChecked() ? team : null);
                button.getStyle().imageUpColor = team.color;
                button.update(() -> button.setChecked(team == drawTeam));

                if (i++ % max == max - 1) {
                    t.row();
                }
            }

            if (i % max != 0) {
                int remaining = max - (i % max);
                for (int j = 0; j < remaining; j++) {
                    t.image(Styles.black6);
                }
            }
        });

        return new Table(t -> t.add(table).maxHeight(Scl.scl(6 * 10 * 5)));
    }

    Table buildToolTable() {
        return new Table(t -> {
            t.table(tt -> {
                ImageButton pencil = new ImageButton(Icon.pencil, Styles.clearTogglei);
                pencil.changed(() -> drawTool = pencil.isChecked() ? 0 : -1);
                pencil.getStyle().imageUp = new TextureRegionDrawable(Icon.pencil);
                pencil.update(() -> pencil.setChecked(drawTool >= 0 && drawTool < 1));
                pencil.clicked(() -> canDraw = canDraw());

                ImageButton fill = new ImageButton(Icon.fill, Styles.clearTogglei);
                fill.changed(() -> drawTool = fill.isChecked() ? 1 : -1);
                fill.getStyle().imageUp = new TextureRegionDrawable(Icon.fill);
                fill.update(() -> fill.setChecked(drawTool >= 1 && drawTool < 2));
                fill.clicked(() -> canDraw = canDraw());

                ImageButton eraser = new ImageButton(Icon.eraser, Styles.clearTogglei);
                eraser.changed(() -> drawTool = eraser.isChecked() ? 2 : -1);
                eraser.getStyle().imageUp = new TextureRegionDrawable(Icon.eraser);
                eraser.update(() -> eraser.setChecked(drawTool >= 2));
                eraser.clicked(() -> canDraw = canDraw());

                ImageButton rotation = new ImageButton(Styles.clearNonei);
                if (this.rotation == 0) {
                    rotation.getStyle().imageUp = new TextureRegionDrawable(Icon.right);
                } else if (this.rotation == 1) {
                    rotation.getStyle().imageUp = new TextureRegionDrawable(Icon.up);
                } else if (this.rotation == 2) {
                    rotation.getStyle().imageUp = new TextureRegionDrawable(Icon.left);
                } else if (this.rotation == 3) {
                    rotation.getStyle().imageUp = new TextureRegionDrawable(Icon.down);
                }
                rotation.changed(() -> {
                    this.rotation = Mathf.mod(this.rotation + 1, 4);
                    if (this.rotation == 0) {
                        rotation.getStyle().imageUp = new TextureRegionDrawable(Icon.right);
                    } else if (this.rotation == 1) {
                        rotation.getStyle().imageUp = new TextureRegionDrawable(Icon.up);
                    } else if (this.rotation == 2) {
                        rotation.getStyle().imageUp = new TextureRegionDrawable(Icon.left);
                    } else if (this.rotation == 3) {
                        rotation.getStyle().imageUp = new TextureRegionDrawable(Icon.down);
                    }
                });


                tt.table(ttt -> {
                    ttt.add(pencil).grow();
                    ttt.add(fill).grow();
                    ttt.add(eraser).grow();
                    ttt.add(rotation).grow();
                }).grow();

                tt.row();

                Label label = new Label("笔刷大小:" + brushSize);

                label.touchable = Touchable.disabled;
                Slider slider = new Slider(0, MapEditor.brushSizes.length - 1, 1, false);
                slider.moved(f -> {
                    brushSize = MapEditor.brushSizes[(int) f];
                    label.setText("笔刷大小:" + brushSize);
                    brushIndex = (int) f;
                });
                slider.setValue(brushIndex);
                tt.stack(slider, label).growX().center();
            }).left().width(createModeTable.getWidth() / 2).margin(8f).growY();

            t.image().width(4f).height(t.getHeight()).color(Pal.gray).growY();

            t.table(tt -> tt.add(rebuildDescTable(drawTool)).grow()).left().width(createModeTable.getWidth() / 2).margin(8f).growY();
        });
    }

    Table rebuildDescTable(float i) {
        return new Table(t -> {
            if (i >= 0 && i < 1) {
                t.button(b -> {
                    b.left().marginLeft(6);
                    b.setStyle(Styles.clearTogglei);
                    b.add("放置").left().row();
                    b.add("在当前位置放置").color(Color.lightGray).left();
                }, () -> drawTool = 0f).update(b -> b.setChecked(drawTool == 0f)).margin(12f).growX().row();
            } else if (i >= 1 && i < 2) {
                t.button(b -> {
                    b.left().marginLeft(6);
                    b.setStyle(Styles.clearTogglei);
                    b.add("部分替换").left().row();
                    b.add("替换同类地形").color(Color.lightGray).left();
                }, () -> drawTool = 1).update(b -> b.setChecked(drawTool == 1)).margin(12f).growX().row();

                t.button(b -> {
                    b.left().marginLeft(6);
                    b.setStyle(Styles.clearTogglei);
                    b.add("全部替换").left().row();
                    b.add("替换地图中所有同类地形").color(Color.lightGray).left();
                }, () -> drawTool = 1.5f).update(b -> b.setChecked(drawTool == 1.5f)).margin(12f).growX().row();
            } else if (i >= 2) {
                t.button(b -> {
                    b.left().marginLeft(6);
                    b.setStyle(Styles.clearTogglei);
                    b.add("清除").left().row();
                    b.add("仅清除建筑和墙体").color(Color.lightGray).left();
                }, () -> drawTool = 2).update(b -> b.setChecked(drawTool == 2)).margin(12f).growX().row();
                t.button(b -> {
                    b.left().marginLeft(6);
                    b.setStyle(Styles.clearTogglei);
                    b.add("清除矿脉").left().row();
                    b.add("仅清除矿脉").color(Color.lightGray).left();
                }, () -> drawTool = 2.5f).update(b -> b.setChecked(drawTool == 2.5f)).margin(12f).growX().row();
            }
        });
    }

    boolean canDraw() {
        if (drawTool >= 2) return true;
        if (drawBlock == null || drawTool == -1) return false;
        if (drawBlock.isFloor() || drawBlock instanceof TreeBlock || drawBlock instanceof TallBlock || drawBlock instanceof Prop) {
            return drawTool >= 0 && drawTool <= 2;
        }
        if ((Items.erekirOnlyItems.asSet().isEmpty() || !Structs.contains(drawBlock.requirements, i -> Items.erekirOnlyItems.asSet().contains(i.item))) && drawTeam != null) {
            return true;
        }
        if (!(Items.erekirOnlyItems.asSet().isEmpty() || !Structs.contains(drawBlock.requirements, i -> Items.erekirOnlyItems.asSet().contains(i.item))) && drawTeam != null) {
            return true;
        }
        return false;
    }

    boolean hasOverlap(Tile tile) {
        if (tile.isCenter() && tile.block() != drawBlock && tile.block().size == drawBlock.size) {
            return false;
        }

        int offsetX = -(drawBlock.size - 1) / 2;
        int offsetY = -(drawBlock.size - 1) / 2;
        for (int dx = 0; dx < drawBlock.size; dx++) {
            for (int dy = 0; dy < drawBlock.size; dy++) {
                int worldX = dx + offsetX + tile.x;
                int worldY = dy + offsetY + tile.y;
                Tile other = world.tile(worldX, worldY);

                if (other != null && other.block().isMultiblock()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void drawBlocks(int x, int y, Boolf<Tile> tester) {
        if (drawBlock.isMultiblock()) {
            x = Mathf.clamp(x, (drawBlock.size - 1) / 2, world.width() - drawBlock.size / 2 - 1);
            y = Mathf.clamp(y, (drawBlock.size - 1) / 2, world.height() - drawBlock.size / 2 - 1);
            Tile tile = world.tile(x, y);
            if (tile != null && !hasOverlap(tile)) {
                tile.setBlock(drawBlock, drawTeam, rotation);
            }
        } else {
            Cons<Tile> drawer = tile -> {
                if (!tester.get(tile)) return;

                if (drawBlock.isFloor() && drawBlock != Blocks.air && !(drawBlock instanceof OverlayFloor)) {
                    tile.setFloor(drawBlock.asFloor());
                } else if (drawBlock instanceof OverlayFloor) {
                    tile.setOverlay(drawBlock);
                } else if (!(tile.block().isMultiblock() && !drawBlock.isMultiblock())) {
                    tile.setBlock(drawBlock, drawTeam, rotation);
                }
            };

            drawCircle(x, y, drawer);
        }
    }

    public void drawCircle(int x, int y, Cons<Tile> drawer) {
        int clamped = (int) brushSize;
        for (int rx = -clamped; rx <= clamped; rx++) {
            for (int ry = -clamped; ry <= clamped; ry++) {
                if (Mathf.within(rx, ry, brushSize - 0.5f + 0.0001f)) {
                    int wx = x + rx, wy = y + ry;

                    if (wx < 0 || wy < 0 || wx >= world.width() || wy >= world.height()) {
                        continue;
                    }

                    drawer.get(world.tile(wx, wy));
                }
            }
        }
    }

    IntSeq stack = new IntSeq();

    void fill(int x, int y, boolean replace, Boolf<Tile> tester, Cons<Tile> filler) {
        int width = world.width(), height = world.height();

        if (replace) {
            for (int cx = 0; cx < width; cx++) {
                for (int cy = 0; cy < height; cy++) {
                    Tile tile = world.tile(cx, cy);
                    if (tester.get(tile)) {
                        filler.get(tile);
                    }
                }
            }

        } else {
            int x1;

            stack.clear();
            stack.add(Point2.pack(x, y));

            try {
                while (stack.size > 0 && stack.size < width * height) {
                    int popped = stack.pop();
                    x = Point2.x(popped);
                    y = Point2.y(popped);

                    x1 = x;
                    while (x1 >= 0 && tester.get(world.tile(x1, y))) x1--;
                    x1++;
                    boolean spanAbove = false, spanBelow = false;
                    while (x1 < width && tester.get(world.tile(x1, y))) {
                        filler.get(world.tile(x1, y));

                        if (!spanAbove && y > 0 && tester.get(world.tile(x1, y - 1))) {
                            stack.add(Point2.pack(x1, y - 1));
                            spanAbove = true;
                        } else if (spanAbove && !tester.get(world.tile(x1, y - 1))) {
                            spanAbove = false;
                        }

                        if (!spanBelow && y < height - 1 && tester.get(world.tile(x1, y + 1))) {
                            stack.add(Point2.pack(x1, y + 1));
                            spanBelow = true;
                        } else if (spanBelow && y < height - 1 && !tester.get(world.tile(x1, y + 1))) {
                            spanBelow = false;
                        }
                        x1++;
                    }
                }
                stack.clear();
            } catch (OutOfMemoryError e) {
                //hack
                stack = null;
                System.gc();
                stack = new IntSeq();
                Log.err(e);
            }
        }
    }
}