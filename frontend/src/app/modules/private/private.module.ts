import { NgModule } from '@angular/core';
import { GalleryComponent } from './pages/gallery/gallery/gallery.component';
import { RouterModule, Routes } from "@angular/router";
import { CommonModule } from "@angular/common";


const routes: Routes = [
  { path: 'gallery', component: GalleryComponent }
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes)
  ],
  declarations: [ GalleryComponent ]
})
export class PrivateModule {
}
