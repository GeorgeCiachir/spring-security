CREATE TABLE IF NOT EXISTS `spring`.`product`
(
    `id`         INT         NOT NULL AUTO_INCREMENT,
    `owner`      VARCHAR(45) NOT NULL,
    `name`       VARCHAR(45) NOT NULL,
    `price`      INT         NOT NULL,
    PRIMARY KEY (`id`)
);
