/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.web.app.personas;

import ar.com.zir.osiris.api.personas.Persona;
import ar.com.zir.osiris.api.personas.profiles.ProfileInterface;
import ar.com.zir.osiris.api.personas.profiles.Socio;
import ar.com.zir.osiris.ejb.exceptions.ValidationException;

/**
 *
 * @author jmrunge
 */
public class SocioBean implements ProfileInterface {
    private Socio socio;
    
    @Override
    public String getPermisoName() {
        return "adminSoc";
    }

    @Override
    public Object getObject() {
        return socio;
    }

    @Override
    public void newObject(Persona persona) {
        socio = new Socio();
        socio.setPersona(persona);
    }

    @Override
    public void setObject(Object object) {
        socio = (Socio) object;
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
        if (socio.getPorcentaje() == null)
            throw new ValidationException("Debe ingresar el porcentaje");
    }

    @Override
    public boolean isNew() {
        return socio.getId() == null;
    }
    
}
