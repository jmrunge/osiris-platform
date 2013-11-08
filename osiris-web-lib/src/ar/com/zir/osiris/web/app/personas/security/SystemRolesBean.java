/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.com.zir.osiris.web.app.personas.security;

import ar.com.zir.osiris.api.security.SystemOption;
import ar.com.zir.osiris.api.security.SystemRole;
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
@Named(value="systemRolesBean")
@ConversationScoped
public class SystemRolesBean extends SearchBean implements Serializable {
    @Inject
    private SeguridadService seguridadService;

    @Override
    public Collection findObjects(String searchKey) throws DuplicatedSessionException {
        return seguridadService.findRolesBySearchKey(searchKey, getSystemUser());
    }

    @Override
    protected void addNode(Object object, TreeNode parent) {
        new SystemRoleNode((SystemRole) object, parent, true, true);
    }
    
    @Override
    protected SystemOption getSystemOption() throws DuplicatedSessionException {
        return seguridadService.getSystemOption("ROL", getSystemUser());
    }

    @Override
    protected String getGetterMethod() {
        return "getSystemRole";
    }

    @Override
    protected String getSetterMethod() {
        return "setSystemRole";
    }

    @Override
    protected String getHolderName() {
        return "systemRoleHolder";
    }

}
