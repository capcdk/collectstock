package business.leetcode;

/**
 * 最长回文子串
 * Created by Chendk on 2019/1/17
 */
public class _5_Question {

    private String s;
    private int length;
    private int left;
    private int right = 1;
    private int max = 1;

    public String longestPalindrome(String s) {
        this.s = s;
        length = s.length();
        if (length == 0) {
            return "";
        }

        boolean middle = false;
        for (int i = 0; i < length; middle = !middle) {
            isPalindrome(i, middle);
            if (middle) {
                i++;
            }
        }
        return s.substring(left, right);
    }

    private void isPalindrome(int i, boolean middle) {
        int l, r = i + 1;
        if (middle) {
            l = i;
        } else {
            l = i - 1;
        }
        while (l >= 0 && r < length && s.charAt(l) == s.charAt(r)) {
            l--;
            r++;
        }
        int m = r - ++l;
        if (m > max) {
            left = l;
            right = r;
            max = m;
        }
    }
}
