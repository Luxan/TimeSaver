export class MultipleClientResponse {
  clientDetails: ClientDetails[];

  constructor(clientDetails: ClientDetails[]) {
    this.clientDetails = clientDetails;
  }

}

export class ClientDetails {

  id: number;
  name: string;
  phone: string;
  email: string;

  constructor(id: number, name: string, phone: string, email: string) {
    this.id = id;
    this.name = name;
    this.phone = phone;
    this.email = email;
  }

}
