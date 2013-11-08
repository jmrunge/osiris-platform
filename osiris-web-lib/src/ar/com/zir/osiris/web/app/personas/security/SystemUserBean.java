/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.com.zir.osiris.web.app.personas.security;

import ar.com.zir.osiris.api.security.*;
import ar.com.zir.osiris.ejb.services.SeguridadService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ConversationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.DualListModel;

/**
 *
 * @author jmrunge
 */
@Named(value="systemUserBean")
@ConversationScoped
public class SystemUserBean extends SecurityObjectBean implements Serializable {
    private DualListModel<String> roles = new DualListModel<>(new ArrayList<String>(), new ArrayList<String>());
    @Inject
    private SeguridadService seguridadService;

    /** Creates a new instance of UsuarioBean */
    public SystemUserBean() {
    }

    @Override
    protected Object getNewObject() {
        return new SystemUserPersonaFisica();
    }

    @Override
    protected void _initObject() throws DuplicatedSessionException {
        setRoles(getOriginalRoles());
    }

    @Override
    protected boolean isNew(Object object) {
        return ((SystemUser)object).getId() == null;
    }

    @Override
    protected SecurityObject _createObject(SecurityObject object) throws Exception {
        return seguridadService.createSystemUser((SystemUser)object, getSystemUser());
    }

    @Override
    protected SecurityObject _updateObject(SecurityObject object) throws Exception {
        SystemUser su = seguridadService.updateSystemUser((SystemUser)object, getSystemUser());
        if (su.equals(getSystemUser()))
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("systemUser", su);
        return su;
    }

    @Override
    protected void deleteObject(Object object) throws Exception {
        seguridadService.deleteSystemUser((SystemUser)object, getSystemUser());
    }

    public DualListModel<String> getRoles() {
        return roles;
    }

    public DualListModel<String> getOriginalRoles() throws DuplicatedSessionException {
        List<String> source = new ArrayList<>();
        List<String> target = new ArrayList<>();
        if (getObject() != null) {
            for (SystemRole role : seguridadService.getSystemRoles(getSystemUser())) {
                source.add(role.getNombre());
            }
            List<SystemRoleUsers> sru = ((SystemUser)getObject()).getSystemRoleUsers();
            if (sru != null) {
                for (SystemRoleUsers sr : sru) {
                    target.add(sr.getSystemRole().getNombre());
                    source.remove(sr.getSystemRole().getNombre());
                }
            }
        }
        roles = new DualListModel<>(source, target);
        return roles;
    }

    public void setRoles(DualListModel<String> roles) {
        this.roles = roles;
    }

    public void updateRoles() throws DuplicatedSessionException {
        SystemUser su = (SystemUser)getObject();
        for (String s : roles.getTarget()) {
            SystemRole sr = seguridadService.getSystemRoleByNombre(s, getSystemUser());
            boolean addRole = true;
            if (su.getSystemRoleUsers() != null) {
                for (SystemRoleUsers sru : su.getSystemRoleUsers()) {
                    if (sru.getSystemRole().equals(sr)) {
                        addRole = false;
                        break;
                    }
                }
            }
            if (addRole) {
                SystemRoleUsers sru = new SystemRoleUsers();
                sru.setSystemUser(su);
                sru.setSystemRole(sr);
                su.addSystemRole(sru);
                for (SystemOptionSecurityValue sosv : su.getSystemOptionSecurityValueCollection()) {
                    if (sosv.getInheritedFrom() == null) {
                        SystemOptionSecurityValue inheritedFrom = seguridadService.checkSecurityObjectOptions(sosv, sr, getSystemUser());
                        if (inheritedFrom != null) {
                            sosv.setInheritedFrom(inheritedFrom);
                            sosv.setCanDoIt(true);
                        }
                    }
                }
            }
        }
        if (su.getSystemRoleUsers() != null) {
            List<SystemRoleUsers> rolesToDelete = new ArrayList<>();
            for (SystemRoleUsers sru : su.getSystemRoleUsers()) {
                boolean deleteRole = true;
                for (String s : roles.getTarget()) {
                    SystemRole sr = seguridadService.getSystemRoleByNombre(s, getSystemUser());
                    if (sru.getSystemRole().equals(sr)) {
                        deleteRole = false;
                        break;
                    }
                }
                if (deleteRole) 
                    rolesToDelete.add(sru);
            }
            for (SystemRoleUsers sru : rolesToDelete) {
                int roleToDelete = -1;
                for (int i = 0; i < su.getSystemRoleUsers().size(); i++) {
                    SystemRole aux = su.getSystemRoleUsers().get(i).getSystemRole();
                    if (aux.equals(sru.getSystemRole())) {
                        roleToDelete = i;
                        break;
                    }
                }
                if (roleToDelete > -1) 
                    su.getSystemRoleUsers().remove(roleToDelete);
                for (SystemOptionSecurityValue sosv : su.getSystemOptionSecurityValueCollection()) {
                    if (sosv.getInheritedFrom() != null && sosv.getInheritedFrom().getSecurityObject().equals(sru.getSystemRole())){
                        SystemOptionSecurityValue inheritedFrom = null;
                        for (SystemRoleUsers sru2 : su.getSystemRoleUsers()) {
                            inheritedFrom = seguridadService.checkSecurityObjectOptions(sosv, sru2.getSystemRole(), getSystemUser());
                            if (inheritedFrom != null)
                                break;
                        }
                        if (inheritedFrom != null) {
                            sosv.setInheritedFrom(inheritedFrom);
                            sosv.setCanDoIt(Boolean.TRUE);
                        } else {
                            sosv.setInheritedFrom(null);
                            sosv.setCanDoIt(Boolean.FALSE);
                        }
                    }
                }
            }
        }
        fillRootOptions();
    }
    
    public void cleanPersona() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("personaHolder", null);
    }

    public void selectPersona() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("personaHolder", getObject());
    }

    public boolean isAdmin() {
        if ((getObject() != null) && (getObject() instanceof SystemUserAdmin))
            return true;
        else
            return false;
    }

    @Override
    protected Object refreshObject(Object obj) {
        return obj;
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
    
    public void connectedUsersChangeListener() {
        SystemUser su = (SystemUser) getObject();
        if (!su.getCanSeeConnectedUsers()) {
            su.setCanDisconnectUser(false);
            su.setCanSendMessages(false);
        }
    }

}
