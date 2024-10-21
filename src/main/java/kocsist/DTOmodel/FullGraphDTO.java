package kocsist.DTOmodel;

import java.util.ArrayList;

public class FullGraphDTO {
	private ArrayList<EdgeDTO> edgedtos;
	private ArrayList<NodeDTO> nodedtos;
	private ArrayList<PicDataDTO> picdtos;
	private ArrayList<InvElemDTO> invelemdtos;
	private ArrayList<DescDTO> descdtos;
	private int[] orphandescids;
	private int[] orphaninvelemids;
	private int[] orphanpicids;
	public ArrayList<EdgeDTO> getEdgedtos() {
		return edgedtos;
	}
	public void setEdgedtos(ArrayList<EdgeDTO> edgedtos) {
		this.edgedtos = edgedtos;
	}
	public ArrayList<NodeDTO> getNodedtos() {
		return nodedtos;
	}
	public void setNodedtos(ArrayList<NodeDTO> nodedtos) {
		this.nodedtos = nodedtos;
	}
	public ArrayList<PicDataDTO> getPicdtos() {
		return picdtos;
	}
	public void setPicdtos(ArrayList<PicDataDTO> picdtos) {
		this.picdtos = picdtos;
	}
	public ArrayList<InvElemDTO> getInvelemdtos() {
		return invelemdtos;
	}
	public void setInvelemdtos(ArrayList<InvElemDTO> invelemdtos) {
		this.invelemdtos = invelemdtos;
	}
	public ArrayList<DescDTO> getDescdtos() {
		return descdtos;
	}
	public void setDescdtos(ArrayList<DescDTO> descdtos) {
		this.descdtos = descdtos;
	}
	public int[] getOrphandescids() {
		return orphandescids;
	}
	public void setOrphandescids(int[] orphandescids) {
		this.orphandescids = orphandescids;
	}
	public int[] getOrphaninvelemids() {
		return orphaninvelemids;
	}
	public void setOrphaninvelemids(int[] orphaninvelemids) {
		this.orphaninvelemids = orphaninvelemids;
	}
	public int[] getOrphanpicids() {
		return orphanpicids;
	}
	public void setOrphanpicids(int[] orphanpicids) {
		this.orphanpicids = orphanpicids;
	}
}
