package graf.less;

import java.util.*;

public class SimpleArrayList<T> implements List<T> {

    private Object[] values;
    private Integer size;

    public SimpleArrayList(int maxSize) {
        this.values = new Object[maxSize];
        this.size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size.equals(0);
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (o.equals(values[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new Itr();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(values, size);
    }

    @Override
    public boolean add(Object o) {
        if (!(size < values.length)) {
            throw new IllegalStateException();
        }
        values[size++] = o;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            throw new UnsupportedOperationException();
        }
        for (int i = 0; i < values.length; i++) {
            if (Objects.equals(o, values[i])) {
                for (int j = i; j < values.length - 1; j++) {
                    values[j] = values[j + 1];
                    values[--size] = null;
                }
            }
        }
        return false;
    }

    @Override
    public boolean addAll(Collection c) {
        if (!(size + c.size() < values.length)) {
            throw new IllegalStateException();
        }
        for (Object o : c) {
            values[size++] = o;
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        int i = 0;
        for (T t : c) {
            add(index + (i++), t);
        }
        return true;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            values[i] = null;
        }
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private T values(int index) {
        return (T) values[index];
    }

    @Override
    public T get(int index) {
        return values(index);
    }

    @Override
    public T set(int index, Object element) {
        if (index > size) {
            throw new IndexOutOfBoundsException();
        }
        T oldValue = values(index);
        values[index] = element;
        return oldValue;
    }

    @Override
    public void add(int index, Object element) {
        if (size + 1 > values.length || index > size) {
            throw new IndexOutOfBoundsException();
        }
        System.arraycopy(values, size + 1 - 1, values, size + 1, index + 1 - (size + 1));
        values[index] = element;
    }

    @Override
    public T remove(int index) {
        if (index > size) {
            throw new IndexOutOfBoundsException();
        }
        T oldElement = values(index);
        System.arraycopy(values, index + 1, values, index, size - index - 1);
        values[(size--) - 1] = null;
        return oldElement;
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            throw new UnsupportedOperationException();
        }
        for (int i = 0; i < size; i++) {
            if (Objects.equals(o, values[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            throw new UnsupportedOperationException();
        }
        for (int i = size; i > 0; i--) {
            if (Objects.equals(o, values[i])) {
                return i;
            }
        }
        return -1;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ListIterator listIterator() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    @Override
    public ListIterator listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection c) {
        for (Object o : c) {
            remove(o);
        }
        return true;
    }

    @Override
    public boolean containsAll(Collection c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object[] toArray(Object[] a) {
        throw new UnsupportedOperationException();
    }

    private class Itr implements Iterator<T> {
        int nextIndex = 0;
        int lastRet = -1;

        @Override
        public boolean hasNext() {
            return nextIndex < size;
        }

        @Override
        public T next() {
            lastRet = nextIndex;
            return values(nextIndex++);
        }

        @Override
        public void remove() {
            SimpleArrayList.this.remove(lastRet);
        }
    }
}
