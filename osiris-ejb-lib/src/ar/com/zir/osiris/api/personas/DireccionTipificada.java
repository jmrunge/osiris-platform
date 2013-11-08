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
@Table(name = "DireccionTipificada")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DireccionTipificada.findAll", query = "SELECT d FROM DireccionTipificada d"),
    @NamedQuery(name = "DireccionTipificada.findById", query = "SELECT d FROM DireccionTipificada d WHERE d.id = :id"),
    @NamedQuery(name = "DireccionTipificada.findByNumero", query = "SELECT d FROM DireccionTipificada d WHERE d.numero = :numero"),
    @NamedQuery(name = "DireccionTipificada.findByCalle", query = "SELECT d FROM DireccionTipificada d WHERE d.calle = :calle"),
    @NamedQuery(name = "DireccionTipificada.findByUnidadFuncional", query = "SELECT d FROM DireccionTipificada d WHERE d.unidadFuncional = :unidadFuncional")})
public class DireccionTipificada extends Direccion {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "numero")
    private String numero;
    @Column(name = "unidadFuncional")
    private String unidadFuncional;
    @JoinColumn(name = "calle_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Calle calle;

    public DireccionTipificada() {
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getUnidadFuncional() {
        return unidadFuncional;
    }

    public void setUnidadFuncional(String unidadFuncional) {
        this.unidadFuncional = unidadFuncional;
    }

    public Calle getCalle() {
        return calle;
    }

    public void setCalle(Calle calle) {
        this.calle = calle;
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
        if (!(object instanceof DireccionTipificada)) {
            return false;
        }
        DireccionTipificada other = (DireccionTipificada) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String getDireccionCompleta() {
        return calle.getNombre() 
                + " " + numero 
                + (unidadFuncional != null && unidadFuncional.length() > 0 ? " " + unidadFuncional : "") 
                + (getObservaciones() != null && getObservaciones().length() > 0 ? " [" + getObservaciones() + "] " : "") 
                + " - " + calle.getLocalidad().getNombre() 
                + " (" + getCodigoPostal() + "), "
                + calle.getLocalidad().getProvincia().getNombre() 
                + ", " + calle.getLocalidad().getProvincia().getPais().getNombre();
    }
    
}
