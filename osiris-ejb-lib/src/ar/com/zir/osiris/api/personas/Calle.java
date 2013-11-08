/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.api.personas;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jmrunge
 */
@Entity
@Table(name = "Calle")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Calle.findAll", query = "SELECT c FROM Calle c"),
    @NamedQuery(name = "Calle.findById", query = "SELECT c FROM Calle c WHERE c.id = :id"),
    @NamedQuery(name = "Calle.findByLocalidad", query = "SELECT c FROM Calle c WHERE c.localidad = :localidad"),
    @NamedQuery(name = "Calle.findByNombre", query = "SELECT c FROM Calle c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "Calle.findAllByNombre", query = "SELECT c FROM Calle c WHERE c.nombre like :nombre")})
public class Calle implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
//    @TableGenerator(
//        name="CalleSeq",
//        table="SEQUENCE",
//        pkColumnName="SEQ_NAME",
//        valueColumnName="SEQ_COUNT",
//        pkColumnValue="Calle",
//        allocationSize=1)
//    @GeneratedValue(strategy=GenerationType.TABLE, generator="CalleSeq")
    @GeneratedValue(generator="CalleSeq")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @JoinColumn(name = "localidad_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Localidad localidad;

    public Calle() {
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

    public Localidad getLocalidad() {
        return localidad;
    }

    public void setLocalidad(Localidad localidad) {
        this.localidad = localidad;
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
        if (!(object instanceof Calle)) {
            return false;
        }
        Calle other = (Calle) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre + " (" + localidad.getNombre() + ", " + localidad.getProvincia().getNombre() + ", " + localidad.getProvincia().getPais().getNombre() + ")";
    }
    
}
