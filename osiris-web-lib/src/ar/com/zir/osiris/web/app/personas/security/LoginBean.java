/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.com.zir.osiris.web.app.personas.security;

import ar.com.zir.osiris.api.security.SystemUser;
import ar.com.zir.osiris.ejb.services.SeguridadService;
import java.io.Serializable;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author jmrunge
 */
@Named(value="loginBean")
@RequestScoped
public class LoginBean implements Serializable {
    @Inject
    private SeguridadService seguridadService;
    private String username;
    private String password;

    /** Creates a new instance of LoginBean */
    public LoginBean() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void login() {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = null;
        boolean loggedIn = false;

        if(username != null  && password != null) {
            SystemUser su = seguridadService.logIn(username, password);
            if (su != null) {
                loggedIn = true;
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Bienvenido", su.getNombreCompleto());
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("systemUser", su);
                Map<String, Object> appMap = FacesContext.getCurrentInstance().getExternalContext().getApplicationMap();
                HttpSession session1 = (HttpSession) appMap.get(su.getNombre());
                HttpSession session2 = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
                if ((session1 != null) && !(session1.equals(session2))) {
                    try {
                        session1.invalidate();
                    } catch (IllegalStateException e) {}
                }
                appMap.put(su.getNombre(), session2);
            } else {
                loggedIn = false;
                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error", "Credenciales inválidas");
            }
        } else {
            loggedIn = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error", "Credenciales inválidas");
        }

        FacesContext.getCurrentInstance().addMessage(null, msg);
        context.addCallbackParam("loggedIn", loggedIn);
    }

}
