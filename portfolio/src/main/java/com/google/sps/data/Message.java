package com.google.sps.data;
import java.util.List;

/** An item on a list of messages */
public final class Message {

  private final String text;
  private final String emojiCode;
  private final String imageUrl;
  private final List<String> imageLabelDescriptions;
  private final List<String> imageLabelScores;

  public Message(String text, String emojiCode, String imageUrl, 
      List<String> imageLabelDescriptions, List<String> imageLabelScores) {
    this.text = text;
    this.emojiCode = emojiCode;
    this.imageUrl = imageUrl;
    this.imageLabelDescriptions = imageLabelDescriptions;
    this.imageLabelScores = imageLabelScores;
  }
}