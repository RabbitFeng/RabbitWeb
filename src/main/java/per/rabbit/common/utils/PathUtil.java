package per.rabbit.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.system.ApplicationHome;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathUtil {
    private static final Logger log = LoggerFactory.getLogger(PathUtil.class);

    public static Path get(String first, String... more) {
        return Paths.get(first, more);
    }

    private static volatile String rootPathCache;

    /**
     * 获取项目根目录
     */
    public static Path getRootPath(String... more) {
        if (rootPathCache != null && !rootPathCache.isEmpty()) {
            return get(rootPathCache, more);
        }

        ApplicationHome applicationHome = new ApplicationHome(PathUtil.class);
        rootPathCache = applicationHome.getDir().getAbsolutePath();
        log.info("absolutePath:{}", rootPathCache);
        return get(rootPathCache, more);
    }
}
