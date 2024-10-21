package kocsist.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import kocsist.model.Description;
import kocsist.model.Edge;
import kocsist.model.GraphData;
import kocsist.model.InventoryElement;
import kocsist.model.Node;
import kocsist.model.Picture;
import kocsist.model.UserInfo;
import kocsist.repository.EdgeRepo;
import kocsist.service.interfaces.DescriptionService;
import kocsist.service.interfaces.EdgeService;
import kocsist.service.interfaces.GraphDataService;
import kocsist.service.interfaces.GraphService;
import kocsist.service.interfaces.InventoryService;
import kocsist.service.interfaces.NodeService;
import kocsist.service.interfaces.PictureService;
import kocsist.service.interfaces.UserService;

@DataJpaTest
public class TestAttachedEntitiesService {
	@Autowired
	private InventoryService invService;
	@Autowired
	private PictureService picService;
	@Autowired
	private DescriptionService descService;
	@Autowired
	private EdgeService edgeService;
	@Autowired
	private UserService userService;
	@Autowired
	private NodeService nodeService;
	@Autowired
	private GraphDataService gdService;
	@Autowired
	private EdgeRepo er;
	@Autowired
	private GraphService gs;
	
	private GraphData testgd;
	private long testedgeid;
	private Long testinvelemid;
	private int testuserid;
		
	@BeforeEach
	public void setUp() {
		UserInfo user = new UserInfo();
		user.setEmail("testuser@t.hu");
		this.testuserid = this.userService.addUser(user);
				
		Node entrynode = new Node();
		entrynode.setEntry(true);
		entrynode.setLabel("testentrynode"); 
		Node finishnode = new Node();
		finishnode.setLabel("testfinishnode");
		finishnode.setFinish(true);
		this.nodeService.addNode(finishnode);
		this.nodeService.addNode(entrynode);
		
		Edge edge = new Edge();
		edge.setLabel("e1");
		
		edge.setFromNode(entrynode);
		edge.setToNode(finishnode);
		this.edgeService.addEdge(edge);
		this.testedgeid = edge.getId();
		InventoryElement ie1 = new InventoryElement();
		ie1.setText("test item1");
		//ie1.setUser(null); //IF user is not set, deleting edge with 
		// the option of preserving component will not preserve at all
		ie1.setUser(user);
		edge.addInventoryElement(ie1);
		this.testinvelemid = this.invService.addInventory(ie1);
		InventoryElement ie2 = new InventoryElement();
		ie2.setText("test item2");
		ie2.setUser(user);
		edge.addInventoryElement(ie2);
		
		Picture pic1 = new Picture();
		pic1.setPathOnServer("testpath1");
		edge.addPicture(pic1);
		pic1.setUser(user);
		Picture pic2 = new Picture();
		pic2.setPathOnServer("testpath2");
		edge.addPicture(pic2);
		pic2.setUser(user);
		Description desc1 = new Description();
		desc1.setText("testtext1");
		edge.setDesc(desc1);
		desc1.setUser(user);		
		
		GraphData gd = new GraphData();
		gd.setEntryNode(entrynode);
		gd.setName("testgd");
		gd.setUser(user);
		this.gdService.addGraphData(gd);
		this.testgd  = gd;
		//persist inventoryelement, picture and description not attached to any edge
		InventoryElement orphanie = new InventoryElement();
		orphanie.setText("orphan item");
		orphanie.setUser(user);
		this.invService.addInventory(orphanie);
		
		Picture orphanpic = new Picture();
		orphanpic.setPathOnServer("orphanpath");
		orphanpic.setUser(user);
		this.picService.addPicture(orphanpic);
		
		Description orphandesc = new Description();
		orphandesc.setText("orphandesc");
		orphandesc.setUser(user);
		this.descService.addDescription(orphandesc);
	}
	@Test
	public void test1GetOrphanInventoryByUserId() {
		int origorphaninvelemcount = this.invService.getOrphanInventoryByUserId(testuserid).size();
		Optional<Edge> myedge = this.er.findById(this.testedgeid);
		int attachedinvelemcount = 0;
		if(myedge.isPresent()) {
			attachedinvelemcount = myedge.get().getInventory().size();
		}
		System.out.println("  attachedinvelemcount: " + attachedinvelemcount);
		System.out.println("  origorphaninvelemcount: " + origorphaninvelemcount);
		this.edgeService.deleteEdgeById(this.testedgeid, true); // preserves components - only if their user field is not null
		int orphaninventorycount = this.invService.getOrphanInventoryByUserId(testuserid).size(); 
		System.out.println(" ***********************   graph as text in test1GetOrphanInventoryByUserId() AFTER DELETED edge *********************");
		System.out.println(this.gs.getGraphAsText(this.testgd));
		System.out.println("  orphaninventorycount: " + orphaninventorycount);
		
		assertNotEquals(this.invService.getOrphanInventoryByUserId(testuserid).size(), 1);
		assertEquals(orphaninventorycount, origorphaninvelemcount + attachedinvelemcount);
	}
	@Test
	public void test2GetOrphanInventoryByUserId() {
		int origorphaninvelemcount = this.invService.getOrphanInventoryByUserId(testuserid).size();
		Optional<Edge> myedge = this.er.findById(this.testedgeid);
		int attachedinvelemcount = 0;
		if(myedge.isPresent()) {
			attachedinvelemcount = myedge.get().getInventory().size();
		}
		this.edgeService.deleteEdgeById(this.testedgeid); // preserves none of the components regardless of their field user's value
		int orphaninventorycount = this.invService.getOrphanInventoryByUserId(testuserid).size(); 
		
		assertEquals(this.invService.getOrphanInventoryByUserId(testuserid).size(), 1);
		assertNotEquals(orphaninventorycount, origorphaninvelemcount + attachedinvelemcount);
	}
	@Test
	public void test3GetOrphanInventoryByUserId() {
		System.out.println("   ***************************  T E S T   3  *************************************");
		int origorphaninvelemcount = this.invService.getOrphanInventoryByUserId(testuserid).size();
		InventoryElement invelem = this.invService.findById(testinvelemid);
		if (invelem != null) {
			invelem.setUser(null);
		}
		Optional<Edge> myedge = this.er.findById(this.testedgeid);
		int attachedinvelemcount = 0;
		if(myedge.isPresent()) {
			attachedinvelemcount = myedge.get().getInventory().size(); //should be 2
		}
		System.out.println("  attachedinvelemcount: " + attachedinvelemcount);
		System.out.println("  origorphaninvelemcount: " + origorphaninvelemcount);
		this.edgeService.deleteEdgeById(this.testedgeid, true); // preserves components - only if their user field is not null
		int orphaninventorycount = this.invService.getOrphanInventoryByUserId(testuserid).size(); 
		System.out.println(" ***********************   graph as text in test3GetOrphanInventoryByUserId() AFTER DELETED edge *********************");
		System.out.println(this.gs.getGraphAsText(this.testgd));
		System.out.println("  orphaninventorycount: " + orphaninventorycount);
		
		assertNotEquals(this.invService.getOrphanInventoryByUserId(testuserid).size(), 1);
		assertNotEquals(orphaninventorycount, origorphaninvelemcount + attachedinvelemcount);
	}
	@Test
	public void test1GetOrphanPicturesByUserId() {
		int origorphanpicelemcount = this.picService.getOrphanPicturesByUserId(testuserid).size();
		Edge myedge = this.edgeService.findById(testinvelemid);
		int attachedpicelemcount = 0;
		if(myedge != null) {
			attachedpicelemcount = myedge.getPictures().size();
		}
		System.out.println("  attachedpicelemcount: " + attachedpicelemcount);
		this.edgeService.deleteEdgeById(this.testedgeid);
		final int finalorphanpicturecount = this.picService.getOrphanPicturesByUserId(testuserid).size(); 
		assertNotEquals(finalorphanpicturecount, 0);
		assertEquals(finalorphanpicturecount, origorphanpicelemcount);
	}
	@Test
	public void test2GetOrphanPicturesByUserId() {
		System.out.println(" ***************  test2GetOrphanPicturesByUserId  *******************");
		int origorphanpicelemcount = this.picService.getOrphanPicturesByUserId(testuserid).size();
		Edge myedge = this.edgeService.findById(this.testedgeid);
		int attachedpicelemcount = 0;
		if(myedge != null) {
			//System.out.println(myedge.printMe());
			attachedpicelemcount = myedge.getPictures().size();
		}
		this.edgeService.deleteEdgeById(this.testedgeid, true);
		final int finalorphanpicturecount = this.picService.getOrphanPicturesByUserId(testuserid).size(); 
		System.out.println(" finalorphanpicturecount: " + finalorphanpicturecount);
		System.out.println("  origorphanpicturecount: " + origorphanpicelemcount);
		System.out.println("  attachedpicelemcount: " + attachedpicelemcount);
		
		assertNotEquals(finalorphanpicturecount, 0);
		assertEquals(finalorphanpicturecount, origorphanpicelemcount + attachedpicelemcount);
	}
	@Test
	public void test1GetOrphanDescriptionsByUserId() {
		System.out.println("  *******************   test1GetOrphanDescriptionsByUserId   ********");
		int origorphandescelemcount = this.descService.getOrphanDescriptionsByUserId(testuserid).size();
		System.out.println("origorphandescelemcount: " + origorphandescelemcount);
		Edge myedge = this.edgeService.findById(this.testedgeid);
		int attacheddescelemcount = 0;
		if(myedge != null && myedge.getDesc() != null) {
			attacheddescelemcount = 1;
		}
		boolean preservecomponents = true;
		this.edgeService.deleteEdgeById(this.testedgeid, preservecomponents);
		final int finalorphandescriptioncount = this.descService.getOrphanDescriptionsByUserId(testuserid).size(); 
		System.out.println("finalorphandescriptioncount: " + finalorphandescriptioncount);
		assertNotEquals(finalorphandescriptioncount, 0);
		assertEquals(finalorphandescriptioncount, origorphandescelemcount + attacheddescelemcount);
	}
	@Test
	public void test2GetOrphanDescriptionsByUserId() {
		int origorphandescelemcount = this.descService.getOrphanDescriptionsByUserId(testuserid).size();
		Edge myedge = this.edgeService.findById(this.testedgeid);
		int attacheddescelemcount = 0;
		if(myedge != null && myedge.getDesc() != null) {
			attacheddescelemcount = 1;
		}
		this.edgeService.deleteEdgeById(this.testedgeid);
		final int finalorphandescriptioncount = this.descService.getOrphanDescriptionsByUserId(testuserid).size(); 
		assertEquals(finalorphandescriptioncount, 1);
		assertNotEquals(finalorphandescriptioncount, origorphandescelemcount + attacheddescelemcount);
	}
}
