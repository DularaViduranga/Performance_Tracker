import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BranchpageNevigatorCardComponent } from './branchpage-nevigator-card.component';

describe('BranchpageNevigatorCardComponent', () => {
  let component: BranchpageNevigatorCardComponent;
  let fixture: ComponentFixture<BranchpageNevigatorCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BranchpageNevigatorCardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BranchpageNevigatorCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
