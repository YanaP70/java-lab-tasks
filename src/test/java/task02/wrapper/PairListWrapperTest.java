package task02.wrapper;

import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

public class PairListWrapperTest {
    private PairListWrapper list = new PairListWrapper();

    private final SimpleModel[] arr = new SimpleModel[]{
            new SimpleModel(0),
            new SimpleModel(1),
            new SimpleModel(2),
            new SimpleModel(3),
            new SimpleModel(4),
            new SimpleModel(5),
            new SimpleModel(6),
            new SimpleModel(7)};

    private void addElements(int count) {
        for (int i = 0; i < count; i++) {
            list.add(new SimpleModel(i));
        }
    }

    @Test
    public void test() {
        list = new PairListWrapper(2);
        Assert.assertEquals(2, list.getDefaultCapacity());
    }

    @Test
    public void testGet() {
        addElements(6);
        Assert.assertEquals(new SimpleModel(4), list.get(4));
    }

    @Test
    public void testSize() {
        addElements(1);
        Assert.assertEquals(1, list.size());
        addElements(5);
        Assert.assertEquals(6, list.size());
    }

    @Test
    public void testIsEmpty() {
        Assert.assertTrue(list.isEmpty());
        addElements(1);
        Assert.assertFalse(list.isEmpty());
    }

    @Test
    public void testIndexOf() {
        addElements(1);
        addElements(5);
        Assert.assertEquals(0, list.indexOf(new SimpleModel(0)));
        Assert.assertEquals(3, list.indexOf(new SimpleModel(2)));
        Assert.assertEquals(5, list.indexOf(new SimpleModel(4)));
    }

    @Test
    public void testLastIndexOf() {
        addElements(3);
        addElements(5);
        Assert.assertEquals(3, list.lastIndexOf(new SimpleModel(0)));
        Assert.assertEquals(7, list.lastIndexOf(new SimpleModel(4)));
        Assert.assertEquals(-1, list.lastIndexOf(new SimpleModel(-4)));
    }

    @Test
    public void testContains() {
        addElements(10);
        Assert.assertTrue(list.contains(new SimpleModel(5)));
        Assert.assertFalse(list.contains(new SimpleModel(-5)));
    }

    @Test
    public void testContainsAll() {
        addElements(10);
        Assert.assertFalse(list.containsAll(List.of(new SimpleModel(5), new SimpleModel(-5), new SimpleModel(0))));
        Assert.assertTrue(list.containsAll(List.of(new SimpleModel(5), new SimpleModel(9))));
    }

    @Test(expected = NullPointerException.class)
    public void testContainsAllNull() {
        addElements(10);
        Assert.assertTrue(list.containsAll(null));
    }

    @Test
    public void testToArray() {
        Assert.assertArrayEquals(new SimpleModel[0], list.toArray());
        addElements(8);
        Assert.assertArrayEquals(arr, list.toArray());
        list = new PairListWrapper();
        addElements(1);
        SimpleModel[] ar = new SimpleModel[]{new SimpleModel(0)};
        Assert.assertArrayEquals(ar, list.toArray());
    }

    @Test
    public void testSet() {
        addElements(5);
        list.set(4, new SimpleModel(11));
        Assert.assertEquals(new SimpleModel(11), list.get(4));
    }

    @Test(expected = NoChangeUnmodifiableException.class)
    public void testSetNegative() {
        addElements(5);
        list.set(2, new SimpleModel(11));
    }

    @Test
    public void testAdd() {
        list.add(new SimpleModel(0));
        list.add(new SimpleModel(1));
        list.add(new SimpleModel(2));
        list.add(new SimpleModel(3));
        list.add(new SimpleModel(4));
        list.add(new SimpleModel(5));
        list.add(new SimpleModel(6));
        list.add(new SimpleModel(7));
        Assert.assertArrayEquals(arr, list.toArray());
    }

    @Test
    public void testTestAdd() {
        addElements(4);
        list.add(4, new SimpleModel(4));
        list.add(5, new SimpleModel(5));
        list.add(6, new SimpleModel(6));
        list.add(7, new SimpleModel(7));
        Assert.assertArrayEquals(arr, list.toArray());
    }

    @Test(expected = NoChangeUnmodifiableException.class)
    public void testTestAddNegative() {
        list.add(0, new SimpleModel(1));
    }

    @Test
    public void testAddAll() {
        list.addAll(List.of(
                new SimpleModel(0),
                new SimpleModel(1),
                new SimpleModel(2),
                new SimpleModel(3),
                new SimpleModel(4),
                new SimpleModel(5),
                new SimpleModel(6),
                new SimpleModel(7)));
        Assert.assertArrayEquals(arr, list.toArray());
    }

    @Test
    public void testTestAddAll() {
        list.add(new SimpleModel(0));
        list.add(new SimpleModel(1));
        list.add(new SimpleModel(2));
        list.add(new SimpleModel(3));
        list.add(new SimpleModel(7));
        list.addAll(4, List.of(
                new SimpleModel(4),
                new SimpleModel(5),
                new SimpleModel(6)));
        Assert.assertArrayEquals(arr, list.toArray());
    }

    @Test(expected = NoChangeUnmodifiableException.class)
    public void testTestAddAllNegative() {
        addElements(5);
        list.addAll(2, List.of(
                new SimpleModel(4),
                new SimpleModel(5),
                new SimpleModel(6)));
    }

    @Test
    public void testRemove() {
        addElements(9);
        Assert.assertEquals(new SimpleModel(8), list.remove(8));
        Assert.assertArrayEquals(arr, list.toArray());
    }

    @Test(expected = NoChangeUnmodifiableException.class)
    public void testRemoveNegative() {
        addElements(9);
        list.remove(1);
    }

    @Test
    public void testTestRemove() {
        addElements(9);
        Assert.assertTrue(list.remove(new SimpleModel(8)));
        Assert.assertFalse(list.remove(new SimpleModel(9)));
        Assert.assertArrayEquals(arr, list.toArray());
    }

    @Test(expected = NoChangeUnmodifiableException.class)
    public void testTestRemoveNegative() {
        addElements(9);
        list.remove(new SimpleModel(1));
    }

    @Test
    public void testClear() {
        addElements(1);
        list.clear();
        SimpleModel[] ar = new SimpleModel[]{new SimpleModel(1)};
        Assert.assertEquals(1, list.size());
        list.toArray();
    }

    @Test
    public void testRemoveAll() {
        addElements(8);
        SimpleModel[] ar = new SimpleModel[]{
                new SimpleModel(0),
                new SimpleModel(1),
                new SimpleModel(2),
                new SimpleModel(3),
                new SimpleModel(5)
        };
        list.removeAll(List.of(new SimpleModel(4), new SimpleModel(6), new SimpleModel(7)));
        Assert.assertArrayEquals(ar, list.toArray());
        list.removeAll(List.of(new SimpleModel(0), new SimpleModel(1), new SimpleModel(2), new SimpleModel(3)));
        Assert.assertArrayEquals(ar, list.toArray());
    }

    @Test
    public void testRetainAll() {
        addElements(8);
        SimpleModel[] ar = new SimpleModel[]{
                new SimpleModel(0),
                new SimpleModel(1),
                new SimpleModel(2),
                new SimpleModel(3),
                new SimpleModel(4),
                new SimpleModel(5),
                new SimpleModel(7)
        };
        list.retainAll(List.of(new SimpleModel(4), new SimpleModel(5), new SimpleModel(7)));
        Assert.assertArrayEquals(ar, list.toArray());
    }

    @Test
    public void testTestToArray() {
        addElements(8);
        Assert.assertArrayEquals(arr, list.toArray(new SimpleModel[8]));
        Assert.assertArrayEquals(arr, list.toArray(new SimpleModel[1]));
        Assert.assertArrayEquals(arr, list.toArray(new SimpleModel[88]));
    }

    @Test
    public void testTestIterator() {
        SimpleModel[] ar = new SimpleModel[]{
                new SimpleModel(0),
                new SimpleModel(1),
                new SimpleModel(2),
                new SimpleModel(3)};

        addElements(8);

        Iterator<SimpleModel> it = list.iterator();
        it.next();
        it.next();
        it.next();
        it.next();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
        Assert.assertArrayEquals(ar, list.toArray());
    }

    @Test
    public void testIterator() {
        addElements(8);
        Iterator<SimpleModel> it = list.iterator();
        IntStream.range(0, 8).forEach(el -> it.next());
        Assert.assertFalse(it.hasNext());
    }

    @Test(expected = NoChangeUnmodifiableException.class)
    public void testIteratorNegative() {
        addElements(8);
        Iterator<SimpleModel> it = list.iterator();
        it.next();
        it.remove();
    }
}