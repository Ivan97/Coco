package tech.iooo.boot.core;

import java.util.function.Function;
import tech.iooo.boot.core.utils.ServiceHelper;

/**
 * Created on 2019-03-28 09:16
 *
 * @author <a href="mailto:yangkizhang@gmail.com?subject=iooo-boot">Ivan97</a>
 */
public interface Future<T> extends AsyncResult<T>, Handler<AsyncResult<T>> {

  /**
   * Create a future that hasn't completed yet and that is passed to the {@code handler} before it is returned.
   *
   * @param handler the handler
   * @param <T> the result type
   * @return the future.
   */
  static <T> Future<T> future(Handler<Promise<T>> handler) {
    Promise<T> promise = Promise.promise();
    try {
      handler.handle(promise);
    } catch (Throwable e) {
      promise.tryFail(e);
    }
    return promise.future();
  }

  /**
   * Create a future that hasn't completed yet
   *
   * @param <T> the result type
   * @return the future
   * @deprecated instead use {@link Promise#promise()}
   */
  @Deprecated
  static <T> Future<T> future() {
    return factory.future();
  }

  /**
   * Create a succeeded future with a null result
   *
   * @param <T> the result type
   * @return the future
   */
  static <T> Future<T> succeededFuture() {
    return factory.succeededFuture();
  }

  /**
   * Created a succeeded future with the specified result.
   *
   * @param result the result
   * @param <T> the result type
   * @return the future
   */
  static <T> Future<T> succeededFuture(T result) {
    if (result == null) {
      return factory.succeededFuture();
    } else {
      return factory.succeededFuture(result);
    }
  }

  /**
   * Create a failed future with the specified failure cause.
   *
   * @param t the failure cause as a Throwable
   * @param <T> the result type
   * @return the future
   */
  static <T> Future<T> failedFuture(Throwable t) {
    return factory.failedFuture(t);
  }

  /**
   * Create a failed future with the specified failure message.
   *
   * @param failureMessage the failure message
   * @param <T> the result type
   * @return the future
   */
  static <T> Future<T> failedFuture(String failureMessage) {
    return factory.failureFuture(failureMessage);
  }

  /**
   * Has the future completed?
   * <p>
   * It's completed if it's either succeeded or failed.
   *
   * @return true if completed, false if not
   */
  boolean isComplete();

  /**
   * Like {@link #onComplete(Handler)}.
   */
  Future<T> setHandler(Handler<AsyncResult<T>> handler);

  /**
   * Add a handler to be notified of the result. <br/>
   *
   * @param handler the handler that will be called with the result
   * @return a reference to this, so it can be used fluently
   */
  default Future<T> onComplete(Handler<AsyncResult<T>> handler) {
    return setHandler(handler);
  }

  /**
   * Add a handler to be notified of the succeeded result. <br/>
   *
   * @param handler the handler that will be called with the succeeded result
   * @return a reference to this, so it can be used fluently
   */
  default Future<T> onSuccess(Handler<T> handler) {
    return onComplete(ar -> {
      if (ar.succeeded()) {
        handler.handle(ar.result());
      }
    });
  }

  /**
   * Add a handler to be notified of the failed result. <br/>
   *
   * @param handler the handler that will be called with the failed result
   * @return a reference to this, so it can be used fluently
   */
  default Future<T> onFailure(Handler<Throwable> handler) {
    return onComplete(ar -> {
      if (ar.failed()) {
        handler.handle(ar.cause());
      }
    });
  }

  /**
   * Set the result. Any handler will be called, if there is one, and the future will be marked as completed.
   *
   * @param result the result
   * @deprecated instead create a {@link Promise} and use {@link Promise#complete(Object)}
   */
  @Deprecated
  void complete(T result);

  /**
   * Set a null result. Any handler will be called, if there is one, and the future will be marked as completed.
   *
   * @deprecated instead create a {@link Promise} and use {@link Promise#complete()}
   */
  @Deprecated
  void complete();

  /**
   * Set the failure. Any handler will be called, if there is one, and the future will be marked as completed.
   *
   * @param cause the failure cause
   * @deprecated instead create a {@link Promise} and use {@link Promise#fail(Throwable)}
   */
  @Deprecated
  void fail(Throwable cause);

  /**
   * Try to set the failure. When it happens, any handler will be called, if there is one, and the future will be marked as completed.
   *
   * @param failureMessage the failure message
   * @deprecated instead create a {@link Promise} and use {@link Promise#fail(String)}
   */
  @Deprecated
  void fail(String failureMessage);

  /**
   * Set the failure. Any handler will be called, if there is one, and the future will be marked as completed.
   *
   * @param result the result
   * @return false when the future is already completed
   * @deprecated instead create a {@link Promise} and use {@link Promise#tryComplete(Object)}
   */
  @Deprecated
  boolean tryComplete(T result);

  /**
   * Try to set the result. When it happens, any handler will be called, if there is one, and the future will be marked as completed.
   *
   * @return false when the future is already completed
   * @deprecated instead create a {@link Promise} and use {@link Promise#tryComplete()}
   */
  @Deprecated
  boolean tryComplete();

  /**
   * Try to set the failure. When it happens, any handler will be called, if there is one, and the future will be marked as completed.
   *
   * @param cause the failure cause
   * @return false when the future is already completed
   * @deprecated instead create a {@link Promise} and use {@link Promise#tryFail(Throwable)}
   */
  @Deprecated
  boolean tryFail(Throwable cause);

  /**
   * Try to set the failure. When it happens, any handler will be called, if there is one, and the future will be marked as completed.
   *
   * @param failureMessage the failure message
   * @return false when the future is already completed
   * @deprecated instead create a {@link Promise} and use {@link Promise#tryFail(String)}
   */
  @Deprecated
  boolean tryFail(String failureMessage);

  /**
   * The result of the operation. This will be null if the operation failed.
   *
   * @return the result or null if the operation failed.
   */
  @Override
  T result();

  /**
   * A Throwable describing failure. This will be null if the operation succeeded.
   *
   * @return the cause or null if the operation succeeded.
   */
  @Override
  Throwable cause();

  /**
   * Did it succeed?
   *
   * @return true if it succeded or false otherwise
   */
  @Override
  boolean succeeded();

  /**
   * Did it fail?
   *
   * @return true if it failed or false otherwise
   */
  @Override
  boolean failed();

  /**
   * Alias for {@link #compose(Function)}.
   */
  default <U> Future<U> flatMap(Function<T, Future<U>> mapper) {
    return compose(mapper);
  }

  /**
   * Compose this future with a provided {@code next} future.<p>
   *
   * When this (the one on which {@code compose} is called) future succeeds, the {@code handler} will be called with the completed value, this handler should complete the next future.<p>
   *
   * If the {@code handler} throws an exception, the returned future will be failed with this exception.<p>
   *
   * When this future fails, the failure will be propagated to the {@code next} future and the {@code handler} will not be called.
   *
   * @param handler the handler
   * @param next the next future
   * @return the next future, used for chaining
   * @deprecated use {@link #compose(Function)})} instead
   */
  @Deprecated
  default <U> Future<U> compose(Handler<T> handler, Future<U> next) {
    setHandler(ar -> {
      if (ar.succeeded()) {
        try {
          handler.handle(ar.result());
        } catch (Throwable err) {
          if (next.isComplete()) {
            throw err;
          }
          next.fail(err);
        }
      } else {
        next.fail(ar.cause());
      }
    });
    return next;
  }

  /**
   * Compose this future with a {@code mapper} function.<p>
   *
   * When this future (the one on which {@code compose} is called) succeeds, the {@code mapper} will be called with the completed value and this mapper returns another future object. This returned future completion will complete the future
   * returned by this method call.<p>
   *
   * If the {@code mapper} throws an exception, the returned future will be failed with this exception.<p>
   *
   * When this future fails, the failure will be propagated to the returned future and the {@code mapper} will not be called.
   *
   * @param mapper the mapper function
   * @return the composed future
   */
  default <U> Future<U> compose(Function<T, Future<U>> mapper) {
    return compose(mapper, Future::failedFuture);
  }

  /**
   * Compose this future with a {@code successMapper} and {@code failureMapper} functions.<p>
   *
   * When this future (the one on which {@code compose} is called) succeeds, the {@code successMapper} will be called with the completed value and this mapper returns another future object. This returned future completion will complete the
   * future returned by this method call.<p>
   *
   * When this future (the one on which {@code compose} is called) fails, the {@code failureMapper} will be called with the failure and this mapper returns another future object. This returned future completion will complete the future
   * returned by this method call.<p>
   *
   * If any mapper function throws an exception, the returned future will be failed with this exception.<p>
   *
   * @param successMapper the function mapping the success
   * @param failureMapper the function mapping the failure
   * @return the composed future
   */
  default <U> Future<U> compose(Function<T, Future<U>> successMapper, Function<Throwable, Future<U>> failureMapper) {
    if (successMapper == null) {
      throw new NullPointerException();
    }
    if (failureMapper == null) {
      throw new NullPointerException();
    }
    Promise<U> ret = Promise.promise();
    setHandler(ar -> {
      if (ar.succeeded()) {
        Future<U> apply;
        try {
          apply = successMapper.apply(ar.result());
        } catch (Throwable e) {
          ret.fail(e);
          return;
        }
        apply.setHandler(ret);
      } else {
        Future<U> apply;
        try {
          apply = failureMapper.apply(ar.cause());
        } catch (Throwable e) {
          ret.fail(e);
          return;
        }
        apply.setHandler(ret);
      }
    });
    return ret.future();
  }

  /**
   * Apply a {@code mapper} function on this future.<p>
   *
   * When this future succeeds, the {@code mapper} will be called with the completed value and this mapper returns a value. This value will complete the future returned by this method call.<p>
   *
   * If the {@code mapper} throws an exception, the returned future will be failed with this exception.<p>
   *
   * When this future fails, the failure will be propagated to the returned future and the {@code mapper} will not be called.
   *
   * @param mapper the mapper function
   * @return the mapped future
   */
  @Override
  default <U> Future<U> map(Function<T, U> mapper) {
    if (mapper == null) {
      throw new NullPointerException();
    }
    Promise<U> ret = Promise.promise();
    setHandler(ar -> {
      if (ar.succeeded()) {
        U mapped;
        try {
          mapped = mapper.apply(ar.result());
        } catch (Throwable e) {
          ret.fail(e);
          return;
        }
        ret.complete(mapped);
      } else {
        ret.fail(ar.cause());
      }
    });
    return ret.future();
  }

  /**
   * Map the result of a future to a specific {@code value}.<p>
   *
   * When this future succeeds, this {@code value} will complete the future returned by this method call.<p>
   *
   * When this future fails, the failure will be propagated to the returned future.
   *
   * @param value the value that eventually completes the mapped future
   * @return the mapped future
   */
  @Override
  default <V> Future<V> map(V value) {
    Promise<V> ret = Promise.promise();
    setHandler(ar -> {
      if (ar.succeeded()) {
        ret.complete(value);
      } else {
        ret.fail(ar.cause());
      }
    });
    return ret.future();
  }

  /**
   * Map the result of a future to {@code null}.<p>
   *
   * This is a conveniency for {@code future.map((T) null)} or {@code future.map((Void) null)}.<p>
   *
   * When this future succeeds, {@code null} will complete the future returned by this method call.<p>
   *
   * When this future fails, the failure will be propagated to the returned future.
   *
   * @return the mapped future
   */
  @Override
  default <V> Future<V> mapEmpty() {
    return (Future<V>) AsyncResult.super.mapEmpty();
  }

  /**
   * Succeed or fail this future with the {@link AsyncResult} event.
   *
   * @param asyncResult the async result to handle
   */
  @Override
  void handle(AsyncResult<T> asyncResult);

  /**
   * @return an handler completing this future
   * @deprecated use this object instead since it extends {@code Handler<AsyncResult<T>>}
   */
  @Deprecated
  default Handler<AsyncResult<T>> completer() {
    return this;
  }

  /**
   * Handles a failure of this Future by returning the result of another Future. If the mapper fails, then the returned future will be failed with this failure.
   *
   * @param mapper A function which takes the exception of a failure and returns a new future.
   * @return A recovered future
   */
  default Future<T> recover(Function<Throwable, Future<T>> mapper) {
    if (mapper == null) {
      throw new NullPointerException();
    }
    Promise<T> ret = Promise.promise();
    setHandler(ar -> {
      if (ar.succeeded()) {
        ret.complete(result());
      } else {
        Future<T> mapped;
        try {
          mapped = mapper.apply(ar.cause());
        } catch (Throwable e) {
          ret.fail(e);
          return;
        }
        mapped.setHandler(ret);
      }
    });
    return ret.future();
  }

  /**
   * Apply a {@code mapper} function on this future.<p>
   *
   * When this future fails, the {@code mapper} will be called with the completed value and this mapper returns a value. This value will complete the future returned by this method call.<p>
   *
   * If the {@code mapper} throws an exception, the returned future will be failed with this exception.<p>
   *
   * When this future succeeds, the result will be propagated to the returned future and the {@code mapper} will not be called.
   *
   * @param mapper the mapper function
   * @return the mapped future
   */
  @Override
  default Future<T> otherwise(Function<Throwable, T> mapper) {
    if (mapper == null) {
      throw new NullPointerException();
    }
    Promise<T> ret = Promise.promise();
    setHandler(ar -> {
      if (ar.succeeded()) {
        ret.complete(result());
      } else {
        T value;
        try {
          value = mapper.apply(ar.cause());
        } catch (Throwable e) {
          ret.fail(e);
          return;
        }
        ret.complete(value);
      }
    });
    return ret.future();
  }

  /**
   * Map the failure of a future to a specific {@code value}.<p>
   *
   * When this future fails, this {@code value} will complete the future returned by this method call.<p>
   *
   * When this future succeeds, the result will be propagated to the returned future.
   *
   * @param value the value that eventually completes the mapped future
   * @return the mapped future
   */
  @Override
  default Future<T> otherwise(T value) {
    Promise<T> ret = Promise.promise();
    setHandler(ar -> {
      if (ar.succeeded()) {
        ret.complete(result());
      } else {
        ret.complete(value);
      }
    });
    return ret.future();
  }

  /**
   * Map the failure of a future to {@code null}.<p>
   *
   * This is a convenience for {@code future.otherwise((T) null)}.<p>
   *
   * When this future fails, the {@code null} value will complete the future returned by this method call.<p>
   *
   * When this future succeeds, the result will be propagated to the returned future.
   *
   * @return the mapped future
   */
  @Override
  default Future<T> otherwiseEmpty() {
    return (Future<T>) AsyncResult.super.otherwiseEmpty();
  }

  FutureFactory factory = ServiceHelper.loadFactory(FutureFactory.class);
}
