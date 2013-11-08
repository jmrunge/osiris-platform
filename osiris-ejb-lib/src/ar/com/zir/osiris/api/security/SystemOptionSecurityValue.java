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
@Table(name = "systemoptionsecurityvalue")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SystemOptionSecurityValue.findAll", query = "SELECT s FROM SystemOptionSecurityValue s"),
    @NamedQuery(name = "SystemOptionSecurityValue.findById", query = "SELECT s FROM SystemOptionSecurityValue s WHERE s.id = :id"),
    @NamedQuery(name = "SystemOptionSecurityValue.findByInheritedFrom", query = "SELECT s FROM SystemOptionSecurityValue s WHERE s.inheritedFrom = :inheritedFrom"),
    @NamedQuery(name = "SystemOptionSecurityValue.findByCanDoIt", query = "SELECT s FROM SystemOptionSecurityValue s WHERE s.canDoIt = :canDoIt")})
public class SystemOptionSecurityValue implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(generator="SystemOptionSecurityValueSeq")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "canDoIt")
    private Boolean canDoIt;
    @JoinColumn(name = "securityObject_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SecurityObject securityObject;
    @JoinColumn(name = "systemOptionSecurityOption_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SystemOptionSecurityOption systemOptionSecurityOption;
    @JoinColumn(name = "inheritedFrom", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private SystemOptionSecurityValue inheritedFrom;

    public SystemOptionSecurityValue() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getCanDoIt() {
        return canDoIt;
    }

    public void setCanDoIt(Boolean canDoIt) {
        this.canDoIt = canDoIt;
    }

    public SecurityObject getSecurityObject() {
        return securityObject;
    }

    public void setSecurityObject(SecurityObject securityObject) {
        this.securityObject = securityObject;
    }

    public SystemOptionSecurityOption getSystemOptionSecurityOption() {
        return systemOptionSecurityOption;
    }

    public void setSystemOptionSecurityOption(SystemOptionSecurityOption systemOptionSecurityOption) {
        this.systemOptionSecurityOption = systemOptionSecurityOption;
    }

    public SystemOptionSecurityValue getInheritedFrom() {
        return inheritedFrom;
    }

    public void setInheritedFrom(SystemOptionSecurityValue inheritedFrom) {
        this.inheritedFrom = inheritedFrom;
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
        if (!(object instanceof SystemOptionSecurityValue)) {
            return false;
        }
        SystemOptionSecurityValue other = (SystemOptionSecurityValue) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    public String getInheritedFromString() {
        String result = "";
        if (inheritedFrom != null) {
            result = inheritedFrom.getSecurityObject().getNombre();
            if (inheritedFrom.getInheritedFrom() != null)
                result = inheritedFrom.getInheritedFromString() + "/" + result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "ar.com.zir.osiris.api.security.SystemOptionSecurityValue[ id=" + id + " ]";
    }
    
}
