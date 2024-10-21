package kocsist.servicetest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import kocsist.config.PicHandlingProperties;
import kocsist.model.Edge;
import kocsist.model.Node;
import kocsist.model.Picture;
import kocsist.model.UserInfo;
import kocsist.service.interfaces.EdgeService;

import kocsist.service.interfaces.NodeService;
import kocsist.service.interfaces.PictureService;
import kocsist.service.interfaces.UserService;

@DataJpaTest
public class TestPictureService {
	@Autowired
	private PictureService pictureService;
	@Autowired
	private UserService userService;
	@Autowired
	private EdgeService edgeService;
	@Autowired
	private NodeService nodeService;
	@Autowired
	private PicHandlingProperties picHandlingProps;
	
	private Integer testuserid;
	private Edge testedge;
	private ArrayList<Picture> testpicturelist = new ArrayList<>();
	private Random myrandom = new Random();
	
	@BeforeEach
	//@Rollback(false)
	public void setUp() {
		UserInfo testuser = new UserInfo();
		int testnum = this.myrandom.nextInt(5000);
		testuser.setEmail("tps" + testnum + "@mail.com");
		this.testuserid = this.userService.addUser(testuser);
		
		Node entrynode = new Node();
		entrynode.setEntry(true);
		Node othernode = new Node();
		this.nodeService.addNode(othernode);
		this.nodeService.addNode(entrynode);
		Edge edge = new Edge();
		edge.setFromNode(entrynode);
		edge.setToNode(othernode);
		for (int j = 0; j < 4; j++) {
			StringBuilder sb = new StringBuilder(); 
			Picture pic = new Picture();
			pic.setUser(testuser);
			Long picId = this.pictureService.addPicture(pic);
			sb.append(this.picHandlingProps.getPicturefolder());
			sb.append("//");
			sb.append(picId).append(".jpg");
			pic.setPathOnServer(sb.toString());
			this.testpicturelist.add(pic);
			edge.addPicture(pic);
			System.out.println("  " + pic.getPathOnServer().toString());
		}
		this.edgeService.addEdge(edge);
		entrynode.addEdgeToOutgoingEdges(edge);
		this.testedge = edge;
	}
	
	@Test
	//@Rollback(false)
	public void testGetPictureByUserId() {
		Picture testpic = this.testpicturelist.get(this.myrandom.nextInt(this.testpicturelist.size()));
		assertTrue(this.pictureService.getPictureByUserId(this.testuserid).contains(testpic));
	}
	
	@Test
	//@Rollback(false)
	public void testGetPictureByEdgeId() {
		Picture testpic = this.testpicturelist.get(this.myrandom.nextInt(this.testpicturelist.size()));
		assertTrue(this.pictureService.getPictureByEdgeId(testedge.getId()).contains(testpic));
	}
}
