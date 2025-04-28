package per.rabbit.controller;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import per.rabbit.service.FileService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 文件接口
 * https://blog.csdn.net/dubulingbo/article/details/122105876
 */
@RestController
@RequestMapping("/api/img")
public class ImageController {

    @Autowired
    public FileService fileService;

    @RequestMapping("/file/list")
    public ResponseEntity<JSONObject> getFileList() {
        return ResponseEntity.ok().body(new JSONObject());
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = "multipart/form-data")
    public ResponseEntity<String> upload(@RequestPart("file") MultipartFile multipartFile) {
        try {
            String filename = fileService.storeFile(multipartFile);
            return ResponseEntity.ok(filename);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
