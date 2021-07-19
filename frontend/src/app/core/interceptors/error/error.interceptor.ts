import { Injectable } from "@angular/core";
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Observable, throwError } from "rxjs";
import { catchError } from "rxjs/operators";
import { AlertService } from "../../../shared/services/alerts.service";

export class HttpError {
  static BadRequest = 400;
  static Unauthorized = 401;
  static Forbidden = 403;
  static NotFound = 404;
  static TimeOut = 408;
  static Conflict = 409;
  static InternalServerError = 500;
}

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

  constructor(private alertService: AlertService) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          console.log('error interceptor');
          switch (error.status) {
            case HttpError.BadRequest:
              this.alertService.error('BadRequest');
              break;
            case HttpError.Unauthorized:
              this.alertService.error('Unauthorized');
              break;
            case HttpError.Forbidden:
              this.alertService.error('Forbidden');
              break;
            case HttpError.NotFound:
              this.alertService.error('NotFound');
              break;
            case HttpError.TimeOut:
              this.alertService.error('TimeOut');
              break;
            case HttpError.Conflict:
              this.alertService.error('Conflict');
              break;
            case HttpError.InternalServerError:
              this.alertService.error('InternalServerError');
              break;
            default:
              console.log('Unknown error. Server might be down');
              this.alertService.error('Unknown error. Server might be down.');
          }
          return throwError("Intercepted an error.");
        })
      )
  }


}
