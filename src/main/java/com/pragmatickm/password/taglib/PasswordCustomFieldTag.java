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
import com.semanticcms.core.model.Node;
import com.semanticcms.core.model.PageRef;
import com.semanticcms.core.servlet.CurrentNode;
import com.semanticcms.core.servlet.PageRefResolver;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class PasswordCustomFieldTag extends SimpleTagSupport {

	private String name;
	public void setName(String name) {
		this.name = name;
	}

	private String book;
	public void setBook(String book) {
		this.book = book;
	}

	private String page;
	public void setPage(String page) {
		this.page = page;
	}

	private String element;
	public void setElement(String element) {
		this.element = element;
	}

	private String value;
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public void doTag() throws JspTagException, IOException {
		final PageContext pageContext = (PageContext)getJspContext();
		final HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();

		// Find the required password tag
		Node currentNode = CurrentNode.getCurrentNode(request);
		if(!(currentNode instanceof Password)) throw new JspTagException("<d:passwordCustomField> tag must be nested inside a <d:password> tag.");
		Password currentPassword = (Password)currentNode;

		// Determine the book-relative page path
		PageRef pageRef;
		if(page == null) {
			if(book != null) throw new JspTagException("page must be provided when book is provided.");
			if(element != null) throw new JspTagException("page must be provided when element is provided.");
			pageRef = null;
		} else {
			final ServletContext servletContext = pageContext.getServletContext();
			try {
				pageRef = PageRefResolver.getPageRef(servletContext, request, this.book, this.page);
			} catch(ServletException e) {
				throw new JspTagException(e);
			}
		}
		currentPassword.addCustomField(name, pageRef, element, value);
	}
}
