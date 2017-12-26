package plagiarismDetector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

//The following class takes in an input file and creates a hashmap
//where every word is mapped to its synonyms
public class SynonymDenomination {
	
	String filename;
	HashMap<String, ArrayList<String>> synonyms;
	
	/*@param filename: the input file
	 * 
	 * the constructor takes in an input file where all words on a line are synonyms
	 * and maps each word to its synonyms
	 * */
	public SynonymDenomination(String filename) throws FileNotFoundException, IOException {
		
		this.filename = filename;
		this.synonyms = new HashMap<String, ArrayList<String>>();
		
		//read from file
		BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
		
		String currLine;
		while((currLine = br.readLine()) != null) {
			//process the synonyms in the line
			processSynonym(currLine);
		}
		
		br.close();
	}
	
	
	/*@param line: takes in a string that representes words that are synonyms
	 *@return void
	 * 
	 * breaks the strings into words based on whitespace and maps each word
	 * to an arraylist containing the words synonyms
	 * */
	public void processSynonym(String line) {
		
		String[] words = line.split("\\s+");
		
		
		ArrayList<String> currWordSynonyms;
		for(int i = 0; i < words.length; i++) {
			
			//get the synonyms of the current word
			currWordSynonyms = new ArrayList<String>();
			for(int j = 0; j < words.length; j++) {
				if(words[i] != words[j]) currWordSynonyms.add(words[j].toLowerCase());
			}
			
			synonyms.put(words[i], currWordSynonyms);
		}
	}
	
	
	/*@param none
	 *@return HashMap<String, ArrayList<String>> a mapping of each word to its synonyms
	 * */
	public HashMap<String, ArrayList<String>> getList(){
		return this.synonyms;
	}
	

}
