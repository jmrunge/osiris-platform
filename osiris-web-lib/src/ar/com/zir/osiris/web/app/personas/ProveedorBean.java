/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.web.app.personas;

import ar.com.zir.osiris.api.personas.Persona;
import ar.com.zir.osiris.api.personas.PersonaJuridica;
import ar.com.zir.osiris.api.personas.profiles.ProfileInterface;
import ar.com.zir.osiris.api.personas.profiles.Proveedor;
import ar.com.zir.osiris.ejb.exceptions.ValidationException;

/**
 *
 * @author jmrunge
 */
public class ProveedorBean implements ProfileInterface {
    private Proveedor proveedor;
    
    @Override
    public String getPermisoName() {
        return "adminProv";
    }

    @Override
    public Object getObject() {
        return proveedor;
    }

    @Override
    public void newObject(Persona persona) {
        proveedor = new Proveedor();
        proveedor.setPersona((PersonaJuridica)persona);
    }

    @Override
    public void setObject(Object object) {
        proveedor = (Proveedor) object;
    }

    @Override
    public boolean isForPersonaFisica() {
        return false;
    }

    @Override
    public boolean isForPersonaJuridica() {
        return true;
    }

    @Override
    public void validate() throws ValidationException {
        if (proveedor.getLimiteDeuda() == null)
            throw new ValidationException("Debe ingresar el límite de adelantos");
        else if (proveedor.getLimiteDeuda() == null)
            throw new ValidationException("Debe ingresar el límite de deuda");
    }

    @Override
    public boolean isNew() {
        return proveedor.getId() == null;
    }
    
}
