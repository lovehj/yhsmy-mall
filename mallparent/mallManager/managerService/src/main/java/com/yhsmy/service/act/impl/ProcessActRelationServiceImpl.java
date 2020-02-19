package com.yhsmy.service.act.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yhsmy.entity.DataGrid;
import com.yhsmy.entity.Json;
import com.yhsmy.entity.QueryParams;
import com.yhsmy.entity.vo.act.ProcessActRelation;
import com.yhsmy.entity.vo.sys.UpdateMap;
import com.yhsmy.entity.vo.sys.User;
import com.yhsmy.enums.ApproveEnum;
import com.yhsmy.enums.NormalEnum;
import com.yhsmy.exception.PageNotFoundException;
import com.yhsmy.mapper.act.ProcessActRelationMapper;
import com.yhsmy.mapper.sys.MybatisMapper;
import com.yhsmy.mapper.sys.UserMapper;
import com.yhsmy.service.act.ProcessActRelationServiceI;
import com.yhsmy.utils.UUIDUtil;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auth 李正义
 * @date 2019/12/20 15:27
 **/
@Service
@Transactional(readOnly = true)
public class ProcessActRelationServiceImpl implements ProcessActRelationServiceI {

    @Autowired
    private ProcessActRelationMapper processActRelationMapper;

    @Autowired
    private MybatisMapper mybatisMapper;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private UserMapper userMapper;

//    @Autowired
//    private RuntimeService runtimeService;

    @Override
    public DataGrid getProcessActRelationList (QueryParams queryParams) {
        Page<ProcessActRelation> page = PageHelper.startPage (queryParams.getPageNo (), queryParams.getPageSize ());
        List<ProcessActRelation> list = processActRelationMapper.findProcessActRelationList (queryParams.getQueryBy (), queryParams.getQueryText (),
                queryParams.getQueryDate (), queryParams.getStartDate (), queryParams.getEndDate (), true);;
        return new DataGrid (page.getTotal (), list);
    }

    @Override
    public Map<String, Object> getForm () {
        Map<String, Object> result = new HashMap<> (1);
        result.put ("approveList", ApproveEnum.getList ());
        return result;
    }

    @Override
    @Transactional(readOnly = false)
    public Json formSubmit (ProcessActRelation processActRelation, User user) {
        String id = processActRelation.getId ();
        if(StringUtils.isNotBlank (id)) {
            // 仅更新modelId
            UpdateMap updateMap = new UpdateMap ("smy_process_act_relation");
            updateMap.addField ("modelId", processActRelation.getModelId ());
            updateMap.addWhere ("id", id);
            if(mybatisMapper.update (updateMap) <= 0) {
                return Json.fail ();
            }
        } else {
            id = UUIDUtil.generateUUID ();
            processActRelation.setId (id);
            processActRelation.setState (NormalEnum.NORMAL.getKey ());
            processActRelation.setCreator (user.getRealName ());
            processActRelation.setCreateTime (LocalDateTime.now ());
            if(StringUtils.isEmpty (processActRelation.getModelId ())) {
                processActRelation.setModelId ("");
            }
            processActRelationMapper.addProcessActRelation (processActRelation);
        }
        return Json.ok (Json.SUC_MSG, id);
    }

    @Override
    @Transactional(readOnly = false)
    public Json deleteProcessActRelation (String id) {
        UpdateMap updateMap = new UpdateMap ("smy_process_act_relation");
        updateMap.addField ("state", NormalEnum.DELETE.getKey ());
        updateMap.addWhere ("id", id);
        if(mybatisMapper.update (updateMap) <= 0) {
            return Json.fail ();
        }

        // 删除模型
        ProcessActRelation par = processActRelationMapper.findProcessActRelationById (id);
        if(StringUtils.isNotBlank (par.getModelId ())) {
            repositoryService.deleteModel (par.getModelId ());
        }
        return Json.ok ();
    }

    @Override
    @Transactional(readOnly = false)
    public String getModelForm (String processActId) throws Exception {
        if(StringUtils.isBlank (processActId)) {
            throw new PageNotFoundException ();
        }
        ProcessActRelation par = processActRelationMapper.findProcessActRelationById (processActId);
        if(par == null) {
            throw new PageNotFoundException ();
        }
        Model model = repositoryService.newModel ();
        String key = par.getProcessKey ();
        String name = ApproveEnum.getValueByKey (key);
        String description = par.getDescription ();
        int revision = 1;

        ObjectNode objectNode = objectMapper.createObjectNode ();
        objectNode.put (ModelDataJsonConstants.MODEL_NAME, name);
        objectNode.put (ModelDataJsonConstants.MODEL_DESCRIPTION, description);
        objectNode.put (ModelDataJsonConstants.MODEL_REVISION, revision);

        model.setName (name);
        model.setKey (key);
        model.setMetaInfo (objectNode.toString ());

        repositoryService.saveModel (model);
        String id = model.getId ();

        ObjectNode editNode = objectMapper.createObjectNode ();
        editNode.put ("id", "canvas");
        editNode.put ("resourceId", "canvas");

        ObjectNode stencilSetNode = objectMapper.createObjectNode ();
        stencilSetNode.put ("namespace", "http://b3mn.org/stencilset/bpmn2.0#");

        editNode.put ("stencilset", stencilSetNode);
        repositoryService.addModelEditorSource (id, editNode.toString ().getBytes ("UTF-8"));

        // 更新流程图与流程关系表的关联关系
        UpdateMap updateMap = new UpdateMap ("smy_process_act_relation");
        updateMap.addField ("modelId", id);
        updateMap.addWhere ("id", par.getId ());
        if(mybatisMapper.update (updateMap) <= 0) {
            throw new PageNotFoundException ();
        }
        return id;
    }

    @Override
    @Transactional(readOnly = false)
    public Json deployProcess (String id) {
        try{
            Model model = repositoryService.getModel (id);
            String modelName = model.getName ();
            if(StringUtils.isEmpty (modelName)) {
                modelName = "unkownName";
                model.setName (modelName);
            }
            byte[] modelSource = repositoryService.getModelEditorSource (model.getId ());
            if(modelSource == null) {
                return Json.fail ("流程模型不存在!");
            }

            JsonNode modelNode = null;
            modelNode = new ObjectMapper ().readTree (modelSource);
            BpmnModel bpmnModel = new BpmnJsonConverter ().convertToBpmnModel (modelNode);

            if(bpmnModel.getProcesses ().size () == 0) {
                return Json.fail ("不是合法的流程!");
            }

            byte[] bpmnBytes = new BpmnXMLConverter ().convertToXML (bpmnModel);

            // 发布流程
            String processName = model.getName ()+ ".bpmn20.xml";
            String xmlFile = new String(bpmnBytes);

            // 部署流程
            Deployment deployment = repositoryService.createDeployment () //
                    .name (modelName) //
                    .addString (processName, new String (bpmnBytes, "UTF-8")) //
                    .deploy ();

            model.setDeploymentId (deployment.getId ());
            repositoryService.saveModel (model);
        }catch(Exception e){
            return Json.fail ("流程部署失败!");
        }
        return Json.ok ();
    }

    @Override
    public Json syncData () {
        try{
            List<User> userList = userMapper.findUserList (-1, -1, "");
            org.activiti.engine.identity.User act_user = null;
            Group act_group = null;
            for(User user : userList) {
                if(user.getState () == NormalEnum.FREEZE.getKey ()) {
                    continue; // 排除被冻结的用户
                }
                String userId = user.getId (), roleId = user.getRoleId ();
                identityService.deleteUser (userId);
                identityService.deleteGroup (roleId);
                identityService.deleteMembership (userId, roleId );

                act_user = new UserEntity ();
                act_user.setId (userId);
                act_user.setFirstName (user.getRealName ());
                act_user.setEmail (user.getEmail ());
                identityService.saveUser (act_user);

                act_group = new GroupEntity ();
                act_group.setId (roleId);
                act_group.setName (user.getRoleName ());
                identityService.saveGroup (act_group);
                identityService.createMembership (userId, roleId);
            }
        }catch (Exception e){
            return Json.fail ();
        }
        return Json.ok ();
    }


}
