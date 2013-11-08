/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.web.app.configuration;

import ar.com.zir.osiris.api.personas.Provincia;
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
@Named(value="provinciasBean")
@ConversationScoped
public class ProvinciasBean extends SearchBean implements Serializable {
    @Inject
    private ConfigurationService configurationService;
    @Inject
    private SeguridadService seguridadService;

    @Override
    public Collection findObjects(String searchKey) throws DuplicatedSessionException {
        return configurationService.findProvinciasBySearchKey(searchKey, getSystemUser());
    }

    @Override
    protected void addNode(Object object, TreeNode parent) {
        new ProvinciaNode((Provincia)object, parent, true, true);
    }
    
    @Override
    protected SystemOption getSystemOption() throws DuplicatedSessionException {
        return seguridadService.getSystemOption("PROVINCIA", getSystemUser());
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
}
