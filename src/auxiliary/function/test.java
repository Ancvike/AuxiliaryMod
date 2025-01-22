package auxiliary.function;

import mindustry.gen.Icon;

import static mindustry.Vars.state;

public class test extends Function {
    public test() {
        super("test", Icon.steam, "test");
    }

    @Override
    public void onClick() {
        state.rules.waves = false;
    }
}