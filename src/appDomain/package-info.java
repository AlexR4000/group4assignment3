/**
 * *************************************************************
 * Package: appDomain
 * 
 * Description:
 *     This package contains the domain-level classes used by the
 *     Word Tracking and Binary Search Tree application. Classes in
 *     this package represent the **core data models** used by the
 *     system. They are independent of UI, file I/O, and BST logic,
 *     making them reusable and easy to serialize.
 *
 * Contents:
 *     • {@link appDomain.Word}  
 *         Represents a unique word and all of its occurrences across
 *         multiple files. Tracks:
 *             - The word as a string
 *             - Filenames where the word appears
 *             - Line numbers within each file
 *
 * Serialization:
 *     Classes in this package implement {@link java.io.Serializable}
 *     because they are stored and retrieved from a persistent
 *     repository (repository.ser). 
 *
 * Usage:
 *     - Words are inserted into a BST (BSTree<Word>)
 *     - The BST is serialized/deserialized as the index
 *     - Word instances store file/line mappings using:
 *           Map<String, ArrayList<Integer>>
 *
 * *************************************************************
 */
package appDomain;
