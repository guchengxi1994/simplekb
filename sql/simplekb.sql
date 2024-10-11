/*
 Navicat Premium Data Transfer

 Source Server         : local-docker
 Source Server Type    : MySQL
 Source Server Version : 90001 (9.0.1)
 Source Host           : localhost:3307
 Source Schema         : simplekb

 Target Server Type    : MySQL
 Target Server Version : 90001 (9.0.1)
 File Encoding         : 65001

 Date: 04/10/2024 09:25:33
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for kb_file
-- ----------------------------
DROP TABLE IF EXISTS `kb_file`;
CREATE TABLE `kb_file`
(
    `kb_file_id`   bigint                                                        NOT NULL AUTO_INCREMENT,
    `kb_file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `kb_file_type` int NULL DEFAULT NULL,
    `create_at`    datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_at`    datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`   tinyint(1) NULL DEFAULT 0 COMMENT '是否被删除',
    PRIMARY KEY (`kb_file_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for kb_file_chunk
-- ----------------------------
DROP TABLE IF EXISTS `kb_file_chunk`;
CREATE TABLE `kb_file_chunk`
(
    `chunk_id`      bigint   NOT NULL AUTO_INCREMENT,
    `chunk_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `file_id`       bigint   NOT NULL,
    `create_at`     datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_at`     datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`    tinyint(1) NULL DEFAULT 0 COMMENT '是否被删除',
    PRIMARY KEY (`chunk_id`) USING BTREE,
    FULLTEXT        INDEX `idx_content`(`chunk_content`) WITH PARSER `ngram`
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for kb_file_chunk_keywords
-- ----------------------------
DROP TABLE IF EXISTS `kb_file_chunk_keywords`;
CREATE TABLE `kb_file_chunk_keywords`
(
    `keyword`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `chunk_id` bigint                                                        NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for kb_file_type
-- ----------------------------
DROP TABLE IF EXISTS `kb_file_type`;
CREATE TABLE `kb_file_type`
(
    `type_id`    int      NOT NULL AUTO_INCREMENT,
    `type_name`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `create_at`  datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_at`  datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否被删除',
    PRIMARY KEY (`type_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for kb_prompt
-- ----------------------------
DROP TABLE IF EXISTS `kb_prompt`;
CREATE TABLE `kb_prompt`
(
    `prompt_id`      bigint                                                        NOT NULL AUTO_INCREMENT,
    `prompt_name`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `prompt_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `create_at`      datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_at`      datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`     tinyint(1) NULL DEFAULT 0 COMMENT '是否被删除',
    PRIMARY KEY (`prompt_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

SET
FOREIGN_KEY_CHECKS = 1;
