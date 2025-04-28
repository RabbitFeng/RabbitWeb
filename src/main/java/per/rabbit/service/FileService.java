package per.rabbit.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import per.rabbit.common.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {

    // 注入配置中的路径
    @Value("${user.path.img}")
    private String imgPath;

    @Value("${user.path.file}")
    private String filePath;

    public String storeFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String targetPath = getTargetPath(originalFilename);
        String extension = FileUtil.getExtension(originalFilename, true);
        String newFileName = UUID.randomUUID() +  extension;
        File file = Paths.get(targetPath, newFileName).toFile();
        multipartFile.transferTo(file);
        return newFileName;
    }

    private String getTargetPath(String filename) {
        if (FileUtil.isImg(filename)) {
            return imgPath;
        }
        return filePath;
    }
}
