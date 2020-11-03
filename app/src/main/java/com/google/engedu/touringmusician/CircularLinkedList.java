/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.touringmusician;


import android.graphics.Point;

import java.util.Iterator;

public class CircularLinkedList implements Iterable<Point> {

    private class Node {
        Point point;
        Node prev, next;
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
    }

    private float totalDist;
    Node head;

    public void insertBeginning(Point p) {

        //if list empty
        if (head==null){
            head = new Node();
            head.prev=head;
            head.next=head;
            head.point=p;
            totalDist = 0;
        }
        else {
            Node oldPrev = head.prev;
            Node newHead = new Node();
            newHead.point = p;
            newHead.next = head;
            newHead.prev = oldPrev;
            oldPrev.next = newHead;
            head.prev = newHead;
            totalDist += distanceBetween(p,head.point);
            head = newHead;

        }

    }

    private float distanceBetween(Point from, Point to) {
        return (float) Math.sqrt(Math.pow(from.y-to.y, 2) + Math.pow(from.x-to.x, 2));
    }

    public float totalDistance() {
        return totalDist;
    }

    public void insertNearest(Point p) {
        if (head==null){ insertBeginning(p);}
        else {
            Node closestNode = head;
            float closestDist = distanceBetween(head.point, p);
            Node current = head.next;
            while (current != head) {
                if (distanceBetween(p, current.point) < closestDist) {
                    closestNode = current;
                    closestDist = distanceBetween(p, current.point);
                }
                current=current.next;
            }
            totalDist += closestDist;
            insertAfter(p,closestNode);
        }
    }
    private void insertAfter(Point p, Node prevNode){
        Node newNode = new Node();
        newNode.point = p;
        newNode.prev = prevNode;
        newNode.next = prevNode.next;
        prevNode.next.prev = newNode;
        prevNode.next = newNode;
    }

    public void insertSmallest(Point p) {
        if (head==null || head.next==head){ insertBeginning(p);}
        else {
            Node prevNode = head;
            float minDist = totalDist-distanceBetween(head.point,head.next.point)+distanceBetween(head.point, p)+distanceBetween(p,head.next.point);
            Node current = head.next;
            while (current != head) {
                float newTotalDist = totalDist-distanceBetween(current.point,current.next.point)+distanceBetween(current.point, p)+distanceBetween(p,current.next.point);
                if (newTotalDist < minDist) {
                    prevNode = current;
                    minDist = newTotalDist;
                }
                current=current.next;
            }
            totalDist = minDist;
            insertAfter(p,prevNode);
        }
    }

    public void reset() {
        head = null;
    }

    private class CircularLinkedListIterator implements Iterator<Point> {

        Node current;

        public CircularLinkedListIterator() {
            current = head;
        }

        @Override
        public boolean hasNext() {
            return (current != null);
        }

        @Override
        public Point next() {
            Point toReturn = current.point;
            current = current.next;
            if (current == head) {
                current = null;
            }
            return toReturn;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Iterator<Point> iterator() {
        return new CircularLinkedListIterator();
    }


}
