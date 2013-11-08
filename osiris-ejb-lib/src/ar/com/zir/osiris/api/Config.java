/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.api;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jmrunge
 */
@Entity
@Table(name = "config")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Config.findAll", query = "SELECT c FROM Config c"),
    @NamedQuery(name = "Config.findByClave", query = "SELECT c FROM Config c WHERE c.clave = :clave"),
    @NamedQuery(name = "Config.findByValor", query = "SELECT c FROM Config c WHERE c.valor = :valor")})
public class Config implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "clave")
    private String clave;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "valor")
    private String valor;

    public Config() {
    }

    public Config(String clave) {
        this.clave = clave;
    }

    public Config(String clave, String valor) {
        this.clave = clave;
        this.valor = valor;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clave != null ? clave.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Config)) {
            return false;
        }
        Config other = (Config) object;
        if ((this.clave == null && other.clave != null) || (this.clave != null && !this.clave.equals(other.clave))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.com.zir.osiris.api.Config[ clave=" + clave + " ]";
    }
    
}
