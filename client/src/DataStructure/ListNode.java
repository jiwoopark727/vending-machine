package DataStructure;

public class ListNode<T> {

    private T data;
    private ListNode<T> link;

    public ListNode(T data) {
        this.data = data;
        this.link = null;
    }

    public void link(ListNode<T> node) {
        this.link = node;
    }

    public void set(T data) {
    	this.data = data;
    }
    
    public T get() {
        return data;
    }
    
    public T get(int search) {
    	int i = 0;
    	for (ListNode<T> node = this; node != null; node = node.next()) {
    		if (i == search)
    			return node.get();
    		++i;
    	}
    	throw new ArrayIndexOutOfBoundsException();
    }

    public ListNode<T> next() {
        return link;
    }
}