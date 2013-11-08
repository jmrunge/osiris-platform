/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.web.app.configuration;

import ar.com.zir.osiris.api.personas.Pais;
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
public class PaisNode extends DynamicTreeNode {

    public PaisNode(Pais pais, TreeNode parent, boolean selectable, boolean addChildren) {
        super(NodeTypes.PAIS_NODE_TYPE, pais, parent, selectable, addChildren);
    }

    @Override
    protected void addChildren() {
        Pais pais = (Pais)getData();
        new DynamicTreeNode("Nombre: " + pais.getNombre(), this, false, false);
        new DynamicTreeNode("Predeterminado: " + StringUtils.booleanString(pais.getPredeterminado(), false), this, false, false);
        DynamicTreeNode provincias = new DynamicTreeNode(NodeTypes.PROVINCIA_NODE_TYPE, "Provincias", this, false, false);
        Collection<Provincia> provs = pais.getProvinciaCollection();
        if (provs != null) {
            for (Provincia prov : provs) {
                new ProvinciaNode(prov, provincias, false, false);
            }
        }
        
    }

}
