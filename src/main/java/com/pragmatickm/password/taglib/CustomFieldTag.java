/*
 * pragmatickm-password-taglib - Passwords nested within SemanticCMS pages and elements in a JSP environment.
 * Copyright (C) 2013, 2014, 2015, 2016, 2017, 2020  AO Industries, Inc.
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

import static com.aoindustries.lang.Strings.nullIfEmpty;
import com.aoindustries.net.DomainName;
import com.aoindustries.net.Path;
import static com.aoindustries.taglib.AttributeUtils.resolveValue;
import com.aoindustries.validation.ValidationException;
import com.pragmatickm.password.model.Password;
import com.semanticcms.core.controller.PageRefResolver;
import com.semanticcms.core.model.Node;
import com.semanticcms.core.model.PageRef;
import com.semanticcms.core.pages.CaptureLevel;
import com.semanticcms.core.pages.local.CurrentCaptureLevel;
import com.semanticcms.core.pages.local.CurrentNode;
import java.io.IOException;
import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class CustomFieldTag extends SimpleTagSupport {

	private ValueExpression name;
	public void setName(ValueExpression name) {
		this.name = name;
	}

	private ValueExpression domain;
	public void setDomain(ValueExpression domain) {
		this.domain = domain;
	}

	private ValueExpression book;
	public void setBook(ValueExpression book) {
		this.book = book;
	}

	private ValueExpression page;
	public void setPage(ValueExpression page) {
		this.page = page;
	}

	private ValueExpression element;
	public void setElement(ValueExpression element) {
		this.element = element;
	}

	private ValueExpression value;
	public void setValue(ValueExpression value) {
		this.value = value;
	}

	@Override
	public void doTag() throws JspTagException, IOException {
		try {
			final PageContext pageContext = (PageContext)getJspContext();
			final HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();

			// Find the required password tag
			Node currentNode = CurrentNode.getCurrentNode(request);
			if(!(currentNode instanceof Password)) throw new JspTagException("<password:customField> tag must be nested inside a <password:password> tag.");
			Password currentPassword = (Password)currentNode;

			assert
				CurrentCaptureLevel.getCaptureLevel(request).compareTo(CaptureLevel.META) >= 0
				: "This is always contained by a password tag, so this is only invoked at captureLevel >= META";

			// Evaluate expressions
			ELContext elContext = pageContext.getELContext();
			String nameStr = resolveValue(name, String.class, elContext);
			DomainName domainObj = DomainName.valueOf(
				nullIfEmpty(
					resolveValue(domain, String.class, elContext)
				)
			);
			Path bookPath = Path.valueOf(
				nullIfEmpty(
					resolveValue(book, String.class, elContext)
				)
			);
			String pageStr = nullIfEmpty(resolveValue(page, String.class, elContext));
			String elementStr = nullIfEmpty(resolveValue(element, String.class, elContext));
			String valueStr = resolveValue(value, String.class, elContext);

			if(domainObj != null && bookPath == null) {
				throw new JspTagException("book must be provided when domain is provided.");
			}

			// Determine the book-relative page path
			PageRef pageRef;
			if(pageStr == null) {
				if(bookPath != null) throw new JspTagException("page must be provided when book is provided.");
				if(elementStr != null) throw new JspTagException("page must be provided when element is provided.");
				pageRef = null;
			} else {
				final ServletContext servletContext = pageContext.getServletContext();
				try {
					pageRef = PageRefResolver.getPageRef(servletContext, request, domainObj, bookPath, pageStr);
				} catch(ServletException e) {
					throw new JspTagException(e);
				}
			}
			currentPassword.addCustomField(
				nameStr,
				pageRef,
				elementStr,
				valueStr
			);
		} catch(ValidationException e) {
			throw new JspTagException(e);
		}
	}
}
