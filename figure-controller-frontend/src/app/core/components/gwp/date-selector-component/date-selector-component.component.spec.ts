import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DateSelectorComponentComponent } from './date-selector-component.component';

describe('DateSelectorComponentComponent', () => {
  let component: DateSelectorComponentComponent;
  let fixture: ComponentFixture<DateSelectorComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DateSelectorComponentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DateSelectorComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
