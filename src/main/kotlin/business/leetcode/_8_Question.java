package business.leetcode;

/**
 * 8.字符串转换整数 (atoi)
 */
class _8_Question {

    public static void main(String[] args) {
        System.out.println(myAtoi("+1"));
    }

    public static int myAtoi(String str) {
        char negativeChar = '-';
        char positiveChar = '+';
        char blankChar = ' ';

        char[] chars = str.toCharArray();
        if (chars.length == 0) {
            return 0;
        }
        int length = chars.length;

        int i = 0;
        boolean isNegative = false;
        while (i < length) {
            if (blankChar != chars[i]) {
                if (chars[i] == negativeChar) {
                    isNegative = true;
                    i++;
                } else if (chars[i] == positiveChar) {
                    i++;
                } else if (!Character.isDigit(chars[i])) {
                    return 0;
                }
                break;
            }
            i++;
        }

        if (i == length || !Character.isDigit(chars[i])) {
            return 0;
        }

        long result = 0;
        int tmp;
        do {
            tmp = chars[i] - '0';
            result = (result * 10) + tmp;
            if (isNegative && -result < Integer.MIN_VALUE) {
                return Integer.MIN_VALUE;
            } else if (!isNegative && result > Integer.MAX_VALUE) {
                return Integer.MAX_VALUE;
            }
        } while (++i < length && Character.isDigit(chars[i]));
        int finalResult = (int) result;
        return isNegative ? -finalResult : finalResult;
    }

}