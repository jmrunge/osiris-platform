/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.web.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ConversationScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.inject.Named;

/**
 *
 * @author jmrunge
 */
@Named(value = "componentFinder")
@ConversationScoped
public class ComponentFinder implements Serializable {
    private UIForm mainForm;
    
    /**
     * Creates a new instance of ComponentFinder
     */
    public ComponentFinder() {
    }

    public UIForm getMainForm() {
        return mainForm;
    }

    public void setMainForm(UIForm mainForm) {
        this.mainForm = mainForm;
    }
    
    public String getComponentId(String compId) {
        List<String> notIn = new ArrayList<>();
        String aux = getComponentId(mainForm, compId, notIn);
        while (aux != null) {
            notIn.add(aux);
            aux = getComponentId(mainForm, compId, notIn);
        }
        if (notIn.isEmpty()) 
            return null;
        String result = notIn.get(0);
        for (int i = 0; i < notIn.size(); i++) {
            if (i > 0)
                result = result + "," + notIn.get(i);
        }
        return result;
    }
    
    private String getComponentId(UIComponent container, String compId, List<String> notIn) {
        for (UIComponent component : container.getChildren()) {
            String id = component.getId();
            if (id.equals(compId) || id.endsWith(":" + compId)) {
                String aux = getComponentQualifiedId(component);
                if (notExists(aux, notIn))
                    return aux;
            } 
            if (component.getChildCount() > 0) {
                String aux = getComponentId(component, compId, notIn);
                if (aux != null && notExists(aux, notIn))
                    return aux;
            }
        }
        return null;
    }
    
    private String getComponentQualifiedId(UIComponent component) {
        String id = ":" + component.getId();
        UIComponent parent = component.getParent();
        parent = parent.getNamingContainer();
        while (parent != null) {
            id = ":" + parent.getId() + id;
            parent = parent.getParent();
            parent = parent.getNamingContainer();
        }
        return id;
    }
    
    private boolean notExists(String aux, List<String> notIn) {
        for (String s : notIn) {
            if (s.equals(aux))
                return false;
        }
        return true;
    }
}
