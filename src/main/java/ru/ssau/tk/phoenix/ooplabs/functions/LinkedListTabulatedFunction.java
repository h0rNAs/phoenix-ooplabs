package ru.ssau.tk.phoenix.ooplabs.functions;

import java.util.Arrays;

public class LinkedListTabulatedFunction extends AbstractTabulatedFunction implements Insertable, Removable{
    Node head;
    int count;

    public LinkedListTabulatedFunction(double[] xValues, double[] yValues) {
        if (xValues.length != yValues.length) {
            throw new IllegalArgumentException("Arrays must have the same length");
        }
        for (int i = 0; i < xValues.length; i++) {
            addNode(xValues[i], yValues[i]);
        }
        for (int i = 1; i < count; i++) {
            if (xValues[i] <= xValues[i - 1]) {
                throw new IllegalArgumentException("Values must be ordered");
            }
        }
    }

    public LinkedListTabulatedFunction(MathFunction source, double xFrom, double xTo, int count) {
        if (count < 1) {
            throw new IllegalArgumentException("At least 1 point is required");
        }
        if (xFrom > xTo) {
            double temp = xFrom;
            xFrom = xTo;
            xTo = temp;
        }
        if (xFrom == xTo) {
            double value = source.apply(xFrom);
            for (int i = 0; i < count; i++) {
                addNode(xFrom, value);
            }
        } else {
            double step = (xTo - xFrom) / (count - 1);
            for (int i = 0; i < count; i++) {
                double x = xFrom + i * step;
                addNode(x, source.apply(x));
            }
        }
    }

    @Override
    public double apply(double x) {
        if (x < leftBound()) {
            return extrapolateLeft(x);
        } else if (x > rightBound()) {
            return extrapolateRight(x);
        } else {
            int index = indexOfX(x);
            if (index != -1) {
                return getY(index);
            } else {
                return interpolate(x, floorNodeOfX(x));
            }
        }
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public double getX(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + count);
        }
        return getNode(index).x;
    }

    @Override
    public double getY(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + count);
        }
        return getNode(index).y;
    }

    @Override
    public void setY(int index, double value) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + count);
        }
        getNode(index).y = value;
    }

    @Override
    public int indexOfX(double x) {
        for (int i = 0; i < count; i++) {
            if (Math.abs(getNode(i).x - x) < 1e-9) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int indexOfY(double y) {
        for (int i = 0; i < count; i++) {
            if (Math.abs(getNode(i).y - y) < 1e-9) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public double leftBound() {
        return head.x;
    }

    @Override
    public double rightBound() {
        return head.prev.x;
    }

    @Override
    protected int floorIndexOfX(double x) {
        if (x < head.x) return 0;
        if (x > head.prev.x) return count;
        for (int i = 1; i < count; i++) {
            if (x < getNode(i).x) {
                return i - 1;
            }
        }
        return count - 1;
    }

    protected Node floorNodeOfX(double x){
        if (x < head.x) return head;
        if (x > head.prev.x) return head.prev;
        for (int i = 1; i < count; i++) {
            Node iNode = getNode(i);
            if (x < iNode.x) {
                return iNode.prev;
            }
        }
        return head.prev;
    }

    @Override
    protected double extrapolateLeft(double x) {
        if (count == 1) {
            return head.y;
        }
        return interpolate(x, 0);
    }

    @Override
    protected double extrapolateRight(double x) {
        if (count == 1) {
            return head.y;
        }
        return interpolate(x, count - 2);
    }

    @Override
    protected double interpolate(double x, int floorIndex) {
        if (count == 1) {
            return head.y;
        }
        if (floorIndex < 0 || floorIndex >= count - 1) {
            throw new IllegalArgumentException("Invalid floor index");
        }
        return interpolate(x, getNode(floorIndex).x, getNode(floorIndex + 1).x,
                getNode(floorIndex).y, getNode(floorIndex + 1).y);
    }

    private double interpolate(double x, Node floorNode){
        if (count == 1) {
            return floorNode.y;
        }
        return interpolate(x, floorNode.x, floorNode.next.x, floorNode.y, floorNode.next.y);
    }

    void addNode(double x, double y){
        Node newNode = new Node(x, y);
        if (head == null) head = newNode;
        else {
            Node last = head.prev;
            last.next = newNode;
            newNode.prev = last;
            newNode.next = head;
            head.prev = newNode;
        }
        count++;
    }

    Node getNode(int index){
        Node currentNode = head;
        if ((double)index / count > 0.5) {
            while (index < count){
                currentNode = currentNode.prev;
                index++;
            }
        }
        else {
            while (index > 0){
                currentNode = currentNode.next;
                index--;
            }
        }
        return currentNode;
    }
    @Override
    public void insert(double x, double y){
        if (count == 0) {
            addNode(x,y);
            return;
        }
        int ifexistIndex = indexOfX(x);
        if (ifexistIndex != -1) {
            setY(ifexistIndex, y);
            return;
        }
        Node newNode = new Node(x, y); // Вставка в начало.
        if (x < head.x){
            newNode.next = head;
            newNode.prev = head.prev;
            head.prev.next = newNode;
            head.prev = newNode;
            head = newNode;
            count++;
            return;
        }
        Node current = head;
        do {
            if (x > current.x && (current.next == head || x < current.next.x)) {
                break;
            }
            current = current.next;
        } while (current != head);
        newNode.next = current.next;
        newNode.prev = current;
        current.next.prev = newNode;
        current.next = newNode;
        count++;
    }
    @Override
    public void remove(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + count);
        }

        Node currentNode = getNode(index);
        if (currentNode == head) head = currentNode.next;
        currentNode.prev.next = currentNode.next;
        currentNode.next.prev = currentNode.prev;
    }
}

class Node{
    public Node prev = this, next = this;
    public double x, y;

    public Node(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
