import { Component, Input, OnDestroy, OnInit } from "@angular/core";
import { ImagesService } from "../../../../shared/services/images/images.service";
import { HomeImage, HomeImagesResponse, HomeImageWithBytes } from "../../../../shared/responses/home-images.response";
import { ConfirmDialog } from "../../../../shared/dialogs/delete.dialog";
import { MatDialog } from "@angular/material/dialog";
import { FormControl, FormGroup } from "@angular/forms";
import { AlertService } from "../../../../shared/services/alerts/alerts.service";
import { Observable, Subscription } from "rxjs";

@Component({
  selector: 'homepage-image-selector',
  templateUrl: './homepage-image-selector.component.html',
  styleUrls: [ './homepage-image-selector.component.scss' ]
})
export class HomepageImageSelectorComponent implements OnInit, OnDestroy {

  homeImages: HomeImageWithBytes[] = [];
  private originalHomeImages: HomeImageWithBytes[] = [];

  homepageImagesForm: FormGroup = new FormGroup({});

  private eventsSubscription: Subscription = new Subscription();

  @Input() events: Observable<void> = new Observable<void>();

  constructor(
    private imageService: ImagesService,
    private dialog: MatDialog,
    private alertService: AlertService) {
  }

  ngOnInit(): void {
    this.eventsSubscription = this.events.subscribe(() => this.getImages());
  }

  private getImages() {
    console.log("getImages");
    this.imageService.getHomeImages().subscribe((value: HomeImagesResponse) => {
      if (value.images.length > 0) {
        let imageIds: number[] = [];
        value.images.forEach(image => imageIds.push(image.id));
        this.imageService.getOriginalImagesByIds(imageIds).subscribe(images => {
          console.log(images);
          value.images.forEach((image: HomeImage) => {
            // @ts-ignore
            let homeImageWithBytes = new HomeImageWithBytes(image.id, image.name, image.content, images.map[image.id]);
            this.homeImages.push(homeImageWithBytes);
          });
        });
        this.originalHomeImages = this.homeImages.map(x => Object.assign({}, x));
        this.homepageImagesForm = this.createFormGroup(value.images);
      }
    });
  }

  createFormGroup(images: HomeImage[]) {
    const group: any = {};
    images.forEach(image => group[image.name] = new FormControl(''));
    return new FormGroup(group);
  }

  ngOnDestroy() {
    this.eventsSubscription.unsubscribe();
  }

  selectFiles(event: any): void {
    let selectedFiles: FileList = event.target.files;

    if (selectedFiles && selectedFiles[0]) {
      const numberOfFiles = selectedFiles.length;
      for (let i = 0; i < numberOfFiles; i++) {
        const reader = new FileReader();

        reader.onload = (e: any) => {
          let homeImage = new HomeImageWithBytes(0, selectedFiles[i].name, "", e.target.result);
          this.homeImages.push(homeImage);
          this.homepageImagesForm.addControl(selectedFiles[i].name, new FormControl(homeImage.content));
        };

        reader.readAsDataURL(selectedFiles[i]);
      }
    }
  }

  delete(image: HomeImageWithBytes) {
    this.dialog.open(ConfirmDialog, {
      data: {
        name: 'Do you really want to delete this image?',
        description: `Name: ${image.name}`
      },
    }).afterClosed()
      .subscribe((value: boolean) => {
        if (value) {
          this.homeImages.splice(this.homeImages.indexOf(image), 1);
          this.homepageImagesForm.removeControl(image.name);
        }
      });
  }

  valueNotChanged() {
    this.readValuesFromFormGroup();
    if (this.originalHomeImages.length != this.homeImages.length)
      return false;

    if (this.originalHomeImages.length == 0)
      return true;

    for (let i = 0; i < this.originalHomeImages.length; i++) {
      if (this.originalHomeImages[i].bytes.length != this.homeImages[i].bytes.length)
        return false;
      if (this.originalHomeImages[i].content != this.homeImages[i].content)
        return false;
      if (this.originalHomeImages[i].name != this.homeImages[i].name)
        return false;
    }

    return true;
  }

  onSubmit() {
    console.log("onSubmit");
    console.log(this.homeImages);
    this.imageService.uploadHomeImages(this.homeImages).subscribe(value => {
        if (value)
          this.alertService.success("Successfuly saved images");
      }
    );
  }

  private readValuesFromFormGroup() {
    this.homeImages.forEach(image => {
      image.content = this.homepageImagesForm.controls[image.name].value;
    })
  }
}
