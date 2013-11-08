/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.web.app.personas.security;

import ar.com.zir.osiris.api.security.SecurityObject;
import ar.com.zir.osiris.api.security.SystemOption;
import ar.com.zir.osiris.api.security.SystemOptionSecurityOption;
import ar.com.zir.osiris.api.security.SystemOptionSecurityValue;
import ar.com.zir.osiris.web.app.DynamicTreeNode;
import ar.com.zir.osiris.web.app.NodeTypes;
import ar.com.zir.utils.StringUtils;
import org.primefaces.model.TreeNode;

/**
 *
 * @author jmrunge
 */
public abstract class SecurityObjectNodeUtils {
    
    public static void addSecurityOptionsNode(TreeNode node, SecurityObject securityObject) {
        DynamicTreeNode options = new DynamicTreeNode(NodeTypes.SYSTEM_OPTION_NODE_TYPE, "Opciones", node, false, false);
        for (SystemOption so : securityObject.getMainSystemOptions()) {
            DynamicTreeNode parent = new DynamicTreeNode(NodeTypes.SYSTEM_OPTION_NODE_TYPE, so.getTitulo(), options, false, false);
            for (SystemOption child : securityObject.getSystemOptionChildren(so)) {
                DynamicTreeNode sub = new DynamicTreeNode(NodeTypes.SYSTEM_OPTION_NODE_TYPE, child.getTitulo(), parent, false, false);
                for (SystemOptionSecurityOption soso : child.getSystemOptionSecurityOptionCollection()) {
                    boolean canDoIt = false;
                    for (SystemOptionSecurityValue sosv : securityObject.getSystemOptionSecurityValues(child.getCodigo())) {
                        if (sosv.getSystemOptionSecurityOption().equals(soso)) {
                            canDoIt = sosv.getCanDoIt();
                            break;
                        }
                    }
                    new DynamicTreeNode(NodeTypes.SYSTEM_OPTION_NODE_TYPE, soso.getSystemOptionSecurity().getOptionTitle() + ": " + StringUtils.booleanString(canDoIt, false), sub, false, false);
                }
            }
        }
    }
}
