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
@Table(name = "SystemRoleUsers")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SystemRoleUsers.findAll", query = "SELECT s FROM SystemRoleUsers s"),
    @NamedQuery(name = "SystemRoleUsers.findById", query = "SELECT s FROM SystemRoleUsers s WHERE s.id = :id"),
    @NamedQuery(name = "SystemRoleUsers.findBySystemUser", query = "SELECT s FROM SystemRoleUsers s WHERE s.systemUser = :systemUser"),
    @NamedQuery(name = "SystemRoleUsers.findBySystemRole", query = "SELECT s FROM SystemRoleUsers s WHERE s.systemRole = :systemRole")})
public class SystemRoleUsers implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
//    @TableGenerator(
//        name="SystemRoleUsersSeq",
//        table="SEQUENCE",
//        pkColumnName="SEQ_NAME",
//        valueColumnName="SEQ_COUNT",
//        pkColumnValue="SystemRoleUsers",
//        allocationSize=1)
//    @GeneratedValue(strategy=GenerationType.TABLE, generator="SystemRoleUsersSeq")
    @GeneratedValue(generator="SystemRoleUsersSeq")
    private Integer id;
    @JoinColumn(name = "systemUser_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SystemUser systemUser;
    @JoinColumn(name = "systemRole_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SystemRole systemRole;

    public SystemRoleUsers() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SystemUser getSystemUser() {
        return systemUser;
    }

    public void setSystemUser(SystemUser systemUser) {
        this.systemUser = systemUser;
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
        if (!(object instanceof SystemRoleUsers)) {
            return false;
        }
        SystemRoleUsers other = (SystemRoleUsers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.com.zir.osiris.ejb.api.security.SystemRoleUsers[id=" + id + "]";
    }

}
