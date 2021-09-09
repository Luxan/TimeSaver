import { Component } from "@angular/core";
import { Subject } from "rxjs";

@Component({
  selector: 'image-manager',
  templateUrl: './images.page..html',
  styleUrls: [ './images.page.scss' ]
})
export class ImagesPage {

  his = false;
  pis = false;

  hisSubject$: Subject<void> = new Subject<void>();
  pisSubject$: Subject<void> = new Subject<void>();

  private reset() {
    this.his = false;
    this.pis = false;
  }


  goToHis() {
    if (!this.his) {
      console.log("goToHis");
      this.reset();
      this.his = true;
      this.hisSubject$.next();
    }
  }

  goToPis() {
    if (!this.pis) {
      console.log("goToPis");
      this.reset();
      this.pis = true;
      this.pisSubject$.next();
    }
  }
}
