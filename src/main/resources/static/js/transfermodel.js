function createEdgesFromEdgeDTOs(edgedtos, nodelist){
    const list = [];
    if(edgedtos != undefined && edgedtos.length > 0){
        for(const edgedto of edgedtos){
            const fromnodeid = edgedto.fromNodeId;
            const tonodeid = edgedto.toNodeId;
            const fromnode = getElementById(fromnodeid, nodelist);
            const tonode = getElementById(tonodeid, nodelist);
            let myedge = new Edge(edgedto.id, fromnode, tonode, edgedto.label);
            list.push(myedge);
        }   
    }
    return list;
}
function createNodesFromNodeDTOs(nodedtos){
    const nodelist = [];
    globalCounters.nodes = 0;
    if(nodedtos != undefined && nodedtos.length > 0){
        for(const nodedto of nodedtos){
            globalCounters.nodes++;
            const flag = nodedto.entry ? 1 : (nodedto.finish ? 2 : 0);
            const mynode = new Node(nodedto.id, nodedto.x, nodedto.y, flag);
            if(nodedto.label == undefined || nodedto.label == "null"){
                mynode.setLabel('n' + globalNodeCounter);    
            } else{
                mynode.setLabel(nodedto.label);
            }
            if (nodedto.entry)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
                mynode.setEntry();
            if(nodedto.finish)
                mynode.setFinish();
            nodelist.push(mynode);
        }
    }
    return nodelist;
}

function findOutgoingsToNodeDTO(nodeid, edgeDTOs, nodes, edges){
    const outgoings = [];
    if((edgeDTOs != undefined && edgeDTOs.length > 0) && 
       (nodes != undefined && nodes.length > 0)){
        for(const edgedto of edgeDTOs){
            if(edgedto.fromNodeId == nodeid){
                globalCounters.edges++;
                const myEdge = findObjectById(edgedto.id, edges)
                myEdge.setFromNode(findObjectById(edgedto.fromNodeId, nodes));
                myEdge.setToNode(findObjectById(edgedto.toNodeId, nodes));
                if(edgedto.label){
                    myEdge.setLabel(edgedto.label);
                } else {
                    myEdge.setLabel('eo' + globalCounters.edges);
                }
                outgoings.push(myEdge);
                //console.log(myEdge.printMe());
            }
        }
    }
    return outgoings;
}

function findIncomingsToNodeDTO(nodeid, edgeDTOs, nodes, edges){
    const incomings = [];
    if((edgeDTOs != undefined && edgeDTOs.length > 0) && 
       (nodes != undefined && nodes.length > 0)){
        for(const edgedto of edgeDTOs){
            if(edgedto.toNodeId == nodeid){
                const myEdge = findObjectById(edgedto.id, edges)
                myEdge.setFromNode(findObjectById(edgedto.fromNodeId, nodes));
                myEdge.setToNode(findObjectById(edgedto.toNodeId, nodes));
                if(!myEdge.label){
                    myEdge.setLabel('ei' + ++globalCounters.edges);
                } 
                incomings.push(myEdge);
                //console.log(myEdge.printMe());
            }
        }
    }
    return incomings;
}

function populateEdgeData(edgeid){
    if(edgeid != undefined){
        return globalData.createEdgeDataById(edgeid);
    } else {
        console.log(edgeid, "LOG edgeid in populateEdgeData()"); 
    }
}

class DescriptionData{
    constructor(descid, text){
        this.id = descid;
        this.text = text;
    }
}

class PictureData{
    constructor(picture, width){
        this.id = picture.id;
        this.imagesrc = picture.imagesrc;
        this.width = width;
    }
}

class InventoryElementData{
    constructor(elemid, text){
        this.id = elemid;
        this.text = text;
    }
}

class EdgeData{
    constructor(edgedto, description, inventoryelements, pictures){
        this.id = edgedto.id;
        this.label = edgedto.label;
        this.time1 = edgedto.time1;
        this.time2 = edgedto.time2;
        this.description = description;
        this.pictures = pictures;
        this.inventory = inventoryelements;
    }
    addInventoryElement(element){
        this.inventory.push(element);
    }
    removeInventoryElementById(elemid){
        this.inventory = getListAfterRemovalById(elemid, this.inventory);
    }
    addPicture(element){
        this.pictures.push(element);
    }
    removePicture(elemid){
        this.pictures = getListAfterRemovalById(elemid, this.pictures);
    }
    addDescription(element){
        this.description.push(element);
    }
    removeDescription(elemid){
        this.description = getListAfterRemovalById(elemid, this.description);
    }
}

class NodeDTO{
    constructor(id, x, y, label, isfinish, isentry){
        this.X = x;
        this.Y = y;
        this.id = id;
        this.isFinish = isfinish;
        this.isEntry = isentry;
        this.label = label;
        this.outgoingEdges = [];
    }
	printMe(){
		const txt = "Id:" + this.id + " Label:" + this.label;
		return txt;
	}
}

class EdgeDTO{
    constructor(id, fromnodeid, tonodeid, label, descid){
        this.id = id;
        this.fromNodeId = fromnodeid;
        this.toNodeId = tonodeid;
        this.label = label;
        this.time1 = 0;
        this.time2 = 0;
        this.descriptionId = descid;
        this.pictureIds = [];
        this.inventoryElementIds = [];
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
    getLabel(){
        return this.label;
    }   
}

class GraphFactory {
    constructor(name){
        this.name = name;
        this.nodeDTOs = [];
        this.edgeDTOs = [];
        this.edges = [];
        this.nodes = [];
        this.Graph = new Graph(name);
    }
    async setNodeDTOs(nodeDTOs){
        this.nodeDTOs = nodeDTOs;
        this.nodes = createNodesFromNodeDTOs(this.nodeDTOs);
    }
    async setEdgeDTOs(myedgeDTOs){
        this.edgeDTOs = myedgeDTOs;
        if(this.edgeDTOs != undefined && this.edgeDTOs.length > 0){
            for(const item of this.edgeDTOs){
                const fromnode = getElementById(item.fromNodeId, this.nodes);
                const tonode = getElementById(item.toNodeId, this.nodes);
                const myEdge = new Edge(item.id, fromnode, tonode, item.label);
                this.edges.push(myEdge);
            }
           //this.edges = createEdgesFromEdgeDTOs(this.edgeDTOs, this.nodes);
        }
    }
    async constructGraph(){
        if(this.nodeDTOs.length > 0){
            for(const mynode of this.nodes){
                mynode.setOutgoings(findOutgoingsToNodeDTO(mynode.id,
                     this.edgeDTOs, this.nodes, this.edges));
                mynode.setIncomings(findIncomingsToNodeDTO(mynode.id,
                     this.edgeDTOs, this.nodes, this.edges));
                this.Graph.addNode(mynode);
            }
        }
        return this.Graph;
    }
    addNodeDTO(nodeDTO){
        if(nodeDTO != undefined){
            this.nodeDTOs = addElementToList(nodeDTO, this.nodeDTOs);
        }
    }
    addEdgeDTO(edgeDTO){
        if(edgeDTO != undefined){
            this.edgeDTOs = addElementToList(edgeDTO, this.edgeDTOs);
        }
    }
    printMe(){
        console.log(this.name + ': ');
        for(const elem of this.nodeDTOs){
            elem.printMe();
        }
    }
}


class GlobalData{
    constructor(edgedtos, orphandescriptionids, orphaninvelemids, orphanpictureids){
        this.GraphEdgeDatas = edgedtos;
        this.SelectedEdgeData = {};
        this.Descriptions = [];
        this.OrphanDescriptionIds = orphandescriptionids;
        this.InventoryElements = [];
        this.OrphanInventoryElementIds = orphaninvelemids;
        this.Pictures = [];
        this.OrphanPictureIds = orphanpictureids;
        this.MapInvElemIdToEdgeIds = [];
        this.MapPicIdToEdgeIds = [];
    }
    populateMapXXXToEdgeIds(){
        for(const e of this.GraphEdgeDatas){
            if(e.inventoryElementIds != undefined 
                && e.inventoryElementIds.length > 0){
                for(const ieid of e.inventoryElementIds){
                    let entry = new XXXMapEntry(ieid);
                    entry.addEdgeId(e.id);
                    if(!isElementOfListById(entry, this.MapInvElemIdToEdgeIds)){
                        this.MapInvElemIdToEdgeIds.push(entry);
                    }
                    else {
                        let myentry = getElementById(entry.id, this.MapInvElemIdToEdgeIds)
                        myentry.addEdgeId(e.id);
                    }
                }
            }
            if(e.pictureIds != undefined && e.pictureIds.length > 0){
                e.pictureIds.forEach(picid => {
                    let entry = new XXXMapEntry(picid);
                    entry.addEdgeId(e.id);
                    if(!isElementOfListById(entry, this.MapPicIdToEdgeIds)){
                        this.MapPicIdToEdgeIds.push(entry);
                    }
                    else {
                        let myentry = getElementById(entry.id, this.MapPicIdToEdgeIds)
                        myentry.addEdgeId(e.id);
                    }
                });
            }
        }
    }    
    isExistEdgeLabel(edgelabel){
        if(edgelabel != undefined && edgelabel.length > 0){
            const len = this.GraphEdgeDatas.length; 
            let k = 0;
            let label = this.GraphEdgeDatas[k++].label; 
            while(k < len && label != edgelabel){
                label = this.GraphEdgeDatas[k++].label;
            }
            return k < len;
        } else
            return true;
    }
    async getPicData(pictureid){
        return getElementById(pictureid, this.Pictures);
    }
    getDescData(descriptionid){
        return getElementById(descriptionid, this.Descriptions);
    }
    getInvElemData(inventoryelementid){
        return getElementById(inventoryelementid, this.InventoryElements);
    }
    getEdgeDTO(edgeid){
        return getElementById(edgeid, this.GraphEdgeDatas);
    }
    changeDescriptionById(entityid, newtext){
        let desc = getElementById(entityid, this.Descriptions);
        if(desc != undefined){
            desc.text = newtext;    
        }
        else {
            console.log("GlobalData.changeDescriptionById() entityid:" + entityid);
            console.log("GlobalData.changeDescriptionById() newtext:" + newtext);
            
            const newdesc = new DescriptionData(entityid, newtext);
            this.addElementToXXXList(newdesc, "descs");            
        }
    }
    changeInventoryElementById(entityid, newtext){
        let invelem = getElementById(entityid, this.InventoryElements);
        invelem.text = newtext;
    }
    addElementToXXXList(element, listnameabbr){
        switch (listnameabbr.toLowerCase()) {
            case "descs":
                if(!isElementOfListById(element, this.Descriptions)){
                    this.Descriptions.push(element);
                }
                break;
            case "odids":
                if(!isElementOfList(element, this.OrphanDescriptionIds)){
                    this.OrphanDescriptionIds.push(element);
                }
                break;
            case "invelems":
                if(!isElementOfListById(element, this.InventoryElements)){
                    this.InventoryElements.push(element);
                }
                break;
            case "oiids":
                if(!isElementOfList(element, this.OrphanInventoryElementIds)){
                    this.OrphanInventoryElementIds.push(element);
                }
                break;    
            case "pics":
                if(!isElementOfListById(element, this.Pictures)){
                    this.Pictures.push(element);
                }
                break;
            case "opids":
                if(!isElementOfList(element, this.OrphanPictureIds)){
                    this.OrphanPictureIds.push(element);
                }
                break;
            case "edgedtos":
                if(!isElementOfListById(element, this.GraphEdgeDatas)){
                    this.GraphEdgeDatas.push(element);
                }
                break;
            default:
                break;
        }           
    }   
    removeXXXById(elementid, listnameabbr){
        switch (listnameabbr.toLowerCase()) {
            case "odids":
                removeElementFromList(elementid, this.OrphanDescriptionIds);
                break;
            case "descs":
                removeElementFromList(elementid, this.Descriptions);
                break;
            case "oiids":
                removeElementFromList(elementid, this.OrphanInventoryElementIds);
                break;
            case "invelems":
                removeElementFromList(elementid, this.InventoryElements);
                break;    
            case "opids":
                removeElementFromList(elementid, this.OrphanPictureIds);
                break;
            case "pics":
                removeElementFromList(elementid, this.Pictures);
                break;
            case "edgedtolist":
                removeElementFromList(elementid, this.GraphEdgeDatas);              
                break;
            default:
                break;
        }
    }
    removeEdgeDataById(edgeid){
        let edgedto = getElementById(edgeid, this.GraphEdgeDatas);
        this.addElementToXXXList(edgedto.descriptionId, "odids");
        if(edgedto.inventoryElementIds != undefined 
            && edgedto.inventoryElementIds.length > 0){
            edgedto.inventoryElementIds.forEach(ieid => {
                let myentry = getElementById(ieid, this.MapInvElemIdToEdgeIds);
                myentry.removeEdgeId(edgeid);
                this.addElementToXXXList(ieid, "oiids");
            });
        }
        if(edgedto.pictureIds != undefined && edgedto.pictureIds.length > 0){
            edgedto.pictureIds.forEach(picid => {
                let myentry = getElementById(picid, this.MapPicIdToEdgeIds);
                myentry.removeEdgeId(edgeid);
                this.addElementToXXXList(picid, "opids");
            });
        }
        if(edgedto.descriptionId != undefined){
            this.addElementToXXXList(edgedto.descriptionId, "odids");
        }
        this.removeXXXById(edgeid, "edgedtolist");
        if(this.SelectedEdgeData.id == edgeid){
            //check out if an object can be deleted in JS!!!!!
            //if so, delete it here

            const myedgedto = this.GraphEdgeDatas[0];
            const edgedata = new EdgeData(myedgedto, 
                getElementById(myedgedto.descriptionId, this.Descriptions), 
                getElementsByIds(myedgedto.inventoryElementIds, this.InventoryElements),
                getElementsByIds(myedgedto.pictureIds, this.Pictures)
                );
            this.SelectedEdgeData = edgedata;
        }
    }
    async addEdgeDTO(edgeid, fromnodeid, tonodeid, descid){
        let myedge  = getElementById(edgeid, await globalGraph.graph.getEdges())
        console.log("GlobalData.addEdgeDTO was called. myedge:");
        console.log(myedge)
        const myedgedto = {
            id: edgeid,
            fromNodeId: fromnodeid,
            toNodeId: tonodeid,
            label: myedge.label,
            descriptionId : descid,
            pictureIds : [], 
            inventoryElementIds : [],
            time1 : 1,
            time2 : 1
        }
        this.addElementToXXXList(myedgedto,"edgedtos");
    }
   async appendDescToEdge(entity, edgeid){
        const myentity = await entity;
        const myedgeid = await edgeid;
        this.addElementToXXXList(myentity, "descs");
        const myedgedto = await this.getEdgeDTO(myedgeid);
        if(myedgedto){
            myedgedto.descriptionId = myentity.id;
            console.log("myedgedto after adding description:");
            console.log(myedgedto);
        }
    }
    async appendInvElemToEdge(entity, edgeid){
        const myentity = await entity;
        const myedgeid = await edgeid;
        this.addElementToXXXList(myentity, "invelems");
        const myedgedto = await this.getEdgeDTO(myedgeid);
        if(myedgedto){
            let foundfield = false;
            for(const field in myedgedto){
                if(field == "inventoryElementIds"){
                    foundfield = true;
                    break;
                }
            }
            if(!foundfield){
                myedgedto.inventoryElementIds = [];
            }
            if(myedgedto.inventoryElementIds){
                myedgedto.inventoryElementIds.push(myentity.id);
            }
            const myentry = new XXXMapEntry(myentity.id);
            myentry.addEdgeId(myedgeid);
            this.MapInvElemIdToEdgeIds.push(myentry);
            console.log("myedgedto after adding invelem:");
            console.log(myedgedto);
        }
    }
    async appendPicToEdge(entity, edgeid){
        const myentity = await entity;
        if(myentity){
            const myentityid = myentity.id;
            const myedgeid = await edgeid;
            this.addElementToXXXList(myentity, "pics");
            const myedgedto = await this.getEdgeDTO(myedgeid);
            if(myedgedto){
                let foundfield = false;
                for(const field in myedgedto){
                    if(field == "pictureIds"){
                        foundfield = true;
                        break;
                    }
                }
                if(!foundfield){
                    myedgedto.pictureIds = [];
                }
                myedgedto.pictureIds.push(myentityid);
                const myentry = new XXXMapEntry(myentityid);
                myentry.addEdgeId(myedgeid);
                this.MapPicIdToEdgeIds.push(myentry);
                console.log("myedgedto after adding pic:");
                console.log(myedgedto);
            }
        }
        
    }    
    attachPicToEdge(edgeid, myentityid){
        let entityid = undefined;
        if(myentityid != undefined){
            entityid = parseInt(myentityid);
        }
        const edgedto = getElementById(edgeid, this.GraphEdgeDatas);
        if(edgedto != undefined){
            if(edgedto.pictureIds != undefined){
                if(!edgedto.pictureIds.includes(entityid)){
                    edgedto.pictureIds.push(entityid);
                }
            } else {
                edgedto.pictureIds = [];
                edgedto.pictureIds.push(entityid);
            }
            let element = getElementById(entityid, this.MapPicIdToEdgeIds);
            if(element != undefined){
                element.addEdgeId(edgeid);
            } else {
                let entry = new XXXMapEntry(entityid);
                entry.addEdgeId(edgeid);
                this.MapPicIdToEdgeIds.push(entry);
            }
            this.removeXXXById(entityid, "opids"); 
        }
       
    }
    attachInvElemToEdge(edgeid, myentityid){
        let entityid = undefined;
        if(myentityid != undefined){
            entityid = parseInt(myentityid);
        }
        const edgedto = getElementById(edgeid, this.GraphEdgeDatas);
        if(edgedto != undefined){
            if(edgedto.inventoryElementIds != undefined){
                if(!isElementOfList(entityid, edgedto.inventoryElementIds)){
                    edgedto.inventoryElementIds.push(entityid);
                }
            } else {
                edgedto.inventoryElementIds = [];
                edgedto.inventoryElementIds.push(entityid);
            }
            const element = getElementById(entityid, this.MapInvElemIdToEdgeIds);
            if(element != undefined){
                element.addEdgeId(edgeid);
            } else {
                const entry = new XXXMapEntry(entityid);
                entry.addEdgeId(edgeid);
                this.MapInvElemIdToEdgeIds.push(entry);
            }
            this.removeXXXById(entityid, "oiids"); 
        } 
    }
    attachDescToEdge(edgeid, myentityid, isAttachWithOldRemove){
        let entityid = undefined;
        if(myentityid != undefined){
            entityid = parseInt(myentityid);
        }
        const edgedto = getElementById(edgeid, this.GraphEdgeDatas);
        console.log("in attachDescToEdge() edgeid: " + edgeid + ", desc id: " + entityid + ")");
        if(edgedto != undefined){
            if(edgedto.descriptionId == undefined || isAttachWithOldRemove){
                edgedto.descriptionId = entityid;    
            }
            else {
                this.addElementToXXXList(edgedto.descriptionId, "odids");
                edgedto.descriptionId = entityid;
            }
            this.removeXXXById(entityid, "odids");
        } 
    }
    
    detachPicFromEdge(edgeid, myentityid){
        let entityid = undefined;
        if(myentityid != undefined){
            entityid = parseInt(myentityid);
        }
        const edgedto = getElementById(edgeid, this.GraphEdgeDatas);
        if(edgedto != undefined){
            if(edgedto.pictureIds != undefined && edgedto.pictureIds.length > 0){
                removeElementFromList(entityid, edgedto.pictureIds);
                this.addElementToXXXList(entityid, "opids");
                let entry = getElementById(entityid, this.MapPicIdToEdgeIds);
                if(entry != undefined){
                    entry.removeEdgeId(edgeid);
                    if(entry.edgeids.length == 0){
                        removeElementFromList(entry, this.MapPicIdToEdgeIds);
                    }
                }
            } 
        } 
    }
    detachInvElemFromEdge(edgeid, myentityid){
        let entityid = undefined;
        if(myentityid != undefined){
            entityid = parseInt(myentityid);
        }
        const edgedto = getElementById(edgeid, this.GraphEdgeDatas);
        if(edgedto != undefined){
            if(edgedto.inventoryElementIds != undefined && 
                            edgedto.inventoryElementIds.length > 0){
                removeElementFromList(entityid, edgedto.inventoryElementIds);
                this.addElementToXXXList(entityid, "oiids");
                let entry = getElementById(entityid, this.MapInvElemIdToEdgeIds);
                if(entry != undefined){
                    entry.removeEdgeId(edgeid);
                    if(entry.edgeids.length == 0){
                        removeElementFromList(entry, this.MapInvElemIdToEdgeIds);
                    }
                }
            } 
        } 
    }
    detachDescFromEdge(edgeid){
        let edgedto = getElementById(edgeid, this.GraphEdgeDatas);
        if(edgedto != undefined){
            if(edgedto.descriptionId != undefined){
                this.addElementToXXXList(edgedto.descriptionId, "odids");
                edgedto.descriptionId = undefined;    
            }
        } 
    }
    deleteEdgeDTOWithDetachAll(edgeid){
        let detachingobject = {
            descid : null,
            picids : [],
            ieids : []
        };
        const edgedto = getElementById(edgeid, this.GraphEdgeDatas);
        if(edgedto){
            // detach description from viewmodel
            const descid = edgedto.descriptionId;  
            const desc = findObjectById(descid, this.Descriptions);
            if(descid && desc && 
                    (desc.text != undefined && desc.text !== globalEMPTYDESC)){
                detachingobject.descid = descid;
                this.addElementToXXXList(edgedto.descriptionId, "odids");
            }
            // detach pictures from viewmodel
            const picids = edgedto.pictureIds;
            if(picids && picids.length > 0){
                for(const entityid of picids){
                    detachingobject.picids.push(entityid);
                    this.addElementToXXXList(entityid, "opids");
                    let entry = getElementById(entityid, this.MapPicIdToEdgeIds);
                    if(entry != undefined){
                        entry.removeEdgeId(edgeid);
                        if(entry.edgeids.length == 0){
                            removeElementFromList(entry, this.MapPicIdToEdgeIds);
                        }
                    }
                }
            }
            // detach inventoryelements from viewmodel
            const ieids = edgedto.inventoryElementIds;
            if(picids && picids.length > 0){
                for(const entityid of ieids){
                    detachingobject.ieids.push(entityid);
                    this.addElementToXXXList(entityid, "oiids");
                    let entry = getElementById(entityid, this.MapInvElemIdToEdgeIds);
                    if(entry != undefined){
                        entry.removeEdgeId(edgeid);
                        if(entry.edgeids.length == 0){
                            removeElementFromList(entry, this.MapInvElemIdToEdgeIds);
                        }
                    }
                }
            }
            removeElementFromList(edgedto, this.GraphEdgeDatas);
        }
        return detachingobject;
    }
    deleteEdgeDTOWithRemoveAll(edgeid){
        let removingobject = {
            descid : null,
            picids : [],
            ieids : []
        };
        const edgedto = getElementById(edgeid, this.GraphEdgeDatas);
        if(edgedto){
            // remove description from viewmodel
            const descid = edgedto.descriptionId;  
            const desc = findObjectById(descid, this.Descriptions);
            if(descid && desc && 
                    (desc.text != undefined && desc.text !== globalEMPTYDESC)){
                removingobject.descid = descid;
                this.removeXXXById(edgedto.descriptionId,"descs")
            }
            // delete pictures from viewmodel
            const picids = edgedto.pictureIds;
            if(picids && picids.length > 0){
                for(const entityid of picids){
                    removingobject.picids.push(entityid);
                    this.removeXXXById(entityid, "pics");
                    let entry = getElementById(entityid, this.MapPicIdToEdgeIds);
                    if(entry != undefined){
                        entry.removeEdgeId(edgeid);
                        if(entry.edgeids.length == 0){
                            removeElementFromList(entry, this.MapPicIdToEdgeIds);
                        }
                    }
                }
            }
            // delete inventoryelements from viewmodel
            const ieids = edgedto.inventoryElementIds;
            if(ieids && ieids.length > 0){
                for(const entityid of ieids){
                    removingobject.ieids.push(entityid);
                    this.removeXXXById(entityid, "invelems");
                    let entry = getElementById(entityid, this.MapInvElemIdToEdgeIds);
                    if(entry != undefined){
                        entry.removeEdgeId(edgeid);
                        if(entry.edgeids.length == 0){
                            removeElementFromList(entry, this.MapInvElemIdToEdgeIds);
                        }
                    }
                }
            }
            removeElementFromList(edgedto, this.GraphEdgeDatas);
        }
        return removingobject;
    }

    createEdgeDataById(edgeid){
        console.log("createEdgeDataById(" + edgeid + "):");
        const edgedto = getElementById(edgeid, this.GraphEdgeDatas);
        let edgedata = {};
        if(edgedto){
            console.log(edgedto, "LOG edgedto");
            edgedata = new EdgeData(edgedto, 
                getElementById(edgedto.descriptionId, this.Descriptions), 
                getElementsByIds(edgedto.inventoryElementIds, this.InventoryElements),
                getElementsByIds(edgedto.pictureIds, this.Pictures)
            );
        } 
        else {
            
        }
        this.SelectedEdgeData = edgedata;
        return edgedata;
    }
}

class XXXMapEntry {
    constructor(elemid){
        this.id = elemid;
        this.edgeids = [];
    }
    addEdgeId(edgeid){
        if(!isElementOfList(edgeid, this.edgeids)){
            this.edgeids.push(edgeid);
        }
    }
    removeEdgeId(edgeid){
        removeElementFromList(edgeid, this.edgeids);
    }
}
class ParamDTO{
    constructor(email, mainentityid, otherentityid, mytextparam){
        this.paramdto = {
            useremail : email,
            entityid : mainentityid,
            componentid : otherentityid == undefined ? null : otherentityid,
            textparam : mytextparam == undefined ? null : mytextparam
        };
    }
    setUseremail(useremail){
        this.paramdto.useremail = useremail;
    }
    setEntityid(id){
        this.paramdto.entityid = id; 
    }
    setComponentid(id){
        this.paramdto.componentid = id; 
    }
    setText(text){
        this.paramdto.textparam  = text;
    }
    getDTO(){
        return this.paramdto;
    }
}