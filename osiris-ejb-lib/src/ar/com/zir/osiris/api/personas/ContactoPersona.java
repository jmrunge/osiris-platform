/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.api.personas;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author jmrunge
 */
@Entity
@Customizer(ar.com.zir.osiris.ejb.persistence.RelationalDescriptorCustomizer.class)
@Table(name = "contactopersona")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ContactoPersona.findAll", query = "SELECT c FROM ContactoPersona c"),
    @NamedQuery(name = "ContactoPersona.findById", query = "SELECT c FROM ContactoPersona c WHERE c.id = :id"),
    @NamedQuery(name = "ContactoPersona.findByTipo", query = "SELECT c FROM ContactoPersona c WHERE c.tipo = :tipo"),
    @NamedQuery(name = "ContactoPersona.findByPersona", query = "SELECT c FROM ContactoPersona c WHERE c.persona = :persona")})
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class ContactoPersona implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(generator="ContactoPersonaSeq")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "tipo")
    private TipoContacto tipo;
    @JoinColumn(name = "persona_id", referencedColumnName = "id")
    @ManyToOne(optional=false)
    private Persona persona;

    public ContactoPersona() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TipoContacto getTipo() {
        return tipo;
    }

    public void setTipo(TipoContacto tipo) {
        this.tipo = tipo;
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
        if (!(object instanceof ContactoPersona)) {
            return false;
        }
        ContactoPersona other = (ContactoPersona) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return _toString() + " (" + tipo + ")";
    }
    
    protected abstract String _toString();
    
}
