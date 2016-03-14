package bTree;

public class BaseNode extends Node {
	public Node[] child;

	public Node getChild(int x) {
		return child[x];
	}

	public void setChild(Node child, int x) {
		this.child[x] = child;
	}

	public boolean isRoot() {
		return this.getParent() == null;
	}

	public void insertRightMostChild(Node c) {
		if (this.getDataCount() < max + 1) {
			this.setChild(c, this.getDataCount());
			if (this.getChild(this.getDataCount()) instanceof Leaf)
				((Leaf) this.getChild(this.getDataCount())).setNext(c);
		}
	}

	public void insertFirstData(Node l, int x) {
		this.setData(x + 1, this.getDataCount());
		this.setChild(l, this.getDataCount());
		this.setDataCount(this.getDataCount() + 1);
	}

	public void sort() {
		for (int j = 0; j < this.getDataCount(); j++)
			for (int i = 1; i < this.getDataCount(); i++) {
				if (this.getData(i).intValue() < this.getData(i - 1).intValue()) {
					Integer temp1 = this.getData(i - 1);
					this.setData(this.getData(i), i - 1);
					this.setData(temp1, i);
					Node temp2 = this.getChild(i);
					this.setChild(this.getChild(i + 1), i);
					this.setChild(temp2, i + 1);
				}
			}
		// if (this.getDataCount() > 0 && this.getChild(1) != null)
		// for (int j = 0; j < this.getDataCount(); j++)
		// for (int i = 0; i < this.getDataCount(); i++) {
		// if (this.getChild(i).getData(0) != null
		// && this.getChild(i + 1).getData(0) != null
		// && this.getChild(i).getData(0).intValue() > this
		// .getChild(i + 1).getData(0).intValue()) {
		// Node temp2 = this.getChild(i);
		// this.setChild(this.getChild(i + 1), i);
		// this.setChild(temp2, i + 1);
		// }
		// }

	}

	// public void shift(int x) {
	// for (int i = x; i < this.getDataCount(); i++) {
	// this.setData(this.getData(i), i+1);
	// this.setChild(this.getChild(i), i+1);
	// }
	// }

	public BaseNode(int x, Node p) {
		super(x, p);
		this.setMin((int) Math.ceil((x + 1) / 2) - 1);
		child = new Node[max + 1];
	}

}
