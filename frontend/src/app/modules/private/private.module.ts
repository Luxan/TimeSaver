import {NgModule} from '@angular/core';
import {SessionComponent} from './pages/session/session.component';
import {RouterModule, Routes} from "@angular/router";
import {CommonModule} from "@angular/common";
import {SessionsComponent} from "./pages/sessions/sessions.component";
import {SharedModule} from "../../shared/shared.module";
import {NavbarComponent} from "./components/navbar/navbar.component";


const routes: Routes = [
  { path: 'sessions', component: SessionsComponent },
  { path: 'sessions/:id', component: SessionComponent }
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    SharedModule
  ],
  declarations: [
    SessionComponent,
    SessionsComponent,
    NavbarComponent
  ]
})
export class PrivateModule {
}
