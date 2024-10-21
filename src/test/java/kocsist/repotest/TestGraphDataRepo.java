package kocsist.repotest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import kocsist.model.GraphData;
import kocsist.model.Node;
import kocsist.model.UserInfo;
import kocsist.repository.GraphDataRepo;
import kocsist.repository.NodeRepo;
import kocsist.repository.UserRepo;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class TestGraphDataRepo {
	@Autowired
	private GraphDataRepo gdr;
	@Autowired
	private UserRepo ur;
	@Autowired
	private NodeRepo nr;
	
	private Random myrandom = new Random();
	private UserInfo testuser;
	
	@BeforeEach
	public void setUp() {
		Iterator<UserInfo> userIt = ur.findAll().iterator();
		ArrayList<UserInfo> testuserlist = new ArrayList<>();
		int k = 0;
		while(userIt.hasNext()) {
			testuserlist.add(userIt.next());
			k++;
		}
		if(k > 0) {
			this.testuser = testuserlist.get(this.myrandom.nextInt(k));
		} else {
			this.testuser = new UserInfo();
			this.testuser.setEmail("vki" + this.myrandom.nextInt(3000) + "@mailserver.hu");
			this.testuser.setName((char)(65+this.myrandom.nextInt(30)) + "_" +
							(char)(65+this.myrandom.nextInt(30)));
			this.ur.save(this.testuser);
		}
	}
	
	@Test
	public void testFindByNameLikeAndUserId() {
		int testgraphdataid;
		final String partofname = "xWyT";
		
		Node n = new Node();
		n.setEntry(true);
		n.setLabel("n1");
		this.nr.save(n);
		
		GraphData gd = new GraphData();
		gd.setName("t" + this.myrandom.nextInt(99) + partofname + this.myrandom.nextInt(99));
		gd.setEntryNode(n);
		gd.setPublikus(this.myrandom.nextDouble() > 0.5 ? true : false);
		gd.setUser(this.testuser);
		gd = this.gdr.save(gd);
		testgraphdataid = gd.getId();
		
		GraphData gdtest = this.gdr.findById(testgraphdataid).get();
		String namelike = "%" + partofname + "%";
		assertTrue(this.gdr.findByNameLikeAndUserId(namelike, this.testuser.getId()).contains(gdtest));
	}
	
	@Test
	public void testFindByPublikus() {
		GraphData gd;
		
		Node n= new Node();
		n.setEntry(true);
		n.setLabel("Start");
		this.nr.save(n);
		gd = new GraphData();
		gd.setName("z" + this.myrandom.nextInt(99));
		gd.setEntryNode(n);
		gd.setPublikus(true);
		gd.setUser(this.testuser);
		this.gdr.save(gd);
		
		assertTrue(this.gdr.findByPublikus(true).contains(gd));
	}
	
	@Test
	public void testFindByEntryNodeId() {
		//preparation
		GraphData dummygd = new GraphData();
		dummygd.setPublikus(false);
		dummygd.setName("gd1");
		Node testentrynode = new Node();
		testentrynode.setEntry(true);
		testentrynode.setLabel("n1");
		this.nr.save(testentrynode);
		dummygd.setEntryNode(testentrynode);
		this.gdr.save(dummygd);
		
		GraphData dummygd2 = new GraphData();
		dummygd2.setPublikus(true);
		dummygd2.setName("gd2");
		Node testentrynode2 = new Node();
		testentrynode2.setEntry(true);
		testentrynode2.setLabel("n2");
		this.nr.save(testentrynode2);
		dummygd2.setEntryNode(testentrynode2);
		this.gdr.save(dummygd2);
		
		GraphData dummygd3 = new GraphData();
		dummygd3.setPublikus(false);
		dummygd3.setName("gd3");
		this.gdr.save(dummygd3);
		Node testentrynode3 = new Node();
		testentrynode3.setEntry(true);
		testentrynode3.setLabel("n3");
		this.nr.save(testentrynode3);
		
		GraphData foundgd1 = this.gdr.findByEntryNodeId(testentrynode.getId());
		GraphData foundgd2 = this.gdr.findByEntryNodeId(testentrynode2.getId());
		GraphData foundgd3 = this.gdr.findByEntryNodeId(testentrynode3.getId());
		assertNotNull(foundgd1);
		assertNotNull(foundgd2);
		assertNull(foundgd3);
		assertEquals(foundgd2.getId(), testentrynode2.getId());
	}
}
