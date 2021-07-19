import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';
import { Observable, of } from "rxjs";
import { catchError, tap } from "rxjs/operators";
import { Client } from "../../../../shared/models/client/client.model";
import { environment } from "../../../../../environments/environment";
import { LoggerService } from "../../../../shared/components/logger/logger.service";
import { CreateClientRequest } from "./requests/create-client-request";
import { UpdateClientRequest } from "./requests/update-client-request";

@Injectable({
  providedIn: 'root'
})
export class ClientsService {

  constructor(private http: HttpClient, private logger: LoggerService) {
  }

  public getClients(): Observable<Client[]> {
    return this.http.get<Client[]>(environment.serverApiUrl + 'clients')
      .pipe(
        tap(_ => this.logger.log('fetched clients: ' + _.length)),
        catchError(this.handleError<Client[]>('getClients', []))
      );
  }

  public getClientById(id: number): Observable<Client> {
    return this.http.get<Client>(environment.serverApiUrl + 'clients/' + id)
      .pipe(
        tap(_ => this.logger.log('fetched client: ' + _.id)),
        catchError(this.handleError<Client>(`getClientById id=${id}`))
      );
  }

  public searchClients(searchString: string): Observable<Client[]> {
    return this.http.get<Client[]>(environment.serverApiUrl + 'clients/search/' + searchString)
      .pipe(
        tap(_ => this.logger.log('fetched clients by seatch string: ' + _.length)),
        catchError(this.handleError<Client[]>('getClientByString', []))
      );
  }

  public createClient(name: string, email: string, phone: string): Observable<Client> {
    let request = new CreateClientRequest(name, email, phone);
    return this.http.post<Client>(environment.serverApiUrl + 'clients', request)
      .pipe(
        tap(_ => this.logger.log('created client: ' + _.id)),
        catchError(this.handleError<Client>('createClient'))
      );
  }

  public updateClient(id: number, name: string, email: string, phone: string): Observable<Client> {
    let request = new UpdateClientRequest(id, name, email, phone);
    return this.http.put<Client>(environment.serverApiUrl + 'clients/' + id, request)
      .pipe(
        tap(_ => this.logger.log('updated client: ' + _.id)),
        catchError(this.handleError<Client>('updateClient'))
      );
  }

  public deleteClient(id: number): Observable<Client> {
    return this.http.delete<Client>(environment.serverApiUrl + 'clients/' + id)
      .pipe(
        tap(_ => this.logger.log('deleted client: ' + _.id)),
        catchError(this.handleError<Client>('deleteClient'))
      );
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      this.logger.log(`${operation} failed: ${error.message}`);
      return of(result as T);
    };
  }

}
