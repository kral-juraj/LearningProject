package com.beekeeper.shared.viewmodel;

import com.beekeeper.shared.entity.CalendarEvent;
import com.beekeeper.shared.repository.CalendarEventRepository;
import com.beekeeper.shared.scheduler.SchedulerProvider;
import com.beekeeper.shared.util.DateUtils;
import com.jakewharton.rxrelay2.BehaviorRelay;

import java.util.List;
import java.util.UUID;

import io.reactivex.Observable;

/**
 * ViewModel for CalendarEvent management.
 * 100% platform-agnostic - shared between Android and Desktop.
 */
public class CalendarEventViewModel extends BaseViewModel {

    private final CalendarEventRepository repository;
    private final SchedulerProvider schedulerProvider;

    private final BehaviorRelay<List<CalendarEvent>> events = BehaviorRelay.create();
    private final BehaviorRelay<Boolean> loading = BehaviorRelay.createDefault(false);
    private final BehaviorRelay<String> error = BehaviorRelay.create();
    private final BehaviorRelay<String> success = BehaviorRelay.create();

    public CalendarEventViewModel(CalendarEventRepository repository, SchedulerProvider schedulerProvider) {
        this.repository = repository;
        this.schedulerProvider = schedulerProvider;
    }

    public Observable<List<CalendarEvent>> getEvents() {
        return events;
    }

    public Observable<Boolean> getLoading() {
        return loading;
    }

    public Observable<String> getError() {
        return error;
    }

    public Observable<String> getSuccess() {
        return success;
    }

    /**
     * Loads all upcoming events from current date.
     */
    public void loadUpcomingEvents() {
        long currentDate = DateUtils.getCurrentTimestamp();
        loading.accept(true);
        addDisposable(
            repository.getUpcomingEvents(currentDate)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(
                    eventList -> {
                        events.accept(eventList);
                        loading.accept(false);
                    },
                    throwable -> {
                        error.accept("Chyba pri načítaní udalostí: " + throwable.getMessage());
                        loading.accept(false);
                    }
                )
        );
    }

    /**
     * Loads all events (past, present, and future).
     */
    public void loadAllEvents() {
        loading.accept(true);
        addDisposable(
            repository.getAllEvents()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(
                    eventList -> {
                        events.accept(eventList);
                        loading.accept(false);
                    },
                    throwable -> {
                        error.accept("Chyba pri načítaní udalostí: " + throwable.getMessage());
                        loading.accept(false);
                    }
                )
        );
    }

    /**
     * Loads events by hive ID.
     */
    public void loadEventsByHiveId(String hiveId) {
        loading.accept(true);
        addDisposable(
            repository.getEventsByHiveId(hiveId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(
                    eventList -> {
                        events.accept(eventList);
                        loading.accept(false);
                    },
                    throwable -> {
                        error.accept("Chyba pri načítaní udalostí: " + throwable.getMessage());
                        loading.accept(false);
                    }
                )
        );
    }

    /**
     * Creates a new calendar event.
     */
    public void createEvent(CalendarEvent event) {
        if (event.getId() == null || event.getId().isEmpty()) {
            event.setId(UUID.randomUUID().toString());
        }
        if (event.getCreatedAt() == 0) {
            event.setCreatedAt(DateUtils.getCurrentTimestamp());
        }

        loading.accept(true);
        addDisposable(
            repository.insertEvent(event)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(
                    () -> {
                        success.accept("Udalosť úspešne vytvorená");
                        loading.accept(false);
                        loadAllEvents();
                    },
                    throwable -> {
                        error.accept("Chyba pri vytváraní udalosti: " + throwable.getMessage());
                        loading.accept(false);
                    }
                )
        );
    }

    /**
     * Updates an existing calendar event.
     */
    public void updateEvent(CalendarEvent event) {
        loading.accept(true);
        addDisposable(
            repository.updateEvent(event)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(
                    () -> {
                        success.accept("Udalosť úspešne aktualizovaná");
                        loading.accept(false);
                        loadAllEvents();
                    },
                    throwable -> {
                        error.accept("Chyba pri aktualizácii udalosti: " + throwable.getMessage());
                        loading.accept(false);
                    }
                )
        );
    }

    /**
     * Deletes a calendar event.
     */
    public void deleteEvent(CalendarEvent event) {
        loading.accept(true);
        addDisposable(
            repository.deleteEvent(event)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribe(
                    () -> {
                        success.accept("Udalosť úspešne zmazaná");
                        loading.accept(false);
                        loadAllEvents();
                    },
                    throwable -> {
                        error.accept("Chyba pri mazaní udalosti: " + throwable.getMessage());
                        loading.accept(false);
                    }
                )
        );
    }

    /**
     * Toggles the completed status of an event.
     */
    public void toggleCompleted(CalendarEvent event) {
        event.setCompleted(!event.isCompleted());
        updateEvent(event);
    }
}
