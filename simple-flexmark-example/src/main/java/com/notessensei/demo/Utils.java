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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.common.base.Charsets;
import com.google.common.io.CharSink;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;

/**
 * @author Stephan H. Wissel
 */
public class Utils {

  /**
   * @param resourceName
   * @return a String
   */
  public static String getStringFromResource(final String resourceName) {

    String result = null;

    try {
      final InputStream in = Utils.class.getResourceAsStream(resourceName);
      result = CharStreams.toString(new InputStreamReader(in, Charsets.UTF_8));
      in.close();
    } catch (final Exception e) {
      System.err.println("Could not read " + resourceName + " - " + e.getMessage());
    }
    return result;
  }

  /**
   * @param source
   * @param resultDestination
   * @throws IOException
   */
  public static void writeStringToFile(final String source, final String resultDestination) throws IOException {
    final File file = new File(resultDestination);
    final CharSink sink = Files.asCharSink(file, Charsets.UTF_8);
    sink.write(source);

  }

  /**
   * Helper class for things
   */
  private Utils() {
    // Static methods only
  }

}
