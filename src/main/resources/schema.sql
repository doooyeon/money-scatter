DROP TABLE IF EXISTS `assigned_money`;
DROP TABLE IF EXISTS `scattered_money`;
DROP TABLE IF EXISTS `token`;

CREATE TABLE `token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,

  `value` varchar(10) COLLATE utf8mb4_bin NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `room_id` bigint(20) NOT NULL,

  `created_at` datetime(6) NOT NULL,
  `using_expired_at` datetime(6) NOT NULL,
  `reading_expired_at` datetime(6) NOT NULL,

  PRIMARY KEY (`id`),
  KEY `token_room_idx` (`value`, `room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE `scattered_money` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,

  `value` bigint(20) NOT NULL,
  `token_id` bigint(20) NOT NULL,

  `assigned` bit(1) DEFAULT FALSE NOT NULL,

  PRIMARY KEY (`id`),
  FOREIGN KEY (`token_id`) REFERENCES token (`id`),
  KEY `token_idx` (`token_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE `assigned_money` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,

  `scattered_money_id` bigint(20) NOT NULL,
  `token_id` bigint(20) NOT NULL,

  `assignor` bigint(20) DEFAULT NULL,
  `assigned_at` datetime(6) DEFAULT NULL,

  PRIMARY KEY (`id`),
  FOREIGN KEY (`scattered_money_id`) REFERENCES scattered_money (`id`),
  FOREIGN KEY (`token_id`) REFERENCES token (`id`),
  KEY `scattered_money_idx` (`scattered_money_id`),
  KEY `token_idx` (`token_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
