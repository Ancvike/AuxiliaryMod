package auxiliary.functions;

import arc.scene.ui.layout.Table;

public class Function {
    private final int classID;
    private final int functionID;
    private final String name;

    public Function(int classID, int functionID, String name) {
        this.classID = classID;
        this.functionID = functionID;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getClassID() {
        return classID;
    }

    public int getFunctionID() {
        return functionID;
    }

    public Table function() {
        return null;
    }
}