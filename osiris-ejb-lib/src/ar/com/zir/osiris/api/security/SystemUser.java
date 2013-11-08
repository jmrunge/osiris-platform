/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.com.zir.osiris.api.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jmrunge
 */
@Entity
@Table(name = "SystemUser")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SystemUser.findAll", query = "SELECT s FROM SystemUser s"),
    @NamedQuery(name = "SystemUser.findAllByNombre", query = "SELECT s FROM SystemUser s WHERE s.nombre like :nombre"),
    @NamedQuery(name = "SystemUser.findByNombre", query = "SELECT s FROM SystemUser s WHERE s.nombre = :nombre"),
    @NamedQuery(name = "SystemUser.findById", query = "SELECT s FROM SystemUser s WHERE s.id = :id")})
public abstract class SystemUser extends SecurityObject {
    private static final long serialVersionUID = 1L;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "systemUser", orphanRemoval=true, fetch=FetchType.EAGER)
    private Collection<SystemUserPassword> systemUserPasswordCollection;
    @Transient
    private String password;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "systemUser", orphanRemoval=true, fetch=FetchType.EAGER)
    private List<SystemRoleUsers> systemRoleUsers;
    @Basic(optional = true)
    @Column(name = "theme")
    private String theme;
    @Basic(optional = false)
    @Column(name = "canChangeTheme")
    private boolean canChangeTheme;
    @Basic(optional = false)
    @Column(name = "canSeeConnectedUsers")
    private boolean canSeeConnectedUsers;
    @Basic(optional = false)
    @Column(name = "canSendMessages")
    private boolean canSendMessages;
    @Basic(optional = false)
    @Column(name = "canDisconnectUser")
    private boolean canDisconnectUser;
    @Basic(optional = false)
    @Column(name = "sessionTimeout")
    private int sessionTimeout;
    @Basic(optional = false)
    @Column(name = "canChangeSessionTimeout")
    private boolean canChangeSessionTimeout;
    
    public SystemUser() {
    }

    public Collection<SystemUserPassword> getSystemUserPasswordCollection() {
        return systemUserPasswordCollection;
    }

    public void setSystemUserPasswordCollection(Collection<SystemUserPassword> systemUserPasswordCollection) {
        this.systemUserPasswordCollection = systemUserPasswordCollection;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<SystemRoleUsers> getSystemRoleUsers() {
        return systemRoleUsers;
    }

    public void setSystemRoleUsers(List<SystemRoleUsers> systemRoleUsers) {
        this.systemRoleUsers = systemRoleUsers;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public boolean getCanChangeTheme() {
        return canChangeTheme;
    }

    public void setCanChangeTheme(boolean canChangeTheme) {
        this.canChangeTheme = canChangeTheme;
    }

    public boolean getCanDisconnectUser() {
        return canDisconnectUser;
    }

    public void setCanDisconnectUser(boolean canDisconnectUser) {
        this.canDisconnectUser = canDisconnectUser;
    }

    public boolean getCanSeeConnectedUsers() {
        return canSeeConnectedUsers;
    }

    public void setCanSeeConnectedUsers(boolean canSeeConnectedUsers) {
        this.canSeeConnectedUsers = canSeeConnectedUsers;
    }

    public boolean getCanSendMessages() {
        return canSendMessages;
    }

    public void setCanSendMessages(boolean canSendMessages) {
        this.canSendMessages = canSendMessages;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public boolean getCanChangeSessionTimeout() {
        return canChangeSessionTimeout;
    }

    public void setCanChangeSessionTimeout(boolean canChangeSessionTimeout) {
        this.canChangeSessionTimeout = canChangeSessionTimeout;
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
        if (!(object instanceof SystemUser)) {
            return false;
        }
        SystemUser other = (SystemUser) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    public String getCurrentPassword() {
        SystemUserPassword sup = getCurrentSystemUserPassword();
        if (sup == null)
            return null;
        else
            return sup.getPassword();
    }

    public SystemUserPassword getCurrentSystemUserPassword() {
        if (systemUserPasswordCollection == null || systemUserPasswordCollection.isEmpty())
            return null;
        for (SystemUserPassword sup : systemUserPasswordCollection) {
            if (sup.getFechaHasta() == null)
                return sup;
        }
        return null;
    }

    public void addSystemUserPassword(SystemUserPassword sup) {
        if (systemUserPasswordCollection == null)
            systemUserPasswordCollection = new ArrayList<>();
        systemUserPasswordCollection.add(sup);
    }

    public abstract String getNombreCompleto();

    public void addSystemRole(SystemRoleUsers sru) {
        if (systemRoleUsers == null)
            systemRoleUsers = new ArrayList<>();
        systemRoleUsers.add(sru);
    }

}
