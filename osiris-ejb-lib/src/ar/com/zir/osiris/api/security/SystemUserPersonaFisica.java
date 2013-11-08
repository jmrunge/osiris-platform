/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.api.security;

import ar.com.zir.osiris.api.personas.PersonaFisica;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jmrunge
 */
@Entity
@Table(name = "systemuserpersonafisica")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SystemUserPersonaFisica.findAll", query = "SELECT s FROM SystemUserPersonaFisica s"),
    @NamedQuery(name = "SystemUserPersonaFisica.findById", query = "SELECT s FROM SystemUserPersonaFisica s WHERE s.id = :id"),
    @NamedQuery(name = "SystemUserPersonaFisica.findByPersona", query = "SELECT s FROM SystemUserPersonaFisica s WHERE s.persona = :persona")})
public class SystemUserPersonaFisica extends SystemUser {
    private static final long serialVersionUID = 1L;
    @JoinColumn(name = "personaFisica_id", referencedColumnName = "id")
    @OneToOne(optional = false)
    private PersonaFisica persona;

    public SystemUserPersonaFisica() {
    }

    public PersonaFisica getPersona() {
        return persona;
    }

    public void setPersona(PersonaFisica persona) {
        this.persona = persona;
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
        if (!(object instanceof SystemUserPersonaFisica)) {
            return false;
        }
        SystemUserPersonaFisica other = (SystemUserPersonaFisica) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String getNombreCompleto() {
        return persona.getNombre();
    }

}
