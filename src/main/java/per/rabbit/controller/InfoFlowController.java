package per.rabbit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import per.rabbit.common.Result;
import per.rabbit.dao.InfoFlowBean;
import per.rabbit.dto.InfoFlowAddDTO;
import per.rabbit.service.FileStorageService;
import per.rabbit.service.InfoFlowService;

import java.util.ArrayList;
import java.util.List;

/**
 * 信息流接口
 */
@RestController
@RequestMapping("api/flow")
public class InfoFlowController {
    private static final Logger log = LoggerFactory.getLogger(InfoFlowController.class);
    @Autowired
    private InfoFlowService infoFlowService;

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * 获取卡片列表数据
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "list")
    public Result<Page<InfoFlowBean>> getList(@RequestParam(defaultValue = "0") @Valid @Min(value = 0) int page,
                                              @RequestParam(defaultValue = "10") @Valid @Max(value = 100) int size) {
        Page<InfoFlowBean> infoFlowList = infoFlowService.getInfoFlowList(page, size);
        return Result.success(infoFlowList);
    }

    /**
     * 添加信息流数据
     * multipart/form-data 中支持多个file
     */
    @PostMapping(value = "add", consumes = "multipart/form-data")
    public Result<String> addCardData(@Valid @ModelAttribute InfoFlowAddDTO infoFlowAddDTO) {
        log.info("add card data: {}", infoFlowAddDTO.toString());

        List<String> fileNames = new ArrayList<>();

        // 1.先保存文件，获取uuid文件名
        MultipartFile[] files = infoFlowAddDTO.getFiles();
        if (files != null && files.length > 0) {
            // 1.先保存文件
            try {
                for (MultipartFile file : infoFlowAddDTO.getFiles()) {
                    String fileName = fileStorageService.storeFile(file);
                    fileNames.add(fileName);
                }
            } catch (Exception e) {
                log.error("add card data error: {}", e.getMessage(), e);
                return Result.failed("Internal error!");
            }
        }

        infoFlowService.addInfoFlow(new InfoFlowBean() {{
            setTitle(infoFlowAddDTO.getTitle());
            setContent(infoFlowAddDTO.getContent());
            setImages(fileNames);
            setType("1");
        }});

        return Result.success("");
    }

}
