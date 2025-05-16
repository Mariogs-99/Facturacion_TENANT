-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: master
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `int_lock`
--

DROP TABLE IF EXISTS `int_lock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `int_lock` (
  `REGION` varchar(100) NOT NULL,
  `LOCK_KEY` varchar(100) NOT NULL,
  `CLIENT_ID` varchar(100) DEFAULT NULL,
  `CREATED_DATE` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`REGION`,`LOCK_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `int_lock`
--

LOCK TABLES `int_lock` WRITE;
/*!40000 ALTER TABLE `int_lock` DISABLE KEYS */;
/*!40000 ALTER TABLE `int_lock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `master_company`
--

DROP TABLE IF EXISTS `master_company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `master_company` (
  `company_id` bigint NOT NULL AUTO_INCREMENT,
  `name_company` varchar(150) DEFAULT NULL,
  `address_company` varchar(250) DEFAULT NULL,
  `phone_company` varchar(100) DEFAULT NULL,
  `email_company` varchar(150) DEFAULT NULL,
  `description_company` varchar(250) DEFAULT NULL,
  `social_reason_company` varchar(250) DEFAULT NULL,
  `nit` varchar(25) DEFAULT NULL,
  `nrc` varchar(25) DEFAULT NULL,
  `num_id` varchar(50) DEFAULT NULL,
  `mh_user` varchar(100) DEFAULT NULL,
  `mh_pass` varchar(100) DEFAULT NULL,
  `database_url` varchar(255) DEFAULT NULL,
  `database_username` varchar(100) DEFAULT NULL,
  `database_password` varchar(100) DEFAULT NULL,
  `active` bit(1) DEFAULT b'1',
  `new_company` tinyint DEFAULT '0',
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `subscription_until` datetime DEFAULT NULL,
  PRIMARY KEY (`company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `master_company`
--

LOCK TABLES `master_company` WRITE;
/*!40000 ALTER TABLE `master_company` DISABLE KEYS */;
INSERT INTO `master_company` VALUES (1,'Infologic S.A. de C.V.','Col. Escalón, San Salvador','2250-1234','info@infologic.com','Empresa de software y soluciones fiscales','INFLOGIC','0614-030806-102-5','123456-7','JGM010101AE1','mhuser_infologic','mhpass123','jdbc:mysql://localhost:3306/infologic','root','123456',_binary '',0,NULL,NULL,NULL),(2,'Global Solutions S.A de C.V','Av. Siempre Viva 742','2222-0000','demo@empresa.com','Empresa de prueba para testing','EMPRESA DEMO','0614-290122-002-0','987654-3','DEM020202BE2','mhuser_demo','mhpass456','jdbc:mysql://localhost:3306/global','root','123456',_binary '',0,NULL,NULL,NULL),(28,'Wallas','calle las lomas',NULL,'correo@corre.com',NULL,NULL,'1111-111111-111-1','2131231',NULL,NULL,NULL,'jdbc:mysql://localhost:3306/empresa_WA1795','WA1795','lgApTF?U!d62',_binary '',1,'2025-05-16 11:05:06.209137','2025-05-16 11:05:06.209137',NULL);
/*!40000 ALTER TABLE `master_company` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `master_user`
--

DROP TABLE IF EXISTS `master_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `master_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `usuario` varchar(50) NOT NULL,
  `clave` varchar(255) NOT NULL,
  `company_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `usuario` (`usuario`),
  KEY `company_id` (`company_id`),
  CONSTRAINT `master_user_ibfk_1` FOREIGN KEY (`company_id`) REFERENCES `master_company` (`company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `master_user`
--

LOCK TABLES `master_user` WRITE;
/*!40000 ALTER TABLE `master_user` DISABLE KEYS */;
INSERT INTO `master_user` VALUES (1,'GM3879','$2a$10$/7xE2NMxjQTShKkylwAKEuQZ8/c1vpcp3jC8XRXXxVyU4znLmvtbG',1),(2,'GM2025','$2a$10$/7xE2NMxjQTShKkylwAKEuQZ8/c1vpcp3jC8XRXXxVyU4znLmvtbG',2),(28,'WA1795','$2a$10$3/5ZALF0Cnb//4Qv5TVJDe.3dTYVnFiKyPsj3a.HTCKf61ZjqtkWq',28);
/*!40000 ALTER TABLE `master_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'master'
--

--
-- Dumping routines for database 'master'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-16 11:50:44
