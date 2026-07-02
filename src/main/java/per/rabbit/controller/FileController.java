package per.rabbit.controller;

import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import per.rabbit.service.FileService;

import java.io.IOException;

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
