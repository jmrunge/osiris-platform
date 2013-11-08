/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.zir.osiris.ejb.persistence;

import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.sessions.factories.DescriptorCustomizer;

/**
 *
 * @author jmrunge
 */
public class RelationalDescriptorCustomizer implements DescriptorCustomizer {

    @Override
    public void customize(ClassDescriptor cd) throws Exception {
        cd.setHasMultipleTableConstraintDependecy(true);
    }
    
}
