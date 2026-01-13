import { AfterViewInit, Component, ViewChild } from '@angular/core';
import { FormsModule, NgModel } from '@angular/forms';
import { Testing2Component } from '../testing2/testing2.component';

@Component({
  selector: 'app-testing1',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './testing1.component.html',
  styleUrls: ['./testing1.component.scss']
})
export class Testing1Component {
  // @ViewChild(Testing2Component) testing2Component !: Testing2Component;

  // message: string = "";


  // constructor() {
  //   console.log(this.testing2Component);
  // }

  // ngAfterViewInit(): void {
  //   console.log(this.testing2Component);
  //   this.message = this.testing2Component.childMessage;
  // }

  handleData(data: string) {
    console.log("Child message: " + data);

  }

}

