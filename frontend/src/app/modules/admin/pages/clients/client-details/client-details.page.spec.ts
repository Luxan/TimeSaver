import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientDetailsPage } from './client-details.page';

describe('ClientDetailsComponent', () => {
  let component: ClientDetailsPage;
  let fixture: ComponentFixture<ClientDetailsPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClientDetailsPage ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientDetailsPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
