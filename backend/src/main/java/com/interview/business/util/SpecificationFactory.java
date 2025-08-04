package com.interview.business.util;

import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class SpecificationFactory<T> {

    /**
     * Provides a specification to search for the given text in any of the given fields in join table.
     *
     * @param searchText the text to search for
     * @param joinField  name of join field
     * @param fieldNames the fields to search in
     * @return the specification, or null if no field names were given or the search text is empty
     */
    @Nullable
    public Specification<T> filterContainingInJoinFields(@Nullable String searchText,
                                                         String joinField,
                                                         @NonNull String... fieldNames) {
        if (fieldNames.length == 0 || StringUtils.isEmpty(searchText)) {
            return null;
        }

        String pattern = "%" + searchText.toLowerCase() + "%";
        return (root, query, builder) -> {
            var predicates = new Predicate[fieldNames.length];
            for (int i = 0; i < fieldNames.length; i++) {
                predicates[i] = builder.like(builder.lower(root.join(joinField, JoinType.LEFT).get(fieldNames[i])), pattern);
            }
            return builder.or(predicates);
        };
    }

    /**
     * Provides a specification to match the given text with values of the given field in join table.
     *
     * @param searchValue the text to search for
     * @param joinField  name of join field
     * @param fieldName the field to search in
     * @return the specification, or null if no field name were given or the search text is empty
     */
    @Nullable
    public Specification<T> filterEqualInJoinField(@Nullable Object searchValue,
                                                   String joinField,
                                                   @NonNull String fieldName) {
        if (searchValue == null) {
            return null;
        }

        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Expression<?> fieldExpression = root.join(joinField, JoinType.LEFT).get(fieldName);
            if (searchValue instanceof Collection<?>) {
                predicates.add(fieldExpression.in((Collection<?>) searchValue));
            } else {
                predicates.add(builder.equal(fieldExpression, searchValue));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Provides a specification to search for the different value in any of the given fields.
     *
     * @param fields key value with key -> field name, value -> desired value
     * @return the specification
     */
    public Specification<T> filterEqualInFields(@NotNull Map<String, Object> fields) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            fields.forEach((field, value) -> {
                Expression<?> fieldExpression = root.get(field);
                if (value instanceof List) {
                    predicates.add(fieldExpression.in((List<?>) value));
                } else {
                    predicates.add(builder.equal(fieldExpression, value));
                }
            });
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
