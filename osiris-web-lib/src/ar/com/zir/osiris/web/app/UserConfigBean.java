/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.web.app;

import ar.com.zir.osiris.api.security.SystemUser;
import ar.com.zir.osiris.ejb.exceptions.PersistenceException;
import ar.com.zir.osiris.ejb.exceptions.ValidationException;
import ar.com.zir.osiris.ejb.services.SeguridadService;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author jmrunge
 */
@Named(value = "userConfigBean")
@SessionScoped
public class UserConfigBean implements Serializable {
    @Inject
    private SeguridadService seguridadService;
    private Map<String, String> themes;  
    private String theme; 
    private Integer sessionTimeout;
   
    public Map<String, String> getThemes() {  
        return themes;  
    }  
  
    public String getTheme() {  
        if (getSystemUser() != null)
            theme = getSystemUser().getTheme();
        if (theme == null)
            theme = "aristo";
        return theme;  
    }  
  
    public void setTheme(String theme) {  
        this.theme = theme;  
    }
    
    public void saveTheme() throws ValidationException, PersistenceException {
        SystemUser su = getSystemUser();
        su.setTheme(theme);
        su = seguridadService.updateSystemUser(su, su);
        setSystemUser(su);
    }  

    public boolean getCanChangeTheme() {
        SystemUser su = getSystemUser();
        if (su == null)
            return false;
        else
            return su.getCanChangeTheme();
    }

    public Integer getSessionTimeout() {
        if (getSystemUser() != null)
            sessionTimeout = getSystemUser().getSessionTimeout();
        if (sessionTimeout == null)
            sessionTimeout = 5;
        return sessionTimeout;  
    }

    public void setSessionTimeout(Integer sessionTimeout) throws ValidationException, PersistenceException {
        this.sessionTimeout = sessionTimeout;
        SystemUser su = getSystemUser();
        su.setSessionTimeout(sessionTimeout);
        su = seguridadService.updateSystemUser(su, su);
        setSystemUser(su);
    }
  
    public boolean getCanChangeSessionTimeout() {
        SystemUser su = getSystemUser();
        if (su == null)
            return false;
        else
            return su.getCanChangeSessionTimeout();
    }

    @PostConstruct  
    public void init() {  
        themes = new TreeMap<>();  
        themes.put("Afterdark", "afterdark");  
        themes.put("Afternoon", "afternoon");  
        themes.put("Afterwork", "afterwork");  
        themes.put("Aristo", "aristo");  
        themes.put("Black-Tie", "black-tie");  
        themes.put("Blitzer", "blitzer");  
        themes.put("Bluesky", "bluesky");  
        themes.put("Casablanca", "casablanca");  
        themes.put("Cruze", "cruze");  
        themes.put("Cupertino", "cupertino");  
        themes.put("Dark-Hive", "dark-hive");  
        themes.put("Dot-Luv", "dot-luv");  
        themes.put("Eggplant", "eggplant");  
        themes.put("Excite-Bike", "excite-bike");  
        themes.put("Flick", "flick");  
        themes.put("Glass-X", "glass-x");  
        themes.put("Home", "home");  
        themes.put("Hot-Sneaks", "hot-sneaks");  
        themes.put("Humanity", "humanity");  
        themes.put("Le-Frog", "le-frog");  
        themes.put("Midnight", "midnight");  
        themes.put("Mint-Choc", "mint-choc");  
        themes.put("Overcast", "overcast");  
        themes.put("Pepper-Grinder", "pepper-grinder");  
        themes.put("Redmond", "redmond");  
        themes.put("Rocket", "rocket");  
        themes.put("Sam", "sam");  
        themes.put("Smoothness", "smoothness");  
        themes.put("South-Street", "south-street");  
        themes.put("Start", "start");  
        themes.put("Sunny", "sunny");  
        themes.put("Swanky-Purse", "swanky-purse");  
        themes.put("Trontastic", "trontastic");  
        themes.put("UI-Darkness", "ui-darkness");  
        themes.put("UI-Lightness", "ui-lightness");  
        themes.put("Vader", "vader");  
    }  
      
    private SystemUser getSystemUser() {
        return (SystemUser) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("systemUser");
    }
    
    private void setSystemUser(SystemUser su) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("systemUser", su);
    }

}  
