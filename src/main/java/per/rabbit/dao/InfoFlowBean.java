package per.rabbit.dao;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import per.rabbit.common.StringListTypeHandler;

import java.util.List;

@TableName(value = "info_flow_data", autoResultMap = true)
public class InfoFlowBean {
    @JSONField(serialize = false, deserialize = false)
    @TableId(type = IdType.AUTO)
    private Long id;

    @JSONField(name = "title")
    @TableField("title")
    private String title;

    @JSONField(name = "content")
    @TableField(value = "content")
    private String content;

    @JSONField(name = "images")
    @TableField(value = "images", typeHandler = StringListTypeHandler.class)
    private List<String> images;

    @JSONField(name = "type")
    @TableField("type")
    private String type;

    @JSONField(name = "status")
    @TableField("status")
    private String status;

    public InfoFlowBean() {
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CardDataBean{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", images=" + images +
                ", type='" + type + '\'' +
                '}';
    }
}
