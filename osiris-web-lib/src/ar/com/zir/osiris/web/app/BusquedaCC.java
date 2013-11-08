/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.web.app;

import javax.el.MethodExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;

/**
 *
 * @author jmrunge
 */
@FacesComponent(value="busquedaCC")
public class BusquedaCC extends UINamingContainer {

    public void objectSelectListener(NodeSelectEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        MethodExpression objectSelectListener = (MethodExpression) getAttributes().get("objectSelectListener");
        objectSelectListener.invoke(context.getELContext(), new Object[] { event });
    }

    public void nodeExpandListener(NodeExpandEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        MethodExpression nodeExpandListener = (MethodExpression) getAttributes().get("nodeExpandListener");
        nodeExpandListener.invoke(context.getELContext(), new Object[] { event });
    }

    public void searchObject() {
        FacesContext context = FacesContext.getCurrentInstance();
        MethodExpression searchObject = (MethodExpression) getAttributes().get("searchObject");
        searchObject.invoke(context.getELContext(), new Object[] {});
    }
    
    public void newObject() {
        FacesContext context = FacesContext.getCurrentInstance();
        MethodExpression newObject = (MethodExpression) getAttributes().get("newObject");
        newObject.invoke(context.getELContext(), new Object[] {});
    }

}
