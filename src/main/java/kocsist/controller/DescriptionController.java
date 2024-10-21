package kocsist.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kocsist.DTOmodel.DescDTO;
import kocsist.DTOmodel.ParamDTO;
import kocsist.DTOmodel.ParamDescDTO;
import kocsist.DTOmodel.PlainTextDTO;
import kocsist.model.Description;
import kocsist.model.Edge;
import kocsist.model.GraphData;
import kocsist.model.UserInfo;
import kocsist.repository.EdgeRepo;
import kocsist.service.interfaces.DescriptionService;
import kocsist.service.interfaces.GraphDataService;
import kocsist.service.interfaces.GraphService;
import kocsist.service.interfaces.UserService;
@RestController
public class DescriptionController {
	@Autowired
	private GraphService gs;
	@Autowired
	private GraphDataService gds;
	@Autowired
	private UserService us;
	@Autowired
	private EdgeRepo er;
	@Autowired
	private DescriptionService ds;
	
	@PostMapping(value="/api/dataexchange/orphandescs")
	public ResponseEntity<ArrayList<DescDTO>> getOrphanUserDescs(@RequestBody ParamDTO paramdto){
		if(paramdto != null && paramdto.getUseremail() != null && !"".equals(paramdto.getUseremail())) {
			UserInfo user = this.us.findByEmail(paramdto.getUseremail());
			if(user != null) {
				List<Description> desclist = this.ds.getOrphanDescriptionsByUserId(user.getId());
				if(desclist != null && desclist.size() > 0) {
					ArrayList<DescDTO> descdtolist = new ArrayList<>(desclist.size());
					for(Description d : desclist) {
						DescDTO dto = new DescDTO();
						dto.buildByDesc(d);
						descdtolist.add(dto);
					}
					return new ResponseEntity<ArrayList<DescDTO>>(descdtolist, null, HttpStatus.OK); 
				}
				return new ResponseEntity<ArrayList<DescDTO>>(null, null, HttpStatus.OK);
			}
			return new ResponseEntity<ArrayList<DescDTO>>(null, null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ArrayList<DescDTO>>(null, null, HttpStatus.NOT_FOUND);
	}
	@PostMapping(value="/api/dataexchange/graphdescs")
	public ResponseEntity<ArrayList<DescDTO>> getGraphDescs(@RequestBody ParamDTO paramdto){
		if(paramdto != null && paramdto.getUseremail() != null && !"".equals(paramdto.getUseremail())) {
			Integer graphid = paramdto.getEntityid().intValue();
			String useremail = paramdto.getUseremail();
			UserInfo user = this.us.findByEmail(useremail);
			if(user != null) {
				ArrayList<DescDTO> descdtolist = new ArrayList<>();
				if(graphid != null) {
					GraphData gd = this.gds.findById(graphid);
					if(gd != null && gd.getUser().equals(user)) {
						ArrayList<Description> graphdesclist = this.gs.getAllDescriptionsByEntryNode(gd.getEntryNode());
						if(graphdesclist != null && graphdesclist.size() > 0) {
							for(Description d : graphdesclist) {
								DescDTO dto = new DescDTO();
								dto.buildByDesc(d);
								descdtolist.add(dto);
							}
						}
					}
				}
				return new ResponseEntity<ArrayList<DescDTO>>(descdtolist, null, HttpStatus.OK); 
			}
			return new ResponseEntity<ArrayList<DescDTO>>(null, null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ArrayList<DescDTO>>(null, null, HttpStatus.NOT_FOUND);
	}
	@PostMapping(value="/api/dataexchange/descs")
	public ResponseEntity<ArrayList<DescDTO>> getGraphAndOrphanDescs(@RequestBody ParamDTO paramdto){
		if(paramdto != null && paramdto.getUseremail() != null && !"".equals(paramdto.getUseremail())) {
			Integer graphid = paramdto.getEntityid().intValue();
			String useremail = paramdto.getUseremail();
			UserInfo user = this.us.findByEmail(useremail);
			if(user != null) {
				ArrayList<DescDTO> descdtolist = new ArrayList<>();
				List<Description> desclist = this.ds.getOrphanDescriptionsByUserId(user.getId());
				if(desclist != null && desclist.size() > 0) {
					for(Description d : desclist) {
						DescDTO dto = new DescDTO();
						dto.buildByDesc(d);
						descdtolist.add(dto);
					}
				}
				if(graphid != null) {
					GraphData gd = this.gds.findById(graphid);
					if(gd != null && gd.getUser().equals(user)) {
						ArrayList<Description> graphdesclist = this.gs.getAllDescriptionsByEntryNode(gd.getEntryNode());
						if(graphdesclist != null && graphdesclist.size() > 0) {
							for(Description d : graphdesclist) {
								DescDTO dto = new DescDTO();
								dto.buildByDesc(d);
								descdtolist.add(dto);
							}
						}
					}
				}
				return new ResponseEntity<ArrayList<DescDTO>>(descdtolist, null, HttpStatus.OK); 
			}
			return new ResponseEntity<ArrayList<DescDTO>>(null, null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<ArrayList<DescDTO>>(null, null, HttpStatus.NOT_FOUND);
	}
	@PostMapping(value="/api/dataexchange/publicdescs")
	public ResponseEntity<ArrayList<DescDTO>> getDistinctPublicDescs(){
		ArrayList<DescDTO> descdtolist = new ArrayList<>();
		List<Description> desclist = this.ds.getDistinctPublicDescriptions();
		if(desclist != null && desclist.size() > 0) {
			for(Description d : desclist) {
				DescDTO dto = new DescDTO();
				dto.buildByDesc(d);
				descdtolist.add(dto);
			}
			return new ResponseEntity<ArrayList<DescDTO>>(descdtolist, null, HttpStatus.OK); 
		}
		return new ResponseEntity<ArrayList<DescDTO>>(null, null, HttpStatus.NOT_FOUND);
	}
	@PostMapping(value="/api/dataexchange/getdescdata")
	public ResponseEntity<DescDTO> getDescDTO(@RequestBody ParamDTO paramdto){
		Long descid = paramdto.getEntityid();
		if(descid != null) {
			Description d = this.ds.findById(descid);
			if(d != null) {
				DescDTO descdto = new DescDTO();
				descdto.buildByDesc(d);
				return new ResponseEntity<DescDTO>(descdto, null, HttpStatus.OK); 
			}
			return new ResponseEntity<DescDTO>(null, null, HttpStatus.OK);
		}
		return new ResponseEntity<DescDTO>(null, null, HttpStatus.NOT_FOUND);
		
	}
	@PostMapping(value="/api/dataexchange/updatedescdata")
	public ResponseEntity<PlainTextDTO> updateDescDTO(@RequestBody DescDTO descdto){
		if(descdto != null) {
			PlainTextDTO ptdto = new PlainTextDTO();
			Description d = this.ds.findById(descdto.getId());
			if(d != null) {
				d.setText(descdto.getText());
				d.setTag(descdto.getTag());
				this.ds.updateDescription(d);
				ptdto.setText("OK");
				return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
			}
			ptdto.setText("nem találtam az entitást");
			return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
		}
		return new ResponseEntity<PlainTextDTO>(null, null, HttpStatus.NOT_FOUND);
	}
	@PostMapping(value="/api/dataexchange/createdescdata")
	public ResponseEntity<DescDTO> createDescDTO(@RequestBody ParamDescDTO paramdescdto){
		if(paramdescdto != null && paramdescdto.getDescdto() != null) {
			Long edgeid = paramdescdto.getEntityid();
			DescDTO descdto = paramdescdto.getDescdto();
			String useremail = paramdescdto.getUseremail();
			Description d = new Description();
			if(descdto.getText() != null) {
				d.setText(descdto.getText());
			}
			if(descdto.getTag() != null) {
				d.setTag(descdto.getTag());
			}
			if(useremail != null && !"".equals(useremail)) {
				UserInfo user = this.us.findByEmail(useremail);
				if(user != null) {
					d.setUser(user);
				}
			}
			Edge e;
			if(edgeid != null) {
				Optional<Edge> oe = this.er.findById(edgeid);
				if(oe.isPresent()) {
					e = oe.get();
					e.setDesc(d);
					this.er.save(e);
				}
			}
			Long newdescid = this.ds.addDescription(d);
			d.setId(newdescid);
			DescDTO ddto = new DescDTO();
			ddto.buildByDesc(d);
			return new ResponseEntity<DescDTO>(ddto, null, HttpStatus.OK);
		}
		return new ResponseEntity<DescDTO>(null, null, HttpStatus.NOT_FOUND); 
	}
	@PostMapping(value="/api/dataexchange/removedescdata")
	public ResponseEntity<PlainTextDTO> removeDesc(@RequestBody ParamDTO paramdto){
		Long descid = paramdto.getEntityid();
		if(descid != null && descid > -1) {
			PlainTextDTO ptdto = new PlainTextDTO();
			try {
				this.ds.deleteDescriptionById(descid);
				return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
			} catch (Exception e) {
				ptdto.setText(e.getMessage());
				return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
			}
		}
		return new ResponseEntity<PlainTextDTO>(null, null, HttpStatus.NOT_FOUND); 
	}
}
