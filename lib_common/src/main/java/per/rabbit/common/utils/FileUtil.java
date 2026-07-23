package per.rabbit.common.utils;

import java.io.File;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class FileUtil {

    public static final Set<String> IMG_EXTENSION_SET = new HashSet<>() {{
        add("jpg");
        add("png");
        add("webp");
    }};

    /**
     * 是否是图片资源
     *
     * @param path
     * @return
     */
    public static boolean isImg(Path path) {
        return IMG_EXTENSION_SET.contains(getExtension(path, false));
    }

    /**
     * 是否是图片资源
     *
     * @param pathname
     * @return
     */
    public static boolean isImg(String pathname) {
        return IMG_EXTENSION_SET.contains(getExtension(pathname, false));
    }

    /**
     * 获取文件扩展名
     *
     * @param path
     * @return
     */
    public static String getExtension(Path path,boolean withDot) {
        if (path == null) {
            return "";
        }
        String pathStr = path.toString();
        return getExtension(pathStr, withDot);
    }

    public static String getExtension(String pathstr, boolean withDot) {
        if (pathstr == null) {
            return "";
        }
        int i = pathstr.lastIndexOf('.');
        return i < 0 ? "" : pathstr.substring(withDot ? i : i + 1);
    }
}
