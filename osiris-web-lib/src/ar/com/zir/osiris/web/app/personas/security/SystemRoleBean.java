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
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.DualListModel;

/**
 *
 * @author jmrunge
 */
@Named(value="systemRoleBean")
@ConversationScoped
public class SystemRoleBean extends SecurityObjectBean implements Serializable {
    private DualListModel<String> grupos = new DualListModel<>(new ArrayList<String>(), new ArrayList<String>());
    @Inject
    private SeguridadService seguridadService;

    /** Creates a new instance of RolBean */
    public SystemRoleBean() {
    }

    @Override
    protected Object getNewObject() {
        return new SystemRole();
    }

    @Override
    protected void _initObject() throws DuplicatedSessionException {
        setGrupos(getOriginalGrupos());
    }

    @Override
    protected boolean isNew(Object object) {
        return ((SystemRole)object).getId() == null;
    }

    @Override
    protected SecurityObject _createObject(SecurityObject object) throws Exception {
        return seguridadService.createSystemRole((SystemRole)object, getSystemUser());
    }

    @Override
    protected SecurityObject _updateObject(SecurityObject object) throws Exception {
        return seguridadService.updateSystemRole((SystemRole)object, getSystemUser());
    }

    @Override
    protected void deleteObject(Object object) throws Exception {
        seguridadService.deleteSystemRole((SystemRole)object, getSystemUser());
    }

    public DualListModel<String> getGrupos() {
        return grupos;
    }

    public DualListModel<String> getOriginalGrupos() throws DuplicatedSessionException {
        List<String> source = new ArrayList<>();
        List<String> target = new ArrayList<>();
        if (getObject() != null) {
            for (SystemGroup grupo : seguridadService.getSystemGroups(getSystemUser())) {
                source.add(grupo.getNombre());
            }
            List<SystemGroupRoles> sru = ((SystemRole)getObject()).getSystemGroupRoles();
            if (sru != null) {
                for (SystemGroupRoles sr : sru) {
                    target.add(sr.getSystemGroup().getNombre());
                    source.remove(sr.getSystemGroup().getNombre());
                }
            }
        }
        grupos = new DualListModel<>(source, target);
        return grupos;
    }

    public void setGrupos(DualListModel<String> grupos) {
        this.grupos = grupos;
    }

    public void updateGrupos() throws DuplicatedSessionException {
        SystemRole sr = (SystemRole)getObject();
        for (String s : grupos.getTarget()) {
            SystemGroup sg = seguridadService.getSystemGroupByNombre(s, getSystemUser());
            boolean addGroup = true;
            if (sr.getSystemGroupRoles() != null) {
                for (SystemGroupRoles sgr : sr.getSystemGroupRoles()) {
                    if (sgr.getSystemGroup().equals(sg)) {
                        addGroup = false;
                        break;
                    }
                }
            }
            if (addGroup) {
                SystemGroupRoles sgr = new SystemGroupRoles();
                sgr.setSystemRole(sr);
                sgr.setSystemGroup(sg);
                sr.addSystemGroup(sgr);
                for (SystemOptionSecurityValue sosv : sr.getSystemOptionSecurityValueCollection()) {
                    if (sosv.getInheritedFrom() == null) {
                        SystemOptionSecurityValue inheritedFrom = seguridadService.checkSecurityObjectOptions(sosv, sg, getSystemUser());
                        if (inheritedFrom != null) {
                            sosv.setInheritedFrom(inheritedFrom);
                            sosv.setCanDoIt(true);
                        }
                    }
                }
            }
        }
        if (sr.getSystemGroupRoles() != null) {
            List<SystemGroupRoles> groupsToDelete = new ArrayList<>();
            for (SystemGroupRoles sgr : sr.getSystemGroupRoles()) {
                boolean deleteGroup = true;
                for (String s : grupos.getTarget()) {
                    SystemGroup sg = seguridadService.getSystemGroupByNombre(s, getSystemUser());
                    if (sgr.getSystemGroup().equals(sg)) {
                        deleteGroup = false;
                        break;
                    }
                }
                if (deleteGroup) 
                    groupsToDelete.add(sgr);
            }
            for (SystemGroupRoles sgr : groupsToDelete) {
                int groupToDelete = -1;
                for (int i = 0; i < sr.getSystemGroupRoles().size(); i++) {
                    SystemGroup aux = sr.getSystemGroupRoles().get(i).getSystemGroup();
                    if (aux.equals(sgr.getSystemGroup())) {
                        groupToDelete = i;
                        break;
                    }
                }
                if (groupToDelete > -1) 
                    sr.getSystemGroupRoles().remove(groupToDelete);
                for (SystemOptionSecurityValue sosv : sr.getSystemOptionSecurityValueCollection()) {
                    if (sosv.getInheritedFrom() != null && sosv.getInheritedFrom().getSecurityObject().equals(sgr.getSystemGroup())){
                        SystemOptionSecurityValue inheritedFrom = null;
                        for (SystemGroupRoles sgr2 : sr.getSystemGroupRoles()) {
                            inheritedFrom = seguridadService.checkSecurityObjectOptions(sosv, sgr2.getSystemRole(), getSystemUser());
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

    @Override
    protected Object refreshObject(Object obj) {
        return obj;
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
