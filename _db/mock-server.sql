CREATE TABLE `t_api` (
                         `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
                         `url` VARCHAR(255) NOT NULL COMMENT 'url' COLLATE 'utf8mb4_unicode_ci',
                         `method` VARCHAR(10) NOT NULL COMMENT 'method' COLLATE 'utf8mb4_unicode_ci',
                         `code` INT(11) NOT NULL COMMENT 'code',
                         `type` VARCHAR(20) NULL DEFAULT NULL COMMENT '类型' COLLATE 'utf8mb4_unicode_ci',
                         `param` VARCHAR(500) NULL DEFAULT NULL COMMENT '参数' COLLATE 'utf8mb4_unicode_ci',
                         `data` TEXT NULL DEFAULT NULL COMMENT '返回值' COLLATE 'utf8mb4_unicode_ci',
                         `attachment_id` INT(11) NULL DEFAULT NULL COMMENT '附件ID',
                         `attachment_name` VARCHAR(200) NULL DEFAULT NULL COMMENT '附件名' COLLATE 'utf8mb4_unicode_ci',
                         PRIMARY KEY (`id`) USING BTREE
)
    COLLATE='utf8mb4_unicode_ci'
    ENGINE=InnoDB
    AUTO_INCREMENT=4
;

CREATE TABLE `t_attachment` (
                                `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                `name` VARCHAR(255) NOT NULL COMMENT '添付ファイル名称' COLLATE 'utf8mb4_unicode_ci',
                                `url` VARCHAR(255) NOT NULL COMMENT '添付ファイルアドレス' COLLATE 'utf8mb4_unicode_ci',
                                `size` BIGINT(20) NULL DEFAULT NULL COMMENT '添付ファイルサイズ',
                                PRIMARY KEY (`id`) USING BTREE
)
    COMMENT='添付ファイル管理'
    COLLATE='utf8mb4_unicode_ci'
    ENGINE=InnoDB
    AUTO_INCREMENT=18
;
