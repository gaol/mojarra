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

package components.components;


import java.io.IOException;
import javax.faces.FacesException;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import javax.faces.component.UICommand;


/**
 * This class represents the <code>UIMap</code> component, which corresponds
 * to the <code>map</code> tag.  A <code>map</code> tag specifies a
 * a image map, which has one or more "hot spots", which a user can 
 * click on and mouse over, resulting in some client-side or server-side
 * action.  A <code>map</code> tag contains one or more <code>area</code>
 * tags, each corresponding to a "hot spot". 
*/


public class UIMap extends UICommand {


    /* Component type for this component
    public static final String TYPE = "UIMap";

    // Return our component type
    public String getComponentType() {
        return (TYPE);
    } */ 

   // Renders the beginning of the <code>map</code> tag.
   public void encodeBegin(FacesContext context) throws IOException {

   	if (context == null) {
	    throw new NullPointerException();
	}

        // Delegate to our associated Renderer if needed
        if (getRendererType() != null) {
            super.encodeEnd(context);
            return;
        }

        ResponseWriter writer = context.getResponseWriter();
	writer.write("<map name=\"");
	writer.write(getComponentId());
	writer.write("\">");
   }

    /**
     * <p>Renders the <code>input</code> tag and the end of 
     * the <code>map</code> tag.
     */
    public void encodeEnd(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }

        // Delegate to our associated Renderer if needed
        if (getRendererType() != null) {
            super.encodeEnd(context);
            return;
        }

        ResponseWriter writer = context.getResponseWriter();
        writer.write("<input type=\"hidden\" name=\"selectedArea\"");
        writer.write("\">");
	writer.write("</map>");

    }



    /**
     * <p>Decodes the new value of this component from the incoming request.</p>
     *<p>Gets the value of the <code>selectedArea</code> hidden variable.
     * <p>Sets the value of this component's <code>currentArea</code> attribute
     * to the value of <code>selectedArea</code> to indicate which region of the 
     * map was clicked.
     * <p>Adds a <code>FacesEvent</code>, generated by this component,
     * to the <code>welcomeLabel</code> component. 
     */
    public void decode(FacesContext context) throws IOException {

        if (context == null) {
            throw new NullPointerException();
        }

        String value =
            context.getServletRequest().getParameter("selectedArea");
	if (value != null) {
	    setAttribute("currentArea", value);
            context.addFacesEvent(new ActionEvent(this, value));
        }
        setValid(true);
    }
}
