package de.markusdangl.mazegen.util;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {

    /**
     * Select a random element from a non-empty list.
     * @param list The list to select from.
     * @param <T> The element type of the list.
     * @return One of the lists elements, chose via non-secure random number.
     */
    public static <T> T selectRandomFrom(@NotNull List<T> list) {
        assert list.size() > 0;
        if (list.size() == 1)
            return list.get(0);

        int index = ThreadLocalRandom.current().nextInt(list.size());
        return list.get(index);
    }

}
