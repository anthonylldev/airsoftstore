import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-card',
  templateUrl: './standard-card.component.html',
  styleUrls: ['./standard-card.component.scss']
})
export class CardComponent {
  @Input() item?: any

  constructor(
    private router: Router
  ) {}

  protected navToSubItems(): void {
    const querySubItemObject: any = {
      'filter[subCategoryId.in]': this.item.id
    }
    this.router.navigate(['/item'], {queryParams: querySubItemObject})
  }

}

