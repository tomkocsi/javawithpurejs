/*
class Node{
    constructor(id, x, y, flag){
        this.X = x;
        this.Y = y;
        this.id = id;
        this.isFinish = flag == 2 ? true : false;
        this.isEntry = flag == 1 ? true : false;
        this.incomingEdges = [];
        this.outgoingEdges = [];
        this.label = this.isFinish ? "Exit" : (this.isEntry ? "Start" : id);
    }
    setXY(x, y){
        this.X = x;
        this.Y = y;
    }
    setFinish(){
        this.isFinish = true;
        this.label = "Exit";
    }
    setEntry(){
        this.isEntry = true;
        this.label = "Start";
    }
    setLabel(text){
        this.label = this.isFinish || this.isEntry ? this.label : text;
    }
    getIncomings(){
        return this.incomingEdges;
    }
    setIncomings(inEdges){
        this.incomingEdges = inEdges;  
    }
    getOutgoings(){
        return this.outgoingEdges;
    }
    setOutgoings(outEdges){
        this.outgoingEdges = outEdges;  
    }
    addIncomingEdge(edge){
        this.incomingEdges = addElementToList(edge, this.incomingEdges);
    }
    addOutgoingEdge(edge){
        this.outgoingEdges = addElementToList(edge, this.outgoingEdges);
    }
    removeIncomingEdge(edge){
        removeElementFromList(edge, this.incomingEdges);
    }
    removeOutgoingEdge(edge){
        removeElementFromList(edge, this.outgoingEdges);
    }
    removeIncomingEdgeById(edgeid){
        this.incomingEdges = removeElementById(edgeid, this.incomingEdges);
    }
    removeOutgoingEdgeById(edgeid){
        this.outgoingEdges = removeElementById(edgeid, this.outgoingEdges);
    }
    printEdges(){
        let text1 = this.label + '.incomingEdges: ';
        let text2 = this.label + '.outgoingEdges: ';
        if(this.incomingEdges != undefined && this.incomingEdges.length > 0){
            for (let k = 0; k < this.incomingEdges.length; k++) {
                text1 += this.incomingEdges[k].id + ", ";
                
            }
        }
        if(this.outgoingEdges != undefined && this.outgoingEdges.length > 0){
            for (let k = 0; k < this.outgoingEdges.length; k++) {
                text2 += this.outgoingEdges[k].id + ", ";
            }
        }
        console.log(text1 + '\n' + text2);
    }
}

class Edge{
    constructor(id, fromnode, tonode, label){
        this.id = id;
        this.fromNode = fromnode;
        this.toNode = tonode;
        this.label = label;
    }
    getFromNode(){
        return this.fromNode;
    }
    getToNode(){
        return this.toNode;
    }
    setFromNode(fromnode){
        this.fromNode = fromnode;
    }
    setToNode(tonode){
        this.toNode = tonode;
    }
    setLabel(label){
        this.label = label;
    }
    getLabel(){
        return this.label;
    }
    isDoubleConn(){
        let len = this.fromNode.getOutgoings().length; 
        let j = 0;
        while (j < len && this.toNode !== this.fromNode.getOutgoings()[j].toNode){
            ++j;
        }
        if(j >= len){
            return false;
        } else {
            len = this.toNode.getOutgoings().length;
            j = 0;
            while (j < len && this.fromNode !== this.toNode.getOutgoings()[j].toNode){
                ++j;
            }
            return j < len;
        }
    }
    isSuperbIfDouble(){
        if(this.isDoubleConn()){
            let j = 0;
            const len = this.toNode.getOutgoings().length;
            while(j < len && this.fromNode !== this.toNode.getOutgoings()[j].toNode){
                ++j;
            }
            if(j < len){
                return parseInt(this.id) > parseInt(this.toNode.getOutgoings()[j].id); 
            }  
        }
        return false;
    }
    printMe(){
        return this.label + ": from " + this.fromNode.label + 
                 " to " + this.toNode.label;
    }   
}

*/
/* experimental */
class Node{
    constructor(id, x, y, flag){
        this.X = x;
        this.Y = y;
        this.id = id;
        this.isFinish = flag == 2 ? true : false;
        this.isEntry = flag == 1 ? true : false;
        this.incomingEdges = [];
        this.outgoingEdges = [];
        this.label = this.isFinish ? "Exit" : (this.isEntry ? "Start" : id);
    }
}
Node.prototype.setXY = function(x, y){
        this.X = x;
        this.Y = y;
};
Node.prototype.setFinish = function(){
        this.isFinish = true;
        this.label = "Exit";
};
Node.prototype.setEntry = function(){
        this.isEntry = true;
        this.label = "Start";
};
Node.prototype.setLabel = function(text){
        this.label = this.isFinish || this.isEntry ? this.label : text;
};
Node.prototype.getIncomings = function(){
        return this.incomingEdges;
};
Node.prototype.setIncomings = function(inEdges){
        this.incomingEdges = inEdges;  
};
Node.prototype.getOutgoings = function(){
        return this.outgoingEdges;
};
Node.prototype.setOutgoings = function(outEdges){
        this.outgoingEdges = outEdges;  
};
Node.prototype.addIncomingEdge = function(edge){
        this.incomingEdges = addElementToList(edge, this.incomingEdges);
};
Node.prototype.addOutgoingEdge = function(edge){
        this.outgoingEdges = addElementToList(edge, this.outgoingEdges);
};
Node.prototype.removeIncomingEdge = function(edge){
        removeElementFromList(edge, this.incomingEdges);
};
Node.prototype.removeOutgoingEdge = function(edge){
        removeElementFromList(edge, this.outgoingEdges);
};
Node.prototype.removeIncomingEdgeById = function(edgeid){
        this.incomingEdges = removeElementById(edgeid, this.incomingEdges);
};
Node.prototype.removeOutgoingEdgeById = function(edgeid){
        this.outgoingEdges = removeElementById(edgeid, this.outgoingEdges);
};
Node.prototype.printEdges = function(){
        let text1 = this.label + '.incomingEdges: ';
        let text2 = this.label + '.outgoingEdges: ';
        if(this.incomingEdges != undefined && this.incomingEdges.length > 0){
            for (let k = 0; k < this.incomingEdges.length; k++) {
                text1 += this.incomingEdges[k].id + ", ";
            }
        }
        if(this.outgoingEdges != undefined && this.outgoingEdges.length > 0){
            for (let k = 0; k < this.outgoingEdges.length; k++) {
                text2 += this.outgoingEdges[k].id + ", ";
            }
        }
        console.log(text1 + '\n' + text2);
};

class Edge{
    constructor(id, fromnode, tonode, label){
        this.id = id;
        this.fromNode = fromnode;
        this.toNode = tonode;
        this.label = label;
    }
}
Edge.prototype.getFromNode = function(){
        return this.fromNode;
};
Edge.prototype.getToNode = function(){
        return this.toNode;
};
Edge.prototype.setFromNode = function(fromnode){
        this.fromNode = fromnode;
};
Edge.prototype.setToNode = function(tonode){
        this.toNode = tonode;
};
Edge.prototype.setLabel = function(label){
        this.label = label;
};
Edge.prototype.getLabel = function(){
        return this.label;
};
Edge.prototype.isDoubleConn = function(){
        let len = this.fromNode.getOutgoings().length; 
        let j = 0;
        while (j < len && this.toNode !== this.fromNode.getOutgoings()[j].toNode){
            ++j;
        }
        if(j >= len){
            return false;
        } else {
            len = this.toNode.getOutgoings().length;
            j = 0;
            while (j < len && this.fromNode !== this.toNode.getOutgoings()[j].toNode){
                ++j;
            }
            return j < len;
        }
};
Edge.prototype.isSuperbIfDouble = function(){
        if(this.isDoubleConn()){
            let j = 0;
            const len = this.toNode.getOutgoings().length;
            while(j < len && this.fromNode !== this.toNode.getOutgoings()[j].toNode){
                ++j;
            }
            if(j < len){
                return parseInt(this.id) > parseInt(this.toNode.getOutgoings()[j].id); 
            }  
        }
        return false;
};
Edge.prototype.printMe = function(){
        return this.label + ": from " + this.fromNode.label + 
                 " to " + this.toNode.label;
};   

class Graph {
    constructor(name){
        this.name = name;
        this.nodes = [];
        this.edges = [];
        this.entryNode = {};
    }
    addNode(node){
        this.nodes = addElementToList(node, this.nodes);
        if (node != undefined && 
                (node.getOutgoings().length > 0 || 
                node.getIncomings().length > 0)){
            if(node.getOutgoings().length > 0){
                node.getOutgoings().forEach(elem => {
                    addElementToList(elem, this.edges);
                });
            }
            if(node.getIncomings().length > 0){
                node.getIncomings().forEach(elem => {
                    addElementToList(elem, this.edges);
                });
            }
        }
        if (node.isEntry){
            this.entryNode = node;
        }
    }
    removeNode(node){
        if(node){
            if(!node.isEntry && !node.isFinish){
                console.log(node, "LOG node to be removed");
                          
                if(node.incomingEdges != null){
                    node.incomingEdges.forEach(element => {
                        removeElementFromList(element, element.fromNode.outgoingEdges);
                        removeElementFromList(element, this.edges); 
                    });
                    
                }
                if(node.outgoingEdges != null){
                    node.outgoingEdges.forEach(element => {
                        removeElementFromList(element, element.toNode.incomingEdges);
                        removeElementFromList(element, this.edges);
                    });
                }
                
                removeElementFromList(node, this.nodes); 
                
            }
            else {
                alert("A belépő- vagy kilépőpont törlése helyett törölje az egész tervet!");
            }
        }
    }
    removeNodeById(nodeid){
        let node = getElementById(nodeid, this.nodes);
        this.removeNode(node);
    }
    getEdgesOfNode(nodeid){
        let node = getElementById(nodeid, this.nodes);
        let edges = [];
        if(node) {
            if(node.incomingEdges){
                node.incomingEdges.forEach(edge =>{
                    if(edge) {
                        edges.push(edge)
                    }
                });
            }
            if(node.outgoingEdges){
                node.outgoingEdges.forEach(edge =>{
                    if(edge) {
                        edges.push(edge)
                    }
                });
            }
        }
        return edges;
    }
    setLabelToEdge(label, edgeid){
        let edge = getElementById(edgeid, this.edges);
        edge.setLabel(label);
    }
    addEdge(edge){
        console.log("class Graph method addEdge was called, graph.edges:");
        this.edges = addElementToList(edge, this.edges);
        console.log(this.edges);
    }
    removeEdge(myedge){

        if(myedge){
            console.log("Before edge (" + myedge.label + ") removal:");
            console.log("myedge.fromNode(" + myedge.fromNode.label + ").printEdges():");
            myedge.fromNode.printEdges();
            console.log("myedge.toNode(" + myedge.toNode.label + ").printEdges():");
            myedge.toNode.printEdges();
            
            myedge.fromNode.removeOutgoingEdge(myedge);
            myedge.toNode.removeIncomingEdge(myedge);
            removeElementFromList(myedge, this.edges);
            
            console.log("After edge (" + myedge.label + ") removal:");
            console.log("myedge.fromNode(" + myedge.fromNode.label + ").printEdges():");
            myedge.fromNode.printEdges();
            console.log("myedge.toNode(" + myedge.toNode.label + ").printEdges():");
            myedge.toNode.printEdges();
            
        } else{
            console.log("myedge undefined but Graph.removeEdge() was called ");
        } 
    }
    removeEdgeById(edgeid){
        let myedge = getElementById(edgeid, this.edges);
        this.removeEdge(myedge);
    }
    getNodes(){
        return this.nodes;
    }
    getEdges(){
        return this.edges;
    }
    getNodeById(nodeid){
        return getElementById(nodeid, this.nodes);    
    }
    getEdgeById(edgeid){
        return getElementById(edgeid, this.edges);
    }
    updateNodeXYById(nodeid, node_x, node_y){
        let node2update = getElementById(nodeid, this.nodes);
        if (node2update != undefined){
            node2update.X = node_x;
            node2update.Y = node_y;   
        }    
    }
    getEntryNode(){
        return this.entryNode;
    }
    isEdgeExist(nodefrom, nodeto){
        let k = 0;
        while(k < this.edges.length && 
            !(this.edges[k].fromNode === nodefrom && 
                this.edges[k].toNode === nodeto)){
            ++k;
        } 
        return k < this.edges.length;
    }
    getNodeByLabel(nodelabel){
        return getElementByLabel(nodelabel, this.nodes)
    }
    setNodeLabel(node, label){
        let k = 0;
        while(k < this.nodes.length && this.nodes[k].label != label){
            ++k;
        } 
        if(k == this.nodes.length){
            node.setLabel(label);
        } 
        else {
            alert("már van ilyen nevű csomópont");
        } 
        
    }
    setEdgeLabel(edge, label){
        let k = 0;
        while(k < this.edges.length && this.edges[k].label != label){
            ++k;
        } 
        if(k == this.edges.length){
            edge.setLabel(label);
        } 
        else {
            alert("már van ilyen nevű kapcsolat");
        } 
        
    }
    getNewNodeLabel(pre = "n") {
		const nodenum = this.nodes.length;
		let numberislabel = true;
		let z = 1;
		let k;
		while(numberislabel) {
			k = 0;
			let found;
			do {
				found = false;
				if(!!parseInt(this.nodes[k].label.substring(pre.length))) {
					if(parseInt(this.nodes[k].label.substring(pre.length)) != z) {
						k++;
					} else {
						found = true;
					}
				} else {
					k++;
				}
			} while (k < nodenum && !found);
			if(k < nodenum) {
				z++;
			} else {
				numberislabel = false;
			}
		}
		return pre + "" + z;  
	}
    printGraph(){
        console.log(this.name + ': ');
        this.nodes.forEach(elem => {
            elem.printEdges();
        });
    }
}