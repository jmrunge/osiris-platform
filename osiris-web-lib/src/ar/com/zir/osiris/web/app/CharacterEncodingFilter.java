/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.com.zir.osiris.web.app;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 *
 * @author jmrunge
 */
public class CharacterEncodingFilter implements Filter {

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
	throws IOException, ServletException {

	request.setCharacterEncoding("UTF-8");
//        response.setCharacterEncoding("UTF-8");

        chain.doFilter(request, response);
    }
    
    /**
     * Destroy method for this filter 
     */
    @Override
    public void destroy() { 
    }

    /**
     * Init method for this filter 
     */
    @Override
    public void init(FilterConfig filterConfig) { 
    }

}
