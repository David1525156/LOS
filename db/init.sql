-- =========================================================
-- 在线图书借阅系统 数据库初始化脚本
-- 环境：MySQL 8.0 / utf8mb4
-- 用法：mysql -uroot -p < db/init.sql
-- =========================================================

CREATE DATABASE IF NOT EXISTS library
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_general_ci;

USE library;

-- ---------------------------
-- 图书表
-- ---------------------------
DROP TABLE IF EXISTS book;
CREATE TABLE book (
    id          BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
    title       VARCHAR(100)  NOT NULL COMMENT '书名',
    author      VARCHAR(50)   NOT NULL COMMENT '作者',
    description VARCHAR(1000) DEFAULT NULL COMMENT '图书简介',
    stock       INT           NOT NULL DEFAULT 0 COMMENT '可借阅库存',
    create_time DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_title (title)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '图书表';

-- ---------------------------
-- 管理员表
-- ---------------------------
DROP TABLE IF EXISTS admin;
CREATE TABLE admin (
    id       BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    username VARCHAR(50)  NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码(Sprint1 演示用明文,后续可改 BCrypt)',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '管理员表';

-- 默认管理员：admin / 123456
INSERT INTO admin (username, password) VALUES ('admin', '123456');

-- ---------------------------
-- 种子数据：25 本图书
-- ---------------------------
INSERT INTO book (title, author, description, stock) VALUES
('三体', '刘慈欣', '地球文明与三体文明之间的星际战争与文明兴衰。', 5),
('三体Ⅱ·黑暗森林', '刘慈欣', '宇宙社会学与黑暗森林法则的极致演绎。', 4),
('三体Ⅲ·死神永生', '刘慈欣', '宇宙文明终极命运的恢弘故事。', 3),
('红楼梦', '曹雪芹', '中国古典四大名著之一，封建社会的百科全书。', 6),
('西游记', '吴承恩', '唐僧师徒四人西天取经的神话故事。', 8),
('水浒传', '施耐庵', '北宋末年宋江起义的英雄传奇。', 7),
('三国演义', '罗贯中', '东汉末年至三国时期的乱世争霸。', 9),
('活着', '余华', '福贵历经苦难却依旧坚韧的生存故事。', 4),
('平凡的世界', '路遥', '黄土高原上普通人的奋斗与爱情。', 5),
('围城', '钱钟书', '知识分子的婚姻与人生困境。', 3),
('百年孤独', '加西亚·马尔克斯', '布恩迪亚家族七代人的传奇故事。', 2),
('小王子', '安托万·德·圣埃克苏佩里', '关于爱与责任的童话。', 9),
('老人与海', '欧内斯特·海明威', '老渔夫与大海搏斗的硬汉精神。', 4),
('呐喊', '鲁迅', '中国现代文学的开山之作。', 3),
('朝花夕拾', '鲁迅', '鲁迅回忆性散文集。', 2),
('骆驼祥子', '老舍', '人力车夫祥子的悲剧人生。', 5),
('边城', '沈从文', '湘西小镇翠翠的纯真爱情。', 3),
('白鹿原', '陈忠实', '渭河平原上白鹿村的家族恩怨。', 4),
('狼图腾', '姜戎', '草原狼与游牧文明的生存哲学。', 2),
('我们仨', '杨绛', '钱锺书一家三口的生活回忆。', 1),
('挪威的森林', '村上春树', '青春与死亡的爱情故事。', 3),
('解忧杂货店', '东野圭吾', '杂货店与时空交错的温情故事。', 6),
('嫌疑人X的献身', '东野圭吾', '天才数学家的极致爱情与牺牲。', 4),
('目送', '龙应台', '关于亲情与生命的随笔集。', 2),
('天才在左 疯子在右', '高铭', '精神病人访谈录。', 5);
