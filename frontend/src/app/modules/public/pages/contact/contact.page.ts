import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { AlertService } from "../../../../shared/services/alerts/alerts.service";
import { SessionRequestService } from "../../../../shared/services/session-request/session-request.service";

@Component({
  selector: 'contact',
  templateUrl: './contact.page.html',
  styleUrls: [ './contact.page.scss' ]
})
export class ContactPage implements OnInit {

  submitted = false;

  orderForm = new FormGroup({
    name: new FormControl('', [ Validators.required, Validators.minLength(4) ]),
    email: new FormControl('', [ Validators.required, Validators.email ]),
    phone: new FormControl('', Validators.pattern('[- +()0-9]+')),
    sessionType: new FormControl('', Validators.required),
    description: new FormControl(''),
  });

  constructor(
    private alertService: AlertService,
    private sessionService: SessionRequestService
  ) {
  }

  ngOnInit(): void {

  }

  get f() {
    return this.orderForm.controls;
  }

  onSubmit() {
    this.submitted = true;
    let name = this.orderForm.get('name')?.value;
    let email = this.orderForm.get('email')?.value;
    let phone = this.orderForm.get('phone')?.value;
    let sessionType = this.orderForm.get('sessionType')?.value;
    let description = this.orderForm.get('description')?.value;
    this.sessionService.createSessionRequest(name, email, phone, sessionType, description).subscribe()
  }

  getNameErrorMessage() {
    if (this.f.name.hasError('required')) {
      return 'You must enter a value';
    }

    return this.f.name.hasError('name') ? 'Enter at least 4 characters' : '';
  }

  getEmailErrorMessage() {
    if (this.f.email.hasError('required')) {
      return 'You must enter a value';
    }

    return this.f.email.hasError('email') ? 'Not a valid email' : '';
  }

  getPhoneErrorMessage() {
    return this.f.phone.hasError('phone') ? 'Not a valid phone' : '';
  }

  getSessionTypeErrorMessage() {
    return 'You must select type';
  }
}
