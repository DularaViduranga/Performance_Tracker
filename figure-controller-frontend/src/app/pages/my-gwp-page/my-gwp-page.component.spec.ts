import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyGWPPageComponent } from './my-gwp-page.component';

describe('MyGWPPageComponent', () => {
  let component: MyGWPPageComponent;
  let fixture: ComponentFixture<MyGWPPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyGWPPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MyGWPPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
