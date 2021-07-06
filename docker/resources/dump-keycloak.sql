-- MySQL dump 10.13  Distrib 5.5.62, for Win64 (AMD64)
--
-- Host: localhost    Database: keycloak
-- ------------------------------------------------------
-- Server version	5.5.5-10.1.19-MariaDB-1~jessie

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ADMIN_EVENT_ENTITY`
--

DROP TABLE IF EXISTS `ADMIN_EVENT_ENTITY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ADMIN_EVENT_ENTITY` (
  `ID` varchar(36) NOT NULL,
  `ADMIN_EVENT_TIME` bigint(20) DEFAULT NULL,
  `REALM_ID` varchar(255) DEFAULT NULL,
  `OPERATION_TYPE` varchar(255) DEFAULT NULL,
  `AUTH_REALM_ID` varchar(255) DEFAULT NULL,
  `AUTH_CLIENT_ID` varchar(255) DEFAULT NULL,
  `AUTH_USER_ID` varchar(255) DEFAULT NULL,
  `IP_ADDRESS` varchar(255) DEFAULT NULL,
  `RESOURCE_PATH` varchar(2550) DEFAULT NULL,
  `REPRESENTATION` text,
  `ERROR` varchar(255) DEFAULT NULL,
  `RESOURCE_TYPE` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ADMIN_EVENT_ENTITY`
--

LOCK TABLES `ADMIN_EVENT_ENTITY` WRITE;
/*!40000 ALTER TABLE `ADMIN_EVENT_ENTITY` DISABLE KEYS */;
INSERT INTO `ADMIN_EVENT_ENTITY` VALUES ('10dd86b0-e975-4dbc-af9b-c89295418b14',1621350042000,'liferay-portal','DELETE','master','d6d25c43-45e4-4107-89b4-7fc91bca0f80','5544a352-9524-44ad-8617-6ed1e77dcb8e','172.23.0.1','users/63919307-da21-426a-a43a-4cf9cd60f12a',NULL,NULL,'USER'),('4b5b7f57-2994-425c-9589-cf934d83733b',1621350040000,'liferay-portal','DELETE','master','d6d25c43-45e4-4107-89b4-7fc91bca0f80','5544a352-9524-44ad-8617-6ed1e77dcb8e','172.23.0.1','users/b1709578-5d32-41f9-937c-197a5fbd183e',NULL,NULL,'USER');
/*!40000 ALTER TABLE `ADMIN_EVENT_ENTITY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ASSOCIATED_POLICY`
--

DROP TABLE IF EXISTS `ASSOCIATED_POLICY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ASSOCIATED_POLICY` (
  `POLICY_ID` varchar(36) NOT NULL,
  `ASSOCIATED_POLICY_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`POLICY_ID`,`ASSOCIATED_POLICY_ID`),
  KEY `IDX_ASSOC_POL_ASSOC_POL_ID` (`ASSOCIATED_POLICY_ID`),
  CONSTRAINT `FK_FRSR5S213XCX4WNKOG82SSRFY` FOREIGN KEY (`ASSOCIATED_POLICY_ID`) REFERENCES `RESOURCE_SERVER_POLICY` (`ID`),
  CONSTRAINT `FK_FRSRPAS14XCX4WNKOG82SSRFY` FOREIGN KEY (`POLICY_ID`) REFERENCES `RESOURCE_SERVER_POLICY` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ASSOCIATED_POLICY`
--

LOCK TABLES `ASSOCIATED_POLICY` WRITE;
/*!40000 ALTER TABLE `ASSOCIATED_POLICY` DISABLE KEYS */;
INSERT INTO `ASSOCIATED_POLICY` VALUES ('1d1be5b2-368b-4f73-91ef-f5fff682aa73','eca0e811-43db-41f8-a3b9-5d37f235e1b3'),('464a4f94-3ebe-4b25-a773-43004e2e73d6','8119b5a3-d21e-4c4b-bac8-134c495e0a77'),('d66a85ba-62e8-49ed-b0cc-29ab865a86e5','7e75f663-76ca-42cc-92ce-732e3caf409a');
/*!40000 ALTER TABLE `ASSOCIATED_POLICY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AUTHENTICATION_EXECUTION`
--

DROP TABLE IF EXISTS `AUTHENTICATION_EXECUTION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AUTHENTICATION_EXECUTION` (
  `ID` varchar(36) NOT NULL,
  `ALIAS` varchar(255) DEFAULT NULL,
  `AUTHENTICATOR` varchar(36) DEFAULT NULL,
  `REALM_ID` varchar(36) DEFAULT NULL,
  `FLOW_ID` varchar(36) DEFAULT NULL,
  `REQUIREMENT` int(11) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `AUTHENTICATOR_FLOW` bit(1) NOT NULL DEFAULT b'0',
  `AUTH_FLOW_ID` varchar(36) DEFAULT NULL,
  `AUTH_CONFIG` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_AUTH_EXEC_REALM_FLOW` (`REALM_ID`,`FLOW_ID`),
  KEY `IDX_AUTH_EXEC_FLOW` (`FLOW_ID`),
  CONSTRAINT `FK_AUTH_EXEC_FLOW` FOREIGN KEY (`FLOW_ID`) REFERENCES `AUTHENTICATION_FLOW` (`ID`),
  CONSTRAINT `FK_AUTH_EXEC_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AUTHENTICATION_EXECUTION`
--

LOCK TABLES `AUTHENTICATION_EXECUTION` WRITE;
/*!40000 ALTER TABLE `AUTHENTICATION_EXECUTION` DISABLE KEYS */;
INSERT INTO `AUTHENTICATION_EXECUTION` VALUES ('0724405c-e735-4f05-aea2-166bbfed173e',NULL,'auth-cookie','liferay-portal','c209b812-1e70-41f3-97cd-6cff5b7550b0',2,10,'\0',NULL,NULL),('0861edbe-d4d8-4066-b1e3-ad1c6870ec20',NULL,'reset-credentials-choose-user','liferay-portal','77ffbc63-aaaf-4d0d-8108-8dd6e890ca7b',0,10,'\0',NULL,NULL),('0bd59152-7ffa-473d-8c82-7603c98474a8',NULL,'auth-username-password-form','master','22c03d7d-a755-4a8f-bdb2-9f16a827aae0',0,10,'\0',NULL,NULL),('17a4737b-67ff-4f8d-b012-34d7d74c563f',NULL,'no-cookie-redirect','master','afabf1c5-a49b-4462-9db1-395dc3214ad5',0,10,'\0',NULL,NULL),('18160280-9ece-4241-9ec5-6c24a35e483f',NULL,'registration-page-form','master','3dc56f89-e689-4131-860e-566332cf39bb',0,10,'','dea50c9a-b9bb-4230-b46d-6fa1a543ee2d',NULL),('1a7d6592-4ccc-494f-81f1-991c52c8bfb4',NULL,'registration-password-action','liferay-portal','6c3d060d-6393-4fea-b2a8-72b644fd9927',0,61,'\0',NULL,NULL),('1d7aab81-97f4-44ea-bfbc-b3a8d086e837',NULL,'idp-confirm-link','liferay-portal','c3bdfa10-03f9-474c-b9a7-4a3f4a4a0f5f',0,10,'\0',NULL,NULL),('1eb506d5-7d51-47a2-bd95-baf7fe3e8d8e',NULL,NULL,'master','cf66ba4c-bc58-4659-a550-c7a2dc0ed9a2',2,30,'','1d4a340b-8aa4-43a4-a151-979466a3cbd5',NULL),('234b8c7f-f594-4b43-9f3d-3e3c06efc02d',NULL,'auth-otp-form','liferay-portal','fa6eb2a4-7f51-4749-aebc-c6d095d31cbc',1,20,'\0',NULL,NULL),('24ef4706-0cab-4aea-946f-36ee477aec82',NULL,'registration-password-action','liferay-portal','94a62ca6-01e9-47b1-9d28-5872a33195d5',0,50,'\0',NULL,NULL),('2695289f-b2b9-4e95-a0ca-5361cdbeda36',NULL,'auth-cookie','master','7c50a617-e202-46ec-b3aa-6ae1bf549129',2,10,'\0',NULL,NULL),('2719425c-8538-4358-b4eb-9c9b318d5c66',NULL,'idp-confirm-link','master','cf66ba4c-bc58-4659-a550-c7a2dc0ed9a2',0,10,'\0',NULL,NULL),('27ad0136-c8c1-449f-8560-5e909475f5be',NULL,'registration-password-action','master','dea50c9a-b9bb-4230-b46d-6fa1a543ee2d',0,50,'\0',NULL,NULL),('2811a6c3-33f2-4505-a3fd-5f44c55c711e',NULL,'registration-page-form','liferay-portal','b8389120-a7d5-4ae2-9fb2-c75025808d84',0,10,'','94a62ca6-01e9-47b1-9d28-5872a33195d5',NULL),('29d1e077-b02d-4ade-88d2-6ef0ea0c2077',NULL,'auth-spnego','master','7c50a617-e202-46ec-b3aa-6ae1bf549129',3,20,'\0',NULL,NULL),('2b3030bc-5323-47cf-92dc-27e0d976acaf',NULL,'idp-username-password-form','liferay-portal','fa6eb2a4-7f51-4749-aebc-c6d095d31cbc',0,10,'\0',NULL,NULL),('2ca627bd-bd29-4377-b94e-945081b80236',NULL,'auth-username-password-form','liferay-portal','27bde0c2-35f0-4a8b-b0df-16f4c1fb22e0',0,10,'\0',NULL,NULL),('36509933-6c73-430b-a4e4-d52887cf9a50',NULL,'client-x509','master','d45fb920-2a72-4874-95d6-204465be333b',2,40,'\0',NULL,NULL),('36dabb64-5bfa-4075-98e9-05eadb9176c9',NULL,'registration-user-creation','liferay-portal','6c3d060d-6393-4fea-b2a8-72b644fd9927',0,50,'\0',NULL,NULL),('3724cef1-1707-47a8-bb4c-996ab403653a',NULL,'reset-otp','master','ca05262f-631f-4ddf-8c8c-03f11a4c6508',1,40,'\0',NULL,NULL),('3a379d8c-e2d8-4fbd-9102-5026a2e15c9c',NULL,'direct-grant-validate-password','master','75250d37-7175-4d91-a36b-82a751d2fdbe',0,20,'\0',NULL,NULL),('3ae61489-29ca-453d-8039-d67f30e16482',NULL,'auth-otp-form','master','1d4a340b-8aa4-43a4-a151-979466a3cbd5',1,20,'\0',NULL,NULL),('3b5442db-f3db-473c-8436-a82272befa8e',NULL,'basic-auth-otp','master','afabf1c5-a49b-4462-9db1-395dc3214ad5',3,30,'\0',NULL,NULL),('3ecc4f77-0cf3-4c03-b292-e8a6505b32af',NULL,'auth-cookie','liferay-portal','b560f942-5754-4d3a-ab48-127a624faa0d',2,10,'\0',NULL,NULL),('47026b65-2277-40c9-a47a-00ae08a9c547',NULL,NULL,'liferay-portal','c3bdfa10-03f9-474c-b9a7-4a3f4a4a0f5f',2,30,'','fa6eb2a4-7f51-4749-aebc-c6d095d31cbc',NULL),('49470b68-2911-46f2-ae79-8ec47087a725',NULL,'idp-create-user-if-unique','liferay-portal','fbd304fa-b190-475b-bf02-139776d5e1a0',2,20,'\0',NULL,'e43d487c-3712-47a8-9243-2d3684942de3'),('49a8d97f-5854-4a31-9ff7-1f427c04c6a3',NULL,NULL,'master','dff1a7dd-1d0c-4e5f-abe7-8bcaa63afeeb',2,30,'','cf66ba4c-bc58-4659-a550-c7a2dc0ed9a2',NULL),('4b03d7b4-c95f-4614-becb-556f1652789e',NULL,'client-secret-jwt','master','d45fb920-2a72-4874-95d6-204465be333b',2,30,'\0',NULL,NULL),('4e5ed7db-7c45-481f-a5eb-10f72ca0c775',NULL,'idp-email-verification','liferay-portal','c3bdfa10-03f9-474c-b9a7-4a3f4a4a0f5f',2,20,'\0',NULL,NULL),('507bff70-27f3-4209-b55e-5c3dd5829377',NULL,'registration-recaptcha-action','master','dea50c9a-b9bb-4230-b46d-6fa1a543ee2d',3,60,'\0',NULL,NULL),('56ee9e15-fd74-41fe-b421-73675394ce9f',NULL,'reset-password','liferay-portal','77ffbc63-aaaf-4d0d-8108-8dd6e890ca7b',0,30,'\0',NULL,NULL),('591b41c6-bd2c-4752-aa32-9f8bff4dc50e',NULL,'auth-otp-form','liferay-portal','27bde0c2-35f0-4a8b-b0df-16f4c1fb22e0',1,20,'\0',NULL,NULL),('5926673d-4f04-44fa-a3dd-d179bc8d31a2',NULL,'direct-grant-validate-otp','master','75250d37-7175-4d91-a36b-82a751d2fdbe',1,30,'\0',NULL,NULL),('5f89ad24-644c-4e49-b3a1-da1f7afcbac8',NULL,'auth-spnego','liferay-portal','b560f942-5754-4d3a-ab48-127a624faa0d',3,20,'\0',NULL,NULL),('60aea8f2-5cf4-42d7-a870-cd64cb4a85c3',NULL,'http-basic-authenticator','master','2107f895-d7dd-4c2b-8cf3-3637fee2960d',0,10,'\0',NULL,NULL),('60f9b2e9-0cef-4dc9-ad95-26a09907f1df',NULL,'basic-auth-otp','liferay-portal','6c18ae99-0885-43f4-b01b-3caaf20c0de6',3,30,'\0',NULL,NULL),('67623c40-708a-410f-80ed-5cafe8d25e48',NULL,'basic-auth','liferay-portal','6c18ae99-0885-43f4-b01b-3caaf20c0de6',0,20,'\0',NULL,NULL),('781b8758-6722-43a0-989b-f729df7b71e3',NULL,'docker-http-basic-authenticator','liferay-portal','f29a850a-300b-4420-a2c6-25badaddee1d',0,10,'\0',NULL,NULL),('7d00f2e0-75fb-4df4-a59c-58e50d5f443b',NULL,'idp-review-profile','master','dff1a7dd-1d0c-4e5f-abe7-8bcaa63afeeb',0,10,'\0',NULL,'591c9977-a350-4103-b229-4226814648a3'),('7f62f5d1-2d53-4fc4-8dd3-e6e3d5900a99',NULL,'registration-user-creation','master','dea50c9a-b9bb-4230-b46d-6fa1a543ee2d',0,20,'\0',NULL,NULL),('7fcb176e-ffe9-4b65-9f6a-fd450a9b6ce9',NULL,'reset-credentials-choose-user','master','ca05262f-631f-4ddf-8c8c-03f11a4c6508',0,10,'\0',NULL,NULL),('8183b6df-7a5a-4a3b-9245-35a1cc686372',NULL,'auth-otp-form','liferay-portal','0a2a7889-7098-4311-8589-956522a5d3f2',1,21,'\0',NULL,NULL),('829fda9b-9090-4ee8-892d-d93381de34de',NULL,'direct-grant-validate-password','liferay-portal','1f10b2ed-9d4e-4547-85ed-e7809b0bbe0f',0,20,'\0',NULL,NULL),('85185556-532e-4c75-bb33-cc0ece0eea9b',NULL,'identity-provider-redirector','master','7c50a617-e202-46ec-b3aa-6ae1bf549129',2,25,'\0',NULL,NULL),('8ac3eff4-c7a9-4dcd-a53c-c867b192d004',NULL,'client-x509','liferay-portal','3c55c28f-96d8-46d3-84b9-a5277265e976',2,40,'\0',NULL,NULL),('8f608905-188c-4a08-b6b2-9fea049ecb59',NULL,'idp-create-user-if-unique','master','dff1a7dd-1d0c-4e5f-abe7-8bcaa63afeeb',2,20,'\0',NULL,'aee30666-24d6-44b6-b946-8836c55a8892'),('8fc3c47c-a85e-4978-b362-c2cd6ad64922',NULL,'client-secret','master','d45fb920-2a72-4874-95d6-204465be333b',2,10,'\0',NULL,NULL),('9110d3a8-fcbf-415d-931f-9f2bae5f3ea1',NULL,'registration-deltares-user-action','liferay-portal','6c3d060d-6393-4fea-b2a8-72b644fd9927',0,40,'\0',NULL,NULL),('91eb9ffa-0140-43d3-ad30-25ff568e2a07',NULL,'client-jwt','liferay-portal','3c55c28f-96d8-46d3-84b9-a5277265e976',2,20,'\0',NULL,NULL),('a1e202d4-5140-48b0-acb5-2ef958e0e18a',NULL,'direct-grant-validate-username','liferay-portal','1f10b2ed-9d4e-4547-85ed-e7809b0bbe0f',0,10,'\0',NULL,NULL),('a4a9895a-da29-417a-9d58-aeb68993c39c',NULL,'direct-grant-validate-otp','liferay-portal','1f10b2ed-9d4e-4547-85ed-e7809b0bbe0f',1,30,'\0',NULL,NULL),('a7fed2d4-ea2c-46e2-bb1d-59bc441c1f09',NULL,'registration-profile-action','liferay-portal','94a62ca6-01e9-47b1-9d28-5872a33195d5',0,40,'\0',NULL,NULL),('a822c826-b27f-4d54-b74e-c8d0424efca5',NULL,'identity-provider-redirector','liferay-portal','b560f942-5754-4d3a-ab48-127a624faa0d',2,25,'\0',NULL,NULL),('aa619057-b238-40d8-af39-b9cf77143c01',NULL,'registration-recaptcha-action','liferay-portal','94a62ca6-01e9-47b1-9d28-5872a33195d5',3,60,'\0',NULL,NULL),('aba53a0c-03ec-41aa-8b85-02dacbbf21fa',NULL,'registration-user-creation','liferay-portal','94a62ca6-01e9-47b1-9d28-5872a33195d5',0,20,'\0',NULL,NULL),('abea8013-96f9-40ac-9d5f-a9617f87da2f',NULL,'auth-spnego','liferay-portal','c209b812-1e70-41f3-97cd-6cff5b7550b0',3,20,'\0',NULL,NULL),('ad5f3688-2725-49fe-9f88-00f217181f42',NULL,'direct-grant-validate-username','master','75250d37-7175-4d91-a36b-82a751d2fdbe',0,10,'\0',NULL,NULL),('ae73a011-9f46-4d2a-b30e-23ba7541d89a',NULL,'registration-profile-action','liferay-portal','6c3d060d-6393-4fea-b2a8-72b644fd9927',0,60,'\0',NULL,NULL),('b01270fa-f01a-4239-8bb4-d1f821eb7e49',NULL,'reset-credential-email','liferay-portal','77ffbc63-aaaf-4d0d-8108-8dd6e890ca7b',0,20,'\0',NULL,NULL),('b3cc2cca-bece-4f87-95e3-e1c9b8f56246',NULL,'idp-email-verification','master','cf66ba4c-bc58-4659-a550-c7a2dc0ed9a2',2,20,'\0',NULL,NULL),('b6a18758-829d-4b41-a68d-c2ad25da360d',NULL,'docker-http-basic-authenticator','master','7c9bcd0a-09e2-4981-95c0-1ea5099c6e30',0,10,'\0',NULL,NULL),('b8fbc3b2-64bb-419f-9beb-df5ce6ece31b',NULL,'client-jwt','master','d45fb920-2a72-4874-95d6-204465be333b',2,20,'\0',NULL,NULL),('bd9a77ad-6bf1-4a3c-9bca-204fcb7232f5',NULL,'registration-profile-action','master','dea50c9a-b9bb-4230-b46d-6fa1a543ee2d',0,40,'\0',NULL,NULL),('c4707b4f-706c-43d0-9caa-b373c014897f',NULL,'registration-page-form','liferay-portal','6d517d95-18b4-429d-92bb-d506528d5d60',0,10,'','6c3d060d-6393-4fea-b2a8-72b644fd9927',NULL),('cd6f0808-9f62-44fc-8e86-d0b60bdadc0e',NULL,'idp-review-profile','liferay-portal','fbd304fa-b190-475b-bf02-139776d5e1a0',0,10,'\0',NULL,'8bfd4c9a-1d33-45ca-99db-719d5ad9007e'),('d277a9d5-22c2-4c21-9c52-43b4ed94def8',NULL,'identity-provider-redirector','liferay-portal','c209b812-1e70-41f3-97cd-6cff5b7550b0',2,25,'\0',NULL,NULL),('d357637b-2030-4db0-b56b-1d9a85463c6f',NULL,'reset-credential-email','master','ca05262f-631f-4ddf-8c8c-03f11a4c6508',0,20,'\0',NULL,NULL),('d6de8d66-d707-491b-9e9a-6e1b675f77f8',NULL,'basic-auth','master','afabf1c5-a49b-4462-9db1-395dc3214ad5',0,20,'\0',NULL,NULL),('d92d2cc7-92e2-4420-b6f8-78badce21dc2',NULL,NULL,'liferay-portal','fbd304fa-b190-475b-bf02-139776d5e1a0',2,30,'','c3bdfa10-03f9-474c-b9a7-4a3f4a4a0f5f',NULL),('da6df951-b1ba-4500-91af-9618f4f9a07f',NULL,'http-basic-authenticator','liferay-portal','86579854-3f7f-476d-b211-253f4f6f6f0a',0,10,'\0',NULL,NULL),('db8ab0f8-1e46-464f-8831-9c869d342354',NULL,'registration-recaptcha-action','liferay-portal','6c3d060d-6393-4fea-b2a8-72b644fd9927',3,62,'\0',NULL,NULL),('e04bd2e6-f948-4d03-8b0c-2fd628c9eca5',NULL,'client-secret','liferay-portal','3c55c28f-96d8-46d3-84b9-a5277265e976',2,10,'\0',NULL,NULL),('e0cfcbd8-29e7-4ada-977e-77d474368100',NULL,NULL,'master','7c50a617-e202-46ec-b3aa-6ae1bf549129',2,30,'','22c03d7d-a755-4a8f-bdb2-9f16a827aae0',NULL),('e171dfb5-4f34-45b9-9482-e9f7b649ec6d',NULL,'idp-username-password-form','master','1d4a340b-8aa4-43a4-a151-979466a3cbd5',0,10,'\0',NULL,NULL),('e2331286-aaf3-4fc6-930c-cd03cc7accce',NULL,'registration-deltares-email-action','liferay-portal','6c3d060d-6393-4fea-b2a8-72b644fd9927',0,20,'\0',NULL,NULL),('e454886d-3ae6-4cb9-ae4e-548b0bfdaf2e',NULL,NULL,'liferay-portal','b560f942-5754-4d3a-ab48-127a624faa0d',2,30,'','0a2a7889-7098-4311-8589-956522a5d3f2',NULL),('e4e42c4a-86b7-4414-8e80-2961a62a8cae',NULL,'auth-spnego','master','afabf1c5-a49b-4462-9db1-395dc3214ad5',3,40,'\0',NULL,NULL),('e8e3dec6-9774-4e21-96c0-a769db134ccd',NULL,'no-cookie-redirect','liferay-portal','6c18ae99-0885-43f4-b01b-3caaf20c0de6',0,10,'\0',NULL,NULL),('edacc63b-72c0-4417-9fd4-6b1695dd43f4',NULL,NULL,'liferay-portal','c209b812-1e70-41f3-97cd-6cff5b7550b0',2,30,'','27bde0c2-35f0-4a8b-b0df-16f4c1fb22e0',NULL),('f106aabb-7819-4416-bd9a-e53e1c151263',NULL,'client-secret-jwt','liferay-portal','3c55c28f-96d8-46d3-84b9-a5277265e976',2,30,'\0',NULL,NULL),('f673c934-556f-48be-a6eb-ed55e02b01fc',NULL,'auth-spnego','liferay-portal','6c18ae99-0885-43f4-b01b-3caaf20c0de6',3,40,'\0',NULL,NULL),('f8d1234a-8b89-40c0-ae02-76b0657daa76',NULL,'reset-otp','liferay-portal','77ffbc63-aaaf-4d0d-8108-8dd6e890ca7b',1,40,'\0',NULL,NULL),('fa2d599a-727d-407b-b4bf-7d6a8a9bd0a4',NULL,'reset-password','master','ca05262f-631f-4ddf-8c8c-03f11a4c6508',0,30,'\0',NULL,NULL),('fa6e8b29-b553-4508-b8a7-7f105b0aa80c',NULL,'deltares-auth-username-password-form','liferay-portal','0a2a7889-7098-4311-8589-956522a5d3f2',0,20,'\0',NULL,NULL),('fb760dc1-c4bf-48c8-8199-f91f12a91bd4',NULL,'auth-otp-form','master','22c03d7d-a755-4a8f-bdb2-9f16a827aae0',1,20,'\0',NULL,NULL);
/*!40000 ALTER TABLE `AUTHENTICATION_EXECUTION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AUTHENTICATION_FLOW`
--

DROP TABLE IF EXISTS `AUTHENTICATION_FLOW`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AUTHENTICATION_FLOW` (
  `ID` varchar(36) NOT NULL,
  `ALIAS` varchar(255) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `REALM_ID` varchar(36) DEFAULT NULL,
  `PROVIDER_ID` varchar(36) NOT NULL DEFAULT 'basic-flow',
  `TOP_LEVEL` bit(1) NOT NULL DEFAULT b'0',
  `BUILT_IN` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`ID`),
  KEY `IDX_AUTH_FLOW_REALM` (`REALM_ID`),
  CONSTRAINT `FK_AUTH_FLOW_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AUTHENTICATION_FLOW`
--

LOCK TABLES `AUTHENTICATION_FLOW` WRITE;
/*!40000 ALTER TABLE `AUTHENTICATION_FLOW` DISABLE KEYS */;
INSERT INTO `AUTHENTICATION_FLOW` VALUES ('0a2a7889-7098-4311-8589-956522a5d3f2','deltares browser forms','Username, password, otp and other auth forms.','liferay-portal','basic-flow','\0','\0'),('1d4a340b-8aa4-43a4-a151-979466a3cbd5','Verify Existing Account by Re-authentication','Reauthentication of existing account','master','basic-flow','\0',''),('1f10b2ed-9d4e-4547-85ed-e7809b0bbe0f','direct grant','OpenID Connect Resource Owner Grant','liferay-portal','basic-flow','',''),('2107f895-d7dd-4c2b-8cf3-3637fee2960d','saml ecp','SAML ECP Profile Authentication Flow','master','basic-flow','',''),('22c03d7d-a755-4a8f-bdb2-9f16a827aae0','forms','Username, password, otp and other auth forms.','master','basic-flow','\0',''),('27bde0c2-35f0-4a8b-b0df-16f4c1fb22e0','forms','Username, password, otp and other auth forms.','liferay-portal','basic-flow','\0',''),('3c55c28f-96d8-46d3-84b9-a5277265e976','clients','Base authentication for clients','liferay-portal','client-flow','',''),('3dc56f89-e689-4131-860e-566332cf39bb','registration','registration flow','master','basic-flow','',''),('6c18ae99-0885-43f4-b01b-3caaf20c0de6','http challenge','An authentication flow based on challenge-response HTTP Authentication Schemes','liferay-portal','basic-flow','',''),('6c3d060d-6393-4fea-b2a8-72b644fd9927','deltares registration registration form','registration form','liferay-portal','form-flow','\0','\0'),('6d517d95-18b4-429d-92bb-d506528d5d60','deltares registration','registration flow','liferay-portal','basic-flow','','\0'),('75250d37-7175-4d91-a36b-82a751d2fdbe','direct grant','OpenID Connect Resource Owner Grant','master','basic-flow','',''),('77ffbc63-aaaf-4d0d-8108-8dd6e890ca7b','reset credentials','Reset credentials for a user if they forgot their password or something','liferay-portal','basic-flow','',''),('7c50a617-e202-46ec-b3aa-6ae1bf549129','browser','browser based authentication','master','basic-flow','',''),('7c9bcd0a-09e2-4981-95c0-1ea5099c6e30','docker auth','Used by Docker clients to authenticate against the IDP','master','basic-flow','',''),('86579854-3f7f-476d-b211-253f4f6f6f0a','saml ecp','SAML ECP Profile Authentication Flow','liferay-portal','basic-flow','',''),('94a62ca6-01e9-47b1-9d28-5872a33195d5','registration form','registration form','liferay-portal','form-flow','\0',''),('afabf1c5-a49b-4462-9db1-395dc3214ad5','http challenge','An authentication flow based on challenge-response HTTP Authentication Schemes','master','basic-flow','',''),('b560f942-5754-4d3a-ab48-127a624faa0d','deltares browser','browser based authentication','liferay-portal','basic-flow','','\0'),('b8389120-a7d5-4ae2-9fb2-c75025808d84','registration','registration flow','liferay-portal','basic-flow','',''),('c209b812-1e70-41f3-97cd-6cff5b7550b0','browser','browser based authentication','liferay-portal','basic-flow','',''),('c3bdfa10-03f9-474c-b9a7-4a3f4a4a0f5f','Handle Existing Account','Handle what to do if there is existing account with same email/username like authenticated identity provider','liferay-portal','basic-flow','\0',''),('ca05262f-631f-4ddf-8c8c-03f11a4c6508','reset credentials','Reset credentials for a user if they forgot their password or something','master','basic-flow','',''),('cf66ba4c-bc58-4659-a550-c7a2dc0ed9a2','Handle Existing Account','Handle what to do if there is existing account with same email/username like authenticated identity provider','master','basic-flow','\0',''),('d45fb920-2a72-4874-95d6-204465be333b','clients','Base authentication for clients','master','client-flow','',''),('dea50c9a-b9bb-4230-b46d-6fa1a543ee2d','registration form','registration form','master','form-flow','\0',''),('dff1a7dd-1d0c-4e5f-abe7-8bcaa63afeeb','first broker login','Actions taken after first broker login with identity provider account, which is not yet linked to any Keycloak account','master','basic-flow','',''),('f29a850a-300b-4420-a2c6-25badaddee1d','docker auth','Used by Docker clients to authenticate against the IDP','liferay-portal','basic-flow','',''),('fa6eb2a4-7f51-4749-aebc-c6d095d31cbc','Verify Existing Account by Re-authentication','Reauthentication of existing account','liferay-portal','basic-flow','\0',''),('fbd304fa-b190-475b-bf02-139776d5e1a0','first broker login','Actions taken after first broker login with identity provider account, which is not yet linked to any Keycloak account','liferay-portal','basic-flow','','');
/*!40000 ALTER TABLE `AUTHENTICATION_FLOW` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AUTHENTICATOR_CONFIG`
--

DROP TABLE IF EXISTS `AUTHENTICATOR_CONFIG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AUTHENTICATOR_CONFIG` (
  `ID` varchar(36) NOT NULL,
  `ALIAS` varchar(255) DEFAULT NULL,
  `REALM_ID` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_AUTH_CONFIG_REALM` (`REALM_ID`),
  CONSTRAINT `FK_AUTH_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AUTHENTICATOR_CONFIG`
--

LOCK TABLES `AUTHENTICATOR_CONFIG` WRITE;
/*!40000 ALTER TABLE `AUTHENTICATOR_CONFIG` DISABLE KEYS */;
INSERT INTO `AUTHENTICATOR_CONFIG` VALUES ('591c9977-a350-4103-b229-4226814648a3','review profile config','master'),('8bfd4c9a-1d33-45ca-99db-719d5ad9007e','review profile config','liferay-portal'),('aee30666-24d6-44b6-b946-8836c55a8892','create unique user config','master'),('e43d487c-3712-47a8-9243-2d3684942de3','create unique user config','liferay-portal');
/*!40000 ALTER TABLE `AUTHENTICATOR_CONFIG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AUTHENTICATOR_CONFIG_ENTRY`
--

DROP TABLE IF EXISTS `AUTHENTICATOR_CONFIG_ENTRY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AUTHENTICATOR_CONFIG_ENTRY` (
  `AUTHENTICATOR_ID` varchar(36) NOT NULL,
  `VALUE` longtext,
  `NAME` varchar(255) NOT NULL,
  PRIMARY KEY (`AUTHENTICATOR_ID`,`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AUTHENTICATOR_CONFIG_ENTRY`
--

LOCK TABLES `AUTHENTICATOR_CONFIG_ENTRY` WRITE;
/*!40000 ALTER TABLE `AUTHENTICATOR_CONFIG_ENTRY` DISABLE KEYS */;
INSERT INTO `AUTHENTICATOR_CONFIG_ENTRY` VALUES ('591c9977-a350-4103-b229-4226814648a3','missing','update.profile.on.first.login'),('8bfd4c9a-1d33-45ca-99db-719d5ad9007e','missing','update.profile.on.first.login'),('aee30666-24d6-44b6-b946-8836c55a8892','false','require.password.update.after.registration'),('e43d487c-3712-47a8-9243-2d3684942de3','false','require.password.update.after.registration');
/*!40000 ALTER TABLE `AUTHENTICATOR_CONFIG_ENTRY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BROKER_LINK`
--

DROP TABLE IF EXISTS `BROKER_LINK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `BROKER_LINK` (
  `IDENTITY_PROVIDER` varchar(255) NOT NULL,
  `STORAGE_PROVIDER_ID` varchar(255) DEFAULT NULL,
  `REALM_ID` varchar(36) NOT NULL,
  `BROKER_USER_ID` varchar(255) DEFAULT NULL,
  `BROKER_USERNAME` varchar(255) DEFAULT NULL,
  `TOKEN` text,
  `USER_ID` varchar(255) NOT NULL,
  PRIMARY KEY (`IDENTITY_PROVIDER`,`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BROKER_LINK`
--

LOCK TABLES `BROKER_LINK` WRITE;
/*!40000 ALTER TABLE `BROKER_LINK` DISABLE KEYS */;
/*!40000 ALTER TABLE `BROKER_LINK` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT`
--

DROP TABLE IF EXISTS `CLIENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CLIENT` (
  `ID` varchar(36) NOT NULL,
  `ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `FULL_SCOPE_ALLOWED` bit(1) NOT NULL DEFAULT b'0',
  `CLIENT_ID` varchar(255) DEFAULT NULL,
  `NOT_BEFORE` int(11) DEFAULT NULL,
  `PUBLIC_CLIENT` bit(1) NOT NULL DEFAULT b'0',
  `SECRET` varchar(255) DEFAULT NULL,
  `BASE_URL` varchar(255) DEFAULT NULL,
  `BEARER_ONLY` bit(1) NOT NULL DEFAULT b'0',
  `MANAGEMENT_URL` varchar(255) DEFAULT NULL,
  `SURROGATE_AUTH_REQUIRED` bit(1) NOT NULL DEFAULT b'0',
  `REALM_ID` varchar(36) DEFAULT NULL,
  `PROTOCOL` varchar(255) DEFAULT NULL,
  `NODE_REREG_TIMEOUT` int(11) DEFAULT '0',
  `FRONTCHANNEL_LOGOUT` bit(1) NOT NULL DEFAULT b'0',
  `CONSENT_REQUIRED` bit(1) NOT NULL DEFAULT b'0',
  `NAME` varchar(255) DEFAULT NULL,
  `SERVICE_ACCOUNTS_ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `CLIENT_AUTHENTICATOR_TYPE` varchar(255) DEFAULT NULL,
  `ROOT_URL` varchar(255) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `REGISTRATION_TOKEN` varchar(255) DEFAULT NULL,
  `STANDARD_FLOW_ENABLED` bit(1) NOT NULL DEFAULT b'1',
  `IMPLICIT_FLOW_ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `DIRECT_ACCESS_GRANTS_ENABLED` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_B71CJLBENV945RB6GCON438AT` (`REALM_ID`,`CLIENT_ID`),
  CONSTRAINT `FK_P56CTINXXB9GSK57FO49F9TAC` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT`
--

LOCK TABLES `CLIENT` WRITE;
/*!40000 ALTER TABLE `CLIENT` DISABLE KEYS */;
INSERT INTO `CLIENT` VALUES ('07a7d9af-fe25-4479-8d74-9ddc0baeb35e','','','admin-api-admin',0,'\0','**********',NULL,'\0',NULL,'\0','liferay-portal','openid-connect',-1,'\0','\0',NULL,'','client-secret',NULL,NULL,NULL,'\0','\0',''),('27a25fc2-29bd-4afd-a135-a5b2b8be12b8','','','admin-api-viewer',0,'\0','**********',NULL,'\0',NULL,'\0','liferay-portal','openid-connect',-1,'\0','\0',NULL,'','client-secret',NULL,NULL,NULL,'\0','\0',''),('3453a904-cef0-4361-9226-54d6fa84af18','','','liferay-portal-realm',0,'\0','c1e1b77a-e942-4501-8cbb-aafdeecc9f34',NULL,'',NULL,'\0','master',NULL,0,'\0','\0','liferay-portal Realm','\0','client-secret',NULL,NULL,NULL,'','\0','\0'),('364c3e71-9195-4162-9d08-e048421fdd0f','','\0','security-admin-console',0,'','**********','/auth/admin/liferay-portal/console/index.html','\0',NULL,'\0','liferay-portal','openid-connect',0,'\0','\0','${client_security-admin-console}','\0','client-secret',NULL,NULL,NULL,'','\0','\0'),('434f2a4d-52d7-4823-badc-54b9835ec942','','\0','admin-cli',0,'','**********',NULL,'\0',NULL,'\0','liferay-portal','openid-connect',0,'\0','\0','${client_admin-cli}','\0','client-secret',NULL,NULL,NULL,'\0','\0',''),('67d6d3e3-38a7-4562-aac7-03832d713532','','','user-api',0,'\0','**********',NULL,'\0',NULL,'\0','liferay-portal','openid-connect',-1,'\0','\0',NULL,'','client-secret',NULL,NULL,NULL,'','\0',''),('7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6','','\0','realm-management',0,'\0','**********',NULL,'\0',NULL,'\0','liferay-portal','openid-connect',0,'\0','\0','${client_realm-management}','','client-secret',NULL,NULL,NULL,'','\0','\0'),('88f76073-729e-461a-aaa3-9fe42dae6a0e','','\0','admin-cli',0,'','57e45f1b-5c63-4792-bd0a-c317fd0bbb8d',NULL,'\0',NULL,'\0','master','openid-connect',0,'\0','\0','${client_admin-cli}','\0','client-secret',NULL,NULL,NULL,'\0','\0',''),('9b3c4d13-0a92-46a4-a07c-e412e33024d7','','\0','broker',0,'\0','**********',NULL,'\0',NULL,'\0','liferay-portal','openid-connect',0,'\0','\0','${client_broker}','\0','client-secret',NULL,NULL,NULL,'','\0','\0'),('a6cac270-1663-4123-8db5-54ccaaee5871','','','master-realm',0,'\0','4e88e120-be67-4352-928a-94cd275e68dc',NULL,'',NULL,'\0','master',NULL,0,'\0','\0','master Realm','\0','client-secret',NULL,NULL,NULL,'','\0','\0'),('c5f79f09-95d5-4a96-a845-47bad8536eb7','','\0','account',0,'\0','a491a892-dd67-47a3-849f-1cc6808b0109','/auth/realms/master/account','\0',NULL,'\0','master','openid-connect',0,'\0','\0','${client_account}','\0','client-secret',NULL,NULL,NULL,'','\0','\0'),('d6d25c43-45e4-4107-89b4-7fc91bca0f80','','\0','security-admin-console',0,'','32196446-ac94-44ba-abc8-56118f4a3488','/auth/admin/master/console/index.html','\0',NULL,'\0','master','openid-connect',0,'\0','\0','${client_security-admin-console}','\0','client-secret',NULL,NULL,NULL,'','\0','\0'),('f1b13f7c-a916-4b24-b314-0200496926ce','','\0','account',0,'\0','**********','/auth/realms/liferay-portal/account','\0',NULL,'\0','liferay-portal','openid-connect',0,'\0','\0','${client_account}','\0','client-secret',NULL,NULL,NULL,'','\0','\0'),('f9a0e8ea-d2c7-4ea1-b66f-26022f37d23b','','\0','broker',0,'\0','ef13ceb7-9b8e-4ce6-bf30-6e6614ae8acd',NULL,'\0',NULL,'\0','master','openid-connect',0,'\0','\0','${client_broker}','\0','client-secret',NULL,NULL,NULL,'','\0','\0');
/*!40000 ALTER TABLE `CLIENT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT_ATTRIBUTES`
--

DROP TABLE IF EXISTS `CLIENT_ATTRIBUTES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CLIENT_ATTRIBUTES` (
  `CLIENT_ID` varchar(36) NOT NULL,
  `VALUE` varchar(4000) DEFAULT NULL,
  `NAME` varchar(255) NOT NULL,
  PRIMARY KEY (`CLIENT_ID`,`NAME`),
  CONSTRAINT `FK3C47C64BEACCA966` FOREIGN KEY (`CLIENT_ID`) REFERENCES `CLIENT` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT_ATTRIBUTES`
--

LOCK TABLES `CLIENT_ATTRIBUTES` WRITE;
/*!40000 ALTER TABLE `CLIENT_ATTRIBUTES` DISABLE KEYS */;
INSERT INTO `CLIENT_ATTRIBUTES` VALUES ('07a7d9af-fe25-4479-8d74-9ddc0baeb35e','false','display.on.consent.screen'),('07a7d9af-fe25-4479-8d74-9ddc0baeb35e','false','exclude.session.state.from.auth.response'),('07a7d9af-fe25-4479-8d74-9ddc0baeb35e','false','saml.assertion.signature'),('07a7d9af-fe25-4479-8d74-9ddc0baeb35e','false','saml.authnstatement'),('07a7d9af-fe25-4479-8d74-9ddc0baeb35e','false','saml.client.signature'),('07a7d9af-fe25-4479-8d74-9ddc0baeb35e','false','saml.encrypt'),('07a7d9af-fe25-4479-8d74-9ddc0baeb35e','false','saml.force.post.binding'),('07a7d9af-fe25-4479-8d74-9ddc0baeb35e','false','saml.multivalued.roles'),('07a7d9af-fe25-4479-8d74-9ddc0baeb35e','false','saml.onetimeuse.condition'),('07a7d9af-fe25-4479-8d74-9ddc0baeb35e','false','saml.server.signature'),('07a7d9af-fe25-4479-8d74-9ddc0baeb35e','false','saml.server.signature.keyinfo.ext'),('07a7d9af-fe25-4479-8d74-9ddc0baeb35e','false','saml_force_name_id_format'),('07a7d9af-fe25-4479-8d74-9ddc0baeb35e','false','tls.client.certificate.bound.access.tokens'),('27a25fc2-29bd-4afd-a135-a5b2b8be12b8','false','display.on.consent.screen'),('27a25fc2-29bd-4afd-a135-a5b2b8be12b8','false','exclude.session.state.from.auth.response'),('27a25fc2-29bd-4afd-a135-a5b2b8be12b8','false','saml.assertion.signature'),('27a25fc2-29bd-4afd-a135-a5b2b8be12b8','false','saml.authnstatement'),('27a25fc2-29bd-4afd-a135-a5b2b8be12b8','false','saml.client.signature'),('27a25fc2-29bd-4afd-a135-a5b2b8be12b8','false','saml.encrypt'),('27a25fc2-29bd-4afd-a135-a5b2b8be12b8','false','saml.force.post.binding'),('27a25fc2-29bd-4afd-a135-a5b2b8be12b8','false','saml.multivalued.roles'),('27a25fc2-29bd-4afd-a135-a5b2b8be12b8','false','saml.onetimeuse.condition'),('27a25fc2-29bd-4afd-a135-a5b2b8be12b8','false','saml.server.signature'),('27a25fc2-29bd-4afd-a135-a5b2b8be12b8','false','saml.server.signature.keyinfo.ext'),('27a25fc2-29bd-4afd-a135-a5b2b8be12b8','false','saml_force_name_id_format'),('27a25fc2-29bd-4afd-a135-a5b2b8be12b8','false','tls.client.certificate.bound.access.tokens'),('67d6d3e3-38a7-4562-aac7-03832d713532','false','display.on.consent.screen'),('67d6d3e3-38a7-4562-aac7-03832d713532','false','exclude.session.state.from.auth.response'),('67d6d3e3-38a7-4562-aac7-03832d713532','false','saml.assertion.signature'),('67d6d3e3-38a7-4562-aac7-03832d713532','false','saml.authnstatement'),('67d6d3e3-38a7-4562-aac7-03832d713532','false','saml.client.signature'),('67d6d3e3-38a7-4562-aac7-03832d713532','false','saml.encrypt'),('67d6d3e3-38a7-4562-aac7-03832d713532','false','saml.force.post.binding'),('67d6d3e3-38a7-4562-aac7-03832d713532','false','saml.multivalued.roles'),('67d6d3e3-38a7-4562-aac7-03832d713532','false','saml.onetimeuse.condition'),('67d6d3e3-38a7-4562-aac7-03832d713532','false','saml.server.signature'),('67d6d3e3-38a7-4562-aac7-03832d713532','false','saml.server.signature.keyinfo.ext'),('67d6d3e3-38a7-4562-aac7-03832d713532','false','saml_force_name_id_format'),('67d6d3e3-38a7-4562-aac7-03832d713532','false','tls.client.certificate.bound.access.tokens'),('f1b13f7c-a916-4b24-b314-0200496926ce','false','display.on.consent.screen'),('f1b13f7c-a916-4b24-b314-0200496926ce','false','exclude.session.state.from.auth.response'),('f1b13f7c-a916-4b24-b314-0200496926ce','false','saml.assertion.signature'),('f1b13f7c-a916-4b24-b314-0200496926ce','false','saml.authnstatement'),('f1b13f7c-a916-4b24-b314-0200496926ce','false','saml.client.signature'),('f1b13f7c-a916-4b24-b314-0200496926ce','false','saml.encrypt'),('f1b13f7c-a916-4b24-b314-0200496926ce','false','saml.force.post.binding'),('f1b13f7c-a916-4b24-b314-0200496926ce','false','saml.multivalued.roles'),('f1b13f7c-a916-4b24-b314-0200496926ce','false','saml.onetimeuse.condition'),('f1b13f7c-a916-4b24-b314-0200496926ce','false','saml.server.signature'),('f1b13f7c-a916-4b24-b314-0200496926ce','false','saml.server.signature.keyinfo.ext'),('f1b13f7c-a916-4b24-b314-0200496926ce','false','saml_force_name_id_format'),('f1b13f7c-a916-4b24-b314-0200496926ce','false','tls.client.certificate.bound.access.tokens');
/*!40000 ALTER TABLE `CLIENT_ATTRIBUTES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT_AUTH_FLOW_BINDINGS`
--

DROP TABLE IF EXISTS `CLIENT_AUTH_FLOW_BINDINGS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CLIENT_AUTH_FLOW_BINDINGS` (
  `CLIENT_ID` varchar(36) NOT NULL,
  `FLOW_ID` varchar(36) DEFAULT NULL,
  `BINDING_NAME` varchar(255) NOT NULL,
  PRIMARY KEY (`CLIENT_ID`,`BINDING_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT_AUTH_FLOW_BINDINGS`
--

LOCK TABLES `CLIENT_AUTH_FLOW_BINDINGS` WRITE;
/*!40000 ALTER TABLE `CLIENT_AUTH_FLOW_BINDINGS` DISABLE KEYS */;
/*!40000 ALTER TABLE `CLIENT_AUTH_FLOW_BINDINGS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT_DEFAULT_ROLES`
--

DROP TABLE IF EXISTS `CLIENT_DEFAULT_ROLES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CLIENT_DEFAULT_ROLES` (
  `CLIENT_ID` varchar(36) NOT NULL,
  `ROLE_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`CLIENT_ID`,`ROLE_ID`),
  UNIQUE KEY `UK_8AELWNIBJI49AVXSRTUF6XJOW` (`ROLE_ID`),
  KEY `IDX_CLIENT_DEF_ROLES_CLIENT` (`CLIENT_ID`),
  CONSTRAINT `FK_8AELWNIBJI49AVXSRTUF6XJOW` FOREIGN KEY (`ROLE_ID`) REFERENCES `KEYCLOAK_ROLE` (`ID`),
  CONSTRAINT `FK_NUILTS7KLWQW2H8M2B5JOYTKY` FOREIGN KEY (`CLIENT_ID`) REFERENCES `CLIENT` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT_DEFAULT_ROLES`
--

LOCK TABLES `CLIENT_DEFAULT_ROLES` WRITE;
/*!40000 ALTER TABLE `CLIENT_DEFAULT_ROLES` DISABLE KEYS */;
INSERT INTO `CLIENT_DEFAULT_ROLES` VALUES ('c5f79f09-95d5-4a96-a845-47bad8536eb7','43c67ebb-198b-4547-96e9-86b7a0111566'),('c5f79f09-95d5-4a96-a845-47bad8536eb7','b930fbec-a391-40dc-887c-a36fe46999a7'),('f1b13f7c-a916-4b24-b314-0200496926ce','0ab906cb-cc4f-4af4-9dd4-e676e6f0f9f3'),('f1b13f7c-a916-4b24-b314-0200496926ce','8c430ca8-55e7-43c6-8625-527ab3f3ec3f');
/*!40000 ALTER TABLE `CLIENT_DEFAULT_ROLES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT_INITIAL_ACCESS`
--

DROP TABLE IF EXISTS `CLIENT_INITIAL_ACCESS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CLIENT_INITIAL_ACCESS` (
  `ID` varchar(36) NOT NULL,
  `REALM_ID` varchar(36) NOT NULL,
  `TIMESTAMP` int(11) DEFAULT NULL,
  `EXPIRATION` int(11) DEFAULT NULL,
  `COUNT` int(11) DEFAULT NULL,
  `REMAINING_COUNT` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_CLIENT_INIT_ACC_REALM` (`REALM_ID`),
  CONSTRAINT `FK_CLIENT_INIT_ACC_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT_INITIAL_ACCESS`
--

LOCK TABLES `CLIENT_INITIAL_ACCESS` WRITE;
/*!40000 ALTER TABLE `CLIENT_INITIAL_ACCESS` DISABLE KEYS */;
/*!40000 ALTER TABLE `CLIENT_INITIAL_ACCESS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT_NODE_REGISTRATIONS`
--

DROP TABLE IF EXISTS `CLIENT_NODE_REGISTRATIONS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CLIENT_NODE_REGISTRATIONS` (
  `CLIENT_ID` varchar(36) NOT NULL,
  `VALUE` int(11) DEFAULT NULL,
  `NAME` varchar(255) NOT NULL,
  PRIMARY KEY (`CLIENT_ID`,`NAME`),
  CONSTRAINT `FK4129723BA992F594` FOREIGN KEY (`CLIENT_ID`) REFERENCES `CLIENT` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT_NODE_REGISTRATIONS`
--

LOCK TABLES `CLIENT_NODE_REGISTRATIONS` WRITE;
/*!40000 ALTER TABLE `CLIENT_NODE_REGISTRATIONS` DISABLE KEYS */;
/*!40000 ALTER TABLE `CLIENT_NODE_REGISTRATIONS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT_SCOPE`
--

DROP TABLE IF EXISTS `CLIENT_SCOPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CLIENT_SCOPE` (
  `ID` varchar(36) NOT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `REALM_ID` varchar(36) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `PROTOCOL` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_CLI_SCOPE` (`REALM_ID`,`NAME`),
  KEY `IDX_REALM_CLSCOPE` (`REALM_ID`),
  CONSTRAINT `FK_REALM_CLI_SCOPE` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT_SCOPE`
--

LOCK TABLES `CLIENT_SCOPE` WRITE;
/*!40000 ALTER TABLE `CLIENT_SCOPE` DISABLE KEYS */;
INSERT INTO `CLIENT_SCOPE` VALUES ('105782f5-b3bb-48d5-8f77-0e5ecbfe116e','phone','master','OpenID Connect built-in scope: phone','openid-connect'),('12cb8b52-bfdf-4650-88be-998a34c53bd8','email','master','OpenID Connect built-in scope: email','openid-connect'),('3522f3d8-9fad-4c62-bfc8-000b9091361a','profile','liferay-portal','OpenID Connect built-in scope: profile','openid-connect'),('3e58a816-cd84-48e6-8da1-2baa706dbdda','microprofile-jwt','master','Microprofile - JWT built-in scope','openid-connect'),('534ed1af-eb77-46d7-b255-348e140a87af','role_list','master','SAML role list','saml'),('55164a6a-d9a0-4d36-a289-abafa7fc1551','role_list','liferay-portal','SAML role list','saml'),('5bfaf140-06b1-46a8-9332-5b2b9520ebb4','offline_access','master','OpenID Connect built-in scope: offline_access','openid-connect'),('7f259547-fc8a-4689-a51f-5991b1a93562','email','liferay-portal','OpenID Connect built-in scope: email','openid-connect'),('80bd4364-efe7-4991-a593-067f1c45c7ef','address','master','OpenID Connect built-in scope: address','openid-connect'),('93b2f0f7-c201-4196-b6bc-2829369f70e2','phone','liferay-portal','OpenID Connect built-in scope: phone','openid-connect'),('c6dc943c-f19c-4f65-9116-38d8773fb28d','web-origins','liferay-portal','OpenID Connect scope for add allowed web origins to the access token','openid-connect'),('d3813bc9-fcb8-4d3e-bd5a-aefd3d4b4ca6','roles','liferay-portal','OpenID Connect scope for add user roles to the access token','openid-connect'),('eb4606ff-4ec0-4ffd-980a-a735c2465aeb','address','liferay-portal','OpenID Connect built-in scope: address','openid-connect'),('ede142ae-567f-46a4-bce5-7e868321805d','roles','master','OpenID Connect scope for add user roles to the access token','openid-connect'),('f1c5a5e7-78b0-4560-8162-3c5db1dbf768','profile','master','OpenID Connect built-in scope: profile','openid-connect'),('f820f43b-5e2f-4fd7-9e1a-2d24b88c4b11','web-origins','master','OpenID Connect scope for add allowed web origins to the access token','openid-connect'),('fca7dd02-3fb8-45c2-b72e-0f4e9daeeca5','offline_access','liferay-portal','OpenID Connect built-in scope: offline_access','openid-connect');
/*!40000 ALTER TABLE `CLIENT_SCOPE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT_SCOPE_ATTRIBUTES`
--

DROP TABLE IF EXISTS `CLIENT_SCOPE_ATTRIBUTES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CLIENT_SCOPE_ATTRIBUTES` (
  `SCOPE_ID` varchar(36) NOT NULL,
  `VALUE` varchar(2048) DEFAULT NULL,
  `NAME` varchar(255) NOT NULL,
  PRIMARY KEY (`SCOPE_ID`,`NAME`),
  KEY `IDX_CLSCOPE_ATTRS` (`SCOPE_ID`),
  CONSTRAINT `FK_CL_SCOPE_ATTR_SCOPE` FOREIGN KEY (`SCOPE_ID`) REFERENCES `CLIENT_SCOPE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT_SCOPE_ATTRIBUTES`
--

LOCK TABLES `CLIENT_SCOPE_ATTRIBUTES` WRITE;
/*!40000 ALTER TABLE `CLIENT_SCOPE_ATTRIBUTES` DISABLE KEYS */;
INSERT INTO `CLIENT_SCOPE_ATTRIBUTES` VALUES ('105782f5-b3bb-48d5-8f77-0e5ecbfe116e','${phoneScopeConsentText}','consent.screen.text'),('105782f5-b3bb-48d5-8f77-0e5ecbfe116e','true','display.on.consent.screen'),('105782f5-b3bb-48d5-8f77-0e5ecbfe116e','true','include.in.token.scope'),('12cb8b52-bfdf-4650-88be-998a34c53bd8','${emailScopeConsentText}','consent.screen.text'),('12cb8b52-bfdf-4650-88be-998a34c53bd8','true','display.on.consent.screen'),('12cb8b52-bfdf-4650-88be-998a34c53bd8','true','include.in.token.scope'),('3522f3d8-9fad-4c62-bfc8-000b9091361a','${profileScopeConsentText}','consent.screen.text'),('3522f3d8-9fad-4c62-bfc8-000b9091361a','true','display.on.consent.screen'),('3e58a816-cd84-48e6-8da1-2baa706dbdda','false','display.on.consent.screen'),('3e58a816-cd84-48e6-8da1-2baa706dbdda','true','include.in.token.scope'),('534ed1af-eb77-46d7-b255-348e140a87af','${samlRoleListScopeConsentText}','consent.screen.text'),('534ed1af-eb77-46d7-b255-348e140a87af','true','display.on.consent.screen'),('55164a6a-d9a0-4d36-a289-abafa7fc1551','${samlRoleListScopeConsentText}','consent.screen.text'),('55164a6a-d9a0-4d36-a289-abafa7fc1551','true','display.on.consent.screen'),('5bfaf140-06b1-46a8-9332-5b2b9520ebb4','${offlineAccessScopeConsentText}','consent.screen.text'),('5bfaf140-06b1-46a8-9332-5b2b9520ebb4','true','display.on.consent.screen'),('7f259547-fc8a-4689-a51f-5991b1a93562','${emailScopeConsentText}','consent.screen.text'),('7f259547-fc8a-4689-a51f-5991b1a93562','true','display.on.consent.screen'),('80bd4364-efe7-4991-a593-067f1c45c7ef','${addressScopeConsentText}','consent.screen.text'),('80bd4364-efe7-4991-a593-067f1c45c7ef','true','display.on.consent.screen'),('80bd4364-efe7-4991-a593-067f1c45c7ef','true','include.in.token.scope'),('93b2f0f7-c201-4196-b6bc-2829369f70e2','${phoneScopeConsentText}','consent.screen.text'),('93b2f0f7-c201-4196-b6bc-2829369f70e2','true','display.on.consent.screen'),('c6dc943c-f19c-4f65-9116-38d8773fb28d','','consent.screen.text'),('c6dc943c-f19c-4f65-9116-38d8773fb28d','false','display.on.consent.screen'),('c6dc943c-f19c-4f65-9116-38d8773fb28d','false','include.in.token.scope'),('d3813bc9-fcb8-4d3e-bd5a-aefd3d4b4ca6','${rolesScopeConsentText}','consent.screen.text'),('d3813bc9-fcb8-4d3e-bd5a-aefd3d4b4ca6','true','display.on.consent.screen'),('d3813bc9-fcb8-4d3e-bd5a-aefd3d4b4ca6','false','include.in.token.scope'),('eb4606ff-4ec0-4ffd-980a-a735c2465aeb','${addressScopeConsentText}','consent.screen.text'),('eb4606ff-4ec0-4ffd-980a-a735c2465aeb','true','display.on.consent.screen'),('ede142ae-567f-46a4-bce5-7e868321805d','${rolesScopeConsentText}','consent.screen.text'),('ede142ae-567f-46a4-bce5-7e868321805d','true','display.on.consent.screen'),('ede142ae-567f-46a4-bce5-7e868321805d','false','include.in.token.scope'),('f1c5a5e7-78b0-4560-8162-3c5db1dbf768','${profileScopeConsentText}','consent.screen.text'),('f1c5a5e7-78b0-4560-8162-3c5db1dbf768','true','display.on.consent.screen'),('f1c5a5e7-78b0-4560-8162-3c5db1dbf768','true','include.in.token.scope'),('f820f43b-5e2f-4fd7-9e1a-2d24b88c4b11','','consent.screen.text'),('f820f43b-5e2f-4fd7-9e1a-2d24b88c4b11','false','display.on.consent.screen'),('f820f43b-5e2f-4fd7-9e1a-2d24b88c4b11','false','include.in.token.scope'),('fca7dd02-3fb8-45c2-b72e-0f4e9daeeca5','${offlineAccessScopeConsentText}','consent.screen.text'),('fca7dd02-3fb8-45c2-b72e-0f4e9daeeca5','true','display.on.consent.screen');
/*!40000 ALTER TABLE `CLIENT_SCOPE_ATTRIBUTES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT_SCOPE_CLIENT`
--

DROP TABLE IF EXISTS `CLIENT_SCOPE_CLIENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CLIENT_SCOPE_CLIENT` (
  `CLIENT_ID` varchar(36) NOT NULL,
  `SCOPE_ID` varchar(36) NOT NULL,
  `DEFAULT_SCOPE` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`CLIENT_ID`,`SCOPE_ID`),
  KEY `IDX_CLSCOPE_CL` (`CLIENT_ID`),
  KEY `IDX_CL_CLSCOPE` (`SCOPE_ID`),
  CONSTRAINT `FK_C_CLI_SCOPE_CLIENT` FOREIGN KEY (`CLIENT_ID`) REFERENCES `CLIENT` (`ID`),
  CONSTRAINT `FK_C_CLI_SCOPE_SCOPE` FOREIGN KEY (`SCOPE_ID`) REFERENCES `CLIENT_SCOPE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT_SCOPE_CLIENT`
--

LOCK TABLES `CLIENT_SCOPE_CLIENT` WRITE;
/*!40000 ALTER TABLE `CLIENT_SCOPE_CLIENT` DISABLE KEYS */;
INSERT INTO `CLIENT_SCOPE_CLIENT` VALUES ('07a7d9af-fe25-4479-8d74-9ddc0baeb35e','3522f3d8-9fad-4c62-bfc8-000b9091361a',''),('07a7d9af-fe25-4479-8d74-9ddc0baeb35e','55164a6a-d9a0-4d36-a289-abafa7fc1551',''),('07a7d9af-fe25-4479-8d74-9ddc0baeb35e','7f259547-fc8a-4689-a51f-5991b1a93562',''),('07a7d9af-fe25-4479-8d74-9ddc0baeb35e','93b2f0f7-c201-4196-b6bc-2829369f70e2','\0'),('07a7d9af-fe25-4479-8d74-9ddc0baeb35e','c6dc943c-f19c-4f65-9116-38d8773fb28d',''),('07a7d9af-fe25-4479-8d74-9ddc0baeb35e','d3813bc9-fcb8-4d3e-bd5a-aefd3d4b4ca6',''),('07a7d9af-fe25-4479-8d74-9ddc0baeb35e','eb4606ff-4ec0-4ffd-980a-a735c2465aeb','\0'),('07a7d9af-fe25-4479-8d74-9ddc0baeb35e','fca7dd02-3fb8-45c2-b72e-0f4e9daeeca5','\0'),('27a25fc2-29bd-4afd-a135-a5b2b8be12b8','3522f3d8-9fad-4c62-bfc8-000b9091361a',''),('27a25fc2-29bd-4afd-a135-a5b2b8be12b8','55164a6a-d9a0-4d36-a289-abafa7fc1551',''),('27a25fc2-29bd-4afd-a135-a5b2b8be12b8','7f259547-fc8a-4689-a51f-5991b1a93562',''),('27a25fc2-29bd-4afd-a135-a5b2b8be12b8','93b2f0f7-c201-4196-b6bc-2829369f70e2','\0'),('27a25fc2-29bd-4afd-a135-a5b2b8be12b8','c6dc943c-f19c-4f65-9116-38d8773fb28d',''),('27a25fc2-29bd-4afd-a135-a5b2b8be12b8','d3813bc9-fcb8-4d3e-bd5a-aefd3d4b4ca6',''),('27a25fc2-29bd-4afd-a135-a5b2b8be12b8','eb4606ff-4ec0-4ffd-980a-a735c2465aeb','\0'),('27a25fc2-29bd-4afd-a135-a5b2b8be12b8','fca7dd02-3fb8-45c2-b72e-0f4e9daeeca5','\0'),('3453a904-cef0-4361-9226-54d6fa84af18','105782f5-b3bb-48d5-8f77-0e5ecbfe116e','\0'),('3453a904-cef0-4361-9226-54d6fa84af18','12cb8b52-bfdf-4650-88be-998a34c53bd8',''),('3453a904-cef0-4361-9226-54d6fa84af18','3e58a816-cd84-48e6-8da1-2baa706dbdda','\0'),('3453a904-cef0-4361-9226-54d6fa84af18','534ed1af-eb77-46d7-b255-348e140a87af',''),('3453a904-cef0-4361-9226-54d6fa84af18','5bfaf140-06b1-46a8-9332-5b2b9520ebb4','\0'),('3453a904-cef0-4361-9226-54d6fa84af18','80bd4364-efe7-4991-a593-067f1c45c7ef','\0'),('3453a904-cef0-4361-9226-54d6fa84af18','ede142ae-567f-46a4-bce5-7e868321805d',''),('3453a904-cef0-4361-9226-54d6fa84af18','f1c5a5e7-78b0-4560-8162-3c5db1dbf768',''),('3453a904-cef0-4361-9226-54d6fa84af18','f820f43b-5e2f-4fd7-9e1a-2d24b88c4b11',''),('364c3e71-9195-4162-9d08-e048421fdd0f','3522f3d8-9fad-4c62-bfc8-000b9091361a',''),('364c3e71-9195-4162-9d08-e048421fdd0f','55164a6a-d9a0-4d36-a289-abafa7fc1551',''),('364c3e71-9195-4162-9d08-e048421fdd0f','7f259547-fc8a-4689-a51f-5991b1a93562',''),('364c3e71-9195-4162-9d08-e048421fdd0f','93b2f0f7-c201-4196-b6bc-2829369f70e2','\0'),('364c3e71-9195-4162-9d08-e048421fdd0f','c6dc943c-f19c-4f65-9116-38d8773fb28d',''),('364c3e71-9195-4162-9d08-e048421fdd0f','d3813bc9-fcb8-4d3e-bd5a-aefd3d4b4ca6',''),('364c3e71-9195-4162-9d08-e048421fdd0f','eb4606ff-4ec0-4ffd-980a-a735c2465aeb','\0'),('364c3e71-9195-4162-9d08-e048421fdd0f','fca7dd02-3fb8-45c2-b72e-0f4e9daeeca5','\0'),('434f2a4d-52d7-4823-badc-54b9835ec942','3522f3d8-9fad-4c62-bfc8-000b9091361a',''),('434f2a4d-52d7-4823-badc-54b9835ec942','55164a6a-d9a0-4d36-a289-abafa7fc1551',''),('434f2a4d-52d7-4823-badc-54b9835ec942','7f259547-fc8a-4689-a51f-5991b1a93562',''),('434f2a4d-52d7-4823-badc-54b9835ec942','93b2f0f7-c201-4196-b6bc-2829369f70e2','\0'),('434f2a4d-52d7-4823-badc-54b9835ec942','c6dc943c-f19c-4f65-9116-38d8773fb28d',''),('434f2a4d-52d7-4823-badc-54b9835ec942','d3813bc9-fcb8-4d3e-bd5a-aefd3d4b4ca6',''),('434f2a4d-52d7-4823-badc-54b9835ec942','eb4606ff-4ec0-4ffd-980a-a735c2465aeb','\0'),('434f2a4d-52d7-4823-badc-54b9835ec942','fca7dd02-3fb8-45c2-b72e-0f4e9daeeca5','\0'),('67d6d3e3-38a7-4562-aac7-03832d713532','3522f3d8-9fad-4c62-bfc8-000b9091361a',''),('67d6d3e3-38a7-4562-aac7-03832d713532','55164a6a-d9a0-4d36-a289-abafa7fc1551',''),('67d6d3e3-38a7-4562-aac7-03832d713532','7f259547-fc8a-4689-a51f-5991b1a93562',''),('67d6d3e3-38a7-4562-aac7-03832d713532','93b2f0f7-c201-4196-b6bc-2829369f70e2','\0'),('67d6d3e3-38a7-4562-aac7-03832d713532','c6dc943c-f19c-4f65-9116-38d8773fb28d',''),('67d6d3e3-38a7-4562-aac7-03832d713532','d3813bc9-fcb8-4d3e-bd5a-aefd3d4b4ca6',''),('67d6d3e3-38a7-4562-aac7-03832d713532','eb4606ff-4ec0-4ffd-980a-a735c2465aeb','\0'),('67d6d3e3-38a7-4562-aac7-03832d713532','fca7dd02-3fb8-45c2-b72e-0f4e9daeeca5','\0'),('7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6','3522f3d8-9fad-4c62-bfc8-000b9091361a',''),('7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6','55164a6a-d9a0-4d36-a289-abafa7fc1551',''),('7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6','7f259547-fc8a-4689-a51f-5991b1a93562',''),('7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6','93b2f0f7-c201-4196-b6bc-2829369f70e2','\0'),('7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6','eb4606ff-4ec0-4ffd-980a-a735c2465aeb','\0'),('7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6','fca7dd02-3fb8-45c2-b72e-0f4e9daeeca5','\0'),('88f76073-729e-461a-aaa3-9fe42dae6a0e','105782f5-b3bb-48d5-8f77-0e5ecbfe116e','\0'),('88f76073-729e-461a-aaa3-9fe42dae6a0e','12cb8b52-bfdf-4650-88be-998a34c53bd8',''),('88f76073-729e-461a-aaa3-9fe42dae6a0e','3e58a816-cd84-48e6-8da1-2baa706dbdda','\0'),('88f76073-729e-461a-aaa3-9fe42dae6a0e','534ed1af-eb77-46d7-b255-348e140a87af',''),('88f76073-729e-461a-aaa3-9fe42dae6a0e','5bfaf140-06b1-46a8-9332-5b2b9520ebb4','\0'),('88f76073-729e-461a-aaa3-9fe42dae6a0e','80bd4364-efe7-4991-a593-067f1c45c7ef','\0'),('88f76073-729e-461a-aaa3-9fe42dae6a0e','ede142ae-567f-46a4-bce5-7e868321805d',''),('88f76073-729e-461a-aaa3-9fe42dae6a0e','f1c5a5e7-78b0-4560-8162-3c5db1dbf768',''),('88f76073-729e-461a-aaa3-9fe42dae6a0e','f820f43b-5e2f-4fd7-9e1a-2d24b88c4b11',''),('9b3c4d13-0a92-46a4-a07c-e412e33024d7','3522f3d8-9fad-4c62-bfc8-000b9091361a',''),('9b3c4d13-0a92-46a4-a07c-e412e33024d7','55164a6a-d9a0-4d36-a289-abafa7fc1551',''),('9b3c4d13-0a92-46a4-a07c-e412e33024d7','7f259547-fc8a-4689-a51f-5991b1a93562',''),('9b3c4d13-0a92-46a4-a07c-e412e33024d7','93b2f0f7-c201-4196-b6bc-2829369f70e2','\0'),('9b3c4d13-0a92-46a4-a07c-e412e33024d7','c6dc943c-f19c-4f65-9116-38d8773fb28d',''),('9b3c4d13-0a92-46a4-a07c-e412e33024d7','d3813bc9-fcb8-4d3e-bd5a-aefd3d4b4ca6',''),('9b3c4d13-0a92-46a4-a07c-e412e33024d7','eb4606ff-4ec0-4ffd-980a-a735c2465aeb','\0'),('9b3c4d13-0a92-46a4-a07c-e412e33024d7','fca7dd02-3fb8-45c2-b72e-0f4e9daeeca5','\0'),('a6cac270-1663-4123-8db5-54ccaaee5871','105782f5-b3bb-48d5-8f77-0e5ecbfe116e','\0'),('a6cac270-1663-4123-8db5-54ccaaee5871','12cb8b52-bfdf-4650-88be-998a34c53bd8',''),('a6cac270-1663-4123-8db5-54ccaaee5871','3e58a816-cd84-48e6-8da1-2baa706dbdda','\0'),('a6cac270-1663-4123-8db5-54ccaaee5871','534ed1af-eb77-46d7-b255-348e140a87af',''),('a6cac270-1663-4123-8db5-54ccaaee5871','5bfaf140-06b1-46a8-9332-5b2b9520ebb4','\0'),('a6cac270-1663-4123-8db5-54ccaaee5871','80bd4364-efe7-4991-a593-067f1c45c7ef','\0'),('a6cac270-1663-4123-8db5-54ccaaee5871','ede142ae-567f-46a4-bce5-7e868321805d',''),('a6cac270-1663-4123-8db5-54ccaaee5871','f1c5a5e7-78b0-4560-8162-3c5db1dbf768',''),('a6cac270-1663-4123-8db5-54ccaaee5871','f820f43b-5e2f-4fd7-9e1a-2d24b88c4b11',''),('c5f79f09-95d5-4a96-a845-47bad8536eb7','105782f5-b3bb-48d5-8f77-0e5ecbfe116e','\0'),('c5f79f09-95d5-4a96-a845-47bad8536eb7','12cb8b52-bfdf-4650-88be-998a34c53bd8',''),('c5f79f09-95d5-4a96-a845-47bad8536eb7','3e58a816-cd84-48e6-8da1-2baa706dbdda','\0'),('c5f79f09-95d5-4a96-a845-47bad8536eb7','534ed1af-eb77-46d7-b255-348e140a87af',''),('c5f79f09-95d5-4a96-a845-47bad8536eb7','5bfaf140-06b1-46a8-9332-5b2b9520ebb4','\0'),('c5f79f09-95d5-4a96-a845-47bad8536eb7','80bd4364-efe7-4991-a593-067f1c45c7ef','\0'),('c5f79f09-95d5-4a96-a845-47bad8536eb7','ede142ae-567f-46a4-bce5-7e868321805d',''),('c5f79f09-95d5-4a96-a845-47bad8536eb7','f1c5a5e7-78b0-4560-8162-3c5db1dbf768',''),('c5f79f09-95d5-4a96-a845-47bad8536eb7','f820f43b-5e2f-4fd7-9e1a-2d24b88c4b11',''),('d6d25c43-45e4-4107-89b4-7fc91bca0f80','105782f5-b3bb-48d5-8f77-0e5ecbfe116e','\0'),('d6d25c43-45e4-4107-89b4-7fc91bca0f80','12cb8b52-bfdf-4650-88be-998a34c53bd8',''),('d6d25c43-45e4-4107-89b4-7fc91bca0f80','3e58a816-cd84-48e6-8da1-2baa706dbdda','\0'),('d6d25c43-45e4-4107-89b4-7fc91bca0f80','534ed1af-eb77-46d7-b255-348e140a87af',''),('d6d25c43-45e4-4107-89b4-7fc91bca0f80','5bfaf140-06b1-46a8-9332-5b2b9520ebb4','\0'),('d6d25c43-45e4-4107-89b4-7fc91bca0f80','80bd4364-efe7-4991-a593-067f1c45c7ef','\0'),('d6d25c43-45e4-4107-89b4-7fc91bca0f80','ede142ae-567f-46a4-bce5-7e868321805d',''),('d6d25c43-45e4-4107-89b4-7fc91bca0f80','f1c5a5e7-78b0-4560-8162-3c5db1dbf768',''),('d6d25c43-45e4-4107-89b4-7fc91bca0f80','f820f43b-5e2f-4fd7-9e1a-2d24b88c4b11',''),('f1b13f7c-a916-4b24-b314-0200496926ce','3522f3d8-9fad-4c62-bfc8-000b9091361a',''),('f1b13f7c-a916-4b24-b314-0200496926ce','55164a6a-d9a0-4d36-a289-abafa7fc1551',''),('f1b13f7c-a916-4b24-b314-0200496926ce','7f259547-fc8a-4689-a51f-5991b1a93562',''),('f1b13f7c-a916-4b24-b314-0200496926ce','93b2f0f7-c201-4196-b6bc-2829369f70e2','\0'),('f1b13f7c-a916-4b24-b314-0200496926ce','c6dc943c-f19c-4f65-9116-38d8773fb28d',''),('f1b13f7c-a916-4b24-b314-0200496926ce','d3813bc9-fcb8-4d3e-bd5a-aefd3d4b4ca6',''),('f1b13f7c-a916-4b24-b314-0200496926ce','eb4606ff-4ec0-4ffd-980a-a735c2465aeb','\0'),('f1b13f7c-a916-4b24-b314-0200496926ce','fca7dd02-3fb8-45c2-b72e-0f4e9daeeca5','\0'),('f9a0e8ea-d2c7-4ea1-b66f-26022f37d23b','105782f5-b3bb-48d5-8f77-0e5ecbfe116e','\0'),('f9a0e8ea-d2c7-4ea1-b66f-26022f37d23b','12cb8b52-bfdf-4650-88be-998a34c53bd8',''),('f9a0e8ea-d2c7-4ea1-b66f-26022f37d23b','3e58a816-cd84-48e6-8da1-2baa706dbdda','\0'),('f9a0e8ea-d2c7-4ea1-b66f-26022f37d23b','534ed1af-eb77-46d7-b255-348e140a87af',''),('f9a0e8ea-d2c7-4ea1-b66f-26022f37d23b','5bfaf140-06b1-46a8-9332-5b2b9520ebb4','\0'),('f9a0e8ea-d2c7-4ea1-b66f-26022f37d23b','80bd4364-efe7-4991-a593-067f1c45c7ef','\0'),('f9a0e8ea-d2c7-4ea1-b66f-26022f37d23b','ede142ae-567f-46a4-bce5-7e868321805d',''),('f9a0e8ea-d2c7-4ea1-b66f-26022f37d23b','f1c5a5e7-78b0-4560-8162-3c5db1dbf768',''),('f9a0e8ea-d2c7-4ea1-b66f-26022f37d23b','f820f43b-5e2f-4fd7-9e1a-2d24b88c4b11','');
/*!40000 ALTER TABLE `CLIENT_SCOPE_CLIENT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT_SCOPE_ROLE_MAPPING`
--

DROP TABLE IF EXISTS `CLIENT_SCOPE_ROLE_MAPPING`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CLIENT_SCOPE_ROLE_MAPPING` (
  `SCOPE_ID` varchar(36) NOT NULL,
  `ROLE_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`SCOPE_ID`,`ROLE_ID`),
  KEY `IDX_CLSCOPE_ROLE` (`SCOPE_ID`),
  KEY `IDX_ROLE_CLSCOPE` (`ROLE_ID`),
  CONSTRAINT `FK_CL_SCOPE_RM_ROLE` FOREIGN KEY (`ROLE_ID`) REFERENCES `KEYCLOAK_ROLE` (`ID`),
  CONSTRAINT `FK_CL_SCOPE_RM_SCOPE` FOREIGN KEY (`SCOPE_ID`) REFERENCES `CLIENT_SCOPE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT_SCOPE_ROLE_MAPPING`
--

LOCK TABLES `CLIENT_SCOPE_ROLE_MAPPING` WRITE;
/*!40000 ALTER TABLE `CLIENT_SCOPE_ROLE_MAPPING` DISABLE KEYS */;
INSERT INTO `CLIENT_SCOPE_ROLE_MAPPING` VALUES ('3522f3d8-9fad-4c62-bfc8-000b9091361a','3d9c5e71-233a-4e29-9e60-f5d9129f9c57'),('5bfaf140-06b1-46a8-9332-5b2b9520ebb4','012f4c09-2656-4603-aac9-4a132a391e94'),('fca7dd02-3fb8-45c2-b72e-0f4e9daeeca5','f4e93900-0b7d-4fa3-b2ca-523715fff9d1');
/*!40000 ALTER TABLE `CLIENT_SCOPE_ROLE_MAPPING` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT_SESSION`
--

DROP TABLE IF EXISTS `CLIENT_SESSION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CLIENT_SESSION` (
  `ID` varchar(36) NOT NULL,
  `CLIENT_ID` varchar(36) DEFAULT NULL,
  `REDIRECT_URI` varchar(255) DEFAULT NULL,
  `STATE` varchar(255) DEFAULT NULL,
  `TIMESTAMP` int(11) DEFAULT NULL,
  `SESSION_ID` varchar(36) DEFAULT NULL,
  `AUTH_METHOD` varchar(255) DEFAULT NULL,
  `REALM_ID` varchar(255) DEFAULT NULL,
  `AUTH_USER_ID` varchar(36) DEFAULT NULL,
  `CURRENT_ACTION` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_CLIENT_SESSION_SESSION` (`SESSION_ID`),
  CONSTRAINT `FK_B4AO2VCVAT6UKAU74WBWTFQO1` FOREIGN KEY (`SESSION_ID`) REFERENCES `USER_SESSION` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT_SESSION`
--

LOCK TABLES `CLIENT_SESSION` WRITE;
/*!40000 ALTER TABLE `CLIENT_SESSION` DISABLE KEYS */;
/*!40000 ALTER TABLE `CLIENT_SESSION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT_SESSION_AUTH_STATUS`
--

DROP TABLE IF EXISTS `CLIENT_SESSION_AUTH_STATUS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CLIENT_SESSION_AUTH_STATUS` (
  `AUTHENTICATOR` varchar(36) NOT NULL,
  `STATUS` int(11) DEFAULT NULL,
  `CLIENT_SESSION` varchar(36) NOT NULL,
  PRIMARY KEY (`CLIENT_SESSION`,`AUTHENTICATOR`),
  CONSTRAINT `AUTH_STATUS_CONSTRAINT` FOREIGN KEY (`CLIENT_SESSION`) REFERENCES `CLIENT_SESSION` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT_SESSION_AUTH_STATUS`
--

LOCK TABLES `CLIENT_SESSION_AUTH_STATUS` WRITE;
/*!40000 ALTER TABLE `CLIENT_SESSION_AUTH_STATUS` DISABLE KEYS */;
/*!40000 ALTER TABLE `CLIENT_SESSION_AUTH_STATUS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT_SESSION_NOTE`
--

DROP TABLE IF EXISTS `CLIENT_SESSION_NOTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CLIENT_SESSION_NOTE` (
  `NAME` varchar(255) NOT NULL,
  `VALUE` varchar(255) DEFAULT NULL,
  `CLIENT_SESSION` varchar(36) NOT NULL,
  PRIMARY KEY (`CLIENT_SESSION`,`NAME`),
  CONSTRAINT `FK5EDFB00FF51C2736` FOREIGN KEY (`CLIENT_SESSION`) REFERENCES `CLIENT_SESSION` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT_SESSION_NOTE`
--

LOCK TABLES `CLIENT_SESSION_NOTE` WRITE;
/*!40000 ALTER TABLE `CLIENT_SESSION_NOTE` DISABLE KEYS */;
/*!40000 ALTER TABLE `CLIENT_SESSION_NOTE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT_SESSION_PROT_MAPPER`
--

DROP TABLE IF EXISTS `CLIENT_SESSION_PROT_MAPPER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CLIENT_SESSION_PROT_MAPPER` (
  `PROTOCOL_MAPPER_ID` varchar(36) NOT NULL,
  `CLIENT_SESSION` varchar(36) NOT NULL,
  PRIMARY KEY (`CLIENT_SESSION`,`PROTOCOL_MAPPER_ID`),
  CONSTRAINT `FK_33A8SGQW18I532811V7O2DK89` FOREIGN KEY (`CLIENT_SESSION`) REFERENCES `CLIENT_SESSION` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT_SESSION_PROT_MAPPER`
--

LOCK TABLES `CLIENT_SESSION_PROT_MAPPER` WRITE;
/*!40000 ALTER TABLE `CLIENT_SESSION_PROT_MAPPER` DISABLE KEYS */;
/*!40000 ALTER TABLE `CLIENT_SESSION_PROT_MAPPER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT_SESSION_ROLE`
--

DROP TABLE IF EXISTS `CLIENT_SESSION_ROLE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CLIENT_SESSION_ROLE` (
  `ROLE_ID` varchar(255) NOT NULL,
  `CLIENT_SESSION` varchar(36) NOT NULL,
  PRIMARY KEY (`CLIENT_SESSION`,`ROLE_ID`),
  CONSTRAINT `FK_11B7SGQW18I532811V7O2DV76` FOREIGN KEY (`CLIENT_SESSION`) REFERENCES `CLIENT_SESSION` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT_SESSION_ROLE`
--

LOCK TABLES `CLIENT_SESSION_ROLE` WRITE;
/*!40000 ALTER TABLE `CLIENT_SESSION_ROLE` DISABLE KEYS */;
/*!40000 ALTER TABLE `CLIENT_SESSION_ROLE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CLIENT_USER_SESSION_NOTE`
--

DROP TABLE IF EXISTS `CLIENT_USER_SESSION_NOTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CLIENT_USER_SESSION_NOTE` (
  `NAME` varchar(255) NOT NULL,
  `VALUE` varchar(2048) DEFAULT NULL,
  `CLIENT_SESSION` varchar(36) NOT NULL,
  PRIMARY KEY (`CLIENT_SESSION`,`NAME`),
  CONSTRAINT `FK_CL_USR_SES_NOTE` FOREIGN KEY (`CLIENT_SESSION`) REFERENCES `CLIENT_SESSION` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CLIENT_USER_SESSION_NOTE`
--

LOCK TABLES `CLIENT_USER_SESSION_NOTE` WRITE;
/*!40000 ALTER TABLE `CLIENT_USER_SESSION_NOTE` DISABLE KEYS */;
/*!40000 ALTER TABLE `CLIENT_USER_SESSION_NOTE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `COMPONENT`
--

DROP TABLE IF EXISTS `COMPONENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `COMPONENT` (
  `ID` varchar(36) NOT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `PARENT_ID` varchar(36) DEFAULT NULL,
  `PROVIDER_ID` varchar(36) DEFAULT NULL,
  `PROVIDER_TYPE` varchar(255) DEFAULT NULL,
  `REALM_ID` varchar(36) DEFAULT NULL,
  `SUB_TYPE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_COMPONENT_REALM` (`REALM_ID`),
  KEY `IDX_COMPONENT_PROVIDER_TYPE` (`PROVIDER_TYPE`),
  CONSTRAINT `FK_COMPONENT_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `COMPONENT`
--

LOCK TABLES `COMPONENT` WRITE;
/*!40000 ALTER TABLE `COMPONENT` DISABLE KEYS */;
INSERT INTO `COMPONENT` VALUES ('056cc5c2-6332-47b4-8163-5eb2e1cb3807','email','8484c070-b463-4a3d-b77e-97ce40f5bb0a','user-attribute-ldap-mapper','org.keycloak.storage.ldap.mappers.LDAPStorageMapper','liferay-portal',NULL),('0a1b32c6-c823-4e63-b414-6522f672ccc3','Allowed Client Scopes','liferay-portal','allowed-client-templates','org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy','liferay-portal','anonymous'),('1123f3a2-aeeb-436d-8d8d-10d63c2e99ba','Allowed Client Scopes','master','allowed-client-templates','org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy','master','anonymous'),('28e62e97-a445-4efa-86d4-d46b17820bb0','rsa-generated','liferay-portal','rsa-generated','org.keycloak.keys.KeyProvider','liferay-portal',NULL),('35a25db2-a60c-4dc9-a04c-d7e669944b29','Consent Required','master','consent-required','org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy','master','anonymous'),('375f68c1-aeaf-43a5-b181-c5a814d7410c','Allowed Protocol Mapper Types','liferay-portal','allowed-protocol-mappers','org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy','liferay-portal','anonymous'),('5e326c40-1e7d-4b6d-a8cc-91dacff056c0','Trusted Hosts','master','trusted-hosts','org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy','master','anonymous'),('64592b11-28fe-4b99-81e9-e30b9d1e3d95','username','8484c070-b463-4a3d-b77e-97ce40f5bb0a','user-attribute-ldap-mapper','org.keycloak.storage.ldap.mappers.LDAPStorageMapper','liferay-portal',NULL),('676fbf1b-df0d-4b7f-b784-1b59acff57b5','aes-generated','liferay-portal','aes-generated','org.keycloak.keys.KeyProvider','liferay-portal',NULL),('68b48bd4-d709-49af-ba37-7398b2f8c5b4','Max Clients Limit','liferay-portal','max-clients','org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy','liferay-portal','anonymous'),('6ae1ef70-c65c-4f14-a089-d30e50f474e2','rsa-generated','master','rsa-generated','org.keycloak.keys.KeyProvider','master',NULL),('6d585d10-c207-4988-893e-0d53009fb17f','Full Scope Disabled','master','scope','org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy','master','anonymous'),('7599466f-ef4e-4482-88e7-537e2ff9ea6a','Allowed Client Scopes','liferay-portal','allowed-client-templates','org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy','liferay-portal','authenticated'),('7dfe4eaa-9a70-435d-bbd0-3edd33ca837b','hmac-generated','liferay-portal','hmac-generated','org.keycloak.keys.KeyProvider','liferay-portal',NULL),('8484c070-b463-4a3d-b77e-97ce40f5bb0a','openldap','liferay-portal','ldap','org.keycloak.storage.UserStorageProvider','liferay-portal',NULL),('856b0103-1268-4822-a388-500eb2eec903','last name','8484c070-b463-4a3d-b77e-97ce40f5bb0a','user-attribute-ldap-mapper','org.keycloak.storage.ldap.mappers.LDAPStorageMapper','liferay-portal',NULL),('93039206-19a8-459f-b6a7-15eb78bdc411','Allowed Client Scopes','master','allowed-client-templates','org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy','master','authenticated'),('93ae25ff-6c32-428a-a878-a5c421aaa48a','Allowed Protocol Mapper Types','master','allowed-protocol-mappers','org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy','master','anonymous'),('a76b14da-a3ec-4633-84f1-9f415a56120d','Allowed Protocol Mapper Types','master','allowed-protocol-mappers','org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy','master','authenticated'),('a7b2235b-7322-4895-8c08-fb9ff2bb8fd9','modify date','8484c070-b463-4a3d-b77e-97ce40f5bb0a','user-attribute-ldap-mapper','org.keycloak.storage.ldap.mappers.LDAPStorageMapper','liferay-portal',NULL),('ad492ca5-e112-499e-9b23-30e360770127','aes-generated','master','aes-generated','org.keycloak.keys.KeyProvider','master',NULL),('b8bf10ce-a53b-4eb3-9d5f-ea9455551c29','creation date','8484c070-b463-4a3d-b77e-97ce40f5bb0a','user-attribute-ldap-mapper','org.keycloak.storage.ldap.mappers.LDAPStorageMapper','liferay-portal',NULL),('ba7a1f32-fff1-4380-9941-1f437d928164','Full Scope Disabled','liferay-portal','scope','org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy','liferay-portal','anonymous'),('c4bcf24d-f3d9-4252-b594-9298cb98217d','Max Clients Limit','master','max-clients','org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy','master','anonymous'),('c86fc1b2-504a-4417-bd42-779d46dcceab','Trusted Hosts','liferay-portal','trusted-hosts','org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy','liferay-portal','anonymous'),('f0b85e83-55b5-4129-a0d7-9e0f930434c4','Allowed Protocol Mapper Types','liferay-portal','allowed-protocol-mappers','org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy','liferay-portal','authenticated'),('fa24c2ee-61a9-48ac-baaa-0dd6a806b77d','Consent Required','liferay-portal','consent-required','org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy','liferay-portal','anonymous'),('fadc8c67-6aa2-4d2e-b7ab-c118b9791c53','first name','8484c070-b463-4a3d-b77e-97ce40f5bb0a','user-attribute-ldap-mapper','org.keycloak.storage.ldap.mappers.LDAPStorageMapper','liferay-portal',NULL),('ff8b2cad-b703-465b-a930-c3c1b7bae01d','hmac-generated','master','hmac-generated','org.keycloak.keys.KeyProvider','master',NULL);
/*!40000 ALTER TABLE `COMPONENT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `COMPONENT_CONFIG`
--

DROP TABLE IF EXISTS `COMPONENT_CONFIG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `COMPONENT_CONFIG` (
  `ID` varchar(36) NOT NULL,
  `COMPONENT_ID` varchar(36) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `VALUE` varchar(4000) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_COMPO_CONFIG_COMPO` (`COMPONENT_ID`),
  CONSTRAINT `FK_COMPONENT_CONFIG` FOREIGN KEY (`COMPONENT_ID`) REFERENCES `COMPONENT` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `COMPONENT_CONFIG`
--

LOCK TABLES `COMPONENT_CONFIG` WRITE;
/*!40000 ALTER TABLE `COMPONENT_CONFIG` DISABLE KEYS */;
INSERT INTO `COMPONENT_CONFIG` VALUES ('02cfe62e-365e-4f94-885e-59c4c4a04d2e','fadc8c67-6aa2-4d2e-b7ab-c118b9791c53','always.read.value.from.ldap','true'),('04f426fe-9074-48ec-ae3a-1154f4ff48fc','93ae25ff-6c32-428a-a878-a5c421aaa48a','allowed-protocol-mapper-types','oidc-usermodel-property-mapper'),('057da1bb-1dea-4863-9baa-e309361bb737','64592b11-28fe-4b99-81e9-e30b9d1e3d95','read.only','false'),('05a51ff5-f129-43c3-84e2-b4d07b62d5db','6ae1ef70-c65c-4f14-a089-d30e50f474e2','privateKey','MIIEpAIBAAKCAQEAmZ3hJUUnUbhLWiiunP14hS6sd9/4Bzk/S1A2xsZIw4ZObGdl7wyujRLwKyKPBAxl80cE/aDvmW97NLIT0m0tCXJXSLRLmv5E3YlSwgrD7kfrggVtZll/eULWWtUetlLQycsGWE1pAIIpRqdN7nM6ni2GLSVUHlstlQ7Qx7hsspxmBJ+k3EFg3EjLY6v7ekymbNFcsumzdX8qPYB4O3EmJOT6+Mi0vz28TE02DuzeEjiMmUcD47pVV/kkhVK5zlr0ZoD8tK7jM88yNdksE2js3oassUgydjeX0LEn6xQbkwkg1/KuW0gIlWcSx4M1pzksB9t2acUi1G7elTCLnoTIEQIDAQABAoIBAQCKFBt4ALzi+6DfnYm2my9lpxpnde3fsFlwPN2/tiNZfLOpRRee0+th6w/t9dZPcUWCcufNgodbh5VbPGYM0w2UXC6Qr172Jm+IIhM9T6fl2seZy38WbIPkX2uFoeNFRxNdDxzY6lvjlRu84k1udCzQWWMt2UvPLW2/ed24Z9FsiTtrG2AMx9hyyBHhiw8a4fUNFQwe3VJUnUemb33gj2hHfvvdlfKckkWglT/AprhQXgjynJ7Nr36aJty4Gm7E5Yn/LXufDLL327ikcAJT5RpRW1nmWBlskT7qE2atCmCOmo9/wrazchd2FgPBogY4/Z8CQIDWCKxs2ACuIRAvDsOxAoGBAM6vOVTaaJH1HoYWEbbsvw3LF15QgZQ0TkHDMDoomTKgOn385VJR9s2xHfwoBQ9k/BRoFQiCcWvISrkFXKbmZZejR8+XAbYo+qaKsJahZDbtKhebxXcF2ozW8WXB8O9NWggNnieI2RKuQz4Zb2iriwwzynKCbbJ0Etb3Od9BUV8DAoGBAL5FJtnI1Lw44Wpfx/t7zOfQzt2Qy7jBfzexiwfSJ9t/5/3/lTq/cV8ze26U6pWctk0vCG4q5pNDw2OGiSOmHDNsMFDsM/SLhcfmbo5NJsWYIJRP0nOpVVygbMky62JCeNFe6SiG6wcuTLmdusPyvvfvUz+jXgipoIwMlOaqj1ZbAoGBAMOO5acUtGaNz+C81uwGm0r4w1aYw9Ivn2/3uBT/Pty44D8+3tgB94xtrlpcn1UOjrjcNjKEAb8ilxjzKtQ8IRnWA2c2kQcDRQ35IdaURBb4VaBa4f6ZCk3CPkuysj4qKSp/gAoWIJXdToBeXpxh20Gja0TnhdWYoA7HgvlrewtzAoGAK2jLfFR9dJPlmcdQcOfFz/2rtJEcaYJ769Dtz1L7FPERlLmC6mxTCU1Nk+UVwByI2wMoATp4cJC9Oy+uihuTw4ejXlMFZ32NgBXqkzy2483ZYPz1Hr47Y26WYzDi0wAnYF0vBLgDVxgRGSgLPDldqGNJsvMquP86Sbl1H0s6eJECgYBdcy+YezonJ4pgvc9jvrbp6VU/F37Ubnzsa7nSB4iWhRM/iTXCDCpVQ2j1hexSUd4bJTZNff1ylj4picWbRG0BAt+DGT3/hyQuknPqrvRKCL75UST7mGwYaTmLJvUSebg3UxSqnsPnDareic1+hmvKqWfIFGtSFb9J28G6TM4aOA=='),('082002c9-3537-4d71-adc9-22739871b739','375f68c1-aeaf-43a5-b181-c5a814d7410c','allowed-protocol-mapper-types','saml-user-attribute-mapper'),('0c41eea2-d8cb-48a5-8f47-4580e2637eab','fadc8c67-6aa2-4d2e-b7ab-c118b9791c53','user.model.attribute','firstName'),('0c47cc7e-d739-4f09-96d5-7210801d5e2d','f0b85e83-55b5-4129-a0d7-9e0f930434c4','allowed-protocol-mapper-types','saml-user-property-mapper'),('0dfe0bd3-dae4-43d3-bce8-bf1df9152e33','8484c070-b463-4a3d-b77e-97ce40f5bb0a','bindDn','cn=admin,dc=oss,dc=deltares,dc=nl'),('0e455373-44ff-429f-a6d2-68355267c633','ff8b2cad-b703-465b-a930-c3c1b7bae01d','priority','100'),('1093e75d-615f-4a87-827d-5813d88abaf2','8484c070-b463-4a3d-b77e-97ce40f5bb0a','rdnLDAPAttribute','cn'),('14cb0aa7-d597-4f24-963c-77fe87483c43','056cc5c2-6332-47b4-8163-5eb2e1cb3807','is.mandatory.in.ldap','false'),('173268b9-cd35-4b0c-8fb6-5ca038843af0','8484c070-b463-4a3d-b77e-97ce40f5bb0a','bindCredential','**********'),('1cd0cf13-484a-4be2-a153-58a110289e24','ff8b2cad-b703-465b-a930-c3c1b7bae01d','algorithm','HS256'),('1fcf767c-373a-472d-a5f8-86c64fe32fe3','93ae25ff-6c32-428a-a878-a5c421aaa48a','allowed-protocol-mapper-types','oidc-sha256-pairwise-sub-mapper'),('203279e7-ffa5-4e2c-812d-ffc8828ab102','7599466f-ef4e-4482-88e7-537e2ff9ea6a','allow-default-scopes','true'),('2214e9a7-a101-4490-9cf7-9955151d7e97','c4bcf24d-f3d9-4252-b594-9298cb98217d','max-clients','200'),('22aec104-b280-43e7-8e0b-1347784d1bd9','8484c070-b463-4a3d-b77e-97ce40f5bb0a','useKerberosForPasswordAuthentication','false'),('236af015-8b75-4978-9375-a4a91cd7f86c','375f68c1-aeaf-43a5-b181-c5a814d7410c','allowed-protocol-mapper-types','oidc-sha256-pairwise-sub-mapper'),('236d24f2-4431-4ff9-899a-edb5f7350d43','a76b14da-a3ec-4633-84f1-9f415a56120d','allowed-protocol-mapper-types','saml-user-property-mapper'),('23d22970-46be-45d7-b81e-96e340664974','8484c070-b463-4a3d-b77e-97ce40f5bb0a','vendor','other'),('2482aeeb-d454-4d2c-b6c8-3e55e3b82629','7dfe4eaa-9a70-435d-bbd0-3edd33ca837b','algorithm','HS256'),('2625588d-4abe-42b7-bf92-3fc5017f2a7e','856b0103-1268-4822-a388-500eb2eec903','ldap.attribute','sn'),('27a1ed9e-dd9c-4e2f-a119-084ec618560e','856b0103-1268-4822-a388-500eb2eec903','is.mandatory.in.ldap','true'),('2e0f78c2-e4e1-4b6a-8817-c86ffacd9374','8484c070-b463-4a3d-b77e-97ce40f5bb0a','allowKerberosAuthentication','false'),('2e7a0e40-f723-4dfa-961f-4f7184dc5c65','a76b14da-a3ec-4633-84f1-9f415a56120d','allowed-protocol-mapper-types','oidc-usermodel-attribute-mapper'),('312065ae-456f-4bbb-b3ae-9c3ff57c800c','a7b2235b-7322-4895-8c08-fb9ff2bb8fd9','ldap.attribute','modifyTimestamp'),('3495e11f-d933-454e-a52c-4115aaedd76b','93ae25ff-6c32-428a-a878-a5c421aaa48a','allowed-protocol-mapper-types','saml-role-list-mapper'),('387d6b24-5957-443d-8a32-33ef112ab430','8484c070-b463-4a3d-b77e-97ce40f5bb0a','lastSync','1612453774'),('39dd08e2-c5ed-4057-8791-4e805e90d47d','676fbf1b-df0d-4b7f-b784-1b59acff57b5','priority','100'),('3b4fc3d7-d3df-4239-a700-b64b30c45450','64592b11-28fe-4b99-81e9-e30b9d1e3d95','ldap.attribute','cn'),('3bb1b06b-30cd-428a-b153-8ecaed473d25','f0b85e83-55b5-4129-a0d7-9e0f930434c4','allowed-protocol-mapper-types','oidc-usermodel-attribute-mapper'),('3e65ab85-0a09-4250-919d-4d0d70b7e0d4','8484c070-b463-4a3d-b77e-97ce40f5bb0a','useTruststoreSpi','ldapsOnly'),('4082c4ef-b9d1-40d5-933c-71860f97be77','8484c070-b463-4a3d-b77e-97ce40f5bb0a','validatePasswordPolicy','false'),('457f99db-02bc-4ff6-9574-6752565f3c00','b8bf10ce-a53b-4eb3-9d5f-ea9455551c29','is.mandatory.in.ldap','false'),('45fca1cd-b531-4c2b-ad1f-718c15238240','8484c070-b463-4a3d-b77e-97ce40f5bb0a','changedSyncPeriod','-1'),('46a4b808-5fe4-47b0-8c48-904d3e7f78f6','375f68c1-aeaf-43a5-b181-c5a814d7410c','allowed-protocol-mapper-types','oidc-full-name-mapper'),('46aa0250-aeca-4b29-b92a-f59a9ce5c2ce','f0b85e83-55b5-4129-a0d7-9e0f930434c4','allowed-protocol-mapper-types','oidc-sha256-pairwise-sub-mapper'),('48ed6007-da86-4e27-a386-e8a4e99be521','a76b14da-a3ec-4633-84f1-9f415a56120d','allowed-protocol-mapper-types','oidc-full-name-mapper'),('4a87e2f4-d118-457a-89fe-c2f05cf53e21','a7b2235b-7322-4895-8c08-fb9ff2bb8fd9','read.only','true'),('4b55fa14-ca2d-45de-a239-7ab908184f58','676fbf1b-df0d-4b7f-b784-1b59acff57b5','kid','bf7fb815-6983-4508-86d2-53ebfbbf045d'),('54ccb853-dcc0-4037-a4af-7c8dd0e7d6e6','856b0103-1268-4822-a388-500eb2eec903','always.read.value.from.ldap','true'),('56444457-180d-4e72-bd8b-2841fb8f3308','056cc5c2-6332-47b4-8163-5eb2e1cb3807','user.model.attribute','email'),('56b4151d-962c-4532-aee6-fc613993550c','f0b85e83-55b5-4129-a0d7-9e0f930434c4','allowed-protocol-mapper-types','oidc-full-name-mapper'),('58f61d5e-ee13-4c27-a535-7ba8edb7c14e','64592b11-28fe-4b99-81e9-e30b9d1e3d95','is.mandatory.in.ldap','true'),('5ac0a2c1-7259-4242-90a4-c865052d9b62','056cc5c2-6332-47b4-8163-5eb2e1cb3807','always.read.value.from.ldap','false'),('5b6f3aba-680f-4fa2-960b-c6a60e3c0cc5','6ae1ef70-c65c-4f14-a089-d30e50f474e2','priority','100'),('5d5b2c1e-c5ae-4842-80ec-be6495aaad04','375f68c1-aeaf-43a5-b181-c5a814d7410c','allowed-protocol-mapper-types','oidc-address-mapper'),('5f12dfa7-cbe6-46f3-99f9-b73c53744b38','b8bf10ce-a53b-4eb3-9d5f-ea9455551c29','read.only','true'),('608e2b18-17f5-4a14-a89f-5e3801f65753','ad492ca5-e112-499e-9b23-30e360770127','kid','6473b004-0bb8-4761-97b5-a95d2e6eec67'),('6118ea73-4150-4342-acf8-17f6aec3b0ec','8484c070-b463-4a3d-b77e-97ce40f5bb0a','usernameLDAPAttribute','cn'),('61fde1b0-6410-4d13-bc4c-40df0a8e81f5','64592b11-28fe-4b99-81e9-e30b9d1e3d95','user.model.attribute','username'),('63b1e313-63b4-4a6d-8420-c8d0020a0915','a76b14da-a3ec-4633-84f1-9f415a56120d','allowed-protocol-mapper-types','saml-role-list-mapper'),('66405329-2bb1-4ad1-b8bc-12d29dd7d4bb','f0b85e83-55b5-4129-a0d7-9e0f930434c4','allowed-protocol-mapper-types','oidc-usermodel-property-mapper'),('69395147-4818-4967-a841-5ef607152c7c','856b0103-1268-4822-a388-500eb2eec903','read.only','false'),('6d3e4efe-024c-4564-83c2-5745bf64af61','93039206-19a8-459f-b6a7-15eb78bdc411','allow-default-scopes','true'),('7015fa78-04e8-4f28-b964-536201160f8a','8484c070-b463-4a3d-b77e-97ce40f5bb0a','connectionPooling','true'),('73a2af0e-469e-468a-bd4d-6a4f9943c7fa','375f68c1-aeaf-43a5-b181-c5a814d7410c','allowed-protocol-mapper-types','oidc-usermodel-attribute-mapper'),('74e5d0a1-d9c7-4716-a08d-3f4c95e660e2','a7b2235b-7322-4895-8c08-fb9ff2bb8fd9','always.read.value.from.ldap','true'),('759c6b54-5c44-4cab-affc-27c431873a7c','fadc8c67-6aa2-4d2e-b7ab-c118b9791c53','read.only','false'),('7722e521-b7bf-497e-bdcd-e50bd875e027','7dfe4eaa-9a70-435d-bbd0-3edd33ca837b','priority','100'),('78028f45-299c-421a-abb9-f48c551c99f0','8484c070-b463-4a3d-b77e-97ce40f5bb0a','userObjectClasses','inetOrgPerson'),('79e11d14-253b-438e-9a2e-e092b6d0bc9e','ff8b2cad-b703-465b-a930-c3c1b7bae01d','kid','b2a9fba1-735d-48d7-b4fe-bd5f158a6ee2'),('7df70e59-a633-45cd-82f4-aeb790ea6c33','93ae25ff-6c32-428a-a878-a5c421aaa48a','allowed-protocol-mapper-types','oidc-address-mapper'),('7e5e1c34-5d1c-4b27-96a9-69f60f526d08','a76b14da-a3ec-4633-84f1-9f415a56120d','allowed-protocol-mapper-types','oidc-address-mapper'),('80045517-4331-41b2-a90d-22b553c67a59','8484c070-b463-4a3d-b77e-97ce40f5bb0a','pagination','true'),('81a10785-59fd-49f6-9115-9c52ee335a34','a7b2235b-7322-4895-8c08-fb9ff2bb8fd9','user.model.attribute','modifyTimestamp'),('833133bc-5092-46ff-8712-3e7a7caffa33','fadc8c67-6aa2-4d2e-b7ab-c118b9791c53','is.mandatory.in.ldap','true'),('8473ce5e-21f5-4042-9df9-d560b102c3c1','93ae25ff-6c32-428a-a878-a5c421aaa48a','allowed-protocol-mapper-types','oidc-usermodel-attribute-mapper'),('863b1aac-79bd-44e8-ad23-f0a1e54ef60a','fadc8c67-6aa2-4d2e-b7ab-c118b9791c53','ldap.attribute','givenName'),('86417ddf-f058-4ab3-9f1f-b2bf2896fb0a','ff8b2cad-b703-465b-a930-c3c1b7bae01d','secret','KqewE5Lb3I_uZ8twJI3VhokMLj94qBMElA9OleVtBv1Xd3zOm6TbPfaJY5kAIaOAWd6gaMKphjtKpWem9-3kng'),('871200be-4a3c-4a9c-ae01-40b2d87bdfcc','b8bf10ce-a53b-4eb3-9d5f-ea9455551c29','always.read.value.from.ldap','true'),('8770b069-e3b4-4923-a1d5-36f9fd9631d8','8484c070-b463-4a3d-b77e-97ce40f5bb0a','debug','false'),('8aecd323-007c-4a4e-9626-8030ddd134c3','056cc5c2-6332-47b4-8163-5eb2e1cb3807','ldap.attribute','mail'),('8c9b70f8-96b4-4c91-9093-83517090703f','8484c070-b463-4a3d-b77e-97ce40f5bb0a','authType','simple'),('9396855a-6641-4a55-9234-3f322e0a288a','f0b85e83-55b5-4129-a0d7-9e0f930434c4','allowed-protocol-mapper-types','saml-role-list-mapper'),('94f05a1b-72a9-45af-835a-de63080e88f4','1123f3a2-aeeb-436d-8d8d-10d63c2e99ba','allow-default-scopes','true'),('95ae576a-5064-42a0-9483-9f7288db3320','93ae25ff-6c32-428a-a878-a5c421aaa48a','allowed-protocol-mapper-types','saml-user-property-mapper'),('963c3f9a-1d19-48db-a510-aa0ff31989ad','a7b2235b-7322-4895-8c08-fb9ff2bb8fd9','is.mandatory.in.ldap','false'),('96db5097-4623-4ee3-9165-bafc32d6bdc5','8484c070-b463-4a3d-b77e-97ce40f5bb0a','batchSizeForSync','1000'),('97ac24d4-a566-4ae5-8461-a277ad1792da','f0b85e83-55b5-4129-a0d7-9e0f930434c4','allowed-protocol-mapper-types','saml-user-attribute-mapper'),('99549fe6-bec6-47cc-811a-b2555e90b6fb','a76b14da-a3ec-4633-84f1-9f415a56120d','allowed-protocol-mapper-types','oidc-sha256-pairwise-sub-mapper'),('9a5c249e-e41e-4ef5-9fbb-22f2f74c888c','93ae25ff-6c32-428a-a878-a5c421aaa48a','allowed-protocol-mapper-types','saml-user-attribute-mapper'),('a2ed484c-c899-4258-84f9-8af276c5850e','056cc5c2-6332-47b4-8163-5eb2e1cb3807','read.only','false'),('a4e45fec-4bc7-4b22-a3ad-2195c9c00d5f','64592b11-28fe-4b99-81e9-e30b9d1e3d95','always.read.value.from.ldap','false'),('a62c2b83-6723-42df-8039-4a3b299228ec','5e326c40-1e7d-4b6d-a8cc-91dacff056c0','host-sending-registration-request-must-match','true'),('acbd5a0a-fb62-44eb-9144-49c73759bffa','a76b14da-a3ec-4633-84f1-9f415a56120d','allowed-protocol-mapper-types','oidc-usermodel-property-mapper'),('adbb33b5-20dd-4f89-810e-933aafda56cd','375f68c1-aeaf-43a5-b181-c5a814d7410c','allowed-protocol-mapper-types','saml-role-list-mapper'),('ae951b87-8c45-4430-a795-a96205f02c2c','8484c070-b463-4a3d-b77e-97ce40f5bb0a','searchScope','1'),('b0c44b03-8ba5-44db-b0e4-ef62725f36ee','8484c070-b463-4a3d-b77e-97ce40f5bb0a','cachePolicy','DEFAULT'),('b0d599ca-11c5-474c-816e-abc1eab23bb9','ad492ca5-e112-499e-9b23-30e360770127','priority','100'),('b6112b75-c2f8-4c34-af7a-959111f66ab6','7dfe4eaa-9a70-435d-bbd0-3edd33ca837b','kid','900e6d19-44f2-4639-8966-0487c5fee5e8'),('bc413dac-c4a7-46e0-a20b-8efa9512b76f','c86fc1b2-504a-4417-bd42-779d46dcceab','client-uris-must-match','true'),('beb79b01-dee1-4ad0-af6c-9b5824585169','8484c070-b463-4a3d-b77e-97ce40f5bb0a','connectionUrl','ldap://openldap:1389'),('bf1fc3a2-1020-4264-b1a7-9a9c8463aa0c','c86fc1b2-504a-4417-bd42-779d46dcceab','host-sending-registration-request-must-match','true'),('c0af0e31-9193-44fd-b312-b5bffe35c344','28e62e97-a445-4efa-86d4-d46b17820bb0','privateKey','MIIEpAIBAAKCAQEAppxrDWKdfWSfto9rAhDsUEixynR85nhCAsG6SPL9PmQ5y+UT+9kTTyfN4d1CAaE8CTd6K34Swtor6+3IW6VmA1TwyCo4IPCsipe0ilDEUtdsHafpPa36MnyKiceaA6UrTFn9Hgt+F2b0KzYw2bPwZz7ptkOmQMETcb7FbbJt2SeF/jiTJMyEjiiRN32xlxAZeVSxCCFJzkkAwPOtksKQGK1t2loqOeFwlWag/fkSx5JB6SdGsvosI0rcGlkKeyTzcnvJIpmkeZAh87BWu/m7GEEeT7wGuTxWQDcBO/fVnlBzLc9z6NPar8FDznE+BBDMNy9hrg0zWwClE6Hh4hHkHwIDAQABAoIBAFA9pwUkSGsaqhzXR5cQJW+pAw9ZOMoagtgnCxuVA0goJvm+0vgJHNaTegFceSylxW8+67TEnC6BuoN+2m7g4peaMfiE9NFQeBirZYTzzwmCvIwv0RImlwf3mRUwL0mwI4E7Gb99k3Wb3N5u0+oVUiO3VgG05P3svvuaACWLuIHLUuN+AS+V2lXvaCj0RAp0SxEW3xH6b5RVKXpX4LXpOKlALsV8sA4wMKvCIXbG9QAWOB2TF5XdeyMXlspuDNT5nGBxzZMmnMC55Z44K7It/f4sauSHkhJdtDGx8qeqPZ301znZO5jeOKqoMz+zObDzooKBzKfEEgsM7Bz4o521sfECgYEA2xDQ2So/w+RbKPQp9Zlz1Cnv5CAEpqO346oeeE1DNsD9Rd72DDcdGCrNPt782/28gVBxCl9VOSG643lzc6JGW20DgV0tQqNexNtZEk4bXiMCfZIhdDoPCekLbiNoj3nrVAhEwDrY3iI7kE6sgFKm6ei5RB/ZBUIbSOKS87pOwZcCgYEAwrOVnJawliokHE2CwKHd7FLn/Z8DeSeHBGuWYbZJST84HeDwK2LvLfDVeWAWPkY4R71e7htc+4Bkga8bsatS8JtOfllQQTyeTfvZeFXQin2PE/h+Bo3gHqhPIy/QZrBmNxNvUw4mtRLbLmb8fb8PtoHZQpWq/wV6oyt2wm97srkCgYBl1UZQfSxRIpkhy4apzaCjcq86S4v97sMvyMgLQldJX6Mc1M1p1kDnGBZ68Gt41FXJgxxZJwHV4hEfZzi8+yVboKxteWPtw0Xtms2oDbj5JKsmtkRo5AhTogqXM0yKzbEQShuzhGeFs0IfDgNwT3uZ5n4g5fEbWaSXWAPZ2rR7lwKBgQCCVbM+aoV1UGpx49U2XGTx0SHpXmv1y2cPp3G4oUz30nEfVzeO46CW0VQl9xOMxnbgVz1YCUs6NN2TDoRsb/KKjHyECge2U/zo2UHf0xpRyvFffgXnH0w7WZH4TQal50nVbpYkNCHnLuM4ccpt5tt+4fdtIkGgi6tn1S2fxP86SQKBgQC+y8OU/r7kQKBG5ETNyGlQJ9dSWfpHs0cwzv1XxpA0Y5uGom4hQt1EwYCfrlV8BbweqVikA1qR4ojnK87KI9Z0JdKizFmTerca1iG4AGseGgZQlsyjHMvCxmaasJEDjkjnEgN26IYQCGaKhAahilb46TXi1EctgmGwXSRr9MmJMg=='),('c2e4119a-8dcf-463a-88a7-8667a4ee2041','8484c070-b463-4a3d-b77e-97ce40f5bb0a','enabled','false'),('c31c4276-c0d1-4376-b6bf-eba506422d96','856b0103-1268-4822-a388-500eb2eec903','user.model.attribute','lastName'),('c33dd71e-942c-4ac9-b65e-d4ff1aedb7fb','8484c070-b463-4a3d-b77e-97ce40f5bb0a','usersDn','ou=people,dc=oss,dc=deltares,dc=nl'),('c4a6a41f-b574-4cb0-bc9c-c472dc55438a','8484c070-b463-4a3d-b77e-97ce40f5bb0a','priority','0'),('c94efc3c-03f6-4a14-9412-54e6e0dfeb86','6ae1ef70-c65c-4f14-a089-d30e50f474e2','certificate','MIICmzCCAYMCBgF4+S80pzANBgkqhkiG9w0BAQsFADARMQ8wDQYDVQQDDAZtYXN0ZXIwHhcNMjEwNDIyMTA0MzQzWhcNMzEwNDIyMTA0NTIzWjARMQ8wDQYDVQQDDAZtYXN0ZXIwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCZneElRSdRuEtaKK6c/XiFLqx33/gHOT9LUDbGxkjDhk5sZ2XvDK6NEvArIo8EDGXzRwT9oO+Zb3s0shPSbS0JcldItEua/kTdiVLCCsPuR+uCBW1mWX95QtZa1R62UtDJywZYTWkAgilGp03uczqeLYYtJVQeWy2VDtDHuGyynGYEn6TcQWDcSMtjq/t6TKZs0Vyy6bN1fyo9gHg7cSYk5Pr4yLS/PbxMTTYO7N4SOIyZRwPjulVX+SSFUrnOWvRmgPy0ruMzzzI12SwTaOzehqyxSDJ2N5fQsSfrFBuTCSDX8q5bSAiVZxLHgzWnOSwH23ZpxSLUbt6VMIuehMgRAgMBAAEwDQYJKoZIhvcNAQELBQADggEBACu1hQEqdUzpJNNu61F+6Z/D3PR4eIQ1Oq30P7zsfLXY2+gEQ3ERYxlhVjYYn8Nqs3WyM1M5ms3l4VAyYTE9AhyObpToFlx7XvnskDUSkVEC/AkXCPElx5nO5kx/WCddlg9YaUpe9FRoMlVznefYNjpobAhBkXk/l/PebwW3JpklQWKvGYUflymXswP/RnCnTmS5+yZPK5WR6w23jvhBTnkYtwJltow6BxPcUZ+a/OebU8bzrQN7gXc4J6E0DkYdy5NujkS/mCta7XeOrUPe2v11NAs0f0I2Gu4TK3wn+K4QmMoBvnQUYscozhIJJnkAccC5rg1eO7695rzCzxnWQ2c='),('cb314622-f575-4cd1-ac13-24324e19f0f8','b8bf10ce-a53b-4eb3-9d5f-ea9455551c29','ldap.attribute','createTimestamp'),('cb4e1804-9610-4cbd-ab01-0498781c053d','8484c070-b463-4a3d-b77e-97ce40f5bb0a','syncRegistrations','true'),('cb567eb4-3733-43d5-8f4c-1b9d250ca584','8484c070-b463-4a3d-b77e-97ce40f5bb0a','fullSyncPeriod','-1'),('ce75ca56-cf7f-4a70-96cd-c02d5401e72c','7dfe4eaa-9a70-435d-bbd0-3edd33ca837b','secret','VxTHjkMP25vo5L5pj0kvNGvqfXSC6ZFkhCxiVOtMNLjrK1GTurbToMs8hbGuixSgCWymDEMl4K7gNWtwBZspfA'),('d017cea9-933f-4860-a32b-e22b7b269c8e','f0b85e83-55b5-4129-a0d7-9e0f930434c4','allowed-protocol-mapper-types','oidc-address-mapper'),('d304dc7a-7206-46e2-9769-f6b6502893fa','375f68c1-aeaf-43a5-b181-c5a814d7410c','allowed-protocol-mapper-types','saml-user-property-mapper'),('d32e8f2c-6cc3-4261-84d2-00f88a9d5ec1','8484c070-b463-4a3d-b77e-97ce40f5bb0a','importEnabled','true'),('d4d312d0-3216-4169-bd5a-dfc66f551526','68b48bd4-d709-49af-ba37-7398b2f8c5b4','max-clients','200'),('d90acd91-800a-4552-bd53-e1be225db3b2','8484c070-b463-4a3d-b77e-97ce40f5bb0a','editMode','WRITABLE'),('da2d928d-097f-47c1-a033-0b6c26b5f307','93ae25ff-6c32-428a-a878-a5c421aaa48a','allowed-protocol-mapper-types','oidc-full-name-mapper'),('e3ee43c9-8f35-469d-b5ca-3174d1da9750','ad492ca5-e112-499e-9b23-30e360770127','secret','InWT2F0nCIqEti4BfqHYRg'),('eae74386-fc0d-4fe6-9f55-006f576cc9c9','5e326c40-1e7d-4b6d-a8cc-91dacff056c0','client-uris-must-match','true'),('eeadea9b-fdee-44d9-b294-281a7eb33804','676fbf1b-df0d-4b7f-b784-1b59acff57b5','secret','HvuT07Wfcz6AKGfoqPlN3Q'),('ef944d96-a469-43ac-ab68-3d2ceb8b26d6','28e62e97-a445-4efa-86d4-d46b17820bb0','certificate','MIICqzCCAZMCBgF4+S86xDANBgkqhkiG9w0BAQsFADAZMRcwFQYDVQQDDA5saWZlcmF5LXBvcnRhbDAeFw0yMTA0MjIxMDQzNDVaFw0zMTA0MjIxMDQ1MjVaMBkxFzAVBgNVBAMMDmxpZmVyYXktcG9ydGFsMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAppxrDWKdfWSfto9rAhDsUEixynR85nhCAsG6SPL9PmQ5y+UT+9kTTyfN4d1CAaE8CTd6K34Swtor6+3IW6VmA1TwyCo4IPCsipe0ilDEUtdsHafpPa36MnyKiceaA6UrTFn9Hgt+F2b0KzYw2bPwZz7ptkOmQMETcb7FbbJt2SeF/jiTJMyEjiiRN32xlxAZeVSxCCFJzkkAwPOtksKQGK1t2loqOeFwlWag/fkSx5JB6SdGsvosI0rcGlkKeyTzcnvJIpmkeZAh87BWu/m7GEEeT7wGuTxWQDcBO/fVnlBzLc9z6NPar8FDznE+BBDMNy9hrg0zWwClE6Hh4hHkHwIDAQABMA0GCSqGSIb3DQEBCwUAA4IBAQCMW2O/eEFmZDSDLjqmlYrBBj0PuLmzkMVRHRJqOf691RYHWpw08rfS7ga+nFDfto3savz+lAUNo6ljGcCFPrgVL2Rq1CLQr3J5VfvfP5vNNha0QgK8cwu5s/IuxF6UAn6jXPBbXCOvaSV53dPKg6IY1Xh461Ua9g3BdcU0WhSUKgNtDOhFAW3hnegXwmhyYcTDza1ZD/Phzkaik0H5wZFUqxIXraiVbw2oTpIkx8aPUBdkpa67OFQlm+hnIrBG9QFM0Vf/BmT2z6ZGj+W4CwLYQytpnyTaHmB4sq+hbdY4v9ziy+1Y8khrVv/RsAqGBsJWHvlxkYMXtzmhYeKRY6hv'),('f446042f-7149-4757-a5d1-da66bf02fae3','375f68c1-aeaf-43a5-b181-c5a814d7410c','allowed-protocol-mapper-types','oidc-usermodel-property-mapper'),('f7eb6680-d77b-4c49-9229-dff663564523','b8bf10ce-a53b-4eb3-9d5f-ea9455551c29','user.model.attribute','createTimestamp'),('f9b5ae75-91f5-4dd3-9883-fb4ccca6ba39','28e62e97-a445-4efa-86d4-d46b17820bb0','priority','100'),('fd334699-8682-4bba-ba52-c9b8a4d26451','a76b14da-a3ec-4633-84f1-9f415a56120d','allowed-protocol-mapper-types','saml-user-attribute-mapper'),('fe2ce659-feb6-42bb-adc7-84006b99f297','8484c070-b463-4a3d-b77e-97ce40f5bb0a','uuidLDAPAttribute','cn'),('ff680332-454b-4fb2-8ba9-a5f17237caf0','0a1b32c6-c823-4e63-b414-6522f672ccc3','allow-default-scopes','true');
/*!40000 ALTER TABLE `COMPONENT_CONFIG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `COMPOSITE_ROLE`
--

DROP TABLE IF EXISTS `COMPOSITE_ROLE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `COMPOSITE_ROLE` (
  `COMPOSITE` varchar(36) NOT NULL,
  `CHILD_ROLE` varchar(36) NOT NULL,
  PRIMARY KEY (`COMPOSITE`,`CHILD_ROLE`),
  KEY `IDX_COMPOSITE` (`COMPOSITE`),
  KEY `IDX_COMPOSITE_CHILD` (`CHILD_ROLE`),
  CONSTRAINT `FK_A63WVEKFTU8JO1PNJ81E7MCE2` FOREIGN KEY (`COMPOSITE`) REFERENCES `KEYCLOAK_ROLE` (`ID`),
  CONSTRAINT `FK_GR7THLLB9LU8Q4VQA4524JJY8` FOREIGN KEY (`CHILD_ROLE`) REFERENCES `KEYCLOAK_ROLE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `COMPOSITE_ROLE`
--

LOCK TABLES `COMPOSITE_ROLE` WRITE;
/*!40000 ALTER TABLE `COMPOSITE_ROLE` DISABLE KEYS */;
INSERT INTO `COMPOSITE_ROLE` VALUES ('0ab906cb-cc4f-4af4-9dd4-e676e6f0f9f3','5e57a33b-de71-4df9-b941-05bd9d599478'),('2f42ba40-29d9-4010-927a-fb5b09d9aacd','73cc2ecd-a445-4e40-bbc2-e117dce12165'),('2f42ba40-29d9-4010-927a-fb5b09d9aacd','e5a2418f-2c2c-42fc-8aff-5f08d0509832'),('3d9c5e71-233a-4e29-9e60-f5d9129f9c57','c524cfb5-69f9-4423-8eab-e057118149ef'),('3d9c5e71-233a-4e29-9e60-f5d9129f9c57','df21d69e-011e-41c6-b8f4-983b4b7b1c01'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','020fb301-6df2-41e8-8f3d-372d5ee70642'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','14756887-8dc0-414b-9396-515ed0f1f083'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','19e2e60b-2aa1-4a5c-91dc-45c0e9424418'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','1e6947a7-c960-4b23-80c5-fd5ee8f9496f'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','2a68b9ec-af76-495d-86b6-90a498ef0a87'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','2f42ba40-29d9-4010-927a-fb5b09d9aacd'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','2f50935c-4449-4cf9-9e99-25df0a53a179'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','34644efb-56e5-4902-92df-29b540d3d474'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','3742ea4c-5aa9-4112-ad1c-4f3bdc870c93'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','3dd32d36-12ae-4b3f-88e7-77808d6ccce3'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','3f427fa4-f17f-452c-b8ad-018e2bc2a412'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','43f3a380-168b-49c9-a68f-0683e8c1bd35'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','54fb69c9-7442-485a-a65b-8e59a4899faa'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','5b4697e2-773c-4c06-a4b3-76dfef5433f5'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','65a1d897-fd63-41cd-9aa8-4142e08b6c7d'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','661a9e04-bccd-47da-97a8-0592e21f3e84'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','67372504-f0fc-4713-bd22-7f90a7f394c0'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','68fb355d-4023-4176-9227-f6f4dfe73b91'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','6a2b87d7-7299-4268-a39a-9c4bef2087b8'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','6bce23fb-3261-45ec-be62-0eebd45e6df9'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','73cc2ecd-a445-4e40-bbc2-e117dce12165'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','86f1eac0-b367-44c1-92e8-3db92d638e77'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','8720933a-030e-436e-90b3-e44b6f678051'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','8925dd2c-2339-4e90-a56b-f99d31c66209'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','936fdecc-b13d-47dc-85af-91b8512aca71'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','95925b41-f82f-4530-b8b8-14084b85ebc4'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','bd640cda-cc3d-4c22-8a33-d41de2c73c64'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','c219942a-04da-492e-929a-82bf4c3954df'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','c9052b8c-51dc-456d-9fe5-05a0eeafe756'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','dd93723b-74a7-419e-9588-9c115cf6f833'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','e2846703-970e-497d-8d13-d36de5ee7d9d'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','e2a5c5db-7689-4aff-beb1-1b9b9252a080'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','e5a2418f-2c2c-42fc-8aff-5f08d0509832'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','e708f5db-ed07-42aa-911f-fd5d0d955754'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','e9f8d2a1-f49f-40c2-bd7b-d12efe053aa6'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','f234d8d8-c739-481b-a6ef-cd3c2963449b'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','f9f0481f-3bdd-4cfd-923d-917b1b7e40ef'),('67372504-f0fc-4713-bd22-7f90a7f394c0','020fb301-6df2-41e8-8f3d-372d5ee70642'),('68fb355d-4023-4176-9227-f6f4dfe73b91','3dd32d36-12ae-4b3f-88e7-77808d6ccce3'),('68fb355d-4023-4176-9227-f6f4dfe73b91','8720933a-030e-436e-90b3-e44b6f678051'),('84bfe1ab-3fa2-493f-a52a-20cd26d5d6ad','06f1980a-cdbc-4fe3-a2aa-01ea57eae2ce'),('84bfe1ab-3fa2-493f-a52a-20cd26d5d6ad','174374c7-a893-4867-98b4-22b624d927ee'),('84bfe1ab-3fa2-493f-a52a-20cd26d5d6ad','2ff71c9b-391d-4e49-8871-c0429f5d3ada'),('84bfe1ab-3fa2-493f-a52a-20cd26d5d6ad','375717d0-db43-469d-a2bc-2b514bf8251c'),('84bfe1ab-3fa2-493f-a52a-20cd26d5d6ad','3d9c5e71-233a-4e29-9e60-f5d9129f9c57'),('84bfe1ab-3fa2-493f-a52a-20cd26d5d6ad','4ae9f0e8-1f16-44ef-90c9-558002aee88b'),('84bfe1ab-3fa2-493f-a52a-20cd26d5d6ad','5f6c2d9f-744b-49f1-ad3a-03c1abbbe1f5'),('84bfe1ab-3fa2-493f-a52a-20cd26d5d6ad','789a51ae-8ae9-406b-a9bf-02fa6df70bac'),('84bfe1ab-3fa2-493f-a52a-20cd26d5d6ad','7c636332-3fd3-4f72-bc53-551b3b91d5bf'),('84bfe1ab-3fa2-493f-a52a-20cd26d5d6ad','907300b5-9e9a-423d-96fe-1c5604d00dc9'),('84bfe1ab-3fa2-493f-a52a-20cd26d5d6ad','996812e8-82a2-4b44-9b38-0cad02f391ec'),('84bfe1ab-3fa2-493f-a52a-20cd26d5d6ad','b6d58577-5e67-4e02-a166-80da656d8c57'),('84bfe1ab-3fa2-493f-a52a-20cd26d5d6ad','bf76efcd-e2c7-4ba3-9f8a-507e787c26b2'),('84bfe1ab-3fa2-493f-a52a-20cd26d5d6ad','c524cfb5-69f9-4423-8eab-e057118149ef'),('84bfe1ab-3fa2-493f-a52a-20cd26d5d6ad','d76e15a1-3afe-435d-8720-69c533fd4b18'),('84bfe1ab-3fa2-493f-a52a-20cd26d5d6ad','df21d69e-011e-41c6-b8f4-983b4b7b1c01'),('84bfe1ab-3fa2-493f-a52a-20cd26d5d6ad','eb738200-9d7e-4235-86b8-f5351679f7df'),('84bfe1ab-3fa2-493f-a52a-20cd26d5d6ad','fcc87b08-b814-48c0-baee-97f9b81de4e4'),('b930fbec-a391-40dc-887c-a36fe46999a7','d725efa3-0a0e-4aa9-879d-7f099957ea15'),('c219942a-04da-492e-929a-82bf4c3954df','2f50935c-4449-4cf9-9e99-25df0a53a179'),('fcc87b08-b814-48c0-baee-97f9b81de4e4','d76e15a1-3afe-435d-8720-69c533fd4b18');
/*!40000 ALTER TABLE `COMPOSITE_ROLE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CREDENTIAL`
--

DROP TABLE IF EXISTS `CREDENTIAL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CREDENTIAL` (
  `ID` varchar(36) NOT NULL,
  `DEVICE` varchar(255) DEFAULT NULL,
  `HASH_ITERATIONS` int(11) DEFAULT NULL,
  `SALT` tinyblob,
  `TYPE` varchar(255) DEFAULT NULL,
  `VALUE` varchar(4000) DEFAULT NULL,
  `USER_ID` varchar(36) DEFAULT NULL,
  `CREATED_DATE` bigint(20) DEFAULT NULL,
  `COUNTER` int(11) DEFAULT '0',
  `DIGITS` int(11) DEFAULT '6',
  `PERIOD` int(11) DEFAULT '30',
  `ALGORITHM` varchar(36),
  PRIMARY KEY (`ID`),
  KEY `IDX_USER_CREDENTIAL` (`USER_ID`),
  CONSTRAINT `FK_PFYR0GLASQYL0DEI3KL69R6V0` FOREIGN KEY (`USER_ID`) REFERENCES `USER_ENTITY` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CREDENTIAL`
--

LOCK TABLES `CREDENTIAL` WRITE;
/*!40000 ALTER TABLE `CREDENTIAL` DISABLE KEYS */;
INSERT INTO `CREDENTIAL` VALUES ('1c847046-9360-498c-93e6-afb32c2021d4',NULL,27500,'��	�M��9PE�s�','password','OyBlsQjSNywhsCPuIjAtZYb5hCmokeL4G3km5l5EbntfW4E1G7Fw2XZgd1zAhjbUvtzPcwGWWCQj5RNTpaVhQQ==','5544a352-9524-44ad-8617-6ed1e77dcb8e',NULL,0,0,0,'pbkdf2-sha256');
/*!40000 ALTER TABLE `CREDENTIAL` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CREDENTIAL_ATTRIBUTE`
--

DROP TABLE IF EXISTS `CREDENTIAL_ATTRIBUTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `CREDENTIAL_ATTRIBUTE` (
  `ID` varchar(36) NOT NULL,
  `CREDENTIAL_ID` varchar(36) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `VALUE` varchar(4000) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_CREDENTIAL_ATTR_CRED` (`CREDENTIAL_ID`),
  CONSTRAINT `FK_CRED_ATTR` FOREIGN KEY (`CREDENTIAL_ID`) REFERENCES `CREDENTIAL` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CREDENTIAL_ATTRIBUTE`
--

LOCK TABLES `CREDENTIAL_ATTRIBUTE` WRITE;
/*!40000 ALTER TABLE `CREDENTIAL_ATTRIBUTE` DISABLE KEYS */;
/*!40000 ALTER TABLE `CREDENTIAL_ATTRIBUTE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DATABASECHANGELOG`
--

DROP TABLE IF EXISTS `DATABASECHANGELOG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DATABASECHANGELOG` (
  `ID` varchar(255) NOT NULL,
  `AUTHOR` varchar(255) NOT NULL,
  `FILENAME` varchar(255) NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int(11) NOT NULL,
  `EXECTYPE` varchar(10) NOT NULL,
  `MD5SUM` varchar(35) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `COMMENTS` varchar(255) DEFAULT NULL,
  `TAG` varchar(255) DEFAULT NULL,
  `LIQUIBASE` varchar(20) DEFAULT NULL,
  `CONTEXTS` varchar(255) DEFAULT NULL,
  `LABELS` varchar(255) DEFAULT NULL,
  `DEPLOYMENT_ID` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DATABASECHANGELOG`
--

LOCK TABLES `DATABASECHANGELOG` WRITE;
/*!40000 ALTER TABLE `DATABASECHANGELOG` DISABLE KEYS */;
INSERT INTO `DATABASECHANGELOG` VALUES ('1.0.0.Final-KEYCLOAK-5461','sthorger@redhat.com','META-INF/jpa-changelog-1.0.0.Final.xml','2021-04-22 10:44:48',1,'EXECUTED','7:4e70412f24a3f382c82183742ec79317','createTable tableName=APPLICATION_DEFAULT_ROLES; createTable tableName=CLIENT; createTable tableName=CLIENT_SESSION; createTable tableName=CLIENT_SESSION_ROLE; createTable tableName=COMPOSITE_ROLE; createTable tableName=CREDENTIAL; createTable tab...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('1.0.0.Final-KEYCLOAK-5461','sthorger@redhat.com','META-INF/db2-jpa-changelog-1.0.0.Final.xml','2021-04-22 10:44:48',2,'MARK_RAN','7:cb16724583e9675711801c6875114f28','createTable tableName=APPLICATION_DEFAULT_ROLES; createTable tableName=CLIENT; createTable tableName=CLIENT_SESSION; createTable tableName=CLIENT_SESSION_ROLE; createTable tableName=COMPOSITE_ROLE; createTable tableName=CREDENTIAL; createTable tab...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('1.1.0.Beta1','sthorger@redhat.com','META-INF/jpa-changelog-1.1.0.Beta1.xml','2021-04-22 10:44:48',3,'EXECUTED','7:0310eb8ba07cec616460794d42ade0fa','delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION; createTable tableName=CLIENT_ATTRIBUTES; createTable tableName=CLIENT_SESSION_NOTE; createTable tableName=APP_NODE_REGISTRATIONS; addColumn table...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('1.1.0.Final','sthorger@redhat.com','META-INF/jpa-changelog-1.1.0.Final.xml','2021-04-22 10:44:48',4,'EXECUTED','7:5d25857e708c3233ef4439df1f93f012','renameColumn newColumnName=EVENT_TIME, oldColumnName=TIME, tableName=EVENT_ENTITY','',NULL,'3.5.4',NULL,NULL,'9088286888'),('1.2.0.Beta1','psilva@redhat.com','META-INF/jpa-changelog-1.2.0.Beta1.xml','2021-04-22 10:44:49',5,'EXECUTED','7:c7a54a1041d58eb3817a4a883b4d4e84','delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION; createTable tableName=PROTOCOL_MAPPER; createTable tableName=PROTOCOL_MAPPER_CONFIG; createTable tableName=...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('1.2.0.Beta1','psilva@redhat.com','META-INF/db2-jpa-changelog-1.2.0.Beta1.xml','2021-04-22 10:44:49',6,'MARK_RAN','7:2e01012df20974c1c2a605ef8afe25b7','delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION; createTable tableName=PROTOCOL_MAPPER; createTable tableName=PROTOCOL_MAPPER_CONFIG; createTable tableName=...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('1.2.0.RC1','bburke@redhat.com','META-INF/jpa-changelog-1.2.0.CR1.xml','2021-04-22 10:44:50',7,'EXECUTED','7:0f08df48468428e0f30ee59a8ec01a41','delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION_NOTE; delete tableName=USER_SESSION; createTable tableName=MIGRATION_MODEL; createTable tableName=IDENTITY_P...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('1.2.0.RC1','bburke@redhat.com','META-INF/db2-jpa-changelog-1.2.0.CR1.xml','2021-04-22 10:44:50',8,'MARK_RAN','7:a77ea2ad226b345e7d689d366f185c8c','delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION_NOTE; delete tableName=USER_SESSION; createTable tableName=MIGRATION_MODEL; createTable tableName=IDENTITY_P...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('1.2.0.Final','keycloak','META-INF/jpa-changelog-1.2.0.Final.xml','2021-04-22 10:44:50',9,'EXECUTED','7:a3377a2059aefbf3b90ebb4c4cc8e2ab','update tableName=CLIENT; update tableName=CLIENT; update tableName=CLIENT','',NULL,'3.5.4',NULL,NULL,'9088286888'),('1.3.0','bburke@redhat.com','META-INF/jpa-changelog-1.3.0.xml','2021-04-22 10:44:50',10,'EXECUTED','7:04c1dbedc2aa3e9756d1a1668e003451','delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_PROT_MAPPER; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION_NOTE; delete tableName=USER_SESSION; createTable tableName=ADMI...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('1.4.0','bburke@redhat.com','META-INF/jpa-changelog-1.4.0.xml','2021-04-22 10:44:51',11,'EXECUTED','7:36ef39ed560ad07062d956db861042ba','delete tableName=CLIENT_SESSION_AUTH_STATUS; delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_PROT_MAPPER; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION_NOTE; delete table...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('1.4.0','bburke@redhat.com','META-INF/db2-jpa-changelog-1.4.0.xml','2021-04-22 10:44:51',12,'MARK_RAN','7:d909180b2530479a716d3f9c9eaea3d7','delete tableName=CLIENT_SESSION_AUTH_STATUS; delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_PROT_MAPPER; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION_NOTE; delete table...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('1.5.0','bburke@redhat.com','META-INF/jpa-changelog-1.5.0.xml','2021-04-22 10:44:51',13,'EXECUTED','7:cf12b04b79bea5152f165eb41f3955f6','delete tableName=CLIENT_SESSION_AUTH_STATUS; delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_PROT_MAPPER; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION_NOTE; delete table...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('1.6.1_from15','mposolda@redhat.com','META-INF/jpa-changelog-1.6.1.xml','2021-04-22 10:44:51',14,'EXECUTED','7:7e32c8f05c755e8675764e7d5f514509','addColumn tableName=REALM; addColumn tableName=KEYCLOAK_ROLE; addColumn tableName=CLIENT; createTable tableName=OFFLINE_USER_SESSION; createTable tableName=OFFLINE_CLIENT_SESSION; addPrimaryKey constraintName=CONSTRAINT_OFFL_US_SES_PK2, tableName=...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('1.6.1_from16-pre','mposolda@redhat.com','META-INF/jpa-changelog-1.6.1.xml','2021-04-22 10:44:51',15,'MARK_RAN','7:980ba23cc0ec39cab731ce903dd01291','delete tableName=OFFLINE_CLIENT_SESSION; delete tableName=OFFLINE_USER_SESSION','',NULL,'3.5.4',NULL,NULL,'9088286888'),('1.6.1_from16','mposolda@redhat.com','META-INF/jpa-changelog-1.6.1.xml','2021-04-22 10:44:51',16,'MARK_RAN','7:2fa220758991285312eb84f3b4ff5336','dropPrimaryKey constraintName=CONSTRAINT_OFFLINE_US_SES_PK, tableName=OFFLINE_USER_SESSION; dropPrimaryKey constraintName=CONSTRAINT_OFFLINE_CL_SES_PK, tableName=OFFLINE_CLIENT_SESSION; addColumn tableName=OFFLINE_USER_SESSION; update tableName=OF...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('1.6.1','mposolda@redhat.com','META-INF/jpa-changelog-1.6.1.xml','2021-04-22 10:44:51',17,'EXECUTED','7:d41d8cd98f00b204e9800998ecf8427e','empty','',NULL,'3.5.4',NULL,NULL,'9088286888'),('1.7.0','bburke@redhat.com','META-INF/jpa-changelog-1.7.0.xml','2021-04-22 10:44:51',18,'EXECUTED','7:91ace540896df890cc00a0490ee52bbc','createTable tableName=KEYCLOAK_GROUP; createTable tableName=GROUP_ROLE_MAPPING; createTable tableName=GROUP_ATTRIBUTE; createTable tableName=USER_GROUP_MEMBERSHIP; createTable tableName=REALM_DEFAULT_GROUPS; addColumn tableName=IDENTITY_PROVIDER; ...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('1.8.0','mposolda@redhat.com','META-INF/jpa-changelog-1.8.0.xml','2021-04-22 10:44:52',19,'EXECUTED','7:c31d1646dfa2618a9335c00e07f89f24','addColumn tableName=IDENTITY_PROVIDER; createTable tableName=CLIENT_TEMPLATE; createTable tableName=CLIENT_TEMPLATE_ATTRIBUTES; createTable tableName=TEMPLATE_SCOPE_MAPPING; dropNotNullConstraint columnName=CLIENT_ID, tableName=PROTOCOL_MAPPER; ad...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('1.8.0-2','keycloak','META-INF/jpa-changelog-1.8.0.xml','2021-04-22 10:44:52',20,'EXECUTED','7:df8bc21027a4f7cbbb01f6344e89ce07','dropDefaultValue columnName=ALGORITHM, tableName=CREDENTIAL; update tableName=CREDENTIAL','',NULL,'3.5.4',NULL,NULL,'9088286888'),('1.8.0','mposolda@redhat.com','META-INF/db2-jpa-changelog-1.8.0.xml','2021-04-22 10:44:52',21,'MARK_RAN','7:f987971fe6b37d963bc95fee2b27f8df','addColumn tableName=IDENTITY_PROVIDER; createTable tableName=CLIENT_TEMPLATE; createTable tableName=CLIENT_TEMPLATE_ATTRIBUTES; createTable tableName=TEMPLATE_SCOPE_MAPPING; dropNotNullConstraint columnName=CLIENT_ID, tableName=PROTOCOL_MAPPER; ad...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('1.8.0-2','keycloak','META-INF/db2-jpa-changelog-1.8.0.xml','2021-04-22 10:44:52',22,'MARK_RAN','7:df8bc21027a4f7cbbb01f6344e89ce07','dropDefaultValue columnName=ALGORITHM, tableName=CREDENTIAL; update tableName=CREDENTIAL','',NULL,'3.5.4',NULL,NULL,'9088286888'),('1.9.0','mposolda@redhat.com','META-INF/jpa-changelog-1.9.0.xml','2021-04-22 10:44:52',23,'EXECUTED','7:ed2dc7f799d19ac452cbcda56c929e47','update tableName=REALM; update tableName=REALM; update tableName=REALM; update tableName=REALM; update tableName=CREDENTIAL; update tableName=CREDENTIAL; update tableName=CREDENTIAL; update tableName=REALM; update tableName=REALM; customChange; dr...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('1.9.1','keycloak','META-INF/jpa-changelog-1.9.1.xml','2021-04-22 10:44:52',24,'EXECUTED','7:80b5db88a5dda36ece5f235be8757615','modifyDataType columnName=PRIVATE_KEY, tableName=REALM; modifyDataType columnName=PUBLIC_KEY, tableName=REALM; modifyDataType columnName=CERTIFICATE, tableName=REALM','',NULL,'3.5.4',NULL,NULL,'9088286888'),('1.9.1','keycloak','META-INF/db2-jpa-changelog-1.9.1.xml','2021-04-22 10:44:52',25,'MARK_RAN','7:1437310ed1305a9b93f8848f301726ce','modifyDataType columnName=PRIVATE_KEY, tableName=REALM; modifyDataType columnName=CERTIFICATE, tableName=REALM','',NULL,'3.5.4',NULL,NULL,'9088286888'),('1.9.2','keycloak','META-INF/jpa-changelog-1.9.2.xml','2021-04-22 10:44:52',26,'EXECUTED','7:b82ffb34850fa0836be16deefc6a87c4','createIndex indexName=IDX_USER_EMAIL, tableName=USER_ENTITY; createIndex indexName=IDX_USER_ROLE_MAPPING, tableName=USER_ROLE_MAPPING; createIndex indexName=IDX_USER_GROUP_MAPPING, tableName=USER_GROUP_MEMBERSHIP; createIndex indexName=IDX_USER_CO...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('authz-2.0.0','psilva@redhat.com','META-INF/jpa-changelog-authz-2.0.0.xml','2021-04-22 10:44:53',27,'EXECUTED','7:9cc98082921330d8d9266decdd4bd658','createTable tableName=RESOURCE_SERVER; addPrimaryKey constraintName=CONSTRAINT_FARS, tableName=RESOURCE_SERVER; addUniqueConstraint constraintName=UK_AU8TT6T700S9V50BU18WS5HA6, tableName=RESOURCE_SERVER; createTable tableName=RESOURCE_SERVER_RESOU...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('authz-2.5.1','psilva@redhat.com','META-INF/jpa-changelog-authz-2.5.1.xml','2021-04-22 10:44:53',28,'EXECUTED','7:03d64aeed9cb52b969bd30a7ac0db57e','update tableName=RESOURCE_SERVER_POLICY','',NULL,'3.5.4',NULL,NULL,'9088286888'),('2.1.0-KEYCLOAK-5461','bburke@redhat.com','META-INF/jpa-changelog-2.1.0.xml','2021-04-22 10:44:53',29,'EXECUTED','7:f1f9fd8710399d725b780f463c6b21cd','createTable tableName=BROKER_LINK; createTable tableName=FED_USER_ATTRIBUTE; createTable tableName=FED_USER_CONSENT; createTable tableName=FED_USER_CONSENT_ROLE; createTable tableName=FED_USER_CONSENT_PROT_MAPPER; createTable tableName=FED_USER_CR...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('2.2.0','bburke@redhat.com','META-INF/jpa-changelog-2.2.0.xml','2021-04-22 10:44:53',30,'EXECUTED','7:53188c3eb1107546e6f765835705b6c1','addColumn tableName=ADMIN_EVENT_ENTITY; createTable tableName=CREDENTIAL_ATTRIBUTE; createTable tableName=FED_CREDENTIAL_ATTRIBUTE; modifyDataType columnName=VALUE, tableName=CREDENTIAL; addForeignKeyConstraint baseTableName=FED_CREDENTIAL_ATTRIBU...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('2.3.0','bburke@redhat.com','META-INF/jpa-changelog-2.3.0.xml','2021-04-22 10:44:53',31,'EXECUTED','7:d6e6f3bc57a0c5586737d1351725d4d4','createTable tableName=FEDERATED_USER; addPrimaryKey constraintName=CONSTR_FEDERATED_USER, tableName=FEDERATED_USER; dropDefaultValue columnName=TOTP, tableName=USER_ENTITY; dropColumn columnName=TOTP, tableName=USER_ENTITY; addColumn tableName=IDE...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('2.4.0','bburke@redhat.com','META-INF/jpa-changelog-2.4.0.xml','2021-04-22 10:44:53',32,'EXECUTED','7:454d604fbd755d9df3fd9c6329043aa5','customChange','',NULL,'3.5.4',NULL,NULL,'9088286888'),('2.5.0','bburke@redhat.com','META-INF/jpa-changelog-2.5.0.xml','2021-04-22 10:44:53',33,'EXECUTED','7:57e98a3077e29caf562f7dbf80c72600','customChange; modifyDataType columnName=USER_ID, tableName=OFFLINE_USER_SESSION','',NULL,'3.5.4',NULL,NULL,'9088286888'),('2.5.0-unicode-oracle','hmlnarik@redhat.com','META-INF/jpa-changelog-2.5.0.xml','2021-04-22 10:44:53',34,'MARK_RAN','7:e4c7e8f2256210aee71ddc42f538b57a','modifyDataType columnName=DESCRIPTION, tableName=AUTHENTICATION_FLOW; modifyDataType columnName=DESCRIPTION, tableName=CLIENT_TEMPLATE; modifyDataType columnName=DESCRIPTION, tableName=RESOURCE_SERVER_POLICY; modifyDataType columnName=DESCRIPTION,...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('2.5.0-unicode-other-dbs','hmlnarik@redhat.com','META-INF/jpa-changelog-2.5.0.xml','2021-04-22 10:44:53',35,'EXECUTED','7:09a43c97e49bc626460480aa1379b522','modifyDataType columnName=DESCRIPTION, tableName=AUTHENTICATION_FLOW; modifyDataType columnName=DESCRIPTION, tableName=CLIENT_TEMPLATE; modifyDataType columnName=DESCRIPTION, tableName=RESOURCE_SERVER_POLICY; modifyDataType columnName=DESCRIPTION,...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('2.5.0-duplicate-email-support','slawomir@dabek.name','META-INF/jpa-changelog-2.5.0.xml','2021-04-22 10:44:53',36,'EXECUTED','7:26bfc7c74fefa9126f2ce702fb775553','addColumn tableName=REALM','',NULL,'3.5.4',NULL,NULL,'9088286888'),('2.5.0-unique-group-names','hmlnarik@redhat.com','META-INF/jpa-changelog-2.5.0.xml','2021-04-22 10:44:53',37,'EXECUTED','7:a161e2ae671a9020fff61e996a207377','addUniqueConstraint constraintName=SIBLING_NAMES, tableName=KEYCLOAK_GROUP','',NULL,'3.5.4',NULL,NULL,'9088286888'),('2.5.1','bburke@redhat.com','META-INF/jpa-changelog-2.5.1.xml','2021-04-22 10:44:53',38,'EXECUTED','7:37fc1781855ac5388c494f1442b3f717','addColumn tableName=FED_USER_CONSENT','',NULL,'3.5.4',NULL,NULL,'9088286888'),('3.0.0','bburke@redhat.com','META-INF/jpa-changelog-3.0.0.xml','2021-04-22 10:44:54',39,'EXECUTED','7:13a27db0dae6049541136adad7261d27','addColumn tableName=IDENTITY_PROVIDER','',NULL,'3.5.4',NULL,NULL,'9088286888'),('3.2.0-fix','keycloak','META-INF/jpa-changelog-3.2.0.xml','2021-04-22 10:44:54',40,'MARK_RAN','7:550300617e3b59e8af3a6294df8248a3','addNotNullConstraint columnName=REALM_ID, tableName=CLIENT_INITIAL_ACCESS','',NULL,'3.5.4',NULL,NULL,'9088286888'),('3.2.0-fix-with-keycloak-5416','keycloak','META-INF/jpa-changelog-3.2.0.xml','2021-04-22 10:44:54',41,'MARK_RAN','7:e3a9482b8931481dc2772a5c07c44f17','dropIndex indexName=IDX_CLIENT_INIT_ACC_REALM, tableName=CLIENT_INITIAL_ACCESS; addNotNullConstraint columnName=REALM_ID, tableName=CLIENT_INITIAL_ACCESS; createIndex indexName=IDX_CLIENT_INIT_ACC_REALM, tableName=CLIENT_INITIAL_ACCESS','',NULL,'3.5.4',NULL,NULL,'9088286888'),('3.2.0-fix-offline-sessions','hmlnarik','META-INF/jpa-changelog-3.2.0.xml','2021-04-22 10:44:54',42,'EXECUTED','7:72b07d85a2677cb257edb02b408f332d','customChange','',NULL,'3.5.4',NULL,NULL,'9088286888'),('3.2.0-fixed','keycloak','META-INF/jpa-changelog-3.2.0.xml','2021-04-22 10:44:54',43,'EXECUTED','7:a72a7858967bd414835d19e04d880312','addColumn tableName=REALM; dropPrimaryKey constraintName=CONSTRAINT_OFFL_CL_SES_PK2, tableName=OFFLINE_CLIENT_SESSION; dropColumn columnName=CLIENT_SESSION_ID, tableName=OFFLINE_CLIENT_SESSION; addPrimaryKey constraintName=CONSTRAINT_OFFL_CL_SES_P...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('3.3.0','keycloak','META-INF/jpa-changelog-3.3.0.xml','2021-04-22 10:44:54',44,'EXECUTED','7:94edff7cf9ce179e7e85f0cd78a3cf2c','addColumn tableName=USER_ENTITY','',NULL,'3.5.4',NULL,NULL,'9088286888'),('authz-3.4.0.CR1-resource-server-pk-change-part1','glavoie@gmail.com','META-INF/jpa-changelog-authz-3.4.0.CR1.xml','2021-04-22 10:44:54',45,'EXECUTED','7:6a48ce645a3525488a90fbf76adf3bb3','addColumn tableName=RESOURCE_SERVER_POLICY; addColumn tableName=RESOURCE_SERVER_RESOURCE; addColumn tableName=RESOURCE_SERVER_SCOPE','',NULL,'3.5.4',NULL,NULL,'9088286888'),('authz-3.4.0.CR1-resource-server-pk-change-part2-KEYCLOAK-6095','hmlnarik@redhat.com','META-INF/jpa-changelog-authz-3.4.0.CR1.xml','2021-04-22 10:44:54',46,'EXECUTED','7:e64b5dcea7db06077c6e57d3b9e5ca14','customChange','',NULL,'3.5.4',NULL,NULL,'9088286888'),('authz-3.4.0.CR1-resource-server-pk-change-part3-fixed','glavoie@gmail.com','META-INF/jpa-changelog-authz-3.4.0.CR1.xml','2021-04-22 10:44:54',47,'MARK_RAN','7:fd8cf02498f8b1e72496a20afc75178c','dropIndex indexName=IDX_RES_SERV_POL_RES_SERV, tableName=RESOURCE_SERVER_POLICY; dropIndex indexName=IDX_RES_SRV_RES_RES_SRV, tableName=RESOURCE_SERVER_RESOURCE; dropIndex indexName=IDX_RES_SRV_SCOPE_RES_SRV, tableName=RESOURCE_SERVER_SCOPE','',NULL,'3.5.4',NULL,NULL,'9088286888'),('authz-3.4.0.CR1-resource-server-pk-change-part3-fixed-nodropindex','glavoie@gmail.com','META-INF/jpa-changelog-authz-3.4.0.CR1.xml','2021-04-22 10:44:55',48,'EXECUTED','7:542794f25aa2b1fbabb7e577d6646319','addNotNullConstraint columnName=RESOURCE_SERVER_CLIENT_ID, tableName=RESOURCE_SERVER_POLICY; addNotNullConstraint columnName=RESOURCE_SERVER_CLIENT_ID, tableName=RESOURCE_SERVER_RESOURCE; addNotNullConstraint columnName=RESOURCE_SERVER_CLIENT_ID, ...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('authn-3.4.0.CR1-refresh-token-max-reuse','glavoie@gmail.com','META-INF/jpa-changelog-authz-3.4.0.CR1.xml','2021-04-22 10:44:55',49,'EXECUTED','7:edad604c882df12f74941dac3cc6d650','addColumn tableName=REALM','',NULL,'3.5.4',NULL,NULL,'9088286888'),('3.4.0','keycloak','META-INF/jpa-changelog-3.4.0.xml','2021-04-22 10:44:55',50,'EXECUTED','7:0f88b78b7b46480eb92690cbf5e44900','addPrimaryKey constraintName=CONSTRAINT_REALM_DEFAULT_ROLES, tableName=REALM_DEFAULT_ROLES; addPrimaryKey constraintName=CONSTRAINT_COMPOSITE_ROLE, tableName=COMPOSITE_ROLE; addPrimaryKey constraintName=CONSTR_REALM_DEFAULT_GROUPS, tableName=REALM...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('3.4.0-KEYCLOAK-5230','hmlnarik@redhat.com','META-INF/jpa-changelog-3.4.0.xml','2021-04-22 10:44:55',51,'EXECUTED','7:d560e43982611d936457c327f872dd59','createIndex indexName=IDX_FU_ATTRIBUTE, tableName=FED_USER_ATTRIBUTE; createIndex indexName=IDX_FU_CONSENT, tableName=FED_USER_CONSENT; createIndex indexName=IDX_FU_CONSENT_RU, tableName=FED_USER_CONSENT; createIndex indexName=IDX_FU_CREDENTIAL, t...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('3.4.1','psilva@redhat.com','META-INF/jpa-changelog-3.4.1.xml','2021-04-22 10:44:55',52,'EXECUTED','7:c155566c42b4d14ef07059ec3b3bbd8e','modifyDataType columnName=VALUE, tableName=CLIENT_ATTRIBUTES','',NULL,'3.5.4',NULL,NULL,'9088286888'),('3.4.2','keycloak','META-INF/jpa-changelog-3.4.2.xml','2021-04-22 10:44:55',53,'EXECUTED','7:b40376581f12d70f3c89ba8ddf5b7dea','update tableName=REALM','',NULL,'3.5.4',NULL,NULL,'9088286888'),('3.4.2-KEYCLOAK-5172','mkanis@redhat.com','META-INF/jpa-changelog-3.4.2.xml','2021-04-22 10:44:55',54,'EXECUTED','7:a1132cc395f7b95b3646146c2e38f168','update tableName=CLIENT','',NULL,'3.5.4',NULL,NULL,'9088286888'),('4.0.0-KEYCLOAK-6335','bburke@redhat.com','META-INF/jpa-changelog-4.0.0.xml','2021-04-22 10:44:55',55,'EXECUTED','7:d8dc5d89c789105cfa7ca0e82cba60af','createTable tableName=CLIENT_AUTH_FLOW_BINDINGS; addPrimaryKey constraintName=C_CLI_FLOW_BIND, tableName=CLIENT_AUTH_FLOW_BINDINGS','',NULL,'3.5.4',NULL,NULL,'9088286888'),('4.0.0-CLEANUP-UNUSED-TABLE','bburke@redhat.com','META-INF/jpa-changelog-4.0.0.xml','2021-04-22 10:44:55',56,'EXECUTED','7:7822e0165097182e8f653c35517656a3','dropTable tableName=CLIENT_IDENTITY_PROV_MAPPING','',NULL,'3.5.4',NULL,NULL,'9088286888'),('4.0.0-KEYCLOAK-6228','bburke@redhat.com','META-INF/jpa-changelog-4.0.0.xml','2021-04-22 10:44:55',57,'EXECUTED','7:c6538c29b9c9a08f9e9ea2de5c2b6375','dropUniqueConstraint constraintName=UK_JKUWUVD56ONTGSUHOGM8UEWRT, tableName=USER_CONSENT; dropNotNullConstraint columnName=CLIENT_ID, tableName=USER_CONSENT; addColumn tableName=USER_CONSENT; addUniqueConstraint constraintName=UK_JKUWUVD56ONTGSUHO...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('4.0.0-KEYCLOAK-5579-fixed','mposolda@redhat.com','META-INF/jpa-changelog-4.0.0.xml','2021-04-22 10:44:56',58,'EXECUTED','7:6d4893e36de22369cf73bcb051ded875','dropForeignKeyConstraint baseTableName=CLIENT_TEMPLATE_ATTRIBUTES, constraintName=FK_CL_TEMPL_ATTR_TEMPL; renameTable newTableName=CLIENT_SCOPE_ATTRIBUTES, oldTableName=CLIENT_TEMPLATE_ATTRIBUTES; renameColumn newColumnName=SCOPE_ID, oldColumnName...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('authz-4.0.0.CR1','psilva@redhat.com','META-INF/jpa-changelog-authz-4.0.0.CR1.xml','2021-04-22 10:44:56',59,'EXECUTED','7:57960fc0b0f0dd0563ea6f8b2e4a1707','createTable tableName=RESOURCE_SERVER_PERM_TICKET; addPrimaryKey constraintName=CONSTRAINT_FAPMT, tableName=RESOURCE_SERVER_PERM_TICKET; addForeignKeyConstraint baseTableName=RESOURCE_SERVER_PERM_TICKET, constraintName=FK_FRSRHO213XCX4WNKOG82SSPMT...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('authz-4.0.0.Beta3','psilva@redhat.com','META-INF/jpa-changelog-authz-4.0.0.Beta3.xml','2021-04-22 10:44:57',60,'EXECUTED','7:2b4b8bff39944c7097977cc18dbceb3b','addColumn tableName=RESOURCE_SERVER_POLICY; addColumn tableName=RESOURCE_SERVER_PERM_TICKET; addForeignKeyConstraint baseTableName=RESOURCE_SERVER_PERM_TICKET, constraintName=FK_FRSRPO2128CX4WNKOG82SSRFY, referencedTableName=RESOURCE_SERVER_POLICY','',NULL,'3.5.4',NULL,NULL,'9088286888'),('authz-4.2.0.Final','mhajas@redhat.com','META-INF/jpa-changelog-authz-4.2.0.Final.xml','2021-04-22 10:44:57',61,'EXECUTED','7:2aa42a964c59cd5b8ca9822340ba33a8','createTable tableName=RESOURCE_URIS; addForeignKeyConstraint baseTableName=RESOURCE_URIS, constraintName=FK_RESOURCE_SERVER_URIS, referencedTableName=RESOURCE_SERVER_RESOURCE; customChange; dropColumn columnName=URI, tableName=RESOURCE_SERVER_RESO...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('4.2.0-KEYCLOAK-6313','wadahiro@gmail.com','META-INF/jpa-changelog-4.2.0.xml','2021-04-22 10:44:57',62,'EXECUTED','7:14d407c35bc4fe1976867756bcea0c36','addColumn tableName=REQUIRED_ACTION_PROVIDER','',NULL,'3.5.4',NULL,NULL,'9088286888'),('4.3.0-KEYCLOAK-7984','wadahiro@gmail.com','META-INF/jpa-changelog-4.3.0.xml','2021-04-22 10:44:57',63,'EXECUTED','7:241a8030c748c8548e346adee548fa93','update tableName=REQUIRED_ACTION_PROVIDER','',NULL,'3.5.4',NULL,NULL,'9088286888'),('4.6.0-KEYCLOAK-7950','psilva@redhat.com','META-INF/jpa-changelog-4.6.0.xml','2021-04-22 10:44:57',64,'EXECUTED','7:7d3182f65a34fcc61e8d23def037dc3f','update tableName=RESOURCE_SERVER_RESOURCE','',NULL,'3.5.4',NULL,NULL,'9088286888'),('4.6.0-KEYCLOAK-8377','keycloak','META-INF/jpa-changelog-4.6.0.xml','2021-04-22 10:44:57',65,'EXECUTED','7:b30039e00a0b9715d430d1b0636728fa','createTable tableName=ROLE_ATTRIBUTE; addPrimaryKey constraintName=CONSTRAINT_ROLE_ATTRIBUTE_PK, tableName=ROLE_ATTRIBUTE; addForeignKeyConstraint baseTableName=ROLE_ATTRIBUTE, constraintName=FK_ROLE_ATTRIBUTE_ID, referencedTableName=KEYCLOAK_ROLE...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('4.6.0-KEYCLOAK-8555','gideonray@gmail.com','META-INF/jpa-changelog-4.6.0.xml','2021-04-22 10:44:57',66,'EXECUTED','7:3797315ca61d531780f8e6f82f258159','createIndex indexName=IDX_COMPONENT_PROVIDER_TYPE, tableName=COMPONENT','',NULL,'3.5.4',NULL,NULL,'9088286888'),('4.7.0-KEYCLOAK-1267','sguilhen@redhat.com','META-INF/jpa-changelog-4.7.0.xml','2021-04-22 10:44:57',67,'EXECUTED','7:c7aa4c8d9573500c2d347c1941ff0301','addColumn tableName=REALM','',NULL,'3.5.4',NULL,NULL,'9088286888'),('4.7.0-KEYCLOAK-7275','keycloak','META-INF/jpa-changelog-4.7.0.xml','2021-04-22 10:44:57',68,'EXECUTED','7:b207faee394fc074a442ecd42185a5dd','renameColumn newColumnName=CREATED_ON, oldColumnName=LAST_SESSION_REFRESH, tableName=OFFLINE_USER_SESSION; addNotNullConstraint columnName=CREATED_ON, tableName=OFFLINE_USER_SESSION; addColumn tableName=OFFLINE_USER_SESSION; customChange; createIn...','',NULL,'3.5.4',NULL,NULL,'9088286888'),('4.8.0-KEYCLOAK-8835','sguilhen@redhat.com','META-INF/jpa-changelog-4.8.0.xml','2021-04-22 10:44:57',69,'EXECUTED','7:ab9a9762faaba4ddfa35514b212c4922','addNotNullConstraint columnName=SSO_MAX_LIFESPAN_REMEMBER_ME, tableName=REALM; addNotNullConstraint columnName=SSO_IDLE_TIMEOUT_REMEMBER_ME, tableName=REALM','',NULL,'3.5.4',NULL,NULL,'9088286888');
/*!40000 ALTER TABLE `DATABASECHANGELOG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DATABASECHANGELOGLOCK`
--

DROP TABLE IF EXISTS `DATABASECHANGELOGLOCK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DATABASECHANGELOGLOCK` (
  `ID` int(11) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime DEFAULT NULL,
  `LOCKEDBY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DATABASECHANGELOGLOCK`
--

LOCK TABLES `DATABASECHANGELOGLOCK` WRITE;
/*!40000 ALTER TABLE `DATABASECHANGELOGLOCK` DISABLE KEYS */;
INSERT INTO `DATABASECHANGELOGLOCK` VALUES (1,'\0',NULL,NULL);
/*!40000 ALTER TABLE `DATABASECHANGELOGLOCK` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DATABASECHANGELOG_AVATAR_ENT`
--

DROP TABLE IF EXISTS `DATABASECHANGELOG_AVATAR_ENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DATABASECHANGELOG_AVATAR_ENT` (
  `ID` varchar(255) NOT NULL,
  `AUTHOR` varchar(255) NOT NULL,
  `FILENAME` varchar(255) NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int(11) NOT NULL,
  `EXECTYPE` varchar(10) NOT NULL,
  `MD5SUM` varchar(35) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `COMMENTS` varchar(255) DEFAULT NULL,
  `TAG` varchar(255) DEFAULT NULL,
  `LIQUIBASE` varchar(20) DEFAULT NULL,
  `CONTEXTS` varchar(255) DEFAULT NULL,
  `LABELS` varchar(255) DEFAULT NULL,
  `DEPLOYMENT_ID` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DATABASECHANGELOG_AVATAR_ENT`
--

LOCK TABLES `DATABASECHANGELOG_AVATAR_ENT` WRITE;
/*!40000 ALTER TABLE `DATABASECHANGELOG_AVATAR_ENT` DISABLE KEYS */;
INSERT INTO `DATABASECHANGELOG_AVATAR_ENT` VALUES ('1.0','erik.derooij@deltares.nl','META-INF/avatar-changelog.xml','2021-04-22 10:45:20',1,'EXECUTED','7:6ea25f29f5520133018408ac09a19ef7','createTable tableName=USER_AVATAR; addPrimaryKey constraintName=PK_USER_AVATAR, tableName=USER_AVATAR; addForeignKeyConstraint baseTableName=USER_AVATAR, constraintName=FK_USER_AVATAR_USER_ENTITY, referencedTableName=USER_ENTITY; addForeignKeyCons...','',NULL,'3.5.4',NULL,NULL,'9088320412'),('1.1','erik.derooij@deltares.nl','META-INF/avatar-changelog.xml','2021-04-22 10:45:20',2,'EXECUTED','7:285ce67a1144c7d749322d938b0c0fdd','addColumn tableName=USER_AVATAR','',NULL,'3.5.4',NULL,NULL,'9088320412');
/*!40000 ALTER TABLE `DATABASECHANGELOG_AVATAR_ENT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DATABASECHANGELOG_MAILING_EN`
--

DROP TABLE IF EXISTS `DATABASECHANGELOG_MAILING_EN`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DATABASECHANGELOG_MAILING_EN` (
  `ID` varchar(255) NOT NULL,
  `AUTHOR` varchar(255) NOT NULL,
  `FILENAME` varchar(255) NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int(11) NOT NULL,
  `EXECTYPE` varchar(10) NOT NULL,
  `MD5SUM` varchar(35) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `COMMENTS` varchar(255) DEFAULT NULL,
  `TAG` varchar(255) DEFAULT NULL,
  `LIQUIBASE` varchar(20) DEFAULT NULL,
  `CONTEXTS` varchar(255) DEFAULT NULL,
  `LABELS` varchar(255) DEFAULT NULL,
  `DEPLOYMENT_ID` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DATABASECHANGELOG_MAILING_EN`
--

LOCK TABLES `DATABASECHANGELOG_MAILING_EN` WRITE;
/*!40000 ALTER TABLE `DATABASECHANGELOG_MAILING_EN` DISABLE KEYS */;
INSERT INTO `DATABASECHANGELOG_MAILING_EN` VALUES ('1.0','erik.derooij@deltares.nl','META-INF/mailing-changelog.xml','2021-04-22 10:45:20',1,'EXECUTED','7:80c8e5f1cd0e1ee66b7dd9adb2418780','createTable tableName=MAILING_ENTITY; addPrimaryKey constraintName=PK_MAILING_ENTITY, tableName=MAILING_ENTITY; addUniqueConstraint constraintName=UK_MAILING_NAME, tableName=MAILING_ENTITY; addForeignKeyConstraint baseTableName=MAILING_ENTITY, con...','',NULL,'3.5.4',NULL,NULL,'9088320306'),('1.1','erik.derooij@deltares.nl','META-INF/mailing-changelog.xml','2021-04-22 10:45:20',2,'EXECUTED','7:0727d0d04f32655429acb1b971c02ba2','addDefaultValue columnName=FREQUENCY, tableName=MAILING_ENTITY','',NULL,'3.5.4',NULL,NULL,'9088320306');
/*!40000 ALTER TABLE `DATABASECHANGELOG_MAILING_EN` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DATABASECHANGELOG_USER_MAILI`
--

DROP TABLE IF EXISTS `DATABASECHANGELOG_USER_MAILI`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DATABASECHANGELOG_USER_MAILI` (
  `ID` varchar(255) NOT NULL,
  `AUTHOR` varchar(255) NOT NULL,
  `FILENAME` varchar(255) NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int(11) NOT NULL,
  `EXECTYPE` varchar(10) NOT NULL,
  `MD5SUM` varchar(35) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `COMMENTS` varchar(255) DEFAULT NULL,
  `TAG` varchar(255) DEFAULT NULL,
  `LIQUIBASE` varchar(20) DEFAULT NULL,
  `CONTEXTS` varchar(255) DEFAULT NULL,
  `LABELS` varchar(255) DEFAULT NULL,
  `DEPLOYMENT_ID` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DATABASECHANGELOG_USER_MAILI`
--

LOCK TABLES `DATABASECHANGELOG_USER_MAILI` WRITE;
/*!40000 ALTER TABLE `DATABASECHANGELOG_USER_MAILI` DISABLE KEYS */;
INSERT INTO `DATABASECHANGELOG_USER_MAILI` VALUES ('1.0','erik.derooij@deltares.nl','META-INF/usermailing-changelog.xml','2021-04-22 10:45:20',1,'EXECUTED','7:6e9818f583c408e4b23a5ddc88d4b89f','createTable tableName=USER_MAILING; addPrimaryKey constraintName=PK_USER_MAILING, tableName=USER_MAILING; addForeignKeyConstraint baseTableName=USER_MAILING, constraintName=FK_USER_MAILING_USER_ENTITY, referencedTableName=USER_ENTITY; addForeignKe...','',NULL,'3.5.4',NULL,NULL,'9088320541');
/*!40000 ALTER TABLE `DATABASECHANGELOG_USER_MAILI` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DEFAULT_CLIENT_SCOPE`
--

DROP TABLE IF EXISTS `DEFAULT_CLIENT_SCOPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DEFAULT_CLIENT_SCOPE` (
  `REALM_ID` varchar(36) NOT NULL,
  `SCOPE_ID` varchar(36) NOT NULL,
  `DEFAULT_SCOPE` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`REALM_ID`,`SCOPE_ID`),
  KEY `IDX_DEFCLS_REALM` (`REALM_ID`),
  KEY `IDX_DEFCLS_SCOPE` (`SCOPE_ID`),
  CONSTRAINT `FK_R_DEF_CLI_SCOPE_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`),
  CONSTRAINT `FK_R_DEF_CLI_SCOPE_SCOPE` FOREIGN KEY (`SCOPE_ID`) REFERENCES `CLIENT_SCOPE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DEFAULT_CLIENT_SCOPE`
--

LOCK TABLES `DEFAULT_CLIENT_SCOPE` WRITE;
/*!40000 ALTER TABLE `DEFAULT_CLIENT_SCOPE` DISABLE KEYS */;
INSERT INTO `DEFAULT_CLIENT_SCOPE` VALUES ('liferay-portal','3522f3d8-9fad-4c62-bfc8-000b9091361a',''),('liferay-portal','55164a6a-d9a0-4d36-a289-abafa7fc1551',''),('liferay-portal','7f259547-fc8a-4689-a51f-5991b1a93562',''),('liferay-portal','93b2f0f7-c201-4196-b6bc-2829369f70e2','\0'),('liferay-portal','c6dc943c-f19c-4f65-9116-38d8773fb28d',''),('liferay-portal','d3813bc9-fcb8-4d3e-bd5a-aefd3d4b4ca6',''),('liferay-portal','eb4606ff-4ec0-4ffd-980a-a735c2465aeb','\0'),('liferay-portal','fca7dd02-3fb8-45c2-b72e-0f4e9daeeca5','\0'),('master','105782f5-b3bb-48d5-8f77-0e5ecbfe116e','\0'),('master','12cb8b52-bfdf-4650-88be-998a34c53bd8',''),('master','3e58a816-cd84-48e6-8da1-2baa706dbdda','\0'),('master','534ed1af-eb77-46d7-b255-348e140a87af',''),('master','5bfaf140-06b1-46a8-9332-5b2b9520ebb4','\0'),('master','80bd4364-efe7-4991-a593-067f1c45c7ef','\0'),('master','ede142ae-567f-46a4-bce5-7e868321805d',''),('master','f1c5a5e7-78b0-4560-8162-3c5db1dbf768',''),('master','f820f43b-5e2f-4fd7-9e1a-2d24b88c4b11','');
/*!40000 ALTER TABLE `DEFAULT_CLIENT_SCOPE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `EVENT_ENTITY`
--

DROP TABLE IF EXISTS `EVENT_ENTITY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EVENT_ENTITY` (
  `ID` varchar(36) NOT NULL,
  `CLIENT_ID` varchar(255) DEFAULT NULL,
  `DETAILS_JSON` varchar(2550) DEFAULT NULL,
  `ERROR` varchar(255) DEFAULT NULL,
  `IP_ADDRESS` varchar(255) DEFAULT NULL,
  `REALM_ID` varchar(255) DEFAULT NULL,
  `SESSION_ID` varchar(255) DEFAULT NULL,
  `EVENT_TIME` bigint(20) DEFAULT NULL,
  `TYPE` varchar(255) DEFAULT NULL,
  `USER_ID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EVENT_ENTITY`
--

LOCK TABLES `EVENT_ENTITY` WRITE;
/*!40000 ALTER TABLE `EVENT_ENTITY` DISABLE KEYS */;
INSERT INTO `EVENT_ENTITY` VALUES ('83c53e98-36d7-4c64-a79a-2e9f62e1b415','account','{\"auth_method\":\"openid-connect\",\"auth_type\":\"code\",\"register_method\":\"form\",\"redirect_uri\":\"http://localhost:8080/auth/realms/liferay-portal/account/login-redirect?path=sessions\",\"code_id\":\"ce237b2f-c029-4108-9e9d-2538e3f56b80\",\"email\":\"c@test.nl\",\"username\":\"c\"}',NULL,'172.23.0.1','liferay-portal',NULL,1621349517213,'REGISTER','b1709578-5d32-41f9-937c-197a5fbd183e'),('b622e569-5cb2-434f-917d-4760ee90c50a','account','{\"auth_method\":\"openid-connect\",\"auth_type\":\"code\",\"register_method\":\"form\",\"redirect_uri\":\"http://localhost:8080/auth/realms/liferay-portal/account/login-redirect?path=sessions\",\"code_id\":\"8a570f7f-c7da-4003-8401-63bc44ba3c44\",\"email\":\"a@test.nl\",\"username\":\"a\"}',NULL,'172.23.0.1','liferay-portal',NULL,1621349485974,'REGISTER','63919307-da21-426a-a43a-4cf9cd60f12a');
/*!40000 ALTER TABLE `EVENT_ENTITY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FEDERATED_IDENTITY`
--

DROP TABLE IF EXISTS `FEDERATED_IDENTITY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FEDERATED_IDENTITY` (
  `IDENTITY_PROVIDER` varchar(255) NOT NULL,
  `REALM_ID` varchar(36) DEFAULT NULL,
  `FEDERATED_USER_ID` varchar(255) DEFAULT NULL,
  `FEDERATED_USERNAME` varchar(255) DEFAULT NULL,
  `TOKEN` text,
  `USER_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`IDENTITY_PROVIDER`,`USER_ID`),
  KEY `IDX_FEDIDENTITY_USER` (`USER_ID`),
  KEY `IDX_FEDIDENTITY_FEDUSER` (`FEDERATED_USER_ID`),
  CONSTRAINT `FK404288B92EF007A6` FOREIGN KEY (`USER_ID`) REFERENCES `USER_ENTITY` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FEDERATED_IDENTITY`
--

LOCK TABLES `FEDERATED_IDENTITY` WRITE;
/*!40000 ALTER TABLE `FEDERATED_IDENTITY` DISABLE KEYS */;
/*!40000 ALTER TABLE `FEDERATED_IDENTITY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FEDERATED_USER`
--

DROP TABLE IF EXISTS `FEDERATED_USER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FEDERATED_USER` (
  `ID` varchar(255) NOT NULL,
  `STORAGE_PROVIDER_ID` varchar(255) DEFAULT NULL,
  `REALM_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FEDERATED_USER`
--

LOCK TABLES `FEDERATED_USER` WRITE;
/*!40000 ALTER TABLE `FEDERATED_USER` DISABLE KEYS */;
/*!40000 ALTER TABLE `FEDERATED_USER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FED_CREDENTIAL_ATTRIBUTE`
--

DROP TABLE IF EXISTS `FED_CREDENTIAL_ATTRIBUTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FED_CREDENTIAL_ATTRIBUTE` (
  `ID` varchar(36) NOT NULL,
  `CREDENTIAL_ID` varchar(36) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `VALUE` varchar(4000) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_FED_CRED_ATTR_CRED` (`CREDENTIAL_ID`),
  CONSTRAINT `FK_FED_CRED_ATTR` FOREIGN KEY (`CREDENTIAL_ID`) REFERENCES `FED_USER_CREDENTIAL` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FED_CREDENTIAL_ATTRIBUTE`
--

LOCK TABLES `FED_CREDENTIAL_ATTRIBUTE` WRITE;
/*!40000 ALTER TABLE `FED_CREDENTIAL_ATTRIBUTE` DISABLE KEYS */;
/*!40000 ALTER TABLE `FED_CREDENTIAL_ATTRIBUTE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FED_USER_ATTRIBUTE`
--

DROP TABLE IF EXISTS `FED_USER_ATTRIBUTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FED_USER_ATTRIBUTE` (
  `ID` varchar(36) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `USER_ID` varchar(255) NOT NULL,
  `REALM_ID` varchar(36) NOT NULL,
  `STORAGE_PROVIDER_ID` varchar(36) DEFAULT NULL,
  `VALUE` varchar(2024) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_FU_ATTRIBUTE` (`USER_ID`,`REALM_ID`,`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FED_USER_ATTRIBUTE`
--

LOCK TABLES `FED_USER_ATTRIBUTE` WRITE;
/*!40000 ALTER TABLE `FED_USER_ATTRIBUTE` DISABLE KEYS */;
/*!40000 ALTER TABLE `FED_USER_ATTRIBUTE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FED_USER_CONSENT`
--

DROP TABLE IF EXISTS `FED_USER_CONSENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FED_USER_CONSENT` (
  `ID` varchar(36) NOT NULL,
  `CLIENT_ID` varchar(36) DEFAULT NULL,
  `USER_ID` varchar(255) NOT NULL,
  `REALM_ID` varchar(36) NOT NULL,
  `STORAGE_PROVIDER_ID` varchar(36) DEFAULT NULL,
  `CREATED_DATE` bigint(20) DEFAULT NULL,
  `LAST_UPDATED_DATE` bigint(20) DEFAULT NULL,
  `CLIENT_STORAGE_PROVIDER` varchar(36) DEFAULT NULL,
  `EXTERNAL_CLIENT_ID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_FU_CONSENT` (`USER_ID`,`CLIENT_ID`),
  KEY `IDX_FU_CONSENT_RU` (`REALM_ID`,`USER_ID`),
  KEY `IDX_FU_CNSNT_EXT` (`USER_ID`,`CLIENT_STORAGE_PROVIDER`,`EXTERNAL_CLIENT_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FED_USER_CONSENT`
--

LOCK TABLES `FED_USER_CONSENT` WRITE;
/*!40000 ALTER TABLE `FED_USER_CONSENT` DISABLE KEYS */;
/*!40000 ALTER TABLE `FED_USER_CONSENT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FED_USER_CONSENT_CL_SCOPE`
--

DROP TABLE IF EXISTS `FED_USER_CONSENT_CL_SCOPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FED_USER_CONSENT_CL_SCOPE` (
  `USER_CONSENT_ID` varchar(36) NOT NULL,
  `SCOPE_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`USER_CONSENT_ID`,`SCOPE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FED_USER_CONSENT_CL_SCOPE`
--

LOCK TABLES `FED_USER_CONSENT_CL_SCOPE` WRITE;
/*!40000 ALTER TABLE `FED_USER_CONSENT_CL_SCOPE` DISABLE KEYS */;
/*!40000 ALTER TABLE `FED_USER_CONSENT_CL_SCOPE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FED_USER_CREDENTIAL`
--

DROP TABLE IF EXISTS `FED_USER_CREDENTIAL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FED_USER_CREDENTIAL` (
  `ID` varchar(36) NOT NULL,
  `DEVICE` varchar(255) DEFAULT NULL,
  `HASH_ITERATIONS` int(11) DEFAULT NULL,
  `SALT` tinyblob,
  `TYPE` varchar(255) DEFAULT NULL,
  `VALUE` varchar(255) DEFAULT NULL,
  `CREATED_DATE` bigint(20) DEFAULT NULL,
  `COUNTER` int(11) DEFAULT '0',
  `DIGITS` int(11) DEFAULT '6',
  `PERIOD` int(11) DEFAULT '30',
  `ALGORITHM` varchar(36) DEFAULT 'HmacSHA1',
  `USER_ID` varchar(255) NOT NULL,
  `REALM_ID` varchar(36) NOT NULL,
  `STORAGE_PROVIDER_ID` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_FU_CREDENTIAL` (`USER_ID`,`TYPE`),
  KEY `IDX_FU_CREDENTIAL_RU` (`REALM_ID`,`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FED_USER_CREDENTIAL`
--

LOCK TABLES `FED_USER_CREDENTIAL` WRITE;
/*!40000 ALTER TABLE `FED_USER_CREDENTIAL` DISABLE KEYS */;
/*!40000 ALTER TABLE `FED_USER_CREDENTIAL` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FED_USER_GROUP_MEMBERSHIP`
--

DROP TABLE IF EXISTS `FED_USER_GROUP_MEMBERSHIP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FED_USER_GROUP_MEMBERSHIP` (
  `GROUP_ID` varchar(36) NOT NULL,
  `USER_ID` varchar(255) NOT NULL,
  `REALM_ID` varchar(36) NOT NULL,
  `STORAGE_PROVIDER_ID` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`GROUP_ID`,`USER_ID`),
  KEY `IDX_FU_GROUP_MEMBERSHIP` (`USER_ID`,`GROUP_ID`),
  KEY `IDX_FU_GROUP_MEMBERSHIP_RU` (`REALM_ID`,`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FED_USER_GROUP_MEMBERSHIP`
--

LOCK TABLES `FED_USER_GROUP_MEMBERSHIP` WRITE;
/*!40000 ALTER TABLE `FED_USER_GROUP_MEMBERSHIP` DISABLE KEYS */;
/*!40000 ALTER TABLE `FED_USER_GROUP_MEMBERSHIP` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FED_USER_REQUIRED_ACTION`
--

DROP TABLE IF EXISTS `FED_USER_REQUIRED_ACTION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FED_USER_REQUIRED_ACTION` (
  `REQUIRED_ACTION` varchar(255) NOT NULL DEFAULT ' ',
  `USER_ID` varchar(255) NOT NULL,
  `REALM_ID` varchar(36) NOT NULL,
  `STORAGE_PROVIDER_ID` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`REQUIRED_ACTION`,`USER_ID`),
  KEY `IDX_FU_REQUIRED_ACTION` (`USER_ID`,`REQUIRED_ACTION`),
  KEY `IDX_FU_REQUIRED_ACTION_RU` (`REALM_ID`,`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FED_USER_REQUIRED_ACTION`
--

LOCK TABLES `FED_USER_REQUIRED_ACTION` WRITE;
/*!40000 ALTER TABLE `FED_USER_REQUIRED_ACTION` DISABLE KEYS */;
/*!40000 ALTER TABLE `FED_USER_REQUIRED_ACTION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `FED_USER_ROLE_MAPPING`
--

DROP TABLE IF EXISTS `FED_USER_ROLE_MAPPING`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `FED_USER_ROLE_MAPPING` (
  `ROLE_ID` varchar(36) NOT NULL,
  `USER_ID` varchar(255) NOT NULL,
  `REALM_ID` varchar(36) NOT NULL,
  `STORAGE_PROVIDER_ID` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`ROLE_ID`,`USER_ID`),
  KEY `IDX_FU_ROLE_MAPPING` (`USER_ID`,`ROLE_ID`),
  KEY `IDX_FU_ROLE_MAPPING_RU` (`REALM_ID`,`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `FED_USER_ROLE_MAPPING`
--

LOCK TABLES `FED_USER_ROLE_MAPPING` WRITE;
/*!40000 ALTER TABLE `FED_USER_ROLE_MAPPING` DISABLE KEYS */;
/*!40000 ALTER TABLE `FED_USER_ROLE_MAPPING` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GROUP_ATTRIBUTE`
--

DROP TABLE IF EXISTS `GROUP_ATTRIBUTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GROUP_ATTRIBUTE` (
  `ID` varchar(36) NOT NULL DEFAULT 'sybase-needs-something-here',
  `NAME` varchar(255) NOT NULL,
  `VALUE` varchar(255) DEFAULT NULL,
  `GROUP_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_GROUP_ATTR_GROUP` (`GROUP_ID`),
  CONSTRAINT `FK_GROUP_ATTRIBUTE_GROUP` FOREIGN KEY (`GROUP_ID`) REFERENCES `KEYCLOAK_GROUP` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GROUP_ATTRIBUTE`
--

LOCK TABLES `GROUP_ATTRIBUTE` WRITE;
/*!40000 ALTER TABLE `GROUP_ATTRIBUTE` DISABLE KEYS */;
/*!40000 ALTER TABLE `GROUP_ATTRIBUTE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `GROUP_ROLE_MAPPING`
--

DROP TABLE IF EXISTS `GROUP_ROLE_MAPPING`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GROUP_ROLE_MAPPING` (
  `ROLE_ID` varchar(36) NOT NULL,
  `GROUP_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`ROLE_ID`,`GROUP_ID`),
  KEY `IDX_GROUP_ROLE_MAPP_GROUP` (`GROUP_ID`),
  CONSTRAINT `FK_GROUP_ROLE_GROUP` FOREIGN KEY (`GROUP_ID`) REFERENCES `KEYCLOAK_GROUP` (`ID`),
  CONSTRAINT `FK_GROUP_ROLE_ROLE` FOREIGN KEY (`ROLE_ID`) REFERENCES `KEYCLOAK_ROLE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GROUP_ROLE_MAPPING`
--

LOCK TABLES `GROUP_ROLE_MAPPING` WRITE;
/*!40000 ALTER TABLE `GROUP_ROLE_MAPPING` DISABLE KEYS */;
/*!40000 ALTER TABLE `GROUP_ROLE_MAPPING` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IDENTITY_PROVIDER`
--

DROP TABLE IF EXISTS `IDENTITY_PROVIDER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IDENTITY_PROVIDER` (
  `INTERNAL_ID` varchar(36) NOT NULL,
  `ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `PROVIDER_ALIAS` varchar(255) DEFAULT NULL,
  `PROVIDER_ID` varchar(255) DEFAULT NULL,
  `STORE_TOKEN` bit(1) NOT NULL DEFAULT b'0',
  `AUTHENTICATE_BY_DEFAULT` bit(1) NOT NULL DEFAULT b'0',
  `REALM_ID` varchar(36) DEFAULT NULL,
  `ADD_TOKEN_ROLE` bit(1) NOT NULL DEFAULT b'1',
  `TRUST_EMAIL` bit(1) NOT NULL DEFAULT b'0',
  `FIRST_BROKER_LOGIN_FLOW_ID` varchar(36) DEFAULT NULL,
  `POST_BROKER_LOGIN_FLOW_ID` varchar(36) DEFAULT NULL,
  `PROVIDER_DISPLAY_NAME` varchar(255) DEFAULT NULL,
  `LINK_ONLY` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`INTERNAL_ID`),
  UNIQUE KEY `UK_2DAELWNIBJI49AVXSRTUF6XJ33` (`PROVIDER_ALIAS`,`REALM_ID`),
  KEY `IDX_IDENT_PROV_REALM` (`REALM_ID`),
  CONSTRAINT `FK2B4EBC52AE5C3B34` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IDENTITY_PROVIDER`
--

LOCK TABLES `IDENTITY_PROVIDER` WRITE;
/*!40000 ALTER TABLE `IDENTITY_PROVIDER` DISABLE KEYS */;
/*!40000 ALTER TABLE `IDENTITY_PROVIDER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IDENTITY_PROVIDER_CONFIG`
--

DROP TABLE IF EXISTS `IDENTITY_PROVIDER_CONFIG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IDENTITY_PROVIDER_CONFIG` (
  `IDENTITY_PROVIDER_ID` varchar(36) NOT NULL,
  `VALUE` longtext,
  `NAME` varchar(255) NOT NULL,
  PRIMARY KEY (`IDENTITY_PROVIDER_ID`,`NAME`),
  CONSTRAINT `FKDC4897CF864C4E43` FOREIGN KEY (`IDENTITY_PROVIDER_ID`) REFERENCES `IDENTITY_PROVIDER` (`INTERNAL_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IDENTITY_PROVIDER_CONFIG`
--

LOCK TABLES `IDENTITY_PROVIDER_CONFIG` WRITE;
/*!40000 ALTER TABLE `IDENTITY_PROVIDER_CONFIG` DISABLE KEYS */;
/*!40000 ALTER TABLE `IDENTITY_PROVIDER_CONFIG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IDENTITY_PROVIDER_MAPPER`
--

DROP TABLE IF EXISTS `IDENTITY_PROVIDER_MAPPER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IDENTITY_PROVIDER_MAPPER` (
  `ID` varchar(36) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `IDP_ALIAS` varchar(255) NOT NULL,
  `IDP_MAPPER_NAME` varchar(255) NOT NULL,
  `REALM_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_ID_PROV_MAPP_REALM` (`REALM_ID`),
  CONSTRAINT `FK_IDPM_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IDENTITY_PROVIDER_MAPPER`
--

LOCK TABLES `IDENTITY_PROVIDER_MAPPER` WRITE;
/*!40000 ALTER TABLE `IDENTITY_PROVIDER_MAPPER` DISABLE KEYS */;
/*!40000 ALTER TABLE `IDENTITY_PROVIDER_MAPPER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IDP_MAPPER_CONFIG`
--

DROP TABLE IF EXISTS `IDP_MAPPER_CONFIG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IDP_MAPPER_CONFIG` (
  `IDP_MAPPER_ID` varchar(36) NOT NULL,
  `VALUE` longtext,
  `NAME` varchar(255) NOT NULL,
  PRIMARY KEY (`IDP_MAPPER_ID`,`NAME`),
  CONSTRAINT `FK_IDPMCONFIG` FOREIGN KEY (`IDP_MAPPER_ID`) REFERENCES `IDENTITY_PROVIDER_MAPPER` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IDP_MAPPER_CONFIG`
--

LOCK TABLES `IDP_MAPPER_CONFIG` WRITE;
/*!40000 ALTER TABLE `IDP_MAPPER_CONFIG` DISABLE KEYS */;
/*!40000 ALTER TABLE `IDP_MAPPER_CONFIG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `KEYCLOAK_GROUP`
--

DROP TABLE IF EXISTS `KEYCLOAK_GROUP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `KEYCLOAK_GROUP` (
  `ID` varchar(36) NOT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `PARENT_GROUP` varchar(36) DEFAULT NULL,
  `REALM_ID` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `SIBLING_NAMES` (`REALM_ID`,`PARENT_GROUP`,`NAME`),
  CONSTRAINT `FK_GROUP_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `KEYCLOAK_GROUP`
--

LOCK TABLES `KEYCLOAK_GROUP` WRITE;
/*!40000 ALTER TABLE `KEYCLOAK_GROUP` DISABLE KEYS */;
/*!40000 ALTER TABLE `KEYCLOAK_GROUP` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `KEYCLOAK_ROLE`
--

DROP TABLE IF EXISTS `KEYCLOAK_ROLE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `KEYCLOAK_ROLE` (
  `ID` varchar(36) NOT NULL,
  `CLIENT_REALM_CONSTRAINT` varchar(36) DEFAULT NULL,
  `CLIENT_ROLE` bit(1) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `REALM_ID` varchar(255) DEFAULT NULL,
  `CLIENT` varchar(36) DEFAULT NULL,
  `REALM` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_J3RWUVD56ONTGSUHOGM184WW2-2` (`NAME`,`CLIENT_REALM_CONSTRAINT`),
  KEY `IDX_KEYCLOAK_ROLE_CLIENT` (`CLIENT`),
  KEY `IDX_KEYCLOAK_ROLE_REALM` (`REALM`),
  CONSTRAINT `FK_6VYQFE4CN4WLQ8R6KT5VDSJ5C` FOREIGN KEY (`REALM`) REFERENCES `REALM` (`ID`),
  CONSTRAINT `FK_KJHO5LE2C0RAL09FL8CM9WFW9` FOREIGN KEY (`CLIENT`) REFERENCES `CLIENT` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `KEYCLOAK_ROLE`
--

LOCK TABLES `KEYCLOAK_ROLE` WRITE;
/*!40000 ALTER TABLE `KEYCLOAK_ROLE` DISABLE KEYS */;
INSERT INTO `KEYCLOAK_ROLE` VALUES ('012f4c09-2656-4603-aac9-4a132a391e94','master','\0','${role_offline-access}','offline_access','master',NULL,'master'),('020fb301-6df2-41e8-8f3d-372d5ee70642','3453a904-cef0-4361-9226-54d6fa84af18','','${role_query-clients}','query-clients','master','3453a904-cef0-4361-9226-54d6fa84af18',NULL),('06f1980a-cdbc-4fe3-a2aa-01ea57eae2ce','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6','','${role_impersonation}','impersonation','liferay-portal','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6',NULL),('0ab906cb-cc4f-4af4-9dd4-e676e6f0f9f3','f1b13f7c-a916-4b24-b314-0200496926ce','','${role_manage-account}','manage-account','liferay-portal','f1b13f7c-a916-4b24-b314-0200496926ce',NULL),('14756887-8dc0-414b-9396-515ed0f1f083','a6cac270-1663-4123-8db5-54ccaaee5871','','${role_manage-realm}','manage-realm','master','a6cac270-1663-4123-8db5-54ccaaee5871',NULL),('174374c7-a893-4867-98b4-22b624d927ee','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6','','${role_view-authorization}','view-authorization','liferay-portal','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6',NULL),('19e2e60b-2aa1-4a5c-91dc-45c0e9424418','3453a904-cef0-4361-9226-54d6fa84af18','','${role_manage-events}','manage-events','master','3453a904-cef0-4361-9226-54d6fa84af18',NULL),('1e6947a7-c960-4b23-80c5-fd5ee8f9496f','3453a904-cef0-4361-9226-54d6fa84af18','','${role_impersonation}','impersonation','master','3453a904-cef0-4361-9226-54d6fa84af18',NULL),('297f8ae3-b7f1-4a35-b0a7-1f103a282bc9','liferay-portal','\0','Liferay administrator','liferay-admin','liferay-portal',NULL,'liferay-portal'),('2a68b9ec-af76-495d-86b6-90a498ef0a87','3453a904-cef0-4361-9226-54d6fa84af18','','${role_view-identity-providers}','view-identity-providers','master','3453a904-cef0-4361-9226-54d6fa84af18',NULL),('2f42ba40-29d9-4010-927a-fb5b09d9aacd','3453a904-cef0-4361-9226-54d6fa84af18','','${role_view-users}','view-users','master','3453a904-cef0-4361-9226-54d6fa84af18',NULL),('2f50935c-4449-4cf9-9e99-25df0a53a179','a6cac270-1663-4123-8db5-54ccaaee5871','','${role_query-clients}','query-clients','master','a6cac270-1663-4123-8db5-54ccaaee5871',NULL),('2ff71c9b-391d-4e49-8871-c0429f5d3ada','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6','','${role_view-identity-providers}','view-identity-providers','liferay-portal','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6',NULL),('34644efb-56e5-4902-92df-29b540d3d474','3453a904-cef0-4361-9226-54d6fa84af18','','${role_manage-identity-providers}','manage-identity-providers','master','3453a904-cef0-4361-9226-54d6fa84af18',NULL),('3742ea4c-5aa9-4112-ad1c-4f3bdc870c93','a6cac270-1663-4123-8db5-54ccaaee5871','','${role_manage-clients}','manage-clients','master','a6cac270-1663-4123-8db5-54ccaaee5871',NULL),('375717d0-db43-469d-a2bc-2b514bf8251c','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6','','${role_view-realm}','view-realm','liferay-portal','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6',NULL),('3d9c5e71-233a-4e29-9e60-f5d9129f9c57','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6','','${role_view-users}','view-users','liferay-portal','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6',NULL),('3dd32d36-12ae-4b3f-88e7-77808d6ccce3','a6cac270-1663-4123-8db5-54ccaaee5871','','${role_query-groups}','query-groups','master','a6cac270-1663-4123-8db5-54ccaaee5871',NULL),('3f427fa4-f17f-452c-b8ad-018e2bc2a412','a6cac270-1663-4123-8db5-54ccaaee5871','','${role_manage-events}','manage-events','master','a6cac270-1663-4123-8db5-54ccaaee5871',NULL),('43c67ebb-198b-4547-96e9-86b7a0111566','c5f79f09-95d5-4a96-a845-47bad8536eb7','','${role_view-profile}','view-profile','master','c5f79f09-95d5-4a96-a845-47bad8536eb7',NULL),('43f3a380-168b-49c9-a68f-0683e8c1bd35','a6cac270-1663-4123-8db5-54ccaaee5871','','${role_create-client}','create-client','master','a6cac270-1663-4123-8db5-54ccaaee5871',NULL),('4ae9f0e8-1f16-44ef-90c9-558002aee88b','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6','','${role_manage-users}','manage-users','liferay-portal','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6',NULL),('54fb69c9-7442-485a-a65b-8e59a4899faa','3453a904-cef0-4361-9226-54d6fa84af18','','${role_view-realm}','view-realm','master','3453a904-cef0-4361-9226-54d6fa84af18',NULL),('5b4697e2-773c-4c06-a4b3-76dfef5433f5','a6cac270-1663-4123-8db5-54ccaaee5871','','${role_view-authorization}','view-authorization','master','a6cac270-1663-4123-8db5-54ccaaee5871',NULL),('5e57a33b-de71-4df9-b941-05bd9d599478','f1b13f7c-a916-4b24-b314-0200496926ce','','${role_manage-account-links}','manage-account-links','liferay-portal','f1b13f7c-a916-4b24-b314-0200496926ce',NULL),('5f6c2d9f-744b-49f1-ad3a-03c1abbbe1f5','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6','','${role_query-realms}','query-realms','liferay-portal','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6',NULL),('64a2ceb2-c94c-461f-869a-4d2d0806c389','master','\0','${role_admin}','admin','master',NULL,'master'),('65a1d897-fd63-41cd-9aa8-4142e08b6c7d','a6cac270-1663-4123-8db5-54ccaaee5871','','${role_manage-users}','manage-users','master','a6cac270-1663-4123-8db5-54ccaaee5871',NULL),('661a9e04-bccd-47da-97a8-0592e21f3e84','3453a904-cef0-4361-9226-54d6fa84af18','','${role_manage-users}','manage-users','master','3453a904-cef0-4361-9226-54d6fa84af18',NULL),('67372504-f0fc-4713-bd22-7f90a7f394c0','3453a904-cef0-4361-9226-54d6fa84af18','','${role_view-clients}','view-clients','master','3453a904-cef0-4361-9226-54d6fa84af18',NULL),('68fb355d-4023-4176-9227-f6f4dfe73b91','a6cac270-1663-4123-8db5-54ccaaee5871','','${role_view-users}','view-users','master','a6cac270-1663-4123-8db5-54ccaaee5871',NULL),('6a2b87d7-7299-4268-a39a-9c4bef2087b8','3453a904-cef0-4361-9226-54d6fa84af18','','${role_create-client}','create-client','master','3453a904-cef0-4361-9226-54d6fa84af18',NULL),('6bce23fb-3261-45ec-be62-0eebd45e6df9','3453a904-cef0-4361-9226-54d6fa84af18','','${role_manage-authorization}','manage-authorization','master','3453a904-cef0-4361-9226-54d6fa84af18',NULL),('6ec8ccad-ff0e-40e6-a429-295f985ff0c9','07a7d9af-fe25-4479-8d74-9ddc0baeb35e','',NULL,'uma_protection','liferay-portal','07a7d9af-fe25-4479-8d74-9ddc0baeb35e',NULL),('73788df5-8b96-4a88-bd3a-17f99b717b46','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6','',NULL,'uma_protection','liferay-portal','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6',NULL),('73cc2ecd-a445-4e40-bbc2-e117dce12165','3453a904-cef0-4361-9226-54d6fa84af18','','${role_query-groups}','query-groups','master','3453a904-cef0-4361-9226-54d6fa84af18',NULL),('789a51ae-8ae9-406b-a9bf-02fa6df70bac','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6','','${role_create-client}','create-client','liferay-portal','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6',NULL),('7c636332-3fd3-4f72-bc53-551b3b91d5bf','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6','','${role_manage-realm}','manage-realm','liferay-portal','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6',NULL),('7ed9fd1b-7561-443d-941c-91b4a28a5974','liferay-portal','\0','','view','liferay-portal',NULL,'liferay-portal'),('7f6fa3f4-51b5-440e-9ab4-b6d7f3bab57e','f9a0e8ea-d2c7-4ea1-b66f-26022f37d23b','','${role_read-token}','read-token','master','f9a0e8ea-d2c7-4ea1-b66f-26022f37d23b',NULL),('84bfe1ab-3fa2-493f-a52a-20cd26d5d6ad','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6','','${role_realm-admin}','realm-admin','liferay-portal','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6',NULL),('84c3ea10-f27d-44a0-b7c6-d20938ef151f','9b3c4d13-0a92-46a4-a07c-e412e33024d7','','${role_read-token}','read-token','liferay-portal','9b3c4d13-0a92-46a4-a07c-e412e33024d7',NULL),('86f1eac0-b367-44c1-92e8-3db92d638e77','3453a904-cef0-4361-9226-54d6fa84af18','','${role_view-events}','view-events','master','3453a904-cef0-4361-9226-54d6fa84af18',NULL),('8720933a-030e-436e-90b3-e44b6f678051','a6cac270-1663-4123-8db5-54ccaaee5871','','${role_query-users}','query-users','master','a6cac270-1663-4123-8db5-54ccaaee5871',NULL),('8925dd2c-2339-4e90-a56b-f99d31c66209','3453a904-cef0-4361-9226-54d6fa84af18','','${role_manage-realm}','manage-realm','master','3453a904-cef0-4361-9226-54d6fa84af18',NULL),('8c430ca8-55e7-43c6-8625-527ab3f3ec3f','f1b13f7c-a916-4b24-b314-0200496926ce','','${role_view-profile}','view-profile','liferay-portal','f1b13f7c-a916-4b24-b314-0200496926ce',NULL),('907300b5-9e9a-423d-96fe-1c5604d00dc9','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6','','${role_manage-authorization}','manage-authorization','liferay-portal','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6',NULL),('936fdecc-b13d-47dc-85af-91b8512aca71','a6cac270-1663-4123-8db5-54ccaaee5871','','${role_view-realm}','view-realm','master','a6cac270-1663-4123-8db5-54ccaaee5871',NULL),('95925b41-f82f-4530-b8b8-14084b85ebc4','3453a904-cef0-4361-9226-54d6fa84af18','','${role_query-realms}','query-realms','master','3453a904-cef0-4361-9226-54d6fa84af18',NULL),('992f5685-d4b5-44a8-8745-78ef23eee8b4','27a25fc2-29bd-4afd-a135-a5b2b8be12b8','',NULL,'uma_protection','liferay-portal','27a25fc2-29bd-4afd-a135-a5b2b8be12b8',NULL),('996812e8-82a2-4b44-9b38-0cad02f391ec','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6','','${role_view-events}','view-events','liferay-portal','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6',NULL),('a8772b31-9ccd-42b4-aff3-b9a354828e95','67d6d3e3-38a7-4562-aac7-03832d713532','',NULL,'uma_protection','liferay-portal','67d6d3e3-38a7-4562-aac7-03832d713532',NULL),('b6d58577-5e67-4e02-a166-80da656d8c57','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6','','${role_manage-clients}','manage-clients','liferay-portal','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6',NULL),('b930fbec-a391-40dc-887c-a36fe46999a7','c5f79f09-95d5-4a96-a845-47bad8536eb7','','${role_manage-account}','manage-account','master','c5f79f09-95d5-4a96-a845-47bad8536eb7',NULL),('bd640cda-cc3d-4c22-8a33-d41de2c73c64','a6cac270-1663-4123-8db5-54ccaaee5871','','${role_view-identity-providers}','view-identity-providers','master','a6cac270-1663-4123-8db5-54ccaaee5871',NULL),('beda296e-ad85-43a6-9d77-d37193147efa','liferay-portal','\0','${role_uma_authorization}','uma_authorization','liferay-portal',NULL,'liferay-portal'),('bf76efcd-e2c7-4ba3-9f8a-507e787c26b2','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6','','${role_manage-identity-providers}','manage-identity-providers','liferay-portal','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6',NULL),('c219942a-04da-492e-929a-82bf4c3954df','a6cac270-1663-4123-8db5-54ccaaee5871','','${role_view-clients}','view-clients','master','a6cac270-1663-4123-8db5-54ccaaee5871',NULL),('c524cfb5-69f9-4423-8eab-e057118149ef','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6','','${role_query-groups}','query-groups','liferay-portal','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6',NULL),('c9052b8c-51dc-456d-9fe5-05a0eeafe756','a6cac270-1663-4123-8db5-54ccaaee5871','','${role_impersonation}','impersonation','master','a6cac270-1663-4123-8db5-54ccaaee5871',NULL),('d725efa3-0a0e-4aa9-879d-7f099957ea15','c5f79f09-95d5-4a96-a845-47bad8536eb7','','${role_manage-account-links}','manage-account-links','master','c5f79f09-95d5-4a96-a845-47bad8536eb7',NULL),('d76e15a1-3afe-435d-8720-69c533fd4b18','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6','','${role_query-clients}','query-clients','liferay-portal','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6',NULL),('dd93723b-74a7-419e-9588-9c115cf6f833','a6cac270-1663-4123-8db5-54ccaaee5871','','${role_view-events}','view-events','master','a6cac270-1663-4123-8db5-54ccaaee5871',NULL),('df21d69e-011e-41c6-b8f4-983b4b7b1c01','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6','','${role_query-users}','query-users','liferay-portal','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6',NULL),('e2846703-970e-497d-8d13-d36de5ee7d9d','3453a904-cef0-4361-9226-54d6fa84af18','','${role_view-authorization}','view-authorization','master','3453a904-cef0-4361-9226-54d6fa84af18',NULL),('e2a5c5db-7689-4aff-beb1-1b9b9252a080','a6cac270-1663-4123-8db5-54ccaaee5871','','${role_manage-identity-providers}','manage-identity-providers','master','a6cac270-1663-4123-8db5-54ccaaee5871',NULL),('e5a2418f-2c2c-42fc-8aff-5f08d0509832','3453a904-cef0-4361-9226-54d6fa84af18','','${role_query-users}','query-users','master','3453a904-cef0-4361-9226-54d6fa84af18',NULL),('e5db3777-0d0b-4d47-9e72-5b32e740b339','master','\0','${role_uma_authorization}','uma_authorization','master',NULL,'master'),('e708f5db-ed07-42aa-911f-fd5d0d955754','a6cac270-1663-4123-8db5-54ccaaee5871','','${role_manage-authorization}','manage-authorization','master','a6cac270-1663-4123-8db5-54ccaaee5871',NULL),('e9f8d2a1-f49f-40c2-bd7b-d12efe053aa6','master','\0','${role_create-realm}','create-realm','master',NULL,'master'),('eb738200-9d7e-4235-86b8-f5351679f7df','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6','','${role_manage-events}','manage-events','liferay-portal','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6',NULL),('f234d8d8-c739-481b-a6ef-cd3c2963449b','3453a904-cef0-4361-9226-54d6fa84af18','','${role_manage-clients}','manage-clients','master','3453a904-cef0-4361-9226-54d6fa84af18',NULL),('f4e93900-0b7d-4fa3-b2ca-523715fff9d1','liferay-portal','\0','${role_offline-access}','offline_access','liferay-portal',NULL,'liferay-portal'),('f9f0481f-3bdd-4cfd-923d-917b1b7e40ef','a6cac270-1663-4123-8db5-54ccaaee5871','','${role_query-realms}','query-realms','master','a6cac270-1663-4123-8db5-54ccaaee5871',NULL),('fcc87b08-b814-48c0-baee-97f9b81de4e4','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6','','${role_view-clients}','view-clients','liferay-portal','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6',NULL);
/*!40000 ALTER TABLE `KEYCLOAK_ROLE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MAILING_ENTITY`
--

DROP TABLE IF EXISTS `MAILING_ENTITY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MAILING_ENTITY` (
  `ID` varchar(36) NOT NULL,
  `REALM_ID` varchar(36) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `LANGUAGES` varchar(36) NOT NULL DEFAULT 'en' COMMENT 'array of available publications “en”;”nl”;…',
  `FREQUENCY` tinyint(4) NOT NULL DEFAULT '4' COMMENT '0,1,2,3 (weekly, monthly, annual, other)',
  `DELIVERY` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0,1 or 2 (email/post/both)',
  `CREATED_TIMESTAMP` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_MAILING_NAME` (`REALM_ID`,`NAME`),
  CONSTRAINT `FK_MAILING_REALM_ENTITY` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MAILING_ENTITY`
--

LOCK TABLES `MAILING_ENTITY` WRITE;
/*!40000 ALTER TABLE `MAILING_ENTITY` DISABLE KEYS */;
INSERT INTO `MAILING_ENTITY` VALUES ('0054724c-daaf-408b-94ac-6bed7e109404','liferay-portal','Test Mailing 1','','nl',0,0,1621350055721),('15e5024c-83cb-4c91-a4d5-4ecb3e76eda8','liferay-portal','Test Mailing 2','This is a dummy','en',0,0,1621350067634);
/*!40000 ALTER TABLE `MAILING_ENTITY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MIGRATION_MODEL`
--

DROP TABLE IF EXISTS `MIGRATION_MODEL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MIGRATION_MODEL` (
  `ID` varchar(36) NOT NULL,
  `VERSION` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MIGRATION_MODEL`
--

LOCK TABLES `MIGRATION_MODEL` WRITE;
/*!40000 ALTER TABLE `MIGRATION_MODEL` DISABLE KEYS */;
INSERT INTO `MIGRATION_MODEL` VALUES ('SINGLETON','4.6.0');
/*!40000 ALTER TABLE `MIGRATION_MODEL` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `OFFLINE_CLIENT_SESSION`
--

DROP TABLE IF EXISTS `OFFLINE_CLIENT_SESSION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `OFFLINE_CLIENT_SESSION` (
  `USER_SESSION_ID` varchar(36) NOT NULL,
  `CLIENT_ID` varchar(36) NOT NULL,
  `OFFLINE_FLAG` varchar(4) NOT NULL,
  `TIMESTAMP` int(11) DEFAULT NULL,
  `DATA` longtext,
  `CLIENT_STORAGE_PROVIDER` varchar(36) NOT NULL DEFAULT 'local',
  `EXTERNAL_CLIENT_ID` varchar(255) NOT NULL DEFAULT 'local',
  PRIMARY KEY (`USER_SESSION_ID`,`CLIENT_ID`,`CLIENT_STORAGE_PROVIDER`,`EXTERNAL_CLIENT_ID`,`OFFLINE_FLAG`),
  KEY `IDX_US_SESS_ID_ON_CL_SESS` (`USER_SESSION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `OFFLINE_CLIENT_SESSION`
--

LOCK TABLES `OFFLINE_CLIENT_SESSION` WRITE;
/*!40000 ALTER TABLE `OFFLINE_CLIENT_SESSION` DISABLE KEYS */;
/*!40000 ALTER TABLE `OFFLINE_CLIENT_SESSION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `OFFLINE_USER_SESSION`
--

DROP TABLE IF EXISTS `OFFLINE_USER_SESSION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `OFFLINE_USER_SESSION` (
  `USER_SESSION_ID` varchar(36) NOT NULL,
  `USER_ID` varchar(255) DEFAULT NULL,
  `REALM_ID` varchar(36) NOT NULL,
  `CREATED_ON` int(11) NOT NULL,
  `OFFLINE_FLAG` varchar(4) NOT NULL,
  `DATA` longtext,
  `LAST_SESSION_REFRESH` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`USER_SESSION_ID`,`OFFLINE_FLAG`),
  KEY `IDX_OFFLINE_USS_CREATEDON` (`CREATED_ON`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `OFFLINE_USER_SESSION`
--

LOCK TABLES `OFFLINE_USER_SESSION` WRITE;
/*!40000 ALTER TABLE `OFFLINE_USER_SESSION` DISABLE KEYS */;
/*!40000 ALTER TABLE `OFFLINE_USER_SESSION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `POLICY_CONFIG`
--

DROP TABLE IF EXISTS `POLICY_CONFIG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `POLICY_CONFIG` (
  `POLICY_ID` varchar(36) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `VALUE` longtext,
  PRIMARY KEY (`POLICY_ID`,`NAME`),
  CONSTRAINT `FKDC34197CF864C4E43` FOREIGN KEY (`POLICY_ID`) REFERENCES `RESOURCE_SERVER_POLICY` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `POLICY_CONFIG`
--

LOCK TABLES `POLICY_CONFIG` WRITE;
/*!40000 ALTER TABLE `POLICY_CONFIG` DISABLE KEYS */;
INSERT INTO `POLICY_CONFIG` VALUES ('1d1be5b2-368b-4f73-91ef-f5fff682aa73','defaultResourceType','urn:api-user:resources:default'),('464a4f94-3ebe-4b25-a773-43004e2e73d6','defaultResourceType','urn:api-admin:resources:default'),('7e75f663-76ca-42cc-92ce-732e3caf409a','code','// by default, grants any permission associated with this policy\n$evaluation.grant();\n'),('8119b5a3-d21e-4c4b-bac8-134c495e0a77','code','// by default, grants any permission associated with this policy\n$evaluation.grant();\n'),('d66a85ba-62e8-49ed-b0cc-29ab865a86e5','defaultResourceType','urn:api-viewer:resources:default'),('eca0e811-43db-41f8-a3b9-5d37f235e1b3','code','// by default, grants any permission associated with this policy\n$evaluation.grant();\n');
/*!40000 ALTER TABLE `POLICY_CONFIG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PROTOCOL_MAPPER`
--

DROP TABLE IF EXISTS `PROTOCOL_MAPPER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PROTOCOL_MAPPER` (
  `ID` varchar(36) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `PROTOCOL` varchar(255) NOT NULL,
  `PROTOCOL_MAPPER_NAME` varchar(255) NOT NULL,
  `CLIENT_ID` varchar(36) DEFAULT NULL,
  `CLIENT_SCOPE_ID` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_PROTOCOL_MAPPER_CLIENT` (`CLIENT_ID`),
  KEY `IDX_CLSCOPE_PROTMAP` (`CLIENT_SCOPE_ID`),
  CONSTRAINT `FK_CLI_SCOPE_MAPPER` FOREIGN KEY (`CLIENT_SCOPE_ID`) REFERENCES `CLIENT_SCOPE` (`ID`),
  CONSTRAINT `FK_PCM_REALM` FOREIGN KEY (`CLIENT_ID`) REFERENCES `CLIENT` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PROTOCOL_MAPPER`
--

LOCK TABLES `PROTOCOL_MAPPER` WRITE;
/*!40000 ALTER TABLE `PROTOCOL_MAPPER` DISABLE KEYS */;
INSERT INTO `PROTOCOL_MAPPER` VALUES ('01a7b21a-9e19-4450-8aff-1ecc2ba6cc0a','phone number','openid-connect','oidc-usermodel-attribute-mapper',NULL,'93b2f0f7-c201-4196-b6bc-2829369f70e2'),('045d423c-8990-4044-bae1-1f3661c6d668','Client ID','openid-connect','oidc-usersessionmodel-note-mapper','27a25fc2-29bd-4afd-a135-a5b2b8be12b8',NULL),('06f791b2-84e5-4fac-95ca-d7c534cce961','Client Host','openid-connect','oidc-usersessionmodel-note-mapper','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6',NULL),('06f8dcce-bf1b-492c-999d-adce28f0b07d','profile','openid-connect','oidc-usermodel-attribute-mapper',NULL,'f1c5a5e7-78b0-4560-8162-3c5db1dbf768'),('07d8e212-3f95-4184-8bdf-de7bd9bc03bf','locale','openid-connect','oidc-usermodel-attribute-mapper',NULL,'3522f3d8-9fad-4c62-bfc8-000b9091361a'),('0f5034f0-64f5-4cc5-80f8-e4e240a78865','family name','openid-connect','oidc-usermodel-property-mapper',NULL,'3522f3d8-9fad-4c62-bfc8-000b9091361a'),('143a9c11-ddf2-4369-b4fc-8717de072403','picture','openid-connect','oidc-usermodel-attribute-mapper',NULL,'f1c5a5e7-78b0-4560-8162-3c5db1dbf768'),('15db7f20-f92e-4d8e-bbd0-34f62ee69fa5','middle name','openid-connect','oidc-usermodel-attribute-mapper',NULL,'f1c5a5e7-78b0-4560-8162-3c5db1dbf768'),('1c80b47d-8ed3-4563-a449-c5da5995d1e2','phone number verified','openid-connect','oidc-usermodel-attribute-mapper',NULL,'93b2f0f7-c201-4196-b6bc-2829369f70e2'),('23c27fd3-6a87-48d3-895e-769c0e2835cc','phone number verified','openid-connect','oidc-usermodel-attribute-mapper',NULL,'105782f5-b3bb-48d5-8f77-0e5ecbfe116e'),('2458f314-f212-4462-abe1-5eaf2ac3d8b0','picture','openid-connect','oidc-usermodel-attribute-mapper',NULL,'3522f3d8-9fad-4c62-bfc8-000b9091361a'),('25912d18-92e7-4180-b490-389d20dbe86e','birthdate','openid-connect','oidc-usermodel-attribute-mapper',NULL,'f1c5a5e7-78b0-4560-8162-3c5db1dbf768'),('2a75bf7d-c172-49e3-9f3d-2df3eea7ccd1','Organization name','openid-connect','oidc-usermodel-attribute-mapper',NULL,'3522f3d8-9fad-4c62-bfc8-000b9091361a'),('2b14df9c-8490-43b9-8f96-7dbdeba820bd','email verified','openid-connect','oidc-usermodel-property-mapper',NULL,'7f259547-fc8a-4689-a51f-5991b1a93562'),('2b6f2c57-8a29-433e-95e2-425c150c9bc4','website','openid-connect','oidc-usermodel-attribute-mapper',NULL,'3522f3d8-9fad-4c62-bfc8-000b9091361a'),('2cdd150b-e8ae-4d48-9314-c901953e7fce','address','openid-connect','oidc-address-mapper',NULL,'80bd4364-efe7-4991-a593-067f1c45c7ef'),('2f77770d-ae82-49fd-a28c-1a50f85cfbe7','email verified','openid-connect','oidc-usermodel-property-mapper',NULL,'12cb8b52-bfdf-4650-88be-998a34c53bd8'),('32032ddd-2c2d-4eae-b2d8-fbec8ec27287','birthdate','openid-connect','oidc-usermodel-attribute-mapper',NULL,'3522f3d8-9fad-4c62-bfc8-000b9091361a'),('360cd76b-5d9d-4945-83e3-f58374073b64','audience resolve','openid-connect','oidc-audience-resolve-mapper',NULL,'ede142ae-567f-46a4-bce5-7e868321805d'),('3e79c98a-dce6-4703-8e3e-e29b67eed953','gender','openid-connect','oidc-usermodel-attribute-mapper',NULL,'3522f3d8-9fad-4c62-bfc8-000b9091361a'),('416e1204-e475-455d-a035-281d74c0946e','gender','openid-connect','oidc-usermodel-attribute-mapper',NULL,'f1c5a5e7-78b0-4560-8162-3c5db1dbf768'),('44e8d22d-0a71-410f-84b4-eb6e0cffb977','Client ID','openid-connect','oidc-usersessionmodel-note-mapper','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6',NULL),('4fb974c1-cc35-403a-924a-8b401fd816aa','role list','saml','saml-role-list-mapper',NULL,'534ed1af-eb77-46d7-b255-348e140a87af'),('53abc792-a449-4a0d-9387-82aa17ccd3c9','Client Host','openid-connect','oidc-usersessionmodel-note-mapper','07a7d9af-fe25-4479-8d74-9ddc0baeb35e',NULL),('567ef905-63e4-416a-90a4-58c22bcf43d6','locale','openid-connect','oidc-usermodel-attribute-mapper',NULL,'f1c5a5e7-78b0-4560-8162-3c5db1dbf768'),('584073bb-6875-4f78-8d56-5df274bcff72','phone number','openid-connect','oidc-usermodel-attribute-mapper',NULL,'105782f5-b3bb-48d5-8f77-0e5ecbfe116e'),('5e8c6d7e-667e-4183-8cc1-8dc63cb5606c','email','openid-connect','oidc-usermodel-property-mapper',NULL,'12cb8b52-bfdf-4650-88be-998a34c53bd8'),('67ca5089-9320-4388-b3c0-0c7c709ff966','full name','openid-connect','oidc-full-name-mapper',NULL,'3522f3d8-9fad-4c62-bfc8-000b9091361a'),('6b821bca-5532-4510-a43e-e2b062bb155e','groups','openid-connect','oidc-usermodel-realm-role-mapper',NULL,'3e58a816-cd84-48e6-8da1-2baa706dbdda'),('6c547391-05e3-4819-91f3-24ecffd26488','given name','openid-connect','oidc-usermodel-property-mapper',NULL,'3522f3d8-9fad-4c62-bfc8-000b9091361a'),('6cd91773-7ee3-4e9d-be6f-c840f0127bb1','Client Host','openid-connect','oidc-usersessionmodel-note-mapper','67d6d3e3-38a7-4562-aac7-03832d713532',NULL),('6df7c096-0a63-442e-b152-1a9cd1a8f0b4','family name','openid-connect','oidc-usermodel-property-mapper',NULL,'f1c5a5e7-78b0-4560-8162-3c5db1dbf768'),('6f1d227b-54df-4888-a3ba-81884c41224d','role list','saml','saml-role-list-mapper',NULL,'55164a6a-d9a0-4d36-a289-abafa7fc1551'),('6f9e8935-da3a-45bf-ba2e-242beff49bc3','username','openid-connect','oidc-usermodel-property-mapper',NULL,'3522f3d8-9fad-4c62-bfc8-000b9091361a'),('76b2d4d8-9ca2-4645-84ae-92446f2394a0','Client IP Address','openid-connect','oidc-usersessionmodel-note-mapper','07a7d9af-fe25-4479-8d74-9ddc0baeb35e',NULL),('76f41b6d-6704-4f6b-87af-fa2cdada20ad','profile','openid-connect','oidc-usermodel-attribute-mapper',NULL,'3522f3d8-9fad-4c62-bfc8-000b9091361a'),('7960d8e7-8a2f-4e22-9a21-751ffa26e094','updated at','openid-connect','oidc-usermodel-attribute-mapper',NULL,'3522f3d8-9fad-4c62-bfc8-000b9091361a'),('8521f557-223a-43f7-9472-b7654534d3e7','client roles','openid-connect','oidc-usermodel-client-role-mapper',NULL,'d3813bc9-fcb8-4d3e-bd5a-aefd3d4b4ca6'),('86e7260c-3995-4da4-aaba-355465da2a1e','allowed web origins','openid-connect','oidc-allowed-origins-mapper',NULL,'c6dc943c-f19c-4f65-9116-38d8773fb28d'),('8ce14cac-aca7-4ee2-9ff2-37610bc58828','Client Host','openid-connect','oidc-usersessionmodel-note-mapper','27a25fc2-29bd-4afd-a135-a5b2b8be12b8',NULL),('95e06f1c-7f95-4c83-99ba-51c53fa28a2e','full name','openid-connect','oidc-full-name-mapper',NULL,'f1c5a5e7-78b0-4560-8162-3c5db1dbf768'),('99c3f3a1-cf2d-4498-8efc-ba563fb88eac','username','openid-connect','oidc-usermodel-property-mapper',NULL,'f1c5a5e7-78b0-4560-8162-3c5db1dbf768'),('9dc000da-80d7-418b-a5ea-48ffbbf119d7','Client IP Address','openid-connect','oidc-usersessionmodel-note-mapper','27a25fc2-29bd-4afd-a135-a5b2b8be12b8',NULL),('a10e61b0-67f7-4f26-9b6a-f127167d83e8','zoneinfo','openid-connect','oidc-usermodel-attribute-mapper',NULL,'f1c5a5e7-78b0-4560-8162-3c5db1dbf768'),('a1f7f9c1-bf2d-48f5-bf75-649474b39426','given name','openid-connect','oidc-usermodel-property-mapper',NULL,'f1c5a5e7-78b0-4560-8162-3c5db1dbf768'),('a7ae66b7-61a4-4d56-a3e6-5ad2c96e38d7','allowed web origins','openid-connect','oidc-allowed-origins-mapper',NULL,'f820f43b-5e2f-4fd7-9e1a-2d24b88c4b11'),('a8032276-8a51-4223-8843-e90b49dbb604','Client IP Address','openid-connect','oidc-usersessionmodel-note-mapper','7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6',NULL),('b181f52a-406f-43a0-a77f-16a34b2b6fc8','realm roles','openid-connect','oidc-usermodel-realm-role-mapper',NULL,'d3813bc9-fcb8-4d3e-bd5a-aefd3d4b4ca6'),('b541e4e1-8929-40d3-b62f-7fe1f2046690','nickname','openid-connect','oidc-usermodel-attribute-mapper',NULL,'f1c5a5e7-78b0-4560-8162-3c5db1dbf768'),('bc1a2d75-2ade-4bb1-ac01-5d11ea676816','realm roles','openid-connect','oidc-usermodel-realm-role-mapper',NULL,'ede142ae-567f-46a4-bce5-7e868321805d'),('c1e8460d-84d3-4736-a851-cdbf2f83acc6','email','openid-connect','oidc-usermodel-property-mapper',NULL,'7f259547-fc8a-4689-a51f-5991b1a93562'),('c1f338af-1d4e-4311-91b6-92ed6488410b','upn','openid-connect','oidc-usermodel-property-mapper',NULL,'3e58a816-cd84-48e6-8da1-2baa706dbdda'),('c569ab91-32b5-457b-a559-e86738517797','address','openid-connect','oidc-address-mapper',NULL,'eb4606ff-4ec0-4ffd-980a-a735c2465aeb'),('c730e294-8c33-465b-a1a9-a42a0a1a79db','Client IP Address','openid-connect','oidc-usersessionmodel-note-mapper','f1b13f7c-a916-4b24-b314-0200496926ce',NULL),('c77be71a-b281-4532-8d89-f663caa47d8a','Client ID','openid-connect','oidc-usersessionmodel-note-mapper','07a7d9af-fe25-4479-8d74-9ddc0baeb35e',NULL),('cc24a115-a033-4a43-8669-efc955721694','audience resolve','openid-connect','oidc-audience-resolve-mapper',NULL,'d3813bc9-fcb8-4d3e-bd5a-aefd3d4b4ca6'),('cc947cdb-8e1e-4b67-a17f-079f7277f21c','Client ID','openid-connect','oidc-usersessionmodel-note-mapper','f1b13f7c-a916-4b24-b314-0200496926ce',NULL),('cd68315c-0d20-4cfe-9a52-a7f019501b98','Client ID','openid-connect','oidc-usersessionmodel-note-mapper','67d6d3e3-38a7-4562-aac7-03832d713532',NULL),('ce5940df-d2b4-4d0e-a520-afcfb40016a1','locale','openid-connect','oidc-usermodel-attribute-mapper','d6d25c43-45e4-4107-89b4-7fc91bca0f80',NULL),('d9535228-87c2-4872-aa16-2e21347044bd','client roles','openid-connect','oidc-usermodel-client-role-mapper',NULL,'ede142ae-567f-46a4-bce5-7e868321805d'),('dad203c1-eacb-4d73-be8a-907eafca608b','nickname','openid-connect','oidc-usermodel-attribute-mapper',NULL,'3522f3d8-9fad-4c62-bfc8-000b9091361a'),('e00d3801-9894-4862-8eb0-a567adb18cf5','updated at','openid-connect','oidc-usermodel-attribute-mapper',NULL,'f1c5a5e7-78b0-4560-8162-3c5db1dbf768'),('e1916664-741a-45bd-ad97-5cbe0422ce0d','Client Host','openid-connect','oidc-usersessionmodel-note-mapper','f1b13f7c-a916-4b24-b314-0200496926ce',NULL),('e38548f4-d688-4266-bf51-a38d58adbab6','middle name','openid-connect','oidc-usermodel-attribute-mapper',NULL,'3522f3d8-9fad-4c62-bfc8-000b9091361a'),('e39f0f4d-ff37-417e-8ab6-aaa4bfd1df11','locale','openid-connect','oidc-usermodel-attribute-mapper','364c3e71-9195-4162-9d08-e048421fdd0f',NULL),('eaf0c207-03e4-4f06-8814-2a4e0f054bcf','Client IP Address','openid-connect','oidc-usersessionmodel-note-mapper','67d6d3e3-38a7-4562-aac7-03832d713532',NULL),('eb79ff4c-d4d3-4327-b15c-a26f01eb7dfc','zoneinfo','openid-connect','oidc-usermodel-attribute-mapper',NULL,'3522f3d8-9fad-4c62-bfc8-000b9091361a'),('fa6d1f7c-908a-4ed8-a0e4-390ea01dadeb','website','openid-connect','oidc-usermodel-attribute-mapper',NULL,'f1c5a5e7-78b0-4560-8162-3c5db1dbf768');
/*!40000 ALTER TABLE `PROTOCOL_MAPPER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PROTOCOL_MAPPER_CONFIG`
--

DROP TABLE IF EXISTS `PROTOCOL_MAPPER_CONFIG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PROTOCOL_MAPPER_CONFIG` (
  `PROTOCOL_MAPPER_ID` varchar(36) NOT NULL,
  `VALUE` longtext,
  `NAME` varchar(255) NOT NULL,
  PRIMARY KEY (`PROTOCOL_MAPPER_ID`,`NAME`),
  CONSTRAINT `FK_PMCONFIG` FOREIGN KEY (`PROTOCOL_MAPPER_ID`) REFERENCES `PROTOCOL_MAPPER` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PROTOCOL_MAPPER_CONFIG`
--

LOCK TABLES `PROTOCOL_MAPPER_CONFIG` WRITE;
/*!40000 ALTER TABLE `PROTOCOL_MAPPER_CONFIG` DISABLE KEYS */;
INSERT INTO `PROTOCOL_MAPPER_CONFIG` VALUES ('01a7b21a-9e19-4450-8aff-1ecc2ba6cc0a','true','access.token.claim'),('01a7b21a-9e19-4450-8aff-1ecc2ba6cc0a','phone_number','claim.name'),('01a7b21a-9e19-4450-8aff-1ecc2ba6cc0a','true','id.token.claim'),('01a7b21a-9e19-4450-8aff-1ecc2ba6cc0a','String','jsonType.label'),('01a7b21a-9e19-4450-8aff-1ecc2ba6cc0a','phoneNumber','user.attribute'),('01a7b21a-9e19-4450-8aff-1ecc2ba6cc0a','true','userinfo.token.claim'),('045d423c-8990-4044-bae1-1f3661c6d668','true','access.token.claim'),('045d423c-8990-4044-bae1-1f3661c6d668','clientId','claim.name'),('045d423c-8990-4044-bae1-1f3661c6d668','true','id.token.claim'),('045d423c-8990-4044-bae1-1f3661c6d668','String','jsonType.label'),('045d423c-8990-4044-bae1-1f3661c6d668','clientId','user.session.note'),('045d423c-8990-4044-bae1-1f3661c6d668','true','userinfo.token.claim'),('06f791b2-84e5-4fac-95ca-d7c534cce961','true','access.token.claim'),('06f791b2-84e5-4fac-95ca-d7c534cce961','clientHost','claim.name'),('06f791b2-84e5-4fac-95ca-d7c534cce961','true','id.token.claim'),('06f791b2-84e5-4fac-95ca-d7c534cce961','String','jsonType.label'),('06f791b2-84e5-4fac-95ca-d7c534cce961','clientHost','user.session.note'),('06f791b2-84e5-4fac-95ca-d7c534cce961','true','userinfo.token.claim'),('06f8dcce-bf1b-492c-999d-adce28f0b07d','true','access.token.claim'),('06f8dcce-bf1b-492c-999d-adce28f0b07d','profile','claim.name'),('06f8dcce-bf1b-492c-999d-adce28f0b07d','true','id.token.claim'),('06f8dcce-bf1b-492c-999d-adce28f0b07d','String','jsonType.label'),('06f8dcce-bf1b-492c-999d-adce28f0b07d','profile','user.attribute'),('06f8dcce-bf1b-492c-999d-adce28f0b07d','true','userinfo.token.claim'),('07d8e212-3f95-4184-8bdf-de7bd9bc03bf','true','access.token.claim'),('07d8e212-3f95-4184-8bdf-de7bd9bc03bf','locale','claim.name'),('07d8e212-3f95-4184-8bdf-de7bd9bc03bf','true','id.token.claim'),('07d8e212-3f95-4184-8bdf-de7bd9bc03bf','String','jsonType.label'),('07d8e212-3f95-4184-8bdf-de7bd9bc03bf','locale','user.attribute'),('07d8e212-3f95-4184-8bdf-de7bd9bc03bf','true','userinfo.token.claim'),('0f5034f0-64f5-4cc5-80f8-e4e240a78865','true','access.token.claim'),('0f5034f0-64f5-4cc5-80f8-e4e240a78865','family_name','claim.name'),('0f5034f0-64f5-4cc5-80f8-e4e240a78865','true','id.token.claim'),('0f5034f0-64f5-4cc5-80f8-e4e240a78865','String','jsonType.label'),('0f5034f0-64f5-4cc5-80f8-e4e240a78865','lastName','user.attribute'),('0f5034f0-64f5-4cc5-80f8-e4e240a78865','true','userinfo.token.claim'),('143a9c11-ddf2-4369-b4fc-8717de072403','true','access.token.claim'),('143a9c11-ddf2-4369-b4fc-8717de072403','picture','claim.name'),('143a9c11-ddf2-4369-b4fc-8717de072403','true','id.token.claim'),('143a9c11-ddf2-4369-b4fc-8717de072403','String','jsonType.label'),('143a9c11-ddf2-4369-b4fc-8717de072403','picture','user.attribute'),('143a9c11-ddf2-4369-b4fc-8717de072403','true','userinfo.token.claim'),('15db7f20-f92e-4d8e-bbd0-34f62ee69fa5','true','access.token.claim'),('15db7f20-f92e-4d8e-bbd0-34f62ee69fa5','middle_name','claim.name'),('15db7f20-f92e-4d8e-bbd0-34f62ee69fa5','true','id.token.claim'),('15db7f20-f92e-4d8e-bbd0-34f62ee69fa5','String','jsonType.label'),('15db7f20-f92e-4d8e-bbd0-34f62ee69fa5','middleName','user.attribute'),('15db7f20-f92e-4d8e-bbd0-34f62ee69fa5','true','userinfo.token.claim'),('1c80b47d-8ed3-4563-a449-c5da5995d1e2','true','access.token.claim'),('1c80b47d-8ed3-4563-a449-c5da5995d1e2','phone_number_verified','claim.name'),('1c80b47d-8ed3-4563-a449-c5da5995d1e2','true','id.token.claim'),('1c80b47d-8ed3-4563-a449-c5da5995d1e2','boolean','jsonType.label'),('1c80b47d-8ed3-4563-a449-c5da5995d1e2','phoneNumberVerified','user.attribute'),('1c80b47d-8ed3-4563-a449-c5da5995d1e2','true','userinfo.token.claim'),('23c27fd3-6a87-48d3-895e-769c0e2835cc','true','access.token.claim'),('23c27fd3-6a87-48d3-895e-769c0e2835cc','phone_number_verified','claim.name'),('23c27fd3-6a87-48d3-895e-769c0e2835cc','true','id.token.claim'),('23c27fd3-6a87-48d3-895e-769c0e2835cc','boolean','jsonType.label'),('23c27fd3-6a87-48d3-895e-769c0e2835cc','phoneNumberVerified','user.attribute'),('23c27fd3-6a87-48d3-895e-769c0e2835cc','true','userinfo.token.claim'),('2458f314-f212-4462-abe1-5eaf2ac3d8b0','true','access.token.claim'),('2458f314-f212-4462-abe1-5eaf2ac3d8b0','picture','claim.name'),('2458f314-f212-4462-abe1-5eaf2ac3d8b0','true','id.token.claim'),('2458f314-f212-4462-abe1-5eaf2ac3d8b0','String','jsonType.label'),('2458f314-f212-4462-abe1-5eaf2ac3d8b0','picture','user.attribute'),('2458f314-f212-4462-abe1-5eaf2ac3d8b0','true','userinfo.token.claim'),('25912d18-92e7-4180-b490-389d20dbe86e','true','access.token.claim'),('25912d18-92e7-4180-b490-389d20dbe86e','birthdate','claim.name'),('25912d18-92e7-4180-b490-389d20dbe86e','true','id.token.claim'),('25912d18-92e7-4180-b490-389d20dbe86e','String','jsonType.label'),('25912d18-92e7-4180-b490-389d20dbe86e','birthdate','user.attribute'),('25912d18-92e7-4180-b490-389d20dbe86e','true','userinfo.token.claim'),('2a75bf7d-c172-49e3-9f3d-2df3eea7ccd1','true','access.token.claim'),('2a75bf7d-c172-49e3-9f3d-2df3eea7ccd1','org_name','claim.name'),('2a75bf7d-c172-49e3-9f3d-2df3eea7ccd1','true','id.token.claim'),('2a75bf7d-c172-49e3-9f3d-2df3eea7ccd1','String','jsonType.label'),('2a75bf7d-c172-49e3-9f3d-2df3eea7ccd1','org_name','user.attribute'),('2a75bf7d-c172-49e3-9f3d-2df3eea7ccd1','true','userinfo.token.claim'),('2b14df9c-8490-43b9-8f96-7dbdeba820bd','true','access.token.claim'),('2b14df9c-8490-43b9-8f96-7dbdeba820bd','email_verified','claim.name'),('2b14df9c-8490-43b9-8f96-7dbdeba820bd','true','id.token.claim'),('2b14df9c-8490-43b9-8f96-7dbdeba820bd','boolean','jsonType.label'),('2b14df9c-8490-43b9-8f96-7dbdeba820bd','emailVerified','user.attribute'),('2b14df9c-8490-43b9-8f96-7dbdeba820bd','true','userinfo.token.claim'),('2b6f2c57-8a29-433e-95e2-425c150c9bc4','true','access.token.claim'),('2b6f2c57-8a29-433e-95e2-425c150c9bc4','website','claim.name'),('2b6f2c57-8a29-433e-95e2-425c150c9bc4','true','id.token.claim'),('2b6f2c57-8a29-433e-95e2-425c150c9bc4','String','jsonType.label'),('2b6f2c57-8a29-433e-95e2-425c150c9bc4','website','user.attribute'),('2b6f2c57-8a29-433e-95e2-425c150c9bc4','true','userinfo.token.claim'),('2cdd150b-e8ae-4d48-9314-c901953e7fce','true','access.token.claim'),('2cdd150b-e8ae-4d48-9314-c901953e7fce','true','id.token.claim'),('2cdd150b-e8ae-4d48-9314-c901953e7fce','country','user.attribute.country'),('2cdd150b-e8ae-4d48-9314-c901953e7fce','formatted','user.attribute.formatted'),('2cdd150b-e8ae-4d48-9314-c901953e7fce','locality','user.attribute.locality'),('2cdd150b-e8ae-4d48-9314-c901953e7fce','postal_code','user.attribute.postal_code'),('2cdd150b-e8ae-4d48-9314-c901953e7fce','region','user.attribute.region'),('2cdd150b-e8ae-4d48-9314-c901953e7fce','street','user.attribute.street'),('2cdd150b-e8ae-4d48-9314-c901953e7fce','true','userinfo.token.claim'),('2f77770d-ae82-49fd-a28c-1a50f85cfbe7','true','access.token.claim'),('2f77770d-ae82-49fd-a28c-1a50f85cfbe7','email_verified','claim.name'),('2f77770d-ae82-49fd-a28c-1a50f85cfbe7','true','id.token.claim'),('2f77770d-ae82-49fd-a28c-1a50f85cfbe7','boolean','jsonType.label'),('2f77770d-ae82-49fd-a28c-1a50f85cfbe7','emailVerified','user.attribute'),('2f77770d-ae82-49fd-a28c-1a50f85cfbe7','true','userinfo.token.claim'),('32032ddd-2c2d-4eae-b2d8-fbec8ec27287','true','access.token.claim'),('32032ddd-2c2d-4eae-b2d8-fbec8ec27287','birthdate','claim.name'),('32032ddd-2c2d-4eae-b2d8-fbec8ec27287','true','id.token.claim'),('32032ddd-2c2d-4eae-b2d8-fbec8ec27287','String','jsonType.label'),('32032ddd-2c2d-4eae-b2d8-fbec8ec27287','birthdate','user.attribute'),('32032ddd-2c2d-4eae-b2d8-fbec8ec27287','true','userinfo.token.claim'),('3e79c98a-dce6-4703-8e3e-e29b67eed953','true','access.token.claim'),('3e79c98a-dce6-4703-8e3e-e29b67eed953','gender','claim.name'),('3e79c98a-dce6-4703-8e3e-e29b67eed953','true','id.token.claim'),('3e79c98a-dce6-4703-8e3e-e29b67eed953','String','jsonType.label'),('3e79c98a-dce6-4703-8e3e-e29b67eed953','gender','user.attribute'),('3e79c98a-dce6-4703-8e3e-e29b67eed953','true','userinfo.token.claim'),('416e1204-e475-455d-a035-281d74c0946e','true','access.token.claim'),('416e1204-e475-455d-a035-281d74c0946e','gender','claim.name'),('416e1204-e475-455d-a035-281d74c0946e','true','id.token.claim'),('416e1204-e475-455d-a035-281d74c0946e','String','jsonType.label'),('416e1204-e475-455d-a035-281d74c0946e','gender','user.attribute'),('416e1204-e475-455d-a035-281d74c0946e','true','userinfo.token.claim'),('44e8d22d-0a71-410f-84b4-eb6e0cffb977','true','access.token.claim'),('44e8d22d-0a71-410f-84b4-eb6e0cffb977','clientId','claim.name'),('44e8d22d-0a71-410f-84b4-eb6e0cffb977','true','id.token.claim'),('44e8d22d-0a71-410f-84b4-eb6e0cffb977','String','jsonType.label'),('44e8d22d-0a71-410f-84b4-eb6e0cffb977','clientId','user.session.note'),('44e8d22d-0a71-410f-84b4-eb6e0cffb977','true','userinfo.token.claim'),('4fb974c1-cc35-403a-924a-8b401fd816aa','Role','attribute.name'),('4fb974c1-cc35-403a-924a-8b401fd816aa','Basic','attribute.nameformat'),('4fb974c1-cc35-403a-924a-8b401fd816aa','false','single'),('53abc792-a449-4a0d-9387-82aa17ccd3c9','true','access.token.claim'),('53abc792-a449-4a0d-9387-82aa17ccd3c9','clientHost','claim.name'),('53abc792-a449-4a0d-9387-82aa17ccd3c9','true','id.token.claim'),('53abc792-a449-4a0d-9387-82aa17ccd3c9','String','jsonType.label'),('53abc792-a449-4a0d-9387-82aa17ccd3c9','clientHost','user.session.note'),('53abc792-a449-4a0d-9387-82aa17ccd3c9','true','userinfo.token.claim'),('567ef905-63e4-416a-90a4-58c22bcf43d6','true','access.token.claim'),('567ef905-63e4-416a-90a4-58c22bcf43d6','locale','claim.name'),('567ef905-63e4-416a-90a4-58c22bcf43d6','true','id.token.claim'),('567ef905-63e4-416a-90a4-58c22bcf43d6','String','jsonType.label'),('567ef905-63e4-416a-90a4-58c22bcf43d6','locale','user.attribute'),('567ef905-63e4-416a-90a4-58c22bcf43d6','true','userinfo.token.claim'),('584073bb-6875-4f78-8d56-5df274bcff72','true','access.token.claim'),('584073bb-6875-4f78-8d56-5df274bcff72','phone_number','claim.name'),('584073bb-6875-4f78-8d56-5df274bcff72','true','id.token.claim'),('584073bb-6875-4f78-8d56-5df274bcff72','String','jsonType.label'),('584073bb-6875-4f78-8d56-5df274bcff72','phoneNumber','user.attribute'),('584073bb-6875-4f78-8d56-5df274bcff72','true','userinfo.token.claim'),('5e8c6d7e-667e-4183-8cc1-8dc63cb5606c','true','access.token.claim'),('5e8c6d7e-667e-4183-8cc1-8dc63cb5606c','email','claim.name'),('5e8c6d7e-667e-4183-8cc1-8dc63cb5606c','true','id.token.claim'),('5e8c6d7e-667e-4183-8cc1-8dc63cb5606c','String','jsonType.label'),('5e8c6d7e-667e-4183-8cc1-8dc63cb5606c','email','user.attribute'),('5e8c6d7e-667e-4183-8cc1-8dc63cb5606c','true','userinfo.token.claim'),('67ca5089-9320-4388-b3c0-0c7c709ff966','true','access.token.claim'),('67ca5089-9320-4388-b3c0-0c7c709ff966','true','id.token.claim'),('67ca5089-9320-4388-b3c0-0c7c709ff966','true','userinfo.token.claim'),('6b821bca-5532-4510-a43e-e2b062bb155e','true','access.token.claim'),('6b821bca-5532-4510-a43e-e2b062bb155e','groups','claim.name'),('6b821bca-5532-4510-a43e-e2b062bb155e','true','id.token.claim'),('6b821bca-5532-4510-a43e-e2b062bb155e','String','jsonType.label'),('6b821bca-5532-4510-a43e-e2b062bb155e','true','multivalued'),('6b821bca-5532-4510-a43e-e2b062bb155e','foo','user.attribute'),('6c547391-05e3-4819-91f3-24ecffd26488','true','access.token.claim'),('6c547391-05e3-4819-91f3-24ecffd26488','given_name','claim.name'),('6c547391-05e3-4819-91f3-24ecffd26488','true','id.token.claim'),('6c547391-05e3-4819-91f3-24ecffd26488','String','jsonType.label'),('6c547391-05e3-4819-91f3-24ecffd26488','firstName','user.attribute'),('6c547391-05e3-4819-91f3-24ecffd26488','true','userinfo.token.claim'),('6cd91773-7ee3-4e9d-be6f-c840f0127bb1','true','access.token.claim'),('6cd91773-7ee3-4e9d-be6f-c840f0127bb1','clientHost','claim.name'),('6cd91773-7ee3-4e9d-be6f-c840f0127bb1','true','id.token.claim'),('6cd91773-7ee3-4e9d-be6f-c840f0127bb1','String','jsonType.label'),('6cd91773-7ee3-4e9d-be6f-c840f0127bb1','clientHost','user.session.note'),('6cd91773-7ee3-4e9d-be6f-c840f0127bb1','true','userinfo.token.claim'),('6df7c096-0a63-442e-b152-1a9cd1a8f0b4','true','access.token.claim'),('6df7c096-0a63-442e-b152-1a9cd1a8f0b4','family_name','claim.name'),('6df7c096-0a63-442e-b152-1a9cd1a8f0b4','true','id.token.claim'),('6df7c096-0a63-442e-b152-1a9cd1a8f0b4','String','jsonType.label'),('6df7c096-0a63-442e-b152-1a9cd1a8f0b4','lastName','user.attribute'),('6df7c096-0a63-442e-b152-1a9cd1a8f0b4','true','userinfo.token.claim'),('6f1d227b-54df-4888-a3ba-81884c41224d','Role','attribute.name'),('6f1d227b-54df-4888-a3ba-81884c41224d','Basic','attribute.nameformat'),('6f1d227b-54df-4888-a3ba-81884c41224d','false','single'),('6f9e8935-da3a-45bf-ba2e-242beff49bc3','true','access.token.claim'),('6f9e8935-da3a-45bf-ba2e-242beff49bc3','preferred_username','claim.name'),('6f9e8935-da3a-45bf-ba2e-242beff49bc3','true','id.token.claim'),('6f9e8935-da3a-45bf-ba2e-242beff49bc3','String','jsonType.label'),('6f9e8935-da3a-45bf-ba2e-242beff49bc3','username','user.attribute'),('6f9e8935-da3a-45bf-ba2e-242beff49bc3','true','userinfo.token.claim'),('76b2d4d8-9ca2-4645-84ae-92446f2394a0','true','access.token.claim'),('76b2d4d8-9ca2-4645-84ae-92446f2394a0','clientAddress','claim.name'),('76b2d4d8-9ca2-4645-84ae-92446f2394a0','true','id.token.claim'),('76b2d4d8-9ca2-4645-84ae-92446f2394a0','String','jsonType.label'),('76b2d4d8-9ca2-4645-84ae-92446f2394a0','clientAddress','user.session.note'),('76b2d4d8-9ca2-4645-84ae-92446f2394a0','true','userinfo.token.claim'),('76f41b6d-6704-4f6b-87af-fa2cdada20ad','true','access.token.claim'),('76f41b6d-6704-4f6b-87af-fa2cdada20ad','profile','claim.name'),('76f41b6d-6704-4f6b-87af-fa2cdada20ad','true','id.token.claim'),('76f41b6d-6704-4f6b-87af-fa2cdada20ad','String','jsonType.label'),('76f41b6d-6704-4f6b-87af-fa2cdada20ad','profile','user.attribute'),('76f41b6d-6704-4f6b-87af-fa2cdada20ad','true','userinfo.token.claim'),('7960d8e7-8a2f-4e22-9a21-751ffa26e094','true','access.token.claim'),('7960d8e7-8a2f-4e22-9a21-751ffa26e094','updated_at','claim.name'),('7960d8e7-8a2f-4e22-9a21-751ffa26e094','true','id.token.claim'),('7960d8e7-8a2f-4e22-9a21-751ffa26e094','String','jsonType.label'),('7960d8e7-8a2f-4e22-9a21-751ffa26e094','updatedAt','user.attribute'),('7960d8e7-8a2f-4e22-9a21-751ffa26e094','true','userinfo.token.claim'),('8521f557-223a-43f7-9472-b7654534d3e7','true','access.token.claim'),('8521f557-223a-43f7-9472-b7654534d3e7','resource_access.${client_id}.roles','claim.name'),('8521f557-223a-43f7-9472-b7654534d3e7','String','jsonType.label'),('8521f557-223a-43f7-9472-b7654534d3e7','true','multivalued'),('8521f557-223a-43f7-9472-b7654534d3e7','foo','user.attribute'),('8ce14cac-aca7-4ee2-9ff2-37610bc58828','true','access.token.claim'),('8ce14cac-aca7-4ee2-9ff2-37610bc58828','clientHost','claim.name'),('8ce14cac-aca7-4ee2-9ff2-37610bc58828','true','id.token.claim'),('8ce14cac-aca7-4ee2-9ff2-37610bc58828','String','jsonType.label'),('8ce14cac-aca7-4ee2-9ff2-37610bc58828','clientHost','user.session.note'),('8ce14cac-aca7-4ee2-9ff2-37610bc58828','true','userinfo.token.claim'),('95e06f1c-7f95-4c83-99ba-51c53fa28a2e','true','access.token.claim'),('95e06f1c-7f95-4c83-99ba-51c53fa28a2e','true','id.token.claim'),('95e06f1c-7f95-4c83-99ba-51c53fa28a2e','true','userinfo.token.claim'),('99c3f3a1-cf2d-4498-8efc-ba563fb88eac','true','access.token.claim'),('99c3f3a1-cf2d-4498-8efc-ba563fb88eac','preferred_username','claim.name'),('99c3f3a1-cf2d-4498-8efc-ba563fb88eac','true','id.token.claim'),('99c3f3a1-cf2d-4498-8efc-ba563fb88eac','String','jsonType.label'),('99c3f3a1-cf2d-4498-8efc-ba563fb88eac','username','user.attribute'),('99c3f3a1-cf2d-4498-8efc-ba563fb88eac','true','userinfo.token.claim'),('9dc000da-80d7-418b-a5ea-48ffbbf119d7','true','access.token.claim'),('9dc000da-80d7-418b-a5ea-48ffbbf119d7','clientAddress','claim.name'),('9dc000da-80d7-418b-a5ea-48ffbbf119d7','true','id.token.claim'),('9dc000da-80d7-418b-a5ea-48ffbbf119d7','String','jsonType.label'),('9dc000da-80d7-418b-a5ea-48ffbbf119d7','clientAddress','user.session.note'),('9dc000da-80d7-418b-a5ea-48ffbbf119d7','true','userinfo.token.claim'),('a10e61b0-67f7-4f26-9b6a-f127167d83e8','true','access.token.claim'),('a10e61b0-67f7-4f26-9b6a-f127167d83e8','zoneinfo','claim.name'),('a10e61b0-67f7-4f26-9b6a-f127167d83e8','true','id.token.claim'),('a10e61b0-67f7-4f26-9b6a-f127167d83e8','String','jsonType.label'),('a10e61b0-67f7-4f26-9b6a-f127167d83e8','zoneinfo','user.attribute'),('a10e61b0-67f7-4f26-9b6a-f127167d83e8','true','userinfo.token.claim'),('a1f7f9c1-bf2d-48f5-bf75-649474b39426','true','access.token.claim'),('a1f7f9c1-bf2d-48f5-bf75-649474b39426','given_name','claim.name'),('a1f7f9c1-bf2d-48f5-bf75-649474b39426','true','id.token.claim'),('a1f7f9c1-bf2d-48f5-bf75-649474b39426','String','jsonType.label'),('a1f7f9c1-bf2d-48f5-bf75-649474b39426','firstName','user.attribute'),('a1f7f9c1-bf2d-48f5-bf75-649474b39426','true','userinfo.token.claim'),('a8032276-8a51-4223-8843-e90b49dbb604','true','access.token.claim'),('a8032276-8a51-4223-8843-e90b49dbb604','clientAddress','claim.name'),('a8032276-8a51-4223-8843-e90b49dbb604','true','id.token.claim'),('a8032276-8a51-4223-8843-e90b49dbb604','String','jsonType.label'),('a8032276-8a51-4223-8843-e90b49dbb604','clientAddress','user.session.note'),('a8032276-8a51-4223-8843-e90b49dbb604','true','userinfo.token.claim'),('b181f52a-406f-43a0-a77f-16a34b2b6fc8','true','access.token.claim'),('b181f52a-406f-43a0-a77f-16a34b2b6fc8','realm_access.roles','claim.name'),('b181f52a-406f-43a0-a77f-16a34b2b6fc8','String','jsonType.label'),('b181f52a-406f-43a0-a77f-16a34b2b6fc8','true','multivalued'),('b181f52a-406f-43a0-a77f-16a34b2b6fc8','foo','user.attribute'),('b541e4e1-8929-40d3-b62f-7fe1f2046690','true','access.token.claim'),('b541e4e1-8929-40d3-b62f-7fe1f2046690','nickname','claim.name'),('b541e4e1-8929-40d3-b62f-7fe1f2046690','true','id.token.claim'),('b541e4e1-8929-40d3-b62f-7fe1f2046690','String','jsonType.label'),('b541e4e1-8929-40d3-b62f-7fe1f2046690','nickname','user.attribute'),('b541e4e1-8929-40d3-b62f-7fe1f2046690','true','userinfo.token.claim'),('bc1a2d75-2ade-4bb1-ac01-5d11ea676816','true','access.token.claim'),('bc1a2d75-2ade-4bb1-ac01-5d11ea676816','realm_access.roles','claim.name'),('bc1a2d75-2ade-4bb1-ac01-5d11ea676816','String','jsonType.label'),('bc1a2d75-2ade-4bb1-ac01-5d11ea676816','true','multivalued'),('bc1a2d75-2ade-4bb1-ac01-5d11ea676816','foo','user.attribute'),('c1e8460d-84d3-4736-a851-cdbf2f83acc6','true','access.token.claim'),('c1e8460d-84d3-4736-a851-cdbf2f83acc6','email','claim.name'),('c1e8460d-84d3-4736-a851-cdbf2f83acc6','true','id.token.claim'),('c1e8460d-84d3-4736-a851-cdbf2f83acc6','String','jsonType.label'),('c1e8460d-84d3-4736-a851-cdbf2f83acc6','email','user.attribute'),('c1e8460d-84d3-4736-a851-cdbf2f83acc6','true','userinfo.token.claim'),('c1f338af-1d4e-4311-91b6-92ed6488410b','true','access.token.claim'),('c1f338af-1d4e-4311-91b6-92ed6488410b','upn','claim.name'),('c1f338af-1d4e-4311-91b6-92ed6488410b','true','id.token.claim'),('c1f338af-1d4e-4311-91b6-92ed6488410b','String','jsonType.label'),('c1f338af-1d4e-4311-91b6-92ed6488410b','username','user.attribute'),('c1f338af-1d4e-4311-91b6-92ed6488410b','true','userinfo.token.claim'),('c569ab91-32b5-457b-a559-e86738517797','true','access.token.claim'),('c569ab91-32b5-457b-a559-e86738517797','true','id.token.claim'),('c569ab91-32b5-457b-a559-e86738517797','country','user.attribute.country'),('c569ab91-32b5-457b-a559-e86738517797','formatted','user.attribute.formatted'),('c569ab91-32b5-457b-a559-e86738517797','locality','user.attribute.locality'),('c569ab91-32b5-457b-a559-e86738517797','postal_code','user.attribute.postal_code'),('c569ab91-32b5-457b-a559-e86738517797','region','user.attribute.region'),('c569ab91-32b5-457b-a559-e86738517797','street','user.attribute.street'),('c569ab91-32b5-457b-a559-e86738517797','true','userinfo.token.claim'),('c730e294-8c33-465b-a1a9-a42a0a1a79db','true','access.token.claim'),('c730e294-8c33-465b-a1a9-a42a0a1a79db','clientAddress','claim.name'),('c730e294-8c33-465b-a1a9-a42a0a1a79db','true','id.token.claim'),('c730e294-8c33-465b-a1a9-a42a0a1a79db','String','jsonType.label'),('c730e294-8c33-465b-a1a9-a42a0a1a79db','clientAddress','user.session.note'),('c730e294-8c33-465b-a1a9-a42a0a1a79db','true','userinfo.token.claim'),('c77be71a-b281-4532-8d89-f663caa47d8a','true','access.token.claim'),('c77be71a-b281-4532-8d89-f663caa47d8a','clientId','claim.name'),('c77be71a-b281-4532-8d89-f663caa47d8a','true','id.token.claim'),('c77be71a-b281-4532-8d89-f663caa47d8a','String','jsonType.label'),('c77be71a-b281-4532-8d89-f663caa47d8a','clientId','user.session.note'),('c77be71a-b281-4532-8d89-f663caa47d8a','true','userinfo.token.claim'),('cc947cdb-8e1e-4b67-a17f-079f7277f21c','true','access.token.claim'),('cc947cdb-8e1e-4b67-a17f-079f7277f21c','clientId','claim.name'),('cc947cdb-8e1e-4b67-a17f-079f7277f21c','true','id.token.claim'),('cc947cdb-8e1e-4b67-a17f-079f7277f21c','String','jsonType.label'),('cc947cdb-8e1e-4b67-a17f-079f7277f21c','clientId','user.session.note'),('cc947cdb-8e1e-4b67-a17f-079f7277f21c','true','userinfo.token.claim'),('cd68315c-0d20-4cfe-9a52-a7f019501b98','true','access.token.claim'),('cd68315c-0d20-4cfe-9a52-a7f019501b98','clientId','claim.name'),('cd68315c-0d20-4cfe-9a52-a7f019501b98','true','id.token.claim'),('cd68315c-0d20-4cfe-9a52-a7f019501b98','String','jsonType.label'),('cd68315c-0d20-4cfe-9a52-a7f019501b98','clientId','user.session.note'),('cd68315c-0d20-4cfe-9a52-a7f019501b98','true','userinfo.token.claim'),('ce5940df-d2b4-4d0e-a520-afcfb40016a1','true','access.token.claim'),('ce5940df-d2b4-4d0e-a520-afcfb40016a1','locale','claim.name'),('ce5940df-d2b4-4d0e-a520-afcfb40016a1','true','id.token.claim'),('ce5940df-d2b4-4d0e-a520-afcfb40016a1','String','jsonType.label'),('ce5940df-d2b4-4d0e-a520-afcfb40016a1','locale','user.attribute'),('ce5940df-d2b4-4d0e-a520-afcfb40016a1','true','userinfo.token.claim'),('d9535228-87c2-4872-aa16-2e21347044bd','true','access.token.claim'),('d9535228-87c2-4872-aa16-2e21347044bd','resource_access.${client_id}.roles','claim.name'),('d9535228-87c2-4872-aa16-2e21347044bd','String','jsonType.label'),('d9535228-87c2-4872-aa16-2e21347044bd','true','multivalued'),('d9535228-87c2-4872-aa16-2e21347044bd','foo','user.attribute'),('dad203c1-eacb-4d73-be8a-907eafca608b','true','access.token.claim'),('dad203c1-eacb-4d73-be8a-907eafca608b','nickname','claim.name'),('dad203c1-eacb-4d73-be8a-907eafca608b','true','id.token.claim'),('dad203c1-eacb-4d73-be8a-907eafca608b','String','jsonType.label'),('dad203c1-eacb-4d73-be8a-907eafca608b','nickname','user.attribute'),('dad203c1-eacb-4d73-be8a-907eafca608b','true','userinfo.token.claim'),('e00d3801-9894-4862-8eb0-a567adb18cf5','true','access.token.claim'),('e00d3801-9894-4862-8eb0-a567adb18cf5','updated_at','claim.name'),('e00d3801-9894-4862-8eb0-a567adb18cf5','true','id.token.claim'),('e00d3801-9894-4862-8eb0-a567adb18cf5','String','jsonType.label'),('e00d3801-9894-4862-8eb0-a567adb18cf5','updatedAt','user.attribute'),('e00d3801-9894-4862-8eb0-a567adb18cf5','true','userinfo.token.claim'),('e1916664-741a-45bd-ad97-5cbe0422ce0d','true','access.token.claim'),('e1916664-741a-45bd-ad97-5cbe0422ce0d','clientHost','claim.name'),('e1916664-741a-45bd-ad97-5cbe0422ce0d','true','id.token.claim'),('e1916664-741a-45bd-ad97-5cbe0422ce0d','String','jsonType.label'),('e1916664-741a-45bd-ad97-5cbe0422ce0d','clientHost','user.session.note'),('e1916664-741a-45bd-ad97-5cbe0422ce0d','true','userinfo.token.claim'),('e38548f4-d688-4266-bf51-a38d58adbab6','true','access.token.claim'),('e38548f4-d688-4266-bf51-a38d58adbab6','middle_name','claim.name'),('e38548f4-d688-4266-bf51-a38d58adbab6','true','id.token.claim'),('e38548f4-d688-4266-bf51-a38d58adbab6','String','jsonType.label'),('e38548f4-d688-4266-bf51-a38d58adbab6','middleName','user.attribute'),('e38548f4-d688-4266-bf51-a38d58adbab6','true','userinfo.token.claim'),('e39f0f4d-ff37-417e-8ab6-aaa4bfd1df11','true','access.token.claim'),('e39f0f4d-ff37-417e-8ab6-aaa4bfd1df11','locale','claim.name'),('e39f0f4d-ff37-417e-8ab6-aaa4bfd1df11','true','id.token.claim'),('e39f0f4d-ff37-417e-8ab6-aaa4bfd1df11','String','jsonType.label'),('e39f0f4d-ff37-417e-8ab6-aaa4bfd1df11','locale','user.attribute'),('e39f0f4d-ff37-417e-8ab6-aaa4bfd1df11','true','userinfo.token.claim'),('eaf0c207-03e4-4f06-8814-2a4e0f054bcf','true','access.token.claim'),('eaf0c207-03e4-4f06-8814-2a4e0f054bcf','clientAddress','claim.name'),('eaf0c207-03e4-4f06-8814-2a4e0f054bcf','true','id.token.claim'),('eaf0c207-03e4-4f06-8814-2a4e0f054bcf','String','jsonType.label'),('eaf0c207-03e4-4f06-8814-2a4e0f054bcf','clientAddress','user.session.note'),('eaf0c207-03e4-4f06-8814-2a4e0f054bcf','true','userinfo.token.claim'),('eb79ff4c-d4d3-4327-b15c-a26f01eb7dfc','true','access.token.claim'),('eb79ff4c-d4d3-4327-b15c-a26f01eb7dfc','zoneinfo','claim.name'),('eb79ff4c-d4d3-4327-b15c-a26f01eb7dfc','true','id.token.claim'),('eb79ff4c-d4d3-4327-b15c-a26f01eb7dfc','String','jsonType.label'),('eb79ff4c-d4d3-4327-b15c-a26f01eb7dfc','zoneinfo','user.attribute'),('eb79ff4c-d4d3-4327-b15c-a26f01eb7dfc','true','userinfo.token.claim'),('fa6d1f7c-908a-4ed8-a0e4-390ea01dadeb','true','access.token.claim'),('fa6d1f7c-908a-4ed8-a0e4-390ea01dadeb','website','claim.name'),('fa6d1f7c-908a-4ed8-a0e4-390ea01dadeb','true','id.token.claim'),('fa6d1f7c-908a-4ed8-a0e4-390ea01dadeb','String','jsonType.label'),('fa6d1f7c-908a-4ed8-a0e4-390ea01dadeb','website','user.attribute'),('fa6d1f7c-908a-4ed8-a0e4-390ea01dadeb','true','userinfo.token.claim');
/*!40000 ALTER TABLE `PROTOCOL_MAPPER_CONFIG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `REALM`
--

DROP TABLE IF EXISTS `REALM`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `REALM` (
  `ID` varchar(36) NOT NULL,
  `ACCESS_CODE_LIFESPAN` int(11) DEFAULT NULL,
  `USER_ACTION_LIFESPAN` int(11) DEFAULT NULL,
  `ACCESS_TOKEN_LIFESPAN` int(11) DEFAULT NULL,
  `ACCOUNT_THEME` varchar(255) DEFAULT NULL,
  `ADMIN_THEME` varchar(255) DEFAULT NULL,
  `EMAIL_THEME` varchar(255) DEFAULT NULL,
  `ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `EVENTS_ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `EVENTS_EXPIRATION` bigint(20) DEFAULT NULL,
  `LOGIN_THEME` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `NOT_BEFORE` int(11) DEFAULT NULL,
  `PASSWORD_POLICY` varchar(2550) DEFAULT NULL,
  `REGISTRATION_ALLOWED` bit(1) NOT NULL DEFAULT b'0',
  `REMEMBER_ME` bit(1) NOT NULL DEFAULT b'0',
  `RESET_PASSWORD_ALLOWED` bit(1) NOT NULL DEFAULT b'0',
  `SOCIAL` bit(1) NOT NULL DEFAULT b'0',
  `SSL_REQUIRED` varchar(255) DEFAULT NULL,
  `SSO_IDLE_TIMEOUT` int(11) DEFAULT NULL,
  `SSO_MAX_LIFESPAN` int(11) DEFAULT NULL,
  `UPDATE_PROFILE_ON_SOC_LOGIN` bit(1) NOT NULL DEFAULT b'0',
  `VERIFY_EMAIL` bit(1) NOT NULL DEFAULT b'0',
  `MASTER_ADMIN_CLIENT` varchar(36) DEFAULT NULL,
  `LOGIN_LIFESPAN` int(11) DEFAULT NULL,
  `INTERNATIONALIZATION_ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `DEFAULT_LOCALE` varchar(255) DEFAULT NULL,
  `REG_EMAIL_AS_USERNAME` bit(1) NOT NULL DEFAULT b'0',
  `ADMIN_EVENTS_ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `ADMIN_EVENTS_DETAILS_ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `EDIT_USERNAME_ALLOWED` bit(1) NOT NULL DEFAULT b'0',
  `OTP_POLICY_COUNTER` int(11) DEFAULT '0',
  `OTP_POLICY_WINDOW` int(11) DEFAULT '1',
  `OTP_POLICY_PERIOD` int(11) DEFAULT '30',
  `OTP_POLICY_DIGITS` int(11) DEFAULT '6',
  `OTP_POLICY_ALG` varchar(36) DEFAULT 'HmacSHA1',
  `OTP_POLICY_TYPE` varchar(36) DEFAULT 'totp',
  `BROWSER_FLOW` varchar(36) DEFAULT NULL,
  `REGISTRATION_FLOW` varchar(36) DEFAULT NULL,
  `DIRECT_GRANT_FLOW` varchar(36) DEFAULT NULL,
  `RESET_CREDENTIALS_FLOW` varchar(36) DEFAULT NULL,
  `CLIENT_AUTH_FLOW` varchar(36) DEFAULT NULL,
  `OFFLINE_SESSION_IDLE_TIMEOUT` int(11) DEFAULT '0',
  `REVOKE_REFRESH_TOKEN` bit(1) NOT NULL DEFAULT b'0',
  `ACCESS_TOKEN_LIFE_IMPLICIT` int(11) DEFAULT '0',
  `LOGIN_WITH_EMAIL_ALLOWED` bit(1) NOT NULL DEFAULT b'1',
  `DUPLICATE_EMAILS_ALLOWED` bit(1) NOT NULL DEFAULT b'0',
  `DOCKER_AUTH_FLOW` varchar(36) DEFAULT NULL,
  `REFRESH_TOKEN_MAX_REUSE` int(11) DEFAULT '0',
  `ALLOW_USER_MANAGED_ACCESS` bit(1) NOT NULL DEFAULT b'0',
  `SSO_MAX_LIFESPAN_REMEMBER_ME` int(11) NOT NULL,
  `SSO_IDLE_TIMEOUT_REMEMBER_ME` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_ORVSDMLA56612EAEFIQ6WL5OI` (`NAME`),
  KEY `IDX_REALM_MASTER_ADM_CLI` (`MASTER_ADMIN_CLIENT`),
  CONSTRAINT `FK_TRAF444KK6QRKMS7N56AIWQ5Y` FOREIGN KEY (`MASTER_ADMIN_CLIENT`) REFERENCES `CLIENT` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REALM`
--

LOCK TABLES `REALM` WRITE;
/*!40000 ALTER TABLE `REALM` DISABLE KEYS */;
INSERT INTO `REALM` VALUES ('liferay-portal',300,300,300,'deltares-keycloak-theme','deltares-keycloak-theme','deltares-keycloak-theme','','',432000,'deltares-keycloak-theme','liferay-portal',1562574826,NULL,'','\0','','\0','EXTERNAL',60,36000,'\0','','3453a904-cef0-4361-9226-54d6fa84af18',1800,'','en','\0','','','',0,1,30,6,'HmacSHA1','totp','b560f942-5754-4d3a-ab48-127a624faa0d','6d517d95-18b4-429d-92bb-d506528d5d60','1f10b2ed-9d4e-4547-85ed-e7809b0bbe0f','77ffbc63-aaaf-4d0d-8108-8dd6e890ca7b','3c55c28f-96d8-46d3-84b9-a5277265e976',2592000,'\0',900,'','\0','f29a850a-300b-4420-a2c6-25badaddee1d',0,'\0',0,0),('master',60,300,60,NULL,'deltares-keycloak-theme',NULL,'','\0',0,NULL,'master',0,NULL,'\0','\0','\0','\0','EXTERNAL',1800,36000,'\0','\0','a6cac270-1663-4123-8db5-54ccaaee5871',1800,'\0',NULL,'\0','\0','\0','\0',0,1,30,6,'HmacSHA1','totp','7c50a617-e202-46ec-b3aa-6ae1bf549129','3dc56f89-e689-4131-860e-566332cf39bb','75250d37-7175-4d91-a36b-82a751d2fdbe','ca05262f-631f-4ddf-8c8c-03f11a4c6508','d45fb920-2a72-4874-95d6-204465be333b',2592000,'\0',900,'','\0','7c9bcd0a-09e2-4981-95c0-1ea5099c6e30',0,'\0',0,0);
/*!40000 ALTER TABLE `REALM` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `REALM_ATTRIBUTE`
--

DROP TABLE IF EXISTS `REALM_ATTRIBUTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `REALM_ATTRIBUTE` (
  `NAME` varchar(255) NOT NULL,
  `VALUE` varchar(255) DEFAULT NULL,
  `REALM_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`NAME`,`REALM_ID`),
  KEY `IDX_REALM_ATTR_REALM` (`REALM_ID`),
  CONSTRAINT `FK_8SHXD6L3E9ATQUKACXGPFFPTW` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REALM_ATTRIBUTE`
--

LOCK TABLES `REALM_ATTRIBUTE` WRITE;
/*!40000 ALTER TABLE `REALM_ATTRIBUTE` DISABLE KEYS */;
INSERT INTO `REALM_ATTRIBUTE` VALUES ('actionTokenGeneratedByAdminLifespan','43200','liferay-portal'),('actionTokenGeneratedByAdminLifespan','43200','master'),('actionTokenGeneratedByUserLifespan','300','liferay-portal'),('actionTokenGeneratedByUserLifespan','300','master'),('actionTokenGeneratedByUserLifespan.reset-credentials','1800','liferay-portal'),('actionTokenGeneratedByUserLifespan.verify-email','1800','liferay-portal'),('bruteForceProtected','false','liferay-portal'),('bruteForceProtected','false','master'),('displayName','MyDeltares','liferay-portal'),('displayName','Keycloak','master'),('displayNameHtml','<div class=\"kc-logo-text\"><span>Keycloak</span></div>','master'),('failureFactor','30','liferay-portal'),('failureFactor','30','master'),('maxDeltaTimeSeconds','43200','liferay-portal'),('maxDeltaTimeSeconds','43200','master'),('maxFailureWaitSeconds','900','liferay-portal'),('maxFailureWaitSeconds','900','master'),('minimumQuickLoginWaitSeconds','60','liferay-portal'),('minimumQuickLoginWaitSeconds','60','master'),('offlineSessionMaxLifespan','5184000','liferay-portal'),('offlineSessionMaxLifespan','5184000','master'),('offlineSessionMaxLifespanEnabled','false','liferay-portal'),('offlineSessionMaxLifespanEnabled','false','master'),('permanentLockout','false','liferay-portal'),('permanentLockout','false','master'),('quickLoginCheckMilliSeconds','1000','liferay-portal'),('quickLoginCheckMilliSeconds','1000','master'),('waitIncrementSeconds','60','liferay-portal'),('waitIncrementSeconds','60','master'),('_browser_header.contentSecurityPolicy','frame-src \'self\'; frame-ancestors \'self\'; object-src \'none\';','liferay-portal'),('_browser_header.contentSecurityPolicy','frame-src \'self\'; frame-ancestors \'self\'; object-src \'none\';','master'),('_browser_header.contentSecurityPolicyReportOnly','','liferay-portal'),('_browser_header.contentSecurityPolicyReportOnly','','master'),('_browser_header.strictTransportSecurity','max-age=31536000; includeSubDomains','liferay-portal'),('_browser_header.strictTransportSecurity','max-age=31536000; includeSubDomains','master'),('_browser_header.xContentTypeOptions','nosniff','liferay-portal'),('_browser_header.xContentTypeOptions','nosniff','master'),('_browser_header.xFrameOptions','SAMEORIGIN','liferay-portal'),('_browser_header.xFrameOptions','SAMEORIGIN','master'),('_browser_header.xRobotsTag','none','liferay-portal'),('_browser_header.xRobotsTag','none','master'),('_browser_header.xXSSProtection','1; mode=block','liferay-portal'),('_browser_header.xXSSProtection','1; mode=block','master');
/*!40000 ALTER TABLE `REALM_ATTRIBUTE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `REALM_DEFAULT_GROUPS`
--

DROP TABLE IF EXISTS `REALM_DEFAULT_GROUPS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `REALM_DEFAULT_GROUPS` (
  `REALM_ID` varchar(36) NOT NULL,
  `GROUP_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`REALM_ID`,`GROUP_ID`),
  UNIQUE KEY `CON_GROUP_ID_DEF_GROUPS` (`GROUP_ID`),
  KEY `IDX_REALM_DEF_GRP_REALM` (`REALM_ID`),
  CONSTRAINT `FK_DEF_GROUPS_GROUP` FOREIGN KEY (`GROUP_ID`) REFERENCES `KEYCLOAK_GROUP` (`ID`),
  CONSTRAINT `FK_DEF_GROUPS_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REALM_DEFAULT_GROUPS`
--

LOCK TABLES `REALM_DEFAULT_GROUPS` WRITE;
/*!40000 ALTER TABLE `REALM_DEFAULT_GROUPS` DISABLE KEYS */;
/*!40000 ALTER TABLE `REALM_DEFAULT_GROUPS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `REALM_DEFAULT_ROLES`
--

DROP TABLE IF EXISTS `REALM_DEFAULT_ROLES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `REALM_DEFAULT_ROLES` (
  `REALM_ID` varchar(36) NOT NULL,
  `ROLE_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`REALM_ID`,`ROLE_ID`),
  UNIQUE KEY `UK_H4WPD7W4HSOOLNI3H0SW7BTJE` (`ROLE_ID`),
  KEY `IDX_REALM_DEF_ROLES_REALM` (`REALM_ID`),
  CONSTRAINT `FK_EVUDB1PPW84OXFAX2DRS03ICC` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`),
  CONSTRAINT `FK_H4WPD7W4HSOOLNI3H0SW7BTJE` FOREIGN KEY (`ROLE_ID`) REFERENCES `KEYCLOAK_ROLE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REALM_DEFAULT_ROLES`
--

LOCK TABLES `REALM_DEFAULT_ROLES` WRITE;
/*!40000 ALTER TABLE `REALM_DEFAULT_ROLES` DISABLE KEYS */;
INSERT INTO `REALM_DEFAULT_ROLES` VALUES ('liferay-portal','beda296e-ad85-43a6-9d77-d37193147efa'),('liferay-portal','f4e93900-0b7d-4fa3-b2ca-523715fff9d1'),('master','012f4c09-2656-4603-aac9-4a132a391e94'),('master','e5db3777-0d0b-4d47-9e72-5b32e740b339');
/*!40000 ALTER TABLE `REALM_DEFAULT_ROLES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `REALM_ENABLED_EVENT_TYPES`
--

DROP TABLE IF EXISTS `REALM_ENABLED_EVENT_TYPES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `REALM_ENABLED_EVENT_TYPES` (
  `REALM_ID` varchar(36) NOT NULL,
  `VALUE` varchar(255) NOT NULL,
  PRIMARY KEY (`REALM_ID`,`VALUE`),
  KEY `IDX_REALM_EVT_TYPES_REALM` (`REALM_ID`),
  CONSTRAINT `FK_H846O4H0W8EPX5NWEDRF5Y69J` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REALM_ENABLED_EVENT_TYPES`
--

LOCK TABLES `REALM_ENABLED_EVENT_TYPES` WRITE;
/*!40000 ALTER TABLE `REALM_ENABLED_EVENT_TYPES` DISABLE KEYS */;
INSERT INTO `REALM_ENABLED_EVENT_TYPES` VALUES ('liferay-portal','CLIENT_REGISTER'),('liferay-portal','CLIENT_REGISTER_ERROR'),('liferay-portal','IDENTITY_PROVIDER_FIRST_LOGIN'),('liferay-portal','REGISTER'),('liferay-portal','REGISTER_ERROR'),('liferay-portal','SEND_IDENTITY_PROVIDER_LINK_ERROR'),('liferay-portal','SEND_RESET_PASSWORD_ERROR'),('liferay-portal','SEND_VERIFY_EMAIL_ERROR');
/*!40000 ALTER TABLE `REALM_ENABLED_EVENT_TYPES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `REALM_EVENTS_LISTENERS`
--

DROP TABLE IF EXISTS `REALM_EVENTS_LISTENERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `REALM_EVENTS_LISTENERS` (
  `REALM_ID` varchar(36) NOT NULL,
  `VALUE` varchar(255) NOT NULL,
  PRIMARY KEY (`REALM_ID`,`VALUE`),
  KEY `IDX_REALM_EVT_LIST_REALM` (`REALM_ID`),
  CONSTRAINT `FK_H846O4H0W8EPX5NXEV9F5Y69J` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REALM_EVENTS_LISTENERS`
--

LOCK TABLES `REALM_EVENTS_LISTENERS` WRITE;
/*!40000 ALTER TABLE `REALM_EVENTS_LISTENERS` DISABLE KEYS */;
INSERT INTO `REALM_EVENTS_LISTENERS` VALUES ('liferay-portal','jboss-logging'),('master','jboss-logging');
/*!40000 ALTER TABLE `REALM_EVENTS_LISTENERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `REALM_REQUIRED_CREDENTIAL`
--

DROP TABLE IF EXISTS `REALM_REQUIRED_CREDENTIAL`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `REALM_REQUIRED_CREDENTIAL` (
  `TYPE` varchar(255) NOT NULL,
  `FORM_LABEL` varchar(255) DEFAULT NULL,
  `INPUT` bit(1) NOT NULL DEFAULT b'0',
  `SECRET` bit(1) NOT NULL DEFAULT b'0',
  `REALM_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`REALM_ID`,`TYPE`),
  CONSTRAINT `FK_5HG65LYBEVAVKQFKI3KPONH9V` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REALM_REQUIRED_CREDENTIAL`
--

LOCK TABLES `REALM_REQUIRED_CREDENTIAL` WRITE;
/*!40000 ALTER TABLE `REALM_REQUIRED_CREDENTIAL` DISABLE KEYS */;
INSERT INTO `REALM_REQUIRED_CREDENTIAL` VALUES ('password','password','','','liferay-portal'),('password','password','','','master');
/*!40000 ALTER TABLE `REALM_REQUIRED_CREDENTIAL` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `REALM_SMTP_CONFIG`
--

DROP TABLE IF EXISTS `REALM_SMTP_CONFIG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `REALM_SMTP_CONFIG` (
  `REALM_ID` varchar(36) NOT NULL,
  `VALUE` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) NOT NULL,
  PRIMARY KEY (`REALM_ID`,`NAME`),
  CONSTRAINT `FK_70EJ8XDXGXD0B9HH6180IRR0O` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REALM_SMTP_CONFIG`
--

LOCK TABLES `REALM_SMTP_CONFIG` WRITE;
/*!40000 ALTER TABLE `REALM_SMTP_CONFIG` DISABLE KEYS */;
INSERT INTO `REALM_SMTP_CONFIG` VALUES ('liferay-portal','true','auth'),('liferay-portal','mydeltares@deltares.nl','from'),('liferay-portal','MyDeltares Account Management','fromDisplayName'),('liferay-portal','smtp.office365.com','host'),('liferay-portal','qywplkxggxrtlhhz','password'),('liferay-portal','587','port'),('liferay-portal','false','ssl'),('liferay-portal','true','starttls'),('liferay-portal','mydeltares@deltares.nl','user');
/*!40000 ALTER TABLE `REALM_SMTP_CONFIG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `REALM_SUPPORTED_LOCALES`
--

DROP TABLE IF EXISTS `REALM_SUPPORTED_LOCALES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `REALM_SUPPORTED_LOCALES` (
  `REALM_ID` varchar(36) NOT NULL,
  `VALUE` varchar(255) NOT NULL,
  PRIMARY KEY (`REALM_ID`,`VALUE`),
  KEY `IDX_REALM_SUPP_LOCAL_REALM` (`REALM_ID`),
  CONSTRAINT `FK_SUPPORTED_LOCALES_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REALM_SUPPORTED_LOCALES`
--

LOCK TABLES `REALM_SUPPORTED_LOCALES` WRITE;
/*!40000 ALTER TABLE `REALM_SUPPORTED_LOCALES` DISABLE KEYS */;
INSERT INTO `REALM_SUPPORTED_LOCALES` VALUES ('liferay-portal','en'),('liferay-portal','nl');
/*!40000 ALTER TABLE `REALM_SUPPORTED_LOCALES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `REDIRECT_URIS`
--

DROP TABLE IF EXISTS `REDIRECT_URIS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `REDIRECT_URIS` (
  `CLIENT_ID` varchar(36) NOT NULL,
  `VALUE` varchar(255) NOT NULL,
  PRIMARY KEY (`CLIENT_ID`,`VALUE`),
  KEY `IDX_REDIR_URI_CLIENT` (`CLIENT_ID`),
  CONSTRAINT `FK_1BURS8PB4OUJ97H5WUPPAHV9F` FOREIGN KEY (`CLIENT_ID`) REFERENCES `CLIENT` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REDIRECT_URIS`
--

LOCK TABLES `REDIRECT_URIS` WRITE;
/*!40000 ALTER TABLE `REDIRECT_URIS` DISABLE KEYS */;
INSERT INTO `REDIRECT_URIS` VALUES ('27a25fc2-29bd-4afd-a135-a5b2b8be12b8','http://localhost:8080/*'),('364c3e71-9195-4162-9d08-e048421fdd0f','/auth/admin/liferay-portal/console/*'),('67d6d3e3-38a7-4562-aac7-03832d713532','http://localhost:8080/*'),('c5f79f09-95d5-4a96-a845-47bad8536eb7','/auth/realms/master/account/*'),('d6d25c43-45e4-4107-89b4-7fc91bca0f80','/auth/admin/master/console/*'),('f1b13f7c-a916-4b24-b314-0200496926ce','/auth/realms/liferay-portal/account/*');
/*!40000 ALTER TABLE `REDIRECT_URIS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `REQUIRED_ACTION_CONFIG`
--

DROP TABLE IF EXISTS `REQUIRED_ACTION_CONFIG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `REQUIRED_ACTION_CONFIG` (
  `REQUIRED_ACTION_ID` varchar(36) NOT NULL,
  `VALUE` longtext,
  `NAME` varchar(255) NOT NULL,
  PRIMARY KEY (`REQUIRED_ACTION_ID`,`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REQUIRED_ACTION_CONFIG`
--

LOCK TABLES `REQUIRED_ACTION_CONFIG` WRITE;
/*!40000 ALTER TABLE `REQUIRED_ACTION_CONFIG` DISABLE KEYS */;
/*!40000 ALTER TABLE `REQUIRED_ACTION_CONFIG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `REQUIRED_ACTION_PROVIDER`
--

DROP TABLE IF EXISTS `REQUIRED_ACTION_PROVIDER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `REQUIRED_ACTION_PROVIDER` (
  `ID` varchar(36) NOT NULL,
  `ALIAS` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) DEFAULT NULL,
  `REALM_ID` varchar(36) DEFAULT NULL,
  `ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `DEFAULT_ACTION` bit(1) NOT NULL DEFAULT b'0',
  `PROVIDER_ID` varchar(255) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_REQ_ACT_PROV_REALM` (`REALM_ID`),
  CONSTRAINT `FK_REQ_ACT_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REQUIRED_ACTION_PROVIDER`
--

LOCK TABLES `REQUIRED_ACTION_PROVIDER` WRITE;
/*!40000 ALTER TABLE `REQUIRED_ACTION_PROVIDER` DISABLE KEYS */;
INSERT INTO `REQUIRED_ACTION_PROVIDER` VALUES ('098f3f40-e783-439d-8a0b-def6a8d76522','UPDATE_PROFILE','Update Profile','liferay-portal','','\0','UPDATE_PROFILE',51),('2131c6d2-95c5-48ae-9644-3049b1e690d6','VERIFY_EMAIL','Verify Email','liferay-portal','\0','\0','VERIFY_EMAIL',40),('270ad276-214b-4d66-9307-1162e7377eba','terms_and_conditions','Terms and Conditions','master','\0','\0','terms_and_conditions',20),('35e7a2f6-c969-4c46-a83a-33dcc23668e4','CONFIGURE_TOTP','Configure OTP','liferay-portal','','\0','CONFIGURE_TOTP',10),('59ac968c-bb2b-4430-932c-e1b2c58d563f','login_stats_action','Record Login Statistics Action','liferay-portal','','\0','login_stats_action',52),('654e838b-9381-40c3-b996-11833556199a','CONFIGURE_TOTP','Configure OTP','master','','\0','CONFIGURE_TOTP',10),('8ee36c23-78c4-4fb0-95a3-0192b8c730c5','terms_and_conditions','Terms and Conditions','liferay-portal','\0','','terms_and_conditions',20),('ab722060-a75e-4b71-9040-d9eeb40130ad','UPDATE_PASSWORD','Update Password','master','','\0','UPDATE_PASSWORD',30),('be9c9e82-a39b-4f5e-8b2a-3fec76363337','VERIFY_EMAIL','Verify Email','master','','\0','VERIFY_EMAIL',50),('c054206b-1a12-437e-81a9-553b0a8ea8b2','UPDATE_PASSWORD','Update Password','liferay-portal','','\0','UPDATE_PASSWORD',50),('ed9af355-f1ea-4855-a7e6-0b6f02d960ae','UPDATE_PROFILE','Update Profile','master','','\0','UPDATE_PROFILE',40),('f302a95c-28c3-4126-bb76-5d7e843a39da','terms_and_privacy','Deltares Terms and Privacy','liferay-portal','','','terms_and_privacy',30);
/*!40000 ALTER TABLE `REQUIRED_ACTION_PROVIDER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RESOURCE_ATTRIBUTE`
--

DROP TABLE IF EXISTS `RESOURCE_ATTRIBUTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RESOURCE_ATTRIBUTE` (
  `ID` varchar(36) NOT NULL DEFAULT 'sybase-needs-something-here',
  `NAME` varchar(255) NOT NULL,
  `VALUE` varchar(255) DEFAULT NULL,
  `RESOURCE_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_5HRM2VLF9QL5FU022KQEPOVBR` (`RESOURCE_ID`),
  CONSTRAINT `FK_5HRM2VLF9QL5FU022KQEPOVBR` FOREIGN KEY (`RESOURCE_ID`) REFERENCES `RESOURCE_SERVER_RESOURCE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RESOURCE_ATTRIBUTE`
--

LOCK TABLES `RESOURCE_ATTRIBUTE` WRITE;
/*!40000 ALTER TABLE `RESOURCE_ATTRIBUTE` DISABLE KEYS */;
/*!40000 ALTER TABLE `RESOURCE_ATTRIBUTE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RESOURCE_POLICY`
--

DROP TABLE IF EXISTS `RESOURCE_POLICY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RESOURCE_POLICY` (
  `RESOURCE_ID` varchar(36) NOT NULL,
  `POLICY_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`RESOURCE_ID`,`POLICY_ID`),
  KEY `IDX_RES_POLICY_POLICY` (`POLICY_ID`),
  CONSTRAINT `FK_FRSRPOS53XCX4WNKOG82SSRFY` FOREIGN KEY (`RESOURCE_ID`) REFERENCES `RESOURCE_SERVER_RESOURCE` (`ID`),
  CONSTRAINT `FK_FRSRPP213XCX4WNKOG82SSRFY` FOREIGN KEY (`POLICY_ID`) REFERENCES `RESOURCE_SERVER_POLICY` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RESOURCE_POLICY`
--

LOCK TABLES `RESOURCE_POLICY` WRITE;
/*!40000 ALTER TABLE `RESOURCE_POLICY` DISABLE KEYS */;
/*!40000 ALTER TABLE `RESOURCE_POLICY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RESOURCE_SCOPE`
--

DROP TABLE IF EXISTS `RESOURCE_SCOPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RESOURCE_SCOPE` (
  `RESOURCE_ID` varchar(36) NOT NULL,
  `SCOPE_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`RESOURCE_ID`,`SCOPE_ID`),
  KEY `IDX_RES_SCOPE_SCOPE` (`SCOPE_ID`),
  CONSTRAINT `FK_FRSRPOS13XCX4WNKOG82SSRFY` FOREIGN KEY (`RESOURCE_ID`) REFERENCES `RESOURCE_SERVER_RESOURCE` (`ID`),
  CONSTRAINT `FK_FRSRPS213XCX4WNKOG82SSRFY` FOREIGN KEY (`SCOPE_ID`) REFERENCES `RESOURCE_SERVER_SCOPE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RESOURCE_SCOPE`
--

LOCK TABLES `RESOURCE_SCOPE` WRITE;
/*!40000 ALTER TABLE `RESOURCE_SCOPE` DISABLE KEYS */;
/*!40000 ALTER TABLE `RESOURCE_SCOPE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RESOURCE_SERVER`
--

DROP TABLE IF EXISTS `RESOURCE_SERVER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RESOURCE_SERVER` (
  `ID` varchar(36) NOT NULL,
  `ALLOW_RS_REMOTE_MGMT` bit(1) NOT NULL DEFAULT b'0',
  `POLICY_ENFORCE_MODE` varchar(15) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RESOURCE_SERVER`
--

LOCK TABLES `RESOURCE_SERVER` WRITE;
/*!40000 ALTER TABLE `RESOURCE_SERVER` DISABLE KEYS */;
INSERT INTO `RESOURCE_SERVER` VALUES ('07a7d9af-fe25-4479-8d74-9ddc0baeb35e','','0'),('27a25fc2-29bd-4afd-a135-a5b2b8be12b8','','0'),('67d6d3e3-38a7-4562-aac7-03832d713532','','0'),('7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6','','0');
/*!40000 ALTER TABLE `RESOURCE_SERVER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RESOURCE_SERVER_PERM_TICKET`
--

DROP TABLE IF EXISTS `RESOURCE_SERVER_PERM_TICKET`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RESOURCE_SERVER_PERM_TICKET` (
  `ID` varchar(36) NOT NULL,
  `OWNER` varchar(36) NOT NULL,
  `REQUESTER` varchar(36) NOT NULL,
  `CREATED_TIMESTAMP` bigint(20) NOT NULL,
  `GRANTED_TIMESTAMP` bigint(20) DEFAULT NULL,
  `RESOURCE_ID` varchar(36) NOT NULL,
  `SCOPE_ID` varchar(36) DEFAULT NULL,
  `RESOURCE_SERVER_ID` varchar(36) NOT NULL,
  `POLICY_ID` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_FRSR6T700S9V50BU18WS5PMT` (`OWNER`,`REQUESTER`,`RESOURCE_SERVER_ID`,`RESOURCE_ID`,`SCOPE_ID`),
  KEY `FK_FRSRHO213XCX4WNKOG82SSPMT` (`RESOURCE_SERVER_ID`),
  KEY `FK_FRSRHO213XCX4WNKOG83SSPMT` (`RESOURCE_ID`),
  KEY `FK_FRSRHO213XCX4WNKOG84SSPMT` (`SCOPE_ID`),
  KEY `FK_FRSRPO2128CX4WNKOG82SSRFY` (`POLICY_ID`),
  CONSTRAINT `FK_FRSRHO213XCX4WNKOG82SSPMT` FOREIGN KEY (`RESOURCE_SERVER_ID`) REFERENCES `RESOURCE_SERVER` (`ID`),
  CONSTRAINT `FK_FRSRHO213XCX4WNKOG83SSPMT` FOREIGN KEY (`RESOURCE_ID`) REFERENCES `RESOURCE_SERVER_RESOURCE` (`ID`),
  CONSTRAINT `FK_FRSRHO213XCX4WNKOG84SSPMT` FOREIGN KEY (`SCOPE_ID`) REFERENCES `RESOURCE_SERVER_SCOPE` (`ID`),
  CONSTRAINT `FK_FRSRPO2128CX4WNKOG82SSRFY` FOREIGN KEY (`POLICY_ID`) REFERENCES `RESOURCE_SERVER_POLICY` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RESOURCE_SERVER_PERM_TICKET`
--

LOCK TABLES `RESOURCE_SERVER_PERM_TICKET` WRITE;
/*!40000 ALTER TABLE `RESOURCE_SERVER_PERM_TICKET` DISABLE KEYS */;
/*!40000 ALTER TABLE `RESOURCE_SERVER_PERM_TICKET` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RESOURCE_SERVER_POLICY`
--

DROP TABLE IF EXISTS `RESOURCE_SERVER_POLICY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RESOURCE_SERVER_POLICY` (
  `ID` varchar(36) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `TYPE` varchar(255) NOT NULL,
  `DECISION_STRATEGY` varchar(20) DEFAULT NULL,
  `LOGIC` varchar(20) DEFAULT NULL,
  `RESOURCE_SERVER_ID` varchar(36) DEFAULT NULL,
  `OWNER` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_FRSRPT700S9V50BU18WS5HA6` (`NAME`,`RESOURCE_SERVER_ID`),
  KEY `IDX_RES_SERV_POL_RES_SERV` (`RESOURCE_SERVER_ID`),
  CONSTRAINT `FK_FRSRPO213XCX4WNKOG82SSRFY` FOREIGN KEY (`RESOURCE_SERVER_ID`) REFERENCES `RESOURCE_SERVER` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RESOURCE_SERVER_POLICY`
--

LOCK TABLES `RESOURCE_SERVER_POLICY` WRITE;
/*!40000 ALTER TABLE `RESOURCE_SERVER_POLICY` DISABLE KEYS */;
INSERT INTO `RESOURCE_SERVER_POLICY` VALUES ('1d1be5b2-368b-4f73-91ef-f5fff682aa73','Default Permission','A permission that applies to the default resource type','resource','1','0','67d6d3e3-38a7-4562-aac7-03832d713532',NULL),('464a4f94-3ebe-4b25-a773-43004e2e73d6','Default Permission','A permission that applies to the default resource type','resource','1','0','07a7d9af-fe25-4479-8d74-9ddc0baeb35e',NULL),('7e75f663-76ca-42cc-92ce-732e3caf409a','Default Policy','A policy that grants access only for users within this realm','js','0','0','27a25fc2-29bd-4afd-a135-a5b2b8be12b8',NULL),('8119b5a3-d21e-4c4b-bac8-134c495e0a77','Default Policy','A policy that grants access only for users within this realm','js','0','0','07a7d9af-fe25-4479-8d74-9ddc0baeb35e',NULL),('d66a85ba-62e8-49ed-b0cc-29ab865a86e5','Default Permission','A permission that applies to the default resource type','resource','1','0','27a25fc2-29bd-4afd-a135-a5b2b8be12b8',NULL),('eca0e811-43db-41f8-a3b9-5d37f235e1b3','Default Policy','A policy that grants access only for users within this realm','js','0','0','67d6d3e3-38a7-4562-aac7-03832d713532',NULL);
/*!40000 ALTER TABLE `RESOURCE_SERVER_POLICY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RESOURCE_SERVER_RESOURCE`
--

DROP TABLE IF EXISTS `RESOURCE_SERVER_RESOURCE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RESOURCE_SERVER_RESOURCE` (
  `ID` varchar(36) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `TYPE` varchar(255) DEFAULT NULL,
  `ICON_URI` varchar(255) DEFAULT NULL,
  `OWNER` varchar(36) NOT NULL,
  `RESOURCE_SERVER_ID` varchar(36) DEFAULT NULL,
  `OWNER_MANAGED_ACCESS` bit(1) NOT NULL DEFAULT b'0',
  `DISPLAY_NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_FRSR6T700S9V50BU18WS5HA6` (`NAME`,`OWNER`,`RESOURCE_SERVER_ID`),
  KEY `IDX_RES_SRV_RES_RES_SRV` (`RESOURCE_SERVER_ID`),
  CONSTRAINT `FK_FRSRHO213XCX4WNKOG82SSRFY` FOREIGN KEY (`RESOURCE_SERVER_ID`) REFERENCES `RESOURCE_SERVER` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RESOURCE_SERVER_RESOURCE`
--

LOCK TABLES `RESOURCE_SERVER_RESOURCE` WRITE;
/*!40000 ALTER TABLE `RESOURCE_SERVER_RESOURCE` DISABLE KEYS */;
INSERT INTO `RESOURCE_SERVER_RESOURCE` VALUES ('6911acb5-9cf5-4d38-a808-8e79701c2371','Default Resource','urn:api-user:resources:default',NULL,'67d6d3e3-38a7-4562-aac7-03832d713532','67d6d3e3-38a7-4562-aac7-03832d713532','\0',NULL),('6d6d1cf1-4d22-491d-8357-75cee19b88f5','Default Resource','urn:api-viewer:resources:default',NULL,'27a25fc2-29bd-4afd-a135-a5b2b8be12b8','27a25fc2-29bd-4afd-a135-a5b2b8be12b8','\0',NULL),('d2571a2f-7378-40ee-888c-6b8238ba6f6e','Default Resource','urn:api-admin:resources:default',NULL,'07a7d9af-fe25-4479-8d74-9ddc0baeb35e','07a7d9af-fe25-4479-8d74-9ddc0baeb35e','\0',NULL);
/*!40000 ALTER TABLE `RESOURCE_SERVER_RESOURCE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RESOURCE_SERVER_SCOPE`
--

DROP TABLE IF EXISTS `RESOURCE_SERVER_SCOPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RESOURCE_SERVER_SCOPE` (
  `ID` varchar(36) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `ICON_URI` varchar(255) DEFAULT NULL,
  `RESOURCE_SERVER_ID` varchar(36) DEFAULT NULL,
  `DISPLAY_NAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_FRSRST700S9V50BU18WS5HA6` (`NAME`,`RESOURCE_SERVER_ID`),
  KEY `IDX_RES_SRV_SCOPE_RES_SRV` (`RESOURCE_SERVER_ID`),
  CONSTRAINT `FK_FRSRSO213XCX4WNKOG82SSRFY` FOREIGN KEY (`RESOURCE_SERVER_ID`) REFERENCES `RESOURCE_SERVER` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RESOURCE_SERVER_SCOPE`
--

LOCK TABLES `RESOURCE_SERVER_SCOPE` WRITE;
/*!40000 ALTER TABLE `RESOURCE_SERVER_SCOPE` DISABLE KEYS */;
INSERT INTO `RESOURCE_SERVER_SCOPE` VALUES ('112db9aa-16ff-49d1-be05-5edfd0cd4b97','map-role-composite',NULL,'7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6',NULL),('1fd3b2dd-6b0a-4c10-b2c6-5ef05598fc86','map-role',NULL,'7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6',NULL),('742e7c8a-0523-4e24-a018-171f7171fd4f','map-role-client-scope',NULL,'7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6',NULL),('c8874f85-9eb3-4aac-98f3-dd17428a1611','token-exchange',NULL,'7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6',NULL);
/*!40000 ALTER TABLE `RESOURCE_SERVER_SCOPE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RESOURCE_URIS`
--

DROP TABLE IF EXISTS `RESOURCE_URIS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `RESOURCE_URIS` (
  `RESOURCE_ID` varchar(36) NOT NULL,
  `VALUE` varchar(255) NOT NULL,
  KEY `FK_RESOURCE_SERVER_URIS` (`RESOURCE_ID`),
  CONSTRAINT `FK_RESOURCE_SERVER_URIS` FOREIGN KEY (`RESOURCE_ID`) REFERENCES `RESOURCE_SERVER_RESOURCE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RESOURCE_URIS`
--

LOCK TABLES `RESOURCE_URIS` WRITE;
/*!40000 ALTER TABLE `RESOURCE_URIS` DISABLE KEYS */;
INSERT INTO `RESOURCE_URIS` VALUES ('d2571a2f-7378-40ee-888c-6b8238ba6f6e','/*'),('6d6d1cf1-4d22-491d-8357-75cee19b88f5','/*'),('6911acb5-9cf5-4d38-a808-8e79701c2371','/*');
/*!40000 ALTER TABLE `RESOURCE_URIS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ROLE_ATTRIBUTE`
--

DROP TABLE IF EXISTS `ROLE_ATTRIBUTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ROLE_ATTRIBUTE` (
  `ID` varchar(36) NOT NULL,
  `ROLE_ID` varchar(36) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `VALUE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_ROLE_ATTRIBUTE` (`ROLE_ID`),
  CONSTRAINT `FK_ROLE_ATTRIBUTE_ID` FOREIGN KEY (`ROLE_ID`) REFERENCES `KEYCLOAK_ROLE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ROLE_ATTRIBUTE`
--

LOCK TABLES `ROLE_ATTRIBUTE` WRITE;
/*!40000 ALTER TABLE `ROLE_ATTRIBUTE` DISABLE KEYS */;
/*!40000 ALTER TABLE `ROLE_ATTRIBUTE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SCOPE_MAPPING`
--

DROP TABLE IF EXISTS `SCOPE_MAPPING`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SCOPE_MAPPING` (
  `CLIENT_ID` varchar(36) NOT NULL,
  `ROLE_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`CLIENT_ID`,`ROLE_ID`),
  KEY `IDX_SCOPE_MAPPING_ROLE` (`ROLE_ID`),
  CONSTRAINT `FK_OUSE064PLMLR732LXJCN1Q5F1` FOREIGN KEY (`CLIENT_ID`) REFERENCES `CLIENT` (`ID`),
  CONSTRAINT `FK_P3RH9GRKU11KQFRS4FLTT7RNQ` FOREIGN KEY (`ROLE_ID`) REFERENCES `KEYCLOAK_ROLE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SCOPE_MAPPING`
--

LOCK TABLES `SCOPE_MAPPING` WRITE;
/*!40000 ALTER TABLE `SCOPE_MAPPING` DISABLE KEYS */;
/*!40000 ALTER TABLE `SCOPE_MAPPING` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SCOPE_POLICY`
--

DROP TABLE IF EXISTS `SCOPE_POLICY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SCOPE_POLICY` (
  `SCOPE_ID` varchar(36) NOT NULL,
  `POLICY_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`SCOPE_ID`,`POLICY_ID`),
  KEY `IDX_SCOPE_POLICY_POLICY` (`POLICY_ID`),
  CONSTRAINT `FK_FRSRASP13XCX4WNKOG82SSRFY` FOREIGN KEY (`POLICY_ID`) REFERENCES `RESOURCE_SERVER_POLICY` (`ID`),
  CONSTRAINT `FK_FRSRPASS3XCX4WNKOG82SSRFY` FOREIGN KEY (`SCOPE_ID`) REFERENCES `RESOURCE_SERVER_SCOPE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SCOPE_POLICY`
--

LOCK TABLES `SCOPE_POLICY` WRITE;
/*!40000 ALTER TABLE `SCOPE_POLICY` DISABLE KEYS */;
/*!40000 ALTER TABLE `SCOPE_POLICY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USERNAME_LOGIN_FAILURE`
--

DROP TABLE IF EXISTS `USERNAME_LOGIN_FAILURE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USERNAME_LOGIN_FAILURE` (
  `REALM_ID` varchar(36) NOT NULL,
  `USERNAME` varchar(255) NOT NULL,
  `FAILED_LOGIN_NOT_BEFORE` int(11) DEFAULT NULL,
  `LAST_FAILURE` bigint(20) DEFAULT NULL,
  `LAST_IP_FAILURE` varchar(255) DEFAULT NULL,
  `NUM_FAILURES` int(11) DEFAULT NULL,
  PRIMARY KEY (`REALM_ID`,`USERNAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USERNAME_LOGIN_FAILURE`
--

LOCK TABLES `USERNAME_LOGIN_FAILURE` WRITE;
/*!40000 ALTER TABLE `USERNAME_LOGIN_FAILURE` DISABLE KEYS */;
/*!40000 ALTER TABLE `USERNAME_LOGIN_FAILURE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_ATTRIBUTE`
--

DROP TABLE IF EXISTS `USER_ATTRIBUTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USER_ATTRIBUTE` (
  `NAME` varchar(255) NOT NULL,
  `VALUE` varchar(255) DEFAULT NULL,
  `USER_ID` varchar(36) NOT NULL,
  `ID` varchar(36) NOT NULL DEFAULT 'sybase-needs-something-here',
  PRIMARY KEY (`ID`),
  KEY `IDX_USER_ATTRIBUTE` (`USER_ID`),
  CONSTRAINT `FK_5HRM2VLF9QL5FU043KQEPOVBR` FOREIGN KEY (`USER_ID`) REFERENCES `USER_ENTITY` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_ATTRIBUTE`
--

LOCK TABLES `USER_ATTRIBUTE` WRITE;
/*!40000 ALTER TABLE `USER_ATTRIBUTE` DISABLE KEYS */;
/*!40000 ALTER TABLE `USER_ATTRIBUTE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_AVATAR`
--

DROP TABLE IF EXISTS `USER_AVATAR`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USER_AVATAR` (
  `ID` varchar(36) NOT NULL,
  `REALM_ID` varchar(36) NOT NULL,
  `USER_ID` varchar(36) NOT NULL,
  `AVATAR` blob NOT NULL,
  `CONTENT_TYPE` varchar(12) NOT NULL DEFAULT 'image/jpeg',
  PRIMARY KEY (`ID`),
  KEY `FK_USER_AVATAR_USER_ENTITY` (`USER_ID`),
  KEY `FK_USER_AVATAR_REALM_ENTITY` (`REALM_ID`),
  CONSTRAINT `FK_USER_AVATAR_REALM_ENTITY` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`) ON DELETE CASCADE,
  CONSTRAINT `FK_USER_AVATAR_USER_ENTITY` FOREIGN KEY (`USER_ID`) REFERENCES `USER_ENTITY` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_AVATAR`
--

LOCK TABLES `USER_AVATAR` WRITE;
/*!40000 ALTER TABLE `USER_AVATAR` DISABLE KEYS */;
/*!40000 ALTER TABLE `USER_AVATAR` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_CONSENT`
--

DROP TABLE IF EXISTS `USER_CONSENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USER_CONSENT` (
  `ID` varchar(36) NOT NULL,
  `CLIENT_ID` varchar(36) DEFAULT NULL,
  `USER_ID` varchar(36) NOT NULL,
  `CREATED_DATE` bigint(20) DEFAULT NULL,
  `LAST_UPDATED_DATE` bigint(20) DEFAULT NULL,
  `CLIENT_STORAGE_PROVIDER` varchar(36) DEFAULT NULL,
  `EXTERNAL_CLIENT_ID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_JKUWUVD56ONTGSUHOGM8UEWRT` (`CLIENT_ID`,`CLIENT_STORAGE_PROVIDER`,`EXTERNAL_CLIENT_ID`,`USER_ID`),
  KEY `IDX_USER_CONSENT` (`USER_ID`),
  CONSTRAINT `FK_GRNTCSNT_USER` FOREIGN KEY (`USER_ID`) REFERENCES `USER_ENTITY` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_CONSENT`
--

LOCK TABLES `USER_CONSENT` WRITE;
/*!40000 ALTER TABLE `USER_CONSENT` DISABLE KEYS */;
/*!40000 ALTER TABLE `USER_CONSENT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_CONSENT_CLIENT_SCOPE`
--

DROP TABLE IF EXISTS `USER_CONSENT_CLIENT_SCOPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USER_CONSENT_CLIENT_SCOPE` (
  `USER_CONSENT_ID` varchar(36) NOT NULL,
  `SCOPE_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`USER_CONSENT_ID`,`SCOPE_ID`),
  KEY `IDX_USCONSENT_CLSCOPE` (`USER_CONSENT_ID`),
  CONSTRAINT `FK_GRNTCSNT_CLSC_USC` FOREIGN KEY (`USER_CONSENT_ID`) REFERENCES `USER_CONSENT` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_CONSENT_CLIENT_SCOPE`
--

LOCK TABLES `USER_CONSENT_CLIENT_SCOPE` WRITE;
/*!40000 ALTER TABLE `USER_CONSENT_CLIENT_SCOPE` DISABLE KEYS */;
/*!40000 ALTER TABLE `USER_CONSENT_CLIENT_SCOPE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_ENTITY`
--

DROP TABLE IF EXISTS `USER_ENTITY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USER_ENTITY` (
  `ID` varchar(36) NOT NULL,
  `EMAIL` varchar(255) DEFAULT NULL,
  `EMAIL_CONSTRAINT` varchar(255) DEFAULT NULL,
  `EMAIL_VERIFIED` bit(1) NOT NULL DEFAULT b'0',
  `ENABLED` bit(1) NOT NULL DEFAULT b'0',
  `FEDERATION_LINK` varchar(255) DEFAULT NULL,
  `FIRST_NAME` varchar(255) DEFAULT NULL,
  `LAST_NAME` varchar(255) DEFAULT NULL,
  `REALM_ID` varchar(255) DEFAULT NULL,
  `USERNAME` varchar(255) DEFAULT NULL,
  `CREATED_TIMESTAMP` bigint(20) DEFAULT NULL,
  `SERVICE_ACCOUNT_CLIENT_LINK` varchar(36) DEFAULT NULL,
  `NOT_BEFORE` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `UK_DYKN684SL8UP1CRFEI6ECKHD7` (`REALM_ID`,`EMAIL_CONSTRAINT`),
  UNIQUE KEY `UK_RU8TT6T700S9V50BU18WS5HA6` (`REALM_ID`,`USERNAME`),
  KEY `IDX_USER_EMAIL` (`EMAIL`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_ENTITY`
--

LOCK TABLES `USER_ENTITY` WRITE;
/*!40000 ALTER TABLE `USER_ENTITY` DISABLE KEYS */;
INSERT INTO `USER_ENTITY` VALUES ('4af62885-2585-4fda-bd22-6f6171c7a285','service-account-user-api@placeholder.org','service-account-user-api@placeholder.org','\0','',NULL,NULL,NULL,'liferay-portal','service-account-user-api',1619088325658,'67d6d3e3-38a7-4562-aac7-03832d713532',0),('5544a352-9524-44ad-8617-6ed1e77dcb8e','mydeltares@deltares.nl','mydeltares@deltares.nl','\0','',NULL,NULL,NULL,'master','admin',1619088325841,NULL,0),('5e270d90-f0ff-4991-8b9c-3f0226b136d3','service-account-admin-api-viewer@placeholder.org','service-account-admin-api-viewer@placeholder.org','\0','',NULL,NULL,NULL,'liferay-portal','service-account-admin-api-viewer',1619088325550,'27a25fc2-29bd-4afd-a135-a5b2b8be12b8',0),('bb0d7915-fe6e-4c03-9842-5ee8004d728c','service-account-admin-api-admin@placeholder.org','service-account-admin-api-admin@placeholder.org','\0','',NULL,NULL,NULL,'liferay-portal','service-account-admin-api-admin',1619088325459,'07a7d9af-fe25-4479-8d74-9ddc0baeb35e',0),('cbd69188-754f-41a3-89bd-7a07e48d5fd1','service-account-realm-management@placeholder.org','service-account-realm-management@placeholder.org','\0','',NULL,NULL,NULL,'liferay-portal','service-account-realm-management',1619088325611,'7b08eb13-2fdb-4b3e-8ff6-9c09047be2e6',0);
/*!40000 ALTER TABLE `USER_ENTITY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_FEDERATION_CONFIG`
--

DROP TABLE IF EXISTS `USER_FEDERATION_CONFIG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USER_FEDERATION_CONFIG` (
  `USER_FEDERATION_PROVIDER_ID` varchar(36) NOT NULL,
  `VALUE` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) NOT NULL,
  PRIMARY KEY (`USER_FEDERATION_PROVIDER_ID`,`NAME`),
  CONSTRAINT `FK_T13HPU1J94R2EBPEKR39X5EU5` FOREIGN KEY (`USER_FEDERATION_PROVIDER_ID`) REFERENCES `USER_FEDERATION_PROVIDER` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_FEDERATION_CONFIG`
--

LOCK TABLES `USER_FEDERATION_CONFIG` WRITE;
/*!40000 ALTER TABLE `USER_FEDERATION_CONFIG` DISABLE KEYS */;
/*!40000 ALTER TABLE `USER_FEDERATION_CONFIG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_FEDERATION_MAPPER`
--

DROP TABLE IF EXISTS `USER_FEDERATION_MAPPER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USER_FEDERATION_MAPPER` (
  `ID` varchar(36) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `FEDERATION_PROVIDER_ID` varchar(36) NOT NULL,
  `FEDERATION_MAPPER_TYPE` varchar(255) NOT NULL,
  `REALM_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_USR_FED_MAP_FED_PRV` (`FEDERATION_PROVIDER_ID`),
  KEY `IDX_USR_FED_MAP_REALM` (`REALM_ID`),
  CONSTRAINT `FK_FEDMAPPERPM_FEDPRV` FOREIGN KEY (`FEDERATION_PROVIDER_ID`) REFERENCES `USER_FEDERATION_PROVIDER` (`ID`),
  CONSTRAINT `FK_FEDMAPPERPM_REALM` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_FEDERATION_MAPPER`
--

LOCK TABLES `USER_FEDERATION_MAPPER` WRITE;
/*!40000 ALTER TABLE `USER_FEDERATION_MAPPER` DISABLE KEYS */;
/*!40000 ALTER TABLE `USER_FEDERATION_MAPPER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_FEDERATION_MAPPER_CONFIG`
--

DROP TABLE IF EXISTS `USER_FEDERATION_MAPPER_CONFIG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USER_FEDERATION_MAPPER_CONFIG` (
  `USER_FEDERATION_MAPPER_ID` varchar(36) NOT NULL,
  `VALUE` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) NOT NULL,
  PRIMARY KEY (`USER_FEDERATION_MAPPER_ID`,`NAME`),
  CONSTRAINT `FK_FEDMAPPER_CFG` FOREIGN KEY (`USER_FEDERATION_MAPPER_ID`) REFERENCES `USER_FEDERATION_MAPPER` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_FEDERATION_MAPPER_CONFIG`
--

LOCK TABLES `USER_FEDERATION_MAPPER_CONFIG` WRITE;
/*!40000 ALTER TABLE `USER_FEDERATION_MAPPER_CONFIG` DISABLE KEYS */;
/*!40000 ALTER TABLE `USER_FEDERATION_MAPPER_CONFIG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_FEDERATION_PROVIDER`
--

DROP TABLE IF EXISTS `USER_FEDERATION_PROVIDER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USER_FEDERATION_PROVIDER` (
  `ID` varchar(36) NOT NULL,
  `CHANGED_SYNC_PERIOD` int(11) DEFAULT NULL,
  `DISPLAY_NAME` varchar(255) DEFAULT NULL,
  `FULL_SYNC_PERIOD` int(11) DEFAULT NULL,
  `LAST_SYNC` int(11) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `PROVIDER_NAME` varchar(255) DEFAULT NULL,
  `REALM_ID` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_USR_FED_PRV_REALM` (`REALM_ID`),
  CONSTRAINT `FK_1FJ32F6PTOLW2QY60CD8N01E8` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_FEDERATION_PROVIDER`
--

LOCK TABLES `USER_FEDERATION_PROVIDER` WRITE;
/*!40000 ALTER TABLE `USER_FEDERATION_PROVIDER` DISABLE KEYS */;
/*!40000 ALTER TABLE `USER_FEDERATION_PROVIDER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_GROUP_MEMBERSHIP`
--

DROP TABLE IF EXISTS `USER_GROUP_MEMBERSHIP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USER_GROUP_MEMBERSHIP` (
  `GROUP_ID` varchar(36) NOT NULL,
  `USER_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`GROUP_ID`,`USER_ID`),
  KEY `IDX_USER_GROUP_MAPPING` (`USER_ID`),
  CONSTRAINT `FK_USER_GROUP_USER` FOREIGN KEY (`USER_ID`) REFERENCES `USER_ENTITY` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_GROUP_MEMBERSHIP`
--

LOCK TABLES `USER_GROUP_MEMBERSHIP` WRITE;
/*!40000 ALTER TABLE `USER_GROUP_MEMBERSHIP` DISABLE KEYS */;
/*!40000 ALTER TABLE `USER_GROUP_MEMBERSHIP` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_MAILING`
--

DROP TABLE IF EXISTS `USER_MAILING`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USER_MAILING` (
  `ID` varchar(36) NOT NULL,
  `REALM_ID` varchar(36) NOT NULL,
  `USER_ID` varchar(36) NOT NULL,
  `MAILING_ID` varchar(36) NOT NULL,
  `LANGUAGE` varchar(2) NOT NULL DEFAULT 'en',
  `DELIVERY` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0,1 or 2 (email/post/both)',
  PRIMARY KEY (`ID`),
  KEY `FK_USER_MAILING_USER_ENTITY` (`USER_ID`),
  KEY `FK_USER_MAILING_REALM_ENTITY` (`REALM_ID`),
  KEY `FK_USER_MAILING_MAILING_ENTITY` (`MAILING_ID`),
  CONSTRAINT `FK_USER_MAILING_MAILING_ENTITY` FOREIGN KEY (`MAILING_ID`) REFERENCES `MAILING_ENTITY` (`ID`) ON DELETE CASCADE,
  CONSTRAINT `FK_USER_MAILING_REALM_ENTITY` FOREIGN KEY (`REALM_ID`) REFERENCES `REALM` (`ID`) ON DELETE CASCADE,
  CONSTRAINT `FK_USER_MAILING_USER_ENTITY` FOREIGN KEY (`USER_ID`) REFERENCES `USER_ENTITY` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_MAILING`
--

LOCK TABLES `USER_MAILING` WRITE;
/*!40000 ALTER TABLE `USER_MAILING` DISABLE KEYS */;
/*!40000 ALTER TABLE `USER_MAILING` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_REQUIRED_ACTION`
--

DROP TABLE IF EXISTS `USER_REQUIRED_ACTION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USER_REQUIRED_ACTION` (
  `USER_ID` varchar(36) NOT NULL,
  `REQUIRED_ACTION` varchar(255) NOT NULL DEFAULT ' ',
  PRIMARY KEY (`REQUIRED_ACTION`,`USER_ID`),
  KEY `IDX_USER_REQACTIONS` (`USER_ID`),
  CONSTRAINT `FK_6QJ3W1JW9CVAFHE19BWSIUVMD` FOREIGN KEY (`USER_ID`) REFERENCES `USER_ENTITY` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_REQUIRED_ACTION`
--

LOCK TABLES `USER_REQUIRED_ACTION` WRITE;
/*!40000 ALTER TABLE `USER_REQUIRED_ACTION` DISABLE KEYS */;
INSERT INTO `USER_REQUIRED_ACTION` VALUES ('4af62885-2585-4fda-bd22-6f6171c7a285','terms_and_conditions'),('5e270d90-f0ff-4991-8b9c-3f0226b136d3','terms_and_conditions'),('bb0d7915-fe6e-4c03-9842-5ee8004d728c','terms_and_conditions'),('cbd69188-754f-41a3-89bd-7a07e48d5fd1','terms_and_conditions');
/*!40000 ALTER TABLE `USER_REQUIRED_ACTION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_ROLE_MAPPING`
--

DROP TABLE IF EXISTS `USER_ROLE_MAPPING`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USER_ROLE_MAPPING` (
  `ROLE_ID` varchar(255) NOT NULL,
  `USER_ID` varchar(36) NOT NULL,
  PRIMARY KEY (`ROLE_ID`,`USER_ID`),
  KEY `IDX_USER_ROLE_MAPPING` (`USER_ID`),
  CONSTRAINT `FK_C4FQV34P1MBYLLOXANG7B1Q3L` FOREIGN KEY (`USER_ID`) REFERENCES `USER_ENTITY` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_ROLE_MAPPING`
--

LOCK TABLES `USER_ROLE_MAPPING` WRITE;
/*!40000 ALTER TABLE `USER_ROLE_MAPPING` DISABLE KEYS */;
INSERT INTO `USER_ROLE_MAPPING` VALUES ('012f4c09-2656-4603-aac9-4a132a391e94','5544a352-9524-44ad-8617-6ed1e77dcb8e'),('0ab906cb-cc4f-4af4-9dd4-e676e6f0f9f3','4af62885-2585-4fda-bd22-6f6171c7a285'),('0ab906cb-cc4f-4af4-9dd4-e676e6f0f9f3','5e270d90-f0ff-4991-8b9c-3f0226b136d3'),('0ab906cb-cc4f-4af4-9dd4-e676e6f0f9f3','bb0d7915-fe6e-4c03-9842-5ee8004d728c'),('0ab906cb-cc4f-4af4-9dd4-e676e6f0f9f3','cbd69188-754f-41a3-89bd-7a07e48d5fd1'),('43c67ebb-198b-4547-96e9-86b7a0111566','5544a352-9524-44ad-8617-6ed1e77dcb8e'),('64a2ceb2-c94c-461f-869a-4d2d0806c389','5544a352-9524-44ad-8617-6ed1e77dcb8e'),('6ec8ccad-ff0e-40e6-a429-295f985ff0c9','bb0d7915-fe6e-4c03-9842-5ee8004d728c'),('73788df5-8b96-4a88-bd3a-17f99b717b46','cbd69188-754f-41a3-89bd-7a07e48d5fd1'),('8c430ca8-55e7-43c6-8625-527ab3f3ec3f','4af62885-2585-4fda-bd22-6f6171c7a285'),('8c430ca8-55e7-43c6-8625-527ab3f3ec3f','5e270d90-f0ff-4991-8b9c-3f0226b136d3'),('8c430ca8-55e7-43c6-8625-527ab3f3ec3f','bb0d7915-fe6e-4c03-9842-5ee8004d728c'),('8c430ca8-55e7-43c6-8625-527ab3f3ec3f','cbd69188-754f-41a3-89bd-7a07e48d5fd1'),('992f5685-d4b5-44a8-8745-78ef23eee8b4','5e270d90-f0ff-4991-8b9c-3f0226b136d3'),('a8772b31-9ccd-42b4-aff3-b9a354828e95','4af62885-2585-4fda-bd22-6f6171c7a285'),('b930fbec-a391-40dc-887c-a36fe46999a7','5544a352-9524-44ad-8617-6ed1e77dcb8e'),('beda296e-ad85-43a6-9d77-d37193147efa','4af62885-2585-4fda-bd22-6f6171c7a285'),('beda296e-ad85-43a6-9d77-d37193147efa','5e270d90-f0ff-4991-8b9c-3f0226b136d3'),('beda296e-ad85-43a6-9d77-d37193147efa','bb0d7915-fe6e-4c03-9842-5ee8004d728c'),('beda296e-ad85-43a6-9d77-d37193147efa','cbd69188-754f-41a3-89bd-7a07e48d5fd1'),('e5db3777-0d0b-4d47-9e72-5b32e740b339','5544a352-9524-44ad-8617-6ed1e77dcb8e'),('f4e93900-0b7d-4fa3-b2ca-523715fff9d1','4af62885-2585-4fda-bd22-6f6171c7a285'),('f4e93900-0b7d-4fa3-b2ca-523715fff9d1','5e270d90-f0ff-4991-8b9c-3f0226b136d3'),('f4e93900-0b7d-4fa3-b2ca-523715fff9d1','bb0d7915-fe6e-4c03-9842-5ee8004d728c'),('f4e93900-0b7d-4fa3-b2ca-523715fff9d1','cbd69188-754f-41a3-89bd-7a07e48d5fd1');
/*!40000 ALTER TABLE `USER_ROLE_MAPPING` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_SESSION`
--

DROP TABLE IF EXISTS `USER_SESSION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USER_SESSION` (
  `ID` varchar(36) NOT NULL,
  `AUTH_METHOD` varchar(255) DEFAULT NULL,
  `IP_ADDRESS` varchar(255) DEFAULT NULL,
  `LAST_SESSION_REFRESH` int(11) DEFAULT NULL,
  `LOGIN_USERNAME` varchar(255) DEFAULT NULL,
  `REALM_ID` varchar(255) DEFAULT NULL,
  `REMEMBER_ME` bit(1) NOT NULL DEFAULT b'0',
  `STARTED` int(11) DEFAULT NULL,
  `USER_ID` varchar(255) DEFAULT NULL,
  `USER_SESSION_STATE` int(11) DEFAULT NULL,
  `BROKER_SESSION_ID` varchar(255) DEFAULT NULL,
  `BROKER_USER_ID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_SESSION`
--

LOCK TABLES `USER_SESSION` WRITE;
/*!40000 ALTER TABLE `USER_SESSION` DISABLE KEYS */;
/*!40000 ALTER TABLE `USER_SESSION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `USER_SESSION_NOTE`
--

DROP TABLE IF EXISTS `USER_SESSION_NOTE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USER_SESSION_NOTE` (
  `USER_SESSION` varchar(36) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `VALUE` varchar(2048) DEFAULT NULL,
  PRIMARY KEY (`USER_SESSION`,`NAME`),
  CONSTRAINT `FK5EDFB00FF51D3472` FOREIGN KEY (`USER_SESSION`) REFERENCES `USER_SESSION` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `USER_SESSION_NOTE`
--

LOCK TABLES `USER_SESSION_NOTE` WRITE;
/*!40000 ALTER TABLE `USER_SESSION_NOTE` DISABLE KEYS */;
/*!40000 ALTER TABLE `USER_SESSION_NOTE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `WEB_ORIGINS`
--

DROP TABLE IF EXISTS `WEB_ORIGINS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `WEB_ORIGINS` (
  `CLIENT_ID` varchar(36) NOT NULL,
  `VALUE` varchar(255) NOT NULL,
  PRIMARY KEY (`CLIENT_ID`,`VALUE`),
  KEY `IDX_WEB_ORIG_CLIENT` (`CLIENT_ID`),
  CONSTRAINT `FK_LOJPHO213XCX4WNKOG82SSRFY` FOREIGN KEY (`CLIENT_ID`) REFERENCES `CLIENT` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `WEB_ORIGINS`
--

LOCK TABLES `WEB_ORIGINS` WRITE;
/*!40000 ALTER TABLE `WEB_ORIGINS` DISABLE KEYS */;
/*!40000 ALTER TABLE `WEB_ORIGINS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'keycloak'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-05-18 17:15:16
