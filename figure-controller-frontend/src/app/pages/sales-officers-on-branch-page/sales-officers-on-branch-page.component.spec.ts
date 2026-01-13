import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SalesOfficersOnBranchPageComponent } from './sales-officers-on-branch-page.component';

describe('SalesOfficersOnBranchPageComponent', () => {
  let component: SalesOfficersOnBranchPageComponent;
  let fixture: ComponentFixture<SalesOfficersOnBranchPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SalesOfficersOnBranchPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SalesOfficersOnBranchPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
