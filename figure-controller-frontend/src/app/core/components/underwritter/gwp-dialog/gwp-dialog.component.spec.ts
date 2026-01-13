import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GwpDialogComponent } from './gwp-dialog.component';

describe('GwpDialogComponent', () => {
  let component: GwpDialogComponent;
  let fixture: ComponentFixture<GwpDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GwpDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GwpDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
