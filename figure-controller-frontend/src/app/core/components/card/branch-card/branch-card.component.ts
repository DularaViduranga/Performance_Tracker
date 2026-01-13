import { CommonModule } from '@angular/common';
import { Component, ElementRef, Input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { LucideAngularModule } from 'lucide-angular';

@Component({
  selector: 'app-branch-card',
  imports: [CommonModule, MatIconModule, LucideAngularModule],
  templateUrl: './branch-card.component.html',
  styleUrls: ['./branch-card.component.scss']
})
export class BranchCardComponent {
  @Input() branchCode: string = '';
  @Input() branchName: string = '';
  @Input() currentMonthGwp!: number ;
  @Input() accumulatedGwp!: number ;

  isTruncated: boolean = false;

  constructor(
    private router: Router,
    private el: ElementRef
  ) { }

  ngAfterViewInit(): void {
    // Check if text overflows after rendering
    setTimeout(() => {
      const h3 = this.el.nativeElement.querySelector('h3');
      if (h3) {
        // Create temporary span to measure text width
        const tempSpan = document.createElement('span');
        tempSpan.style.visibility = 'hidden';
        tempSpan.style.position = 'absolute';
        tempSpan.style.whiteSpace = 'nowrap';
        tempSpan.className = h3.className;
        tempSpan.textContent = this.branchName;
        document.body.appendChild(tempSpan);

        const textWidth = tempSpan.offsetWidth;
        const containerWidth = h3.offsetWidth;

        document.body.removeChild(tempSpan);

        this.isTruncated = textWidth > containerWidth;
      }
    }, 0);
  }

  onCardClick() : void {
    // Navigate to the sales officers page for the given branch code
    this.router.navigate(['/sales-officers-on-branch', this.branchCode, this.branchName]);
  }
}
