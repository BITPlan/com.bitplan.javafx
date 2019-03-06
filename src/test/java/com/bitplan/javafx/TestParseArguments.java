/**
 *
 * This file is part of the https://github.com/BITPlan/com.bitplan.javafx open source project
 *
 * The copyright and license below holds true
 * for all files except
 *    - the ones in the stackoverflow package
 *    - SwingFXUtils.java from Oracle which is provided e.g. for the raspberry env
 *
 * Copyright 2017-2018 BITPlan GmbH https://github.com/BITPlan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 *  You may obtain a copy of the License at
 *
 *  http:www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bitplan.javafx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;

/**
 * test for the argument parsing
 * @author wf
 *
 */
public class TestParseArguments {

  static public class ProperMain extends Main {
    @Argument
    public List<String> arguments = new ArrayList<String>();

    @Override
    public String getSupportEMail() {
      return "support@bitplan.com";
    }

    @Override
    public String getSupportEMailPreamble() {
      return "Hi folks - this is just a TestProject";
    }

    @Override
    public void work() throws Exception {
      // just do nothing
    }

  }

  @Test
  public void testParseArguments() throws CmdLineException {
    String argtest[][] = { { "a", "b", "c", "d" }, { "a", "b c", "d" } };
    int expectedArgs[]= {4,3};
    int i=0;
    for (String[] args : argtest) {
      ProperMain main = new ProperMain();
      main.parseArguments(args);
      assertEquals(expectedArgs[i], main.args.length);
      assertEquals(expectedArgs[i], main.arguments.size());
      i++;
    }
  }

}
