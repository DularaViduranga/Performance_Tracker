import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyGwpDetailedPageComponent } from './my-gwp-detailed-page.component';

describe('MyGwpDetailedPageComponent', () => {
  let component: MyGwpDetailedPageComponent;
  let fixture: ComponentFixture<MyGwpDetailedPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyGwpDetailedPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MyGwpDetailedPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
