/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.web.app;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author jmrunge
 */
public class EntityConverter implements Converter {
    private Collection col;
    
    public EntityConverter(Collection col) {
        this.col = col;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && value.trim().length() != 0) {
            Integer key = Integer.parseInt(value);
            for (Object obj : col) {
                Integer aux = getId(obj);
                if (aux.equals(key))
                    return obj;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null || value.toString().trim().length() == 0) return "0";
        return getId(value).toString();
    }
    
    private Integer getId(Object value) {
        if (value != null) {
            for (Method m : value.getClass().getMethods()) {
                if (m.getName().equals("getId")) {
                    Integer key;
                    try {
                        key = (Integer) m.invoke(value, null);
                        return key;
                    } catch (Exception ex) {
                        Logger.getLogger(EntityConverter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return null;
    }
    
    
}
