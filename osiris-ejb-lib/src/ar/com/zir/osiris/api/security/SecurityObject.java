/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.com.zir.osiris.api.security;

import java.io.Serializable;
import java.util.*;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author jmrunge
 */
@Entity
@Customizer(ar.com.zir.osiris.ejb.persistence.RelationalDescriptorCustomizer.class)
@Table(name = "SecurityObject")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SecurityObject.findAll", query = "SELECT s FROM SecurityObject s"),
    @NamedQuery(name = "SecurityObject.findByNombre", query = "SELECT s FROM SecurityObject s WHERE s.nombre = :nombre"),
    @NamedQuery(name = "SecurityObject.findById", query = "SELECT s FROM SecurityObject s WHERE s.id = :id")})
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class SecurityObject implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(generator="SecurityObjectSeq")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "securityObject", orphanRemoval=true)
    private Collection<SystemOptionSecurityValue> systemOptionSecurityValueCollection;

    public SecurityObject() {
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

    public Collection<SystemOptionSecurityValue> getSystemOptionSecurityValueCollection() {
        return systemOptionSecurityValueCollection;
    }

    public void setSystemOptionSecurityValueCollection(Collection<SystemOptionSecurityValue> systemOptionSecurityValueCollection) {
        this.systemOptionSecurityValueCollection = systemOptionSecurityValueCollection;
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
        if (!(object instanceof SecurityObject)) {
            return false;
        }
        SecurityObject other = (SecurityObject) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public Collection<SystemOptionSecurityValue> getSystemOptionSecurityValues(String option) {
        Collection<SystemOptionSecurityValue> options = new ArrayList<>();
        if (systemOptionSecurityValueCollection != null) {
            for (SystemOptionSecurityValue sosv : systemOptionSecurityValueCollection) {
                if (sosv.getSystemOptionSecurityOption().getSystemOption().getCodigo().toLowerCase().trim().equals(option.toLowerCase().trim()))
                    options.add(sosv);
            }
        }
        return options;
    }

    public void addSystemOptionSecurityValue(SystemOptionSecurityValue sosv) {
        if (systemOptionSecurityValueCollection == null)
            systemOptionSecurityValueCollection = new ArrayList<>();
        systemOptionSecurityValueCollection.add(sosv);
    }

    public List<SystemOption> getMainSystemOptions() {
        List<SystemOption> options = new ArrayList<>();
        for (SystemOptionSecurityValue sosv : getSystemOptionSecurityValueCollection()) {
            if (sosv.getCanDoIt()) {
                SystemOption so = getParentOption(sosv);
                if (!options.contains(so))
                    options.add(so);
            }
        }
        sortOptions(options);
        return options;
    }

    private SystemOption getParentOption(SystemOptionSecurityValue sosv) {
        SystemOption parent = sosv.getSystemOptionSecurityOption().getSystemOption();
        while (parent.getParent() != null) {
            parent = parent.getParent();
        }
        return parent;
    }

    public List<SystemOption> getSystemOptionChildren(SystemOption so) {
        List<SystemOption> options = new ArrayList<>();
        if (so.getChildren() != null && !so.getChildren().isEmpty()) {
            for (SystemOption child : so.getChildren()) {
                Collection<SystemOptionSecurityValue> values = getSystemOptionSecurityValues(child.getCodigo());
                for (SystemOptionSecurityValue sosv : values) {
                    if (sosv.getCanDoIt()) {
                        options.add(child);
                        break;
                    }
                }
            }
        }
        sortOptions(options);
        return options;
    }
    
    private void sortOptions(List<SystemOption> options) {
        Collections.sort(options, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                SystemOption s1 = (SystemOption)o1;
                SystemOption s2 = (SystemOption)o2;
                Integer i1 = s1.getOrden();
                Integer i2 = s2.getOrden();
                return i1.compareTo(i2);
            }
        });
    }
}
