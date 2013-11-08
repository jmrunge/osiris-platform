/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.api.personas.profiles;

import ar.com.zir.osiris.api.personas.Persona;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jmrunge
 */
@Entity
@Table(name = "socio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Socio.findAll", query = "SELECT s FROM Socio s"),
    @NamedQuery(name = "Socio.findById", query = "SELECT s FROM Socio s WHERE s.id = :id"),
    @NamedQuery(name = "Socio.findByPersona", query = "SELECT s FROM Socio s where s.persona = :persona"),
    @NamedQuery(name = "Socio.findByPorcentaje", query = "SELECT s FROM Socio s WHERE s.porcentaje = :porcentaje")})
public class Socio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(generator="SocioSeq")
    private Integer id;
    @JoinColumn(name = "persona_id", referencedColumnName = "id")
    @OneToOne(optional = false)
    private Persona persona;
    @Basic(optional = false)
    @Column(name = "porcentaje")
    private Short porcentaje;

    public Socio() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Short getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Short porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
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
        if (!(object instanceof Socio)) {
            return false;
        }
        Socio other = (Socio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.com.zir.osiris.api.personas.profiles.Socio[ id=" + id + " ]";
    }
    
}
