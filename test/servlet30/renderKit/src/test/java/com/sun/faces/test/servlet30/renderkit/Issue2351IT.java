/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.faces.test.servlet30.renderkit; 

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import org.junit.*;
import static org.junit.Assert.*;

public class Issue2351IT {

    /**
     * Stores the web URL.
     */
    private String webUrl;
    /**
     * Stores the web client.
     */
    private WebClient webClient;

    @Before
    public void setUp() {
        webUrl = System.getProperty("integration.url");
        webClient = new WebClient();
    }

    @After
    public void tearDown() {
        webClient.close();
    }


    // ------------------------------------------------------------ Test Methods

    /**
     * This test verifies that using a null value for the value of outputFormat works.
     * It should just dispaly nothing. 
     */
    @Test
    public void testNullValueForOutputFormat() throws Exception {
        HtmlPage page = webClient.getPage(webUrl+"faces/outputFormat.xhtml");
        assertTrue(page.asText().contains("outputFormat with no arguements: end outputFormat with no arguements"));
        assertTrue(page.asText().contains("outputFormat with null expression: end outputFormat with null expression"));
        assertTrue(page.asText().contains("outputFormat with expression that evaluates to null (via bean) : end outputFormat with expression that evaluates to null (via bean)"));
    }
}
