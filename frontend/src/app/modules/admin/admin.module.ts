import { NgModule } from '@angular/core';

import { ClientsPage } from './pages/clients/clients/clients.page';
import { RouterModule, Routes } from "@angular/router";
import { CommonModule } from "@angular/common";
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { ClientsTableComponent } from "./components/clients-table/clients-table.component";
import { SharedModule } from "../../shared/shared.module";
import { HTTP_INTERCEPTORS, HttpClientModule } from "@angular/common/http";
import { ClientDetailsPage } from "./pages/clients/client-details/client-details.page";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatDialogModule } from "@angular/material/dialog";
import { RetryInterceptor } from "../../core/interceptors/retry/retry.interceptor";
import { ErrorInterceptor } from "../../core/interceptors/error/error.interceptor";
import { AdminComponent } from "./admin.component";
import { ImagesPage } from "./pages/images/images.page";
import { SessionsPage } from "./pages/sessions/sessions.page";
import { NotificationComponent } from "./components/notification/notification.component";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { NavbarComponent } from "./components/navbar/navbar.component";
import { HomepageImageSelectorComponent } from "./components/homepage-image-selector/homepage-image-selector.component";
import { ClientDetailsComponent } from "./components/client-details/client-details.component";
import { PortfolioImageSelectorComponent } from "./components/portfolio-image-selector/portfolio-image-selector.component";
import { PostsPage } from "./pages/posts/posts/posts.page";
import { PostDetailsPage } from "./pages/posts/posts-details/post-details.page";
import { MatCheckboxModule } from "@angular/material/checkbox";
import { MatIconModule } from "@angular/material/icon";
import { PostPage } from "../public/pages/blog/post/post.page";


const routes: Routes = [
  { path: 'clients', component: ClientsPage },
  { path: 'clients/view/:id', component: ClientDetailsPage },
  { path: 'clients/edit/:id', component: ClientDetailsPage },
  { path: 'clients/new', component: ClientDetailsPage },
  { path: 'images', component: ImagesPage },
  { path: 'sessions', component: SessionsPage },
  { path: 'posts', component: PostsPage },
  { path: 'posts/new', component:  PostDetailsPage },
  { path: 'posts/edit/:id', component: PostDetailsPage },
  { path: 'posts/view/:id', component: PostPage },
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
        MatDialogModule,
        MatFormFieldModule,
        MatInputModule,
        MatCheckboxModule,
        MatIconModule,
        FormsModule,
    ],
  declarations: [
    ClientsPage,
    ClientsTableComponent,
    ClientDetailsPage,
    AdminComponent,
    ImagesPage,
    SessionsPage,
    NotificationComponent,
    NavbarComponent,
    HomepageImageSelectorComponent,
    PortfolioImageSelectorComponent,
    ClientDetailsComponent,
    PostsPage,
    PostDetailsPage
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: RetryInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }
  ],
  bootstrap: [ AdminComponent ]
})
export class AdminModule {
}
