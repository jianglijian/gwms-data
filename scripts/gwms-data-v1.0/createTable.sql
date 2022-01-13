CREATE TABLE `abc_setting_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_time`  datetime DEFAULT NULL,
  `creator_id` bigint(20) DEFAULT NULL,
  `updated_time`  datetime DEFAULT NULL,
  `updator_id` bigint(20) DEFAULT NULL,
  `lock_version` bigint(20) DEFAULT NULL,
  `wh_id` BIGINT DEFAULT NULL,
  `level_a` varchar(255) DEFAULT NULL,
  `level_b` varchar(255) DEFAULT NULL,
  `level_c` varchar(255) DEFAULT NULL,
  `level_d` varchar(255) DEFAULT NULL,
  `level_e` varchar(255) DEFAULT NULL,
  `analyze_type` BIGINT DEFAULT NULL,
  `degree_amount` INTEGER DEFAULT NULL,
  `udf1` varchar(255) DEFAULT NULL,
  `udf2` varchar(255) DEFAULT NULL,
  `udf3` varchar(255) DEFAULT NULL,
  `udf4` varchar(255) DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

