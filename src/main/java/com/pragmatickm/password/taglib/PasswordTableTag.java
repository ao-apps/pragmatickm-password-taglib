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

import com.aoindustries.io.buffer.BufferResult;
import com.aoindustries.io.buffer.BufferWriter;
import com.aoindustries.io.buffer.SegmentedWriter;
import com.pragmatickm.password.model.Password;
import com.pragmatickm.password.model.PasswordTable;
import com.pragmatickm.password.servlet.impl.PasswordTableImpl;
import com.semanticcms.core.model.ElementContext;
import com.semanticcms.core.servlet.CaptureLevel;
import com.semanticcms.core.taglib.ElementTag;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;

public class PasswordTableTag extends ElementTag<PasswordTable> {

	public PasswordTableTag() {
		super(new PasswordTable());
	}

	public void setHeader(String header) {
		element.setHeader(header);
	}

	private final List<Password> passwords = new ArrayList<Password>();
	public void setPasswords(Iterable<Password> passwords) {
		this.passwords.clear();
		for(Password password : passwords) this.passwords.add(password);
	}

	private String style;
	public void setStyle(String style) {
		this.style = style;
	}

	private BufferResult writeMe;
	@Override
	protected void doBody(CaptureLevel captureLevel) throws JspException, IOException {
		try {
			super.doBody(captureLevel);
			BufferWriter out = (captureLevel == CaptureLevel.BODY) ? new SegmentedWriter() : null;
			// TODO: Auto temp file here for arbitrary size content within passwordTable?
			try {
				final PageContext pageContext = (PageContext)getJspContext();
				PasswordTableImpl.writePasswordTable(
					pageContext.getServletContext(),
					(HttpServletRequest)pageContext.getRequest(),
					(HttpServletResponse)pageContext.getResponse(),
					out,
					element,
					passwords,
					style
				);
			} finally {
				if(out != null) out.close();
			}
			writeMe = out==null ? null : out.getResult();
		} catch(ServletException e) {
			throw new JspTagException(e);
		}
	}

	@Override
	public void writeTo(Writer out, ElementContext context) throws IOException {
		if(writeMe != null) writeMe.writeTo(out);
	}
}
