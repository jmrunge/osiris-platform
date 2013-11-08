/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.api.personas;

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
@Table(name = "Localidad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Localidad.findAll", query = "SELECT l FROM Localidad l"),
    @NamedQuery(name = "Localidad.findById", query = "SELECT l FROM Localidad l WHERE l.id = :id"),
    @NamedQuery(name = "Localidad.findByPredeterminada", query = "SELECT l FROM Localidad l WHERE l.predeterminada = :predeterminada"),
    @NamedQuery(name = "Localidad.findByProvinciaAndPredeterminada", query = "SELECT l FROM Localidad l WHERE l.provincia = :provincia and l.predeterminada = :predeterminada"),
    @NamedQuery(name = "Localidad.findByProvincia", query = "SELECT l FROM Localidad l WHERE l.provincia = :provincia"),
    @NamedQuery(name = "Localidad.findByNombre", query = "SELECT l FROM Localidad l WHERE l.nombre = :nombre"),
    @NamedQuery(name = "Localidad.findAllByNombre", query = "SELECT l FROM Localidad l WHERE l.nombre like :nombre")})
public class Localidad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
//    @TableGenerator(
//        name="LocalidadSeq",
//        table="SEQUENCE",
//        pkColumnName="SEQ_NAME",
//        valueColumnName="SEQ_COUNT",
//        pkColumnValue="Localidad",
//        allocationSize=1)
//    @GeneratedValue(strategy=GenerationType.TABLE, generator="LocalidadSeq")
    @GeneratedValue(generator="LocalidadSeq")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "predeterminada")
    private boolean predeterminada;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @JoinColumn(name = "provincia_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Provincia provincia;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "localidad")
    private Collection<Calle> calleCollection;

    public Localidad() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean getPredeterminada() {
        return predeterminada;
    }

    public void setPredeterminada(boolean predeterminada) {
        this.predeterminada = predeterminada;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    @XmlTransient
    public Collection<Calle> getCalleCollection() {
        return calleCollection;
    }

    public void setCalleCollection(Collection<Calle> calleCollection) {
        this.calleCollection = calleCollection;
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
        if (!(object instanceof Localidad)) {
            return false;
        }
        Localidad other = (Localidad) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre + " (" + provincia.getNombre() + ", " + provincia.getPais().getNombre() + ")";
    }
    
}
