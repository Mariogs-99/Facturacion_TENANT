import { Directive, HostListener, ElementRef, Renderer2 } from '@angular/core';
import * as moment from 'moment';

@Directive({
  selector: '[appDateInput]'
})
export class DateInputDirective {
  //!Establecidos como readonly
  private readonly regex: RegExp = new RegExp(/^\d{2}\/\d{2}\/\d{4}$/);

  constructor(private readonly el: ElementRef, private readonly renderer: Renderer2) { }

  @HostListener('input', ['$event.target.value'])
  onInput(value: string) {
    let formattedValue = value;
    if (value.length === 10 && this.regex.test(value)) {
      formattedValue = moment(value, 'DD/MM/YYYY').format('DD/MM/YYYY');
    }
    this.renderer.setProperty(this.el.nativeElement, 'value', formattedValue);
  }

  @HostListener('blur')
  onBlur() {
    const value = this.el.nativeElement.value;
    if (value && !this.regex.test(value)) {
      this.renderer.setProperty(this.el.nativeElement, 'value', '');
    }
  }
}
