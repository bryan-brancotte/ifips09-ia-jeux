package utils;


public class SortedList<T> {

	protected Element<T> pool = null;

	protected Element<T> head = null;

	public boolean add(T obj, float cost) {
		if (!remove(obj, cost))
			return false;
		Element<T> target = head;
		Element<T> parent = null;
		Element<T> newbi;
		while (target != null && target.cost < cost) {
			parent = target;
			target = target.next;
		}
		if (pool == null) {
			newbi = new Element<T>();
		} else {
			newbi = pool;
			pool = pool.next;
		}
		newbi.init(target, cost, obj);

		if (parent == null)
			head = newbi;
		else
			parent.next = newbi;
		return true;
	}

	/**
	 * Retire l'element passé en paramètre s'il est présent et que son coût est supérieur à celui passé en paramètre
	 * 
	 * @param obj
	 * @param cost
	 * @return true si on l'a effectivement retiré
	 */
	protected boolean remove(T obj, float cost) {
		Element<T> target = head;
		Element<T> parent = null;
		while (target != null && target.obj != obj) {
			parent = target;
			target = target.next;
		}
		if (target == null)
			return true;
		if (parent == null) {
			if (head.cost <= cost)
				return false;
			removeFirst();
		} else {
			if (target.cost <= cost)
				return false;
			Element<T> freeElement = parent.next;
			parent.next = parent.next.next;
			freeElement.next = pool;
			freeElement.obj = null;
			pool = freeElement;
		}
		return true;
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

	public boolean hasNext() {
		return (head != null);
	}

	public String toString() {
		String ret = "SortedList<T>: (size,pool)=(";
		int i = 0;
		Element<T> target = head;
		while (target != null) {
			i++;
			target = target.next;
		}
		ret += i + ",";
		i = 0;
		target = pool;
		while (target != null) {
			i++;
			target = target.next;
		}
		ret += i + ") Head(";
		if (head != null)
			ret += head.cost + ")";
		else
			ret += "null)";
		return ret;
	}
	
	public int size(){
		int i=0;
		Element<T> target = head;
		while (target != null) {
			i++;
			target = target.next;
		}
		return i;
	}

	protected static class Element<TE> {
		protected Element<TE> next = null;
		protected float cost = 0;
		protected TE obj;

		protected void init(Element<TE> next, float cost, TE obj) {
			this.next = next;
			this.cost = cost;
			this.obj = obj;
		}
	}

	// public static void main(String args[]) {
	// SortedList<String> lst = new SortedList<String>();
	// lst.add("1", 1);
	// lst.add("3", 3);
	// lst.add("2", 2);
	// lst.add("0", 0);
	// lst.add("5", 5);
	// while (lst.hasNext())
	// System.out.println(lst + " => " + lst.removeFirst());
	// lst.add("1", 1);
	// lst.add("3", 3);
	// while (lst.hasNext())
	// System.out.println(lst + " => " + lst.removeFirst());
	// }
}
