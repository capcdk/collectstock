package business.leetcode;

/**
 * 寻找两个有序数组的中位数
 *
 * 思路：单个有值直接取中位数；两个均有值时：算两组长度总和，确认中位值下标，逐个
 * poll出来个两组游标位值，值小的数组游标进1，直到取到中位值下标=游标时的值。
 * Created by Chendk on 2019/1/16
 */
public class _4_Question {

    public static void main(String[] args) {

    }

    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        if (nums1.length <= 0) {
            return findMedian(nums2);
        } else if (nums2.length <= 0) {
            return findMedian(nums1);
        } else {
            return findMedian(nums1, nums2);
        }
    }

    private double findMedian(int[] nums1, int[] nums2) {
        int len1 = nums1.length;
        int len2 = nums2.length;

        int i1 = 0, i2 = 0, i = 0, iv = 0;
        double median;
        int mi = (len1 + len2) / 2;
        if ((len1 + len2) % 2 == 0) {
            while (i < mi) {
                if (i1 >= len1) {
                    iv = nums2[i2++];
                } else if (i2 >= len2) {
                    iv = nums1[i1++];
                } else {
                    if (nums1[i1] > nums2[i2]) {
                        iv = nums2[i2++];
                    } else {
                        iv = nums1[i1++];
                    }
                }
                i++;
            }
            int ivnext;
            if (i1 >= len1) {
                ivnext = nums2[i2];
            } else if (i2 >= len2) {
                ivnext = nums1[i1];
            } else {
                ivnext = Math.min(nums1[i1], nums2[i2]);
            }
            median = (iv + ivnext) / 2.0;
        } else {
            while (i <= mi) {
                if (i1 >= len1) {
                    iv = nums2[i2++];
                } else if (i2 >= len2) {
                    iv = nums1[i1++];
                } else {
                    if (nums1[i1] > nums2[i2]) {
                        iv = nums2[i2++];
                    } else {
                        iv = nums1[i1++];
                    }
                }
                i++;
            }
            median = iv;
        }
        return median;
    }

    private double findMedian(int[] nums) {
        int mi = nums.length / 2;
        if (nums.length % 2 == 0) {
            return (nums[mi] + nums[mi - 1]) / 2.0;
        } else {
            return nums[mi];
        }
    }
}
