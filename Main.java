/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Anika Agarwal
 * aa59662
 */


package assignment3;
import java.util.*;
import java.io.*;

public class Main {
	
	// static variables and constants only here.
	public static Set<String> dict; 
	static ArrayList<String> input;
	static Scanner keyboard = new Scanner(System.in);
	
	public static void main(String[] args) throws Exception {
		
		Scanner kb;	// input Scanner for commands
		PrintStream ps;	// output file, for student testing and grading only
		// If arguments are specified, read/write from/to files instead of Std IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps);			// redirect output to ps
		} else {
			kb = new Scanner(System.in);// default input from Stdin
			ps = System.out;			// default output to Stdout
		}
		//initialize dict and input array list 
		initialize();
		
		//read in start and end word 
		parse(keyboard);
		makeUpper();
		
		//System.out.println(returnNeighbors(input.get(0)));
		//System.out.println(returnNumNeighbors(input.get(0)));
		System.out.println(getWordLadderBFS(input.get(0), input.get(1)));
		System.out.println(getWordLadderDFS(input.get(0), input.get(1)));
	}
	
	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests.  So call it 
		// only once at the start of main.
		
		dict = makeDictionary();  //make dictionary available 
		input = new ArrayList<String>(); //hold user input in array list 
	}
	
	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of Strings containing start word and end word. 
	 * If command is /quit, return empty ArrayList. 
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		System.out.println("enter two words separated by a space:");
		String word = keyboard.next();
		if (word.equals("/quit")) {
            return input;
        }
		input.add(word);
		word = keyboard.next();
		if (word.equals("/quit")) {
			input.remove(0);
            return input;
        }
		input.add(word);
		
		//to test scanner 
		//System.out.println("contents of array list input:");
		//System.out.println(input);
		//
		
		return input;
	}
	public static void makeUpper() {
		for(int i=0; i<input.size(); i++) {
			String element = input.get(i);
			input.remove(i);
			input.add(i, element.toUpperCase());
		}
		
	}
	public static boolean checkIfNeighbors(String word1, String word2) {
		int count = 0;
		
		if(word1.length() != word2.length()) { //check if words are same length
			return false;
		}
		
		for(int i=0; i< word1.length(); i++) {
			if(word1.charAt(i) != word2.charAt(i)) {
				count = count + 1;			//count incremented by number of letter differences in word1 and word2
			}
		}
		
		if(count == 1) {
			return true;
		}
		
		return false;
	
	}
	
	public static ArrayList<String> returnNeighbors(String s){
		//String check = input.get(0);  //checks neighbors of first word in list 
		ArrayList<String> neighbors = new ArrayList<String>();
		Iterator<String> it = dict.iterator();
		
		
		while(it.hasNext()) {
			String add = it.next();
			if(checkIfNeighbors(s, add)) {
				neighbors.add(add);
			}
		}
		
		return neighbors;
	}
	
	public static int returnNumNeighbors(String s){
		//String check = input.get(0);  //checks neighbors of first word in list 
		ArrayList<String> neighbors = new ArrayList<String>();
		Iterator<String> it = dict.iterator();
		int count = 0;
		
		while(it.hasNext()) {
			String add = it.next();
			if(checkIfNeighbors(s, add)) {
				neighbors.add(add);
				count++;
			}
		}
		
		return count;
	}
	
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		if(start.equals(end)) { //no ladder between equal words
			return null;
		}
		if(!(dict.contains(start) && dict.contains(end))){
			return null;
		}
		
		ArrayList<String> ladder = DFSHelper(start, end); 
		
		// create reversed ladder for result
				ArrayList<String> reverseLadder = new ArrayList<String>();
				// if no path to create ladder, return null
				if(ladder == null) {
					System.out.println("no word ladder can be found between " + input.get(0) + " and " + input.get(1));
    					return null;
				}
				for (int i = ladder.size()-1; i > 0; i -= 1) { 
					String word = ladder.get(i);
					reverseLadder.add(word);	
				}
				System.out.println("a " + reverseLadder.size() + " word ladder exists between " + input.get(0) + " and " + input.get(1));
				return reverseLadder;		
	}
	
	public static ArrayList<String> DFSHelper(String start, String end) {
		ArrayList<String> ladder = new ArrayList<String>();
		ladder = returnNeighbors(start);
		
		if(ladder.contains(end)) {
			ArrayList<String> path = new ArrayList<String>();
			path.add(start);
			path.add(end);
			return path;
		}
		
		ArrayList<String> shortestPath = new ArrayList<String>();
		int shortestLen = dict.size();
		String shorter = null;
		shortestPath = null;
		
		dict.removeAll(ladder);
		
		for (String s : ladder) {
			ArrayList<String> path = DFSHelper(s,end);
	       
			if (!(path == null)) {	
				if (path.size() < shortestLen) {
					shortestPath = path;
					shortestLen = path.size();
					shorter = s;
				}
			} 
			else { continue; }
		}
		// if no path for ladder, 
		if (shortestPath == null) { 
			return null;
		}		else{
					dict.add(shorter);
					shortestPath.add(start);
					return shortestPath;			
		}
	}
		/*ArrayList<String> ladder = new ArrayList<String>();
		//ArrayList<String> visited = new ArrayList<String>();
		
		if (!((dict.contains(start)) && (dict.contains(end))) || (start.length() != end.length()) ) { //if start and end not in dict, return empty ladder 
			return ladder; 
		}
		
		ladder = returnNeighbors(start); 
		
		//start neighbors end
		if(ladder.contains(end)) {
			ArrayList<String> finalPath = new ArrayList<String>();
			finalPath.add(start);
			finalPath.add(end);
			return finalPath;
		}
		
		if (ladder.isEmpty()) { 
			return null; 
		}
		
		//mark all visited words 
		//visited.addAll(ladder);
		dict.removeAll(ladder);
		//dict.remove(start);
		
		//recursion
		
		
		//Iterator<String> it = dict.iterator();
		int length = dict.size();
		String str = null;
		ArrayList<String> shortestPath = new ArrayList<String>();
		shortestPath = null;
		ArrayList<String> testpath = null;
		
		for(String s: ladder) {
			//while(it.hasNext()) {
			//	String dictWord = it.next();
				//if(checkIfNeighbors(s, dictWord)) {
					testpath = getWordLadderDFS(s,end);
				//}
			//}
			
			if(testpath != null) {
				if(testpath.size() < length) {
					shortestPath = testpath;
					length = testpath.size();
					str = s;
				}
			}
			
			else {
				continue;
			}
		}
		
		if(shortestPath == null) {
			return null;
		}
		
		dict.add(str);
		shortestPath.add(start);
		return shortestPath;
	}
		
		/*ArrayList<String> testpath = null;
		for(int i=0; i< ladder.size(); i++) {
			while(it.hasNext()) {
				String dictWord = it.next();
				if(checkIfNeighbors(ladder.get(i), dictWord)) {
					testpath = getWordLadderDFS(it.next(),end);
				}
			}
		
		
			if(testpath != null) {
				if(testpath.size() < length) {
					shortestPath = testpath;
					length = testpath.size();
				}
			}
			else {
				continue;
			}
	}
		
			return shortestPath; // replace this line later with real return
	}*/
	
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
    		ArrayList<String> ladder = new ArrayList<String>();
    		ArrayList<String> visited = new ArrayList<String>();
    		Queue<ArrayList<String>> queue = new LinkedList<ArrayList<String>>();
    		
    		if (!( (dict.contains(start)) && (dict.contains(end))) ) { //if start and end not in dict, return empty ladder 
    			return ladder; 
    		}
    		
    		if(start.equals(end)) { //no ladder between equal words
    			return ladder;
    		}
    	
    		ladder.add(start); //put starting word in queue
    		queue.add(ladder); 
    	
    		ArrayList<String> nextWordList = queue.remove();	//get head list from queue
    		
    		while((!nextWordList.contains(end))) {
    				String nextWord = nextWordList.get(nextWordList.size() -1); //get last word in head list 
    					
			    			ArrayList<String> list = new ArrayList<String>();
			    			
			    			Iterator<String> it = dict.iterator();
				    			while(it.hasNext()) {
				    				String dictWord = it.next();
				    				if(checkIfNeighbors(nextWord, dictWord)) {
				    					list.add(dictWord);   
				    					//visited.add(dictWord);
				    				}
				    			}		
				    			for(int i=0; i<list.size(); i++) {
				    				ArrayList<String> enqueue = new ArrayList<String>();
				    				if(!visited.contains(list.get(i))){
		    						enqueue.addAll(nextWordList);
		    						enqueue.add(enqueue.size(), list.get(i));
		    						visited.addAll(enqueue);
		    						queue.add(enqueue);
				    				}
		    					}
		    			
    					//visited.add(nextWord);
    					nextWordList = queue.remove();	
	    		}
    			//nextWordList = queue.remove();
    			if(nextWordList.contains(end)) {
	    			System.out.println("a " + nextWordList.size() + " word ladder exists between " + input.get(0) + " and " + input.get(1));
	    			return nextWordList; // replace this line later with real return
    			}
    			
    			
    			System.out.println("no word ladder can be found between " + input.get(0) + " and " + input.get(1));
    			return null;
    		}
    		
	public static void printLadder(ArrayList<String> ladder) {
		
	}
	// TODO
	// Other private static methods here


	/* Do not modify makeDictionary */
	public static Set<String>  makeDictionary () {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner (new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}
		return words;
	}
}
