/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.com.zir.osiris.web.app.personas.security;

import ar.com.zir.osiris.api.security.*;
import ar.com.zir.osiris.ejb.services.SeguridadService;
import ar.com.zir.osiris.web.app.AbmBean;
import java.util.Collection;
import javax.inject.Inject;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author jmrunge
 */
public abstract class SecurityObjectBean extends AbmBean {
    private TreeNode rootOptions = new DefaultTreeNode("root", null);
    @Inject
    private SeguridadService seguridadService;

    @Override
    public String newObject() throws DuplicatedSessionException {
        String url = super.newObject();
        _initObject();
        fillRootOptions();
        return url;
    }

    @Override
    public void selectObject(NodeSelectEvent evt) throws DuplicatedSessionException {
        super.selectObject(evt);
        _initObject();
        fillRootOptions();
    }
    
    protected void fillRootOptions() throws DuplicatedSessionException {
        rootOptions = new DefaultTreeNode("root", null);
        for (SystemOption so : seguridadService.getParentSystemOptions(getSystemUser())) {
            addSubOption(rootOptions, so);
        }
    }
    
    private void addSubOption(TreeNode parent, SystemOption option) {
        SystemOptionDAO dao = new SystemOptionDAO();
        dao.setInherited(null);
        dao.setName(option.getTitulo());
        DefaultTreeNode node = new DefaultTreeNode(dao, parent);
        if (option.getChildren() == null || option.getChildren().isEmpty()) {
            addSystemOptionSecurityValues(node, option);
        } else {
            for (SystemOption so : option.getChildren()) {
                addSubOption(node, so);
            }
        }
    }
    
    private void addSystemOptionSecurityValues(TreeNode parent, SystemOption option) {
        if (option.getSystemOptionSecurityOptionCollection() != null && !option.getSystemOptionSecurityOptionCollection().isEmpty()) {
            for (SystemOptionSecurityOption soso : option.getSystemOptionSecurityOptionCollection()) {
                SecurityObject so = (SecurityObject) getObject();
                boolean existe = false;
                for (SystemOptionSecurityValue sosv : so.getSystemOptionSecurityValues(option.getCodigo())) {
                    if (sosv.getSystemOptionSecurityOption().equals(soso)) {
                        existe = true;
                        addSystemOptionSecurityValueNode(sosv, parent);
                        break;
                    }
                }
                if (!existe) {
                    SystemOptionSecurityValue sosv = new SystemOptionSecurityValue();
                    sosv.setCanDoIt(false);
                    sosv.setInheritedFrom(null);
                    sosv.setSecurityObject(so);
                    sosv.setSystemOptionSecurityOption(soso);
                    so.addSystemOptionSecurityValue(sosv);
                    addSystemOptionSecurityValueNode(sosv, parent);
                }
            }
        }
    }
    
    private void addSystemOptionSecurityValueNode(SystemOptionSecurityValue sosv, TreeNode parent) {
        SystemOptionDAO dao = new SystemOptionDAO();
        dao.setInherited(sosv.getInheritedFromString());
        dao.setName(sosv.getSystemOptionSecurityOption().getSystemOptionSecurity().getOptionTitle());
        dao.setOption(sosv);
        new DefaultTreeNode(dao, parent);
    }
    
    protected abstract void _initObject() throws DuplicatedSessionException;

    @Override
    protected Object createObject(Object object) throws Exception {
        return _createObject((SecurityObject)object);
    }

    protected abstract SecurityObject _createObject(SecurityObject object) throws Exception;

    @Override
    protected Object updateObject(Object object) throws Exception {
        return _updateObject((SecurityObject)object);
    }
    
    protected abstract SecurityObject _updateObject(SecurityObject object) throws Exception;

    public TreeNode getSystemOptionNode() {
        return rootOptions;
    }
    
}
