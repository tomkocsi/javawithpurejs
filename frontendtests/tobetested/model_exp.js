
function addElementToList(element, elements){
    let mylist = elements;
    if(element != undefined && elements != undefined){
        const len = mylist.length; 
        let k = 0;
        while (k < len && element.id != mylist[k].id){
            ++k;
        }
        if (k == len){
            mylist.push(element);
        }
    }
    return mylist;
}

function removeElementFromList (element, elements){
    let mylist = [];
    if(element != undefined && elements != undefined && elements.length > 0){
        elements.forEach(elem => {
            if(elem.id != element.id){
                mylist.push(elem);
            }
        }); 
    }
    return mylist;
}

function removeElementById(elementid, elements){
    let mylist = [];
    let text =  "";
    if(elementid != undefined && elements != undefined && elements.length > 0){
        elements.forEach(elem => {
            if(elem.id != elementid){
                mylist.push(elem);
                text += elem.id + ', ';
            }
        }); 
    }
    /*console.log('elements.length : ' + elements.length + 
                ', removed element id: ' + 
                elementid + ' remaining: ' + text);
    */
    return mylist;
}

function getElementById(elementid, elements){
    let mylist = elements;
    if(elementid != undefined && elements != undefined){
        const len = mylist.length; 
        let k = 0;
        while (k < len && elementid != mylist[k].id){
            ++k;
        }
        if (k == len){
            return undefined;
        }
        else {
            return mylist[k];
        }
    }
}

function getElementByLabel(elementlabel, elements){
    let mylist = elements;
    if(elementlabel != undefined && elements != undefined){
        const len = mylist.length; 
        let k = 0;
        while (k < len && elementlabel != mylist[k].label){
            ++k;
        }
        if (k == len){
            return undefined;
        }
        else {
            return mylist[k];
        }
    }
}

class Vertex{
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
        this.incomingEdges = removeElementFromList(edge, this.incomingEdges);
    }
    removeOutgoingEdge(edge){
        this.outgoingEdges = removeElementFromList(edge, this.outgoingEdges);
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
        for (let k = 0; k < this.incomingEdges.length; k++) {
            text1 += this.incomingEdges[k].id + ", ";
            
        }
        for (let k = 0; k < this.outgoingEdges.length; k++) {
            text2 += this.outgoingEdges[k].id + ", ";
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
}

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
        if(node != undefined){
            if(!node.isEntry && !node.isFinish){
                node.outgoingEdges.forEach(element => {
                    this.removeEdge(element); 
                });
                node.incomingEdges.forEach(element => {
                    this.removeEdge(element); 
                });
                this.nodes = removeElementFromList(node, this.nodes);
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
    addEdge(edge){
        this.edges = addElementToList(edge, this.edges);
    }
    removeEdge(myedge){
        if(myedge != undefined){
            this.edges = removeElementFromList(myedge, this.edges);
            myedge.fromNode.removeOutgoingEdge(myedge);
            myedge.toNode.removeIncomingEdge(myedge);
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
        if (node2update!= undefined){
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
    printGraph(){
        console.log(this.name + ': ');
        this.nodes.forEach(elem => {
            elem.printEdges();
        });
    }
}

export {addElementToList, removeElementFromList, removeElementById, 
        getElementById, getElementByLabel};
export {Vertex, Edge, Graph};