package com.cddgg.base.arithmetic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 回溯算法
 * 
 * @author ldd
 * 
 */
public class BacktrackArithmetic {

    /**
     * 
     * @param moneys
     *            金额数组
     * @param indexs
     *            匹配下标数组
     * @param val
     *            目标金额
     * @param num
     *            最大匹配个数(当为-1时，不限制)
     * @return 匹配金额+个数
     */
    private static Object[] sum(double[] moneys, int[] indexs, double val,
            int num) {

        int valid = 0;
        double sum = 0d;

        for (int i = 0; i < indexs.length && sum < val
                && (num == -1 || valid < num); i++) {
            if (indexs[i] == 1) {
                sum += moneys[i];
                valid++;
            }
        }

        return new Object[] { sum, valid };
    }

    /**
     * 
     * @param moneys
     *            待计算数组
     * @param val
     *            期望匹配值
     * @param num
     *            匹配长度
     * @param size
     *            最大返回条数
     * @return 匹配下标
     */
    public static List<int[]> exec(double[] moneys, double val, int num,
            int size) {

        int[] indexs = new int[moneys.length];
        for (int i = 0; i < moneys.length; i++) {
            indexs[i] = -1;
        }

        int k = 0;
        Object[] sum;
        List<int[]> list = new ArrayIntList();

        while (k >= 0) {

            while (indexs[k] <= 0) {

                indexs[k]++;

                sum = sum(moneys, indexs, val, num);

                if (((Double) sum[0] == val) && (k <= indexs.length - 1)
                        && (num == -1 || (Integer) sum[1] == num)) {

                    list.add(indexs);
                    if (-1 != size && list.size() >= size) {
                        return list;
                    }

                } else if (((Double) sum[0] < val) && (k < indexs.length - 1)
                        && (num == -1 || (Integer) sum[1] < num)) {

                    k++;
                }
            }

            indexs[k] = -1;
            k--;
        }
        return list;
    }

    /**
     * 
     * @param moneys
     *            带计算数组
     * @param val
     *            期望匹配值
     * @return 匹配下标
     */
    public static List<int[]> exec(double[] moneys, double val) {
        return exec(moneys, val, -1, -1);
    }

}

/**
 * 自定义int数组集合
 * 
 * @author ldd
 * 
 */
class ArrayIntList extends ArrayList<int[]> {

    /**
     * 版本标示
     */
    private static final long serialVersionUID = 1L;

    /**
     * Returns the index of the first occurrence of the specified element in
     * this list, or -1 if this list does not contain the element. More
     * formally, returns the lowest index <tt>i</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
     * or -1 if there is no such index.
     * 
     * @param o
     *            目标对象
     * @return 匹配下标
     */
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < this.size(); i++) {
                if (get(i) == null) {
                    return i;
                }
            }

        } else {
            for (int i = 0; i < this.size(); i++) {
                if (Arrays.equals((int[]) get(i), (int[]) o)) {
                    return i;
                }
            }

        }
        return -1;
    }

}
