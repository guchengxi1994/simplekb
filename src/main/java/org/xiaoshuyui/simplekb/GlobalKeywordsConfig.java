package org.xiaoshuyui.simplekb;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class GlobalKeywordsConfig {

    private static final AtomicReference<List<String>> config = new AtomicReference<>();

    private static AtomicReference<Boolean> needUpdate = new AtomicReference<>(true);

    public static List<String> getConfig() {
        markedAsNotNeedUpdate();
        return config.get();
    }

    public static void setConfig(List<String> newConfig) {
        if (config.get() != null && configEquals(newConfig)) {
            return;
        }
        markedAsNeedUpdate();
        GlobalKeywordsConfig.config.set(newConfig);
    }

    private static boolean configEquals(List<String> newConfig) {
        Set<String> incoming = newConfig.stream().map(String::toLowerCase).collect(java.util.stream.Collectors.toSet());
        Set<String> configSet = config.get().stream().map(String::toLowerCase).collect(java.util.stream.Collectors.toSet());
        return incoming.equals(configSet);
    }

    private static void markedAsNeedUpdate() {
        needUpdate.set(true);
    }

    public static boolean isNeedUpdate() {
        return needUpdate.get();
    }

    private static void markedAsNotNeedUpdate() {
        needUpdate.set(false);
    }
}
