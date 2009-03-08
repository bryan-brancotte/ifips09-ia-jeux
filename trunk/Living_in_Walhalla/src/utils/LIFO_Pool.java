package utils;

public class LIFO_Pool<T> {

	protected static class Element<TE> {
		protected Element<TE> next = null;
		protected TE obj;

		protected void init(Element<TE> next, TE obj) {
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

	protected Element<T> pool = null;

	public void add(T obj) {
		Element<T> newbi;
		if (pool == null) {
			newbi = new Element<T>();
		} else {
			newbi = pool;
			pool = pool.next;
		}
		newbi.init(head, obj);
		head = newbi;
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
	public void removeAll(boolean flushPool) {
		if (head == null)
			return;
		if (flushPool) {
			Element<T> target = head;
			while (target.next != null) {
				target.obj = null;
				target = target.next;
			}
			target.next = pool;
			pool = target;
		}
		head = null;
	}

	public T removeFirst() {
		if (head == null)
			return null;
		T ret = head.obj;
		Element<T> freeElement = head;
		head = head.next;
		freeElement.next = pool;
		freeElement.obj = null;
		pool = freeElement;
		return ret;
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
