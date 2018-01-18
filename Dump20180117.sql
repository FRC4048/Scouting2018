CREATE DATABASE  IF NOT EXISTS `scouting` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `scouting`;
-- MySQL dump 10.13  Distrib 5.7.9, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: scouting
-- ------------------------------------------------------
-- Server version	5.7.13-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `datatype`
--

DROP TABLE IF EXISTS `datatype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `datatype` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Type` varchar(45) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `datatype`
--

LOCK TABLES `datatype` WRITE;
/*!40000 ALTER TABLE `datatype` DISABLE KEYS */;
INSERT INTO `datatype` (`ID`, `Type`) VALUES (1,'integer'),(2,'boolean'),(3,'string'),(4,'option');
/*!40000 ALTER TABLE `datatype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item`
--

DROP TABLE IF EXISTS `item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL,
  `Active` tinyint(1) NOT NULL DEFAULT '1',
  `DATATYPE_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`,`DATATYPE_ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`),
  KEY `fk_ITEM_DATATYPE_idx` (`DATATYPE_ID`),
  CONSTRAINT `fk_ITEM_DATATYPE` FOREIGN KEY (`DATATYPE_ID`) REFERENCES `datatype` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=123 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item`
--

LOCK TABLES `item` WRITE;
/*!40000 ALTER TABLE `item` DISABLE KEYS */;
INSERT INTO `item` (`ID`, `Name`, `Active`, `DATATYPE_ID`) VALUES (1,'Present',1,2),(2,'Score',0,1),(3,'Auto: reaches over walls?',0,2),(4,'Auto: crosses outer works?',0,2),(5,'Auto: shooting?',0,2),(6,'Can shoot?',0,2),(7,'Can climb?',1,2),(8,'Rate shooting',0,1),(9,'Attempt portcullis',0,4),(10,'Attempt Cheval De Frise',0,4),(11,'Attempt Moat',0,4),(12,'Attempt Ramparts',0,4),(13,'Attempt Drawbridge',0,4),(14,'Attempt Rock Wall',0,4),(15,'Attempt Sally Port',0,4),(16,'Attempt Rough Terrain',0,4),(17,'Attempt Low Bar',0,4),(18,'Times crossed Portcullis',0,1),(19,'Times crossed Cheval De Frise',0,1),(20,'Times crossed Moat',0,1),(21,'Times crossed Ramparts',0,1),(22,'Times crossed Drawbridge',0,1),(23,'Times crossed Rock Wall',0,1),(24,'Times crossed Sally Port',0,1),(25,'Times crossed Rough Terrain',0,1),(26,'Times crossed Low Bar',0,1),(27,'Got stuck Portcullis',0,2),(28,'Got stuck Cheval De Frise',0,2),(29,'Got stuck Moat',0,2),(30,'Got stuck Ramparts',0,2),(31,'Got stuck Drawbridge',0,2),(32,'Got stuck Rock Wall',0,2),(33,'Got stuck Sally Port',0,2),(34,'Got stuck Rough Terrain',0,2),(35,'Got stuck Low Bar',0,2),(36,'Timing Portcullis',0,1),(37,'Timing  Cheval De Frise',0,1),(38,'Timing Moat',0,1),(39,'Timing  Ramparts',0,1),(40,'Timing Drawbridge',0,1),(41,'Timing Rock Wall',0,1),(42,'Timing Sally Port',0,1),(43,'Timing Rough Terrain',0,1),(44,'Comments',1,3),(45,'Timing Shooting',0,1),(46,'Drive Team Student Only?',1,2),(47,'Team Strategies',0,3),(48,'Friendliness',1,1),(49,'Speed',0,1),(50,'Height',0,1),(51,'Intake Speed',0,1),(52,'Number Of Wheels',0,1),(53,'Added Spin On Shooter?',0,2),(54,'Shooter Type',0,4),(55,'Shooter Category',0,4),(56,'Intake Type',0,4),(57,'Climber Type',0,4),(58,'Drivetrain Type',1,4),(59,'Code Language Used',1,4),(60,'Cross Portcullis On Auto?',0,2),(61,'Cross Cheval De Frise On Auto?',0,2),(62,'Cross Moat On Auto?',0,2),(63,'Cross Ramparts On Auto?',0,2),(64,'Cross Drawbridge On Auto?',0,2),(65,'Cross Rock Wall On Auto?',0,2),(66,'Cross Sally Port On Auto?',0,2),(67,'Cross Rough Terrain On Auto?',0,2),(68,'Cross Low Bar On Auto?',0,2),(69,'Cross Portcullis On Teleop?',0,2),(70,'Cross Cheval De Frise On Teleop?',0,2),(71,'Cross Moat On Teleop?',0,2),(72,'Cross Ramparts On Teleop?',0,2),(73,'Cross Drawbridge On Teleop?',0,2),(74,'Cross Rock Wall On Teleop?',0,2),(75,'Cross Sally Port On Teleop?',0,2),(76,'Cross Rough Terrain On Teleop?',0,2),(77,'Cross Low Bar On Teleop?',0,2),(78,'Description Of Robot',1,3),(79,'Rate driving',1,2),(80,'Shoots High?',1,2),(81,'Shoots Low?',1,2),(82,'Times Shot',0,1),(83,'Auto: Gear Success',0,1),(84,'Auto: Handle Gears?',1,1),(85,'Auto: Gear Success?',1,1),(86,'Auto: Gear Placement',1,4),(87,'Auto: Pilot Performance?',1,2),(88,'Auto: Shoots High?',1,1),(89,'Auto: Shoots Low?',1,1),(90,'Auto: Shot Makes',1,2),(91,'Auto: Cross Baseline?',1,1),(92,'Strategy',1,4),(93,'Shooting Speed',1,4),(94,'Shots Made',1,4),(95,'Shooter Accuracy',1,4),(96,'Handle Gears?',1,1),(97,'Pilot Performance',1,4),(98,'Gear Attempts',1,2),(99,'Gear Makes',1,2),(100,'Rotors Spinning',1,2),(101,'Climb Success?',1,1),(102,'Stays Put When Power Cut?',1,1),(103,'Climbing Speed',1,4),(104,'Did They Break Down?',1,1),(105,'Foul Points?',1,1),(106,'Yellow Card?',1,1),(107,'Red Card?',1,1),(108,'Is Robot Finished?',1,1),(109,'Gears From The Ground?',1,1),(110,'Fuel From The Ground?',1,1),(111,'Max Fuel Storage',1,2),(112,'Time To Empty Storage',1,2),(113,'Shoots Multiple Directions?',1,1),(114,'Time To Climb',1,2),(115,'Auto?',1,1),(116,'Auto: Starts Next To Key?',1,1),(117,'Auto: Starts Next To Boiler?',1,1),(118,'Auto: Starts Center?',1,1),(119,'Auto: Starts In Line Left Gear?',1,1),(120,'Auto: Starts In Line Right Gear?',1,1),(121,'Auto: Time To Shoot',1,2),(122,'Auto: Strategies',1,3);
/*!40000 ALTER TABLE `item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `option`
--

DROP TABLE IF EXISTS `option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `option` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) NOT NULL,
  `Value` tinyint(5) NOT NULL,
  `ITEM_ID` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_OPTION_ITEM1_idx` (`ITEM_ID`),
  CONSTRAINT `fk_OPTION_ITEM1` FOREIGN KEY (`ITEM_ID`) REFERENCES `item` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `option`
--

LOCK TABLES `option` WRITE;
/*!40000 ALTER TABLE `option` DISABLE KEYS */;
INSERT INTO `option` (`id`, `Name`, `Value`, `ITEM_ID`) VALUES (30,'Pneumatic',2,58),(31,'Swerve',3,58),(32,'Mecanum',1,58),(33,'Tank',0,58),(34,'Java',2,59),(35,'C++',1,59),(36,'LabVIEW',0,59),(37,'Left',-1,86),(38,'Center',0,86),(39,'Right',1,86),(40,'Gear',2,92),(41,'Fuel',1,92),(42,'Defense',0,92),(43,'Slow',0,93),(44,'Medium',1,93),(45,'Fast',2,93),(46,'0-30',0,94),(47,'31-60',1,94),(48,'61-90',2,94),(49,'90+',3,94),(50,'0-25%',0,95),(51,'26-50%',1,95),(52,'51-75%',2,95),(53,'76-100%',3,95),(54,'Good',1,97),(55,'Bad',0,97),(56,'Fast',2,103),(57,'Medium',1,103),(58,'Slow',0,103);
/*!40000 ALTER TABLE `option` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `rating_view`
--

DROP TABLE IF EXISTS `rating_view`;
/*!50001 DROP VIEW IF EXISTS `rating_view`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `rating_view` AS SELECT 
 1 AS `teamnum`,
 1 AS `Name`,
 1 AS `rating`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `record`
--

DROP TABLE IF EXISTS `record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `record` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `Value` varchar(500) DEFAULT NULL,
  `ITEM_ID` int(11) NOT NULL,
  `REPORT_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`,`ITEM_ID`,`REPORT_ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`),
  KEY `fk_RECORD_ITEM1_idx` (`ITEM_ID`),
  KEY `fk_RECORD_REPORT1_idx` (`REPORT_ID`),
  KEY `idx_value` (`Value`(3)),
  CONSTRAINT `fk_RECORD_ITEM1` FOREIGN KEY (`ITEM_ID`) REFERENCES `item` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_RECORD_REPORT1` FOREIGN KEY (`REPORT_ID`) REFERENCES `report` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `record`
--

LOCK TABLES `record` WRITE;
/*!40000 ALTER TABLE `record` DISABLE KEYS */;
/*!40000 ALTER TABLE `record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report`
--

DROP TABLE IF EXISTS `report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `FormType` tinyint(2) NOT NULL DEFAULT '1',
  `TabletNum` int(11) NOT NULL,
  `ScoutName` varchar(250) NOT NULL,
  `TeamNum` int(11) NOT NULL,
  `MatchNum` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`),
  KEY `IDX_TEAMNUMBER` (`TeamNum`),
  KEY `idx_matchNum` (`MatchNum`),
  KEY `idx_formType` (`FormType`),
  KEY `idx_scoutName` (`ScoutName`(3)),
  KEY `idx_tabletNum` (`TabletNum`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report`
--

LOCK TABLES `report` WRITE;
/*!40000 ALTER TABLE `report` DISABLE KEYS */;
/*!40000 ALTER TABLE `report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stack`
--

DROP TABLE IF EXISTS `stack`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stack` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `size` int(10) unsigned NOT NULL DEFAULT '1',
  `container` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `litter` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `coopertition` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `report_ID` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_report` (`report_ID`),
  KEY `idx_size` (`size`),
  KEY `idx_container` (`container`),
  KEY `idx_litter` (`litter`),
  KEY `idx_coop` (`coopertition`),
  CONSTRAINT `fk_report_ID` FOREIGN KEY (`report_ID`) REFERENCES `report` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stack`
--

LOCK TABLES `stack` WRITE;
/*!40000 ALTER TABLE `stack` DISABLE KEYS */;
/*!40000 ALTER TABLE `stack` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'scouting'
--
/*!50003 DROP PROCEDURE IF EXISTS `procAverages` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `procAverages`(
	IN teamnum INT(11)
)
BEGIN
SELECT i.id as ItemID, avg(r.`value`) as Average, STDDEV_SAMP(r.`value`) as StandardDeviation, COUNT(r.`value`) as SampleSize
FROM scouting.record r
join scouting.item i on (i.ID = r.ITEM_ID and i.active = 1 and i.datatype_id!= 3 and i.datatype_id != 1)
join scouting.report rpt on rpt.id = r.report_id and rpt.teamnum = teamnum
group by i.`Name`
order by i.`Name`;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `procComments` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `procComments`(
	IN teamnum INT(11)
)
BEGIN
SELECT r.`Value`
FROM scouting.record r
join scouting.item i on (i.ID = r.ITEM_ID and i.id = 44)
join scouting.report rpt on rpt.id = r.report_id and rpt.teamnum = teamnum;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `procGetAverage` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `procGetAverage`(
	IN itemName VARCHAR(50)
)
BEGIN
SELECT rpt.teamnum, avg(r.value) as results FROM scouting.record r
join scouting.item i on i.ID = r.ITEM_ID and i.Name = itemName
join scouting.report rpt on rpt.id = r.report_id and rpt.teamnum > 0
group by rpt.teamnum
order by rpt.teamnum asc;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `procInsertRecord` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `procInsertRecord`(
IN val VARCHAR(500), 
IN REPORT_ID int(11),
IN ITEM_ID int(11))
BEGIN
INSERT scouting.record(Value, REPORT_ID, ITEM_ID)
VALUES(val, REPORT_ID, ITEM_ID);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `procInsertReport` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `procInsertReport`(
	IN formType BOOL,
    IN tabletNum INT(11),
    IN scoutName VARCHAR(250),
    IN teamNum INT(11),
	IN matchNum INT(11),
    OUT id INT(11) 
)
BEGIN
INSERT INTO scouting.report (FormType, TabletNum, ScoutName, TeamNum, MatchNum)
VALUES (formType, tabletNum, scoutName, teamNum, matchNum);
SELECT LAST_INSERT_ID() INTO id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `procProportions` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `procProportions`(
	IN teamnum INT(11)
)
BEGIN
SELECT @samplesize := COUNT(report.id) FROM scouting.report WHERE report.TeamNum = teamnum AND report.FormType = 1;
SELECT i.id as ItemID, sum(r.value) as `Sum`, @samplesize as SampleSize, sum(r.value)/@samplesize*100 as SuccessRate
FROM scouting.record r
join scouting.item i on (i.ID = r.ITEM_ID and i.active = 1 and i.datatype_id = 1)
join scouting.report rpt on rpt.id = r.report_id and rpt.teamnum = teamnum
group by i.Name
order by i.Name;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Final view structure for view `rating_view`
--

/*!50001 DROP VIEW IF EXISTS `rating_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `rating_view` AS select `rpt`.`TeamNum` AS `teamnum`,`i`.`Name` AS `Name`,(2 - avg(`r`.`Value`)) AS `rating` from ((`record` `r` join `item` `i` on(((`i`.`ID` = `r`.`ITEM_ID`) and (`i`.`Name` in ('Attempt Cheval De Frise','Attempt portcullis','Attempt Rock Wall','Attempt Sally Port','Attempt Low Bar','Attempt Rough Terrain','Attempt Ramparts','Attempt Drawbridge','Attempt Moat'))))) join `report` `rpt` on(((`rpt`.`ID` = `r`.`REPORT_ID`) and (`rpt`.`TeamNum` > 0) and (`r`.`Value` < 3)))) group by `rpt`.`TeamNum`,`i`.`Name` union select `rpt`.`TeamNum` AS `teamNum`,`i`.`Name` AS `name`,avg(`r`.`Value`) AS `rating` from ((`record` `r` join `item` `i` on(((`i`.`ID` = `r`.`ITEM_ID`) and (`i`.`Name` in ('Auto: reaches over walls?','Auto: crosses outer works?','Auto: shooting?','Can shoot?','Can climb?','Rate shooting','Rate driving','Shoots High?','Shoots Low?'))))) join `report` `rpt` on((`rpt`.`ID` = `r`.`REPORT_ID`))) group by `rpt`.`TeamNum`,`i`.`Name` union select `rpt`.`TeamNum` AS `teamNum`,`i`.`Name` AS `name`,(avg(`r`.`Value`) / 10) AS `rating` from ((`record` `r` join `item` `i` on(((`i`.`ID` = `r`.`ITEM_ID`) and (`i`.`Name` in ('Times Crossed Cheval De Frise','Times Crossed portcullis','Times Crossed Rock Wall','Times Crossed Sally Port','Times Crossed Low Bar','Times Crossed Rough Terrain','Times Crossed Ramparts','Times Crossed Drawbridge','Times Crossed Moat')) and (`r`.`Value` > 0)))) join `report` `rpt` on((`rpt`.`ID` = `r`.`REPORT_ID`))) group by `rpt`.`TeamNum`,`i`.`Name` union select `rpt`.`TeamNum` AS `teamNum`,`i`.`Name` AS `name`,-(avg(`r`.`Value`)) AS `rating` from ((`record` `r` join `item` `i` on(((`i`.`ID` = `r`.`ITEM_ID`) and (`i`.`Name` in ('Got Stuck Cheval De Frise','Got Stuck portcullis','Got Stuck Rock Wall','Got Stuck Sally Port','Got Stuck Low Bar','Got Stuck Rough Terrain','Got Stuck Ramparts','Got Stuck Drawbridge','Got Stuck Moat')) and (`r`.`Value` > 0)))) join `report` `rpt` on((`rpt`.`ID` = `r`.`REPORT_ID`))) group by `rpt`.`TeamNum`,`i`.`Name` order by `teamnum`,`Name` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-01-17 19:32:22
