package implementations;

import java.io.Serializable;

/**
 * A single node within a Binary Search Tree (BST).
 * <p>
 * Each node stores:
 * - A data element of type E
 * - A reference to its left child
 * - A reference to its right child
 *
 * @param <E> The type stored in the node, which must be comparable.
 */
public class BSTreeNode<E extends Comparable<? super E>> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** The data element stored in this node. */
    E data;

    /** Reference to the left child node. */
    BSTreeNode<E> left;

    /** Reference to the right child node. */
    BSTreeNode<E> right;

    /**
     * Constructs a new BSTreeNode containing the given element.
     * The node starts with no children (left and right are null).
     *
     * @param data the value to store in the node
     * @throws NullPointerException if data is null
     */
    public BSTreeNode(E data) {
        if (data == null)
            throw new NullPointerException("BSTreeNode cannot store null data.");
        this.data = data;
        this.left = null;
        this.right = null;
    }

    /**
     * Returns the element stored in this node.
     * Required by some unit tests.
     *
     * @return the data stored in this node
     */
    public E getElement() {
        return data;
    }

    /**
     * Returns the left child of this node.
     *
     * @return the left child, or null if no left child exists
     */
    public BSTreeNode<E> getLeft() {
        return left;
    }

    /**
     * Returns the right child of this node.
     *
     * @return the right child, or null if no right child exists
     */
    public BSTreeNode<E> getRight() {
        return right;
    }

    /**
     * Sets the left child node reference.
     *
     * @param left the new left child node
     */
    public void setLeft(BSTreeNode<E> left) {
        this.left = left;
    }

    /**
     * Sets the right child node reference.
     *
     * @param right the new right child node
     */
    public void setRight(BSTreeNode<E> right) {
        this.right = right;
    }
}
