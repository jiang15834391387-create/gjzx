SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for data_node_type
-- ----------------------------
DROP TABLE IF EXISTS `data_node_type`;
CREATE TABLE `data_node_type`  (
                                   `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主键',
                                   `node_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '节点类型',
                                   `node_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '节点名称',
                                   `node_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '节点编码',
                                   `parent_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '父节点ID',
                                   `tenant_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '000000' COMMENT '租户编号',
                                   `version` int NULL DEFAULT 0 COMMENT '版本',
                                   `create_dept` bigint NULL DEFAULT NULL COMMENT '创建部门',
                                   `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                   `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
                                   `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                   `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
                                   `del_flag` int NULL DEFAULT 0 COMMENT '删除标志',
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '树节点' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;




ALTER TABLE sys_oss MODIFY url VARCHAR(1024);
