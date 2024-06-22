package DataStructure;

// 매출 정보는 입력만 받으므로 삭제 구현하지 않음
public class BinarySearchTree <T extends Comparable<T>> {

	private TreeNode<T> root;
	private int size;
	
	public BinarySearchTree() {
		root = null;
		size = 0;
	}
	
	public TreeNode<T> getRoot() {
		return root;
	}
	
	public T find(T key) {
		TreeNode<T> current = root;
		
		while (current != null) {
			// 현재 노드 == 찾는 값
			if (current.getData().compareTo(key) == 0)
				return current.getData();
			// 현재 노드 > 찾는 값
			else if (current.getData().compareTo(key) > 0)
				current = current.getLeft();
			// 현재 노드 < 찾는 값
			else
				current = current.getRight();
		}
		
		return null;
	}
	
	public void insert(T data) {
		TreeNode<T> new_node = new TreeNode<T>(data);	// �ڽ� ��� ���� ���ο� ���
		++size;

		// root 없으면 새로운 노드가 root 가 됨
		if (root == null) {
			root = new_node;
//			System.out.println("3. root 는 " + root);
			return;
		}
		
		TreeNode<T> current = root;
		TreeNode<T> parent = null;
		
		while(true) {
			
			parent = current;

			// 추가된 노드보다 크면
			if (data.compareTo(current.getData()) < 0) {
				current = current.getLeft();

				// 자식이 없다면 그 자리에 삽입
				if (current == null) {
					parent.setLeft(new_node);
					return;
				}
				// 추가된 노드보다 작으면
			} else {
				current = current.getRight();

				// 자식이 없다면 그 자리에 삽입
				if (current == null) {
					parent.setRight(new_node);
					return;
				}
			}
		}
	}
	
	public int size() {
		return size;
	}
	
}
