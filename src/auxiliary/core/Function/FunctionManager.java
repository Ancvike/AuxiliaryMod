package auxiliary.core.Function;

public class FunctionManager {
    public static void init() {
        new Setting().init();
        FullResource.init();
        BuildingRestoration.init();
        ConveyorUP.init();
    }
}
