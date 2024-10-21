package kocsist.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kocsist.blogic.MyHelper;
import kocsist.config.PicHandlingProperties;
import kocsist.model.Description;
import kocsist.model.Edge;
import kocsist.model.GraphData;
import kocsist.model.InventoryElement;
import kocsist.model.Node;
import kocsist.model.Picture;
import kocsist.model.UserInfo;
import kocsist.repository.DescriptionRepo;
import kocsist.repository.EdgeRepo;
import kocsist.repository.GraphDataRepo;
import kocsist.repository.InventoryRepo;
import kocsist.repository.NodeRepo;
import kocsist.repository.PictureRepo;
import kocsist.repository.UserRepo;
import kocsist.service.interfaces.GraphService;

@Component
public class GraphServiceImpl implements GraphService {
	@Autowired
	private EdgeRepo edgeRepo;
	@Autowired
	private NodeRepo nodeRepo;
	@Autowired
	private DescriptionRepo descRepo;
	@Autowired
	private InventoryRepo inventoryRepo;
	@Autowired
	private PictureRepo pictureRepo;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private GraphDataRepo graphDataRepo;
	@Autowired
	private PicHandlingProperties picHandlingProperties;
	
	@Override
	public ArrayList<Node> getAllNodesByEntryNodeId(Integer nodeid) {
		Node entrynode = nodeRepo.findById(nodeid).orElse(null);
		if(entrynode != null) {
				return getAllNodesByEntryNode(entrynode); 
		}
		return null; 
	}
	@Override
	public ArrayList<Node> getAllNodesByEntryNode(Node entrynode) {
		if(entrynode == null) {
			return null;
		}
		Queue<Node> neighborsToBeExplored = new LinkedList<>();
		ArrayList<Node> explored = new ArrayList<>();
		neighborsToBeExplored.offer(entrynode);
		explored.add(entrynode);
		while(!neighborsToBeExplored.isEmpty()) {
			Node exploring = neighborsToBeExplored.poll();
			List<Node> neighbors = this.getNeighborsOfNode(exploring);
			if(neighbors != null) {
				for(Node n : neighbors) {
					if(!explored.contains(n)) {
						neighborsToBeExplored.offer(n);
						explored.add(n);
					}
				}
			}
		}
		//explored.add(entrynode);
		return explored;
	}

	private ArrayList<Node> getNeighborsOfNode(Node node) {
		ArrayList<Node> neighbors = new ArrayList<>();
		if(node != null) {
			List<Edge> out = node.getOutgoingEdges(); 
			if(out !=null && out.size() > 0) {
				for (Edge e : out) {
					neighbors.add(e.getToNode());
				}
				return neighbors;
			}
			return null;
		}
		return null;
	}
		
	@Override
	public ArrayList<Edge> getAllEdgesByEntryNodeId(Integer nodeid) {
		Node entrynode = nodeRepo.findById(nodeid).orElse(null);
		if(entrynode != null) {
			return getAllEdgesByEntryNode(entrynode);
		}
		return null;
	}
	
	public ArrayList<Edge> getAllEdgesByEntryNode(Node entrynode) {
		ArrayList<Node> nodes = this.getAllNodesByEntryNode(entrynode);
		Set<Edge> edges = new HashSet<>(); 
		if(nodes != null && nodes.size() > 0) {
			for(Node n : nodes) {
				if(n != null) {
					List<Edge> outgoing = n.getOutgoingEdges();
					if(outgoing != null && outgoing.size() > 0) {
						for(Edge out : outgoing ) {
							edges.add(out);
						}
					}
				}
			}
			ArrayList<Edge> edgelist = new ArrayList<>(edges);
			return edgelist;
		}
		return null;
	}
	
	@Override
	@Transactional
	public GraphData cloneGraph(GraphData graphdata, Integer newuserid){
		Node entrynode = graphdata.getEntryNode();
		if(entrynode == null) {
			return null;
		}
		System.out.println("   ********   inside cloneGraph()    *********");
		ArrayList<Node> orignodes = this.getAllNodesByEntryNodeId(entrynode.getId());
		ArrayList<Edge> origedges = this.getAllEdgesByEntryNodeId(entrynode.getId());
		Map<Integer, Node> copiednodesmap = new HashMap<>(orignodes.size());
		Optional<UserInfo> ou = this.userRepo.findById(newuserid);
		if (orignodes != null && orignodes.size() > 0 && ou.isPresent()) {
			UserInfo newUser = ou.get();
			Node entrynodecopy = null;
			 
			for(Node node : orignodes) {
				Node nodecopy = new Node();
				nodecopy.setLabel(node.getLabel());
				nodecopy.setEntry(node.isEntry());
				nodecopy.setFinish(node.isFinish());
				nodecopy.setX(node.getX());
				nodecopy.setY(node.getY());
				copiednodesmap.put(node.getId(), nodecopy);
				if(node.isEntry()) {
					entrynodecopy = nodecopy;
				}
				System.out.println("copied node: " + nodecopy.getLabel());
			}
			this.nodeRepo.saveAll(copiednodesmap.values());
			
			if(origedges != null && origedges.size() > 0) {
				for(Edge edge : origedges) {
					Edge edgecopy = new Edge();
					edgecopy.setLabel(edge.getLabel());
					edgecopy.setTime1(edge.getTime1());
					edgecopy.setTime2(edge.getTime2());
					Node fromNode = copiednodesmap.get(edge.getFromNode().getId());
					Node toNode = copiednodesmap.get(edge.getToNode().getId());
					edgecopy.setFromNode(fromNode);
					edgecopy.setToNode(toNode);
					this.edgeRepo.save(edgecopy);
					Description desc = edge.getDesc();
					if(desc != null) {
						Description desccopy = new Description();
						desccopy.setTag(desc.getTag());
						desccopy.setText(desc.getText());
						desccopy.setUser(newUser);
						edgecopy.setDesc(desccopy);
						this.descRepo.save(desccopy);
					}
					
					List<InventoryElement> inventory = edge.getInventory();
					if(inventory.size() > 0) {
						for(InventoryElement elem : inventory) {
							InventoryElement elemcopy = new InventoryElement();
							elemcopy.setUser(newUser);
							elemcopy.setText(elem.getText());
							edgecopy.addInventoryElement(elemcopy);
							this.inventoryRepo.save(elemcopy);
						}
					}
					
					List<Picture> piclist = edge.getPictures();
					if(piclist.size() > 0) {
						int piclistlen = piclist.size();
						if(piclist != null && piclistlen > 0) {
							String folder = this.picHandlingProperties.getPicturefolder();
							for(Picture pic : piclist) {
								final String ERRORPIC_URL = this.picHandlingProperties.getErrorpicurl();
								Picture picturecopy = new Picture();
								picturecopy = this.pictureRepo.save(picturecopy);
								try {
									String piccopyurl = MyHelper.getCopiedPictureUrl(pic, picturecopy.getId(), folder);
									picturecopy.setPathOnServer(piccopyurl);
									picturecopy.setSize(pic.getSize());
								} catch (Exception e) {
									System.out.println("ERROR while copying picture (id: " + pic.getId() + ") to edge (label: " + pic.getEdge().getLabel() + ")");
									System.out.println("Error text: " + e.getMessage());
									picturecopy.setPathOnServer(ERRORPIC_URL);
									picturecopy.setSize(pic.getSize());
								} finally {
									picturecopy.setUser(newUser);
								}
								edgecopy.addPicture(picturecopy);
								this.pictureRepo.save(picturecopy);
							}
						}
					}
				}
			}
			GraphData graphdatacopy = new GraphData();
			graphdatacopy.setEntryNode(entrynodecopy);
			graphdatacopy.setUser(newUser);
			graphdatacopy.setName(graphdata.getName() + "_clone");
			graphdatacopy.setPublikus(false);
			this.graphDataRepo.save(graphdatacopy);
			return graphdatacopy;
		} else
			return null;
	}

	@Override
	public void updateEdgesAndNodes(Node entrynode) {
		List<Edge> edges = this.getAllEdgesByEntryNode(entrynode);
		this.edgeRepo.saveAll(edges);
		List<Node> nodes = this.getAllNodesByEntryNode(entrynode);
		this.nodeRepo.saveAll(nodes);
	}

	@Override
	public boolean isSame(Edge e1, Edge e2) {
		return e1.getFromNode().equals(e2.getFromNode()) && e1.getToNode().equals(e2.getToNode());
	}

	@Override
	public void addEdgeToNodeWithSave(Edge e, Node fromnode) {
		if(e.getFromNode().equals(fromnode)) {
			List<Edge> eout; 
			if(fromnode.getOutgoingEdges()!=null) {
				eout = fromnode.getOutgoingEdges();
			} else {
				eout = new ArrayList<Edge>();
			}
			eout.add(e);
			fromnode.setOutgoingEdges(eout);
			this.nodeRepo.save(fromnode);
		} else {
			System.out.println("***********************************");
			System.out.println(" * new setfromnode setting        *");
		}
	}
	
	@Override
	@Transactional
	public void deleteEdgesAndNodesByEntryNode(Node entrynode) {
		GraphData gd = this.graphDataRepo.findByEntryNodeId(entrynode.getId());
		if(gd != null) {
			this.graphDataRepo.delete(gd);
		}
		List <Node> nodes = this.getAllNodesByEntryNode(entrynode);
		List <Edge> edges = this.getAllEdgesByEntryNode(entrynode);
		System.out.println("edges size: " + edges.size());
		System.out.println("nodes size: " + nodes.size());
		
		if(edges != null && edges.size() > 0) {
			for(Edge de : edges) {
				try {
					System.out.println("edge " + de.getLabel() + " is going to be deleted");
					this.edgeRepo.delete(de);
					System.out.println("edge " + de.getLabel() + " was deleted");
				} catch(Exception e) {
					System.out.println(e.getMessage());
					System.out.println(de.getLabel() + " could not delete");
				}
			}
		}
		
		if(nodes != null && nodes.size() > 0) {
			for(Node n : nodes) {
				System.out.println(n.getLabel() + " is going to be deleted");
				this.nodeRepo.delete(n);
				System.out.println(n.getLabel() + " was deleted");
			}
		}
	}
	
	@Override
	public ArrayList<Picture> getAllPicturesByEntryNode(Node entrynode) {
		ArrayList<Picture> result = new ArrayList<>();
		if(this.getAllEdgesByEntryNode(entrynode)!=null && this.getAllEdgesByEntryNode(entrynode).size() > 0) {
			for(Edge e : this.getAllEdgesByEntryNode(entrynode)) {
				if(e.getPictures().size() > 0) {
					for(Picture p : e.getPictures()) {
						result.add(p);
					}
				}
			}
		}
		return result;
	}
	
	@Override
	public ArrayList<Description> getAllDescriptionsByEntryNode(Node entrynode) {
		ArrayList<Description> result = new ArrayList<>();
		if(this.getAllEdgesByEntryNode(entrynode) != null && this.getAllEdgesByEntryNode(entrynode).size() > 0) {
			for(Edge e : this.getAllEdgesByEntryNode(entrynode)) {
				if(e.getDesc() != null) {
					result.add(e.getDesc());
				}
			}
		}
		return result;
	}
	
	@Override
	public ArrayList<InventoryElement> getAllInventoryElementsByEntryNode(Node entrynode) {
		ArrayList<InventoryElement> result = new ArrayList<>();
		if(this.getAllEdgesByEntryNode(entrynode)!=null && this.getAllEdgesByEntryNode(entrynode).size() > 0) {
			for(Edge e : this.getAllEdgesByEntryNode(entrynode)) {
				if(e.getInventory().size() > 0) {
					for(InventoryElement ie : e.getInventory()) {
						result.add(ie);
					}
				}
			}
		}
		return result;
	}
	@Override
	public GraphData createGraph(UserInfo user) {
		Node entrynode = new Node();
		entrynode.setEntry(true);
		entrynode.setX(50);
		entrynode.setY(180);
		entrynode.setLabel("Start");
		
		Node finishnode = new Node();
		finishnode.setFinish(true);
		finishnode.setX(300);
		finishnode.setY(180);
		finishnode.setLabel("Exit");
		this.nodeRepo.save(finishnode);
		Edge e = new Edge();
		e.setFromNode(entrynode);
		e.setToNode(finishnode);
		e.setLabel("e1");
		e.setTime1(1);
		e.setTime2(2);
		entrynode.addEdgeToOutgoingEdges(e);
		this.nodeRepo.save(entrynode);
		Description d = new Description();
		d.setUser(user);
		d.setTag("feladat");
		d.setText("Rejtvény leírása / Description of puzzle");
		this.descRepo.save(d);
		e.setDesc(d);
		this.edgeRepo.save(e);
		String pretext = "plan";
		Random myrnd = new Random(); 
		int num = myrnd.nextInt(1000);
		boolean nameexists = this.containsGraphDataNameByUserId(user.getId(), pretext + num);
		while(nameexists) {
			num = myrnd.nextInt(1000);
			nameexists = this.containsGraphDataNameByUserId(user.getId(), pretext + num);
		}
		GraphData gd = new GraphData();
		gd.setEntryNode(entrynode);
		gd.setName(pretext+num);
		gd.setUser(user);
		gd.setPublikus(false);
		this.graphDataRepo.save(gd);
		return gd;
	}
	
	public boolean containsGraphDataNameByUserId(Integer userid, String gdname) {
		List<GraphData> graphdatalist = this.graphDataRepo.findByUserId(userid);
		if(graphdatalist != null && graphdatalist.size() > 0) {
			Iterator<GraphData> myIt = graphdatalist.iterator();
			int k = 0;
			while(myIt.hasNext() && !myIt.next().getName().equals(gdname)){
				++k;
			}
			return k < graphdatalist.size(); 
		}
		return false;
	}
	
	@Override
	public ArrayList<Description> getAllDescriptionsPublic() {
		Function<Node, ArrayList<Description>> getAllDescByEntryNode = entrynode -> {
			return this.getAllDescriptionsByEntryNode(entrynode);
		};
		Function<Description, String> getText = d -> {
			return d.getText();
		};
		return this.getAllXXXPublic(getAllDescByEntryNode, getText);
	}
	
	@Override
	public ArrayList<InventoryElement> getAllInventoryPublic(){
		Function<Node, ArrayList<InventoryElement>> getAllXXXByEntryNode = entrynode -> {
			return this.getAllInventoryElementsByEntryNode(entrynode);
		};
		Function<InventoryElement, String> getText = elem -> {
			return elem.getText();
		};
		return this.getAllXXXPublic(getAllXXXByEntryNode, getText);
	}
	private <T> ArrayList<T> getAllXXXPublic(Function<Node, ArrayList<T>> getAllXXXByEntryNode, 
			Function<T, String> getTxt){
		List<GraphData> publicgds =  this.graphDataRepo.findByPublikus(true);
		if(publicgds != null && publicgds.size() > 0) {
			ArrayList<T> xxxlist = new ArrayList<T>();
			boolean first = true;
			for(GraphData gd : publicgds) {
				Node entrynode = gd.getEntryNode();
				ArrayList<T> graphxxxlist = getAllXXXByEntryNode.apply(entrynode);
				if(graphxxxlist != null && graphxxxlist.size() > 0) {
					if(first) {
						xxxlist.addAll(graphxxxlist);
						first = false;
					}
					for(T elem : graphxxxlist) {
						int k = 0;
						while (k < xxxlist.size() && !getTxt.apply(elem).equals(getTxt.apply(xxxlist.get(k)))) {
							k++;
						}
						if(k < xxxlist.size()) {
							xxxlist.add(elem);
						}
					}
				}
			}
			return xxxlist;
		}
		return null;
	}
	
	@Override
	public ArrayList<Picture> getAllPicturesPublic() {
		List<GraphData> publicgds =  this.graphDataRepo.findByPublikus(true);
		if(publicgds != null && publicgds.size() > 0) {
			ArrayList<Picture> piclist = new ArrayList<Picture>();
			for(GraphData gd : publicgds) {
				Node entrynode = gd.getEntryNode();
				ArrayList<Picture> graphpiclist = this.getAllPicturesByEntryNode(entrynode);
				if(graphpiclist != null && graphpiclist.size() > 0) {
						piclist.addAll(graphpiclist);
				}
			}
			return piclist;
		}
		return null;
	}
	 
	@Override
	public void deleteGraphWithAllComponents(Node entrynode) {
		ArrayList<InventoryElement> ielist = this.getAllInventoryElementsByEntryNode(entrynode);
		ArrayList<Description> desclist = this.getAllDescriptionsByEntryNode(entrynode);
		ArrayList<Picture> piclist = this.getAllPicturesByEntryNode(entrynode);
		this.deleteListFromRepo(ielist, this.inventoryRepo);
		this.deleteListFromRepo(desclist, this.descRepo);
		this.deleteListFromRepo(piclist, this.pictureRepo);
		this.deleteEdgesAndNodesByEntryNode(entrynode);
	}
	
	private <T> void deleteListFromRepo(ArrayList<T> elementList, CrudRepository<T, Long> repo) {
		if(elementList != null && elementList.size() > 0) {
			try {
				repo.deleteAll(elementList);
			} catch(Exception idaaue) {
				//InvalidDataAccessApiUsageException
				System.out.println(idaaue.getMessage());
				System.out.println("Error was throwed in " + this.getClass());
				for(T elem : elementList) {
					if(elem != null) {
						//System.out.println(elem.toString());
						repo.delete(elem);
					}
				}
				System.out.println("Deleted non null entities successfully");
			}
		}  
	}
	
	@Override
	public void removeNodeRefFromEdges(Integer nodeid, Integer graphid) {
		Optional<Node> mynodeo = this.nodeRepo.findById(nodeid);
		Optional<GraphData> gdo = this.graphDataRepo.findById(graphid);
		if(gdo.isPresent() && mynodeo.isPresent()) {
			Node entrynode = gdo.get().getEntryNode();
			Node mynode = mynodeo.get();
			ArrayList<Edge> edges = this.getAllEdgesByEntryNode(entrynode);
			if(edges != null && edges.size() > 0) {
				for(Edge e : edges) {
					if(e != null) {
						if(e.getFromNode().getId().equals(mynode.getId())) {
							e.setFromNode(null);
						} else if(e.getToNode().getId().equals(mynode.getId())) {
							e.setToNode(null);
						}
					}
				}
			}
		}
	}
	
	@Override
	public String getGraphAsText(GraphData graphdata) {
		Node entrynode = graphdata.getEntryNode();
		StringBuilder sb = new StringBuilder();
		sb.append("Terv neve: " + graphdata.getName());
		ArrayList<Edge> edges = this.getAllEdgesByEntryNode(entrynode);
		if(edges != null && edges.size() > 0) {
			for(Edge e : edges) {
				String label = e.getLabel();
				String edgeid = Long.toString(e.getId());
				sb.append("\n" + label + " [" + edgeid + "] feladvány");
				sb.append(" (min: " + e.getTime1() + " perc, ");
				sb.append("max: " + e.getTime2() + " perc): ");
				if(e.getDesc() != null && e.getDesc().getText() != null) {
					sb.append(e.getDesc().getText());
				}
				List<InventoryElement> ielist = e.getInventory();
				if(ielist != null && ielist.size() > 0) {
					sb.append("\nTárgyak: ");	
					for(InventoryElement ie : ielist) {
						sb.append(ie.getText() + " | ");
					}
				}
				if(e.getPictures().size() > 0) {
					sb.append(" + " + e.getPictures().size() + " db kép");
				}
				Node n = e.getToNode();
				if(n != null && n.getOutgoingEdges() != null && 
						n.getOutgoingEdges().size() > 0){
					List<Edge> outgoings = n.getOutgoingEdges();
					int out = outgoings.size();
					sb.append(". " + label + "-től " + out + " feladvány függ: ");
					for(Edge oe : outgoings) {
						sb.append(oe.getLabel());
						if(oe.getDesc() != null && !"".equals(oe.getDesc().getText())){
							int len = oe.getDesc().getText().length() > 23 ? 23 :
								oe.getDesc().getText().length();
							sb.append(" (" + oe.getDesc().getText().substring(0, len) +
									"...) ");
						}
						sb.append(" | ");
					}
				}
			}
		}
		return sb.toString();
	}
	@Transactional
	@Override
	public void detachOrDeleteInvElem(Long invelemid, Long edgeid, UserInfo user) {
		Optional<Edge> oe = this.edgeRepo.findById(edgeid);
		if(oe.isPresent()) {
			Edge e = oe.get();
			Optional<InventoryElement> oitem = this.inventoryRepo.findById(invelemid);
			if(oitem.isPresent()) {
				InventoryElement item = oitem.get();
				String text = item.getText();
				String operation;
				e.removeInventoryElement(item);
				if(text != null && !"".equals(text)) {
					if(item.getUser() == null) {
						item.setUser(user);
						//this.inventoryRepo.save(item);
					}
					operation = "detached from " + e.getLabel();
				} else {
					this.inventoryRepo.delete(item);
					operation = "deleted.";
				}
				this.edgeRepo.save(e);
				Optional<InventoryElement> oie = this.inventoryRepo.findById(item.getId());
				if(oie.isPresent()) {
					InventoryElement ie = oie.get();
					System.out.println("  <-> Inventory element (" + ie.getText() + ") exist, owner: " + ie.getUser().getEmail());
				}
				System.out.println("  <-> Inventory element (text: " + text + " id: " + invelemid + ") was " + operation);
			}
		}
	}
	
	@Override
	public void detachOrDeleteInventory(Long edgeid, UserInfo user) {
		Optional<Edge> oe = this.edgeRepo.findById(edgeid);
		if(oe.isPresent()) {
			Edge e = oe.get();
			if(e.getInventory() != null) {
				InventoryElement item = null;
				int k = e.getInventory().size();
				while(k > 0) {
					item = e.getInventory().get(0);
					if(item.getText() != null && !"".equals(item.getText())) {
						item.setUser(user);
						e.removeInventoryElement(item);
						this.inventoryRepo.save(item);
					} else {
						//item.setUser(null);
						e.removeInventoryElement(item);
						this.inventoryRepo.delete(item);
					}
					k = e.getInventory().size();
				}
				this.edgeRepo.save(e);
			}	
		}
	}
	
	@Override
	public void detachOrDeleteDescription(Long edgeid, UserInfo user) {
		Optional<Edge> oe = this.edgeRepo.findById(edgeid);
		if(oe.isPresent()) {
			Edge e = oe.get();
			Long id = null;
			if (e.getDesc() != null) {
				try{
					Description item = e.getDesc();
					id = item.getId();
					String desctext = item.getText(); 
					String operation; 
					//Description newitem = new Description();
					//newitem.setText(MyHelper.emptydesctext);
					if(desctext != null && !"".equals(desctext) && !desctext.equals(MyHelper.emptydesctext)) {
						item.setEdge(null);
						item.setUser(user);
						e.setDesc(null);
						this.descRepo.save(item);
						operation = "detached.";
					} else {
						item.setEdge(null);
						e.setDesc(null);	
						this.descRepo.delete(item);
						operation = "deleted.";
					}
					this.edgeRepo.save(e);
					System.out.println(" <-> Desc[" + id + "](text: " + desctext + ") was " +
							operation +	" Edge: " + e.getLabel() + "[" + e.getId() + "]");
					Optional<Description> opd = this.descRepo.findById(id);
					if(opd.isPresent()) {
						if(opd.get().getEdge() != null) {
							System.out.println(" <-> Desc (" + opd.get().getId() + ") exists and not orphan");
						} else 
							System.out.println(" <-> Desc (" + opd.get().getId() + ") is now orphan");
					}	
				} catch (Exception exc) {
					System.out.println("Error when detaching or deleting description:\n" + 
							exc.getMessage());
				}
			}
		}
	}
	
	@Override
	public void detachPicture(Long picid, Long edgeid, UserInfo user) {
		Optional<Edge> oe = this.edgeRepo.findById(edgeid);
		if(oe.isPresent()) {
			Edge e = oe.get();
			Optional<Picture> oitem = this.pictureRepo.findById(picid);
			if(oitem.isPresent()) {
				Picture item = oitem.get();
				e.removePicture(item);
				item.setUser(user);
				this.edgeRepo.save(e);
				this.pictureRepo.save(item);
			}
		}
	}
	
	@Override
	public void detachPictureList(Long edgeid, UserInfo user) {
		Optional<Edge> oe = this.edgeRepo.findById(edgeid);
		if(oe.isPresent()) {
			Edge e = oe.get();
			if(e.getPictures() != null) {
				int k = e.getPictures().size();
				Picture item = null;
				while(k > 0) {
					item = e.getPictures().get(0);
					e.removePicture(item);
					item.setUser(user);
					this.pictureRepo.save(item);
					k = e.getPictures().size(); 
				}
				this.edgeRepo.save(e);
			}
		}
	}
	
	@Override
	public void attachInvElem(Long invelemid, Long edgeid) {
		Optional<Edge> oe = this.edgeRepo.findById(edgeid);
		if(oe.isPresent()) {
			Edge e = oe.get();
			Optional<InventoryElement> oitem = this.inventoryRepo.findById(invelemid);
			if(oitem.isPresent()) {
				InventoryElement item = oitem.get();
				List<InventoryElement> elemlist = e.getInventory(); 
				boolean foundsametext = false;
				for(InventoryElement ie : elemlist) {
					if(ie.getText().equals(item.getText())) {
						foundsametext = true;
						break;
					}
				}
				if(!foundsametext) {
					e.addInventoryElement(item);
					this.edgeRepo.save(e);
				}
			}
		}
	}
	
	@Override
	public void attachDescription(Long descid, Long edgeid, UserInfo user) {
		Optional<Edge> oe = this.edgeRepo.findById(edgeid);
		if(oe.isPresent()){
			Edge e = oe.get();
			Optional<Description> oitem = this.descRepo.findById(descid);
			if(oitem.isPresent()) {
				Description olddesc = e.getDesc();
				Description item = oitem.get();
				e.setDesc(item);	
				this.edgeRepo.save(e);
				if(olddesc != null && olddesc.getId() != null && !olddesc.getId().equals(descid)) {
					String oldtext = olddesc.getText(); 
					if(oldtext != null && !"".equals(oldtext) && !oldtext.equals(MyHelper.emptydesctext)) {
						olddesc.setEdge(null);
						olddesc.setUser(user);
						this.descRepo.save(olddesc);
					} else {
						try {
							System.out.println("<-> Attached description (id:" + olddesc.getId() + ") will be deleted because of its text = " + oldtext);
							olddesc.setUser(null);
							this.descRepo.delete(olddesc);	
						}
						catch(Exception ex) {
							System.out.println("Error occured while deleting description");
							System.out.println(ex.getMessage());
						}
					}
				} else {
					System.out.println("<-> Attached Description is null/has no id/already attached -> no detach");
				}
			}
		}
	}
	
	@Override
	public void attachPicture(Long picid, Long edgeid) {
		Optional<Edge> oe = this.edgeRepo.findById(edgeid);
		if(oe.isPresent()) {
			Edge e = oe.get();
			Optional<Picture> oitem = this.pictureRepo.findById(picid);
			if(oitem.isPresent()) {
				Picture item = oitem.get();
				e.addPicture(item);
				this.pictureRepo.save(item);
				this.edgeRepo.save(e);
			}
		}
	}
	
	@SuppressWarnings("unused")
	public Node getEntryNodeOfEdge(Edge e) {
		Iterable<GraphData> gdlist = this.graphDataRepo.findAll();	
		Iterator<GraphData> gdit = gdlist.iterator();
		if(gdlist != null && gdit.hasNext()) {
			for(GraphData gditem : gdlist) {
				ArrayList<Edge> edgelist = this.getAllEdgesByEntryNode(gditem.getEntryNode());
				for(Edge edgeitem : edgelist) {
					if(e.getId().equals(edgeitem.getId())){
						return gditem.getEntryNode();
					}
				}
			}
		}
		return null; 	
	}
	
	@Override
	public void detachAllComponents(Long edgeid, UserInfo user) {
		Optional<Edge> oe = this.edgeRepo.findById(edgeid);
		if(oe.isPresent() && user != null) {
			this.detachOrDeleteInventory(edgeid, user);
			this.detachPictureList(edgeid, user);
			this.detachOrDeleteDescription(edgeid, user);
		}
	}
}

/*
public ArrayList<Node> getAllNodesByEntryNodeId(Integer nodeid) {
	Node entrynode = nodeRepo.findById(nodeid).get();
	Queue<Node> yetToBeExplored = new LinkedList<>();
	ArrayList<Node> explored = new ArrayList<>();
	yetToBeExplored.offer(entrynode);
	while(!yetToBeExplored.isEmpty()) {
		Node exploring = yetToBeExplored.poll();
		if(!explored.contains(exploring)) {
			explored.add(exploring);
			List<Node> neighbors = this.getNeighborsOfNode(exploring);
			if(neighbors != null) {
				for(Node n : neighbors) {
					yetToBeExplored.offer(n);
				}
			}
		}
	}
	return explored;
}
*/