package task01.cow;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class CowListTest {
    private CowList<Integer> cowList;

    @Before
    public void setUp() {
        cowList = new CowList<>();
        cowList.addAll(List.of(1, 2, 3, 4, 5));
    }

    @Test
    public void getArray() {
        Assert.assertArrayEquals(new Object[]{1, 2, 3, 4, 5}, cowList.getArray());
        Assert.assertEquals(5, cowList.getArray().length);
    }

    @Test
    public void setArray() {
        cowList.setArray(new Object[]{9});
        Assert.assertArrayEquals(new Object[]{9}, cowList.getArray());
        Assert.assertEquals(1, cowList.getArray().length);
    }

    @Test
    public void size() {
        Assert.assertEquals(5, cowList.size());
    }

    @Test
    public void isEmpty() {
        cowList.setArray(new Object[]{});
        Assert.assertTrue(cowList.isEmpty());
    }

    @Test
    public void indexOf() {
        Assert.assertEquals(0, cowList.indexOf(1));
    }

    @Test
    public void indexOfNotFound() {
        Assert.assertEquals(-1, cowList.indexOf(0));
    }

    @Test
    public void testLastIndexOf() {
        cowList.setArray(new Object[]{1, 5, 2, 3, 5, 4});
        Assert.assertEquals(4, cowList.lastIndexOf(5));
    }

    @Test
    public void testLastIndexOfNotFound() {
        Assert.assertEquals(-1, cowList.lastIndexOf(0));
    }

    @Test
    public void contains() {
        Assert.assertTrue(cowList.contains(1));
        Assert.assertFalse(cowList.contains(0));
    }

    @Test
    public void containsAll() {
        Assert.assertTrue(cowList.containsAll(List.of(1, 2)));
        Assert.assertFalse(cowList.containsAll(List.of(0, 1)));
    }

    @Test
    public void toArray() {
        Assert.assertArrayEquals(new Integer[]{1, 2, 3, 4, 5}, cowList.toArray());
    }

    @Test
    public void get() {
        Assert.assertEquals(Integer.valueOf(3), cowList.get(2));
    }

    @Test
    public void set() {
        cowList.set(2, -3);
        Assert.assertArrayEquals(new Integer[]{1, 2, -3, 4, 5}, cowList.getArray());
    }

    @Test
    public void add() {
        cowList.add(6);
        Assert.assertArrayEquals(new Integer[]{1, 2, 3, 4, 5, 6}, cowList.getArray());
    }

    @Test
    public void testAdd() {
        cowList.add(5, 6);
        Assert.assertArrayEquals(new Integer[]{1, 2, 3, 4, 5, 6}, cowList.getArray());
    }

    @Test
    public void remove() {
        cowList.remove(4);
        Assert.assertArrayEquals(new Integer[]{1, 2, 3, 4}, cowList.getArray());
    }

    @Test
    public void testRemove() {
        cowList.remove(Integer.valueOf(5));
        Assert.assertArrayEquals(new Integer[]{1, 2, 3, 4}, cowList.getArray());
    }

    @Test
    public void clear() {
        cowList.clear();
        Assert.assertArrayEquals(new Integer[]{}, cowList.getArray());
    }

    @Test
    public void addAll() {
        cowList.addAll(List.of(6, 7, 8, 9));
        Assert.assertArrayEquals(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9}, cowList.getArray());
    }

    @Test
    public void testAddAll() {
        cowList.addAll(3, List.of(6, 7, 8, 9));
        Assert.assertArrayEquals(new Integer[]{1, 2, 3, 6, 7, 8, 9, 4, 5}, cowList.getArray());
    }

    @Test
    public void removeAll() {
        cowList.removeAll(List.of(2, 3));
        Assert.assertArrayEquals(new Integer[]{1, 4, 5}, cowList.getArray());
    }

    @Test
    public void retainAll() {
        cowList.retainAll(List.of(2, 3));
        Assert.assertArrayEquals(new Integer[]{2, 3}, cowList.getArray());
    }

    @Test
    public void iterator() throws InterruptedException {
        Iterator<Integer> iterator = cowList.iterator();
        Thread thAdd = new Thread(() -> {
            Random rand = new Random();
            for (int i = 0; i < 10000; i++) {
                try {
                    TimeUnit.MILLISECONDS.sleep(1);
                    cowList.add(rand.nextInt(cowList.size()), i);
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

        Assert.assertArrayEquals(new Object[]{}, cowList.getArray());

        List<Integer> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        Assert.assertArrayEquals(new Object[]{1, 2, 3, 4, 5}, list.toArray());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void iteratorRemove() {
        Iterator<Integer> iterator = cowList.iterator();
        iterator.remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void iteratorForEach() {
        Iterator<Integer> iterator = cowList.iterator();
        List<Integer> list = new ArrayList<>();
        iterator.forEachRemaining(el -> list.add(el + 1));
    }

    @Test(expected = NoSuchElementException.class)
    public void iteratorNegative() {
        Iterator<Integer> iterator = cowList.iterator();
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

    @Test(expected = UnsupportedOperationException.class)
    public void testTestToArray() {
        cowList.toArray(new Integer[25]);
    }
}