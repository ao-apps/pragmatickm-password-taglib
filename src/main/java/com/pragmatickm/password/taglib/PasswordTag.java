/*
 * pragmatickm-password-taglib - Passwords nested within SemanticCMS pages and elements in a JSP environment.
 * Copyright (C) 2013, 2014, 2015, 2016, 2017, 2020, 2021, 2022, 2023  AO Industries, Inc.
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
 * along with pragmatickm-password-taglib.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.pragmatickm.password.taglib;

import static com.aoapps.servlet.el.ElUtils.resolveValue;

import com.aoapps.encoding.Doctype;
import com.aoapps.encoding.Serialization;
import com.aoapps.encoding.servlet.DoctypeEE;
import com.aoapps.encoding.servlet.SerializationEE;
import com.aoapps.html.Document;
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
import java.nio.charset.Charset;
import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;

/**
 * Writes a password, with optional username and href.
 */
public class PasswordTag extends ElementTag<Password> {

  public static final String TAG_NAME = "<password:password>";

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
  protected void evaluateAttributes(Password pssword, ELContext elContext) throws JspTagException {
    super.evaluateAttributes(pssword, elContext);
    pssword.setHref(resolveValue(href, String.class, elContext));
    pssword.setUsername(resolveValue(username, String.class, elContext));
    final PageContext pageContext = (PageContext) getJspContext();
    boolean demoMode = SemanticCMS.getInstance(pageContext.getServletContext()).getDemoMode();
    pssword.setPassword(
        demoMode
            ? com.aoapps.security.Password.MASKED_PASSWORD
            : resolveValue(password, String.class, elContext)
    );
  }

  private SemanticCMS semanticCms;
  private PageIndex pageIndex;
  private Serialization serialization;
  private Doctype doctype;
  private Charset characterEncoding;

  @Override
  protected void doBody(Password password, CaptureLevel captureLevel) throws JspException, IOException {
    PageContext pageContext = (PageContext) getJspContext();
    ServletContext servletContext = pageContext.getServletContext();
    HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
    semanticCms = SemanticCMS.getInstance(pageContext.getServletContext());
    pageIndex = PageIndex.getCurrentPageIndex(pageContext.getRequest());
    serialization = SerializationEE.get(servletContext, request);
    doctype = DoctypeEE.get(servletContext, request);
    characterEncoding = Charset.forName(pageContext.getResponse().getCharacterEncoding());
    super.doBody(password, captureLevel);
  }

  @Override
  public void writeTo(Writer out, ElementContext context) throws IOException {
    Password element = getElement();
    if (!(element.getParentElement() instanceof PasswordTable)) {
      PasswordImpl.writePassword(
          semanticCms,
          pageIndex,
          new Document(serialization, doctype, characterEncoding, out)
              .setAutonli(false)// Do not add extra newlines to JSP
              .setIndent(false), // Do not add extra indentation to JSP
          context,
          element
      );
    }
  }
}
