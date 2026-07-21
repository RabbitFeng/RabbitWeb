package per.rabbit.controller;

import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import per.rabbit.common.Result;
import per.rabbit.common.utils.PathUtil;
import per.rabbit.dao.FileDao;
import per.rabbit.service.FileService;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 文件接口
 * https://blog.csdn.net/dubulingbo/article/details/122105876
 */
@RestController
@RequestMapping("/api/file")
public class FileController {

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @Autowired
    public FileService fileService;

    @RequestMapping("/file/list")
    public ResponseEntity<JSONObject> getFileList() {
        log.info("getFileList");
        return ResponseEntity.ok().body(new JSONObject());
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = "multipart/form-data")
    public Result<String> upload(@RequestPart("file") MultipartFile multipartFile) {
        try {
            String filename = fileService.storeFile(multipartFile);
            return Result.success(filename);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.failed("上传失败: " + e.getMessage());
        }
    }

    @GetMapping(value = "/download/{filename:.+}")
    public ResponseEntity<Resource> download(@PathVariable String filename) {
        log.info("download {}", filename);
        try {
            Resource resource = fileService.takeFile(filename);

            String contentType = MediaType.APPLICATION_OCTET_STREAM.toString();
            try {
                contentType = Files.probeContentType(resource.getFile().toPath());
            }catch (IOException e){
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            ContentDisposition.inline()// inline预览
                                    .filename(filename, StandardCharsets.UTF_8)
                                    .build()
                                    .toString())
                    .body(resource);
        } catch (IOException e) {
            log.warn("download failed for {}", filename, e);
            return ResponseEntity.notFound().build();
        }
    }

}
