function showDialogSure(text, deletingedgenum){
    if(deletingedgenum == undefined || deletingedgenum > 0){
        return confirm(text);
    }
    return true;
}

function showInfo(DOMid, edgeid){
    document.getElementById(DOMid).innerHTML = 
    '<span>Kapcsolat (él) azonosító: ' + edgeid + '</span>';
}

function hideEdgeInfo(DOMid){
    document.getElementById(DOMid).innerHTML = '';
}

function stageOnClickListener(ev) {
    // e.target is a clicked Konva.Shape or 
    // current stage if you clicked on empty space
    const viewelem = ev.target.getParent(); 
    if(viewelem && viewelem.attrs.id != undefined && 
            viewelem.attrs.id.substring(0,3) =="elv" ){
        const entityid = getEntityIdFromDOMId("elv", viewelem.attrs.id);
        console.log('clicked on edge id: '  + entityid, "LOG stage click");
        updateEdgeDataContainer(entityid, 'datacontainers');
    }
}

function putTrashOnLayer(mylayer, TrashRectData){
    const trashview = new TrashRectView(TrashRectData, mylayer);
    trashview.getLayer();
}

function putNodeCrOnLayer(mylayer, NodeCrRectData){
    const nodecrview = new NodeCrRectView(NodeCrRectData, mylayer);
    nodecrview.getLayer();
}

async function putNodeInCreator(mylayer, position, node_R, graphid){
    console.log("globalNodeCrBinIsEmpty " + globalNodeCrBinIsEmpty +
     " globalIsCreateNode " + globalIsCreateNode);
    if(globalNodeCrBinIsEmpty && globalIsCreateNode){
        const newnodedto = {};
        newnodedto.x = position.X;
        newnodedto.y = position.Y;
        newnodedto.label = globalGraph.graph.getNewNodeLabel('n');
        console.log(globalGraph.graph.nodes, 'LOG globalGraph.graph.nodes');
        /*
        newnodedto.label = null;
        if(globalCounters.hasgraphloaded == false){
           newnodedto.label = 'n' +  globalCounters.nodes++;
        }
        */
        const upON = {
            nodedto : newnodedto,
            entityid : graphid
        }
        // AJAX call
        const doWithResponse = async (json) => {
            const dto = await json;
            if(dto){
                const flag = dto.entry ? 1 : (dto.finish ? 2 : 0);  
                const element = new Node(dto.id, position.X, position.Y, flag);
                element.label = dto.label;
                const nv = new NodeView(element, node_R, element.label);
                console.log(element, "LOG node element");
                globalNextNode.node = element;
                globalNextNode.view = nv;
                mylayer.add(globalNextNode.view.getView());
                mylayer.draw();
            }
        };
        getNewNodeDTOFromServer(upON, doWithResponse);
    } else {
        //mylayer.draw();
    }
    globalNodeCrBinIsEmpty = false;
    //return mylayer;
}
async function updateCanvasAndGlobalData(graphid){
    //create static canvas elements
    globalCounters.hasgraphloaded = true;
    if(globalUser.firstrun){
        globalStage = new Konva.Stage({
            container: 'draw-shapes',
            width: globalBoundX,
            height: globalBoundY,
        });
        const stageClickListener = stageOnClickListener;
        globalStage.on('click', stageClickListener);
        globalUser.firstrun = false;
    } else {
        // remove dynamic canvas elements
        globalStage.destroyChildren();
        globalLayer = null;
    }
    const konvalayer = new Konva.Layer();
    globalLayer = konvalayer;
    putNodeCrOnLayer(konvalayer, globalNodeCrData);
    putTrashOnLayer(konvalayer, globalTrashData);
        
    // place dynamic elements on canvas and update DOM
    putGraphOnLayerAndUpdateDOM(konvalayer, graphid, false)
    //When actual user behavior is not cloning
    setTimeout(function(){
        putNodeInCreator(konvalayer, {X : globalNewNodeCrPosX,
            Y : globalNewNodeCrPosY}, globalNode_R, graphid);
        globalStage.add(konvalayer);
        konvalayer.draw();    
    },750);    
}

function putGraphOnLayerAndUpdateDOM(mylayer, graphid, isupdate){
    if(graphid){
        // AJAX call
        //real graph
        const doWithResponse = async (json) => {
            const data = await json;
            if(data){
                globalCounters.hasgraphloaded = false;
                globalData = new GlobalData([], [], [], []);
                const edgedtolist = data.edgedtos;
                const nodedtolist = data.nodedtos;
                if(nodedtolist){
                    globalCounters.nodes = nodedtolist.length;
                }
                if(edgedtolist){
                    globalCounters.edges = edgedtolist.length;
                    if(edgedtolist.length > 0){
                        for(const dto of edgedtolist){
                            globalData.GraphEdgeDatas.push(dto);
                        }
                    }
                }
                const desclist = await data.descdtos;
                if(desclist && desclist.length > 0){
                    for(const dto of desclist){
                        globalData.Descriptions.push(dto);
                    }
                }
                const ielist = await data.invelemdtos;
                if(ielist && ielist.length > 0){
                    for(const dto of ielist){
                        globalData.InventoryElements.push(dto);
                    }
                }
                const piclist = await data.picdtos;
                if(piclist && piclist.length > 0){
                    for(const dto of piclist){
                        globalData.Pictures.push(dto);
                    }
                }
                const orphandescids = await data.orphandescids;
                if(orphandescids && orphandescids.length > 0){
                    for(const dto of orphandescids){
                        globalData.OrphanDescriptionIds.push(dto);
                    }
                }
                const orphaninvelemids = await data.orphaninvelemids;
                if(orphaninvelemids && orphaninvelemids.length > 0){
                    for(const dto of orphaninvelemids){
                        globalData.OrphanInventoryElementIds.push(dto);
                    }
                }
                const orphanpicids = await data.orphanpicids;
                if(orphanpicids && orphanpicids.length > 0){
                    for(const dto of orphanpicids){
                        globalData.OrphanPictureIds.push(dto);
                    }
                }
                globalData.populateMapXXXToEdgeIds();
                const modelgraph = new GraphFactory(document.getElementById("graphname").value);
                modelgraph.setNodeDTOs(nodedtolist);
                modelgraph.setEdgeDTOs(edgedtolist);
                const mygraph = await modelgraph.constructGraph();
                globalGraph.graph = mygraph;
                globalGraph.graphView = new GraphView(mygraph, globalNode_R, mylayer);
                if(modelgraph.edges && modelgraph.edges.length > 0){
                    const firstedge = modelgraph.edges[0];
                    const edgedata = await globalData.createEdgeDataById(firstedge.id);
                    populateDOMelementsOfEdgeData(edgedata);
                }
                
                // populate components
                if(!isupdate){
                    populateDOMelementsOfOrphanData();
                }
                
                updateDOMSelectNodeFromTo("selectnodefrom", "selectnodeto");
                updateDOMSelectNodeFromToByIndex("selectnodestart", "selectnodeend");
                console.log("user pictures: " + globalData.OrphanPictureIds.length + 
                        ", user inventory elements: " + globalData.OrphanInventoryElementIds.length +
                        ", user descriptions: " + globalData.OrphanDescriptionIds.length);
            }
        };
        getOrphanAndGraphDTOByGraphId(graphid, doWithResponse);
    } else {
        // dummy graph
        const dummygraph = new GraphFactory("proba");
        dummygraph.setNodeDTOs(dummyNodeDTOs);
        dummygraph.setEdgeDTOs(dummyEdgeDTOs);
        globalGraph.graph = dummygraph.constructGraph();
    }
}

class VectorView{
    constructor(fromX, fromY, toX, toY, edge, id){
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        this.edge = edge;
        this.id = id;
    }
}
VectorView.prototype.getView = function(){
        const edgeid = this.edge.id;
        const edgeviewid = this.id;
        const nodeid = this.edge.toNode.id;
        let vektor = new Konva.Arrow({
                points: [this.fromX, this.fromY, this.toX, this.toY],
                pointerLength: 9,
                pointerWidth: 6,
                fill: 'blue',
                stroke: 'blue',
                strokeWidth: 2,
                draggable : true,
                dragDistance : 3,
                id : this.id, 
                name : 'mygraph',
                fromNodeId : this.edge.fromNodeId, 
                toNodeId : this.edge.toNodeId
            });
        vektor.on('mouseover', pointerCursor);
        vektor.on('mouseover', function(){
            showInfo("msgFromKonvaEvent", edgeid);
        });
        vektor.on('mouseout', defaultCursor);
        vektor.on('mouseout', function(){
            hideEdgeInfo("msgFromKonvaEvent");
        });
        vektor.on('dragend', function(){
            if(showDialogSure('Biztos törli ezt a (' + edgeid + ') élt?')){
                const deleteComp = document.getElementById("cbDeleteEdgeData").checked;
                const upON = {
                    useremail : globalUser.email,
                    entityid : edgeid 
                };
                if(!deleteComp){
                    //if detaching in backend works uncomment:
                    /*
                    const edto = globalData.getEdgeDTO(edgeid);
                    const detachingobject = {
                            ieids : edto.inventoryElementIds ? edto.inventoryElementIds : null, 
                            picids : edto.pictureIds ? edto.pictureIds : null,
                            descid : edto.descriptionId ? edto.descriptionId : null 
                    };
                    const doWithResponse1 = async (text) => {
                        const resp =  await text;
                        if(resp === "OK"){
                            console.log("detached elements from edge (" + edgeid + ") successfully");
                        }
                    };
                    detachAllFromEdgeIterated(upON, detachingobject, doWithResponse1);
                    // or a more effective API call:
                    // detachAllFromEdgeOnServer(upON, doWithResponse1);
                    */
                    const doWithResponse2 = async (text) => {
                        const resp =  await text;
                        if(resp === "OK"){
                            //if detaching in backend works, uncomment:
                            //globalData.deleteEdgeDTOWithDetachAll(edgeid);
                            //if detaching in backend works, comment:
                            globalData.deleteEdgeDTOWithRemoveAll(edgeid);
                            //if detaching in backend works, uncomment:
                            //detachDescPicInventoryDOMElements(edgeid, detachingobject);
                            
                            // deleting from model is necessary 
                            globalGraph.graph.removeEdgeById(edgeid);
                            globalGraph.graphView.removeEdgeWithLabelViewById(edgeid);
                            updateDOMSelectEdge("selectedge");
                        }
                    };
                    deleteEdgeFromServer(upON , doWithResponse2);
                } else {
                    const doWithResponse = async (text) => {
                        const resp =  await text;
                        if(resp === "OK"){
                            globalData.deleteEdgeDTOWithRemoveAll(edgeid);
                            // deleting from model is necessary 
                            globalGraph.graph.removeEdgeById(edgeid);
                            globalGraph.graphView.removeEdgeWithLabelViewById(edgeid);
                            updateDOMSelectEdge("selectedge");
                            console.log(globalData.SelectedEdgeData, "LOG globalData.SelectedEdgeData");
                            if(globalData.SelectedEdgeData.id === edgeid){
                                const containerid = 'datacontainers';
                                // a modelbol mar torolve lett!
                                const edges = globalGraph.graph.getEdges();
                                if(edges.length > 0){
                                    updateEdgeDataContainer(edges[0].id, containerid);
                                } else {
                                        //function to remove edgedata from container's
                                        const parent = document.getElementById(containerid);
                                        const exist = !!parent.getElementsByClassName('edgedatacontainer');
                                        const childDOMid = parent.firstChild.getAttribute("id"); 
                                        if(exist && childDOMid && childDOMid.substring(0,6) == "DOMedc"){
                                            parent.removeChild(parent.firstChild);
                                        } 
                                    }
                                }
                        }
                    };
                    deleteEdgeFromServer(upON , doWithResponse);
                }
            }
        });
        return vektor;                    
};

class EdgeView extends VectorView{
    constructor(edge, node_R, getView){
        const atfogo = Math.sqrt((edge.fromNode.X - edge.toNode.X) *
        (edge.fromNode.X - edge.toNode.X) + 
        (edge.fromNode.Y - edge.toNode.Y) * (edge.fromNode.Y - edge.toNode.Y));
        const rate = node_R / atfogo;
        const offset_X = Math.floor(rate * (edge.toNode.X - edge.fromNode.X));
        const offset_Y = Math.floor(rate * (edge.toNode.Y - edge.fromNode.Y));    
        const id = 'ev' + edge.id;
        super(edge.fromNode.X + offset_X, edge.fromNode.Y + offset_Y, 
            edge.toNode.X - offset_X, edge.toNode.Y - offset_Y, edge, id, getView);
        this.id = id;
        this.edge = edge;
        this.node_R = node_R;    
    }
}

class EdgeLabel{
    constructor(edge, fontsize){
        this.viewId = 'elv' + edge.id;
        this.edge = edge;
        this.fontsize = fontsize;
    }
}
EdgeLabel.prototype.getView = function(){
        let myedge = this.edge;
        const offsetX = -5;
        const offsetY = -2;
        const groupH = this.fontsize + 1;
        const groupW = myedge.label.length * (this.fontsize - 2);
        let groupX = Math.floor(((myedge.fromNode.X + myedge.toNode.X)-groupW) / 2) -7;
        let groupY = Math.floor(((myedge.fromNode.Y + myedge.toNode.Y)-groupW) / 2); 
        if(myedge.isSuperbIfDouble()){
            groupY += globalNode_R; 
            groupX += globalNode_R + 7;
        }
        let edgelabelview = new Konva.Group({
            x : groupX + offsetX,
            y : groupY + offsetY,
            width : groupW,
            height : groupH,
            draggable : false,
            id : 'elv' + myedge.id,
            name : 'mygraph',
            fromNodeId : myedge.fromNodeId, 
            toNodeId : myedge.toNodeId
        });
        let txt = new Konva.Text({
            text : myedge.label,
            fontSize : this.fontsize,
            fontFamily : 'Calibri',
            fill : 'black',
            width : (groupW + 1),
            padding : 1,
            align : 'center',
            id : 'eltv' + myedge.id
        });
        edgelabelview.add(txt);
        edgelabelview.on('mouseover', pointerCursor);
        edgelabelview.on('mouseout', defaultCursor);
        return edgelabelview;
};
EdgeLabel.prototype.getInfo = function(){
        return "Honnan: " + this.edge.fromNode.label + " | Hova: " +
        this.edge.toNode.label + " | Címke: " + this.label;
};

class NodeView {
    constructor(node, node_R, nodelabel){
        this.X = node.X;
        this.Y = node.Y;
        this.node = node;
        this.viewId = 'nv' + node.id;
        this.myr = node_R;
        this.cR = node.isFinish ? 255 : 140;
        this.cG = node.isEntry ? 255 : 150;
        this.cB = 140;
        this.rgb = "rgb(" + this.cR +"," + this.cG +"," + this.cB +")";
        this.stroke = node.isEntry ? 'black' : this.rgb;
        this.label = nodelabel == undefined ? node.label : nodelabel; 
    }
}
NodeView.prototype.getView = function(){
        const groupX = this.X - this.myr;
        const groupY = this.Y - this.myr;
        const groupW = 2 * this.myr;
        const fontsize = this.myr - 1;
        const mynode = this.node;
        const viewid = this.viewId;
        const node_r = this.myr;
        let nodeview = new Konva.Group({
                x : groupX,
                y : groupY,
                width : groupW,
                height : groupW,
                draggable : true,
                id : viewid,
                name : 'mygraph'
            });
        let circle = new Konva.Circle({
                x: this.myr,
                y: this.myr,
                radius: this.myr,
                fill: this.rgb,
                //strokeWidth : 5,
                //stroke : this.stroke
            });    
        let txt = new Konva.Text({
                text : this.label,
                fontSize : fontsize,
                fontFamily : 'Calibri',
                fill : 'black',
                width : (groupW + 1),
                padding : 4,
                align : 'center'
            });
        nodeview.add(circle);
        nodeview.add(txt);
                
        nodeview.on('mouseover', pointerCursor);
        nodeview.on('mouseout', defaultCursor);
        nodeview.on('dragmove', function (){
            const X = Math.floor(nodeview.attrs.x + node_r);
            const Y = nodeview.attrs.y + node_r;
            if((X > 0 && X < globalBoundX - groupW) && 
                    (Y > 0 && Y < globalBoundY - groupW)){
                nodeview.draggable(true);
                globalGraph.graph.updateNodeXYById(mynode.id, X, Y); 
                globalGraph.graphView.updateEdges(mynode);
            } else {
                nodeview.draggable(false);
            }
        });
        nodeview.on('dragend', function(){
            const X = nodeview.attrs.x;
            const Y = nodeview.attrs.y;
            let deleteself = false;
            const graphid = document.getElementById("graphid").value;
            if (!isInsideRect(X, Y, globalNodeCrData.ULeft.X,
                    globalNodeCrData.ULeft.Y, globalNodeCrData.DRight.X, 
                    globalNodeCrData.DRight.Y) && 
                    viewid == globalNextNode.view.getViewId() &&
                    !globalNodeCrBinIsEmpty){
                globalNodeCrBinIsEmpty = true;
                globalGraph.graph.addNode(mynode);
                putNodeInCreator(globalLayer, 
                    {X : globalNewNodeCrPosX, Y : globalNewNodeCrPosY}, 
                    globalNode_R, graphid);
                globalGraph.graph.updateNodeXYById(mynode.id, X + node_r, Y + node_r); 
                globalGraph.graphView.updateEdges(mynode);
                updateDOMSelectNodeFromTo("selectnodefrom", "selectnodeto");
                updateDOMSelectNodeFromToByIndex("selectnodestart", "selectnodeend");
            }
            if (isInsideRect(X, Y, globalTrashData.ULeft.X, 
                    globalTrashData.ULeft.Y, globalTrashData.DRight.X, 
                    globalTrashData.DRight.Y)){
                const somenode = globalGraph.graph.getNodeById(mynode.id);        
                if(somenode !=undefined && (somenode.isEntry || somenode.isFinish)){
                    const xpos = globalTrashData.ULeft.X - 2*node_r;
                    const ypos = globalTrashData.ULeft.Y + node_r;
                    nodeview.absolutePosition({x : xpos, y : ypos});
                    const newX = Math.floor(xpos + node_r);
                    const newY = Math.floor(ypos + node_r);
                    globalGraph.graph.updateNodeXYById(mynode.id, newX, newY);
                    globalGraph.draggedNodeId = mynode.id;
                    globalGraph.draggedNode.X = newX;
                    globalGraph.draggedNode.Y = newY;
                    const upON = {
                        id : mynode.id, 
                        x : newX,
                        y : newY
                    }
                    updateNodeXY(upON);
                    nodeview.draggable(true);
                } else {
                    const deletingedges = globalGraph.graph.getEdgesOfNode(mynode.id);
                    const deletingedgenum = deletingedges.length;
                    /*
                    const isDelWithDetachOk = showDialogSure('A csomóponttal (' + 
                                        mynode.label +' [' + mynode.id + ']) ' +
                                        'együtt törlődik ' + deletingedgenum + 
                                        ' db csatlakozó él és a kapcsolódó adatokat ' +
                                        'leválasztom.\nMehet?', deletingedgenum);
                    */
                    const isDeletingOk = showDialogSure('A csomóponttal (' + 
                                        mynode.label +' [' + mynode.id + ']) ' +
                                        'együtt törlődik ' + deletingedgenum + 
                                        ' db csatlakozó él és a kapcsolódó adatok ' +
                                        'elvesznek\nMehet?', deletingedgenum);
                    if(isDeletingOk){
                        //AJAX CALL !!!
                        if(deletingedgenum > 0){
                            // remove edge data from viewmodel and 
                            // detaching attached entities
                            let nondeletingedgeids = [];
                            for (const myedge of globalGraph.graph.getEdges()){
                                if(!deletingedges.includes(myedge)){
                                    nondeletingedgeids.push(myedge.id);
                                } 
                            }
                            for(const edge of deletingedges){
                                const upON = {
                                    useremail : globalUser.email,
                                    entityid : edge.id 
                                }
                                // if detaching before deleting edge
                                // in backend works, uncomment:
                                /*
                                const edto = globalData.getEdgeDTO(edge.id);
                                const detachingobject = {
                                    ieids : edto.inventoryElementIds ? edto.inventoryElementIds : null, 
                                    picids : edto.pictureIds ? edto.pictureIds : null,
                                    descid : edto.descriptionId ? edto.descriptionId : null 
                                }
                                const doWithResponse1 = async (text) => {
                                    const resp =  await text;
                                    if(resp === "OK"){
                                        console.log("detached elements from edge (" + edge.id + ") successfully");
                                    }
                                }
                                detachAllFromEdgeOnServer(upON, doWithResponse1)
                                */
                                const doWithResponse2 = async (text) => {
                                    const resp =  await text;
                                    if(resp === "OK"){
                                        // if detach before deleting edge works in backend
                                        // uncomment:
                                        /*
                                        globalData.deleteEdgeDTOWithDetachAll(edge.id);
                                        detachDescPicInventoryDOMElements(edge.id, detachingobject);
                                        */
                                        globalData.deleteEdgeDTOWithRemoveAll(edge.id);
                                    }
                                }
                                deleteEdgeFromServer(upON , doWithResponse2);
                            }
                            // remove Edge from canvas
                            globalGraph.graphView.removeEdgeAndLabelViews(deletingedges);
                            // update DOM 
                            const containerid = 'datacontainers';
                            const DOMcont = document.getElementById(containerid);
                            if(nondeletingedgeids.length > 0){
                                const poppededgeid = nondeletingedgeids.pop();
                                // ez alabbi sor a globalData.SelectedEdgeData-t is beallitja
                                globalData.createEdgeDataById(poppededgeid);
                                updateEdgeDataContainer(globalData.SelectedEdgeData.id, containerid);
                            } else {
                                for(let elem of DOMcont.childNodes){
                                    if(elem["id"] && elem["id"].substring(0, 4) ==="DOMe"){
                                        elem.parentNode.removeChild(elem);
                                        break;
                                    }
                                }
                            }    
                            updateDOMSelectEdge("selectedge");
                            deleteself = true;
                        }
                        //AJAX call
                        let upON3 = {
                            entityid : graphid, 
                            componentid : mynode.id 
                        };
                        const doWithResponse3 = async (text) => {
                            const resp = await text;
                            console.log(`server's response deleting Node (${mynode.id}):`)
                            console.log(resp);
                        };
                        deleteNodeFromServer(upON3, doWithResponse3);
                        // remove node from canvas
                        globalGraph.graphView.removeNodeView(mynode.id); 
                        // remove node from model
                        globalGraph.graph.removeNodeById(mynode.id);
                        updateDOMSelectNodeFromTo("selectnodefrom", "selectnodeto");
                        updateDOMSelectNodeFromToByIndex("selectnodestart", "selectnodeend");    
                    }
                }
            } else {
                globalGraph.draggedNodeId = mynode.id;
                globalGraph.draggedNode.X = X;
                globalGraph.draggedNode.Y = Y;
                const upON = {
                    id : mynode.id, 
                    x : X,
                    y : Y
                }
                updateNodeXY(upON);
                nodeview.draggable(true);
            }
            if(!deleteself){
                globalGraph.graphView.updateEdgeLabels(mynode);
            }
        });
        nodeview.on('dragstart', function(){
            //globalGraph.graphView.updateGraph(globalGraph.graph);
        });
        return nodeview;
};
NodeView.prototype.getNodeId = function(){
        return this.node.id;
};
NodeView.prototype.getViewId = function(){
        return this.viewId;
};
NodeView.prototype.getViewLabel = function(){
        return this.label;
};


class GraphView{
    constructor(graph, node_R, layer){
        this.name = 'gv' + graph.name;
        this.mylayer = layer;
        this.mygraph = graph;
        this.node_R = node_R;
        if(this.mygraph.getNodes() != undefined){
            for(const element of this.mygraph.getNodes()){
                const nv = new NodeView(element, node_R);
                this.mylayer.add(nv.getView());
            }
        }
        if(this.mygraph.getEdges() != undefined){
            for(const element of this.mygraph.getEdges()){
                const ev = new EdgeView(element, node_R);
                this.mylayer.add(ev.getView());
                const el = new EdgeLabel(element, node_R - 1); 
                this.mylayer.add(el.getView());
            }
        }
    }
    getLayer(){
        console.log(this.mylayer, "layer");
        return this.mylayer;       
    }
    updateSelf(graph){
        this.mygraph = graph;
        this.mylayer.find('.mygraph').forEach(element =>{
            element.destroy();
        });
        if(this.mygraph.getNodes()){
            this.mygraph.getNodes().forEach(element => {
                const nv = new NodeView(element, this.node_R);
                this.mylayer.add(nv.getView());
            });
        }
        else {
            console.log('this.mygraph.getNodes() is undefined');
        }
        if(this.mygraph.getEdges() != undefined){
            this.mygraph.getEdges().forEach(element => {
                const ev = new EdgeView(element, this.node_R);
                this.mylayer.add(ev.getView());
                const el = new EdgeLabel(element, this.node_R - 1); 
                this.mylayer.add(el.getView());
                //console.log(element);
            });
        }
        this.mylayer.draw();
    }
    
    createNodeView(nodeid){
        const element = this.mygraph.getNodeById(nodeid);
        const nv = new NodeView(element, this.node_R, element.id);
        this.mylayer.add(nv.getView());
        this.mylayer.draw();
    }
    updateEdges(my_node){
        const my_node_R = this.node_R;
        if(my_node != undefined){
            //console.log("updating (" + my_node.label + ") connecting edges positions. Connecting edges:");
            //console.log(my_node);
            if(my_node.getIncomings() != undefined){
                //let count = 0; 
                my_node.getIncomings().forEach(elem => {
                    //count++;
                    //console.log("incoming (" + count + ") :" + elem.label);
                    const idselector = '#ev'+ elem.id;
                    this.mylayer.find(idselector).destroy();
                    const ev = new EdgeView(elem, my_node_R);
                    this.mylayer.add(ev.getView());
                });
            }
            if(my_node.getOutgoings() != undefined){
                //let count = 0;
                my_node.getOutgoings().forEach(elem => {
                    //count++;
                    //console.log("outgoing (" + count + ") :" + elem.label);
                    const idselector = '#ev'+ elem.id;
                    this.mylayer.find(idselector).destroy();
                    const ev = new EdgeView(elem, my_node_R);
                    this.mylayer.add(ev.getView());
                });
            }
            this.mylayer.draw();
        }
    }
    removeEdgeAndLabelViews(deletingedges){
        //const deletingedges = mygraph.getEdgesOfNode(nodeid);
        if(deletingedges && deletingedges.length > 0){
            console.log("try to destroy relating canvas elements to " + deletingedges.length + " edges");
            deletingedges.forEach(elem => {
                const idselector2 = '#elv'+ elem.id;
                this.mylayer.find(idselector2).destroy();
                const idselector3 = '#ev'+ elem.id;
                this.mylayer.find(idselector3).destroy();
                console.log("edgeview(" + idselector3 + ") was deleted from canvas");
                console.log("edgelabelview(" + idselector2 + ") was deleted from canvas");
            });
            this.mylayer.draw();
        }
    }
    updateEdgeLabels(mynode){
        let mynode_R = this.node_R;
        if(mynode){
            if(mynode.getIncomings() != undefined){
                mynode.getIncomings().forEach(elem => {
                    const idselector = '#elv'+ elem.id;
                    this.mylayer.find(idselector).destroy();
                    const el = new EdgeLabel(elem, mynode_R - 1);
                    this.mylayer.add(el.getView());
                });
            }
            if(mynode.getOutgoings() != undefined){
                mynode.getOutgoings().forEach(elem => {
                    const idselector = '#elv'+ elem.id;
                    this.mylayer.find(idselector).destroy();
                    const el = new EdgeLabel(elem, mynode_R - 1);
                    this.mylayer.add(el.getView());
                });
            }
            this.mylayer.draw();
        }
    }
    updateLabelofEdge(myedge){
        const idselector = '#elv'+ myedge.id;
        const edgelabelview = this.mylayer.find(idselector); 
        this.mylayer.find(idselector).destroy();
        const el = new EdgeLabel(myedge, this.node_R - 1);
        this.mylayer.add(el.getView());
        this.mylayer.draw();
    }
    removeEdgeViewsByNodeId(nodeid){
        console.log("remove edges to " + nodeid);
        const mynode = this.mygraph.getNodeById(nodeid);
        console.log(mynode);
        const self = {
            graph : this.mygraph,
            layer : this.mylayer
        }
        if(mynode != undefined){
            if(mynode.getIncomings() != undefined){
                const edges = [];
                for(const elem of mynode.getIncomings()){
                    edges.push(elem)
                }
                for(const elem of edges){
                    // AJAX call
                    const upON = {
                        entityid : elem.id
                    }
                    const doWithResponse = async (json) => {
                        const resp = await json.text;
                        console.log("deleting edge (" + elem.id + ") is " +
                          resp);
                        if(resp === "OK"){
                            // delete from model too to make things simple
                            self.graph.removeEdge(elem);
                            const idselector1 = '#ev'+ elem.id;
                            self.layer.find(idselector1).destroy();
                            const idselector2 = '#elv'+ elem.id;
                            self.layer.find(idselector2).destroy();
                        }
                    }
                    deleteEdgeFromServer(upON, doWithResponse);
                }
            }
            if(mynode.getOutgoings() != undefined){
                const edges = [];
                for(const elem of mynode.getOutgoings()){
                    edges.push(elem)
                }
                for(const elem of edges){
                    //AJAX call
                    const opON = {
                       entityid : elem.id 
                    }
                    const doWithResponse = async (json) =>{
                        const resp = await json.text;
                        if(resp === "OK"){
                            // delete from model too to make things simple
                            self.graph.removeEdge(elem);
                            const idselector1 = '#ev'+ elem.id;
                            self.layer.find(idselector1).destroy()
                            const idselector2 = '#elv'+ elem.id;
                            self.layer.find(idselector2).destroy();
                        }
                    }   
                    deleteEdgeFromServer(upON, doWithResponse);
                }
            }
        }
    }
    removeEdgeWithLabelViewById(edgeid){
            const idselector1 = '#ev'+ edgeid;
            this.mylayer.find(idselector1).destroy();
            const idselector2 = '#elv'+ edgeid;
            this.mylayer.find(idselector2).destroy();
            this.mylayer.draw();
    }
    removeNodeView(nodeid){
        const idselector = '#nv' + nodeid;
        this.mylayer.find(idselector).destroy();
        this.mylayer.draw();
    }
    removeNodeViewWithEdgeViewsById(nodeid){
            const idselector = '#nv' + nodeviewid;
            this.removeEdgeViewsByNodeId(nodeid);
            // deleting from model is necessary
            this.mygraph.removeNodeById(nodeid);
            this.mylayer.find(idselector).destroy();
            this.mylayer.draw();
    }
    changeColorOfEdgeViewsById(edgeids, color){
        if(edgeids && edgeids.length > 0){
            try {
                for(const edgeid of edgeids){
                    const idselector = '#ev' + edgeid;
                    const edgeview = this.mylayer.find(idselector);
                    edgeview[0].attrs.fill = color;
                    edgeview[0].attrs.stroke = color;
                }
                this.mylayer.draw();    
            } catch (error) {
                console.log(error, "LOG error in changeColorOfEdgeViewsById");
            }
        }
    }
}

class TrashRectView{
    constructor(trashRectData, layer){
        this.mytrdata = trashRectData;
        this.mylayer = layer;
        this.label = 'Kuka';
        this.fontsize = 13;
    }
    getLayer(){
        let groupW = this.mytrdata.DRight.X - this.mytrdata.ULeft.X;
        let groupH = this.mytrdata.DRight.Y - this.mytrdata.ULeft.Y;    
        let trashbin = new Konva.Group({
                x : this.mytrdata.ULeft.X,
                y : this.mytrdata.ULeft.Y,
                width : groupW,
                height : groupH,
                draggable : false,
                id : 'trashbin'
            });
        let myrect = new Konva.Rect({
                width: groupW,
                height: groupH,
                strokeWidth : 1,
                stroke : 'black',
                fill : 'lightgray',
                opacity : 0.6,
                cornerRadius : 8,
            });    
        let txt = new Konva.Text({
                text : this.label,
                fontSize : this.fontsize,
                fontFamily : 'Calibri',
                fill : 'black',
                width : (groupW-1),
                padding : 4,
                align : 'center'
            });
        trashbin.add(myrect);
        trashbin.add(txt);    
        this.mylayer.add(trashbin);
        return this.mylayer; 
    }   
}

class NodeCrRectView{
    constructor(nodeCrRectData, layer){
        this.myncrdata = nodeCrRectData;
        this.mylayer = layer;
        this.label = 'Csomópont';
        this.fontsize = 13;
    }
    getLayer(){
        let groupW = this.myncrdata.DRight.X - this.myncrdata.ULeft.X;
        let groupH = this.myncrdata.DRight.Y - this.myncrdata.ULeft.Y;    
        let nodecreator = new Konva.Group({
                x : this.myncrdata.ULeft.X,
                y : this.myncrdata.ULeft.Y,
                width : groupW,
                height : groupH,
                draggable : false,
                id : 'nodecreator'
            });
        let myrect = new Konva.Rect({
                width: groupW,
                height: groupH,
                strokeWidth : 1,
                stroke : 'black',
                fill : 'yellow',
                opacity : 0.6,
                cornerRadius : 8,
                draggable : false
            });    
        let txt = new Konva.Text({
                text : this.label,
                fontSize : this.fontsize,
                fontFamily : 'Calibri',
                fill : 'black',
                width : (groupW-1),
                padding : 4,
                align : 'center',
                draggable : false
            });
        nodecreator.add(myrect);
        nodecreator.add(txt);    
        this.mylayer.add(nodecreator);
        return this.mylayer; 
    }
}