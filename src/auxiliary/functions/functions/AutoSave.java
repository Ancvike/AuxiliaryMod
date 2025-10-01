package auxiliary.functions.functions;

import arc.Core;
import arc.Events;
import arc.files.Fi;
import arc.scene.ui.CheckBox;
import arc.scene.ui.layout.Table;
import arc.util.Threads;
import arc.util.Time;
import auxiliary.functions.Function;
import mindustry.Vars;
import mindustry.core.GameState;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.ui.dialogs.BaseDialog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static auxiliary.binding.MyKeyBind.SAVE;
import static mindustry.Vars.*;
import static mindustry.Vars.state;


public class AutoSave extends Function {
    private boolean isOpen = false;
    public static final SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    public static final Fi saveDirectory = Vars.dataDirectory.child("AuxiliaryMod-AutoSaveData");
    Fi[] saveFiles = saveDirectory.list();
    BaseDialog saveDataDialog = new BaseDialog("存档管理");
    float time = 0f;

    public AutoSave() {
        super(0, new Table(table -> table.add("自动保存").tooltip("开启后每两分钟自动保存游戏数据,也可按下L键保存, [red]保存目录在游戏数据目录下的AuxiliaryMod-AutoSaveData文件夹中")));

        saveDataDialog.addCloseButton();

        Events.run(EventType.Trigger.update, () -> {
            if (!(isOpen && Vars.state.isGame())) return;
            time += Time.delta;
            if (time < 7200f) return;
            time = 0f;

            Date date = new Date();
            String fileName = fileNameDateFormat.format(date);
            Fi saveFi = saveDirectory.child(fileName + ".zip");
            Threads.thread(() -> {
                try {
                    Vars.ui.settings.exportData(saveFi);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).setPriority(1);
        });

        Events.run(EventType.Trigger.update, () -> {
            if (!isOpen) return;
            if (Core.input.keyTap(SAVE.nowKeyCode)) {
                Date date = new Date();
                String fileName = fileNameDateFormat.format(date);
                Fi saveFi = saveDirectory.child(fileName + ".zip");
                Threads.thread(() -> {
                    try {
                        Vars.ui.settings.exportData(saveFi);
                        ui.showInfo("@data.exported");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).setPriority(1);
            }
        });
    }

    @Override
    public Table function() {
        return new Table(t -> {
            CheckBox box = new CheckBox("");
            box.update(() -> box.setChecked(isOpen));
            box.changed(() -> isOpen = !isOpen);
            t.add(box);

            t.button("查看存档数据", () -> {
                rebuildSaveDataDialog();
                saveDataDialog.show();
            }).grow();
        });
    }

    void rebuildSaveDataDialog() {
        saveDataDialog.cont.clear();
        saveDataDialog.setWidth(400f);
        saveFiles = saveDirectory.list();
        if (saveFiles.length == 0) {
            saveDataDialog.cont.add("暂无存档数据").row();
            return;
        }
        saveDataDialog.cont.pane(p -> {
            for (Fi fi : saveFiles) {
                p.button(fi.nameWithoutExtension(), () -> ui.showConfirm("@confirm", "@data.import.confirm", () -> {
                    try {
                        ui.settings.importData(fi);
                        control.saves.resetSave();
                        state = new GameState();
                        Core.app.exit();
                    } catch (IllegalArgumentException e) {
                        ui.showErrorMessage("@data.invalid");
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (e.getMessage() == null || !e.getMessage().contains("too short")) {
                            ui.showException(e);
                        } else {
                            ui.showErrorMessage("@data.invalid");
                        }
                    }
                })).growX();
                p.button(Icon.trash, () -> ui.showConfirm("@confirm", "确定删除吗?[red]操作不可逆", () -> {
                    fi.delete();
                    rebuildSaveDataDialog();
                }));
                p.row();
            }
        }).grow();
    }
}