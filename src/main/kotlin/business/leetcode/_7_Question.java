package business.leetcode;

/**
 * 7.整数反转
 */
class _7_Question {

    public static void main(String[] args) {
        System.out.println('9'-'0');
    }

    public static int reverse(int x) {
        boolean isNegative = x < 0;
        String xStr = String.valueOf(isNegative ? -(long) x : x);
        char[] chars = xStr.toCharArray();
        StringBuilder sb = new StringBuilder(isNegative ? "-" : "");
        for (int charsLength = chars.length, i = charsLength - 1; i >= 0; i--) {
            sb.append(chars[i]);
        }
        String resultStr = sb.toString();
        if (resultStr.isEmpty()) {
            return 0;
        }
        Long result = Long.valueOf(resultStr);
        return result > Integer.MAX_VALUE || result < (Integer.MIN_VALUE + 1) ? 0 : Integer.valueOf(resultStr);
    }

}