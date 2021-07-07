import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from "./app.component";
import { PublicModule } from "./modules/public/public.module";
import { PrivateModule } from "./modules/private/private.module";
import { AdminModule } from "./modules/admin/admin.module";
import { RouterModule, Routes } from "@angular/router";

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
    RouterModule.forRoot(appRoutes)
  ],
  providers: [],
  bootstrap: [ AppComponent ]
})
export class AppModule {
}
