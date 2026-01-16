import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MonthlyRegionGwpPageComponent } from './monthly-region-gwp-page.component';

describe('MonthlyRegionGwpPageComponent', () => {
  let component: MonthlyRegionGwpPageComponent;
  let fixture: ComponentFixture<MonthlyRegionGwpPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MonthlyRegionGwpPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MonthlyRegionGwpPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
