import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MobileSidebarComponentComponent } from './mobile-sidebar-component.component';

describe('MobileSidebarComponentComponent', () => {
  let component: MobileSidebarComponentComponent;
  let fixture: ComponentFixture<MobileSidebarComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MobileSidebarComponentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MobileSidebarComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
