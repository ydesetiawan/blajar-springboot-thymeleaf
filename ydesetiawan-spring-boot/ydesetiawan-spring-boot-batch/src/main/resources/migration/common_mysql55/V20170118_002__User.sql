CREATE TABLE `user` (
  `uuid` varchar(36) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `enabled` bit(1) NOT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user_role` (
   `uuid` varchar(36) NOT NULL,
  `role` varchar(50) NOT NULL,
  `user` varchar(36) NOT NULL,
  PRIMARY KEY (`uuid`),
  FOREIGN KEY (`user`) REFERENCES `user` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;