/*
 Navicat Premium Data Transfer

 Source Server         : 229mysql
 Source Server Type    : MySQL
 Source Server Version : 90100 (9.1.0)
 Source Host           : 211.159.156.229:3306
 Source Schema         : dangan_new

 Target Server Type    : MySQL
 Target Server Version : 90100 (9.1.0)
 File Encoding         : 65001

 Date: 13/11/2025 13:53:50
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件路径',
  `query_param` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由参数',
  `is_frame` int NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `is_cache` int NULL DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '显示状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `create_dept` bigint NULL DEFAULT NULL COMMENT '创建部门',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 8, 'system', NULL, '', 1, 0, 'M', '0', '0', '', 'system', 103, 1, '2024-12-25 13:00:37', 1, '2025-11-07 14:46:27', '系统管理目录');
INSERT INTO `sys_menu` VALUES (2, '系统监控', 0, 9, 'monitor', NULL, '', 1, 0, 'M', '0', '0', '', 'monitor', 103, 1, '2024-12-25 13:00:37', 1, '2025-11-07 14:45:38', '系统监控目录');
INSERT INTO `sys_menu` VALUES (3, '系统工具', 0, 12, 'tool', NULL, '', 1, 0, 'M', '1', '0', '', 'tool', 103, 1, '2024-12-25 13:00:37', 1, '2025-11-07 14:46:32', '系统工具目录');
INSERT INTO `sys_menu` VALUES (4, 'PLUS官网', 0, 9, 'https://gitee.com/dromara/RuoYi-Vue-Plus', NULL, '', 0, 0, 'M', '1', '0', '', 'guide', 103, 1, '2024-12-25 13:00:37', 1, '2025-11-07 14:45:26', 'RuoYi-Vue-Plus官网地址');
INSERT INTO `sys_menu` VALUES (5, '支付审核', 0, 9, 'demo', NULL, '', 1, 0, 'M', '1', '0', '', 'star', 103, 1, '2024-12-25 13:00:37', 1, '2025-11-07 14:50:43', '测试菜单');
INSERT INTO `sys_menu` VALUES (6, '租户管理', 0, 5, 'tenant', NULL, '', 1, 0, 'M', '0', '0', '', 'chart', 103, 1, '2024-12-25 13:00:37', 1, '2025-11-07 14:45:12', '租户管理目录');
INSERT INTO `sys_menu` VALUES (100, '用户管理', 6, 1, 'user', 'system/user/index', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user', 103, 1, '2024-12-25 13:00:37', 1, '2025-11-07 14:40:09', '用户管理菜单');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 2, 'role', 'system/role/index', '', 1, 0, 'C', '0', '0', 'system:role:list', 'peoples', 103, 1, '2024-12-25 13:00:37', 1, '2025-10-09 15:41:00', '角色管理菜单');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'tree-table', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '菜单管理菜单');
INSERT INTO `sys_menu` VALUES (103, '部门管理', 1986683157960404994, 5, 'dept', 'system/dept/index', '', 1, 0, 'C', '0', '0', 'system:dept:list', 'tree', 103, 1, '2024-12-25 13:00:37', 1, '2025-11-07 14:37:06', '部门管理菜单');
INSERT INTO `sys_menu` VALUES (104, '岗位管理', 1986682678966693889, 5, 'post', 'system/post/index', '', 1, 0, 'C', '0', '0', 'system:post:list', 'post', 103, 1, '2024-12-25 13:00:37', 1, '2025-11-07 14:30:59', '岗位管理菜单');
INSERT INTO `sys_menu` VALUES (105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', 1, 0, 'C', '0', '0', 'system:dict:list', 'dict', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '字典管理菜单');
INSERT INTO `sys_menu` VALUES (106, '参数设置', 1, 7, 'config', 'system/config/index', '', 1, 0, 'C', '0', '0', 'system:config:list', 'edit', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '参数设置菜单');
INSERT INTO `sys_menu` VALUES (107, '通知公告', 6, 8, 'notice', 'system/notice/index', '', 1, 0, 'C', '0', '0', 'system:notice:list', 'message', 103, 1, '2024-12-25 13:00:37', 1, '2025-11-07 14:40:29', '通知公告菜单');
INSERT INTO `sys_menu` VALUES (108, '日志管理', 1, 9, 'log', '', '', 1, 0, 'M', '0', '0', '', 'log', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '日志管理菜单');
INSERT INTO `sys_menu` VALUES (109, '在线用户', 2, 1, 'online', 'monitor/online/index', '', 1, 0, 'C', '0', '0', 'monitor:online:list', 'online', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '在线用户菜单');
INSERT INTO `sys_menu` VALUES (113, '缓存监控', 2, 5, 'cache', 'monitor/cache/index', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '缓存监控菜单');
INSERT INTO `sys_menu` VALUES (115, '代码生成', 3, 2, 'gen', 'tool/gen/index', '', 1, 0, 'C', '0', '0', 'tool:gen:list', 'code', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '代码生成菜单');
INSERT INTO `sys_menu` VALUES (117, 'Admin监控', 2, 5, 'Admin', 'monitor/admin/index', '', 1, 0, 'C', '0', '0', 'monitor:admin:list', 'dashboard', 103, 1, '2024-12-25 13:00:37', NULL, NULL, 'Admin监控菜单');
INSERT INTO `sys_menu` VALUES (118, '文件管理', 1, 10, 'oss', 'system/oss/index', '', 1, 0, 'C', '0', '0', 'system:oss:list', 'upload', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '文件管理菜单');
INSERT INTO `sys_menu` VALUES (120, '任务调度中心', 2, 6, 'snailjob', 'monitor/snailjob/index', '', 1, 0, 'C', '0', '0', 'monitor:snailjob:list', 'job', 103, 1, '2024-12-25 13:00:37', NULL, NULL, 'SnailJob控制台菜单');
INSERT INTO `sys_menu` VALUES (121, '租户管理', 6, 1, 'tenant', 'system/tenant/index', '', 1, 0, 'C', '0', '0', 'system:tenant:list', 'list', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '租户管理菜单');
INSERT INTO `sys_menu` VALUES (122, '租户套餐管理', 6, 2, 'tenantPackage', 'system/tenantPackage/index', '', 1, 0, 'C', '0', '0', 'system:tenantPackage:list', 'form', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '租户套餐管理菜单');
INSERT INTO `sys_menu` VALUES (123, '白名单管理', 1, 11, 'client', 'system/client/index', '', 1, 0, 'C', '0', '0', 'system:client:list', 'international', 103, 1, '2024-12-25 13:00:37', 1, '2025-11-07 14:41:13', '客户端管理菜单');
INSERT INTO `sys_menu` VALUES (500, '操作日志', 108, 1, 'operlog', 'monitor/operlog/index', '', 1, 0, 'C', '0', '0', 'monitor:operlog:list', 'form', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '操作日志菜单');
INSERT INTO `sys_menu` VALUES (501, '登录日志', 108, 2, 'logininfor', 'monitor/logininfor/index', '', 1, 0, 'C', '0', '0', 'monitor:logininfor:list', 'logininfor', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '登录日志菜单');
INSERT INTO `sys_menu` VALUES (1001, '用户查询', 100, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1002, '用户新增', 100, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1003, '用户修改', 100, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1004, '用户删除', 100, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1005, '用户导出', 100, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1006, '用户导入', 100, 6, '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1007, '重置密码', 100, 7, '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1008, '角色查询', 101, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1009, '角色新增', 101, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1010, '角色修改', 101, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1011, '角色删除', 101, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1012, '角色导出', 101, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1013, '菜单查询', 102, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1014, '菜单新增', 102, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1015, '菜单修改', 102, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1016, '菜单删除', 102, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1017, '部门查询', 103, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1018, '部门新增', 103, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1019, '部门修改', 103, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1020, '部门删除', 103, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1021, '岗位查询', 104, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1022, '岗位新增', 104, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1023, '岗位修改', 104, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1024, '岗位删除', 104, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1025, '岗位导出', 104, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1026, '字典查询', 105, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1027, '字典新增', 105, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1028, '字典修改', 105, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1029, '字典删除', 105, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1030, '字典导出', 105, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1031, '参数查询', 106, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1032, '参数新增', 106, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1033, '参数修改', 106, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1034, '参数删除', 106, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1035, '参数导出', 106, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1036, '公告查询', 107, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:query', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1037, '公告新增', 107, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:add', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1038, '公告修改', 107, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1039, '公告删除', 107, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1040, '操作查询', 500, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:query', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1041, '操作删除', 500, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:remove', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1042, '日志导出', 500, 4, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:export', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1043, '登录查询', 501, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:query', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1044, '登录删除', 501, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:remove', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1045, '日志导出', 501, 3, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:export', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1046, '在线查询', 109, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1047, '批量强退', 109, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1048, '单条强退', 109, 3, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1050, '账户解锁', 501, 4, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:unlock', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1055, '生成查询', 115, 1, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1056, '生成修改', 115, 2, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1057, '生成删除', 115, 3, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1058, '导入代码', 115, 2, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1059, '预览代码', 115, 4, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1060, '生成代码', 115, 5, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1061, '客户端管理查询', 123, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:client:query', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1062, '客户端管理新增', 123, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:client:add', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1063, '客户端管理修改', 123, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:client:edit', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1064, '客户端管理删除', 123, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:client:remove', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1065, '客户端管理导出', 123, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:client:export', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1500, '测试单表', 5, 1, 'demo', 'demo/demo/index', '', 1, 0, 'C', '1', '0', 'demo:demo:list', '#', 103, 1, '2024-12-25 13:00:37', 1, '2025-09-28 14:14:31', '测试单表菜单');
INSERT INTO `sys_menu` VALUES (1501, '测试单表查询', 1500, 1, '#', '', '', 1, 0, 'F', '0', '0', 'demo:demo:query', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1502, '测试单表新增', 1500, 2, '#', '', '', 1, 0, 'F', '0', '0', 'demo:demo:add', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1503, '测试单表修改', 1500, 3, '#', '', '', 1, 0, 'F', '0', '0', 'demo:demo:edit', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1504, '测试单表删除', 1500, 4, '#', '', '', 1, 0, 'F', '0', '0', 'demo:demo:remove', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1505, '测试单表导出', 1500, 5, '#', '', '', 1, 0, 'F', '0', '0', 'demo:demo:export', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1506, '测试树表', 5, 1, 'tree', 'demo/tree/index', '', 1, 0, 'C', '1', '0', 'demo:tree:list', '#', 103, 1, '2024-12-25 13:00:37', 1, '2025-09-28 14:14:26', '测试树表菜单');
INSERT INTO `sys_menu` VALUES (1507, '测试树表查询', 1506, 1, '#', '', '', 1, 0, 'F', '0', '0', 'demo:tree:query', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1508, '测试树表新增', 1506, 2, '#', '', '', 1, 0, 'F', '0', '0', 'demo:tree:add', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1509, '测试树表修改', 1506, 3, '#', '', '', 1, 0, 'F', '0', '0', 'demo:tree:edit', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1510, '测试树表删除', 1506, 4, '#', '', '', 1, 0, 'F', '0', '0', 'demo:tree:remove', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1511, '测试树表导出', 1506, 5, '#', '', '', 1, 0, 'F', '0', '0', 'demo:tree:export', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1600, '文件查询', 118, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:oss:query', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1601, '文件上传', 118, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:oss:upload', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1602, '文件下载', 118, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:oss:download', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1603, '文件删除', 118, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:oss:remove', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1606, '租户查询', 121, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:tenant:query', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1607, '租户新增', 121, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:tenant:add', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1608, '租户修改', 121, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:tenant:edit', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1609, '租户删除', 121, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:tenant:remove', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1610, '租户导出', 121, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:tenant:export', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1611, '租户套餐查询', 122, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:tenantPackage:query', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1612, '租户套餐新增', 122, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:tenantPackage:add', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1613, '租户套餐修改', 122, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:tenantPackage:edit', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1614, '租户套餐删除', 122, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:tenantPackage:remove', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1615, '租户套餐导出', 122, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:tenantPackage:export', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1620, '配置列表', 118, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:ossConfig:list', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1621, '配置添加', 118, 6, '#', '', '', 1, 0, 'F', '0', '0', 'system:ossConfig:add', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1622, '配置编辑', 118, 6, '#', '', '', 1, 0, 'F', '0', '0', 'system:ossConfig:edit', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1623, '配置删除', 118, 6, '#', '', '', 1, 0, 'F', '0', '0', 'system:ossConfig:remove', '#', 103, 1, '2024-12-25 13:00:37', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (11616, '工作流', 0, 9, 'workflow1', '', '', 1, 0, 'M', '1', '0', '', 'workflow', 103, 1, '2024-12-25 13:00:56', 1, '2025-11-07 14:52:10', '');
INSERT INTO `sys_menu` VALUES (11617, '工作流模版', 1986681502116294657, 2, 'model', 'workflow/model/index', '', 1, 1, 'C', '0', '0', 'workflow:model:list', 'model', 103, 1, '2024-12-25 13:00:56', 1, '2025-11-07 14:29:44', '');
INSERT INTO `sys_menu` VALUES (11618, '我的任务', 0, 12, 'task', '', '', 1, 0, 'M', '1', '0', '', 'my-task', 103, 1, '2024-12-25 13:00:56', 1, '2025-11-07 14:50:59', '');
INSERT INTO `sys_menu` VALUES (11619, '我的待办', 1986679523210113026, 2, 'taskWaiting', 'workflow/task/taskWaiting', '', 1, 1, 'C', '0', '0', '', 'waiting', 103, 1, '2024-12-25 13:00:56', 1, '2025-11-07 14:19:13', '');
INSERT INTO `sys_menu` VALUES (11620, '工作流配置', 1986681502116294657, 3, 'processDefinition', 'workflow/processDefinition/index', '', 1, 1, 'C', '0', '0', '', 'process-definition', 103, 1, '2024-12-25 13:00:56', 1, '2025-11-07 14:29:15', '');
INSERT INTO `sys_menu` VALUES (11621, '流程实例', 1986681201330171906, 1, 'processInstance', 'workflow/processInstance/index', '', 1, 1, 'C', '0', '0', '', 'tree-table', 103, 1, '2024-12-25 13:00:56', 1, '2025-11-07 14:25:03', '');
INSERT INTO `sys_menu` VALUES (11622, '工作台组别配置', 1986681502116294657, 0, 'category', 'workflow/category/index', '', 1, 0, 'C', '0', '0', 'workflow:category:list', 'category', 103, 1, '2024-12-25 13:00:56', 1, '2025-11-07 14:28:11', '');
INSERT INTO `sys_menu` VALUES (11623, '流程分类查询', 11622, 1, '#', '', '', 1, 0, 'F', '0', '0', 'workflow:category:query', '#', 103, 1, '2024-12-25 13:00:56', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (11624, '流程分类新增', 11622, 2, '#', '', '', 1, 0, 'F', '0', '0', 'workflow:category:add', '#', 103, 1, '2024-12-25 13:00:56', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (11625, '流程分类修改', 11622, 3, '#', '', '', 1, 0, 'F', '0', '0', 'workflow:category:edit', '#', 103, 1, '2024-12-25 13:00:56', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (11626, '流程分类删除', 11622, 4, '#', '', '', 1, 0, 'F', '0', '0', 'workflow:category:remove', '#', 103, 1, '2024-12-25 13:00:56', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (11627, '流程分类导出', 11622, 5, '#', '', '', 1, 0, 'F', '0', '0', 'workflow:category:export', '#', 103, 1, '2024-12-25 13:00:56', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (11628, '表单管理', 1986681502116294657, 5, 'formManage', 'workflow/formManage/index', NULL, 1, 0, 'C', '0', '0', 'workflow:formManage:list', 'tree-table', 103, 1, '2024-12-25 13:00:56', 1, '2025-11-07 14:51:52', '表单管理菜单');
INSERT INTO `sys_menu` VALUES (11629, '我发起的', 1986678335609712642, 1, 'myDocument', 'workflow/task/myDocument', '', 1, 1, 'C', '0', '0', '', 'guide', 103, 1, '2024-12-25 13:00:56', 1, '2025-11-07 14:18:48', '');
INSERT INTO `sys_menu` VALUES (11630, '流程监控', 11616, 4, 'monitor', '', '', 1, 0, 'M', '1', '0', '', 'monitor', 103, 1, '2024-12-25 13:00:56', 1, '2025-11-07 14:50:07', '');
INSERT INTO `sys_menu` VALUES (11631, '待办任务', 1986680818960642050, 2, 'allTaskWaiting', 'workflow/task/allTaskWaiting', '', 1, 1, 'C', '0', '0', '', 'waiting', 103, 1, '2024-12-25 13:00:56', 1, '2025-11-07 14:23:45', '');
INSERT INTO `sys_menu` VALUES (11632, '我的已办', 1986679523210113026, 3, 'taskFinish', 'workflow/task/taskFinish', '', 1, 1, 'C', '0', '0', '', 'finish', 103, 1, '2024-12-25 13:00:56', 1, '2025-11-07 14:19:54', '');
INSERT INTO `sys_menu` VALUES (11633, '我的抄送', 1986679523210113026, 4, 'taskCopyList', 'workflow/task/taskCopyList', '', 1, 1, 'C', '0', '0', '', 'my-copy', 103, 1, '2024-12-25 13:00:56', 1, '2025-11-07 14:19:34', '');
INSERT INTO `sys_menu` VALUES (11638, '请假申请', 5, 1, 'leave', 'workflow/leave/index', NULL, 1, 0, 'C', '1', '0', 'workflow:leave:list', '#', 103, 1, '2024-12-25 13:00:56', 1, '2025-09-28 14:13:54', '请假申请菜单');
INSERT INTO `sys_menu` VALUES (11639, '请假申请查询', 11638, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'workflow:leave:query', '#', 103, 1, '2024-12-25 13:00:56', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (11640, '请假申请新增', 11638, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'workflow:leave:add', '#', 103, 1, '2024-12-25 13:00:56', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (11641, '请假申请修改', 11638, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'workflow:leave:edit', '#', 103, 1, '2024-12-25 13:00:56', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (11642, '请假申请删除', 11638, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'workflow:leave:remove', '#', 103, 1, '2024-12-25 13:00:56', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (11643, '请假申请导出', 11638, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'workflow:leave:export', '#', 103, 1, '2024-12-25 13:00:56', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (11644, '表单管理查询', 11628, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'workflow:formManage:query', '', 103, 1, '2024-12-25 13:00:56', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (11645, '表单管理新增', 11628, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'workflow:formManage:add', '', 103, 1, '2024-12-25 13:00:56', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (11646, '表单管理修改', 11628, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'workflow:formManage:edit', '', 103, 1, '2024-12-25 13:00:56', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (11647, '表单管理删除', 11628, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'workflow:formManage:remove', '', 103, 1, '2024-12-25 13:00:56', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (11648, '表单管理导出', 11628, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'workflow:formManage:export', 'tree-table', 103, 1, '2024-12-25 13:00:56', NULL, NULL, '');
INSERT INTO `sys_menu` VALUES (1877908356287098881, '提交申请', 1986678335609712642, 1, 'expenseCaim', 'expenseCaim/index', NULL, 1, 0, 'C', '0', '0', 'system:expense:Reimbursement', 'dict', 103, 1, '2025-01-11 10:40:17', 1, '2025-11-07 14:16:07', '');
INSERT INTO `sys_menu` VALUES (1877908792465354753, '添加', 1877908356287098881, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:expenseReimbursement:add', '', 103, 1, '2025-01-11 10:42:01', 1, '2025-01-11 10:42:01', '');
INSERT INTO `sys_menu` VALUES (1899741403080712194, '表单配置', 1986681502116294657, 1, 'form', 'form/index', NULL, 1, 0, 'C', '0', '0', NULL, 'build', 103, 1, '2025-03-12 16:37:01', 1, '2025-11-07 14:29:03', '');
INSERT INTO `sys_menu` VALUES (1900373783877464066, '工作台配置', 1986681502116294657, 1, 'configuration', 'configuration/index', NULL, 1, 0, 'C', '0', '0', NULL, 'clipboard', 103, 1, '2025-03-14 10:29:52', 1, '2025-11-07 14:28:49', '');
INSERT INTO `sys_menu` VALUES (1902648761729597441, '查询', 1877908356287098881, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:expenseReimbursement:list', '', 103, 1, '2025-03-20 17:09:49', 1, '2025-03-20 17:09:49', '');
INSERT INTO `sys_menu` VALUES (1902648834685321217, '详情', 1877908356287098881, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:expenseReimbursement:query', '', 103, 1, '2025-03-20 17:10:06', 1, '2025-03-20 17:10:06', '');
INSERT INTO `sys_menu` VALUES (1902648939693916162, '修改', 1877908356287098881, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:expenseReimbursement:edit', 'bug', 103, 1, '2025-03-20 17:10:31', 1, '2025-03-20 17:10:31', '');
INSERT INTO `sys_menu` VALUES (1902649051857993730, '删除', 1877908356287098881, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'system:expenseReimbursement:remove', '', 103, 1, '2025-03-20 17:10:58', 1, '2025-03-20 17:10:58', '');
INSERT INTO `sys_menu` VALUES (1952975234688262145, '流程设计表单', 1899741403080712194, 1, '', NULL, NULL, 1, 0, 'F', '0', '0', 'form:form:editor', 'button', 103, 1, '2025-08-06 14:09:15', 1, '2025-08-06 14:09:41', '');
INSERT INTO `sys_menu` VALUES (1986678335609712642, '工作台', 0, 0, 'work', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'build', 103, 1, '2025-11-07 14:13:21', 1, '2025-11-07 14:13:21', '');
INSERT INTO `sys_menu` VALUES (1986679523210113026, '我的审批中心', 1986678335609712642, 3, 'approval', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'documentation', 103, 1, '2025-11-07 14:18:04', 1, '2025-11-07 14:18:04', '');
INSERT INTO `sys_menu` VALUES (1986680818960642050, '公司待办', 1986678335609712642, 1, 'todo', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'eye-open', 103, 1, '2025-11-07 14:23:13', 1, '2025-11-13 13:46:18', '');
INSERT INTO `sys_menu` VALUES (1986681201330171906, '公司审批中心', 1986678335609712642, 6, 'exam', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'job', 103, 1, '2025-11-07 14:24:44', 1, '2025-11-07 14:24:44', '');
INSERT INTO `sys_menu` VALUES (1986681502116294657, '工作流配置', 0, 1, 'workflow', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'clipboard', 103, 1, '2025-11-07 14:25:56', 1, '2025-11-07 14:49:10', '');
INSERT INTO `sys_menu` VALUES (1986682678966693889, '费控配置', 0, 2, 'cost', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'monitor', 103, 1, '2025-11-07 14:30:36', 1, '2025-11-07 14:43:08', '');
INSERT INTO `sys_menu` VALUES (1986682900899901442, '费用类型管理', 1986682678966693889, 0, 'costtype', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'list', 103, 1, '2025-11-07 14:31:29', 1, '2025-11-07 14:31:29', '');
INSERT INTO `sys_menu` VALUES (1986682997578608642, '风控项管理', 1986682678966693889, 1, 'risk', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'log', 103, 1, '2025-11-07 14:31:52', 1, '2025-11-07 14:31:52', '');
INSERT INTO `sys_menu` VALUES (1986683157960404994, '预算配置', 0, 3, 'budget', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'form', 103, 1, '2025-11-07 14:32:30', 1, '2025-11-07 14:43:25', '');
INSERT INTO `sys_menu` VALUES (1986683466728288258, '预算编制', 1986683157960404994, 1, 'aaa', NULL, NULL, 1, 0, 'C', '0', '0', NULL, 'eye-open', 103, 1, '2025-11-07 14:33:44', 1, '2025-11-07 14:35:40', '');
INSERT INTO `sys_menu` VALUES (1986684044757905410, '预算执行', 1986683157960404994, 2, 'bbb', NULL, NULL, 1, 0, 'C', '0', '0', NULL, 'excel', 103, 1, '2025-11-07 14:36:02', 1, '2025-11-07 14:36:02', '');
INSERT INTO `sys_menu` VALUES (1986684112995037186, '预算配置', 1986683157960404994, 3, 'ccc', NULL, NULL, 1, 0, 'C', '0', '0', NULL, 'documentation', 103, 1, '2025-11-07 14:36:18', 1, '2025-11-07 14:36:18', '');
INSERT INTO `sys_menu` VALUES (1986684242536116225, '项目管理', 1986683157960404994, 4, 'ddd', NULL, NULL, 1, 0, 'C', '0', '0', '', 'example', 103, 1, '2025-11-07 14:36:49', 1, '2025-11-07 14:36:49', '');
INSERT INTO `sys_menu` VALUES (1986684582467678210, '统计管理', 0, 4, 'statistics', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'example', 103, 1, '2025-11-07 14:38:10', 1, '2025-11-07 14:44:09', '');
INSERT INTO `sys_menu` VALUES (1986684707969642497, '统计项配置', 1986684582467678210, 1, 'aaa', NULL, NULL, 1, 0, 'C', '0', '0', NULL, 'list', 103, 1, '2025-11-07 14:38:40', 1, '2025-11-07 14:38:40', '');
INSERT INTO `sys_menu` VALUES (1986684892212834305, '报表配置', 1986684582467678210, 1, 'bbbb', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'form', 103, 1, '2025-11-07 14:39:24', 1, '2025-11-07 14:39:24', '');
INSERT INTO `sys_menu` VALUES (1986684959820820482, '图标配置', 1986684582467678210, 2, 'ddd', NULL, NULL, 1, 0, 'C', '0', '0', NULL, 'log', 103, 1, '2025-11-07 14:39:40', 1, '2025-11-07 14:39:40', '');

SET FOREIGN_KEY_CHECKS = 1;
