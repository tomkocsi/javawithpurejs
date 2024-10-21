package kocsist.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import kocsist.model.InventoryElement;
import kocsist.repository.InventoryRepo;
import kocsist.service.interfaces.InventoryService;
@Component
public class InventoryServiceImpl implements InventoryService {
	@Autowired
	private InventoryRepo inventoryRepo;
	
	@Override
	public InventoryElement findById(Long inventoryid) {
		return this.inventoryRepo.findById(inventoryid).orElse(null);
	}
	
	@Override
	public Long addInventory(InventoryElement inventoryelement) {
		InventoryElement i = this.inventoryRepo.save(inventoryelement);
		return i.getId();
	}
	
	@Override
	public void deleteInventoryById(Long inventoryid) {
		this.inventoryRepo.deleteById(inventoryid);
	}
	
	@Override
	public void updateInventory(InventoryElement inventoryelement) {
		this.inventoryRepo.save(inventoryelement);
	}
	
	@Override
	public List<InventoryElement> getInventoryByUserId(Integer userid) {
		return inventoryRepo.findByUserId(userid);
	}
	
	@Override
	public Iterable<InventoryElement> getAll() {
		return inventoryRepo.findAll();
	}
	
	@Override
	public List<InventoryElement> getOrphanInventoryByUserId(Integer userid) {
		return this.inventoryRepo.findByUserIdAndEdgeIdIsNull(userid);
	}

}
