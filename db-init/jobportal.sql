-- MySQL dump 10.13  Distrib 9.2.0, for macos15 (arm64)
--
-- Host: localhost    Database: job-portal
-- ------------------------------------------------------
-- Server version	9.2.0

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

--
-- Table structure for table `APPLICATIONS`
--

DROP TABLE IF EXISTS `APPLICATIONS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `APPLICATIONS` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `job_id` int DEFAULT NULL,
  `resume_url` text NOT NULL,
  `STATUS` enum('PENDING','ACCEPTED','REJECTED','WITHDRAWN') DEFAULT NULL,
  `applied_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `job_id` (`job_id`),
  KEY `fk_application_user` (`user_id`),
  CONSTRAINT `applications_ibfk_2` FOREIGN KEY (`job_id`) REFERENCES `jobs` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_application_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `APPLICATIONS`
--

LOCK TABLES `APPLICATIONS` WRITE;
/*!40000 ALTER TABLE `APPLICATIONS` DISABLE KEYS */;
INSERT INTO `APPLICATIONS` VALUES (10,4,7,'https://job-portal-s3-v1.s3.ap-southeast-1.amazonaws.com/100423_Screenshot%202025-04-23%20at%209.07.47%E2%80%AFPM.png','PENDING','2025-06-25 14:14:18','2025-06-26 03:35:20'),(11,6,7,'https://job-portal-s3-v1.s3.ap-southeast-1.amazonaws.com/100993_Screenshot%202025-04-23%20at%209.07.47%E2%80%AFPM.png','PENDING','2025-06-25 14:15:39','2025-06-26 03:35:20'),(12,4,9,'https://job-portal-s3-v1.s3.ap-southeast-1.amazonaws.com/100423_Screenshot%202025-04-23%20at%209.07.47%E2%80%AFPM.png','WITHDRAWN','2025-06-26 03:01:35','2025-06-26 03:42:44'),(13,4,11,'https://job-portal-s3-v1.s3.ap-southeast-1.amazonaws.com/100423_Screenshot%202025-04-23%20at%209.07.47%E2%80%AFPM.png','PENDING','2025-06-26 03:01:41','2025-06-26 03:35:20'),(14,6,11,'https://job-portal-s3-v1.s3.ap-southeast-1.amazonaws.com/100993_Screenshot%202025-04-23%20at%209.07.47%E2%80%AFPM.png','ACCEPTED','2025-06-27 14:30:06','2025-06-27 14:30:06'),(15,6,12,'https://job-portal-s3-v1.s3.ap-southeast-1.amazonaws.com/100993_Screenshot%202025-04-23%20at%209.07.47%E2%80%AFPM.png','PENDING','2025-06-26 03:41:49','2025-06-26 03:41:49');
/*!40000 ALTER TABLE `APPLICATIONS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bookmarks`
--

DROP TABLE IF EXISTS `bookmarks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookmarks` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `job_id` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_bookmark` (`user_id`,`job_id`),
  KEY `job_id` (`job_id`),
  CONSTRAINT `bookmarks_ibfk_2` FOREIGN KEY (`job_id`) REFERENCES `jobs` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_bookmarks_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookmarks`
--

LOCK TABLES `bookmarks` WRITE;
/*!40000 ALTER TABLE `bookmarks` DISABLE KEYS */;
INSERT INTO `bookmarks` VALUES (2,4,6,'2025-06-30 03:30:14'),(3,4,7,'2025-06-30 04:21:27'),(4,4,10,'2025-06-30 04:21:31');
/*!40000 ALTER TABLE `bookmarks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jobs`
--

DROP TABLE IF EXISTS `jobs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `jobs` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `description` text NOT NULL,
  `location` varchar(100) DEFAULT NULL,
  `company_name` varchar(100) DEFAULT NULL,
  `salary` decimal(10,2) DEFAULT NULL,
  `job_type` varchar(50) DEFAULT NULL,
  `job_status` tinyint(1) DEFAULT NULL,
  `posted_by` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_jobs_posted_by` (`posted_by`),
  CONSTRAINT `fk_jobs_posted_by` FOREIGN KEY (`posted_by`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jobs`
--

LOCK TABLES `jobs` WRITE;
/*!40000 ALTER TABLE `jobs` DISABLE KEYS */;
INSERT INTO `jobs` VALUES (5,'Data Analyst','Analyze business data to uncover trends and insights.','Sabah','InsightWorks Analytics',10000.00,'Contract',1,2,'2025-06-21 08:49:14','2025-06-24 02:57:37'),(6,'Cloud Engineer','Manage and optimize cloud infrastructure and deployments.','Kuala Lumpur','NimbusNet',9000.00,'Remote',1,2,'2025-06-21 08:49:14','2025-06-24 02:57:37'),(7,'Frontend Web Developer','Build responsive and user-friendly web interfaces using React.js.','Penang','PixelForge Studio',7500.00,'Full-time',1,3,'2025-06-21 13:12:39','2025-06-24 02:57:37'),(8,'UX/UI Designer','Design intuitive user experiences and interfaces for mobile and web platforms.','Petaling Jaya','DesignCraft Co.',6800.00,'Part-time',1,3,'2025-06-21 13:12:39','2025-06-24 02:57:37'),(9,'DevOps Engineer','Automate CI/CD pipelines and manage infrastructure-as-code deployments.','Shah Alam','AutoDeploy Solutions',9500.00,'Hybrid',1,3,'2025-06-21 13:12:39','2025-06-24 02:57:37'),(10,'Backend Developer','Develop scalable RESTful APIs and integrate with third-party services using Node.js.','Kuala Lumpur','CodeNexus Labs',8200.00,'Full-time',1,3,'2025-06-24 03:00:37','2025-06-24 03:00:37'),(11,'Mobile App Developer','Create high-performance Android and iOS applications using Flutter.','Cyberjaya','AppVenture Tech',7200.00,'Contract',1,3,'2025-06-24 03:00:37','2025-06-24 03:00:37'),(12,'QA Automation Engineer','Design and execute automated tests to ensure software quality and reliability.','Johor Bahru','TestFlow Solutions',6700.00,'Remote',0,3,'2025-06-24 03:00:37','2025-06-24 03:25:02'),(13,'DevOps Engineer','Automate CI/CD pipelines and manage cloud infrastructure on AWS. Ensure system reliability and scalability.','Cyberjaya','CloudStride Technologies',9000.00,'Contract',1,2,'2025-08-08 03:36:57','2025-08-08 03:36:57'),(14,'QA Automation Engineer','Design and implement automated test scripts using Selenium and Postman. Ensure software quality across releases','Penang','TestSphere Labs',7700.00,'Full-Time',0,2,'2025-08-08 08:30:30','2025-08-08 08:30:30'),(15,'Cloud Architect','Lead cloud infrastructure design on AWS and Azure. Ensure scalability, security, and cost optimization.','Kuala Lumpur','SkyBridge Technologies',25000.00,'Contract',1,3,'2025-08-08 08:35:42','2025-08-08 08:35:42');
/*!40000 ALTER TABLE `jobs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` text NOT NULL,
  `role` enum('ADMIN','EMPLOYER','JOB_SEEKER') NOT NULL,
  `status` enum('ACTIVE','INACTIVE') NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `user_id_2` (`user_id`),
  UNIQUE KEY `user_id_3` (`user_id`),
  UNIQUE KEY `user_id_4` (`user_id`),
  UNIQUE KEY `user_id_5` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'100752','Itachi','admin@jobportal.co','$2a$10$k8fuKyHKAZHkj8WgqiLiU.uKI.9q5I2dtLMRnCPMd/1UDC5JpbCmm','ADMIN','ACTIVE',NULL,'2025-06-25 11:45:31'),(2,'100234','TechNova HR','hr@technova.com','$2a$10$e5CHPh8FdDkQsgwaXgrcguaw8j2To4BIJXlbcTwq7/7heJKbOTxr.','EMPLOYER','ACTIVE',NULL,'2025-06-25 11:45:31'),(3,'100982','UIWorks Talent','talent@uiworks.io','$2a$10$SUlNxgwFjx17mNuqvv1bJub9a1.Gt7ScYVPaLF0kcrjqqkPz4pDre','EMPLOYER','ACTIVE',NULL,'2025-06-25 11:45:31'),(4,'100423','Konohamaru','john@example.com','$2a$10$Oq5WF84T779vrlAB3/bF3O.UDXLzefeLkspLYung4E4dtvl2MB6tW','JOB_SEEKER','INACTIVE',NULL,'2025-06-25 11:45:31'),(6,'100993','Miles Morales','miles.@example.com','$2a$10$5MTT8wkkgRz8n/.NW1hNX.Lm7gPoHjNl7jTfw9Im9G0onnu08YUBG','JOB_SEEKER','ACTIVE','2025-06-18 03:20:48','2025-06-25 11:45:31'),(7,'121205','David','david@example.com','$2a$10$a33/xX8KT3qN5Pg0JC/zGeXiyhNHteI8bco6.avbqxGsLUU7YcgBq','JOB_SEEKER','ACTIVE','2025-08-05 08:56:58','2025-08-05 08:56:58'),(8,'266631','Rush','rush@example.com','$2a$10$CM..bAPhE41yPcOtnaoUL.wCZhW1iei6Pg2DrTcE.NGQicAmYvXTO','EMPLOYER','ACTIVE','2025-08-05 11:03:45','2025-08-05 11:03:45');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-01 14:24:34
