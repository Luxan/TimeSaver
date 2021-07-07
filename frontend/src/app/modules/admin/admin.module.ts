import { NgModule } from '@angular/core';

import { ClientsComponent } from './pages/clients/clients/clients.component';
import { RouterModule, Routes } from "@angular/router";
import { CommonModule } from "@angular/common";


const routes: Routes = [
  { path: 'clients', component: ClientsComponent }
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes)
  ],
  declarations: [ ClientsComponent ]
})
export class AdminModule {
}
