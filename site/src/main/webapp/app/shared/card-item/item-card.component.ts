import { Component, Input } from '@angular/core';
import { IItem } from '../../entities/item/item.model';

@Component({
  selector: 'jhi-item-card',
  templateUrl: './item-card.component.html',
  styleUrls: ['./item-card.component.scss']
})
export class ItemCardComponent {
  @Input() item?: IItem

}
