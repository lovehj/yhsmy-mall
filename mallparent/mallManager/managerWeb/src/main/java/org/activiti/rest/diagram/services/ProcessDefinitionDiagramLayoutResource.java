package org.activiti.rest.diagram.services;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auth 李正义
 * @date 2019/12/14 10:56
 **/
@RestController
public class ProcessDefinitionDiagramLayoutResource extends
        BaseProcessDefinitionDiagramLayoutResource {

    @RequestMapping(value="/process-definition/{processDefinitionId}/diagram-layout", method = RequestMethod.GET, produces = "application/json")
    public ObjectNode getDiagram(@PathVariable String processDefinitionId) {
        return getDiagramNode(null, processDefinitionId);
    }
}
