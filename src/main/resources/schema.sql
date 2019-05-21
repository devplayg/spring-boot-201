-- --------------------------------------------------------
-- 호스트:                          127.0.0.1
-- 서버 버전:                        10.3.14-MariaDB-1:10.3.14+maria~bionic - mariadb.org binary distribution
-- 서버 OS:                        debian-linux-gnu
-- HeidiSQL 버전:                  10.1.0.5464
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- sb201 데이터베이스 구조 내보내기
CREATE DATABASE IF NOT EXISTS `sb201` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;
USE `sb201`;

-- 테이블 sb201.adt_audit 구조 내보내기
CREATE TABLE IF NOT EXISTS `adt_audit` (
  `audit_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `member_id` bigint(20) NOT NULL,
  `category` varchar(32) NOT NULL,
  `ip` int(10) unsigned NOT NULL,
  `message` varchar(256) NOT NULL,
  `detail` varchar(4096) DEFAULT NULL,
  `created` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`audit_id`),
  KEY `ix_memberID` (`member_id`),
  KEY `ix_created` (`created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 내보낼 데이터가 선택되어 있지 않습니다.
-- 테이블 sb201.mbr_member 구조 내보내기
CREATE TABLE IF NOT EXISTS `mbr_member` (
  `member_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) NOT NULL DEFAULT 0,
  `username` varchar(32) NOT NULL,
  `email` varchar(256) NOT NULL,
  `name` varchar(64) NOT NULL,
  `enabled` bit(1) NOT NULL DEFAULT b'0',
  `timezone` varchar(64) NOT NULL DEFAULT '',
  `password` varchar(72) NOT NULL DEFAULT '',
  `login_count` int(11) unsigned NOT NULL DEFAULT 0,
  `failed_login_count` int(11) unsigned NOT NULL DEFAULT 0,
  `last_success_login` datetime NOT NULL DEFAULT '1970-01-01 00:00:00',
  `last_failed_login` datetime NOT NULL DEFAULT '1970-01-01 00:00:00',
  `last_read_message` int(11) unsigned NOT NULL DEFAULT 0,
  `last_password_change` datetime NOT NULL DEFAULT current_timestamp(),
  `session_id` varchar(64) NOT NULL DEFAULT '',
  `created` datetime NOT NULL DEFAULT current_timestamp(),
  `updated` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`member_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 내보낼 데이터가 선택되어 있지 않습니다.
-- 테이블 sb201.mbr_role 구조 내보내기
CREATE TABLE IF NOT EXISTS `mbr_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `member_id` bigint(20) NOT NULL,
  `role` varchar(32) NOT NULL,
  `created` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`role_id`),
  KEY `fk_mbrRole_memberID` (`member_id`),
  CONSTRAINT `fk_mbrRole_memberID` FOREIGN KEY (`member_id`) REFERENCES `mbr_member` (`member_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 내보낼 데이터가 선택되어 있지 않습니다.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
