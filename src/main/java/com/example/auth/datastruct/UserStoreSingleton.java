package com.example.auth.datastruct;

import com.example.auth.domain.User;

import java.util.*;

public class UserStoreSingleton {
  private static volatile UserStoreSingleton INSTANCE;

  private final Map<String, User> byEmail = new HashMap<>();
  private final List<User> users = new ArrayList<>();

  private UserStoreSingleton() {}

  public static UserStoreSingleton getInstance() {
    if (INSTANCE == null) {
      synchronized (UserStoreSingleton.class) {
        if (INSTANCE == null) {
          INSTANCE = new UserStoreSingleton();
        }
      }
    }
    return INSTANCE;
  }

  public synchronized Optional<User> findByEmail(String email) {
    return Optional.ofNullable(byEmail.get(email.toLowerCase()))
      .map(u -> u);
  }

  public synchronized void save(User user) {
    byEmail.put(user.getEmail().toLowerCase(), user);
    users.add(user);
  }

  public synchronized List<User> list() { return Collections.unmodifiableList(users); }
}

