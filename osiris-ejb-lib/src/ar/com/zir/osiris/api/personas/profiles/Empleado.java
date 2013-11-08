/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.api.personas.profiles;

import ar.com.zir.osiris.api.personas.Persona;
import ar.com.zir.osiris.api.security.SystemUser;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jmrunge
 */
@Entity
@Table(name = "empleado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Empleado.findAll", query = "SELECT e FROM Empleado e"),
    @NamedQuery(name = "Empleado.findByPersona", query = "SELECT e FROM Empleado e where e.persona = :persona"),
    @NamedQuery(name = "Empleado.findById", query = "SELECT e FROM Empleado e WHERE e.id = :id")})
public class Empleado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(generator="EmpleadoSeq")
    private Integer id;
    @JoinColumn(name = "personaFisica_id", referencedColumnName = "id")
    @OneToOne(optional = false)
    private Persona persona;
    @Basic(optional = false)
    @Column(name = "limiteAdelantos")
    private BigDecimal limiteAdelantos;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empleado")
    private List<Cliente> clientes;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empleado")
    private List<Proveedor> proveedores;
    @JoinColumn(name = "systemUser_id", referencedColumnName = "id")
    @OneToOne(optional = false)
    private SystemUser systemUser;

    public Empleado() {
    }

    public Empleado(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public BigDecimal getLimiteAdelantos() {
        return limiteAdelantos;
    }

    public void setLimiteAdelantos(BigDecimal limiteAdelantos) {
        this.limiteAdelantos = limiteAdelantos;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public List<Proveedor> getProveedores() {
        return proveedores;
    }

    public void setProveedores(List<Proveedor> proveedores) {
        this.proveedores = proveedores;
    }

    public SystemUser getSystemUser() {
        return systemUser;
    }

    public void setSystemUser(SystemUser systemUser) {
        this.systemUser = systemUser;
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
        if (!(object instanceof Empleado)) {
            return false;
        }
        Empleado other = (Empleado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return persona.getNombre();
    }
    
    public void addCliente(Cliente cliente) {
        if (clientes == null)
            clientes = new ArrayList<>();
        clientes.add(cliente);
    }
    
    public void addProveedor(Proveedor proveedor) {
        if (proveedores == null)
            proveedores = new ArrayList<>();
        proveedores.add(proveedor);
    }
    
}
