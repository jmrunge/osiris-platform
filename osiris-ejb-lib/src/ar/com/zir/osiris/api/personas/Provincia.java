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
@Table(name = "Provincia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Provincia.findAll", query = "SELECT p FROM Provincia p"),
    @NamedQuery(name = "Provincia.findById", query = "SELECT p FROM Provincia p WHERE p.id = :id"),
    @NamedQuery(name = "Provincia.findByPredeterminada", query = "SELECT p FROM Provincia p WHERE p.predeterminada = :predeterminada"),
    @NamedQuery(name = "Provincia.findByPaisAndPredeterminada", query = "SELECT p FROM Provincia p WHERE p.pais = :pais and p.predeterminada = :predeterminada"),
    @NamedQuery(name = "Provincia.findByPais", query = "SELECT p FROM Provincia p WHERE p.pais = :pais"),
    @NamedQuery(name = "Provincia.findByNombre", query = "SELECT p FROM Provincia p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Provincia.findAllByNombre", query = "SELECT p FROM Provincia p WHERE p.nombre like :nombre")})
public class Provincia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
//    @TableGenerator(
//        name="ProvinciaSeq",
//        table="SEQUENCE",
//        pkColumnName="SEQ_NAME",
//        valueColumnName="SEQ_COUNT",
//        pkColumnValue="Provincia",
//        allocationSize=1)
//    @GeneratedValue(strategy=GenerationType.TABLE, generator="ProvinciaSeq")
    @GeneratedValue(generator="ProvinciaSeq")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "predeterminada")
    private Boolean predeterminada;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "provincia")
    private Collection<Localidad> localidadCollection;
    @JoinColumn(name = "pais_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Pais pais;

    public Provincia() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getPredeterminada() {
        return predeterminada;
    }

    public void setPredeterminada(Boolean predeterminada) {
        this.predeterminada = predeterminada;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public Collection<Localidad> getLocalidadCollection() {
        return localidadCollection;
    }

    public void setLocalidadCollection(Collection<Localidad> localidadCollection) {
        this.localidadCollection = localidadCollection;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
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
        if (!(object instanceof Provincia)) {
            return false;
        }
        Provincia other = (Provincia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre + " (" + pais.getNombre() + ")";
    }
    
}
