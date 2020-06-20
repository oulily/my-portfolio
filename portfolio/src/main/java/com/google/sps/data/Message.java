package com.google.sps.data;

/** An item on a list of messages */
public final class Message {

  private final String text;
  private final String emojiCode;
  private final String imageUrl;

  public Message(String text, String emojiCode, String imageUrl) {
    this.text = text;
    this.emojiCode = emojiCode;
    this.imageUrl = imageUrl;
  }
}