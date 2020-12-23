package com.Denzo.firl.common;

public enum  AppEventBus {
    INSTANCE;

    AppEventBus(){
        init();
    }

    private EventBus eventBus ;

    private void init(){
        eventBus = EventBus.builder()
                .installDefaultEventBus();
    }

    public EventBus getEventBus() {
        return eventBus;
    }
}
