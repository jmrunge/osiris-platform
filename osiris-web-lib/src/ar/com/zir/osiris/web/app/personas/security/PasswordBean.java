/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.com.zir.osiris.web.app.personas.security;

import ar.com.zir.osiris.api.security.SystemUser;
import ar.com.zir.osiris.ejb.services.SeguridadService;
import ar.com.zir.utils.CryptUtils;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

/**
 *
 * @author jmrunge
 */
@Named(value="passwordBean")
@RequestScoped
public class PasswordBean {
    @Inject
    private SeguridadService seguridadService;
    private String oldPassword;
    private String newPassword1;
    private String newPassword2;

    /** Creates a new instance of PasswordBean */
    public PasswordBean() {
    }

    public String getNewPassword1() {
        return newPassword1;
    }

    public void setNewPassword1(String newPassword1) {
        this.newPassword1 = newPassword1;
    }

    public String getNewPassword2() {
        return newPassword2;
    }

    public void setNewPassword2(String newPassword2) {
        this.newPassword2 = newPassword2;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public void changePassword() {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = null;
        boolean passwordChanged = false;
        String error = "";

        if (oldPassword == null || oldPassword.trim().length() == 0)
            error = "Debe ingresar la contraseña anterior";
        else if (newPassword1 == null  || newPassword1.trim().length() == 0)
            error = "Debe ingresar la nueva contraseña";
        else if (newPassword2 == null || newPassword2.trim().length() == 0)
            error = "Debe repetir la nueva contraseña";
        else if (!newPassword1.equals(newPassword2))
            error = "La nueva contraseña y su repetición no coinciden";

        try {
            oldPassword = CryptUtils.encrypt(oldPassword);
        } catch (Exception ex) {
            Logger.getLogger(PasswordBean.class.getName()).log(Level.SEVERE, "Error encriptando la contraseña", ex);
            error = "No se puedo encriptar la contraseña";
        }

        if (error.length() == 0) {
            SystemUser su = getSystemUser();

            if (!oldPassword.equals(su.getCurrentPassword())) {
                error = "Contraseña actual errónea";
            } else {
                su.setPassword(newPassword1);
                try {
                    su = seguridadService.updateSystemUser(su, su);
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("systemUser", su);
                } catch (Exception e) {
                    Logger.getLogger(PasswordBean.class.getName()).log(Level.SEVERE, "Error cambiando la contraseña", e);
                    error = "No se puedo cambiar la contraseña";
                }
            }
        }


        if(error.length() == 0) {
            passwordChanged = true;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Operación exitosa", "La contraseña se ha modificado");
        } else {
            passwordChanged = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Error", error);
        }

        FacesContext.getCurrentInstance().addMessage(null, msg);
        context.addCallbackParam("passwordChanged", passwordChanged);
    }

    private SystemUser getSystemUser() {
        return (SystemUser) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("systemUser");
    }

}
