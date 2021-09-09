import {Component, Input, OnInit} from "@angular/core";
import {MatDialog} from "@angular/material/dialog";
import {ImageDialog} from "./dialog/image.dialog";


@Component({
  selector: 'image-viewer',
  templateUrl: './image.viewer.html',
  styleUrls: ['./image.viewer.scss']
})
export class ImageViewer implements OnInit {

  @Input('src') source: string = '';

  constructor(private dialog: MatDialog,) {
  }

  ngOnInit(): void {
  }

  click() {
    let matDialogRef = this.dialog.open(ImageDialog, {
      data: {
        source: this.source
      },
      width: '100vw',
      height: '100vh'
    });
    matDialogRef.afterOpened().subscribe(() => {
      this.changeNavbarVisibility('hidden');
    })
    matDialogRef.afterClosed().subscribe(() => {
      this.changeNavbarVisibility('visible');
    })
  }

  changeNavbarVisibility(value: string) {
    let navbar = document.getElementsByClassName('navbar');
    if (navbar.length == 1) {
      // @ts-ignore
      navbar.item(0).style.visibility = value;
    }
  }
}
