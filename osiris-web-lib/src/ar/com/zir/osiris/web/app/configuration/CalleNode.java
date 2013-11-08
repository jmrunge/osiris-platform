/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.web.app.configuration;

import ar.com.zir.osiris.api.personas.Calle;
import ar.com.zir.osiris.web.app.DynamicTreeNode;
import ar.com.zir.osiris.web.app.NodeTypes;
import org.primefaces.model.TreeNode;

/**
 *
 * @author jmrunge
 */
public class CalleNode extends DynamicTreeNode {

    public CalleNode(Calle calle, TreeNode parent, boolean selectable, boolean addChildren) {
        super(NodeTypes.CALLE_NODE_TYPE, calle, parent, selectable, addChildren);
    }

    @Override
    protected void addChildren() {
        Calle calle = (Calle)getData();
        new DynamicTreeNode("Nombre: " + calle.getNombre(), this, false, false);
        new LocalidadNode(calle.getLocalidad(), this, false, false);
    }

}
