import java.util.Iterator;

import edu.uwm.cs.junit.EfficiencyTestCase;
import edu.uwm.cs351.LinkedTagCollection;
import edu.uwm.cs351.TagCollection;


public class TestEfficiency extends EfficiencyTestCase {
	LinkedTagCollection<Integer> s;
	TagCollection<Integer> c;
	Iterator<Integer> it;
	
	@Override
	public void setUp() {
		s = new LinkedTagCollection<Integer>();
		c = s;
		try {
			assert 1/s.size() < 0 : "OK";
			assertTrue(true);
		} catch (ArithmeticException ex) {
			System.err.println("Assertions must NOT be enabled to use this test suite.");
			System.err.println("In Eclipse: remove -ea from the VM Arguments box under Run>Run Configurations>Arguments");
			assertFalse("Assertions must NOT be enabled while running efficiency tests.",true);
		}
		super.setUp();
	}

	private static final int POWER = 20;
	private static final int MAX_LENGTH = 1 << POWER;
	
	public void test0() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			assertEquals(i, c.size());
			c.add(1, "x");
		}
	}

	public void test1() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			c.add(i, "x");
			assertEquals(Integer.valueOf(0), c.get(0, "x"));
		}
		assertEquals(Integer.valueOf(MAX_LENGTH-1), c.get(MAX_LENGTH-1));
	}
		
	public void test2() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			c.add(MAX_LENGTH-i, "x");
		}
		for (int i=0; i < (1 << (POWER/2)); ++i) { // allow linear time
			assertEquals(Integer.valueOf(MAX_LENGTH-i), c.get(i));
		}
	}

	public void test3() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			c.add(i, "tail");
			assertEquals(Integer.valueOf(0), c.iterator("tail").next());
		}
	}

	public void test4() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			c.add(i, "a");
		}
		it = c.iterator();
		for (int i=0; i < MAX_LENGTH; ++i) {
			assertEquals(Integer.valueOf(i), it.next());
		}
	}

	private static final String[] TAGS = {"A", "B", "C", "D" };
	
	public void test5() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			String f = TAGS[i % TAGS.length];
			c.add(i, f);
		}
		for (int offset = 0; offset < TAGS.length; ++offset) {
			it = s.iterator(TAGS[offset]);
			for (int i= offset; i < MAX_LENGTH; i += TAGS.length) {
				assertEquals(Integer.valueOf(i), it.next());
			}
			assertFalse(it.hasNext());
		}
	}

	public void test6() {
		s.add(0, "a");
		for (int i=1; i < MAX_LENGTH/2; ++i) {
			c.add(i, "A");
		}
		c.add((MAX_LENGTH/2), new String("a"));
		for (int i=MAX_LENGTH/2+1; i < MAX_LENGTH; ++i) {
			c.add(i, "A");
		}
		it = c.iterator(new String("a"));
		for (int i = 0; i < MAX_LENGTH; ++i) {
			assertTrue(it.hasNext());
		}
		it.next();
		for (int i = 0; i < MAX_LENGTH; ++i) {
			assertTrue(it.hasNext());
		}
		it.next();
		for (int i = 0; i < MAX_LENGTH; ++i) {
			assertFalse(it.hasNext());
		}
	}	
	
	public void test7() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			c.add(i, "x");
		}
		c.clear();
		assertEquals(0, c.size());
	}
}
