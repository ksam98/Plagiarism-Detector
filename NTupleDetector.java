package plagiarismDetector;

import java.io.IOException;
import java.text.DecimalFormat;

public class NTupleDetector {
	
	/*
	 * Takes in arguments in the format:
	 * arguments format: <synonyms file> <file1> <file2> <NTuples>
	 * arguments format: <synonyms file> <file1> <file2>
	 * 
	 * and it computes the percentage of plagiarism in file1 when based of file2
	 * as described in the README document
	 * */
	public static void main(String[] args) throws IOException {
		
		String synonymFile;
		String checkFile;
		String baseFile;
		int tuple = 3;
		
		if(args.length == 3 || args.length == 4) {
			
			
			synonymFile = args[0];
			checkFile = args[1];
			baseFile = args[2];
			
			if(args.length == 4) tuple = Integer.valueOf(args[3]); //set the value of NTuples if provided
			
			//Organize the data into synonym mapping, word -> synonyms
			SynonymDenomination synonyms = new SynonymDenomination(synonymFile);
			
			//Organize the data in files in tuples 
			FileTupleDenomination checkFileTuple = new FileTupleDenomination(checkFile, tuple);
			FileTupleDenomination baseFileTuple = new FileTupleDenomination(baseFile, tuple);
			
			//attach the synonym mappings to the files
			checkFileTuple.attachSynonymDenomination(synonyms);
			baseFileTuple.attachSynonymDenomination(synonyms);
			
			double pctPlagiarized = checkFileTuple.computePlagiarismPercent(baseFileTuple) * 100;
			if((int) pctPlagiarized > 0) {
				DecimalFormat decimalPlaces2 = new DecimalFormat("#.00");
				System.out.println("Plagiarized percentage: " + decimalPlaces2.format(pctPlagiarized) + "%");
			} else {
				System.out.println("Plagiarized percentage: 0.00%");
			}
			
			
		} else {
			
			System.err.println("Incorrect number of variables supplied: " 
					+ args.length);
			System.err.println("Correct program usage -> <SynonymFile> <checkFile> <baseFile> <tupleSize>");
		
			System.exit(-1);
		}
		
		
	}
}
