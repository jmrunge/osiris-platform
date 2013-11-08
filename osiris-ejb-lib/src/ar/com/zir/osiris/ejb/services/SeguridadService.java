/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.com.zir.osiris.ejb.services;


import ar.com.zir.osiris.api.personas.Persona;
import ar.com.zir.osiris.api.personas.PersonaFisica;
import ar.com.zir.osiris.api.security.*;
import ar.com.zir.osiris.ejb.exceptions.PersistenceException;
import ar.com.zir.osiris.ejb.exceptions.ValidationException;
import ar.com.zir.osiris.ejb.interceptors.AllowedOption;
import ar.com.zir.osiris.ejb.interceptors.RollbackTransaction;
import ar.com.zir.osiris.ejb.interceptors.SecuredBean;
import ar.com.zir.osiris.ejb.interceptors.SecuredMethod;
import ar.com.zir.utils.CryptUtils;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
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
public class SeguridadService implements Serializable {
    @PersistenceContext(unitName="osiris-ejbPU")
    private EntityManager em;
    @EJB
    private PersonaService personaService;


    public SystemUser logIn(String userName, String password) {
        try {
            userName = userName.toUpperCase();
            password = CryptUtils.encrypt(password);
            SystemUser su = _getSystemUserByNombre(userName);
            String pwd = su.getCurrentPassword();
            if (password.equals(pwd)) {
                return su;
            } else {
                return null;
            }
        } catch (Exception ex) {
            Logger.getLogger(SeguridadService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @SecuredMethod({
        @AllowedOption(option="PERSONA", access="all"),
        @AllowedOption(option="USUARIO", access="all")
    })
    public Collection<SystemUser> findSystemUsersBySearchKey(String searchKey, SystemUser user) {
        return findSystemUsersBySearchKey(searchKey, false, user);
    }
    
    @SecuredMethod({
        @AllowedOption(option="EMPLEADO", access="all"),
        @AllowedOption(option="PERSONA", access="all"),
        @AllowedOption(option="USUARIO", access="all")
    })
    public Collection<SystemUser> findSystemUsersBySearchKey(String searchKey, boolean fromPersona, SystemUser user) {
        Collection<SystemUser> users = em.createNamedQuery("SystemUser.findAllByNombre").setParameter("nombre", "%" + searchKey + "%").getResultList();
        if (!fromPersona) {
            Collection<PersonaFisica> personas = personaService.findPersonasFisicasBySearchKey(searchKey, user);
            for (PersonaFisica p : personas) {
                if (p.getSystemUser() != null) {
                    if (!users.contains(p.getSystemUser()))
                        users.add(p.getSystemUser());                    
                }
            }
        }
        Collection<SystemRole> roles = findRolesBySearchKey(searchKey, user);
        for (SystemRole sr : roles) {
            if (sr.getSystemRoleUsers() != null) {
                for (SystemRoleUsers sru : sr.getSystemRoleUsers()) {
                    if (!users.contains(sru.getSystemUser()))
                        users.add(sru.getSystemUser());
                }
            }
        }
        Collection<SystemGroup> groups = findGruposBySearchKey(searchKey, user);
        for (SystemGroup sg : groups) {
            if (sg.getSystemGroupRoles() != null) {
                for (SystemGroupRoles sgr : sg.getSystemGroupRoles()) {
                    if (sgr.getSystemRole().getSystemRoleUsers() != null) {
                        for (SystemRoleUsers sru : sgr.getSystemRole().getSystemRoleUsers()) {
                            if (!users.contains(sru.getSystemUser()))
                                users.add(sru.getSystemUser());
                        }
                    }
                }
            }
        }
        SystemUser admin = null;
        for (SystemUser su : users) {
            if (su instanceof SystemUserAdmin)
                admin = su;
        }
        if (admin != null)
            users.remove(admin);
        return users;
    }

    @SecuredMethod({
        @AllowedOption(option="USUARIO", access="create")
    })
    public SystemUser createSystemUser(SystemUser s, SystemUser user) throws ValidationException, PersistenceException {
        try {
            validateSystemUser(s, true);
            setSystemUserPassword(s);
            em.persist(s);
            if (s instanceof SystemUserPersonaFisica)
                personaService.refreshPersona(((SystemUserPersonaFisica)s).getPersona());
            if (s.getSystemRoleUsers() != null) {
                for (SystemRoleUsers sru : s.getSystemRoleUsers()) {
                    em.getEntityManagerFactory().getCache().evict(SystemRole.class, sru.getSystemRole().getId());
                }
            }
            return s;
        } catch (ValidationException ve) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error validando SystemUser " + s.toString(), ve);
            throw ve;
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error creando SystemUser " + s.toString(), e);
            throw new PersistenceException("Error creando usuario " + s.toString(), e);
        }
    }

    @SecuredMethod({
        @AllowedOption(option="USUARIO", access="update")
    })
    public SystemUser updateSystemUser(SystemUser s, SystemUser user) throws ValidationException, PersistenceException {
        return updateSystemUser(s);
    }

    private SystemUser updateSystemUser(SystemUser s) throws ValidationException, PersistenceException {
        try {
            validateSystemUser(s, false);
            setSystemUserPassword(s);
            s = em.merge(s);
            if (s instanceof SystemUserPersonaFisica)
                personaService.refreshPersona(((SystemUserPersonaFisica)s).getPersona());
            if (s.getSystemRoleUsers() != null) {
                for (SystemRoleUsers sru : s.getSystemRoleUsers()) {
                    em.getEntityManagerFactory().getCache().evict(SystemRole.class, sru.getSystemRole().getId());
                }
            }
            return s;
        } catch (ValidationException ve) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error validando SystemUser " + s.toString(), ve);
            throw ve;
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error actualizando SystemUser " + s.toString(), e);
            throw new PersistenceException("Error actualizando usuario " + s.toString(), e);
        }
    }

    private void setSystemUserPassword(SystemUser su) throws ValidationException {
        if (su.getPassword() == null || su.getPassword().trim().length() == 0) return;
        try {
            String oldPassword = su.getCurrentPassword();
            String newPassword = CryptUtils.encrypt(su.getPassword());
            if (oldPassword == null || !oldPassword.equals(newPassword)) {
                Calendar c = Calendar.getInstance();
                Date fechaDesde = c.getTime();
                c.add(Calendar.SECOND, -1);
                Date fechaHasta = c.getTime();
                if (oldPassword != null)
                    su.getCurrentSystemUserPassword().setFechaHasta(fechaHasta);
                SystemUserPassword sup = new SystemUserPassword();
                sup.setFechaDesde(fechaDesde);
                sup.setPassword(newPassword);
                sup.setSystemUser(su);
                su.addSystemUserPassword(sup);
            }
        } catch (Exception ex) {
            Logger.getLogger(SeguridadService.class.getName()).log(Level.SEVERE, "Error encriptando nuevo password", ex);
            throw new ValidationException("La contraseña no puede ser encriptada");
        }
    }

    private void validateSecurityObject(SecurityObject so, String objectName) throws ValidationException {
        if (so.getNombre() == null || so.getNombre().trim().length() == 0)
            throw new ValidationException("Debe ingresar el nombre de " + objectName);
        if (so.getNombre().trim().length() > 20)
            throw new ValidationException("El nombre de " + objectName + " no puede superar los 20 caracteres");
        if (so instanceof SystemUser && existsSecurityObjectWithSameName("SystemUser", so))
            throw new ValidationException("Ya existe un usuario con ese nombre");
        if (so instanceof SystemRole && existsSecurityObjectWithSameName("SystemRole", so))
            throw new ValidationException("Ya existe un rol con ese nombre");
        if (so instanceof SystemGroup && existsSecurityObjectWithSameName("SystemGroup", so))
            throw new ValidationException("Ya existe un grupo con ese nombre");
    }
    
    private boolean existsSecurityObjectWithSameName(String clazz, SecurityObject so) {
        try {
            SecurityObject so2 = (SecurityObject) em.createNamedQuery(clazz + ".findByNombre").setParameter("nombre", so.getNombre()).getSingleResult();
            if (so2 != null) {
                if (so.getId() == null) {
                    return true;
                } else  {
                    if (!so.getId().equals(so2.getId()))
                        return true;
                    else
                        return false;
                }
            } else {
                return false;
            }
        } catch (NoResultException ex) {
            return false;
        }
    }

    private void validateSystemUser(SystemUser su, boolean checkPassword) throws ValidationException {
        validateSecurityObject(su, "usuario");
        if (su instanceof SystemUserPersonaFisica) { 
            if (((SystemUserPersonaFisica)su).getPersona() == null)
                throw new ValidationException("Debe seleccionar la persona");
            if (existsSystemUserWithThisPerson((SystemUserPersonaFisica)su))
                throw new ValidationException("Ya existe un usuario asociado a esta persona");
        }
        if (checkPassword && (su.getPassword() == null || su.getPassword().trim().length() == 0))
            throw new ValidationException("Debe ingresar la contraseña");
    }
    
    private boolean existsSystemUserWithThisPerson(SystemUserPersonaFisica su) {
        try {
            SecurityObject su2 = (SystemUserPersonaFisica) em.createNamedQuery("SystemUserPersonaFisica.findByPersona").setParameter("persona", su.getPersona()).getSingleResult();
            if (su2 != null) {
                if (su.getId() == null) {
                    return true;
                } else  {
                    if (!su.getId().equals(su2.getId()))
                        return true;
                    else
                        return false;
                }
            } else {
                return false;
            }
        } catch (NoResultException ex) {
            return false;
        }
    }

    @SecuredMethod({
        @AllowedOption(option="USUARIO", access="delete")
    })
    public void deleteSystemUser(SystemUser s, SystemUser user) throws PersistenceException {
        String nombre = null;
        try {
            s = em.find(SystemUser.class, s.getId());
            Persona p = null;
            if (s instanceof SystemUserPersonaFisica)
                p = ((SystemUserPersonaFisica)s).getPersona();
            nombre = s.getNombre();
            em.remove(s);
            if (p != null)
                personaService.refreshPersona(p);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error eliminando SystemUser " + nombre, e);
            throw new PersistenceException("Error eliminando usuario " + nombre, e);
        }
    }

    @SecuredMethod({
        @AllowedOption(option="USUARIO", access="all"),
        @AllowedOption(option="ROL", access="all"),
        @AllowedOption(option="GRUPO", access="all")
    })
    public List<SystemOption> getParentSystemOptions(SystemUser user) {
        return em.createNamedQuery("SystemOption.findAllParents").getResultList();
    }

    @SecuredMethod({
        @AllowedOption(option="USUARIO", access="all")
    })
    public List<SystemRole> getSystemRoles(SystemUser user) {
        return em.createNamedQuery("SystemRole.findAll").getResultList();
    }

    @SecuredMethod({
        @AllowedOption(option="USUARIO", access="all")
    })
    public SystemRole getSystemRoleByNombre(String nombre, SystemUser user) {
        return (SystemRole) em.createNamedQuery("SystemRole.findByNombre").setParameter("nombre", nombre).getSingleResult();
    }

    @SecuredMethod({
        @AllowedOption(option="ROL", access="create")
    })
    public SecurityObject createSystemRole(SystemRole s, SystemUser user) throws ValidationException, PersistenceException {
        try {
            validateSystemRole(s);
            em.persist(s);
            if (s.getSystemGroupRoles() != null) {
                for (SystemGroupRoles sgr : s.getSystemGroupRoles()) {
                    em.getEntityManagerFactory().getCache().evict(SystemGroup.class, sgr.getSystemGroup().getId());
                }
            }
            return s;
        } catch (ValidationException ve) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error validando SystemRole " + s.toString(), ve);
            throw ve;
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error creando SystemRole " + s.toString(), e);
            throw new PersistenceException("Error creando rol " + s.toString(), e);
        }
    }

    private void validateSystemRole(SystemRole sr) throws ValidationException {
        validateSecurityObject(sr, "rol");
        if (sr.getDescripcion() == null || sr.getDescripcion().trim().length() == 0)
            throw new ValidationException("Debe ingresar la descripción del rol");
        if (sr.getDescripcion().trim().length() > 40)
            throw new ValidationException("La descripción del rol no puede superar los 40 caracteres");
    }

    private void validateSystemGroup(SystemGroup sg) throws ValidationException {
        validateSecurityObject(sg, "grupo");
        if (sg.getDescripcion() == null || sg.getDescripcion().trim().length() == 0)
            throw new ValidationException("Debe ingresar la descripción del grupo");
        if (sg.getDescripcion().trim().length() > 40)
            throw new ValidationException("La descripción del grupo no puede superar los 40 caracteres");
    }

    @SecuredMethod({
        @AllowedOption(option="ROL", access="update")
    })
    public SecurityObject updateSystemRole(SystemRole s, SystemUser user) throws ValidationException, PersistenceException {
        return updateSystemRole(s);
    }
    
    private SecurityObject updateSystemRole(SystemRole s) throws ValidationException, PersistenceException {
        try {
            validateSystemRole(s);
            s = em.merge(s);
            updateSystemRoleChildrenOptions(s);
            if (s.getSystemGroupRoles() != null) {
                for (SystemGroupRoles sgr : s.getSystemGroupRoles()) {
                    em.getEntityManagerFactory().getCache().evict(SystemGroup.class, sgr.getSystemGroup().getId());
                }
            }
            return s;
        } catch (ValidationException ve) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error validando SystemRole " + s.toString(), ve);
            throw ve;
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error actualizando SystemRole " + s.toString(), e);
            throw new PersistenceException("Error actualizando rol " + s.toString(), e);
        }
    }

    @SecuredMethod({
        @AllowedOption(option="ROL", access="delete")
    })
    public void deleteSystemRole(SystemRole s, SystemUser user) throws PersistenceException {
        try {
            s = em.find(SystemRole.class, s.getId());
            em.remove(s);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error eliminando SystemRole " + s.toString(), e);
            throw new PersistenceException("Error eliminando rol " + s.toString(), e);
        }
    }

    @SecuredMethod({
        @AllowedOption(option="ROL", access="all")
    })
    public List<SystemGroup> getSystemGroups(SystemUser user) {
        return em.createNamedQuery("SystemGroup.findAll").getResultList();
    }

    @SecuredMethod({
        @AllowedOption(option="ROL", access="all")
    })
    public SystemGroup getSystemGroupByNombre(String nombre, SystemUser user) {
        return (SystemGroup) em.createNamedQuery("SystemGroup.findByNombre").setParameter("nombre", nombre).getSingleResult();
    }

    @SecuredMethod({
        @AllowedOption(option="GRUPO", access="create")
    })
    public SecurityObject createSystemGroup(SystemGroup s, SystemUser user) throws ValidationException, PersistenceException {
        try {
            validateSystemGroup(s);
            em.persist(s);
            return s;
        } catch (ValidationException ve) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error validando SystemGroup " + s.toString(), ve);
            throw ve;
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error creando SystemGroup " + s.toString(), e);
            throw new PersistenceException("Error creando grupo " + s.toString(), e);
        }
    }

    @SecuredMethod({
        @AllowedOption(option="GRUPO", access="update")
    })
    public SecurityObject updateSystemGroup(SystemGroup s, SystemUser user) throws ValidationException, PersistenceException {
        try {
            validateSystemGroup(s);
            s = em.merge(s);
            updateSystemGroupChildrenOptions(s);
            return s;
        } catch (ValidationException ve) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error validando SystemGroup " + s.toString(), ve);
            throw ve;
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error actualizando SystemGroup " + s.toString(), e);
            throw new PersistenceException("Error actualizando grupo " + s.toString(), e);
        }
    }

    @SecuredMethod({
        @AllowedOption(option="GRUPO", access="delete")
    })
    public void deleteSystemGroup(SystemGroup s, SystemUser user) throws PersistenceException {
        try {
            s = em.find(SystemGroup.class, s.getId());
            em.remove(s);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error eliminando SystemGroup " + s.toString(), e);
            throw new PersistenceException("Error eliminando grupo " + s.toString(), e);
        }
    }

    @SecuredMethod({
        @AllowedOption(option="EMPLEADO", access="all"),
        @AllowedOption(option="PERSONA", access="all"),
        @AllowedOption(option="USUARIO", access="all"),
        @AllowedOption(option="ROL", access="all")
    })
    public Collection findRolesBySearchKey(String searchKey, SystemUser user) {
        Collection<SystemRole> roles = em.createNamedQuery("SystemRole.findAllByNombre").setParameter("nombre", "%" + searchKey + "%").getResultList();
        Collection<SystemRole> roles2 = em.createNamedQuery("SystemRole.findAllByDescripcion").setParameter("descripcion", "%" + searchKey + "%").getResultList();
        for (SystemRole r : roles2) {
            if (!roles.contains(r))
                roles.add(r);
        }
        Collection<SystemGroup> groups = findGruposBySearchKey(searchKey, user);
        for (SystemGroup sg : groups) {
            if (sg.getSystemGroupRoles() != null) {
                for (SystemGroupRoles sgr : sg.getSystemGroupRoles()) {
                    if (!roles.contains(sgr.getSystemRole()))
                        roles.add(sgr.getSystemRole());
                }
            }
        }
        return roles;
    }

    @SecuredMethod({
        @AllowedOption(option="EMPLEADO", access="all"),
        @AllowedOption(option="PERSONA", access="all"),
        @AllowedOption(option="USUARIO", access="all"),
        @AllowedOption(option="ROL", access="all"),
        @AllowedOption(option="GRUPO", access="all")
    })
    public Collection findGruposBySearchKey(String searchKey, SystemUser user) {
        Collection<SystemGroup> grupos = em.createNamedQuery("SystemGroup.findAllByNombre").setParameter("nombre", "%" + searchKey + "%").getResultList();
        Collection<SystemGroup> grupos2 = em.createNamedQuery("SystemGroup.findAllByDescripcion").setParameter("descripcion", "%" + searchKey + "%").getResultList();
        for (SystemGroup g : grupos2) {
            if (!grupos.contains(g))
                grupos.add(g);
        }
        return grupos;
    }

    @SecuredMethod({
        @AllowedOption(option="ALL", access="all")
    })
    public SystemOption getSystemOption(String codigo, SystemUser user) {
        return (SystemOption) em.createNamedQuery("SystemOption.findByCodigo").setParameter("codigo", codigo).getSingleResult();
    }

//    public SystemUser getSystemUserByPersona(Persona p) {
//        try {
//            return (SystemUser) em.createNamedQuery("SystemUser.findByPersona").setParameter("persona", p).getSingleResult();
//        } catch (Exception e) {
//            return null;
//        }
//    }

    @SecuredMethod({
        @AllowedOption(option="USUARIO", access="execute")
    })
    public SystemUser getSystemUserByNombre(String nombre, SystemUser su) {
        return _getSystemUserByNombre(nombre);
    }
    
    private SystemUser _getSystemUserByNombre(String nombre) {
        try {
            return (SystemUser) em.createNamedQuery("SystemUser.findByNombre").setParameter("nombre", nombre).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Collection<SystemOption> getSystemOptions() {
        return em.createNamedQuery("SystemOption.findAll").getResultList();
    }
    
    private void updateSystemGroupChildrenOptions(SystemGroup group) throws ValidationException, PersistenceException {
        for (SystemGroupRoles sgr : group.getSystemGroupRoles()) {
            SystemRole role = sgr.getSystemRole();
            for (SystemOptionSecurityValue sosv : group.getSystemOptionSecurityValueCollection()) {
                updateSecurityObjectOption(sosv, role);
            }
            updateSystemRole(role);
        }
    }
    
    private void updateSystemRoleChildrenOptions(SystemRole role) throws ValidationException, PersistenceException {
        for (SystemRoleUsers sru : role.getSystemRoleUsers()) {
            SystemUser user = sru.getSystemUser();
            for (SystemOptionSecurityValue sosv : role.getSystemOptionSecurityValueCollection()) {
                updateSecurityObjectOption(sosv, user);
            }
            updateSystemUser(user);
        }
    }
    
    private void updateSecurityObjectOption(SystemOptionSecurityValue parent, SecurityObject so) {
        boolean exists = false;
        for (SystemOptionSecurityValue sosv : so.getSystemOptionSecurityValueCollection()) {
            if (sosv.getSystemOptionSecurityOption().equals(parent.getSystemOptionSecurityOption())) {
                if (sosv.getInheritedFrom() != null && sosv.getInheritedFrom().equals(parent)) {
                    if (parent.getCanDoIt().booleanValue()) {
                        if (!sosv.getCanDoIt().booleanValue()) {
                            sosv.setCanDoIt(Boolean.TRUE);
                        }
                    } else {
                        SystemOptionSecurityValue inheritedFrom = checkParentsOptions(sosv);
                        if (inheritedFrom == null) {
                            sosv.setCanDoIt(Boolean.FALSE);
                            sosv.setInheritedFrom(null);
                        } else {
                            sosv.setCanDoIt(Boolean.TRUE);
                            sosv.setInheritedFrom(inheritedFrom);
                        }
                    }
                } else {
                    if (sosv.getInheritedFrom() == null && parent.getCanDoIt().booleanValue()) {
                        sosv.setInheritedFrom(parent);
                        sosv.setCanDoIt(Boolean.TRUE);
                    }
                }
                exists = true;
                break;
            }
        }
        if (!exists) {
            SystemOptionSecurityValue sosv = new SystemOptionSecurityValue();
            sosv.setCanDoIt(Boolean.FALSE);
            sosv.setInheritedFrom(null);
            sosv.setSecurityObject(so);
            sosv.setSystemOptionSecurityOption(parent.getSystemOptionSecurityOption());
            if (parent.getCanDoIt().booleanValue()) {
                sosv.setCanDoIt(Boolean.TRUE);
                sosv.setInheritedFrom(parent);
            }
            so.addSystemOptionSecurityValue(sosv);
        }
    }
    
    private SystemOptionSecurityValue checkParentsOptions(SystemOptionSecurityValue option) {
        SecurityObject so = option.getSecurityObject();
        if (so instanceof SystemUser) {
            for (SystemRoleUsers sru : ((SystemUser)so).getSystemRoleUsers()) {
                SystemOptionSecurityValue inheritedFrom = checkSecurityObjectOptions(option, sru.getSystemRole());
                if (inheritedFrom != null)
                    return inheritedFrom;
            }
            return null;
        } else if (so instanceof SystemRole) {
            for (SystemGroupRoles sgr : ((SystemRole)so).getSystemGroupRoles()) {
                SystemOptionSecurityValue inheritedFrom = checkSecurityObjectOptions(option, sgr.getSystemGroup());
                if (inheritedFrom != null)
                    return inheritedFrom;
            }
            return null;
        } else {
            return null;
        }
    }
    
    private SystemOptionSecurityValue checkSecurityObjectOptions(SystemOptionSecurityValue option, SecurityObject so) {
        for (SystemOptionSecurityValue sosv : so.getSystemOptionSecurityValueCollection()) {
            if (sosv.getSystemOptionSecurityOption().equals(option.getSystemOptionSecurityOption())) {
                if (sosv.getCanDoIt().booleanValue())
                    return sosv;
            }
        }
        return null;
    }
    
    @SecuredMethod({
        @AllowedOption(option="ROL", access="create"),
        @AllowedOption(option="ROL", access="update"),
        @AllowedOption(option="USUARIO", access="create"),
        @AllowedOption(option="USUARIO", access="update"),
    })
    public SystemOptionSecurityValue checkSecurityObjectOptions(SystemOptionSecurityValue option, SecurityObject so, SystemUser su) {
        return checkSecurityObjectOptions(option, so);
    }

}
