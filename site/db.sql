SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

CREATE TABLE IF NOT EXISTS `lnch_sip` (
  `time` varchar(255) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sip` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC AUTO_INCREMENT=0 ;

CREATE TABLE IF NOT EXISTS `lnch_banlist` (
  `name` varchar(32) NOT NULL,
  `reason` text NOT NULL,
  `admin` varchar(32) NOT NULL,
  `time` bigint(20) NOT NULL,
  `temptime` bigint(20) NOT NULL DEFAULT '0',
  `type` int(11) NOT NULL DEFAULT '0',
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC AUTO_INCREMENT=0 ;

CREATE TABLE IF NOT EXISTS `lnch_usersession` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user` varchar(255) DEFAULT 'user',
  `session` varchar(255) DEFAULT NULL,
  `server` varchar(255) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `realmoney` int(255) DEFAULT '0',
  `md5` varchar(255) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=0 ;