export class MultiplePostsResponse {
  postCount: number;
  page: number;
  postDetails: PostDetails[];

  constructor(postCount: number, page: number, postDetails: PostDetails[]) {
    this.postCount = postCount;
    this.page = page;
    this.postDetails = postDetails;
  }
}

export class PostDetails {
  id: number;
  name: string;
  youtubeLink: string;
  date: string;
  content: string;
  active: boolean;
  coverImageId: number;

  constructor(id: number, name: string, youtubeLink: string, date: string, content: string, active: boolean, coverImageId: number) {
    this.id = id;
    this.name = name;
    this.youtubeLink = youtubeLink;
    this.date = date;
    this.content = content;
    this.active = active;
    this.coverImageId = coverImageId;
  }
}

export class PostDetailsWithImage {
  id: number;
  name: string;
  youtubeLink: string;
  date: string;
  content: string;
  active: boolean;
  coverImage: string;

  constructor(id: number, name: string, youtubeLink: string, date: string, content: string, active: boolean, coverImage: string) {
    this.id = id;
    this.name = name;
    this.youtubeLink = youtubeLink;
    this.date = date;
    this.content = content;
    this.active = active;
    this.coverImage = coverImage;
  }
}
