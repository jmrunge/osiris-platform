/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.web.app.personas;

import ar.com.zir.osiris.api.personas.profiles.Empleado;
import ar.com.zir.osiris.web.app.DynamicTreeNode;
import ar.com.zir.osiris.web.app.NodeTypes;
import org.primefaces.model.TreeNode;

/**
 *
 * @author jmrunge
 */
public class EmpleadoNode extends DynamicTreeNode {

    public EmpleadoNode(Empleado empleado, TreeNode parent, boolean selectable, boolean addChildren) {
        super(NodeTypes.EMPLEADO_NODE_TYPE, empleado, parent, selectable, addChildren);
    }

    @Override
    protected void addChildren() {
        Empleado empleado = (Empleado)getData();
        new PersonaNode(empleado.getPersona(), this, false, false);
    }

}