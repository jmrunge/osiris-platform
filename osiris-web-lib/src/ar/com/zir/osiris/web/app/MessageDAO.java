/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.web.app;

import ar.com.zir.osiris.api.security.SystemUser;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author jmrunge
 */
public class MessageDAO {
    private Date sentDate;
    private String message;
    private SystemUser sender;

    public MessageDAO() {
    }

    public MessageDAO(String message, SystemUser sender) {
        this(message, sender, Calendar.getInstance().getTime());
    }

    public MessageDAO(String message, SystemUser sender, Date sentDate) {
        this.sentDate = sentDate;
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SystemUser getSender() {
        return sender;
    }

    public void setSender(SystemUser sender) {
        this.sender = sender;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }
    
}
