
export class ClientDetailsResponse {

  id: number;
  name: string;
  phone: string;
  email: string;
  sessions: SessionDetail[];

  constructor(id:number, name: string, email: string, phone: string, sessions: SessionDetail[]) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.phone = phone;
    this.sessions = sessions;
  }

}

export class SessionDetail {

  id: number;
  name: string;
  imageId: number;

  constructor(id: number, name: string, imageId: number) {
    this.id = id;
    this.name = name;
    this.imageId = imageId;
  }
}

export class SessionDetailWithImage {

  id: number;
  name: string;
  image: string;

  constructor(id: number, name: string, image: string) {
    this.id = id;
    this.name = name;
    this.image = image;
  }
}
