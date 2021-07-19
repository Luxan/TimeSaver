import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable, throwError, timer } from 'rxjs';
import { mergeMap, retryWhen } from 'rxjs/operators';

export interface RetryParams {
  maxAttempts?: number;
  scalingDuration?: number;
  shouldRetry?: ({ status }: { status: number }) => boolean;
}

const defaultParams: RetryParams = {
  maxAttempts: 3,
  scalingDuration: 1000,
  shouldRetry: ({ status }) => status >= 400
}

export const genericRetryStrategy = (params: RetryParams = {}) => (attempts: Observable<any>) => attempts.pipe(
  mergeMap((error, i) => {
    const { maxAttempts, scalingDuration, shouldRetry } = { ...defaultParams, ...params }
    const retryAttempt = i + 1;
    // @ts-ignore
    if (retryAttempt > maxAttempts || !shouldRetry(error)) {
      return throwError(error);
    }
    // @ts-ignore
    console.log(`Attempt ${retryAttempt}: retrying in ${retryAttempt * scalingDuration}ms`);
    // @ts-ignore
    return timer(retryAttempt * scalingDuration);
  })
);


@Injectable()
export class RetryInterceptor implements HttpInterceptor {

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const { shouldRetry } = this;
    return next.handle(request)
      .pipe(retryWhen(genericRetryStrategy({
        shouldRetry
      })));
  }

  // @ts-ignore
  private shouldRetry = (error) => error.status === 404;
}

