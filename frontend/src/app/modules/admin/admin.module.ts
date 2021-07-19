import { NgModule } from '@angular/core';

import { ClientsPage } from './pages/clients/clients/clients.page';
import { RouterModule, Routes } from "@angular/router";
import { CommonModule } from "@angular/common";
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { ClientsTableComponent } from "./components/clients-table/clients-table.component";
import { SharedModule } from "../../shared/shared.module";
import { ClientsService } from "./services/clients/clients.service";
import { HTTP_INTERCEPTORS, HttpClientModule } from "@angular/common/http";
import { ClientDetailsPage } from "./pages/clients/client-details/client-details.page";
import { ReactiveFormsModule } from "@angular/forms";
import { MatDialogModule } from "@angular/material/dialog";
import { RetryInterceptor } from "../../core/interceptors/retry/retry.interceptor";
import { ErrorInterceptor } from "../../core/interceptors/error/error.interceptor";
import { AdminComponent } from "./admin.component";


const routes: Routes = [
  { path: 'clients', component: ClientsPage },
  { path: 'clients/view/:id', component: ClientDetailsPage },
  { path: 'clients/edit/:id', component: ClientDetailsPage },
  { path: 'clients/new', component: ClientDetailsPage }
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    HttpClientModule,
    SharedModule,
    ReactiveFormsModule,
    MatDialogModule
  ],
  declarations: [
    ClientsPage,
    ClientsTableComponent,
    ClientDetailsPage,
    AdminComponent
  ],
  providers: [
    ClientsService,
    { provide: HTTP_INTERCEPTORS, useClass: RetryInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }
  ],
  bootstrap: [ AdminComponent ]
})
export class AdminModule {
}
