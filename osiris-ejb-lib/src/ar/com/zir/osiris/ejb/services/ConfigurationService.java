/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.ejb.services;

import ar.com.zir.osiris.api.personas.Calle;
import ar.com.zir.osiris.api.personas.Localidad;
import ar.com.zir.osiris.api.personas.Pais;
import ar.com.zir.osiris.api.personas.Provincia;
import ar.com.zir.osiris.api.security.SystemUser;
import ar.com.zir.osiris.ejb.exceptions.PersistenceException;
import ar.com.zir.osiris.ejb.exceptions.ValidationException;
import ar.com.zir.osiris.ejb.interceptors.AllowedOption;
import ar.com.zir.osiris.ejb.interceptors.RollbackTransaction;
import ar.com.zir.osiris.ejb.interceptors.SecuredBean;
import ar.com.zir.osiris.ejb.interceptors.SecuredMethod;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jmrunge
 */
@Stateless
@LocalBean
@RollbackTransaction
@SecuredBean
public class ConfigurationService implements Serializable {
    @PersistenceContext(unitName="osiris-ejbPU")
    private EntityManager em;

    @SecuredMethod({
        @AllowedOption(option="PAIS", access="all"),
        @AllowedOption(option="PROVINCIA", access="all"),
        @AllowedOption(option="LOCALIDAD", access="all"),
        @AllowedOption(option="CALLE", access="all")
    })
    public Collection<Pais> findPaisesBySearchKey(String searchKey, SystemUser user) {
        return em.createNamedQuery("Pais.findAllByNombre").setParameter("nombre", "%" + searchKey + "%").getResultList();
    }

    @SecuredMethod({
        @AllowedOption(option="PAIS", access="create")
    })
    public Pais createPais(Pais p, SystemUser user) throws ValidationException, PersistenceException {
        try {
            validatePais(p);
            em.persist(p);
            checkPaisPredeterminado(p, user);
            return p;
        } catch (ValidationException ve) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error validando País " + p.toString(), ve);
            throw ve;
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error creando País " + p.toString(), e);
            throw new PersistenceException("Error creando País " + p.toString(), e);
        }
    }
    
    private void checkPaisPredeterminado(Pais p, SystemUser user) throws ValidationException, PersistenceException {
        try {
            Pais aux = (Pais) em.createNamedQuery("Pais.findByPredeterminado").setParameter("predeterminado", Boolean.TRUE).getSingleResult();
        } catch (NoResultException e) {
            throw new ValidationException("Debe haber un país predeterminado");
        } catch (NonUniqueResultException e) {
            List<Pais> paises = em.createNamedQuery("Pais.findByPredeterminado").setParameter("predeterminado", Boolean.TRUE).getResultList();
            for (Pais aux : paises) {
                if (!aux.equals(p)) {
                    aux.setPredeterminado(false);
                    updatePais(aux, user);
                }
            }
        }
    }

    @SecuredMethod({
        @AllowedOption(option="PAIS", access="update")
    })
    public Pais updatePais(Pais p, SystemUser user) throws ValidationException, PersistenceException {
        try {
            validatePais(p);
            p = em.merge(p);
            checkPaisPredeterminado(p, user);
            return p;
        } catch (ValidationException ve) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error validando País " + p.toString(), ve);
            throw ve;
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error actualizando País " + p.toString(), e);
            throw new PersistenceException("Error actualizando País " + p.toString(), e);
        }
    }

    private void validatePais(Pais p) throws ValidationException {
        if (p.getNombre() == null || p.getNombre().trim().length() == 0)
            throw new ValidationException("Debe ingresar el nombre");
        else if (p.getNombre().trim().length() > 40)
            throw new ValidationException("El nombre no puede superar los 40 caracteres");
    }

    @SecuredMethod({
        @AllowedOption(option="PAIS", access="delete")
    })
    public void deletePais(Pais p, SystemUser user) throws PersistenceException, ValidationException {
        boolean ok = false;
        try {
            em.createNamedQuery("Provincia.findByPais").setParameter("pais", p).getSingleResult();
        } catch (NoResultException nre) {
            ok = true;
        }
        if (!ok) {
            ValidationException ve = new ValidationException("No puede eliminar un país que tenga provincias");
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error eliminando País " + p.toString(), ve);
            throw ve;
        }
        try {
            p = em.find(Pais.class, p.getId());
            em.remove(p);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error eliminando País " + p.toString(), e);
            throw new PersistenceException("Error eliminando País " + p.toString(), e);
        }
    }
    
    private void refreshPais(Pais p) {
        em.getEntityManagerFactory().getCache().evict(Pais.class);
    }

    @SecuredMethod({
        @AllowedOption(option="PROVINCIA", access="all"),
        @AllowedOption(option="LOCALIDAD", access="all"),
        @AllowedOption(option="CALLE", access="all")
    })
    public Collection<Provincia> findProvinciasBySearchKey(String searchKey, SystemUser user) {
        return em.createNamedQuery("Provincia.findAllByNombre").setParameter("nombre", "%" + searchKey + "%").getResultList();
    }

    @SecuredMethod({
        @AllowedOption(option="PROVINCIA", access="create")
    })
    public Provincia createProvincia(Provincia p, SystemUser user) throws ValidationException, PersistenceException {
        try {
            validateProvincia(p);
            em.persist(p);
            checkProvinciaPredeterminada(p, user);
            refreshPais(p.getPais());
            return p;
        } catch (ValidationException ve) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error validando Provincia " + p.toString(), ve);
            throw ve;
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error creando Provincia " + p.toString(), e);
            throw new PersistenceException("Error creando Provincia " + p.toString(), e);
        }
    }
    
    private void checkProvinciaPredeterminada(Provincia p, SystemUser user) throws ValidationException, PersistenceException {
        try {
            Provincia aux = (Provincia) em.createNamedQuery("Provincia.findByPaisAndPredeterminada").setParameter("pais", p.getPais()).setParameter("predeterminada", Boolean.TRUE).getSingleResult();
        } catch (NoResultException e) {
            throw new ValidationException("Debe haber una provincia predeterminada");
        } catch (NonUniqueResultException e) {
            List<Provincia> provincias = em.createNamedQuery("Provincia.findByPaisAndPredeterminada").setParameter("pais", p.getPais()).setParameter("predeterminada", Boolean.TRUE).getResultList();
            for (Provincia aux : provincias) {
                if (!aux.equals(p)) {
                    aux.setPredeterminada(false);
                    updateProvincia(aux, user);
                }
            }
        }
    }

    @SecuredMethod({
        @AllowedOption(option="PROVINCIA", access="update")
    })
    public Provincia updateProvincia(Provincia p, SystemUser user) throws ValidationException, PersistenceException {
        try {
            validateProvincia(p);
            p = em.merge(p);
            checkProvinciaPredeterminada(p, user);
            refreshPais(p.getPais());
            return p;
        } catch (ValidationException ve) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error validando Provincia " + p.toString(), ve);
            throw ve;
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error actualizando Provincia " + p.toString(), e);
            throw new PersistenceException("Error actualizando Provincia " + p.toString(), e);
        }
    }
    
    private void validateProvincia(Provincia p) throws ValidationException {
        if (p.getNombre() == null || p.getNombre().trim().length() == 0)
            throw new ValidationException("Debe ingresar el nombre");
        else if (p.getNombre().trim().length() > 40)
            throw new ValidationException("El nombre no puede superar los 40 caracteres");
        else if (p.getPais() == null)
            throw new ValidationException("Debe seleccionar el país");
    }

    @SecuredMethod({
        @AllowedOption(option="PROVINCIA", access="delete")
    })
    public void deleteProvincia(Provincia p, SystemUser user) throws PersistenceException, ValidationException {
        boolean ok = false;
        try {
            em.createNamedQuery("Localidad.findByProvincia").setParameter("provincia", p).getSingleResult();
        } catch (NoResultException nre) {
            ok = true;
        }
        if (!ok) {
            ValidationException ve = new ValidationException("No puede eliminar una provincia que tenga localidades");
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error eliminando Provincia " + p.toString(), ve);
            throw ve;
        }
        try {
            p = em.find(Provincia.class, p.getId());
            em.remove(p);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error eliminando Provincia " + p.toString(), e);
            throw new PersistenceException("Error eliminando Provincia " + p.toString(), e);
        }
    }
    
    private void refreshProvincia(Provincia p) {
        em.getEntityManagerFactory().getCache().evict(Provincia.class);
    }

    @SecuredMethod({
        @AllowedOption(option="LOCALIDAD", access="all"),
        @AllowedOption(option="CALLE", access="all")
    })
    public Collection<Localidad> findLocalidadesBySearchKey(String searchKey, SystemUser user) {
        return em.createNamedQuery("Localidad.findAllByNombre").setParameter("nombre", "%" + searchKey + "%").getResultList();
    }

    @SecuredMethod({
        @AllowedOption(option="LOCALIDAD", access="create")
    })
    public Localidad createLocalidad(Localidad l, SystemUser user) throws ValidationException, PersistenceException {
        try {
            validateLocalidad(l);
            em.persist(l);
            checkLocalidadPredeterminada(l, user);
            refreshProvincia(l.getProvincia());
            return l;
        } catch (ValidationException ve) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error validando Localidad " + l.toString(), ve);
            throw ve;
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error creando Localidad " + l.toString(), e);
            throw new PersistenceException("Error creando Localidad " + l.toString(), e);
        }
    }
    
    private void checkLocalidadPredeterminada(Localidad l, SystemUser user) throws ValidationException, PersistenceException {
        try {
            Localidad aux = (Localidad) em.createNamedQuery("Localidad.findByProvinciaAndPredeterminada").setParameter("provincia", l.getProvincia()).setParameter("predeterminada", Boolean.TRUE).getSingleResult();
        } catch (NoResultException e) {
            throw new ValidationException("Debe haber una localidad predeterminada");
        } catch (NonUniqueResultException e) {
            List<Localidad> localidades = em.createNamedQuery("Localidad.findByProvinciaAndPredeterminada").setParameter("provincia", l.getProvincia()).setParameter("predeterminada", Boolean.TRUE).getResultList();
            for (Localidad aux : localidades) {
                if (!aux.equals(l)) {
                    aux.setPredeterminada(false);
                    updateLocalidad(aux, user);
                }
            }
        }
    }

    @SecuredMethod({
        @AllowedOption(option="LOCALIDAD", access="update")
    })
    public Localidad updateLocalidad(Localidad l, SystemUser user) throws ValidationException, PersistenceException {
        try {
            validateLocalidad(l);
            l = em.merge(l);
            checkLocalidadPredeterminada(l, user);
            refreshProvincia(l.getProvincia());
            return l;
        } catch (ValidationException ve) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error validando Localidad " + l.toString(), ve);
            throw ve;
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error actualizando Localidad " + l.toString(), e);
            throw new PersistenceException("Error actualizando Localidad " + l.toString(), e);
        }
    }
    
    private void validateLocalidad(Localidad p) throws ValidationException {
        if (p.getNombre() == null || p.getNombre().trim().length() == 0)
            throw new ValidationException("Debe ingresar el nombre");
        else if (p.getNombre().trim().length() > 40)
            throw new ValidationException("El nombre no puede superar los 40 caracteres");
        else if (p.getProvincia() == null)
            throw new ValidationException("Debe seleccionar la provincia");
    }

    @SecuredMethod({
        @AllowedOption(option="LOCALIDAD", access="delete")
    })
    public void deleteLocalidad(Localidad l, SystemUser user) throws PersistenceException, ValidationException {
        boolean ok = false;
        try {
            em.createNamedQuery("Calle.findByLocalidad").setParameter("localidad", l).getSingleResult();
        } catch (NoResultException nre) {
            ok = true;
        }
        if (!ok) {
            ValidationException ve = new ValidationException("No puede eliminar una localidad que tenga calles");
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error eliminando Localidad " + l.toString(), ve);
            throw ve;
        }
        ok = false;
        try {
            em.createNamedQuery("DireccionSinCalle.findByLocalidad").setParameter("localidad", l).getSingleResult();
        } catch (NoResultException nre) {
            ok = true;
        }
        if (!ok) {
            ValidationException ve = new ValidationException("No puede eliminar una localidad que tenga direcciones");
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error eliminando Localidad " + l.toString(), ve);
            throw ve;
        }
        try {
            l = em.find(Localidad.class, l.getId());
            em.remove(l);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error eliminando Localidad " + l.toString(), e);
            throw new PersistenceException("Error eliminando Localidad " + l.toString(), e);
        }
    }
    
    private void refreshLocalidad(Localidad l) {
        em.getEntityManagerFactory().getCache().evict(Localidad.class);
    }

    @SecuredMethod({
        @AllowedOption(option="CALLE", access="all")
    })
    public Collection<Calle> findCallesBySearchKey(String searchKey, SystemUser user) {
        return em.createNamedQuery("Calle.findAllByNombre").setParameter("nombre", "%" + searchKey + "%").getResultList();
    }

    @SecuredMethod({
        @AllowedOption(option="CALLE", access="create")
    })
    public Calle createCalle(Calle c, SystemUser user) throws ValidationException, PersistenceException {
        try {
            validateCalle(c);
            em.persist(c);
            refreshLocalidad(c.getLocalidad());
            return c;
        } catch (ValidationException ve) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error validando Calle " + c.toString(), ve);
            throw ve;
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error creando Calle " + c.toString(), e);
            throw new PersistenceException("Error creando Calle " + c.toString(), e);
        }
    }
    
    @SecuredMethod({
        @AllowedOption(option="CALLE", access="update")
    })
    public Calle updateCalle(Calle c, SystemUser user) throws ValidationException, PersistenceException {
        try {
            validateCalle(c);
            c = em.merge(c);
            refreshLocalidad(c.getLocalidad());
            return c;
        } catch (ValidationException ve) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error validando Calle " + c.toString(), ve);
            throw ve;
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error actualizando Calle " + c.toString(), e);
            throw new PersistenceException("Error actualizando Calle " + c.toString(), e);
        }
    }

    private void validateCalle(Calle c) throws ValidationException {
        if (c.getNombre() == null || c.getNombre().trim().length() == 0)
            throw new ValidationException("Debe ingresar el nombre");
        else if (c.getNombre().trim().length() > 40)
            throw new ValidationException("El nombre no puede superar los 40 caracteres");
        else if (c.getLocalidad() == null)
            throw new ValidationException("Debe seleccionar la localidad");
    }

    @SecuredMethod({
        @AllowedOption(option="CALLE", access="delete")
    })
    public void deleteCalle(Calle c, SystemUser user) throws PersistenceException, ValidationException {
        boolean ok = false;
        try {
            em.createNamedQuery("DireccionTipificada.findByCalle").setParameter("calle", c).getSingleResult();
        } catch (NoResultException nre) {
            ok = true;
        }
        if (!ok) {
            ValidationException ve = new ValidationException("No puede eliminar una calle que tenga direcciones");
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error eliminando Calle " + c.toString(), ve);
            throw ve;
        }
        try {
            c = em.find(Calle.class, c.getId());
            em.remove(c);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error eliminando Calle " + c.toString(), e);
            throw new PersistenceException("Error eliminando Calle " + c.toString(), e);
        }
    }
    
    private void refreshCalle(Calle c) {
        em.getEntityManagerFactory().getCache().evict(Calle.class);
    }

}
