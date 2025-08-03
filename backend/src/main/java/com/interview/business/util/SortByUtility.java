package com.interview.business.util;

import com.interview.business.exception.InvalidParameterException;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class SortByUtility
{
    private SortByUtility() {
        throw new IllegalStateException("Utility class");
    }

    @NonNull
    public static <T> Sort getSorting(String value, Class<T> clazz) {
        if (isBlank(value)) {
            return Sort.unsorted();
        }
        if (!value.contains(",") || (!value.contains("ASC") && !value.contains("DESC"))) {
            throwInvalidParameterException(value);
        }

        String[] expression = value.split(",");

        List<String> allFields = Arrays.stream(clazz.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toList());

        allFields.addAll(Arrays.stream(clazz.getSuperclass().getDeclaredFields())
                .map(Field::getName)
                .toList());

        if (!allFields.contains(expression[0])) {
            throw new InvalidParameterException("validation.sort.field.not.exists", expression[0]);
        }

        if (!expression[1].equals("ASC") && !expression[1].equals("DESC")) {
            throwInvalidParameterException(value);
        }
        Sort.Direction direction = Sort.Direction.valueOf(expression[1]);
        return Sort.by(direction, expression[0]);
    }

    private static void throwInvalidParameterException(String value) {
        throw new InvalidParameterException("validation.sort.invalid.format", value);
    }
}
