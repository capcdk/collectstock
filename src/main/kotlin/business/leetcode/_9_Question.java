package business.leetcode;

/**
 * 9.回文数
 */
class _9_Question {

    public static void main(String[] args) {
        System.out.println(isPalindrome(0));
    }

    public static boolean isPalindrome(int x) {
        if (x < 0) {
            return false;
        }
        String s = String.valueOf(x);
        char[] chars = s.toCharArray();
        int length = chars.length;
        if (length == 1) {
            return true;
        }

        int l = 0, r = length - 1;
        while (l <= r && chars[l] == chars[r]) {
            l++;
            r--;
        }
        return l >= r;
    }
}