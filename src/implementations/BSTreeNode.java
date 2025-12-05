package implementations;

public class BSTreeNode<E>
{
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
