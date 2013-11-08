/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.ejb.interceptors;

import static java.lang.annotation.ElementType.METHOD;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 *
 * @author jmrunge
 */
@Inherited
@Retention(RUNTIME)
@Target({METHOD})
public @interface SecuredMethod {
    public AllowedOption[] value();
}
