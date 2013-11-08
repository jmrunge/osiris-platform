/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.web.app;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;

/**
 *
 * @author jmrunge
 */
public abstract class HolderBean {

    protected abstract String getGetterMethod();

    protected void setHolderValue(Object object) {
        Object holder = getObjectHolder();
        Object[] params = new Object[1];
        params[0] = object;
        if (holder != null) {
            for (Method m : holder.getClass().getMethods()) {
                if (m.getName().equals(getSetterMethod())) {
                    try {
                        m.invoke(holder, params);
                    } catch (Exception ex) {
                        Logger.getLogger(HolderBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
            }
        }
    }

    protected abstract String getSetterMethod();

    protected Object getObjectHolder() {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(getHolderName());
    }

    protected abstract String getHolderName();

    
}
