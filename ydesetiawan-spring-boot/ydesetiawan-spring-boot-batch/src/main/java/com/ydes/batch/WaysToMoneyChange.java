package com.ydes.batch;

/**
 * @author edys
 * @version 1.0, Jan 21, 2017
 * @since
 */
public class WaysToMoneyChange {

	public static int change(int[] v, int m) {
		int[][] result = new int[v.length + 1][m + 1];

		for (int i = 0; i <= v.length; i++) {
			result[i][0] = 1;
		}

		for (int i = 1; i <= m; i++) {
			result[0][i] = 0;
		}

		for (int i = 1; i <= v.length; i++) {
			for (int j = 1; j <= m; j++) {
				if (v[i - 1] <= j) {
					result[i][j] = result[i - 1][j] + result[i][j - v[i - 1]];
				} else {
					result[i][j] = result[i - 1][j];
				}
			}
		}
		return result[v.length][m];
	}

	public static void main(String[] args) {
		int m = 10;
		int[] val = { 2, 5, 3, 6 };
		System.out.println("Result for m : " + 10 + " is : (" + change(val, m)
				+ ")");
	}

}
