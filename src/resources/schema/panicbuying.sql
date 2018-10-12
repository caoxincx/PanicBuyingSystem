/*
SQLyog 企业版 - MySQL GUI v8.14 
MySQL - 5.7.20-log : Database - panicbuyingsystem_db
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`panicbuyingsystem_db` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `panicbuyingsystem_db`;

/*Table structure for table `goods` */

DROP TABLE IF EXISTS `goods`;

CREATE TABLE `goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goods_name` varchar(16) DEFAULT NULL,
  `goods_title` varchar(64) DEFAULT NULL,
  `goods_img` varchar(64) DEFAULT NULL,
  `goods_detail` longtext,
  `goods_price` decimal(10,2) DEFAULT NULL,
  `goods_stock` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `goods` */

insert  into `goods`(`id`,`goods_name`,`goods_title`,`goods_img`,`goods_detail`,`goods_price`,`goods_stock`) values (1,'iphonex','APPLE iPhone X(A1865) 64g','/img/iphonex.png','iphonex 1111111111111111111111\r\n','8765.00',100),(2,'华为Mate9','华为Mate9 4gb + 32G','/img/meta10.png','华为mate9 0000000000000000000000000000000','3212.00',30);

/*Table structure for table `order_info` */

DROP TABLE IF EXISTS `order_info`;

CREATE TABLE `order_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `goods_id` bigint(20) DEFAULT NULL,
  `delivery_addr_id` bigint(20) DEFAULT NULL,
  `goods_name` varchar(16) DEFAULT NULL,
  `goods_count` int(11) DEFAULT '0',
  `goods_price` decimal(10,2) DEFAULT '0.00',
  `status` tinyint(4) DEFAULT '0',
  `create_date` datetime DEFAULT NULL,
  `pay_date` datetime DEFAULT NULL,
  `order_channel` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `order_info` */

insert  into `order_info`(`id`,`user_id`,`goods_id`,`delivery_addr_id`,`goods_name`,`goods_count`,`goods_price`,`status`,`create_date`,`pay_date`,`order_channel`) values (1,18320338949,1,NULL,'iphonex',1,'8765.00',0,'2018-09-09 11:55:44',NULL,1),(2,18320338949,2,NULL,'华为Mate9',1,'3212.00',0,'2018-09-09 22:57:42',NULL,1);

/*Table structure for table `pbs_goods` */

DROP TABLE IF EXISTS `pbs_goods`;

CREATE TABLE `pbs_goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goods_id` bigint(20) DEFAULT NULL,
  `pb_price` decimal(10,2) DEFAULT NULL,
  `stock_count` int(11) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `pbs_goods` */

insert  into `pbs_goods`(`id`,`goods_id`,`pb_price`,`stock_count`,`start_date`,`end_date`) values (1,1,'0.01',4,'2018-09-01 00:01:00','2018-09-30 00:01:00'),(2,2,'0.01',9,'2018-09-01 00:01:00','2018-09-30 00:01:00');

/*Table structure for table `pbs_order_info` */

DROP TABLE IF EXISTS `pbs_order_info`;

CREATE TABLE `pbs_order_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `goods_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `pbs_order_info` */

insert  into `pbs_order_info`(`id`,`user_id`,`order_id`,`goods_id`) values (1,18320338949,1,1),(2,18320338949,1,2);

/*Table structure for table `pbs_user` */

DROP TABLE IF EXISTS `pbs_user`;

CREATE TABLE `pbs_user` (
  `id` bigint(20) NOT NULL,
  `nickname` varchar(255) NOT NULL,
  `password` varchar(32) DEFAULT NULL,
  `salt` varchar(10) DEFAULT NULL,
  `head` varchar(128) DEFAULT NULL,
  `register_date` datetime DEFAULT NULL,
  `last_login_date` datetime DEFAULT NULL,
  `login_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `pbs_user` */

insert  into `pbs_user`(`id`,`nickname`,`password`,`salt`,`head`,`register_date`,`last_login_date`,`login_count`) values (18320338949,'曹小欣','672c7ad0c16d5095badf1a03073428f8','a1&2#3d%q',NULL,'2018-09-08 00:00:00','2018-09-08 00:00:00',1);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
