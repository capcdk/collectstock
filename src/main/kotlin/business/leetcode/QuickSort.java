package business.leetcode;

public class QuickSort {
    private static int count;

    public static void main(String[] args) {
        int[] num = {3, 45, 78, 64, 52, 11, 64, 55, 99, 11, 18};
        System.out.println(arrayToString(num, "未排序"));
        quickSort(num, 0, num.length - 1);
        System.out.println(arrayToString(num, "排序"));
        System.out.println("数组个数：" + num.length);
        System.out.println("循环次数：" + count);

    }

    /**
     * 快速排序
     *
     * @param num 排序的数组
     */
    private static void quickSort(int[] num, int left, int right) {
        if (left >= right) {
            return;
        }
        int base = num[left];

        int i = left, j = right;
        while (i < j) {

            while (num[j] >= base && i < j) {
                j--;
            }
            while (num[i] <= base && i < j) {
                i++;
            }

            if (i < j) {
                num[j] += num[i];
                num[i] = num[j] - num[i];
                num[j] = num[j] - num[i];
            }
        }
        num[left] = num[i];
        num[i] = base;

        quickSort(num, left, i - 1);
        quickSort(num, j + 1, right);
    }

    /**
     * 将一个int类型数组转化为字符串
     */
    private static String arrayToString(int[] arr, String flag) {
        StringBuilder str = new StringBuilder("数组为(" + flag + ")：");
        for (int a : arr) {
            str.append(a).append("\t");
        }
        return str.toString();
    }

}