/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50540
Source Host           : localhost:3306
Source Database       : bilifuture

Target Server Type    : MYSQL
Target Server Version : 50540
File Encoding         : 65001

Date: 2019-05-29 10:22:05
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` varchar(100) NOT NULL COMMENT '用户名',
  `pwd` varchar(100) NOT NULL COMMENT '密码',
  `tel` varchar(11) NOT NULL COMMENT '手机号',
  `lastLoginDate` datetime NOT NULL COMMENT '上次登录时间',
  `lastLoginIp` varchar(255) DEFAULT NULL COMMENT '上次登录ip地址',
  `loginCount` int(11) DEFAULT NULL COMMENT '登录次数',
  `status` char(255) NOT NULL COMMENT '账户状态 0:正常  1:注销',
  `email` varchar(255) DEFAULT NULL COMMENT '用户邮箱',
  `remark1` varchar(2000) DEFAULT NULL COMMENT '扩展字段1',
  `remark2` varchar(2000) DEFAULT NULL COMMENT '扩展字段2',
  PRIMARY KEY (`id`),
  KEY `index_user` (`userId`),
  KEY `index_tel` (`tel`) USING BTREE,
  KEY `index_email` (`email`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COMMENT='主键';
