/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.web.app.configuration;

import ar.com.zir.osiris.api.personas.Localidad;
import ar.com.zir.osiris.api.security.SystemOption;
import ar.com.zir.osiris.ejb.services.ConfigurationService;
import ar.com.zir.osiris.ejb.services.SeguridadService;
import ar.com.zir.osiris.web.app.SearchBean;
import ar.com.zir.osiris.web.app.personas.security.DuplicatedSessionException;
import java.io.Serializable;
import java.util.Collection;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.TreeNode;

/**
 *
 * @author jmrunge
 */
@Named(value="localidadesBean")
@ConversationScoped
public class LocalidadesBean extends SearchBean implements Serializable {
    @Inject
    private ConfigurationService configurationService;
    @Inject
    private SeguridadService seguridadService;

    @Override
    public Collection findObjects(String searchKey) throws DuplicatedSessionException {
        return configurationService.findLocalidadesBySearchKey(searchKey, getSystemUser());
    }

    @Override
    protected void addNode(Object object, TreeNode parent) {
        new LocalidadNode((Localidad)object, parent, true, true);
    }
    
    @Override
    protected SystemOption getSystemOption() throws DuplicatedSessionException {
        return seguridadService.getSystemOption("LOCALIDAD", getSystemUser());
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
}
