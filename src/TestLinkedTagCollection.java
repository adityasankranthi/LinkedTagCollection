import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.function.Supplier;

import edu.uwm.cs351.Employee;
import edu.uwm.cs351.LinkedTagCollection;
import junit.framework.TestCase;

public class TestLinkedTagCollection extends TestCase {
	protected void assertException(Class<? extends Throwable> c, Runnable r) {
		try {
			r.run();
			assertFalse("Exception should have been thrown",true);
		} catch (Throwable ex) {
			assertTrue("should throw exception of " + c + ", not of " + ex.getClass(), c.isInstance(ex));
		}
	}
	
	protected Employee.Factory factory;
	protected LinkedTagCollection<Employee> self;
	protected Iterator<Employee> it;

	@Override // implementation
	protected void setUp() {
		try {
			assert it.hasNext();
			System.out.println("Assertions must be enabled for this test.");
			System.out.println("In Eclipse: Go to Run > Run Configurations ...");
			System.out.println("In the Arguments tab, add '-ea' (without quotes) to the VM Arguments box");
			assertFalse("Assertions must be enabled to run this test", true);
		} catch (NullPointerException ex) {
			assertTrue("All is OK", true);
		}
		factory = new Employee.Factory(new Random());
		self = new LinkedTagCollection<>();
		
	}
	protected String asString(Supplier<?> supp) {
		try {
			return "" + supp.get();
		} catch(RuntimeException ex) {
			return ex.getClass().getSimpleName();
		}
	}
	
	protected String asString(Runnable r) {
		return asString(() -> { r.run(); return "void"; });
	}

	/// test0x: tests of the collection itself
	
	public void test00() {
		assertEquals(0, self.size());
	}
	
	public void test01() {
		assertTrue(self.isEmpty());
	}
	
	public void test02() {
		Employee p1 = factory.get("p1");
		assertException(UnsupportedOperationException.class, () -> self.add(p1));
	}
	
	public void test03() {
		Employee p1 = factory.get("p1");
		assertEquals(true, self.add(p1, "peon"));
	}
	
	public void test04() {
		self.add(factory.get(), "peon");
		assertEquals(1, self.size());
	}
	
	public void test05() {
		self.add(factory.get(), "peon");
		self.add(factory.get(), "boss");
		assertEquals(2, self.size());
	}
	
	public void test06() {
		self.add(factory.get(), "sec");
		self.add(factory.get(), "sec");
		assertEquals(2, self.size());
	}
	
	public void test07() {
		self.add(factory.get(), "peon");
		self.add(factory.get(), "sec");
		self.add(factory.get(), "peon");
		assertEquals(3, self.size());
	}
	
	public void test08() {
		self.add(factory.get(), "boss");
		assertFalse(self.isEmpty());
	}

	
	/// test1x/2x: tests of iterator on small collections
	
	public void test10() {
		it = self.iterator();
		assertFalse(it.hasNext());
	}
	
	public void test11() {
		it = self.iterator("worker");
		assertFalse(it.hasNext());
	}
	
	public void test12() {
		it = self.iterator();
		assertException(NoSuchElementException.class, () -> it.next());
	}
	
	public void test13() {
		it = self.iterator("peon");
		assertException(NoSuchElementException.class, () -> it.next());
	}
	
	public void test14() {
		self.add(factory.get("I4"), "worker");
		it = self.iterator();
		assertTrue(it.hasNext());
	}
	
	public void test15() {
		self.add(factory.get("I5"), "face");
		it = self.iterator("back");
		assertFalse(it.hasNext());
	}
	
	public void test16() {
		self.add(factory.get("I5"), "back");
		it = self.iterator("back");
		assertTrue(it.hasNext());
	}
	
	public void test17() {
		Employee emp = factory.get("I7");
		self.add(emp, "peon");
		it = self.iterator();
		assertEquals(emp, it.next());
	}
	
	public void test18() {
		self.add(factory.get("I8"), "sec");
		it = self.iterator("boss");
		assertException(NoSuchElementException.class, () -> it.next());		
	}
	
	public void test19() {
		Employee emp = factory.get("I9");
		self.add(emp, "front");
		it = self.iterator("front");
		assertEquals(emp, it.next());
	}
	
	public void test20() {
		self.add(factory.get("#0"), "boss");
		it = self.iterator(null);
		it.next();
		assertFalse(it.hasNext());
	}
	
	public void test21() {
		self.add(factory.get("#1"), "antenna");
		it = self.iterator("antenna");
		it.next();
		assertException(NoSuchElementException.class, () -> it.next());
	}
	
	public void test22() {
		self.add(factory.get("#2"), "peon");
		self.add(factory.get("J"), "sec");
		it = self.iterator("peon");
		it.next();
		assertFalse(it.hasNext());
	}
	
	public void test23() {
		self.add(factory.get("#3"), "peon");
		Employee sec = factory.get("J");
		self.add(sec, "sec");
		it = self.iterator("sec");
		assertEquals(sec, it.next());
	}
	
	public void test24() {
		self.add(factory.get("#4"), "peon");
		self.add(factory.get("J"), "sec");
		it = self.iterator("boss");
		assertException(NoSuchElementException.class, () -> it.next());		
	}
	
	public void test25() {
		Employee peon = factory.get("#4");
		self.add(peon, "peon");
		Employee sec = factory.get("J");
		self.add(sec, "sec");
		it = self.iterator(null);
		assertEquals(peon, it.next());
		assertEquals(sec, it.next());
		assertFalse(it.hasNext());
	}
	
	public void test26() {
		Employee b1 = factory.get("#6");
		self.add(b1, "boss");
		Employee b2 = factory.get("J");
		self.add(b2, "boss");
		it = self.iterator("boss");
		assertEquals(b1, it.next());
		assertEquals(b2, it.next());
		assertException(NoSuchElementException.class, () -> it.next());		
	}
	
	public void test27() {
		Employee peon = factory.get("#7");
		self.add(peon, "peon");
		self.add(factory.get("J"), "sec");
		self.iterator().next();
		it = self.iterator(null);
		assertEquals(peon, it.next());
	}
	
	public void test28() {
		self.add(factory.get("#8"), "peon");
		Employee sec = factory.get("J");
		self.add(sec, "sec");
		it = self.iterator(null);
		self.iterator("peon").next();
		it.next();
		assertEquals(sec, it.next());
	}
	
	public void test29() {
		Employee b1 = factory.get("#9");
		self.add(b1, "boss");
		self.add(factory.get("J"), "boss");
		it = self.iterator("boss");
		self.iterator("peon");
		assertEquals(b1, it.next());
	}
	
	
	/// test3x: test of larger collections and iterators
	
	public void test30() {
		Employee a = factory.get("A");
		Employee b = factory.get("B");
		Employee c = factory.get("C");
		Employee d = factory.get("D");
		Employee e = factory.get("E");
		Employee f = factory.get("F");
		Employee g = factory.get("G");
		self.add(a, "peon");
		self.add(b, "sec");
		self.add(c, "sec");
		self.add(d, "boss");
		self.add(e, "peon");
		self.add(f, "boss");
		self.add(g, "sec");
		it = self.iterator();
		assertEquals(a, it.next());
		assertEquals(b, it.next());
		assertEquals(c, it.next());
		assertEquals(d, it.next());
		assertEquals(e, it.next());
		assertEquals(f, it.next());
		assertEquals(g, it.next());
		assertFalse(it.hasNext());
	}

	public void test31() {
		Employee a = factory.get("A");
		Employee b = factory.get("B");
		Employee c = factory.get("C");
		Employee d = factory.get("D");
		Employee e = factory.get("E");
		Employee f = factory.get("F");
		Employee g = factory.get("G");
		self.add(a, "peon");
		self.add(b, "sec");
		self.add(c, "sec");
		self.add(d, "boss");
		self.add(e, "peon");
		self.add(f, "boss");
		self.add(g, "sec");
		it = self.iterator("peon");
		assertEquals(a, it.next());
		assertTrue(it.hasNext());
		assertEquals(e, it.next());
		assertFalse(it.hasNext());
	}

	public void test32() {
		Employee a = factory.get("A");
		Employee b = factory.get("B");
		Employee c = factory.get("C");
		Employee d = factory.get("D");
		Employee e = factory.get("E");
		Employee f = factory.get("F");
		Employee g = factory.get("G");
		self.add(a, "peon");
		self.add(b, "sec");
		self.add(c, "sec");
		self.add(d, "boss");
		self.add(e, "peon");
		self.add(f, "boss");
		self.add(g, new String("sec"));
		it = self.iterator(new String("sec"));
		assertTrue(it.hasNext());
		assertEquals(b, it.next());
		assertEquals(c, it.next());
		assertEquals(g, it.next());
		assertFalse(it.hasNext());
	}

	public void test33() {
		Employee a = factory.get("A");
		Employee b = factory.get("B");
		Employee c = factory.get("C");
		Employee d = factory.get("D");
		Employee e = factory.get("E");
		Employee f = factory.get("F");
		Employee g = factory.get("G");
		self.add(a, "peon");
		self.add(b, "sec");
		self.add(c, "sec");
		self.add(d, "boss");
		self.add(e, "peon");
		self.add(f, "boss");
		self.add(g, "sec");
		it = self.iterator("boss");
		assertEquals(d, it.next());
		assertEquals(f, it.next());
		assertException(NoSuchElementException.class, () -> it.next());		
	}
	
	public void test34() {
		self.add(factory.get("A"), "peon");
		self.add(factory.get("B"), "sec");
		self.add(factory.get("C"), "sec");
		self.add(factory.get("D"), "boss");
		self.add(factory.get("E"), "peon");
		self.add(factory.get("F"), "boss");
		self.add(factory.get("G"), "sec");
		it = self.iterator("Peon");
		assertFalse(it.hasNext());
	}
	
	public void test35() {
		Employee a = factory.get("A");
		Employee b = factory.get("B");
		Employee c = factory.get("C");
		Employee d = factory.get("D");
		Employee e = factory.get("E");
		Employee f = factory.get("F");
		Employee g = factory.get("G");
		self.add(a, "peon");
		self.add(b, "sec");
		self.add(c, "sec");
		self.add(d, "boss");
		self.add(e, "peon");
		self.add(f, "boss");
		self.add(g, "sec");
		Iterator<Employee> it1 = self.iterator("sec");
		assertEquals(b, it1.next());
		Iterator<Employee> it2 = self.iterator(null);
		assertEquals(a, it2.next());
		Iterator<Employee> it3 = self.iterator("boss");
		assertEquals(d, it3.next());
		assertEquals(c, it1.next());
		assertEquals(b, it2.next());
		assertTrue(it3.hasNext());
	}

	
	/// test4x/5x: tests of iterator removal, and then clear
	
	public void test40() {
		it = self.iterator();
		assertException(IllegalStateException.class, () -> it.remove());
	}
	
	public void test41() {
		self.add(factory.get("101"), "Peon");
		it = self.iterator("Peon");
		assertException(IllegalStateException.class, () -> it.remove());
	}
	
	public void testL2() {
		self.add(factory.get("86"), "peon");
		it = self.iterator(null);
		assertException(IllegalStateException.class, () -> it.remove());
	}
	
	public void test43() {
		self.add(factory.get("256"), "sec");
		it = self.iterator(null);
		it.next();
		it.remove();
		assertTrue(self.isEmpty());
	}
	
	public void test44() {
		self.add(factory.get("#16"), "boss");
		it = self.iterator("boss");
		it.next();
		it.remove();
		assertFalse(it.hasNext());
	}
	
	public void test45() {
		self.add(factory.get("55"), "sec");
		it = self.iterator("sec");
		it.next();
		it.remove();
		it = self.iterator();
		assertFalse(it.hasNext());
	}
	
	public void test46() {
		self.add(factory.get("66"), "peon");
		Employee boss = factory.get("7");
		self.add(boss, "boss");
		it = self.iterator();
		it.next();
		it.remove();
		assertEquals(boss, self.iterator().next());
	}
	
	public void test47() {
		self.add(factory.get("666"), "worker");
		self.add(factory.get("007"), new String("worker"));
		it = self.iterator("worker");
		it.next();
		it.remove();
		assertTrue(it.hasNext());
	}
	
	public void test48() {
		Employee b1 = factory.get("81");
		self.add(b1, "boss");
		self.add(factory.get("12"), "supervisor");
		it = self.iterator("supervisor");
		it.next();
		it.remove();
		assertFalse(it.hasNext());
		assertEquals(b1, self.iterator().next());
	}
	
	public void test49() {
		self.add(factory.get("A"), "boss");
		self.add(factory.get("B"), "peon");
		Employee c = factory.get("C");
		self.add(c, "boss");
		it = self.iterator("boss");
		it.next();
		it.remove();
		assertEquals(c, it.next());
	}
	
	public void test50() {
		Employee a = factory.get("A");
		Employee b = factory.get("B");
		Employee c = factory.get("C");
		Employee d = factory.get("D");
		Employee e = factory.get("E");
		Employee f = factory.get("F");
		Employee g = factory.get("G");
		self.add(a, "peon");
		self.add(b, "sec");
		self.add(c, "sec");
		self.add(d, "boss");
		self.add(e, "peon");
		self.add(f, "boss");
		self.add(g, "sec");
		it = self.iterator();
		it.next();
		it.remove();
		it.next();
		it.next();
		it.remove();
		assertEquals(d, it.next());
		assertEquals(5, self.size());
		it = self.iterator();
		assertEquals(b, it.next());
	}
	
	public void test51() {
		Employee a = factory.get("A");
		Employee b = factory.get("B");
		Employee c = factory.get("C");
		Employee d = factory.get("D");
		Employee e = factory.get("E");
		Employee f = factory.get("F");
		Employee g = factory.get("G");
		self.add(a, "peon");
		self.add(b, "sec");
		self.add(c, "sec");
		self.add(d, "boss");
		self.add(e, "peon");
		self.add(f, "boss");
		self.add(g, "sec");
		it = self.iterator(new String("peon"));
		it.next();
		it.remove();
		it.next();
		assertFalse(it.hasNext());
		assertEquals(6, self.size());
		it = self.iterator();
		assertEquals(b, it.next());
	}
	
	public void test52() {
		Employee a = factory.get("A");
		Employee b = factory.get("B");
		Employee c = factory.get("C");
		Employee d = factory.get("D");
		Employee e = factory.get("E");
		Employee f = factory.get("F");
		Employee g = factory.get("G");
		self.add(a, "peon");
		self.add(b, "sec");
		self.add(c, new String("sec"));
		self.add(d, "boss");
		self.add(e, "peon");
		self.add(f, "boss");
		self.add(g, "sec");
		it = self.iterator("sec");
		it.next();
		it.remove();
		it.next();
		it.next();
		it.remove();
		assertFalse(it.hasNext());
		assertEquals(5, self.size());
		it = self.iterator(null);
		assertEquals(a, it.next());
		assertEquals(c, it.next());
	}
	
	public void test53() {
		Employee a = factory.get("A");
		Employee b = factory.get("B");
		Employee c = factory.get("C");
		Employee d = factory.get("D");
		Employee e = factory.get("E");
		Employee f = factory.get("F");
		Employee g = factory.get("G");
		self.add(a, "peon");
		self.add(b, "sec");
		self.add(c, new String("sec"));
		self.add(d, "boss");
		self.add(e, "peon");
		self.add(f, "boss");
		self.add(g, "sec");
		it = self.iterator("boss");
		it.next();
		it.remove();
		it.next();
		assertException(NoSuchElementException.class, () -> it.next());
		assertEquals(6, self.size());
		it = self.iterator();
		assertEquals(a, it.next());
		assertEquals(b, it.next());
		assertEquals(c, it.next());
		assertEquals(e, it.next());
	}
	
	public void test54() {
		Employee a = factory.get("A");
		Employee b = factory.get("B");
		Employee c = factory.get("C");
		Employee d = factory.get("D");
		Employee e = factory.get("E");
		Employee f = factory.get("F");
		Employee g = factory.get("G");
		self.add(a, "peon");
		self.add(b, "sec");
		self.add(c, "sec");
		self.add(d, "boss");
		self.add(e, "peon");
		self.add(f, "boss");
		self.add(g, "sec");
		it = self.iterator();
		it.next();
		it.remove();
		it.next();
		it.remove();
		it.next();
		it.remove();
		it.next();
		it.next();
		it.remove();
		it.next();
		it.remove();
		it.next();
		it.remove();
		assertFalse(it.hasNext());
		assertEquals(1, self.size());
		assertEquals(d, self.iterator().next());
	}
	
	public void test55() {
		self.clear();
		assertEquals(0, self.size());
	}
	
	public void test56() {
		self.add(factory.get("1"), "peon");
		self.clear();
		assertFalse(self.iterator().hasNext());
	}
	
	public void test57() {
		self.add(factory.get("2"), "sec");
		self.add(factory.get("3"), "boss");
		self.clear();
		assertTrue(self.isEmpty());
	}
	
	public void test58() {
		self.add(factory.get("4"), "peon");
		self.add(factory.get("5"), "peon");
		self.add(factory.get("6"), "peon");
		self.clear();
		assertException(NoSuchElementException.class, () -> self.iterator().next());
	}
	
	public void test59() {
		self.add(factory.get("A"), "peon");
		self.add(factory.get("B"), "sec");
		self.add(factory.get("C"), "sec");
		self.add(factory.get("D"), "boss");
		self.add(factory.get("E"), "peon");
		self.add(factory.get("F"), "boss");
		self.add(factory.get("G"), "sec");
		self.clear();
		assertEquals(0, self.size());
	}
	
	
	/// test6x: fail fast tests
	
	public void test60() {
		it = self.iterator();
		self.add(factory.get("0"), "peon");
		assertException(ConcurrentModificationException.class, () -> it.hasNext());
	}
	
	public void test61() {
		self.add(factory.get("1"), "sec");
		it = self.iterator("sec");
		self.add(factory.get("N1"), "peon");
		assertException(ConcurrentModificationException.class, () -> it.next());
	}
	
	public void test62() {
		self.add(factory.get("N2"), "boss");
		it = self.iterator();
		assertException(RuntimeException.class, () -> self.add(factory.get("2"), null));
		assertTrue(it.hasNext());
	}
	
	public void test63() {
		self.add(factory.get("N3"), "Sec");
		it = self.iterator("Sec");
		it.next();
		Iterator<Employee> it2 = self.iterator();
		it2.next();
		it2.remove();
		assertException(ConcurrentModificationException.class, () -> it.remove());
	}
	
	public void test64() {
		self.add(factory.get("N4"), "worker");
		Employee supe = factory.get("#4");
		self.add(supe, "supervisor");
		it = self.iterator("worker");
		self.remove(supe);
		assertException(ConcurrentModificationException.class, () -> it.hasNext());
	}
	
	public void test65() {
		self.add(factory.get("178"), "Peon");
		it = self.iterator(null);
		Iterator<Employee> it1 = self.iterator("Peon");
		it1.next();
		it1.remove();
		assertException(ConcurrentModificationException.class, () -> it.hasNext());
	}
	
	public void test66() {
		self.add(factory.get("1024"), "supervisor");
		self.add(factory.get("1215"), "worker");
		it = self.iterator();
		self.add(factory.get("1296"), "boss");
		Iterator<Employee> it2 = self.iterator("worker");
		it2.next(); 
		it2.remove();
		assertException(ConcurrentModificationException.class, () -> it.remove());
	}
	
	public void test67() {
		self.add(factory.get("43"), "peon");
		it = self.iterator();
		it.next();
		self.clear();
		assertException(ConcurrentModificationException.class, () -> it.next());
	}
	
	public void test68() {
		it = self.iterator();
		self.clear();
		assertFalse(it.hasNext());
	}
	
	public void test69() {
		self.add(factory.get("X"), "peon");
		it = self.iterator("peon");
		self.clear();
		self.add(factory.get("Y"), "sec");
		assertException(ConcurrentModificationException.class, () -> it.next());
	}
	
	
	/// test7x: clone tests
	
	protected LinkedTagCollection<Employee> c, d;
	
	public void test70() {
		c = self.clone();
		assertTrue(c.isEmpty());
	}
	
	public void test71() {
		c = self.clone();
		d = self.clone();
		Employee peon = factory.get("A");
		c.add(peon, "peon");
		d.add(factory.get("L"), "sec");
		assertEquals(peon, c.iterator().next());
	}
	
	public void test72() {
		Employee b1 = factory.get("H1");
		self.add(b1, "boss");
		self.add(factory.get("H2"), "boss");
		it = self.iterator();
		c = self.clone();
		it.next();
		it.remove();
		assertEquals(b1, c.iterator().next());
	}
	
	public void test73() {
		Employee a = factory.get("A");
		self.add(a, "Peon");
		self.add(factory.get("B"), "Leg");
		it = self.iterator("Peon");
		c = self.clone();
		Iterator<Employee> itc = c.iterator();
		itc.next();
		itc.remove();
		assertEquals(a, it.next());
	}
	
	public void test74() {
		Employee e1 = factory.get("1");
		self.add(e1, "peon");
		Employee e2 = factory.get("2");
		self.add(e2, "sec");
		Employee e3 = factory.get("3");
		self.add(e3, "Peon");
		Employee e4 = factory.get("4");
		self.add(e4, new String("peon"));
		c = self.clone();
		it = c.iterator(new String("peon"));
		assertEquals(e1, it.next());
		Employee e5 = factory.get("5");
		self.add(e5, "peon");
		assertEquals(e4, it.next());
		assertFalse(it.hasNext());
	}
	
	public void test75() {
		class Secret extends LinkedTagCollection<Employee> {
			public Secret() {
				super();
			}
		};
		self = new Secret();
		self.add(factory.get("A"), "Peon");
		c = self.clone();
		assertTrue(c instanceof Secret);
	}
	
	
	/// text8x: testing "get"
	
	public void test80() {
		assertNull(self.get(0));
	}
	
	public void test81() {
		assertNull(self.get(81));
		assertException(IllegalArgumentException.class, () -> self.get(-81));
	}
	
	public void test82() {
		Employee pat = factory.get("Pat");
		self.add(pat, "peon");
		assertSame(pat, self.get(0));
	}
	
	public void test83() {
		Employee pat = factory.get("Pat");
		self.add(pat, "peon");
		assertSame(pat, self.get(0, "peon"));
	}
	
	public void test84() {
		Employee pat = factory.get("Pat");
		self.add(pat, "peon");
		assertNull(self.get(0, "PEON"));
	}
	
	public void test85() {
		Employee pat = factory.get("Pat");
		self.add(pat, "peon");
		assertNull(self.get(1, "peon"));
	}
	
	public void test86() {
		Employee pat = factory.get("Pat");
		Employee chris = factory.get("Chris");
		self.add(pat, "peon");
		self.add(chris, "peon");
		assertSame(chris, self.get(1));
	}
	
	public void test87() {
		Employee pat = factory.get("Pat");
		Employee chris = factory.get("Chris");
		self.add(pat, "peon");
		self.add(chris, "boss");
		assertSame(chris, self.get(0, "boss"));
	}
	
	public void test88() {
		Employee pat = factory.get("Pat");
		Employee chris = factory.get("Chris");
		Employee sandy = factory.get("Sandy");
		self.add(pat, "peon");
		self.add(chris, "boss");
		self.add(sandy, new String("peon"));
		assertSame(sandy, self.get(1, new String("peon")));
	}
	
	public void test89() {
		Employee a = factory.get("A");
		Employee b = factory.get("B");
		Employee c = factory.get("C");
		Employee d = factory.get("D");
		Employee e = factory.get("E");
		Employee f = factory.get("F");
		Employee g = factory.get("G");
		self.add(a, "peon");
		self.add(b, "sec");
		self.add(c, "sec");
		self.add(d, "boss");
		self.add(e, "peon");
		self.add(f, "boss");
		self.add(g, "sec");
		it = self.iterator("sec");
		assertEquals(a, self.get(0, "peon"));
		it.next();
		it.remove();
		assertEquals(c, self.get(1, null));
		it.next();
		assertEquals(g, it.next());
		it.remove();
		assertEquals(e, self.get(1, "peon"));
	}
}
