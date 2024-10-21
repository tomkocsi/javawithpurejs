package kocsist.service;



import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kocsist.blogic.MyHelper;
import kocsist.model.Description;
import kocsist.model.Edge;
import kocsist.model.InventoryElement;
import kocsist.model.Node;
import kocsist.model.Picture;
import kocsist.model.UserInfo;
import kocsist.repository.DescriptionRepo;
import kocsist.repository.EdgeRepo;
import kocsist.repository.InventoryRepo;
import kocsist.repository.PictureRepo;
import kocsist.service.interfaces.EdgeService;

@Component
public class EdgeServiceImpl implements EdgeService {
	@Autowired
	private EdgeRepo edgeRepo;
	@Autowired
	private InventoryRepo invRepo;
	@Autowired
	private PictureRepo picRepo;
	@Autowired
	private DescriptionRepo descRepo;
	
	@Override
	public Edge findById(Long edgeid) {
		Optional<Edge> oe = this.edgeRepo.findById(edgeid);
		if(oe.isPresent()) {
			return oe.get();	
		}
		return null;
	}
	@Transactional
	@Override
	public Long addEdge(Edge edge) {
		Edge myEdge = edgeRepo.save(edge);
		return myEdge.getId();
	}
	@Override
	public void deleteEdgeById(Long edgeid) {
		this.deleteEdgeById(edgeid, false);
	}
	
	@Transactional
	@Override
	public void deleteEdgeById(Long edgeid, boolean persistcomponents) {
		Optional<Edge> oe = this.edgeRepo.findById(edgeid);
		if(oe.isPresent()) {
			Edge e = oe.get();
			if(e != null) {
				Node fromnode = e.getFromNode();
				if(fromnode != null) {
					fromnode.removeEdgeFromOutgoingEdges(e);
				}
				if(persistcomponents) {
					if(e.getInventory().size() > 0) {
						List<InventoryElement> invlist = e.getInventory();
						for(InventoryElement item : invlist) {
							if(item.getText() != null && !"".equals(item.getText())) {
								item.setEdge(null);
								if(item.getUser() == null) {
									item.setUser(null);
								}
								this.invRepo.save(item);
							}
						}
						e.removeAllInventoryElement(invlist);
					}
					if(e.getPictures().size() > 0) {
						List<Picture> piclist = e.getPictures();
						for(Picture item : piclist) {
							item.setEdge(null);
							this.picRepo.save(item);
						}
						e.removeAllPictures(piclist);
					}
					if(e.getDesc() != null) {
						Description item = e.getDesc(); 
						String text = item.getText();
						if(text != null && !"".equals(text) && !text.equals(MyHelper.emptydesctext)) {
							item.setEdge(null);
							e.setDesc(null);
							this.descRepo.save(item);
						}
					}
				}
				this.edgeRepo.deleteById(edgeid);
			}
		}
	}
	@Transactional
	@Override
	public void deleteEdgeById(Long edgeid, boolean persistcomponents, UserInfo user) {
		Optional<Edge> oe = this.edgeRepo.findById(edgeid);
		if(oe.isPresent()) {
			Edge e = oe.get();
			if(e != null) {
				Node fromnode = e.getFromNode();
				if(fromnode != null) {
					fromnode.removeEdgeFromOutgoingEdges(e);
				}
				if(persistcomponents && user != null) {
					if(e.getInventory().size() > 0) {
						List<InventoryElement> invlist = e.getInventory();
						for(InventoryElement item : invlist) {
							if(item.getText() != null && !"".equals(item.getText())) {
								item.setEdge(null);
								if(item.getUser() == null) {
									item.setUser(null);
								}
								this.invRepo.save(item);
							}
						}
						e.removeAllInventoryElement(invlist);
					}
					if(e.getPictures().size() > 0) {
						List<Picture> piclist = e.getPictures();
						for(Picture item : piclist) {
							item.setEdge(null);
							this.picRepo.save(item);
						}
						e.removeAllPictures(piclist);
					}
					if(e.getDesc() != null) {
						Description item = e.getDesc(); 
						String text = item.getText();
						if(text != null && !"".equals(text) && !text.equals(MyHelper.emptydesctext)) {
							item.setEdge(null);
							if(item.getUser() == null && user != null) {
								item.setUser(user);
							}
							e.setDesc(null);
							this.descRepo.save(item);
						}
					}
				}
				this.edgeRepo.deleteById(edgeid);
			}
		}
	}
	@Transactional
	@Override
	public void updateEdge(Edge edge) {
		edgeRepo.save(edge);
	}

	@Override
	public Iterable<Edge> getAll() {
		return edgeRepo.findAll();
	}

}
