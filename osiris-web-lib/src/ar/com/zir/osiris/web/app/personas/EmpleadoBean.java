/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.web.app.personas;

import ar.com.zir.osiris.api.personas.Persona;
import ar.com.zir.osiris.api.personas.PersonaFisica;
import ar.com.zir.osiris.api.personas.profiles.Empleado;
import ar.com.zir.osiris.api.personas.profiles.ProfileInterface;
import ar.com.zir.osiris.ejb.exceptions.ValidationException;

/**
 *
 * @author jmrunge
 */
public class EmpleadoBean implements ProfileInterface {
    private Empleado empleado;
    
    @Override
    public String getPermisoName() {
        return "adminEmp";
    }

    @Override
    public Object getObject() {
        return empleado;
    }

    @Override
    public void newObject(Persona persona) {
        empleado = new Empleado();
        empleado.setPersona((PersonaFisica)persona);
    }

    @Override
    public void setObject(Object object) {
        empleado = (Empleado) object;
    }

    @Override
    public boolean isForPersonaFisica() {
        return true;
    }

    @Override
    public boolean isForPersonaJuridica() {
        return false;
    }

    @Override
    public void validate() throws ValidationException {
        if (empleado.getLimiteAdelantos() == null)
            throw new ValidationException("Debe ingresar el límite de adelantos");
    }

    @Override
    public boolean isNew() {
        return empleado.getId() == null;
    }

}
