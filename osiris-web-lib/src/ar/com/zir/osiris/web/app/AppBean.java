/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.com.zir.osiris.web.app;

import ar.com.zir.osiris.api.security.SystemOption;
import ar.com.zir.osiris.api.security.SystemUser;
import ar.com.zir.osiris.ejb.services.OsirisConfig;
import ar.com.zir.osiris.ejb.services.SeguridadService;
import ar.com.zir.utils.DateUtils;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;

/**
 *
 * @author jmrunge
 */
@Named(value="appBean")
@ConversationScoped
public class AppBean implements Serializable {
    private static final long SESSION_TIMEOUT = 5 * 60 * 1000;
    private static final long CONVERSATION_TIMEOUT = 24 * 60 * 60 * 1000;
    private MenuModel menuModel;
    private Map<String, String> tooltips;
    private SystemUser messageRecipient;
    private String message;
    @Inject
    private Conversation conversation;
    @Inject
    private SeguridadService seguridadService;
    @Inject
    private OsirisConfig config;

    /** Creates a new instance of AppBean */
    public AppBean() {
    }
    
    public long getSessionTimeout() {
        SystemUser su = getSystemUser();
        if (su == null)
            return SESSION_TIMEOUT;
        else
            return su.getSessionTimeout() * 60 * 1000;
    }

    public boolean isLoggedIn() {
        startConversation();
//        initTimer();
        if (getSystemUser() == null)
            return false;
        else
            return true;
    }

    private void startConversation() {
//        System.out.println("ENTRA A STARTCONVERSATION " + conversation.getId());
        if (conversation.isTransient()) {
//            System.out.println("INICIA LA CONVERSACION " + conversation.getId());
            conversation.begin();
//            System.out.println("SETEA TIMEOUT " + conversation.getId());
            conversation.setTimeout(CONVERSATION_TIMEOUT);
        }
    }

//    private void initTimer() {
//        long startTime = Calendar.getInstance().getTimeInMillis();
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sigrediSessionStarted", startTime);
//    }

    public String getUserName() {
        SystemUser su = getSystemUser();
        if (su != null)
            return su.getNombreCompleto();
        else
            return null;
    }

    public void logout() {
//        System.out.println("ENTRO AL LOGOUT");
        Map<String, Object> appMap = FacesContext.getCurrentInstance().getExternalContext().getApplicationMap();
        appMap.remove(getSystemUser().getNombre());
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(config.getHomeURL());
        } catch (IOException ex) {
            Logger.getLogger(AppBean.class.getName()).log(Level.SEVERE, null, ex);
        }
//        System.out.println("FINALIZA CONVERSACION " + conversation.getId());
        if (!conversation.isTransient())
            conversation.end();
    }

//    public void checkSessionExpired() {
//        Long startTime = (Long) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sigrediSessionStarted");
//        if (startTime == null) {
//            logout();
//        } else {
//            long currentTime = Calendar.getInstance().getTimeInMillis();
//            if ((startTime.longValue() + SESSION_TIMEOUT) < currentTime)
//                logout();
//        }
//    }

    private SystemUser getSystemUser() {
        return (SystemUser) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("systemUser");
    }

    public MenuModel getMenu() {
        SystemUser su = getSystemUser();
        if (su == null) {
            return new DefaultMenuModel();
        }
        if (menuModel == null) {
            menuModel = new DefaultMenuModel();
            List<SystemOption> menu = su.getMainSystemOptions();
            for (SystemOption so : menu) {
                List<SystemOption> children = su.getSystemOptionChildren(so);
                if (so.getChildren().isEmpty()) {
                    MenuItem mi = new MenuItem();
                    mi.setValue(so.getTitulo());
                    mi.setUrl(so.getUrl());
                    mi.setId(so.getCodigo());
                    menuModel.addMenuItem(mi);
                } else {
                    Submenu sm = new Submenu();
                    sm.setLabel(so.getTitulo());
                    sm.setId(so.getCodigo());
                    for (SystemOption so2 : children) {
                        MenuItem mi = new MenuItem();
                        mi.setValue(so2.getTitulo());
                        mi.setUrl(so2.getUrl());
                        mi.setId(so2.getCodigo());
                        sm.getChildren().add(mi);
                    }
                    menuModel.addSubmenu(sm);
                }
            }
        }
        return menuModel;
    }
    
    private void loadSystemOptions() {
        tooltips = new HashMap<>();
        for (SystemOption so : seguridadService.getSystemOptions()) {
            tooltips.put(so.getCodigo(), so.getDescripcion());
        }
    }
    
    public Map<String, String> getMenuToolTips() {
        if (tooltips == null)
            loadSystemOptions();
        return tooltips;
    }
    
    public List<SystemUser> getLoggedUsers() {
        List<SystemUser> sus = new ArrayList<>();
        if (isLoggedIn()) {
            Map<String, Object> appMap = FacesContext.getCurrentInstance().getExternalContext().getApplicationMap();
            for (String key : appMap.keySet()) {
                SystemUser su = seguridadService.getSystemUserByNombre(key, getSystemUser());
                if (su != null)
                    sus.add(su);
            }
        }
        return sus;
    }
    
    public void disconnectUser(SystemUser su) {
        if (su.equals(getSystemUser())) {
            showMessage(FacesMessage.SEVERITY_WARN, "No puede desconectarse a si mismo");
            return;
        }
        Map<String, Object> appMap = FacesContext.getCurrentInstance().getExternalContext().getApplicationMap();
        HttpSession session = (HttpSession) appMap.get(su.getNombre());
        session.setAttribute("disconnect", Boolean.TRUE);
        showMessage(FacesMessage.SEVERITY_INFO, "Se ha enviado a desconectar al usuario [" + su.getNombre() + "]");
//        System.out.println("Se envio mensaje a sesión " + session.toString());
    }
    
    public void checkMessages() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        Boolean disconnectNOW = (Boolean) session.getAttribute("disconnectNOW");
        if (disconnectNOW != null) {
            if (disconnectNOW.booleanValue()) {
                logout();
                return;
            }
        }
        Boolean disconnect = (Boolean) session.getAttribute("disconnect");
        if (disconnect != null) {
            if (disconnect.booleanValue()) {
//                System.out.println("Recibió mensaje en sesion " + session.toString());
                showMessage(FacesMessage.SEVERITY_FATAL, "Usted ha sido desconectado por el administrador");
                session.removeAttribute("disconnect");
                session.setAttribute("disconnectNOW", Boolean.TRUE);
                updateMsgGrowl();
            }
        }
        MessageDAO msg = (MessageDAO) session.getAttribute("message");
        if (msg != null) {
            showMessage(FacesMessage.SEVERITY_INFO, "[" + DateUtils.format(msg.getSentDate(), "dd/MM/yyyy HH:mm") + "] " 
                    + msg.getMessage(), "Mensaje de " + msg.getSender().getNombreCompleto());
            session.removeAttribute("message");
            updateMsgGrowl();
        }
    }

    private void showMessage(FacesMessage.Severity severity, String message) {
        showMessage(severity, message, "Mensaje del Sistema");
    }
    
    private void showMessage(FacesMessage.Severity severity, String message, String title) {
        FacesMessage msg = new FacesMessage(severity, title, message);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public void setMessageRecipient(SystemUser su) {
        messageRecipient = su;
        setMessage(null);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public void sendMessage() {
        if (messageRecipient.equals(getSystemUser())) {
            showMessage(FacesMessage.SEVERITY_WARN, "No puede enviarse un mensaje a si mismo");
            return;
        }
        if (message == null || message.trim().length() == 0) {
            showMessage(FacesMessage.SEVERITY_ERROR, "No puede enviar un mensaje vacío");
            return;
        }
        Map<String, Object> appMap = FacesContext.getCurrentInstance().getExternalContext().getApplicationMap();
        HttpSession session = (HttpSession) appMap.get(messageRecipient.getNombre());
        session.setAttribute("message", new MessageDAO(message, getSystemUser()));
        showMessage(FacesMessage.SEVERITY_INFO, "Se ha enviado el mensaje al usuario [" + messageRecipient.getNombre() + "]");
    }
    
    private void updateMsgGrowl() {
        RequestContext context = RequestContext.getCurrentInstance();  
        context.update("msgGrowl");
    }
    
    public boolean getCanChangeConfig() {
        SystemUser su = getSystemUser();
        if (su == null)
            return false;
        else
            return su.getCanChangeTheme() || su.getCanChangeSessionTimeout();
    }

    public boolean getCanDisconnectUser() {
        SystemUser su = getSystemUser();
        if (su == null)
            return false;
        else
            return su.getCanDisconnectUser();
    }

    public boolean getCanSeeConnectedUsers() {
        SystemUser su = getSystemUser();
        if (su == null)
            return false;
        else
            return su.getCanSeeConnectedUsers();
    }

    public boolean getCanSendMessages() {
        SystemUser su = getSystemUser();
        if (su == null)
            return false;
        else
            return su.getCanSendMessages();
    }

    public String getPersonaNodeType() {return NodeTypes.PERSONA_NODE_TYPE;}
    public String getSystemUserNodeType() {return NodeTypes.SYSTEM_USER_NODE_TYPE;}
    public String getSystemRoleNodeType() {return NodeTypes.SYSTEM_ROLE_NODE_TYPE;}
    public String getSystemGroupNodeType() {return NodeTypes.SYSTEM_GROUP_NODE_TYPE;}
    public String getSystemOptionNodeType() {return NodeTypes.SYSTEM_OPTION_NODE_TYPE;}
    public String getPaisNodeType() {return NodeTypes.PAIS_NODE_TYPE;}
    public String getProvinciaNodeType() {return NodeTypes.PROVINCIA_NODE_TYPE;}
    public String getLocalidadNodeType() {return NodeTypes.LOCALIDAD_NODE_TYPE;}
    public String getCuentaNodeType() {return NodeTypes.CUENTA_NODE_TYPE;}
    public String getCatastroNodeType() {return NodeTypes.CATASTRO_NODE_TYPE;}
    public String getCalleNodeType() {return NodeTypes.CALLE_NODE_TYPE;}
    public String getEmpleadoNodeType() {return NodeTypes.EMPLEADO_NODE_TYPE;}


}
