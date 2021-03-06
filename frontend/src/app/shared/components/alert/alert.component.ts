import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';

import { AlertService } from '../../services/alerts/alerts.service';

@Component({
  selector: 'alert',
  templateUrl: 'alert.component.html',
  styleUrls: [ 'alert.component.scss' ]
})
export class AlertComponent implements OnInit, OnDestroy {
  private subscription: Subscription = new Subscription();
  message: any;

  constructor(private alertService: AlertService) {
  }

  ngOnInit() {
    this.subscription = this.alertService.getAlert()
      .subscribe((message: { type: any; cssClass: string; }) => {
        switch (message && message.type) {
          case 'success':
            message.cssClass = 'alert alert-success';
            break;
          case 'error':
            message.cssClass = 'alert alert-danger';
            break;
        }

        this.message = message;

        setTimeout(() => {
          this.message = false;
        }, 5000);
      });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
