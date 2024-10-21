package kocsist.blogictest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

import kocsist.blogic.MyHelper;
import kocsist.model.Edge;
import kocsist.model.Node;

public class TestMyHelper {
	@Test
	public void testIfHashesEquals() {
		String testtext = "Mendegel_A_mandarin";
		String testhash1 = MyHelper.encodeStringToHash(testtext);
		String testhash2 = MyHelper.encodeStringToHash("Mendegel_A_mandarin");
		assertEquals(testhash1, testhash2);
	}
	@Test
	public void testIfHashesDiffers() {
		String testtext = "Mendegel_A_mandarin";
		String testhash1 = MyHelper.encodeStringToHash(testtext);
		String testhash2 = MyHelper.encodeStringToHash("Mendegel_a_mandarin");
		assertNotEquals(testhash1, testhash2);
	}
	@Test
	public void testIsSameHash() {
		String testtext1 = "jhk34r3rwed";
		String testtext2 = "Mehalommolha";
		String testtext3 = "super_secretPassword";
		String testtext4 = "super_secretPassword";
		assertFalse(MyHelper.isSameHash(testtext1, testtext2));
		assertTrue(MyHelper.isSameHash(testtext3, testtext4));
	}
	@Test
	public void testGetNewEdgeLabel() {
		String prefix = "e";
		String lbl1 = prefix + 1;
		String lbl2 = prefix + 2;
		String lbl3 = prefix + 3;
		Edge e1 = new Edge();
		Edge e2 = new Edge();
		Edge e3 = new Edge();
		e1.setLabel(lbl1);
		e2.setLabel(lbl2);
		e3.setLabel(lbl3);
		List<Edge> l1 = new LinkedList<Edge>();
		l1.add(e1);
		l1.add(e2);
		l1.add(e3);
		String lbl4 = prefix + 1;
		String lbl5 = prefix + 2;
		String lbl6 = prefix + 5;
		Edge e4 = new Edge();
		Edge e5 = new Edge();
		Edge e6 = new Edge();
		e4.setLabel(lbl4);
		e5.setLabel(lbl5);
		e6.setLabel(lbl6);
		List<Edge> l2 = new LinkedList<Edge>();
		l2.add(e4);
		l2.add(e5);
		l2.add(e6);
		String testlabel1 = MyHelper.getNewEdgeLabel(prefix, l1);
		String testlabel2 = MyHelper.getNewEdgeLabel(prefix, l2);
		System.out.println("  ********************** testGetNewEdgeLabel() ********** " );
		System.out.println(" testlabel1:" + testlabel1);
		System.out.println(" testlabel2:" + testlabel2);
		assertEquals(testlabel1, prefix + 4);
		assertEquals(testlabel2, prefix + 3);
		assertNotEquals(MyHelper.getNewEdgeLabel(prefix, l2), prefix + 6);
	}
	@Test
	public void testGetNewNodeLabel() {
		String prefix = "n";
		String lbl1 = prefix + 1;
		String lbl2 = prefix + 2;
		String lbl3 = prefix + 3;
		Node e1 = new Node();
		Node e2 = new Node();
		Node e3 = new Node();
		e1.setLabel(lbl1);
		e2.setLabel(lbl2);
		e3.setLabel(lbl3);
		List<Node> l1 = new LinkedList<Node>();
		l1.add(e1);
		l1.add(e2);
		l1.add(e3);
		String lbl4 = prefix + 2;
		String lbl5 = prefix + 3;
		String lbl6 = prefix + 4;
		Node e4 = new Node();
		Node e5 = new Node();
		Node e6 = new Node();
		e4.setLabel(lbl4);
		e5.setLabel(lbl5);
		e6.setLabel(lbl6);
		List<Node> l2 = new LinkedList<Node>();
		l2.add(e4);
		l2.add(e5);
		l2.add(e6);
		String testlabel1 = MyHelper.getNewNodeLabel(prefix, l1);
		String testlabel2 = MyHelper.getNewNodeLabel(prefix, l2);
		System.out.println("  ********************** testGetNewNodeLabel() ********** " );
		System.out.println(" testlabel1:" + testlabel1);
		System.out.println(" testlabel2:" + testlabel2);
		assertEquals(testlabel1, prefix + 4);
		assertEquals(testlabel2, prefix + 1);
		assertNotEquals(MyHelper.getNewNodeLabel(prefix, l2), prefix + 6);
	}
}
