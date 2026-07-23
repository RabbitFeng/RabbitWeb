package per.rabbit.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathUtil {
    private static final Logger log = LoggerFactory.getLogger(PathUtil.class);
    private static final String DEPLOY_HOME_PROPERTY = "rabbit.home";
    private static final String DEPLOY_HOME_ENV = "RABBIT_HOME";

    public static Path get(String first, String... more) {
        return Paths.get(first, more);
    }

    private static volatile String rootPathCache;

    /**
     * 获取部署目录。
     *
     * <p>优先级：系统属性 rabbit.home -> 环境变量 RABBIT_HOME -> jar 所在目录 -> 当前工作目录。</p>
     */
    public static Path getRootPath(String... more) {
        if (rootPathCache != null && !rootPathCache.isEmpty()) {
            return get(rootPathCache, more);
        }

        rootPathCache = resolveRootPath();
        log.info("rootPath:{}", rootPathCache);
        return get(rootPathCache, more);
    }

    private static String resolveRootPath() {
        String configuredRoot = System.getProperty(DEPLOY_HOME_PROPERTY);
        if (configuredRoot == null || configuredRoot.isBlank()) {
            configuredRoot = System.getenv(DEPLOY_HOME_ENV);
        }
        if (configuredRoot != null && !configuredRoot.isBlank()) {
            return Paths.get(configuredRoot).toAbsolutePath().normalize().toString();
        }

        Path commandPath = resolveCommandPath();
        if (commandPath != null) {
            Path parent = commandPath.getParent();
            if (parent != null) {
                return parent.toAbsolutePath().normalize().toString();
            }
            return commandPath.toAbsolutePath().normalize().toString();
        }

        return Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize().toString();
    }

    private static Path resolveCommandPath() {
        String command = System.getProperty("sun.java.command");
        if (command == null || command.isBlank()) {
            return null;
        }

        String mainArgument = command.split("\\s+")[0];
        if (mainArgument.endsWith(".jar")) {
            return Paths.get(mainArgument);
        }
        return null;
    }
}
