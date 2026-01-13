import { Component, EventEmitter, Input, Output, output } from '@angular/core';

@Component({
  selector: 'app-testing2',
  standalone: true,
  imports: [],
  templateUrl: './testing2.component.html',
  styleUrl: './testing2.component.scss'
})
export class Testing2Component {
  childMessage: string = "Hello from Testing2 Component!";

  username: string = 'Nimal';

  @Output() dataEmitter = new EventEmitter<string>();

  sendData() {
    this.dataEmitter.emit(this.childMessage);
  }
}
