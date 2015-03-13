package ua.ieromenko.util;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @Author Alexandr Ieromenko on 04.03.15.
 * <p/>
 * ConcurrentLinkedQueue implementation that holds only 16 values
 */
public class LoggingQueue<E> extends ConcurrentLinkedQueue<E> {
    @Override
    public boolean add(E e) {
        if (size() > 15) clean();
        return super.add(e);
    }

    private void clean() {
        do {
            super.remove();
        }
        while (size() > 15);

    }
}
