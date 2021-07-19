import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoggerService } from "./components/logger/logger.service";
import { ConfirmDialog } from "./dialogs/delete.dialog";
import { MAT_DIALOG_DEFAULT_OPTIONS, MatDialogModule } from "@angular/material/dialog";
import { AlertComponent } from "./components/alert/alert.component";
import { AlertService } from "./services/alerts.service";

@NgModule({
  imports: [
    CommonModule,
    MatDialogModule,
  ],
  declarations: [
    ConfirmDialog,
    AlertComponent
  ],
  exports: [
    AlertComponent
  ],
  providers: [
    LoggerService,
    { provide: MAT_DIALOG_DEFAULT_OPTIONS, useValue: { hasBackdrop: false } },
    AlertService
  ],
  entryComponents: [
    ConfirmDialog
  ],
})
export class SharedModule {
}
