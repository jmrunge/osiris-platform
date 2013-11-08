/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.com.zir.osiris.web.app;

import ar.com.zir.osiris.api.security.SystemUser;
import ar.com.zir.osiris.web.app.personas.security.DuplicatedSessionException;
import ar.com.zir.utils.StringUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import org.primefaces.event.NodeSelectEvent;

/**
 *
 * @author jmrunge
 */
public abstract class AbmBean extends HolderBean {
    private Object object;

    protected abstract Object getNewObject();
    protected abstract boolean isNew(Object object);
    protected abstract Object createObject(Object object) throws Exception;
    protected abstract Object updateObject(Object object) throws Exception;
    protected abstract void deleteObject(Object object) throws Exception;

    public Object getObject() {
//        if (object == null)
//            System.out.println("Devuelve objeto null de bean " + this.getClass().getName());
//        else
//            System.out.println("Devuelve objeto " + object.getClass().getName());
        return object;
    }

    public String newObject() throws DuplicatedSessionException {
        setObject(getNewObject());
//        System.out.println("Solicito nuevo objeto " + object.getClass().getName());
        return null;
    }

    public void setObject(Object obj) {
        if (obj != null && !isNew(obj))
            obj = refreshObject(obj);
        this.object = obj;
    }

    protected void setObjectWithoutRefresh(Object obj) {
        this.object = obj;
    }

    public void selectObject(NodeSelectEvent evt) throws DuplicatedSessionException {
        if (evt.getTreeNode() == null)
            setObject(null);
        else {
            setObject(evt.getTreeNode().getData());
        }
    }

    public void cleanObject() {
        setObject(null);
    }

    public void updateObject() {
        if (object != null) {
            try {
                if (isNew(object))
                    object = createObject(object);
                else
                    object = updateObject(object);
                showMessage("El objeto " + object.toString() + " ha sido actualizado", true);
                if (getObjectHolder() != null)
                    setHolderValue(object);
                setObject(null);
            } catch (Exception e) {
                showMessage(e.getMessage(), false);
            }
        }
    }

    public void deleteObject() {
        try {
            deleteObject(object);
            showMessage("El objeto " + object.toString() + " ha sido eliminado", true);
            setObject(null);
        } catch (Exception e) {
            showMessage(e.getMessage(), false);
        }
    }

    protected void showMessage(String message, boolean success) {
        FacesMessage.Severity sev = null;
        String title = null;
        if (success) {
            sev = FacesMessage.SEVERITY_INFO;
            title = "Operación exitosa";
        } else {
            sev = FacesMessage.SEVERITY_FATAL;
            title = "Error";
        }
        FacesMessage msg = new FacesMessage(sev, title, message);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    protected SystemUser getSystemUser() throws DuplicatedSessionException {
        SystemUser su = (SystemUser) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("systemUser");
        if (su == null)
            throw new DuplicatedSessionException("La sesión actual de este usuario ha sido cerrada por duplicidad");
        return su;
    }

    public String refreshObject() {
        Object o = refreshObject(getObject());
        setObject(o);
        return null;
    }

    protected abstract Object refreshObject(Object obj);

    public Collection collection(String name) throws SecurityException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (getObject() == null) return null;
        
        String method = "get" + StringUtils.setFirstLetterUpperCase(name);
        Method m = getObject().getClass().getMethod(method, (Class<?>[]) null);
        return (Collection) m.invoke(getObject(), (Object[]) null);
    }

}
