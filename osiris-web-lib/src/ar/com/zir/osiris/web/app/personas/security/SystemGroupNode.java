/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.com.zir.osiris.web.app.personas.security;

import ar.com.zir.osiris.api.security.*;
import ar.com.zir.osiris.web.app.DynamicTreeNode;
import ar.com.zir.osiris.web.app.NodeTypes;
import java.util.List;
import org.primefaces.model.TreeNode;

/**
 *
 * @author jmrunge
 */
public class SystemGroupNode extends DynamicTreeNode {    

    public SystemGroupNode(SystemGroup systemGroup, TreeNode parent, boolean selectable, boolean addChildren) {
        super(NodeTypes.SYSTEM_GROUP_NODE_TYPE, systemGroup, parent, selectable, addChildren);
    }

    @Override
    protected void addChildren() {
        SystemGroup systemGroup = (SystemGroup)getData();
        new DynamicTreeNode("Nombre: " + systemGroup.getNombre(), this, false, false);
        new DynamicTreeNode("Descripción: " + systemGroup.getDescripcion(), this, false, false);
        DynamicTreeNode roles = new DynamicTreeNode(NodeTypes.SYSTEM_ROLE_NODE_TYPE, "Roles", this, false, false);
        List<SystemGroupRoles> sru = systemGroup.getSystemGroupRoles();
        if (sru != null) {
            for (SystemGroupRoles sr : sru) {
                new SystemRoleNode(sr.getSystemRole(), roles, false, false);
            }
        }
        SecurityObjectNodeUtils.addSecurityOptionsNode(this, systemGroup);
    }

}
