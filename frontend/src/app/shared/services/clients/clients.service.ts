import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';
import { Observable, of } from "rxjs";
import { catchError, tap } from "rxjs/operators";
import { environment } from "../../../../environments/environment";
import { LoggerService } from "../../components/logger/logger.service";
import { MultipleClientResponse } from "../../responses/multiple-client.response";
import { ClientDetailsResponse } from "../../responses/client-details.response";
import { NameResponse } from "../../responses/name.response";
import { CreateClientRequest } from "../../requests/create-client-request";
import { UpdateClientRequest } from "../../requests/update-client-request";

@Injectable({
  providedIn: 'root'
})
export class ClientsService {

  constructor(private http: HttpClient, private logger: LoggerService) {
  }

  public getClients(): Observable<MultipleClientResponse> {
    return this.http.get<MultipleClientResponse>(environment.serverApiUrl + 'clients')
      .pipe(
        tap(_ => this.logger.log('fetched clients: ' + _.clientDetails.length)),
        catchError(this.handleError<MultipleClientResponse>('getClients'))
      );
  }

  public getClientDetails(id: number): Observable<ClientDetailsResponse> {
    return this.http.get<ClientDetailsResponse>(environment.serverApiUrl + 'clients/' + id)
      .pipe(
        tap(_ => this.logger.log('fetched client: ' + _.id)),
        catchError(this.handleError<ClientDetailsResponse>(`getClientById id=${id}`))
      );
  }

  public getClientsByString(searchString: string): Observable<MultipleClientResponse> {
    return this.http.get<MultipleClientResponse>(environment.serverApiUrl + 'clients/search/' + searchString)
      .pipe(
        tap(_ => this.logger.log('fetched clients by seatch string: ' + _.clientDetails.length)),
        catchError(this.handleError<MultipleClientResponse>('getClientByString'))
      );
  }

  public createClient(name: string, email: string, phone: string): Observable<NameResponse> {
    let request = new CreateClientRequest(name, email, phone);
    return this.http.post<NameResponse>(environment.serverApiUrl + 'clients', request)
      .pipe(
        tap(_ => this.logger.log('created client: ' + _)),
        catchError(this.handleError<NameResponse>('createClient'))
      );
  }

  public updateClient(id: number, name: string, email: string, phone: string): Observable<NameResponse> {
    let request = new UpdateClientRequest(id, name, email, phone);
    return this.http.put<NameResponse>(environment.serverApiUrl + 'clients', request)
      .pipe(
        tap(_ => this.logger.log('updated client: ' + _)),
        catchError(this.handleError<NameResponse>('updateClient'))
      );
  }

  public deleteClient(id: number): Observable<NameResponse> {
    return this.http.delete<NameResponse>(environment.serverApiUrl + 'clients/' + id)
      .pipe(
        tap(_ => this.logger.log('deleted client: ' + _)),
        catchError(this.handleError<NameResponse>('deleteClient'))
      );
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      this.logger.log(`${operation} failed: ${error.message}`);
      return of(result as T);
    };
  }

}
