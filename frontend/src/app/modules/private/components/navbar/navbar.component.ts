import {Component, OnInit} from "@angular/core";
import {environment} from "../../../../../environments/environment";

@Component({
  selector: 'navbar-private',
  templateUrl: './navbar.component.html',
  styleUrls: [ './navbar.component.scss' ]
})
export class NavbarComponent implements OnInit {

  facebookLink: string = '';
  instagramLink: string = '';
  expanded = false;

  ngOnInit(): void {
    this.facebookLink = environment.facebookLink;
    this.instagramLink = environment.instagramLink;
  }

}
