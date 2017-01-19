CREATE TABLE `twitter_data` (
  `uuid` varchar(36) NOT NULL,
  `profile_name` varchar(100) NOT NULL,
  `profile_img_url` varchar(250) NOT NULL,
  `posting_date` datetime NOT NULL,
  `text` varchar(250) NOT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;