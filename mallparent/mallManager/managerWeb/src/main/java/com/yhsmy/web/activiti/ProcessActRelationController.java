package com.yhsmy.web.activiti;

import com.yhsmy.entity.Json;
import com.yhsmy.entity.vo.act.ProcessActRelation;
import com.yhsmy.enums.ApproveEnum;
import com.yhsmy.service.act.ProcessActRelationServiceI;
import com.yhsmy.util.ShiroUtil;
import com.yhsmy.web.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @auth 李正义
 * @date 2019/12/20 15:26
 **/
@Controller
@Scope("request")
@RequestMapping("/processAct")
public class ProcessActRelationController extends BaseController {

    @Autowired
    private ProcessActRelationServiceI processActRelationServiceI;

    @GetMapping("form")
    public String form (Model model) {
        model.addAllAttributes (processActRelationServiceI.getForm ());
        return "activiti/processAct/form";
    }

    @PostMapping("edit")
    @ResponseBody
    public Json formSubmit (@Valid ProcessActRelation processActRelation, BindingResult result) {
        Json json = validResult (result);
        if (json.getStatus () != Json.SUC_CODE) {
            return json;
        }
        processActRelation.setProcessName (ApproveEnum.getValueByKey (processActRelation.getProcessKey ()));
        return processActRelationServiceI.formSubmit (processActRelation, ShiroUtil.getUser ());
    }

    @DeleteMapping("delete")
    @ResponseBody
    public Json delete (String id) {
        if (StringUtils.isEmpty (id)) {
            return Json.fail ();
        }
        return processActRelationServiceI.deleteProcessActRelation (id);
    }


}
