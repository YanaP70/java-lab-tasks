package task01.cow;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

public class CowList<E> extends AbstractList<E> {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    private volatile Object[] array = {};

    public Object[] getArray() {
        return array;
    }

    public void setArray(Object[] array) {
        this.array = array;
    }

    @Override
    public int size() {
        return array.length;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public int indexOf(Object o) {
        readLock.lock();
        try {
            for (int i = 0; i < size(); i++) {
                if (array[i].equals(o)) {
                    return i;
                }
            }
        } finally {
            readLock.unlock();
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        readLock.lock();
        try {
            for (int i = size() - 1; i >= 0; i--) {
                if (array[i].equals(o)) {
                    return i;
                }
            }
        } finally {
            readLock.unlock();
        }
        return -1;
    }

    @Override
    public boolean contains(Object o) {
        readLock.lock();
        boolean result;
        try {
            result = indexOf(o) >= 0;
        } finally {
            readLock.unlock();
        }
        return result;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        readLock.lock();
        boolean result;
        try {
            result = new ArrayList<>(Arrays.asList(array)).containsAll(c);
        } finally {
            readLock.unlock();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E[] toArray() {
        readLock.lock();
        E[] elements;
        try {
            elements = (E[]) Arrays.copyOf(array, array.length);
        } finally {
            readLock.unlock();
        }
        return elements;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E get(int index) {
        readLock.lock();
        E element;
        try {
            element = (E) array[index];
        } finally {
            readLock.unlock();
        }
        return element;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E set(int index, E element) {
        writeLock.lock();
        E oldElement;
        try {
            Object[] snapshot = Arrays.copyOf(array, array.length);
            oldElement = (E) snapshot[index];
            snapshot[index] = element;
            setArray(snapshot);
        } finally {
            writeLock.unlock();
        }
        return oldElement;
    }

    @Override
    public boolean add(E element) {
        writeLock.lock();
        try {
            Object[] snapshot = Arrays.copyOf(array, array.length + 1);
            snapshot[snapshot.length - 1] = element;
            setArray(snapshot);
        } finally {
            writeLock.unlock();
        }
        return true;
    }

    @Override
    public void add(int index, E element) {
        writeLock.lock();
        try {
            Object[] snapshot = new Object[size() + 1];
            System.arraycopy(array, 0, snapshot, 0, index);
            System.arraycopy(array, index, snapshot, index + 1, size() - index);
            snapshot[index] = element;
            setArray(snapshot);
        } finally {
            writeLock.unlock();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public E remove(int index) {
        writeLock.lock();
        E element;
        try {
            element = (E) getArray()[index];
            Object[] snapshot = new Object[size() - 1];
            System.arraycopy(array, 0, snapshot, 0, index);
            System.arraycopy(array, index + 1, snapshot, index, size() - 1 - index);
            setArray(snapshot);
        } finally {
            writeLock.unlock();
        }
        return element;
    }

    @Override
    public boolean remove(Object o) {
        remove(indexOf(o));
        return true;
    }

    @Override
    public void clear() {
        writeLock.lock();
        try {
            Object[] snapshot = {};
            setArray(snapshot);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        writeLock.lock();
        try {
            Object[] snapshot = new Object[size() + c.size()];
            System.arraycopy(array, 0, snapshot, 0, size());
            System.arraycopy(c.toArray(), 0, snapshot, size(), c.size());
            setArray(snapshot);
        } finally {
            writeLock.unlock();
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        writeLock.lock();
        try {
            Object[] snapshot = new Object[size() + c.size()];
            System.arraycopy(array, 0, snapshot, 0, index);
            System.arraycopy(c.toArray(), 0, snapshot, index, c.size());
            System.arraycopy(array, index, snapshot, c.size() + index, size() - index);
            setArray(snapshot);
        } finally {
            writeLock.unlock();
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        writeLock.lock();
        try {
            List<Object> list = new ArrayList<>(Arrays.asList(array));
            list.removeAll(c);
            setArray(list.toArray());
        } finally {
            writeLock.unlock();
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        writeLock.lock();
        try {
            List<Object> list = new ArrayList<>(Arrays.asList(array));
            list.retainAll(c);
            setArray(list.toArray());
        } finally {
            writeLock.unlock();
        }
        return true;
    }

    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private final Object[] snapshot = Arrays.copyOf(array, size());
            private int position = 0;

            @Override
            public boolean hasNext() {
                return position < snapshot.length;
            }

            @SuppressWarnings("unchecked")
            @Override
            public E next() {
                if (! hasNext())
                    throw new NoSuchElementException();
                return (E) snapshot[position++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void forEachRemaining(Consumer<? super E> action) {
                throw new UnsupportedOperationException();
            }
        };
    }
}
