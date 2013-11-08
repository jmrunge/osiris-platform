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
@Table(name = "systemoptionsecurity")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SystemOptionSecurity.findAll", query = "SELECT s FROM SystemOptionSecurity s"),
    @NamedQuery(name = "SystemOptionSecurity.findById", query = "SELECT s FROM SystemOptionSecurity s WHERE s.id = :id"),
    @NamedQuery(name = "SystemOptionSecurity.findByOptionCode", query = "SELECT s FROM SystemOptionSecurity s WHERE s.optionCode = :optionCode"),
    @NamedQuery(name = "SystemOptionSecurity.findByOptionTitle", query = "SELECT s FROM SystemOptionSecurity s WHERE s.optionTitle = :optionTitle")})
public class SystemOptionSecurity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(generator="SystemOptionSecuritySeq")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "optionCode")
    private String optionCode;
    @Basic(optional = false)
    @Column(name = "optionTitle")
    private String optionTitle;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "systemOptionSecurity")
    private Collection<SystemOptionSecurityOption> systemOptionSecurityOptionCollection;

    public SystemOptionSecurity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOptionCode() {
        return optionCode;
    }

    public void setOptionCode(String optionCode) {
        this.optionCode = optionCode;
    }

    public String getOptionTitle() {
        return optionTitle;
    }

    public void setOptionTitle(String optionTitle) {
        this.optionTitle = optionTitle;
    }

    @XmlTransient
    public Collection<SystemOptionSecurityOption> getSystemOptionSecurityOptionCollection() {
        return systemOptionSecurityOptionCollection;
    }

    public void setSystemOptionSecurityOptionCollection(Collection<SystemOptionSecurityOption> systemOptionSecurityOptionCollection) {
        this.systemOptionSecurityOptionCollection = systemOptionSecurityOptionCollection;
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
        if (!(object instanceof SystemOptionSecurity)) {
            return false;
        }
        SystemOptionSecurity other = (SystemOptionSecurity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.com.zir.osiris.api.security.SystemOptionSecurity[ id=" + id + " ]";
    }
    
}
