package com.beekeeper.shared.repository;

import com.beekeeper.shared.dao.CalendarEventDao;
import com.beekeeper.shared.entity.CalendarEvent;
import io.reactivex.Completable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CalendarEventRepository.
 */
class CalendarEventRepositoryTest {

    @Mock
    private CalendarEventDao calendarEventDao;

    private CalendarEventRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        repository = new CalendarEventRepository(calendarEventDao);
    }

    @Test
    void testInsertCalendarEvent_generatesIdAndTimestamps() {
        CalendarEvent event = new CalendarEvent();
        event.setTitle("Test Event");
        event.setEventDate(System.currentTimeMillis());

        when(calendarEventDao.insert(any(CalendarEvent.class))).thenReturn(Completable.complete());

        long before = System.currentTimeMillis();
        repository.insertEvent(event).blockingAwait();
        long after = System.currentTimeMillis();

        assertNotNull(event.getId());
        assertTrue(event.getCreatedAt() >= before);
        verify(calendarEventDao, times(1)).insert(event);
    }

    @Test
    void testUpdateCalendarEvent_callsDao() {
        CalendarEvent event = new CalendarEvent();
        event.setId("test-id");

        when(calendarEventDao.update(any(CalendarEvent.class))).thenReturn(Completable.complete());

        repository.updateEvent(event).blockingAwait();

        verify(calendarEventDao, times(1)).update(event);
    }

    @Test
    void testDeleteCalendarEvent_callsDao() {
        CalendarEvent event = new CalendarEvent();
        event.setId("test-id");

        when(calendarEventDao.delete(any(CalendarEvent.class))).thenReturn(Completable.complete());

        repository.deleteEvent(event).blockingAwait();

        verify(calendarEventDao, times(1)).delete(event);
    }

}
