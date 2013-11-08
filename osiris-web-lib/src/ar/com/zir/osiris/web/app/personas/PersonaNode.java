/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.com.zir.osiris.web.app.personas;

import ar.com.zir.osiris.api.personas.*;
import ar.com.zir.osiris.web.app.DynamicTreeNode;
import ar.com.zir.osiris.web.app.NodeTypes;
import ar.com.zir.osiris.web.app.personas.security.SystemUserNode;
import ar.com.zir.utils.DateUtils;
import org.primefaces.model.TreeNode;

/**
 *
 * @author jmrunge
 */
public class PersonaNode extends DynamicTreeNode {
    
    public PersonaNode(Persona persona, TreeNode parent, boolean selectable, boolean addChildren) {
        super(NodeTypes.PERSONA_NODE_TYPE, persona, parent, selectable, addChildren);
    }

    @Override
    protected void addChildren() {
        Persona persona = (Persona)getData();
        new DynamicTreeNode("Nombre: " + persona.getNombre(), this, false, false);
        new DynamicTreeNode("Condición IVA: " + persona.getCondicionIVA(), this, false, false);
        if (persona instanceof PersonaFisica) {
            PersonaFisica pf = (PersonaFisica) persona;
            new DynamicTreeNode("DNI: " + pf.getDni(), this, false, false);
            new DynamicTreeNode("Sexo: " + pf.getSexo().toString(), this, false, false);
            new DynamicTreeNode("Fecha Nacimiento: " + DateUtils.format(pf.getFechaNacimiento()), this, false, false);
            if (pf.getSystemUser() != null)
                new SystemUserNode(pf.getSystemUser(), this, false, false);
        } else if (persona instanceof PersonaJuridica) {
            PersonaJuridica pj = (PersonaJuridica)persona;
            new DynamicTreeNode("CUIT: " + pj.getCuit(), this, false, false);
            new DynamicTreeNode("Razón social: " + pj.getRazonSocial(), this, false, false);
        }
        DynamicTreeNode direcciones = new DynamicTreeNode("Domicilios", this, false, false);
        for (Direccion d : persona.getDireccionCollection()) {
            new DynamicTreeNode("[" + d.getTipoDireccion().toString() + "] " + d.toString(), direcciones, false, false);
        }
        DynamicTreeNode telefonos = new DynamicTreeNode("Teléfonos", this, false, false);
        DynamicTreeNode emails = new DynamicTreeNode("Emails", this, false, false);
        DynamicTreeNode webs = new DynamicTreeNode("Páginas Web", this, false, false);
        for (ContactoPersona c : persona.getContactoPersonaCollection()) {
            DynamicTreeNode parent = null;
            if (c instanceof EmailPersona)
                parent = emails;
            else if (c instanceof TelefonoPersona)
                parent = telefonos;
            else if (c instanceof WebPersona)
                parent = webs;
            new DynamicTreeNode(c.toString(), parent, false, false);
        }
    }

}
