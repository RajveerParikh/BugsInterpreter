package bugs;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import tree.Tree;


/**
 * Interpreter class for Bugs Program. 
 * Creates an interpreter object and interprets its various children
 * @author Rajveer Parikh
 * @version March 2015
 */
public class Interpreter extends Thread {
	Tree<Token> allbugs;
	HashMap<String, Bugs> bugs;
	HashMap<String, Double> variables;
	HashMap<String, Tree<Token>> functions;
	private int numberOfBugs; 
	Tree<Token> program; 
	Bugs bugInterpreter;
	ArrayList<Command> listOfCommands; 

	Interpreter(Tree<Token> tree){

		bugs = new HashMap<String, Bugs>();
		variables = new HashMap<String, Double>();
		functions = new HashMap<String, Tree<Token>>();
		allbugs = tree.getChild(0);
		program = tree;
		listOfCommands = new ArrayList<Command>();
		

		if (allbugs.getChild(0).getNumberOfChildren() > 0) {
			interpretAllbugs(allbugs.getChild(0));		
		}
		if (allbugs.getChild(1).getNumberOfChildren() > 0) {
			interpretAllbugs(allbugs.getChild(1));			
		}
		interpretBug(tree.getChild(1));
	}
	
	/**
	 * Interprets the allbugs children of the program 
	 * @param tree Allbugs tree
	 */
	public void interpretAllbugs(Tree<Token> tree){
		String root = tree.getValue().value;
		switch(root){
		case("list"): 
			for (int i = 0; i <tree.getNumberOfChildren(); i++){
				interpretAllbugs(tree.getChild(i));
			}
		case("function"):

			this.functions.put(tree.getChild(0).getChild(0).getValue().value, tree.getChild(0));

		break;
		case("var"):
			for (int i = 0; i < tree.getChild(0).getNumberOfChildren(); i++){
				String child9 = tree.getChild(0).getChild(i).getValue().value; 
				this.variables.put(child9, 0.0);
			}
		break;
		}	
	}

	/**
	 * Interprets the bug children of the program 
	 * @param tree Allbugs tree
	 */
	public void interpretBug(Tree<Token> tree){
		for (int i = 0; i < tree.getNumberOfChildren(); i++){
			Bugs bug = new Bugs(this, tree.getChild(i));
			bugs.put(tree.getChild(i).getChild(0).getValue().value, bug);
			bug.setBlocked(true);
		}
	}

	/**
	 * Store variables belonging to allbugs in hashmap
	 * @param variable The variable to be stored.
	 * @param value The value to store with variable as key in the HashMap
	 */
	void store(String variable, double value){
		if (variables.containsKey(variable)){
			variables.put(variable, value);
		}
		else{
			throw new RuntimeException("Variable not declared");
		}
	}

	/**
	 * Searches the variables hashmap to find value of variable
	 * @param variable The variable who's value is to be fetched
	 * @return value of variable to be fetched
	 */
	double fetch(String variable){
		if (this.variables.containsKey(variable)){
			return this.variables.get(variable);
		}
		throw new RuntimeException("Variable not found");
	}
	/** Creates and coordinates all Workers */
	public void run() {

		int numberOfBugs = this.program.getChild(1).getNumberOfChildren();
		for (int i = 0; i < numberOfBugs; i++) {
			Bugs runBug = bugs.get(this.program.getChild(1).getChild(i).getChild(0).getValue().value);
			runBug.start();
		}
		while (bugs.size() > 0) {
			unblockAllWorkers();
		}

	}
	/** Called by a Worker to try to get permission to work */
	synchronized void getBugPermit(Bugs bug) {
		while (bug.isBlocked()) {
			try {
				wait();
			}
			catch (InterruptedException e) {
			}
		}
	}
	/** Counts the number of currently blocked Workers; since this is
	 *  called from a synchronized method, it is effectively synchronized */
	private int countBlockedBugs() {
		int count = 0;
		for (Bugs bug : bugs.values()) {
			if (bug.isBlocked()) {
				count++;
			}
		}
		return count;
	}

	/** Called by a Worker to indicate completion of work */
	synchronized void completeCurrentTask(Bugs bugs2) {
		bugs2.setBlocked(true);
		notifyAll();
	}

	/** Called by this TaskManager to allow all Workers to work */
	synchronized void unblockAllWorkers() {
		while (countBlockedBugs() < bugs.size()) {
			try {
				wait();
			}
			catch (InterruptedException e) {
			}
		}
		for (Bugs bug : bugs.values()) {
			bug.setBlocked(false);
		};
		notifyAll();  
	}
	/** Called by a Worker to die; synchronized because it modifies the
	 * ArrayList of workers, which is used by other synchronized methods. */
	synchronized void terminateBug(Bugs bug) {
		bugs.remove(bug);
	}	

}