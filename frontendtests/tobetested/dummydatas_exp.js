// dummy DTO data
const dummyNodeDTOs = [
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

const dummyEdgeDTOs = [
    {
        id: 40678,
        fromNodeId: 63243,
        toNodeId: 63244,
        label: "e1"
    }, 
    {
        id: 40679,
        fromNodeId: 63244,
        toNodeId: 63245,
        label: "e2"
    },
    {
        id: 40680,
        fromNodeId: 63243,
        toNodeId: 63245,
        label: "e3"
    },
    {
        id: 40681,
        fromNodeId: 63245,
        toNodeId: 63246,
        label: "e4"
    },
    {
        id: 40682,
        fromNodeId: 63246,
        toNodeId: 63247,
        label: "e5"
    },
    {
        id: 40683,
        fromNodeId: 63244,
        toNodeId: 63248,
        label: "e6"
    },
    {
        id: 40684,
        fromNodeId: 63248,
        toNodeId: 63249,
        label: "e7"
    },
    {
        id: 40684,
        fromNodeId: 63246,
        toNodeId: 63248,
        label: "e8"
    }
];

export {dummyNodeDTOs, dummyEdgeDTOs}