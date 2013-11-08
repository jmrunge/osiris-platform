/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.web.app.configuration;

import ar.com.zir.osiris.api.personas.Calle;
import ar.com.zir.osiris.api.personas.Localidad;
import ar.com.zir.osiris.web.app.DynamicTreeNode;
import ar.com.zir.osiris.web.app.NodeTypes;
import ar.com.zir.utils.StringUtils;
import java.util.Collection;
import org.primefaces.model.TreeNode;

/**
 *
 * @author jmrunge
 */
public class LocalidadNode extends DynamicTreeNode {

    public LocalidadNode(Localidad localidad, TreeNode parent, boolean selectable, boolean addChildren) {
        super(NodeTypes.LOCALIDAD_NODE_TYPE, localidad, parent, selectable, addChildren);
    }

    @Override
    protected void addChildren() {
        Localidad localidad = (Localidad)getData();
        new DynamicTreeNode("Nombre: " + localidad.getNombre(), this, false, false);
        new DynamicTreeNode("Predeterminada: " + StringUtils.booleanString(localidad.getPredeterminada(), false), this, false, false);
        new ProvinciaNode(localidad.getProvincia(), this, false, false);
        DynamicTreeNode cs = new DynamicTreeNode(NodeTypes.CALLE_NODE_TYPE, "Calles", this, false, false);
        Collection<Calle> calles = localidad.getCalleCollection();
        if (calles != null) {
            for (Calle calle : calles) {
                new CalleNode(calle, cs, false, false);
            }
        }
        
    }

}