/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.com.zir.osiris.api.security;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jmrunge
 */
@Entity
@Table(name = "SystemOption")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SystemOption.findAll", query = "SELECT s FROM SystemOption s"),
    @NamedQuery(name = "SystemOption.findAllParents", query = "SELECT s FROM SystemOption s WHERE s.parent is null ORDER BY s.orden"),
    @NamedQuery(name = "SystemOption.findById", query = "SELECT s FROM SystemOption s WHERE s.id = :id"),
    @NamedQuery(name = "SystemOption.findByCodigo", query = "SELECT s FROM SystemOption s WHERE s.codigo = :codigo"),
    @NamedQuery(name = "SystemOption.findByTitulo", query = "SELECT s FROM SystemOption s WHERE s.titulo = :titulo"),
    @NamedQuery(name = "SystemOption.findByDescripcion", query = "SELECT s FROM SystemOption s WHERE s.descripcion = :descripcion"),
    @NamedQuery(name = "SystemOption.findByParent", query = "SELECT s FROM SystemOption s WHERE s.parent = :parent"),
    @NamedQuery(name = "SystemOption.findByUrl", query = "SELECT s FROM SystemOption s WHERE s.url = :url")})
public class SystemOption implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(generator="SystemOptionSeq")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @Column(name = "titulo")
    private String titulo;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "url")
    private String url;
    @Basic(optional = false)
    @Column(name = "orden")
    private int orden;
    @OneToMany(mappedBy = "parent")
    private Collection<SystemOption> children;
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    @ManyToOne
    private SystemOption parent;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "systemOption")
    private Collection<SystemOptionSecurityOption> systemOptionSecurityOptionCollection;

    public SystemOption() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public Collection<SystemOption> getChildren() {
        return children;
    }

    public void setChildren(Collection<SystemOption> children) {
        this.children = children;
    }

    public SystemOption getParent() {
        return parent;
    }

    public void setParent(SystemOption parent) {
        this.parent = parent;
    }

    public Collection<SystemOptionSecurityOption> getSystemOptionSecurityOptionCollection() {
        return systemOptionSecurityOptionCollection;
    }

    public void setSystemOptionSecurityOptionCollection(Collection<SystemOptionSecurityOption> systemOptionSecurityOptionCollection) {
        this.systemOptionSecurityOptionCollection = systemOptionSecurityOptionCollection;
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
        if (!(object instanceof SystemOption)) {
            return false;
        }
        SystemOption other = (SystemOption) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getTitulo();
    }

}
