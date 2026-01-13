import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AchievementTableComponentComponent } from './achievement-table-component.component';

describe('AchievementTableComponentComponent', () => {
  let component: AchievementTableComponentComponent;
  let fixture: ComponentFixture<AchievementTableComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AchievementTableComponentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AchievementTableComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
