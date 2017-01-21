CREATE TABLE `user_search_params` (
   `uuid` varchar(36) NOT NULL,
  `user` varchar(36) NOT NULL,
  `key` varchar(50) NOT NULL,
  `value` varchar(50) NOT NULL,
  PRIMARY KEY (`uuid`),
  FOREIGN KEY (`user`) REFERENCES `user` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;