package Path;

import java.util.*;

public class Solutions implements Set<Path> {
    LinkedList<Path> list;
    Random random;
    Solutions(){
        list = new LinkedList<Path>();
        random = new Random();
    }
    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public Iterator<Path> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public boolean equals(Object o) {
        return list.equals(o);
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }

    @Override
    public boolean addAll(Collection<? extends Path> c) {
        return list.addAll(c);
    }

    @Override
    public boolean add(Path path) {
        String str;
        do {
            str = colorGenerator(random);
        }while (DuplicateColor(str));
        path.setColor(str);
        return list.add(path);
    }
    private boolean DuplicateColor(String color){
        for (Path p:list) {
            if (p.getColor()==color)
                return true;
        }
        return false;
    }
    private String colorGenerator(Random r){
        final char [] hex = { '0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        char [] s = new char[7];
        int     n = r.nextInt(0x1000000);

        s[0] = '#';
        for (int i=1;i<7;i++) {
            s[i] = hex[n & 0xf];
            n >>= 4;
        }
        return new String(s);
    }
}
