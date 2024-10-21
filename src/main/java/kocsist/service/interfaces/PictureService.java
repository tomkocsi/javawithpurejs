package kocsist.service.interfaces;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kocsist.model.Picture;
import kocsist.model.UserInfo;
@Service
public interface PictureService {
	public Picture findById(Long pictureid);
	public Picture getPictureByPathOnServer(String pathonserver);
	public Long addPicture(Picture picture);
	public boolean deletePictureById(Long pictureid);
	public void updatePicture(Picture picture);
	public List<Picture> getPictureByUserId(Integer userid);
	public List<Picture> getPictureByEdgeId(Long edgeid);
	public List<Picture> getOrphanPicturesByUserId(Integer userid);
	public Iterable<Picture> getAll();
	public Picture getPictureFromFile(MultipartFile file, UserInfo user) throws IOException;
	public void updatePictureFile(MultipartFile file, Picture picture) throws IOException;
}
