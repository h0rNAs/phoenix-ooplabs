package ru.ssau.tk.phoenix.ooplabs.functions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ssau.tk.phoenix.ooplabs.util.GlobalLoggerInitializer;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListTabulatedFunction extends AbstractTabulatedFunction implements Insertable, Removable, Serializable {
    private static long serialVersionUID;
    Node head;
    int count;

    private static final Logger logger = LogManager.getLogger(LinkedListTabulatedFunction.class);

    public LinkedListTabulatedFunction(double[] xValues, double[] yValues) {
        checkLengthIsTheSame(xValues, yValues);
        if (xValues.length < 2) {
            throw new IllegalArgumentException("At least 2 point is required");
        }
        checkSorted(xValues);
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
        logger.info("Инициализация точек");
        if (count < 2) {
            throw new IllegalArgumentException("At least 2 point is required");
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
        logger.info("Инициализация точек успешно завершена");
    }

    @Override
    public double apply(double x) {
        logger.info(String.format("Вызов метода apply для x = %.2f", x));
        if (x < leftBound()) {
            logger.debug("Применена экстраполяция слева");
            return extrapolateLeft(x);
        } else if (x > rightBound()) {
            logger.debug("Применена экстраполяция справа");
            return extrapolateRight(x);
        } else {
            logger.debug("Применена интерполяция");
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
            throw new IllegalArgumentException("Invalid index");
        }
        return getNode(index).x;
    }

    @Override
    public double getY(int index) {
        if (index < 0 || index >= count) {
            throw new IllegalArgumentException("Invalid index");
        }
        return getNode(index).y;
    }

    @Override
    public void setY(int index, double value) {
        if (index < 0 || index >= count) {
            throw new IllegalArgumentException("Invalid index");
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
        if (x < head.x) throw new IllegalArgumentException("x out of left bound");
        if (x > head.prev.x) return count;
        for (int i = 1; i < count; i++) {
            if (x < getNode(i).x) {
                return i - 1;
            }
        }
        return count - 1;
    }

    protected Node floorNodeOfX(double x){
        if (x < head.x) throw new IllegalArgumentException("x out of left bound");
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
        Node first = head;
        Node second = head.next;
        return interpolate(x, first.x, second.x, first.y, second.y);
    }

    @Override
    protected double extrapolateRight(double x) {
        if (count == 1) {
            return head.y;
        }
        Node last = head.prev;
        Node secondLast = last.prev;
        return interpolate(x, secondLast.x, last.x, secondLast.y, last.y);
    }

    @Override
    protected double interpolate(double x, int floorIndex) {
        if (floorIndex < 0 || floorIndex >= count - 1) {
            throw new IllegalArgumentException("Invalid index");
        }
        return interpolate(x, getNode(floorIndex).x, getNode(floorIndex + 1).x,
                getNode(floorIndex).y, getNode(floorIndex + 1).y);
    }

    private double interpolate(double x, Node floorNode){
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
        if (index < 0 || index >= count) {
            throw new IllegalArgumentException("Invalid index");
        }

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
        logger.info(String.format("Добавление точки (%.3f,%.3f)", x, y));
        if (count == 0) {
            addNode(x,y);
            return;
        }
        int ifexistIndex = indexOfX(x);
        if (ifexistIndex != -1) {
            setY(ifexistIndex, y);
            logger.info(String.format("Точка (%.3f,%.3f) заменила уже существующую", x, y));
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
            logger.info(String.format("Точка (%.3f,%.3f) добавленна в самое начало", x, y));
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
        logger.info(String.format("Точка (%.3f,%.3f) дабавленна", x, y));
    }
    @Override
    public void remove(int index) {
        logger.info(String.format("Удаление точки №%d", index));
        if (index < 0 || index >= count) {
            logger.error(String.format("Точка №%d не найдена", index));
            throw new IllegalArgumentException("Invalid index");
        }

        Node currentNode = getNode(index);
        if (currentNode == head) head = currentNode.next;
        currentNode.prev.next = currentNode.next;
        currentNode.next.prev = currentNode.prev;
        count--;
        logger.info(String.format("Удаление точки №%d успешно завершено", index));
    }

    @Override
    public Iterator<Point> iterator() {
        return new Iterator<Point>() {
            private Node currentNode = head;
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < count;
            }

            @Override
            public Point next() {
                if (!hasNext()) {
                    logger.debug("Итератор дошел до конца");
                    throw new NoSuchElementException();
                }
                Point point = new Point(currentNode.x, currentNode.y);
                currentNode = currentNode.next;
                logger.info(String.format("Итератор вернул точку №%d", currentIndex));
                currentIndex++;
                return point;
            }
        };
    }


    static class Node implements Serializable{
        private static long serialVersionUID;
        public Node prev = this, next = this;
        public double x, y;

        public Node(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
