package com.example.bach0.hustplant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by bach0 on 4/11/2018.
 */

public class GraphNode {
    int x;
    int y;
    Set<GraphNode> children;

    @Override
    public String toString() {
        return "GraphNode{" +
                "x=" + x +
                ", y=" + y +
                ", children=" + children +
                '}';
    }

    GraphNode(int x, int y) {
        this.x = x;
        this.y = y;
        this.children = new HashSet<>();
    }

    GraphNode(int x, int y, Collection<GraphNode> children) {
        this.x = x;
        this.y = y;
        this.children = new HashSet<>();
        this.children.addAll(children);
    }

    public void addChild(GraphNode node) {
        children.add(node);
    }

    public void addChildren(Collection<GraphNode> children) {
        this.children.addAll(children);
    }

    public void removeChild(GraphNode node) {
        children.remove(node);
    }

    List<GraphNode> findPath(GraphNode end) {
        Set<GraphNode> closed = new HashSet<>();
        Set<GraphNode> open = new HashSet<>();
        open.add(this);
        Map<GraphNode, GraphNode> cameFrom = new HashMap<>();
        Map<GraphNode, Integer> score = new HashMap<>();
        score.put(this, 0);
        while (!open.isEmpty()) {
            int minScore = Integer.MAX_VALUE;
            GraphNode current = null;
            for (GraphNode n : open) {
                int currentScore = score.get(n);
                if (currentScore < minScore) {
                    current = n;
                    minScore = currentScore;
                }
            }
            if (current == end) {
                List<GraphNode> path = new ArrayList<>();
                path.add(current);
                while (cameFrom.get(current) != null) {
                    path.add(0, cameFrom.get(current));
                    current = cameFrom.get(current);
                }
                return path;
            }
            open.remove(current);
            closed.add(current);
            for (GraphNode c : current.children) {
                if (closed.contains(c)) {
                    continue;
                }
                if (!open.contains(c)) {
                    open.add(c);
                }
                int childScore = score.get(current) + costEstimate(current, c);
                if (score.containsKey(c) && childScore >= score.get(c)) {
                    continue;
                }
                cameFrom.put(c, current);
                score.put(c, childScore);
            }
        }
        return null;
    }

    int costEstimate(GraphNode start, GraphNode end) {
        return (int) Math.sqrt((start.x - end.x) * (start.x - end.x) + (start.y - end.y) * (start.y - end.y));
    }
}
