import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegionMonthlyDetailComponent } from './region-monthly-detail.component';

describe('RegionMonthlyDetailComponent', () => {
  let component: RegionMonthlyDetailComponent;
  let fixture: ComponentFixture<RegionMonthlyDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegionMonthlyDetailComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegionMonthlyDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
