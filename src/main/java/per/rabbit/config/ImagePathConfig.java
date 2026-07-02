package per.rabbit.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.CssLinkResourceTransformer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.thymeleaf.util.TextUtils;
import per.rabbit.common.utils.FileUtil;
import per.rabbit.common.utils.PathUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.temporal.TemporalUnit;

/**
 * https://blog.csdn.net/goodjava2007/article/details/126132375
 * https://blog.csdn.net/qq_39749527/article/details/112492160
 * 通过这层配置可以通过 /api/file/img/path 来访问指定的图片
 */
@Configuration
public class ImagePathConfig implements WebMvcConfigurer {
    private static final Logger log = LoggerFactory.getLogger(ImagePathConfig.class);

    @Value("${user.path.file}")
    private String fileDir;

    @Value("${user.path.img}")
    private String imgDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path filePath = PathUtil.getRootPath(fileDir);
        createDirectories(filePath);
        log.info("filePath:{}", filePath);
        registry.addResourceHandler("/api/file/file/**")
                .addResourceLocations("file:" + filePath)
                .resourceChain(true)
                .addResolver(new LogPathResourceResolver());

        Path imgPath = PathUtil.getRootPath(imgDir);
        createDirectories(imgPath);
        log.info("imgPath:{}", imgPath);
        registry.addResourceHandler("/api/file/img/**")
                .addResourceLocations("file:" + imgPath) // 固定是file:
                .resourceChain(true)
                .addResolver(new LogPathResourceResolver());
    }

    private static void createDirectories(Path path) {
        if (!Files.exists(path)) {
            try {
                log.warn("createDirectories, path:{}", path);
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class LogPathResourceResolver extends PathResourceResolver {
        @Override
        protected Resource getResource(String resourcePath, Resource location) throws IOException {
            Resource resource = super.getResource(resourcePath, location);
            if (resource != null && resource.exists() && resource.isReadable()) {
                log.info("【访问成功】resource:{}", resource);
                return resource;
            } else {
                log.warn("【访问失败】resourcePath:{}, location{}", resourcePath, location);
            }
            return super.getResource(resourcePath, location);
        }
    }
}
