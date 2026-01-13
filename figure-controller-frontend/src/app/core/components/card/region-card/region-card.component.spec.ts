import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegionCardComponent } from './region-card.component';

describe('RegionCardComponent', () => {
  let component: RegionCardComponent;
  let fixture: ComponentFixture<RegionCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegionCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegionCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
