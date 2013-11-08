/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.web.app;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author jmrunge
 */
public class DynamicTreeNode extends DefaultTreeNode {
    
    public DynamicTreeNode(Object data, TreeNode parent, boolean selectable, boolean addChildren) {
        this(DEFAULT_TYPE, data, parent, selectable, addChildren);
    }

    public DynamicTreeNode(String nodeType, Object data, TreeNode parent, boolean selectable, boolean addChildren) {
        super(nodeType, data, parent);
        setSelectable(selectable);
        if (addChildren)
            addChildren();
    }
    
    protected void addChildren(){};
    
    public void expand() {
        for (TreeNode tn : getChildren()) {
            if (tn.getChildCount() == 0)
                ((DynamicTreeNode)tn).addChildren();
        }
    }
}
