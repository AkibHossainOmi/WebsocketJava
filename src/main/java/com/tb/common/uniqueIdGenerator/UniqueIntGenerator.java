package com.tb.common.uniqueIdGenerator;

public class UniqueIntGenerator implements UniqueIdGenerator<Integer> {
    int value=0;
    public UniqueIntGenerator(int initialValue) {
        this.value = initialValue;
    }
    @Override
    public Integer getNext() {
        if (value==Integer.MAX_VALUE){
            value=0;
        }
        return ++value;
    }
}
