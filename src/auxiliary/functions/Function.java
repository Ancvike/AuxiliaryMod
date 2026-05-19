package auxiliary.functions;

import arc.scene.ui.layout.Table;

public class Function {
    private final int classID;
    private final Table name;

    public Function(int classID, Table name) {
        this.classID = classID;
        this.name = name;
    }

    public Table getName() {
        return name;
    }

    public int getClassID() {
        return classID;
    }

    public Table function() {
        return null;
    }
}