package task01.cow;


import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class CowListTest {

    private final Collection<Model> models = List.of(
            new Model(2, "2"),
            new Model(3, "3"),
            new Model(5, "5"));

    private final Model[] arr = {
            new Model(1, "1"),
            new Model(2, "2"),
            new Model(3, "3"),
            new Model(4, "4"),
            new Model(5, "5"),
            new Model(6, "6")};

    private CowList cowList = new CowList(arr);

    @Test
    public void size() {
        Assert.assertEquals(6, cowList.size());
    }

    @Test
    public void isEmpty() {
        cowList = new CowList();
        Assert.assertTrue(cowList.isEmpty());

        cowList = new CowList(new Model[0]);
        Assert.assertTrue(cowList.isEmpty());
    }

    @Test
    public void indexOf() {
        cowList.add(new Model(6, "6"));
        Assert.assertEquals(5, cowList.indexOf(new Model(6, "6")));
    }

    @Test
    public void indexOfNotFound() {
        Assert.assertEquals(-1, cowList.indexOf(new Model(9, "9")));
    }

    @Test
    public void testLastIndexOf() {
        cowList.add(new Model(6, "6"));
        Assert.assertEquals(6, cowList.lastIndexOf(new Model(6, "6")));
    }

    @Test
    public void testLastIndexOfNotFound() {
        Assert.assertEquals(-1, cowList.lastIndexOf(new Model(7, "7")));
    }

    @Test
    public void contains() {
        Assert.assertTrue(cowList.contains(new Model(5, "5")));
        Assert.assertFalse(cowList.contains(new Model(9, "9")));
    }

    @Test
    public void containsAll() {
        Assert.assertTrue(cowList.containsAll(List.of(new Model(5, "5"), new Model(6, "6"))));
        Assert.assertFalse(cowList.containsAll(List.of(new Model(7, "7"), new Model(6, "6"))));
        Assert.assertFalse(cowList.containsAll(List.of(new Model(7, "7"), new Model(8, "8"))));
    }

    @Test(expected = NullPointerException.class)
    public void containsAllNegative() {
        Assert.assertFalse(cowList.containsAll(null));
    }

    @Test
    public void toArray() {
        Assert.assertArrayEquals(arr, cowList.toArray());
    }

    @Test
    public void get() {
        Assert.assertEquals(new Model(3, "3"), cowList.get(2));
    }

    @Test
    public void set() {
        cowList.set(2, new Model(22, "22"));
        Assert.assertEquals(new Model(22, "22"), cowList.get(2));
    }

    @Test
    public void add() {
        cowList = new CowList();
        Assert.assertTrue(cowList.add(new Model(1, "1")));
        Assert.assertTrue(cowList.add(new Model(2, "2")));
        Assert.assertTrue(cowList.add(new Model(3, "3")));
        Assert.assertTrue(cowList.add(new Model(4, "4")));
        Assert.assertTrue(cowList.add(new Model(5, "5")));
        Assert.assertTrue(cowList.add(new Model(6, "6")));
        Assert.assertArrayEquals(arr, cowList.toArray());
    }

    @Test
    public void testAdd() {
        cowList = new CowList();
        cowList.add(0, new Model(6, "6"));
        cowList.add(0, new Model(5, "5"));
        cowList.add(0, new Model(4, "4"));
        cowList.add(0, new Model(3, "3"));
        cowList.add(0, new Model(2, "2"));
        cowList.add(0, new Model(1, "1"));
        Assert.assertArrayEquals(arr, cowList.toArray());
    }

    @Test
    public void remove() {
        Assert.assertTrue(cowList.remove(new Model(1, "1")));
        Assert.assertTrue(cowList.remove(new Model(2, "2")));
        Assert.assertTrue(cowList.remove(new Model(3, "3")));
        Assert.assertTrue(cowList.remove(new Model(4, "4")));
        Assert.assertTrue(cowList.remove(new Model(5, "5")));
        Assert.assertArrayEquals(new Model[]{new Model(6, "6")}, cowList.toArray());
    }

    @Test
    public void testRemove() {
        Assert.assertEquals(new Model(5, "5"), cowList.remove(4));
        Assert.assertEquals(new Model(4, "4"), cowList.remove(3));
        Assert.assertEquals(new Model(3, "3"), cowList.remove(2));
        Assert.assertEquals(new Model(2, "2"), cowList.remove(1));
        Assert.assertEquals(new Model(1, "1"), cowList.remove(0));
        Assert.assertArrayEquals(new Model[]{new Model(6, "6")}, cowList.toArray());
    }

    @Test
    public void clear() {
        cowList.clear();
        Assert.assertArrayEquals(new Model[]{}, cowList.toArray());
    }

    @Test
    public void addAll() {
        cowList = new CowList();
        cowList.addAll(Arrays.asList(arr));
        Assert.assertArrayEquals(arr, cowList.toArray());
    }

    @Test
    public void testAddAll() {
        cowList = new CowList();
        cowList.addAll(0, List.of(new Model(1, "1")));
        cowList.addAll(1, models);
        cowList.addAll(3,List.of(new Model(4, "4")));
        cowList.addAll(5, List.of(new Model(6, "6")));
        Assert.assertArrayEquals(arr, cowList.toArray());
    }

    @Test
    public void removeAll() {
        cowList.removeAll(models);
        Assert.assertArrayEquals(new Model[]{new Model(1, "1"), new Model(4, "4"), new Model(6, "6")}, cowList.toArray());
        cowList.removeAll(List.of(new Model(1, "1")));
        Assert.assertArrayEquals(new Model[]{new Model(4, "4"), new Model(6, "6")}, cowList.toArray());
        cowList.removeAll(List.of(new Model(4, "4")));
        Assert.assertArrayEquals(new Model[]{new Model(6, "6")}, cowList.toArray());
    }

    @Test
    public void retainAll() {
        cowList.retainAll(models);
        Assert.assertArrayEquals(models.toArray(), cowList.toArray());
    }

    @Test
    public void iterator() throws InterruptedException {

        Iterator<Model> iterator = cowList.iterator();
        Thread thAdd = new Thread(() -> {
            Random rand = new Random();
            for (int i = 0; i < 10000; i++) {
                try {
                    TimeUnit.MILLISECONDS.sleep(1);
                    int next = rand.nextInt(cowList.size());
                    cowList.add(next, new Model(next, String.valueOf(next)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thRem = new Thread(() -> {
            Random rand = new Random();
            try {
                while (cowList.size() > 0) {
                    TimeUnit.MILLISECONDS.sleep(2);
                    cowList.remove(rand.nextInt(cowList.size()));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thAdd.start();
        thRem.start();
        thAdd.join();
        thRem.join();

        Assert.assertArrayEquals(new Model[]{}, cowList.toArray());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void iteratorRemove() {
        Iterator<Model> iterator = cowList.iterator();
        iterator.remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void iteratorForEach() {
        Iterator<Model> iterator = cowList.iterator();
        List<Model> list = new ArrayList<>();
        iterator.forEachRemaining(list::add);
    }

    @Test(expected = NoSuchElementException.class)
    public void iteratorNegative() {
        Iterator<Model> iterator = cowList.iterator();
        while (iterator.hasNext()) {
            iterator.next();
        }
        iterator.next();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testListIterator() {
        cowList.listIterator();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTestListIterator() {
        cowList.listIterator(2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSubList() {
        cowList.subList(1, 3);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveRange() {
        cowList.removeRange(1, 3);
    }

    @Test(expected = ArrayStoreException.class)
    public void testToArrayNegative() {
        cowList.toArray(new Integer[25]);
    }

    @Test
    public void testToArray() {
        Assert.assertArrayEquals(arr, cowList.toArray(new Model[arr.length -1]));
        Assert.assertArrayEquals(arr, cowList.toArray(new Model[arr.length]));
        Assert.assertArrayEquals(arr, cowList.toArray(new Model[arr.length + 1]));
    }
}