INSERT INTO `spring`.`user` (`id`, `username`, `password`, `algorithm`)
VALUES (NULL, 'john', '$2a$10$b4eoR6Lh2WrBGcoMiJe1q.lAwr0HWkus6xEJcSDMwN64hwfJrNc1i', 'BCRYPT');

INSERT INTO `spring`.`authority` (`id`, `name`, `user`)
VALUES (NULL, 'READ', '1');

INSERT INTO `spring`.`authority` (`id`, `name`, `user`)
VALUES (NULL, 'WRITE', '1');

INSERT INTO `spring`.`product` (`id`, `name`, `price`, `currency`)
VALUES (NULL, 'Chocolate', '10', 'USD');
