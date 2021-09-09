import {Component, OnInit} from '@angular/core';
import {ImagesService} from "../../../../shared/services/images/images.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MultipleImagesResponse} from "../../../../shared/responses/multiple-images.response";

@Component({
  selector: 'private-session',
  templateUrl: './session.component.html',
  styleUrls: ['./session.component.scss']
})
export class SessionComponent implements OnInit {

  private id: number = 0;
  images: string[] = []

  images1of2: string[] = [];
  images2of2: string[] = [];
  images1of3: string[] = [];
  images2of3: string[] = [];
  images3of3: string[] = [];

  constructor(
    private imagesService: ImagesService,
    private router: Router,
    private route: ActivatedRoute,
  ) { }

  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    if (this.id > 0) {
      this.imagesService.getImagesBySessionId(this.id).subscribe((response: MultipleImagesResponse) => {
        this.images = response.images;
        this.sortImages();
      });
    }
  }

  private sortImages() {
    let heightColumn1of2 = 0;
    let heightColumn2of2 = 0;
    let heightColumn1of3 = 0;
    let heightColumn2of3 = 0;
    let heightColumn3of3 = 0;

    let images = this.images;
    let images1of2 = this.images1of2;
    let images2of2 = this.images2of2;
    let images1of3 = this.images1of3;
    let images2of3 = this.images2of3;
    let images3of3 = this.images3of3;

    for (let i = 0; i < this.images.length; i++) {
      let img = new Image();
      img.src = this.images[i];
      img.onload = function (event) {
        let  loadedImage = event.currentTarget;
        // @ts-ignore
        let height = loadedImage.height;
        if (heightColumn1of2 <= heightColumn2of2) {
          heightColumn1of2 += height;
          images1of2.push(images[i]);
        } else {
          heightColumn2of2 += height;
          images2of2.push(images[i]);
        }

        if (heightColumn1of3 <= heightColumn2of3 && heightColumn1of3 <= heightColumn3of3) {
          heightColumn1of3 += height;
          images1of3.push(images[i]);
        } else if (heightColumn2of3 <= heightColumn1of3 && heightColumn2of3 <= heightColumn3of3) {
          heightColumn2of3 += height;
          images2of3.push(images[i]);
        } else {
          heightColumn3of3 += height;
          images3of3.push(images[i]);
        }
      }
    }
  }
}
