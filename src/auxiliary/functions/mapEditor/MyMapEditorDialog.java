package auxiliary.functions.mapEditor;

import mindustry.editor.MapEditorDialog;

public class MyMapEditorDialog extends MapEditorDialog {

    // 必须显式调用父类构造函数
    public MyMapEditorDialog() {
        super(); // 关键：调用父类无参构造函数
    }
}