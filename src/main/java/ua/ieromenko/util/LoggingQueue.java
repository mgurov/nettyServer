package ua.ieromenko.util;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

/**
 * @Author Alexandr Ieromenko on 04.03.15.
 * <p/>
 * ConcurrentLinkedQueue implementation that holds only 16 values
 */
public final class LoggingQueue<E> implements Iterable<E> {
    private final ConcurrentLinkedQueue<E> queue = new ConcurrentLinkedQueue<>();

    public Iterator<E> iterator() {
        return queue.iterator();
    }

    public boolean add(E e) {
        if (queue.size() > 15) clean();
        return queue.add(e);
    }

    private void clean() {
        do {
            queue.remove();
        }
        while (queue.size() > 15);

    }
}