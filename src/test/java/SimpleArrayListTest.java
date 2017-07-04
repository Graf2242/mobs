import graf.less.SimpleArrayList;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SimpleArrayListTest {

    @Test
    public void removeObjectTest() {
        List<Integer> list = new SimpleArrayList<>(10);
        List<Integer> list1 = new SimpleArrayList<>(10);

        Integer i = 1;
        Integer j = 10;

        list.add(i);
        list.add(j);
        list.add(i);

        list1.add(i);
        list1.add(i);

        list.remove(j);

        Assert.assertEquals(list, list1);
    }

    @Test
    public void removeObjectNewTest() {
        List<Integer> list = new SimpleArrayList<>(10);
        List<Integer> list1 = new SimpleArrayList<>(10);

        Integer i = 1;
        Integer j = 10;

        list.add(i);
        list.add(j);

        list1.add(i);

        list.remove(new Integer(10));

        Assert.assertEquals(list, list1);
    }

    @Test
    public void removeObjectNullTest() {
        List<Integer> list = new SimpleArrayList<>(10);
        List<Integer> list1 = new SimpleArrayList<>(10);

        Integer i = 1;

        list.add(i);
        list.add(null);

        list1.add(i);

        list.remove(null);

        Assert.assertEquals(list, list1);
    }

    @Test
    public void removeObjectNotContainedTest() {
        List<Integer> list = new SimpleArrayList<>(10);
        List<Integer> list1 = new SimpleArrayList<>(10);

        Integer i = 1;
        Integer j = 10;

        list.add(i);
        list.add(i);

        list1.add(i);
        list1.add(i);

        list.remove(j);

        Assert.assertEquals(list, list1);
    }

    @Test
    public void sizeTest() {
        List<Integer> list = new SimpleArrayList<>(5);

        list.add(1);
        list.add(2);

        Assert.assertEquals(2, list.size());
    }

    @Test
    public void isEmptyTest() {
        List<Integer> list = new SimpleArrayList<>(5);

        Assert.assertEquals(true, list.isEmpty());

        list.add(1);
        list.remove(new Integer(1));
        Assert.assertEquals(true, list.isEmpty());

        list.add(1);
        list.add(2);
        list.remove(0);
        list.remove(0);
        Assert.assertEquals(true, list.isEmpty());

        list.add(1);
        Assert.assertEquals(false, list.isEmpty());
    }

    @Test
    public void containsTest() {
        List<Integer> list = new SimpleArrayList<>(5);
        list.add(1);
        Assert.assertEquals(list.contains(1), true);
        Assert.assertEquals(list.contains(2), false);

        list = new SimpleArrayList<>(5);
        list.add(1);
        Assert.assertEquals(list.contains(null), false);

        list.add(null);
        Assert.assertEquals(list.contains(null), true);
    }

    @Test
    public void iteratorTest() {
        List<Integer> list = new SimpleArrayList<>(5);
        Iterator<Integer> iterator = list.iterator();
        Assert.assertEquals(iterator.hasNext(), false);

        list.add(0);
        iterator = list.iterator();
        Assert.assertEquals(iterator.hasNext(), true);

        list.add(1);
        list.add(2);
        list.add(3);
        iterator = list.iterator();
        List<Integer> list1 = new ArrayList<>();
        while (iterator.hasNext()) {
            list1.add(iterator.next());
        }

        Assert.assertEquals(list1, list);

        iterator = list.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
        Assert.assertEquals(list.isEmpty(), true);

        list = new SimpleArrayList<>(5);
        list.add(null);
        list.add(1);

        iterator = list.iterator();
        list1 = new ArrayList<>();
        while (iterator.hasNext()) {
            list1.add(iterator.next());
        }
        Assert.assertEquals(list, list1);
    }

    @Test
    public void toArrayTest() {
        List<Integer> list = new SimpleArrayList<>(5);

        list.add(1);
        list.add(2);
        list.add(3);
        Object[] array;
        array = list.toArray();
        Assert.assertEquals(array.length, list.size());
        for (int i = 0; i < array.length; i++) {
            Assert.assertEquals(array[i], list.get(i));
        }
    }

    @Test
    public void addTest() {
        List<Integer> list = new SimpleArrayList<>(2);
        list.add(1);
        Assert.assertEquals(list.contains(1), true);
        Assert.assertEquals(list.contains(null), false);
        list.add(null);
        Assert.assertEquals(list.contains(1), true);
        Assert.assertEquals(list.contains(null), true);

        try {
            list.add(5);
            Assert.assertEquals(1, 0);
        } catch (Exception ignored) {
        }
    }

    @Test
    public void equalsTest() {
        List<Integer> list = new SimpleArrayList<>(5);
        List<Integer> list1 = new SimpleArrayList<>(5);

        list.add(0);
        list.add(1);
        list1.add(0);
        list1.add(1);

        Assert.assertEquals(list, list1);

        list.add(null);
        list.add(1);
        list1.add(null);
        list1.add(1);

        Assert.assertEquals(list, list1);
    }

    @Test
    public void addAllTest() {
        List<Integer> list = new SimpleArrayList<>(5);
        List<Integer> list1 = new SimpleArrayList<>(5);
        List<Integer> list2 = new ArrayList<>();

        list.add(1);
        list.add(2);
        list1.add(1);
        list1.add(2);

        list2.add(6);
        list2.add(7);
        list1.add(6);
        list1.add(7);
        list1.add(null);
        list2.add(null);

        list.addAll(list2);

        Assert.assertEquals(list1, list);
    }

    @Test
    public void addAllIndexText() {
        List<Integer> list = new SimpleArrayList<>(5);
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();

        list.add(1);
        list1.add(1);

        list2.add(null);
        list2.add(3);
        list.add(4);
        list1.add(null);
        list1.add(3);
        list1.add(4);

        list.addAll(1, list2);
        Assert.assertEquals(list1, list);
    }

    @Test
    public void clearTest() {
        List<Integer> list = new SimpleArrayList<>(5);
        List<Integer> list1 = new SimpleArrayList<>(5);

        list.add(1);
        list.add(null);
        list.clear();

        Assert.assertEquals(list, list1);
    }

    @Test
    public void getTest() {
        List<Integer> list = new SimpleArrayList<>(5);
        list.add(1);
        list.add(null);
        list.add(3);

        Assert.assertEquals(list.get(0).intValue(), 1);
        Assert.assertEquals(list.get(1), null);
        Assert.assertEquals(list.get(2).intValue(), 3);
    }

    @Test
    public void setTest() {
        List<Integer> list = new SimpleArrayList<>(5);
        List<Integer> list1 = new SimpleArrayList<>(5);

        list.add(null);
        list.add(2);
        list.add(3);
        list1.add(null);
        list1.add(4);
        list1.add(3);
        list.set(1, 4);
        Assert.assertEquals(list, list1);
    }

    @Test
    public void addIndexTest() {
        List<Integer> list = new SimpleArrayList<>(5);
        List<Integer> list1 = new SimpleArrayList<>(5);
        list.add(null);
        list.add(3);
        list.add(1, 2);
        list1.add(null);
        list1.add(2);
        list1.add(3);
        Assert.assertEquals(list, list1);

        list1.add(null);
        list1.add(5);
        list.add(5);
        list.add(3, null);
        Assert.assertEquals(list, list1);
    }

    @Test
    public void removeIndexTest() {
        List<Integer> list = new SimpleArrayList<>(10);
        List<Integer> list1 = new SimpleArrayList<>(10);

        Integer i = 1;
        Integer j = 10;

        list.add(i);
        list.add(j);
        list.add(i);

        list1.add(i);
        list1.add(i);

        int k = 1;
        list.remove(k);

        Assert.assertEquals(list, list1);

        k = -1;
        try {
            list.remove(k);
            Assert.assertEquals(1, 0);
        } catch (Exception ignored) {

        }
    }

    @Test
    public void indexOfTest() {
        List<Integer> list = new SimpleArrayList<>(6);

        list.add(1);
        list.add(2);
        list.add(null);
        list.add(3);
        list.add(null);
        list.add(1);

        Assert.assertEquals(list.indexOf(null), 2);
        Assert.assertEquals(list.indexOf(1), 0);
        Assert.assertEquals(list.indexOf(5), -1);

        Assert.assertEquals(list.lastIndexOf(null), 4);
        Assert.assertEquals(list.lastIndexOf(1), 5);
        Assert.assertEquals(list.lastIndexOf(5), -1);
    }

    @Test
    public void listIteratorTest() {
        List<Integer> list = new SimpleArrayList<>(5);
        ListIterator<Integer> iterator = list.listIterator();
        //hasNext
        Assert.assertEquals(iterator.hasNext(), false);

        list.add(0);
        iterator = list.listIterator();
        Assert.assertEquals(iterator.hasNext(), true);

        iterator.next();
        Assert.assertEquals(iterator.hasNext(), false);
        //next
        list = new SimpleArrayList<>(5);
        List<Integer> list1 = new SimpleArrayList<>(5);

        list.add(0);
        list.add(null);
        list.add(2);
        iterator = list.listIterator();
        while (iterator.hasNext()) {
            list1.add(iterator.next());
        }
        Assert.assertEquals(list, list1);
        //remove
        iterator = list.listIterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
        Assert.assertEquals(list.isEmpty(), true);
        //hasPrevious
        list.add(0);
        list.add(null);
        list.add(2);
        iterator = list.listIterator();

        Assert.assertEquals(iterator.hasPrevious(), false);
        iterator.next();
        Assert.assertEquals(iterator.hasPrevious(), true);
        //previous
        Assert.assertEquals(iterator.previous().intValue(), 0);
        Assert.assertEquals(iterator.hasPrevious(), false);
        //nextIndex
        Assert.assertEquals(iterator.nextIndex(), 0);
        iterator.next();
        Assert.assertEquals(iterator.nextIndex(), 1);
        //previousIndex
        Assert.assertEquals(iterator.previousIndex(), 0);
        iterator.next();
        Assert.assertEquals(iterator.previousIndex(), 1);
        //set
        list = new SimpleArrayList<>(5);
        list1 = new SimpleArrayList<>(5);

        list.add(0);
        list.add(null);
        list1.add(0);
        list1.add(1);

        iterator = list.listIterator();
        while (iterator.hasNext()) {
            Integer i = iterator.next();
            if (i == null) {
                iterator.set(1);
            }
        }
        Assert.assertEquals(list, list1);
        //add
        list = new SimpleArrayList<>(5);
        list1 = new SimpleArrayList<>(5);

        list.add(0);
        list.add(2);
        list1.add(0);
        list1.add(1);
        list1.add(2);
        iterator = list.listIterator();
        iterator.next();
        iterator.add(1);
        Assert.assertEquals(list, list1);
    }

    @Test
    public void removeAllTest() {
        List<Integer> list = new SimpleArrayList<>(5);
        List<Integer> list1 = new SimpleArrayList<>(5);
        List<Integer> list2 = new SimpleArrayList<>(5);

        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);

        list1.add(0);
        list1.add(3);

        list2.add(1);
        list2.add(2);

        list.removeAll(list2);
        Assert.assertEquals(list, list1);
    }

    @Test
    public void containsAll() {
        List<Integer> list = new SimpleArrayList<>(5);
        List<Integer> list1 = new SimpleArrayList<>(5);
        List<Integer> list2 = new SimpleArrayList<>(5);

        list.add(0);
        list.add(1);
        list.add(null);
        list.add(3);

        list1.add(0);
        list1.add(null);

        list2.add(1);
        list2.add(4);

        Assert.assertEquals(list.containsAll(list1), true);
        Assert.assertEquals(list.containsAll(list2), false);
    }

    @Test
    public void retainAllTest() {
        List<Integer> list = new SimpleArrayList<>(10);
        List<Integer> list1 = new SimpleArrayList<>(10);
        List<Integer> list2 = new SimpleArrayList<>(10);

        list.add(1);
        list.add(2);
        list.add(3);
        list.add(1);
        list.add(1);

        list1.add(1);
        list1.add(2);
        list1.add(1);
        list1.add(1);

        list2.add(1);
        list2.add(2);

        list.retainAll(list2);

        Assert.assertEquals(list, list1);
    }

//    @Test
//    public void outT() throws Throwable {
//        new AddAll().main(new String[]{});
//        new IteratorMicroBenchmark().main(new String[]{});
//        new RangeCheckMicroBenchmark().main(new String[]{});
//    }

}
