package kocsist.factory;

import java.util.ArrayList;
import kocsist.model.Description;
import kocsist.model.Edge;
import kocsist.model.GraphData;
import kocsist.model.UserInfo;
import kocsist.service.interfaces.EdgeService;
import kocsist.service.interfaces.GraphDataService;
import kocsist.service.interfaces.GraphService;
import kocsist.service.interfaces.NodeService;

public class LinearGraphBuilder extends GraphBuilderImpl {
	
	public LinearGraphBuilder(EdgeService es, NodeService ns, GraphDataService gds, GraphService gs, int nodenum, UserInfo u) {
		super(es, ns, gds, gs, nodenum, u);
	}

	public GraphData build(int node_R) {
		if(graphName == null || "".equals(graphName)) {
			graphName = this.getUser().getEmail() + "_" + rand.nextInt(5000) + "plan";
		}
		this.populateNodeList(node_R);
		int j = 0;
		while(j < this.getNodes().size()-1) {
			Edge e = new Edge();
			e.setLabel("e"+(j+1));
			e.setFromNode(this.getNodes().get(j));
			e.setToNode(this.getNodes().get(j+1));
			Description d = new Description();
			d.setText(getEmptydesctext());
			e.setDesc(d);
			this.getEdgeService().addEdge(e);
			this.edges.add(e);
			j++;
		}
		this.populateNodeDTOList();
		this.populateEdgeDTOList();
		GraphData gd = new GraphData();
		gd.setName(graphName);
		gd.setEntryNode(this.getNodes().get(0));
		gd.setPublikus(true);
		gd.setUser(this.getUser());
		this.getGraphDataService().addGraphData(gd);
		return gd;
	}
	@Override
	protected ArrayList<Position> populatePositions(int node_R) {
		if(node_R < 15 || node_R > boundaryWidth / 6 || node_R > boundaryHeight / 4) {
			node_R = 15;
		}
		final int padding = 9;
		final int Ycenter = ((int)(boundaryHeight / 2)) + this.getYOffset();
		final int space = (boundaryWidth - (2*(padding + node_R))) / this.getNodeNum();
		ArrayList<Position> positions = new ArrayList<Position>(this.getNodeNum());
		for (int i = 0; i < this.getNodeNum(); i++) {
			positions.add(new Position((padding + node_R)+(i*space), Ycenter, "n" + i));
		}
		positions.get(0).setLabel("Start");
		positions.get(positions.size()-1).setLabel("Cél");
		return positions;
	}
}