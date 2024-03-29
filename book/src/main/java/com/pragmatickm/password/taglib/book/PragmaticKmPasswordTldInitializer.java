/*
 * pragmatickm-password-taglib - Passwords nested within SemanticCMS pages and elements in a JSP environment.
 * Copyright (C) 2017, 2019, 2020, 2021, 2022  AO Industries, Inc.
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

package com.pragmatickm.password.taglib.book;

import com.semanticcms.tagreference.TagReferenceInitializer;
import javax.servlet.ServletContainerInitializer;

/**
 * Initializes a tag reference during {@linkplain ServletContainerInitializer application start-up}.
 */
public class PragmaticKmPasswordTldInitializer extends TagReferenceInitializer {

  /**
   * Parses the TLD file.
   */
  public PragmaticKmPasswordTldInitializer() {
    super(
        Maven.properties.getProperty("documented.name") + " Reference",
        "Taglib Reference",
        "/password/taglib",
        "/pragmatickm-password.tld",
        true,
        Maven.properties.getProperty("documented.javadoc.link.javase"),
        Maven.properties.getProperty("documented.javadoc.link.javaee"),
        // Self
        "com.pragmatickm.password.taglib", Maven.properties.getProperty("project.url") + "apidocs/com.pragmatickm.password.taglib/",
        // Dependencies
        "com.pragmatickm.password.model", "https://pragmatickm.com/password/model/apidocs/com.pragmatickm.password.model/"
    );
  }
}
