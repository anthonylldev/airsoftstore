import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { faBan, faCartShopping } from '@fortawesome/free-solid-svg-icons';

import { IItem } from '../item.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-item-detail',
  templateUrl: './item-detail.component.html',
  styleUrls: ['./item-detail.component.scss']
})
export class ItemDetailComponent implements OnInit {
  item: IItem | null = null;
  quantity = 1;
  faBan = faBan;
  faCartShopping = faCartShopping;


  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ item }) => {
      this.item = item;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }

  navToBrand(): void {
    // TODO Nav to items filter by brand
  }

  addToCart(): void {
    // TODO Add to cart
  }

  decreaseQuantity(): void {
    if (this.quantity > 1 ) {
      --this.quantity;
    }
  }

  increaseQuantity(): void {
    if (this.item?.stock != null) {
      const aux = this.quantity + 1;
      if (this.item.stock >= aux) {
        ++this.quantity;
      } else {
        // TODO Alert no hay stock suficiente
      }
    }
  }

}
