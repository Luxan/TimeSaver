import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';
import { Observable, of } from "rxjs";
import { catchError, tap } from "rxjs/operators";
import { environment } from "../../../../environments/environment";
import { LoggerService } from "../../components/logger/logger.service";
import { MultipleClientResponse } from "../../responses/multiple-client.response";
import { NameResponse } from "../../responses/name.response";
import { UpdateClientRequest } from "../../requests/update-client-request";
import { CreateSessionRequest } from "../../requests/create-session-request";

@Injectable({
  providedIn: 'root'
})
export class SessionRequestService {

  constructor(private http: HttpClient, private logger: LoggerService) {
  }

  public getSessionRequests(): Observable<MultipleClientResponse> {
    return this.http.get<MultipleClientResponse>(environment.serverApiUrl + 'clients')
      .pipe(
        tap(_ => this.logger.log('fetched clients: ' + _.clientDetails.length)),
        catchError(this.handleError<MultipleClientResponse>('getClients'))
      );
  }

  public getSessionRequestsByString(searchString: string): Observable<MultipleClientResponse> {
    return this.http.get<MultipleClientResponse>(environment.serverApiUrl + 'clients/search/' + searchString)
      .pipe(
        tap(_ => this.logger.log('fetched clients by seatch string: ' + _.clientDetails.length)),
        catchError(this.handleError<MultipleClientResponse>('getClientByString'))
      );
  }

  public updateSessionRequest(id: number, name: string, email: string, phone: string): Observable<NameResponse> {
    let request = new UpdateClientRequest(id, name, email, phone);
    return this.http.put<NameResponse>(environment.serverApiUrl + 'clients', request)
      .pipe(
        tap(_ => this.logger.log('updated client: ' + _)),
        catchError(this.handleError<NameResponse>('updateClient'))
      );
  }

  public deleteSessionRequest(id: number): Observable<NameResponse> {
    return this.http.delete<NameResponse>(environment.serverApiUrl + 'clients/' + id)
      .pipe(
        tap(_ => this.logger.log('deleted client: ' + _)),
        catchError(this.handleError<NameResponse>('deleteClient'))
      );
  }

  createSessionRequest(name: string, email: string, phone: string, sessionType: number, description: string) {
    let request = new CreateSessionRequest(name, email, phone, sessionType, description);
    return this.http.post<NameResponse>(environment.serverApiUrl + 'sessions', request)
      .pipe(
        tap(_ => this.logger.log('created session request: ' + _)),
        catchError(this.handleError<NameResponse>('createSessionRequest'))
      );
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      this.logger.log(`${operation} failed: ${error.message}`);
      return of(result as T);
    };
  }
}
