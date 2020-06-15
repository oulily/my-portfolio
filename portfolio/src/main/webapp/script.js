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

/**
 * Adds a random fact to the page.
 */
function addRandomFact() {
  const facts =
    ["I played the viola in high school!",
    "My favorite TV show is Killing Eve!",
    "I have two younger sisters!",
    "I love coffee!",
    "I took an aerial silks class in college!",
    "I love spicy foods!"
    ];

  // Pick a random fact.
  const fact = facts[Math.floor(Math.random() * facts.length)];

  // Add it to the page.
  const factContainer = document.getElementById('fact-container');
  factContainer.innerText = fact;
}

/**
 * Adds messages to the page.
 */
function getMessages() {
  fetch('/messages').then(response => response.json()).then((messages) => {
    const messagesPElement = document.getElementById('messages-container');
    messagesPElement.innerHTML = '';
    for (i = 0; i < messages.length; i++) {
      messagesPElement.appendChild(createPElement(messages[i]));
    }
  });
}

/** Creates an <p> element containing text. */
function createPElement(text) {
  const pElement = document.createElement('p');
  pElement.innerText = text;
  return pElement;
}
