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
@Table(name = "personajuridica")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PersonaJuridica.findAll", query = "SELECT p FROM PersonaJuridica p"),
    @NamedQuery(name = "PersonaJuridica.findById", query = "SELECT p FROM PersonaJuridica p WHERE p.id = :id"),
    @NamedQuery(name = "PersonaJuridica.findByCondicionIVA", query = "SELECT p FROM PersonaJuridica p WHERE p.condicionIVA = :condicionIVA"),
    @NamedQuery(name = "PersonaJuridica.findByRazonSocial", query = "SELECT p FROM PersonaJuridica p WHERE p.razonSocial = :razonSocial"),
    @NamedQuery(name = "PersonaJuridica.findAllByRazonSocial", query = "SELECT p FROM PersonaJuridica p WHERE p.razonSocial like :razonSocial"),
    @NamedQuery(name = "PersonaJuridica.findByCuit", query = "SELECT p FROM PersonaJuridica p WHERE p.cuit = :cuit"),
    @NamedQuery(name = "PersonaJuridica.findAllByCuit", query = "SELECT p FROM PersonaJuridica p WHERE p.cuit like :cuit")})
public class PersonaJuridica extends Persona {
    @Basic(optional = false)
    @Column(name = "cuit")
    private String cuit;
    @Basic(optional = false)
    @Column(name = "razonSocial")
    private String razonSocial;

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
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
        if (!(object instanceof PersonaJuridica)) {
            return false;
        }
        PersonaJuridica other = (PersonaJuridica) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

}
