package kocsist.factory;

import kocsist.model.UserInfo;
import kocsist.service.interfaces.EdgeService;
import kocsist.service.interfaces.GraphDataService;
import kocsist.service.interfaces.GraphService;
import kocsist.service.interfaces.NodeService;

public class GraphBuilderFactory {
	private int nodeNum;
	private EdgeService edgeService;
	private NodeService nodeService;
	private GraphDataService graphDataService;
	private GraphService graphService;
	private boolean isServicesSet = false;
	public int getNodeNum() {
		return nodeNum;
	}
	public void setNodeNum(int nodeNum) {
		this.nodeNum = nodeNum;
	}
	public GraphBuilderImpl getGraphBuilder(GraphBuilderType gb, UserInfo u) {
		if(!this.isServicesSet) {
			return null;
		} else {
			if(this.nodeNum < 5 || gb == GraphBuilderType.LINEAR) {
				return new LinearGraphBuilder(this.edgeService, 
						this.nodeService, this.graphDataService, 
						this.graphService, this.nodeNum, u);
			} else {
				return new LargeGraphBuilder(this.edgeService,
						this.nodeService, this.graphDataService, 
						this.graphService, this.nodeNum, u);
			}
		}
	}
	public GraphBuilderFactory(int nodeNum) {
		this.nodeNum = nodeNum < 2 ? 2 : nodeNum;
	}
	public void setServices(EdgeService es, NodeService ns, GraphDataService gds, GraphService gs) {
		this.edgeService = es;
		this.nodeService = ns;
		this.graphDataService = gds;
		this.graphService = gs;
		this.isServicesSet = true;
	}
}