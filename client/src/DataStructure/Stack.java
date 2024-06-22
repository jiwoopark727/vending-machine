package DataStructure;

public class Stack<T> {

    ListNode<T> top;

    public Stack() {
        this.top = null;
    }

    public void push(T data) {
    	ListNode<T> node = new ListNode<T>(data);
        node.link(top);
        top = node;
    }

    public T pop() {
    	if (empty())
    		return null;
    	else {
    		T temp = top.get();
    		top = top.next();
    		return temp;
    	}
    }
    
    public T peek() {
        return top.get();
    }


    public int search(T item) {
    	ListNode<T> search = top;
        int index = 1;
        while(true) {
            if (search.get() == item) {
                return index;
            } else {
            	search = search.next();
                ++index;
                if (search == null)
                    break;
            }
        }
        return -1;
    }

    private boolean empty() {
        return top == null;
    }
}