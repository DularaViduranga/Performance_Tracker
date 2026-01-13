import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyUnderwritersPageComponent } from './my-underwriters-page.component';

describe('MyUnderwritersPageComponent', () => {
  let component: MyUnderwritersPageComponent;
  let fixture: ComponentFixture<MyUnderwritersPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyUnderwritersPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MyUnderwritersPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
