/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.web.app.configuration;

import ar.com.zir.osiris.api.personas.Calle;
import ar.com.zir.osiris.api.personas.Localidad;
import ar.com.zir.osiris.ejb.services.ConfigurationService;
import ar.com.zir.osiris.web.app.AbmBean;
import java.io.Serializable;
import java.util.Collection;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author jmrunge
 */
@Named(value = "localidadBean")
@ConversationScoped
public class LocalidadBean extends AbmBean implements Serializable {
    @Inject
    private ConfigurationService configurationService;

    /** Creates a new instance of LocalidadBean */
    public LocalidadBean() {
    }

    @Override
    protected Object getNewObject() {
        return new Localidad();
    }

    @Override
    protected boolean isNew(Object object) {
        return ((Localidad)object).getId() == null;
    }
    
    public Boolean getPredeterminada() {
        if (getObject() == null)
            return null;
        else
            return ((Localidad)getObject()).getPredeterminada();
    }
    
    public void setPredeterminada(Boolean predeterminada) {
        if (getObject() != null)
            ((Localidad)getObject()).setPredeterminada(predeterminada);
    }

    @Override
    protected Object createObject(Object object) throws Exception {
        return configurationService.createLocalidad((Localidad)object, getSystemUser());
    }

    @Override
    protected Object updateObject(Object object) throws Exception {
        return configurationService.updateLocalidad((Localidad)object, getSystemUser());
    }

    @Override
    protected void deleteObject(Object object) throws Exception {
        configurationService.deleteLocalidad((Localidad)object, getSystemUser());
    }

    @Override
    protected Object refreshObject(Object obj) {
        return obj;
    }

    @Override
    protected String getGetterMethod() {
        return "getLocalidad";
    }

    @Override
    protected String getSetterMethod() {
        return "setLocalidad";
    }

    @Override
    protected String getHolderName() {
        return "localidadHolder";
    }

    public void cleanProvincia() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("provinciaHolder", null);
    }

    public void selectProvincia() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("provinciaHolder", (Localidad)getObject());
    }

    public Collection<Calle> getCalleCollection() {
        if (getObject() == null)
            return null;
        else
            return ((Localidad)getObject()).getCalleCollection();
    }
    
    public void setCalleCollection(Collection<Calle> calles) {
        if (getObject() != null)
            ((Localidad)getObject()).setCalleCollection(calles);
    }

}
