package org.sedaq.scp.service.utils;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Pavel Seda
 */
@Service
public class PrintUtility {

    public static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.printf("%2s", matrix[i][j]);
            }
            System.out.println();
        }
    }

    public void printMap(Map<Integer, List<Integer>> map) {
        map.forEach((key, value) -> {
            System.out.println("Nezbytny radek je: " + key + " a sloupce, ktere obsahuje: " + value.toString());
        });
    }

}
