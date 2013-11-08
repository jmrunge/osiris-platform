/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.web.app.configuration;

import ar.com.zir.osiris.api.personas.Localidad;
import ar.com.zir.osiris.api.personas.Provincia;
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
@Named(value = "provinciaBean")
@ConversationScoped
public class ProvinciaBean extends AbmBean implements Serializable {
    @Inject
    private ConfigurationService configurationService;

    /** Creates a new instance of ProvinciaBean */
    public ProvinciaBean() {
    }

    @Override
    protected Object getNewObject() {
        return new Provincia();
    }

    @Override
    protected boolean isNew(Object object) {
        return ((Provincia)object).getId() == null;
    }
    
    public Boolean getPredeterminada() {
        if (getObject() == null)
            return null;
        else
            return ((Provincia)getObject()).getPredeterminada();
    }
    
    public void setPredeterminada(Boolean predeterminada) {
        if (getObject() != null)
            ((Provincia)getObject()).setPredeterminada(predeterminada);
    }

    @Override
    protected Object createObject(Object object) throws Exception {
        return configurationService.createProvincia((Provincia)object, getSystemUser());
    }

    @Override
    protected Object updateObject(Object object) throws Exception {
        return configurationService.updateProvincia((Provincia)object, getSystemUser());
    }

    @Override
    protected void deleteObject(Object object) throws Exception {
        configurationService.deleteProvincia((Provincia)object, getSystemUser());
    }

    @Override
    protected Object refreshObject(Object obj) {
        return obj;
    }

    @Override
    protected String getGetterMethod() {
        return "getProvincia";
    }

    @Override
    protected String getSetterMethod() {
        return "setProvincia";
    }

    @Override
    protected String getHolderName() {
        return "provinciaHolder";
    }

    public void cleanPais() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paisHolder", null);
    }

    public void selectPais() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paisHolder", (Provincia)getObject());
    }

    public Collection<Localidad> getLocalidadCollection() {
        if (getObject() == null)
            return null;
        else
            return ((Provincia)getObject()).getLocalidadCollection();
    }
    
    public void setCalleCollection(Collection<Localidad> localidades) {
        if (getObject() != null)
            ((Provincia)getObject()).setLocalidadCollection(localidades);
    }

}
