import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTable } from '@angular/material/table';
import { ClientsService } from "../../../../shared/services/clients/clients.service";
import { fromEvent } from "rxjs";
import { debounceTime, distinctUntilChanged, filter, map } from "rxjs/operators";
import { ConfirmDialog } from "../../../../shared/dialogs/delete.dialog";
import { MatDialog } from "@angular/material/dialog";
import { AlertService } from "../../../../shared/services/alerts/alerts.service";
import { ClientDetails, MultipleClientResponse } from "../../../../shared/responses/multiple-client.response";
import { TableDataSource } from "../table-datasource/table-datasource";

@Component({
  selector: 'admin-clients-table',
  templateUrl: './clients-table.component.html',
  styleUrls: [ './clients-table.component.scss' ]
})
export class ClientsTableComponent implements AfterViewInit, OnInit {

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatTable) table!: MatTable<ClientDetails>;
  @ViewChild('searchInput', { static: true }) clientsSearchInput: ElementRef | undefined;

  dataSource: TableDataSource<ClientDetails> = new TableDataSource<ClientDetails>([]);
  isSearching = false;
  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = [ 'name', 'email', 'phone', 'buttons' ];

  constructor(
    private clientsService: ClientsService,
    private dialog: MatDialog,
    private alertService: AlertService
  ) {
  }

  ngOnInit() {
    this.clientsService.getClients().subscribe(value => this.updateClientsTable(value));

    fromEvent(this.clientsSearchInput?.nativeElement, 'input').pipe(
      map((event: any) => {
        return event.target.value;
      }),
      filter(res => res.length > 2 || res.length == 0),
      debounceTime(1500),
      distinctUntilChanged(),
    ).subscribe(searchText => {
      console.log('searching ' + searchText);
      this.isSearching = true;
      if (searchText.length == 0)
        this.clientsService.getClients().subscribe(value => {
          this.dataSource = new TableDataSource<ClientDetails>(value.clientDetails);
          this.updateClientsTable(value);
          this.isSearching = false;
        });
      else
        this.clientsService.getClientsByString(searchText).subscribe(value => {
          this.dataSource = new TableDataSource<ClientDetails>(value.clientDetails);
          this.updateClientsTable(value);
          this.isSearching = false;
        });
    });
  }

  updateClientsTable(value: MultipleClientResponse) {
    this.dataSource = new TableDataSource<ClientDetails>(value.clientDetails);
    this.ngAfterViewInit();
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    this.table.dataSource = this.dataSource;
  }

  delete(client: ClientDetails) {
    this.dialog.open(ConfirmDialog, {
      data: {
        name: 'Do you really want to delete this client?',
        description: `Name: ${client.name}<br> Email: ${client.email}<br>Phone: ${client.phone}`
      },
    }).afterClosed()
      .subscribe((value: boolean) => {
        if (value)
          this.clientsService.deleteClient(client.id).subscribe(value => {
            this.alertService.success('client deleted');
            this.ngOnInit();
          });
      });
  }
}
