import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyCancellationPageComponent } from './my-cancellation-page.component';

describe('MyCancellationPageComponent', () => {
  let component: MyCancellationPageComponent;
  let fixture: ComponentFixture<MyCancellationPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyCancellationPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MyCancellationPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
