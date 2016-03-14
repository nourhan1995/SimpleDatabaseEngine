package bTree;

public class Node {
	public Node parent;
	public Integer Data[];
	int dataCount;
	int max;
	int min;

	public Integer getData(int x) {
		return Data[x];
	}

	public void setData(Integer data, int x) {
		Data[x] = data;
	}

	public int getMax() {
		return max;
	}

	public int getMin() {
		return min;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public void setMin(int min) {
		this.min = min;
	}
	
	public int getDataCount() {
		return dataCount;
	}

	public void setDataCount(int dataCount) {
		this.dataCount = dataCount;
	}
	
	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public boolean isFull(){
		return (dataCount == max);
	}

	public Node(int x, Node p) {
		max = x;
		Data = new Integer[max];
		dataCount = 0;
		parent = p;
	}

	public String showData() {
		String l = "";
		for (int i = 0; i < this.getDataCount(); i++) {
			l += this.getData(i).intValue() + "   ";
		}
		return l;
	}
}
