package bTree;

import java.util.ArrayList;

public class BPlusTree {
	public int key;
	public BaseNode root;
	public int depth;

	public BPlusTree(int x) {
		key = x;
		depth = 0;
		root = new BaseNode(x, null);
	}

	public int getKey() {
		return key;
	}

	public BaseNode getRoot() {
		return root;
	}

	public void insertInFirstNode(int x, int p, int k) {
		Leaf l = new Leaf(this.getKey(), this.getRoot(), null);
		l.insert(x, p, k);
		this.getRoot().insertFirstData(l, x);
		this.depth++;
	}

	public static void insertNewLeaf(Node n, int j, int x, int p, int k) {
		Leaf l = new Leaf(n.getMax(), n, null);
		l.insert(x, p, k);
		((BaseNode) n).insertRightMostChild(l);
	}

	public static void insertInNonFullLeaf(Leaf l, int x, int p, int k) {
		l.insert(x, p, k);
	}

	public static void insertInFullLeaf(BPlusTree btree, Leaf l, int x, int p,
			int k) {
		ArrayList<Integer> array1 = new ArrayList<Integer>();
		ArrayList<Integer> array2 = new ArrayList<Integer>();
		ArrayList<Integer> array3 = new ArrayList<Integer>();
		for (int i = 1; i < l.getMin(); i++) {
			array1.add(l.getData(l.getDataCount() - 1));
			array2.add(l.getPage(l.getDataCount() - 1));
			array3.add(l.getPosition(l.getDataCount() - 1));
			l.setData(null, l.getDataCount() - 1);
			l.setDataCount(l.getDataCount() - 1);
		}
		Leaf n = new Leaf(l.getMax(), l.getParent(), null);
		while (!array1.isEmpty()) {
			n.insert(array1.get(0), array2.get(0).intValue(), array2.get(0)
					.intValue());
			array1.remove(0);
			array2.remove(0);
			array3.remove(0);
		}
		if (x < l.getData(l.getDataCount() - 1).intValue()) {
			n.insert(l.getData(l.getDataCount() - 1),
					l.getPage(l.getDataCount() - 1).intValue(),
					l.getPosition(l.getDataCount() - 1).intValue());
			l.insert(x, p, k);
			l.setData(null, l.getDataCount() - 1);
			l.setDataCount(l.getDataCount() - 1);
		} else
			n.insert(x, p, k);
		if (!((BaseNode) l.parent).isFull()) {
			insertInNonFullBase(l.getParent(), n);
		} else
			insertInFullBase(btree, l.getParent(), n,l);
	}

	public static void insertInFullBase(BPlusTree btree, Node parent, Node n, Node n2) {
		ArrayList<Integer> array1 = new ArrayList<Integer>();
		ArrayList<Node> array2 = new ArrayList<Node>();

		array2.add(((BaseNode) parent).getChild(parent.getDataCount()));
		parent.setData(null, parent.getDataCount() - 1);
		((BaseNode) parent).setChild(null, parent.getDataCount());
		parent.setDataCount(parent.getDataCount() - 1);

		for (int i = 1; i < parent.getMin(); i++) {
			array1.add(parent.getData(parent.getDataCount() - 1));
			array2.add(((BaseNode) parent).getChild(parent.getDataCount()));
			parent.setData(null, parent.getDataCount() - 1);
			parent.setDataCount(parent.getDataCount() - 1);
		}
		BaseNode b = new BaseNode(parent.getMax(), parent.getParent());
		if (array2.get(0).getData(0).intValue() < n.getData(0).intValue()) {
			b.setChild(array2.get(0), 0);
			b.setData(n.getData(0), b.getDataCount());
			b.setDataCount(b.getDataCount() + 1);
			b.setChild(n, b.getDataCount());
			
		} else {
			b.setChild(n, 0);	
			b.setData(array2.get(0).getData(0), b.getDataCount());
			b.setDataCount(b.getDataCount() + 1);
			b.setChild(array2.get(0), b.getDataCount());
		}
		array2.remove(0);
		n2.setParent(b);
		n.setParent(b);
		b.sort();
		while (!array1.isEmpty()) {
			b.setData(array1.get(0), b.getDataCount());
			b.setChild(array2.get(0), b.getDataCount() + 1);
			b.setDataCount(b.getDataCount() + 1);
			array1.remove(0);
			array2.remove(0);
			b.sort();
		}
		
		for(int i= 0; i<parent.getDataCount()+1;i++){
			if(((BaseNode)parent).getChild(i)!=null)
				((BaseNode)parent).getChild(i).setParent(parent);
		}
		
		for(int i= 0; i<b.getDataCount()+1;i++){
			if(((BaseNode)b).getChild(i)!=null)
				((BaseNode)b).getChild(i).setParent(b);
		}

		if (!((BaseNode) parent).isRoot()) {
			if (b.getParent().isFull())
				insertInFullBase(btree, b.getParent(), b, parent);
			else
				insertInNonFullBase(b.getParent(), b);
		} else
			createNewRoot(btree, (BaseNode) parent, b);
	}

	public static void createNewRoot(BPlusTree btree, BaseNode parent,
			BaseNode b) {
		BaseNode root = new BaseNode(parent.getMax(), null);
		root.setChild(parent, root.getDataCount());
		root.setData(b.getData(0), root.getDataCount());
		root.setDataCount(root.getDataCount() + 1);
		root.setChild(b, root.getDataCount());
		parent.setParent(root);
		b.setParent(root);
		btree.root = root;
		btree.depth++;
	}

	public static void insertInNonFullBase(Node n, Node l) {
		n.setData(l.getData(0), n.getDataCount());
		if (((BaseNode) n).getChild(n.getDataCount()) instanceof Leaf)
			((Leaf) ((BaseNode) n).getChild(n.getDataCount())).setNext(l);
		n.setDataCount(n.getDataCount() + 1);
		((BaseNode) n).insertRightMostChild(l);
		((BaseNode) n).sort();
		l.setParent(n);
	}

	public static void insert(BPlusTree btree, int x, int p, int k) {
		if (btree.depth == 0) {
			btree.insertInFirstNode(x, p, k);
		} else {
			Node n = btree.getRoot();
			for (int i = 0; i < btree.depth; i++) {
				for (int j = 0; j < n.getDataCount(); j++) {
					if (x < n.getData(j).intValue()) {
						n = ((BaseNode) n).getChild(j);
						break;
					} else if (j == n.getDataCount() - 1) {
						if (((BaseNode) n).getChild(j + 1) != null) {
							n = ((BaseNode) n).getChild(j + 1);
							break;
						} else {
							insertNewLeaf(n, j, x, p, k);
							return;
						}
					}
				}
			}
			Leaf l = (Leaf) n;
			if (l.isFull())
				insertInFullLeaf(btree, l, x, p, k);
			else
				insertInNonFullLeaf(l, x, p, k);
		}
	}

	public static void print(BPlusTree b) {
		String l = "";
		ArrayList<Node> x = new ArrayList<Node>();
		x.add(b.getRoot());
		int[] a = new int[b.depth + 1];
		int c = 0;
		int k = 0;
		while (!x.isEmpty()) {
			Node n = x.get(0);
			x.remove(0);
			l += n.showData() + "  \t  ";
			if (n instanceof BaseNode) {
				for (int i = 0; i < n.getDataCount() + 1; i++) {
					if (((BaseNode) n).getChild(i) != null) {
						x.add(((BaseNode) n).getChild(i));
					}
				}
				a[c + 1] += n.getDataCount() + 1;
			}
			if (k == a[c]) {
				l += '\n';
				k = 0;
				c++;
			}
			k++;

		}
		System.out.println(l);
		// printArray(a);
	}

	// private static void printArray(int[]a) {
	// for(int i = 0; i<a.length; i++)
	// System.out.print(a[i] + "    ");
	// }

	public static String search(BPlusTree btree, int k) {
		if (btree.depth == 0)
			return null;
		Node n = btree.getRoot();
		for (int i = 0; i < btree.depth; i++) {
			for (int j = 0; j < n.getDataCount(); j++) {
				if (k < n.getData(j).intValue()) {
					n = ((BaseNode) n).getChild(j);
					break;
				} else if (j == n.getDataCount() - 1) {
					if (((BaseNode) n).getChild(j + 1) != null) {
						n = ((BaseNode) n).getChild(j + 1);
						break;
					} else {
						return null;
					}
				}
			}
		}
		Leaf l = (Leaf) n;
		for (int i = 0; i < l.getDataCount(); i++) {
			if (k == n.getData(i).intValue()) {
				return l.getPage(i) + "," + l.getPosition(i);
			}
		}
		return null;
	}

	public static void main(String[] args) {
		BPlusTree x = new BPlusTree(3);
		insert(x, 20, 2, 3);
		insert(x, 4, 2, 3);
		insert(x, 5, 2, 3);
		insert(x, 21, 2, 3);
		insert(x, 13, 23, 13);
		insert(x, 18, 2222, 3);
		insert(x, 1, 2, 3);
		insert(x, 2, 2, 3);
		insert(x, 6, 2, 3);
		insert(x, 22, 2, 3);
		insert(x, 19, 2, 3);
		insert(x, 17, 2, 3);
		insert(x, 26, 2, 3);
		insert(x, 27, 2, 3);
		insert(x, 28, 2, 3);
		insert(x, 30, 2, 3);
		insert(x, 31, 2, 3);
		insert(x, 32, 2, 3);
		insert(x, 33, 2, 3);
		insert(x, 34, 2, 3);
		insert(x, 3, 2, 3);
		print(x);
		System.out.println(search(x,18));
	}

}
