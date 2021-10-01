package org.hl7.tinkar.common.alert;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import org.reactivestreams.FlowAdapters;

import java.util.concurrent.Flow;

/**
 * Alert streams are generated by tasks?
 * <p>
 * Alert streams are intercepted first at the node level, then at the window level, in a dedicated alert node, then at an application level.
 * <p>
 * Alerts have a scope: node, window, application
 * <p>
 * Alerts have resolvers/fixers? Could simply be dismissers.
 * <p>
 * Alerts have an importance: information, confirmation, warning, error
 * <p>
 * Each node has a filter that either consumes alerts, or sends them on.
 * <p>
 * Each node has a set of resolvers that can be automatically or manually applied.
 */
public class AlertStream implements Flow.Processor<AlertObject, AlertObject> {
    final Multi<AlertObject> alertStream;
    final BroadcastProcessor<AlertObject> processor;

    public AlertStream() {
        this.processor = BroadcastProcessor.create();
        this.alertStream = processor.toHotStream();
    }

    @Override
    public void subscribe(Flow.Subscriber<? super AlertObject> subscriber) {
        alertStream.subscribe().withSubscriber(FlowAdapters.toSubscriber(subscriber));
    }

    public void dispatch(AlertObject alertObject) {
        processor.onNext(alertObject);
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onNext(AlertObject item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onError(Throwable throwable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onComplete() {
        throw new UnsupportedOperationException();
    }
}