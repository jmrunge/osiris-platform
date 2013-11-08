/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.api.personas;

import ar.com.zir.osiris.api.security.SystemUserPersonaFisica;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jmrunge
 */
@Entity
@Table(name = "personafisica")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PersonaFisica.findAll", query = "SELECT p FROM PersonaFisica p"),
    @NamedQuery(name = "PersonaFisica.findById", query = "SELECT p FROM PersonaFisica p WHERE p.id = :id"),
    @NamedQuery(name = "PersonaFisica.findByDni", query = "SELECT p FROM PersonaFisica p WHERE p.dni = :dni"),
    @NamedQuery(name = "PersonaFisica.findBySexo", query = "SELECT p FROM PersonaFisica p WHERE p.sexo = :sexo"),
    @NamedQuery(name = "PersonaFisica.findAllByNombre", query = "SELECT p FROM PersonaFisica p WHERE p.nombre like :nombre"),
    @NamedQuery(name = "PersonaFisica.findByFechaNacimiento", query = "SELECT p FROM PersonaFisica p WHERE p.fechaNacimiento = :fechaNacimiento")})
public class PersonaFisica extends Persona {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "dni")
    private int dni;
    @Basic(optional = false)
    @Column(name = "sexo")
    private Sexo sexo;
    @Basic(optional = false)
    @Column(name = "fechaNacimiento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaNacimiento;
    @OneToOne(mappedBy = "persona", optional = true)
    private SystemUserPersonaFisica systemUser;

    public PersonaFisica() {
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public SystemUserPersonaFisica getSystemUser() {
        return systemUser;
    }

    public void setSystemUser(SystemUserPersonaFisica systemUser) {
        this.systemUser = systemUser;
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
        if (!(object instanceof PersonaFisica)) {
            return false;
        }
        PersonaFisica other = (PersonaFisica) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

}
