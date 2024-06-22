package DataStructure;

public class TreeNode <T extends Comparable<T>> {
	
	private T data;
	private TreeNode<T> left;
	private TreeNode<T> right;
	
	public TreeNode(T data) {
		setData(data);
		setLeft(null);
		setRight(null);
	}
	
	public T getData() {
		return data;
	}
	
	public void setData(T data) {
		this.data = data;
	}
	
	public TreeNode<T> getRight() {
		return right;
	}
	
	public void setRight(TreeNode<T> right) {
		this.right = right;
	}
	
	public TreeNode<T> getLeft() {
		return left;
	}
	
	public void setLeft(TreeNode<T> left) {
		this.left = left;
	}

}
