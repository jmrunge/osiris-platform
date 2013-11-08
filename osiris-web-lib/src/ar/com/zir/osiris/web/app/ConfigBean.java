/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.web.app;

import ar.com.zir.osiris.ejb.services.OsirisConfig;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author jmrunge
 */
@Named(value = "configBean")
@ApplicationScoped
public class ConfigBean {
    @Inject
    private OsirisConfig config;

    /**
     * Creates a new instance of ConfigBean
     */
    public ConfigBean() {
    }

    public String getTitle() {
        return config.getAppTitle();
    }
    
    public void reload() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(getHomeURL());
        } catch (IOException ex) {
            Logger.getLogger(AppBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getHomeURL() {
        return config.getHomeURL();
    }
    
}
