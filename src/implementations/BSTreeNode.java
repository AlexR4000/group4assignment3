package implementations;

import java.io.Serializable;

public class BSTreeNode<E extends Comparable<? super E>> implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	E data;
	BSTreeNode<E> left;
	BSTreeNode<E> right;
	
	public BSTreeNode(E data) {
		this.data = data;
		this.left = null;
		this.right = null;
	}
	
	public E getData() {
		return data;
	}
	
	public BSTreeNode<E> getLeft(){
		return left;
	}
	
	public BSTreeNode<E> getRight(){
		return right;
	}
	
	public void setLeft(BSTreeNode<E> left){
		this.left = left;
	}
	
	public void setRight(BSTreeNode<E> right){
		this.right = right;
	}
}
