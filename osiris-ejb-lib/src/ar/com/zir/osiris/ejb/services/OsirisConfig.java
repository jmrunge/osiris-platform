/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.ejb.services;

import ar.com.zir.osiris.api.Config;
import java.io.Serializable;
import java.util.*;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jmrunge
 */
@Singleton
@LocalBean
public class OsirisConfig implements Serializable {
    @PersistenceContext(unitName="osiris-ejbPU")
    private EntityManager em;
    private Map<String, String> config = null;
    
    public String getHomeURL() {
        if (config == null)
            initConfig();
        return config.get("app.home.url");
    }
    
    public String getAppTitle() {
        if (config == null)
            initConfig();
        return config.get("app.title");
    }
    
    private void initConfig() {
        Collection<Config> conf = em.createNamedQuery("Config.findAll").getResultList();
        config = new HashMap<>();
        for (Config c : conf) {
            config.put(c.getClave(), c.getValor());
            System.out.println("Config value: [" + c.getClave() + "] " + c.getValor());
        }
    }
    
    public List<String> getPersonaProfiles() {
        if (config == null)
            initConfig();
        return new LinkedList<>(Arrays.asList(config.get("persona.profiles").split("\\|")));
    }
    
    public String getProperty(String propName) {
        if (config == null)
            initConfig();
        return config.get(propName);
    }
}
