package tech.picnic.assignment.impl;

import tech.picnic.assignment.api.StreamProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;

public abstract class BaseStreamProcessor implements StreamProcessor {
    protected int maxEvents;
    protected Duration duration;

    public void initializeConditions(int maxEvents, Duration maxTime) {
        this.maxEvents = maxEvents;
        this.duration = maxTime;
    }

    @Override
    public abstract void process(InputStream source, OutputStream sink) throws IOException;

    @Override
    public void close() {
        StreamProcessor.super.close();
    }
}
