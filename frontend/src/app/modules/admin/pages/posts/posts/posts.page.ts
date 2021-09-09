import {Component, ElementRef, OnInit, ViewChild} from "@angular/core";
import {PostsService, PostsServiceSort} from "../../../../../shared/services/posts/posts.service";
import {
  MultiplePostsResponse,
  PostDetails,
  PostDetailsWithImage
} from "../../../../../shared/responses/multiple-posts.response";
import {PageEvent} from "@angular/material/paginator";
import {Sort} from "@angular/material/sort";
import {MatTable} from "@angular/material/table";
import {MatDialog} from "@angular/material/dialog";
import {AlertService} from "../../../../../shared/services/alerts/alerts.service";
import {fromEvent} from "rxjs";
import {debounceTime, distinctUntilChanged, filter, map} from "rxjs/operators";
import {ConfirmDialog} from "../../../../../shared/dialogs/delete.dialog";
import {ImagesService} from "../../../../../shared/services/images/images.service";
import {NameResponse} from "../../../../../shared/responses/name.response";
import {MatCheckboxChange} from "@angular/material/checkbox";

@Component({
  selector: 'app-post',
  templateUrl: './posts.page.html',
  styleUrls: ['./posts.page.scss']
})
export class PostsPage implements OnInit {

  postsOnOnePage = 20;
  postDetails: PostDetailsWithImage[] = [];
  postCount = 0;
  currentPage = 0;

  @ViewChild(MatTable) table!: MatTable<PostDetailsWithImage>;
  @ViewChild('searchInput', {static: true}) postsSearchInput: ElementRef | undefined;

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['coverImageId', 'name', 'youtubeLink', 'date', 'active', 'buttons'];

  private sort: PostsServiceSort = {
    name: '',
    youtubeLink: '',
    date: 'asc',
    active: ''
  };
  private searchText = "";

  constructor(
    private postsService: PostsService,
    private imagesService: ImagesService,
    private dialog: MatDialog,
    private alertService: AlertService
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
    this.postsService.getPostsPaginated(this.searchText, this.currentPage, this.postsOnOnePage, this.sort)
      .subscribe(this.populatePostTable());
  }

  private populatePostTable() {
    return (response: MultiplePostsResponse) => {
      if (response.postDetails.length > 0) {
        let imageIds: number[] = [];
        response.postDetails.forEach(post => imageIds.push(post.coverImageId));
        this.imagesService.getSmallImagesByIds(imageIds).subscribe(images => {
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
          this.updatePostsTable();
        });
      } else {
        this.postDetails = [];
        this.updatePostsTable();
      }
      this.postCount = response.postCount;
      this.currentPage = response.page;
    };
  }

  private updatePostsTable() {
    this.table.dataSource = this.postDetails;
  }

  delete(post: PostDetails) {
    this.dialog.open(ConfirmDialog, {
      data: {
        name: 'Do you really want to delete this post?',
        description: `Name: ${post.name}`
      },
    }).afterClosed()
      .subscribe((value: boolean) => {
        if (value)
          this.postsService.deletePost(post.id).subscribe(value => {
            this.alertService.success('Post deleted: ' + value.name);
            this.ngOnInit();
          });
      });
  }

  changeActivity($event: MatCheckboxChange, post: PostDetailsWithImage) {
    this.dialog.open(ConfirmDialog, {
      data: {
        name: 'Do you really want to publish this post?',
        description: `Name: ${post.name}`
      },
    }).afterClosed()
      .subscribe((value: boolean) => {
        if (value) {
          this.postsService.changePostActivity(
            post.id,
            $event.checked
          ).subscribe((response: NameResponse) => {
            this.alertService.success("Successfully changed activity of post: " + response.name);
            post.active = $event.checked;
            console.log(post.active);
          });
        } else {
          $event.source.checked = !$event.checked;
        }
      });
  }

  sortData($event: Sort) {
    this.sort = {
      name: $event.active == 'name' ? $event.direction : this.sort.name,
      youtubeLink: $event.active == 'youtubeLink' ? $event.direction : this.sort.youtubeLink,
      active: $event.active == 'active' ? $event.direction : this.sort.active,
      date: $event.active == 'date' ? $event.direction : this.sort.date
    }
  }

  pageChanged($event: PageEvent) {
    this.currentPage = $event.pageIndex;
    this.getPosts();
  }

}
