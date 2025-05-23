/*
 * pragmatickm-password-taglib - Passwords nested within SemanticCMS pages and elements in a JSP environment.
 * Copyright (C) 2013, 2014, 2015, 2016, 2021, 2022, 2024  AO Industries, Inc.
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

import static com.aoapps.servlet.filter.FunctionContext.getRequest;

import java.io.IOException;

/**
 * Tag library function implementations.
 */
public final class Functions {

  /** Make no instances. */
  private Functions() {
    throw new AssertionError();
  }

  /**
   * See {@link com.pragmatickm.password.servlet.Functions#generatePassword(javax.servlet.ServletRequest)}.
   */
  public static String generatePassword() throws IOException {
    return com.pragmatickm.password.servlet.Functions.generatePassword(getRequest());
  }

  /**
   * See {@link com.pragmatickm.password.servlet.Functions#generateShortPassword(javax.servlet.ServletRequest)}.
   */
  public static String generateShortPassword() {
    return com.pragmatickm.password.servlet.Functions.generateShortPassword(getRequest());
  }
}
