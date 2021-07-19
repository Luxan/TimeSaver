import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { ClientsService } from "../../../services/clients/clients.service";
import { Location } from '@angular/common'
import { AlertService } from "../../../../../shared/services/alerts.service";

@Component({
  selector: 'app-client-details',
  templateUrl: './client-details.page.html',
  styleUrls: [ './client-details.page.scss' ]
})
export class ClientDetailsPage implements OnInit {

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
    this.clientsService.getClientById(id).subscribe(value => {
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
    this.clientsService.createClient(name, email, phone).subscribe(value => {
      this.alertService.success(`Client created: ${value.name} ${value.email} ${value.phone}`);
    });
  }

  private editClient(name: string, email: string, phone: string) {
    this.clientsService.updateClient(this.id, name, email, phone).subscribe(value => {
      this.alertService.success(`Client updated: ${value.id} ${value.name} ${value.email} ${value.phone}`);
    });
  }
}
