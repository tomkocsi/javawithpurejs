package kocsist.controller;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kocsist.DTOmodel.NodeDTO;
import kocsist.DTOmodel.NodeDTOLight;
import kocsist.DTOmodel.ParamDTO;
import kocsist.DTOmodel.ParamNodeDTO;
import kocsist.DTOmodel.PlainTextDTO;
import kocsist.blogic.MyHelper;
import kocsist.model.Edge;
import kocsist.model.GraphData;
import kocsist.model.Node;
import kocsist.model.UserInfo;
import kocsist.service.interfaces.EdgeService;
import kocsist.service.interfaces.GraphDataService;
import kocsist.service.interfaces.GraphService;
import kocsist.service.interfaces.NodeService;
import kocsist.service.interfaces.UserService;
@RestController
public class NodeController {
	@Autowired
	private GraphService gs;
	@Autowired
	private GraphDataService gds;
	@Autowired
	private UserService us;
	@Autowired
	private EdgeService es;
	@Autowired
	private NodeService ns;
		
	@PostMapping(value="/api/dataexchange/graphnodesbyid")
	public ResponseEntity<ArrayList<NodeDTO>> getGraphNodesById(@RequestBody ParamDTO paramdto){
		if(paramdto != null && paramdto.getUseremail() != null && !"".equals(paramdto.getUseremail())) {
			UserInfo user = this.us.findByEmail(paramdto.getUseremail());
			if(user != null) {
				Integer graphid = paramdto.getEntityid().intValue();
				if(graphid != null && !"".equals(graphid)) {
					GraphData gd = this.gds.findById(graphid);
					if(gd != null && gd.getUser().equals(user)) {
						ArrayList<NodeDTO> dtolist = new ArrayList<>();
						ArrayList<Node> graphnodelist = this.gs.getAllNodesByEntryNode(gd.getEntryNode());
						if(graphnodelist != null && graphnodelist.size() > 0) {
							for(Node n : graphnodelist) {
								NodeDTO dto = new NodeDTO();
								dto.buildByNode(n);
								dtolist.add(dto);
							}
						}
						return new ResponseEntity<ArrayList<NodeDTO>>(dtolist, null, HttpStatus.OK); 
					}
					return new ResponseEntity<ArrayList<NodeDTO>>(null, null, HttpStatus.OK);
				}
			}
		}
		return new ResponseEntity<ArrayList<NodeDTO>>(null, null, HttpStatus.NOT_FOUND);
	}
	
	@PostMapping(value="/api/dataexchange/getnodedata")
	public ResponseEntity<NodeDTO> getNodeDTO(@RequestBody ParamDTO paramdto){
		if(paramdto != null && paramdto.getEntityid() != null) {
			Integer id = paramdto.getEntityid().intValue();
			Node n = this.ns.findById(id);
			if(n != null) {
				NodeDTO dto = new NodeDTO();
				dto.buildByNode(n);
				return new ResponseEntity<NodeDTO>(dto, null, HttpStatus.OK); 
			}
			return new ResponseEntity<NodeDTO>(null, null, HttpStatus.OK);
		}
		return new ResponseEntity<NodeDTO>(null, null, HttpStatus.NOT_FOUND);
	}
	@PostMapping(value="/api/dataexchange/updatenodedata")
	public ResponseEntity<PlainTextDTO> updateNodeData(@RequestBody NodeDTO dto){
		if(dto != null) {
			PlainTextDTO ptdto = new PlainTextDTO();
			Node n = this.ns.findById(dto.getId());
			if(n != null) {
				if(dto.getLabel() != null) {
					n.setLabel(dto.getLabel());
				}
				if(dto.getX() != null) {
					n.setX(dto.getX());	
				}
				if(dto.getY() != null) {
					n.setY(dto.getY());	
				}
				if(dto.isEntry()) {
					n.setEntry(true);
				}
				if(dto.isFinish()) {
					n.setFinish(true);
				}
				if(dto.getOutgoingEdges() != null && dto.getOutgoingEdges().length > 0) {
					for(int eid : dto.getOutgoingEdges()) {
						Edge e = this.es.findById(Long.valueOf(eid));
						if(e != null) {
							n.addEdgeToOutgoingEdges(e); // method checks duplicates
						}
					}
				}
				this.ns.updateNode(n);
				return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
			}
			ptdto.setText("nem találtam az entitást");
			return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
		}
		return new ResponseEntity<PlainTextDTO>(null, null, HttpStatus.NOT_FOUND);
	}
	@PostMapping(value="/api/dataexchange/updatenodedatalight")
	public ResponseEntity<PlainTextDTO> updateNodeDataLight(@RequestBody NodeDTOLight dto){
		PlainTextDTO ptdto = new PlainTextDTO();
		if(dto != null) {
			Node n = this.ns.findById(dto.getId());
			if(n != null) {
				if(dto.getX() != null) {
					n.setX(dto.getX());	
				}
				if(dto.getY() != null) {
					n.setY(dto.getY());	
				}
				this.ns.updateNode(n);
				ptdto.setText("OK");
				return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
			}
			ptdto.setText("nem találtam az entitást");
			return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
		}
		return new ResponseEntity<PlainTextDTO>(null, null, HttpStatus.NOT_FOUND);
	}
	@PostMapping(value="/api/dataexchange/createnodedata")
	public ResponseEntity<NodeDTO> createNode(@RequestBody ParamNodeDTO paramdto){
		if(paramdto != null && paramdto.getNodedto() != null && paramdto.getEntityid() != null) {
			Node n = new Node();
			NodeDTO dto = paramdto.getNodedto();
			if(dto.getLabel() != null && !"".equals(dto.getLabel())) {
				n.setLabel(dto.getLabel());
			} else {
				if(paramdto.getEntityid() != null) {
					Integer graphdataid = Integer.valueOf(paramdto.getEntityid().intValue());
					GraphData gd = this.gds.findById(graphdataid);
					if(gd != null) {
						String newlabel = MyHelper
								.getNewNodeLabel("n", 
									this.gs.getAllNodesByEntryNode(gd.getEntryNode()));
						n.setLabel(newlabel);
					}
				}
			}
			if(dto.getX() != null && dto.getY() != null) {
				n.setX(dto.getX());
				n.setY(dto.getY());
			}
			if(dto.isEntry()) {
				n.setEntry(true);
			}
			if(dto.isFinish()) {
				n.setFinish(true);
			}
			dto.setId(this.ns.addNode(n));
			dto.setLabel(n.getLabel());
			return new ResponseEntity<NodeDTO>(dto, null, HttpStatus.OK);
		}
		return new ResponseEntity<NodeDTO>(null, null, HttpStatus.NOT_FOUND);	
	}
	@PostMapping(value="/api/dataexchange/removenodedata")
	public ResponseEntity<PlainTextDTO> removeNodeDTO(@RequestBody ParamDTO paramdto){
		PlainTextDTO ptdto = new PlainTextDTO();
		if(paramdto != null && paramdto.getComponentid() != null){
			Integer nodeid = paramdto.getComponentid().intValue();
			Node n = this.ns.findById(nodeid);
			if(n != null) {
				try {
					if(paramdto.getEntityid() != null) {
						Integer graphid = paramdto.getEntityid().intValue();
						this.gs.removeNodeRefFromEdges(nodeid, graphid);
					}
					this.ns.deleteNodeById(n.getId());
					return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
				} catch (Exception ex) {
					ptdto.setText(ex.getMessage());
					return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
				}
			}	
			ptdto.setText("No Node with id: " + nodeid);
			return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
		}
		return new ResponseEntity<PlainTextDTO>(null, null, HttpStatus.NOT_FOUND); 
	}
}
