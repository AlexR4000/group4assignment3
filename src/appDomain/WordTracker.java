package appDomain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import implementations.BSTree;
import implementations.BSTreeNode;
import utilities.Iterator;
import utilities.Utils;

/**
 * Manages a repository of {@link Word} objects stored inside a {@link BSTree}.
 *
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Load a previously serialized BST repository from {@code repository.ser}.</li>
 *   <li>Read an input text file and add/update Word entries with occurrences (filename -> list of line numbers).</li>
 *   <li>Persist the BST back to {@code repository.ser}.</li>
 *   <li>Generate reports in three modes:
 *       <ul>
 *         <li>{@code pf} — prints words and filenames only</li>
 *         <li>{@code pl} — prints words with filenames and line numbers</li>
 *         <li>{@code po} — prints words with filenames, line numbers and frequency</li>
 *       </ul>
 *   </li>
 * </ul>
 * </p>
 *
 * <p>The class uses {@link utilities.Utils#check(String)} to locate files robustly
 * in common development/execution environments (working directory, res folder,
 * or JAR directory).</p>
 */
public class WordTracker {
	private static final String REPO_FILE = "repository.ser";
	
	BSTree<Word> tree = new BSTree<>();
	int counter = 0;
	String fileName = null;
	File file = null;
	
	/**
     * Reads {@code fileName}, tokenizes lines into words (stripping punctuation),
     * and updates the BST with occurrences (per-filename line numbers).
     *
     * <p>If {@code fileName} was previously processed, all prior occurrences for
     * that filename are cleared first (so repeated runs update the repository,
     * rather than duplicating entries).</p>
     *
     * @param fileName path to the input text file to scan
     */
	public void constructsFromFile(String fileName) {
		this.fileName = fileName;
		file = Utils.check(fileName);
		
		if (file == null) {
            System.out.println("Could not locate file: " + fileName);
            return;
        }
		
		counter = 0;
		
		clearOccurrencesForFile(fileName);
		
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = br.readLine()) != null) {
                counter++;
                line = line.trim();
                if (line.isEmpty()) continue;
                
                //read each word in the line
             // split on whitespace, not just single space (handles tabs/multiple spaces)
                String[] tokens = line.split(" ");
                for (String rawToken : tokens) {
                    if (rawToken == null) continue;
                    // Remove punctuation at ends and in the middle (keeps letters/digits)
                    String w = rawToken.replaceAll("[\\p{Punct}]", "").trim();
                    if (w.isEmpty()) continue;
                	
                	Word word = new Word(w);
                	word.addOccurrences(fileName, counter);
                	
                	//if the tree already contains the same word, skip
                	BSTreeNode<Word> existingNode = tree.search(word);
                	
                	if (existingNode != null) {
                		existingNode.getElement().addOccurrences(fileName, counter);
                		continue;
                	}
                	
                	tree.add(word);
                }
            }
        } catch (IOException e) {
        	System.err.println("Error while reading file " + fileName + ": " + e.getMessage());
            e.printStackTrace();
        }
	}
	
	/**
     * Loads the repository tree from {@code repository.ser} if it exists.
     *
     * <p>The method performs a couple of safety checks:
     * <ul>
     *   <li>Verifies the deserialized object is a {@code BSTree}.</li>
     *   <li>If the tree has elements, verifies the element type is {@code Word}.</li>
     * </ul>
     * If the file is missing, corrupted, or types don't match, the method returns {@code null}
     * and the tracker continues with an empty tree.
     * </p>
     *
     * @return the loaded {@link BSTree} or null if not loaded
     */
	public BSTree<Word> loadTree() {
		File repo = new File(REPO_FILE);
        if (!repo.exists()) {
            System.out.println("Repository file not found; starting with an empty tree.");
            return null;
        }
        
	    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(repo))) {

	        Object obj = ois.readObject();

	        if (!(obj instanceof BSTree<?>)) {
	            System.out.println("Error: File does not contain a BSTree object.");
	            return null;
	        }

	        BSTree<?> rawTree = (BSTree<?>) obj;

	        Iterator<?> it = rawTree.inorderIterator();
	        if (it.hasNext()) {
	            Object element = it.next();
	            if (!(element instanceof Word)) {
	                System.out.println("Error: Tree does not contain Word objects.");
	                return null;
	            }
	        }

	        this.tree = (BSTree<Word>) rawTree;
	        System.out.println("Tree loaded from repository.ser\n");
	        return this.tree;

	    } catch (IOException e) {
	        System.out.println("File not found: " + e.getMessage() +"\n");
	        return null;
	    } catch (ClassNotFoundException e) {
	    	System.out.println("File error: " + e.getMessage());
	        return null;
	    }
	}
	
	/**
     * Serializes the current BST to {@code repository.ser}.
     *
     * <p>Any IOException will be printed to standard error.</p>
     */
	public void saveTree() {
	    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("repository.ser"))) {
	        oos.writeObject(tree);
	        System.out.println("\nTree saved to " + REPO_FILE);
	    } catch (IOException e) {
	    	System.err.println("Error saving repository: " + e.getMessage());
	        e.printStackTrace();
	    }
	}
	
	/**
     * Remove any recorded occurrences for the supplied filename across all Words
     * in the current tree. This is used so that rescanning a file will not cause
     * duplicated line records.
     *
     * @param fileName filename whose occurrences should be cleared
     */
	private void clearOccurrencesForFile(String fileName) {
		if (tree == null) return;
	    Iterator<Word> it = tree.inorderIterator();
	    while (it.hasNext()) {
	        Word w = it.next();
	        w.removeOccurrences(fileName); 
	    }
	}
	
	/**
     * Generate a report of the repository using the given output stream.
     *
     * @param mode either "pf", "pl", or "po" (files only / files+lines / files+lines+frequency)
     * @param out  the PrintStream to write the report to (e.g. {@code System.out} or a file stream)
     */
    public void generateReport(String mode, PrintStream out) {
    	if (tree == null) {
            out.println("No repository loaded.");
            return;
        }
    	
        Iterator<Word> it = tree.inorderIterator();

        while (it.hasNext()) {
            Word w = it.next();
            switch (mode) {
                case "pf":
                    out.println(w.toPrintFilesOnly());
                    break;
                case "pl":
                    out.println(w.toPrintFilesAndLines());
                    break;
                case "po":
                    out.println(w.toPrintFilesLinesFrequency());
                    break;
                default:
                    out.println("Unknown report mode: " + mode);
                    return;
            }
        }
	}
}
