package com.example.auth.datastruct;

import java.util.ArrayDeque;
import java.util.Queue;

public class RequestQueue {
  private final Queue<String> queue = new ArrayDeque<>();

  public synchronized void push(String info) { queue.add(info); }
  public synchronized String poll() { return queue.poll(); }
  public synchronized int size() { return queue.size(); }
}

