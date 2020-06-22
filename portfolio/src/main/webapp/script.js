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
  const messagesElement = document.getElementById('messages-container');
  const buttonElement = document.getElementById('view-button');

  // Toggle hide and show
  if (messagesElement.style.display === "none") {
    messagesElement.style.display = "block";
    buttonElement.innerText = "Hide messages";
  } else {
    messagesElement.style.display = "none";
    buttonElement.innerText = "View messages";
  }

  fetch('/form-handler').then(response => response.json()).then((messages) => {
    messagesElement.innerHTML = '';

    for (i = 0; i < messages.length; i++) {
      const emoji = String.fromCodePoint(parseInt(messages[i].emojiCode));
      const imageLabels = messages[i].imageLabels;
    //   const imageLabelDescriptions = messages[i].imageLabelDescriptions;
    //   const imageLabelScores = messages[i].imageLabelScores;
      messagesElement.appendChild(createPElement("--------------------------------"));
      messagesElement.appendChild(createPElement(messages[i].text + " " + emoji));
      messagesElement.appendChild(createImgElement(messages[i].imageUrl));
      messagesElement.appendChild(createPElement("Image analysis:"));
      messagesElement.appendChild(createUlElement(imageLabels));
    //   messagesElement.appendChild(createUlElement(imageLabelDescriptions, imageLabelScores));
    }
  });
}

/** Creates an <p> element containing text. */
function createPElement(text) {
  const pElement = document.createElement('p');
  pElement.innerText = text;
  pElement.style.cssText = 'margin:auto; text-align:center';
  return pElement;
}

/** Creates an <img> element containing an image. */
function createImgElement(url) {
  const imgElement = document.createElement('img');
  imgElement.src = url;
  imgElement.style.cssText = 
      'width:200px; display:block; margin:auto; margin-bottom:10px; margin-top:10px;';
  return imgElement;
}

/** Creates a <ul> element containing image labels. */
function createUlElement(imageLabels) {
  const ulElement = document.createElement('ul');
  for (j = 0; j < imageLabels.length; j++){
    const liElement = document.createElement('li');
    liElement.innerText = imageLabels[j].description + " " + imageLabels[j].score;
    ulElement.appendChild(liElement);
  }
  ulElement.style.cssText = 'font-size:13px;';
  return ulElement;
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


