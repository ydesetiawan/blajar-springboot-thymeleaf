CREATE TABLE `twitter_data` (
  `id` BIGINT NOT NULL,
  `profile_name` varchar(100) NOT NULL,
  `profile_img_url` varchar(250) NOT NULL,
  `posting_date` datetime NOT NULL,
  `text` longtext DEFAULT null,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;