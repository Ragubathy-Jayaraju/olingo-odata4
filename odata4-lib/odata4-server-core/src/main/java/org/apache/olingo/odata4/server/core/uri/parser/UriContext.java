/* 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.olingo.odata4.server.core.uri.parser;

import java.util.Stack;

import org.apache.olingo.odata4.commons.api.edm.EdmType;
import org.apache.olingo.odata4.server.core.uri.UriInfoImpl;
import org.apache.olingo.odata4.server.core.uri.antlr.UriParserParser.ExpandPathContext;
import org.apache.olingo.odata4.server.core.uri.antlr.UriParserParser.ExpandPathExtensionContext;
import org.apache.olingo.odata4.server.core.uri.antlr.UriParserParser.SelectItemContext;
import org.apache.olingo.odata4.server.core.uri.antlr.UriParserParser.SelectSegmentContext;
import org.apache.olingo.odata4.server.core.uri.parser.UriParseTreeVisitor.TypeInformation;
import org.apache.olingo.odata4.server.core.uri.queryoption.ExpandItemImpl;
import org.apache.olingo.odata4.server.core.uri.queryoption.SelectItemImpl;

public class UriContext {

  public static class LambdaVariables {
    public boolean isCollection;
    public String name;
    public EdmType type;
  }

  /**
   * Hold all currently allowed lambda variables
   * As lambda functions can be nested there may be more than one allowed lambda variables at a time while parsing a
   * $filter or $orderby expressions.
   */
  public Stack<LambdaVariables> allowedLambdaVariables;
  /**
   * Used to stack type information for nested $expand, $filter query options and other cases.
   */
  public Stack<TypeInformation> contextTypes;
  /**
   * Set within method {@link #visitExpandItem(ExpandPathContext ctx)} and {@link #visitExpandPathExtension(final
   * ExpandPathExtensionContext ctx)} to allow nodes
   * deeper in the expand tree at {@link #visitExpandPathExtension(ExpandPathExtensionContext ctx)} appending path
   * segments to the currently processed {@link ExpandItemImpl}.
   */
  public ExpandItemImpl contextExpandItemPath;
  /**
   * Set within method {@link #visitSelectItem(SelectItemContext ctx)} to allow nodes
   * deeper in the expand tree at {@link #visitSelectSegment(SelectSegmentContext ctx)} appending path segments to the
   * currently processed {@link SelectItemImpl}.
   */
  public SelectItemImpl contextSelectItem;
  /**
   * Stores the currently processed UriInfo objects. There is one URI Info object for the resource path
   * and one for each new first member access within $filter and $orderBy options.
   */
  public UriInfoImpl contextUriInfo;
  public boolean contextReadingFunctionParameters;

  public UriContext() {

    contextExpandItemPath = null;
    contextReadingFunctionParameters = false;
    contextSelectItem = null;
    contextTypes = new Stack<UriParseTreeVisitor.TypeInformation>();
    allowedLambdaVariables = new Stack<UriContext.LambdaVariables>();

  }
}