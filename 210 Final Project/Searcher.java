/*
 * Copyright 2023 Marc Liberatore.
 */

package search;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Map;

import mazes.MazeGenerator;
import mazes.Maze;
import mazes.Cell;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * An implementation of a Searcher that performs an iterative search,
 * storing the list of next states in a Queue. This results in a
 * breadth-first search.
 * 
 * @author liberato
 *
 * @param <T> the type for each vertex in the search graph
 */
public class Searcher<T> {
	private final SearchProblem<T> searchProblem;
	
	/**
	 * Instantiates a searcher.
	 * 
	 * @param searchProblem
	 *            the search problem for which this searcher will find and
	 *            validate solutions
	 */
	public Searcher(SearchProblem<T> searchProblem) {
		this.searchProblem = searchProblem;
	}

	/**
	 * Finds and return a solution to the problem, consisting of a list of
	 * states.
	 * 
	 * The list should start with the initial state of the underlying problem.
	 * Then, it should have one or more additional states. Each state should be
	 * a successor of its predecessor. The last state should be a goal state of
	 * the underlying problem.
	 * 
	 * If there is no solution, then this method should return an empty list.
	 * 
	 * @return a solution to the problem (or an empty list)
	 */
	public List<T> findSolution() {		
		// TODO
		List<T> solutionList = new LinkedList<>();

		T start = searchProblem.getInitialState();
		Queue<T> queue = new LinkedList<>();
		queue.add(start);

		Map<T, T> predecessor = new HashMap<>();
		predecessor.put(start, null);

		while (!queue.isEmpty()) {
			T current = queue.remove();
			
			if (searchProblem.isGoal(current)) {
				solutionList.add(current);
				T previous = predecessor.get(current);
				while (previous != null) {
					solutionList.add(0, previous);
					previous = predecessor.get(previous);
				}
				return solutionList;
			}

			for (T next : searchProblem.getSuccessors(current)) {
				if (!predecessor.containsKey(next)) {
					queue.add(next);
					predecessor.put(next, current);
				}
			}
		}
		return solutionList;
	}

	/**
	 * Checks that a solution is valid.
	 * 
	 * THIS METHOD DOES NOT PERFORM SEARCH! It only checks if a provided solution
	 * is valid!
	 * 
	 * A valid solution consists of a list of states. The list should start with
	 * the initial state of the underlying problem. Then, it should have one or
	 * more additional states. Each state should be a successor of its
	 * predecessor. The last state should be a goal state of the underlying
	 * problem.
	 * 
	 * @param solution
	 * @return true iff this solution is a valid solution
	 * @throws NullPointerException
	 *             if solution is null
	 */
	public final boolean isValidSolution(List<T> solution) throws NullPointerException {
		// TODO
		if (solution == null) {
			throw new NullPointerException();
		}
		if (solution.size() == 0) {
			return false;
		}
		if (!searchProblem.isGoal(solution.get(solution.size()-1))) {
			return false;
		}
		if (!searchProblem.getInitialState().equals(solution.get(0))) {
			return false;
		}
		for (int i = 0; i < solution.size() - 1; i++) {
			if (!searchProblem.getSuccessors(solution.get(i)).contains(solution.get(i + 1))) {
				return false;
			}
		}
		return true;
	}
	public static void main(String[] args) {
		Maze maze = new MazeGenerator(3, 3, 2).generateDfs();
		Searcher<Cell> searcher = new Searcher<Cell>(maze);
		List<Cell> solution = new ArrayList<Cell>();
		solution.add(new Cell(1, 0));
		solution.add(new Cell(0, 0));
		solution.add(new Cell(0, 1));
		solution.add(new Cell(0, 2));
		solution.add(new Cell(1, 2));
		System.out.println(searcher.isValidSolution(solution));				

	}
}
