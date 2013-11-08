/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.web.app;

import java.net.URL;
import javax.faces.view.facelets.ResourceResolver;

/**
 *
 * @author jmrunge
 */
public class FaceletsResourceResolver extends ResourceResolver {

    private ResourceResolver parent;
    private String basePath;

    public FaceletsResourceResolver(ResourceResolver parent) {
        this.parent = parent;
        this.basePath = "/META-INF";
    }

    @Override
    public URL resolveUrl(String path) {
        URL url = parent.resolveUrl(path); 
        
        if (url == null) {
            url = getClass().getResource(basePath + path);
        }

        return url;
    }

}
