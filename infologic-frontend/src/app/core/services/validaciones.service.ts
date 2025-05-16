import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ValidacionesService {
  constructor() {}

  onKeyPressNoNumeros(event: KeyboardEvent) {
    if (!/^[a-zA-Z ]$/.test(event.key)) {
      event.preventDefault();
    }
  }

  onKeyPressNoSignos(event: KeyboardEvent) {
    if (!/^[a-zA-Z\d ]$/.test(event.key)) {
      event.preventDefault();
    }
  }

  onKeyPressNumbersAndHyphens(event: KeyboardEvent) {
    if (!/^[\d-]$/.test(event.key)) {
      event.preventDefault();
    }
  }

  onKeyPressLettersAndDash(event: KeyboardEvent) {
    if (!/^[a-zA-Z -]$/.test(event.key)) {
      event.preventDefault();
    }
  }

  onKeyPressNumbersAndDecimal(event: KeyboardEvent) {
    const currentValue = (event.target as HTMLInputElement).value;
    const decimalRegex = /^(?:\d+|\d*\.\d+)$/;

    if (event.key === '.') {
      if (currentValue.match(/\d/)) {
        if (decimalRegex.exec(currentValue + event.key)) {
          return;
        } else {
          return event.preventDefault();
        }
      }
    }

    if (!/^\d$/.test(event.key)) {
      event.preventDefault();
    }
  }

  onKeyPressNumbers(event: KeyboardEvent) {
    if (!/^\d$/.test(event.key)) {
      event.preventDefault();
    }
  }

  limitNumberTo999(event: Event) {
    const input = event.target as HTMLInputElement;
    if (+input.value > 999) {
      input.value = '999';
    }
  }

  limitNumberTo127(event: Event) {
    const input = event.target as HTMLInputElement;
    if (+input.value > 127) {
      input.value = '127';
    }
  }
}
