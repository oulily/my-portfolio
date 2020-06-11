// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/random-fact")
public class DataServlet extends HttpServlet {

  private List<String> facts;
  
  @Override
  public void init() {
    facts = new ArrayList<>();
    facts.add("I played the viola in high school!");
    facts.add("My favorite TV show is Killing Eve!");
    facts.add("I have two younger sisters!");
    facts.add("I love coffee!"); 
    facts.add("I took an aerial silks class in college!");
    facts.add("I love spicy foods!");
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String fact = facts.get((int) (Math.random() * facts.size()));

    response.setContentType("text/html;");
    response.getWriter().println(fact);
  }
}
