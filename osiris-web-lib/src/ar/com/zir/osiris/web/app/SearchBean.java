/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.com.zir.osiris.web.app;

import ar.com.zir.osiris.api.security.*;
import ar.com.zir.osiris.web.app.personas.security.DuplicatedSessionException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author jmrunge
 */
public abstract class SearchBean extends HolderBean {
    protected String searchKey;
    protected TreeNode root;
    private TreeNode selectedNode;
    
    public SearchBean() {
    }

    public abstract Collection findObjects(String searchKey) throws DuplicatedSessionException;
    protected abstract void addNode(Object object, TreeNode parent);

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public TreeNode getObjects() {
//        System.out.println("GETOBJECTS " + this.getClass().getName());
        if (root == null)
            root = new DefaultTreeNode("root", null);
        return root;
    }

    public void search() {
        try {
    //        System.out.println("SEARCH " + this.getClass().getName());
            root = new DynamicTreeNode("root", null, false, true);        
            if (searchKey != null && !searchKey.trim().isEmpty()) {
                for (Object o : findObjects(searchKey)) {
                    addNode(o, root);
                }
            }
        } catch (Exception e) {
            showMessage(e.getMessage(), false);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public boolean canRead() throws DuplicatedSessionException {
        return canDoIt("read");
    }

    public boolean canCreate() throws DuplicatedSessionException {
        return canDoIt("create");
    }

    public boolean canUpdate() throws DuplicatedSessionException {
        return canDoIt("update");
    }

    public boolean canDelete() throws DuplicatedSessionException {
        return canDoIt("delete");
    }

    public boolean canExecute() throws DuplicatedSessionException {
        return canDoIt("execute");
    }

    private boolean canDoIt(String optionCode) throws DuplicatedSessionException {
        SystemOption so = getSystemOption();
        SystemUser su = getSystemUser();

        for (SystemOptionSecurityValue sosv : su.getSystemOptionSecurityValues(so.getCodigo())) {
            if (sosv.getSystemOptionSecurityOption().getSystemOptionSecurity().getOptionCode().toLowerCase().trim().equals(optionCode)) {
                return sosv.getCanDoIt();
            }
        }
        return false;
    }

    protected abstract SystemOption getSystemOption() throws DuplicatedSessionException;

    protected SystemUser getSystemUser() throws DuplicatedSessionException {
        SystemUser su = (SystemUser) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("systemUser");
        if (su == null)
            throw new DuplicatedSessionException("La sesión actual de este usuario ha sido cerrada por duplicidad");
        return su;
    }

    public void cleanObject() {
        setHolderValue(null);
    }

    public void selectObject(NodeSelectEvent evt) {
        if (evt.getTreeNode() == null)
            setHolderValue(null);
        else {
            setHolderValue(evt.getTreeNode().getData());
        }
    }

    public void expandNode(NodeExpandEvent evt) {
        ((DynamicTreeNode)evt.getTreeNode()).expand();
    }

    protected void showMessage(String message, boolean success) {
        FacesMessage.Severity sev = null;
        String title = null;
        if (success) {
            sev = FacesMessage.SEVERITY_INFO;
            title = "Operación exitosa";
        } else {
            sev = FacesMessage.SEVERITY_FATAL;
            title = "Error";
        }
        FacesMessage msg = new FacesMessage(sev, title, message);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

}
