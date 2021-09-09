import { Injectable } from '@angular/core';

import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, of } from "rxjs";
import { catchError, tap } from "rxjs/operators";
import { environment } from "../../../../environments/environment";
import { LoggerService } from "../../components/logger/logger.service";
import { HomeImagesResponse, HomeImageWithBytes } from "../../responses/home-images.response";
import { ImageResponse } from "../../responses/image.response";
import { DeleteImagesRequest } from "../../requests/delete-images-request";
import { ImageNameListResponse } from "../../responses/image-name-list.response";
import { ImageMapResponse } from "../../responses/image-map.response";
import { MultipleImagesResponse } from "../../responses/multiple-images.response";
import { ImageIdNameMapResponse } from "../../responses/image-id-name-map.response";
import { PortfolioImagesResponse, PortfolioImageWithBytes } from "../../responses/portfolio-images.response";

@Injectable({
  providedIn: 'root'
})
export class ImagesService {

  constructor(private http: HttpClient, private logger: LoggerService) {
  }

  public getHomeImages(): Observable<HomeImagesResponse> {
    return this.http.get<HomeImagesResponse>(environment.serverApiUrl + 'images/home')
      .pipe(
        tap(_ => this.logger.log('fetched home images: ' + _.images.length)),
        catchError(this.handleError<HomeImagesResponse>('getHomeImages'))
      );
  }

  public getPortfolioImages(): Observable<PortfolioImagesResponse> {
    return this.http.get<PortfolioImagesResponse>(environment.serverApiUrl + 'images/portfolio')
      .pipe(
        tap(_ => this.logger.log('fetched portfolio images: ' + _.images.length)),
        catchError(this.handleError<PortfolioImagesResponse>('getPortfolioImages'))
      );
  }

  public uploadHomeImages(homeImages: HomeImageWithBytes[]): Observable<ImageNameListResponse> {
    const formData = new FormData();
    const jsonData: any = { document: { data: [] } };

    homeImages.forEach(image => {
      let blob = new Blob([ image.bytes ], { type: 'image/jpeg' });
      let file = new File([ blob ], image.name, { type: "image/jpeg" });
      formData.append('imageFiles', file);
      jsonData.document.data.push({ content: image.content })
    });
    formData.append('jsonString', JSON.stringify(jsonData));
    return this.http.post<ImageNameListResponse>(environment.serverApiUrl + 'images/home', formData)
      .pipe(
        tap(_ => this.logger.log('uploaded home images: ' + _.imagesNames.length)),
        catchError(this.handleError<ImageNameListResponse>('uploadHomeImages'))
      );
  }

  public uploadPortfolioImages(portfolioImages: PortfolioImageWithBytes[]): Observable<ImageNameListResponse> {
    const formData = new FormData();

    portfolioImages.forEach(image => {
      let blob = new Blob([ image.bytes ], { type: 'image/jpeg' });
      let file = new File([ blob ], image.name, { type: "image/jpeg" });

      formData.append('imageFiles', file);
    });
    return this.http.post<ImageNameListResponse>(environment.serverApiUrl + 'images/portfolio', formData)
      .pipe(
        tap(_ => this.logger.log('uploaded portfolio images: ' + _.imagesNames.length)),
        catchError(this.handleError<ImageNameListResponse>('uploadPortfolioImages'))
      );
  }

  public getSmallImageById(id: number): Observable<ImageResponse> {
    return this.http.get<ImageResponse>(environment.serverApiUrl + 'images/small/' + id)
      .pipe(
        tap(_ => this.logger.log('fetched small image by id: ' + _.bytes.length)),
        catchError(this.handleError<ImageResponse>('getImageById'))
      );
  }

  public getMediumImageById(id: number): Observable<ImageResponse> {
    return this.http.get<ImageResponse>(environment.serverApiUrl + 'images/medium/' + id)
      .pipe(
        tap(_ => this.logger.log('fetched medium image by id: ' + _.bytes.length)),
        catchError(this.handleError<ImageResponse>('getImageById'))
      );
  }

  public getOriginalImageById(id: number): Observable<ImageResponse> {
    return this.http.get<ImageResponse>(environment.serverApiUrl + 'images/original/' + id)
      .pipe(
        tap(_ => this.logger.log('fetched original image by id: ' + _.bytes.length)),
        catchError(this.handleError<ImageResponse>('getImageById'))
      );
  }

  public getSmallImagesByIds(ids: number[]): Observable<ImageMapResponse> {
    let params = new HttpParams();
    ids.forEach(id => params = params.append('ids', id));
    return this.http.get<ImageMapResponse>(environment.serverApiUrl + 'images/small', { params: params })
      .pipe(
        tap(_ => this.logger.log('fetched small images by ids: ' + _.map.size)),
        catchError(this.handleError<ImageMapResponse>('getImageById'))
      );
  }

  public getMediumImagesByIds(ids: number[]): Observable<ImageMapResponse> {
    let params = new HttpParams();
    ids.forEach(id => params = params.append('ids', id));
    return this.http.get<ImageMapResponse>(environment.serverApiUrl + 'images/medium', { params: params })
      .pipe(
        tap(_ => this.logger.log('fetched image by id: ' + _.map.size)),
        catchError(this.handleError<ImageMapResponse>('getImageById'))
      );
  }

  public getOriginalImagesByIds(ids: number[]): Observable<ImageMapResponse> {
    let params = new HttpParams();
    ids.forEach(id => params = params.append('ids', id));
    return this.http.get<ImageMapResponse>(environment.serverApiUrl + 'images/original', { params: params })
      .pipe(
        tap(_ => this.logger.log('fetched image by id: ' + _.map.size)),
        catchError(this.handleError<ImageMapResponse>('getImageById'))
      );
  }

  public getImagesBySessionId(id: number): Observable<MultipleImagesResponse> {
    return this.http.get<MultipleImagesResponse>(environment.serverApiUrl + 'images/session/' + id)
      .pipe(
        tap(_ => this.logger.log('fetched images by session id: ' + _.images.length)),
        catchError(this.handleError<MultipleImagesResponse>('getImagesBySessionId'))
      );
  }

  public uploadImages(id: number): Observable<ImageIdNameMapResponse> {
    return this.http.post<ImageIdNameMapResponse>(environment.serverApiUrl + 'images/' + id, null)
      .pipe(
        tap(_ => this.logger.log('uploaded images: ' + _.map.size)),
        catchError(this.handleError<ImageIdNameMapResponse>('uploadImages'))
      );
  }

  public deleteImages(request: DeleteImagesRequest): Observable<ImageNameListResponse> {
    return this.http.delete<ImageNameListResponse>(environment.serverApiUrl + 'images')
      .pipe(
        tap(_ => this.logger.log('deleted images: ' + _.imagesNames.length)),
        catchError(this.handleError<ImageNameListResponse>('uploadImages'))
      );
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      this.logger.log(`${operation} failed: ${error.message}`);
      return of(result as T);
    };
  }

}
