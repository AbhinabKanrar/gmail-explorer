package com.mabsisa;

public class App {
	static void combinationUtil(char arr[], char data[], int start, int end, int index, int r) {
		if (index == r) {
			for (int j = 0; j < r; j++) {
				System.out.print(data[j]);	
			}
			return;
		}

		for (int i = start; i <= end && end - i + 1 >= r - index; i++) {
			data[index] = arr[i];
			combinationUtil(arr, data, i + 1, end, index + 1, r);
		}
	}

	static void printCombination(char arr[], int n, int r) {
		char data[] = new char[r];
		combinationUtil(arr, data, 0, n - 1, 0, r);
	}

	public static void main(String[] args) {
		char arr[] = {'a', 'b', 'c', 'd', 'e','f','g'};
		int r = 5;
		int n = arr.length;
		printCombination(arr, n, r);
	}
}
