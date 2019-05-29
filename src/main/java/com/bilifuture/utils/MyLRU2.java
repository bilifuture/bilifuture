package com.bilifuture.utils;
import java.util.HashMap;

/**
 * 使用cache和链表实现缓存
 * 
 * 每次添加元素或者读取元素就将元素放置在链表的头，当缓存满了之后，就可以将尾结点元素删除，这样就实现了LRU缓存。
这种方法中是通过自己编写代码移动结点和删除结点，为了防止缓存大小超过限制，每次进行put的时候都会进行检查，若缓存满了则删除尾部元素。
 */
public class MyLRU2<K, V> {
    private final int MAX_CACHE_SIZE;
    private Entry<K, V> head;
    private Entry<K, V> tail;

    private HashMap<K, Entry<K, V>> cache;

    public MyLRU2(int cacheSize) {
        MAX_CACHE_SIZE = cacheSize;
        cache = new HashMap<>();
    }

    public void put(K key, V value) {
        Entry<K, V> entry = getEntry(key);
        if (entry == null) {
            if (cache.size() >= MAX_CACHE_SIZE) {
                cache.remove(tail.key);
                removeTail();
            }
            entry = new Entry<>();
            entry.key = key;
            entry.value = value;
            moveToHead(entry);
            cache.put(key, entry);
        } else {
            entry.value = value;
            moveToHead(entry);
        }
    }

    public V get(K key) {
        Entry<K, V> entry = getEntry(key);
        if (entry == null) {
            return null;
        }
        moveToHead(entry);
        return entry.value;
    }

    public void remove(K key) {
        Entry<K, V> entry = getEntry(key);
        if (entry != null) {
            if (entry == head) {
                Entry<K, V> next = head.next;
                head.next = null;
                head = next;
                head.pre = null;
            } else if (entry == tail) {
                Entry<K, V> prev = tail.pre;
                tail.pre = null;
                tail = prev;
                tail.next = null;
            } else {
                entry.pre.next = entry.next;
                entry.next.pre = entry.pre;
            }
            cache.remove(key);
        }
    }

    private void removeTail() {
        if (tail != null) {
            Entry<K, V> prev = tail.pre;
            if (prev == null) {
                head = null;
                tail = null;
            } else {
                tail.pre = null;
                tail = prev;
                tail.next = null;
            }
        }
    }

    private void moveToHead(Entry<K, V> entry) {
        if (entry == head) {
            return;
        }
        if (entry.pre != null) {
            entry.pre.next = entry.next;
        }
        if (entry.next != null) {
            entry.next.pre = entry.pre;
        }
        if (entry == tail) {
            Entry<K, V> prev = entry.pre;
            if (prev != null) {
                tail.pre = null;
                tail = prev;
                tail.next = null;
            }
        }

        if (head == null || tail == null) {
            head = tail = entry;
            return;
        }

        entry.next = head;
        head.pre = entry;
        entry.pre = null;
        head = entry;
    }

    private Entry<K, V> getEntry(K key) {
        return cache.get(key);
    }

    private static class Entry<K, V> {
        Entry<K, V> pre;
        Entry<K, V> next;
        K key;
        V value;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Entry<K, V> entry = head;
        while (entry != null) {
            stringBuilder.append(String.format("%s:%s ", entry.key, entry.value));
            entry = entry.next;
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        MyLRU2<Integer, Integer> lru2 = new MyLRU2<>(5);
        lru2.put(1, 1);
        System.out.println(lru2);
        lru2.put(2, 2);
        System.out.println(lru2);
        lru2.put(3, 3);
        System.out.println(lru2);
        lru2.get(1);
        System.out.println(lru2);
        lru2.put(4, 4);
        lru2.put(5, 5);
        lru2.put(6, 6);
        System.out.println(lru2);
    }
}