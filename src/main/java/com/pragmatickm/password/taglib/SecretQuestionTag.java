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

import static com.aoapps.taglib.AttributeUtils.resolveValue;
import com.pragmatickm.password.model.Password;
import com.semanticcms.core.controller.SemanticCMS;
import com.semanticcms.core.model.Node;
import com.semanticcms.core.pages.CaptureLevel;
import com.semanticcms.core.pages.local.CurrentCaptureLevel;
import com.semanticcms.core.pages.local.CurrentNode;
import java.io.IOException;
import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class SecretQuestionTag extends SimpleTagSupport {

	public static final String TAG_NAME = "<password:secretQuestion>";

	private ValueExpression question;
    public void setQuestion(ValueExpression question) {
		this.question = question;
    }

	private ValueExpression answer;
    public void setAnswer(ValueExpression answer) {
		this.answer = answer;
    }

	@Override
    public void doTag() throws JspException, IOException {
		final PageContext pageContext = (PageContext)getJspContext();
		final ServletRequest request = pageContext.getRequest();

		// Find the required password tag
		Node currentNode = CurrentNode.getCurrentNode(request);
		if(!(currentNode instanceof Password)) throw new JspTagException(TAG_NAME + " tag must be nested inside a " + PasswordTag.TAG_NAME + " tag.");
		Password currentPassword = (Password)currentNode;

		assert
			CurrentCaptureLevel.getCaptureLevel(request).compareTo(CaptureLevel.META) >= 0
			: "This is always contained by a password tag, so this is only invoked at captureLevel >= META";

		// Evaluate expressions
		ELContext elContext = pageContext.getELContext();

		boolean demoMode = SemanticCMS.getInstance(pageContext.getServletContext()).getDemoMode();
		currentPassword.addSecretQuestion(
			resolveValue(question, String.class, elContext),
			demoMode
				? com.aoapps.security.Password.MASKED_PASSWORD
				: resolveValue(answer, String.class, elContext)
		);
	}
}
