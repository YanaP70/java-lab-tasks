package task02.wrapper;

import java.util.*;
import java.util.stream.IntStream;

public class PairListWrapper extends AbstractList<SimpleModel> {
    private int defaultCapacity = 4;

    private SimpleModel[] head = new SimpleModel[0];
    private SimpleModel[] tail = new SimpleModel[0];

    public PairListWrapper() {
    }

    public PairListWrapper(int defaultCapacity) {
        this.defaultCapacity = defaultCapacity;
    }

    public int getDefaultCapacity() {
        return defaultCapacity;
    }

    @Override
    public SimpleModel get(int index) {
        checkIndexBounds(index);
        return getElementByIndex(index);
    }

    private SimpleModel[] getContainerByIndex(int index) {
        return index < defaultCapacity ? head : tail;
    }

    @Override
    public int size() {
        return head.length + tail.length;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public Iterator<SimpleModel> iterator() {
        return new Iterator<>() {
            private int position = 0;
            private int prev = -1;

            @Override
            public boolean hasNext() {
                return position < size();
            }

            @Override
            public SimpleModel next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                SimpleModel result = getElementByIndex(position);
                prev = position;
                position++;
                return result;
            }

            @Override
            public void remove() {
                if (prev < 0) {
                    throw new IllegalStateException();
                }
                if (prev < defaultCapacity) {
                    throw new NoChangeUnmodifiableException();
                }
                PairListWrapper.this.remove(prev);
                if (prev < position) {
                    position--;
                    prev = -1;
                }
            }
        };
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size(); i++) {
            if (isSame(o, getElementByIndex(i))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size() - 1; i >= 0; i--) {
            if (isSame(o, getElementByIndex(i))) {
                return i;
            }
        }
        return -1;
    }

    private SimpleModel getElementByIndex(int index) {
        return index < defaultCapacity ? head[index] : tail[index - defaultCapacity];
    }

    private boolean isSame(Object o, SimpleModel obj) {
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
    public SimpleModel[] toArray() {
        SimpleModel[] array = new SimpleModel[size()];
        System.arraycopy(head, 0, array, 0, head.length);
        System.arraycopy(tail, 0, array, head.length, tail.length);
        return array;
    }

    @Override
    public SimpleModel set(int index, SimpleModel element) {
        checkIndexBounds(index);
        checkToChange(index);

        int ind = correctIndex(index);
        SimpleModel oldElement = tail[ind];
        tail[ind] = element;
        return oldElement;
    }

    private void checkIndexBounds(int index) {
        if (index > size() || index < 0)
            throw new IndexOutOfBoundsException();
    }

    private void checkToChange(int index) {
        if (index < defaultCapacity)
            throw new NoChangeUnmodifiableException();
    }

    @Override
    public void add(int index, SimpleModel element) {
        checkIndexBounds(index);
        checkToChange(index);

        SimpleModel[] array = addElement(index, element);
        tail = Arrays.copyOf(array, array.length);
    }

    @Override
    public boolean add(SimpleModel element) {
        int index = size();
        SimpleModel[] array = addElement(index, element);
        setArray(array, index);
        return true;
    }

    private SimpleModel[] addElement(int index, SimpleModel element) {
        SimpleModel[] container = getContainerByIndex(index);
        int ind = correctIndex(index);

        SimpleModel[] array = new SimpleModel[container.length + 1];
        System.arraycopy(container, 0, array, 0, ind);
        System.arraycopy(container, ind, array, ind + 1, container.length - ind);
        array[ind] = element;
        return array;
    }

    private int correctIndex(int index) {
        return index < defaultCapacity ? index : index - defaultCapacity;
    }

    private void setArray(SimpleModel[] array, int index) {
        if (index < defaultCapacity) {
            head = Arrays.copyOf(array, array.length);
        } else {
            tail = Arrays.copyOf(array, array.length);
        }
    }

    @Override
    public boolean addAll(Collection<? extends SimpleModel> c) {
        Objects.requireNonNull(c);
        c.forEach(this::add);
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends SimpleModel> c) {
        Objects.requireNonNull(c);
        int ind = index;
        for (SimpleModel element : c) {
            add(ind++, element);
        }
        return true;
    }

    @Override
    public SimpleModel remove(int index) {
        checkIndexBounds(index);
        checkToChange(index);
        return removeElement(index);
    }

    private SimpleModel removeElement(int index) {
        int ind = correctIndex(index);

        SimpleModel element = tail[ind];
        SimpleModel[] array = new SimpleModel[tail.length - 1];
        System.arraycopy(tail, 0, array, 0, ind);
        System.arraycopy(tail, ind + 1, array, ind, tail.length - 1 - ind);
        tail = Arrays.copyOf(array, array.length);
        return element;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index == -1) {
            return false;
        }
        remove(index);
        return true;
    }

    @Override
    public void clear() {
        tail = new SimpleModel[0];
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        for (Object obj : c) {
            int index = indexOf(obj);
            if (index >= defaultCapacity) {
                remove(index);
            }
        }
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        Iterator<SimpleModel> iterator = iterator();
        IntStream.range(0, defaultCapacity).forEach(el -> iterator.next());
        while (iterator.hasNext()) {
            SimpleModel model = iterator.next();
            if (!c.contains(model)) {
                iterator.remove();
            }
        }
        return true;
    }

    @SuppressWarnings({"unchecked", "SuspiciousSystemArraycopy"})
    @Override
    public <T> T[] toArray(T[] a) {
        int len = size();
        if (Objects.isNull(a) || a.length != len) {
            a = (T[]) Arrays.copyOf(head, len, a.getClass());
            System.arraycopy(tail, 0, a, head.length, len - head.length);
        } else {
            System.arraycopy(head, 0, a, 0, head.length);
            System.arraycopy(tail, 0, a, head.length, len - head.length);
        }
        return a;
    }
}
