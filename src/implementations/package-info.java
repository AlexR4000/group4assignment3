/**
 * The {@code implementations} package provides concrete implementations of
 * data structures used by the application.
 *
 * <p>Main components include:
 * <ul>
 *   <li>{@link implementations.BSTree} – A generic Binary Search Tree that
 *       stores Comparable elements and provides searching, insertion, height
 *       calculation, removal of min/max nodes, and tree traversal iterators.</li>
 *
 *   <li>{@link implementations.BSTreeNode} – The internal node structure used by
 *       the BST, storing a single element and references to left and right
 *       children.</li>
 * </ul>
 *
 * <p>The BST implementation supports in-order, pre-order, and post-order
 * iterators using an internal ArrayList-based iterator.
 *
 * <p>All classes are Serializable to allow saving and loading tree data.
 */
package implementations;
