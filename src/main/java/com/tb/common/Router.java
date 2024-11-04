package com.tb.common;

public interface Router<TInput, TRoute> {
    TRoute route(TInput input);
}
