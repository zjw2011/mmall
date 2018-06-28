CREATE DATABASE IF NOT EXISTS orders DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS=0;
use orders;

DROP TABLE IF EXISTS `order_record`;
CREATE TABLE `order_record`(
  `id` BIGINT NOT NULL COMMENT '主键/订单号',
  `order_status` TINYINT NOT NULL COMMENT '订单状态: 0未确认 1已确认 2已取消 3无效 4退款',
  `pay_status` TINYINT NOT NULL COMMENT '订单状态: 0未支付 1 支付中 2已支付',
  `createtime` TIMESTAMP NOT NULL COMMENT '创建时间',
  `updatetime` TIMESTAMP NOT NULL COMMENT '更新时间',
  `rowversion` BIGINT NOT NULL COMMENT '行版本号',
 PRIMARY KEY ( `id` )
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '订单流水表';

SET FOREIGN_KEY_CHECKS=1;