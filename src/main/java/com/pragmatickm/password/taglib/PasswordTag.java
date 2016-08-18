/*
 * pragmatickm-password-taglib - Passwords nested within SemanticCMS pages and elements in a JSP environment.
 * Copyright (C) 2013, 2014, 2015, 2016  AO Industries, Inc.
 *     support@aoindustries.com
 *     7262 Bull Pen Cir
 *     Mobile, AL 36695
 *
 * This file is part of pragmatickm-password-taglib.
 *
 * pragmatickm-password-taglib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * pragmatickm-password-taglib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with pragmatickm-password-taglib.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.pragmatickm.password.taglib;

import com.pragmatickm.password.model.Password;
import com.pragmatickm.password.model.PasswordTable;
import com.pragmatickm.password.servlet.impl.PasswordImpl;
import com.semanticcms.core.model.ElementContext;
import com.semanticcms.core.servlet.CaptureLevel;
import com.semanticcms.core.servlet.PageIndex;
import com.semanticcms.core.servlet.SemanticCMS;
import com.semanticcms.core.taglib.ElementTag;
import java.io.IOException;
import java.io.Writer;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

public class PasswordTag extends ElementTag<Password> {

	public PasswordTag() {
		super(new Password());
	}

	public void setHref(String href) {
		element.setHref(href);
    }

    public void setUsername(String username) {
		element.setUsername(username);
    }

    public void setPassword(String password) {
		final PageContext pageContext = (PageContext)getJspContext();
		boolean demoMode = SemanticCMS.getInstance(pageContext.getServletContext()).getDemoMode();
		element.setPassword(demoMode ? com.pragmatickm.password.servlet.Password.DEMO_MODE_PASSWORD : password);
    }

	private PageIndex pageIndex;
	@Override
	protected void doBody(CaptureLevel captureLevel) throws JspException, IOException {
		final PageContext pageContext = (PageContext)getJspContext();
		pageIndex = PageIndex.getCurrentPageIndex(pageContext.getRequest());
		super.doBody(captureLevel);
	}

	@Override
	public void writeTo(Writer out, ElementContext context) throws IOException {
		if(!(element.getParentElement() instanceof PasswordTable)) {
			PasswordImpl.writePassword(pageIndex, out, context, element);
		}
	}
}
