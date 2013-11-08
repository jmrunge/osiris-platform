/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.web.app.configuration;

import ar.com.zir.osiris.api.personas.Calle;
import ar.com.zir.osiris.ejb.services.ConfigurationService;
import ar.com.zir.osiris.web.app.AbmBean;
import java.io.Serializable;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author jmrunge
 */
@Named(value = "calleBean")
@ConversationScoped
public class CalleBean extends AbmBean implements Serializable {
    @Inject
    private ConfigurationService configurationService;

    /** Creates a new instance of CalleBean */
    public CalleBean() {
    }

    @Override
    protected Object getNewObject() {
        return new Calle();
    }

    @Override
    protected boolean isNew(Object object) {
        return ((Calle)object).getId() == null;
    }
    
    @Override
    protected Object createObject(Object object) throws Exception {
        return configurationService.createCalle((Calle)object, getSystemUser());
    }

    @Override
    protected Object updateObject(Object object) throws Exception {
        return configurationService.updateCalle((Calle)object, getSystemUser());
    }

    @Override
    protected void deleteObject(Object object) throws Exception {
        configurationService.deleteCalle((Calle)object, getSystemUser());
    }

    @Override
    protected Object refreshObject(Object obj) {
        return obj;
    }

    @Override
    protected String getGetterMethod() {
        return "getCalle";
    }

    @Override
    protected String getSetterMethod() {
        return "setCalle";
    }

    @Override
    protected String getHolderName() {
        return "calleHolder";
    }

    public void cleanLocalidad() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("localidadHolder", null);
    }

    public void selectLocalidad() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("localidadHolder", (Calle)getObject());
    }

}
