package appDomain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class WordTracker {
	BSTtam tree = new BSTtam();
	
	public void constructsFromFile(File file) {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line = null;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                
                //read each word in the line
                for (String word : line.split(" ")) {
                	word = word.replaceAll("[\\p{Punct}]", "");
                	
                	//if the tree already contains the same word, skip
                	if (tree.find(word)) {
                		continue;
                	}
                	
                	tree.insert(word);
                }
            }
            
            tree.display();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public BSTtam loadTree() {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("repository.ser"))) {
			tree = (BSTtam) ois.readObject();
			System.out.println("Tree loaded from repository.ser\n");
			return tree;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
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
}
