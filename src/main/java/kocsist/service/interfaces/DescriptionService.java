package kocsist.service.interfaces;

import java.util.List;

import org.springframework.stereotype.Service;

import kocsist.model.Description;
@Service
public interface DescriptionService {
	public Description findById(Long descriptionid);
	public Long addDescription(Description description);
	public void deleteDescriptionById(Long descriptionid);
	public void updateDescription(Description description);
	public List<Description> getDescriptionByUserId(Integer userid);
	public List<Description> getOrphanDescriptionsByUserId(Integer userid);
	public List<Description> getDistinctPublicDescriptions();
	public Iterable<Description> getAll();
}
