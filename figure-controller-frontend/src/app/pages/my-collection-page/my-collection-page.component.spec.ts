import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyCollectionPageComponent } from './my-collection-page.component';

describe('MyCollectionPageComponent', () => {
  let component: MyCollectionPageComponent;
  let fixture: ComponentFixture<MyCollectionPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyCollectionPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MyCollectionPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
