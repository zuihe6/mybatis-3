DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL,
    `age` INT,
    `email` VARCHAR(100)
);

INSERT INTO `user` (`id`, `name`, `age`, `email`) VALUES
(1, 'Tom', 18, 'tom@example.com'),
(2, 'Jerry', 20, 'jerry@example.com');
