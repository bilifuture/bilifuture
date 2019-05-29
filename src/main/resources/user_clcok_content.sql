/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50540
Source Host           : localhost:3306
Source Database       : bilifuture

Target Server Type    : MYSQL
Target Server Version : 50540
File Encoding         : 65001

Date: 2019-05-29 10:22:52
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user_clcok_content
-- ----------------------------
DROP TABLE IF EXISTS `user_clcok_content`;
CREATE TABLE `user_clcok_content` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userId` varchar(255) NOT NULL COMMENT '用户id',
  `content` text COMMENT '打卡内容',
  `clockType` char(1) NOT NULL COMMENT '打卡类型   1--打开开始clockIn  2---打卡结束clockOut',
  `time` datetime NOT NULL COMMENT '打卡时间',
  PRIMARY KEY (`id`),
  KEY `index_userId` (`userId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8;
