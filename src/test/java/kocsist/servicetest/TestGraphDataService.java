package kocsist.servicetest;
 

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import kocsist.model.GraphData;
import kocsist.model.Node;
import kocsist.model.UserInfo;
import kocsist.service.interfaces.GraphDataService;
import kocsist.service.interfaces.NodeService;
import kocsist.service.interfaces.UserService;

@DataJpaTest
public class TestGraphDataService {
		
	@Autowired
	private UserService us;
	@Autowired
	private NodeService ns;
	@Autowired
	private GraphDataService gds;
	
	private long graphdatacount;
	private String testgdname;
	private UserInfo testuser;
	private Random myrandom = new Random();
	final private String partofname = "terv"; 
	
	@BeforeEach
	//@Rollback(false)
	public void setUp() {
		UserInfo myuser;
		GraphData mygraphdata = new GraphData();
		graphdatacount = gds.count();
				
		Node mynode = new Node();
		mynode.setEntry(true);
		mynode.setLabel("n1");
		mynode.setX(30 + this.myrandom.nextInt(300));
		mynode.setY(this.myrandom.nextInt(200));
		this.ns.addNode(mynode);
		
		Iterator<UserInfo> myuserit = this.us.getAll().iterator();
		ArrayList<Integer> userids = new ArrayList<>();   
		int k = 0;
		while (myuserit.hasNext()) {
			userids.add(myuserit.next().getId());
			k++;
		}
		if (k > 0) {
			myuser = this.us.findById(userids.get(myrandom.nextInt(k)));
		}
		else {
			myuser = new UserInfo();
			myuser.setEmail(String.format("tomkocsi%d@gmail.com",myrandom.nextInt(1000)));
			myuser.setName("Chokito");
			myuser.setPassword("pwd");
			this.us.addUser(myuser);
		}
		this.testuser = myuser; 		
		this.testgdname = this.partofname + myuser.getId();
				
		mygraphdata.setName(this.testgdname);
		mygraphdata.setEntryNode(mynode);
		mygraphdata.setPublikus(false);
		
		myuser.addGraphData(mygraphdata);
		this.us.updateUser(myuser);
		
	}
	
	@Test
	//@Rollback(false)
	public void testGraphDataCountAll() {
		assertEquals(gds.count(), this.graphdatacount+1);
	}
	
	@Test
	//@Rollback(false)
	public void testFindByNameContainingByUserId() {
		Iterator<GraphData> graphdataIt = gds.getAllByUserId(testuser.getId()).iterator();
		ArrayList<GraphData> graphdatas = new ArrayList<>();
		while(graphdataIt.hasNext()) {
			GraphData gd = graphdataIt.next();
			if (gd.getName().indexOf(partofname) != -1) {
				graphdatas.add(gd);
			}
		}
		assertEquals(gds.findByNameContainingByUserId(partofname,testuser.getId()).size(),
				graphdatas.size());
	}
	
}
