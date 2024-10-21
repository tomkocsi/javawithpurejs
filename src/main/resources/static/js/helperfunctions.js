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
function getListAfterRemoval(element, elements){
    const mylist = [];
    if(element && elements != undefined && elements.length > 0){
        elements.forEach(elem => {
            if(elem.id != element.id){
                mylist.push(elem);
            }
        }); 
    }
    return mylist;
}
function removeElementById(id, elements){
    const mylist = [];
    if(id && id != "" && elements != undefined && elements.length > 0){
        elements.forEach(elem => {
            if(elem.id != id){
                mylist.push(elem);
            }
        }); 
    }
    return mylist;
}
function isElementOfListById(element, elements){
    if(element && elements != undefined && elements.length > 0){
        const len = elements.length; 
        let k = 0;
        while (k < len && element.id != elements[k].id){
            ++k;
        }
        return k < len;
    }
    return false;
}
function isElementOfList(element, elements){
    if(element && element != "" && elements != undefined && 
        elements.length > 0){
        const len = elements.length; 
        let k = 0;
        while (k < len && element != elements[k]){
            ++k;
        }
        return k < len;
    }
    return false;
}
function removeElementFromList(element, elements){
    //console.log(" in helperfunctions > removeElementFromList(...)");
    //console.log("before removing" + element.id + " from list elements: ");
    //console.log(elements);
    if (element && element != "" && elements != undefined){
        for (let index = 0; index < elements.length; index++) {
            if(elements[index] === element){
                elements.splice(index, 1);
            }
        }
    }
    //console.log("after removing " + element.id + " from elements: ");
    //console.log(elements);
}
function getListAfterRemovalById(elementid, elements){
    const mylist = [];
    if(elementid && elements != undefined && elements.length > 0){
        elements.forEach(elem => {
            if(elem.id != elementid && elem.id != ("" + elementid)){
                mylist.push(elem);
            }
        }); 
    }
    return mylist;
}
function getElementById(elementid, elements){
    const mylist = elements;
    if(elementid && elementid != "" && elements.length > 0){
        const len = mylist.length; 
        let k = 0;
        if(elements[k].id != undefined){
            let found = false;
            while (k < len && !found){
                if(mylist[k].id){
                    if(elementid === mylist[k].id || 
                            elementid.toString() === mylist[k].id.toString()){
                        found = true;
                    } else {
                        ++k;
                    }
                } else {
                    ++k;
                }
            }
        } else {
            while (k < len && (elementid !== mylist[k] &&
                elementid.toString() !== mylist[k].toString())){
                ++k;
            }
        }
        if (k == len){
            return null;
        }
        else {
            return mylist[k];
        }
    } else {
        return null;
    }
}
function getElementsByIds(elementids, elements){
    let resultlist = [];
    if(elementids != undefined && elementids.length > 0 && 
            elements != undefined && elements.length > 0){
        const mylist = elements;
        const len = mylist.length;
        elementids.forEach(elemid =>{
            let k = 0;
            while (k < len && elemid != mylist[k].id){
                ++k;
            }
            if (k < len){
                resultlist.push(mylist[k]);
            }
        });
    }
    return resultlist; 
}
function findObjectById(myobjectid, myobjects){
    const len = myobjects.length;
    let j = 0;
    while(j < len && myobjects[j].id != myobjectid){
        j++;
    }
    if(j == len)
        return null;
    else
        return myobjects[j];
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
            return null;
        }
        else {
            return mylist[k];
        }
    }
}
function getCreatedNodeId(){
    globalNodeCounter++;
    return 'n' + globalNodeCounter;    
}
function isInsideRect(checkX, checkY, ULeftX, ULeftY, DRightX, DRightY){
    return (
        checkX > ULeftX && checkX < DRightX && 
        checkY > ULeftY && checkY < DRightY
    );
}
function pointerCursor() {
    document.body.style.cursor = 'pointer';
}
function defaultCursor() {
    document.body.style.cursor = 'default';
}
function fireClick(node){
    if (document.createEvent) {
        var evt = document.createEvent('MouseEvents');
        evt.initEvent('click', true, false);
        node.dispatchEvent(evt);    
    } else if (document.createEventObject) {
        node.fireEvent('onclick') ; 
    } else if (typeof node.onclick == 'function') {
        node.onclick(); 
    }
}
function getEntityIdFromDOMId(pretext, DOMId){
    if(pretext != undefined && DOMId != undefined){
        return DOMId.substring(pretext.length, DOMId.length);
    }
    return null;
}
function printWithAllChildren(DOMelement){
    console.log("DOMelement in printWithAllChildren()");
    console.log(DOMelement);
    
    let txt = "";
    let childtxt = "";
    let parenttxt = "";
    let explored = new QueueStack();
    let tobeexplore = new QueueStack();

    tobeexplore.push(DOMelement);
    while(!tobeexplore.isEmpty()){
        const myelem = tobeexplore.pop();
        if(myelem != null && myelem != undefined){
            parenttxt = "";
            parenttxt += myelem.nodeName;
            if(myelem["getAttribute"] != undefined){
                parenttxt += "(";
                parenttxt += (myelem.getAttribute("id") == undefined) ?
                         "" :
                         "" + myelem.getAttribute("id");
                parenttxt += ")";
            }
            if(myelem.childNodes != undefined && myelem.childNodes.length > 0){
                childtxt = "";
                myelem.childNodes.forEach(childelem => {
                    tobeexplore.push(childelem);
                    if(!explored.contains(childelem)){
                        childtxt += "\n    -> " + childelem.nodeName;
                        childtxt += "(";
                        childtxt += (childelem["getAttribute"] == undefined) ? 
                            "" : 
                            "" + childelem.getAttribute("id");
                        childtxt += ")";
                        explored.push(childelem);
                    }
                });
                txt += "\n+ " + parenttxt + childtxt;
            }
        }
        
    }
    console.log(txt);
}
function isSaveAble(mygraph){
    // Graph can be persisted on server if
    // no loops, exists path from entry node to finish node
    // all nodes connected
    const isallconnected = isAllNodesConnected(mygraph);

}
function findEdgeByNodeIds(fromnodeid, tonodeid, graph){
    if(graph.getEdges().length > 0){
        for(const edge of graph.getEdges()){
            if(Number(edge.fromNode.id) == Number(fromnodeid) && Number(edge.toNode.id) == Number(tonodeid)){
                return edge;
            }
        }
    } else 
        return null; 
}

function isAllNodesConnected(mygraph){
    const nodes = mygraph.getNodes();
    const nodeslength = nodes.length; 
    const edges = mygraph.getEdges(); 
    const edgeslength = edges.length;
    let allconnected = true;
    let j = 0;
    while(j < nodeslength && allconnected){
        let k = 0;
        while(k < edgeslength && !isNodeInEdge(nodes[j], edges[k])){
            k++;
        }
        allconnected = k < len;
        j++;
    }
    return allconnected;
}
function isNodeInEdge(node, edge){
    return (edge.getFromNode() === node || edge.getToNode() === node);
}
function countHopsBetween2Nodes(node1, node2, mygraph){
    const paths = new Set();
}
function getNeighbors(node, mygraph){
    let neighbors = [];
    if(node.getOutgoings() != undefined && node.getOutgoings().length > 0){
        node.getOutgoings().forEach(edge => {
            addElementToList(edge.getToNode(), neighbors);
        });    
    }
    /*
    if(node.getIncomings() != undefined && node.getIncomings().length > 0){
        node.getIncomings().forEach(edge => {
            addElementToList(edge.getFromNode(), neighbors);
        });
    }
    */
}
function isOptionIncluded(text, DOMselectid){
    const options = document.getElementById(DOMselectid).options;
    let idx = 0;
    while(idx < options.length - 1 && options[idx].firstChild.data != text){
        idx++;
    }
    if(idx >= options.length - 1){
        return false;
    } else {
        return true;
    }
}
function appendOption(text, value, DOMselectid){
    const option = document.createElement("OPTION");
    const select = document.getElementById(DOMselectid);
    option.value = value;
    option.text = text;
    select.add(option);
     
}
function removeOption(text, value, DOMselectid){
    const select = document.getElementById(DOMselectid);
    const len = select.options.length;
    let idx = 0;
    while(idx < len && select.options[idx].firstChild.data != text){
            idx++;
    }
    if(idx < len && select.options[idx].value == value){
        select.remove(idx);
    }
}

function hasChildByIdStaringWith(startingwith, parent){
    if(parent && parent.children && parent.children.length > 0){
        let k = 0;
        while(k < parent.children.length &&
                parent.children[k].getAttribute("id").lastIndexOf(startingwith) != 0){
            k++;
        }
        return k < parent.children.length;
    }
    return false;
}
function removeChildByIdStaringWith(startingwith, parent){
    if(parent && parent.children && parent.children.length > 0){
        let k = 0;
        let elements = [];
        let count = 0;
        while(k < parent.children.length){
            if(parent.children[k].getAttribute("id").lastIndexOf(startingwith) == 0){
                elements.push(parent.children[k]);
                count++;
            }    
            k++;
        }
        let j = 0
        while(j < count){
            parent.removeChild(elements[j++]);
        }
        return count;
    }
    return 0;
}

function testQueueStack(){
    let myarray = [];
    for (let index = 0; index < 10; index++){
        myarray[index] = "num" + Math.floor(Math.random()*1000);
    }
    let testQueue = new QueueStack();
    let testStack = new QueueStack();
    
    console.log("testQueue: " + testQueue.printMe());
    console.log("testStack: " + testStack.printMe());
    for (let k = 0; k < myarray.length; k++) {
        testQueue.offer(myarray[k]);
        testStack.push(myarray[k]);
    }
    console.log("testQueue: " + testQueue.printMe());
    console.log("testStack: " + testStack.printMe());
    for (let k = 0; k < myarray.length/2; k++) {
        console.log(" testQueue.poll(): " + testQueue.poll() + 
        " size: " + testQueue.size());
        console.log(" testStack.pop(): " + testStack.pop() + 
        " size: " + testStack.size());
    }
    console.log("testQueue: " + testQueue.printMe());
    console.log("testStack: " + testStack.printMe());
    testQueue.offer("test1");
    testStack.push("test2");
    console.log("testQueue: " + testQueue.printMe() + " size: " + testQueue.size());
    console.log("testStack: " + testStack.printMe() + " size: " + testStack.size());
    for (let j = 0; j < 10; j++) {
        console.log("" + j + ". iteráció:");
        console.log("testQueue: " + testQueue.printMe());
        console.log("testQueue.poll():" + testQueue.poll() +
             " size: " + testQueue.size());
        console.log("testQueue: " + testQueue.printMe());     
        console.log("*********************");
        console.log("testStack: " + testStack.printMe());
        console.log("testStack.pop():" + testStack.pop() +
             " size: " + testStack.size());
        console.log("testStack: " + testStack.printMe());
    }
    console.log("végeredmény: ");
    console.log("testQueue: " + testQueue.printMe());
    console.log("testStack: " + testStack.printMe());
    console.log("method  contains() test: ");
    testStack.clear();
    console.log("testStack.contains('test7') " + testStack.contains('test7'));
    console.log("testStack.push('test6')");
    testStack.push("test6");
    console.log("testStack.contains('test7') " + testStack.contains('test7'));
    console.log("testStack.contains('test6') " + testStack.contains("test6"));
}



class QueueStack {
    constructor(){
        this.head = null;
        this.tail = null;
        this.first = null;
        this.last = null;
        this.count = 0;
    }
    clear(){
        this.head = null;
        this.tail = null;
        this.first = null;
        this.last = null;
        this.count = 0;
    } 
    offer(mycontent){
        // offer does same as push
        // it is implemented to be syntactically compatible with Java Queue
        this.push(mycontent);
    }
    poll(){
        // retrieves last element's content and detach last element of linked list 
        if (this.count > 0){
            let content = this.last;
            let temp = this.tail.getBefore();
            if(temp != null && temp != undefined){
                temp.setNext(null);
                this.last = temp.getContent();
                this.tail = temp;
            } else {
                this.head = null;
                this.tail = null;
                this.last = null;
                this.first = null;
            }
            this.count--;
            return content;
        }
        return null;
    }
    push(mycontent){
        // inserts element before the first element of linked list
        if (this.count == 0){
            this.head = new LinkedElem(mycontent);
            this.first = mycontent;
            this.last = mycontent;
            this.tail = this.head; 
        } else {
            let newelem = new LinkedElem(mycontent);
            newelem.setNext(this.head);
            this.head.setBefore(newelem); 
            this.head = newelem;
            this.first = mycontent;
        }
        this.count++;
    }
    pop(){
        // retrieves first element's content and detach first element of linked list 
        if (this.count > 0){
            let content = this.first;
            let temp = this.head.getNext();
            if(temp != undefined && temp != null){
                temp.setBefore(null);
                this.first = temp.getContent();
                this.head = temp;
            } else {
                this.head = null;
                this.tail = null;
                this.last = null;
                this.first = null;
            }
            this.count--;
            return content;
        }
        return null;
    }
    size(){
        return this.count;
    }
    isEmpty(){
        return this.count == 0;
    }
    toArray(){
        let myarray = [];
        let temp = this.head;
        while(temp != undefined && temp !== null){
            myarray.push(temp.getContent());
            temp = temp.getNext();
        }
        return myarray;
    }
    contains(element){
        let temp = this.head;
        let k = 1; 
        while(k <= this.count && temp.getContent() != element){
            temp = temp.getNext();
            k++;
        }
        return k < this.count + 1;
    }
    printMe(){
        return this.toArray().join(" | ");
    }
}

class LinkedElem{
    constructor(mycontent){
        this.content = mycontent;
        this.next = null;
        this.before = null;
    }
    getContent(){
        return this.content;
    }
    getNext(){
        return this.next;
    }
    setNext(myreference){
        this.next = myreference; 
    }
    getBefore(){
        return this.before;
    }
    setBefore(myreference){
        this.before = myreference; 
    }
    hasNext(){
        return (this.next !== undefined && this.next !== null);
    }
    hasBefore(){
        return (this.before !== undefined && this.before !== null);
    }
}
function fillArrayWithKonvaElements(konvaelement, myarray){
    if(!myarray.includes(konvaelement)){
        myarray.push(konvaelement);
    }
    
    if(konvaelement.getChildren()){
        konvaelement.getChildren().forEach(elem =>{
            fillArrayWithKonvaElements(elem, myarray);
        });
    }
}
function getAllElementsOfKonvaLayer(layer){
    let myarray = [];
    fillArrayWithKonvaElements(layer, myarray);
    console.log("layer elements num : " + myarray.length);
    return myarray;   
}