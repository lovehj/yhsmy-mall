package org.activiti.rest.editor.main;

import org.activiti.engine.ActivitiException;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

/**
 * @auth 李正义
 * @date 2019/12/14 10:37
 **/
@RestController
@RequestMapping("/service")
public class StencilsetRestResource {

    @RequestMapping(value="/editor/stencilset", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public @ResponseBody
    String getStencilset() {
        InputStream stencilsetStream = this.getClass().getClassLoader().getResourceAsStream("stencilset.json");
        try {
            return IOUtils.toString(stencilsetStream, "utf-8");
        } catch (Exception e) {
            throw new ActivitiException ("Error while loading stencil set", e);
        }
    }
}
