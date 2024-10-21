package kocsist.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kocsist.DTOmodel.ParamDTO;
import kocsist.DTOmodel.PicDataDTO;
import kocsist.DTOmodel.PlainTextDTO;
import kocsist.blogic.MyHelper;
import kocsist.config.PicHandlingProperties;
import kocsist.model.Edge;
import kocsist.model.GraphData;
import kocsist.model.Picture;
import kocsist.model.UserInfo;
import kocsist.service.interfaces.EdgeService;
import kocsist.service.interfaces.GraphDataService;
import kocsist.service.interfaces.GraphService;
import kocsist.service.interfaces.PictureService;
import kocsist.service.interfaces.UserService;

@RestController
public class PictureController {
	@Autowired
	private PicHandlingProperties picHandlingProperties;
	@Autowired
	private PictureService pictureService;
	@Autowired
	private UserService userService;
	@Autowired
	private EdgeService edgeService;
	@Autowired
	private GraphDataService gds;
	@Autowired
	private GraphService gs;
		
	// source:  https://mkyong.com/spring-boot/spring-boot-file-upload-example-ajax-and-rest/
	// downloaded: 2021.04.18.
	@PostMapping(value="/api/uploadpics")
	public ResponseEntity<PicDataDTO> uploadFileMulti(@RequestParam(name = "modaledgeid", required = false) String edgeid, 
			@RequestParam("modaluseremail") String useremail,
            @RequestParam("file") MultipartFile uploadfile) {
		// file name builds up like this: folder + 'pic' + picid + '.ext'
		PicDataDTO picdto = new PicDataDTO();
		if(uploadfile != null && useremail != null && !"".equals(useremail)) {
			UserInfo user = this.userService.findByEmail(useremail);
			if(user != null) {
				Picture savedpic;
				picdto.setUserEmail(user.getEmail());
				if(edgeid != null && !"".equals(edgeid)) {
					picdto.setEdgeId(Long.parseLong(edgeid));
				}
				try {
					savedpic = this.pictureService.getPictureFromFile(uploadfile, user);
					if(savedpic != null) {
						if(picdto.getEdgeId() != null) {
							Edge e = this.edgeService.findById(picdto.getEdgeId());
							if(e != null) {
								e.addPicture(savedpic);
								edgeService.updateEdge(e);
							}
						}
						picdto.setId(savedpic.getId());
						picdto.setImagesrc(savedpic.getPathOnServer());
					}
				} catch (IOException ioexc) {
					picdto.setMessage(ioexc.getMessage());
					return new ResponseEntity<PicDataDTO>(picdto, HttpStatus.OK);
				} catch (Exception exc) {
					picdto.setMessage(exc.getMessage());
					return new ResponseEntity<PicDataDTO>(picdto, HttpStatus.OK);
				}
				picdto.setMessage("OK");
				return new ResponseEntity<PicDataDTO>(picdto, HttpStatus.OK);
			}
			else {
				System.out.println("user nem létezik");
				picdto.setMessage("Nincs ilyen felasználó");
				return new ResponseEntity<PicDataDTO>(picdto, HttpStatus.NOT_FOUND);
			}
		} else {
			System.out.println("fájl feltoltes gond");
			picdto.setMessage("Nem sikerült fájlt feltölteni");
			return new ResponseEntity<PicDataDTO>(picdto, HttpStatus.NOT_FOUND);
		}
	}
	// source:
	// https://medium.com/@ahmedgrati1999/an-easier-way-to-upload-retrieve-images-with-spring-boot-2-0-angular-8-400d1a51dccb
	@GetMapping(value = "/api/downloadpic/{imageName:.+}", 	produces = {
							MediaType.IMAGE_JPEG_VALUE,
							MediaType.IMAGE_GIF_VALUE,
							MediaType.IMAGE_PNG_VALUE})
	public ResponseEntity<byte[]> getImageFile(@PathVariable(name = "imageName") String filename) {
		HttpHeaders headers = new HttpHeaders();
		if(filename != null && !"".equals(filename)) {
			Picture pic = this.pictureService.getPictureByPathOnServer(filename);
			if(pic != null && pic.getPathOnServer() != null && !"".equals(pic.getPathOnServer())) {
				StringBuilder sb = new StringBuilder();
				sb.append(this.picHandlingProperties.getPicturefolder());
				sb.append(pic.getPathOnServer());
				byte[] media;
				try {
					media = MyHelper.getFileAsBytes(sb.toString());
					headers.setCacheControl(CacheControl.noCache().getHeaderValue());
				} catch (Exception e) {
					return new ResponseEntity<>(new byte[]{'e', 'r', 'r', 'o', 'r'}, headers, HttpStatus.NOT_FOUND);
				}
				return new ResponseEntity<>(media, headers, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(new byte[]{'e', 'r', 'r', 'o', 'r'}, headers, HttpStatus.NOT_FOUND);
	}
	@PostMapping("/api/upload") 
	public ResponseEntity<?> handleFileUpload( @RequestParam("file") MultipartFile file ) {
	    String fileName = file.getOriginalFilename();
	    try {
	      file.transferTo(new File(this.picHandlingProperties.getPicturefolder() + fileName));
	    } catch (Exception e) {
	      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    } 
	    return ResponseEntity.ok("File uploaded successfully.");
	}
	@PostMapping("/api/dataexchange/graphandorphanpics")
	public ResponseEntity<ArrayList<PicDataDTO>> getGraphAndOrphanPicDataDTOs(@RequestBody ParamDTO paramdto){
		if(paramdto != null && paramdto.getUseremail() != null && !"".equals(paramdto.getUseremail())) {
			UserInfo user = this.userService.findByEmail(paramdto.getUseremail());
			if(user != null) {
				ArrayList<PicDataDTO> dtolist = new ArrayList<>();
				List<Picture> piclist = this.pictureService.getOrphanPicturesByUserId(user.getId());
				if(piclist != null && piclist.size() > 0) {
					for(Picture elem : piclist) {
						PicDataDTO dto = new PicDataDTO();
						dto.buildByPicture(elem);
						dtolist.add(dto);
					}
				}
				Integer graphid = paramdto.getEntityid().intValue(); 
				if(graphid != null) {
					GraphData gd = this.gds.findById(graphid);
					if(gd != null && gd.getUser().equals(user)) {
						ArrayList<Picture> graphpiclist = this.gs.getAllPicturesByEntryNode(gd.getEntryNode());
						if(graphpiclist != null && graphpiclist.size() > 0) {
							for(Picture elem : graphpiclist) {
								PicDataDTO dto = new PicDataDTO();
								dto.buildByPicture(elem);
								dtolist.add(dto);
							}
						}
					}
				}
				return new ResponseEntity<ArrayList<PicDataDTO>>(dtolist, null, HttpStatus.OK); 
			}
		}
		return new ResponseEntity<ArrayList<PicDataDTO>>(null, null, HttpStatus.NOT_FOUND);
	}
	@PostMapping(value="/api/dataexchange/removepicture")
	public ResponseEntity<PlainTextDTO> removePicture(@RequestBody PicDataDTO dto){
		if(dto != null){
			PlainTextDTO ptdto = new PlainTextDTO();
			Picture p = this.pictureService.findById(dto.getId());
			if(p != null) {
				try {
					this.pictureService.deletePictureById(p.getId());
					return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
				} catch (Exception ex) {
					ptdto.setText(ex.getMessage());
					return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
				}
			}
			ptdto.setText("No picture with id: " + dto.getId().toString());
			return new ResponseEntity<PlainTextDTO>(ptdto, null, HttpStatus.OK);
		}
		return new ResponseEntity<PlainTextDTO>(null, null, HttpStatus.NOT_FOUND); 
	}
}
