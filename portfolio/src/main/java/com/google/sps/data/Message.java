package com.google.sps.data;
import com.google.sps.data.ImageLabel;
import java.util.List;

public final class Message {

  private final String text;
  private final String emojiCode;
  private final String imageUrl;
  private final List<ImageLabel> imageLabels;

  public Message(String text, String emojiCode, String imageUrl, 
      List<ImageLabel> imageLabels) {
    this.text = text;
    this.emojiCode = emojiCode;
    this.imageUrl = imageUrl;
    this.imageLabels = imageLabels;
  }
}