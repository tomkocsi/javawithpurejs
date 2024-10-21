package kocsist.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kocsist.DTOmodel.EdgeDTO;
import kocsist.DTOmodel.ParamDTO;
import kocsist.DTOmodel.ParamEdgeDTO;
import kocsist.DTOmodel.PlainTextDTO;
import kocsist.blogic.MyHelper;
import kocsist.model.Description;
import kocsist.model.Edge;
import kocsist.model.GraphData;
import kocsist.model.InventoryElement;
import kocsist.model.Node;
import kocsist.model.Picture;
import kocsist.model.UserInfo;
import kocsist.service.interfaces.DescriptionService;
import kocsist.service.interfaces.EdgeService;
import kocsist.service.interfaces.GraphDataService;
import kocsist.service.interfaces.GraphService;
import kocsist.service.interfaces.NodeService;
import kocsist.service.interfaces.UserService;
@RestController
public class EdgeController {
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
	@Autowired
	private DescriptionService ds;
			
	@PostMapping(value="/api/dataexchange/graphedges")
	public ResponseEntity<ArrayList<EdgeDTO>> getGraphEdges(@RequestBody ParamDTO paramdto){
		if(paramdto != null && paramdto.getUseremail() != null && !"".equals(paramdto.getUseremail())) {
			UserInfo user = this.us.findByEmail(paramdto.getUseremail());
			if(user != null) {
				Integer graphid = paramdto.getEntityid().intValue();
				if(graphid != null) {
					GraphData gd = this.gds.findById(graphid);
					if(gd != null && gd.getUser().equals(user)) {
						ArrayList<EdgeDTO> dtolist = new ArrayList<>();
						ArrayList<Edge> graphedgelist = this.gs.getAllEdgesByEntryNode(gd.getEntryNode());
						if(graphedgelist != null && graphedgelist.size() > 0) {
							for(Edge e : graphedgelist) {
								EdgeDTO dto = new EdgeDTO();
								dto.buildByEdge(e);
								dtolist.add(dto);
							}
						}
						return new ResponseEntity<ArrayList<EdgeDTO>>(dtolist, null, HttpStatus.OK); 
					}
					return new ResponseEntity<ArrayList<EdgeDTO>>(null, null, HttpStatus.OK);
				}
			}
		}
		return new ResponseEntity<ArrayList<EdgeDTO>>(null, null, HttpStatus.NOT_FOUND);
	}
	
	@PostMapping(value="/api/dataexchange/getedgedata")
	public ResponseEntity<EdgeDTO> getEdgeDTO(@RequestBody ParamDTO paramdto){
		if(paramdto != null && paramdto.getEntityid() != null) {
			Long id = paramdto.getEntityid();
			Edge e = this.es.findById(id);
			if(e != null) {
				EdgeDTO dto = new EdgeDTO();
				dto.buildByEdge(e);
				return new ResponseEntity<EdgeDTO>(dto, null, HttpStatus.OK); 
			}
			return new ResponseEntity<EdgeDTO>(null, null, HttpStatus.OK);
		}
		return new ResponseEntity<EdgeDTO>(null, null, HttpStatus.NOT_FOUND);
	}
	@PostMapping(value="/api/dataexchange/updateedgedata")
	public ResponseEntity<PlainTextDTO> updateEdgeData(@RequestBody EdgeDTO dto){
		if(dto != null) {
			PlainTextDTO ptdto = new PlainTextDTO();
			Edge e = null;
			try {
				e = this.es.findById(dto.getId());
			} catch (Exception exc) {
				ptdto.setText(exc.getMessage());
				return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
			}
			if(e != null) {
				e.setLabel(dto.getLabel());
				e.setTime1(dto.getTime1());
				e.setTime2(dto.getTime2());
				this.es.updateEdge(e);
				return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
			} else {
				ptdto.setText("nem találtam az entitást");
				return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
			}
		}
		return new ResponseEntity<PlainTextDTO>(null, null, HttpStatus.NOT_FOUND);
	}
	@PostMapping(value="/api/dataexchange/createedgedata")
	public ResponseEntity<EdgeDTO> createEdge(@RequestBody ParamEdgeDTO paramdto){
		if(paramdto != null && paramdto.getEdgedto() != null) {
			EdgeDTO dto = paramdto.getEdgedto();
			Description desc;
			Edge e = new Edge();
			if(paramdto.getEntityid() != null) {
				Integer graphid = paramdto.getEntityid().intValue();
				GraphData gd = this.gds.findById(graphid);
				if(gd != null && gd.getEntryNode() != null) {
					List<Edge> edges = this.gs.getAllEdgesByEntryNode(gd.getEntryNode());
					String label;
					if(edges != null & edges.size() > 0) {
						label = MyHelper.getNewEdgeLabel("e", edges);
					} else {
						label = "e1";
					}
					e.setLabel(label);
					dto.setLabel(label);
				}
			}
			e.setTime1(dto.getTime1());
			e.setTime2(dto.getTime2());
			Node fromnode = this.ns.findById(dto.getFromNodeId());
			if(fromnode != null) {
				e.setFromNode(fromnode);
			}
			Node tonode = this.ns.findById(dto.getToNodeId());
			if(tonode != null) {
				e.setToNode(tonode);
			}
			if(dto.getDescriptionId() == null) {
				desc = new Description();
				desc.setText(MyHelper.emptydesctext);
				if (paramdto.getUseremail() != null) {
					UserInfo u = this.us.findByEmail(paramdto.getUseremail());
					if(u != null) {
						desc.setUser(u);
					}
				}
				Long descid = this.ds.addDescription(desc);
				e.setDesc(desc);
				dto.setDescriptionId(descid);
			} else {
				desc = this.ds.findById(dto.getDescriptionId());
				if(desc != null) {
					e.setDesc(desc);
				}
			}
			dto.setId(this.es.addEdge(e));
			desc.setEdge(e);
			return new ResponseEntity<EdgeDTO>(dto, null, HttpStatus.OK);
		}
		return new ResponseEntity<EdgeDTO>(null, null, HttpStatus.NOT_FOUND);	
	}
	@PostMapping(value="/api/dataexchange/removeedgedata")
	public ResponseEntity<PlainTextDTO> removeEdge(@RequestBody ParamDTO paramdto){
		PlainTextDTO ptdto = new PlainTextDTO();
		if(paramdto != null && paramdto.getEntityid() != null){
			Long edgeid = paramdto.getEntityid();
			try {
				Edge e = this.es.findById(edgeid);
				if(e != null) {
					this.es.deleteEdgeById(edgeid);
					return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
				} else {
					ptdto.setText("No Edge with id: " + edgeid.toString());
					return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);		
				}
			} catch (Exception ex) {
				ptdto.setText(ex.getMessage());
				return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
			}
		}
		return new ResponseEntity<PlainTextDTO>(null, null, HttpStatus.NOT_FOUND); 
	}
	@PostMapping(value="/api/dataexchange/attachcomponent")
	public ResponseEntity<PlainTextDTO> attachComponentToEdge(@RequestBody ParamDTO paramdto){
		PlainTextDTO ptdto = new PlainTextDTO();
		if(paramdto != null && paramdto.getEntityid() != null && 
				paramdto.getComponentid() != null && 
				paramdto.getTextparam() != null && !"".equals(paramdto.getTextparam()) &&
				paramdto.getUseremail() != null && !"".equals(paramdto.getUseremail())) {
			Long eid = paramdto.getEntityid();
			Long cid = paramdto.getComponentid();
			String useremail = paramdto.getUseremail();
			switch(paramdto.getTextparam()) {
				case "desc" :
					UserInfo user = this.us.findByEmail(useremail);
					this.gs.attachDescription(cid, eid, user);
					break;
				case "invelem" :
					this.gs.attachInvElem(cid, eid);
					break;
				case "pic" :
					this.gs.attachPicture(cid, eid); 
					break;
				default:
					break;
			}
			return new ResponseEntity<PlainTextDTO>(ptdto , null, HttpStatus.OK);
		}
		ptdto.setText("Entity not found.");
		return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
	}
	@PostMapping(value="/api/dataexchange/detachcomponent")
	public ResponseEntity<PlainTextDTO> detachComponentFromEdge(@RequestBody ParamDTO paramdto){
		PlainTextDTO ptdto = new PlainTextDTO();
		if(paramdto != null && paramdto.getEntityid() != null && 
				paramdto.getComponentid() != null && 
				paramdto.getTextparam() != null && !"".equals(paramdto.getTextparam()) &&
				paramdto.getUseremail() != null && !"".equals(paramdto.getUseremail())) {
			Long eid = paramdto.getEntityid();
			Long cid = paramdto.getComponentid();
			String useremail = paramdto.getUseremail();
			UserInfo user = this.us.findByEmail(useremail);
			switch(paramdto.getTextparam()) {
				case "desc" :
					this.gs.detachOrDeleteDescription(eid, user);
					break;
				case "invelem" :
					this.gs.detachOrDeleteInvElem(cid, eid, user);
					break;
				case "pic" :
					this.gs.detachPicture(cid, eid, user); 
					break;
				default:
					break;
			}
			return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
		}
		ptdto.setText("Request data is missing or incomplete.");
		return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
	}
	@PostMapping(value="/api/dataexchange/detachall")
	public ResponseEntity<PlainTextDTO> detachAllFromEdge(@RequestBody ParamDTO paramdto){
		PlainTextDTO ptdto = new PlainTextDTO();
		if(paramdto != null) {
			Long edgeid = paramdto.getEntityid();
			String useremail = paramdto.getUseremail();
			if(edgeid != null && useremail != null && !"".equals(useremail)) {
				Edge e = this.es.findById(edgeid);
				UserInfo u = this.us.findByEmail(useremail);
				if(e != null && u != null) {
					try {
						//this.gs.detachAllComponents(e.getId(), u);
						if(e.getInventory().size() > 0) {
							ArrayList<Long> ieidlist = new ArrayList<>(); 
							for(InventoryElement item : e.getInventory()) {
								ieidlist.add(item.getId());
							}
							for(Long id : ieidlist) {
								this.gs.detachOrDeleteInvElem(id, edgeid, u);
								System.out.println("detached invelem(" + id + ")");
							}
						}
						if(e.getPictures().size() > 0) {
							ArrayList<Long> picidlist = new ArrayList<>(); 
							for(Picture item : e.getPictures()) {
								picidlist.add(item.getId());
							}
							for(Long id : picidlist) {
								this.gs.detachPicture(id, edgeid, u);
								System.out.println("detached pic(" + id + ")");
							}
						}
						if(e.getDesc() != null) {
							Long descid = e.getDesc().getId();
							this.gs.detachOrDeleteDescription(e.getId(), u);
							System.out.println("detached desc(" + descid + ")");
						}
					} catch(Exception err) {
						ptdto.setText(err.getMessage());
					}
					return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
				}
			}
			ptdto.setText("could not find edge or user entitity");
			return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
		}
		ptdto.setText("Request data is missing or incomplete.");
		return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
	}
}
