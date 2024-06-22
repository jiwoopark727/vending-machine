package DataStructure;

import Money.Money;

public class MaxHeap <T> extends LinkedList<T> {
	
	public MaxHeap() {
		this.add(null);	// 인덱스가 1부터 시작
	}

	public void insert(T data) {

		// 제네릭을 통해 비교를 구현하는 법을 몰라 Money 인 경우만 처리
		if (data instanceof Money) {
			this.add(data);
			int i = this.size() - 1;

			Money data_m = (Money)data;
			while (i != 1 && data_m.getPrice() > ((Money)this.get(i/2)).getPrice()) {
				this.set(i/2,  data);
				i /= 2;
			}
			this.set(i, data);
		}
	}
	
	public T delete(T data) {
		
		if (data instanceof Money) {
			int parent, child;
			T item, temp;
			int size = this.size();
			
			item = this.get(1);
			temp = this.remove(size--);
			parent = 1;
			child = 2;
			
			while (child <= size) {
				// 작은 자식에게 붙여주기 위한 조건문
				if ((child < size) &&
					((Money)this.get(child)).getPrice() > ((Money)this.get(child+1)).getPrice())
					++child;
				// 이 경우 temp 가 child 와 같은 레벨에 있음
				if (((Money)temp).getPrice() <= ((Money)this.get(child)).getPrice())
					break;
				this.set(parent, this.get(child));
				parent = child;
				child *= 2;
			}
			
			this.set(parent, temp);
			
			return item;
		}
		
		return null;
	}
}
