/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.com.zir.osiris.web.app.personas.security;

import ar.com.zir.osiris.api.security.SystemRoleUsers;
import ar.com.zir.osiris.api.security.SystemUser;
import ar.com.zir.osiris.api.security.SystemUserPersonaFisica;
import ar.com.zir.osiris.web.app.DynamicTreeNode;
import ar.com.zir.osiris.web.app.NodeTypes;
import ar.com.zir.osiris.web.app.personas.PersonaNode;
import java.util.List;
import org.primefaces.model.TreeNode;

/**
 *
 * @author jmrunge
 */
public class SystemUserNode extends DynamicTreeNode {

    public SystemUserNode(SystemUser systemUser, TreeNode parent, boolean selectable, boolean addChildren) {
        super(NodeTypes.SYSTEM_USER_NODE_TYPE, systemUser, parent, selectable, addChildren);
    }

    @Override
    protected void addChildren() {
        SystemUser systemUser = (SystemUser)getData();
        new DynamicTreeNode("Nombre: " + systemUser.getNombre(), this, false, false);
        if (systemUser instanceof SystemUserPersonaFisica)
            new PersonaNode(((SystemUserPersonaFisica)systemUser).getPersona(), this, false, false);
        else
            new DynamicTreeNode("Nombre completo: " + systemUser.getNombreCompleto(), this, false, false);
        DynamicTreeNode roles = new DynamicTreeNode(NodeTypes.SYSTEM_ROLE_NODE_TYPE, "Roles", this, false, false);
        List<SystemRoleUsers> sru = systemUser.getSystemRoleUsers();
        if (sru != null) {
            for (SystemRoleUsers sr : sru) {
                new SystemRoleNode(sr.getSystemRole(), roles, false, false);
            }
        }
        SecurityObjectNodeUtils.addSecurityOptionsNode(this, systemUser);
    }

}
