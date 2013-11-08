/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.web.app.personas;

import ar.com.zir.osiris.api.personas.Persona;
import ar.com.zir.osiris.api.personas.profiles.Cliente;
import ar.com.zir.osiris.api.personas.profiles.ProfileInterface;
import ar.com.zir.osiris.ejb.exceptions.ValidationException;

/**
 *
 * @author jmrunge
 */
public class ClienteBean implements ProfileInterface {
    private Cliente cliente;
    
    @Override
    public String getPermisoName() {
        return "adminCli";
    }

    @Override
    public Object getObject() {
        return cliente;
    }

    @Override
    public void setObject(Object object) {
        this.cliente = (Cliente) object;
    }

    @Override
    public void newObject(Persona persona) {
        cliente = new Cliente();
        cliente.setPersona(persona);
    }

    @Override
    public boolean isForPersonaFisica() {
        return true;
    }

    @Override
    public boolean isForPersonaJuridica() {
        return true;
    }

    @Override
    public void validate() throws ValidationException {
        if (cliente.getDiasDeuda() == null)
            throw new ValidationException("Debe ingresar los días de tolerancia");
        else if (cliente.getLimiteDeuda() == null)
            throw new ValidationException("Debe ingresar el límite de deuda");
    }

    @Override
    public boolean isNew() {
        return cliente.getId() == null;
    }
    
}
