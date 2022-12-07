/*
SQLyog Ultimate v10.00 Beta1
MySQL - 8.0.27 : Database - dashboard
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`dashboard` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `dashboard`;

/*Table structure for table `cluster` */

DROP TABLE IF EXISTS `cluster`;

CREATE TABLE `cluster` (
  `cluster_id` int NOT NULL,
  `is_healthy` tinyint DEFAULT '1',
  `used_space` int DEFAULT '0',
  `total_space` int DEFAULT '0',
  `create_time` date DEFAULT NULL,
  `update_time` date DEFAULT NULL,
  PRIMARY KEY (`cluster_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `cluster` */

insert  into `cluster`(`cluster_id`,`is_healthy`,`used_space`,`total_space`,`create_time`,`update_time`) values (0,1,0,0,'2022-11-17','2022-11-20'),(1,1,0,0,'2022-11-17','2022-11-20'),(2,1,0,0,'2022-11-17','2022-11-20');

/*Table structure for table `node` */

DROP TABLE IF EXISTS `node`;

CREATE TABLE `node` (
  `node_id` int NOT NULL,
  `cluster_id` int DEFAULT NULL,
  `node_name` varchar(50) DEFAULT NULL,
  `is_healthy` tinyint DEFAULT '1',
  `is_master` tinyint DEFAULT '0',
  `used_space` int DEFAULT '0',
  `total_space` int DEFAULT '10000',
  `create_time` date DEFAULT NULL,
  `update_time` date DEFAULT NULL,
  PRIMARY KEY (`node_id`),
  KEY `cluster_id` (`cluster_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `node` */

insert  into `node`(`node_id`,`cluster_id`,`node_name`,`is_healthy`,`is_master`,`used_space`,`total_space`,`create_time`,`update_time`) values (0,0,'ipfs0',1,1,0,10000,'2022-11-17','2022-11-20'),(1,1,'ipfs1',1,1,0,10000,'2022-11-17','2022-11-20'),(2,2,'ipfs2',1,1,0,10000,'2022-11-17','2022-11-20');

/*Table structure for table `website` */

DROP TABLE IF EXISTS `website`;

CREATE TABLE `website` (
  `website_id` int NOT NULL AUTO_INCREMENT,
  `content_identifier` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `create_time` date DEFAULT NULL,
  `update_time` date DEFAULT NULL,
  PRIMARY KEY (`website_id`),
  UNIQUE KEY `content_identifier` (`content_identifier`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `website` */

insert  into `website`(`website_id`,`content_identifier`,`create_time`,`update_time`) values (1,'QmevHYpfvatpQ7rYAGb5mCxHZDsCELk1seBUr3Hfu97H14','2022-11-17','2022-11-20');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
