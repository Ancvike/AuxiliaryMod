package auxiliary.functions;

public class Function {
    private final int buttonID;
    private final String name;

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