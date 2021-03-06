package generics;

import java.util.*;

public class SimpleArray<T> implements Iterable<T> {
    private Object[] array;
    private int count = 0;

    private SimpleArray(int num) {
        this.array = new Objects[num];
    }

    private void add(T model) {
        array[count++] = model;
    }

    private void set(int index, T model) {
        Objects.checkIndex(index, count);
        array[index] = model;
    }

    private void remove(int index) {
        Objects.checkIndex(index, count);
        array[index] = null;
        System.arraycopy(array, index + 1, array, index, array.length - index - 1);
        count--;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iter();
    }

    class Iter implements Iterator<T> {
        int counter;

        @Override
        public boolean hasNext() {
            return this.counter < count;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return (T) array[counter++];
        }
    }
}
