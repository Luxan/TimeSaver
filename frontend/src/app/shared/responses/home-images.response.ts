export class HomeImagesResponse {

  images: HomeImage[];

  constructor(images: HomeImage[]) {
    this.images = images;
  }

}

export class HomeImage {

  id: number;
  name: string;
  content: string;

  constructor(id: number, name: string, content: string) {
    this.id = id;
    this.name = name;
    this.content = content;
  }

}

export class HomeImageWithBytes {
  id: number;
  name: string;
  content: string;
  bytes: string;

  constructor(id: number, name: string, content: string, bytes: string) {
    this.id = id;
    this.name = name;
    this.content = content;
    this.bytes = bytes;
  }
}
