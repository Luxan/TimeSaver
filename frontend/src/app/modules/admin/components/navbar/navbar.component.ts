import { Component, OnInit } from "@angular/core";
import { environment } from "../../../../../environments/environment";
import { Router } from "@angular/router";

@Component({
  selector: 'navbar-admin',
  templateUrl: './navbar.component.html',
  styleUrls: [ './navbar.component.scss' ]
})
export class NavbarComponent implements OnInit {


  constructor(private router: Router) {
  }

  ngOnInit(): void {
  }

}
