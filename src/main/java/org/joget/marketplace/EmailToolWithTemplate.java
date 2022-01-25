package org.joget.marketplace;

import java.util.HashMap;
import java.util.Map;
import org.joget.apps.app.lib.EmailTool;
import org.joget.apps.app.service.AppUtil;
import org.joget.workflow.model.WorkflowAssignment;

public class EmailToolWithTemplate extends EmailTool {
    @Override
    public String getName() {
        return "Email Tool With Template";
    }
    
    @Override
    public String getDescription() {
        return "Sends email message to targeted recipient(s), with ability to select a body template via hash variable.";
    }
    
    @Override
    public String getVersion() {
        return "7.0.0";
    }
    
    @Override
    public Object execute(Map properties) {
        WorkflowAssignment wfAssignment = (WorkflowAssignment) properties.get("workflowAssignment");
        
        String isHtml = (String) properties.get("isHtml");
        String emailTemplate = (String) properties.get("bodyTemplate");
        String emailMessage = (String) properties.get("message");
        
        Map<String, String> replaceMap = null;
        if ("true".equalsIgnoreCase(isHtml)) {
            replaceMap = new HashMap<String, String>();
            replaceMap.put("\\n", "<br/>");
        }
        
        emailTemplate = AppUtil.processHashVariable(emailTemplate, wfAssignment, null, replaceMap);
        emailMessage = AppUtil.processHashVariable(emailMessage, wfAssignment, null, replaceMap);
        
        emailMessage = emailTemplate.replace("$(body)", emailMessage);
        
        properties.put("message", emailMessage);
        
        return super.execute(properties);
    }
    
    @Override
    public String getLabel() {
        return getName();
    }
    
    @Override
    public String getPropertyOptions() {
        return AppUtil.readPluginResource(getClass().getName(), "/properties/emailToolWithTemplate.json", null, true, null);
    }
}
