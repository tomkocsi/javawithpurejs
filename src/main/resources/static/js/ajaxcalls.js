async function getGraphDataListByUserFromServer(useremail, doWithResponse){
    const myuseremail = (useremail == undefined ? globalUser.email : useremail);
    const upON = {
        useremail : myuseremail
    }
    reqWithParamsUpJSONDown("/api/dataexchange/allgraphnamebyuser", upON, doWithResponse);
}
async function getGraphDataListFromServer(doWithResponse){
    await reqWithJSONDown("/api/dataexchange/allpublicgraphnames", doWithResponse);
}
async function isPublicGraphFromServer(upON, dWR){
    await reqWithParamsUpStringDown("/api/dataexchange/ispublicgraph", upON, dWR);
}
async function getGraphStringFromServer(graphid, doWithResponse){
    const upON = {
        entityid : graphid
    }
    await reqWithParamsUpStringDown("/api/dataexchange/graphstring", upON, doWithResponse);
}
async function updateGraphData(upON, doWithResponse){
    reqWithParamsUpJSONDown("/api/dataexchange/updategraphdata", upON, doWithResponse);
}
async function updateEdgeData(upON, doWithResponse){
    reqWithJSONUpDown("/api/dataexchange/updateedgedata", upON, doWithResponse);
}
async function deleteEdgeFromServer(upON, doWithResponse){
    await reqWithParamsUpStringDown("/api/dataexchange/removeedgedata", upON, doWithResponse);
}
async function deleteNodeFromServer(upON, doWithResponse){
    await reqWithParamsUpStringDown("/api/dataexchange/removenodedata", upON, doWithResponse);
}

async function getNewNodeDTOFromServer(paramup, doWithResponse){
    //AJAX call
    await reqWithJSONUpDown("/api/dataexchange/createnodedata", paramup, doWithResponse);
}
async function getNewEdgeDTOFromServer(upON, doWithResponse){
    
    reqWithJSONUpDown("/api/dataexchange/createedgedata", upON, doWithResponse);
    
}
async function deleteGraphFromServer(upON, dWR){
    const endpoint = "/api/dataexchange/deletegraph"
    await reqWithParamsUpStringDown(endpoint, upON, dWR);
}
async function updateNodeXY(paramup, doWithResponse){
    const upON = paramup;
    let func = doWithResponse;
    if(func === undefined){
        func = async (text) => {
            const output = await text;
            console.log(output, "LOG server response to updating node data");
        };
    }
    reqWithJSONUpStringDown("/api/dataexchange/updatenodedatalight", upON, func);
}
// not effective, write dedicated API endpoint method for array download 
async function getNodeDTOsByIds(nodeids){
    console.log("requesting nodes: " + nodeids.join(", ") + " from server");
    if(nodeids.length > 0){
        const dtos = [];
        let data = {};
        data.dto = null;
        for(const mynodeid of nodeids){
            const upON = {
                entityid : mynodeid
            }
            const doWithResponse = (json) => {
                data.dto = json;
                console.log(data.dto, "LOG_server's response - Node DTO");
            };
            await reqWithParamsUpJSONDown("api/dataexchange/getnodedata", upON, doWithResponse);
            if(data.dto){
                dtos.push(data.dto);
            }
        }
        return dtos;
    }
    return null;
}
function getNodeDTOsByGraphId(mygraphid, doWithResponse){
    const upON = {
        useremail: globalUser.email,
        entityid: mygraphid
    };
    const url = globalEndpoint.getServer + "/api/dataexchange/graphnodesbyid";
    const options = {
            method: "POST",
            headers: new Headers({'Content-Type': 'application/json'}),
            body: JSON.stringify(upON)
    }; 
    const request = async () => {
        const response = await fetch(url, options);
        const json = await response.json();
        doWithResponse(json);
    }
    request();
    //reqWithParamsUpJSONDown("/api/dataexchange/graphnodesbyid", upON, doWithResponse);
}
function getEdgeDTOsByGraphId(mygraphid, doWithResponse){
    console.log("requesting edgeDTOs to graph by id: " + mygraphid);
    const upON = {
        useremail: globalUser.email,
        entityid: mygraphid
    };
    const url = globalEndpoint.getServer + "/api/dataexchange/graphedges";
    const options = {
            method: "POST",
            headers: new Headers({'Content-Type': 'application/json'}),
            body: JSON.stringify(upON)
    }; 
    const request = async () => {
        const response = await fetch(url, options);
        const json = await response.json();
        doWithResponse(json);
    }
    request();

    //reqWithParamsUpJSONDown("/api/dataexchange/graphedges", upON, doWithResponse);
}
async function getOrphanAndGraphDTOByGraphId(mygraphid, doWithResponse){
    const upON = {
        useremail: globalUser.email,
        entityid: mygraphid
    };
    reqWithParamsUpJSONDown("/api/dataexchange/getorphanandgraphdto", upON, doWithResponse);
}
async function getNewDescFromServer(upON, doWithResponse){
    await reqWithParamsUpJSONDown("/api/dataexchange/createdescdata", upON, doWithResponse);
}
async function saveDescToServer(upON, doWithResponse){
    reqWithParamsUpJSONDown("/api/dataexchange/updatedescdata", upON, doWithResponse);
}
async function removeDescFromServer(upON, doWithResponse){
    reqWithParamsUpStringDown("/api/dataexchange/removedescdata", upON, doWithResponse);
}
async function getInvElemIdFromServer(upON, doWithResponse){
    reqWithParamsUpJSONDown("/api/dataexchange/createinvelemdata", upON, doWithResponse);
}
async function saveInvElemToServer(upON, doWithResponse){
   reqWithJSONUpStringDown("/api/dataexchange/updateinvelemdata", upON, doWithResponse);
}
async function removeInvElemFromServer(upON, doWithResponse){
    reqWithJSONUpStringDown("/api/dataexchange/removeinvelemdata", upON, doWithResponse);
 }
 async function removePicFromServer(upON, doWithResponse){
    reqWithJSONUpStringDown("/api/dataexchange/removepicture", upON, doWithResponse);
 }
async function detachXXXFromEdgeOnServer(upON, doWithResponse){
        const endpoint = "/api/dataexchange/detachcomponent";
        reqWithParamsUpStringDown(endpoint, upON, doWithResponse);
 }
async function detachAllFromEdgeIterated(upON, detachingobject, doWithResponse){
    const endpoint = "/api/dataexchange/detachcomponent";
    if(upON.entityid){
        const checkAllWentWell = async (wrap, dtobject, upon) => {
            if(dtobject.ieids && dtobject.ieids.length > 0){
                for(const id of dtobject.ieids){
                    const upONie = {
                        useremail: upon.useremail,
                        entityid: upon.entityid,
                        componentid: id,
                        textparam: "invelem" 
                    }
                    const dWR = async (json, obj) => {
                        const resp = await json;
                        if(resp != "OK"){
                            obj.allWentWell = false;
                            console.log(resp, 
                                "server's response detaching invelem(" + upONie.componentid + ")");
                        } else {
                            //console.log("OK detaching invelem");
                        }
                    }
                    console.log(upONie, "in detachAllFromEdgeIterated()");
                    reqWithParamsUpStringDown(endpoint, upONie, dWR, wrap);
                }
            }
            if(dtobject.picids && dtobject.picids.length > 0){
                for(const id of dtobject.picids){
                    const upONp = {
                        useremail: upon.useremail,
                        entityid: upon.entityid,
                        componentid: id,
                        textparam: "pic" 
                    }
                    const dWR = async (json, obj) => {
                        const resp = await json;
                        if(resp != "OK"){
                            obj.allWentWell = false;
                            console.log(resp, 
                                "server's response detaching pic(" + upONp.componentid + ")");
                        }
                    }
                    reqWithParamsUpStringDown(endpoint, upONp, dWR, wrap);
                }
            }
            if(dtobject.descid){
                const upONd = {
                    useremail: upon.useremail,
                    entityid: upon.entityid, 
                    componentid: dtobject.descid,
                    textparam: "desc"
                }
                const dWR = async (json, obj) => {
                    const resp = await json;
                    if(resp != "OK"){
                        obj.allWentWell = false;
                        console.log(resp, 
                            "server's response detaching desc(" + upONd.componentid + ")");
                    }
                }
                reqWithParamsUpStringDown(endpoint, upONd, dWR, wrap);
            }
            return await wrap;
        }
        const wrapobj = {
            allWentWell: true
        }
        const aWW = await checkAllWentWell(wrapobj, detachingobject, upON);
        if(aWW.allWentWell){
            await doWithResponse("OK");
        }
    }
} 
async function detachAllFromEdgeOnServer(upON, doWithResponse){
    const endpoint = "/api/dataexchange/detachall";
    
    reqWithParamsUpStringDown(endpoint, upON, doWithResponse);

}
async function attachXXXToEdgeOnServer(upON, doWithResponse){
    const endpoint = "/api/dataexchange/attachcomponent";
    reqWithParamsUpStringDown(endpoint, upON, doWithResponse);
}
async function getOrphanDescsFromServer(useremail, doWithResponse){
    const myuseremail = (useremail == undefined ? globalUser.email : useremail);
    const upON = {
        useremail : myuseremail,
        entityid : null,
        textparam : null
    };
    await reqWithParamsUpJSONDown("/api/dataexchange/orphandescs", upON, doWithResponse);
}
async function getGraphDescsFromServer(mygraphid, doWithResponse){
    const myuseremail = (useremail == undefined ? globalUser.email : useremail);
    const upON = {
        useremail : globalUser.email,
        entityid : mygraphid,
        textparam : null
    };
    await reqWithParamsUpJSONDown("/api/dataexchange/graphdescs", upON, doWithResponse);
}
async function getGraphAndOrphanDescsFromServer(mygraphid, useremail, doWithResponse){
    const myuseremail = (useremail == undefined ? globalUser.email : useremail);
    const upON = {
        useremail : myuseremail,
        entityid : mygraphid,
        textparam : null
    };
    await reqWithParamsUpJSONDown("/api/dataexchange/descs", upON, doWithResponse);
}
async function getOrphanInvElemsFromServer(useremail, doWithResponse){
    const myuseremail = (useremail == undefined ? globalUser.email : useremail);
    const upON = {
        useremail : myuseremail,
        entityid : null,
        textparam : null
    };
    await reqWithParamsUpJSONDown("/api/dataexchange/orphaninventory", upON, doWithResponse);
}
async function getGraphAndOrphanInvElemsFromServer(mygraphid, useremail, doWithResponse){
    const myuseremail = (useremail == undefined ? globalUser.email : useremail);
    const upON = {
        useremail : myuseremail,
        entityid : mygraphid,
        textparam : null
    };
   await reqWithParamsUpJSONDown("/api/dataexchange/graphandorphaninventory", upON, doWithResponse);
}
async function getOrphanPicturesFromServer(useremail, doWithResponse){
    const myuseremail = (useremail == undefined ? globalUser.email : useremail);
    const upON = {
        useremail : myuseremail,
        entityid : null,
        textparam : null
    };
    await reqWithParamsUpJSONDown("/api/dataexchange/orphaninventory", upON, doWithResponse);
}
async function getGraphAndOrphanPicturesFromServer(mygraphid, useremail, doWithResponse){
    const myuseremail = (useremail == undefined ? globalUser.email : useremail);
    const upON = {
        useremail : myuseremail,
        entityid : mygraphid,
        textparam : null
    };
    await reqWithParamsUpJSONDown("/api/dataexchange/graphandorphaninventory", upON, doWithResponse);
}
async function getOrphanCompIdsDTOFromServer(useremail, doWithResponse){
    const myuseremail = (useremail == undefined ? globalUser.email : useremail);
    const upON = {
        useremail : myuseremail
    };
    await reqWithParamsUpJSONDown("/api/dataexchange/getorphancompidsdto", upON, doWithResponse);
}
async function getGraphFromServer(graphid, useremail, graphname, doWithResponse){
    const myuseremail = (useremail == undefined ? globalUser.email : useremail);
    const upON = {
        useremail : myuseremail,
        entityid : graphid,
        textparam : graphname
    }
    await reqWithParamsUpJSONDown("/api/dataexchange/graphdto", upON, doWithResponse);
}
async function getCloneGraphFromServer(useremail, graphid, doWithResponse){
    const myuseremail = (useremail == undefined ? globalUser.email : useremail);
    const upON = {
        useremail : myuseremail,
        entityid : graphid,
        textparam : null
    }
    await reqWithParamsUpJSONDown("/api/dataexchange/getclonedgraph", upON, doWithResponse);
}
async function getNewGraphFromServer(useremail, nodenum, graphbuildertype, doWithResponse){
    const myuseremail = (useremail == undefined ? globalUser.email : useremail);
    const mynodenum = (nodenum == undefined ? 3 : nodenum);
    const mygraphbuildertype = (graphbuildertype == undefined ? "linear" : graphbuildertype); 
    console.log(myuseremail, useremail);
    const upON = {
        useremail : myuseremail,
        entityid : mynodenum,
        textparam : mygraphbuildertype
    }
    await reqWithParamsUpJSONDown("/api/dataexchange/newgraphdto", upON, doWithResponse);
}
// CURRENT
async function uploadPictureFromForm(upON, doWithResponse){
    //https://www.youtube.com/watch?v=JtKIcqZdLLM
    //downloaded 2021-05-03
let req = new Request(globalEndpoint.getServer + "/api/uploadpics",
    {
        method: "POST",
        headers: {'Accept': 'application/json'},
        body: upON  // origonal: body: formdata
    }
);
fetch(req).then(function(response){
        //put response data on chain
        return response.json();
    }).then(function(json){
        doWithResponse(json);
        
    }).catch(function(err){
        console.log("Fetch problem: " + err.message);
        console.log(upON, "LOG upON in uploadPictureFromForm");
    }
);
}
async function reqWithJSONUpDown(endpoint, upON, myfunction){
    const req = new Request(globalEndpoint.getServer + endpoint,
            { method: "POST",
              headers: new Headers({'Content-Type': 'application/json'}),
              body: JSON.stringify(upON) }
    );
    fetch(req)
        .then(function(response){
            //put response data on chain
            return response.json();
        })
        .then(function(json){
            myfunction(json);       
        })
        .catch(function(err){
            console.log("Problem while transferring object : " + JSON.stringify(upON));
            console.log("Error: " + err.message);
        }
    );
}
async function reqWithJSONUpStringDown(endpoint, upON, myfunction){
    // https://dmitripavlutin.com/fetch-with-json/
    // Letöltve: 2021.11.26
    const req = new Request(globalEndpoint.getServer + endpoint,
            { method: "POST",
              headers: new Headers({'Content-Type': 'application/json'}),
              body: JSON.stringify(upON) }
    );
    fetch(req)
        .then(function(response){
            //put response data on chain
            return response.json();
        })
        .then(function(json){
            myfunction(json.text);       
        })
        .catch(function(err){
            //console.log("Problem while transferring object : " + JSON.stringify(upON));
            console.log("Error: " + err.message);
        }
    );
}
async function reqWithParamsUpJSONDown(endpoint, paramsON, myfunction){
    const req = new Request(globalEndpoint.getServer + endpoint,
            { method: "POST",
              headers: new Headers({'Content-Type' : 'application/json'}),
              body: JSON.stringify(paramsON) }
    ); 
    fetch(req)
        .then(function(response){
            //put response data on chain
            return response.json();
        })
        .then(function(json){
            myfunction(json);
        })
        .catch(function(err){
            console.log("Problem while transferring params : " + JSON.stringify(paramsON));
            console.log("Error: " + err.message);
        }
    );
}
async function reqWithParamsUpStringDown(endpoint, paramsON, myfunction, myobj){
    const req = new Request(globalEndpoint.getServer + endpoint,
            { method: "POST",
              headers: new Headers({'Content-Type' : 'application/json'}),
              body: JSON.stringify(paramsON) } 
    );
    fetch(req)
        .then(function(response){
            //put response data on chain
            return response.json();
        })
        .then(function(json){
            myfunction(json.text, myobj);
            //console.log(myobj, "myobj in reqWithParamsUpStringDown");       
        })
        .catch(function(err){
            console.log("Problem while transferring params : " + JSON.stringify(paramsON));
            console.log("Error: " + err.message);
        }
    );
}
async function reqWithJSONDown(endpoint, myfunction){
    const req = new Request(globalEndpoint.getServer + endpoint, {method: "GET"});
    await fetch(req)
        .then(function(response){
            //put response data on chain
            return response.json();
        })
        .then(function(json){
            myfunction(json);       
        })
        .catch(function(err){
            console.log("Problem while accessing endpoint: " + endpoint);
            console.log("Error: " + err.message);
        }
    );
}