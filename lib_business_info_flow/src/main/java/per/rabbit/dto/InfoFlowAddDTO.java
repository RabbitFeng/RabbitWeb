package per.rabbit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.BindParam;
import org.springframework.web.multipart.MultipartFile;

public class InfoFlowAddDTO {
    @NotBlank(message = "标题不能为空")
    @Size(min = 1, max = 100, message = "标题长度必须在1到100之间")
    @BindParam(value = "title")
    private String title;

    @Size(max = 65535, message = "内容长度不能过长")
    @BindParam(value = "content")
    private String content;

    @Size(max = 5, message = "文件数量不能超过5")
    @BindParam(value = "files")
    private MultipartFile[] files;

    public InfoFlowAddDTO() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MultipartFile[] getFiles() {
        return files;
    }

    public void setFiles(MultipartFile[] files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "InfoFlowDTO{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", files=" + (files == null ? "null" : files.length) +
                '}';
    }
}
