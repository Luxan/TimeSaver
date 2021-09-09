import {Component, OnInit} from '@angular/core';
import {ClientsService} from "../../../../shared/services/clients/clients.service";
import {ClientDetailsResponse, SessionDetailWithImage} from "../../../../shared/responses/client-details.response";
import {ImagesService} from "../../../../shared/services/images/images.service";
import {Router} from "@angular/router";

@Component({
  selector: 'private-sessions',
  templateUrl: './sessions.component.html',
  styleUrls: ['./sessions.component.scss']
})
export class SessionsComponent implements OnInit {

  clientId = 0;
  name = '';
  sessions: SessionDetailWithImage[] = []

  sessions1of2: SessionDetailWithImage[] = [];
  sessions2of2: SessionDetailWithImage[] = [];
  sessions1of3: SessionDetailWithImage[] = [];
  sessions2of3: SessionDetailWithImage[] = [];
  sessions3of3: SessionDetailWithImage[] = [];

  constructor(
    private clientsService: ClientsService,
    private imagesService: ImagesService,
    private router: Router,
  ) { }

  ngOnInit(): void {
    this.clientId = 1;

    this.clientsService.getClientDetails(this.clientId).subscribe((response: ClientDetailsResponse) => {
      this.name = response.name;

      let imageIds: number[] = [];
      response.sessions.forEach(session => imageIds.push(session.imageId));

      this.imagesService.getSmallImagesByIds(imageIds).subscribe(images => {
        response.sessions.forEach(session => {
          let sessionDetailWithImage = new SessionDetailWithImage(
            session.id,
            session.name,
            // @ts-ignore
            images.map[session.imageId]
          );
          this.sessions.push(sessionDetailWithImage);
        });
        this.sortSessions();
      });
    })
  }

  private sortSessions() {
    let heightColumn1of2 = 0;
    let heightColumn2of2 = 0;
    let heightColumn1of3 = 0;
    let heightColumn2of3 = 0;
    let heightColumn3of3 = 0;

    let sessions = this.sessions;
    let sessions1of2 = this.sessions1of2;
    let sessions2of2 = this.sessions2of2;
    let sessions1of3 = this.sessions1of3;
    let sessions2of3 = this.sessions2of3;
    let sessions3of3 = this.sessions3of3;

    for (let i = 0; i < this.sessions.length; i++) {
      let img = new Image();
      img.src = this.sessions[i].image;
      img.onload = function (event) {
        let  loadedImage = event.currentTarget;
        // @ts-ignore
        let height = loadedImage.height;
        if (heightColumn1of2 <= heightColumn2of2) {
          heightColumn1of2 += height;
          sessions1of2.push(sessions[i]);
        } else {
          heightColumn2of2 += height;
          sessions2of2.push(sessions[i]);
        }

        if (heightColumn1of3 <= heightColumn2of3 && heightColumn1of3 <= heightColumn3of3) {
          heightColumn1of3 += height;
          sessions1of3.push(sessions[i]);
        } else if (heightColumn2of3 <= heightColumn1of3 && heightColumn2of3 <= heightColumn3of3) {
          heightColumn2of3 += height;
          sessions2of3.push(sessions[i]);
        } else {
          heightColumn3of3 += height;
          sessions3of3.push(sessions[i]);
        }
      }
    }
  }

  viewSession(session: SessionDetailWithImage) {
    this.router.navigateByUrl('/private/sessions/' + session.id).then(r => {});
  }
}
