package kocsist.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import kocsist.model.Description;
import kocsist.model.Edge;
import kocsist.model.InventoryElement;
import kocsist.model.Node;
import kocsist.model.Picture;
import kocsist.service.interfaces.EdgeService;
import kocsist.service.interfaces.NodeService;
@DataJpaTest
public class TestEdgeService {
	@Autowired
	private EdgeService edgeService;
	
	@Autowired
	private NodeService nodeService;
	
	private Long testedgeid;
	private ArrayList<InventoryElement> testinventory = new ArrayList<>();
	private ArrayList<Picture> testpictures = new ArrayList<>();
	private Description testdesc;
	
	@BeforeEach
	@Rollback(false)
	public void setUp() {
		Edge e = new Edge();
		Node fromnode = new Node();
		fromnode.setLabel("nt_from");
		Node tonode = new Node();
		tonode.setLabel("nt_to");
		this.nodeService.addNode(tonode);//without this line hibernate error
		this.nodeService.addNode(fromnode);//without this line hibernate error
		e.setToNode(tonode);
		e.setFromNode(fromnode);
		e.setTime1(93);
		e.setTime2(112);
		for (int k = 0; k < 3; k++) {
			Picture p = new Picture();
			p.setSize(912);
			p.setPathOnServer("testpath_" + k); 
			e.addPicture(p);
			this.testpictures.add(p);
			
			InventoryElement elem = new InventoryElement();
			elem.setText("test_ie_" + (k*2)); 
			e.addInventoryElement(elem);
			this.testinventory.add(elem);
		}	
		Description d = new Description();
		d.setTag("testtag");
		d.setText("testdesc");
		e.setDesc(d);
		this.testdesc = d;
		e.setLabel("testedge");
		this.testedgeid = this.edgeService.addEdge(e);
	}
	
	@Test
	@Rollback(false)
	public void testPersistEdge() {
		Edge persistededge =  this.edgeService.findById(this.testedgeid);
		System.out.println("\n  *******************  testPersistEdge()  *******************\n");
		System.out.println(persistededge.printMe());
		List<Picture> persistedpictures = persistededge.getPictures();
		List<InventoryElement> persistedinventory = persistededge.getInventory();
		Description persisteddesc = persistededge.getDesc();
		assertEquals(this.testdesc.getText(), persisteddesc.getText());
		
		List<Picture> diffpictures = persistedpictures.stream()
			    .filter(aObject -> !this.testpictures.contains(aObject))
			    .collect(Collectors.toList());
		List<InventoryElement> diffinventory = persistedinventory.stream()
			    .filter(aObject -> !this.testinventory.contains(aObject))
			    .collect(Collectors.toList());
		System.out.println("  diffpictures size: " + diffpictures.size());
		System.out.println("  diffinventory size: " + diffinventory.size());
		assertEquals(diffpictures.size(), 0);
		assertEquals(diffinventory.size(), 0);
		List<InventoryElement> extendedinventory = new ArrayList<>(this.testinventory);
		List<Picture> extendedpiclist = new ArrayList<>(this.testpictures);
		InventoryElement extelem = new InventoryElement();
		extelem.setText("ext");
		extendedinventory.add(extelem);
		Picture extpic = new Picture();
		extpic.setPathOnServer("ext");
		extendedpiclist.add(extpic);
		List<Picture> diffpictures2 = extendedpiclist.stream()
			    .filter(aObject -> !persistedpictures.contains(aObject))
			    .collect(Collectors.toList());
		List<InventoryElement> diffinventory2 = extendedinventory.stream()
			    .filter(aObject -> !persistedinventory.contains(aObject))
			    .collect(Collectors.toList());
		System.out.println("  diffinventory2: " + diffinventory2.size());
		System.out.println("  diffpictures2: " + diffpictures2.size());
		
		assertEquals(diffpictures2.size(), 1);
		assertEquals(diffinventory2.size(), 1);
		
	}
}
