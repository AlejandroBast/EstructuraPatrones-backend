package com.example.auth.datastruct;

import java.util.ArrayList;
import java.util.List;

public class RoleHierarchyTree {
  public static class Node {
    private final String role;
    private final List<Node> children = new ArrayList<>();
    public Node(String role) { this.role = role; }
    public String getRole() { return role; }
    public List<Node> getChildren() { return children; }
    public Node addChild(String role) { Node n = new Node(role); children.add(n); return n; }
  }

  private final Node root = new Node("ROOT");

  public RoleHierarchyTree() {
    Node user = root.addChild("USER");
    user.addChild("ADMIN");
  }

  public Node getRoot() { return root; }
}

