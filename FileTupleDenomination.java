package plagiarismDetector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/*The following class organizes the data in a file into tuples (represented as tuple
 * objects) and if called upon compares the called upon object's tuples against another
 * files set of tuples to check for plagiarism. The user also has the chance to assign 
 * a synonym list that assists in checking for plagiarism
 * */
public class FileTupleDenomination {
	
	///////////////////////////////////////////////////////////////////////////////////////
	
	/*The following class is a representation of a tuple 
	 * by storing the words in a tuple
	 * */
	class Tuple{
		ArrayList<String> currTuple = new ArrayList<String>();
		
		/*@param tupleMembers: an arraylist of the words in the tuple
		 * */
		public Tuple (ArrayList<String> tupleMembers){
			
			for(int i = 0; i < tupleMembers.size(); i++) {
				currTuple.add(tupleMembers.get(i));
			}
		}
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////

	String filename;
	int tupleSize;
	
	HashMap<String, Tuple> tuples;
	HashMap<String, ArrayList<String>> synonymList;
	
	/*@param filename: file to read from
	 *@param tupleSize: size of each tuple
	 *
	 *The following Constructor reads all NTuples from the file and stores them in a hashmap
	 * */
	public FileTupleDenomination(String filename, int tupleSize) throws FileNotFoundException, IOException {
		
		this.tuples = new HashMap<String, Tuple>();
		this.synonymList = new HashMap<String, ArrayList<String>>();
		this.filename = filename;
		this.tupleSize = tupleSize;
		
		//read from file and store the input in a string
		BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
		
		String read = "";
		String currLine;
		while((currLine = br.readLine()) != null) {
			read = read.concat(currLine);
		}
		
		br.close();
		processInput(read);
	}
	
	/*@param input: a string representing input
	 *@return void;
	 *
	 *Takes in a string representing the input and breaks down the words based
	 *on whitespace and stores all NTuples
	 * */
	public void processInput(String input) {
		//System.out.println(input);
		String[] words = input.split("\\s+");
		ArrayList<String> currTuple;
		
		for(int i = 0; i < words.length - this.tupleSize + 1; i++) {
			currTuple = new ArrayList<String>();
			
			//get the tuple starting at index i
			for(int j = 0, word = i; j < this.tupleSize; j++, word++) {
				currTuple.add(words[word].toLowerCase());
			}
			
			//create the tuple object and store
			Tuple currTupleObj = new Tuple(currTuple);
			tuples.put(currTupleObj.currTuple.toString(), currTupleObj);
		}
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	
	
	/*@param synonyms: an object that stores mapping of words to their synonyms
	 *@return void
	 *
	 *The following gets a synonym set to help assist in plagiarism detection
	 * */
	public void attachSynonymDenomination(SynonymDenomination synonyms) {
		synonymList = synonyms.getList();
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	
	/*@param baseFile: an object containing tuples to be checked against
	 *@return double: percentage of tuples in the calling object that can be found in baseFile
	 *
	 *the following method returns the percentage of tuples in the calling object that can be found
	 *in baseFile's tuples while accounting for similarity in synonyms
	 * */
	public double computePlagiarismPercent(FileTupleDenomination baseFile) {
		
		int plagiarizedTuples = 0;
		int numTuplesChecked = 0;
		
		Iterator<Map.Entry<String, Tuple>> iter = 
				(Iterator<Map.Entry<String, Tuple>>) this.tuples.entrySet().iterator();
		
		//for every tuple in the object, check if it can be found in baseFile's tuples
		while(iter.hasNext()) {
			Map.Entry<String, Tuple> pair = (Map.Entry<String, Tuple>) iter.next(); 
			Tuple curr = pair.getValue();
			
			//if the tuple is plagiarized, increase the number of plagiarized tuples by 1
			if(isTuplePlagiarized(curr.currTuple, baseFile, 0)) plagiarizedTuples++;
			
			numTuplesChecked++;
		}
		
		return (double) plagiarizedTuples/numTuplesChecked;
	}
	
	
	/*@param tupleRepresentaton: a string representation of a tuple
	 *@return boolean: if the tuple is in the current tupleSet
	 * */
	public boolean containsTuple(String tupleRepresentation) {
		
		if(tuples.containsKey(tupleRepresentation)) return true;
		
		return false;
	}
	
	/*@param curr: ArrayList of strings in a tuple
	 *@param baseFile: object containing tuples to be checked against
	 *@param idx: current idx of the tuple ArrayList (used in recursion)
	 * 
	 *@return boolean: true if curr is a tuple in baseFile, else false
	 *
	 *the following method finds whether a tuple is in the baseFiles tuples
	 *based on equality and synonym equality (see README) 
	 * */
	public boolean isTuplePlagiarized(ArrayList<String> curr, 
											FileTupleDenomination baseFile, int idx) {
		
		
		//check if curr tuple is in baseFiles tuples
		if(baseFile.containsTuple(curr.toString())) return true;
		
		if(idx >= curr.size()) {
			return false;
		}
		
		
		//implementing a backtracking solution to figure 
		//out possible combinations of equivalent tuples based on synonyms
		boolean synonymIteration = false;
		for(int i = idx; i < curr.size(); i++) {
			
			if(this.synonymList.containsKey(curr.get(i))){
				
				ArrayList<String> currWordSynonyms = synonymList.get(curr.get(i));
				
				for(int j = 0; j < currWordSynonyms.size(); j++) {
					//System.out.println(currWordSynonyms.get(j));
					String currentStore = curr.get(i);
					curr.set(i, currWordSynonyms.get(j));
					synonymIteration |= isTuplePlagiarized(curr, baseFile, i+1);
					curr.set(i, currentStore);
				}
			} 
		}
		
		return false | synonymIteration;
	}
	
	
	////////////////////////////////////////////////////////////////////////////////////////
	
}
