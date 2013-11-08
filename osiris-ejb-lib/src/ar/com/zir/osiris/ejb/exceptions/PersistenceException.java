/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.com.zir.osiris.ejb.exceptions;

/**
 *
 * @author jmrunge
 */
public class PersistenceException extends Exception {

    public PersistenceException(String msg) {
        super(msg);
    }

    public PersistenceException(String msg, Throwable t) {
        super(msg, t);
    }
}
