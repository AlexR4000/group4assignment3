package implementations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import utilities.BSTreeADT;
import utilities.Iterator;

/**
 * A generic Binary Search Tree (BST) implementation.
 * 
 * <p>This class supports insertion, search, min/max removal, clearing, height
 * calculation, and three traversal iterators (inorder, preorder, postorder).</p>
 *
 * <p>The BST enforces the natural ordering of elements via Comparable. Duplicate
 * values are not allowed.</p>
 * 
 * @param <E> The type stored in the tree, must be Comparable
 */
public class BSTree<E extends Comparable<? super E>> implements BSTreeADT<E>, Serializable {

    private static final long serialVersionUID = 1L;

    /** Root node of the BST */
    private BSTreeNode<E> root;

    /** Number of nodes currently in the tree */
    private int size;

    // ------------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------------

    /**
     * Default constructor. Creates an empty BST.
     */
    public BSTree() {
        root = null;
        size = 0;
    }

    /**
     * Constructs a BST with a single root node.
     * Useful for testing.
     *
     * @param rootData value for the root
     * @throws NullPointerException if rootData is null
     */
    public BSTree(E rootData) {
        if (rootData == null)
            throw new NullPointerException("Root value cannot be null.");
        root = new BSTreeNode<>(rootData);
        size = 1;
    }

    // ------------------------------------------------------------
    // BASIC TREE METHODS
    // ------------------------------------------------------------

    /**
     * Returns the root of the tree.
     *
     * @return root BSTreeNode
     * @throws NullPointerException if tree is empty
     */
    @Override
    public BSTreeNode<E> getRoot() throws NullPointerException {
        if (root == null)
            throw new NullPointerException("Tree is empty.");
        return root;
    }

    /**
     * Computes the height of the BST.
     * Height of an empty tree is 0.
     */
    @Override
    public int getHeight() {
        return root == null ? 0 : getHeightRecursive(root);
    }

    /** Recursive height helper */
    private int getHeightRecursive(BSTreeNode<E> node) {
        if (node == null)
            return 0;
        return 1 + Math.max(getHeightRecursive(node.left), getHeightRecursive(node.right));
    }

    /** @return number of nodes in the tree */
    @Override
    public int size() {
        return size;
    }

    /** @return true if tree contains no nodes */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /** Removes all nodes from the tree */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    // ------------------------------------------------------------
    // SEARCH
    // ------------------------------------------------------------

    /**
     * Checks if a value exists in the BST.
     *
     * @param entry value to find
     * @return true if found, false otherwise
     */
    @Override
    public boolean contains(E entry) {
        if (entry == null)
            throw new NullPointerException("Cannot search for null.");
        return search(entry) != null;
    }

    /**
     * Searches the tree and returns the node containing the entry.
     *
     * @param entry value to find
     * @return BSTreeNode if found, otherwise null
     */
    @Override
    public BSTreeNode<E> search(E entry) {
        if (entry == null)
            throw new NullPointerException("Cannot search for null.");
        return searchRecursive(root, entry);
    }

    /** Recursive search helper */
    private BSTreeNode<E> searchRecursive(BSTreeNode<E> node, E entry) {
        if (node == null)
            return null;

        int cmp = entry.compareTo(node.data);
        if (cmp == 0)
            return node;
        else if (cmp < 0)
            return searchRecursive(node.left, entry);
        else
            return searchRecursive(node.right, entry);
    }

    // ------------------------------------------------------------
    // INSERTION
    // ------------------------------------------------------------

    /**
     * Inserts a new entry into the BST.
     *
     * @param newEntry value to insert
     * @return true if inserted, false if duplicate
     */
    @Override
    public boolean add(E newEntry) {
        if (newEntry == null)
            throw new NullPointerException("Cannot insert null.");

        if (root == null) {
            root = new BSTreeNode<>(newEntry);
            size++;
            return true;
        }

        return insertRecursive(root, newEntry);
    }

    /** Recursive insert helper */
    private boolean insertRecursive(BSTreeNode<E> node, E entry) {
        int cmp = entry.compareTo(node.data);

        if (cmp == 0)
            return false; // duplicate not allowed

        if (cmp < 0) {
            if (node.left == null) {
                node.left = new BSTreeNode<>(entry);
                size++;
                return true;
            }
            return insertRecursive(node.left, entry);
        } else {
            if (node.right == null) {
                node.right = new BSTreeNode<>(entry);
                size++;
                return true;
            }
            return insertRecursive(node.right, entry);
        }
    }

    // ------------------------------------------------------------
    // REMOVE MIN / MAX
    // ------------------------------------------------------------

    /**
     * Removes and returns the smallest node.
     *
     * @return BSTreeNode removed, or null if tree is empty
     */
    @Override
    public BSTreeNode<E> removeMin() {
        if (root == null) return null;

        if (root.left == null) {
            BSTreeNode<E> removed = root;
            root = root.right;
            size--;
            return removed;
        }

        BSTreeNode<E> parent = root;
        BSTreeNode<E> current = root.left;

        while (current.left != null) {
            parent = current;
            current = current.left;
        }

        parent.left = current.right;
        size--;
        return current;
    }

    /**
     * Removes and returns the largest node.
     *
     * @return BSTreeNode removed, or null if tree is empty
     */
    @Override
    public BSTreeNode<E> removeMax() {
        if (root == null) return null;

        if (root.right == null) {
            BSTreeNode<E> removed = root;
            root = root.left;
            size--;
            return removed;
        }

        BSTreeNode<E> parent = root;
        BSTreeNode<E> current = root.right;

        while (current.right != null) {
            parent = current;
            current = current.right;
        }

        parent.right = current.left;
        size--;
        return current;
    }

    // ------------------------------------------------------------
    // ITERATOR IMPLEMENTATION
    // ------------------------------------------------------------

    /**
     * Private iterator class that walks through an ArrayList.
     */
    private class ArrayListIterator implements Iterator<E> {
        private final ArrayList<E> list;
        private int index = 0;

        public ArrayListIterator(ArrayList<E> list) {
            this.list = list;
        }

        @Override
        public boolean hasNext() {
            return index < list.size();
        }

        @Override
        public E next() {
            if (!hasNext())
                throw new NoSuchElementException("No more elements.");
            return list.get(index++);
        }
    }

    // ------------------------------------------------------------
    // INORDER
    // ------------------------------------------------------------

    /**
     * @return an iterator that traverses the tree inorder (L, Root, R)
     */
    @Override
    public Iterator<E> inorderIterator() {
        ArrayList<E> list = new ArrayList<>();
        inorder(root, list);
        return new ArrayListIterator(list);
    }

    private void inorder(BSTreeNode<E> node, ArrayList<E> list) {
        if (node == null) return;
        inorder(node.left, list);
        list.add(node.data);
        inorder(node.right, list);
    }

    // ------------------------------------------------------------
    // PREORDER
    // ------------------------------------------------------------

    /**
     * @return a preorder iterator (Root, L, R)
     */
    @Override
    public Iterator<E> preorderIterator() {
        ArrayList<E> list = new ArrayList<>();
        preorder(root, list);
        return new ArrayListIterator(list);
    }

    private void preorder(BSTreeNode<E> node, ArrayList<E> list) {
        if (node == null) return;
        list.add(node.data);
        preorder(node.left, list);
        preorder(node.right, list);
    }

    // ------------------------------------------------------------
    // POSTORDER
    // ------------------------------------------------------------

    /**
     * @return a postorder iterator (L, R, Root)
     */
    @Override
    public Iterator<E> postorderIterator() {
        ArrayList<E> list = new ArrayList<>();
        postorder(root, list);
        return new ArrayListIterator(list);
    }

    private void postorder(BSTreeNode<E> node, ArrayList<E> list) {
        if (node == null) return;
        postorder(node.left, list);
        postorder(node.right, list);
        list.add(node.data);
    }
}
