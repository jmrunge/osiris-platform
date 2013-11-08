/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ar.com.zir.osiris.web.app.personas.security;

import ar.com.zir.osiris.api.security.SecurityObject;
import ar.com.zir.osiris.api.security.SystemOption;
import ar.com.zir.osiris.api.security.SystemOptionSecurityValue;

/**
 *
 * @author jmrunge
 */
public class SystemOptionDAO {
    private SystemOptionSecurityValue option;
    private String inherited;
    private String name;

    public String getInherited() {
        return inherited;
    }

    public void setInherited(String inherited) {
        this.inherited = inherited;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public Boolean getValue() {
        return option.getCanDoIt();
    }

    public void setValue(Boolean value) {
        option.setCanDoIt(value.booleanValue());
    }

    public SystemOptionSecurityValue getOption() {
        return option;
    }

    public void setOption(SystemOptionSecurityValue option) {
        this.option = option;
    }

    @Override
    public String toString() {
        return getName();
    }
    
    public boolean isReadOnly() {
        return ((inherited != null) && (inherited.trim().length() > 0)) || (option == null);
    }
    
    public void valueChanged() {
        if (option.getSystemOptionSecurityOption().getSystemOptionSecurity().getOptionCode().trim().equals("read")) {
            if (!option.getCanDoIt()) {
                SecurityObject so = option.getSecurityObject();
                SystemOption parent = option.getSystemOptionSecurityOption().getSystemOption();
                for (SystemOptionSecurityValue sosv : so.getSystemOptionSecurityValues(parent.getCodigo())) {
                    if (!sosv.getSystemOptionSecurityOption().getSystemOptionSecurity().getOptionCode().trim().equals("read")) {
                        sosv.setCanDoIt(Boolean.FALSE);
                    }
                }
            }
        } else {
            if (option.getCanDoIt()) {
                SecurityObject so = option.getSecurityObject();
                SystemOption parent = option.getSystemOptionSecurityOption().getSystemOption();
                for (SystemOptionSecurityValue sosv : so.getSystemOptionSecurityValues(parent.getCodigo())) {
                    if (sosv.getSystemOptionSecurityOption().getSystemOptionSecurity().getOptionCode().trim().equals("read")) {
                        sosv.setCanDoIt(Boolean.TRUE);
                    }
                }
            }
        }
    }

}
