import { Injectable } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Subject, Observable } from 'rxjs';
import { map, take } from 'rxjs/operators';
import { ConfirmDialogComponent } from '../../components/base/modal-basico/confirm-dialog/confirm-dialog.component';

@Injectable({
  providedIn: 'root',
})
export class DataService {
  private subject = new Subject<any>();

  private subjects = new Subject<void>();

  public subscription: any;
  public dialogRef!: MatDialogRef<ConfirmDialogComponent>;

  private pages: string = '&page=1&size=5';
  private filter: string = '';
  private sorts: string = '';
  private filterOptions: any = {};

  constructor(private dialog: MatDialog) {}

  sendMessage(message: any) {
    this.subject.next(message);
  }

  getData() {
    return this.subject.asObservable();
  }

  cancelRequest() {
    if (this.subscription != undefined) {
      this.subscription.unsubscribe();
    }
  }

  public open(options: any) {
    this.dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        title: options.title,
        message: options.message,
        cancelText: options.cancelText,
        confirmText: options.confirmText,
        data: options.data,
        type: options.type,
      },
    });
  }

  public close() {
    return this.dialogRef.close();
  }

  public confirmed(): Observable<any> {
    return this.dialogRef.afterClosed().pipe(
      take(1),
      map((res) => {
        return res;
      })
    );
  }

  sendClickEvent() {
    this.subjects.next();
  }

  getClickEvent(): Observable<any> {
    return this.subjects.asObservable();
  }

  headerTableProLocal(filter: any, filters: any, tableName: any): Promise<any> {
    return new Promise((resolve, reject) => {
      try {
        let filterBy = '';
        let sortBy = '';

        if (typeof filter.from === 'string' && filter.from === tableName) {
          if (filter.events === 'reset') {
            filterBy = '';
          }

          if (filter.events === 'filter') {
            filters[filter.input] = filter.value;
            Object.keys(filters).forEach((key: string) => {
              if (filters[key] !== '' && filters[key] !== undefined) {
                filterBy += `${key}:${filters[key]};`;
              }
            });
          }

          if (filter.events === 'sort' && filter.value !== 'none') {
            sortBy = `${filter.input}:${filter.value}`;
          }

          filterBy = filterBy.slice(0, -1);

          resolve({ filterBy, sortBy });
        } else {
          reject(new Error('Invalid filter.from or tableName'));
        }
      } catch (error) {
        console.warn('headerTablePro encountered an error:', error);
        reject(error instanceof Error ? error : new Error(String(error)));
      }
    });
  }
}
