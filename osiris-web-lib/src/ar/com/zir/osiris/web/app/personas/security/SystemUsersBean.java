/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.com.zir.osiris.web.app.personas.security;

import ar.com.zir.osiris.api.security.SystemOption;
import ar.com.zir.osiris.api.security.SystemUser;
import ar.com.zir.osiris.ejb.services.SeguridadService;
import ar.com.zir.osiris.web.app.SearchBean;
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
@Named(value="systemUsersBean")
@ConversationScoped
public class SystemUsersBean extends SearchBean implements Serializable {
    @Inject
    private SeguridadService seguridadService;

    @Override
    public Collection findObjects(String searchKey) throws DuplicatedSessionException {
        return seguridadService.findSystemUsersBySearchKey(searchKey, getSystemUser());
    }

    @Override
    protected void addNode(Object object, TreeNode parent) {
        new SystemUserNode((SystemUser) object, parent, true, true);
    }
    
    @Override
    protected SystemOption getSystemOption() throws DuplicatedSessionException {
        return seguridadService.getSystemOption("USUARIO", getSystemUser());
    }

    @Override
    protected String getGetterMethod() {
        return "getSystemUser";
    }

    @Override
    protected String getSetterMethod() {
        return "setSystemUser";
    }

    @Override
    protected String getHolderName() {
        return "systemUserHolder";
    }

}
