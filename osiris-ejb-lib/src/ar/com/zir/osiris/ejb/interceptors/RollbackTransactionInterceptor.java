/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.com.zir.osiris.ejb.interceptors;

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
@RollbackTransaction
public class RollbackTransactionInterceptor {
    @Resource
    private SessionContext sessionContext;

    @AroundInvoke
    public Object executeInContext(InvocationContext ctx) throws Exception {
//        System.out.println("Paso por ROLLBACK " + ctx.getMethod().getDeclaringClass().getSimpleName() + " - " + ctx.getMethod().getName());
        try {
            return ctx.proceed();
        } catch (Exception e) {
            sessionContext.setRollbackOnly();
            throw e;
        }
    }
}
