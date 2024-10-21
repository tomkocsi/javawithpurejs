package kocsist.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kocsist.DTOmodel.ComponentIdsDTO;
import kocsist.DTOmodel.DescDTO;
import kocsist.DTOmodel.EdgeDTO;
import kocsist.DTOmodel.FullGraphDTO;
import kocsist.DTOmodel.GraphDTO;
import kocsist.DTOmodel.GraphDataDTO;
import kocsist.DTOmodel.InvElemDTO;
import kocsist.DTOmodel.NodeDTO;
import kocsist.DTOmodel.ParamDTO;
import kocsist.DTOmodel.ParamGraphDataDTO;
import kocsist.DTOmodel.PicDataDTO;
import kocsist.DTOmodel.PlainTextDTO;
import kocsist.blogic.MyHelper;
import kocsist.factory.GraphBuilder;
import kocsist.factory.GraphBuilderFactory;
import kocsist.factory.GraphBuilderType;
import kocsist.model.Description;
import kocsist.model.Edge;
import kocsist.model.GraphData;
import kocsist.model.InventoryElement;
import kocsist.model.Node;
import kocsist.model.Picture;
import kocsist.model.UserInfo;
import kocsist.repository.DescriptionRepo;
import kocsist.repository.InventoryRepo;
import kocsist.repository.PictureRepo;
import kocsist.service.interfaces.EdgeService;
import kocsist.service.interfaces.GraphDataService;
import kocsist.service.interfaces.GraphService;
import kocsist.service.interfaces.NodeService;
import kocsist.service.interfaces.UserService;


@RestController
public class GraphDataController {
	@Autowired
	private EdgeService es;
	@Autowired
	private NodeService ns;
	@Autowired
	private GraphService gs;
	@Autowired
	private GraphDataService gds;
	@Autowired
	private UserService us;
	@Autowired
	private PictureRepo picRepo;
	@Autowired
	private DescriptionRepo descRepo;
	@Autowired
	private InventoryRepo ieRepo;
			
	@PostMapping(value="/api/dataexchange/newgraphdto")
	public ResponseEntity<GraphDTO> getNewGraphDTO(@RequestBody ParamDTO paramdto){
		if(paramdto != null && paramdto.getUseremail() != null && !"".equals(paramdto.getUseremail())) {
			System.out.println(paramdto.toString());
			Long entityid = paramdto.getEntityid();
			int nodenum = 3;
			if(entityid != null) {
				nodenum = paramdto.getEntityid().intValue();
				System.out.println("nodenum :" + nodenum);
			}
			String gbtype = paramdto.getTextparam();		
			UserInfo user = this.us.findByEmail(paramdto.getUseremail());
			if(user != null) {
				int node_num = (nodenum < 2 ? 2 : nodenum);
				GraphBuilderFactory gbf = new GraphBuilderFactory(node_num); 				
				gbf.setServices(es, ns, gds, gs);
				GraphBuilder gb;
				if(gbtype != null && gbtype.equals("linear")) {
					gb = gbf.getGraphBuilder(GraphBuilderType.LINEAR, user);
				} else {
					gb = gbf.getGraphBuilder(GraphBuilderType.LARGE, user);
				}
				GraphData gd = gb.build(16);
				GraphDTO gdto = new GraphDTO();
				gdto.setGraphDataName(gd.getName());
				gdto.setGraphDataId(gd.getId());
				gdto.setEdges(gb.getEdgeDTOs());
				gdto.setNodes(gb.getNodeDTOs());
				gdto.setNodeR(16);
				return new ResponseEntity<GraphDTO>(gdto, HttpStatus.OK);
			} else {
				return new ResponseEntity<GraphDTO>(null, null, HttpStatus.OK);
			}
		}
		return new ResponseEntity<GraphDTO>(null, null, HttpStatus.OK);
	}
	@PostMapping(value="/api/dataexchange/graphdto")
	public ResponseEntity<GraphDTO> getGraphDTOById(@RequestBody ParamDTO paramdto){
		if(paramdto != null && paramdto.getUseremail() != null && !"".equals(paramdto.getUseremail())) {
			Long graphid = paramdto.getEntityid();
			UserInfo user = this.us.findByEmail(paramdto.getUseremail());
			if(user != null) {
				if(graphid != null) {
					GraphData gd = this.gds.findById(graphid.intValue());
					if(gd != null && gd.getUser().equals(user)) {
						Node entrynode = gd.getEntryNode();
						List<Edge> edges = this.gs.getAllEdgesByEntryNode(entrynode);
						List<Node> nodes = this.gs.getAllNodesByEntryNode(entrynode);
						ArrayList<NodeDTO> nodeDTOs = new ArrayList<NodeDTO>();
						ArrayList<EdgeDTO> edgeDTOs = new ArrayList<EdgeDTO>();
						for (Node n : nodes) {
							NodeDTO ndto = new NodeDTO();
							ndto.buildByNode(n);
							nodeDTOs.add(ndto);
						}
						for (Edge e : edges) {
							EdgeDTO edto = new EdgeDTO();
							edto.buildByEdge(e);
							edgeDTOs.add(edto);
						}
						GraphDTO gdto = new GraphDTO();
						gdto.setGraphDataName(gd.getName());
						gdto.setGraphDataId(gd.getId());
						gdto.setEdges(edgeDTOs);
						gdto.setNodes(nodeDTOs);
						gdto.setNodeR(16);
						return new ResponseEntity<GraphDTO>(gdto, HttpStatus.OK);
					}
				}
				return new ResponseEntity<GraphDTO>(null, null, HttpStatus.OK);
			}
		}
		return new ResponseEntity<GraphDTO>(null, null, HttpStatus.OK);
	}
	@PostMapping(value="/api/dataexchange/allgraphnamescontainigtext")
	public ResponseEntity<ArrayList<GraphDataDTO>> getAllGraphNamesContainingText(@RequestBody ParamDTO paramdto){
		if(paramdto != null && paramdto.getTextparam() != null && !"".equals(paramdto.getTextparam())) {
			String useremail = paramdto.getUseremail();
			String text = paramdto.getTextparam(); 
			ArrayList<GraphDataDTO> gdtos;
			List<GraphData> AllAccessibleGDs = this.gds.findByNameContainingAndPublic(text);
			if(useremail != null && !"".equals(useremail)) {
				UserInfo user = this.us.findByEmail(useremail);
				if(user != null) {
					List<GraphData> PrivateGDs = 
							this.gds.findByNameContainingByUserId(text,user.getId());
					for(GraphData gd : PrivateGDs) {
						if(!AllAccessibleGDs.contains(gd)) {
							AllAccessibleGDs.add(gd);
						}
					}
				}
			}
			gdtos = new ArrayList<GraphDataDTO>(AllAccessibleGDs.size());
			for(GraphData gd : AllAccessibleGDs) {
				GraphDataDTO gdto = new GraphDataDTO();
				gdto.buildByGraphData(gd);
				gdtos.add(gdto);
			}
			return new ResponseEntity<ArrayList<GraphDataDTO>>(gdtos, null, HttpStatus.OK);
		}
		return new ResponseEntity<ArrayList<GraphDataDTO>>(null, null, HttpStatus.OK);
	}
	@GetMapping(value="/api/dataexchange/allpublicgraphnames")
	public ResponseEntity<ArrayList<GraphDataDTO>> getAllPublicGraphNames(){
		ArrayList<GraphDataDTO> gdtos = new ArrayList<>();
		List<GraphData> AllAccessibleGDs = this.gds.findAllPublic();
		for(GraphData gd : AllAccessibleGDs) {
			GraphDataDTO gdto = new GraphDataDTO();
			gdto.buildByGraphData(gd);
			gdtos.add(gdto);
		}
		return new ResponseEntity<ArrayList<GraphDataDTO>>(gdtos, null, HttpStatus.OK);
	}
	@PostMapping(value="/api/dataexchange/allgraphnamebyuser")
	public ResponseEntity<ArrayList<GraphDataDTO>> getAllGraphNameByUser(@RequestBody ParamDTO paramdto){
		if(paramdto != null && paramdto.getUseremail() != null && !"".equals(paramdto.getUseremail())) {
			String useremail = paramdto.getUseremail();
			ArrayList<GraphDataDTO> gdtos = new ArrayList<GraphDataDTO>();
			UserInfo user = this.us.findByEmail(useremail);
			if(user != null) {
				List<GraphData> owngraphdatas = this.gds.getAllByUserId(user.getId());
				if(owngraphdatas != null && owngraphdatas.size() > 0) {
					for(GraphData gd : owngraphdatas) {
						GraphDataDTO gdto = new GraphDataDTO();
						gdto.buildByGraphData(gd);
						gdtos.add(gdto);
					}
				}
				return new ResponseEntity<ArrayList<GraphDataDTO>>(gdtos, null, HttpStatus.OK);
			}
		}
		return new ResponseEntity<ArrayList<GraphDataDTO>>(null, null, HttpStatus.OK);
	}
	
	@PostMapping(value="/api/dataexchange/updategraphdata")
	public ResponseEntity<GraphDataDTO> updateGraphData(@RequestBody ParamGraphDataDTO paramdto){
		GraphDataDTO returndto = new GraphDataDTO();
		UserInfo user = null;
		if(paramdto.getUseremail() != null && !"".equals(paramdto.getUseremail())) {
			user = this.us.findByEmail(paramdto.getUseremail());
		}
		GraphDataDTO graphdatadto = paramdto.getGraphdatadto();
		if(graphdatadto != null && graphdatadto.getName() != null &&
				!"".equals(graphdatadto.getName()) &&
				graphdatadto.getId() != null) {
			String newgraphname = graphdatadto.getName();
			boolean newpublikus = graphdatadto.isPublikus();
			GraphData gd = this.gds.findById(graphdatadto.getId()); 
			returndto.setName(newgraphname);
			returndto.setNodeId(graphdatadto.getNodeId());
			returndto.setId(graphdatadto.getId());
			returndto.setPublikus(newpublikus);
			if(gd != null && gd.getUser().equals(user)) {
				returndto.setNodeId(gd.getEntryNode().getId());
				returndto.setId(gd.getId());
				int options;
				if(newpublikus != gd.isPublikus() && newgraphname.equals(gd.getName())) {
					// only publikus changed
					System.out.println("only publikus changed");
					options = 0;
				} else if(newpublikus == gd.isPublikus() && !newgraphname.equals(gd.getName())) {
					// only name changed
					options = 1;
				} else if (newpublikus != gd.isPublikus() && !newgraphname.equals(gd.getName())) {
					// both name and publikus changed 
					options = 2;
				} else {
					// none changed
					options = 3;
				}
				List<GraphData> publicgds = this.gds.findAllPublic(); 
				List<GraphData> privategds = new ArrayList<GraphData>();
				for(GraphData item : this.gds.getAllByUserId(user.getId())) {
					if(!item.isPublikus()) {
						privategds.add(item);
						//System.out.println(item.getName() + " was added to privategds");
					}
				}
				StringBuilder sbplus = new StringBuilder();
				String modname; 
				switch (options) {
				case 0:
					// only publikus changed
					sbplus.append(gd.getName());
					if(newpublikus) {
						if(this.gds.findByNameAndPublic(gd.getName()).size() > 0) {
							sbplus.append("_");
							sbplus.append(MyHelper.randomString(3));
							while(this.hasName(publicgds, sbplus)) {
								sbplus.setLength(sbplus.length()-3);
								sbplus.append(MyHelper.randomString(3));
							}
						}
					} else {
						if(this.hasName(privategds, sbplus)) {
							sbplus.append("_");
							sbplus.append(MyHelper.randomString(3));
							while(this.hasName(privategds, sbplus)) {
								sbplus.setLength(sbplus.length()-3);
								sbplus.append(MyHelper.randomString(3));
							}
						}
					}
					break;
				case 1:
					// only name changed
					sbplus.append(newgraphname);
					if(gd.isPublikus()) {
						if(this.gds.findByNameAndPublic(newgraphname).size() > 0) {
							sbplus.append("_");
							sbplus.append(MyHelper.randomString(3));
							while(this.hasName(publicgds, sbplus)) {
								sbplus.setLength(sbplus.length()-3);
								sbplus.append(MyHelper.randomString(3));
							}
						}
					} else {
						if(this.hasName(privategds, sbplus)) {
							sbplus.append("_");
							sbplus.append(MyHelper.randomString(3));
							while(this.hasName(privategds, sbplus)) {
								sbplus.setLength(sbplus.length()-3);
								sbplus.append(MyHelper.randomString(3));
							}
						} 
					}
					break;
				case 2:
					// both name and publikus changed 
					sbplus.append(newgraphname);
					if(newpublikus) {
						if(this.gds.findByNameAndPublic(newgraphname).size() > 0) {
							sbplus.append("_");
							sbplus.append(MyHelper.randomString(3));
							while(this.hasName(publicgds, sbplus)) {
								sbplus.setLength(sbplus.length()-3);
								sbplus.append(MyHelper.randomString(3));
							}
						}
					} else {
						if(this.hasName(privategds, sbplus)) {
							sbplus.append("_");
							sbplus.append(MyHelper.randomString(3));
							while(this.hasName(privategds, sbplus)) {
								sbplus.setLength(sbplus.length()-3);
								sbplus.append(MyHelper.randomString(3));
							}
						} 
					}
					break;
				default:
					break;
				}			
				modname = sbplus.toString();
				gd.setName(modname);
				gd.setPublikus(newpublikus);
				this.gds.updateGraphData(gd);
				returndto.setName(modname);
				return new ResponseEntity<GraphDataDTO>(returndto, null, HttpStatus.OK);
			} 
			return new ResponseEntity<GraphDataDTO>(returndto, null, HttpStatus.OK);
		}
		return new ResponseEntity<GraphDataDTO>(null, null, HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping(value="/api/dataexchange/deletegraph")
	public ResponseEntity<PlainTextDTO> deleteGraph(@RequestBody ParamDTO paramdto){
		PlainTextDTO ptdto = new PlainTextDTO();
		if(paramdto != null && paramdto.getUseremail() != null && !"".equals(paramdto.getUseremail()) && 
				paramdto.getEntityid() != null) {
			Integer graphid = paramdto.getEntityid().intValue();
			UserInfo user = this.us.findByEmail(paramdto.getUseremail());
			GraphData gd = this.gds.findById(graphid);
			if(user != null && gd != null && gd.getUser().equals(user)) {
				Node entrynode = gd.getEntryNode();
				//Integer entrynodeid = entrynode.getId();
				try {
					this.gds.deleteGraphDataById(gd.getId());
					this.gs.deleteGraphWithAllComponents(entrynode);
					//this.ns.deleteNodeById(entrynodeid);
					ptdto.setText(gd.getName() + " is deleted");
				} catch(Exception e) {
					ptdto.setText(e.getMessage());
				}
				return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
			} else {
				ptdto.setText("Ezt a tervet nincs jogod törölni");
				return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
			}
		}
		ptdto.setText("Deleting was not attempted.");
		return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
	}
	@PostMapping(value="/api/dataexchange/getclonedgraph")
	public ResponseEntity<GraphDataDTO> getClonedGraph(@RequestBody ParamDTO paramdto){
		if(paramdto != null && paramdto.getUseremail() != null && !"".equals(paramdto.getUseremail()) && 
				paramdto.getEntityid() != null) {
			Integer graphid = paramdto.getEntityid().intValue(); 
			UserInfo user = this.us.findByEmail(paramdto.getUseremail());
			GraphData gd = this.gds.findById(graphid);
			if(user != null && gd != null) {
				GraphData newgd = this.gs.cloneGraph(gd, user.getId());
				if(newgd != null) {
					GraphDataDTO dto = new GraphDataDTO();
					dto.buildByGraphData(newgd);
					return new ResponseEntity<GraphDataDTO>(dto, null, HttpStatus.OK);
				}
				return new ResponseEntity<GraphDataDTO>(null, null, HttpStatus.OK);
			} 
		}
		return new ResponseEntity<GraphDataDTO>(null, null, HttpStatus.NOT_FOUND);
	}
	@PostMapping(value="/api/dataexchange/graphstring")
	public ResponseEntity<PlainTextDTO> getGraphString(@RequestBody ParamDTO paramdto){	
		PlainTextDTO ptdto = new PlainTextDTO();
		if(paramdto != null && paramdto.getEntityid() != null) {
			Integer graphid = paramdto.getEntityid().intValue();
			GraphData gd = this.gds.findById(graphid);
			if(gd != null) {
				ptdto.setText(this.gs.getGraphAsText(gd));
				return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
			}
		}
		ptdto.setText("Required data is missing or invalid.");
		return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
	}
	@PostMapping(value="/api/dataexchange/getorphanandgraphdto")
	public ResponseEntity<FullGraphDTO> getOrphanAndGraphDTO(@RequestBody ParamDTO paramdto){
		if(paramdto != null && paramdto.getUseremail() != null && !"".equals(paramdto.getUseremail()) && 
				paramdto.getEntityid() != null) {
			Integer graphid = paramdto.getEntityid().intValue(); 
			GraphData gd = this.gds.findById(graphid);
			UserInfo user = this.us.findByEmail(paramdto.getUseremail());
			if(user != null && gd != null) {
				Node entry = gd.getEntryNode();
				ArrayList<Edge> edgelist = this.gs.getAllEdgesByEntryNode(entry);
				ArrayList<Node> nodelist = this.gs.getAllNodesByEntryNode(entry);
				
				ArrayList<Picture> piclistG = this.gs.getAllPicturesByEntryNode(entry);
				ArrayList<Description> desclistG = this.gs.getAllDescriptionsByEntryNode(entry);
				ArrayList<InventoryElement> ielistG = this.gs.getAllInventoryElementsByEntryNode(entry);
				List<Picture> orphanpiclist = this.picRepo.findByUserIdAndEdgeIdIsNull(user.getId());
				List<Description> orphandesclist = this.descRepo.findByUserIdAndEdgeIdIsNull(user.getId());
				List<InventoryElement> orphanielist = this.ieRepo.findByUserIdAndEdgeIdIsNull(user.getId());
				
				ArrayList<EdgeDTO> edtos = new ArrayList<>();
				ArrayList<NodeDTO> ndtos = new ArrayList<>();
				ArrayList<PicDataDTO> picdtos = new ArrayList<>();
				ArrayList<DescDTO> descdtos = new ArrayList<>();
				ArrayList<InvElemDTO> iedtos = new ArrayList<>();
				FullGraphDTO fgdto = new FullGraphDTO();
				if(edgelist != null && edgelist.size() > 0) {
					for(Edge item : edgelist) {
						if(item.getToNode() != null && item.getFromNode() != null) {
							EdgeDTO edto = new EdgeDTO();
							edto.buildByEdge(item);
							edtos.add(edto);
						}
					}
					fgdto.setEdgedtos(edtos);
				}
				if(nodelist != null && nodelist.size() > 0) {
					for(Node item : nodelist) {
						if(item != null) {
							NodeDTO ndto = new NodeDTO();
							ndto.buildByNode(item);
							ndtos.add(ndto);
						}
					}
					fgdto.setNodedtos(ndtos);
				}
				if(piclistG != null && piclistG.size() > 0) {
					for(Picture item : piclistG) {
						PicDataDTO pdto = new PicDataDTO();
						pdto.buildByPicture(item);
						picdtos.add(pdto);
					}
				}
				if(ielistG != null && ielistG.size() > 0) {
					for(InventoryElement item : ielistG) {
						InvElemDTO iedto = new InvElemDTO();
						iedto.buildByInvElem(item);
						iedtos.add(iedto);
					}
				}
				if(desclistG != null && desclistG.size() > 0) {
					for(Description item : desclistG) {
						DescDTO ddto = new DescDTO();
						ddto.buildByDesc(item);
						descdtos.add(ddto);
					}
				}
				if(orphandesclist != null && orphandesclist.size() > 0) {
					int k = 0;
					int[] arr = new int[orphandesclist.size()];
					for(Description elem : orphandesclist) {
						arr[k++] = elem.getId().intValue();
						DescDTO ddto = new DescDTO();
						ddto.buildByDesc(elem);
						descdtos.add(ddto);
					}
					fgdto.setOrphandescids(arr);
				}
				if(orphanielist != null && orphanielist.size() > 0) {
					int k = 0;
					int[] arr = new int[orphanielist.size()];
					for(InventoryElement elem : orphanielist) {
						arr[k++] = elem.getId().intValue();
						InvElemDTO iedto = new InvElemDTO();
						iedto.buildByInvElem(elem);
						iedtos.add(iedto);
					}
					fgdto.setOrphaninvelemids(arr);
				}
				if(orphanpiclist != null && orphanpiclist.size() > 0) {
					int k = 0;
					int[] arr = new int[orphanpiclist.size()];
					for(Picture elem : orphanpiclist) {
						arr[k++] = elem.getId().intValue();
						PicDataDTO pdto = new PicDataDTO();
						pdto.buildByPicture(elem);
						picdtos.add(pdto);
					}
					fgdto.setOrphanpicids(arr);
				}
				fgdto.setPicdtos(picdtos);
				fgdto.setInvelemdtos(iedtos);
				fgdto.setDescdtos(descdtos);
				return new ResponseEntity<FullGraphDTO>(fgdto, null, HttpStatus.OK);
			} 
		}
		return new ResponseEntity<FullGraphDTO>(null, null, HttpStatus.NOT_FOUND);
	}
	@PostMapping(value="/api/dataexchange/getorphancompidsdto")
	public ResponseEntity<ComponentIdsDTO> getOrphanCompIdsDTO(@RequestBody ParamDTO paramdto){
		if(paramdto != null && paramdto.getUseremail() != null && !"".equals(paramdto.getUseremail())) {
			UserInfo user = this.us.findByEmail(paramdto.getUseremail());
			if(user != null) {
				List<Picture> piclist = this.picRepo.findByUserId(user.getId());
				List<Description> desclist = this.descRepo.findByUserId(user.getId());
				List<InventoryElement> ielist = this.ieRepo.findByUserId(user.getId());
				ComponentIdsDTO cidto = new ComponentIdsDTO();
				if(piclist != null && piclist.size() > 0) {
					int[] picids = new int[piclist.size()];
					int k = 0;
					for(Picture item : piclist) {
						picids[k++] = item.getId().intValue();
					}
					cidto.setPicids(picids);
				}
				if(ielist != null && ielist.size() > 0) {
					int[] invelemids = new int[ielist.size()];
					int k = 0;
					for(InventoryElement item : ielist) {
						invelemids[k++] = item.getId().intValue();
					}
					cidto.setInvelemids(invelemids);
				}
				if(desclist != null && desclist.size() > 0) {
					int[] descids = new int[desclist.size()];
					int k = 0;
					for(Description item : desclist) {
						descids[k++] = item.getId().intValue();
					}
					cidto.setDescids(descids);
				}
				return new ResponseEntity<ComponentIdsDTO>(cidto, null, HttpStatus.OK);
			} 
		}
		return new ResponseEntity<ComponentIdsDTO>(null, null, HttpStatus.NOT_FOUND);
	}
	@PostMapping(value="/api/dataexchange/ispublicgraph")
	public ResponseEntity<PlainTextDTO> isPublicGraph(@RequestBody ParamDTO paramdto){
		PlainTextDTO ptdto = new PlainTextDTO();
		if(paramdto != null && paramdto.getUseremail() != null && !"".equals(paramdto.getUseremail()) && 
				paramdto.getEntityid() != null) {
			Integer graphid = paramdto.getEntityid().intValue();
			UserInfo user = this.us.findByEmail(paramdto.getUseremail());
			GraphData gd = this.gds.findById(graphid);
			if(user != null && gd != null && gd.getUser().equals(user)) {
				if(gd.isPublikus()) {
					ptdto.setText("true");
				} else {
					ptdto.setText("false");
				}
				return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
			} else {
				ptdto.setText("Adat integritás probléma");
				return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
			}
		}
		ptdto.setText("fetching database was not attempted.");
		return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
	}
	private boolean hasName(List<GraphData> list, StringBuilder name) {
		if(list != null && list.size() > 0 && name != null && name.length() > 0) {
			int len = list.size();
			int j = 0;
			while(j < len && !name.equals(list.get(j).getName())) {
				j++;
			}
			if(j == len) {
				return false;
			} else {
				return true;	
			}
		} else 
			return false;
	}
	/*
	private boolean hasTwoNames(List<GraphData> list, StringBuilder name) {
		if(list != null && list.size() > 0 && name != null && name.length() > 0) {
			int len = list.size();
			int j = 0;
			int found = 0;
			while(j < len && found < 2) {
				if(name.lastIndexOf(list.get(j).getName()) == 0) {
					found++;
				}
				j++;
			}
			if(j == len) {
				return found == 2;
			} else
				return true;
		} else 
			return false;
	}
	*/
}
