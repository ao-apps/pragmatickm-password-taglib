/*
 * pragmatickm-password-taglib - Passwords nested within SemanticCMS pages and elements in a JSP environment.
 * Copyright (C) 2021  AO Industries, Inc.
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
module com.pragmatickm.password.taglib {
	exports com.pragmatickm.password.taglib;
	// Direct
	requires com.aoapps.encoding; // <groupId>com.aoapps</groupId><artifactId>ao-encoding</artifactId>
	requires com.aoapps.encoding.servlet; // <groupId>com.aoapps</groupId><artifactId>ao-encoding-servlet</artifactId>
	requires com.aoapps.encoding.taglib; // <groupId>com.aoapps</groupId><artifactId>ao-encoding-taglib</artifactId>
	requires com.aoapps.html; // <groupId>com.aoapps</groupId><artifactId>ao-fluent-html</artifactId>
	requires com.aoapps.html.servlet; // <groupId>com.aoapps</groupId><artifactId>ao-fluent-html-servlet</artifactId>
	requires com.aoapps.io.buffer; // <groupId>com.aoapps</groupId><artifactId>ao-io-buffer</artifactId>
	requires com.aoapps.lang; // <groupId>com.aoapps</groupId><artifactId>ao-lang</artifactId>
	requires com.aoapps.security; // <groupId>com.aoapps</groupId><artifactId>ao-security</artifactId>
	requires com.aoapps.servlet.filter; // <groupId>com.aoapps</groupId><artifactId>ao-servlet-filter</artifactId>
	requires com.aoapps.taglib; // <groupId>com.aoapps</groupId><artifactId>ao-taglib</artifactId>
	requires javax.el.api; // <groupId>javax.el</groupId><artifactId>javax.el-api</artifactId>
	requires javax.servlet.api; // <groupId>javax.servlet</groupId><artifactId>javax.servlet-api</artifactId>
	requires javax.servlet.jsp.api; // <groupId>javax.servlet.jsp</groupId><artifactId>javax.servlet.jsp-api</artifactId>
	requires com.pragmatickm.password.model; // <groupId>com.pragmatickm</groupId><artifactId>pragmatickm-password-model</artifactId>
	requires com.pragmatickm.password.servlet; // <groupId>com.pragmatickm</groupId><artifactId>pragmatickm-password-servlet</artifactId>
	requires com.semanticcms.core.model; // <groupId>com.semanticcms</groupId><artifactId>semanticcms-core-model</artifactId>
	requires com.semanticcms.core.servlet; // <groupId>com.semanticcms</groupId><artifactId>semanticcms-core-servlet</artifactId>
	requires com.semanticcms.core.taglib; // <groupId>com.semanticcms</groupId><artifactId>semanticcms-core-taglib</artifactId>
}
