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
@Table(name = "Direccion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Direccion.findAll", query = "SELECT d FROM Direccion d"),
    @NamedQuery(name = "Direccion.findById", query = "SELECT d FROM Direccion d WHERE d.id = :id"),
    @NamedQuery(name = "Direccion.findByCodigoPostal", query = "SELECT d FROM Direccion d WHERE d.codigoPostal = :codigoPostal"),
    @NamedQuery(name = "Direccion.findByObservaciones", query = "SELECT d FROM Direccion d WHERE d.observaciones = :observaciones"),
    @NamedQuery(name = "Direccion.findByTipoDireccion", query = "SELECT d FROM Direccion d WHERE d.tipoDireccion = :tipoDireccion")})
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class Direccion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
//    @TableGenerator(
//        name="DireccionSeq",
//        table="SEQUENCE",
//        pkColumnName="SEQ_NAME",
//        valueColumnName="SEQ_COUNT",
//        pkColumnValue="Direccion",
//        allocationSize=1)
//    @GeneratedValue(strategy=GenerationType.TABLE, generator="DireccionSeq")
    @GeneratedValue(generator="DireccionSeq")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "codigoPostal")
    private String codigoPostal;
    @Column(name = "observaciones")
    private String observaciones;
    @Basic(optional = false)
    @Column(name = "tipoDireccion")
    private TipoDireccion tipoDireccion;
    @JoinColumn(name = "persona_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Persona persona;

    public Direccion() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public TipoDireccion getTipoDireccion() {
        return tipoDireccion;
    }

    public void setTipoDireccion(TipoDireccion tipoDireccion) {
        this.tipoDireccion = tipoDireccion;
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
        if (!(object instanceof Direccion)) {
            return false;
        }
        Direccion other = (Direccion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getDireccionCompleta();
    }
    
    public abstract String getDireccionCompleta();
    
}
