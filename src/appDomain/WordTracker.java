package appDomain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import implementations.BSTree;
import implementations.BSTreeNode;
import utilities.Iterator;
import utilities.Utils;

public class WordTracker {
	BSTree<Word> tree = new BSTree<>();
	int counter = 0;
	String fileName = null;
	File file = null;
	
	public void constructsFromFile(String fileName) {
		file = Utils.check(fileName);
		
		clearOccurrencesForFile(fileName);
		
		if (file == null) return;
		
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line = null;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                counter++;
                
                //read each word in the line
                for (String w : line.split(" ")) {
                	w = w.replaceAll("[\\p{Punct}]", "");
                	
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
            
            Iterator<Word> i = tree.inorderIterator();
            
            while (i.hasNext()) System.out.println(i.next().toPrintFilesLinesFrequency());
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public BSTree<Word> loadTree() {
	    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("repository.ser"))) {

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

	        tree = (BSTree<Word>) rawTree;

	        System.out.println("Tree loaded from repository.ser\n");
	        return tree;

	    } catch (IOException e) {
	        System.out.println("File not found: " + e.getMessage() +"\n");
	        return null;
	    } catch (ClassNotFoundException e) {
	    	System.out.println("File error: " + e.getMessage());
	        return null;
	    }
	}
	
	public void saveTree() {
	    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("repository.ser"))) {
	        oos.writeObject(tree);
	        
	        System.out.println("\nTree saved to repository.ser");
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	private void clearOccurrencesForFile(String fileName) {
	    Iterator<Word> it = tree.inorderIterator();
	    
	    while (it.hasNext()) {
	        Word w = it.next();
	        w.removeOccurrences(fileName); 
	    }
	}

}
