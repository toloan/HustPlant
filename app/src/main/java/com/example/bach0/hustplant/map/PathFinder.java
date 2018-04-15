package com.example.bach0.hustplant.map;

import android.graphics.Color;
import android.graphics.Point;
import android.support.annotation.NonNull;

import com.google.common.collect.TreeMultimap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by bach0 on 4/12/2018.
 */

public class PathFinder {
    int[][] mMap;
    int mWidth;
    int mHeight;
    Set<Point> closedSet = new HashSet<>();
    Set<Point> openSet = new HashSet<>();
    Map<Point, Point> cameFrom = new HashMap<>();
    Map<Point, Float> gScore = new HashMap<>();
    MutablePriorityQueue<Float, ComparablePoint> fScore = new MutablePriorityQueue<>();

    public PathFinder(int[][] map) {
        mMap = map;
        mWidth = map[0].length;
        mHeight = map.length;
    }

    public List<Point> findPath(Point start, Point end) {
        closedSet.clear();
        openSet.clear();
        cameFrom.clear();
        gScore.clear();
        fScore.clear();
        ComparablePoint s = new ComparablePoint(start);
        openSet.add(s);
        gScore.put(s, 0f);
        fScore.insertOrUpdate(distance(start, end), s);
        while (!openSet.isEmpty()) {
            ComparablePoint current = fScore.frontValue();
            fScore.pop();
            if (current.compareTo(end) == 0) {
                List<Point> path = new ArrayList<>();
                path.add(current);
                Point c = cameFrom.get(current);
                while (c != null) {
                    path.add(0, c);
                    c = cameFrom.get(c);
                }
                return path;
            }
            openSet.remove(current);
            closedSet.add(current);

            for (int i = Math.max(0, current.x - 1); i <= Math.min(mWidth - 1, current.x + 1);
                 i++) {
                for (int j = Math.max(0, current.y - 1); j <= Math.min(mHeight - 1, current.y + 1);
                     j++) {
                    if (i == current.x && j == current.y) {
                        continue;
                    }
                    if (Color.alpha(mMap[j][i]) < 128) {
                        continue;
                    }
                    ComparablePoint neighbor = new ComparablePoint(i, j);
                    if (closedSet.contains(neighbor)) {
                        continue;
                    }
                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                    float bonus = (255f - Color.red(mMap[neighbor.y][neighbor.x])) / 255f;
                    Float tentativeGScore = gScore.get(current);
                    tentativeGScore += distance(current, neighbor) + bonus;
                    Float neighborGScore = gScore.get(neighbor);
                    if (neighborGScore != null) {
                        if (tentativeGScore >= neighborGScore)
                            continue;
                    }
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    if (!closedSet.contains(neighbor))
                        fScore.insertOrUpdate(tentativeGScore + distance(neighbor, end) + bonus,
                                neighbor);
                }
            }
        }
        return null;
    }

    public float distance(int startX, int startY, int endX, int endY) {
        int dx = startX - endX;
        int dy = startY - endY;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public float distance(Point start, Point end) {
        return distance(start.x, start.y, end.x, end.y);
    }

    class ComparablePoint extends Point implements Comparable<Point> {
        ComparablePoint(Point p) {
            super(p);
        }

        ComparablePoint(int x, int y) {
            super(x, y);
        }

        @Override
        public int compareTo(@NonNull Point p) {
            return y < p.y ? -1 : y > p.y ? 1 : x < p.x ? -1 : x > p.x ? 1 : 0;
        }
    }

    class MutablePriorityQueue<K extends Comparable, V extends Comparable> {
        TreeMultimap<K, V> mKeyToVal;
        HashMap<V, K> mValToKey = new HashMap<>();

        public MutablePriorityQueue() {
            mKeyToVal = TreeMultimap.create();
        }

        public void insertOrUpdate(K key, V value) {
            K k = mValToKey.get(value);
            if (k != null) {
                for (V v : mKeyToVal.get(k)) {
                    if (v.equals(value)) {
                        mKeyToVal.remove(k, v);
                        break;
                    }
                }
            }
            mValToKey.put(value, key);
            mKeyToVal.put(key, value);
        }

        public K frontKey() {
            return mKeyToVal.entries().iterator().next().getKey();
        }

        public V frontValue() {
            return mKeyToVal.entries().iterator().next().getValue();
        }

        public void pop() {
            Map.Entry<K, V> entry = mKeyToVal.entries().iterator().next();
            K k = entry.getKey();
            V v = entry.getValue();
            mKeyToVal.remove(k, v);
            mValToKey.remove(v);
        }

        public void clear() {
            mKeyToVal.clear();
            mValToKey.clear();
        }
    }
}
