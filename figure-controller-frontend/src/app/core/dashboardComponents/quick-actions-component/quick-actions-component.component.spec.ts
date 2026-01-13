import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuickActionsComponentComponent } from './quick-actions-component.component';

describe('QuickActionsComponentComponent', () => {
  let component: QuickActionsComponentComponent;
  let fixture: ComponentFixture<QuickActionsComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QuickActionsComponentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(QuickActionsComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
