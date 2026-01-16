import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MonthlyBranchGwpPageComponent } from './monthly-branch-gwp-page.component';

describe('MonthlyBranchGwpPageComponent', () => {
  let component: MonthlyBranchGwpPageComponent;
  let fixture: ComponentFixture<MonthlyBranchGwpPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MonthlyBranchGwpPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MonthlyBranchGwpPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
