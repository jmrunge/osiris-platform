/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.api.personas.profiles;

import ar.com.zir.osiris.api.personas.Persona;
import ar.com.zir.osiris.ejb.exceptions.ValidationException;

/**
 *
 * @author jmrunge
 */
public interface ProfileInterface {
    String getPermisoName();
    Object getObject();
    void setObject(Object object);
    void newObject(Persona persona);
    boolean isForPersonaFisica();
    boolean isForPersonaJuridica();
    void validate() throws ValidationException;
    boolean isNew();
}
