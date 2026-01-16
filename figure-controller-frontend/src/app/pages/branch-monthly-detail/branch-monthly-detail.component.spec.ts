import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BranchMonthlyDetailComponent } from './branch-monthly-detail.component';

describe('BranchMonthlyDetailComponent', () => {
  let component: BranchMonthlyDetailComponent;
  let fixture: ComponentFixture<BranchMonthlyDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BranchMonthlyDetailComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BranchMonthlyDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
