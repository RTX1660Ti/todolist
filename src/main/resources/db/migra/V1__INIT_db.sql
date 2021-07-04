DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
                           `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
                           `user_name` varchar(32)  NOT NULL COMMENT '用户名',
                           `pass_word` varchar(32)  NOT NULL COMMENT '密码',
                           `isAdmin` boolean  DEFAULT false COMMENT '是否是管理员',
                           `inUse` boolean  DEFAULT false COMMENT '是否正在使用',
                           `create_time` BIGINT NOT NULL COMMENT '创建时间',
                           `update_time` BIGINT NOT NULL COMMENT '更新时间',
                           PRIMARY KEY (`id`) USING BTREE,
                           KEY `index_user_name` (`user_name`) USING BTREE
);
INSERT INTO `t_user` VALUES (1,'admin','admin', true,true,1625192927411,1625192927411);
INSERT INTO `t_user` VALUES (2,'userA','userA', false,true,1625192927411,1625192927411);
INSERT INTO `t_user` VALUES (3,'userB','userB', false,true,1625192927411,1625192927411);

DROP TABLE IF EXISTS `t_business`;
CREATE TABLE `t_business`  (
                               `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
                               `user_id` BIGINT  NOT NULL COMMENT '创建人ID',
                               `description` varchar(512)  NOT NULL COMMENT '事务详情',
                               `isEnd` boolean  DEFAULT false COMMENT '是否完成',
                               `isDelete` boolean  DEFAULT false COMMENT '是否删除',
                               `business_time` BIGINT NOT NULL COMMENT '事务时间',
                               `create_time` BIGINT NOT NULL COMMENT '创建时间',
                               `update_time` BIGINT NOT NULL COMMENT '更新时间',
                               PRIMARY KEY (`id`) USING BTREE,
                               KEY `index_user_id` (`user_id`) USING BTREE
);