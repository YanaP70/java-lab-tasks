package task01.cow;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CowList extends AbstractList<Model> {
    private final ReentrantLock lock = new ReentrantLock();

    private volatile Model[] array = {};

    public CowList() {
    }

    public CowList(Model[] array) {
        this.array = array;
    }

    private void setArray(Model[] array) {
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
        for (int i = 0; i < size(); i++) {
            if (isSame(o, array[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size() - 1; i >= 0; i--) {
            if (isSame(o, array[i])) {
                return i;
            }
        }
        return -1;
    }

    private boolean isSame(Object o, Model obj) {
        return Objects.isNull(obj) && Objects.isNull(o) || obj.equals(o);
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        Objects.requireNonNull(c);
        return c.stream().allMatch(this::contains);
    }

    @Override
    public Model[] toArray() {
        return Arrays.copyOf(array, array.length);
    }

    @Override
    public Model get(int index) {
        return array[index];
    }

    @Override
    public Model set(int index, Model element) {
        lock.lock();
        try {
            Model[] snapshot = Arrays.copyOf(array, array.length);
            Model oldElement = snapshot[index];
            snapshot[index] = element;
            setArray(snapshot);
            return oldElement;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean add(Model element) {
        lock.lock();
        try {
            setArray(addElement(size(), element));
            return true;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void add(int index, Model element) {
        lock.lock();
        try {
            setArray(addElement(index, element));
        } finally {
            lock.unlock();
        }
    }

    private Model[] addElement(int index, Model element) {
        Model[] snapshot = new Model[size() + 1];
        System.arraycopy(array, 0, snapshot, 0, index);
        System.arraycopy(array, index, snapshot, index + 1, size() - index);
        snapshot[index] = element;
        return snapshot;
    }

    @Override
    public Model remove(int index) {
        lock.lock();
        try {
            Model element = array[index];
            Model[] snapshot = new Model[size() - 1];
            System.arraycopy(array, 0, snapshot, 0, index);
            System.arraycopy(array, index + 1, snapshot, index, size() - 1 - index);
            setArray(snapshot);
            return element;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean remove(Object o) {
        Objects.requireNonNull(o);
        remove(indexOf(o));
        return true;
    }

    @Override
    public void clear() {
        lock.lock();
        try {
            setArray(new Model[0]);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean addAll(Collection<? extends Model> c) {
        Objects.requireNonNull(c);
        lock.lock();
        try {
            setArray(addAllElements(size(), c));
            return true;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends Model> c) {
        Objects.requireNonNull(c);
        lock.lock();
        try {
            setArray(addAllElements(index, c));
            return true;
        } finally {
            lock.unlock();
        }
    }

    private Model[] addAllElements(int index, Collection<? extends Model> c) {
        Model[] snapshot = new Model[size() + c.size()];
        Model[] collection = Arrays.copyOf(c.toArray(), c.size(), Model[].class);

        System.arraycopy(array, 0, snapshot, 0, index);
        System.arraycopy(collection, 0, snapshot, index, c.size());
        System.arraycopy(array, index, snapshot, c.size() + index, size() - index);
        return snapshot;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        lock.lock();
        try {
            Predicate<Model> p = c::contains;
            setArray(removeElements(p.negate()));
            return true;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        lock.lock();
        try {
            Predicate<Model> p = c::contains;
            setArray(removeElements(p));
            return true;
        } finally {
            lock.unlock();
        }
    }

    private Model[] removeElements(Predicate<Model> predicate) {
        Model[] snapshot = new Model[size()];
        Iterator<Integer> in = iterator(predicate);
        int prev = 0;
        int last = 0;
        while (in.hasNext()) {
            int next = in.next();
            System.arraycopy(array, prev, snapshot, last, next - prev);
            last += next - prev;
            prev = next + 1;
        }
        System.arraycopy(array, prev, snapshot, last, size() - prev);
        last = last + size() - prev;

        Model[] newarray = new Model[last];
        System.arraycopy(snapshot, 0, newarray, 0, last);
        return newarray;
    }

    private Iterator<Integer> iterator(Predicate<Model> predicate) {
        return new Iterator<>() {
            private final Model[] snapshot = array;
            private int position = 0;

            @Override
            public boolean hasNext() {
                while (position < size() && predicate.test(snapshot[position])) {
                    position++;
                }
                return position < size();
            }

            @Override
            public Integer next() {
                if (position >= size()) {
                    throw new NoSuchElementException();
                }
                return position++;
            }
        };
    }

    @SuppressWarnings({"unchecked", "SuspiciousSystemArraycopy"})
    @Override
    public <T> T[] toArray(T[] a) {
        if (Objects.isNull(a) || a.length != size()) {
            a = (T[]) Arrays.copyOf(array, array.length, a.getClass());
        } else {
            System.arraycopy(array, 0, a, 0, size());
        }
        return a;
    }

    @Override
    public ListIterator<Model> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<Model> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Model> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Model> iterator() {
        return new Iterator<>() {
            private final Model[] snapshot = array;
            private int position = 0;

            @Override
            public boolean hasNext() {
                return position < snapshot.length;
            }

            @Override
            public Model next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                return snapshot[position++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void forEachRemaining(Consumer<? super Model> action) {
                throw new UnsupportedOperationException();
            }
        };
    }
}

