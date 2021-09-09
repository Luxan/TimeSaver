import {Component, ElementRef, HostListener, OnInit, ViewChild} from '@angular/core';
import {PostsService, PostsServiceSort} from "../../../../../shared/services/posts/posts.service";
import {MultiplePostsResponse, PostDetailsWithImage} from "../../../../../shared/responses/multiple-posts.response";
import {fromEvent} from "rxjs";
import {debounceTime, distinctUntilChanged, filter, map} from "rxjs/operators";
import {ImagesService} from "../../../../../shared/services/images/images.service";
import {PageEvent} from "@angular/material/paginator";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'blog',
  templateUrl: './blog.page.html',
  styleUrls: ['./blog.page.scss']
})
export class BlogPage implements OnInit {

  postsOnOnePage = 5;
  postDetails: PostDetailsWithImage[] = [];
  postCount = 0;
  currentPage = 0;
  searchText = '';

  @ViewChild('searchInput', {static: true}) postsSearchInput: ElementRef | undefined;

  private sort: PostsServiceSort = {
    name: '',
    youtubeLink: '',
    date: 'asc',
    active: ''
  };

  @HostListener('window:scroll', ['$event']) // for window scroll events
  onScroll($event: Event) {
    let maxScrollHeight = document.scrollingElement!.scrollHeight - window.innerHeight;
    let currentScrollHeight = window.pageYOffset;
    const percentage = currentScrollHeight / maxScrollHeight;

    let bgStatic = document.querySelector('.bg-static');
    // @ts-ignore
    let bgMove = bgStatic.querySelector('.bg-move');
    // @ts-ignore
    (bgMove as HTMLElement).style.left = Math.floor(bgStatic.offsetWidth / 2  * percentage) + 'px';
  }

  constructor(
    private postsService: PostsService,
    private imagesService: ImagesService,
  ) {
  }

  ngOnInit(): void {
    this.getPosts();
    fromEvent(this.postsSearchInput?.nativeElement, 'input').pipe(
      map((event: any) => {
        return event.target.value;
      }),
      filter(res => res.length > 2 || res.length == 0),
      debounceTime(1500),
      distinctUntilChanged(),
    ).subscribe(searchText => {
      this.searchText = searchText;
      this.getPosts();
    });
  }

  private getPosts() {
    this.postsService.getPostsPaginated(this.searchText, this.currentPage, this.postsOnOnePage, this.sort, true)
      .subscribe(this.populatePostTable());
  }

  private populatePostTable() {
    return (response: MultiplePostsResponse) => {
      if (response.postDetails.length > 0) {
        let imageIds: number[] = [];
        response.postDetails.forEach(post => imageIds.push(post.coverImageId));
        this.imagesService.getMediumImagesByIds(imageIds).subscribe(images => {
          this.postDetails = [];
          response.postDetails.forEach(post => {
            let postDetailsWithImage = new PostDetailsWithImage(
              post.id,
              post.name,
              post.youtubeLink,
              post.date,
              post.content,
              post.active,
              // @ts-ignore
              images.map[post.coverImageId]
            );
            this.postDetails.push(postDetailsWithImage);
          });
        });
      } else {
        this.postDetails = [];
      }
      this.postCount = response.postCount;
      this.currentPage = response.page;
    };
  }

  pageChanged($event: PageEvent) {
    this.currentPage = $event.pageIndex;
    this.getPosts();
  }

  formatDate(date: string) {
    let pipe = new DatePipe('en-US');
    return pipe.transform(date, 'short');
  }
}
