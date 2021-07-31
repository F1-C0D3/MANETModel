package de.algorithmhelper;

import java.util.ArrayList;
import java.util.List;

public class Selection {

    public Selection() {
    }

    public static List<List<Integer>> allCombination(int[] elements) {
	List<List<Integer>> result = new ArrayList<List<Integer>>();
	int[] indexes = new int[elements.length];
	for (int i = 0; i < elements.length; i++) {
	    indexes[i] = 0;
	}

	toListL(elements, result);

	int i = 0;
	while (i < elements.length) {
	    if (indexes[i] < i) {
		swap(elements, i % 2 == 0 ? 0 : indexes[i], i);
		toListL(elements, result);
		indexes[i]++;
		i = 0;
	    } else {
		indexes[i] = 0;
		i++;
	    }
	}
	return result;
    }

    private static void swap(int[] input, int a, int b) {
	int tmp = input[a];
	input[a] = input[b];
	input[b] = tmp;
    }

    private static void toListL(int[] input, List<List<Integer>> l) {
	List<Integer> inner = new ArrayList<Integer>();
	for (int i = 0; i < input.length; i++) {
	    inner.add(input[i]);
	}
	l.add(inner);
    }

    private static void printArray(int[] input) {
	System.out.println("\n");
	for (Integer integer : input) {
	    System.out.print(integer + ",");
	}

    }

}
