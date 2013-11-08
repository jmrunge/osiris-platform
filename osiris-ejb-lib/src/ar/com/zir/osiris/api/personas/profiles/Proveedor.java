/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.api.personas.profiles;

import ar.com.zir.osiris.api.personas.Persona;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jmrunge
 */
@Entity
@Table(name = "proveedor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Proveedor.findAll", query = "SELECT p FROM Proveedor p"),
    @NamedQuery(name = "Proveedor.findById", query = "SELECT p FROM Proveedor p WHERE p.id = :id"),
    @NamedQuery(name = "Proveedor.findByPersona", query = "SELECT p FROM Proveedor p where p.persona = :persona"),
    @NamedQuery(name = "Proveedor.findByEmpleado", query = "SELECT p FROM Proveedor p where p.empleado = :empleado"),
    @NamedQuery(name = "Proveedor.findByLimiteDeuda", query = "SELECT p FROM Proveedor p WHERE p.limiteDeuda = :limiteDeuda"),
    @NamedQuery(name = "Proveedor.findByDiasDeuda", query = "SELECT p FROM Proveedor p WHERE p.diasDeuda = :diasDeuda")})
public class Proveedor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(generator="ProveedorSeq")
    private Integer id;
    @JoinColumn(name = "personaJuridica_id", referencedColumnName = "id")
    @OneToOne(optional = false)
    private Persona persona;
    @JoinColumn(name = "empleado_id", referencedColumnName = "id")
    @ManyToOne
    private Empleado empleado;
    @Basic(optional = false)
    @Column(name = "limiteDeuda")
    private BigDecimal limiteDeuda;
    @Basic(optional = false)
    @Column(name = "diasDeuda")
    private Short diasDeuda;

    public Proveedor() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getLimiteDeuda() {
        return limiteDeuda;
    }

    public void setLimiteDeuda(BigDecimal limiteDeuda) {
        this.limiteDeuda = limiteDeuda;
    }

    public Short getDiasDeuda() {
        return diasDeuda;
    }

    public void setDiasDeuda(Short diasDeuda) {
        this.diasDeuda = diasDeuda;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
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
        if (!(object instanceof Proveedor)) {
            return false;
        }
        Proveedor other = (Proveedor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.com.zir.osiris.api.personas.profiles.Proveedor[ id=" + id + " ]";
    }
    
}
