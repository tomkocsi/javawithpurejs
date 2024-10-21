package kocsist.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="user")
public class UserInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	private Integer id;
	@Column(name="name", length=32)
	private String name;
	@Column(name="email", length=64, unique = true)
	private String email;
	@Column(name="password", length=60)
	private String password;
	
	
	//bidirectional relationship
	@OneToMany(mappedBy="user", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<GraphData> myGraphDatas = new ArrayList<>();
	
	//bidirectional relationship
	@OneToMany(mappedBy="user", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<InventoryElement> myinventory = new ArrayList<>();
	
	// bidirectional relationship
	@OneToMany(mappedBy="user", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Picture> mypictures = new ArrayList<>();
	
	//bidirectional relationship
	@OneToMany(mappedBy="user", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Description> mydescriptions = new ArrayList<>();
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<GraphData> getMyGraphDatas() {
		return myGraphDatas;
	}
	public void setMyGraphDatas(List<GraphData> myGraphDatas) {
		this.myGraphDatas = myGraphDatas;
	}
	public List<InventoryElement> getMyinventory() {
		return myinventory;
	}
	public void setMyinventory(List<InventoryElement> myinventory) {
		this.myinventory = myinventory;
	}
	public List<Picture> getMypictures() {
		return mypictures;
	}
	public void setMypictures(List<Picture> mypictures) {
		this.mypictures = mypictures;
	}
	public List<Description> getMydescriptions() {
		return mydescriptions;
	}
	public void setMydescriptions(List<Description> mydescriptions) {
		this.mydescriptions = mydescriptions;
	}
	
	public void addPicture(Picture pic) {
		this.mypictures.add(pic);
		pic.setUser(this);
	}
	public void removePicture(Picture pic) {
		this.mypictures.remove(pic);
		pic.setUser(null);
	}
	public void addInventoryElement(InventoryElement inventoryelem) {
		this.myinventory.add(inventoryelem);
		inventoryelem.setUser(this);
	}
	public void removeInventoryElement(InventoryElement inventoryelem) {
		this.myinventory.remove(inventoryelem);
		inventoryelem.setUser(null);
	}
	public void addDescription(Description desc) {
		this.mydescriptions.add(desc);
		desc.setUser(this);
	}
	public void removeDescription(Description desc) {
		this.mydescriptions.remove(desc);
		desc.setUser(null);
	}
	public void addGraphData(GraphData gd) {
		this.myGraphDatas.add(gd);
		gd.setUser(this);
	}
	public void removeGraphData(GraphData gd) {
		this.myGraphDatas.remove(gd);
		gd.setUser(null);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserInfo [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", email=");
		builder.append(email);
		builder.append("]");
		return builder.toString();
	}	
}
