import { Component, EventEmitter, Input, Output } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';

interface TableAction {
  label: string;
  icon: string;
  btnBackground: string;
  callback: Function;
}

@Component({
  selector: 'app-table',
  standalone: true,
  imports: [RouterOutlet, CommonModule],
  templateUrl: './table.component.html',
})
export class TableComponent {
[x: string]: any;
  @Input() headings: string[] = [];
  @Input() data: any[] = [];
  @Input() actions: TableAction[] = [];
  @Output() actionClicked: EventEmitter<{ action: string, rowData: any }> = new EventEmitter();

  performAction(action: TableAction, rowData: any) {
    action.callback(rowData);
  }

  getObjectKeys(obj: any): string[] {
    return Object.keys(obj);
  }
}
