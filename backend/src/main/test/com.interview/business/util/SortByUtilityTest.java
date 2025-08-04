package com.interview.business.util;

import com.interview.business.exception.InvalidParameterException;
import com.interview.business.util.SortByUtility;
import com.interview.data.entity.Hike;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SortByUtilityTest {

    @Test
    void testGetSorting_unsorted() {
        Sort result = SortByUtility.getSorting("", Hike.class);
        assertEquals(Sort.unsorted(), result);
    }

    @Test
    void testGetSorting_invalidFormat_noComma() {
        assertThrows(InvalidParameterException.class, () -> SortByUtility.getSorting("weather ASC", Hike.class));
    }

    @Test
    void testGetSorting_invalidFormat_noDirection() {
        assertThrows(InvalidParameterException.class, () -> SortByUtility.getSorting("weather,", Hike.class));
    }

    @Test
    void testGetSorting_invalidField() {
        assertThrows(InvalidParameterException.class, () -> SortByUtility.getSorting("nonExistentField,ASC", Hike.class));
    }

    @Test
    void testGetSorting_invalidDirection() {
        assertThrows(InvalidParameterException.class, () -> SortByUtility.getSorting("weather,DESC;", Hike.class));
    }

    @Test
    void testGetSorting_valid() {
        Sort result = SortByUtility.getSorting("weather,ASC", Hike.class);
        assertEquals(Sort.by(Sort.Direction.ASC, "weather"), result);
    }
}