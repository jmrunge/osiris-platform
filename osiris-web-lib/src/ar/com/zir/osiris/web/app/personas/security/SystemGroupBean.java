/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.com.zir.osiris.web.app.personas.security;

import ar.com.zir.osiris.api.security.SecurityObject;
import ar.com.zir.osiris.api.security.SystemGroup;
import ar.com.zir.osiris.ejb.services.SeguridadService;
import java.io.Serializable;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author jmrunge
 */
@Named(value="systemGroupBean")
@ConversationScoped
public class SystemGroupBean extends SecurityObjectBean implements Serializable {
    @Inject
    private SeguridadService seguridadService;

    /** Creates a new instance of GrupoBean */
    public SystemGroupBean() {
    }

    @Override
    protected Object getNewObject() {
        return new SystemGroup();
    }

    @Override
    protected void _initObject() {}

    @Override
    protected boolean isNew(Object object) {
        return ((SystemGroup)object).getId() == null;
    }

    @Override
    protected SecurityObject _createObject(SecurityObject object) throws Exception {
        return seguridadService.createSystemGroup((SystemGroup)object, getSystemUser());
    }

    @Override
    protected SecurityObject _updateObject(SecurityObject object) throws Exception {
        return seguridadService.updateSystemGroup((SystemGroup)object, getSystemUser());
    }

    @Override
    protected void deleteObject(Object object) throws Exception {
        seguridadService.deleteSystemGroup((SystemGroup)object, getSystemUser());
    }

    @Override
    protected Object refreshObject(Object obj) {
        return obj;
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
