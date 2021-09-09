import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { Location } from '@angular/common'
import { ClientsService } from "../../../../shared/services/clients/clients.service";
import { AlertService } from "../../../../shared/services/alerts/alerts.service";
import { ClientDetailsResponse } from "../../../../shared/responses/client-details.response";
import { NameResponse } from "../../../../shared/responses/name.response";


@Component({
  selector: 'client-details',
  templateUrl: './client-details.component.html',
  styleUrls: [ './client-details.component.scss' ]
})
export class ClientDetailsComponent implements OnInit {

  private id: number = 0;
  create: boolean = false;
  edit: boolean = false;
  view: boolean = false;

  submitted = false;

  clientForm = new FormGroup({
    name: new FormControl('', [ Validators.required, Validators.minLength(4) ]),
    email: new FormControl('', [ Validators.required, Validators.email ]),
    phone: new FormControl('', Validators.pattern('[- +()0-9]+'))
  });

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private clientsService: ClientsService,
    public location: Location,
    private alertService: AlertService
  ) {
  }

  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.create = this.router.url.includes('new');
    this.edit = this.router.url.includes('edit');
    this.view = this.router.url.includes('view');

    if (this.id > 0)
      this.initializeForm(this.id);
  }

  private initializeForm(id: number) {
    this.clientsService.getClientDetails(id).subscribe((value: ClientDetailsResponse) => {
      this.clientForm.patchValue({
        name: value.name,
        email: value.email,
        phone: value.phone
      });
    })
  }

  get f() {
    return this.clientForm.controls;
  }

  onSubmit() {
    this.submitted = true;
    let name = this.clientForm.get('name')?.value;
    let email = this.clientForm.get('email')?.value;
    let phone = this.clientForm.get('phone')?.value;
    if (this.create)
      this.createClient(name, email, phone);
    if (this.edit)
      this.editClient(name, email, phone);
    this.location.back();
  }

  private createClient(name: string, email: string, phone: string) {
    this.clientsService.createClient(name, email, phone).subscribe((value: NameResponse) => {
      this.alertService.success(`Client created: ${value}`);
    });
  }

  private editClient(name: string, email: string, phone: string) {
    this.clientsService.updateClient(this.id, name, email, phone).subscribe((value: NameResponse) => {
      this.alertService.success(`Client updated: ${value}`);
    });
  }
}
