import {createNodesFromNodeDTOs, findNodeById, findOutgoingsToNodeDTO,
    findIncomingsToNodeDTO} from '../tobetested/transfermodel_exp.js';
import {dummyNodeDTOs, dummyEdgeDTOs} from '../tobetested/dummydatas_exp.js';
import {NodeDTO, EdgeDTO, GraphDTO} from '../tobetested/transfermodel_exp.js';

import {addElementToList, removeElementFromList, removeElementById, 
    getElementById, getElementByLabel} from '../tobetested/model_exp.js';
import {Vertex, Edge, Graph} from '../tobetested/model_exp.js';

describe ('findOutgoingsToNodeDTO', () =>{
    it('returns outgoing edges to a specific nodeDTO', function() {
        const entrynode = dummyNodeDTOs[0];
        const outgoingedgeids = [dummyEdgeDTOs[0].id, dummyEdgeDTOs[2].id].join(' ');
        const nodes = createNodesFromNodeDTOs(dummyNodeDTOs);
        expect(findOutgoingsToNodeDTO(entrynode.id, dummyEdgeDTOs, nodes)
        .map(elem => {return elem.id;}).join(' ')).toBe(outgoingedgeids);
    });
});