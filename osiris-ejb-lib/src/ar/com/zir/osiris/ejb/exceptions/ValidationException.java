/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.com.zir.osiris.ejb.exceptions;

/**
 *
 * @author jmrunge
 */
public class ValidationException extends Exception {

    public ValidationException(String msg) {
        super(msg);
    }

    public ValidationException(String msg, Throwable t) {
        super(msg, t);
    }
}
