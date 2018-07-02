CREATE DATABASE IF NOT EXISTS orders DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS=0;
use orders;

DROP TABLE IF EXISTS `order_record`;
CREATE TABLE `order_record`(
  `id` BIGINT NOT NULL COMMENT '主键/订单号',
  `order_status` TINYINT NOT NULL COMMENT '订单状态: 0未确认 1已确认 2已取消 3无效 4退款',
  `pay_status` TINYINT NOT NULL COMMENT '订单状态: 0未支付 1 支付中 2已支付',
  `data_status` TINYINT NOT NULL COMMENT '订单状态: 0禁用 1 启用 99删除',
  `createtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updatetime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `rowversion` BIGINT NOT NULL COMMENT '行版本号',
  PRIMARY KEY ( `id` )
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '订单流水表';


-- 业务ID自动生成 buf min max max * 0.2 if(curr == max) { 需要更新 需要加锁; }
-- buf min max 是否更新

-- 以后肯定要拆开
DROP TABLE IF EXISTS `leaf_segment`;
CREATE TABLE `leaf_segment`(
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `biz_tag` VARCHAR(128) NOT NULL COMMENT '业务标识',
  `max_id` BIGINT(20) NOT NULL COMMENT '最大ID',
  `step` INT(11) NOT NULL COMMENT '最大ID',
  `load_factor` TINYINT(2) NOT NULL COMMENT '加载系数,30',
  `data_status` TINYINT NOT NULL COMMENT '订单状态: 0禁用 1 启用 99删除',
  `createtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updatetime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `rowversion` BIGINT NOT NULL COMMENT '行版本号',
  UNIQUE KEY `leaf_segment_index` (`biz_tag`),
  PRIMARY KEY ( `id` )
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT 'ID分段表';

DROP TABLE IF EXISTS `test_id`;
CREATE TABLE `test_id`(
  `id` BIGINT NOT NULL COMMENT '主键',
  `createtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY ( `id` )
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '测试id';

