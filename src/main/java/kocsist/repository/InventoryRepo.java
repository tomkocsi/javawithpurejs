package kocsist.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import kocsist.model.InventoryElement;
@Repository
public interface InventoryRepo extends CrudRepository<InventoryElement, Long> {
	// custom methods
	public List<InventoryElement> findByUserId(Integer userid);
	public List<InventoryElement> findByUserIdAndEdgeIdIsNull(Integer userid);
}
