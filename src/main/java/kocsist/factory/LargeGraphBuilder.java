package kocsist.factory;

import java.util.ArrayList;
import java.util.Comparator;

import kocsist.model.Description;
import kocsist.model.Edge;
import kocsist.model.GraphData;
import kocsist.model.Node;
import kocsist.model.UserInfo;
import kocsist.service.interfaces.EdgeService;
import kocsist.service.interfaces.GraphDataService;
import kocsist.service.interfaces.GraphService;
import kocsist.service.interfaces.NodeService;

public class LargeGraphBuilder extends GraphBuilderImpl {

	public LargeGraphBuilder(EdgeService es, NodeService ns, GraphDataService gds, GraphService gs, int nodeNum, UserInfo u) {
		super(es, ns, gds, gs, nodeNum, u);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public GraphData build(int node_R) {
		if(graphName == null || "".equals(graphName)) {
			graphName = this.getUser().getEmail() + "_" + rand.nextInt(5000) + "plan";
		}
		this.populateNodeList(node_R);
		Comparator<Node> mycomp = new Comparator<Node>() {
			@Override
			public int compare(Node n1, Node n2) {
				if(n2.getX() < n1.getX()) {
					return 1;
				} else if (n2.getX() == n1.getX()) {
					return 0;
				} else return -1;
			}
		};
		this.getNodes().sort(mycomp);
		int k = 0;
		while(k < this.getNodes().size() - 1) {
			Node nf = this.getNodes().get(k);
			Node nt = this.getNodes().get(k+1);
			Edge e = new Edge();
			e.setLabel("e" + (k + 1));
			e.setTime1(1);
			e.setTime2(2);
			e.setFromNode(nf);
			e.setToNode(nt);
			Description d = new Description();
			d.setText(getEmptydesctext());
			e.setDesc(d);
			this.getEdgeService().addEdge(e);
			k++;
		}
		
		this.populateNodeDTOList();
		this.populateEdgeDTOList();
		GraphData gd = new GraphData();
		gd.setName(graphName);
		gd.setEntryNode(this.getNodes().get(0));
		gd.setPublikus(false);
		gd.setUser(this.getUser());
		Integer id = this.getGraphDataService().addGraphData(gd);
		gd.setId(id);
		GraphService mygs = this.getGraphService();
		System.out.println(" *********** GraphString ***************");
		System.out.println(mygs.getGraphAsText(gd));
		return gd;
	}
	 
	@Override
	protected ArrayList<Position> populatePositions(int node_R) {
		if(node_R < 15 || node_R > boundaryWidth / 6 || node_R > boundaryHeight / 4) {
			node_R = 15;
		}
		final int padding = 9;
		final int Ycenter = (boundaryHeight / 2) + this.getYOffset();
		ArrayList<Position> mypositions = new ArrayList<Position>(this.getNodeNum());
		mypositions.add(new Position((padding + node_R), Ycenter, "Start"));
		float angle = 2 * (float)Math.PI / (this.getNodeNum()-2);
		float offsetangle = angle / 2;
		for (int i = 0; i < this.getNodeNum()-2; i++) {
			int pX = (boundaryWidth / 2) + (int)((boundaryWidth / 4)*Math.cos(offsetangle + i*angle));
			int pY = Ycenter + (int)((boundaryHeight / 3)*Math.sin(offsetangle + i*angle));
			mypositions.add(new Position(pX, pY, "n" + (i+1)));
		}
		mypositions.add(new Position(boundaryWidth - (padding + node_R), Ycenter, "Cél"));
		System.out.println("  ******************* POSITIONS *******************");
		for(Position p : mypositions) {
			StringBuilder sb = new StringBuilder();
			sb.append(p.getLabel());
			sb.append(" x:");
			sb.append(p.getX());
			sb.append(" y:");
			sb.append(p.getY());
			System.out.println(sb.toString());
		}
		return mypositions;
	}
}