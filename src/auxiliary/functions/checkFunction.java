package auxiliary.functions;

import arc.scene.ui.layout.Table;
import mindustry.ui.dialogs.BaseDialog;

public class checkFunction extends Function {
    BaseDialog baseDialog = new BaseDialog("");

    public checkFunction() {
        super(0, new Table(table -> table.add("查看mod功能")));

        baseDialog.addCloseButton();
        baseDialog.cont.add("基本功能都在其中,其他功能都会在本页说明");
        baseDialog.cont.row();
        baseDialog.cont.row();
        baseDialog.cont.add("埃里克尔星球新增装卸器,可对核心进行卸载");
        baseDialog.cont.row();
        baseDialog.cont.row();
        baseDialog.cont.add("[accent]电脑端----");
        baseDialog.cont.row();
        baseDialog.cont.add("U键:框选己方建筑,并对建筑血量进行修改");
        baseDialog.cont.row();
        baseDialog.cont.add("I键:在指挥模式中,对选中单位进行血量修改");
        baseDialog.cont.row();
        baseDialog.cont.add("O键:快捷打开面板");
        baseDialog.cont.row();
        baseDialog.cont.row();
        baseDialog.cont.add("[accent]手机端----");
        baseDialog.cont.row();
        baseDialog.cont.add("主功能按钮下安卓图标按钮开启后:长按屏幕框选己方建筑,并对建筑血量进行修改");
        baseDialog.cont.row();
        baseDialog.cont.add("在指挥模式下,按下指挥按钮上方的安卓图标按钮:对选中单位进行血量修改");
    }

    @Override
    public Table function() {
        return new Table(t -> t.button("查看", () -> baseDialog.show()).width(200f));
    }
}
