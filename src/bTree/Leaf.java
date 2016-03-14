package bTree;

public class Leaf extends Node {
	public int[] Page;
	public int[] Position;
	public Node next;

	public Node getNext() {
		return next;
	}

	public void setNext(Node next) {
		this.next = next;
	}

	public Integer getPage(int x) {
		return Page[x];
	}

	public void setPage(Integer page, int x) {
		Page[x] = page;
	}

	public Integer getPosition(int x) {
		return Position[x];
	}

	public void setPosition(Integer position, int x) {
		Position[x] = position;
	}

	public void insert(int x, int p, int k) {
		if (!this.isFull()) {
			if (this.getDataCount() == 0) {
				this.setData(x, 0);
				this.setPage(p, 0);
				this.setPosition(k, 0);
			}else
			for (int i = 0; i < this.getDataCount(); i++) {
				if (x < this.getData(i).intValue()) {
					shift(i);
					this.setData(x, i);
					this.setPage(p, i);
					this.setPosition(k, i);
					break;
				}
				else if(i == this.getDataCount()-1)
				{
					i++;
					this.setData(x, i);
					this.setPage(p, i);
					this.setPosition(k, i);
				}
			}
			this.setDataCount(this.getDataCount() + 1);
		}
	}

	public void shift(int x) {
		Integer temp11 = this.getData(x);
		Integer temp21 = null;
		int temp12 = this.getPosition(x);
		int temp22 = 0;
		int temp13 = this.getPage(x);
		int temp23 = 0;
		for (int i = x; i < this.getDataCount(); i++) {
			temp21=this.getData(i+1);
			temp22=this.getPosition(i+1);
			temp23=this.getPage(i+1);
			this.setData(temp11, i + 1);
			this.setPosition(temp12, i + 1);
			this.setPage(temp13, i + 1);
			temp11 = temp21;
			temp12 = temp22;
			temp13 = temp23;
		}
	}

	public Leaf(int x, Node p, Leaf n) {
		super(x, p);
		this.setMin((int) Math.floor((x + 1) / 2));
		Page = new int[max];
		Position = new int[max];
		next = n;
	}
}
