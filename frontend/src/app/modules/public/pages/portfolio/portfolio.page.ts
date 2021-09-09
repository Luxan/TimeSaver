import { Component, OnInit } from '@angular/core';
import { ImagesService } from "../../../../shared/services/images/images.service";
import { PortfolioImageWithBytes } from "../../../../shared/responses/portfolio-images.response";

@Component({
  selector: 'portfolio',
  templateUrl: './portfolio.page.html',
  styleUrls: [ './portfolio.page.scss' ]
})
export class PortfolioPage implements OnInit {

  portfolioImages: PortfolioImageWithBytes[] = [];
  portfolioImages1of2: PortfolioImageWithBytes[] = [];
  portfolioImages2of2: PortfolioImageWithBytes[] = [];
  portfolioImages1of3: PortfolioImageWithBytes[] = [];
  portfolioImages2of3: PortfolioImageWithBytes[] = [];
  portfolioImages3of3: PortfolioImageWithBytes[] = [];

  constructor(private imageService: ImagesService) {
  }

  ngOnInit(): void {
    this.imageService.getPortfolioImages().subscribe(value => {
      if (value.images.length > 0) {
        let imageIds: number[] = [];
        value.images.forEach(image => imageIds.push(image.id));
        this.imageService.getMediumImagesByIds(imageIds).subscribe(images => {
          value.images.forEach(image => {
            // @ts-ignore
            let portfolioImageWithBytes = new PortfolioImageWithBytes(image.id, image.name, images.map[image.id]);
            this.portfolioImages.push(portfolioImageWithBytes);
          });
          this.sortImages();
        });
      }
    });
  }

  private sortImages() {
    let heightColumn1of2 = 0;
    let heightColumn2of2 = 0;
    let heightColumn1of3 = 0;
    let heightColumn2of3 = 0;
    let heightColumn3of3 = 0;

    let portfolioImages = this.portfolioImages;
    let portfolioImages1of2 = this.portfolioImages1of2;
    let portfolioImages2of2 = this.portfolioImages2of2;
    let portfolioImages1of3 = this.portfolioImages1of3;
    let portfolioImages2of3 = this.portfolioImages2of3;
    let portfolioImages3of3 = this.portfolioImages3of3;

    for (let i = 0; i < this.portfolioImages.length; i++) {
      let img = new Image();
      img.src = this.portfolioImages[i].bytes;
      img.onload = function (event) {
        let  loadedImage = event.currentTarget;
        // @ts-ignore
        let height = loadedImage.height;
        if (heightColumn1of2 <= heightColumn2of2) {
          heightColumn1of2 += height;
          portfolioImages1of2.push(portfolioImages[i]);
        } else {
          heightColumn2of2 += height;
          portfolioImages2of2.push(portfolioImages[i]);
        }

        if (heightColumn1of3 <= heightColumn2of3 && heightColumn1of3 <= heightColumn3of3) {
          heightColumn1of3 += height;
          portfolioImages1of3.push(portfolioImages[i]);
        } else if (heightColumn2of3 <= heightColumn1of3 && heightColumn2of3 <= heightColumn3of3) {
          heightColumn2of3 += height;
          portfolioImages2of3.push(portfolioImages[i]);
        } else {
          heightColumn3of3 += height;
          portfolioImages3of3.push(portfolioImages[i]);
        }
      }
    }
  }

}
