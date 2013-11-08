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
@Table(name = "Pais")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pais.findAll", query = "SELECT p FROM Pais p"),
    @NamedQuery(name = "Pais.findById", query = "SELECT p FROM Pais p WHERE p.id = :id"),
    @NamedQuery(name = "Pais.findByPredeterminado", query = "SELECT p FROM Pais p WHERE p.predeterminado = :predeterminado"),
    @NamedQuery(name = "Pais.findByNombre", query = "SELECT p FROM Pais p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Pais.findAllByNombre", query = "SELECT p FROM Pais p WHERE p.nombre like :nombre")})
public class Pais implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
//    @TableGenerator(
//        name="PaisSeq",
//        table="SEQUENCE",
//        pkColumnName="SEQ_NAME",
//        valueColumnName="SEQ_COUNT",
//        pkColumnValue="Pais",
//        allocationSize=1)
//    @GeneratedValue(strategy=GenerationType.TABLE, generator="PaisSeq")
    @GeneratedValue(generator="PaisSeq")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "predeterminado")
    private Boolean predeterminado;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pais")
    private Collection<Provincia> provinciaCollection;

    public Pais() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getPredeterminado() {
        return predeterminado;
    }

    public void setPredeterminado(Boolean predeterminado) {
        this.predeterminado = predeterminado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public Collection<Provincia> getProvinciaCollection() {
        return provinciaCollection;
    }

    public void setProvinciaCollection(Collection<Provincia> provinciaCollection) {
        this.provinciaCollection = provinciaCollection;
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
        if (!(object instanceof Pais)) {
            return false;
        }
        Pais other = (Pais) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getNombre();
    }
    
}
