package collection;

import java.util.*;

public class SimpleArray<T> implements Iterable<T> {
    private Object[] array;
    private int count = 0;
    private int modCount = 0;

    public SimpleArray() {
        this.array = new Object[10];
    }

    public T get(int index) {
        Objects.checkIndex(index, count);
        return (T) this.array[index];
    }

    public void add(T model) {
        if (count == array.length) {
            array = Arrays.copyOf(array, array.length + (array.length >> 1));
        }
        array[count++] = model;
        modCount++;
    }

    @Override
    public Iterator<T> iterator() throws ConcurrentModificationException, NoSuchElementException {
        return new Iter();
    }

    public class Iter implements Iterator<T> {
        int counter;
        int expectedModCount;

        public Iter() {
            this.expectedModCount = modCount;
        }

        @Override
        public boolean hasNext() {
            return this.counter < count;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return (T) array[counter++];
        }
    }
}
