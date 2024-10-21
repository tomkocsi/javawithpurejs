// dummy DTO data
let dummypicurl = "/images/pic0.jpg"; 
var dummyNodeDTOs = [
    {
        id: 63243,
        X: 50,
        Y: 200,
        isFinish: false,
        isEntry: true,
        label: "Start"
    },
    {
        id: 63244,
        X: 110,
        Y: 100,
        isFinish: false,
        isEntry: false,
        label: "n1"
    },
    {
        id: 63245,
        X: 220,
        Y: 100,
        isFinish: false,
        isEntry: false,
        label: "n2"
    },
    {
        id: 63246,
        X: 110,
        Y: 300,
        isFinish: false,
        isEntry: false,
        label: "n3"
    },
    {
        id: 63247,
        X: 220,
        Y: 300,
        isFinish: false,
        isEntry: false,
        label: "n4"
    },
    {
        id: 63248,
        X: 300,
        Y: 220,
        isFinish: false,
        isEntry: false,
        label: "n5"
    },
    {
        id: 63249,
        X: 400,
        Y: 200,
        isFinish: true,
        isEntry: false,
        label: "Exit"
    }    
];

var dummyEdgeDTOs = [
    {
        id: 40678,
        fromNodeId: 63243,
        toNodeId: 63244,
        label: "e1",
        descriptionId : 28686,
        pictureIds : [55432, 55434], 
        inventoryElementIds : [15766],
        time1 : 5,
        time2 : 43
    }, 
    {
        id: 40679,
        fromNodeId: 63244,
        toNodeId: 63245,
        label: "e2",
        descriptionId : 28687,
        pictureIds : [55432, 55434], 
        inventoryElementIds : [15765, 15768, 15767],
        time1 : 23,
        time2 : 32 
    },
    {
        id: 40680,
        fromNodeId: 63243,
        toNodeId: 63245,
        label: "e3", 
        descriptionId : 28688,
        pictureIds : [55433], 
        inventoryElementIds : [15769, 15766],
        time1 : 30,
        time2 : 56
    },
    {
        id: 40681,
        fromNodeId: 63245,
        toNodeId: 63246,
        label: "e4",
        descriptionId : "",
        pictureIds : [], 
        inventoryElementIds : [15766, 15770],
        time1 : 15,
        time2 : 25
    },
    {
        id: 40682,
        fromNodeId: 63246,
        toNodeId: 63247,
        label: "e5",
        descriptionId : 28687,
        pictureIds : [55434], 
        inventoryElementIds : [15768],
        time1 : 65,
        time2 : 85
    },
    {
        id: 40683,
        fromNodeId: 63244,
        toNodeId: 63248,
        label: "e6",
        descriptionId : 28686,
        pictureIds : [55433], 
        inventoryElementIds : [],
        time1 : 43,
        time2 : 45
    },
    {
        id: 40684,
        fromNodeId: 63248,
        toNodeId: 63249,
        label: "e7",
        descriptionId : 28687,
        pictureIds : [], 
        inventoryElementIds : [15768],
        time1 : 56,
        time2 : 77
    },
    {
        id: 40685,
        fromNodeId: 63246,
        toNodeId: 63248,
        label: "e8",
        descriptionId : 28688,
        pictureIds : [55432], 
        inventoryElementIds : [15765, 15766, 15767, 15768, 15769],
        time1 : 21,
        time2 : 32
    }
];
var dummyGraphDescriptions = [
    {
        id : 28686,
        text : "Speculative execution which allows the execution of complete instructions or parts of instructions before being certain whether this execution should take place. A commonly used form of speculative execution is control flow speculation where instructions past a control flow instruction (e.g., a branch) are executed before the target of the control flow instruction is determined. Several other forms of speculative execution have been proposed and are in use including speculative execution driven by value prediction, memory dependence prediction and cache latency prediction.",
    },
    {
        id : 28687,
        text : "A basic application might characterize various sub-ranges of a continuous variable. For instance, a temperature measurement for anti-lock brakes might have several separate membership functions defining particular temperature ranges needed to control the brakes properly. Each function maps the same temperature value to a truth value in the 0 to 1 range. These truth values can then be used to determine how the brakes should be controlled.[8] Fuzzy set theory provides a means for representing uncertainty.",
    },
    {
        id : 28688,
        text : "A linguistic variable such as age may accept values such as young and its antonym old. Because natural languages do not always contain enough value terms to express a fuzzy value scale, it is common practice to modify linguistic values with adjectives or adverbs. For example, we can use the hedges rather and somewhat to construct the additional values rather old or somewhat young.",
    }
];
var dummyUserDescriptions = [
    {
        id : 28689,
        text : "Itt mindenki jol vegzi a munkajat"
        },
    {
        id : 28670,
        text : "Az asztal teritve van."
    }
];
var dummyGraphInventoryElements = [
    {
        id : 15765,
        text: "víz"
    },
    {
        id : 15766,
        text: "palack"
    },
    {
        id : 15767,
        text: "akkutöltő"
    },
    {
        id : 15768,
        text: "telefontok"
    },
    {
        id : 15769,
        text: "csavarhúzó"
    },
    {
        id : 15770,
        text: "pohár"
    }
];
var dummyUserInventoryElements = [
    {
        id : 15771,
        text: "pohár"
    },
    {
        id : 15772,
        text: "másik pohár"
    }
];

var  dummyGraphPictures = [
     {
         id : 55432,
         imagesrc : "/images/pic0.jpg"
     },
     {
        id : 55433,
        imagesrc : "/images/pic0.jpg"
    },
    {
        id : 55434,
        imagesrc : "/images/pic0.jpg"
    },
    {
        id : 55435,
        imagesrc : "/images/pic0.jpg"
    }
];

var dummyUserPictures = [
    {
        id : 55436,
        imagesrc : "/images/pic0.jpg"
    },
    {
        id : 55437,
        imagesrc : "/images/pic0.jpg"
    }
];

var dummyUserData = {
    id: 90,
    email: "vk6@hoho.hu"
}
var dummyGraphDataList = [
    {
        name: "Öld meg a sárkányt",
        useremail: "csocsesz@ll.hu"
    },
    {
        name: "Menekulés a szigetről",
        useremail: "bamba@tt.hu"
    },
    {
        name: "Séf a tengeralattjárón",
        useremail: "bamba@tt.hu"
    }
];
var dummyPublicDescriptions = [
    {
        graphname: "Séf a tengeralattjárón",
        edgelabel: "késes",
        description: "Rakj össze 3 db kést 3 db pohárra úgy, hogy a kancsó vizet megtartsa. A vízszint magasságában nézz a falon lévő receptre és a hozzávalót vedd ki a fiókból és lépj a mosogatóhoz"
    },
    {
        graphname: "Séf a tengeralattjárón",
        edgelabel: "cetlis",
        description: "A recepteket tartalmazó cetlik alapján találd ki, mennyi olajat kell a gépbe önteni, hogy kinyiljon a kapu"
    }       
]
