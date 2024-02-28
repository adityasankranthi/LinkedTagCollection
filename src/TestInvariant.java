import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Supplier;

import edu.uwm.cs351.LinkedTagCollection;
import edu.uwm.cs351.Part;
import junit.framework.TestCase;

public class TestInvariant extends TestCase {
	
	protected LinkedTagCollection.Spy<Part> spy;
	protected int reports;
	protected LinkedTagCollection<Part> r;
	protected Iterator<Part> it;
	
	protected void assertReporting(boolean expected, Supplier<Boolean> test) {
		reports = 0;
		Consumer<String> savedReporter = spy.getReporter();
		try {
			spy.setReporter((String message) -> {
				++reports;
				if (message == null || message.trim().isEmpty()) {
					assertFalse("Uninformative report is not acceptable", true);
				}
				if (expected) {
					assertFalse("Reported error incorrectly: " + message, true);
				}
			});
			assertEquals(expected, test.get().booleanValue());
			if (!expected) {
				assertEquals("Expected exactly one invariant error to be reported", 1, reports);
			}
			spy.setReporter(null);
		} finally {
			spy.setReporter(savedReporter);
		}
	}
	
	protected void assertWellFormed(boolean expected, LinkedTagCollection<Part> r) {
		assertReporting(expected, () -> spy.wellFormed(r));
	}
	protected void assertWellFormed(boolean expected, Iterator<Part> it) {
		assertReporting(expected, () -> spy.wellFormed(it));
	}
	
	protected LinkedTagCollection.Spy.Node<Part> d;

	@Override // implementation
	protected void setUp() {
		spy = new LinkedTagCollection.Spy<Part>();
		d = spy.newNode();
	}
	
	public void testA0() {
		r = spy.newInstance(null, 0, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testA1() {
		d = null;
		r = spy.newInstance(d, 0, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testA2() {
		d = null;
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("2"), "arm", d, null);
		spy.setNext(n0, d);
		r = spy.newInstance(d, 1, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testB0() {
		d = spy.newNode(null, "arm", null, null);
		spy.setNext(d, d);
		spy.setPrev(d, d);
		r = spy.newInstance(d, 0, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testB1() {
		d = spy.newNode(null, "arm", null, null);
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("1"), "arm", d, d);
		spy.setNext(d, n0);
		spy.setPrev(d, n0);
		r = spy.newInstance(d, 1, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testC0() {
		d = spy.newNode(new Part("0"), null, null, null);
		spy.setNext(d, d);
		spy.setPrev(d, d);
		r = spy.newInstance(d, 0, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testC1() {
		d = spy.newNode(new Part("1"), null, null, null);
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("C1"), "arm", d, d);
		spy.setNext(d, n0);
		spy.setPrev(d, n0);
		r = spy.newInstance(d, 1, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testC2() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("C2"), "arm", d, d);
		spy.setNext(d, n0);
		spy.setPrev(d, n0);
		r = spy.newInstance(n0, 1, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testD0() {
		r = spy.newInstance(d, 0, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testD1() {
		spy.setPrev(d, d);
		r = spy.newInstance(d, 0, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testD2() {
		spy.setPrev(d, d);
		r = spy.newInstance(d, 0, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testD3() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("3"), "arm", d, d);
		spy.setPrev(d, n0);
		r = spy.newInstance(d, 1, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testD4() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("4"), "arm", null, d);
		spy.setPrev(d, n0);
		spy.setNext(d, n0);
		r = spy.newInstance(d, 1, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testD5() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("5"), "arm", null, d);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("D5"), "leg", n0, d);
		spy.setPrev(d, n1);
		spy.setNext(d, n0);
		r = spy.newInstance(d, 2, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testD6() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("6"), "arm", null, d);
		LinkedTagCollection.Spy.Node<Part> n1 = null;
		spy.setPrev(d, n1);
		spy.setNext(d, n0);
		r = spy.newInstance(d, 1, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testD7() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("7"), "arm", null, d);
		LinkedTagCollection.Spy.Node<Part> n1 = null;
		spy.setPrev(d, n1);
		spy.setNext(d, n0);
		r = spy.newInstance(d, 2, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testD8() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("8"), "arm", null, d);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("D8"), "leg", n0, d);
		spy.setPrev(d, n1);
		spy.setNext(d, n0);
		spy.setNext(n0, null);
		r = spy.newInstance(d, 2, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testE0() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("0"), null, d, d);
		spy.setPrev(d, n0);
		spy.setNext(d, n0);
		r = spy.newInstance(d, 1, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testE1() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("1"), null, d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("E1"), "leg", n0, d);
		spy.setPrev(d, n1);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		r = spy.newInstance(d, 2, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testE2() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("2"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("E2"), null, n0, d);
		spy.setPrev(d, n1);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		r = spy.newInstance(d, 2, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testE3() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("3"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("E3"), "leg", n0, d);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-E3"), null, d, n0);
		spy.setPrev(d, n1);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n2, n2);
		r = spy.newInstance(d, 2, 0);
		assertReporting(true, () -> spy.wellFormed(r));
	}
	
	public void testF0() {
		spy.setNext(d, d);
		r = spy.newInstance(d, 0, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testF1() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("1"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("F1"), "leg", n0, d);
		spy.setPrev(d, d);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		r = spy.newInstance(d, 2, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testF2() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("2"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("F2"), "leg", d, d);
		spy.setPrev(d, n1);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		r = spy.newInstance(d, 2, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testF3() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("3"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("F3"), "leg", n0, d);
		spy.setPrev(d, n0);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		r = spy.newInstance(d, 2, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testF4() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("4"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("F4"), "leg", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-F4"), "antenna", n0, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X4"), "head", n2, d);
		spy.setPrev(d, n3);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		r = spy.newInstance(d, 4, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testF5() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("5"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("F5"), "leg", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-F5"), "antenna", n0, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X5"), "head", n2, d);
		spy.setPrev(d, n3);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setPrev(n2, n1);
		spy.setNext(n2, n3);
		r = spy.newInstance(d, 4, 0);
		assertReporting(true, () -> spy.wellFormed(r));
	}
	
	public void testF6() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("6"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("F6"), "leg", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-F6"), "antenna", n0, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X6"), "head", n2, d);
		spy.setPrev(d, n3);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setPrev(n2, n3);
		spy.setNext(n2, n3);
		r = spy.newInstance(d, 4, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testG0() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("0"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("G0"), "leg", n0, null);
		spy.setPrev(d, n1);
		spy.setNext(d, n0);
		spy.setNext(n0, d);
		spy.setNext(n1, d);
		r = spy.newInstance(d, 1, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testG1() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("1"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("G1"), "leg", n0, null);
		spy.setPrev(d, n1);
		spy.setNext(d, n0);
		spy.setNext(n0, d);
		spy.setNext(n1, d);
		r = spy.newInstance(d, 2, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testG2() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("2"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("G2"), "leg", n0, null);
		spy.setPrev(d, n0);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, d);
		r = spy.newInstance(d, 2, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testG3() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("3"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("G3"), "leg", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-G3"), "antenna", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X3"), "head", n2, d);
		spy.setPrev(d, n1);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		r = spy.newInstance(d, 4, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testG4() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("4"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("G4"), "leg", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-G4"), "antenna", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X4"), "head", n2, d);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		r = spy.newInstance(d, 4, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testH0() {
		r = spy.newInstance(d, 0, 0);
		spy.setPrev(d, d);
		spy.setNext(d, d);
		assertReporting(true, () -> spy.wellFormed(r));
	}
	
	public void testH1() {
		r = spy.newInstance(d, 1, 0);
		spy.setPrev(d, d);
		spy.setNext(d, d);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testH2() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("2"), "arm", d, null);
		spy.setPrev(d, d);
		spy.setNext(d, d);
		spy.setNext(n0, d);
		r = spy.newInstance(d, 1, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testH3() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("3"), "arm", d, d);
		spy.setPrev(d, n0);
		spy.setNext(d, n0);
		r = spy.newInstance(d, 1, 0);
		assertReporting(true, () -> spy.wellFormed(r));
	}
	
	public void testH4() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("4"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("H4"), "leg", n0, d);
		spy.setPrev(d, n1);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		r = spy.newInstance(d, 2, 0);
		assertReporting(true, () -> spy.wellFormed(r));
	}
	
	public void testH5() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("5"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("H5"), "leg", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-H5"), "antenna", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X5"), "head", n2, d);
		spy.setPrev(d, n3);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		r = spy.newInstance(d, 3, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testH6() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("6"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("H6"), "leg", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-H6"), "antenna", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X6"), "head", n2, d);
		spy.setPrev(d, n3);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		r = spy.newInstance(d, 4, 0);
		assertReporting(true, () -> spy.wellFormed(r));
	}
	
	public void testH7() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("7"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("H7"), "leg", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-H7"), "antenna", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X7"), "head", n2, d);
		spy.setPrev(d, n3);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		r = spy.newInstance(d, 5, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testH8() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("8"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("H8"), "leg", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-H8"), "antenna", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X8"), "head", n2, d);
		spy.setPrev(d, n3);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		r = spy.newInstance(d, -1, 0);
		assertReporting(false, () -> spy.wellFormed(r));
	}
	
	public void testH9() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("9"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("H9"), "leg", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-H9"), "antenna", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-H9"), "head", n2, d);
		LinkedTagCollection.Spy.Node<Part> n4 = spy.newNode(new Part("X9"), "arm", n3, null);
		LinkedTagCollection.Spy.Node<Part> n5 = spy.newNode(new Part("SN-X9"), "leg", n4, null);
		LinkedTagCollection.Spy.Node<Part> n6 = spy.newNode(new Part("RSN-X9"), "head", n5, n4);
		spy.setPrev(d, n3);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n4, n5);
		spy.setNext(n5, n6);
		r = spy.newInstance(d, 4, 0);
		assertReporting(true, () -> spy.wellFormed(r));
	}
	
	public void testI0() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("0"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("I0"), "leg", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-I0"), "antenna", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X0"), "head", n2, d);
		spy.setPrev(d, n3);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		r = spy.newInstance(d, 4, 0);
		it = spy.newIterator(r, null, d, d, 0);
		assertReporting(true, () -> spy.wellFormed(it));
	}
	
	public void testI1() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("1"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("I1"), "leg", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-I1"), "antenna", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X1"), "head", n2, d);
		spy.setPrev(d, n3);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		r = spy.newInstance(d, 3, 0);
		it = spy.newIterator(r, null, d, d, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testI2() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("2"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("I2"), "leg", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-I2"), "antenna", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X2"), "head", n2, d);
		spy.setPrev(d, n2);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		r = spy.newInstance(d, 4, 0);
		it = spy.newIterator(r, null, d, d, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testI3() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("3"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("I3"), "leg", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-I3"), "antenna", n0, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X3"), "head", n2, d);
		spy.setPrev(d, n3);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		r = spy.newInstance(d, 4, 0);
		it = spy.newIterator(r, null, d, d, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testI4() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("4"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("I4"), null, n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-I4"), "antenna", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X4"), "head", n2, d);
		spy.setPrev(d, n3);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		r = spy.newInstance(d, 4, 0);
		it = spy.newIterator(r, null, d, d, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testI5() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("5"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("I5"), "leg", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-I5"), "antenna", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X5"), "head", n2, d);
		spy.setPrev(d, n3);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		r = spy.newInstance(d, 4, 0);
		it = spy.newIterator(r, null, d, d, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testJ0() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("0"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("J0"), "leg", n0, d);
		spy.setPrev(d, n1);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		r = spy.newInstance(d, 2, 1);
		it = spy.newIterator(r, null, d, d, 0);
		assertReporting(true, () -> spy.wellFormed(it));
	}
	
	public void testJ1() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("1"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("J1"), "leg", n0, d);
		spy.setPrev(d, n1);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		r = spy.newInstance(d, 2, 1);
		it = spy.newIterator(r, null, d, d, 1);
		assertReporting(true, () -> spy.wellFormed(it));
	}
	
	public void testJ2() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("2"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("J2"), "leg", n0, d);
		spy.setPrev(d, n1);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		r = spy.newInstance(d, 2, 1);
		it = spy.newIterator(r, null, d, d, 2);
		assertReporting(true, () -> spy.wellFormed(it));
	}
	
	public void testK0() {
		r = spy.newInstance(d, 0, 0);
		spy.setPrev(d, d);
		spy.setNext(d, d);
		it = spy.newIterator(r, null, null, d, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testK1() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("1"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("K1"), "leg", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-K1"), "antenna", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X1"), "head", n2, d);
		spy.setPrev(d, n3);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		r = spy.newInstance(d, 4, 0);
		it = spy.newIterator(r, null, null, d, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testK2() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("2"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("K2"), "leg", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-K2"), "antenna", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X2"), "head", n2, d);
		spy.setPrev(d, n3);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		r = spy.newInstance(d, 4, 0);
		it = spy.newIterator(r, null, null, n0, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testK3() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("3"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("K3"), "leg", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-K3"), "antenna", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X3"), "head", n2, d);
		spy.setPrev(d, n3);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		r = spy.newInstance(d, 4, 0);
		it = spy.newIterator(r, null, null, n1, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testL0() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("0"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("L0"), "leg", n0, null);
		spy.setPrev(d, d);
		spy.setNext(d, d);
		spy.setNext(n0, n1);
		spy.setNext(n1, d);
		r = spy.newInstance(d, 0, 0);
		it = spy.newIterator(r, null, n0, n1, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testL1() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("1"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("L1"), "leg", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-L1"), "leg", n1, d);
		LinkedTagCollection.Spy.Node<Part> i1 = spy.newNode(new Part("SN-i1"), "leg", n0, n1);
		spy.setPrev(d, n2);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		r = spy.newInstance(d, 3, 0);
		it = spy.newIterator(r, null, i1, n1, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testL2() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("2"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("L2"), "leg", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-L2"), "leg", n1, d);
		LinkedTagCollection.Spy.Node<Part> i1 = spy.newNode(new Part("SN-i1"), "leg", n0, n1);
		spy.setPrev(d, n2);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		r = spy.newInstance(d, 3, 0);
		it = spy.newIterator(r, new String("leg"), i1, n1, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testL3() {
		LinkedTagCollection.Spy.Node<Part> i0 = spy.newNode(new Part("3"), "arm", d, null);
		spy.setPrev(d, d);
		spy.setNext(d, d);
		spy.setNext(i0, i0);
		r = spy.newInstance(d, 0, 0);
		it = spy.newIterator(r, null, i0, d, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testL4() {
		LinkedTagCollection.Spy.Node<Part> i0 = spy.newNode(new Part("4"), "arm", d, null);
		spy.setPrev(d, d);
		spy.setNext(d, d);
		spy.setNext(i0, i0);
		r = spy.newInstance(d, 0, 0);
		it = spy.newIterator(r, "arm", i0, d, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testL5() {
		LinkedTagCollection.Spy.Node<Part> i0 = spy.newNode(new Part("5"), "arm", d, d);
		spy.setPrev(d, d);
		spy.setNext(d, d);
		r = spy.newInstance(d, 0, 0);
		it = spy.newIterator(r, "arm", i0, d, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testL6() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("6"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("L6"), "arm", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-L6"), "leg", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X6"), "leg", n2, d);
		LinkedTagCollection.Spy.Node<Part> i0 = spy.newNode(new Part("RSN-i0"), "leg", null, null);
		LinkedTagCollection.Spy.Node<Part> i1 = spy.newNode(new Part("RSN-i1"), "leg", i0, i0);
		spy.setPrev(d, n3);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setPrev(i0, i1);
		spy.setNext(i0, i1);
		
		r = spy.newInstance(d, 4, 0);
		it = spy.newIterator(r, "leg", i0, n2, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testL7() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("7"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("L7"), "arm", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-L7"), "leg", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X7"), "leg", n2, d);
		LinkedTagCollection.Spy.Node<Part> i0 = spy.newNode(new Part("RSN-i0"), "leg", null, null);
		LinkedTagCollection.Spy.Node<Part> i1 = spy.newNode(new Part("RSN-i1"), "larm", i0, i0);
		spy.setPrev(d, n3);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setPrev(i0, i1);
		spy.setNext(i0, i1);
		
		r = spy.newInstance(d, 4, 0);
		it = spy.newIterator(r, "leg", i0, n2, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testL8() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("8"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("L8"), "arm", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-L8"), "leg", n1, null);
		
		LinkedTagCollection.Spy.Node<Part> id = spy.newNode();
		LinkedTagCollection.Spy.Node<Part> i0 = spy.newNode(new Part("i8"), "arm", id, null);
		LinkedTagCollection.Spy.Node<Part> i1 = spy.newNode(new Part("iL8"), "arm", i0, null);
		LinkedTagCollection.Spy.Node<Part> i2 = spy.newNode(new Part("SN-iL8"), "leg", i1, id);
		
		spy.setPrev(d, n2);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setPrev(id, i2);
		spy.setNext(id, i0);
		spy.setNext(i0, i1);
		spy.setNext(i1, i2);
		
		r = spy.newInstance(d, 3, 0);
		LinkedTagCollection<Part> i = spy.newInstance(id, 3, 0);
		assertReporting(true, () -> spy.wellFormed(i));
		it = spy.newIterator(r, "arm", i0, i1, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testL9() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("9"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("L9"), "arm", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-L9"), "leg", n1, null);
		
		LinkedTagCollection.Spy.Node<Part> id = spy.newNode();
		LinkedTagCollection.Spy.Node<Part> i0 = spy.newNode(new Part("i9"), "arm", id, null);
		LinkedTagCollection.Spy.Node<Part> i1 = spy.newNode(new Part("iL9"), "arm", i0, null);
		LinkedTagCollection.Spy.Node<Part> i2 = spy.newNode(new Part("SN-iL9"), "leg", i1, id);
		
		spy.setPrev(d, n2);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setPrev(id, i2);
		spy.setNext(id, i0);
		spy.setNext(i0, i1);
		spy.setNext(i1, i2);
		
		r = spy.newInstance(d, 3, 0);
		LinkedTagCollection<Part> i = spy.newInstance(id, 3, 0);
		assertReporting(true, () -> spy.wellFormed(i));
		it = spy.newIterator(r, "arm", i1, n0, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testM0() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("0"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("M0"), "arm", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-M0"), "leg", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X0"), "leg", n2, null);
		LinkedTagCollection.Spy.Node<Part> n4 = spy.newNode(new Part("RSN-M00"), "head", n3, d);
		spy.setPrev(d, n4);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		r = spy.newInstance(d, 5, 0);
		it = spy.newIterator(r, "leg", n0, n2, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testM1() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("1"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("M1"), "arm", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-M1"), "leg", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X1"), "leg", n2, null);
		LinkedTagCollection.Spy.Node<Part> n4 = spy.newNode(new Part("RSN-M11"), "head", n3, d);
		spy.setPrev(d, n4);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		r = spy.newInstance(d, 5, 0);
		it = spy.newIterator(r, "leg", n1, n2, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testM2() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("2"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("M2"), "arm", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-M2"), new String("leg"), n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X2"), "leg", n2, null);
		LinkedTagCollection.Spy.Node<Part> n4 = spy.newNode(new Part("RSN-M22"), "head", n3, d);
		spy.setPrev(d, n4);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		r = spy.newInstance(d, 5, 0);
		it = spy.newIterator(r, new String("leg"), n2, n3, 0);
		assertReporting(true, () -> spy.wellFormed(it));
	}
	
	public void testM3() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("3"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("M3"), "arm", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-M3"), "leg", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X3"), "leg", n2, null);
		LinkedTagCollection.Spy.Node<Part> n4 = spy.newNode(new Part("RSN-M33"), "head", n3, d);
		spy.setPrev(d, n4);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		r = spy.newInstance(d, 5, 0);
		it = spy.newIterator(r, "leg", n3, d, 0);
		assertReporting(true, () -> spy.wellFormed(it));
	}
	
	public void testM4() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("4"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("M4"), "arm", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-M4"), "leg", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X4"), "leg", n2, null);
		LinkedTagCollection.Spy.Node<Part> n4 = spy.newNode(new Part("RSN-M44"), "head", n3, d);
		spy.setPrev(d, n4);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		r = spy.newInstance(d, 5, 0);
		it = spy.newIterator(r, "leg", n4, d, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testM5() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("5"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("M5"), "arm", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-M5"), "leg", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X5"), "leg", n2, null);
		LinkedTagCollection.Spy.Node<Part> n4 = spy.newNode(new Part("RSN-M55"), "head", n3, d);
		spy.setPrev(d, n4);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		r = spy.newInstance(d, 5, 0);
		it = spy.newIterator(r, null, n0, n1, 0);
		assertReporting(true, () -> spy.wellFormed(it));
	}
	
	public void testM6() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("6"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("M6"), "arm", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-M6"), "leg", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X6"), "leg", n2, null);
		LinkedTagCollection.Spy.Node<Part> n4 = spy.newNode(new Part("RSN-M66"), "head", n3, d);
		spy.setPrev(d, n4);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		r = spy.newInstance(d, 5, 0);
		it = spy.newIterator(r, null, n2, n3, 0);
		assertReporting(true, () -> spy.wellFormed(it));
	}
	
	public void testM7() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("7"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("M7"), "arm", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-M7"), "leg", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X7"), "leg", n2, null);
		LinkedTagCollection.Spy.Node<Part> n4 = spy.newNode(new Part("RSN-M77"), "head", n3, d);
		spy.setPrev(d, n4);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		r = spy.newInstance(d, 5, 0);
		it = spy.newIterator(r, null, n4, d, 0);
		assertReporting(true, () -> spy.wellFormed(it));
	}
	
	public void testM8() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("8"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("M8"), "arm", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-M8"), "arm", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X8"), "arm", n2, d);
		spy.setPrev(d, n3);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		r = spy.newInstance(d, 4, 0);
		it = spy.newIterator(r, "leg", d, d, 0);
		assertReporting(true, () -> spy.wellFormed(it));
	}
	
	public void testM9() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("9"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("M9"), "arm", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-M9"), "arm", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X9"), "arm", n2, d);
		spy.setPrev(d, n3);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		r = spy.newInstance(d, 4, 0);
		it = spy.newIterator(r, "leg", n3, d, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testN0() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("0"), "arm", d, d);
		spy.setPrev(d, n0);
		spy.setNext(d, n0);
		r = spy.newInstance(d, 1, 0);
		it = spy.newIterator(r, null, d, n0, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testN1() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("1"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("N1"), "arm", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-N1"), "leg", n1, d);
		spy.setPrev(d, n2);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		r = spy.newInstance(d, 3, 0);
		it = spy.newIterator(r, null, d, d, 0);
		assertReporting(true, () -> spy.wellFormed(it));
	}
	
	public void testN2() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("2"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("N2"), "arm", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-N2"), "leg", n1, d);
		spy.setPrev(d, n2);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		r = spy.newInstance(d, 3, 0);
		it = spy.newIterator(r, null, d, n0, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testN3() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("3"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("N3"), "arm", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-N3"), "leg", n1, d);
		spy.setPrev(d, n2);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		r = spy.newInstance(d, 3, 0);
		it = spy.newIterator(r, null, d, n1, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testN4() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("4"), new String("arm"), d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("N4"), "arm", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-N4"), "leg", n1, d);
		spy.setPrev(d, n2);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		r = spy.newInstance(d, 3, 0);
		it = spy.newIterator(r, "arm", d, n0, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testN5() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("5"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("N5"), "arm", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-N5"), "leg", n1, d);
		spy.setPrev(d, n2);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		r = spy.newInstance(d, 3, 0);
		it = spy.newIterator(r, "arm", d, n1, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testO0() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("0"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("O0"), "arm", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-O0"), "leg", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X0"), "leg", n2, null);
		LinkedTagCollection.Spy.Node<Part> n4 = spy.newNode(new Part("RSN-O00"), "head", n3, d);
		spy.setPrev(d, n4);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		r = spy.newInstance(d, 5, 0);
		it = spy.newIterator(r, null, n0, n1, 0);
		assertReporting(true, () -> spy.wellFormed(it));
	}
	
	public void testO1() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("1"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("O1"), "arm", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-O1"), "leg", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X1"), "leg", n2, null);
		LinkedTagCollection.Spy.Node<Part> n4 = spy.newNode(new Part("RSN-O11"), "head", n3, d);
		spy.setPrev(d, n4);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		r = spy.newInstance(d, 5, 0);
		it = spy.newIterator(r, null, n0, n2, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testO2() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("2"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("O2"), "arm", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-O2"), "leg", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X2"), "leg", n2, null);
		LinkedTagCollection.Spy.Node<Part> n4 = spy.newNode(new Part("RSN-O22"), "head", n3, d);
		spy.setPrev(d, n4);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		r = spy.newInstance(d, 5, 0);
		it = spy.newIterator(r, null, n0, d, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testO3() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("3"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("O3"), "arm", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-O3"), "leg", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X3"), "leg", n2, null);
		LinkedTagCollection.Spy.Node<Part> n4 = spy.newNode(new Part("RSN-O33"), "head", n3, d);
		spy.setPrev(d, n4);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		r = spy.newInstance(d, 5, 0);
		it = spy.newIterator(r, null, n1, n0, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testO4() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("4"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("O4"), "arm", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-O4"), "leg", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X4"), "leg", n2, null);
		LinkedTagCollection.Spy.Node<Part> n4 = spy.newNode(new Part("RSN-O44"), "head", n3, d);
		spy.setPrev(d, n4);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		r = spy.newInstance(d, 5, 0);
		it = spy.newIterator(r, "leg", n2, n1, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testO5() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("5"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("O5"), "arm", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-O5"), "leg", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X5"), "leg", n2, null);
		LinkedTagCollection.Spy.Node<Part> n4 = spy.newNode(new Part("RSN-O55"), "head", n3, d);
		spy.setPrev(d, n4);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		r = spy.newInstance(d, 5, 0);
		it = spy.newIterator(r, "leg", n2, n3, 0);
		assertReporting(true, () -> spy.wellFormed(it));
	}
	
	public void testO6() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("6"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("O6"), "arm", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-O6"), "leg", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X6"), "leg", n2, null);
		LinkedTagCollection.Spy.Node<Part> n4 = spy.newNode(new Part("RSN-O66"), "head", n3, d);
		spy.setPrev(d, n4);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		r = spy.newInstance(d, 5, 0);
		it = spy.newIterator(r, "leg", n2, n4, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testO7() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("7"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("O7"), "arm", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-O7"), "leg", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X7"), "leg", n2, null);
		LinkedTagCollection.Spy.Node<Part> n4 = spy.newNode(new Part("RSN-O77"), "head", n3, d);
		spy.setPrev(d, n4);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		r = spy.newInstance(d, 5, 0);
		it = spy.newIterator(r, "leg", n2, d, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
	
	public void testO8() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("8"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("O8"), "arm", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-O8"), "leg", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X8"), "leg", n2, null);
		LinkedTagCollection.Spy.Node<Part> n4 = spy.newNode(new Part("RSN-O88"), "head", n3, d);
		spy.setPrev(d, n4);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		r = spy.newInstance(d, 5, 0);
		it = spy.newIterator(r, "leg", n3, d, 0);
		assertReporting(true, () -> spy.wellFormed(it));
	}
	
	public void testO9() {
		LinkedTagCollection.Spy.Node<Part> n0 = spy.newNode(new Part("9"), "arm", d, null);
		LinkedTagCollection.Spy.Node<Part> n1 = spy.newNode(new Part("O9"), "arm", n0, null);
		LinkedTagCollection.Spy.Node<Part> n2 = spy.newNode(new Part("SN-O9"), "leg", n1, null);
		LinkedTagCollection.Spy.Node<Part> n3 = spy.newNode(new Part("RSN-X9"), "leg", n2, null);
		LinkedTagCollection.Spy.Node<Part> n4 = spy.newNode(new Part("RSN-O99"), "head", n3, d);
		spy.setPrev(d, n4);
		spy.setNext(d, n0);
		spy.setNext(n0, n1);
		spy.setNext(n1, n2);
		spy.setNext(n2, n3);
		spy.setNext(n3, n4);
		r = spy.newInstance(d, 5, 0);
		it = spy.newIterator(r, "leg", n3, n2, 0);
		assertReporting(false, () -> spy.wellFormed(it));
	}
}