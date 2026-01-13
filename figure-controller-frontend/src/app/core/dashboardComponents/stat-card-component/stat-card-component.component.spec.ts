import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StatCardComponentComponent } from './stat-card-component.component';

describe('StatCardComponentComponent', () => {
  let component: StatCardComponentComponent;
  let fixture: ComponentFixture<StatCardComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StatCardComponentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StatCardComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
