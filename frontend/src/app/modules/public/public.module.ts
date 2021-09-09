import { NgModule } from '@angular/core';
import { HomeComponent } from './pages/home/home.component';
import { RouterModule, Routes } from "@angular/router";
import { CommonModule } from "@angular/common";
import { SharedModule } from "../../shared/shared.module";
import { NavbarComponent } from "./components/navbar/navbar.component";
import { AboutMePage } from "./pages/aboutme/aboutme.page";
import { PortfolioPage } from "./pages/portfolio/portfolio.page";
import { ContactPage } from "./pages/contact/contact.page";
import { ReactiveFormsModule } from "@angular/forms";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { MatSelectModule } from "@angular/material/select";
import { BlogPage } from "./pages/blog/blog/blog.page";
import { PostPage } from "./pages/blog/post/post.page";
import { MatIconModule } from "@angular/material/icon";
import {MatPaginatorModule} from "@angular/material/paginator";

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'about-me', component: AboutMePage },
  { path: 'portfolio', component: PortfolioPage },
  { path: 'contact', component: ContactPage },
  { path: 'blog', component: BlogPage },
  { path: 'blog/:id', component: PostPage },
];

@NgModule({
    imports: [
        CommonModule,
        RouterModule.forChild(routes),
        SharedModule,
        ReactiveFormsModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatIconModule,
        MatPaginatorModule
    ],
    exports: [
        NavbarComponent
    ],
    declarations: [
        HomeComponent,
        NavbarComponent,
        AboutMePage,
        PortfolioPage,
        ContactPage,
        BlogPage,
        PostPage
    ]
})
export class PublicModule {
}
