package utils;

public class LIFO<T> {

	protected static class Element<TE> {
		protected Element<TE> next = null;
		protected TE obj;

		protected Element(Element<TE> next, TE obj) {
			super();
			this.next = next;
			this.obj = obj;
		}
	}

	public static class Iterator<TI> {
		protected Element<TI> head = null;

		protected Iterator(Element<TI> head) {
			super();
			this.head = head;
		}

		public boolean hasNext() {
			return (head != null);
		}

		public TI next() {
			if (head == null)
				return null;
			TI ret = head.obj;
			head = head.next;
			return ret;
		}
	}

	protected Element<T> head = null;

	protected int size = 0;

	public int getSize() {
		return size;
	}

	public void add(T obj) {
		Element<T> newbi;
		newbi = new Element<T>(head, obj);
		head = newbi;
		size++;
	}

	public boolean hasNext() {
		return (head != null);
	}

	public Iterator<T> iterator() {
		return new Iterator<T>(head);
	}

	/**
	 * Vide le contenue de la liste.
	 * 
	 * @param flushPool
	 *            true si vous préférez que les elements soit effacé plutôt que remit dans le pool
	 */
	public void removeAll() {
		if (head == null)
			return;
		head = null;
		size = 0;
	}

	public T removeFirst() {
		if (head == null)
			return null;
		T ret = head.obj;
		head = head.next;
		size--;
		return ret;
	}

	public void remove(T obj) {
		if (head.obj == obj)
			removeFirst();
		else {
			Element<T> current = head;
			while (current.next != null && current.next.obj != obj)
				current = current.next;

			if (current.next == null)
				return;
			current.next = current.next.next;
		}
	}

	// public static void main(String args[]) {
	// LIFO<String> lst = new LIFO<String>();
	// lst.add("1");
	// lst.add("2");
	// lst.add("3");
	// lst.add("4");
	// lst.add("5");
	// Iterator<String> it = lst.iterator();
	// while (it.hasNext())
	// System.out.println(it.next());
	// System.out.println("#");
	// lst.add("1");
	// lst.add("3");
	// it = lst.iterator();
	// while (it.hasNext())
	// System.out.println(it.next());
	// }
}
