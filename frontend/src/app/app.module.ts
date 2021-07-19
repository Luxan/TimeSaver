import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from "./app.component";
import { RouterModule, Routes } from "@angular/router";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";
import { SharedModule } from "./shared/shared.module";
import { HttpClientModule } from "@angular/common/http";

const appRoutes: Routes = [
  {
    path: '',
    loadChildren: () => import('./modules/public/public.module').then(mod => mod.PublicModule)
  },
  {
    path: 'private',
    loadChildren: () => import('./modules/private/private.module').then(mod => mod.PrivateModule)
  },
  {
    path: 'admin',
    loadChildren: () => import('./modules/admin/admin.module').then(mod => mod.AdminModule)
  }
];

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    NoopAnimationsModule,
    RouterModule.forRoot(appRoutes),
    SharedModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [ AppComponent ]
})
export class AppModule {
}
