package kocsist.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kocsist.DTOmodel.InvElemDTO;
import kocsist.DTOmodel.ParamDTO;
import kocsist.DTOmodel.ParamInvElemDTO;
import kocsist.DTOmodel.PlainTextDTO;
import kocsist.model.Edge;
import kocsist.model.GraphData;
import kocsist.model.InventoryElement;
import kocsist.model.UserInfo;
import kocsist.service.interfaces.EdgeService;
import kocsist.service.interfaces.GraphDataService;
import kocsist.service.interfaces.GraphService;
import kocsist.service.interfaces.InventoryService;
import kocsist.service.interfaces.UserService;
@RestController
public class InventoryController {
	@Autowired
	private GraphService gs;
	@Autowired
	private GraphDataService gds;
	@Autowired
	private UserService us;
	@Autowired
	private InventoryService invs;
	@Autowired
	private EdgeService es;
	
	@PostMapping(value="/api/dataexchange/orphaninventory")
	public ResponseEntity<ArrayList<InvElemDTO>> getOrphanUserInventory(@RequestBody ParamDTO paramdto){
		if(paramdto != null && paramdto.getUseremail() != null && !"".equals(paramdto.getUseremail())) {
			UserInfo user = this.us.findByEmail(paramdto.getUseremail());
			if(user != null) {
				List<InventoryElement> ielist = this.invs.getOrphanInventoryByUserId(user.getId());
				if(ielist != null && ielist.size() > 0) {
					ArrayList<InvElemDTO> iedtolist = new ArrayList<>(ielist.size());
					for(InventoryElement invelem : ielist) {
						InvElemDTO dto = new InvElemDTO();
						dto.buildByInvElem(invelem);
						iedtolist.add(dto);
					}
					return new ResponseEntity<ArrayList<InvElemDTO>>(iedtolist, null, HttpStatus.OK); 
				}
				return new ResponseEntity<ArrayList<InvElemDTO>>(null, null, HttpStatus.OK);
			}
		}
		return new ResponseEntity<ArrayList<InvElemDTO>>(null, null, HttpStatus.NOT_FOUND);
	}
	
	@PostMapping(value="/api/dataexchange/graphandorphaninventory")
	public ResponseEntity<ArrayList<InvElemDTO>> getGraphAndOrphanInventory(@RequestBody ParamDTO paramdto){
		if(paramdto != null && paramdto.getUseremail() != null && !"".equals(paramdto.getUseremail())) {
			UserInfo user = this.us.findByEmail(paramdto.getUseremail());
			if(user != null) {
				ArrayList<InvElemDTO> iedtolist = new ArrayList<>();
				List<InventoryElement> ielist = this.invs.getOrphanInventoryByUserId(user.getId());
				if(ielist != null && ielist.size() > 0) {
					for(InventoryElement invelem : ielist) {
						InvElemDTO dto = new InvElemDTO();
						dto.buildByInvElem(invelem);
						iedtolist.add(dto);
					}
				}
				Long graphid = paramdto.getEntityid();
				if(graphid != null) {
					GraphData gd = this.gds.findById(graphid.intValue());
					if(gd != null && gd.getUser().equals(user)) {
						ArrayList<InventoryElement> graphielist = this.gs.getAllInventoryElementsByEntryNode(gd.getEntryNode());
						if(graphielist != null && graphielist.size() > 0) {
							for(InventoryElement invelem : graphielist) {
								InvElemDTO dto = new InvElemDTO();
								dto.buildByInvElem(invelem);
								iedtolist.add(dto);
							}
						}
					}
				}
				return new ResponseEntity<ArrayList<InvElemDTO>>(iedtolist, null, HttpStatus.OK); 
			}
		}
		return new ResponseEntity<ArrayList<InvElemDTO>>(null, null, HttpStatus.NOT_FOUND);
	}
	
	@PostMapping(value="/api/dataexchange/getinvelemdata")
	public ResponseEntity<InvElemDTO> getInvElemDTO(@RequestBody ParamDTO paramdto){
		if(paramdto != null && paramdto.getEntityid() != null) {
			Long id = paramdto.getEntityid();
			InventoryElement ie = this.invs.findById(id);
			if(ie != null) {
				InvElemDTO iedto = new InvElemDTO();
				iedto.buildByInvElem(ie);
				return new ResponseEntity<InvElemDTO>(iedto, null, HttpStatus.OK); 
			}
			return new ResponseEntity<InvElemDTO>(null, null, HttpStatus.OK);
		}
		return new ResponseEntity<InvElemDTO>(null, null, HttpStatus.NOT_FOUND);
	}
	@PostMapping(value="/api/dataexchange/updateinvelemdata")
	public ResponseEntity<PlainTextDTO> updateInvElemDTO(@RequestBody InvElemDTO iedto){
		if(iedto != null) {
			PlainTextDTO ptdto = new PlainTextDTO();
			InventoryElement ie = this.invs.findById(iedto.getId());
			if(ie != null) {
				try{
					ie.setText(iedto.getText());
					this.invs.updateInventory(ie);
				} catch (Exception err ) {
					ptdto.setText(err.getMessage());
				}
				
				return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
			}
			ptdto.setText("nem találtam az entitást");
			return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
		}
		return new ResponseEntity<PlainTextDTO>(null, null, HttpStatus.NOT_FOUND);
	}
	@PostMapping(value="/api/dataexchange/createinvelemdata")
	public ResponseEntity<PlainTextDTO> createInvElemDTO(@RequestBody ParamInvElemDTO paramdto){
		if(paramdto != null && paramdto.getInvelemdto() != null) {
			PlainTextDTO ptdto = new PlainTextDTO();
			InventoryElement ie = new InventoryElement();
			if(paramdto.getInvelemdto().getText() != null) {
				ie.setText(paramdto.getInvelemdto().getText());
			}
			String useremail = paramdto.getUseremail();
			if(useremail != null && !"".equals(useremail)) {
				UserInfo user = this.us.findByEmail(useremail);
				if(user != null) {
					ie.setUser(user);
				}
			}
			Long newinvelemid = this.invs.addInventory(ie);
			ie.setText(ie.getText() + newinvelemid);
			this.invs.updateInventory(ie);
			Long edgeid = paramdto.getEntityid();
			if(edgeid != null) {
				try {
					Edge e = this.es.findById(edgeid);
					if (e != null) {
						e.addInventoryElement(ie);
						this.es.updateEdge(e);
					} 
				} catch (Exception e) {
					ptdto.setText("-1");
					ptdto.setErrormsg(e.getMessage());
					return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
				}
			}
			if(newinvelemid != null) {
				ptdto.setText(newinvelemid.toString());
				return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
			} else {
				ptdto.setText("-1");
				ptdto.setErrormsg("could not persist new Inventory Element");
				return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
			}
		}
		return new ResponseEntity<PlainTextDTO>(null, null, HttpStatus.NOT_FOUND); 
	}
	@PostMapping(value="/api/dataexchange/removeinvelemdata")
	public ResponseEntity<PlainTextDTO> removeInvElem(@RequestBody ParamDTO paramdto){
		if(paramdto != null && paramdto.getEntityid() != null && paramdto.getEntityid() > -1) {
			PlainTextDTO ptdto = new PlainTextDTO();
			Long id = paramdto.getEntityid();
			try {
				this.invs.deleteInventoryById(id);
				ptdto.setText("OK");
				return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
			} catch (Exception e) {
				ptdto.setText(e.getMessage());
				return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
			}
		}
		return new ResponseEntity<PlainTextDTO>(null, null, HttpStatus.NOT_FOUND); 
	}
}
