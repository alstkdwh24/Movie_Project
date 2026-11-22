
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `comment_number` int NOT NULL AUTO_INCREMENT,
  `free_number` int NOT NULL,
  `username` varchar(100) DEFAULT NULL,
  `nickname` varchar(100) DEFAULT NULL,
  `comment` text,
  `comment_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`comment_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `comment_g`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment_g` (
  `comment_number` int NOT NULL AUTO_INCREMENT,
  `g_number` int NOT NULL,
  `username` varchar(100) DEFAULT NULL,
  `nickname` varchar(100) DEFAULT NULL,
  `comment` text,
  `comment_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`comment_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `comment_q`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment_q` (
  `comment_number` int NOT NULL AUTO_INCREMENT,
  `q_number` int NOT NULL,
  `username` varchar(100) DEFAULT NULL,
  `nickname` varchar(100) DEFAULT NULL,
  `comment` text,
  `comment_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`comment_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `delicious`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `delicious` (
  `delicious_number` int NOT NULL AUTO_INCREMENT,
  `delicious_name` varchar(255) DEFAULT NULL,
  `delicious_filename` varchar(255) DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `reg_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `movie_filepath` varchar(500) DEFAULT NULL,
  `resist_textarea` text,
  `uploadPaths` varchar(500) DEFAULT NULL,
  `size` bigint DEFAULT '0',
  `movie_resist_filePath` varchar(500) DEFAULT NULL,
  `filePath` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`delicious_number`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `diary_entity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `diary_entity` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `diary_id` bigint DEFAULT NULL,
  `content` text,
  `created_at` date DEFAULT NULL,
  `month` varchar(20) DEFAULT NULL,
  `image_url` varchar(500) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `question_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `event_board_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event_board_image` (
  `movie_event_image_number` int NOT NULL AUTO_INCREMENT,
  `event_name` varchar(255) DEFAULT NULL,
  `movie_filename` varchar(255) DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `reg_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `filePath` varchar(500) DEFAULT NULL,
  `resist_textarea` text,
  `uploadPaths` varchar(500) DEFAULT NULL,
  `size` bigint DEFAULT '0',
  `movie_resist_filePath` varchar(500) DEFAULT NULL,
  `movie_filepath` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`movie_event_image_number`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `free_gallery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `free_gallery` (
  `free_number` int NOT NULL AUTO_INCREMENT,
  `free_title` varchar(255) DEFAULT NULL,
  `free_contents` text,
  `username` varchar(100) DEFAULT NULL,
  `nickname` varchar(100) DEFAULT NULL,
  `free_like` int DEFAULT '0',
  `free_count` int DEFAULT '0',
  `count` int DEFAULT '0',
  `write_time` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`free_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `g_gallery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `g_gallery` (
  `g_number` int NOT NULL AUTO_INCREMENT,
  `g_title` varchar(255) DEFAULT NULL,
  `g_contents` text,
  `username` varchar(100) DEFAULT NULL,
  `nickname` varchar(100) DEFAULT NULL,
  `g_like` int DEFAULT '0',
  `g_count` int DEFAULT '0',
  `g_share_count` int DEFAULT '0',
  `count` int DEFAULT '0',
  `write_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`g_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `members` (
  `ID_number` int NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `pw` varchar(255) NOT NULL,
  `nickname` varchar(100) DEFAULT NULL,
  `namena` varchar(100) DEFAULT NULL,
  `phone` int DEFAULT NULL,
  `birthDate` date DEFAULT NULL,
  `gender` varchar(20) DEFAULT NULL,
  `job` varchar(100) DEFAULT NULL,
  `email` varchar(150) DEFAULT NULL,
  `roles` varchar(50) DEFAULT 'ROLE_1',
  PRIMARY KEY (`ID_number`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `movie_category_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movie_category_table` (
  `movie_category_number` int NOT NULL AUTO_INCREMENT,
  `id` varchar(100) DEFAULT NULL,
  `group_id` varchar(50) DEFAULT NULL,
  `movie_category_lv` int DEFAULT NULL,
  `movie_category_nm` varchar(255) DEFAULT NULL,
  `movie_detail_lv` int DEFAULT NULL,
  `movie_detail_title` varchar(255) DEFAULT NULL,
  `movie_category_parents_lv` int DEFAULT NULL,
  `movie_category_detail_parent_lv` int DEFAULT NULL,
  `movie_write_category` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`movie_category_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `movie_gifticon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movie_gifticon` (
  `Gifticon_number` int NOT NULL AUTO_INCREMENT,
  `Gifticon_name` varchar(255) DEFAULT NULL,
  `Gifticon_filename` varchar(255) DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `movie_filepath` varchar(500) DEFAULT NULL,
  `reg_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `uploadPaths` varchar(500) DEFAULT NULL,
  `size` bigint DEFAULT '0',
  `movie_resist_filePath` varchar(500) DEFAULT NULL,
  `resist_textarea` text,
  `GifticonHtml` text,
  PRIMARY KEY (`Gifticon_number`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `movie_payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movie_payment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `paymentId` varchar(255) DEFAULT NULL,
  `username` varchar(100) DEFAULT NULL,
  `movie_title` varchar(255) DEFAULT NULL,
  `movie_time` varchar(100) DEFAULT NULL,
  `movie_place` varchar(255) DEFAULT NULL,
  `movie_Seat` varchar(100) DEFAULT NULL,
  `reservation_price` varchar(50) DEFAULT NULL,
  `UserPhone` varchar(50) DEFAULT NULL,
  `accessToken` varchar(500) DEFAULT NULL,
  `payment_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `payment_time_two` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `movie_payment_paymentid`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movie_payment_paymentid` (
  `id` int NOT NULL AUTO_INCREMENT,
  `paymentId` varchar(255) DEFAULT NULL,
  `movie_title` varchar(255) DEFAULT NULL,
  `movie_time` varchar(100) DEFAULT NULL,
  `movie_place` varchar(255) DEFAULT NULL,
  `movie_Seat` varchar(100) DEFAULT NULL,
  `reservation_price` varchar(50) DEFAULT NULL,
  `payment_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `payment_time_two` varchar(100) DEFAULT NULL,
  `username` varchar(100) DEFAULT NULL,
  `UserPhone` varchar(50) DEFAULT NULL,
  `accessToken` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `movie_ticket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movie_ticket` (
  `movie_ticket_number` int NOT NULL AUTO_INCREMENT,
  `movie_ticket_name` varchar(255) DEFAULT NULL,
  `movie_ticket_filename` varchar(255) DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `reg_date` date DEFAULT NULL,
  `movie_filepath` varchar(500) DEFAULT NULL,
  `uploadPaths` varchar(500) DEFAULT NULL,
  `uploadPath` varchar(500) DEFAULT NULL,
  `filePath` varchar(500) DEFAULT NULL,
  `size` bigint DEFAULT '0',
  `resist_textarea` text,
  `movie_ticket_html` text,
  PRIMARY KEY (`movie_ticket_number`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `movie_title`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `movie_title` (
  `movie_integer` int NOT NULL AUTO_INCREMENT,
  `movie_title` varchar(255) DEFAULT NULL,
  `movie_textarea` text,
  `movie_filename` varchar(255) DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `reg_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `filePath` varchar(500) DEFAULT NULL,
  `sizes` bigint DEFAULT '0',
  `size` bigint DEFAULT '0',
  `movie_resist_filePath` varchar(500) DEFAULT NULL,
  `uploadPaths` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`movie_integer`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `paymentidvo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `paymentidvo` (
  `id` int NOT NULL AUTO_INCREMENT,
  `paymentId` varchar(255) DEFAULT NULL,
  `reservation_price` varchar(50) DEFAULT NULL,
  `username` varchar(100) DEFAULT NULL,
  `payment_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question` (
  `Q_number` int NOT NULL AUTO_INCREMENT,
  `ID_number` varchar(100) DEFAULT NULL,
  `username` varchar(100) DEFAULT NULL,
  `nickname` varchar(100) DEFAULT NULL,
  `Q_title` varchar(255) DEFAULT NULL,
  `Q_content` text,
  `Q_count` int DEFAULT '0',
  `Q_share_count` int DEFAULT '0',
  `Q_write_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`Q_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `reservation_contents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation_contents` (
  `reservation_number` int NOT NULL AUTO_INCREMENT,
  `username` varchar(100) DEFAULT NULL,
  `movie_title` varchar(255) DEFAULT NULL,
  `movie_place` varchar(255) DEFAULT NULL,
  `movie_time` varchar(100) DEFAULT NULL,
  `movie_Seat` varchar(100) DEFAULT NULL,
  `reservation_price` varchar(50) DEFAULT NULL,
  `UserPhone` varchar(50) DEFAULT NULL,
  `paymentId` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`reservation_number`),
  UNIQUE KEY `uq_reservation_seat` (`movie_title`,`movie_place`,`movie_time`,`movie_Seat`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

