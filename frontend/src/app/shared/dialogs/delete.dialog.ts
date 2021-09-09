import { Component, Inject } from "@angular/core";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";

@Component({
  selector: 'delete-dialog',
  templateUrl: './delete.dialog.html',
})
export class ConfirmDialog {

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { name: string, description: string },
    public dialogRef: MatDialogRef<ConfirmDialog>
  ) {
  }
}
