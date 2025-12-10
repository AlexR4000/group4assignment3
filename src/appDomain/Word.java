package appDomain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a word stored in the BST and tracks all occurrences of that word
 * across multiple files and line numbers.
 *
 * <p>This class is used by the word-indexing application to store:
 * <ul>
 *     <li>The word itself</li>
 *     <li>A mapping of filenames → list of line numbers where the word appears</li>
 * </ul>
 *
 * The class implements:
 * <ul>
 *     <li>{@code Comparable<Word>} so it can be ordered alphabetically in the BST</li>
 *     <li>{@code Serializable} so it can be saved to / loaded from repository.ser</li>
 * </ul>
 */
public class Word implements Comparable<Word>, Serializable {

    private static final long serialVersionUID = 1L;

    /** The actual word text (e.g., "hello"). */
    private String word;

    /**
     * A map storing all occurrences of this word.
     * The key = filename, the value = list of line numbers where the word appears.
     *
     * Example:
     *    "file1.txt" → [3, 8, 20]
     *    "notes.txt" → [1, 1, 2]
     */
    private Map<String, ArrayList<Integer>> occurrences;

    /**
     * Constructs a Word instance with the given word text.
     * Initializes the occurrences map.
     *
     * @param word the word string (must not be null)
     */
    public Word(String word) {
        this.word = word;
        this.occurrences = new HashMap<>();
    }

    /**
     * @return the word text
     */
    public String getWord() {
        return word;
    }

    /**
     * Adds an occurrence of the word at a given filename and line number.
     *
     * If the file has no previous occurrences stored, a new list is created.
     *
     * @param filename   the file where the word was found
     * @param lineNumber the line number in the file
     */
    public void addOccurrences(String filename, int lineNumber) {
        occurrences.putIfAbsent(filename, new ArrayList<>());
        occurrences.get(filename).add(lineNumber);
    }

    /**
     * Removes all occurrences of this word for the specified file.
     *
     * @param fileName the file to remove from the occurrences map
     */
    public void removeOccurrences(String fileName) {
        occurrences.remove(fileName);
    }

    /**
     * @return the map of file names to lists of line numbers
     */
    public Map<String, ArrayList<Integer>> getOccurrences() {
        return occurrences;
    }

    /**
     * Compares two Word objects alphabetically, ignoring case.
     *
     * <p>This allows the BST to maintain correct sorted ordering.</p>
     *
     * @param other the word to compare to
     * @return negative if this word < other, positive if >, zero if equal
     */
    @Override
    public int compareTo(Word other) {
        return this.word.toLowerCase()
                        .compareTo(other.word.toLowerCase());
    }

    // -------------------------------------------------------
    // Printing helpers – used by menu output
    // -------------------------------------------------------

    /**
     * Formats output showing only filenames where the word appears.
     *
     * Example:
     *     hello : [file1.txt, notes.txt]
     *
     * @return formatted string
     */
    public String toPrintFilesOnly() {
        return word + " : " + occurrences.keySet();
    }

    /**
     * Formats output showing filenames and the list of line numbers.
     *
     * Example:
     *     hello :   file1.txt -> [3, 8]   notes.txt -> [1]
     *
     * @return formatted string
     */
    public String toPrintFilesAndLines() {
        StringBuilder sb = new StringBuilder(word + " : ");
        for (String file : occurrences.keySet()) {
            sb.append("   ")
              .append(file)
              .append(" -> ")
              .append(occurrences.get(file));
        }
        return sb.toString();
    }

    /**
     * Formats output showing filenames, line numbers, and frequency count.
     *
     * Example:
     *     hello:   file1.txt -> (2 times) [3, 8]   notes.txt -> (1 times) [1]
     *
     * @return formatted string
     */
    public String toPrintFilesLinesFrequency() {
        StringBuilder sb = new StringBuilder(word + ": ");
        for (String file : occurrences.keySet()) {
            ArrayList<Integer> lines = occurrences.get(file);
            sb.append("   ").append(file)
              .append(" -> ")
              .append(" (")
              .append(lines.size())
              .append(" times) ")
              .append(lines);
        }
        return sb.toString();
    }
}
