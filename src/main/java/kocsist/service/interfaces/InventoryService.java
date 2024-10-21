package kocsist.service.interfaces;

import java.util.List;

import org.springframework.stereotype.Service;

import kocsist.model.InventoryElement;
@Service
public interface InventoryService {
	public InventoryElement findById(Long inventoryid);
	public Long addInventory(InventoryElement inventoryelement);
	public void deleteInventoryById(Long inventoryid);
	public void updateInventory(InventoryElement inventoryelement);
	public List<InventoryElement> getInventoryByUserId(Integer userid);
	public List<InventoryElement> getOrphanInventoryByUserId(Integer userid);
	public Iterable<InventoryElement> getAll();
}
