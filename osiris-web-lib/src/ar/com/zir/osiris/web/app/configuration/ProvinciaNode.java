/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.web.app.configuration;

import ar.com.zir.osiris.api.personas.Localidad;
import ar.com.zir.osiris.api.personas.Provincia;
import ar.com.zir.osiris.web.app.DynamicTreeNode;
import ar.com.zir.osiris.web.app.NodeTypes;
import ar.com.zir.utils.StringUtils;
import java.util.Collection;
import org.primefaces.model.TreeNode;

/**
 *
 * @author jmrunge
 */
public class ProvinciaNode extends DynamicTreeNode {

    public ProvinciaNode(Provincia provincia, TreeNode parent, boolean selectable, boolean addChildren) {
        super(NodeTypes.PROVINCIA_NODE_TYPE, provincia, parent, selectable, addChildren);
    }

    @Override
    protected void addChildren() {
        Provincia prov = (Provincia)getData();
        new DynamicTreeNode("Nombre: " + prov.getNombre(), this, false, false);
        new DynamicTreeNode("Predeterminada: " + StringUtils.booleanString(prov.getPredeterminada(), false), this, false, false);
        new PaisNode(prov.getPais(), this, false, false);
        DynamicTreeNode localidades = new DynamicTreeNode(NodeTypes.LOCALIDAD_NODE_TYPE, "Localidades", this, false, false);
        Collection<Localidad> locs = prov.getLocalidadCollection();
        if (locs != null) {
            for (Localidad loc : locs) {
                new LocalidadNode(loc, localidades, false, false);
            }
        }
        
    }

}