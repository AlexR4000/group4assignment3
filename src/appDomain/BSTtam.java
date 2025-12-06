package appDomain;

import java.io.Serializable;

public class BSTtam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Nodetam root;
	
	public void insert(String word) {
		root = insertHelper(root, word);
	}
	
	private Nodetam insertHelper(Nodetam root, String word) {
		if (root == null) {
			Nodetam node = new Nodetam(word);
			return node;
		} 
        int cmp = word.compareToIgnoreCase(root.word);  // USE IGNORE CASE

        if (cmp == 0) {
            // already exists (case-insensitive), do NOT insert
            return root;
        } 
        else if (cmp < 0) {
            root.left = insertHelper(root.left, word);
        } else {
            root.right = insertHelper(root.right, word);
        }
        
        return root;
	}
	
	public void display() {
		if (root == null) {
			System.out.println("Tree is empty");
			return;
		}
		
		displayHelper(root);
	} 
	
	private void displayHelper(Nodetam root) {
		if (root != null) {
			displayHelper(root.left);
			System.out.println(root.word);
			displayHelper(root.right);
		}
	}
	
	public boolean find(String word) {
		if (root == null) {
//			System.out.println("tree is empty");
			return false;
		}
		return findHelper(root, word);
	}
	
	private boolean findHelper(Nodetam root, String word) {
		if (root == null) {
//			System.out.println(word + " not found");
			return false;
		}

		if (root.word.toLowerCase().equals(word.toLowerCase())) {
//			System.out.println(word + " is found");
			return true;
		} 
		else if (word.toLowerCase().compareTo(root.word.toLowerCase()) < 0) { 
			return findHelper(root.left, word);
		}
		else {
			return findHelper(root.right, word);
		}
	}
	
	public void remove(String word) {
		if (!find(word)) {
			System.out.println(word + " is not found");
			return;
		}
		root = removeHelper(root, word);
	}
	
	private Nodetam removeHelper(Nodetam root, String word) {
		if (root == null) {
			return root;
		} 
		if (word.compareTo(root.word) < 0) {
			root.left = removeHelper(root.left, word);
		} 
		else if (word.compareTo(root.word) > 0) {
			root.right = removeHelper(root.right,word);
		}
		else if (root.word == word) {
			if (root.left == null && root.right == null) {
				root = null;
			}
			else if (root.right != null) {
				root.word = getSuccessor(root.right, word);
				root.right = removeHelper(root.right, root.word);
			}
			else if (root.left != null) {
				root.word = getPredecessor(root.left,word);
				root.left = removeHelper(root.left, root.word);
			}
		}
		
		return root;
	}
	
	private String getSuccessor(Nodetam root, String word) {
		while (root.left != null) {
			root = root.left;
		}
		return root.word;
	}
	private String getPredecessor(Nodetam root, String word) {
		while (root.right != null) {
			root = root.right;
		}
		return root.word;
	}	
	
	public void clear() {
		root = null;
	}
}
