/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.com.zir.osiris.web.app.personas;

import ar.com.zir.osiris.api.personas.Persona;
import ar.com.zir.osiris.api.security.SystemOption;
import ar.com.zir.osiris.ejb.services.PersonaService;
import ar.com.zir.osiris.ejb.services.SeguridadService;
import ar.com.zir.osiris.web.app.DynamicTreeNode;
import ar.com.zir.osiris.web.app.SearchBean;
import ar.com.zir.osiris.web.app.personas.security.DuplicatedSessionException;
import java.io.Serializable;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.TreeNode;

/**
 *
 * @author jmrunge
 */
@Named(value="personasBean")
@ConversationScoped
public class PersonasBean extends SearchBean implements Serializable {
    @Inject
    private PersonaService personaService;
    @Inject
    private SeguridadService seguridadService;

    @Override
    public Collection findObjects(String searchKey) throws DuplicatedSessionException {
        return personaService.findPersonasBySearchKey(searchKey, getSystemUser());
    }

    @Override
    protected void addNode(Object object, TreeNode parent) {
        new PersonaNode((Persona)object, parent, true, true);
    }
    
    @Override
    protected SystemOption getSystemOption() throws DuplicatedSessionException {
        return seguridadService.getSystemOption("PERSONA", getSystemUser());
    }

    @Override
    protected String getGetterMethod() {
        return "getPersona";
    }

    @Override
    protected String getSetterMethod() {
        return "setPersona";
    }

    @Override
    protected String getHolderName() {
        return "personaHolder";
    }

    public void searchPersonaFisica() {
        try {
            root = new DynamicTreeNode("root", null, false, true);        
            if (searchKey != null && !searchKey.trim().isEmpty()) {
                for (Object o : findPersonasFisicas(searchKey)) {
                    addNode(o, root);
                }
            }
        } catch (Exception e) {
            showMessage(e.getMessage(), false);
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    public Collection findPersonasFisicas(String searchKey) throws DuplicatedSessionException {
        return personaService.findPersonasFisicasBySearchKey(searchKey, getSystemUser());
    }

}
