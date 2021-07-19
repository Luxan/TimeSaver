import { Component, Injectable, OnInit } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LoggerService {

  public log(message: string) {
    console.log(message);
  }

}
