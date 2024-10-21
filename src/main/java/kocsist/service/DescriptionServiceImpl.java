package kocsist.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import kocsist.blogic.MyHelper;

import kocsist.model.Description;
import kocsist.model.GraphData;
import kocsist.model.Node;
import kocsist.repository.DescriptionRepo;
import kocsist.repository.GraphDataRepo;
import kocsist.service.interfaces.DescriptionService;
import kocsist.service.interfaces.GraphService;

@Component
public class DescriptionServiceImpl implements DescriptionService {
	@Autowired
	private DescriptionRepo descRepo;
	@Autowired
	private GraphDataRepo gdr;
	@Autowired
	private GraphService gs;
	@Override
	public Description findById(Long descriptionid) {
		return this.descRepo.findById(descriptionid).orElse(null);
	}

	@Override
	public Long addDescription(Description description) {
		Description myDesc = descRepo.save(description);
		return myDesc.getId();
	}

	@Override
	public void deleteDescriptionById(Long descriptionid) {
		Optional<Description> od = this.descRepo.findById(descriptionid);
		if(od.isPresent()) {
			Description d = od.get();
			d.setEdge(null);
			d.setUser(null);
			this.descRepo.delete(d);
		}
	}

	@Override
	public void updateDescription(Description description) {
		descRepo.save(description);
	}

	@Override
	public List<Description> getDescriptionByUserId(Integer userid) {
		return descRepo.findByUserId(userid);
	}

	@Override
	public Iterable<Description> getAll() {
		return descRepo.findAll();
	}

	@Override
	public List<Description> getOrphanDescriptionsByUserId(Integer userid) {
		return this.descRepo.findByUserIdAndEdgeIdIsNull(userid);
	}
	//test this
	@Override
	public List<Description> getDistinctPublicDescriptions() {
		List<GraphData>  publicgraphdatalist = this.gdr.findByPublikus(true);
		Iterator<GraphData> gdit = publicgraphdatalist.iterator();
		if(publicgraphdatalist != null && publicgraphdatalist.size() > 0) {
			ArrayList<Description> publicdesclist = new ArrayList<Description>(); 
			while(gdit.hasNext()) {
				Node entrynode = gdit.next().getEntryNode();
				if(entrynode != null) {
					ArrayList<Description> gdescs = this.gs.getAllDescriptionsByEntryNode(entrynode);
					if(gdescs != null && gdescs.size() > 0) {
						for(Description d : gdescs) {
							if(d.getText() != null && !"".equals(d.getText()) && 
								!d.getText().equals(MyHelper.emptydesctext)){
								int k = 0;
								int len = publicdesclist.size();
								while(k < len && publicdesclist.get(k) != null && 
										!publicdesclist.get(k).getText().equals(d.getText())) {
									k++;
								}
								if(k == len) {
									publicdesclist.add(d);
								}
							}
						}
					}
				}
			}
			return publicdesclist; 
		}
		return null;
	}

}
