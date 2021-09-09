export class CreateSessionRequest {

  name: string;
  email: string;
  phone: string;
  sessionType: number;
  description: string;

  constructor(name: string, email: string, phone: string, sessionType: number, description: string) {
    this.name = name;
    this.email = email;
    this.phone = phone;
    this.sessionType = sessionType;
    this.description = description;
  }
}
