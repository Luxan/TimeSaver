import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientsPage } from './clients.page';

describe('ClientsComponent', () => {
  let component: ClientsPage;
  let fixture: ComponentFixture<ClientsPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClientsPage ]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientsPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
