/*
 * pragmatickm-password-taglib - Passwords nested within SemanticCMS pages and elements in a JSP environment.
 * Copyright (C) 2013, 2014, 2015, 2016, 2017, 2020, 2021  AO Industries, Inc.
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

import com.aoapps.encoding.taglib.EncodingBufferedTag;
import com.aoapps.html.servlet.DocumentEE;
import com.aoapps.io.buffer.BufferResult;
import com.aoapps.io.buffer.BufferWriter;
import com.aoapps.lang.Coercion;
import static com.aoapps.taglib.AttributeUtils.resolveValue;
import com.pragmatickm.password.model.Password;
import com.pragmatickm.password.model.PasswordTable;
import com.pragmatickm.password.servlet.impl.PasswordTableImpl;
import com.semanticcms.core.model.ElementContext;
import com.semanticcms.core.servlet.CaptureLevel;
import com.semanticcms.core.taglib.ElementTag;
import java.io.IOException;
import java.io.Writer;
import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;

public class PasswordTableTag extends ElementTag<PasswordTable> /*implements StyleAttribute*/ {

	private ValueExpression header;
	public void setHeader(ValueExpression header) {
		this.header = header;
	}

	private ValueExpression passwords;
	public void setPasswords(ValueExpression passwords) {
		this.passwords = passwords;
	}

	private ValueExpression style;
	public void setStyle(ValueExpression style) {
		this.style = style;
	}

	@Override
	protected PasswordTable createElement() {
		return new PasswordTable();
	}

	@Override
	protected void evaluateAttributes(PasswordTable element, ELContext elContext) throws JspTagException, IOException {
		super.evaluateAttributes(element, elContext);
		element.setHeader(resolveValue(header, String.class, elContext));
	}

	private BufferResult writeMe;
	@Override
	protected void doBody(PasswordTable passwordTable, CaptureLevel captureLevel) throws JspException, IOException {
		try {
			super.doBody(passwordTable, captureLevel);
			if(captureLevel == CaptureLevel.BODY) {
				final PageContext pageContext = (PageContext)getJspContext();
				final ELContext elContext = pageContext.getELContext();
				final HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();

				// Evaluate expressins
				@SuppressWarnings("unchecked")
				Iterable<? extends Password> passwordIter = resolveValue(passwords, Iterable.class, elContext);
				Object styleObj = Coercion.nullIfEmpty(resolveValue(style, Object.class, elContext));

				BufferWriter capturedOut = EncodingBufferedTag.newBufferWriter(request);
				try {
					ServletContext servletContext = pageContext.getServletContext();
					HttpServletResponse response = (HttpServletResponse)pageContext.getResponse();
					PasswordTableImpl.writePasswordTable(
						servletContext,
						request,
						response,
						new DocumentEE(
							servletContext,
							request,
							response,
							capturedOut,
							false, // Do not add extra newlines to JSP
							false  // Do not add extra indentation to JSP
						),
						passwordTable,
						passwordIter,
						styleObj
					);
				} finally {
					capturedOut.close();
				}
				writeMe = capturedOut.getResult();
			} else {
				writeMe = null;
			}
		} catch(ServletException e) {
			throw new JspTagException(e);
		}
	}

	@Override
	public void writeTo(Writer out, ElementContext context) throws IOException {
		if(writeMe != null) writeMe.writeTo(out);
	}
}
