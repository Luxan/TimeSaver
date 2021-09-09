import { Injectable } from '@angular/core';

import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, of } from "rxjs";
import { catchError, tap } from "rxjs/operators";
import { environment } from "../../../../environments/environment";
import { LoggerService } from "../../components/logger/logger.service";
import { NameResponse } from "../../responses/name.response";
import { PostResponse } from "../../responses/post.response";
import { MultiplePostsResponse } from "../../responses/multiple-posts.response";
import { SortDirection } from "@angular/material/sort/sort-direction";

export interface PostsServiceSort {
  name: SortDirection,
  youtubeLink: SortDirection,
  date: SortDirection,
  active: SortDirection
}

export interface PostImage {
  image: string,
  id: number | undefined,
  name: string | undefined
}

@Injectable({
  providedIn: 'root'
})
export class PostsService {

  constructor(private http: HttpClient, private logger: LoggerService) {
  }

  public getPostById(id: number): Observable<PostResponse> {
    return this.http.get<PostResponse>(environment.serverApiUrl + 'posts/' + id)
      .pipe(
        tap(_ => this.logger.log('fetched post: ' + _.name)),
        catchError(this.handleError<PostResponse>('getPostById'))
      );
  }

  public getPostsPaginated(
    searchString: string,
    page: number,
    size: number,
    sort?: PostsServiceSort,
    active?: boolean
  ): Observable<MultiplePostsResponse> {
    let params = new HttpParams();
    params = params.append('searchText', searchString);
    params = params.append('page', page);
    params = params.append('size', size);
    params = PostsService.handleActive(active, params);
    params = PostsService.handleSorting(params, sort);
    return this.http.get<MultiplePostsResponse>(environment.serverApiUrl + 'posts',
      { params: params }
    ).pipe(
      tap(_ => this.logger.log('Fetched posts: ' + _.postDetails.length)),
      catchError(this.handleError<MultiplePostsResponse>('getPostsPaginated'))
    );
  }

  private static handleActive(active: undefined | boolean, params: HttpParams): HttpParams {
    if (active !== undefined && active !== null) {
      return params.append('active', active);
    }
    return params;
  }

  private static handleSorting(params: HttpParams, sort?: PostsServiceSort): HttpParams {
    if (sort !== undefined && sort !== null) {
      return params.append('sort', 'name,' + sort.name)
        .append('sort', 'youtubeLink,' + sort.youtubeLink)
        .append('sort', 'date,' + sort.date)
        .append('sort', 'active,' + sort.active);
    }
    return params;
  }

  public createPost(
    name: string,
    youtubeLink: string,
    content: string,
    active: boolean,
    images: PostImage[]
  ): Observable<NameResponse> {
    const formData = this.createFormData(images, name, youtubeLink, content, active);
    return this.http.post<NameResponse>(environment.serverApiUrl + 'posts', formData)
      .pipe(
        tap(_ => this.logger.log('created post: ' + _)),
        catchError(this.handleError<NameResponse>('createPost'))
      );
  }

  public updatePost(
    id: number,
    name: string,
    youtubeLink: string,
    content: string,
    active: boolean,
    images: PostImage[]
  ): Observable<NameResponse> {
    const formData = this.createFormData(images, name, youtubeLink, content, active);
    return this.http.put<NameResponse>(environment.serverApiUrl + 'posts', formData)
      .pipe(
        tap(_ => this.logger.log('updated post: ' + _)),
        catchError(this.handleError<NameResponse>('updatePost'))
      );
  }

  private createFormData(images: PostImage[], name: string, youtubeLink: string, content: string, active: boolean) {
    const formData = new FormData();
    images.forEach(image => {
      let blob = new Blob([ image.image ], { type: 'image/jpeg' });
      let file = new File(
        [ blob ],
        image.name ? image.name : "untitled.jpg",
        { type: "image/jpeg" }
      );
      formData.append('imageFiles', file);
    });
    formData.append('jsonString', PostsService.jsonString(name, youtubeLink, content, active));
    return formData;
  }

  private static jsonString(name: string, youtubeLink: string, content: string, active: boolean) {
    return JSON.stringify({
      document: {
        data: {
          id: 0,
          name: name,
          youtubeLink: youtubeLink,
          content: content,
          active: active
        }
      }
    });
  }

  changePostActivity(id: number, active: boolean) {
    const formData = new FormData();
    formData.append('jsonString', JSON.stringify({
      document: {
        data: {
          id: id,
          active: active
        }
      }
    }));

    return this.http.put<NameResponse>(environment.serverApiUrl + 'posts/activate', formData)
      .pipe(
        tap(_ => this.logger.log('changed post activity: ' + _.name)),
        catchError(this.handleError<NameResponse>('changePostActivity'))
      );
  }

  public deletePost(id: number): Observable<NameResponse> {
    return this.http.delete<NameResponse>(environment.serverApiUrl + 'posts/' + id)
      .pipe(
        tap(_ => this.logger.log('deleted post: ' + _)),
        catchError(this.handleError<NameResponse>('deletePost'))
      );
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      this.logger.log(`${operation} failed: ${error.message}`);
      return of(result as T);
    };
  }
}
