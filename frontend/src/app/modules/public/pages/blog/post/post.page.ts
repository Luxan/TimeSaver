import {Component, NgZone, OnInit} from '@angular/core';
import {PostsService} from "../../../../../shared/services/posts/posts.service";
import {ActivatedRoute, Router} from "@angular/router";
import {DatePipe} from "@angular/common";
import {AlertService} from "../../../../../shared/services/alerts/alerts.service";
import {PostResponse} from "../../../../../shared/responses/post.response";
import {ImagesService} from "../../../../../shared/services/images/images.service";
import {ImageMapResponse} from "../../../../../shared/responses/image-map.response";

@Component({
  selector: 'post',
  templateUrl: './post.page.html',
  styleUrls: [ './post.page.scss' ]
})
export class PostPage implements OnInit {

  private id = 0;

  date = '';
  name = '';
  youtubeLink: undefined | string = undefined;
  content = '';
  images: string[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private postsService: PostsService,
    private imagesService: ImagesService,
    private alertService: AlertService,
    private _ngZone: NgZone
  ) {
  }

  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    if (this.id) {
      this.postsService.getPostById(this.id).subscribe((response: PostResponse) => {
        this.name = response.name;
        this.youtubeLink = response.youtubeLink;
        this.date = response.date;
        this.content = response.content;
        this.imagesService.getSmallImagesByIds(response.imagesIds).subscribe((res: ImageMapResponse) => {
          for (let key of Object.keys(res.map)) {
            // @ts-ignore
            this.images.push(res.map[key]);
          }
        });
      });
    }
  }

  formatDate(date: string) {
    let pipe = new DatePipe('en-US');
    return pipe.transform(date, 'short');
  }
}
