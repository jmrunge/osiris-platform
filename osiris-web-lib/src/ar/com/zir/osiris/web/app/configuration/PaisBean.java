/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.web.app.configuration;

import ar.com.zir.osiris.api.personas.Pais;
import ar.com.zir.osiris.api.personas.Provincia;
import ar.com.zir.osiris.ejb.services.ConfigurationService;
import ar.com.zir.osiris.web.app.AbmBean;
import java.io.Serializable;
import java.util.Collection;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author jmrunge
 */
@Named(value = "paisBean")
@ConversationScoped
public class PaisBean extends AbmBean implements Serializable {
    @Inject
    private ConfigurationService configurationService;

    /** Creates a new instance of PaisBean */
    public PaisBean() {
    }
    
    @Override
    protected Object getNewObject() {
        return new Pais();
    }

    @Override
    protected boolean isNew(Object object) {
        return ((Pais)object).getId() == null;
    }
    
    public Boolean getPredeterminado() {
        if (getObject() == null)
            return null;
        else
            return ((Pais)getObject()).getPredeterminado();
    }
    
    public void setPredeterminado(Boolean predeterminado) {
        if (getObject() != null)
            ((Pais)getObject()).setPredeterminado(predeterminado);
    }

    @Override
    protected Object createObject(Object object) throws Exception {
        return configurationService.createPais((Pais)object, getSystemUser());
    }

    @Override
    protected Object updateObject(Object object) throws Exception {
        return configurationService.updatePais((Pais)object, getSystemUser());
    }

    @Override
    protected void deleteObject(Object object) throws Exception {
        configurationService.deletePais((Pais)object, getSystemUser());
    }

    @Override
    protected Object refreshObject(Object obj) {
        return obj;
    }

    @Override
    protected String getGetterMethod() {
        return "getPais";
    }

    @Override
    protected String getSetterMethod() {
        return "setPais";
    }

    @Override
    protected String getHolderName() {
        return "paisHolder";
    }
    
    public Collection<Provincia> getProvinciaCollection() {
        if (getObject() == null)
            return null;
        else
            return ((Pais)getObject()).getProvinciaCollection();
    }
    
    public void setProvinciaCollection(Collection<Provincia> provincias) {
        if (getObject() != null)
            ((Pais)getObject()).setProvinciaCollection(provincias);
    }

}
