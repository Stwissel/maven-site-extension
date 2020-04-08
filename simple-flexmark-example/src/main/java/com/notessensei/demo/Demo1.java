/*
 * ========================================================================== *
 * Copyright (C) 2019, 2020 HCL ( http://www.hcl.com/ ) *
 * All rights reserved. *
 * ========================================================================== *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may *
 * not use this file except in compliance with the License. You may obtain a *
 * copy of the License at <http://www.apache.org/licenses/LICENSE-2.0>. *
 * *
 * Unless required by applicable law or agreed to in writing, software *
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT *
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the *
 * License for the specific language governing permissions and limitations *
 * under the License. *
 * ==========================================================================
 */

package com.notessensei.demo;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.vladsch.flexmark.ext.admonition.AdmonitionExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.PegdownExtensions;
import com.vladsch.flexmark.profile.pegdown.PegdownOptionsAdapter;
import com.vladsch.flexmark.util.ast.IParse;
import com.vladsch.flexmark.util.ast.IRender;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.data.MutableDataHolder;
import com.vladsch.flexmark.util.misc.Extension;

/**
 * @author Stephan H. Wissel
 *         Sample class that shows the translation of a markdown site body
 *         into a HTML Template
 */
public class Demo1 {

  /**
   * @param args
   * @throws IOException
   */
  public static void main(final String[] args) throws IOException {
    final Demo1 d = new Demo1();
    final String template = "/template.mustache";
    final String markdown = "/sample.md";
    final String result = "target/sample.html";
    d.runSample(template, markdown, result);
    System.out.println("Done");
  }

  /**
   * @return Options like in the site-plugin plus extensions
   */
  private DataHolder getFlexOptions() {
    final ArrayList<Extension> extensions = new ArrayList<>();

    final MutableDataHolder flexmarkOptions = PegdownOptionsAdapter.flexmarkOptions(
        PegdownExtensions.ALL & ~(PegdownExtensions.HARDWRAPS | PegdownExtensions.ANCHORLINKS)).toMutable();
    for (final Extension extension : flexmarkOptions.get(com.vladsch.flexmark.parser.Parser.EXTENSIONS)) {
      extensions.add(extension);
    }

    // Commented out since we are not in a Doxia environment
    // extensions.add( FlexmarkDoxiaExtension.create() );
    
    // This is the new line added
    extensions.add( AdmonitionExtension.create());

    flexmarkOptions.set(com.vladsch.flexmark.parser.Parser.EXTENSIONS, extensions);
    flexmarkOptions.set(HtmlRenderer.HTML_BLOCK_OPEN_TAG_EOL, false);
    flexmarkOptions.set(HtmlRenderer.HTML_BLOCK_CLOSE_TAG_EOL, false);
    flexmarkOptions.set(HtmlRenderer.MAX_TRAILING_BLANK_LINES, -1);

    return flexmarkOptions;
  }

  /**
   * @param markdownLocation
   * @return
   */
  private Map<String, String> getMarkdownBody(final String markdownLocation) {
    final Map<String, String> result = new HashMap<>();
    result.put("Body", this.transformMarkdown(markdownLocation));
    return result;
  }

  private void runSample(final String templateLocation, final String markdownLocation, final String resultDestination)
      throws IOException {

    final MustacheFactory mf = new DefaultMustacheFactory();
    final Mustache m = mf.compile(new StringReader(Utils.getStringFromResource(templateLocation)), "Sample");
    final StringWriter writer = new StringWriter();
    m.execute(writer, this.getMarkdownBody(markdownLocation)).flush();
    Utils.writeStringToFile(writer.toString(), resultDestination);
    writer.close();
  }

  /**
   * @param markdownLocation
   * @return HTML Body
   */
  private String transformMarkdown(final String markdownLocation) {

    final String markdownSource = Utils.getStringFromResource(markdownLocation);
    final DataHolder dataHolder = this.getFlexOptions();
    final IParse parser = Parser.builder(dataHolder).build();
    final IRender render = HtmlRenderer.builder(dataHolder).build();
    final Node document = parser.parse(markdownSource);

    return render.render(document);
  }

}
