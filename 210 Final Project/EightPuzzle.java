/*
 * Copyright 2023 Marc Liberatore.
 */

package puzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

import search.SearchProblem;
import search.Searcher;

/**
 * A class to represent an instance of the eight-puzzle.
 * 
 * The spaces in an 8-puzzle are indexed as follows:
 * 
 * 0 | 1 | 2
 * --+---+---
 * 3 | 4 | 5
 * --+---+---
 * 6 | 7 | 8
 * 
 * The puzzle contains the eight numbers 1-8, and an empty space.
 * If we represent the empty space as 0, then the puzzle is solved
 * when the values in the puzzle are as follows:
 * 
 * 1 | 2 | 3
 * --+---+---
 * 4 | 5 | 6
 * --+---+---
 * 7 | 8 | 0
 * 
 * That is, when the space at index 0 contains value 1, the space 
 * at index 1 contains value 2, and so on.
 * 
 * From any given state, you can swap the empty space with a space 
 * adjacent to it (that is, above, below, left, or right of it,
 * without wrapping around).
 * 
 * For example, if the empty space is at index 2, you may swap
 * it with the value at index 1 or 5, but not any other index.
 * 
 * Only half of all possible puzzle states are solvable! See:
 * https://en.wikipedia.org/wiki/15_puzzle
 * for details.
 * 

 * @author liberato
 *
 */
public class EightPuzzle implements SearchProblem<List<Integer>> {
	List<Integer> puzzle;
	/**
	 * Creates a new instance of the 8 puzzle with the given starting values.
	 * 
	 * The values are indexed as described above, and should contain exactly the
	 * nine integers from 0 to 8.
	 * 
	 * @param startingValues
	 *            the starting values, 0 -- 8
	 * @throws IllegalArgumentException
	 *             if startingValues is invalid
	 */
	public EightPuzzle(List<Integer> startingValues) throws IllegalArgumentException {
		if (startingValues.size() == 9) {
			for (int i = 0; i < 9; i++) {
				if (!startingValues.contains(i)) {
					throw new IllegalArgumentException();
				}
			}
			puzzle = startingValues;
		}
		else {
			throw new IllegalArgumentException();
		}

	}

	@Override
	public List<Integer> getInitialState() {
		// TODO
		return puzzle;
	}
	static List<Integer> swap(List<Integer> list, int i, int j) {
		Integer[] newList = new Integer[9];
		for (int k = 0; k < 9; k++) {
			newList[k] = list.get(k);
		}
		int t = newList[i];
        newList[i] = newList[j];
        newList[j] = t;
		return Arrays.asList(newList);
	}
	@Override
	public List<List<Integer>> getSuccessors(List<Integer> currentState) {
		// TODO
		List<List<Integer>> successors = new ArrayList<>();
		int zero = 0;
		for (int i = 0; i < 9; i++) {
			if (currentState.get(i) == 0) {
				zero = i;
				break;
			}
			
		}
		if (zero == 0) {
			List<Integer> successor1 = swap(currentState, 0, 1);
			List<Integer> successor2 = swap(currentState, 0, 3);

			successors.add(successor1);
			successors.add(successor2);
		}
		if (zero == 1) {
			List<Integer> successor1 = swap(currentState, 1, 0);
			List<Integer> successor2 = swap(currentState, 1, 2);
			List<Integer> successor3 = swap(currentState, 1, 4);
			successors.add(successor1);
			successors.add(successor2);
			successors.add(successor3);
		}
		if (zero == 2) {
			List<Integer> successor1 = swap(currentState, 2, 1);
			List<Integer> successor2 = swap(currentState, 2, 5);
			successors.add(successor1);
			successors.add(successor2);
		}
		if (zero == 3) {
			List<Integer> successor1 = swap(currentState, 3, 0);
			List<Integer> successor2 = swap(currentState, 3, 4);
			List<Integer> successor3 = swap(currentState, 3, 6);
			successors.add(successor1);
			successors.add(successor2);
			successors.add(successor3);
		}
		if (zero == 4) {
			List<Integer> successor1 = swap(currentState, 4, 1);
			List<Integer> successor2 = swap(currentState, 4, 3);
			List<Integer> successor3 = swap(currentState, 4, 5);
			List<Integer> successor4 = swap(currentState, 4, 7);
			successors.add(successor1);
			successors.add(successor2);
			successors.add(successor3);
			successors.add(successor4);
		}
		if (zero == 5) {
			List<Integer> successor1 = swap(currentState, 5, 2);
			List<Integer> successor2 = swap(currentState, 5, 4);
			List<Integer> successor3 = swap(currentState, 5, 8);
			successors.add(successor1);
			successors.add(successor2);
			successors.add(successor3);
		}
		if (zero == 6) {
			List<Integer> successor1 = swap(currentState, 6, 3);
			List<Integer> successor2 = swap(currentState, 6, 7);
			successors.add(successor1);
			successors.add(successor2);
		}
		if (zero == 7) {
			List<Integer> successor1 = swap(currentState, 7, 4);
			List<Integer> successor2 = swap(currentState, 7, 6);
			List<Integer> successor3 = swap(currentState, 7, 8);
			successors.add(successor1);
			successors.add(successor2);
			successors.add(successor3);
		}
		if (zero == 8) {
			List<Integer> successor1 = swap(currentState, 8, 5);
			List<Integer> successor2 = swap(currentState, 8, 7);
			successors.add(successor1);
			successors.add(successor2);
		}
		return successors;
	}


	@Override
	public boolean isGoal(List<Integer> state) {
		// TODO
		if (state.size() == 9) {
			if (state.get(8) != 0) {
				return false;
			}
			for (int i = 1; i < 8; i++) {
				if (state.get(i - 1) != i) {
					return false;
				}
			}
		}
		return true;
	}

	public static void main(String[] args) {
		EightPuzzle eightPuzzle = new EightPuzzle(Arrays.asList(new Integer[] {3, 1, 2, 5, 6, 4, 8, 7, 0}));
		List<List<Integer>> solution = new Searcher<List<Integer>>(eightPuzzle).findSolution();
		System.out.println(new Searcher<List<Integer>>(eightPuzzle).isValidSolution(solution));
		for (List<Integer> state : solution) {
			System.out.println(state);
		}
		System.out.println(solution.size() + " states in solution");
	}
}
