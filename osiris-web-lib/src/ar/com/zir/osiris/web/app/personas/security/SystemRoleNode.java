/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.com.zir.osiris.web.app.personas.security;

import ar.com.zir.osiris.api.security.SystemGroupRoles;
import ar.com.zir.osiris.api.security.SystemRole;
import ar.com.zir.osiris.api.security.SystemRoleUsers;
import ar.com.zir.osiris.web.app.DynamicTreeNode;
import ar.com.zir.osiris.web.app.NodeTypes;
import java.util.List;
import org.primefaces.model.TreeNode;

/**
 *
 * @author jmrunge
 */
public class SystemRoleNode extends DynamicTreeNode {    

    public SystemRoleNode(SystemRole systemRole, TreeNode parent, boolean selectable, boolean addChildren) {
        super(NodeTypes.SYSTEM_ROLE_NODE_TYPE, systemRole, parent, selectable, addChildren);
    }

    @Override
    protected void addChildren() {
        SystemRole systemRole = (SystemRole)getData();
        new DynamicTreeNode("Nombre: " + systemRole.getNombre(), this, false, false);
        new DynamicTreeNode("Descripción: " + systemRole.getDescripcion(), this, false, false);
        DynamicTreeNode users = new DynamicTreeNode(NodeTypes.SYSTEM_USER_NODE_TYPE, "Usuarios", this, false, false);
        List<SystemRoleUsers> sru = systemRole.getSystemRoleUsers();
        if (sru != null) {
            for (SystemRoleUsers sr : sru) {
                new SystemUserNode(sr.getSystemUser(), users, false, false);
            }
        }
        DynamicTreeNode groups = new DynamicTreeNode(NodeTypes.SYSTEM_GROUP_NODE_TYPE, "Grupos", this, false, false);
        List<SystemGroupRoles> srg = systemRole.getSystemGroupRoles();
        if (srg != null) {
            for (SystemGroupRoles sr : srg) {
                new SystemGroupNode(sr.getSystemGroup(), groups, false, false);
            }
        }
        SecurityObjectNodeUtils.addSecurityOptionsNode(this, systemRole);
    }

}
