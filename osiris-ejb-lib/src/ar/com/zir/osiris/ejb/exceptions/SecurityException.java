/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.ejb.exceptions;

/**
 *
 * @author jmrunge
 */
public class SecurityException extends Exception {

    public SecurityException(String msg) {
        super(msg);
    }

    public SecurityException(String msg, Throwable t) {
        super(msg, t);
    }
    
}
