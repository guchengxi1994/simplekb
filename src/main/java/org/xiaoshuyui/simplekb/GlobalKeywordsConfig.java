package org.xiaoshuyui.simplekb;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class GlobalKeywordsConfig {

    private static final AtomicReference<List<String>> config = new AtomicReference<>();

    public static List<String> getConfig() {
        return config.get();
    }

    public static void setConfig(List<String> config) {
        GlobalKeywordsConfig.config.set(config);
    }
}
