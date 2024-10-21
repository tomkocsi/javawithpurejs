function createNodesFromNodeDTOs(nodedtos){
    const nodelist = [];
    if(nodedtos != undefined && nodedtos.length > 0){
        nodedtos.forEach(nodedto => {
            let mynode = new Vertex(nodedto.id, nodedto.X, nodedto.Y);
            mynode.setLabel(nodedto.label);
            if (nodedto.isEntry)
                mynode.setEntry();
            if(nodedto.isFinish)
                mynode.setFinish();
            nodelist.push(mynode);
        });
    }
    return nodelist;
}

function findNodeById(nodeid, nodes){
    const k = nodes.length;
    let j = 0;
    while(j < k && nodes[j].id != nodeid){
        j++;
    }
    if(j < k)
        return nodes[j];
    else
        return undefined;
}

function findOutgoingsToNodeDTO(nodeid, edgeDTOs, nodes){
    const edgelist = [];
    if((edgeDTOs != undefined && edgeDTOs.length > 0) && 
       (nodes != undefied && nodes.length > 0)){
        edgeDTOs.forEach(edgedto => {
            if(edgedto.fromNodeId == nodeid){
                const myEdge = new Edge(
                                    edgedto.id,
                                    findNodeById(edgedto.fromNodeId, nodes),
                                    findNodeById(edgedto.toNodeId, nodes),
                                    edgedto.label
                                   );
                edgelist.push(myEdge);
            }
        });
    }
    return edgelist;
}

function findIncomingsToNodeDTO(nodeid, edgeDTOs, nodes){
    const edgelist = [];
    if((edgeDTOs != undefined && edgeDTOs.length > 0) && 
       (nodes != undefied && nodes.length > 0)){
        edgeDTOs.forEach(edgedto => {
            if(edgedto.toNodeId == nodeid){
                const myEdge = new Edge(
                                    edgedto.id,
                                    findNodeById(edgedto.fromNodeId, nodes),
                                    findNodeById(edgedto.toNodeId, nodes),
                                    edgedto.label
                                   );
                edgelist.push(myEdge);
            }
        });
    }
    return edgelist;
}

class NodeDTO{
    constructor(id, x, y, label, isfinish, isentry){
        this.X = x;
        this.Y = y;
        this.id = id;
        this.isFinish = isfinish;
        this.isEntry = isentry;
        this.label = label;
    }
	printMe(){
		const txt = "Id:" + this.id + " Label:" + this.label;
		return txt;
	}
}

class EdgeDTO{
    constructor(id, fromnodeid, tonodeid, label){
        this.id = id;
        this.fromNodeId = fromnodeid;
        this.toNodeId = tonodeid;
        this.label = label;
    }
    getFromNodeId(){
        return this.fromNodeId;
    }
    getToNodeId(){
        return this.toNodeId;
    }
    setFromNode(fromnodeid){
        this.fromNodeId = fromnodeid;
    }
    setToNode(tonodeid){
        this.toNodeId = tonodeid;
    }
    getLabel(label){
        return this.label;
    }   
}

class GraphDTO {
    constructor(name){
        this.name = name;
        this.nodeDTOs = [];
        this.edgeDTOs = [];
        this.Graph = new Graph(name);
    }
    setNodeDTOs(nodeDTOs){
        this.nodeDTOs = nodeDTOs;
    }
    setEdgeDTOs(edgeDTOs){
        this.setEdgeDTOs = edgeDTOs;
    }
    constructGraph(){
        if(this.nodeDTOs.length > 0){
            let nodes = createNodesFromNodeDTOs(this.nodeDTOs);
            nodes.forEach(mynode => {
                mynode.setOutgoings(findOutgoingsToNodeDTO(mynode.id, this.edgeDTOs, nodes));
                mynode.setIncomings(findIncomingsToNodeDTO(mynode.id, this.edgeDTOs, nodes));
                this.nodes.push(mynode);
                this.Graph.addNode(mynode);
            });
        }
        return this.Graph;
    }
    addNodeDTO(nodeDTO){
        if(nodeDTO != undefined){
            this.nodeDTOs = addElementToList(nodeDTO, this.nodeDTOs);
        }
    }
    printMe(){
        console.log(this.name + ': ');
        this.nodeDTOs.forEach(elem => {
            elem.printMe();
        });
    }
}


export {createNodesFromNodeDTOs, findNodeById, findOutgoingsToNodeDTO,
     findIncomingsToNodeDTO};
export {NodeDTO, EdgeDTO, GraphDTO};