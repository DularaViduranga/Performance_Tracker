import { ComponentFixture, TestBed } from '@angular/core/testing';

import { YearandmonthselectorComponentComponent } from './yearandmonthselector-component.component';

describe('YearandmonthselectorComponentComponent', () => {
  let component: YearandmonthselectorComponentComponent;
  let fixture: ComponentFixture<YearandmonthselectorComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [YearandmonthselectorComponentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(YearandmonthselectorComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
