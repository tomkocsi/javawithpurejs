package kocsist.servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import kocsist.blogic.DescWrapper;
import kocsist.blogic.InvElemWrapper;
import kocsist.blogic.ItemWrapper;
import kocsist.blogic.MyHelper;
import kocsist.blogic.PicWrapper;
import kocsist.model.Description;
import kocsist.model.Edge;
import kocsist.model.GraphData;
import kocsist.model.InventoryElement;
import kocsist.model.Node;
import kocsist.model.Picture;
import kocsist.model.UserInfo;
import kocsist.repository.DescriptionRepo;
import kocsist.repository.EdgeRepo;
import kocsist.repository.InventoryRepo;
import kocsist.repository.PictureRepo;
import kocsist.service.interfaces.DescriptionService;
import kocsist.service.interfaces.EdgeService;
import kocsist.service.interfaces.GraphDataService;
import kocsist.service.interfaces.GraphService;
import kocsist.service.interfaces.NodeService;
import kocsist.service.interfaces.UserService;

@DataJpaTest
public class TestGraphService {
	@Autowired
	private GraphService graphService;
	@Autowired
	private NodeService nodeService;
	@Autowired
	private EdgeService edgeService;
	@Autowired
	private UserService userService;
	@Autowired
	private GraphDataService gds;
	@Autowired
	private DescriptionService ds;
	@Autowired
	private DescriptionRepo descRepo;
	@Autowired
	private PictureRepo picRepo;
	@Autowired
	private InventoryRepo invRepo;
	@Autowired
	private EdgeRepo edgeRepo;
	
	private GraphData testgraphdata;
	private Node testentrynode;
	private ArrayList<Node> testnodes = new ArrayList<>();
	private ArrayList<Edge> testedges = new ArrayList<>();
	private ArrayList<Long> testedgeids = new ArrayList<>();
	private ArrayList<InventoryElement> testinventory = new ArrayList<>();
	private ArrayList<Description> testdesc = new ArrayList<>();
	private ArrayList<Picture> testpictures = new ArrayList<>();
	private Random myrandom = new Random();
	private UserInfo testuser; 
	private static int ecount = 0; 
	@BeforeEach
	public void setUp() {
		this.testedges.clear();
		this.testpictures.clear();
		this.testinventory.clear();
		this.testdesc.clear();
		this.edgeRepo.deleteAll();
		this.testuser = new UserInfo();
		this.testuser.setEmail("vki" + this.myrandom.nextInt(5000) + "@mail.hu");
		this.userService.addUser(this.testuser);
		Node entrynode = new Node();
		entrynode.setEntry(true);
		entrynode.setLabel("Start");
		this.testentrynode = entrynode;
		this.nodeService.addNode(this.testentrynode);
		this.testgraphdata = new GraphData();
		this.testgraphdata.setEntryNode(this.testentrynode);
		this.testgraphdata.setUser(this.testuser);
		this.testgraphdata.setName("terv" + this.myrandom.nextInt(100));
		this.testgraphdata.setPublikus(false);
		this.gds.addGraphData(this.testgraphdata);
		//entrynode is not added to testnodes		
		for (int i = 0; i < 10; i++) {
			Node n = new Node();
			n.setLabel("n"+(i+1));
			n.setX(60 + 2*i);
			n.setY(180 + 3*i);
			this.testnodes.add(n);
			this.nodeService.addNode(n);
		}
		Collections.shuffle(this.testnodes);
		Queue<Node> nodepool = new LinkedList<>();
		for (int i = 0; i < this.testnodes.size(); i++) {
			nodepool.offer(this.testnodes.get(i));
		}
		ArrayList<ArrayList<Node>> row = new ArrayList<>(3);
		for (int j = 0; j < 3; j++) {
			row.add(new ArrayList<>());
			for (int i = 0; i < 3; i++) {
				row.get(j).add(nodepool.poll());
			}
		}
		Node finishnode = nodepool.poll();
		finishnode.setFinish(true);
		for (int i = 0; i < 3; i++) {
			Edge e = new Edge();
			e.setLabel("e" + TestGraphService.ecount++);
			Node tonode = row.get(0).get(i); 
			e.setFromNode(this.testentrynode);
			e.setToNode(tonode);
			Description d1 =  new Description();
			d1.setText("dA" + i);
			e.setDesc(d1);
			this.testdesc.add(d1);
			this.testedgeids.add(this.edgeService.addEdge(e));
			this.testedges.add(e);
			
		}
		
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 3; j++) {
				Edge e = new Edge();
				e.setLabel("e" + TestGraphService.ecount++);
				Node fromnode = row.get(i).get(j);
				fromnode.setLabel("n_" + i + "_" + j); //n_1_2
				Node tonode = row.get(i+1).get(j);
				tonode.setLabel("n_" + (i+1) + "_" + j); //n_2_2
				e.setToNode(tonode);
				e.setFromNode(fromnode);
				e.setTime1(20+j);
				e.setTime2(30+i);
				if(i == 1 && j == 2) {
					for (int k = 0; k < 3; k++) {
						Picture p = new Picture();
						p.setSize(1024);
						p.setPathOnServer("path_" + i + "_" + j + "_" + k); //path_1_2_0
						e.addPicture(p);
						this.testpictures.add(p);
						InventoryElement elem = new InventoryElement();
						elem.setText("ie_" + i + "_" + j + "_" + k); //ie_1_2_0
						e.addInventoryElement(elem);
						this.testinventory.add(elem);
					}
				}
				Description d2 =  new Description();
				d2.setText("dL" + i);
				d2.setTag("logik");
				e.setDesc(d2);
				this.testdesc.add(d2);
				this.testedgeids.add(this.edgeService.addEdge(e));
				this.testedges.add(e);
			}
		}	
		for (int i = 0; i < 3; i++) {
			Edge e = new Edge();
			e.setLabel("e" + TestGraphService.ecount++);
			Node fromnode = row.get(2).get(i);
			e.setToNode(finishnode);
			e.setFromNode(fromnode);
			Description d = new Description();
			d.setText("x_" + i);
			d.setTag("jo");
			e.setDesc(d);
			this.testdesc.add(d);
			this.testedgeids.add(this.edgeService.addEdge(e));
			this.testedges.add(e);
		}
		Collections.shuffle(this.testnodes);
		for (int i = 0; i < 5; i++) {
			if(!this.testnodes.get(i).isFinish()) {
				Edge e = new Edge();
				e.setLabel("e" + TestGraphService.ecount++);
				Node fromnode = this.testnodes.get(i);
				Node tonode = this.testnodes.get(i+5);
				e.setToNode(tonode);
				e.setFromNode(fromnode);
				Description d = new Description();
				d.setText("desc_" + i);
				e.setDesc(d);
				this.testdesc.add(d);
				this.testedgeids.add(this.edgeService.addEdge(e));
				this.testedges.add(e);
				
			}
		}
		Node tonode = this.testnodes.get(9);
		Edge e = new Edge();
		e.setLabel("e" + TestGraphService.ecount++);
		e.setFromNode(this.testentrynode);
		e.setToNode(tonode);
		this.testedgeids.add(this.edgeService.addEdge(e));
		this.testedges.add(e);
		this.testnodes.add(entrynode);
	}
	@SuppressWarnings("unused")
	private void printEdgeList(List<Edge> list) {
		System.out.println("\n*************  lista méret: " + list.size() + " ************************************\n");
		for(Edge e : list) {
			System.out.print(e.getLabel() + "(" + e.getId() + ") | ");
		}
	}
	@Test
	public void testGetAllEdgesByEntrynode() {
		boolean found = true;
		int len = this.graphService.getAllEdgesByEntryNode(this.testentrynode).size();
		for(Edge e : this.testedges) {
			int k = 0;
			while(k < len && !e.equals(this.graphService.getAllEdgesByEntryNode(this.testentrynode).get(k))) {
				k++;
			}
			if(k == len) {
				found = false;
				break;
			}
		}
		assertTrue(found);
	}
	@Test
	public void testIsSame() {
		int s = this.testnodes.size();
		int rnd = this.myrandom.nextInt(s);
		if(s > 1 && rnd > 0) {
			Node n1 = this.testnodes.get(0);
			Node n2 = this.testnodes.get(rnd);
			Edge e1 = new Edge();
			e1.setFromNode(n1);
			e1.setToNode(n2);
			Edge e2 = new Edge();
			e2.setFromNode(n1);
			e2.setToNode(n2);
			Edge e3 = new Edge();
			e3.setFromNode(n2);
			e3.setToNode(n1);
			assertTrue(this.graphService.isSame(e1, e2) == true && 
					this.graphService.isSame(e1, e3) == false);
		} else
			assertTrue(true);
	}
	
	@Test
	public void testGetAllNodesByEntryNodeId() {
		Integer nodeid = this.testgraphdata.getEntryNode().getId();
		List<Node> persistedlist= this.graphService.getAllNodesByEntryNodeId(nodeid);
		assertEquals(persistedlist.size(), testnodes.size());
		List<Node> diff = persistedlist.stream()
			    .filter(aObject -> !this.testnodes.contains(aObject))
			    .collect(Collectors.toList());
		assertTrue(persistedlist.size() > 0);
		assertTrue(this.testnodes.size() > 0);
		assertEquals(diff.size(),0);
	}
	
	@Test
	public void testCloneGraph() {
		UserInfo newuser = new UserInfo();
		newuser.setName("user" + this.myrandom.nextInt(500));
		newuser.setEmail("vki_" + this.myrandom.nextInt(5000));
		Integer newuserid = this.userService.addUser(newuser);
		GraphData cloneGraphData = this.graphService.cloneGraph(testgraphdata, newuserid);
		List<Node> nodes = this.graphService.getAllNodesByEntryNode(testgraphdata.getEntryNode());
		List<Edge> edges = this.graphService.getAllEdgesByEntryNode(testgraphdata.getEntryNode());
		List<Node> clonenodes = this.graphService.getAllNodesByEntryNode(cloneGraphData.getEntryNode());
		List<Edge> cloneedges = this.graphService.getAllEdgesByEntryNode(cloneGraphData.getEntryNode());
		ArrayList<InventoryElement> originventory = this.graphService.getAllInventoryElementsByEntryNode(testentrynode);
		ArrayList<Description> origdescs = this.graphService.getAllDescriptionsByEntryNode(testentrynode);
		ArrayList<Picture> origpics = this.graphService.getAllPicturesByEntryNode(testentrynode);
		ArrayList<InventoryElement> cloneinventory = this.graphService.getAllInventoryElementsByEntryNode(cloneGraphData.getEntryNode());
		ArrayList<Description> clonedescs = this.graphService.getAllDescriptionsByEntryNode(cloneGraphData.getEntryNode());
		ArrayList<Picture> clonepics = this.graphService.getAllPicturesByEntryNode(cloneGraphData.getEntryNode());
		ArrayList<ItemWrapper> origiwlist = new ArrayList<>();
		ArrayList<ItemWrapper> cloneiwlist = new ArrayList<>();
		assertTrue(MyHelper.isAllCloned(origiwlist, cloneiwlist));
		for (Picture p : origpics) {
			ItemWrapper pw = new PicWrapper(p);
			origiwlist.add(pw);
		}
		for (Picture p : clonepics) {
			ItemWrapper pw = new PicWrapper(p);
			cloneiwlist.add(pw);
		}
		assertTrue(MyHelper.isAllCloned(origiwlist, cloneiwlist));
		origiwlist.clear();
		cloneiwlist.clear();
		for (InventoryElement ie : originventory) {
			ItemWrapper iew = new InvElemWrapper(ie);
			origiwlist.add(iew);
		}
		for (InventoryElement ie : cloneinventory) {
			ItemWrapper iew = new InvElemWrapper(ie);
			cloneiwlist.add(iew);
		}
		assertTrue(MyHelper.isAllCloned(origiwlist, cloneiwlist));
		origiwlist.clear();
		cloneiwlist.clear();
		for (Description d : origdescs) {
			ItemWrapper dw = new DescWrapper(d);
			origiwlist.add(dw);
		}
		for (Description d : clonedescs) {
			ItemWrapper dw = new DescWrapper(d);
			cloneiwlist.add(dw);
		}
		assertTrue(MyHelper.isAllCloned(origiwlist, cloneiwlist));
		System.out.println("   ********************");
		System.out.println("   cloneGraphData.getName():");
		System.out.println(cloneGraphData.getName());
		System.out.println("   testgraphdata.getName():");
		System.out.println(testgraphdata.getName());
		
		assertTrue(cloneGraphData.getName().substring(0,testgraphdata.getName().length()).equals(testgraphdata.getName()));
		assertFalse(cloneGraphData.getUser().getEmail().equals(testgraphdata.getUser().getEmail()));
		assertTrue(edges.size() == cloneedges.size() && nodes.size() == clonenodes.size());
	}
	@Test
	public void testGetAllNodesByEntryNode() {
		boolean iscontained = true;
		for(Node n : this.testnodes) {
			if(this.graphService.getAllNodesByEntryNode(this.testentrynode) != null &&
					!this.graphService.getAllNodesByEntryNode(this.testentrynode).contains(n)) {
				iscontained = false;
				break;
			}
		}
		assertTrue(iscontained);
	}
	@Test
	public void testGetAllInventoryElementsByEntryNode() {
		ArrayList<InventoryElement> attachedlist = this.graphService.getAllInventoryElementsByEntryNode(this.testentrynode);
		if(attachedlist != null && attachedlist.size() > 0) {
			System.out.println("   ***************  testGetAllInventoryElementsByEntryNode()  *******");
			boolean found = true;
			for(InventoryElement item : this.testinventory) {
				found = false;
				for(InventoryElement attacheditem : attachedlist) {
					if(item.getId().equals(attacheditem.getId())) {
						found = true;
						System.out.println("item.getId()" + item.getId());
						break;
					}
				}
				if(!found) {
					break;
				}
			}
			assertTrue(found);
		}
		assertTrue(attachedlist.size() == this.testinventory.size());
	}
	@Test
	public void testGetDescriptionsByEntryNode() {
		ArrayList<Description> attachedlist = this.graphService.getAllDescriptionsByEntryNode(this.testentrynode);
		if(attachedlist != null && attachedlist.size() > 0) {
			System.out.println("   ***************  testGetAllDescriptionsByEntryNode()  *******");
			System.out.println(" testdesc.size() : " + testdesc.size());
			System.out.println(" attachedlist.size() : " + attachedlist.size());
			boolean found = true;
			for(Description item : this.testdesc) {
				found = false;
				for(Description attacheditem : attachedlist) {
					if(item.getId().equals(attacheditem.getId()) || item.getText().equals(attacheditem.getText())) {
						found = true;
						break;
					}
				}
				if(!found) {
					break;
				}
			}
			assertTrue(found);
		}
		assertTrue(attachedlist.size() == this.testdesc.size());
	}
	@Test
	public void testGetAllPicturesByEntryNode() {
		ArrayList<Picture> attachedlist = this.graphService.getAllPicturesByEntryNode(this.testentrynode);
		if(attachedlist != null && attachedlist.size() > 0) {
			System.out.println("   ***************  testGetAllPicturesByEntryNode()  *******");
			boolean found = true;
			for(Picture item : this.testpictures) {
				found = false;
				for(Picture attacheditem : attachedlist) {
					if(item.getId().equals(attacheditem.getId())) {
						found = true;
						System.out.println(item.getPathOnServer() + " _ " + attacheditem.getPathOnServer());
						break;
					}
				}
				if(!found) {
					break;
				}
			}
			assertTrue(found);
		}
		assertTrue(attachedlist.size() == this.testinventory.size());
	}
	@Test
	public void testGetAllAttachedElementsByEntryNode() {
		boolean isContained = true;
		for (Picture p : this.testpictures) {
			if(!this.graphService.getAllPicturesByEntryNode(this.testentrynode).contains(p)) {
				isContained = false;
				break;
			}
		}
		for (InventoryElement ie : this.testinventory) {
			if(!this.graphService.getAllInventoryElementsByEntryNode(this.testentrynode).contains(ie)) {
				isContained = false;
				break;
			}
		}
		for (Description d : this.testdesc) {
			if(!this.graphService.getAllDescriptionsByEntryNode(this.testentrynode).contains(d)) {
				isContained = false;
				break;
			}
		}
		assertTrue(isContained);
	}
	@Test
	public void testDeleteEdgesAndNodesByEntryNode(){
		GraphData gd = new GraphData();
		gd.setUser(testuser);
		Node entrynode = new Node();
		entrynode.setEntry(true);
		entrynode.setLabel("start");
		gd.setEntryNode(entrynode);
		Node n2 = new Node();
		n2.setLabel("n2");
		Node n3 = new Node();
		n3.setLabel("n3");
		Node n4 = new Node();
		n4.setLabel("n4");
		Edge e1 = new Edge();
		Edge e2 = new Edge();
		Edge e3 = new Edge();
		Edge e4 = new Edge();
		Long eid1 = this.edgeService.addEdge(e1);
		Long eid2 = this.edgeService.addEdge(e2);
		Long eid3 = this.edgeService.addEdge(e3);
		Long eid4 = this.edgeService.addEdge(e4);
		
		e1.setLabel("e01");
		e1.setFromNode(entrynode);
		e1.setToNode(n2);
		e2.setLabel("e02");
		e2.setFromNode(n2);
		e2.setToNode(n3);
		e3.setLabel("e03");
		e3.setFromNode(entrynode);
		e3.setToNode(n3);
		e4.setLabel("e04");
		e4.setFromNode(n2);
		e4.setToNode(n4);
		
		//Integer nid1 = e1.getFromNode().getId();
		Integer nid1 = this.nodeService.addNode(entrynode);
		//Integer nid2 = e2.getFromNode().getId();
		Integer nid2 = this.nodeService.addNode(n2);
		Integer nid3 = this.nodeService.addNode(n3);
		Integer nid4 = this.nodeService.addNode(n4);
		
		
		Node nn1 = new Node();
		Node nn2 = new Node();
		Integer nn1id = this.nodeService.addNode(nn1);
		Integer nn2id = this.nodeService.addNode(nn2);
		Edge ee1 = new Edge();
		Long eeid = this.edgeService.addEdge(ee1);
		ee1.setFromNode(nn2);
		ee1.setToNode(nn1);
		
		assertNotNull(this.edgeService.findById(eid1));
		assertNotNull(this.edgeService.findById(eid2));
		
		this.graphService.deleteEdgesAndNodesByEntryNode(entrynode);
		
		assertNull(this.edgeService.findById(eid1));
		assertNull(this.edgeService.findById(eid2));
		
		
		assertNull(this.nodeService.findById(nid1));
		assertNull(this.nodeService.findById(nid2));
		assertNull(this.nodeService.findById(nid3));
		assertNotNull(this.nodeService.findById(nn1id));
		assertNotNull(this.nodeService.findById(nn2id));
		assertNotNull(this.edgeService.findById(eeid));
		assertNull(this.edgeService.findById(eid3));
		assertNull(this.edgeService.findById(eid4));
		assertNull(this.nodeService.findById(nid4));

	}
	@Test
	public void testCreateGraph() {
		GraphData testgd = this.graphService.createGraph(this.testuser);
		Node testentrynode = testgd.getEntryNode();
		assertTrue(this.testuser.getEmail().equals(testgd.getUser().getEmail()));
		assertTrue(this.graphService.getAllNodesByEntryNode(testentrynode).size() == 2);
		assertTrue(this.graphService.getAllEdgesByEntryNode(testentrynode).size() == 1);
	}
	@Test
	public void testDeleteGraphWithAllComponents() {
		boolean foundbefore = true;
		boolean foundafter = false;
		Node entrynode = this.testgraphdata.getEntryNode();
		ArrayList<Edge> edges = this.graphService.getAllEdgesByEntryNode(entrynode);
		ArrayList<InventoryElement> inventory = new ArrayList<>();
		ArrayList<Description> descs = new ArrayList<>();
		ArrayList<Picture> pics = new ArrayList<>();
		for(Edge e : edges) {
			if(e.getDesc() != null) {
				descs.add(e.getDesc());
			}
			if(e.getInventory() != null && e.getInventory().size() > 0) {
				for(InventoryElement ie : e.getInventory()) {
					inventory.add(ie);
				}
			}
			if(e.getPictures() != null && e.getPictures().size() > 0) {
				for(Picture pic : e.getPictures()) {
					pics.add(pic);
				}
			}
		}
		Node n = entrynode;
		
		Integer entrynodeid = entrynode.getId();
		ArrayList<Node> anodes = this.graphService.getAllNodesByEntryNodeId(entrynodeid); 
		ArrayList<Edge> aedges =  this.graphService.getAllEdgesByEntryNodeId(entrynodeid);
		for(InventoryElement ie : this.graphService.getAllInventoryElementsByEntryNode(n)) {
			if(!inventory.contains(ie)) {
				foundbefore = false;
				break;
			}
		}
		for(Picture pic : this.graphService.getAllPicturesByEntryNode(n)) {
			if(!pics.contains(pic)) {
				foundbefore = false;
				break;
			}
		}
		for(Description desc : this.graphService.getAllDescriptionsByEntryNode(n)) {
			if(!descs.contains(desc)) {
				foundbefore = false;
				break;
			}
		}
		if(anodes != null && aedges != null && anodes.size() + aedges.size() < 2  &&
				this.graphService.getAllInventoryElementsByEntryNode(n).size() < 1 &&
				this.graphService.getAllDescriptionsByEntryNode(n).size() < 1 &&
				this.graphService.getAllPicturesByEntryNode(n).size() < 1) {
			foundbefore = false;
		}
		this.graphService.deleteGraphWithAllComponents(entrynode);
		anodes = this.graphService.getAllNodesByEntryNodeId(entrynodeid); 
		aedges =  this.graphService.getAllEdgesByEntryNodeId(entrynodeid);
		if(anodes != null && aedges != null && anodes.size() + aedges.size() > 0 &&
				this.graphService.getAllInventoryElementsByEntryNode(n).size() > 0 &&
				this.graphService.getAllDescriptionsByEntryNode(n).size() > 0 &&
				this.graphService.getAllPicturesByEntryNode(n).size() > 0) {
			foundafter = true;
		}
		assertTrue(foundbefore);
		assertFalse(foundafter);
	}
	@Test
	public void testGetAllXXXPublic() {
		ArrayList<Description> publicdesclist = new ArrayList<>();
		ArrayList<InventoryElement> publicielist = new ArrayList<>();
		ArrayList<Picture> publicpiclist = new ArrayList<>();
		ArrayList<Node> nodelist = new ArrayList<Node>(6);
		ArrayList<Edge> edgelist = new ArrayList<Edge>(4);
		for (int i = 0; i < 6; i++) {
			Node n = new Node();
			n.setLabel("n" + i);
			nodelist.add(n);
			this.nodeService.addNode(n);
		}
		Node entrynode1 = nodelist.get(0);
		entrynode1.setEntry(true);
		Node entrynode2 = nodelist.get(3);
		entrynode2.setEntry(true);
		this.nodeService.updateNode(entrynode1);
		this.nodeService.updateNode(entrynode2);
		for (int k = 0; k < 2; k++) {
			for(int l = 0; l < 2; l++) {
				Edge e = new Edge();
				e.setFromNode(nodelist.get(l+k*3));
				e.setToNode(nodelist.get(l+k*3+1));
				Description d = new Description();
				d.setText("d" + k + "_" + l);
				publicdesclist.add(d);
				e.setDesc(d);
				for(int i = 0; i < 3; ++i) {
					InventoryElement ie = new InventoryElement();
					ie.setText("ie" + l + "_" + k + "_" + i);
					e.addInventoryElement(ie);
					publicielist.add(ie);
				}
				for(int i = 0; i < 3; ++i) {
					Picture pic = new Picture();
					pic.setPathOnServer("path" + l + "_" + k + "_" + i);
					e.addPicture(pic);
					publicpiclist.add(pic);
				}
				edgelist.add(e);
				this.edgeService.addEdge(e);
			}	
		}
		//publikus
		GraphData gd1 = new GraphData();
		GraphData gd2 = new GraphData();
		gd1.setEntryNode(entrynode1);
		gd1.setPublikus(true);
		gd1.setName("gd1");
		gd2.setEntryNode(entrynode2);
		gd2.setPublikus(true);
		gd2.setName("gd2");
		this.gds.addGraphData(gd1);
		this.gds.addGraphData(gd2);
		ArrayList<Description> persisteddesclist= this.graphService.getAllDescriptionsPublic();
		ArrayList<InventoryElement> persistedielist= this.graphService.getAllInventoryPublic();
		ArrayList<Picture> persistedpiclist= this.graphService.getAllPicturesPublic();
		boolean pictest = false;
		boolean ietest = false;
		boolean desctest = false;
		desctest = MyHelper.hasSameElements(persisteddesclist, publicdesclist);
		ietest = MyHelper.hasSameElements(persistedielist, publicielist);
		pictest = MyHelper.hasSameElements(persistedpiclist, publicpiclist);
		assertTrue(desctest && pictest && ietest && !persisteddesclist.contains(this.testdesc.get(0)));
	}
	@Test
	public void testGetGraphAsText() {
		String graphastext = this.graphService.getGraphAsText(this.testgraphdata);
		System.out.println(graphastext);
		assertTrue(graphastext.length() > this.testgraphdata.getName().length() + 5);
	}
	@Test
	public void testAttachDescripton() {
		System.out.println("   **********************     testAttachDescripton()    ********************");
		UserInfo u = new UserInfo();
		u.setEmail("bela@test.hu");
		this.userService.addUser(u);		
		Node n1 = new Node();
		Node n2 = new Node();
		Node n3 = new Node();
		Node n4 = new Node();
		n1.setEntry(true);
		n4.setFinish(true);
		n1.setLabel("n1");
		n2.setLabel("n2");
		n3.setLabel("n3");
		n4.setLabel("n4");
		this.nodeService.addNode(n1);
		this.nodeService.addNode(n2);
		this.nodeService.addNode(n3);
		this.nodeService.addNode(n4);
		Long[] eids = new Long[5];
		Edge[] earr = new Edge[5];
		for (int k = 0; k < 5; ++k) {
			Edge e = new Edge();
			e.setLabel("e" + (k+1));
			eids[k] = this.edgeService.addEdge(e);
			earr[k] = e;
		}
		GraphData gd = new GraphData();
		gd.setName("testAttachDescrip...");
		gd.setEntryNode(n1);
		gd.setUser(u);
		this.gds.addGraphData(gd);  
		earr[0].setFromNode(n1);	//			 n1
		earr[0].setToNode(n2);		//	   e1-> /  \
		earr[1].setFromNode(n2);	//		   n2   \ <-e5
		earr[1].setToNode(n3);		//	      / |    \
		earr[2].setFromNode(n3);	//	e2-> /  |-e4  \  
		earr[2].setToNode(n4);		//      /   |     /
		earr[3].setFromNode(n2);	//     n3   |    /
		earr[3].setToNode(n4);		//      \   |   / 
		earr[4].setFromNode(n1);	//  e3-> \  |  /
		earr[4].setToNode(n4);		//        \ | /
									//		   n4
		Description d1 = new Description();  
		this.descRepo.save(d1);
		Description d2 = new Description();
		d2.setText("d2helloka");
		this.descRepo.save(d2);
		Description d3 = new Description();
		d3.setText(MyHelper.emptydesctext);
		this.descRepo.save(d3);
		Description d4 = new Description();
		d4.setText("d4");
		this.descRepo.save(d4);
		Description d5 = new Description();
		d5.setText("d5");
		this.descRepo.save(d5);
		for (int i = 1; i < 9; i++) {
			InventoryElement ie = new InventoryElement();
			if(i % 2 == 0) {
				ie.setText("iep" + i);
			} else {
				ie.setText("ie" + (i % 4));
			}
			earr[(int)((i-1)/2)].addInventoryElement(ie);
			this.invRepo.save(ie);	
		}
		Picture p1 = new Picture();
		p1.setPathOnServer("p1");
		p1.setSize(10);
		this.picRepo.save(p1);
		Picture p2 = new Picture();
		p2.setPathOnServer("p2");
		p2.setSize(20);
		this.picRepo.save(p2);
		earr[0].setDesc(d1);
		earr[1].setDesc(d2);
		earr[2].setDesc(d3);
		earr[3].setDesc(d4);
		earr[4].setDesc(d5);
		earr[0].addPicture(p1);
		earr[0].addPicture(p2);
		System.out.println("    ****************   graph as text   *******************");
		System.out.println(this.graphService.getGraphAsText(gd));
		//test if components are persisted too
		Edge pe1 = this.edgeService.findById(eids[0]);
		Edge pe2 = this.edgeService.findById(eids[1]);
		Edge pe3 = this.edgeService.findById(eids[2]);
		Edge pe4 = this.edgeService.findById(eids[3]);
		this.edgeService.findById(eids[4]);
		
		assertTrue(this.graphService.getAllDescriptionsByEntryNode(n1).contains(pe3.getDesc())); 
		assertTrue(this.graphService.getAllDescriptionsByEntryNode(n1).contains(pe4.getDesc()));
		assertTrue(this.graphService.getAllInventoryElementsByEntryNode(n1).contains(pe2.getInventory().get(0))); 
		assertTrue(this.graphService.getAllInventoryElementsByEntryNode(n1).contains(pe2.getInventory().get(1)));
		assertTrue(this.graphService.getAllPicturesByEntryNode(n1).contains(pe1.getPictures().get(0))); 
		assertTrue(this.graphService.getAllPicturesByEntryNode(n1).contains(pe1.getPictures().get(1)));
		//test attaching
		ArrayList<Description> attacheddesclist = new ArrayList<Description>(); 
		for(int z = 0; z < 5; z++) {
			Picture pic = new Picture();
			pic.setPathOnServer("attpic" + (z + 1));
			Long pid = this.picRepo.save(pic).getId();
			InventoryElement invelem = new InventoryElement();
			invelem.setText("attie" + (z + 1));
			Long ieid = this.invRepo.save(invelem).getId();
			Description d = new Description();
			d.setText("attd" + (z + 1));
			Long did = this.ds.addDescription(d);
			System.out.println("  Desc (text:" + d.getText() + ") was persisted as " + this.descRepo.findById(did).get().getText() + 
					"[" + this.descRepo.findById(did).get().getId() + "]");
			attacheddesclist.add(d);
			this.graphService.attachDescription(did, eids[z], u);
			this.graphService.attachPicture(pid, eids[z]);
			this.graphService.attachInvElem(ieid, eids[z]);
		}
		System.out.println("    ****************   graph as text  AFTER ATTACHING Description, InventoryElement, Picture *******************");
		System.out.println(this.graphService.getGraphAsText(gd));
		List<Description> orphanitems = this.descRepo.findByUserIdAndEdgeIdIsNull(u.getId());
		System.out.println("     ****************** orphan descs to user (" + u.getEmail() + ")  *****************");
		for(Description d : orphanitems) {
			System.out.println(d.getText() + "[" + d.getId() + "]");
		}
		assertFalse(orphanitems.contains(attacheddesclist.get(0))); // attd1		
		assertFalse(orphanitems.contains(attacheddesclist.get(2))); // attd3		
		assertFalse(orphanitems.contains(attacheddesclist.get(3))); // attd4
		assertFalse(orphanitems.contains(d3));	// <EMPTYDESC> 
		assertTrue(orphanitems.contains(d2));	// d2helloka
		assertTrue(orphanitems.contains(d4)); 	// d4
		assertTrue(orphanitems.contains(d5)); 	// d5
	}
	@Test
	public void testAttachInvElem() {
		System.out.println("   **********************     testAttachInvElem    ********************");
		UserInfo u = new UserInfo();
		u.setEmail("bela@test.hu");
		this.userService.addUser(u);		
		Node n1 = new Node();
		Node n2 = new Node();
		Node n3 = new Node();
		Node n4 = new Node();
		n1.setEntry(true);
		n4.setFinish(true);
		n1.setLabel("n1");
		n2.setLabel("n2");
		n3.setLabel("n3");
		n4.setLabel("n4");
		this.nodeService.addNode(n1);
		this.nodeService.addNode(n2);
		this.nodeService.addNode(n3);
		this.nodeService.addNode(n4);
		Long[] eids = new Long[5];
		Edge[] earr = new Edge[5];
		for (int k = 0; k < 5; ++k) {
			Edge e = new Edge();
			e.setLabel("e" + (k+1));
			eids[k] = this.edgeService.addEdge(e);
			earr[k] = e;
		}
		GraphData gd = new GraphData();
		gd.setName("testAttachDescrip...");
		gd.setEntryNode(n1);
		gd.setUser(u);
		this.gds.addGraphData(gd);  
		earr[0].setFromNode(n1);	//			 n1
		earr[0].setToNode(n2);		//	   e1-> /  \
		earr[1].setFromNode(n2);	//		   n2   \ <-e5
		earr[1].setToNode(n3);		//	      / |    \
		earr[2].setFromNode(n3);	//	e2-> /  |-e4  \  
		earr[2].setToNode(n4);		//      /   |     /
		earr[3].setFromNode(n2);	//     n3   |    /
		earr[3].setToNode(n4);		//      \   |   / 
		earr[4].setFromNode(n1);	//  e3-> \  |  /
		earr[4].setToNode(n4);		//        \ | /
									//		   n4
		Description d1 = new Description();  
		this.descRepo.save(d1);
		Description d2 = new Description();
		d2.setText("d2helloka");
		this.descRepo.save(d2);
		Description d3 = new Description();
		d3.setText(MyHelper.emptydesctext);
		this.descRepo.save(d3);
		Description d4 = new Description();
		d4.setText("d4");
		this.descRepo.save(d4);
		Description d5 = new Description();
		d5.setText("d5");
		this.descRepo.save(d5);
		for (int i = 1; i < 6; i++) {
			InventoryElement ie = new InventoryElement();
			ie.setText("iep" + i);
			earr[i-1].addInventoryElement(ie);
			this.invRepo.save(ie);	
		}
		Picture p1 = new Picture();
		p1.setPathOnServer("p1");
		p1.setSize(10);
		this.picRepo.save(p1);
		Picture p2 = new Picture();
		p2.setPathOnServer("p2");
		p2.setSize(20);
		this.picRepo.save(p2);
		earr[0].setDesc(d1);
		earr[1].setDesc(d2);
		earr[2].setDesc(d3);
		earr[3].setDesc(d4);
		earr[4].setDesc(d5);
		earr[0].addPicture(p1);
		earr[0].addPicture(p2);
		//test if components are persisted too
		Edge pe1 = this.edgeService.findById(eids[0]);
		Edge pe2 = this.edgeService.findById(eids[1]);
		Edge pe3 = this.edgeService.findById(eids[2]);
		Edge pe4 = this.edgeService.findById(eids[3]);
		Edge pe5 = this.edgeService.findById(eids[4]);
		
		assertTrue(this.graphService.getAllInventoryElementsByEntryNode(n1).contains(pe1.getInventory().get(0))); 
		assertTrue(this.graphService.getAllInventoryElementsByEntryNode(n1).contains(pe2.getInventory().get(0)));
		assertTrue(this.graphService.getAllInventoryElementsByEntryNode(n1).contains(pe3.getInventory().get(0))); 
		assertTrue(this.graphService.getAllInventoryElementsByEntryNode(n1).contains(pe4.getInventory().get(0)));
		assertTrue(this.graphService.getAllInventoryElementsByEntryNode(n1).contains(pe5.getInventory().get(0))); 
		//test attaching
		ArrayList<InventoryElement> attachedielist = new ArrayList<>(); 
		for(int z = 0; z < 5; z++) {
			Picture pic = new Picture();
			pic.setPathOnServer("attpic" + (z + 1));
			Long pid = this.picRepo.save(pic).getId();
			InventoryElement invelem = new InventoryElement();
			invelem.setText(z == 4 ? "iep5" : "attie" + (z + 1)); // iep5 already attached
			Long ieid = this.invRepo.save(invelem).getId();
			Description d = new Description();
			d.setText("attd" + (z + 1));
			Long did = this.ds.addDescription(d);
			attachedielist.add(invelem);
			this.graphService.attachDescription(did, eids[z], u);
			this.graphService.attachPicture(pid, eids[z]);
			this.graphService.attachInvElem(ieid, eids[z]); // z == 4 ? then edge is pe5
		}
		List<InventoryElement> orphanitems = this.invRepo.findByUserIdAndEdgeIdIsNull(u.getId());
		assertFalse(orphanitems.contains(attachedielist.get(0))); // attie1		
		assertFalse(orphanitems.contains(attachedielist.get(1))); // attie2		
		assertTrue(pe2.getInventory().contains(attachedielist.get(1)));	// 
		assertTrue(pe3.getInventory().contains(attachedielist.get(2)));	 	// 
		assertTrue(pe4.getInventory().contains(attachedielist.get(3)));
		assertFalse(pe5.getInventory().size() == 2); // no duplication is allowed 
	}
	@Test
	public void testAttachPicture() {
		System.out.println("   **********************     testAttachPicture    ********************");
		UserInfo u = new UserInfo();
		u.setEmail("bela@test.hu");
		this.userService.addUser(u);		
		Node n1 = new Node();
		Node n2 = new Node();
		Node n3 = new Node();
		Node n4 = new Node();
		n1.setEntry(true);
		n4.setFinish(true);
		n1.setLabel("n1");
		n2.setLabel("n2");
		n3.setLabel("n3");
		n4.setLabel("n4");
		this.nodeService.addNode(n1);
		this.nodeService.addNode(n2);
		this.nodeService.addNode(n3);
		this.nodeService.addNode(n4);
		Long[] eids = new Long[5];
		Edge[] earr = new Edge[5];
		for (int k = 0; k < 5; ++k) {
			Edge e = new Edge();
			e.setLabel("e" + (k+1));
			eids[k] = this.edgeService.addEdge(e);
			earr[k] = e;
		}
		GraphData gd = new GraphData();
		gd.setName("testAttachDescrip...");
		gd.setEntryNode(n1);
		gd.setUser(u);
		this.gds.addGraphData(gd);  
		earr[0].setFromNode(n1);	//			 n1
		earr[0].setToNode(n2);		//	   e1-> /  \
		earr[1].setFromNode(n2);	//		   n2   \ <-e5
		earr[1].setToNode(n3);		//	      / |    \
		earr[2].setFromNode(n3);	//	e2-> /  |-e4  \  
		earr[2].setToNode(n4);		//      /   |     /
		earr[3].setFromNode(n2);	//     n3   |    /
		earr[3].setToNode(n4);		//      \   |   / 
		earr[4].setFromNode(n1);	//  e3-> \  |  /
		earr[4].setToNode(n4);		//        \ | /
									//		   n4
		Description d1 = new Description();  
		this.descRepo.save(d1);
		Description d2 = new Description();
		d2.setText("d2helloka");
		this.descRepo.save(d2);
		Description d3 = new Description();
		d3.setText(MyHelper.emptydesctext);
		this.descRepo.save(d3);
		Description d4 = new Description();
		d4.setText("d4");
		this.descRepo.save(d4);
		Description d5 = new Description();
		d5.setText("d5");
		this.descRepo.save(d5);
		for (int i = 1; i < 6; i++) {
			Picture pic = new Picture();
			pic.setPathOnServer("pic" + i);
			earr[i-1].addPicture(pic);
			this.picRepo.save(pic);	
		}
		earr[0].setDesc(d1);
		earr[1].setDesc(d2);
		earr[2].setDesc(d3);
		earr[3].setDesc(d4);
		earr[4].setDesc(d5);
		//test if components are persisted too
		Edge pe1 = this.edgeService.findById(eids[0]);
		Edge pe2 = this.edgeService.findById(eids[1]);
		Edge pe3 = this.edgeService.findById(eids[2]);
		Edge pe4 = this.edgeService.findById(eids[3]);
		Edge pe5 = this.edgeService.findById(eids[4]);
		
		assertTrue(this.graphService.getAllPicturesByEntryNode(n1).contains(pe1.getPictures().get(0))); 
		assertTrue(this.graphService.getAllPicturesByEntryNode(n1).contains(pe5.getPictures().get(0)));
		//test attaching
		ArrayList<Picture> attachedpiclist = new ArrayList<>(); 
		for(int z = 0; z < 5; z++) {
			Picture pic = new Picture();
			pic.setPathOnServer("attpic" + (z + 1));
			Long pid = this.picRepo.save(pic).getId();
			InventoryElement invelem = new InventoryElement();
			invelem.setText("attie" + (z + 1));
			Long ieid = this.invRepo.save(invelem).getId();
			Description d = new Description();
			d.setText("attd" + (z + 1));
			Long did = this.ds.addDescription(d);
			attachedpiclist.add(pic);
			this.graphService.attachDescription(did, eids[z], u);
			this.graphService.attachPicture(pid, eids[z]);
			this.graphService.attachInvElem(ieid, eids[z]);
		}
		List<Picture> orphanitems = this.picRepo.findByUserIdAndEdgeIdIsNull(u.getId());
		assertFalse(orphanitems.contains(attachedpiclist.get(0))); // attie1		
		assertFalse(orphanitems.contains(attachedpiclist.get(4))); // attpic5		
		assertTrue(pe2.getPictures().contains(attachedpiclist.get(1)));	// 
		assertTrue(pe3.getPictures().contains(attachedpiclist.get(2)));	 	// 
		assertTrue(pe4.getPictures().contains(attachedpiclist.get(3)));	 	// 
	}
	@Test
	public void testDetachOrDeleteDescription() {
		System.out.println("   **********************     testDetachOrDeleteDescription()    ********************");
		UserInfo u = new UserInfo();
		u.setEmail("bela@test.hu");
		this.userService.addUser(u);		
		Node n1 = new Node();
		Node n2 = new Node();
		Node n3 = new Node();
		Node n4 = new Node();
		n1.setEntry(true);
		n4.setFinish(true);
		n1.setLabel("n1");
		n2.setLabel("n2");
		n3.setLabel("n3");
		n4.setLabel("n4");
		this.nodeService.addNode(n1);
		this.nodeService.addNode(n2);
		this.nodeService.addNode(n3);
		this.nodeService.addNode(n4);
		Long[] eids = new Long[5];
		Edge[] earr = new Edge[5];
		for (int k = 0; k < 5; ++k) {
			Edge e = new Edge();
			e.setLabel("e" + (k+1));
			eids[k] = this.edgeService.addEdge(e);
			earr[k] = e;
		}
		GraphData gd = new GraphData();
		gd.setName("testDetachDescrip...");
		gd.setEntryNode(n1);
		gd.setUser(u);
		this.gds.addGraphData(gd);  
		earr[0].setFromNode(n1);	//			 n1
		earr[0].setToNode(n2);		//	   e1-> /  \
		earr[1].setFromNode(n2);	//		   n2   \ <-e5
		earr[1].setToNode(n3);		//	      / |    \
		earr[2].setFromNode(n3);	//	e2-> /  |-e4  \  
		earr[2].setToNode(n4);		//      /   |     /
		earr[3].setFromNode(n2);	//     n3   |    /
		earr[3].setToNode(n4);		//      \   |   / 
		earr[4].setFromNode(n1);	//  e3-> \  |  /
		earr[4].setToNode(n4);		//        \ | /
		Description[] desclist = new Description[5];
		Description d1 = new Description();  
		desclist[0] = this.descRepo.save(d1);
		Description d2 = new Description();
		d2.setText("d2helloka");
		desclist[1] = this.descRepo.save(d2);
		Description d3 = new Description();
		d3.setText(MyHelper.emptydesctext);
		desclist[2] = this.descRepo.save(d3);
		Description d4 = new Description();
		d4.setText("d4");
		desclist[3] = this.descRepo.save(d4);
		Description d5 = new Description();
		d5.setText("d5");
		desclist[4] = this.descRepo.save(d5);
		for (int i = 1; i < 6; i++) {
			InventoryElement ie = new InventoryElement();
			ie.setText("iep" + i);
			earr[i-1].addInventoryElement(ie);
			this.invRepo.save(ie);	
		}
		Picture p1 = new Picture();
		p1.setPathOnServer("p1");
		p1.setSize(10);
		this.picRepo.save(p1);
		Picture p2 = new Picture();
		p2.setPathOnServer("p2");
		p2.setSize(20);
		this.picRepo.save(p2);
		earr[0].setDesc(d1);
		earr[1].setDesc(d2);
		earr[2].setDesc(d3);
		earr[3].setDesc(d4);
		earr[4].setDesc(d5);
		earr[0].addPicture(p1);
		earr[0].addPicture(p2);
		System.out.println("    ****************   graph as text BEFORE DETACHING desc  *******************");
		System.out.println(this.graphService.getGraphAsText(gd));
		
		//test detaching
		for (int i = 0; i < 5; ++i) {
			this.graphService.detachOrDeleteDescription(eids[i], u);
		}
				
		System.out.println("    ****************   graph as text AFTER DETACHING Description  *******************");
		System.out.println(this.graphService.getGraphAsText(gd));
		List<Description> orphanitems = this.descRepo.findByUserIdAndEdgeIdIsNull(u.getId());
		System.out.println("     ****************** orphan descs to user (" + u.getEmail() + ") AFTER DETACHING  *****************");
		for(Description d : orphanitems) {
			System.out.println(d.getText() + "[" + d.getId() + "]");
		}
		assertFalse(orphanitems.contains(desclist[0])); // null		
		assertFalse(orphanitems.contains(desclist[2])); // EMPTYDESC		
		assertTrue(orphanitems.contains(d2));	// d2helloka
		assertTrue(orphanitems.contains(d4)); 	// d4
		assertTrue(orphanitems.contains(d5)); 	// d5
	}
	@Test
	public void testDetachOrDeleteInvElem() {
		System.out.println("   **********************     testDetachOrDeleteInvElem()    ********************");
		UserInfo u = new UserInfo();
		u.setEmail("bela@test.hu");
		this.userService.addUser(u);		
		Node n1 = new Node();
		Node n2 = new Node();
		n1.setEntry(true);
		n2.setFinish(true);
		n1.setLabel("start");
		n2.setLabel("end");
		this.nodeService.addNode(n1);
		this.nodeService.addNode(n2);
		Edge e1 = new Edge();
		e1.setLabel("e1");
		
		GraphData gd = new GraphData();
		gd.setName("testDetachOrDeleteInvElem");
		gd.setEntryNode(n1);
		gd.setUser(u);
		this.gds.addGraphData(gd);  
		e1.setFromNode(n1);			//		   start
		e1.setToNode(n2);			//	   e1->  | 
									//		    end
		Description d1 = new Description();  
		d1.setText("d1");
		this.descRepo.save(d1);
		Long[] ieids = new Long[5];
		InventoryElement[] ies = new InventoryElement[5]; 
		for (int i = 1; i < 6; i++) {
			InventoryElement ie = new InventoryElement();
			ie.setText(i==3 ? "" : "iep" + i);
			e1.addInventoryElement(ie);
			ies[i-1] = this.invRepo.save(ie);
			ieids[i-1] = ies[i-1].getId();	
		}
		e1.setDesc(d1);
		Long edgeid = this.edgeService.addEdge(e1);
		System.out.println("    ****************   graph as text BEFORE DETACHING inventory element  *******************");
		System.out.println(this.graphService.getGraphAsText(gd));
		
		//test detaching
		for (int i = 0; i < 3; ++i) {
			this.graphService.detachOrDeleteInvElem(ieids[i], edgeid, u);
		}
				
		System.out.println("    ****************   graph as text AFTER DETACHING inventory elements  *******************");
		System.out.println(this.graphService.getGraphAsText(gd));
		List<InventoryElement> orphanitems = this.invRepo.findByUserIdAndEdgeIdIsNull(u.getId());
		System.out.println("     ****************** orphan inventory to user (" + u.getEmail() + ") AFTER DETACHING  *****************");
		for(InventoryElement ie : orphanitems) {
			System.out.println(ie.getText() + "[" + ie.getId() + "]");
		}
		assertFalse(orphanitems.contains(ies[3])); // iep4 
		assertFalse(orphanitems.contains(ies[4])); // iep5
		assertFalse(orphanitems.contains(ies[2])); // "" 
		assertTrue(orphanitems.contains(ies[1]));  // iep2
		assertTrue(orphanitems.contains(ies[0]));  // iep1
		assertTrue(orphanitems.size() == 2); 	
	}
	@Test
	public void testDetachPicture() {
		System.out.println("   **********************     testDetachPicture()    ********************");
		UserInfo u = new UserInfo();
		u.setEmail("bela@test.hu");
		this.userService.addUser(u);		
		Node n1 = new Node();
		Node n2 = new Node();
		n1.setEntry(true);
		n2.setFinish(true);
		n1.setLabel("start");
		n2.setLabel("end");
		this.nodeService.addNode(n1);
		this.nodeService.addNode(n2);
		Edge e1 = new Edge();
		e1.setLabel("e1");
		
		GraphData gd = new GraphData();
		gd.setName("testDetachPicture");
		gd.setEntryNode(n1);
		gd.setUser(u);
		this.gds.addGraphData(gd);  
		e1.setFromNode(n1);			//		   start
		e1.setToNode(n2);			//	   e1->  | 
									//		    end
		Description d1 = new Description();  
		d1.setText("d1");
		this.descRepo.save(d1);
		Long[] picids = new Long[5];
		Picture[] pics = new Picture[5]; 
		for (int i = 1; i < 6; i++) {
			Picture pic = new Picture();
			pic.setPathOnServer("picp" + i);
			e1.addPicture(pic);
			pics[i-1] = this.picRepo.save(pic);
			picids[i-1] = pics[i-1].getId();	
		}
		e1.setDesc(d1);
		Long edgeid = this.edgeService.addEdge(e1);
		System.out.println("    ****************   graph as text BEFORE DETACHING picture  *******************");
		System.out.println(this.graphService.getGraphAsText(gd));
		
		//test detaching
		for (int i = 0; i < 3; ++i) {
			this.graphService.detachPicture(picids[i], edgeid, u);
		}
				
		System.out.println("    ****************   graph as text AFTER DETACHING pictures  *******************");
		System.out.println(this.graphService.getGraphAsText(gd));
		List<Picture> orphanitems = this.picRepo.findByUserIdAndEdgeIdIsNull(u.getId());
		System.out.println("     ****************** orphan pictures to user (" + u.getEmail() + ") AFTER DETACHING  *****************");
		for(Picture p : orphanitems) {
			System.out.println(p.getPathOnServer() + "[" + p.getId() + "]");
		}
		assertFalse(orphanitems.contains(pics[3])); 
		assertFalse(orphanitems.contains(pics[4])); 
		assertTrue(orphanitems.contains(pics[2])); 
		assertTrue(orphanitems.contains(pics[1])); 
		assertTrue(orphanitems.contains(pics[0])); 
		assertTrue(orphanitems.size() == 3); 	
	}
	@Test
	public void testDetachAllComponents() {
		System.out.println("   **********************     testDetachAllComponents()    ********************");
		UserInfo u = new UserInfo();
		u.setEmail("bela@test.hu");
		this.userService.addUser(u);		
		Node n1 = new Node();
		Node n2 = new Node();
		n1.setEntry(true);
		n2.setFinish(true);
		n1.setLabel("start");
		n2.setLabel("end");
		this.nodeService.addNode(n1);
		this.nodeService.addNode(n2);
		Edge e1 = new Edge();
		e1.setLabel("e1");
		
		GraphData gd = new GraphData();
		gd.setName("testDetachAllComponents");
		gd.setEntryNode(n1);
		gd.setUser(u);
		this.gds.addGraphData(gd);  
		e1.setFromNode(n1);			//		   start
		e1.setToNode(n2);			//	   e1->  | 
									//		    end
		Description d1 = new Description();  
		d1.setText("d1");
		this.descRepo.save(d1);
		
		Long[] ieids = new Long[5];
		InventoryElement[] ies = new InventoryElement[5]; 
		for (int i = 1; i < 6; i++) {
			InventoryElement ie = new InventoryElement();
			ie.setText(i==3 ? "" : "iep" + i);
			e1.addInventoryElement(ie);
			ies[i-1] = this.invRepo.save(ie);
			ieids[i-1] = ies[i-1].getId();	
		}
		
		Long[] picids = new Long[3];
		Picture[] pics = new Picture[3]; 
		for (int i = 1; i < 4; i++) {
			Picture pic = new Picture();
			pic.setPathOnServer("picp" + i);
			e1.addPicture(pic);
			pics[i-1] = this.picRepo.save(pic);
			picids[i-1] = pics[i-1].getId();	
		}
		e1.setDesc(d1);
		Long edgeid = this.edgeService.addEdge(e1);
		System.out.println("    ****************   graph as text BEFORE DETACHING elements  *******************");
		System.out.println(this.graphService.getGraphAsText(gd));
		
		//test detaching
		this.graphService.detachAllComponents(edgeid, u);
				
		System.out.println("    ****************   graph as text AFTER DETACHING elements  *******************");
		System.out.println(this.graphService.getGraphAsText(gd));
		List<InventoryElement> orphanitems = this.invRepo.findByUserIdAndEdgeIdIsNull(u.getId());
		System.out.println("     ****************** orphan inventory to user (" + u.getEmail() + ") AFTER DETACHING  *****************");
		for(InventoryElement ie : orphanitems) {
			System.out.println(ie.getText() + "[" + ie.getId() + "]");
		}
		List<Picture> orphanitems2 = this.picRepo.findByUserIdAndEdgeIdIsNull(u.getId());
		System.out.println("     ****************** orphan pictures to user (" + u.getEmail() + ") AFTER DETACHING  *****************");
		for(Picture p : orphanitems2) {
			System.out.println(p.getPathOnServer() + "[" + p.getId() + "]");
		}
		System.out.println("     ****************** orphan descriptions to user (" + u.getEmail() + ") AFTER DETACHING  *****************");
		List<Description> orphanitems3 = this.descRepo.findByUserIdAndEdgeIdIsNull(u.getId());
		for(Description  d : orphanitems3) {
			System.out.println(" desc: " + d.getText() + " [" + d.getId() + "] edge:" + d.getEdge());
		}
		Optional<Edge> oPersistedEdge = this.edgeRepo.findById(edgeid);
		Edge persistedEdge = null; 
		if(oPersistedEdge.isPresent()) {
			persistedEdge = oPersistedEdge.get();
		}
		assertTrue(orphanitems.size() == 4); 
		assertTrue(orphanitems2.size() == 3);
		assertFalse(orphanitems.contains(ies[2])); 
		if(persistedEdge != null) {
			assertFalse(persistedEdge.getInventory().size() > 0);
			assertFalse(persistedEdge.getPictures().size() > 0);
		}
	}
}
