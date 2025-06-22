/*
 * Copyright 2023 Marc Liberatore.
 */

package heaps;

import java.util.Arrays;
import java.util.Random;

public class HeapUtilities {
    /**
     * Returns true iff the subtree of a starting at index i is a max-heap.
     * @param a an array representing a mostly-complete tree, possibly a heap
     * @param i an index into that array representing a subtree rooted at i
     * @return true iff the subtree of a starting at index i is a max-heap
     */
    static boolean isHeap(double[] a, int i) {
        if (!hasLeft(a, i)) {
            return true;
        }
        if (a[i] > a[left(i)] && a[i] > a[right(i)]) {
            return isHeap(a, left(i)) && isHeap(a, right(i));
        }
        return false;
    }
    static int parent(int i) {
        return (i - 1) / 2;
    }
    static int left(int i) {
        return 2 * i + 1;
    }
    static int right(int i) {
        return 2 * i + 2;
    }
    static boolean hasLeft(double[] a, int i) {
        return left(i) < a.length;
    }
    static boolean hasRight(double[] a, int i) {
        return right(i) < a.length;
    }
    static void swap(double[] a, int i, int j) {
        double t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    /**
     * Perform the heap siftdown operation on index i of the array a. 
     * 
     * This method assumes the subtrees of i are already valid max-heaps.
     * 
     * This operation is bounded by n (exclusive)! In a regular heap, 
     * n = a.length, but in some cases (for example, heapsort), you will 
     * want to stop the sifting at a particular position in the array. 
     * siftDown should stop before n, in other words, it should not 
     * sift down into any index great than (n-1).
     * 
     * @param a the array being sifted
     * @param i the index of the element to sift down
     * @param n the bound on the array (that is, where to stop sifting)
     */
    static void siftDown(double[] a, int i, int n) {
        while (true) {
            if (!hasLeft(a, i)) {
                System.out.println("doesn't have anything *left* to do");
                return;
            }
            int largestIndex = i;
            System.out.println("Left - " + Integer.toString(left(i)));
            System.out.println("Right - " + Integer.toString(right(i)));
            if (left(i) < n && a[left(i)] > a[largestIndex]) {
                System.out.println("doing the left thing");
                largestIndex = left(i);
            }
            if (hasRight(a, i)) {
                if (right(i) < n && a[right(i)] > a[largestIndex]) {
                    System.out.println("doing the right thing");
                    largestIndex = right(i);
                }
            }
            if (largestIndex == i) {
                System.out.println("It is finished");
                return;
            }
            System.out.println("Swapping " + Integer.toString(i) + " and " + Integer.toString(largestIndex));
            swap(a, i, largestIndex);
            i = largestIndex;
        }
    }
    

    /**
     * Heapify the array a in-place in linear time as a max-heap.
     * @param a an array of values
     */
    static void heapify(double[] a) {
        for (int i = a.length - 1; i >= 0; i--) {
            if (hasLeft(a, i)) {
                if (isHeap(a, left(i)) && isHeap(a, right(i))) {
                    siftDown(a, i, a.length);
                }
            }
        }
    }

    /**
     * Heapsort the array a in-place, resulting in the elements of
     * a being in ascending order.
     * @param a
     */
    static void heapSort(double[] a) {
        heapify(a);
        for (int i = a.length - 1; i >= 0; i--) {
            swap(a, 0, i);
            siftDown(a, 0, i);
        }
    }
    
    public static void main(String[] args) {
        Random r = new Random(0);
        int length = 15;
        double[] l = new double[length];
        for (int i = 0; i < length; i++) {
            l[i] = r.nextInt(20);
        }
        System.out.println(Arrays.toString(l));

        heapify(l);

        System.out.println(Arrays.toString(l));

        heapSort(l);

        System.out.println(Arrays.toString(l));
    }
}
