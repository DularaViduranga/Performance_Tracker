import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeroStatsComponentComponent } from './hero-stats-component.component';

describe('HeroStatsComponentComponent', () => {
  let component: HeroStatsComponentComponent;
  let fixture: ComponentFixture<HeroStatsComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HeroStatsComponentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HeroStatsComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
