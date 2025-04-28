package per.rabbit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * https://blog.csdn.net/goodjava2007/article/details/126132375
 * https://blog.csdn.net/qq_39749527/article/details/112492160
 * 通过这层配置可以通过 /api/file/img/path 来访问指定的图片
 */
@Configuration
public class ImagePathConfig implements WebMvcConfigurer {
    @Value("${user.path.img}")
    private String imgDir;

    @Value("${user.path.file}")
    private String fileDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/api/file/file/**")
                .addResourceLocations("file:" + fileDir);

        registry.addResourceHandler("/api/file/img/**")
                .addResourceLocations("file:" + imgDir);
    }
}
