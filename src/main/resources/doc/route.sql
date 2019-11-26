
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `xt_route`
-- ----------------------------
DROP TABLE IF EXISTS `route`;
CREATE TABLE `route` (
  `id` bigint(19) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `predicates` varchar(1024) NOT NULL DEFAULT '' COMMENT '断言JSON',
  `filters` varchar(1024) DEFAULT '' COMMENT '拦截规则JSON',
  `uri` varchar(248) NOT NULL DEFAULT '' COMMENT '转向URL',
  `route_order` int(6) DEFAULT '0' COMMENT '排序',
  `is_del` smallint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of xt_route
-- ----------------------------
INSERT INTO `route` VALUES ('1', '[{\"args\":{\"pattern\":\"/demo/**\"},\"name\":\"Path\"}]', '[{\"args\":{\"parts\":\"0\"},\"name\":\"StripPrefix\"}]', 'lb://DEMO-WEB', '0', '0');
