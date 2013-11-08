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
@Table(name = "telefonopersona")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TelefonoPersona.findAll", query = "SELECT t FROM TelefonoPersona t"),
    @NamedQuery(name = "TelefonoPersona.findByTelefono", query = "SELECT t FROM TelefonoPersona t WHERE t.telefono = :telefono")})
public class TelefonoPersona extends ContactoPersona {
    @Basic(optional = false)
    @Column(name = "telefono")
    private String telefono;

    public TelefonoPersona() {
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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
        if (!(object instanceof TelefonoPersona)) {
            return false;
        }
        TelefonoPersona other = (TelefonoPersona) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    protected String _toString() {
        return telefono;
    }
    
}
