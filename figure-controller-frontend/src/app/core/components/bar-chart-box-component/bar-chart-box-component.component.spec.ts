import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BarChartBoxComponentComponent } from './bar-chart-box-component.component';

describe('BarChartBoxComponentComponent', () => {
  let component: BarChartBoxComponentComponent;
  let fixture: ComponentFixture<BarChartBoxComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BarChartBoxComponentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BarChartBoxComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
