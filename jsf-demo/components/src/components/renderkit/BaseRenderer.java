/*
 * $Id: BaseRenderer.java,v 1.3 2003/02/21 23:44:54 ofung Exp $
 */

/*
 * Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *    
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *  
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

package components.renderkit;


import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.AttributeDescriptor;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import java.util.ResourceBundle;
import java.util.MissingResourceException;



import java.util.Iterator;

/**
 * <p>Convenient base class for <code>Renderer</code> implementations.</p>
 */

public abstract class BaseRenderer extends Renderer {

    /**
     * <p>String identifer for <em>bundle attribute.</em>.</p>
     */
    public static final String BUNDLE_ATTR = "com.sun.faces.bundle";

    public boolean supportsComponentType(UIComponent component) {
        if ( component == null ) {
            throw new NullPointerException();
        }     
        return supportsComponentType(component.getComponentType());
    }

    public Iterator getAttributeNames(String componentType) {
	return null;
    }

    public Iterator getAttributeNames(UIComponent component) {
	return null;
    }

    public AttributeDescriptor getAttributeDescriptor(
        UIComponent component, String name) {
	return null;
    }

    public AttributeDescriptor getAttributeDescriptor(
        String componentType, String name) {
	return null;
    }

    
    /**
     * <p>Return the client-side id for the argument component.</p>
     *
     * <p>The purpose of this method is to give Renderers a chance to
     * define, in a rendering specific way, the client side id for this
     * component.  The client side id should be derived from the
     * component id, if present.  </p>
     *
     * <p>Look up this component's "clientId" attribute.  If non-null,
     * return it.  Get the component id for the argument
     * <code>UIComponent</code>.  If null, generate one using the closest
     * naming container that is an ancestor of this UIComponent, then set
     * the generated id as the componentId of this UIComponent.  Prepend
     * to the component id the component ids of each naming container up
     * to, but not including, the root, separated by the
     * UIComponent.SEPARATOR_CHAR.  In all cases, save the result as the
     * value of the "clientId" attribute.</p>
     *
     * <p>This method must not return null.</p>
     */ 
    public String getClientId(FacesContext context, UIComponent component) {

        // Has a client identifier been generated for this component already?
	String result = null;
	if (null != (result = (String) component.getAttribute("clientId"))) {
	    return result;
	}
        Object facetParent = null;

	NamingContainer closestContainer = null;
	UIComponent containerComponent = component;

        // check if its a facet (facets are not containers)
        // this also checks if we start off with nested facets
        facetParent = containerComponent.getAttribute(
            UIComponent.FACET_PARENT_ATTR);
        while (facetParent != null) {
            containerComponent = (UIComponent) facetParent;
            facetParent = containerComponent.getAttribute(
                UIComponent.FACET_PARENT_ATTR);
        }
	
        // Search for an ancestor that is a naming container
        while (null != (containerComponent =
                        containerComponent.getParent())) {
            facetParent = containerComponent.getAttribute(
                UIComponent.FACET_PARENT_ATTR);
            if (facetParent != null) {
                containerComponent = (UIComponent) facetParent;
            }
            if (containerComponent instanceof NamingContainer) {
                closestContainer = (NamingContainer) containerComponent;
                break;
            }
        }
	
	// If none is found, see if this is a naming container
	if (null == closestContainer && component instanceof NamingContainer) {
	    closestContainer = (NamingContainer) component;
	}
	
	if (null != closestContainer) {

	    // If there is no componentId, generate one and store it
	    if (null == (result = component.getComponentId())) {
		// Don't call setComponentId() because it checks for
		// uniqueness.  No need.
		component.setAttribute("componentId",
				       result = closestContainer.generateClientId());
	    }

	    // build the client side id
	    containerComponent = (UIComponent) closestContainer;
	    // If this is the root naming container, break
	    if (null != containerComponent.getParent()) {
		result = containerComponent.getClientId(context) +
		    UIComponent.SEPARATOR_CHAR + result;
	    }

	}
	
        // Store the client identifier for future use
	if (null == result) {
	    throw new NullPointerException();
	}
	component.setAttribute("clientId", result);
	return (result);

    }

    protected String getKeyAndLookupInBundle(FacesContext context,
					     UIComponent component, 
					     String keyAttr) throws MissingResourceException{
	String key = null, bundleName = null;
	ResourceBundle bundle = null;

        key = (String) component.getAttribute(keyAttr);
        bundleName = (String)component.getAttribute(BUNDLE_ATTR);

        // if the bundleName is null for this component, it might have
        // been set on the root component.
        if ( bundleName == null ) {
            UIComponent root = context.getTree().getRoot();

            bundleName = (String)root.getAttribute(BUNDLE_ATTR);
        }
	// verify our component has the proper attributes for key and bundle.
	if (null == key || null == bundleName) {
	    throw new MissingResourceException("Can't load JSTL classes", 
					       bundleName, key);
	}
	
	// verify the required Class is loadable
	// PENDING(edburns): Find a way to do this once per ServletContext.
	if (null == Thread.currentThread().getContextClassLoader().
	    getResource("javax.servlet.jsp.jstl.fmt.LocalizationContext")){
	    Object [] params = { "javax.servlet.jsp.jstl.fmt.LocalizationContext" };
	    throw new MissingResourceException("Can't load JSTL classes", 
					       bundleName, key);
	}
	
	// verify there is a ResourceBundle for this modelReference
	javax.servlet.jsp.jstl.fmt.LocalizationContext locCtx = null;
	if (null == (locCtx = (javax.servlet.jsp.jstl.fmt.LocalizationContext) 
		     context.getModelValue(bundleName)) ||
	    null == (bundle = locCtx.getResourceBundle())) {
	    throw new MissingResourceException("Can't load JSTL classes", 
					       bundleName, key);
	}
	
	return bundle.getString(key);
    }



}
