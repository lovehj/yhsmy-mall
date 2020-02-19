package org.activiti.rest.editor.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * @auth 李正义
 * @date 2019/12/14 10:52
 **/
@RestController
@RequestMapping("/service")
@Slf4j
public class ModelSaveRestResource implements ModelDataJsonConstants {
    @Autowired
    private RepositoryService repositoryService;


    @PostMapping(value="/model/{modelId}/save")
    @ResponseStatus(value = HttpStatus.OK)
    public void saveModel(@PathVariable String modelId
            , String name, String description
            , String json_xml, String svg_xml, HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, String[]> map = request.getParameterMap ();
            JSONObject jsonObject = new JSONObject ();
            //全跑到key了，可取方案
            for (Map.Entry<String, String[]> entry : map.entrySet ()) {
                String data = entry.getKey () + "=" + (entry.getValue ()[0]);
                jsonObject = JSON.parseObject (data);
            }
            name = (String) jsonObject.get ("name");
            description = (String) jsonObject.get ("description");
            json_xml = (String) jsonObject.get ("json_xml");
            svg_xml = (String) jsonObject.get ("svg_xml");

            Model model = repositoryService.getModel (modelId);
            JSONObject object = new JSONObject ();
            object.put (MODEL_NAME, name);
            object.put (MODEL_DESCRIPTION, description);
            model.setMetaInfo (object.toString ());
            model.setName (name);
            repositoryService.saveModel (model);

            repositoryService.addModelEditorSource (model.getId (), json_xml.getBytes ("utf-8"));

            InputStream svgStream = new ByteArrayInputStream (svg_xml.getBytes ("utf-8"));
            TranscoderInput input = new TranscoderInput (svgStream);

            PNGTranscoder transcoder = new PNGTranscoder ();
            // Setup output
            ByteArrayOutputStream outStream = new ByteArrayOutputStream ();
            TranscoderOutput output = new TranscoderOutput (outStream);

            // Do the transformation
            transcoder.transcode (input, output);
            final byte[] result = outStream.toByteArray ();
            repositoryService.addModelEditorSourceExtra (model.getId (), result);
            outStream.close ();

        } catch (Exception e) {
            log.error ("Error saving model", e);
            throw new ActivitiException ("Error saving model", e);
        }
    }
}