package com.order.service;

import java.util.Arrays;
import java.util.Vector;

import static java.lang.Math.min;

public class Test {


    public static String removeDigit(String number, char digit) {
        int min_id = -1, last_id = -1;
        boolean isNegative = false;
        if (number.charAt(0) == '-') {
            isNegative = true;
        }
        for (int i = 0; i < number.length(); i++) {
            if (!isNegative) {
                if (number.charAt(i) == digit) {
                    if (i < number.length() - 1 && number.charAt(i) < number.charAt(i + 1)) {
                        min_id = i;
                        break;
                    }
                    last_id = i;
                }
            } else {

                if (number.charAt(i) == digit) {
                    if (i < number.length() - 1 && number.charAt(i) > number.charAt(i + 1)) {
                        min_id = i;
                        break;
                    }
                    last_id = i;
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append(number);
        if (min_id == -1)
            sb = sb.deleteCharAt(last_id);
        else
            sb = sb.deleteCharAt(min_id);
        return sb.toString();
    }

    // Driver code
    public static void main(String[] args) {
        int[] A = {1, 1, 1, 0, 1, 1};

        int nbPairMax = 0;
        System.out.println(nbPair(A));

    }

    private static int nbPair(int[] A) {
        int count1 = 0;
        int count2 = 0;
        if (A.length > 0) {
            for (int i = 0; i < A.length; i++) {
                if (i % 2 == 0) {
                    if (A[i] == 1)
                        count1 += 1;
                    if (A[i] == 0)
                        count2 += 1;
                } else {
                    if (A[i] == 0)
                        count1 += 1;
                    if (A[i] == 1)
                        count2 += 1;
                }
            }

        }
        return min(count2, count1);
    }
}
