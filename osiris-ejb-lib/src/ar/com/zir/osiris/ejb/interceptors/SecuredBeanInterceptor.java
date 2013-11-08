/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.ejb.interceptors;

import ar.com.zir.osiris.api.security.SystemOptionSecurityValue;
import ar.com.zir.osiris.api.security.SystemUser;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 *
 * @author jmrunge
 */
@Interceptor
@SecuredBean
public class SecuredBeanInterceptor {
    @Resource
    private SessionContext sessionContext;

    @AroundInvoke
    public Object executeInContext(InvocationContext ctx) throws Exception {
//        System.out.println("Paso por SECURED " + ctx.getMethod().getDeclaringClass().getSimpleName() + " - " + ctx.getMethod().getName());
        try {
            Method m = ctx.getMethod();
            Annotation a = m.getAnnotation(SecuredMethod.class);
            boolean canProceed = false;
            boolean isAnnotated = false;
            if (a != null) {
                isAnnotated = true;
                for (Annotation a1 : ((SecuredMethod)a).value()) {
                    canProceed = checkOption((AllowedOption)a1, getSystemUser(ctx.getParameters()), m);
                    if (canProceed)
                        break;
                }
            }
            if (!canProceed) {
                a = m.getAnnotation(AllowedOption.class);
                if (a != null) {
                    isAnnotated = true;
                    canProceed = checkOption((AllowedOption)a, getSystemUser(ctx.getParameters()), m);
                }
            }
            if (!isAnnotated || canProceed)
                return ctx.proceed();
            else
                throw new SecurityException("Usuario no habilitado para invocar el método " + m.getName() + " del servicio " + m.getDeclaringClass().getCanonicalName());
        } catch (Exception e) {
            sessionContext.setRollbackOnly();
            throw e;
        }
    }    
    
    private SystemUser getSystemUser(Object[] parameters) {
        SystemUser su = null;
//        for (Object o : parameters) {
//            if (o instanceof SystemUser) {
//                su = (SystemUser) o;
//                break;
//            }
//        }
        Object o = parameters[parameters.length - 1];
        if (o instanceof SystemUser)
            su = (SystemUser)o;
        return su;
    }
    
    private boolean checkOption(AllowedOption option, SystemUser su, Method m) throws SecurityException {
        if (su == null) 
            throw new SecurityException("Un método asegurado debe sí o sí recibir al usuario que lo ejecuta como parámetro [Método: " + m.getName() + " - Servicio: " + m.getDeclaringClass().getCanonicalName() + "]");
        
        boolean canDo = option.option().equals("ALL");
        
        if (!canDo) {
            Collection<SystemOptionSecurityValue> options = su.getSystemOptionSecurityValues(option.option());
            if (!options.isEmpty()) {
                if (option.access().equals("all")) {
                    canDo = true;
                } else {
                    for (SystemOptionSecurityValue sosv : options) {
                        if (sosv.getSystemOptionSecurityOption().getSystemOptionSecurity().getOptionCode().toLowerCase().trim().equals(option.access().toLowerCase().trim())) {
                            if (sosv.getCanDoIt()) {
                                canDo = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return canDo;
    }
}
