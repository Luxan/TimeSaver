
export class PostResponse {
  id: number;
  name: string;
  youtubeLink: string;
  date: string;
  content: string;
  active: boolean;
  imagesIds: number[];

  constructor(id: number, name: string, youtubeLink: string, date: string, content: string, active: boolean, imagesIds: number[]) {
    this.id = id;
    this.name = name;
    this.youtubeLink = youtubeLink;
    this.date = date;
    this.content = content;
    this.active = active;
    this.imagesIds = imagesIds;
  }
}
