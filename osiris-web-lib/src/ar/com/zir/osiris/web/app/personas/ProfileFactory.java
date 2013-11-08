/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.web.app.personas;

import ar.com.zir.osiris.api.personas.profiles.ProfileInterface;
import ar.com.zir.osiris.ejb.services.OsirisConfig;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jmrunge
 */
public class ProfileFactory implements Serializable {
    private OsirisConfig config;
    private Map<String, ProfileInterface> profiles;
    
    public ProfileFactory(OsirisConfig config) {
        this.config = config;
    }
    
    public Map<String, ProfileInterface> getProfiles() {
        if (profiles == null) {
            profiles = new HashMap<>();
            List<String> perfilesDisponibles = config.getPersonaProfiles();
            for (String s : perfilesDisponibles) {
                if (s.toLowerCase().trim().equals("none")) {
                    profiles.put("none", null);
                    break;
                } else {//if (s.equals("Empleado") || s.equals("Cliente") || s.equals("Proveedor")) {
                    String propName = "persona.profiles." + s.toLowerCase() + ".bean";
                    String className = config.getProperty(propName);
                    ProfileInterface obj;
                    try {
                        obj = (ProfileInterface) Class.forName(className).newInstance();
                        profiles.put(s, obj);
                    } catch (Exception ex) {
                        Logger.getLogger(ProfileFactory.class.getName()).log(Level.SEVERE, "Error instanciando clase " + className, ex);
                    }
                }
            }
        }
        return profiles;
    }
}
