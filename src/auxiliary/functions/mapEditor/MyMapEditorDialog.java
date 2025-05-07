package auxiliary.functions.mapEditor;

import arc.files.Fi;
import arc.util.*;
import mindustry.editor.*;
import mindustry.io.MapIO;
import mindustry.maps.Map;

import static mindustry.Vars.*;

public class MyMapEditorDialog extends MapEditorDialog {

    @Override
    public void beginEditMap(Fi file) {
        ui.loadAnd(() -> {
            try {
                Log.info("开始加载地图文件: " + file.path());

                // 1. 验证文件有效性
                if (!file.exists()) {
                    ui.showErrorMessage("[red]文件不存在:[] " + file.path());
                    return;
                }

                // 2. 创建临时地图对象用于调试
                Map tempMap = MapIO.createMap(file, true);
                if (tempMap == null) {
                    ui.showErrorMessage("[red]地图创建失败[]");
                    return;
                }

                Log.info("地图基础信息: " +
                        "名称=" + tempMap.name() +
                        ", 尺寸=" + tempMap.width + "x" + tempMap.height);

                // 3. 强制重置编辑器状态
                editor.clearOp();
                editor.renderer.updateAll();

                // 4. 开始编辑（添加异常捕获细节）
                editor.beginEdit(tempMap);

                // 5. 验证加载结果
                if (editor.tiles() == null || editor.tiles().width == 0) {
                    throw new RuntimeException("地图瓦片数据为空");
                }

                Log.info("地图加载成功，瓦片尺寸: " +
                        editor.tiles().width + "x" + editor.tiles().height);

                // 6. 显示编辑器
                show();

            } catch (Throwable e) {
                Log.err("地图加载失败", e);
                ui.showException("[red]地图加载错误[]", e);

                // 恢复默认状态
                editor.clearOp();
                editor.beginEdit(32, 32); // 创建空地图作为fallback
            }
        });
    }
}