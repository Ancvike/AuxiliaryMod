package FullResource.core.coreSetting;

import arc.struct.Seq;

public class SettingHelper {
    public static void addGraphicCheckSetting(String key, boolean def, Seq<SharSetting> list, Runnable onSetted) {
        list.add(new SharSetting(key, def) {});
    }
}