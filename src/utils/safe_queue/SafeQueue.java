package utils.safe_queue;

import utils.result.Result;

import java.util.Optional;
import java.util.stream.Stream;

public interface SafeQueue<T>
{
    Optional<T> dequeue();

    Optional<T> peek();

    void enqueue(T t);

    Stream<T> stream();
}
