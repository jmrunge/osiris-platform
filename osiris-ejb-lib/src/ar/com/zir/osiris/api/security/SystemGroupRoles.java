/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.com.zir.osiris.api.security;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jmrunge
 */
@Entity
@Table(name = "SystemGroupRoles")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SystemGroupRoles.findAll", query = "SELECT s FROM SystemGroupRoles s"),
    @NamedQuery(name = "SystemGroupRoles.findById", query = "SELECT s FROM SystemGroupRoles s WHERE s.id = :id"),
    @NamedQuery(name = "SystemGroupRoles.findBySystemGroup", query = "SELECT s FROM SystemGroupRoles s WHERE s.systemGroup = :systemGroup"),
    @NamedQuery(name = "SystemGroupRoles.findBySystemRole", query = "SELECT s FROM SystemGroupRoles s WHERE s.systemRole = :systemRole")})
public class SystemGroupRoles implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
//    @TableGenerator(
//        name="SystemGroupRolesSeq",
//        table="SEQUENCE",
//        pkColumnName="SEQ_NAME",
//        valueColumnName="SEQ_COUNT",
//        pkColumnValue="SystemGroupRoles",
//        allocationSize=1)
//    @GeneratedValue(strategy=GenerationType.TABLE, generator="SystemGroupRolesSeq")
    @GeneratedValue(generator="SystemGroupRolesSeq")
    private Integer id;
    @JoinColumn(name = "systemGroup_id", referencedColumnName = "id")
    @ManyToOne(optional=false)
    private SystemGroup systemGroup;
    @JoinColumn(name = "systemRole_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SystemRole systemRole;

    public SystemGroupRoles() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SystemGroup getSystemGroup() {
        return systemGroup;
    }

    public void setSystemGroup(SystemGroup systemGroup) {
        this.systemGroup = systemGroup;
    }

    public SystemRole getSystemRole() {
        return systemRole;
    }

    public void setSystemRole(SystemRole systemRole) {
        this.systemRole = systemRole;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SystemGroupRoles)) {
            return false;
        }
        SystemGroupRoles other = (SystemGroupRoles) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.com.zir.osiris.ejb.api.security.SystemGroupRoles[id=" + id + "]";
    }

}
