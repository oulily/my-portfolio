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
  fetch('/form-handler').then(response => response.json()).then((messages) => {
    const messagesElement = document.getElementById('messages-container');
    messagesElement.innerHTML = '';
    for (i = 0; i < messages.length; i++) {
      const emoji = String.fromCodePoint(parseInt(messages[i].emojiCode));
      messagesElement.appendChild(createPElement(messages[i].text + " " + emoji));
      messagesElement.appendChild(createImgElement(messages[i].imageUrl));
    }
  });
}

/** Creates an <p> element containing text. */
function createPElement(text) {
  const pElement = document.createElement('p');
  pElement.innerText = text;
  return pElement;
}

/** Creates an <img> element containing an image. */
function createImgElement(url) {
  const imgElement = document.createElement('img');
  imgElement.src = url;
  imgElement.style.cssText = 'width:200px; display:block; margin:auto; margin-bottom:30px';
  return imgElement;
}

/** Retrieves the URL that allows users to upload a file to Blobstore*/
function fetchBlobstoreUrl() {
  fetch('/blobstore-upload-url')
    .then((response) => {
      return response.text();
    })
    .then((imageUploadUrl) => {
      let messageForm = document.getElementById('my-form');
      messageForm.action = imageUploadUrl;
    });
}


