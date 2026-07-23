package per.rabbit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import per.rabbit.FileCache;
import per.rabbit.common.utils.FileUtil;
import per.rabbit.common.utils.PathUtil;
import per.rabbit.dao.FileBean;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 文件存取服务
 */
@Service
public class FileStorageService {

    // 注入配置中的路径
    @Value("${user.path.img}")
    private String imgPath;

    @Value("${user.path.disk}")
    private String diskPath;

    @Value("${user.path.file}")
    private String filePath;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FileCache fileCache;

    /**
     * 存储文件
     *
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public String storeFile(MultipartFile multipartFile) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        String extension = FileUtil.getExtension(originalFileName, true);

        String pathDir = getPathDir(originalFileName);

        // 转储时更换为uuid+后缀
        String mappedName = UUID.randomUUID().toString().replace("-", "") + extension;
        File file = Paths.get(PathUtil.getRootPath().toString(), pathDir, mappedName).toFile();
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs() && !parentDir.exists()) {
                throw new IOException("无法创建文件目录: " + parentDir.getAbsolutePath());
            }
        }
        multipartFile.transferTo(file);

        logger.info("store file: {} to {}", originalFileName, pathDir + File.separator + file.getName());
        fileCache.put(mappedName, new FileBean(mappedName, originalFileName, pathDir));
        return mappedName;
    }

    /**
     * 提取文件
     */
    public Resource takeFile(String filename) throws IOException {
        FileBean fileBean = fileCache.getOrLoad(filename);
        if (fileBean == null) {
            throw new IOException("文件不存在: " + filename);
        }

        try {
            Path path = Paths.get(PathUtil.getRootPath().toString(), fileBean.getPath());
            logger.info("take file: {} from {}", filename, path);
            Resource resource = new UrlResource(path.resolve(filename).normalize().toUri());

            if (!resource.exists()) {
                throw new RuntimeException("文件不存在: " + filename);
            }
            if (!resource.isReadable()) {
                throw new RuntimeException("文件不可读: " + filename);
            }
            return resource;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取文件分区路径
     */
    private String getPathDir(String filename) {
        if (FileUtil.isImg(filename)) {
            return imgPath;
        }
        return filePath;
    }
}
