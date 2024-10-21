function loadDummyIntoGlobalData(){
    globalData = new GlobalData([], [], [], []);
    globalData.addElementToXXXList(dummyUserDescriptions[0].id, "odids");
    globalData.addElementToXXXList(dummyUserDescriptions[1].id, "odids");
    globalData.addElementToXXXList(dummyUserInventoryElements[0].id, "oiids");
    globalData.addElementToXXXList(dummyUserInventoryElements[1].id, "oiids");
    globalData.addElementToXXXList(dummyUserPictures[0].id, "opids");
    globalData.addElementToXXXList(dummyUserPictures[1].id, "opids");
    globalData.Pictures =  dummyGraphPictures;
    globalData.addElementToXXXList(dummyUserPictures[0], "pics");
    globalData.addElementToXXXList(dummyUserPictures[1], "pics");
    globalData.InventoryElements = dummyGraphInventoryElements;
    globalData.addElementToXXXList(dummyUserInventoryElements[0], "invelems");
    globalData.addElementToXXXList(dummyUserInventoryElements[1], "invelems");
    globalData.Descriptions = dummyGraphDescriptions;
    globalData.addElementTo0XXXList(dummyUserDescriptions[0], "descs");
    globalData.addElementToXXXList(dummyUserDescriptions[1], "descs");
    dummyEdgeDTOs.forEach(edgedto =>{
        let edgedtocopy = {};
        edgedtocopy.id = edgedto.id,
        edgedtocopy.fromNodeId = edgedto.fromNodeId,
        edgedtocopy.toNodeId = edgedto.toNodeId,
        edgedtocopy.label = edgedto.label,
        edgedtocopy.descriptionId = 
            (edgedto.descriptionId == "" ? undefined : edgedto.descriptionId),
        edgedtocopy.pictureIds = edgedto.pictureIds, 
        edgedtocopy.inventoryElementIds = edgedto.inventoryElementIds,
        edgedtocopy.time1 = edgedto.time1,
        edgedtocopy.time2 = edgedto.time2
        globalData.addElementToXXXList(edgedtocopy, "edgedtos");
    });
    globalData.populateMapXXXToEdgeIds();
    globalUser.id = dummyUserData.id;
    globalUser.email = dummyUserData.email;
}

async function loadGraphNameList(DOMElemName1, DOMElemName2, func){
    document.getElementById("dijkstra").style.display = "none";
    document.getElementById("makeedge").style.display = "none";
    document.getElementById("behaviormod").style.display = "none";
    globalUser.email = document.getElementById("useremail").value;
    //AJAX call
    const doWithResponse1 = async (jsondata) => {
        await func(DOMElemName1, jsondata);
        globalOwnGraphNameList = await jsondata;
    };
    getGraphDataListByUserFromServer(globalUser.email, doWithResponse1);
    const dWR2 = async (jsondata) => {
        await func(DOMElemName2, jsondata);
        globalGraphNameList = await jsondata;
    };
    getGraphDataListFromServer(dWR2);
}

(async (func) => {
    loadGraphNameList("selectowngraph","selectpublicgraph", func);
})(updateDOMSelectGraphName);
//central listener manager
addListenersToDOM();


