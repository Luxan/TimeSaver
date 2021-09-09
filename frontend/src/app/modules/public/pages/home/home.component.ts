import { Component, OnInit } from '@angular/core';
import { ImagesService } from "../../../../shared/services/images/images.service";
import { HomeImageWithBytes } from "../../../../shared/responses/home-images.response";

@Component({
  selector: 'home',
  templateUrl: './home.component.html',
  styleUrls: [ './home.component.scss' ]
})
export class HomeComponent implements OnInit {

  homeImages: HomeImageWithBytes[] = [];
  images = [944, 1011, 984].map((n) => `https://picsum.photos/id/${n}/900/500`);
  constructor(private imageService: ImagesService) {
  }

  ngOnInit(): void {
    this.imageService.getHomeImages().subscribe(value => {
      if (value.images.length > 0) {
        let imageIds: number[] = [];
        value.images.forEach(image => imageIds.push(image.id));
        this.imageService.getOriginalImagesByIds(imageIds).subscribe(images => {
          value.images.forEach(image => {
            // @ts-ignore
            let homeImageWithBytes = new HomeImageWithBytes(image.id, image.name, image.content, images.map[image.id]);
            this.homeImages.push(homeImageWithBytes);
          });
        });
      }
    });
  }


}
