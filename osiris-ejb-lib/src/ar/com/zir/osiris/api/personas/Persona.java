/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.api.personas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author jmrunge
 */
@Entity
@Customizer(ar.com.zir.osiris.ejb.persistence.RelationalDescriptorCustomizer.class)
@Table(name = "persona")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Persona.findAll", query = "SELECT p FROM Persona p"),
    @NamedQuery(name = "Persona.findAllByNombre", query = "SELECT p FROM Persona p WHERE p.nombre like :nombre"),
    @NamedQuery(name = "Persona.findById", query = "SELECT p FROM Persona p WHERE p.id = :id"),
    @NamedQuery(name = "Persona.findByNombre", query = "SELECT p FROM Persona p WHERE p.nombre = :nombre")})
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Persona implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(generator="PersonaSeq")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "condicionIVA")
    private CondicionIVA condicionIVA;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "persona", orphanRemoval = true)
    private Collection<Direccion> direccionCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "persona", orphanRemoval=true)
    private Collection<ContactoPersona> contactoPersonaCollection;

    public Persona() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public CondicionIVA getCondicionIVA() {
        return condicionIVA;
    }

    public void setCondicionIVA(CondicionIVA condicionIVA) {
        this.condicionIVA = condicionIVA;
    }

    @XmlTransient
    public Collection<Direccion> getDireccionCollection() {
        return direccionCollection;
    }

    public void setDireccionCollection(Collection<Direccion> direccionCollection) {
        this.direccionCollection = direccionCollection;
    }

    @XmlTransient
    public Collection<ContactoPersona> getContactoPersonaCollection() {
        return contactoPersonaCollection;
    }

    public void setContactoPersonaCollection(Collection<ContactoPersona> contactoPersonaCollection) {
        this.contactoPersonaCollection = contactoPersonaCollection;
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
        if (!(object instanceof Persona)) {
            return false;
        }
        Persona other = (Persona) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public void addDireccion(Direccion direccion) {
        if (direccionCollection == null)
            direccionCollection = new ArrayList<>();
        direccion.setPersona(this);
        direccionCollection.add(direccion);
    }

    public void addContactoPersona(ContactoPersona contacto) {
        if (contactoPersonaCollection == null)
            contactoPersonaCollection = new ArrayList<>();
        contacto.setPersona(this);
        contactoPersonaCollection.add(contacto);
    }

}
