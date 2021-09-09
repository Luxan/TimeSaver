export class PortfolioImagesResponse {

  images: PortfolioImage[];

  constructor(images: PortfolioImage[]) {
    this.images = images;
  }

}

export class PortfolioImage {

  id: number;
  name: string;

  constructor(id: number, name: string) {
    this.id = id;
    this.name = name;
  }

}

export class PortfolioImageWithBytes {
  id: number;
  name: string;
  bytes: string;

  constructor(id: number, name: string, bytes: string) {
    this.id = id;
    this.name = name;
    this.bytes = bytes;
  }
}
