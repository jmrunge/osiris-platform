/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.com.zir.osiris.api.security;

import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jmrunge
 */
@Entity
@Table(name = "SystemGroup")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SystemGroup.findAll", query = "SELECT s FROM SystemGroup s"),
    @NamedQuery(name = "SystemGroup.findAllByNombre", query = "SELECT s FROM SystemGroup s WHERE s.nombre like :nombre"),
    @NamedQuery(name = "SystemGroup.findAllByDescripcion", query = "SELECT s FROM SystemGroup s WHERE s.nombre like :descripcion"),
    @NamedQuery(name = "SystemGroup.findById", query = "SELECT s FROM SystemGroup s WHERE s.id = :id"),
    @NamedQuery(name = "SystemGroup.findByNombre", query = "SELECT s FROM SystemGroup s WHERE s.nombre = :nombre"),
    @NamedQuery(name = "SystemGroup.findByDescripcion", query = "SELECT s FROM SystemGroup s WHERE s.descripcion = :descripcion")})
public class SystemGroup extends SecurityObject {
    private static final long serialVersionUID = 1L;
    @Column(name = "descripcion")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "systemGroup", orphanRemoval=true, fetch=FetchType.EAGER)
    private List<SystemGroupRoles> systemGroupRoles;

    public SystemGroup() {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SystemGroup)) {
            return false;
        }
        SystemGroup other = (SystemGroup) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

}
