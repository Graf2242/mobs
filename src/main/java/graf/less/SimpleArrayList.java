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
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if ((values[i]) == null) {
                    return true;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(values[i])) {
                    return true;
                }
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
            throw new IndexOutOfBoundsException();
        }
        values[size++] = o;
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof List)) {
            return false;
        }
        Iterator<T> iterator = iterator();
        Iterator<?> iterator1 = ((List) obj).listIterator();
        while (iterator.hasNext() && iterator1.hasNext()) {
            T o1 = iterator.next();
            Object o2 = iterator1.next();
            if (!(o1 == null ? o2 == null : o1.equals(o2))) {
                return false;
            }
        }
        return !(iterator.hasNext() || iterator1.hasNext());
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (values[i] == null) {
                    System.arraycopy(values, i + 1, values, i, values.length - 1 - i);
                    values[--size] = null;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (Objects.equals(o, values[i])) {
                    System.arraycopy(values, i + 1, values, i, values.length - 1 - i);
                    values[--size] = null;
                }
            }
        }
        return false;
    }

    @Override
    public boolean addAll(Collection c) {
        if (size + c.size() > values.length) {
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
        if (!(size < values.length) || index > size) {
            throw new IndexOutOfBoundsException();
        }
        System.arraycopy(values, index, values, index + 1, size - index);
        values[index] = element;
        size++;
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
            for (int i = 0; i < size; i++) {
                if (values[i] == null) {
                    return i;
                }
            }
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
            for (int i = size - 1; i > 0; i--) {
                if (values[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = size - 1; i > 0; i--) {
                if (Objects.equals(o, values[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ListIterator listIterator() {
        return new ListItr();
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
            nextIndex = lastRet;
            lastRet = -1;
        }
    }

    private class ListItr implements ListIterator {
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
            nextIndex = lastRet;
            lastRet = -1;
        }

        @Override
        public boolean hasPrevious() {
            return nextIndex != 0;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Object previous() {
            int i = nextIndex - 1;
            if (i < 0)
                throw new NoSuchElementException();
            Object[] values = SimpleArrayList.this.values;
            if (i >= values.length)
                throw new ConcurrentModificationException();
            nextIndex = i;
            return values[lastRet = i];
        }

        @Override
        public int nextIndex() {
            return nextIndex;
        }

        @Override
        public int previousIndex() {
            return nextIndex - 1;
        }

        @Override
        public void set(Object o) {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            try {
                SimpleArrayList.this.set(lastRet, o);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void add(Object o) {
            try {
                int i = nextIndex;
                SimpleArrayList.this.add(i, o);
                nextIndex = i + 1;
                lastRet = -1;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }
}
