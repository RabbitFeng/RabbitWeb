```sql
-- 卡片内容表
CREATE TABLE `info_flow_data`
(
`id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
`title`      VARCHAR(255)    NOT NULL DEFAULT '' COMMENT '标题',
`content`    TEXT COMMENT '内容',
`images`     JSON                     DEFAULT NULL COMMENT '图片URL列表，存储JSON数组格式如 ["url1", "url2"]',
`type`       VARCHAR(32)     NOT NULL DEFAULT 'singleLine' COMMENT '类型：singleLine-单行, multiLine-多行',
`created_at` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`updated_at` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
`status`     VARCHAR(32)     NOT NULL DEFAULT 'enabled' COMMENT '状态：enabled-启用, disabled-禁用',
PRIMARY KEY (`id`)
) ENGINE = InnoDB
DEFAULT CHARSET = utf8mb4
COLLATE = utf8mb4_unicode_ci COMMENT ='信息流数据表';

DROP TABLE `info_flow_data`;
```

```sql
SELECT id, title, content, images, type
FROM info_flow_data
ORDER BY id DESC;

-- 文件信息表
CREATE TABLE `file_info`
(
`id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
`mapped_name`   VARCHAR(255)    NOT NULL DEFAULT '' COMMENT 'mapped_name',
`original_name` VARCHAR(255) COMMENT '文件原名称',
`path`          VARCHAR(255) COMMENT '磁盘路径',
`created_at`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`updated_at`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (`id`)
) ENGINE = InnoDB
DEFAULT CHARSET = utf8mb4
COLLATE = utf8mb4_unicode_ci COMMENT ='文件信息表';``` 
```

```sql
-- 用户信息表
DROP TABLE `user_info`;

CREATE TABLE `user_info`
(
`id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
`user_id`      VARCHAR(32)     NOT NULL DEFAULT '' COMMENT 'user_id',
`user_name`    VARCHAR(32)     NOT NULL DEFAULT '' COMMENT '用户名称',
`pwd`          VARCHAR(100)    NOT NULL DEFAULT '' COMMENT '密码（BCrypt 哈希，固定 60 字符，留余量）',
`email`        VARCHAR(64)     NOT NULL DEFAULT '' COMMENT '邮箱',
`phone`        VARCHAR(16) COMMENT '电话号码',
`created_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`updated_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
PRIMARY KEY (`id`)
) ENGINE = InnoDB
DEFAULT CHARSET = utf8mb4
DEFAULT COLLATE = utf8mb4_unicode_ci COMMENT = '用户信息表';
```

食品详情表
```sql
CREATE TABLE `food_`
(
    `id`
)
```