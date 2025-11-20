package com.example.auth.datastruct;

import java.util.Stack;

public class HistoryStack {
  private final Stack<String> stack = new Stack<>();

  public synchronized void push(String info) { stack.push(info); }
  public synchronized String pop() { return stack.isEmpty() ? null : stack.pop(); }
  public synchronized int size() { return stack.size(); }
}

