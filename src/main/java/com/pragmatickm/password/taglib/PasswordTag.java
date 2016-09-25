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

import static com.aoindustries.taglib.AttributeUtils.resolveValue;
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
import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;

public class PasswordTag extends ElementTag<Password> {

	private ValueExpression href;
	public void setHref(ValueExpression href) {
		this.href = href;
    }

	private ValueExpression username;
    public void setUsername(ValueExpression username) {
		this.username = username;
    }

	private ValueExpression password;
    public void setPassword(ValueExpression password) {
		this.password = password;
    }

	@Override
	protected Password createElement() {
		return new Password();
	}

	@Override
	protected void evaluateAttributes(Password pssword, ELContext elContext) throws JspTagException, IOException {
		super.evaluateAttributes(pssword, elContext);
		pssword.setHref(resolveValue(href, String.class, elContext));
		pssword.setUsername(resolveValue(username, String.class, elContext));
		final PageContext pageContext = (PageContext)getJspContext();
		boolean demoMode = SemanticCMS.getInstance(pageContext.getServletContext()).getDemoMode();
		pssword.setPassword(
			demoMode
				? com.pragmatickm.password.servlet.Password.DEMO_MODE_PASSWORD
				: resolveValue(password, String.class, elContext)
		);
	}

	private SemanticCMS semanticCMS;
	private PageIndex pageIndex;
	@Override
	protected void doBody(Password password, CaptureLevel captureLevel) throws JspException, IOException {
		final PageContext pageContext = (PageContext)getJspContext();
		semanticCMS = SemanticCMS.getInstance(pageContext.getServletContext());
		pageIndex = PageIndex.getCurrentPageIndex(pageContext.getRequest());
		super.doBody(password, captureLevel);
	}

	@Override
	public void writeTo(Writer out, ElementContext context) throws IOException {
		Password element = getElement();
		if(!(element.getParentElement() instanceof PasswordTable)) {
			PasswordImpl.writePassword(semanticCMS, pageIndex, out, context, element);
		}
	}
}
