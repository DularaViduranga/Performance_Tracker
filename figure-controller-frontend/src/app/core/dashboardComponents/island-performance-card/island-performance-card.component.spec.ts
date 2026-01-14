import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IslandPerformanceCardComponent } from './island-performance-card.component';

describe('IslandPerformanceCardComponent', () => {
  let component: IslandPerformanceCardComponent;
  let fixture: ComponentFixture<IslandPerformanceCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IslandPerformanceCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IslandPerformanceCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
