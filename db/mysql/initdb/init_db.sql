CREATE TABLE IF NOT EXISTS `bklg_db`.`spaces`(
`id` int AUTO_INCREMENT NOT NULL , 
`space_id` varchar(10) NOT NULL, 
`api_key` varchar(1024) NOT NULL, 
`url` varchar(255) NOT NULL, 
`project_key` varchar(255) NOT NULL,
PRIMARY KEY (`id`)
);

-- urlをdomainに変更
ALTER TABLE `bklg_db`.`spaces` CHANGE COLUMN `url` `domain` varchar(255) NOT NULL;

-- project_idの追加
ALTER TABLE `bklg_db`.`spaces` ADD COLUMN `project_id` BIGINT NOT NULL; 