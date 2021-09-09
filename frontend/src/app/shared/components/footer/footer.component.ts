import { Component, OnInit } from "@angular/core";
import { environment } from "../../../../environments/environment";


@Component({
  selector: 'footer-footer',
  templateUrl: './footer.component.html',
  styleUrls: [ './footer.component.scss' ]
})
export class FooterComponent implements OnInit {

  facebookLink: string = '';
  instagramLink: string = '';
  copyright: string = '';

  ngOnInit(): void {
    this.facebookLink = environment.facebookLink;
    this.instagramLink = environment.instagramLink;
    this.copyright = environment.copyright;
  }


}
