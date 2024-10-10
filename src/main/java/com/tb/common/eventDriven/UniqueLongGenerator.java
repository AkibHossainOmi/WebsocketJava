package com.tb.common.eventDriven;

public class UniqueLongGenerator implements UniqueIdGenerator<Long> {
    Long value = 0L;
    public UniqueLongGenerator(long initialValue) {
        this.value = initialValue;
    }
    @Override
    public Long getNext() {
        if (value==Long.MAX_VALUE){
            value=0L;
        }
        return ++value;
    }
}
