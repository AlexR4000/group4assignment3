package appDomain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Word implements Comparable<Word>, Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String word;
	private Map<String, ArrayList<Integer>> occurrences;
	
	public Word (String word) {
		this.word = word.toLowerCase();
        this.occurrences = new HashMap<>();
	}
	
	public String getWord() {
		return word;
	}
	
	public void addOccurrences(String filename, int lineNumber) {
		occurrences.putIfAbsent(filename, new ArrayList<>());
		occurrences.get(filename).add(lineNumber);
	}
	
	public Map<String,ArrayList<Integer>> getOccurrences(){
		return occurrences;
	}

	@Override
	public int compareTo(Word arg0) {
		return this.word.compareTo(arg0.word);
	}
	
	//Prints
	
	public String toPrintFilesOnly() {
        return word + " : " + occurrences.keySet();
    }
	
	public String toPrintFilesAndLines() {
		StringBuilder sb = new StringBuilder(word + ": ");
        for (String file : occurrences.keySet()) {
            sb.append("\n   ").append(file).append(" -> ").append(occurrences.get(file));
        }
        return sb.toString();
	}
	
	public String toPrintFilesLinesFrequency() {
        StringBuilder sb = new StringBuilder(word + ": ");
        for (String file : occurrences.keySet()) {
            ArrayList<Integer> lines = occurrences.get(file);
            sb.append("\n   ").append(file)
              .append(" -> ").append(lines)
              .append(" (freq ").append(lines.size()).append(")");
        }
        return sb.toString();
    }

}
