import {Component, Inject} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'image-dialog',
  styles: [`.dialog-image {
    max-width: 100%;
    max-height: 100%;
  }
  .zoomed {
    cursor: move;
    width: 100%;
  }

  .exit {
    position: fixed;
    top: 1rem;
    right: 1rem;
    border-radius: 50%;
    border: 3px solid #BADA55;
    width: 30px;
    height: 30px;
    -webkit-touch-callout: none;
    -webkit-user-select: none;
    -khtml-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
  }

  .exit:hover {
    cursor: default;
  }
  `],
  templateUrl: './image.dialog.html',
})
export class ImageDialog {
  private timer: number = 0;
  private preventSimpleClick: boolean = true;
  zoomOn: boolean = false;
  private top: string = '';
  private left: string = '';

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { source: string },
    public dialogRef: MatDialogRef<ImageDialog>
  ) {
  }

  singleClick(): void {
    if (!this.preventSimpleClick) {
      console.log('doubleClick')
      clearTimeout(this.timer);
      this.zoomOn = !this.zoomOn;
    } else {
      console.log('singleClick')
    }
    this.timer = 0;
    this.preventSimpleClick = false;
    let delay = 200;

    this.timer = setTimeout(() => {
      this.preventSimpleClick = true;
    }, delay);
  }

  closeDialog() {
    this.dialogRef.close()
  }
}
