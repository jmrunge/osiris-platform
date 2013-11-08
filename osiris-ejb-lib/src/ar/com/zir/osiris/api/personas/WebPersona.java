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
@Table(name = "webpersona")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WebPersona.findAll", query = "SELECT w FROM WebPersona w"),
    @NamedQuery(name = "WebPersona.findByUrl", query = "SELECT w FROM WebPersona w WHERE w.url = :url")})
public class WebPersona extends ContactoPersona {
    @Basic(optional = false)
    @Column(name = "url")
    private String url;

    public WebPersona() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
        if (!(object instanceof WebPersona)) {
            return false;
        }
        WebPersona other = (WebPersona) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    protected String _toString() {
        return url;
    }
    
}
