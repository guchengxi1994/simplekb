package org.xiaoshuyui.simplekb.config;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 全局关键字配置类，用于管理关键字列表及其更新状态
 */
public class GlobalKeywordsConfig {

    /**
     * 使用AtomicReference来安全地更新关键字配置列表
     */
    private static final AtomicReference<List<String>> config = new AtomicReference<>();

    /**
     * 使用AtomicReference来标记配置是否需要更新
     */
    private static AtomicReference<Boolean> needUpdate = new AtomicReference<>(true);

    /**
     * 获取当前的关键字配置列表
     * 在获取配置前，标记配置不需要更新
     *
     * @return 当前的关键字列表
     */
    public static List<String> getConfig() {
        markedAsNotNeedUpdate();
        return config.get();
    }

    /**
     * 设置新的关键字配置列表
     * 如果新旧配置相同（不考虑大小写），则不进行更新
     * 否则，更新配置并标记需要更新
     *
     * @param newConfig 新的关键字列表
     */
    public static void setConfig(List<String> newConfig) {
        if (config.get() != null && configEquals(newConfig)) {
            return;
        }
        markedAsNeedUpdate();
        GlobalKeywordsConfig.config.set(newConfig);
    }

    /**
     * 检查两个配置列表是否相等（不考虑大小写）
     *
     * @param newConfig 新的配置列表
     * @return 如果两个列表相等返回true，否则返回false
     */
    private static boolean configEquals(List<String> newConfig) {
        Set<String> incoming = newConfig.stream().map(String::toLowerCase).collect(java.util.stream.Collectors.toSet());
        Set<String> configSet = config.get().stream().map(String::toLowerCase).collect(java.util.stream.Collectors.toSet());
        return incoming.equals(configSet);
    }

    /**
     * 标记配置需要更新
     */
    private static void markedAsNeedUpdate() {
        needUpdate.set(true);
    }

    /**
     * 检查配置是否需要更新
     *
     * @return 如果配置需要更新返回true，否则返回false
     */
    public static boolean isNeedUpdate() {
        return needUpdate.get();
    }

    /**
     * 标记配置不需要更新
     */
    private static void markedAsNotNeedUpdate() {
        needUpdate.set(false);
    }
}

