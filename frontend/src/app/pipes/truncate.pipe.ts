import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'truncate'
})
export class TruncatePipe implements PipeTransform {

  transform(description:string, limit:number = 90 , ellipse:string = ' ...'): unknown {
    return description.length>limit ? description.slice(0, limit)+ellipse : description;
  }

}


