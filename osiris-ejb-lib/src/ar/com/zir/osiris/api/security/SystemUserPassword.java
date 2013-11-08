/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.com.zir.osiris.api.security;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author jmrunge
 */
@Entity
@Table(name = "SystemUserPassword")
@NamedQueries({
    @NamedQuery(name = "SystemUserPassword.findAll", query = "SELECT s FROM SystemUserPassword s"),
    @NamedQuery(name = "SystemUserPassword.findById", query = "SELECT s FROM SystemUserPassword s WHERE s.id = :id"),
    @NamedQuery(name = "SystemUserPassword.findByFechaDesde", query = "SELECT s FROM SystemUserPassword s WHERE s.fechaDesde = :fechaDesde"),
    @NamedQuery(name = "SystemUserPassword.findByFechaHasta", query = "SELECT s FROM SystemUserPassword s WHERE s.fechaHasta = :fechaHasta"),
    @NamedQuery(name = "SystemUserPassword.findByPassword", query = "SELECT s FROM SystemUserPassword s WHERE s.password = :password")})
public class SystemUserPassword implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
//    @TableGenerator(
//        name="SystemUserPasswordSeq",
//        table="SEQUENCE",
//        pkColumnName="SEQ_NAME",
//        valueColumnName="SEQ_COUNT",
//        pkColumnValue="SystemUserPassword",
//        allocationSize=1)
//    @GeneratedValue(strategy=GenerationType.TABLE, generator="SystemUserPasswordSeq")
    @GeneratedValue(generator="SystemUserPasswordSeq")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "fechaDesde")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDesde;
    @Column(name = "fechaHasta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHasta;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @JoinColumn(name = "systemUser_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SystemUser systemUser;

    public SystemUserPassword() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SystemUser getSystemUser() {
        return systemUser;
    }

    public void setSystemUser(SystemUser systemUser) {
        this.systemUser = systemUser;
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
        if (!(object instanceof SystemUserPassword)) {
            return false;
        }
        SystemUserPassword other = (SystemUserPassword) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.com.zir.osiris.ejb.api.security.SystemUserPassword[id=" + id + "]";
    }

}
