/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.api.personas;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jmrunge
 */
@Entity
@Table(name = "DireccionInternacional")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DireccionInternacional.findAll", query = "SELECT d FROM DireccionInternacional d"),
    @NamedQuery(name = "DireccionInternacional.findById", query = "SELECT d FROM DireccionInternacional d WHERE d.id = :id"),
    @NamedQuery(name = "DireccionInternacional.findByProvincia", query = "SELECT d FROM DireccionInternacional d WHERE d.provincia = :provincia"),
    @NamedQuery(name = "DireccionInternacional.findByLocalidad", query = "SELECT d FROM DireccionInternacional d WHERE d.localidad = :localidad"),
    @NamedQuery(name = "DireccionInternacional.findByDireccion", query = "SELECT d FROM DireccionInternacional d WHERE d.direccion = :direccion")})
public class DireccionInternacional extends Direccion {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "provincia")
    private String provincia;
    @Basic(optional = false)
    @Column(name = "localidad")
    private String localidad;
    @Basic(optional = false)
    @Column(name = "direccion")
    private String direccion;
    @JoinColumn(name = "pais_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Pais pais;

    public DireccionInternacional() {
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DireccionInternacional)) {
            return false;
        }
        DireccionInternacional other = (DireccionInternacional) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String getDireccionCompleta() {
        return direccion + " - " + localidad + " (" + provincia + ", " + pais.getNombre() + ")";
    }
    
}
