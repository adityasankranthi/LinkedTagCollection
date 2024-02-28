package edu.uwm.cs351;

import java.util.Collection;
import java.util.Iterator;

/**
 * A collection where each element in the collection is associated with a tag, a string.
 * This collection does not support the normal {@link Collection#add} method.
 * @param E type of the elements.
 */
public interface TagCollection<E> extends Collection<E> {
	
	/**
	 * Add an element to the collection with the given tag.
	 * @param element element to add, which may be null if the collection permits it
	 * @param tag tag to use, must not be null
	 * @return true 
	 */
	public boolean add(E element, String tag);
	
	/**
	 * Return an element from the collection by 0-based index.
	 * @param i 0-based index, must not be negative
	 * @return the i'th element or null if no such element
	 * @exception IllegalArgumentException if i is negative
	 */
	public default E get(int i) {
		return get(i, null);
	}
	
	/**
	 * Return an element from the collection with the given tag.
	 * If the tag is null, it gets any element
	 * @param i 0-based index, must not be negative
	 * @param tag tag to look for, or if null, accept any tag.
	 * @return the i'th element with that tag or null f no such element
	 * @exception IllegalArgumentException if i is negative
	 */
	public default E get(int i, String tag) {
		return null; // TODO
	}
	
	/**
	 * Iterate over the elements in this collection that have the given tag.
	 * @param tag tag to use, if null, then iterator over all elements
	 * @return iterator over the elements of this collection that have the given tag, never null
	 */
	public Iterator<E> iterator(String tag);
}
