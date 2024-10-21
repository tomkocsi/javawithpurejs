package kocsist.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import kocsist.model.Picture;
@Repository
public interface PictureRepo extends CrudRepository<Picture, Long> {
	public List<Picture> findByUserId(Integer userid);
	public List<Picture> findByEdgeId(Long edgeid);
	public Optional<Picture> findByPathOnServer(String pathonserver);
	public List<Picture> findByUserIdAndEdgeIdIsNull(Integer userid);
}
