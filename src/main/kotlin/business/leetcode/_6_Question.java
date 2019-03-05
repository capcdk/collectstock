package business.leetcode;

/**
 * 6.Z 字形变换
 */
class _6_Question {

    public String convert(String s, int numRows) {
        int length = s.length();

        if (length <= 0) {
            return "";
        }
        if (numRows == 1) {
            return s;
        }

        int halfZLength = numRows * 2 - 2;
        int halfZX = numRows - 1;
        boolean isSingleX = halfZX == 1;

        char[] sArr = s.toCharArray();
        char[] result = new char[length];
        int resultIndex = 0;

        int index;
        int halfZCnt;
        int halfZRemainder;
        int remainder = length % halfZLength;
        int xLength = (length / halfZLength) * halfZX + (remainder <= numRows ? (remainder == 0 ? 0 : 1) : remainder - numRows + 1);

        for (int y = 1; y <= numRows; y++) {
            for (int x = 1; x <= xLength; x++) {
                halfZCnt = isSingleX ? x - 1 : x / halfZX;
                halfZRemainder = x % halfZX;

                if (halfZRemainder == 0) {
                    if (!isSingleX && y != 2) {
                        continue;
                    }
                    index = halfZCnt * halfZLength + (isSingleX ? y : 0);
                } else {
                    if (halfZRemainder > 1 && (numRows - halfZRemainder + 1) != y) {
                        continue;
                    }
                    index = halfZCnt * halfZLength + (halfZRemainder > 1 ? numRows + halfZRemainder - 1 : y);
                }
                if (index - 1 < length) {
                    result[resultIndex++] = sArr[index - 1];
                }
            }
        }
        return new String(result);
    }

}