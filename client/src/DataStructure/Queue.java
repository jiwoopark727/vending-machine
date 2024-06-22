package DataStructure;

public class Queue<T> {

    private ListNode<T> front;
    private ListNode<T> rear;
    private int size;

    public Queue() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    public void enqueue(T data) {
    	ListNode<T> node = new ListNode<T>(data);
        
    	if (isEmpty()) {
        	front = node;
        }
        else {
        	rear.link(node);
        }
    	
    	rear = node;
    	++size;
    }

    public T dequeue() {
    	if (isEmpty())
    		return null;
    	else {
    		T temp = front.get();
    		front = front.next();
    		--size;
    		return temp;
    	}
    }
    
    public T peek() {
        return front.get();
    }

    public boolean isEmpty() {
        return size == 0;
    }
    
}
