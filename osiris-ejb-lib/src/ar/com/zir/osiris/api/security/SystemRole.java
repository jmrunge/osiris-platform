/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.com.zir.osiris.api.security;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jmrunge
 */
@Entity
@Table(name = "SystemRole")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SystemRole.findAll", query = "SELECT s FROM SystemRole s"),
    @NamedQuery(name = "SystemRole.findAllByNombre", query = "SELECT s FROM SystemRole s WHERE s.nombre like :nombre"),
    @NamedQuery(name = "SystemRole.findAllByDescripcion", query = "SELECT s FROM SystemRole s WHERE s.nombre like :descripcion"),
    @NamedQuery(name = "SystemRole.findById", query = "SELECT s FROM SystemRole s WHERE s.id = :id"),
    @NamedQuery(name = "SystemRole.findByNombre", query = "SELECT s FROM SystemRole s WHERE s.nombre = :nombre"),
    @NamedQuery(name = "SystemRole.findByDescripcion", query = "SELECT s FROM SystemRole s WHERE s.descripcion = :descripcion")})
public class SystemRole extends SecurityObject {
    private static final long serialVersionUID = 1L;
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "systemRole", orphanRemoval=true, fetch=FetchType.EAGER)
    private List<SystemGroupRoles> systemGroupRoles;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "systemRole", orphanRemoval=true, fetch=FetchType.EAGER)
    private List<SystemRoleUsers> systemRoleUsers;

    public SystemRole() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<SystemGroupRoles> getSystemGroupRoles() {
        return systemGroupRoles;
    }

    public void setSystemGroupRoles(List<SystemGroupRoles> systemGroupRoles) {
        this.systemGroupRoles = systemGroupRoles;
    }

    public List<SystemRoleUsers> getSystemRoleUsers() {
        return systemRoleUsers;
    }

    public void setSystemRoleUsers(List<SystemRoleUsers> systemRoleUsers) {
        this.systemRoleUsers = systemRoleUsers;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SystemRole)) {
            return false;
        }
        SystemRole other = (SystemRole) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    public void addSystemGroup(SystemGroupRoles sgr) {
        if (systemGroupRoles == null)
            systemGroupRoles = new ArrayList<>();
        systemGroupRoles.add(sgr);
    }

}
