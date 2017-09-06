package com.pvelychko.utils;

import javax.annotation.Nullable;
import java.util.Optional;

public class Functions {
    public static <T> Optional<T> optional(@Nullable T o) {
        return Optional.ofNullable(o);
    }
}
