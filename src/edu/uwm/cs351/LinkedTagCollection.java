package edu.uwm.cs351;

import java.util.AbstractCollection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class LinkedTagCollection<E> extends AbstractCollection<E> implements TagCollection<E>, Cloneable
// need to add parameter, extends and implements
{ 	
	

	private static Consumer<String> reporter = (s) -> System.out.println("Invariant error: "+ s);

	private boolean report(String error) {
		reporter.accept(error);
		return false;
	}
	
	// TODO: Data structure
	Node<E> dummy;
	int version;
	int size;
	
	private boolean wellFormed() {
		//TODO: Complete this.  Cyclic doubly-linked list with a dummy node.
		// Think: What could possibly go wrong?
		// 1. dummy is not null
	    if (dummy == null) {
	        return report("Dummy node is null");
	    }

	    // 2. Check if dummy's data and tag fields are both null
	    if (dummy.data != null || dummy.tag != null) {
	        return report("Dummy node's data or tag is not null");
	    }

	    // 3. The node are linked in a cycle beginning and ending with the dummy
	    // 4. Check if prev links of a node always point back to the node whose next field pointed to them
	    Node<E> current = dummy.next;
	    Node<E> prevNode = dummy;
	    boolean dummySeen = false;

	    if (size == 0) {
	    	if (dummy.next != dummy || dummy.prev != dummy) return report("Incorrect prev link in a node");
	    }
	    else {
		    while (!dummySeen && current != null) {
		    	if (current == dummy) dummySeen = true;
		        if (current.prev != prevNode) {
		            return report("Incorrect prev link in a node");
		        }
		        prevNode = current;
		        current = current.next;
		    }
	    }

	    // 5. Check if none of the links in any of the nodes are null
	    // 6. Check if none of the tags in any of the non-dummy nodes are null
	    int count = 0;
	    current = dummy.next;
	    while (current != dummy && current != null) {
	        count++;
	        if (current.prev == null || current.next == null) return report("Null link in a node");
	        if (current.tag == null) return report("Null tag in a node");
	        current = current.next;
	    }
	    
	    // 7. Check if the size field correctly counts the number of non-dummy nodes in the cyclic list
	    if (count != size) {
	        return report("Size field does not match the number of non-dummy nodes");
	    }

		// If no problems discovered, return true
	    return true;
	}
	
	private LinkedTagCollection(boolean ignored) {} // do not change this constructor
	
	/** Construct an empty tagged collection.
	 */
	public LinkedTagCollection(){
		// DO NOT ASSERT invariant at beginning of constructor!
		// TODO: set up data structure
		dummy = new Node<E>();
		dummy.next = dummy;
		dummy.prev = dummy;
		size = 0;
		version = 0;
		assert wellFormed(): "invariant broken by constructor";		
	}
	
	// TODO: All the methods!
	// Make sure to properly document each.
	// NB: "clone" may suppress warnings about "unchecked"
	// (Follow the textbook and previous home works on the structure for clone.) 
		

	@Override // required
	public boolean add(E element, String tag) {
	    assert wellFormed() : "Invariant broken befor adding element";
		if (tag == null) {
	        throw new NullPointerException("Tag cannot be null");
	    }
	    Node<E> newNode = new Node<>(element, tag);
	    newNode.next = dummy;
	    newNode.prev = dummy.prev;
	    dummy.prev.next = newNode;
	    dummy.prev = newNode;
	    size++;
	    version++;
	    assert wellFormed() : "Invariant broken after adding element";
	    return true;
	}

	@Override // required
	public E get(int i) {
		return get(i, null);
	}
	
	@Override // required
	public E get(int i, String tag) {
		
		return null; // TODO
	}
	
	@Override // required
	public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override //required
	public Iterator<E> iterator(String string) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override // required
	public int size() {
		// TODO Auto-generated method stub
		return size;
	}
	
	@Override // implementation
	public LinkedTagCollection<E> clone() {
		LinkedTagCollection<E> result = null;
		return result;
	}
	
	private static class Node<T> {
		String tag;
		T data;
		
		Node<T> prev;
		Node<T> next;
		private Node() {
			this(null, null);
		}
		private Node(T d, String t) {
			this.tag = t;
			this.data = d;
			
		}
	}

	private class MyIterator implements Iterator<E>// TODO: implements ...
	{
		Node<E> cur, next;
		int colVersion;
		String tag;
			
		private boolean wellFormed() {
			// TODO: check outer and version and then
            if (!LinkedTagCollection.this.wellFormed()) return false;
            if (colVersion != LinkedTagCollection.this.version) return true;
            
            // what could go wrong?
            if (cur == null || next == null) return report("cur and next fields are null ");
            
            //The next pointer should be the node where the next element to be returned
            //by the iterator lives
            if (cur != next ) {
            	if (tag == null) {
            		if (cur.next != next) return report("cur and next fields are inconsistent");
            	}
            	else {
            		Node<E> lag = cur;
                    Node<E> current = cur.next;
                    while (current != dummy && current != null) {
                        if (current.tag != null && current.tag.equals(cur.tag)) {
                            break;
                        }
                        current = current.next;
                    }

                    // Check if lag and current are consistent with cur and next
                    if (lag != cur || current != next) {
                        return report("cur and next fields are not consistent");
                    }
                    // cur and next tags are the same
	            	if (cur.tag != null && next.tag != null) {
	            		if ((!cur.tag.equals(next.tag)) || (!cur.tag.equals(tag)))return report("cur and next fields are inconsistent");
	            	}
	            	else if (cur.tag != null) {
	        			if ((!cur.tag.equals(tag))) return report("cur and next fields are inconsistent");
	        		}
            	}
                
            }
            
            Node<E> current = dummy.next;
            while (current != dummy && current != null) {
                if (current == cur || current == next) {
                    break;
                }
                current = current.next;
            }
            if (current != cur ) {
                return report("cur is not within the bounds of the collection");
            }
            
			return true;
		}
			
		MyIterator(boolean ignored) {} // do not change this constructor
			
		MyIterator(String t) {
			// TODO: initialize fields
			// (We use a helper method)
			assert wellFormed() : "iterator constructor didn't satisfy invariant";
		}
		
		@Override // required
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override // required
		public E next() {
			// TODO Auto-generated method stub
			return null;
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
	
	

