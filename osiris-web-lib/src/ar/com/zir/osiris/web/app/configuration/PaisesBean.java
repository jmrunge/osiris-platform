/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.web.app.configuration;

import ar.com.zir.osiris.api.personas.Pais;
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
@Named(value="paisesBean")
@ConversationScoped
public class PaisesBean extends SearchBean implements Serializable {
    @Inject
    private ConfigurationService configurationService;
    @Inject
    private SeguridadService seguridadService;

    @Override
    public Collection findObjects(String searchKey) throws DuplicatedSessionException {
        return configurationService.findPaisesBySearchKey(searchKey, getSystemUser());
    }

    @Override
    protected void addNode(Object object, TreeNode parent) {
        new PaisNode((Pais)object, parent, true, true);
    }
    
    @Override
    protected SystemOption getSystemOption() throws DuplicatedSessionException {
        return seguridadService.getSystemOption("PAIS", getSystemUser());
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
}
