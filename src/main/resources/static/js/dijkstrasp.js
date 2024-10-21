class GraphConverter2DG {
    constructor(graph, ispositiveweight){
        this.graph = graph;
        this.negate = !ispositiveweight;
        this.n = this.negate ? -1 : 1;
        this.dgraph1 = {};
        this.dgraph2 = {};
        for(const node of this.graph.getNodes()){
            this.dgraph1[node.id] = {};
            this.dgraph2[node.id] = {};
            if(node.getOutgoings() && node.getOutgoings().length > 0){
                const myobj1 = {};
                const myobj2 = {};
                for(const edge of node.getOutgoings()){
                    //cumulative!
                    myobj1[edge.toNode.id] = this.n * Number(globalData.getEdgeDTO(edge.id).time1);    
                    myobj2[edge.toNode.id] = this.n * Number(globalData.getEdgeDTO(edge.id).time2);    
                }
                this.dgraph1[node.id] = myobj1;
                this.dgraph2[node.id] = myobj2;
            }
        } 
    }
    
    shortestDistanceNode(distancemap, visited){
        //https://levelup.gitconnected.com/finding-the-shortest-path-in-javascript-dijkstras-algorithm-8d16451eea34
        //downloaded: 2021.12.06.
        let shortest = null;
        // for each node in the distances object
        for (const nodeid in distancemap) {
            // if no node has been assigned to shortest yet
            // or if the current node's distance is smaller than the current shortest
            let currentIsShortest = false;
            if(shortest === null){
                currentIsShortest = true;
            } else {  
                currentIsShortest = (distancemap[nodeid] < distancemap[shortest]);
            }
            if (currentIsShortest && !visited.includes(nodeid)) {
                shortest = nodeid;
            }
        }
        return shortest;
    }
    findShortestPath(dgraph, startnodeid, endnodeid){
        let distancemap = {}; // track distances from the start node using a hash object
        distancemap[endnodeid] = "Infinity";
        distancemap = Object.assign(distancemap, dgraph[startnodeid]);
        let tofrompairs = {endnodeid: null}; 
	    for (const tonodeid in dgraph[startnodeid]) {
		    tofrompairs[tonodeid] = startnodeid;
	    }
        let visited = [] // collect visited nodes
        let nodeid = this.shortestDistanceNode(distancemap, visited);
        while (nodeid) {
            let distance = distancemap[nodeid];
            let tonodes = dgraph[nodeid];	
            for (const tonodeid in tonodes) {
                if (String(tonodeid) === String(startnodeid)) {
                    continue;
                } else {
                    let newdistance = distance + tonodes[tonodeid];
                    if (!distancemap[tonodeid] || distancemap[tonodeid] > newdistance) {
                        distancemap[tonodeid] = newdistance; 
                        tofrompairs[tonodeid] = nodeid; // record the path
                    } 
                }
            }  
            visited.push(nodeid); // move the current node to the visited set
            nodeid = this.shortestDistanceNode(distancemap, visited); // move to the nearest neighbor node
        }
        let mypath = [endnodeid];
        let fromnodeid = tofrompairs[endnodeid];
        while (fromnodeid) {
            mypath.push(fromnodeid);
            fromnodeid = tofrompairs[fromnodeid];
        }
        mypath.reverse();
        let myedgepath = [];
        let j = 0;
        while(j < mypath.length - 1){
            const edge = findEdgeByNodeIds(mypath[j], mypath[++j], this.graph);
            myedgepath.push(edge);
        }
        const results = {
            "távolság" : this.n * distancemap[endnodeid],
            "csúcsok" : mypath.map(id =>{
                return this.graph.getNodeById(id).label;
            }),
            "élek" : myedgepath.map(e => {
                return e.id;
            })
        };
        return results;
    }
    printMe(dgraph){
        let str = "Graph as distances between node ids\n";
        const keys1 = Object.keys(dgraph);
        let k = 0;
        for(const node in dgraph){
            str += keys1[k++] + " : {";
            const keys2 = Object.keys(dgraph[node])
            let j = 0;
            for(const subnode in dgraph[node]){
                str += keys2[j++] + " : " + (this.n * (dgraph[node])[subnode]) + " perc, ";
            }
            str += "}\n";
        }
        return str; 
    }
}

function printDG(algotype){
    
    switch (algotype) {
        case "longest":
            const mygraphconv1 = new GraphConverter2DG(globalGraph.graph, false);
            alert(mygraphconv1.printMe(mygraphconv1.dgraph2));
            console.log(mygraphconv1.dgraph2, "LOG dijkstragraph - longest path");    
            break;
        case "shortest":
            const mygraphconv2 = new GraphConverter2DG(globalGraph.graph, true);
            alert(mygraphconv2.printMe(mygraphconv2.dgraph1));
            console.log(mygraphconv2.dgraph1, "LOG dijkstragraph - shortest path");        
            break;
        default:
            break;
    }
    
}

//ORIGINAL ALGORYTHM OF Noam Sauer-Utley
/*
class GraphConvOriginal {
    constructor(graph){
        this.graph = graph;
        this.dgraph1 = {};
        this.dgraph2 = {};
        for(const node of this.graph.getNodes()){
            this.dgraph1[node.id] = {};
            this.dgraph2[node.id] = {};
            if(node.getOutgoings() && node.getOutgoings().length > 0){
                const myobj1 = {};
                const myobj2 = {};
                for(const edge of node.getOutgoings()){
                    myobj1[edge.toNode.id] = globalData.getEdgeDTO(edge.id).time1;
                    myobj2[edge.toNode.id] = globalData.getEdgeDTO(edge.id).time2;
                }
                this.dgraph1[node.id] = myobj1;
                this.dgraph2[node.id] = myobj2;
            }
        } 
    }
    shortestDistanceNode(distancemap, visited){
        //https://levelup.gitconnected.com/finding-the-shortest-path-in-javascript-dijkstras-algorithm-8d16451eea34
        //downloaded: 2021.12.06.
        let shortest = null;
        // for each node in the distances object
        for (const nodeid in distancemap) {
            // if no node has been assigned to shortest yet
            // or if the current node's distance is smaller than the current shortest
            let currentIsShortest = (shortest === null || distancemap[nodeid] < distancemap[shortest]);
            // and if the current node is in the unvisited set
            if (currentIsShortest && !visited.includes(nodeid)) {
                // update shortest to be the current node
                shortest = nodeid;
            }
        }
        return shortest;
    }
    findShortestPath(dgraph, startnodeid, endnodeid){
        //https://levelup.gitconnected.com/finding-the-shortest-path-in-javascript-dijkstras-algorithm-8d16451eea34
        //downloaded: 2021.12.06.
        
        let distancemap = {}; // track distances from the start node using a hash object
        distancemap[endnodeid] = "Infinity";
        distancemap = Object.assign(distancemap, dgraph[startnodeid]);
        // create a hash object (child - parent) and 
        // populate with parents of startnode and endnode
        let childparentmap = {endnodeid: null}; 
	    for (const child in dgraph[startnodeid]) {
		    childparentmap[child] = startnodeid;
	    }
        let visited = [] // collect visited nodes
        // find the nearest node
        let nodeid = this.shortestDistanceNode(distancemap, visited);
        // for that node:
        while (nodeid) {
            // find its distance from the start node & its child nodes
            let distance = distancemap[nodeid];
            let children = dgraph[nodeid];	
            for (const child in children) {
                // make sure each child node is not the start node
                if (String(child) === String(startnodeid)) {
                    continue;
                } else {
                    // save the distance from the start node to the child node
                    let newdistance = distance + children[child];
                    // if there's no recorded distance from the start node to the child node in the distances object
                    // or if the recorded distance is shorter 
                    //than the previously stored distance from the start node to the child node
                    if (!distancemap[child] || distancemap[child] > newdistance) {
                        distancemap[child] = newdistance; 
                        childparentmap[child] = nodeid; // record the path
                    } 
                }
            }  
            visited.push(nodeid); // move the current node to the visited set
            nodeid = this.shortestDistanceNode(distancemap, visited); // move to the nearest neighbor node
        }
        // using the stored paths from start node to end node
        // record the shortest path
        let shortestPath = [endnodeid];
        let parent = childparentmap[endnodeid];
        while (parent) {
            shortestPath.push(parent);
            parent = childparentmap[parent];
        }
        shortestPath.reverse();
        const results = {
            distance: distancemap[endnodeid],
            path: shortestPath
        };
        return results;
    }
}
*/
