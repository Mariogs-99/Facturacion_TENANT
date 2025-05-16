import { Component, Input, OnInit } from '@angular/core';

import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
} from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';
import { NgxMaskDirective, provideNgxMask } from 'ngx-mask';
import { DataService } from '../../../core/services/data.service';


type Alignment =
  | 'text-center'
  | 'text-left'
  | 'text-right'
  | 'text-end'
  | 'text-start';

type InputType = 'text' | 'multi-select' | 'select' | 'date';

export interface DataInterface {
  value: any;
  name: string;
}

@Component({
  selector: 'app-table-header',
  templateUrl: './table-header.component.html',
  styleUrls: ['./table-header.component.css'],
  imports: [
    MatIconModule,
    CommonModule,
    MatFormFieldModule,
    MatSelectModule,
    FormsModule,
    MatDatepickerModule,
    ReactiveFormsModule,
    MatInputModule,
    MatButtonModule,
    MatDialogModule,
    MatPaginatorModule,
    MatTableModule,
    MatGridListModule,
    NgxMaskDirective
  ], providers: [provideNgxMask()],
  standalone: true,
})
export class TableHeaderComponent implements OnInit {
  @Input() title: string = 'sin nombre';
  @Input() column: string = 'sin nombre';
  @Input() tableName: string = '';
  @Input() rowHeight: string = '7:1';
  @Input() sort: boolean = false;
  @Input() search: boolean = false;
  @Input() alignment: Alignment = 'text-center';
  @Input() rows: number = this.search ? 2 : 1;
  @Input() type: InputType = 'text';
  @Input() data: any[] = [];
  @Input() mascara: string = '';

  public inputFilter: string = '';
  public sortDirection: string = 'none';
  public find: boolean = false;
  public filterForm: FormGroup;
  public date1!: moment.Moment;

  //!Marcados como readonly
  constructor(private readonly _data: DataService, private readonly formBuilder: FormBuilder) {
    this.filterForm = this.formBuilder.group({
      filterVal: new FormControl(''),
    });
  }


  ngOnInit(): void {
    if (this._data) {
      this._data.getData().subscribe((filter: any) => {
        try {
          if (filter.from == this.tableName && filter.events == 'sort') {
            if (filter.input != this.column) {
              this.sortDirection = 'none';
            }
          }
        } catch (error) {
          console.error('TableHeader fallo =>', error);
        }
      });
    }
  }

  sortData(sort: string) {
    if (this.sortDirection === 'none') {
      this.sortDirection = 'asc';
    } else if (this.sortDirection === 'asc') {
      this.sortDirection = 'desc';
    } else {
      this.sortDirection = 'none';
    }

    if (this.tableName != '') {
      this._data.sendMessage({
        from: this.tableName,
        value: this.sortDirection,
        input: sort,
        events: 'sort',
      });
    }
  }

  applyFilterByColumn(filter: string, enterkey = false) {
    if (
      (this.tableName != '' &&
        this.inputFilter != '' &&
        this.inputFilter != undefined) ||
      !enterkey
    ) {
      if (enterkey) {
        this.find = true;
      } else {
        this.find = false;
        this.filterForm.reset();
      }

      this._data.sendMessage({
        from: this.tableName,
        value: !this.find ? '' : this.inputFilter,
        input: filter,
        events: 'filter',
      });

      this.inputFilter = '';
    }
  }

  inputChange(event: any) {
    if (event.inputType && this.find) {
      this.find = false;
    }

    if (
      event.inputType === 'deleteContentBackward' &&
      this.inputFilter === ''
    ) {
      this.find = false;
      this.clearFilter(this.column);
    }
    this.inputFilter = (event.target as HTMLInputElement).value.trim();
    this.inputFilter = this.inputFilter.replace(/\s/g, '');

  }

  selectChange(filter: any, select: any) {
    this.find = true;

    console.log('Selection change : ', filter, select);
    this._data.sendMessage({
      from: this.tableName,
      value:
        this.type == 'date' ? select.value.format('DD/MM/YYYY') : select.value,
      input: filter,
      events: 'filter',
    });
  }

  clearFilter(filter: any, event?: Event) {
    event?.stopPropagation();
    this.find = false;
    this.filterForm.reset();
    this.filterForm.controls['filterVal'].setValue('');
    this.inputFilter = '';

    this._data.sendMessage({
      from: this.tableName,
      value: '',
      input: filter,
      events: 'filter',
    });
  }

  setFind(find: boolean) {
    this.find = find;
  }
}
