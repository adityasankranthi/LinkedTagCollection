package edu.uwm.cs351;

import java.util.AbstractCollection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class LinkedTagCollection // need to add parameter, extends and implements
{ 	
	private static Consumer<String> reporter = (s) -> System.out.println("Invariant error: "+ s);

	private boolean report(String error) {
		reporter.accept(error);
		return false;
	}
	
	// TODO: Data structure
	
	private boolean wellFormed() {
		//TODO: Complete this.  Cyclic doubly-linked list with a dummy node.
		// Think: What could possibly go wrong?
		// If no problems discovered, return true
		return true;
	}
	
		
	private LinkedTagCollection(boolean ignored) {} // do not change this constructor
	
	/** Construct an empty tagged collection.
	 */
	public LinkedTagCollection(){
		// DO NOT ASSERT invariant at beginning of constructor!
		// TODO: set up data structure
		assert wellFormed(): "invariant broken by constructor";		
	}
	

	// TODO: All the methods!
	// Make sure to properly document each.
	// NB: "clone" may suppress warnings about "unchecked"
	// (Follow the textbook and previous homeworks on the structure for clone.) 
		
	private class MyIterator // TODO: implements ...
	{
		Node<E> cur, next;
		int colVersion;
		String tag;
			
		private boolean wellFormed() {
			// TODO: check outer and version and then
			// what could go wrong?
			return true;
		}
			
		MyIterator(boolean ignored) {} // do not change this constructor
			
		MyIterator(String t) {
			// TODO: initialize fields
			// (We use a helper method)
			assert wellFormed() : "iterator constructor didn't satisfy invariant";
		}
			
		// TODO: Body of iterator class
	}
		
	public static class Spy<T> {
		/**
		 * A public version of the data structure's internal node class.
		 * This class is only used for testing.
		 */
		public static class Node<U> extends LinkedTagCollection.Node<U> {
			/**
			 * Create a node with null data, tag, prev, and next fields.
			 */
			public Node() {
				this(null, null, null, null);
			}
			/**
			 * Create a node with the given values
			 * @param d data for new node, may be null
			 * @param t tag for new node,may be null
			 * @param p prev for new node, may be null
			 * @param n next for new node, may be null
			 */
			public Node(U d, String t, Node<U> p, Node<U> n) {
				super(null, null);
				this.tag = t;
				this.data = d;
				this.prev = p;
				this.next = n;
			}
		}
		
		/**
		 * Create a node for testing.
		 * @param d data for new node, may be null
		 * @param t tag for new node,may be null
		 * @param p prev for new node, may be null
		 * @param n next for new node, may be null
		 * @return newly created test node
		 */
		public Node<T> newNode(T d, String t, Node<T> p, Node<T> n) {
			return new Node<T>(d, t, p, n);
		}
		
		/**
		 * Create a node with all null fields for testing.
		 * @return newly created test node
		 */
		public Node<T> newNode() {
			return new Node<T>();
		}
		
		/**
		 * Change a node's next field
		 * @param n1 node to change, must not be null
		 * @param n2 node to point to, may be null
		 */
		public void setNext(Node<T> n1, Node<T> n2) {
			n1.next = n2;
		}
		
		/**
		 * Change a node's prev field
		 * @param n1 node to change, must not be null
		 * @param n2 node to point to, may be null
		 */
		public void setPrev(Node<T> n1, Node<T> n2) {
			n1.prev = n2;
		}

		/**
		 * Return the sink for invariant error messages
		 * @return current reporter
		 */
		public Consumer<String> getReporter() {
			return reporter;
		}

		/**
		 * Change the sink for invariant error messages.
		 * @param r where to send invariant error messages.
		 */
		public void setReporter(Consumer<String> r) {
			reporter = r;
		}

		/**
		 * Create a testing instance of a linked tag collection with the given
		 * data structure.
		 * @param d the dummy node
		 * @param s the size
		 * @param v the version
		 * @return a new testing linked tag collection with this data structure.
		 */
		public <U> LinkedTagCollection<U> newInstance(Node<U> d, int s, int v) {
			LinkedTagCollection<U> result = new LinkedTagCollection<U>(false);
			result.dummy = d;
			result.size = s;
			result.version = v;
			return result;
		}
			
		/**
		 * Creates a testing instance of an iterator
		 * @param outer the collection attached to the iterator
		 * @param t the tag
		 * @param c the current node
		 * @param n the next node
		 * @param cv the colVersion
		 */
		public <E> Iterator<E> newIterator(LinkedTagCollection<E> outer, String t, Node<E> c, Node<E> n, int cv) {
			LinkedTagCollection<E>.MyIterator result = outer.new MyIterator(false);
			result.tag = t;
			result.cur = c;
			result.next = n;
			result.colVersion = cv;
			return result;
		}
			
		/**
		 * Check the invariant on the given dynamic array robot.
		 * @param r robot to check, must not be null
		 * @return whether the invariant is computed as true
		 */
		public boolean wellFormed(LinkedTagCollection<?> r) {
			return r.wellFormed();
		}
			
		/**
		 * Check the invariant on the given Iterator.
		 * @param it iterator to check, must not be null
		 * @return whether the invariant is computed as true
		 */
		public <E> boolean wellFormed(Iterator<E> it) {
			LinkedTagCollection<E>.MyIterator myIt = (LinkedTagCollection<E>.MyIterator)it;
			return myIt.wellFormed();
		}
	}
}
	
	

