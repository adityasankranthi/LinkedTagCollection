package edu.uwm.cs351;

import java.util.Random;
import java.util.function.Supplier;

/**
 * A simple immutable class representing an employee,
 * with a name and identification number.  
 * Because two employees could have the same name,
 * and because occasionally an employee goes by a different name
 * the identification number is the "real" way that two
 * employees are distinguished.
 */
public class Employee {
	private final String name;
	private final String id;
	
	/**
	 * Create a new employee structure
	 * @param n name (advisory)
	 * @param i identification (\number (actual identity)
	 */
	public Employee(String n, String i) {
		name = n;
		id = i;
	}
	
	/**
	 * Return the name of this employee object.
	 * @return the name
	 */
	public String getName() { return name; }
	
	/**
	 * Return the identification of the employee
	 * @return identification
	 */
	public String getId() { return id; }
	
	@Override // implementation
	public String toString() {
		return name + "(" + id + ")";
	}
	
	@Override // implementation
	public boolean equals(Object obj) {
		if (!(obj instanceof Employee)) return false;
		Employee other = (Employee)obj;
		return id.equals(other.id);
	}
	
	@Override // implementation
	public int hashCode() {
		return id.hashCode();
	}
	
	/**
	 * A class to manufacture random employees 
	 * for testing purposes
	 */
	public static class Factory implements Supplier<Employee> {
		private Random random;
		public Factory(Random r) {
			random = r;
		}
		
		private static String FIRST[] = {
				"Alex", "Avery", "Chris", "Drew", "Pat", "Sandy"
		};
		private static String LAST[] = {
				"Chang", "Garcia", "Johnson", "Lau", 
				"Lee", "Modi", "Reddy", "Smith",
				"Xiong", "Ziegler"
		};
		
		@Override // required
		public Employee get() {
			return get(
					FIRST[random.nextInt(FIRST.length)] + " " +
					LAST[random.nextInt(LAST.length)]);
		}
		
		/**
		 * Return a new employee object with the given name and a random ID.
		 * @param name name to use
		 * @return new employee object
		 */
		public Employee get(String name) {
			return new Employee(name, "S-" + random.nextInt());
		}
	}
}
