/*
 Navicat Premium Dump SQL

 Source Server         : 报销线上
 Source Server Type    : MySQL
 Source Server Version : 80405
 Source Schema         : dangan_new

 Target Server Type    : MySQL
 Target Server Version : 80405
 File Encoding         : 65001

 Date: 2026-03-16
 Description: 同步 dangan_gjzx 的字段变更到 dangan_new
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =====================================================
-- 为 sys_tenant 表添加缺失的字段
-- =====================================================
ALTER TABLE `sys_tenant`
ADD COLUMN `environment_name` varchar(255) NULL DEFAULT '会计档案管理系统' COMMENT '系统名称' AFTER `update_time`,
ADD COLUMN `background_image` varchar(255) NULL DEFAULT '会计背景图.jpg' COMMENT '背景图' AFTER `environment_name`,
ADD COLUMN `label_image` varchar(255) NULL DEFAULT '会计标签logo.png' COMMENT '标签图' AFTER `background_image`;

-- =====================================================
-- 为 sys_user_role 表添加 weight 字段
-- =====================================================
ALTER TABLE `sys_user_role`
ADD COLUMN `weight` int NOT NULL DEFAULT 1 COMMENT '审批权重(1-100)，越大优先' AFTER `role_id`;

-- =====================================================
-- 为 test_expense_reimbursement 表添加缺失的字段
-- =====================================================
ALTER TABLE `test_expense_reimbursement`
ADD COLUMN `audit_detail` text NULL COMMENT '资金渠道' AFTER `receipt_url`,
ADD COLUMN `contract_id` bigint NULL COMMENT '合同id' AFTER `audit_detail`,
ADD COLUMN `node_id` varchar(255) NULL COMMENT '节点id' AFTER `contract_id`;

SET FOREIGN_KEY_CHECKS = 1;
