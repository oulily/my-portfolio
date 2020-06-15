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

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/messages")
public class DataServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    // Load messages from Datastore
    Query query = new Query("Message");

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    List<String> messages = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      String firstName = (String) entity.getProperty("firstName");
      String lastName = (String) entity.getProperty("lastName");
      String text = (String) entity.getProperty("text");
      Double sentimentScore = (Double) entity.getProperty("sentimentScore");
      String sentimentScoreStr = String.format("%.2f", sentimentScore);
      
      String emojiCode;
      if (sentimentScore < -0.5) {
        // angry face
        emojiCode = "0x1F620";
      } else if (sentimentScore < 0) {
        // slightly frowning face
        emojiCode = "0x1F641";
      } else if (sentimentScore == 0) {
        // neutral face
        emojiCode = "0x1F610";
      } else if (sentimentScore < 0.5) {
        // slightly smiling face
        emojiCode = "0x1F642";
      } else {
        // grinning face with big eyes
        emojiCode = "0x1F603";
      }

      String message = text + " - " + firstName + " " + lastName +
          " (Sentiment Score: " + sentimentScoreStr + ")";

      messages.add(message);
      messages.add(emojiCode);
    }

    // Convert the messages to JSON.
    Gson gson = new Gson();
    String json = gson.toJson(messages);

    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    // Get the input from the form.
    String firstName = getParameter(request, "f-name", "");
    String lastName = getParameter(request, "l-name", "");
    String text = getParameter(request, "text-input", "");

    // Get sentiment score of text
    Document doc =
        Document.newBuilder().setContent(text).setType(Document.Type.PLAIN_TEXT).build();
    LanguageServiceClient languageService = LanguageServiceClient.create();
    Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
    float sentimentScore = sentiment.getScore();
    languageService.close();
    
    // Add message to Datastore
    Entity messageEntity = new Entity("Message");
    messageEntity.setProperty("firstName", firstName);
    messageEntity.setProperty("lastName", lastName);
    messageEntity.setProperty("text", text);
    messageEntity.setProperty("sentimentScore", sentimentScore);
    
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(messageEntity);

    // Redirect back to the HTML page.
    response.sendRedirect("/contact.html");
  }

  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client
   */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}
