import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-card',
  templateUrl: './standard-card.component.html',
  styleUrls: ['./standard-card.component.scss'],
})
export class CardComponent {
  @Input() item?: any;
  @Input() url?: string;
  @Input() queryObject?: any;

  constructor(private router: Router) {}

  protected navToSubItems(): void {
    if (!this.url) {
      const querySubItemObject: any = {
        'filter[subCategoryId.in]': this.item.id,
      };

      this.router.navigate(['/item'], { queryParams: querySubItemObject });
    } else {
      if (!this.queryObject) {
        this.router.navigate([this.url]);
      } else {
        this.router.navigate([this.url], { queryParams: this.queryObject });
      }
    }
  }
}
