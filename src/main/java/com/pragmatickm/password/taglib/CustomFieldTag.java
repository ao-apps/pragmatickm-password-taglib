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

import static com.aoapps.lang.Strings.nullIfEmpty;
import static com.aoapps.servlet.el.ElUtils.resolveValue;

import com.pragmatickm.password.model.Password;
import com.semanticcms.core.model.Node;
import com.semanticcms.core.model.PageRef;
import com.semanticcms.core.servlet.CaptureLevel;
import com.semanticcms.core.servlet.CurrentNode;
import com.semanticcms.core.servlet.PageRefResolver;
import java.io.IOException;
import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Provides a custom field to a {@link PasswordTag}.
 */
public class CustomFieldTag extends SimpleTagSupport {

  public static final String TAG_NAME = "<password:customField>";

  private ValueExpression name;

  public void setName(ValueExpression name) {
    this.name = name;
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
  public void doTag() throws JspException, IOException {
    final PageContext pageContext = (PageContext) getJspContext();
    final HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

    // Find the required password tag
    final Node currentNode = CurrentNode.getCurrentNode(request);
    if (!(currentNode instanceof Password)) {
      throw new JspTagException(TAG_NAME + " tag must be nested inside a " + PasswordTag.TAG_NAME + " tag.");
    }
    final Password currentPassword = (Password) currentNode;

    assert
        CaptureLevel.getCaptureLevel(request).compareTo(CaptureLevel.META) >= 0
        : "This is always contained by a password tag, so this is only invoked at captureLevel >= META";

    // Evaluate expressions
    ELContext elContext = pageContext.getELContext();
    String nameStr = resolveValue(name, String.class, elContext);
    String bookStr = nullIfEmpty(resolveValue(book, String.class, elContext));
    String pageStr = nullIfEmpty(resolveValue(page, String.class, elContext));
    String elementStr = nullIfEmpty(resolveValue(element, String.class, elContext));
    String valueStr = resolveValue(value, String.class, elContext);

    // Determine the book-relative page path
    PageRef pageRef;
    if (pageStr == null) {
      if (bookStr != null) {
        throw new JspTagException("page must be provided when book is provided.");
      }
      if (elementStr != null) {
        throw new JspTagException("page must be provided when element is provided.");
      }
      pageRef = null;
    } else {
      final ServletContext servletContext = pageContext.getServletContext();
      try {
        pageRef = PageRefResolver.getPageRef(servletContext, request, bookStr, pageStr);
      } catch (ServletException e) {
        throw new JspTagException(e);
      }
    }
    currentPassword.addCustomField(
        nameStr,
        pageRef,
        elementStr,
        valueStr
    );
  }
}
