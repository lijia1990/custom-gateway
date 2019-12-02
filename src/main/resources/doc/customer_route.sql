

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `limiting_rule`
-- ----------------------------
DROP TABLE IF EXISTS `limiting_rule`;
CREATE TABLE `limiting_rule` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `rule_name` varchar(30) NOT NULL COMMENT '规则名称',
  `rule_type` tinyint(1) DEFAULT '0' COMMENT '规则类型 0:ip 1:接口',
  `rule_vals` varchar(600) NOT NULL DEFAULT '' COMMENT '限流参数集 以逗号隔离',
  `qps_count` bigint(16) NOT NULL COMMENT 'QPS限流指数',
  `limiting_start_time` bigint(16) DEFAULT NULL COMMENT '限流开启时间',
  `limiting_end_time` bigint(16) DEFAULT NULL COMMENT '限流关闭时间',
  `is_del` tinyint(1) DEFAULT '0' COMMENT '默认0：有效 1:无效',
  `create_tm` bigint(16) NOT NULL COMMENT '创建时间',
  `update_tm` bigint(16) NOT NULL COMMENT '修改时间',
  `create_id` varchar(20) DEFAULT '' COMMENT '创建人ID',
  `update_id` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of limiting_rule
-- ----------------------------

-- ----------------------------
-- Table structure for `limiting_rule_global`
-- ----------------------------
DROP TABLE IF EXISTS `limiting_rule_global`;
CREATE TABLE `limiting_rule_global` (
  `id` bigint(16) NOT NULL AUTO_INCREMENT,
  `rule_name` varchar(30) NOT NULL COMMENT '规则名称',
  `rule_type` tinyint(1) DEFAULT '0' COMMENT '规则类型 0:ip 1:接口',
  `qps_count` bigint(16) NOT NULL COMMENT 'QPS限流指数',
  `limiting_start_time` bigint(16) DEFAULT NULL COMMENT '限流开启时间',
  `limiting_end_time` bigint(16) DEFAULT NULL COMMENT '限流关闭时间',
  `is_del` tinyint(1) DEFAULT '0' COMMENT '默认0：有效 1:无效',
  `create_tm` bigint(16) NOT NULL COMMENT '创建时间',
  `update_tm` bigint(16) NOT NULL COMMENT '修改时间',
  `create_id` varchar(20) DEFAULT '' COMMENT '创建人ID',
  `update_id` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of limiting_rule_global
-- ----------------------------

-- ----------------------------
-- Table structure for `route`
-- ----------------------------
DROP TABLE IF EXISTS `route`;
CREATE TABLE `route` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `predicates` varchar(1024) NOT NULL DEFAULT '' COMMENT '断言JSON',
  `filters` varchar(1024) DEFAULT '' COMMENT '拦截规则JSON',
  `uri` varchar(248) NOT NULL DEFAULT '' COMMENT '转向URL',
  `route_order` int(6) DEFAULT '0' COMMENT '排序',
  `is_del` smallint(1) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_id` varchar(20) DEFAULT '',
  `update_id` varchar(20) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of route
-- ----------------------------
INSERT INTO `route` VALUES ('1', '[{\"args\":{\"pattern\":\"/demo/**\"},\"name\":\"Path\"}]', '[{\"args\":{\"parts\":\"0\"},\"name\":\"StripPrefix\"}]', 'lb://DEMO-WEB', '0', '0', '2019-11-29 16:46:17', '2019-11-29 16:46:17', null, null);
