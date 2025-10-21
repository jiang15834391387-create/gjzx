package org.smartlink.business.util;

import java.math.BigDecimal;

/**
 * 金额转大写工具类
 * 支持整数、小数和负数的转换
 */
public class MoneyToChineseUtil {

    // 大写数字
    private static final String[] CN_NUMBERS = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};

    // 整数部分单位
    private static final String[] CN_INTEGER_UNITS = {"", "拾", "佰", "仟"};

    // 小数部分单位
    private static final String[] CN_DECIMAL_UNITS = {"角", "分"};

    // 整数部分扩展单位
    private static final String[] CN_EXPANDED_UNITS = {"", "万", "亿", "兆"};

    /**
     * 将金额转换为中文大写形式
     * @param money 金额数值
     * @return 中文大写金额字符串
     */
    public static String convert(BigDecimal money) {
        if (money == null) {
            throw new IllegalArgumentException("金额不能为null");
        }

        // 处理零值
        if (BigDecimal.ZERO.compareTo(money) == 0) {
            return "零元整";
        }

        // 处理负数
        boolean isNegative = money.compareTo(BigDecimal.ZERO) < 0;
        if (isNegative) {
            money = money.abs();
        }

        // 转换为字符串并分割整数和小数部分
        String moneyStr = money.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        String[] parts = moneyStr.split("\\.");
        String integerPart = parts[0];
        String decimalPart = parts.length > 1 ? parts[1] : "00";

        // 转换整数部分
        String integerChinese = convertIntegerPart(integerPart);

        // 转换小数部分
        String decimalChinese = convertDecimalPart(decimalPart);

        // 组合结果
        StringBuilder result = new StringBuilder();
        if (isNegative) {
            result.append("负");
        }
        result.append(integerChinese);
        if (!decimalChinese.isEmpty()) {
            result.append(decimalChinese);
        } else {
            result.append("整");
        }

        return result.toString();
    }

    /**
     * 转换整数部分
     */
    private static String convertIntegerPart(String integerPart) {
        if ("0".equals(integerPart)) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        int length = integerPart.length();

        // 按四位一组处理
        for (int i = 0; i < length; i++) {
            int digit = integerPart.charAt(i) - '0';
            int position = length - i - 1;
            int groupIndex = position / 4;
            int unitIndex = position % 4;

            // 添加数字
            if (digit != 0 || shouldAddZero(integerPart, i)) {
                result.append(CN_NUMBERS[digit]);
            }

            // 添加单位（拾、佰、仟）
            if (digit != 0 && unitIndex > 0) {
                result.append(CN_INTEGER_UNITS[unitIndex]);
            }

            // 添加扩展单位（万、亿、兆）
            if (unitIndex == 0 && groupIndex > 0) {
                result.append(CN_EXPANDED_UNITS[groupIndex]);
            }
        }

        return result.append("元").toString();
    }

    /**
     * 判断是否需要添加"零"
     */
    private static boolean shouldAddZero(String integerPart, int currentIndex) {
        if (currentIndex == 0) {
            return false;
        }

        int currentDigit = integerPart.charAt(currentIndex) - '0';
        int prevDigit = integerPart.charAt(currentIndex - 1) - '0';

        // 当前位是0，前一位不是0，且不是组的最后一位
        return currentDigit == 0 && prevDigit != 0 && (integerPart.length() - currentIndex) % 4 != 1;
    }

    /**
     * 转换小数部分
     */
    private static String convertDecimalPart(String decimalPart) {
        StringBuilder result = new StringBuilder();
        boolean hasJiao = false;
        boolean hasFen = false;

        // 处理角
        if (decimalPart.length() > 0) {
            int jiao = decimalPart.charAt(0) - '0';
            if (jiao != 0) {
                result.append(CN_NUMBERS[jiao]).append(CN_DECIMAL_UNITS[0]);
                hasJiao = true;
            }
        }

        // 处理分
        if (decimalPart.length() > 1) {
            int fen = decimalPart.charAt(1) - '0';
            if (fen != 0) {
                result.append(CN_NUMBERS[fen]).append(CN_DECIMAL_UNITS[1]);
                hasFen = true;
            }
        }

        // 如果角分都有，直接返回；如果只有角，不需要特殊处理
        if (!hasJiao && !hasFen) {
            return "";
        }

        return result.toString();
    }

    /**
     * 重载方法：转换double类型的金额
     */
    public static String convert(double money) {
        return convert(BigDecimal.valueOf(money));
    }

    /**
     * 重载方法：转换String类型的金额
     */
    public static String convert(String money) {
        return convert(new BigDecimal(money));
    }
}
