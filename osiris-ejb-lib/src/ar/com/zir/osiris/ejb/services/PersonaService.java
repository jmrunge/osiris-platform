/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.ejb.services;

import ar.com.zir.osiris.api.personas.*;
import ar.com.zir.osiris.api.personas.profiles.ProfileInterface;
import ar.com.zir.osiris.api.security.SystemUser;
import ar.com.zir.osiris.api.security.SystemUserPersonaFisica;
import ar.com.zir.osiris.ejb.exceptions.PersistenceException;
import ar.com.zir.osiris.ejb.exceptions.ValidationException;
import ar.com.zir.osiris.ejb.interceptors.AllowedOption;
import ar.com.zir.osiris.ejb.interceptors.RollbackTransaction;
import ar.com.zir.osiris.ejb.interceptors.SecuredBean;
import ar.com.zir.osiris.ejb.interceptors.SecuredMethod;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jmrunge
 */
@Stateless
@LocalBean
@RollbackTransaction
@SecuredBean
public class PersonaService implements Serializable {
    @PersistenceContext(unitName="osiris-ejbPU")
    private EntityManager em;
    @EJB
    private SeguridadService seguridadService;
    @EJB
    private OsirisConfig config;

    @SecuredMethod({
        @AllowedOption(option="PERSONA", access="all"),
        @AllowedOption(option="USUARIO", access="all")
    })
    public Collection<Persona> findPersonasBySearchKey(String searchKey, SystemUser user) {
        Collection<Persona> personas = new ArrayList<>();
        personas.addAll(findPersonasFisicasBySearchKey(searchKey, user));
        Collection<Persona> aux = em.createNamedQuery("Persona.findAllByNombre").setParameter("nombre", "%" + searchKey + "%").getResultList();
        for (Persona p : aux) {
            if (!personas.contains(p))
                personas.add(p);
        }
        aux = em.createNamedQuery("PersonaJuridica.findAllByRazonSocial").setParameter("razonSocial", "%" + searchKey + "%").getResultList();
        for (Persona p : aux) {
            if (!personas.contains(p))
                personas.add(p);
        }
        aux = em.createNamedQuery("PersonaJuridica.findAllByCuit").setParameter("cuit", "%" + searchKey + "%").getResultList();
        for (Persona p : aux) {
            if (!personas.contains(p))
                personas.add(p);
        }
        return personas;
    }

    @SecuredMethod({
        @AllowedOption(option="PERSONA", access="all"),
        @AllowedOption(option="USUARIO", access="all")
    })
    public Collection<PersonaFisica> findPersonasFisicasBySearchKey(String searchKey, SystemUser user) {
        Collection<PersonaFisica> personas = new ArrayList<>();
        try {
            int dni = Integer.parseInt(searchKey);
            personas.add((PersonaFisica)em.createNamedQuery("PersonaFisica.findByDni").setParameter("dni", dni).getSingleResult());
        } catch (NumberFormatException ex) {
            personas = em.createNamedQuery("PersonaFisica.findAllByNombre").setParameter("nombre", "%" + searchKey + "%").getResultList();
            Collection<SystemUser> sus = seguridadService.findSystemUsersBySearchKey(searchKey, true, user);
            for (SystemUser su : sus) {
                if (su instanceof SystemUserPersonaFisica && !personas.contains(((SystemUserPersonaFisica)su).getPersona()))
                    personas.add(((SystemUserPersonaFisica)su).getPersona());
            }
        }
        return personas;
    }

    @SecuredMethod({
        @AllowedOption(option="PERSONA", access="create")
    })
    public Persona createPersona(Persona p, Collection<ProfileInterface> profiles, SystemUser user) throws ValidationException, PersistenceException {
        try {
            validatePersona(p);
            if (profiles != null) {
                for (ProfileInterface profile : profiles) {
                    profile.validate();
                }
            }
            em.persist(p);
            if (profiles != null) {
                for (ProfileInterface profile : profiles) {
                    em.persist(profile.getObject());
                }
            }
            return p;
        } catch (ValidationException ve) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error validando Persona " + p.toString(), ve);
            throw ve;
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error creando Persona " + p.toString(), e);
            throw new PersistenceException("Error creando persona " + p.toString(), e);
        }
    }

    @SecuredMethod({
        @AllowedOption(option="PERSONA", access="update")
    })
    public Persona updatePersona(Persona p, Collection<ProfileInterface> profiles, SystemUser user) throws ValidationException, PersistenceException {
        try {
            validatePersona(p);
            if (profiles != null) {
                for (ProfileInterface profile : profiles) {
                    profile.validate();
                }
            }
            p = em.merge(p);
            if (profiles != null) {
                for (ProfileInterface profile : profiles) {
                    if (profile.isNew())
                        em.persist(profile.getObject());
                    else
                        em.merge(profile.getObject());
                }
            }
            return p;
        } catch (ValidationException ve) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error validando Persona " + p.toString(), ve);
            throw ve;
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error actualizando Persona " + p.toString(), e);
            throw new PersistenceException("Error actualizando persona " + p.toString(), e);
        }
    }

    private void validatePersona(Persona p) throws ValidationException {
        if (p.getNombre() == null || p.getNombre().trim().length() == 0)
            throw new ValidationException("Debe ingresar el nombre");
        else if (p.getNombre().trim().length() > 40)
            throw new ValidationException("El nombre no puede superar los 40 caracteres");
        else if (p.getCondicionIVA() == null)
            throw new ValidationException("Debe especificar la condición de IVA");
        if (p instanceof PersonaFisica)
            validatePersonaFisica((PersonaFisica)p);
        else
            validatePersonaJuridica((PersonaJuridica)p);
        validateDirecciones(p);
        validateContactos(p);
    }
    
    private void validatePersonaFisica(PersonaFisica p) throws ValidationException {
        if (p.getDni() == 0)
            throw new ValidationException("Debe ingresar el DNI");
        else if (p.getFechaNacimiento() == null)
            throw new ValidationException("Debe especificar la fecha de nacimiento");
        else if (p.getSexo() == null)
            throw new ValidationException("Debe especificar el sexo");
        
        try {
            PersonaFisica aux = (PersonaFisica) em.createNamedQuery("PersonaFisica.findByDni").setParameter("dni", p.getDni()).getSingleResult();
            if (!aux.equals(p))
                throw new ValidationException("Ya hay una persona con ese número de DNI [" + aux.getNombre() + "]");
        } catch (NoResultException ex) {}
    }
    
    private void validatePersonaJuridica(PersonaJuridica p) throws ValidationException {
        if (p.getCuit() == null || p.getCuit().trim().length() == 0)
            throw new ValidationException("Debe ingresar el CUIT");
        else if (p.getRazonSocial() == null || p.getRazonSocial().trim().length() == 0)
            throw new ValidationException("Debe ingresar la razón social");
        else if (p.getRazonSocial().trim().length() > 40)
            throw new ValidationException("La razon social no puede superar los 40 caracteres");
        
        try {
            PersonaJuridica aux = (PersonaJuridica) em.createNamedQuery("PersonaJuridica.findByCuit").setParameter("cuit", p.getCuit()).getSingleResult();
            if (!aux.equals(p))
                throw new ValidationException("Ya hay una persona con ese número de CUIT [" + aux.getNombre() + "]");
        } catch (NoResultException ex) {}
    }
    
    private void validateDirecciones(Persona p) throws ValidationException {
        if (p.getDireccionCollection() != null) {
            for (Direccion d : p.getDireccionCollection()) {
                validateDireccion(d);
            }
        }
    }
    
    public void validateDireccion(Direccion d) throws ValidationException {
        if (d.getTipoDireccion() == null)
            throw new ValidationException("Debe especificar el tipo de domicilio");
        else if (d.getCodigoPostal() == null || d.getCodigoPostal().trim().length() == 0)
            throw new ValidationException("Debe especificar el código postal");
        else if (d.getCodigoPostal().trim().length() > 8)
            throw new ValidationException("El Código Postal no puede superar los 8 caracteres");
        else if (d.getObservaciones() != null && d.getObservaciones().trim().length() > 255)
            throw new ValidationException("Las observaciones no pueden superar los 255 caracteres");
        if (d instanceof DireccionTipificada) {
            DireccionTipificada dt = (DireccionTipificada) d;
            if (dt.getCalle() == null) 
                throw new ValidationException("Debe especificar la calle");
            else if (dt.getNumero() == null || dt.getNumero().trim().length() == 0)
                throw new ValidationException("Debe especificar el número");
            else if (dt.getNumero().trim().length() > 10)
                throw new ValidationException("El número no puede superar los 10 caracteres");
            else if (dt.getUnidadFuncional() != null && dt.getUnidadFuncional().trim().length() > 10)
                throw new ValidationException("La U.F. no puede superar los 10 caracteres");
        } else if (d instanceof DireccionSinCalle) {
            DireccionSinCalle dsc = (DireccionSinCalle) d;
            if (dsc.getLocalidad() == null) 
                throw new ValidationException("Debe especificar la localidad");
            else if (dsc.getDireccion() == null || dsc.getDireccion().trim().length() == 0)
                throw new ValidationException("Debe especificar la dirección");
            else if (dsc.getDireccion().trim().length() > 255)
                throw new ValidationException("La dirección no puede superar los 255 caracteres");
        } else if (d instanceof DireccionInternacional) {
            DireccionInternacional di = (DireccionInternacional) d;
            if (di.getPais() == null) 
                throw new ValidationException("Debe especificar el país");
            else if (di.getProvincia() == null || di.getProvincia().trim().length() == 0)
                throw new ValidationException("Debe especificar la provincia");
            else if (di.getProvincia().trim().length() > 40)
                throw new ValidationException("La provincia no puede superar los 40 caracteres");
            else if (di.getLocalidad() == null || di.getLocalidad().trim().length() == 0)
                throw new ValidationException("Debe especificar la localidad");
            else if (di.getLocalidad().trim().length() > 40)
                throw new ValidationException("La localidad no puede superar los 40 caracteres");
            else if (di.getDireccion() == null || di.getDireccion().trim().length() == 0)
                throw new ValidationException("Debe especificar la dirección");
            else if (di.getDireccion().trim().length() > 255)
                throw new ValidationException("La dirección no puede superar los 255 caracteres");
        }
    }

    private void validateContactos(Persona p) throws ValidationException {
        if (p.getContactoPersonaCollection() != null) {
            for (ContactoPersona cp : p.getContactoPersonaCollection()) {
                validateContacto(cp);
            }
        }
    }
    
    public void validateContacto(ContactoPersona cp) throws ValidationException {
        if (cp.getTipo() == null)
            throw new ValidationException("Debe especificar el tipo de contacto");
        if (cp instanceof EmailPersona) {
            EmailPersona ep = (EmailPersona) cp;
            if (ep.getEmail() == null || ep.getEmail().trim().length() == 0)
                throw new ValidationException("Debe especificar el email");
            else if (ep.getEmail().trim().length() > 40)
                throw new ValidationException("El email no puede superar los 40 caracteres");
        } else if (cp instanceof TelefonoPersona) {
            TelefonoPersona tp = (TelefonoPersona) cp;
            if (tp.getTelefono() == null || tp.getTelefono().trim().length() == 0)
                throw new ValidationException("Debe especificar el teléfono");
            else if (tp.getTelefono().trim().length() > 40)
                throw new ValidationException("El teléfono no puede superar los 40 caracteres");
        } else if (cp instanceof WebPersona) {
            WebPersona wp = (WebPersona) cp;
            if (wp.getUrl() == null || wp.getUrl().trim().length() == 0)
                throw new ValidationException("Debe especificar la página web");
            else if (wp.getUrl().trim().length() > 40)
                throw new ValidationException("La página web no puede superar los 40 caracteres");
        }
    }

    @SecuredMethod({
        @AllowedOption(option="PERSONA", access="delete")
    })
    public void deletePersona(Persona p, SystemUser user) throws PersistenceException, ValidationException {
        boolean ok = true;
        if (p instanceof PersonaFisica) {
            try {
                SystemUser su = (SystemUser) em.createNamedQuery("SystemUserPersonaFisica.findByPersona").setParameter("persona", p).getSingleResult();
                if (su != null)
                    ok = false;
            } catch (NoResultException nre) {}
            if (!ok) {
                ValidationException ve = new ValidationException("No puede eliminar una persona que sea un usuario");
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error eliminando Persona " + p.toString(), ve);
                throw ve;
            }
        }
        try {
            p = em.find(Persona.class, p.getId());
            for (String profile : config.getPersonaProfiles()) {
//                if (profile.trim().toLowerCase().equals("empleado") || profile.trim().toLowerCase().equals("cliente") || profile.trim().toLowerCase().equals("proveedor")) {
                    Object obj = getProfileForPersona(p, profile, user);
                    if (obj != null) 
                        em.remove(obj);
//                } else {
//                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, "ACORDARSE DE SACAR ESTOOOO");
//                }
            }
            em.remove(p);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error eliminando Persona " + p.toString(), e);
            throw new PersistenceException("Error eliminando persona " + p.toString(), e);
        }
    }

    @SecuredMethod({
        @AllowedOption(option="PERSONA", access="all")
    })
    public Object getProfileForPersona(Persona p, String profile, SystemUser su) {
        try {
            return em.createNamedQuery(profile + ".findByPersona").setParameter("persona", p).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public void refreshPersona(Persona p) {
        em.getEntityManagerFactory().getCache().evict(Persona.class, p.getId());
    }

    @SecuredMethod({
        @AllowedOption(option="PERSONA", access="create"),
        @AllowedOption(option="PERSONA", access="update")
    })
    public Collection completeProfile(String profile, String query, SystemUser su) {
        Collection<Persona> personas = em.createNamedQuery("Persona.findAllByNombre").setParameter("nombre", query + "%").getResultList();
        Collection profiles = new ArrayList();
        for (Persona p : personas) {
            Object aux = getProfileForPersona(p, profile, su);
            if (aux != null)
                profiles.add(aux);
        }
        return profiles;
    }

    @SecuredMethod({
        @AllowedOption(option="PERSONA", access="create"),
        @AllowedOption(option="PERSONA", access="update")
    })
    public Collection getAllObjectsForProfile(String profile, SystemUser su) {
        Collection col = em.createNamedQuery(profile + ".findAll").getResultList();
        return col;
    }

    @SecuredMethod({
        @AllowedOption(option="PERSONA", access="create"),
        @AllowedOption(option="PERSONA", access="update")
    })
    public Collection getAllObjectsByNombre(String profile, String query, SystemUser su) {
        Collection col = em.createNamedQuery(profile + ".findAllByNombre").setParameter("nombre", query + "%").getResultList();
        return col;
    }
}
