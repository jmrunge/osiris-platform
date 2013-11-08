/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.api.personas;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jmrunge
 */
@Entity
@Table(name = "emailpersona")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EmailPersona.findAll", query = "SELECT e FROM EmailPersona e"),
    @NamedQuery(name = "EmailPersona.findByEmail", query = "SELECT e FROM EmailPersona e WHERE e.email = :email")})
public class EmailPersona extends ContactoPersona {
    @Basic(optional = false)
    @Column(name = "email")
    private String email;

    public EmailPersona() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        if (!(object instanceof EmailPersona)) {
            return false;
        }
        EmailPersona other = (EmailPersona) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    protected String _toString() {
        return email;
    }
    
}
