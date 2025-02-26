package auxiliary.functions;

import arc.scene.ui.Slider;

public class Function {
    private final int buttonID;
    private final String name;
    Slider slider;

    public Function(int buttonID, String name) {
        this.buttonID = buttonID;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getButtonID() {
        return buttonID;
    }

    public void onClick() {

    }
}