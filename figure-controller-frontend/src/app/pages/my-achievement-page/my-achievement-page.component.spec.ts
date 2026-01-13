import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyAchievementPageComponent } from './my-achievement-page.component';

describe('MyAchievementPageComponent', () => {
  let component: MyAchievementPageComponent;
  let fixture: ComponentFixture<MyAchievementPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyAchievementPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MyAchievementPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
