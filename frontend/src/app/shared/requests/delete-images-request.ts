export class DeleteImagesRequest {

  imagesIds: number[];

  constructor(imagesIds: number[]) {
    this.imagesIds = imagesIds;
  }
}
