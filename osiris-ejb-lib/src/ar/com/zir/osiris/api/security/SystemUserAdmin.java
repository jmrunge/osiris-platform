/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.api.security;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jmrunge
 */
@Entity
@Table(name = "systemuseradmin")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SystemUserAdmin.findAll", query = "SELECT s FROM SystemUserAdmin s"),
    @NamedQuery(name = "SystemUserAdmin.findById", query = "SELECT s FROM SystemUserAdmin s WHERE s.id = :id")})
public class SystemUserAdmin extends SystemUser {
    private static final long serialVersionUID = 1L;

    public SystemUserAdmin() {
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
        if (!(object instanceof SystemUserAdmin)) {
            return false;
        }
        SystemUserAdmin other = (SystemUserAdmin) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String getNombreCompleto() {
        return "Administrador del Sistema";
    }
    
}
