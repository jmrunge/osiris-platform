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
@Table(name = "DireccionSinCalle")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DireccionSinCalle.findAll", query = "SELECT d FROM DireccionSinCalle d"),
    @NamedQuery(name = "DireccionSinCalle.findById", query = "SELECT d FROM DireccionSinCalle d WHERE d.id = :id"),
    @NamedQuery(name = "DireccionSinCalle.findByLocalidad", query = "SELECT d FROM DireccionSinCalle d WHERE d.localidad = :localidad"),
    @NamedQuery(name = "DireccionSinCalle.findByDireccion", query = "SELECT d FROM DireccionSinCalle d WHERE d.direccion = :direccion")})
public class DireccionSinCalle extends Direccion {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "direccion")
    private String direccion;
    @JoinColumn(name = "localidad_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Localidad localidad;

    public DireccionSinCalle() {
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DireccionSinCalle)) {
            return false;
        }
        DireccionSinCalle other = (DireccionSinCalle) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String getDireccionCompleta() {
        return direccion
                + (getObservaciones() != null && getObservaciones().length() > 0 ? " [" + getObservaciones() + "] " : "") 
                + " - " + localidad.getNombre() 
                + " (" + getCodigoPostal() + "), "
                + localidad.getProvincia().getNombre() 
                + ", " + localidad.getProvincia().getPais().getNombre();
    }
    
}
