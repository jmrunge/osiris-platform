/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.web.app.personas.security;

/**
 *
 * @author jmrunge
 */
public class DuplicatedSessionException extends Exception {
    public DuplicatedSessionException(String msg) {
        super(msg);
    }

    public DuplicatedSessionException(String msg, Throwable t) {
        super(msg, t);
    }
}
