package implementations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import utilities.BSTreeADT;
import utilities.Iterator;

public class BSTree<E extends Comparable<? super E>> implements BSTreeADT<E>, Serializable {
    private static final long serialVersionUID = 1L;

    private BSTreeNode<E> root;
    private int size;

    // Constructors
    public BSTree() {
        root = null;
        size = 0;
    }

    // Single-element constructor (needed for tests)
    public BSTree(E rootData) {
        if (rootData == null)
            throw new NullPointerException();
        root = new BSTreeNode<>(rootData);
        size = 1;
    }

    @Override
    public BSTreeNode<E> getRoot() throws NullPointerException {
        if (root == null)
            throw new NullPointerException("Tree is empty.");
        return root;
    }

    @Override
    public int getHeight() {
        return root == null ? 0 : getHeightRecursive(root);
    }

    private int getHeightRecursive(BSTreeNode<E> node) {
        if (node == null)
            return 0; // matches test expectation
        return 1 + Math.max(getHeightRecursive(node.left), getHeightRecursive(node.right));
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean contains(E entry) {
        if (entry == null)
            throw new NullPointerException();
        return search(entry) != null;
    }

    @Override
    public BSTreeNode<E> search(E entry) {
        if (entry == null)
            throw new NullPointerException();
        return searchRecursive(root, entry);
    }

    private BSTreeNode<E> searchRecursive(BSTreeNode<E> node, E entry) {
        if (node == null) return null;
        int cmp = entry.compareTo(node.data);
        if (cmp == 0)
            return node;
        else if (cmp < 0)
            return searchRecursive(node.left, entry);
        else
            return searchRecursive(node.right, entry);
    }

    @Override
    public boolean add(E newEntry) {
        if (newEntry == null)
            throw new NullPointerException();

        if (root == null) {
            root = new BSTreeNode<>(newEntry);
            size++;
            return true;
        }

        return insertRecursive(root, newEntry);
    }

    private boolean insertRecursive(BSTreeNode<E> node, E entry) {
        int cmp = entry.compareTo(node.data);

        if (cmp == 0)
            return false; // duplicate

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

    @Override
    public BSTreeNode<E> removeMin() {
        if (root == null) return null;

        BSTreeNode<E> removedNode;
        if (root.left == null) {
            removedNode = root;
            root = root.right;
            size--;
            return removedNode;
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

    @Override
    public BSTreeNode<E> removeMax() {
        if (root == null) return null;

        BSTreeNode<E> removedNode;
        if (root.right == null) {
            removedNode = root;
            root = root.left;
            size--;
            return removedNode;
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

    // ---------------------
    // ITERATORS
    // ---------------------
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
            if (!hasNext()) throw new NoSuchElementException();
            return list.get(index++);
        }
    }

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
