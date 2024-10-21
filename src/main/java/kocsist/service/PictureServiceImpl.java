package kocsist.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import kocsist.blogic.MyHelper;
import kocsist.config.PicHandlingProperties;
import kocsist.model.Picture;
import kocsist.model.UserInfo;
import kocsist.repository.PictureRepo;
import kocsist.service.interfaces.PictureService;

@Component
public class PictureServiceImpl implements PictureService {
	@Autowired
	private PictureRepo pictureRepo;
	@Autowired
	private PicHandlingProperties picHandlingProperties;
	
	@Override
	public Picture findById(Long pictureid) {
		return this.pictureRepo.findById(pictureid).orElse(null);
	}

	@Override
	public Long addPicture(Picture picture) {
		Picture mypicture = pictureRepo.save(picture);
		return mypicture.getId();
	}

	@Override
	public boolean deletePictureById(Long pictureid) {
		boolean ret = false;
		StringBuilder sb = new StringBuilder();
		Picture picture = this.pictureRepo.findById(pictureid).get();
		if(picture.getPathOnServer() != null) {
			sb.append(this.picHandlingProperties.getPicturefolder());
			sb.append(picture.getPathOnServer());
			try {
				Files.deleteIfExists(Paths.get(sb.toString()));
				ret = true;
			} catch (Exception e) {
				ret = false;
			}
			finally {
				this.pictureRepo.deleteById(pictureid);
			}
		}
		return ret;
	}

	@Override
	public void updatePicture(Picture picture) {
		pictureRepo.save(picture);
	}

	@Override
	public List<Picture> getPictureByUserId(Integer userid) {
		return pictureRepo.findByUserId(userid);
	}
	
	@Override
	public Iterable<Picture> getAll() {
		return pictureRepo.findAll();
	}

	@Override
	public List<Picture> getPictureByEdgeId(Long edgeid) {
		return this.pictureRepo.findByEdgeId(edgeid);
	}

	@Override
	public Picture getPictureFromFile(MultipartFile file, UserInfo user) throws IOException {
		StringBuilder sb = new StringBuilder();
		byte[] bytes = file.getBytes();
    	Long picid;
		Picture pic = new Picture();
    	pic.setUser(user);
		pic = this.pictureRepo.save(pic);
		picid = pic.getId();
		sb.append("pic").append(picid.toString());
    	sb.append(".").append(MyHelper.getExtensionFromFilename(file.getOriginalFilename()));
    	Path path = Paths.get(picHandlingProperties.getPicturefolder() + sb.toString());
        if(MyHelper.isDirPathExistOrCreated(picHandlingProperties.getPicturefolder())) {
        	Files.write(path, bytes);
    		pic.setPathOnServer(sb.toString());
    		pic.setSize(bytes.length);
        } 
        this.pictureRepo.save(pic);
		return pic;	
    }

	@Override
	public void updatePictureFile(MultipartFile file, Picture picture) throws IOException {
		StringBuilder sb = new StringBuilder();
		if(picture.getPathOnServer() != null) {
			sb.append(this.picHandlingProperties.getPicturefolder());
			sb.append(picture.getPathOnServer());
			Files.deleteIfExists(Paths.get(sb.toString()));
		}
		
		byte[] bytes = file.getBytes();
    	Long picid = picture.getId();
		sb.setLength(0);
    	sb.append("pic").append(picid.toString());
    	sb.append(".").append(MyHelper.getExtensionFromFilename(file.getOriginalFilename()));
    	Path path = Paths.get(this.picHandlingProperties.getPicturefolder() + sb.toString());
        Files.write(path, bytes);
		picture.setPathOnServer(sb.toString());
        picture.setSize(bytes.length);
		this.pictureRepo.save(picture);
	}

	@Override
	public Picture getPictureByPathOnServer(String pathonserver) {
		return this.pictureRepo.findByPathOnServer(pathonserver).orElse(null);
	}

	@Override
	public List<Picture> getOrphanPicturesByUserId(Integer userid) {
		return this.pictureRepo.findByUserIdAndEdgeIdIsNull(userid);
	}
}
