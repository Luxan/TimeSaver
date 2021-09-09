import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoggerService } from "./components/logger/logger.service";
import { ConfirmDialog } from "./dialogs/delete.dialog";
import { MAT_DIALOG_DEFAULT_OPTIONS, MatDialogModule } from "@angular/material/dialog";
import { AlertComponent } from "./components/alert/alert.component";
import { AlertService } from "./services/alerts/alerts.service";
import { ClientsService } from "./services/clients/clients.service";
import { ImagesService } from "./services/images/images.service";
import { FooterComponent } from "./components/footer/footer.component";
import {ImageViewer} from "./components/image-viewer/image.viewer";
import {ImageDialog} from "./components/image-viewer/dialog/image.dialog";
import {DragDropModule} from "@angular/cdk/drag-drop";

@NgModule({
  imports: [
    CommonModule,
    MatDialogModule,
    DragDropModule,
  ],
  declarations: [
    ConfirmDialog,
    AlertComponent,
    FooterComponent,
    ImageViewer,
    ImageDialog,
  ],
  exports: [
    AlertComponent,
    FooterComponent,
    ImageViewer,
  ],
  providers: [
    LoggerService,
    { provide: MAT_DIALOG_DEFAULT_OPTIONS, useValue: { hasBackdrop: false } },
    AlertService,
    ClientsService,
    ImagesService
  ],
  entryComponents: [
    ConfirmDialog,
    ImageDialog
  ],
})
export class SharedModule {
}
