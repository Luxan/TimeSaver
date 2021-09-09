import { Component, Input, OnDestroy, OnInit } from "@angular/core";
import { ImagesService } from "../../../../shared/services/images/images.service";
import { ConfirmDialog } from "../../../../shared/dialogs/delete.dialog";
import { MatDialog } from "@angular/material/dialog";
import { AlertService } from "../../../../shared/services/alerts/alerts.service";
import {
  PortfolioImage,
  PortfolioImagesResponse,
  PortfolioImageWithBytes
} from "../../../../shared/responses/portfolio-images.response";
import { Observable, Subscription } from "rxjs";

@Component({
  selector: 'portfolio-image-selector',
  templateUrl: './portfolio-image-selector.component.html',
  styleUrls: [ './portfolio-image-selector.component.scss' ]
})
export class PortfolioImageSelectorComponent implements OnInit, OnDestroy {

  portfolioImages: PortfolioImageWithBytes[] = [];
  private originalPortfolioImages: PortfolioImageWithBytes[] = [];

  constructor(
    private imageService: ImagesService,
    private dialog: MatDialog,
    private alertService: AlertService) {
  }

  private eventsSubscription: Subscription = new Subscription();

  @Input() events: Observable<void> = new Observable<void>();

  ngOnInit(): void {
    this.eventsSubscription = this.events.subscribe(() => this.getImages());
  }

  ngOnDestroy() {
    this.eventsSubscription.unsubscribe();
  }

  private getImages() {
    console.log("getImages");
    this.imageService.getPortfolioImages().subscribe((value: PortfolioImagesResponse) => {
      if (value.images.length > 0) {
        let imageIds: number[] = [];
        value.images.forEach(image => imageIds.push(image.id));
        this.imageService.getOriginalImagesByIds(imageIds).subscribe(images => {
          console.log(images);
          value.images.forEach((image: PortfolioImage) => {
            // @ts-ignore
            let portfolioImageWithBytes = new PortfolioImageWithBytes(image.id, image.name, images.map[image.id]);
            this.portfolioImages.push(portfolioImageWithBytes);
          });
        });
        this.originalPortfolioImages = this.portfolioImages.map(x => Object.assign({}, x));
      }
    });
  }

  selectFiles(event: any): void {
    let selectedFiles: FileList = event.target.files;

    if (selectedFiles && selectedFiles[0]) {
      const numberOfFiles = selectedFiles.length;
      for (let i = 0; i < numberOfFiles; i++) {
        const reader = new FileReader();

        reader.onload = (e: any) => {
          let portfolioImage = new PortfolioImageWithBytes(0, selectedFiles[i].name, e.target.result);
          this.portfolioImages.push(portfolioImage);
        };

        reader.readAsDataURL(selectedFiles[i]);
      }
    }
  }

  delete(image: PortfolioImageWithBytes) {
    this.dialog.open(ConfirmDialog, {
      data: {
        name: 'Do you really want to delete this image?',
        description: `Name: ${image.name}`
      },
    }).afterClosed()
      .subscribe((value: boolean) => {
        if (value) {
          this.portfolioImages.splice(this.portfolioImages.indexOf(image), 1);
        }
      });
  }

  valueNotChanged() {
    if (this.originalPortfolioImages.length != this.portfolioImages.length)
      return false;

    if (this.originalPortfolioImages.length == 0)
      return true;

    for (let i = 0; i < this.originalPortfolioImages.length; i++) {
      if (this.originalPortfolioImages[i].bytes.length != this.portfolioImages[i].bytes.length)
        return false;
      if (this.originalPortfolioImages[i].name != this.portfolioImages[i].name)
        return false;
    }

    return true;
  }


  onSubmit() {
    console.log("onSubmit");
    console.log(this.portfolioImages);
    this.imageService.uploadPortfolioImages(this.portfolioImages).subscribe(value => {
        if (value) {
          this.alertService.success("Successfuly saved images");
          this.originalPortfolioImages = this.portfolioImages;
        }
      }
    );
  }
}
