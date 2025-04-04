package auxiliary.functions;

import arc.scene.ui.layout.Table;

public class Function {
    private final int classID;
    private final String name;

    public Function(int classID, String name) {
        this.classID = classID;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getClassID() {
        return classID;
    }

    public Table function() {
        return null;
    }
}