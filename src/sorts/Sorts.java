package sorts;

import java.util.Arrays;

public abstract class Sorts {
	private static BubbleSort bs;
	private static InsertionSort is;
	
	public static final ISort bubbleSort() {
		if(bs == null) {
			bs = new BubbleSort();
		}
		return bs;
	}
	public static final ISort insertionSort() {
		if(is == null) {
			is = new InsertionSort();
		}
		return is;
	}
	
	public static ISort[] getAllSortingAlgos() {
		return new ISort[] {bubbleSort(), insertionSort()};
	}
	
	private Sorts() {}
	public static interface ISort {
		public boolean sortOneIteration(int[] arr);
		public long arrayAccesses();
		public long comparisons();
		public int[] getCurrentComparing();
		public void reset();
	}
	
	private static class BubbleSort implements ISort {
		private int i = 0, j = 0;
		private long aa = 0, cmp = 0;
		public boolean sortOneIteration(int[] arr) {
			int n = arr.length;
			
			if (arr[j] > arr[j + 1]) {
				swap(arr, j + 1, j);
				aa += 2 + 4; // if and swap();
				cmp += 1; // if (idk if for loops count)
			}
			
			j++;
			if(!(j < n - i - 1)) {i++; j = 0;}
			if(!(i < n - 1)) {
				return true;
			}
			
			return false;
		}
		public long arrayAccesses() {
			return aa;
		}
		public long comparisons() {
			return cmp;
		}
		public void reset() {
			i = 0; j = 0; aa = 0; cmp = 0;
		}
		public int[] getCurrentComparing() {
			return new int[] {j, j + 1};
		}
		public String toString() {
			return this.getClass().getSimpleName();
		}
	}
	
	private static class InsertionSort implements ISort {
		
		private int j = 1, i = 0, key = 0;
		boolean whilee = true;
		
		private long aa = 0, cmp = 0;
		public boolean sortOneIteration(int[] array) {
			if(whilee) {
				key = array[j];
				i = j - 1;
				
				aa++;
			}
			whilee = false;
			
			if(i >= 0 && key < array[i]) {
				array[i + 1] = array[i];
				i--;
				
				cmp++;
				aa += 3;
				return false;
			}
			whilee = true;
			
			array[i + 1] = key;
			aa++;
			
			j++;
			if (j < array.length) {
				return false;
			} else {
				return true;
			}
		}
		public long arrayAccesses() {
			return aa;
		}
		public long comparisons() {
			return cmp;
		}
		public void reset() {
			j = 1; i = 0; key = 0; whilee = true;
			aa = 0; cmp = 0;
		}
		public String toString() {
			return this.getClass().getSimpleName();
		}
		public int[] getCurrentComparing() {
			System.out.println("j - "+j);
			System.out.println("i - "+i);
			return new int[] {j, i};
		}
	}
	
	private static void swap(int[] arr, int ind1, int ind2) {
		int tmp = arr[ind1];
		arr[ind1] = arr[ind2];
		arr[ind2] = tmp;
	}
}
