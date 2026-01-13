import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MetricViewComponentComponent } from './metric-view-component.component';

describe('MetricViewComponentComponent', () => {
  let component: MetricViewComponentComponent;
  let fixture: ComponentFixture<MetricViewComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MetricViewComponentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MetricViewComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
