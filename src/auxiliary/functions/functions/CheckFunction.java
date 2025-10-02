package auxiliary.functions.functions;

import arc.Core;
import arc.scene.ui.layout.Table;
import auxiliary.functions.Function;
import mindustry.ui.dialogs.BaseDialog;

public class CheckFunction extends Function {
    BaseDialog baseDialog = new BaseDialog("");

    public CheckFunction() {
        super(0, new Table(table -> table.add("查看mod功能")));

        baseDialog.addCloseButton();
        baseDialog.cont.table(t -> {
            t.left();
            t.add("基本功能都在其中,其他功能都会在本页说明").left();
            t.row();
            t.add("埃里克尔星球新增装卸器,可对核心进行卸载").left();
            t.row();
            t.add("[accent]电脑端----").left();
            t.row();
            t.add("U键:框选己方建筑,并对建筑血量进行修改").left();
            t.row();
            t.add("I键:在指挥模式中,对选中单位进行血量修改").left();
            t.row();
            t.add("O键:快捷打开面板").left();
            t.row();
            t.add("L键:保存游戏数据").left();
            t.row();
            t.add("在设置中可以更改键位").left();
            t.row();
            t.add("[accent]手机端----").left();
            t.row();
            t.add("主功能按钮下安卓图标按钮开启后:长按屏幕框选己方建筑,并对建筑血量进行修改").left();
            t.row();
            t.add("在指挥模式下,按下指挥按钮上方的安卓图标按钮:对选中单位进行血量修改").left();
            t.row();
            t.table(tt -> {
                tt.add("关于作者:").left();
                tt.button("B站", () -> Core.app.openURI("https://space.bilibili.com/1474079275")).width(150f);
                tt.button("Github", () -> Core.app.openURI("https://github.com/Ancvike")).width(150f);
            }).left();
        }).width(400f).growY().center();
    }

    @Override
    public Table function() {
        return new Table(t -> t.button("查看", () -> baseDialog.show()).width(200f));
    }
}
