import { Component, NgZone, OnInit } from "@angular/core";
import { PostImage, PostsService } from "../../../../../shared/services/posts/posts.service";
import { ActivatedRoute, Router } from "@angular/router";
import { AlertService } from "../../../../../shared/services/alerts/alerts.service";
import { PostResponse } from "../../../../../shared/responses/post.response";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { ImagesService } from "../../../../../shared/services/images/images.service";
import { ImageMapResponse } from "../../../../../shared/responses/image-map.response";
import { NameResponse } from "../../../../../shared/responses/name.response";

@Component({
  selector: 'app-post-details',
  templateUrl: './post-details.page.html',
  styleUrls: [ './post-details.page.scss' ]
})
export class PostDetailsPage implements OnInit {

  private id = 0;
  create = false;
  edit = false;

  date = '';
  images: PostImage[] = [];

  postForm = new FormGroup({
    name: new FormControl('', [ Validators.required, Validators.minLength(4) ]),
    youtubeLink: new FormControl('', Validators.pattern('https://youtube.com/embed/[A-Za-z0-9]+')),
    content: new FormControl(''),
    active: new FormControl(false),
    images: new FormControl(''),
  });

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
    this.create = this.router.url.includes('new');
    this.edit = this.router.url.includes('edit');
    if (this.id) {
      this.postsService.getPostById(this.id).subscribe((response: PostResponse) => {
        this.postForm.controls["name"].setValue(response.name);
        this.postForm.controls["youtubeLink"].setValue(response.youtubeLink);
        this.date = response.date;
        this.postForm.controls["content"].setValue(response.content);
        this.postForm.controls["active"].setValue(response.active);
        this.imagesService.getSmallImagesByIds(response.imagesIds).subscribe((res: ImageMapResponse) => {
          for (let key of Object.keys(res.map)) {
            // @ts-ignore
            this.images.push({ id: Number(key), image: res.map[key], name: undefined });
          }
        });
      });
    }
  }

  get f() {
    return this.postForm.controls;
  }

  getNameErrorMessage() {
    if (this.f.name.hasError('required')) {
      return 'You must enter a value';
    }

    return this.f.name.hasError('name') ? 'Enter at least 4 characters' : '';
  }

  getYoutubeLinkErrorMessage() {
    return this.f.youtubeLink.hasError('youtubeLink') ? 'Enter a valid youtube link' : '';
  }

  selectFiles(event: any): void {
    let selectedFiles: FileList = event.target.files;

    if (selectedFiles && selectedFiles[0]) {
      const numberOfFiles = selectedFiles.length;
      for (let i = 0; i < numberOfFiles; i++) {
        const reader = new FileReader();
        reader.onload = (e: any) => {
          this.images.push({ id: undefined, image: e.target.result, name: selectedFiles[i].name });
        };
        reader.readAsDataURL(selectedFiles[i]);
      }
    }
  }

  deleteImage(image: { image: string; id: number | undefined; name: string | undefined }) {
    this.images.splice(this.images.indexOf(image), 1);
  }

  onSubmit() {
    if (this.create) {
      this.postsService.createPost(
        this.postForm.controls["name"].value,
        this.postForm.controls["youtubeLink"].value,
        this.postForm.controls["content"].value,
        this.postForm.controls["active"].value,
        this.images
      ).subscribe((response: NameResponse) => {
        this.alertService.success("Successfully created post: " + response.name);
        this.router.navigateByUrl('/admin/posts').then(r => {});
      });
    }

    if (this.edit) {
      this.postsService.updatePost(
        this.id,
        this.postForm.controls["name"].value,
        this.postForm.controls["youtubeLink"].value,
        this.postForm.controls["content"].value,
        this.postForm.controls["active"].value,
        this.images
      ).subscribe((response: NameResponse) => {
        this.alertService.success("Successfully updated post: " + response.name);
        this.router.navigateByUrl('/admin/posts').then(r => {});
      });
    }
  }
}
