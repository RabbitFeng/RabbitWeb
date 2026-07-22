package per.rabbit.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("file_info")
public class FileBean {
    /**
     * 标记自增主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("mapped_name")
    private String mappedName;

    @TableField("original_name")
    private String originalName;

    @TableField("path")
    private String path;

    public FileBean() {
    }

    public FileBean(String mappedName, String originalName, String path) {
        this.mappedName = mappedName;
        this.originalName = originalName;
        this.path = path;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMappedName() {
        return mappedName;
    }

    public void setMappedName(String mappedName) {
        this.mappedName = mappedName;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "FileDao{" +
                "mappedName='" + mappedName + '\'' +
                ", originalName='" + originalName + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
