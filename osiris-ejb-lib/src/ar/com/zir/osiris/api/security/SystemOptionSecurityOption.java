/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.api.security;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jmrunge
 */
@Entity
@Table(name = "systemoptionsecurityoption")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SystemOptionSecurityOption.findAll", query = "SELECT s FROM SystemOptionSecurityOption s"),
    @NamedQuery(name = "SystemOptionSecurityOption.findById", query = "SELECT s FROM SystemOptionSecurityOption s WHERE s.id = :id")})
public class SystemOptionSecurityOption implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(generator="SystemOptionSecurityOptionSeq")
    private Integer id;
    @JoinColumn(name = "systemOption_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SystemOption systemOption;
    @JoinColumn(name = "systemOptionSecurity_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SystemOptionSecurity systemOptionSecurity;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "systemOptionSecurityOption")
    private Collection<SystemOptionSecurityValue> systemOptionSecurityValueCollection;

    public SystemOptionSecurityOption() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SystemOption getSystemOption() {
        return systemOption;
    }

    public void setSystemOption(SystemOption systemOption) {
        this.systemOption = systemOption;
    }

    public SystemOptionSecurity getSystemOptionSecurity() {
        return systemOptionSecurity;
    }

    public void setSystemOptionSecurityid(SystemOptionSecurity systemOptionSecurity) {
        this.systemOptionSecurity = systemOptionSecurity;
    }

    @XmlTransient
    public Collection<SystemOptionSecurityValue> getSystemOptionSecurityValueCollection() {
        return systemOptionSecurityValueCollection;
    }

    public void setSystemOptionSecurityValueCollection(Collection<SystemOptionSecurityValue> systemOptionSecurityValueCollection) {
        this.systemOptionSecurityValueCollection = systemOptionSecurityValueCollection;
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
        if (!(object instanceof SystemOptionSecurityOption)) {
            return false;
        }
        SystemOptionSecurityOption other = (SystemOptionSecurityOption) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.com.zir.osiris.api.security.SystemOptionSecurityOption[ id=" + id + " ]";
    }
    
}
