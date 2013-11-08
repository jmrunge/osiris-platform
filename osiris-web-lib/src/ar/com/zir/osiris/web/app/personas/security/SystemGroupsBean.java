/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.com.zir.osiris.web.app.personas.security;

import ar.com.zir.osiris.api.security.SystemGroup;
import ar.com.zir.osiris.api.security.SystemOption;
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
@Named(value="systemGroupsBean")
@ConversationScoped
public class SystemGroupsBean extends SearchBean implements Serializable {
    @Inject
    private SeguridadService seguridadService;

    @Override
    public Collection findObjects(String searchKey) throws DuplicatedSessionException {
        return seguridadService.findGruposBySearchKey(searchKey, getSystemUser());
    }

    @Override
    protected void addNode(Object object, TreeNode parent) {
        new SystemGroupNode((SystemGroup) object, parent, true, true);
    }

    @Override
    protected SystemOption getSystemOption() throws DuplicatedSessionException {
        return seguridadService.getSystemOption("GRUPO", getSystemUser());
    }

    @Override
    protected String getGetterMethod() {
        return "getSystemGroup";
    }

    @Override
    protected String getSetterMethod() {
        return "setSystemGroup";
    }

    @Override
    protected String getHolderName() {
        return "systemGroupHolder";
    }

}
