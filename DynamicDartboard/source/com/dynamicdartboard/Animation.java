package com.dynamicdartboard;
import java.io.*;

public interface Animation extends Serializable {

   public void play();

   public void play(String target);

   public void stop();
}
