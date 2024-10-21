function handlePictureDeleteLinkClick(ev){
    const myentityid = getEntityIdFromDOMId("delpic", ev.target.id);
    const DOMelement = ev.target.parentNode.parentNode;
    const parent = DOMelement.parentNode;
    const elemid = parent.getAttribute("id"); 
    console.log("parent.getAttribute(id) :" + elemid);
    switch (elemid.substring(0,6)) {
        case "DOMupc":
            // AJAX call
            const upON1 = {
                id : myentityid,
                userEmail : globalUser.email 
            }
            const doWithResponse1 = async (text) => {
                const mytext = await text;
                if(mytext && mytext == "OK"){
                    globalData.removeXXXById(myentityid, "pics");
                    globalData.removeXXXById(myentityid, "opids");
                    DOMelement.parentNode.removeChild(DOMelement);
                }
                console.log(mytext, "LOG server response deleting picture");
            }
            removePicFromServer(upON1, doWithResponse1);
            break;
        case "DOMepc":
            const myedgeid = getEntityIdFromDOMId("DOMepc", elemid);
            if (myentityid && myedgeid){
                const pdto = new ParamDTO(globalUser.email, myedgeid, myentityid, "pic");
                const doWithResponse2 = async (text) => {
                    const mytext = await text;
                    if(mytext && mytext == "OK"){
                        globalData.detachPicFromEdge(myedgeid, myentityid);
                        console.log("edgeid: " + myedgeid);
                        const edgedata = getElementById(myedgeid, globalData.GraphEdgeDatas);
                        if(edgedata.pictureIds){
                            console.log(edgedata.pictureIds.join(", "));
                        }
                        targetcontainer = document.getElementById("DOMupc");
                        DOMelement.setAttribute("isorphan", true);
                        targetcontainer.insertBefore(DOMelement, targetcontainer.lastChild);
                        //DOMelement.lastChild.lastChild.style.pointerEvents = "auto";
                        const attachlink = document.getElementById("attpic" + myentityid);
                        attachlink.style.pointerEvents = "auto";
                    }
                    console.log("server response detaching picture", mytext);
                };
                detachXXXFromEdgeOnServer(pdto.getDTO(), doWithResponse2);
            }
            break;
        default:
            break;
    }
}
function handlePictureUpdateLinkClick(ev){
    const entityid = getEntityIdFromDOMId("updpic", ev.target.id);
    const DOMelement = ev.target.parentNode.parentNode.firstChild;
    // AJAX call
    // no time for iomplementing... 
    console.log("update entity id: " + entityid + ", img src: " + 
        DOMelement.getAttribute("src"));
}
function handleDescriptionDeleteLinkClick(ev){
    const myentityid = getEntityIdFromDOMId("deldesc", ev.target.id);
    const DOMelement = ev.target.parentNode.parentNode;
    const parent = DOMelement.parentNode;
    const elemid = parent.getAttribute("id");
    console.log("parent.getAttribute(id) " + elemid);
    switch(parent.getAttribute("id").substring(0,6)){
        case "DOMudc":
            // AJAX call
            const upON1 = {
                entityid: myentityid
            };
            const doWithResponse1 = async (text) => {
                const mytext = await text;
                if(mytext && mytext == "OK"){
                    globalData.removeXXXById(myentityid, "descs");
                    globalData.removeXXXById(myentityid, "odids");
                    parent.removeChild(DOMelement);
                }
                console.log(await mytext, "LOG server response deleting description");
            };
            removeDescFromServer(upON1, doWithResponse1);
            break;
        case "DOMedc":
            const edgeid =  getEntityIdFromDOMId("DOMedc", elemid);
            const descdata = globalData.getDescData(myentityid);
            if(descdata.text && descdata.text != ""){
                //AJAX call
                const pdto = new ParamDTO(globalUser.email, edgeid, myentityid, "desc");
                const doWithResponse2 = async (text) => {
                    const mytext = await text;
                    if(mytext && mytext == "OK"){
                        if(descdata.text != globalEMPTYDESC){
                            globalData.detachDescFromEdge(edgeid, myentityid);
                            targetcontainer = document.getElementById("DOMudc");
                            const dicb = new DescInfoContainerBuilder(descdata);
                            dicb.isOrphan = true;
                            targetcontainer.insertBefore(dicb.getCreatedDOM(), targetcontainer.lastChild);
                            const attachlink = document.getElementById("attdesc" + myentityid);
                            attachlink.style.pointerEvents = "auto";
                            DOMelement.parentNode.removeChild(DOMelement);
                        } else {
                            globalData.removeXXXById(myentityid, 'descs');
                            const edgedto = globalData.getEdgeDTO(edgeid);
                            edgedto.descriptionId = null;
                            DOMelement.parentNode.removeChild(DOMelement);
                        }
                    }
                    console.log(await text, "LOG server response detaching description");
                }
                detachXXXFromEdgeOnServer(pdto.getDTO(), doWithResponse2);
            }
            break;
    }
}
function handleDescriptionUpdateLinkClick(ev){
    const entityid = getEntityIdFromDOMId("upddesc", ev.target.id);
    console.log(entityid, "LOG entityid in handleDescriptionUpdateLinkClick()");
    const DOMelement = ev.target.parentNode.parentNode.firstChild;
    const entitydata = DOMelement.value; 
    // AJAX call
    const upON = {
        id: entityid,
        text: entitydata 
    };
    const doWithResponse = async (json) => {
        const resp = await json;
        const mytext = resp.text;
        if(mytext && mytext == "OK"){
            globalData.changeDescriptionById(entityid, entitydata);
            DOMelement.classList.remove("changedbackground");
        }        
        console.log("server's response: " + mytext);
    };
    saveDescToServer(upON, doWithResponse);
}
function handleInventoryElemDeleteLinkClick(ev){
    const myentityid = getEntityIdFromDOMId("delie", ev.target.id);
    const DOMelement = ev.target.parentNode.parentNode;
    const DOMelementValue = DOMelement.firstChild.value;
    //temp
    //console.log(DOMelement, "DEVLOG: DOMelement in handleInventoryElemDeleteLinkClick");
    const parent = DOMelement.parentNode;
    switch (parent.getAttribute("id").substring(0,6)){
        case "DOMeic":
            const edgeid = getEntityIdFromDOMId("DOMeic", parent.getAttribute("id"));
            // AJAX call
            const pdto = new ParamDTO(globalUser.email, edgeid, myentityid, "invelem");
            const doWithResponse1 = async (text) => {
                const mytext = await text;
                if(mytext && mytext == "OK"){
                    globalData.detachInvElemFromEdge(edgeid, myentityid);
                    const DOMtargetcontainer = document.getElementById("DOMuic");
                    DOMelement.setAttribute("isorphan", true);
                    DOMtargetcontainer.insertBefore(DOMelement, DOMtargetcontainer.lastChild);
                    const attachlink = document.getElementById('attie' + myentityid);
                    attachlink.style.pointerEvents = "auto"
                }
                console.log(await text, "LOG server's response detaching inventoryelement");
            }
            //removeInvElemFromServer(upON1, doWithResponse1);
            detachXXXFromEdgeOnServer(pdto.getDTO(), doWithResponse1);
            break;
        case "DOMuic":
            //AJAX call
            const upON2 = {
                entityid: myentityid
            };
            const doWithResponse2 = async (text) => {
                const mytext = await text;
                if(mytext && mytext == "OK"){
                    globalData.removeXXXById(myentityid, "oiids");
                    parent.removeChild(DOMelement);
                }
                console.log(await text, "LOG server's response deleting inventoryelement");
            }
            removeInvElemFromServer(upON2, doWithResponse2);
            break;
    }
}
function handleInventoryElemUpdateLinkClick(ev){
    const entityid = getEntityIdFromDOMId("updie", ev.target.id);
    const DOMinventoryelem = ev.target.parentNode.parentNode.firstChild;
    const entitydata = DOMinventoryelem.value; 
    if(entitydata.length > 32){
        alert("Túl hosszú szöveg. Max ennyit írj:\n" + entitydata.substring(0,32));
    }
    // AJAX call
    const upON = {
        id: Number(entityid),
        text: entitydata 
    };
    const doWithResponse = async (text) => {
        const mytext = await text;
        if(mytext && mytext == "OK"){
            globalData.changeInventoryElementById(entityid, entitydata);
            DOMinventoryelem.classList.remove("changedbackground");
        }
        console.log("server's response: " + mytext);
    };
    saveInvElemToServer(upON, doWithResponse);
    
}
function handleTimeSaveLinkClick(ev){
    const entityid = getEntityIdFromDOMId("stime", ev.target.id);
    console.log(ev.target, "LOG ev.target in handleTimeSaveLinkClick");
    const time1DOMid = "tbtime1" + entityid;
    const time2DOMid = "tbtime2" + entityid;
    const DOMtime1 = document.getElementById(time1DOMid);
    const DOMtime2 = document.getElementById(time2DOMid);
    const myedgedto = globalData.getEdgeDTO(entityid);
    // AJAX call
    myedgedto.time1 = Number(DOMtime1.value);
    myedgedto.time2 = Number(DOMtime2.value);
    const doWithResponse = async (text) => {
        const resp = await text;
        console.log("saved time1: " + DOMtime1.value + " time2: " + DOMtime2.value);
        console.log(resp, "LOG server's response");
        DOMtime1.classList.remove("changedbackground");
        DOMtime2.classList.remove("changedbackground");
    }
    updateEdgeData(myedgedto, doWithResponse);
}
function handleLabelSaveLinkClick(ev){
    const entityid = getEntityIdFromDOMId("slabel", ev.target.id);
    const labelDOMid = "tblabel" + entityid;
    const DOMlabel = document.getElementById(labelDOMid);
    let DOMvalue = DOMlabel.value.substring(0,24);
    if(globalData.isExistEdgeLabel(DOMvalue)){
        alert("Már van ilyen " + DOMvalue + " címkéjű rejtvény");
        DOMvalue = globalData.getEdgeDTO(entityid).label;
        DOMlabel.value = DOMvalue;
        DOMlabel.classList.remove("changedbackground");
    } else {
        const myedgedto = globalData.getEdgeDTO(entityid);
        myedgedto.label = DOMvalue;
        // AJAX call
        const doWithResponse = async (text) => {
            const resp = await text;
            DOMlabel.classList.remove("changedbackground");
            const myedge = globalGraph.graph.getEdgeById(myedgedto.id);
            myedge.label = DOMvalue;
            globalGraph.graphView.updateLabelofEdge(myedge);
        }
        updateEdgeData(myedgedto, doWithResponse);
    }
}
function handleTextBoxChanged(ev, backgroundstyle = "changedbackground"){
    const DOMelem = ev.target;
    DOMelem.classList.add(backgroundstyle);
}
function handlePictureAttachLinkClick(ev){
    const entityid = getEntityIdFromDOMId("attpic", ev.target.id);
    console.log("attach pic to edge was clicked. entity id: " + entityid);
    const thisDOMelem = ev.target;
    thisDOMelem.style.pointerEvents = "none";
    const parentDIV = thisDOMelem.parentNode.parentNode;
    const aeteb = new AttachElementToEdgeBuilder(entityid, "pic", handlePictureAttachToEdgeClick);
    parentDIV.appendChild(aeteb.getCreatedDOM());
}
function handlePictureAttachToEdgeClick(ev){
    const entityid = getEntityIdFromDOMId("attlinkpic_",ev.target.id); 
    const targetparent = ev.target.parentNode;
    const select = ev.target.previousSibling.firstChild;
    const idx = select.options.selectedIndex;
    const value = select.options[idx].value;
    const edgeid = getElementByLabel(value, globalGraph.graph.getEdges()).id;
    //AJAX call

    const pdto = new ParamDTO(globalUser.email, edgeid, entityid, "pic"); 
    /*
     * comptypes: "desc" | "invelem" | "pic"
     */
    const doWithResponse = (text) => {
        globalData.attachPicToEdge(edgeid, entityid);
        const piccontainer = targetparent.parentNode;
        piccontainer.setAttribute("isorphan", false);
        const attachlink = targetparent.previousSibling.lastChild;
        piccontainer.parentNode.removeChild(piccontainer);
        attachlink.style.pointerEvents = "none";
        if(value == globalData.SelectedEdgeData.label){
            const epc = document.getElementById("DOMepc" + edgeid);
            if(epc){
                const addpiclink = epc.lastChild;
                epc.insertBefore(piccontainer, addpiclink);
            }
            hideElementById("att_pic" + entityid);
        }        
        console.log("server's response to attaching picture to edge: " + text, "LOG respose");
    };
    attachXXXToEdgeOnServer(pdto.getDTO(), doWithResponse);
}
function handleDescriptionAttachLinkClick(ev){
    const entityid = getEntityIdFromDOMId("attdesc", ev.target.id);
    console.log("attach desc to edge was clicked. entity id: " + entityid);
    const thisDOMelem = ev.target;
    const parentDIV = thisDOMelem.parentNode.parentNode;
    const aeteb = new AttachElementToEdgeBuilder(entityid, "desc", handleDescriptionAttachToEdgeClick);
    parentDIV.appendChild(aeteb.getCreatedDOM());
}
function handleDescriptionAttachToEdgeClick(ev){
    const myentityid = getEntityIdFromDOMId("attlinkdesc_",ev.target.id); 
    const targetparent = ev.target.parentNode;
    const select = ev.target.previousSibling.firstChild;
    const idx = select.options.selectedIndex;
    const value = select.options[idx].value;
    const edgeid = getElementByLabel(value, globalGraph.graph.getEdges()).id;
    const attacheddescid = globalData.getEdgeDTO(edgeid).descriptionId;
    const attacheddesccontainer = document.getElementById("dic" + attacheddescid);
    console.log("edgeid in handleDescriptionAttachToEdgeClick: " + edgeid);
    console.log("orphan entityid in handleDescriptionAttachToEdgeClick: " + myentityid);
    console.log("attached entityid: " + attacheddescid);
    const isAttachTargetEdgeSelected = 
        (value == globalData.SelectedEdgeData.label);
    const desccontainer = ev.target.parentNode.parentNode;
    const desccontainernextsibling = desccontainer.nextSibling;
    //const attachlink = targetparent.previousSibling.lastChild;
    const attachlink = document.getElementById("attdesc" + myentityid);    
    const isUndefDesc = (attacheddescid === undefined || attacheddescid === null);
    const isEmptyDesc = (!isUndefDesc && 
        globalData.getDescData(attacheddescid).text === globalEMPTYDESC);
    const preserveDesc = (!isUndefDesc && !isEmptyDesc);
    if(isAttachTargetEdgeSelected){
        const edcontainer = document.getElementById("DOMedc" + edgeid);
        const DOMafter = document.getElementById("DOMeic"+ edgeid) === null 
                ? edcontainer.firstChild.nextSibling.nextSibling.nextSibling
                : document.getElementById("DOMeic"+ edgeid);
        const parent = DOMafter.parentNode;
        /*
        Valamiert az alabbi 2 utasitas vizuálisan hatastalan
        noha objektumként működnek 
        parent.insertBefore(desccontainer, DOMafter);
        desccontainer.setAttribute("isorphan", false);
        */
        //Fenti problema miatt trukk: letrehozom a DOM elemet,
        // nem atcsatolom
        const dicb2edge = new DescInfoContainerBuilder(globalData.getDescData(myentityid));
        const attachDOMToEdge = dicb2edge.getCreatedDOM();
        const DOMattdesc = attachDOMToEdge.firstChild.nextSibling.lastChild;
        attachDOMToEdge.setAttribute("isorphan", false);
        DOMattdesc.style.pointerEvents = "none";
        parent.insertBefore(attachDOMToEdge, DOMafter);
        if(attacheddescid){
            attacheddesccontainer.parentNode.removeChild(attacheddesccontainer);
            if(preserveDesc){
                const pdto = new ParamDTO(globalUser.email, edgeid, attacheddescid, "desc");
                const doWithResponse = async (text) => {
                    console.log("server's response to detaching attached description from edge: " + text); 
                    desccontainernextsibling.parentNode.insertBefore(attacheddesccontainer, desccontainernextsibling);
                    attacheddesccontainer.setAttribute("isorphan", true);
                    document.getElementById("attdesc" + attacheddescid).style.pointerEvents = "auto";
                }
                detachXXXFromEdgeOnServer(pdto.getDTO(), doWithResponse);
            } else {
                const pdto2 = new ParamDTO(globalUser.email, attacheddescid);
                const doWithResponse2 = async (text) => {
                    console.log("server's response to removing attached description from edge: " + text); 
                }    
                removeDescFromServer(pdto2.getDTO(), doWithResponse2);
            }
        } 
        //e.g. id: attdesc1083
        const pdto3 = new ParamDTO(globalUser.email, edgeid, myentityid, "desc");
        const doWithResponse3 = async (text) => {
            console.log("server's response to attaching desc to edge: " + text);
            if(text){
                globalData.attachDescToEdge(edgeid, myentityid, preserveDesc);
                if(globalData.SelectedEdgeData.id == edgeid){
                    hideElementById("att_desc" + myentityid);    
                }
                const descriptionContainer = targetparent.parentNode;
                const orphanDescContainer = targetparent.parentNode.parentNode;
                orphanDescContainer.removeChild(descriptionContainer);
            }
        }
        attachXXXToEdgeOnServer(pdto3.getDTO(), doWithResponse3);
    } else {
        if(attacheddescid){
            if(preserveDesc){
                const pdto = new ParamDTO(globalUser.email, edgeid, attacheddescid, "desc");
                const doWithResponse = async (text) => {
                    console.log("server's response to detaching attached description from edge: " + text); 
                    console.log(attacheddesccontainer, "LOG attacheddesccontainer");
                    desccontainernextsibling.parentNode.insertBefore(attacheddesccontainer, desccontainernextsibling);
                    attacheddesccontainer.setAttribute("isorphan", true);
                    document.getElementById("attdesc" + attacheddescid).style.pointerEvents = "auto";
                }
                detachXXXFromEdgeOnServer(pdto.getDTO(), doWithResponse);
            } else {
                const pdto2 = new ParamDTO(globalUser.email, attacheddescid);
                const doWithResponse2 = async (text) => {
                    console.log("server's response to removing attached description from edge: " + text); 
                }    
                removeDescFromServer(pdto2.getDTO(), doWithResponse2);
            }
        } 
        const pdto3 = new ParamDTO(globalUser.email, edgeid, myentityid, "desc");
        const doWithResponse3 = async (text) => {
            globalData.attachDescToEdge(edgeid, myentityid, preserveDesc);
            desccontainer.parentNode.removeChild(desccontainer);
            console.log("server's response to attaching desc to edge: " + text); 
        }
        attachXXXToEdgeOnServer(pdto3.getDTO(), doWithResponse3);
    }
}
function handleInventoryElemAttachLinkClick(ev){
    const entityid = getEntityIdFromDOMId("attie", ev.target.id);
    console.log("attach inventoryelement to edge was clicked. entity id: " + entityid);
    const thisDOMelem = ev.target;
    thisDOMelem.style.pointerEvents = "none";
    const parentDIV = thisDOMelem.parentNode.parentNode;
    const aeteb = new AttachElementToEdgeBuilder(entityid, "ie", handleInventoryElementAttachToEdgeClick);
    parentDIV.appendChild(aeteb.getCreatedDOM());
}
function handleInventoryElementAttachToEdgeClick(ev){
    const myentityid = getEntityIdFromDOMId("attlinkie_",ev.target.id); 
    const targetparent = ev.target.parentNode;
    const select = ev.target.previousSibling.firstChild;
    const idx = select.options.selectedIndex;
    const value = select.options[idx].value;
    const edgeid = getElementByLabel(value, globalGraph.graph.getEdges()).id;
    //AJAX call
    const upON = {
        useremail: globalUser.email,
        entityid: edgeid,
        componentid:  myentityid,
        textparam: "invelem"
    };
    const doWithResponse = async (text) => {
        const resp = await text;
        if(resp === "OK"){
            globalData.attachInvElemToEdge(edgeid, myentityid);
            const iecontainer = targetparent.parentNode;
            const attachlink = targetparent.previousSibling.lastChild;
            attachlink.style.pointerEvents = "none";
            iecontainer.setAttribute("isorphan",false);
            iecontainer.parentNode.removeChild(iecontainer);
            if(value == globalData.SelectedEdgeData.label){
                const eic = document.getElementById("DOMeic" + edgeid);
                if(eic){
                    const addielinkdiv = eic.lastChild;
                    eic.insertBefore(iecontainer, addielinkdiv);
                }
                hideElementById("att_ie" + myentityid);    
            }
        }
    };
    attachXXXToEdgeOnServer(upON, doWithResponse);
}
function handleUserPictureAppendLinkClick(ev){
    const container = ev.target.parentNode.parentNode;
    const uploadModal = document.getElementById('uploadModal');
    uploadModal.setAttribute("usercontid", container.getAttribute("id")); 
    document.getElementById("modaledgeid").value = "";
    document.getElementById("modaluseremail").value = document.getElementById("useremail").value; 
    let BSuploadModal = new bootstrap.Modal(uploadModal, {backdrop: "static"});
    BSuploadModal.show();
}
function handleUserDescAppendLinkClick(ev){
    const DOMtargetcontainer = ev.target.parentNode.parentNode;
    const lastchild = ev.target.parentNode;
    const mytag = null;
    // AJAX call
    const descriptiondata = {
        id: null,
        tag: mytag,
        text: "Rejtvény leírás" 
    }; 
    const upON = {
        descdto: descriptiondata,
        useremail: globalUser.email,
        entityid: null
    };
    const doWithResponse = async (json) => {
        const descdto = await json;
        if(descdto){
            descriptiondata.id = descdto.id;
            globalData.addElementToXXXList(descriptiondata, "descs");
            globalData.addElementToXXXList(descriptiondata.id, "odids");
            const dicb = new DescInfoContainerBuilder(descriptiondata);
            dicb.isOrphan = true;
            DOMtargetcontainer.insertBefore(dicb.getCreatedDOM(), lastchild);
            console.log("appended new desc element id: " + descriptiondata.id);
        }
    };
    getNewDescFromServer(upON, doWithResponse);
    
}
function handleUserInventoryAppendLinkClick(ev){
    const DOMtargetcontainer = ev.target.parentNode.parentNode;
    const lastchild = ev.target.parentNode;
    // AJAX call
    const mytext = "tárgy";
    const upON = {
        invelemdto: {
            id: null,
            text: mytext 
        },
        useremail: globalUser.email,
        entityid : null
    };
    const doWithResponse = async (json) => {
        const mytext = (await json).text;
        if(mytext){
            const entityid = Number(mytext);
            const inventoryelementdata = {
                id: entityid,
                text: mytext
            }
            globalData.addElementToXXXList(inventoryelementdata, "invelems");
            globalData.addElementToXXXList(inventoryelementdata.id, "oiids");
            const iecb = new InventoryElementContainerBuilder(inventoryelementdata, "DOMtbie");
            iecb.isOrphan = true;
            DOMtargetcontainer.insertBefore(iecb.getCreatedDOM(), lastchild);
            console.log("appended new inventory element id: " + inventoryelementdata.id);
        }
    }
    getInvElemIdFromServer(upON, doWithResponse);
}
async function handleEdgeInventoryAppendLinkClick(ev){
    const DOMtargetcontainer = ev.target.parentNode.parentNode;
    const edgeid = getEntityIdFromDOMId("DOMeic", DOMtargetcontainer.getAttribute("id"));
    console.log(edgeid, "LOG edgeid in handleEdgeInventoryAppendLinkClick");
    console.log(DOMtargetcontainer, "LOG DOMtargetcontainer in handleEdgeInventoryAppendLinkClick");
    const lastchild = ev.target.parentNode;
    //AJAX call
    const mytext = "tárgy";
    const upON = {
        invelemdto: {
            id: null,
            text: mytext 
        },
        useremail: globalUser.email,
        entityid : edgeid
    };
    const doWithResponse = async (json) => {
        const resp = await json;
        if(resp){
            const entityid = Number(resp.text);
            if(entityid > -1){
                const inventoryelementdata = {
                    id: entityid,
                    text: mytext + resp.text
                }
                await globalData.appendInvElemToEdge(inventoryelementdata, edgeid);
                const iecb = new InventoryElementContainerBuilder(inventoryelementdata, "DOMtbie");
                iecb.isOrphan = false;
                DOMtargetcontainer.insertBefore(iecb.getCreatedDOM(), lastchild);
                console.log("appended new inventory element id: " + inventoryelementdata.id);
            } else {
                console.log("could not append new inventory element");
                alert("ERROR: " + resp.errormsg);
            }
        }
    };
    getInvElemIdFromServer(upON, doWithResponse);
    
}
function handleEdgePictureAppendLinkClick(ev){
    const DOMtargetcontainer = ev.target.parentNode.parentNode;
    const edgeid = getEntityIdFromDOMId("DOMepc", DOMtargetcontainer.getAttribute("id"));
    document
        .getElementById("uploadModal")
        .setAttribute("edgecontid", DOMtargetcontainer.getAttribute("id"));
    document.getElementById("modaledgeid").value = edgeid;
    document.getElementById("modaluseremail").value = document.getElementById("useremail").value; 
    const uploadModal = document.getElementById("uploadModal");
    let BSuploadModal = new bootstrap.Modal(uploadModal, {backdrop: "static"});
    BSuploadModal.show();
}
function handleUploadFileClick(ev){
    modalform = document.getElementById("fileuploadform");
    const thisModal = document.getElementById("uploadModal");
    const filename = modalform.file.value;
    const filesize = modalform.file.files[0].size;
    const DOMelem = document.getElementById("preview");
    const fileending = (filename.length > 4 ? 
        filename.toLowerCase().substring(filename.length-4) : "");
    if(!(fileending == ".jpg" || fileending == ".png" || 
        filename == "jpeg")){
        DOMelem.innerHTML = "Nem lett fájl kijelölve vagy nem kép (png /jpg) formátumú";
    } else if(filesize > 6291455){
        DOMelem.innerHTML = "Túl nagy fájl (" + filesize + " byte). Elfogadható: max 6291455 byte.";
    } else {
        let formData = new FormData();
        formData.append("modaledgeid", modalform.modaledgeid.value);
        formData.append("modaluseremail", globalUser.email);
        formData.append("file", modalform.file.files[0]);
        if(thisModal.getAttribute("edgecontid") != undefined){
            // AJAX call: 
            const upON1 = formData;
            const doWithResponse1 = async (json) => {
                const mypicid = await json.id;
                if(mypicid){
                    const pictureinfo = {
                        id: mypicid,
                        imagesrc: json.imagesrc
                    }
                    console.log(json.message, "LOG PicDataDTO.message in uploadPictureFromForm");
                    console.log(pictureinfo, "LOG pictureinfo in uploadPictureFromForm");
                    populateEdgePictureContainerFromResponse(thisModal, pictureinfo);    
                }
            }
            uploadPictureFromForm(upON1, doWithResponse1);
        } else if(thisModal.getAttribute("usercontid") != undefined) {
            // AJAX call :
            const upON2 = formData;
            const doWithResponse2 = async (json) => {
                const mypicid = await json.id;
                if(mypicid){
                    const pictureinfo = {
                        id: mypicid,
                        imagesrc: json.imagesrc
                    }
                    console.log(pictureinfo, "LOG pictureinfo in uploadPictureFromForm");
                    populateUserPictureContainerFromResponse(thisModal, pictureinfo);
                }
                console.log(json.message, "LOG PicDataDTO.message in uploadPictureFromForm");
            }
            uploadPictureFromForm(upON2, doWithResponse2);
        }
        clearUploadModalElements();
        $("#uploadModal").modal("hide");
    }
}

function handleUploadFilenameChanged(){
    const DOMelem = document.getElementById("preview");
    DOMelem.innerHTML = "";
}

async function populateEdgePictureContainerFromResponse(thisModal, pictureinfo){
    const picelem = await pictureinfo;
    if(picelem){
        const DOMelemId = thisModal.getAttribute("edgecontid");
        const edgeid = getEntityIdFromDOMId("DOMepc", DOMelemId);
        const container = document.getElementById(DOMelemId);
        const lastchild = container.lastChild;
        const picdata = new PictureData(picelem, globalPicWidth);
        const phb = new PictureHolderBuilder(picdata);
        phb.isOrphan = false;
        container.insertBefore(phb.getCreatedDOM(), lastchild);
        setTimeout(function(){
            (async (picelem, edgeid)  => {
                console.log( picelem, "LOG picelem");
                console.log("edgeid: " + edgeid);
                await globalData.appendPicToEdge(picelem, edgeid);
            })(picelem, edgeid);
        }, 400);
        thisModal.removeAttribute("edgecontid");
        console.log("edgecontid DOM (edge) id: " +  edgeid);
        console.log("appended new picture element id: " + picelem.id +
                    " file name: " +  picelem.imagesrc);
    }
}
async function populateUserPictureContainerFromResponse(thisModal, pictureinfo){
    const picelem = await pictureinfo;
    if(picelem){
        const container = document.getElementById(thisModal.getAttribute("usercontid"));
        const lastchild = container.lastChild;
        const picdata = new PictureData(picelem, globalPicWidth);
        const phb = new PictureHolderBuilder(picdata);
        phb.isOrphan = true;
        container.insertBefore(phb.getCreatedDOM(), lastchild);
        globalData.addElementToXXXList(picelem, "pics");
        globalData.addElementToXXXList(picelem.id, "opids");
        thisModal.removeAttribute("usercontid");
        console.log("appended new picture element id: " + picelem.id +
                        " file: " + picelem.imagesrc);
    }
}

function detachDescPicInventoryDOMElements(edgeid, detachingobject){
    let hasDOMWithThisData = false;
    if(globalData.SelectedEdgeData.id == edgeid){
        // children DOM elements exist, they are only needed to attach 
        // to other parent elements if they haven't contained already
        //Inventory:
		if(detachingobject.ieids && detachingobject.ieids.length > 0){
			for(const entityid of detachingobject.ieids){
				const entitydata = globalData.getInvElemData(entityid);
                // AJAX call - may not be necessary 
					
				const DOMtargetcontainer = document.getElementById("DOMuic");
                hasDOMWithThisData = false;
                for(let childnode of DOMtargetcontainer.childNodes){
                    const DOMid = childnode["id"];
                    const firstch = childnode.firstChild;
                    if((DOMid && DOMid === ("iec" + entityid)) ||
                            (firstch && firstch.value === entitydata.text)){
                        hasDOMWithThisData = true;
                        break;
                    }
                }
                if(!hasDOMWithThisData){
                    const DOMelement = document.getElementById("iec" + entityid);
				    DOMelement.setAttribute("isorphan", true);
				    DOMtargetcontainer.insertBefore(DOMelement, DOMtargetcontainer.lastChild);
				    DOMelement.lastChild.lastChild.style.pointerEvents = "auto";
                }
			}
		}
        //Pictures:
		if(detachingobject.picids && detachingobject.picids.length > 0){
			for(const entityid of detachingobject.picids){
				// AJAX call - may not be necessary

				const DOMtargetcontainer = document.getElementById("DOMupc");
				hasDOMWithThisData = false;
                for(const childnode of DOMtargetcontainer.childNodes){
                    if(childnode["id"] && childnode["id"] == "ph" + entityid){
                        hasDOMWithThisData = true;
                        console.log("pic container has this entity: " + childnode["id"]);
                        break;
                    }
                }
                if(!hasDOMWithThisData){
                    const DOMelement = document.getElementById("ph" + entityid);
                    DOMelement.lastChild.lastChild.style.pointerEvents = "auto";
                    DOMelement.setAttribute("isorphan", true);
                    DOMtargetcontainer.insertBefore(DOMelement, DOMtargetcontainer.lastChild);
                }	
			}
		}
    } else {
        //Inventory:
		if(detachingobject.ieids && detachingobject.ieids.length > 0){
			for(const entityid of detachingobject.ieids){
				const entitydata = globalData.getInvElemData(entityid);
                // AJAX call - may not be necessary
                
                const DOMtargetcontainer = document.getElementById("DOMuic");
                hasDOMWithThisData = false;
                for(const childnode of DOMtargetcontainer.childNodes){
                    const DOMid = childnode["id"];
                    const firstch = childnode.firstChild;
                    if((DOMid && DOMid === "iec" + entityid) || 
                            (firstch && firstch.value === entitydata.text)){
                        hasDOMWithThisData = true;
                        break;
                    }
                }
                if(!hasDOMWithThisData){
                    const iecb = new InventoryElementContainerBuilder(entitydata, "DOMtbie");
                    iecb.isOrphan = true;
                    DOMtargetcontainer.insertBefore(iecb.getCreatedDOM(), DOMtargetcontainer.lastChild);
                }
            }
		}
        //Pictures:
		if(detachingobject.picids && detachingobject.picids.length > 0){
			for(const entityid of detachingobject.picids){
                // AJAX call - may not be necessary

                let picelem = globalData.getPicData(entityid);
                const DOMtargetcontainer = document.getElementById("DOMupc");
                hasDOMWithThisData = false;
                for(let childnode of DOMtargetcontainer.childNodes){
                    if(childnode["id"] && childnode["id"] == "ph" + entityid){
                        hasDOMWithThisData = true;
                        break;
                    }
                }
                if(!hasDOMWithThisData){
                    const picdata = new PictureData(picelem, globalPicWidth);
                    const phb = new PictureHolderBuilder(picdata);
                    phb.isOrphan = true;
                    DOMtargetcontainer.insertBefore(phb.getCreatedDOM(), DOMtargetcontainer.lastChild);
                }
            }
		}
    }
	//does not matter if description DOM element exists
	//Desc: 
	if(detachingobject.descid){
		const descdata = globalData.getDescData(detachingobject.descid);
		if(descdata.text != undefined && descdata.text != "" && 
			descdata.text !== globalEMPTYDESC){
			// AJAX call - may not be necessary
			DOMtargetcontainer = document.getElementById("DOMudc");
			hasDOMWithThisData = false;
            for(const childnode of DOMtargetcontainer.childNodes){
                const firstch = childnode.firstChild;
                if(firstch && firstch.value === descdata.text){
                    hasDOMWithThisData = true;
                    break;
                }
            }
            if(!hasDOMWithThisData){
                const dicb = new DescInfoContainerBuilder(descdata);
                dicb.isOrphan = true;
                DOMtargetcontainer.insertBefore(dicb.getCreatedDOM(), DOMtargetcontainer.lastChild);
            }
        }
	}				 
}

function rearrangeGraphDataDOMelements(edgeid){
    globalData.removeEdgeDataById(edgeid);
    let edgedata = {};
    if(edgeid == globalData.SelectedEdgeData.id){
        console.log("edgeid: " + edgeid + " globalSelectedEdge.id: " + globalSelectedEdge.id); 
        edgedata = populateEdgeData(globalData.GraphEdgeDatas[0].id);
    } else {
        console.log("globalData.SelectedEdgeData.id:" + globalData.SelectedEdgeData.id);
        edgedata = populateEdgeData(globalData.SelectedEdgeData.id);
    }
    clearDataContainers();
    populateDOMelementsOfEdgeData(edgedata);
    populateDOMelementsOfOrphanData();
}

function updateEdgeDataContainer(myedgeid, containerid){
    if(myedgeid){
            const container = document.getElementById(containerid);
            const edgedatacontainer = container.childNodes[0];
            const edgedatacontainersibling = edgedatacontainer.nextSibling;
            edgedatacontainer.parentNode.removeChild(edgedatacontainer);
            const edgedata = populateEdgeData(myedgeid);
            const edcb = new EdgeDataContainerBuilder(edgedata);
            globalData.SelectedEdgeData = edgedata;
            globalSelectedEdge = getElementById(myedgeid, globalData.GraphEdgeDatas);
            container.insertBefore(edcb.getCreatedDOM(),edgedatacontainersibling);    
        }
}
function clearDataContainers(){
    const datacontainer = document.getElementById("datacontainers");
    let last = datacontainer.lastChild;
    while(last != undefined){
        globalDOMelementStack.push(last);
        datacontainer.removeChild(last);
        last = datacontainer.lastChild;
    }
}
function showDataContainers(){
    const datacontainer = document.getElementById("datacontainers");
    while(globalDOMelementStack.size() > 0) {
        datacontainer.appendChild(globalDOMelementStack.pop());
    }
}
function clearUploadModalElements(){
    document.querySelectorAll("#uploadModal input[type=text]").forEach(elem =>{
        elem.value="";
    });
    document.querySelector("#uploadModal input[type=file]").value = "";
    document.getElementById("preview").innerHTML = "";
}

async function populateDOMelementsOfEdgeData(edgedata){
    const tbuseremail = document.getElementById("useremail");
    if(globalUser.email && globalUser.email != ""){
		tbuseremail.value = globalUser.email;
    }
	tbuseremail.disabled = true;
    const hiddenuserid = document.getElementById("hiddenuserid");
    if(hiddenuserid.value && hiddenuserid.value != ""){
        globalUser.id = hiddenuserid.value;
    }
    const datacontainer = document.getElementById("datacontainers");
    const myedgedata = await edgedata;
    if(myedgedata){
        const edcb = new EdgeDataContainerBuilder(myedgedata);
        if(hasChildByIdStaringWith("DOMedc",datacontainer)){
            removeChildByIdStaringWith("DOMedc", datacontainer);
            datacontainer.insertBefore(edcb.getCreatedDOM(),datacontainer.firstChild);    
        } else {
            datacontainer.appendChild(edcb.getCreatedDOM());
        }
    }
}
async function populateDOMelementsOfOrphanData(){
    const datacontainer = document.getElementById("datacontainers");
    const orphandescids = await globalData.OrphanDescriptionIds;
    const orphaninvelemids = await globalData.OrphanInventoryElementIds;
    const orphanpicids = await globalData.OrphanPictureIds;
    if(!hasChildByIdStaringWith("DOMudc",datacontainer)){
        const udcb = new UserDescriptionContainerBuilder(globalUser.id);
        if(orphandescids && orphandescids.length > 0){
            for(const descid of orphandescids){
                const descelem = globalData.getDescData(descid);
                const dicb = new DescInfoContainerBuilder(descelem);
                udcb.addElement(dicb);
            }
            udcb.addLinkTextWithHandler("Új leírás", "addudesc", handleUserDescAppendLinkClick);
            datacontainer.appendChild(udcb.getCreatedDOM());    
        } else {
            udcb.addLinkTextWithHandler("Új leírás", "addudesc", handleUserDescAppendLinkClick);
            datacontainer.appendChild(udcb.getCreatedDOM());
        }
    }
    if(!hasChildByIdStaringWith("DOMuic",datacontainer)){
        const uicb = new UserInventoryContainerBuilder(globalUser.id);
        if(orphaninvelemids && orphaninvelemids.length > 0){
            for(const elemid of orphaninvelemids){
                const invelem = globalData.getInvElemData(elemid);
                const iecb = new InventoryElementContainerBuilder(invelem, "DOMtbie");
                uicb.addElement(iecb);
            }
            uicb.addLinkTextWithHandler("Új tárgy", "addui", handleUserInventoryAppendLinkClick);
            datacontainer.appendChild(uicb.getCreatedDOM());
        } else {
            uicb.addLinkTextWithHandler("Új tárgy", "addui", handleUserInventoryAppendLinkClick);
            datacontainer.appendChild(uicb.getCreatedDOM());
        }
    }
    if(!hasChildByIdStaringWith("DOMupc",datacontainer)){
        const upcb = new UserPictureContainerBuilder(globalUser.id);
        if(orphanpicids != undefined && orphanpicids.length > 0){
            for(const picid of orphanpicids){
                const picinfo = await globalData.getPicData(picid);
                if(picinfo){
                    const picdata = new PictureData(picinfo, globalPicWidth);
                    const phb = new PictureHolderBuilder(picdata);
                    upcb.addElement(phb);
                }
            }
            upcb.addLinkTextWithHandler("Új kép", "addupic", handleUserPictureAppendLinkClick);
            datacontainer.appendChild(upcb.getCreatedDOM());
        } else {
            upcb.addLinkTextWithHandler("Új kép", "addupic", handleUserPictureAppendLinkClick);
            datacontainer.appendChild(upcb.getCreatedDOM());
        }
    }
    const btnCloseModal = document.getElementById("btnCloseModal");
    btnCloseModal.addEventListener("click", function(ev){
        document.getElementById("preview").innerHTML = "";
        $("#uploadModal").modal("hide");
    }, false);
    const btnFileUpload = document.getElementById("btnfileupload");
    btnFileUpload.addEventListener("click", handleUploadFileClick, false);
}

class EdgeDataContainerBuilder{
    constructor(edgedata){
        this.id = "DOMedc" + edgedata.id;
        this.edgedata = edgedata;
        this.classValue = "edgedatacontainer"
        this.DOMpicturecontainer = {};
        this.DOMinventorycontainer = {};
        this.DOMelement = {};
        this.elementsclassValueinline = "inline";
        this.elementsclassValueblock = "block";
    }
    getCreatedDOM(){
        this.DOMelement = document.createElement("DIV");
        this.DOMelement.setAttribute("id", this.id);
        this.DOMelement.classList.add(this.classValue);
                
        const DOMsetlabeldiv = document.createElement("DIV");
        const DOMtextdivlabel = document.createElement("DIV");
        DOMtextdivlabel.innerHTML = "Cimke:";
        DOMtextdivlabel.classList.add(this.elementsclassValueinline);
        DOMsetlabeldiv.appendChild(DOMtextdivlabel);
        const DOMtextboxlabel = document.createElement("INPUT");
        DOMtextboxlabel.setAttribute("type", "text");
        DOMtextboxlabel.setAttribute("value", this.edgedata.label);
        DOMtextboxlabel.setAttribute("id", "tblabel" + this.edgedata.id);
        DOMtextboxlabel.classList.add(this.elementsclassValueinline);
        DOMtextboxlabel.classList.add("label");
        DOMtextboxlabel.addEventListener("keydown", handleTextBoxChanged, false);
        DOMsetlabeldiv.appendChild(DOMtextboxlabel);
        const DOMlinksavelabel = document.createElement("A");
        DOMlinksavelabel.innerHTML = "Mentés";
        DOMlinksavelabel.setAttribute("id", "slabel" + this.edgedata.id);
        DOMlinksavelabel.classList.add(this.elementsclassValueinline);
        DOMlinksavelabel.addEventListener("click", handleLabelSaveLinkClick, false);
        DOMsetlabeldiv.appendChild(DOMlinksavelabel);
        this.DOMelement.appendChild(DOMsetlabeldiv);

        const DOMsettimediv = document.createElement("DIV");
        DOMsettimediv.classList.add(this.elementsclassValueinline);
        const DOMtextdivtime1 = document.createElement("DIV");
        DOMtextdivtime1.innerHTML = "Idő 1:";
        DOMtextdivtime1.classList.add(this.elementsclassValueinline);
        DOMsettimediv.appendChild(DOMtextdivtime1);
        const DOMtextboxtime1 = document.createElement("INPUT");
        DOMtextboxtime1.setAttribute("type", "text");
        DOMtextboxtime1.setAttribute("value", this.edgedata.time1);
        DOMtextboxtime1.setAttribute("id", "tbtime1" + this.edgedata.id);
        DOMtextboxtime1.classList.add(this.elementsclassValueinline);
        DOMtextboxtime1.classList.add("time");
        DOMtextboxtime1.addEventListener("keydown", handleTextBoxChanged, false);
        DOMsettimediv.appendChild(DOMtextboxtime1);
        const DOMtextdivtime2 = document.createElement("DIV");
        DOMtextdivtime2.innerHTML = "&nbsp&nbspIdő 2:";
        DOMtextdivtime2.classList.add(this.elementsclassValueinline);
        DOMsettimediv.appendChild(DOMtextdivtime2);
        const DOMtextboxtime2 = document.createElement("INPUT");
        DOMtextboxtime2.setAttribute("type", "text");
        DOMtextboxtime2.setAttribute("value", this.edgedata.time2);
        DOMtextboxtime2.setAttribute("id", "tbtime2" + this.edgedata.id);
        DOMtextboxtime2.classList.add(this.elementsclassValueinline);
        DOMtextboxtime2.classList.add("time");
        DOMtextboxtime2.addEventListener("keydown", handleTextBoxChanged, false);
        DOMsettimediv.appendChild(DOMtextboxtime2);
        const DOMlinksavetime = document.createElement("A");
        DOMlinksavetime.innerHTML = "Mentés";
        DOMlinksavetime.setAttribute("id", "stime" + this.edgedata.id);
        DOMlinksavetime.classList.add(this.elementsclassValueinline);
        DOMlinksavetime.addEventListener("click", handleTimeSaveLinkClick, false);
        DOMsettimediv.appendChild(DOMlinksavetime);
        this.DOMelement.appendChild(DOMsettimediv);
        
        let description = {};
        if(this.edgedata.description == undefined){
            /*//AJAX call
            description.text = globalEMPTYDESC;
            const upON = {
                descdto : description,
                useremail : globalUser.email,
                entityid : this.edgedata.id
            }
            const doWithResponse = async (json) => {
                const desc = await json;
                description.id = await desc.id;
            }
            console.log("new description id: " + )
            getNewDescFromServer(upON, doWithResponse);
            */
           console.log("edgedata.description is undefined in EdgeDataContainerBuilder");
        } else {
            description = this.edgedata.description;
            const dicb = new DescInfoContainerBuilder(description);
            dicb.isOrphan = false;
            this.DOMelement.appendChild(dicb.getCreatedDOM());
        }
        const eicb = new EdgeInventoryContainerBuilder(this.edgedata.id);
        if(this.edgedata.inventory != undefined && this.edgedata.inventory.length > 0){
            for(let invelem of this.edgedata.inventory){
                const iecb = new InventoryElementContainerBuilder(invelem,"DOMtbie");
                iecb.isOrphan = false;   
                eicb.addElement(iecb);
            }
        }
        eicb.addLinkTextWithHandler("Új tárgy", "addeie", handleEdgeInventoryAppendLinkClick);
        this.DOMelement.appendChild(eicb.getCreatedDOM());
        const epcb = new EdgePictureContainerBuilder(this.edgedata.id);
        if(this.edgedata.pictures != undefined && this.edgedata.pictures.length > 0){
            for(let picelem of this.edgedata.pictures){
                const picdata = new PictureData(picelem, globalPicWidth);
                const phb = new PictureHolderBuilder(picdata); 
                phb.isOrphan = false; 
                epcb.addElement(phb); 
            }
        }
        epcb.addLinkTextWithHandler("Új kép", "addepic", handleEdgePictureAppendLinkClick);
        this.DOMelement.appendChild(epcb.getCreatedDOM());
        return this.DOMelement;
    }
}
class DescInfoContainerBuilder{
    constructor(descriptiondata){
        this.id = "dic" + descriptiondata.id;
        this.descriptiondata = descriptiondata;
        this.DOMelement = {};
        this.classValue = "descriptioncontainer";
        this.heightinchars = 4;
        this.elementsclassValueinline = "inline";
        this.elementsclassValueblock = "block";
        this.isOrphan = true;
    }
    getCreatedDOM(){
        this.DOMelement = document.createElement("DIV");
        this.DOMelement.setAttribute("id", this.id);
        this.DOMelement.setAttribute("isorphan", this.isOrphan);
        this.DOMelement.classList.add(this.classValue);
        
        const DOMtextarea = document.createElement("TEXTAREA")
        DOMtextarea.setAttribute("rows", this.heightinchars);
        DOMtextarea.setAttribute("cols", this.widthinchars);
        DOMtextarea.setAttribute("id", "DOMdesc_ta" + this.descriptiondata.id);
        DOMtextarea.value = this.descriptiondata.text;
        DOMtextarea.addEventListener("keydown", handleTextBoxChanged, false);
        
        const DOMdeletelink = document.createElement("A");
        DOMdeletelink.innerHTML = "Eltávolít";
        DOMdeletelink.setAttribute("id", "deldesc"+this.descriptiondata.id);
        DOMdeletelink.addEventListener("click", handleDescriptionDeleteLinkClick, false);

        const DOMupdatelink = document.createElement("A");
        DOMupdatelink.innerHTML = "Mentés";
        DOMupdatelink.setAttribute("id", "upddesc"+this.descriptiondata.id);
        DOMupdatelink.addEventListener("click", handleDescriptionUpdateLinkClick, false);
        
        const DOMattachdesclink = document.createElement("A");
        DOMattachdesclink.innerHTML = "Tevékenységhez";
        DOMattachdesclink.setAttribute("id", "attdesc" + this.descriptiondata.id);
        DOMattachdesclink.classList.add(this.elementsclassValueinline);
        DOMattachdesclink.addEventListener("click", handleDescriptionAttachLinkClick, false);
        
        const DOMactiondiv = document.createElement("DIV");
        DOMactiondiv.classList.add(this.elementsclassValueblock);
        DOMactiondiv.appendChild(DOMupdatelink);
        DOMactiondiv.appendChild(DOMdeletelink);
        DOMactiondiv.appendChild(DOMattachdesclink);
        if(!this.isOrphan){
            DOMattachdesclink.style.pointerEvents = "none";    
        }
        else {
            DOMattachdesclink.style.pointerEvents = "auto";
        }

        this.DOMelement.appendChild(DOMtextarea);
        this.DOMelement.appendChild(DOMactiondiv);
        return this.DOMelement;
    }
}
class InventoryElementContainerBuilder{
    constructor(inventoryelementdata, innertextboxpretext, deleteword){
        this.id = 'iec' + inventoryelementdata.id;
        this.inventoryelement = inventoryelementdata;
        this.DOMelement = {};
        this.elementsclassValueinline = "inline";
        this.elementsclassValueblock = "block";
        this.classValue = "inventoryelementcontainer";
        this.isOrphan = true;
        this.innerTextBoxPretxt = innertextboxpretext;  
    }
    getCreatedDOM(){
        this.DOMelement = document.createElement("DIV");
        this.DOMelement.setAttribute("id", this.id);
        
        const DOMtextbox = document.createElement("INPUT")
        DOMtextbox.setAttribute("type", "text");
        DOMtextbox.setAttribute("value", this.inventoryelement.text);
        DOMtextbox.setAttribute("id", this.innerTextBoxPretxt + this.inventoryelement.id);
        //DOMtextbox.setAttribute("id", "DOMie" + this.inventoryelement.id);
        DOMtextbox.classList.add(this.elementsclassValueinline);
        DOMtextbox.classList.add("label");
        DOMtextbox.addEventListener("keydown", handleTextBoxChanged, false);
        this.DOMelement.appendChild(DOMtextbox);
        const DOMactiondiv = document.createElement("DIV");
        DOMactiondiv.classList.add(this.elementsclassValueinline);
        const DOMupdatelink = document.createElement("A");
        DOMupdatelink.innerHTML = "Mentés";
        DOMupdatelink.setAttribute("id", "updie" + this.inventoryelement.id);
        DOMupdatelink.classList.add(this.elementsclassValueinline);
        DOMupdatelink.addEventListener("click", handleInventoryElemUpdateLinkClick, false);
        DOMactiondiv.appendChild(DOMupdatelink);
        const DOMdeletelink = document.createElement("A");
        DOMdeletelink.innerHTML = "Eltávolít";
        DOMdeletelink.setAttribute("id", "delie"+this.inventoryelement.id);
        DOMdeletelink.classList.add(this.elementsclassValueinline);
        DOMdeletelink.addEventListener("click", handleInventoryElemDeleteLinkClick, false);
        DOMactiondiv.appendChild(DOMdeletelink);
        const DOMattachinvelemlink = document.createElement("A");
        DOMattachinvelemlink.innerHTML = "Tevékenységhez";
        DOMattachinvelemlink.setAttribute("id", "attie" + this.inventoryelement.id);
        DOMattachinvelemlink.classList.add(this.elementsclassValueinline);
        DOMattachinvelemlink.addEventListener("click", handleInventoryElemAttachLinkClick, false);
        DOMactiondiv.appendChild(DOMattachinvelemlink);
        if(!this.isOrphan){
            DOMattachinvelemlink.style.pointerEvents = "none";
        }
        else {
            DOMattachinvelemlink.style.pointerEvents = "auto";
        }
        this.DOMelement.appendChild(DOMactiondiv);
        return this.DOMelement;
    }
}
class PictureHolderBuilder{
    constructor(picturedata){
        this.id = 'ph' + picturedata.id;
        this.picturedata = picturedata;
        this.classValue = "pictureholder";
        this.DOMelement = {};
        this.imagewidth = picturedata.width;
        this.elementsclassValueinline = "inline";
        this.elementsclassValueblock = "block";
        this.imagesrc = (picturedata.imagesrc == undefined ? dummypicurl : (globalEndpoint.getPicture + picturedata.imagesrc)); 
        this.isOrphan = true;
    }
    getCreatedDOM(){
        this.DOMelement = document.createElement("DIV");
        this.DOMelement.setAttribute("id", this.id);
        this.DOMelement.classList.add(this.classValue);
        this.DOMelement.setAttribute("isorphan", this.isOrphan);        
        const DOMimage = document.createElement("IMG");
        DOMimage.src = this.imagesrc;
        DOMimage.style.width = this.imagewidth;
        DOMimage.classList.add(this.elementsclassValueblock);
        const DOMtextelement = document.createElement("DIV");
        DOMtextelement.innerHTML = this.picturedata.id;
        DOMtextelement.classList.add(this.elementsclassValueblock);
        const DOMdeletelink = document.createElement("A");
        DOMdeletelink.innerHTML = "Eltávolít";
        DOMdeletelink.setAttribute("id", "delpic" + this.picturedata.id);
        DOMdeletelink.classList.add(this.elementsclassValueinline);
        DOMdeletelink.addEventListener("click", handlePictureDeleteLinkClick, false);
        const DOMupdatelink = document.createElement("A");
        DOMupdatelink.innerHTML = "Módosít";
        DOMupdatelink.setAttribute("id", "updpic" + this.picturedata.id);
        DOMupdatelink.classList.add(this.elementsclassValueinline);
        DOMupdatelink.addEventListener("click", handlePictureUpdateLinkClick, false);
        const DOMattachpiclink = document.createElement("A");
        DOMattachpiclink.innerHTML = "Tevékenységhez";
        DOMattachpiclink.setAttribute("id", "attpic"+this.picturedata.id);
        DOMattachpiclink.classList.add(this.elementsclassValueinline);
        DOMattachpiclink.addEventListener("click", handlePictureAttachLinkClick, false);
        const DOMactiondiv = document.createElement("DIV");
        DOMactiondiv.classList.add(this.elementsclassValueblock);
        DOMactiondiv.appendChild(DOMupdatelink);
        DOMactiondiv.appendChild(DOMdeletelink);
        DOMactiondiv.appendChild(DOMattachpiclink);
        if(!this.isOrphan){
            DOMattachpiclink.style.pointerEvents = "none";
        }

        this.DOMelement.appendChild(DOMimage);
        this.DOMelement.appendChild(DOMtextelement);
        this.DOMelement.appendChild(DOMactiondiv);

        return this.DOMelement;
    }
}
class SelectEdgeContainerBuilder{
    constructor(edges, entityid){
        this.id = "att2edgediv" + entityid;
        this.edges = edges;
        this.DOMelement = {};
        this.classValue = "selectedgecontainer";
    }
    getCreatedDOM(){
        this.DOMelement = document.createElement("DIV");
        this.DOMelement.classList.add(this.classValue);
        this.DOMelement.setAttribute("id", this.id);
        console.log("globalGraph in method getCreatedDOM in class SelectEdgeContainerBuilder:");
        console.log(globalGraph);
        
        const DOMselectedge = document.createElement("SELECT");
        DOMselectedge.setAttribute("id", "attachtoedgeselect");
        if(globalGraph.graph != undefined && globalGraph.graph.edges != undefined){
            for(const edge of globalGraph.graph.edges){
                const DOMoption = document.createElement("OPTION");
                DOMoption.text = edge.label;
                DOMoption.value = edge.label;
                DOMselectedge.appendChild(DOMoption);
            }
        }
        
        this.DOMelement.appendChild(DOMselectedge);

        return this.DOMelement; 
    }    
}
class ElementContainerBuilder{
    constructor(id, entityId, classValue, linkContainerClassValue){
        this.entityId = entityId;
        this.elements = [];
        this.classValue = classValue;
        this.linkContainerClassValue = linkContainerClassValue;
        this.linkContainer = document.createElement("DIV");
        this.linkContainer.setAttribute("id","append" + id);
        this.linkContainer.classList.add(this.linkContainerClassValue);
        this.linkandhandlers = [];
        this.id = id;
    }
    addElement(element){
        this.elements.push(element);
    }
    addLinkTextWithHandler(LinkText, LinkIdPretxt, HandlerFunction){
        const entry = {};
        entry.linktext = LinkText;
        entry.handlerfunction = HandlerFunction;
        entry.linkidpretxt = LinkIdPretxt;
        this.linkandhandlers.push(entry);
    }
    getCreatedDOM(){
        const DOMelement = document.createElement("DIV");
        DOMelement.setAttribute("id", this.id);
        DOMelement.classList.add(this.classValue);

        for(const elem of this.elements){
            DOMelement.appendChild(elem.getCreatedDOM());
        }
        for(const entry of this.linkandhandlers){
            const DOMlinkelem = document.createElement("A");
            DOMlinkelem.innerHTML = entry.linktext;
            DOMlinkelem.setAttribute("id", entry.linkidpretxt + this.entityId);
            DOMlinkelem.addEventListener("click", entry.handlerfunction, false);
            DOMlinkelem.classList.add("actionlink");
            this.linkContainer.appendChild(DOMlinkelem);
        }

        DOMelement.appendChild(this.linkContainer);
        return DOMelement;
    }
}
class EdgePictureContainerBuilder extends ElementContainerBuilder{
    constructor(entityId){
        let classValue = "edgepicturecontainer";
        let linkContainerClassValue = "listlinkcontainer";
        let id = "DOMepc" + entityId;
        super(id, entityId, classValue, linkContainerClassValue);
    }
}
class EdgeInventoryContainerBuilder extends ElementContainerBuilder{
    constructor(entityId){
        let classValue = "edgeinventorycontainer";
        let linkContainerClassValue = "listlinkcontainer";
        let id = "DOMeic" + entityId;
        super(id, entityId, classValue, linkContainerClassValue);
    }
}
class UserPictureContainerBuilder extends ElementContainerBuilder{
    constructor(entityId){
        let classValue = "userpicturecontainer";
        let linkContainerClassValue = "listlinkcontainer";
        let id = "DOMupc";
        super(id, entityId, classValue, linkContainerClassValue);
    }
}
class UserInventoryContainerBuilder extends ElementContainerBuilder{
    constructor(entityId){
        let classValue = "userinventorycontainer";
        let linkContainerClassValue = "listlinkcontainer";
        let id = "DOMuic";
        super(id, entityId, classValue, linkContainerClassValue);
    }
}
class UserDescriptionContainerBuilder extends ElementContainerBuilder{
    constructor(entityId){
        let classValue = "userdescriptioncontainer";
        let linkContainerClassValue = "listlinkcontainer";
        let id = "DOMudc";
        super(id, entityId, classValue, linkContainerClassValue);
    }
}
class AttachElementToEdgeBuilder{
    constructor(elementdataid, elementtype, handlerfunction){
        this.id = "att_" + elementtype + elementdataid;
        this.elementdataid = elementdataid;
        this.handlerfunction = handlerfunction;
        this.elementtype = elementtype;
        this.classValueinline = "inline";
    }
    getCreatedDOM(){
        const DOMelement = document.createElement("DIV");
        DOMelement.classList.add(this.classValueinline);
        DOMelement.setAttribute("id", this.id);
        const secb = new SelectEdgeContainerBuilder(globalGraph.graph.getEdges(), 
                            this.elementdataid);
        DOMelement.appendChild(secb.getCreatedDOM());
        const DOMattachlink = document.createElement("A");
        DOMattachlink.innerHTML = "Csatol";
        DOMattachlink.setAttribute("id", "attlink" + this.elementtype + "_" + this.elementdataid);
        DOMattachlink.addEventListener("click", this.handlerfunction, false);
        const DOMcancellink = document.createElement("A");
        DOMcancellink.innerHTML = "Mégsem";
        DOMcancellink.addEventListener("click", function(ev){
               const thisDOMelem = ev.target.parentNode;
               const parent = thisDOMelem.parentNode;
               thisDOMelem.previousSibling.lastChild.style.pointerEvents = "auto";
               parent.removeChild(thisDOMelem); 
        }, false); 
        DOMelement.appendChild(DOMattachlink);
        DOMelement.appendChild(DOMcancellink);
        return DOMelement;
    }    
}
async function updateDOMSelectNodeFromTo(DOMSelectId1, DOMSelectId2){
    const selectnodefrom = document.getElementById(DOMSelectId1);
    const selectnodeto = document.getElementById(DOMSelectId2);
    let last = selectnodefrom.lastChild;
    while (last != null && last.nodeName == 'OPTION')   {
        selectnodefrom.removeChild(last);
        last = selectnodefrom.lastChild;
    }
    
    last = selectnodeto.lastChild;
    while (last != null && last.nodeName == 'OPTION')   {
        selectnodeto.removeChild(last);
        last = selectnodeto.lastChild;
    }
    let j = 0;
    const mygraph = await globalGraph.graph; 
    if(mygraph && mygraph.getNodes()){
        for(const node of mygraph.getNodes()){
            const option1 = document.createElement("option");
            option1.value = node.label;
            option1.innerHTML = node.label;
            if(!(node.isFinish || node.isEntry)){
                selectnodefrom.appendChild(option1);
                const option2 = document.createElement("option");
                option2.value = node.label;
                option2.innerHTML = node.label;
                selectnodeto.appendChild(option2);    
            }
            else if(node.isEntry){
                selectnodefrom.appendChild(option1);
            }
            else if(node.isFinish){
            selectnodeto.appendChild(option1);
            }
        }
    }    
}
async function updateDOMSelectNodeFromToByIndex(DOMSelectId1, DOMSelectId2){
    const selectnodefrom = document.getElementById(DOMSelectId1);
    const selectnodeto = document.getElementById(DOMSelectId2);
    let last = selectnodefrom.lastChild;
    while (last != null && last.nodeName == 'OPTION')   {
        selectnodefrom.removeChild(last);
        last = selectnodefrom.lastChild;
    }
    
    last = selectnodeto.lastChild;
    while (last != null && last.nodeName == 'OPTION')   {
        selectnodeto.removeChild(last);
        last = selectnodeto.lastChild;
    }
    let j = 0;
    const mygraph = await globalGraph.graph; 
    if(mygraph && mygraph.getNodes()){
        for(const node of mygraph.getNodes()){
            const option1 = document.createElement("option");
            option1.value = node.id;
            option1.innerHTML = node.label;
            if(!(node.isFinish || node.isEntry)){
                selectnodefrom.appendChild(option1);
                const option2 = document.createElement("option");
                option2.value = node.id;
                option2.innerHTML = node.label;
                selectnodeto.appendChild(option2);    
            }
            else if(node.isEntry){
                selectnodefrom.appendChild(option1);
            }
            else if(node.isFinish){
            selectnodeto.appendChild(option1);
            }
        }
    }    
}
const updateDOMSelectGraphName = async function (DOMSelectId, namedatalist){
    const selectgraphname = document.getElementById(DOMSelectId);
    let last = selectgraphname.lastChild;
    if(last != null){
        while (last != null && last.nodeName == 'OPTION')   {
            selectgraphname.removeChild(last);
            last = selectgraphname.lastChild;
        }
    }
    const namedatas = await namedatalist;
    if(namedatas){
        for(const namedata of namedatas){
            const option1 = document.createElement("option");
            option1.value = namedata.id;
            option1.innerHTML = namedata.name;
            selectgraphname.appendChild(option1);
        }
    }
}
function updateDOMSelectEdge(DOMSelectId1){
    const selectedge = document.getElementById(DOMSelectId1);
    if(selectedge){
        let last = selectedge.lastChild;
        while (last != null && last.nodeName == 'OPTION')   {
            selectedge.removeChild(last);
            last = selectedge.lastChild;
        }
        let j = 0;
        for(const edge of globalGraph.graph.getEdges()){
            const option1 = document.createElement("option");
            option1.value = edge.label;
            option1.innerHTML = edge.label;
            selectedge.appendChild(option1);
        }
    }
}

function hideElementById(id){
    const DOMelem = document.getElementById(id);
    /*
    DOMelem.classList.remove("inline");
    DOMelem.classList.remove("block");
    DOMelem.classList.add("hide");
    console.log("I hid " + id);
    */
    if(DOMelem){
        DOMelem.style.display = "none";
    }
}

function showElementById(id, disp_type){
    const DOMelem = document.getElementById(id);
    /*
    DOMelem.classList.remove("hide");
    DOMelem.classList.add(disp_type);
    */
    if(DOMelem){
        DOMelem.style.display = disp_type;
    }
}

function selectNodeFromNodeToChange(event, DOM_id1, DOM_id2){
    const DOM_elem1 = document.getElementById(DOM_id1);
    const DOM_elem2 = document.getElementById(DOM_id2);
    idx1 = DOM_elem1.selectedIndex;
    idx2 = DOM_elem2.selectedIndex;
    node1 = globalGraph.graph.getNodeByLabel(DOM_elem1.options[idx1].value);
    node2 = globalGraph.graph.getNodeByLabel(DOM_elem2.options[idx2].value);
    if(node1.label == node2.label || globalGraph.graph.isEdgeExist(node1, node2)){
        hideElementById("btncredge");
    }
    else {
        showElementById("btncredge", "inline");
    }
}

function selectGraphnameChange(event, DOM_id, getgraphasstring){
    const DOMgraphnamelist = document.getElementById("selectpublicgraph");    
    const idx = DOMgraphnamelist.selectedIndex;
    const changedgraphid = DOMgraphnamelist.options[idx].value;
    const doWithResponse = async (data) => {
        const resp = await data;
        if(resp){
            writeInnerHTML(DOM_id, resp);
            document.getElementById("linkcopygraph").style.display = "inline";
            hideNonApplicableDOMElements();
        }
    };
    getGraphStringFromServer(changedgraphid, doWithResponse);
}
function selectOwnGraphnameChange(event, DOM_id){
    const DOMgraphnamelist = document.getElementById("selectowngraph");    
    const idx = DOMgraphnamelist.selectedIndex;
    const changedgraphid = DOMgraphnamelist.options[idx].value;
    const doWithResponse = async (data) => {
        const resp = await data;
        if(resp){
            writeInnerHTML(DOM_id, resp);
            document.getElementById("linkeditgraph").style.display ="inline";
        }
    };
    getGraphStringFromServer(changedgraphid, doWithResponse);
    globalNodeCrBinIsEmpty = true;
    setTimeout(function(){
        linkEditGraphClick(event,"graphname","graphid", true);
    },500);
    
}
function writeInnerHTML(DOM_id, text){
    const DOM_elem = document.getElementById(DOM_id);
    DOM_elem.innerHTML = text.replaceAll("\n","</br>");
}

async function btnSaveGraphClick(){
    const origtervnev = globalActiveGraphData.name;
    const beirttervnev = document.getElementById("graphname").value;
    const graphid = document.getElementById("graphid").value;
    if(beirttervnev.length > 0 && origtervnev != beirttervnev){
        globalActiveGraphData.name = beirttervnev;
        /*
        let graphdataitem = getElementById(graphid, globalOwnGraphNameList);
        if(graphdataitem){
            graphdataitem.name = beirttervnev;
        }
        */
        // AJAX request for graphdata update
        const dto = {
            id : graphid, 
            name : beirttervnev,
            nodeId : globalActiveGraphData.nodeId,
            publikus : globalActiveGraphData.publikus
        };
        const upON = {
            graphdatadto : dto,
            useremail : globalUser.email
        };
        const doWithResponse = async (json) => {
            const resp = await json;
            if(resp.name){
                // another AJAX call
                const doWithResponse = async (json) => {
                    //updateDOMSelectGraphName("selectowngraph", json);
                    globalOwnGraphNameList = await json;
                    updateDOMSelectGraphName("selectowngraph", globalOwnGraphNameList);
                };
                await getGraphDataListByUserFromServer(globalUser.email, doWithResponse);
                
                document.getElementById("graphname").classList.remove("changedbackground");
                if(resp.name != beirttervnev){
                    document.getElementById("graphname").value = resp.name;
                    globalActiveGraphData.name = resp.name;
                    console.log(tervnev + "-t a szerver " + resp.name + "-re cserélte");
                } else {
                    console.log(beirttervnev + " nevu terv mentve");
                }
            }
        };
        updateGraphData(upON, doWithResponse);
    }
}

function btnDelGraphClick(){
    const DOMgraphnamelist = document.getElementById("selectowngraph");    
    const idx = DOMgraphnamelist.selectedIndex;
    const graphid = DOMgraphnamelist.options[idx].value;
    const tervnev = getElementById(graphid, globalOwnGraphNameList).name;
    const delgraph = confirm("Biztosan törli a\n\n" + tervnev + "\n\nmegnevezésű tervet?");
    if(delgraph){
        //AJAX calls
        const upON = {
            useremail: globalUser.email,
            entityid: graphid
        };
        const doWithResponse = async (text) => {
            const resp = await text;
            if(resp){
                console.log(resp, "Server message");
                const dWR1 = async (jsondata) => {
                    globalOwnGraphNameList = await jsondata;
                    updateDOMSelectGraphName("selectowngraph", globalOwnGraphNameList);
                    document.getElementById("graphdatadetails").innerHTML = resp;
                    showElementById("linkeditgraph", "inline")
                    hideElementById("dijkstra");
                    hideElementById("makeedge");
                    hideElementById("behaviormod");
                    hideElementById("draw-shapes");
                    hideElementById("msgFromKonvaEvent");
                    hideElementById("datacontainers");

                };
                getGraphDataListByUserFromServer(globalUser.email, dWR1);
            }
        };
        deleteGraphFromServer(upON, doWithResponse);
        const dWR2 = async (text) => {
            const resp = await text;
            if(resp == "true"){
                console.log(resp, "publikus?");
                const dWR3 = async (jsondata) => {
                    globalGraphDataDTOList = await jsondata;
                    updateDOMSelectGraphName("selectpublicgraph", globalGraphDataDTOList);
                };
                getGraphDataListFromServer(dWR3);
            }
        }
        isPublicGraphFromServer(upON, dWR2);
    }
}

function linkCopyGraphClick(event, DOM_id, DOM2_id){
    const DOMgraphnamelist = document.getElementById("selectpublicgraph");   
    const DOMothergraphnamelist = document.getElementById("selectowngraph");    
    const DOM_elem = document.getElementById(DOM_id);
    const DOM2_elem = document.getElementById(DOM2_id);
    const DOM_ispublic = document.getElementById("cbIsPublic");
    const idx = DOMgraphnamelist.selectedIndex;
    const changedgraphname = getElementById(DOMgraphnamelist.options[idx].value, globalGraphNameList).name;
    const ispublic = getElementById(DOMgraphnamelist.options[idx].value, globalGraphNameList).publikus;
    const graphid = DOMgraphnamelist.options[idx].value;
    const useremail = document.getElementById("useremail").value;
    // AJAX call
    const doWithResponse = async (json) => {
        const newgraphid = await json.id;
        const newgraphname = await json.name;
        DOM2_elem.value = newgraphid;
        DOM_elem.value = newgraphname;
        globalActiveGraphData.id = newgraphid;
        globalActiveGraphData.name = newgraphname;
        globalActiveGraphData.publikus = false;
        DOM_ispublic.checked = false;
        DOMothergraphnamelist.style.display = "none";
        globalCloneHappens = true;
        setVisibilityOfDOMElements();
        showElementById("draw-shapes", "block");
        showElementById("msgFromKonvaEvent", "block");
        showElementById("datacontainers", "block");
        showElementById("selectowngraph", "inline");    
        hideElementById("linkcopygraph");
        const myfunc = async (mygraphid) => {
            globalNodeCrBinIsEmpty = true;
            await updateCanvasAndGlobalData(mygraphid);
        }
        myfunc(newgraphid);
    };
    getCloneGraphFromServer(useremail, graphid, doWithResponse);
    
}
function linkEditGraphClick(event, DOM_id, DOM2_id, graphstringloaded){
    const list = document.getElementById("selectowngraph");   
    const DOMothergraphnamelist = document.getElementById("selectpublicgraph"); 
    const DOM_elem = document.getElementById(DOM_id);
    const DOM2_elem = document.getElementById(DOM2_id);
    const DOM_ispublic = document.getElementById("cbIsPublic");
    const idx = list.selectedIndex;
    const changedgraphname = getElementById(list.options[idx].value, globalOwnGraphNameList).name;
    const ispublic = getElementById(list.options[idx].value, globalOwnGraphNameList).publikus;
    DOM_elem.value = changedgraphname;
    DOM2_elem.value = list.options[idx].value;
    const graphid = DOM2_elem.value;
    globalActiveGraphData.id = graphid;
    globalActiveGraphData.name = changedgraphname;
    globalActiveGraphData.publikus = ispublic;
    DOM_ispublic.checked = ispublic;
    setVisibilityOfDOMElements();
    showElementById("draw-shapes", "block");
    showElementById("msgFromKonvaEvent", "block");
    showElementById("datacontainers", "block");    
    const myfunc1 = async () => {
        if(!graphstringloaded){
            const dWR = async (data) => {
                const resp = await data;
                if(resp){
                    writeInnerHTML("graphdatadetails", resp);
                }
            };
            getGraphStringFromServer(graphid, dWR);
        }
        await updateCanvasAndGlobalData(graphid);
    
    }
    myfunc1();
}
function hideNonApplicableDOMElements(){
    hideElementById("dijkstra");
    hideElementById("makeedge");
    hideElementById("behaviormod");
    hideElementById("draw-shapes");
    hideElementById("msgFromKonvaEvent");
    hideElementById("datacontainers");
}
function setVisibilityOfDOMElements(){
    document.getElementById("btnprintdg").disabled = false;
    document.getElementById("btncomputepath").disabled = false;
    
    showElementById("dijkstra", "block");
    showElementById("makeedge", "block");
    showElementById("behaviormod", "block");
}
function btnChangeEdgeColorClick(event, defaultcolor, color){
    const userinput = prompt("Írd be az élek id adatait vesszővel elválasztva");
    if(userinput){
        const inputedgeids = userinput.split(',');
        if (inputedgeids){
            setEdgeColorOnPath(defaultcolor, color, inputedgeids);
        }
    }
}
function setEdgeColorOnPath(defaultcolor, color, edgeids){
    if(globalGraph.graph.getEdges().length > 0 && edgeids.length > 0){
        const alledgeid = globalGraph.graph.getEdges().map(item=>{return item.id});
        globalGraph.graphView.changeColorOfEdgeViewsById(alledgeid, defaultcolor);
        globalGraph.graphView.changeColorOfEdgeViewsById(edgeids, color);
    }
}
function setEdgeColorAll(defaultcolor){
    if(globalGraph.graph.getEdges().length > 0){
        const alledgeid = globalGraph.graph.getEdges().map(item=>{return item.id});
        globalGraph.graphView.changeColorOfEdgeViewsById(alledgeid, defaultcolor);
    }
} 
function cbShowDataChange(event){
    if(event.target.checked){
        showDataContainers();
    } else {
        clearDataContainers();
    }
}

function btnCreateEdgeClick(){
    const DOMelem1 = document.getElementById('selectnodefrom');
    const DOMelem2 = document.getElementById('selectnodeto');   
    const idx1 = DOMelem1.options.selectedIndex;
    const idx2 = DOMelem2.options.selectedIndex;
    const optionfrom = DOMelem1.options[idx1].value;
    const optionto = DOMelem2.options[idx2].value;
    if(optionfrom != optionto){
        const fromnode = globalGraph.graph.getNodeByLabel(optionfrom);
        const tonode = globalGraph.graph.getNodeByLabel(optionto);
        if((!globalGraph.graph.isEdgeExist(fromnode, tonode)) || 
            (!globalGraph.graphView.mygraph.isEdgeExist(fromnode, tonode))){
            //AJAX call
            const dto = {
                id: null,
                label: null,
                fromNodeId: fromnode.id,
                toNodeId: tonode.id,
                time1: 0, 
                time2: 0,
                descriptionId: null,
                pictureIds: [],
                inventoryElementIds: []
            }
            const upON = {
                edgedto : dto,
                useremail : globalUser.email,
                entityid : globalActiveGraphData.id 
            }
            const doWithResponse = async (json) => {
                const newedgedto = await json;
                if (newedgedto != undefined ){
                    const edge = new Edge(newedgedto.id, fromnode, tonode);
                    edge.label = newedgedto.label;
                    fromnode.addOutgoingEdge(edge);
                    tonode.addIncomingEdge(edge);
                    globalGraph.graph.addEdge(edge);
                    globalGraph.graphView.updateEdges(tonode);
                    globalGraph.graphView.updateEdgeLabels(tonode);
                    const text = globalEMPTYDESC;
                    const descid = newedgedto.descriptionId; //getDescIdFromServer(edgedto.id, text);
                    globalData.addEdgeDTO(newedgedto.id, fromnode.id, tonode.id, descid);
                    console.log("description id: " + descid,"LOG in btnCreateEdgeClick()");
                    const descdata = new DescriptionData(descid, text);
                    globalData.appendDescToEdge(descdata, newedgedto.id);
                    updateDOMSelectEdge("selectedge");
                    updateDOMSelectEdge("attachtoedgeselect");
                }
            };
            getNewEdgeDTOFromServer(upON, doWithResponse);
        }
        else {
            alert("Már van kapcsolat a csomópontok közt");
        }
    }
}
function btnCreateNodeClick(){
    const graphid = globalActiveGraphData.id;
    const myfunc = async () => {
        console.log("in btnCreateNodeClick() globalLayer", globalLayer);
        await putNodeInCreator(globalLayer, {X : globalNewNodeCrPosX,
            Y : globalNewNodeCrPosY}, globalNode_R, graphid);
    };
    myfunc();
    //updateDOMSelectNodeFromTo("selectnodefrom", "selectnodeto");
    //updateDOMSelectNodeFromToByIndex("selectnodestart", "selectnodeend");
}
function cbDeleteEdgeDataChange(event){
    if(event.target.checked){
        globalDeleteEdgeData = true;
    } else {
        globalDeleteEdgeData = false;
    }
}
function cbIsPublicChange(event){
    if(event.target.checked){
        globalActiveGraphData.publikus = true;
    } else {
        globalActiveGraphData.publikus = false;
    }
    // a terv neve akkor is mentve lesz, ha a publikus 
    // checkboxra kattintott
    const beirttervnev = document.getElementById("graphname").value;
    const origname = globalActiveGraphData.name 
    let namechanged = false;
    if(beirttervnev != origname){
        namechanged = true;
    } 
    //AJAX call
    const dto = {
        id : globalActiveGraphData.id, 
        name : beirttervnev,
        nodeId : globalActiveGraphData.nodeId,
        publikus : event.target.checked
    };
    const upON = {
        graphdatadto : dto,
        useremail : globalUser.email
    };
    const doWithResponse = async (json) => {
        const resp = await json;
        if(resp.name){
            if(namechanged){
                document.getElementById("graphname").classList.remove("changedbackground");
                if(resp.name != beirttervnev){
                    document.getElementById("graphname").value = resp.name;
                    globalActiveGraphData.name = resp.name;
                    console.log(tervnev + "-t a szerver " + resp.name + "-re cserélte");
                }
                if(globalActiveGraphData.publikus){
                    appendOption(resp.name, dto.id, "selectpublicgraph");    
                    removeOption(origname, dto.id, "selectowngraph");
                    appendOption(resp.name, dto.id, "selectowngraph");
                } else {
                    removeOption(origname, dto.id, "selectpublicgraph");
                    removeOption(origname, dto.id, "selectowngraph");
                    appendOption(resp.name, dto.id, "selectowngraph");
                }   
            } else {
                if(globalActiveGraphData.publikus){
                    appendOption(origname, dto.id, "selectpublicgraph");    
                } else {
                    removeOption(origname, dto.id, "selectpublicgraph");
                }
            }
        }
    };
    updateGraphData(upON, doWithResponse);
}
function cbIsNodeCreateChange(event){
    if(event.target.checked){
        globalIsCreateNode = true;
        putNodeInCreator(globalLayer, {X : globalNewNodeCrPosX,
            Y : globalNewNodeCrPosY}, globalNode_R, globalActiveGraphData.id);
    } else {
        globalIsCreateNode = false;
    }
}
function btnExitClick(){
    window.location = "/logout";
}
function rbtnAlgoPathTypeChange(event, DOMname){
    const elements = document.getElementsByName(DOMname);
    console.log(elements.length, "LOG elements length");
    for(let k = 0; k < elements.length; ++k){
        if(elements[k].checked){
            globalAlgoPathType = k+1;
        }
    }
}
function btnPrintDGClick(){
    switch (globalAlgoPathType) {
        case 1:
            printDG("shortest");
            break;
        case 2:
            printDG("longest");
            break;
        default:
            break;
    }
}
async function redrawGraph(){
    const mygraph = await globalGraph.graph;
    if(mygraph){
        setEdgeColorAll("blue");
    } 
}
async function btnComputePathClick(){
    const DOMselectnodestart = document.getElementById("selectnodestart");   
    const DOMselectnodeend = document.getElementById("selectnodeend");    
    const idxstart = DOMselectnodestart.selectedIndex;
    const idxend = DOMselectnodeend.selectedIndex;
    const startnodeid = Number(DOMselectnodestart.options[idxstart].value);   
    const endnodeid = Number(DOMselectnodeend.options[idxend].value);
    const logba = {
        start : {idxstart : idxstart, value : startnodeid},
        end : {idxend : idxend, value : endnodeid}
    };
    const dcontainer = document.getElementById("pdijkstra");
    const mygraph = await globalGraph.graph;
    if(mygraph){
        const gc = new GraphConverter2DG(mygraph);
        let result = {}
        switch (globalAlgoPathType) {
            case 1:
                const gc1 = new GraphConverter2DG(mygraph, true);
                result = gc1.findShortestPath(gc1.dgraph1, startnodeid, endnodeid);
                break;
            case 2:
                const gc2 = new GraphConverter2DG(mygraph, false);
                result = gc2.findShortestPath(gc2.dgraph2, startnodeid, endnodeid);
                break;
            default:
                break;
        }
        dcontainer.innerHTML = JSON.stringify(result)/* + c.printMe(gc.dgraph1).replaceAll("\n","<br>")*/;
        document.getElementById("btnchangeedgecolor").disabled = false; 
        globalEdgePath = result["élek"];
        setEdgeColorOnPath("blue", "red", globalEdgePath);
    } else {
        dcontainer.innerHTML = "graph not loaded";
    }
    
}
async function btnMakeNewGraphClick(event, DOM_id, DOM2_id) {
    const DOM_elem = document.getElementById(DOM_id);
    const DOM2_elem = document.getElementById(DOM2_id);
    const DOM_ispublic = document.getElementById("cbIsPublic");
    const DOMowngraphnamelist = document.getElementById("selectowngraph");   
    const DOMpublicgraphnamelist = document.getElementById("selectpublicgraph"); 
    globalUser.email = document.getElementById("useremail").value;

    //AJAX Call
    const doWithResponse = async (json) => {
        const graphid = await json.graphDataId;
        const graphname = await json.graphDataName;
        if(graphid){
            globalActiveGraphData.id = graphid;
            globalActiveGraphData.name = graphname;
            globalActiveGraphData.publikus = false;
            DOM_ispublic.checked = false;
            DOM_elem.value = graphname;
            DOM2_elem.value = graphid;
            // AJAX call
            const dWR = async (data) => {
                const resp = await data;
                if(resp){
                    writeInnerHTML("graphdatadetails", resp);
                }
            }; 
            getGraphStringFromServer(graphid, dWR);
            setVisibilityOfDOMElements();
            showElementById("draw-shapes");
            showElementById("msgFromKonvaEvent");
            showElementById("datacontainers");
            const myfunc = async () => {
                await updateCanvasAndGlobalData(graphid);
            };
            setTimeout(function(){
                myfunc();
            },100);
            
        }
    };
    getNewGraphFromServer(globalUser.email, 7, "large", doWithResponse);
}

function addListenersToDOM(){
    const DOMandhandler = [
        {   DOMid : "selectnodefrom",
            eventtype : "change",
            handler : selectNodeFromNodeToChange,
            param1 : "selectnodefrom",
            param2 : "selectnodeto"    
        }, 
        {   DOMid : "selectnodeto",
            eventtype : "change",
            handler : selectNodeFromNodeToChange,
            param1 : "selectnodefrom",
            param2 : "selectnodeto"    
        },
        {   DOMid : "selectpublicgraph",
            eventtype : "change",
            handler : selectGraphnameChange,
            param1 : "graphdatadetails",
            param2 : null    
        },
        {   DOMid : "selectowngraph",
            eventtype : "change",
            handler : selectOwnGraphnameChange,
            param1 : "graphdatadetails",
            param2 : null
        },
        {   DOMid : "linkcopygraph",
            eventtype : "click",
            handler : linkCopyGraphClick,
            param1 : "graphname",
            param2 : "graphid"    
        },
        {   DOMid : "linkeditgraph",
            eventtype : "click",
            handler : linkEditGraphClick,
            param1 : "graphname",
            param2 : "graphid"    
        },
        {   DOMid : "cbShowData",
            eventtype : "change",
            handler : cbShowDataChange,
            param1 : null,
            param2 : null  
        }, 
        {   DOMid : "btncredge",
            eventtype : "click",
            handler : btnCreateEdgeClick,
            param1 : null,
            param2 : null   
        },
        /*{   DOMid : "btncrnode",
            eventtype : "click",
            handler : btnCreateNodeClick,
            param1 : null,
            param2 : null   
        },*/
        {   DOMid : "btnchangeedgecolor",
            eventtype : "click",
            handler : btnChangeEdgeColorClick,
            param1 : 'blue',
            param2 : 'red'    
        },
        {   DOMid : "btnsavegraph",
            eventtype : "click",
            handler : btnSaveGraphClick,
            param1 : null,
            param2 : null    
        },
        {   DOMid: "btndelgraph",
            eventtype : "click",
            handler : btnDelGraphClick,
            param1 : null,
            param2 : null
        },
        {   DOMid : "cbDeleteEdgeData",
            eventtype : "change",
            handler : cbDeleteEdgeDataChange,
            param1 : null,
            param2 : null    
        },
        {   DOMid : "cbIsPublic",
            eventtype : "change",
            handler : cbIsPublicChange,
            param1 : null,
            param2 : null    
        },
        {   DOMid : "cbIsNodeCreate",
            eventtype : "change",
            handler : cbIsNodeCreateChange,
            param1 : null,
            param2 : null    
        },
        {   DOMid : "rbtnshortest",
            eventtype : "change",
            handler : rbtnAlgoPathTypeChange,
            param1 : "algotype",
            param2 : null    
        },
        {   DOMid : "rbtnlongest",
            eventtype : "change",
            handler : rbtnAlgoPathTypeChange,
            param1 : "algotype",
            param2 : null    
        },
        {   DOMid : "btnprintdg",
            eventtype : "click",
            handler : btnPrintDGClick,
            param1 : null,
            param2 : null    
        },
        {   DOMid : "btnexit",
            eventtype : "click",
            handler : btnExitClick,
            param1 : null,
            param2 : null    
        },
        {   DOMid : "btncomputepath",
            eventtype : "click",
            handler : btnComputePathClick,
            param1 : null,
            param2 : null    
        },
        {   DOMid : "btnmakenewgraph",
            eventtype : "click",
            handler : btnMakeNewGraphClick,
            param1 : "graphname",
            param2 : "graphid"    
        },
        {   DOMid : "graphname",
            eventtype : "keyup",
            handler : handleTextBoxChanged,
            param1 : "changedbackground",
            param2 : null    
        },
        {
            DOMid : "file",
            eventtype : "change",
            handler: handleUploadFilenameChanged,
            param1 : null,
            param2 : null
        }

    ];
    for(const dh of DOMandhandler){
        document.getElementById(dh.DOMid).addEventListener(dh.eventtype,
            function (event){
                dh.handler(event, dh.param1, dh.param2);
            }, false);
    }
}
