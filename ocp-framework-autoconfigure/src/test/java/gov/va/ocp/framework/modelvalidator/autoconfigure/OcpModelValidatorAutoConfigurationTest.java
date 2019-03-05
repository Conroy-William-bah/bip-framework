/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ocp.framework.modelvalidator.autoconfigure;

import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import gov.va.ocp.framework.modelvalidator.autoconfigure.OcpModelValidatorAutoConfiguration;

/**
 *
 * @author rthota
 */
public class OcpModelValidatorAutoConfigurationTest {
    
    private AnnotationConfigWebApplicationContext context;

    @After
    public void close() {
        if (this.context != null) {
            this.context.close();
        }
    }

   @Test
    public void testWebConfiguration() throws Exception {
        context = new AnnotationConfigWebApplicationContext();
        context.register(OcpModelValidatorAutoConfiguration.class);
        context.refresh();
        assertNotNull(context);
        assertNotNull(this.context.getBean(OcpModelValidatorAutoConfiguration.class));

    }
    
}
